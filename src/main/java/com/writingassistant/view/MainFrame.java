package com.writingassistant.view;

import com.writingassistant.model.observer.TextGenerationObserver;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * MainFrame - View in MVC Architecture
 * The graphical user interface for the Writing Assistant.
 * Displays input/output areas, strategy selection, and control buttons.
 */
public class MainFrame extends JFrame {

    // GUI Components
    private JTextArea inputArea;           // Where user types text
    private JTextArea outputArea;          // Where AI result appears
    private JButton generateButton;        // Triggers AI generation
    private JButton saveButton;            // Saves session to file
    private JButton loadButton;            // Loads previous session
    private JRadioButton creativeRadio;    // Creative mode selector
    private JRadioButton professionalRadio; // Professional mode selector
    private JRadioButton academicRadio;    // Academic mode selector
    private JLabel statusLabel;            // Shows current status
    private ButtonGroup strategyGroup;     // Groups radio buttons
    private List<TextGenerationObserver> observers = new ArrayList<>();

    /**
     * Constructor - initializes and displays the window.
     */
    public MainFrame() {
        setTitle("AI Writing Assistant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);    // Center on screen

        initComponents();
        layoutComponents();
    }

    /**
     * Initialize all GUI components.
     */
    private void initComponents() {
        // Text areas
        inputArea = new JTextArea(8, 40);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Output text area (read-only)
        outputArea = new JTextArea(8, 40);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        outputArea.setBackground(new Color(245, 245, 245));

        // Buttons
        generateButton = new JButton("Generate");
        generateButton.setPreferredSize(new Dimension(120, 35));

        saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(100, 35));

        loadButton = new JButton("Load");
        loadButton.setPreferredSize(new Dimension(100, 35));

        // Radio buttons for strategy selection
        creativeRadio = new JRadioButton("Creative");
        professionalRadio = new JRadioButton("Professional", true);
        academicRadio = new JRadioButton("Academic");

        // Group radio buttons (only one can be selected)
        strategyGroup = new ButtonGroup();
        strategyGroup.add(creativeRadio);
        strategyGroup.add(professionalRadio);
        strategyGroup.add(academicRadio);

        // Status label
        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    /**
     * Layout all components using BorderLayout.
     */
    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        // Top panel - Input
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        JLabel inputLabel = new JLabel("Enter your text:");
        inputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(inputLabel, BorderLayout.NORTH);
        topPanel.add(new JScrollPane(inputArea), BorderLayout.CENTER);

        // Middle panel - Strategy selection and Generate button
        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        middlePanel.setBorder(BorderFactory.createTitledBorder("Writing Mode"));
        middlePanel.add(creativeRadio);
        middlePanel.add(professionalRadio);
        middlePanel.add(academicRadio);
        middlePanel.add(Box.createHorizontalStrut(20));
        middlePanel.add(generateButton);

        // Bottom panel - Output
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        JLabel outputLabel = new JLabel("Generated text:");
        outputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(outputLabel, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(outputArea), BorderLayout.CENTER);

        // Control panel - Save/Load buttons
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        controlPanel.add(saveButton);
        controlPanel.add(loadButton);
        bottomPanel.add(controlPanel, BorderLayout.SOUTH);

        // Status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEtchedBorder());
        statusPanel.add(statusLabel, BorderLayout.WEST);

        // Wrapper for output and status
        JPanel southWrapper = new JPanel(new BorderLayout());
        southWrapper.add(bottomPanel, BorderLayout.CENTER);
        southWrapper.add(statusPanel, BorderLayout.SOUTH);

        // Add all panels to frame
        add(topPanel, BorderLayout.NORTH);
        add(middlePanel, BorderLayout.CENTER);
        add(southWrapper, BorderLayout.SOUTH);
    }

    // ============ Getters and Setters ============

    public String getInputText() {
        return inputArea.getText();
    }

    public String getOutputText() {
        return outputArea.getText();
    }

    public void setInputText(String text) {
        inputArea.setText(text);
    }

    public void setOutputText(String text) {
        outputArea.setText(text);
    }

    public void setStatusText(String text) {
        statusLabel.setText(text);
    }

    /**
     * Scroll output area to show the beginning of text.
     */
    public void scrollOutputToTop() {
        outputArea.setCaretPosition(0);
    }

    /**
     * Enable or disable the Generate button.
     * Disabled during API calls to prevent multiple requests.
     */
    public void setGenerateButtonEnabled(boolean enabled) {
        generateButton.setEnabled(enabled);
    }

    // ============ Event Listeners ============

    public void setGenerateButtonListener(ActionListener listener) {
        generateButton.addActionListener(listener);
    }

    public void setSaveButtonListener(ActionListener listener) {
        saveButton.addActionListener(listener);
    }

    public void setLoadButtonListener(ActionListener listener) {
        loadButton.addActionListener(listener);
    }

    public void setStrategyChangeListener(Consumer<String> listener) {
        creativeRadio.addActionListener(e -> listener.accept("Creative"));
        professionalRadio.addActionListener(e -> listener.accept("Professional"));
        academicRadio.addActionListener(e -> listener.accept("Academic"));
    }

    // ============ Observer Pattern Support ============

    public void addObserver(TextGenerationObserver observer) {
        observers.add(observer);
    }

    // ============ Utility Methods ============

    /**
     * Display an error message dialog.
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show file chooser dialog for loading sessions.
     *
     * @return Selected file, or null if cancelled
     */
    public File showFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
            }
            public String getDescription() {
                return "Text Files (*.txt)";
            }
        });

        int result = chooser.showOpenDialog(this);
        return (result == JFileChooser.APPROVE_OPTION) ? chooser.getSelectedFile() : null;
    }
}