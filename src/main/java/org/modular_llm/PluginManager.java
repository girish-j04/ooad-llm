package org.modular_llm;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class PluginManager {
    // Map to store plugin names and their corresponding Plugin objects
    private Map<String, Plugin> plugins = new HashMap<>();

    // Loads a plugin from the src/plugins directory by its name
    public void loadPlugin(String pluginName) {
        try {
            // Define the folder where plugins are located
            File pluginDir = new File("src/plugins");
            URL[] urls = { pluginDir.toURI().toURL() };

            // Create a class loader for the plugin directory
            URLClassLoader loader = new URLClassLoader(urls);

            // Load the class for the plugin. It is assumed that the class is in the "plugins" package.
            Class<?> clazz = loader.loadClass("plugins." + pluginName);
            // Create a new instance of the plugin class
            Plugin plugin = (Plugin) clazz.getDeclaredConstructor().newInstance();
            // Save the plugin in the map
            plugins.put(pluginName, plugin);
            System.out.println("Loaded plugin: " + pluginName);
        } catch (Exception e) {
            System.err.println("Error loading plugin: " + e.getMessage());
        }
    }

    // Unloads a plugin by removing it from the map
    public void unloadPlugin(String pluginName) {
        if (plugins.containsKey(pluginName)) {
            plugins.remove(pluginName);
            System.out.println("Unloaded plugin: " + pluginName);
        }
    }

    // Executes the plugin's code by calling its execute method
    public String executePlugin(String pluginName, String input) {
        Plugin plugin = plugins.get(pluginName);
        if (plugin != null) {
            return plugin.execute(input);
        } else {
            return "Plugin not loaded.";
        }
    }
}
