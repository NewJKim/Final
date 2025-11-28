package com.writingassistant.service;

import com.writingassistant.model.APIClient;
import com.writingassistant.model.RequestFactory;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * APIService - Handles communication with Cohere AI API.
 * Responsible for making HTTP requests and parsing responses.
 */
public class APIService {
    private final APIClient apiClient;
    private final HttpClient httpClient;
    private long lastRequestTime = 0;
    private static final long MIN_REQUEST_INTERVAL = 1000;  // 1 second between requests

    /**
     * Constructor - initializes API client and HTTP client.
     */
    public APIService() {
        this.apiClient = APIClient.getInstance();
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    /**
     * Generate text using Cohere API.
     * This method is called by all Strategy classes.
     *
     * @param prompt The complete prompt (system instructions + user input)
     * @param maxTokens Maximum length of response
     * @return Generated text or error message
     */
    public String generateText(String prompt, int maxTokens) {
        // Check if API key is configured
        if (!apiClient.isConfigured()) {
            return "Error: API key not configured. Please set COHERE_API_KEY.";
        }

        try {
            // Wait to avoid rate limiting
            enforceRateLimit();

            // Create JSON request body using Factory pattern
            JSONObject requestBody = RequestFactory.createCohereRequest(prompt, maxTokens);

            // Debug output (helpful for troubleshooting)
            System.out.println("DEBUG: Endpoint: " + apiClient.getApiEndpoint());
            System.out.println("DEBUG: Request body: " + requestBody.toString());

            // Build HTTP POST request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiClient.getApiEndpoint()))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiClient.getApiKey())
                    .header("Accept", "application/json")
                    // IMPORTANT: remove Cohere-Version header if your URL already has /v1/
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                    .build();

            // Send request and get response
            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Debug output
            System.out.println("DEBUG: Response status: " + response.statusCode());
            System.out.println("DEBUG: Response body: " + response.body());

            // Parse and return the response
            return parseResponse(response);

        } catch (Exception e) {
            e.printStackTrace();
            return handleError(e);
        }
    }

    /**
     * Parse Cohere API response and extract generated text.
     *
     * @param response HTTP response from Cohere
     * @return Generated text or error message
     */
    private String parseResponse(HttpResponse<String> response) {
        if (response.statusCode() == 200) {
            // Success! Extract the generated text
            try {
                JSONObject json = new JSONObject(response.body());

                // Cohere Chat API returns text in "text" field
                if (json.has("text")) {
                    return json.getString("text").trim();
                }

                // Fallback for other response formats
                if (json.has("response")) {
                    return json.getString("response").trim();
                }

                return "Error: Unexpected response format.";
            } catch (Exception e) {
                return "Error parsing response: " + e.getMessage();
            }
        } else if (response.statusCode() == 429) {
            // Too many requests
            return "Error: Rate limit exceeded. Please wait and try again.";
        } else if (response.statusCode() == 401) {
            // Bad API key
            return "Error: Invalid API key. Please check your configuration.";
        } else {
            // Other error
            return "Error: API returned status " + response.statusCode() +
                    "\nResponse: " + response.body();
        }
    }

    /**
     * Handle network and other errors.
     *
     * @param e The exception that occurred
     * @return User-friendly error message
     */
    private String handleError(Exception e) {
        if (e instanceof java.net.ConnectException) {
            return "Error: Cannot connect to API. Check your internet connection.";
        } else if (e instanceof java.net.http.HttpTimeoutException) {
            return "Error: Request timed out. Please try again.";
        } else {
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Enforce rate limiting to avoid hitting API limits.
     * Waits if requests are too frequent.
     */
    private void enforceRateLimit() throws InterruptedException {
        long now = System.currentTimeMillis();
        long delta = now - lastRequestTime;

        // If last request was too recent, wait
        if (delta < MIN_REQUEST_INTERVAL) {
            Thread.sleep(MIN_REQUEST_INTERVAL - delta);
        }
        lastRequestTime = System.currentTimeMillis();
    }
}
