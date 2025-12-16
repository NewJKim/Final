package com.writingassistant.model.strategy;

import com.writingassistant.service.APIService;

/**
 * AbstractWritingStrategy - Demonstrates Inheritance and Abstraction
 * Base class for all writing strategies.
 * Provides common implementation while allowing subclasses to customize behavior.
 */
public abstract class AbstractWritingStrategy implements WritingStrategy {
    protected final APIService apiService;

    /**
     * Constructor - all strategies need APIService.
     * Protected so only subclasses can call it.
     */
    protected AbstractWritingStrategy(APIService apiService) {
        this.apiService = apiService;
    }

    /**
     * Template method - defines the algorithm for text generation.
     * Final so subclasses can't change the overall flow.
     */
    @Override
    public final String generateText(String userInput) {
        // Validation
        if (userInput == null || userInput.trim().isEmpty()) {
            return "Error: Please enter some text to transform.";
        }

        // Build the complete prompt
        String prompt = getSystemPrompt() + "\n\nUser input: " + userInput;

        // Call API
        return apiService.generateText(prompt, 500);
    }

    /**
     * Abstract method - each strategy must define its own system prompt.
     * This is what makes each strategy unique.
     */
    @Override
    public abstract String getSystemPrompt();

    /**
     * Abstract method - each strategy must provide its name.
     */
    @Override
    public abstract String getStrategyName();
}