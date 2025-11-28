package com.writingassistant.model;

import java.io.InputStream;
import java.util.Properties;

/**
 * APIClient - Singleton Pattern
 * Manages API configuration for Cohere AI.
 * Ensures only ONE instance exists in the entire application.
 */
public class APIClient {
    // Singleton instance
    private static APIClient instance;

    // Configuration fields
    private String apiKey;
    private String apiEndpoint;
    private int maxTokens;
    private double temperature;

    /**
     * Private constructor - prevents direct instantiation.
     * This is the key to Singleton pattern.
     */
    private APIClient() {
        loadConfiguration();
    }

    /**
     * Get the single instance (Singleton pattern).
     * Thread-safe with synchronized keyword.
     */
    public static synchronized APIClient getInstance() {
        if (instance == null) {
            instance = new APIClient();
        }
        return instance;
    }

    /**
     * Load configuration from config.properties file.
     * Falls back to COHERE_API_KEY environment variable if file not found.
     */
    private void loadConfiguration() {
        Properties props = new Properties();

        // Try to load from src/main/resources/config.properties
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input != null) {
                props.load(input);
            } else {
                System.err.println("⚠️ Warning: config.properties not found in resources. Falling back to environment variables.");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error loading config.properties: " + e.getMessage());
        }

        // Load values from properties file
        apiKey = props.getProperty("api.key");
        apiEndpoint = props.getProperty("api.endpoint", "https://api.cohere.ai/v1/chat");

        // Parse max tokens with default
        try {
            maxTokens = Integer.parseInt(props.getProperty("max.tokens", "500"));
        } catch (NumberFormatException e) {
            maxTokens = 500;
        }

        // Parse temperature with default
        try {
            temperature = Double.parseDouble(props.getProperty("temperature", "0.7"));
        } catch (NumberFormatException e) {
            temperature = 0.7;
        }

        // Fallback to environment variable if no key in file
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = System.getenv("COHERE_API_KEY");
        }
    }

    // Getters

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isEmpty();
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public double getTemperature() {
        return temperature;
    }
}
