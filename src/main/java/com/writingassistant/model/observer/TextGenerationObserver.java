package com.writingassistant.model.observer;

/**
 * TextGenerationObserver - Observer Pattern Interface
 * Allows objects to be notified when text generation events occur.
 * The MainController implements this to update the UI.
 */
public interface TextGenerationObserver {

    /**
     * Called when text generation completes successfully.
     *
     * @param generatedText The text produced by the AI
     */
    void onTextGenerated(String generatedText);

    /**
     * Called when text generation fails.
     *
     * @param errorMessage Description of what went wrong
     */
    void onGenerationError(String errorMessage);

    /**
     * Called when text generation starts.
     * Used to disable button and show "Generating..." message.
     */
    void onGenerationStarted();
}