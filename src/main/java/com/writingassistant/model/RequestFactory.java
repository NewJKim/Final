package com.writingassistant.model;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * RequestFactory - Factory Pattern
 * Creates API request JSON objects for Cohere AI.
 * Hides the complexity of building request bodies.
 */
public class RequestFactory {

    /**
     * Create a Cohere Chat API request.
     *
     * @param prompt The full prompt (including system instructions and user input)
     * @param maxTokens Maximum length of response
     * @return JSON object ready to send to Cohere API
     */
    public static JSONObject createCohereRequest(String prompt, int maxTokens) {
        JSONObject req = new JSONObject();

        // Cohere's latest chat model
        req.put("model", "command-a-03-2025");

        // User's message/prompt
        req.put("message", prompt);

        // System instructions (tells AI how to behave)
        req.put("preamble", "You are a professional writing assistant. Transform the user's text into clear, formal, business-appropriate content.");

        // Response length limit
        req.put("max_tokens", maxTokens);

        // Creativity level (0.0 = deterministic, 1.0 = very creative)
        req.put("temperature", 0.7);

        return req;
    }


    /**
     * Create a chat completion request for OpenAI API (legacy support).
     */
    public static JSONObject createChatRequest(String systemPrompt, String userMessage, int maxTokens) {
        JSONObject request = new JSONObject();
        request.put("model", "gpt-3.5-turbo");
        request.put("max_tokens", maxTokens);
        request.put("temperature", 0.7);

        JSONArray messages = new JSONArray();

        // System message
        JSONObject systemMsg = new JSONObject();
        systemMsg.put("role", "system");
        systemMsg.put("content", systemPrompt);
        messages.put(systemMsg);

        // User message
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messages.put(userMsg);

        request.put("messages", messages);
        return request;
    }

    /**
     * Create a simple completion request (alternative format).
     */
    public static JSONObject createSimpleRequest(String prompt, int maxTokens) {
        JSONObject request = new JSONObject();
        request.put("prompt", prompt);
        request.put("max_tokens", maxTokens);
        request.put("temperature", 0.7);
        return request;
    }

    /**
     * Create a request with custom temperature for creativity control.
     */
    public static JSONObject createCustomRequest(String systemPrompt, String userMessage,
                                                 int maxTokens, double temperature) {
        JSONObject request = createChatRequest(systemPrompt, userMessage, maxTokens);
        request.put("temperature", temperature);
        return request;
    }
}