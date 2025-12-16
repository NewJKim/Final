package com.writingassistant.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * GenerationRequest - Demonstrates Encapsulation and Polymorphism
 * Immutable domain model representing a text generation request.
 */
public class GenerationRequest {
    private final String input;
    private final String strategyName;
    private final LocalDateTime timestamp;

    public GenerationRequest(String input, String strategyName) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input cannot be null or empty");
        }
        if (strategyName == null || strategyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Strategy name cannot be null or empty");
        }

        this.input = input.trim();
        this.strategyName = strategyName.trim();
        this.timestamp = LocalDateTime.now();
    }

    public String getInput() {
        return input;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Polymorphism - Override equals for proper object comparison
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenerationRequest that = (GenerationRequest) o;
        return Objects.equals(input, that.input) &&
                Objects.equals(strategyName, that.strategyName);
    }

    /**
     * Polymorphism - Override hashCode (required when overriding equals)
     */
    @Override
    public int hashCode() {
        return Objects.hash(input, strategyName);
    }

    /**
     * Polymorphism - Override toString for better debugging
     */
    @Override
    public String toString() {
        return "GenerationRequest{" +
                "strategy='" + strategyName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}