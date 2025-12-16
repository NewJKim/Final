package com.writingassistant.model.strategy;

import com.writingassistant.service.APIService;

/**
 * AcademicWritingStrategy - Strategy Pattern Implementation
 * Generates scholarly, research-focused content.
 * Uses formal academic language and objective tone.
 */
public class AcademicWritingStrategy extends AbstractWritingStrategy {

    public AcademicWritingStrategy(APIService apiService) {
        super(apiService);
    }

    @Override
    public String getSystemPrompt() {
        return "You are an academic writing assistant. Transform the user's text into " +
                "scholarly, well-researched content. Use formal academic language, " +
                "proper citations format, and objective tone suitable for academic papers.";
    }

    @Override
    public String getStrategyName() {
        return "Academic";
    }
}
