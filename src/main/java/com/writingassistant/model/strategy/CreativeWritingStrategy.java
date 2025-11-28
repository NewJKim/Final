package com.writingassistant.model.strategy;

import com.writingassistant.service.APIService;

/**
 * CreativeWritingStrategy - Strategy Pattern Implementation
 * Generates imaginative, expressive, and creative content.
 * Uses vivid language, metaphors, and storytelling techniques.
 */
public class CreativeWritingStrategy implements WritingStrategy {
    private final APIService apiService;

    /**
     * Constructor - receives APIService for making API calls.
     */
    public CreativeWritingStrategy(APIService apiService) {
        this.apiService = apiService;
    }

    /**
     * Generate creative text by combining system prompt with user input.
     * Calls APIService to communicate with Cohere AI.
     */
    @Override
    public String generateText(String userInput) {
        String prompt = getSystemPrompt() + "\n\nUser input: " + userInput;
        return apiService.generateText(prompt, 500);
    }

    /**
     * System prompt that defines creative writing style.
     * This tells the AI to be imaginative and expressive.
     */
    @Override
    public String getSystemPrompt() {
        return "You are a creative writing assistant. Transform the user's text into " +
                "vivid, imaginative, and expressive content. Use metaphors, descriptive " +
                "language, and engaging storytelling techniques. Make it captivating and unique.";
    }

    /**
     * Returns name for UI display.
     */
    @Override
    public String getStrategyName() {
        return "Creative";
    }
}