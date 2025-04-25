package org.modular_llm;

import java.util.HashMap;
import java.util.Map;

public class PluginManager {
    private Map<String, Plugin> plugins = new HashMap<>();

    // Manually load known plugins
    public void loadPlugin(String pluginName) {
        switch (pluginName) {
            case "WeatherPlugin":
                plugins.put(pluginName, new WeatherPlugin());
                System.out.println("Loaded plugin: " + pluginName);
                break;
            default:
                System.err.println("Unknown plugin: " + pluginName);
        }
    }

    public void unloadPlugin(String pluginName) {
        if (plugins.remove(pluginName) != null) {
            System.out.println("Unloaded plugin: " + pluginName);
        } else {
            System.err.println("Plugin not loaded: " + pluginName);
        }
    }

    public String executePlugin(String pluginName, String input) {
        Plugin plugin = plugins.get(pluginName);
        return plugin != null
                ? plugin.execute(input)
                : "Plugin not loaded.";
    }
}
