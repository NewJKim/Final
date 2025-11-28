package com.writingassistant.model.strategy;

import com.writingassistant.service.APIService;

/**
 * AcademicWritingStrategy - Strategy Pattern Implementation
 * Generates scholarly, research-focused content.
 * Uses formal academic language and objective tone.
 */
public class AcademicWritingStrategy implements WritingStrategy {
    private final APIService apiService;

    /**
     * Constructor - receives APIService for making API calls.
     */
    public AcademicWritingStrategy(APIService apiService) {
        this.apiService = apiService;
    }

    /**
     * Generate academic text by combining system prompt with user input.
     * Calls APIService to communicate with Cohere AI.
     */
    @Override
    public String generateText(String userInput) {
        String prompt = getSystemPrompt() + "\n\nUser input: " + userInput;
        return apiService.generateText(prompt, 500);
    }

    /**
     * System prompt that defines academic writing style.
     * This tells the AI to be scholarly and formal.
     */
    @Override
    public String getSystemPrompt() {
        return "You are an academic writing assistant. Transform the user's text into " +
                "scholarly, well-structured content. Use formal academic language, proper " +
                "citations format, and maintain objective tone suitable for research papers.";
    }

    /**
     * Returns name for UI display.
     */
    @Override
    public String getStrategyName() {
        return "Academic";
    }
}