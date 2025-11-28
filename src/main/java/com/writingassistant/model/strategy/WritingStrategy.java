package com.writingassistant.model.strategy;

/**
 * WritingStrategy - Strategy Pattern Interface
 * Defines the contract for all writing modes.
 * Each strategy (Creative, Professional, Academic) implements this interface.
 */
public interface WritingStrategy {

    /**
     * Generate text based on this strategy's style.
     *
     * @param userInput The original text from the user
     * @return AI-enhanced text in this strategy's style
     */
    String generateText(String userInput);

    /**
     * Get the system prompt for this strategy.
     * This tells the AI how to behave.
     *
     * @return System instructions for the AI
     */
    String getSystemPrompt();

    /**
     * Get the name of this strategy for display in UI.
     *
     * @return Strategy name (e.g., "Creative", "Professional", "Academic")
     */
    String getStrategyName();
}