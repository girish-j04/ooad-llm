package org.modular_llm;

import org.modular_llm.plugins.WeatherPlugin;

import java.util.HashMap;
import java.util.Map;

public class PluginManager {
    private Map<String, Plugin> plugins = new HashMap<>();

    // Manually load known plugins
    public void loadPlugin(String pluginName) {
        try {
            String fullClassName = "org.modular_llm.plugins." + pluginName;
            Class<?> pluginClass = Class.forName(fullClassName);
            Plugin plugin = (Plugin) pluginClass.getDeclaredConstructor().newInstance();
            plugins.put(pluginName, plugin);
            System.out.println("Loaded plugin: " + pluginName);
        } catch (Exception e) {
            System.err.println("Failed to load plugin '" + pluginName + "': " + e.getMessage());
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
