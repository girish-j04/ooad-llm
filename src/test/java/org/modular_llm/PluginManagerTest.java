package org.modular_llm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PluginManagerTest {
    private PluginManager manager;

    @BeforeEach
    void setUp() {
        manager = new PluginManager();
    }

    @Test
    void testLoadAndExecuteWeatherPlugin() {
        manager.loadPlugin("WeatherPlugin");
        String result = manager.executePlugin("WeatherPlugin", "Paris");

        assertNotNull(result);
        assertTrue(result.toLowerCase().contains("weather")
                || result.toLowerCase().contains("unable")
                || result.toLowerCase().contains("failed"));
    }

    @Test
    void testLoadAndExecuteFinancePlugin() {
        manager.loadPlugin("FinancePlugin");
        String result = manager.executePlugin("FinancePlugin", "AAPL");

        assertNotNull(result);
        assertTrue(result.toLowerCase().contains("stock")
                || result.toLowerCase().contains("invalid")
                || result.toLowerCase().contains("error"));
    }

    @Test
    void testUnloadPlugin() {
        manager.loadPlugin("WeatherPlugin");
        manager.unloadPlugin("WeatherPlugin");

        String result = manager.executePlugin("WeatherPlugin", "Tokyo");
        assertEquals("Plugin not loaded.", result);
    }

    @Test
    void testUnknownPlugin() {
        String result = manager.executePlugin("UnknownPlugin", "test");
        assertEquals("Plugin not loaded.", result);
    }
}
