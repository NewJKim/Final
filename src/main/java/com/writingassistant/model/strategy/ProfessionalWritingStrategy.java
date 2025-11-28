package com.writingassistant.model.strategy;

import com.writingassistant.service.APIService;

/**
 * ProfessionalWritingStrategy - Strategy Pattern Implementation
 * Generates formal, business-appropriate content.
 * Uses professional tone and workplace-suitable language.
 */
public class ProfessionalWritingStrategy implements WritingStrategy {
    private final APIService apiService;

    /**
     * Constructor - receives APIService for making API calls.
     */
    public ProfessionalWritingStrategy(APIService apiService) {
        this.apiService = apiService;
    }

    /**
     * Generate professional text by combining system prompt with user input.
     * Calls APIService to communicate with Cohere AI.
     */
    @Override
    public String generateText(String userInput) {
        String prompt = getSystemPrompt() + "\n\nUser input: " + userInput;
        return apiService.generateText(prompt, 500);
    }

    /**
     * System prompt that defines professional writing style.
     * This tells the AI to be formal and business-appropriate.
     */
    @Override
    public String getSystemPrompt() {
        return "You are a professional writing assistant. Transform the user's text into " +
                "clear, formal, and business-appropriate content. Use professional tone, " +
                "proper structure, and concise language suitable for workplace communication.";
    }

    /**
     * Returns name for UI display.
     */
    @Override
    public String getStrategyName() {
        return "Professional";
    }
}