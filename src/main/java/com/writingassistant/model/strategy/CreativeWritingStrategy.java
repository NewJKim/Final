package com.writingassistant.model.strategy;

import com.writingassistant.service.APIService;

/**
 * CreativeWritingStrategy - Demonstrates Inheritance
 * Extends AbstractWritingStrategy and customizes system prompt.
 */
public class CreativeWritingStrategy extends AbstractWritingStrategy {

    public CreativeWritingStrategy(APIService apiService) {
        super(apiService);
    }

    @Override
    public String getSystemPrompt() {
        return "You are a creative writing assistant. Transform the user's text into " +
                "vivid, imaginative, and expressive content. Use metaphors, descriptive " +
                "language, and engaging storytelling techniques. Make it captivating and unique.";
    }

    @Override
    public String getStrategyName() {
        return "Creative";
    }
}