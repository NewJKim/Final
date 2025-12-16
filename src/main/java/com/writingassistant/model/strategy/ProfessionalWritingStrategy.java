package com.writingassistant.model.strategy;

import com.writingassistant.service.APIService;

/**
 * ProfessionalWritingStrategy - Strategy Pattern Implementation
 * Generates formal, business-appropriate content.
 * Uses professional tone and workplace-suitable language.
 */
public class ProfessionalWritingStrategy extends AbstractWritingStrategy {

    public ProfessionalWritingStrategy(APIService apiService) {
        super(apiService);
    }

    @Override
    public String getSystemPrompt() {
        return "You are a professional writing assistant. Transform the user's text into " +
                "clear, formal, and business-appropriate content. Use professional tone, " +
                "proper structure, and concise language suitable for workplace communications.";
    }

    @Override
    public String getStrategyName() {
        return "Professional";
    }
}
