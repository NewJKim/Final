package com.writingassistant;

import com.writingassistant.model.APIClient;
import com.writingassistant.model.RequestFactory;
import com.writingassistant.model.strategy.AcademicWritingStrategy;
import com.writingassistant.model.strategy.CreativeWritingStrategy;
import com.writingassistant.model.strategy.ProfessionalWritingStrategy;
import com.writingassistant.model.strategy.WritingStrategy;
import com.writingassistant.service.APIService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Core test suite (10 tests) for the AI Writing Assistant.
 * Focuses on patterns (Singleton, Factory, Strategy) and config.
 *
 * Note: These tests do NOT make real network calls.
 * They only validate JSON shapes, names, and configuration wiring.
 */
public class WritingAssistantCoreTests {

    // 1) Singleton: same instance
    @Test
    void apiClient_isSingleton() {
        APIClient a = APIClient.getInstance();
        APIClient b = APIClient.getInstance();
        assertSame(a, b, "APIClient should be a singleton");
    }

    // 2) APIClient: endpoint present (from config or default)
    @Test
    void apiClient_hasEndpoint() {
        APIClient client = APIClient.getInstance();
        assertNotNull(client.getApiEndpoint(), "Endpoint should not be null");
        assertTrue(client.getApiEndpoint().startsWith("http"),
                "Endpoint should look like a URL");
    }

    // 3) Factory: Cohere v1 chat shape (message + preamble)
    @Test
    void cohereRequest_hasRequiredFields() {
        JSONObject body = RequestFactory.createCohereRequest("test prompt", 250);
        assertEquals("command-a-03-2025", body.getString("model"));
        assertTrue(body.has("message"), "Should include top-level 'message'");
        assertTrue(body.has("preamble"), "Should include 'preamble' for system instructions");
        assertTrue(body.has("max_tokens"));
        assertTrue(body.has("temperature"));
    }

    // 4) Factory: respects max tokens
    @Test
    void cohereRequest_respectsMaxTokens() {
        int k = 123;
        JSONObject body = RequestFactory.createCohereRequest("x", k);
        assertEquals(k, body.getInt("max_tokens"));
    }

    // 5) Factory: temperature in reasonable range
    @Test
    void cohereRequest_temperatureRange() {
        JSONObject body = RequestFactory.createCohereRequest("x", 100);
        double t = body.getDouble("temperature");
        assertTrue(t >= 0.0 && t <= 1.0, "Temperature should be between 0.0 and 1.0");
    }

    // 6) Strategy: Professional name
    @Test
    void professionalStrategy_name() {
        WritingStrategy s = new ProfessionalWritingStrategy(new APIService());
        assertEquals("Professional", s.getStrategyName());
    }

    // 7) Strategy: Creative name
    @Test
    void creativeStrategy_name() {
        WritingStrategy s = new CreativeWritingStrategy(new APIService());
        assertEquals("Creative", s.getStrategyName());
    }

    // 8) Strategy: Academic name
    @Test
    void academicStrategy_name() {
        WritingStrategy s = new AcademicWritingStrategy(new APIService());
        assertEquals("Academic", s.getStrategyName());
    }

    // 9) Strategy: prompts not empty
    @Test
    void strategies_promptsNotEmpty() {
        WritingStrategy creative = new CreativeWritingStrategy(new APIService());
        WritingStrategy professional = new ProfessionalWritingStrategy(new APIService());
        WritingStrategy academic = new AcademicWritingStrategy(new APIService());
        assertFalse(creative.getSystemPrompt().isEmpty());
        assertFalse(professional.getSystemPrompt().isEmpty());
        assertFalse(academic.getSystemPrompt().isEmpty());
    }

    // 10) Strategy: prompts are different (ensures Strategy pattern actually varies behavior)
    @Test
    void strategies_promptsAreDifferent() {
        WritingStrategy creative = new CreativeWritingStrategy(new APIService());
        WritingStrategy professional = new ProfessionalWritingStrategy(new APIService());
        assertNotEquals(creative.getSystemPrompt(), professional.getSystemPrompt(),
                "Creative and Professional prompts should differ");
    }
}
