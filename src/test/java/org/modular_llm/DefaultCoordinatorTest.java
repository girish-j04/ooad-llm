package org.modular_llm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DefaultCoordinatorTest {

    private PluginManager pluginManager;
    private DefaultCoordinator coordinator;

    @BeforeEach
    void setUp() {
        pluginManager = new PluginManager();
        pluginManager.loadPlugin("WeatherPlugin");
        pluginManager.loadPlugin("FinancePlugin");

        // Use a dummy AIModel for testing
        AIModel mockModel = input -> "mock response to: " + input;
        coordinator = new DefaultCoordinator(pluginManager, mockModel);
    }

    @Test
    void testWeatherPlugin() {
        ChatbotResponse response = coordinator.handleInput("weather: Tokyo");
        assertEquals("plugin", response.getSource());
        assertTrue(response.getContent().toLowerCase().contains("weather in tokyo")
                || response.getContent().toLowerCase().contains("unable to get coordinates")
                || response.getContent().toLowerCase().contains("failed"));
    }

    @Test
    void testFinancePlugin() {
        ChatbotResponse response = coordinator.handleInput("finance: AAPL");
        assertEquals("plugin", response.getSource());
        assertTrue(response.getContent().toLowerCase().contains("stock")
                || response.getContent().toLowerCase().contains("error"));
    }

    @Test
    void testLLMCommandSwitching() {
        ChatbotResponse response = coordinator.handleInput("llm: tinyllama");
        assertEquals("system", response.getSource());
        assertTrue(response.getContent().toLowerCase().contains("tinyllama")
                || response.getContent().toLowerCase().contains("failed"));
    }

    @Test
    void testFallbackToAIModel() {
        ChatbotResponse response = coordinator.handleInput("tell me a joke");
        assertEquals("ai", response.getSource());
        assertEquals("mock response to: tell me a joke", response.getContent());
    }
}
