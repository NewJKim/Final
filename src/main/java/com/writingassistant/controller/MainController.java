package com.writingassistant.controller;

import com.writingassistant.model.observer.TextGenerationObserver;
import com.writingassistant.model.strategy.*;
import com.writingassistant.service.APIService;
import com.writingassistant.view.MainFrame;
import javax.swing.SwingWorker;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * MainController - Controller in MVC Architecture
 * Coordinates between Model (strategies, API) and View (GUI).
 * Handles user actions and updates the UI.
 * Implements Observer pattern to respond to generation events.
 */
public class MainController implements TextGenerationObserver {
    private final MainFrame view;
    private final APIService apiService;
    private WritingStrategy currentStrategy;

    /**
     * Constructor - sets up the controller.
     * Initializes API service and default strategy.
     * Registers event listeners for all buttons.
     */
    public MainController(MainFrame view) {
        this.view = view;
        this.apiService = new APIService();

        // Set Professional as default strategy
        this.currentStrategy = new ProfessionalWritingStrategy(apiService);

        // Register this controller as observer
        view.addObserver(this);

        // Set up button listeners
        setupListeners();
    }

    /**
     * Set up listeners for all user interactions.
     */
    private void setupListeners() {
        view.setGenerateButtonListener(e -> handleGenerate());
        view.setSaveButtonListener(e -> handleSave());
        view.setLoadButtonListener(e -> handleLoad());
        view.setStrategyChangeListener(strategy -> handleStrategyChange(strategy));
    }

    /**
     * Handle Generate button click.
     * Runs API call on background thread using SwingWorker to avoid freezing GUI.
     */
    private void handleGenerate() {
        String userInput = view.getInputText();

        // Validate input
        if (userInput.trim().isEmpty()) {
            view.showError("Please enter some text first!");
            return;
        }

        // Notify that generation is starting
        onGenerationStarted();

        // Use SwingWorker to run API call on background thread
        new SwingWorker<String, Void>() {

            /**
             * This runs on background thread - doesn't freeze GUI.
             */
            @Override
            protected String doInBackground() {
                // Call current strategy to generate text
                return currentStrategy.generateText(userInput);
            }

            /**
             * This runs on GUI thread after doInBackground() finishes.
             */
            @Override
            protected void done() {
                try {
                    String result = get();  // Get result from background thread

                    if (result.startsWith("Error:")) {
                        // Generation failed
                        onGenerationError(result);
                    } else {
                        // Generation succeeded
                        onTextGenerated(result);
                        view.scrollOutputToTop();   // Show beginning of text
                    }
                } catch (Exception e) {
                    onGenerationError("Unexpected error: " + e.getMessage());
                }
            }
        }.execute();
    }

    /**
     * Handle strategy change (Creative/Professional/Academic).
     * Uses Strategy pattern to switch between different writing styles.
     */
    private void handleStrategyChange(String strategyName) {
        switch (strategyName) {
            case "Creative":
                currentStrategy = new CreativeWritingStrategy(apiService);
                break;
            case "Professional":
                currentStrategy = new ProfessionalWritingStrategy(apiService);
                break;
            case "Academic":
                currentStrategy = new AcademicWritingStrategy(apiService);
                break;
        }
        view.setStatusText("Strategy changed to: " + strategyName);
    }

    /**
     * Save current session to file.
     */
    private void handleSave() {
        String input = view.getInputText();
        String output = view.getOutputText();

        // Check if there's anything to save
        if (input.isEmpty() && output.isEmpty()) {
            view.showError("Nothing to save!");
            return;
        }

        // Create filename with timestamp
        String filename = "session_" + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";

        // Write to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=== Writing Assistant Session ===");
            writer.println("Strategy: " + currentStrategy.getStrategyName());
            writer.println("Timestamp: " + LocalDateTime.now());
            writer.println("\n--- Input ---");
            writer.println(input);
            writer.println("\n--- Output ---");
            writer.println(output);

            view.setStatusText("Saved to: " + filename);
        } catch (IOException e) {
            view.showError("Failed to save: " + e.getMessage());
        }
    }

    /**
     * Load a previous session from file.
     */
    private void handleLoad() {
        File file = view.showFileChooser();
        if (file == null) return;   // User cancelled

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            boolean inInput = false;
            boolean inOutput = false;
            StringBuilder inputText = new StringBuilder();
            StringBuilder outputText = new StringBuilder();

            // Parse the file to extract input and output sections
            while ((line = reader.readLine()) != null) {
                if (line.equals("--- Input ---")) {
                    inInput = true;
                    inOutput = false;
                } else if (line.equals("--- Output ---")) {
                    inInput = false;
                    inOutput = true;
                } else if (inInput) {
                    inputText.append(line).append("\n");
                } else if (inOutput) {
                    outputText.append(line).append("\n");
                }
            }

            // Set the text in the GUI
            view.setInputText(inputText.toString().trim());
            view.setOutputText(outputText.toString().trim());
            view.setStatusText("Loaded: " + file.getName());

        } catch (IOException e) {
            view.showError("Failed to load: " + e.getMessage());
        }
    }

    // ============ Observer Pattern Implementation ============

    /**
     * Called when text generation completes successfully.
     * Updates UI with the generated text.
     */
    @Override
    public void onTextGenerated(String generatedText) {
        view.setOutputText(generatedText);
        view.scrollOutputToTop();
        view.setGenerateButtonEnabled(true);
        view.setStatusText("Generation complete!");
    }

    /**
     * Called when text generation fails.
     * Shows error message to user.
     */
    @Override
    public void onGenerationError(String errorMessage) {
        view.showError(errorMessage);
        view.setGenerateButtonEnabled(true);
        view.setStatusText("Generation failed.");
    }

    /**
     * Called when text generation starts.
     * Disables button and shows status message.
     */
    @Override
    public void onGenerationStarted() {
        view.setGenerateButtonEnabled(false);
        view.setStatusText("Generating text...");
        view.setOutputText("Generating…");
    }
}