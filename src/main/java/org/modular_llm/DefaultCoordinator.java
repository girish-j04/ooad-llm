package org.modular_llm;


public class DefaultCoordinator implements ResponseCoordinator {
    private final PluginManager pluginManager;
    private final AIModel aiModel;

    public DefaultCoordinator(PluginManager pluginManager, AIModel aiModel) {
        this.pluginManager = pluginManager;
        this.aiModel = aiModel;
    }

    @Override
    public ChatbotResponse handleInput(String input) {
        // Check if input is a plugin request (e.g., weather: Bengaluru)
        if (input.toLowerCase().startsWith("weather:")) {
            String city = input.substring("weather:".length()).trim();
            String result = pluginManager.executePlugin("WeatherPlugin", city);
            return new ChatbotResponse("plugin", result);
        }

        // Default to AI response
        String reply = aiModel.generateResponse(input);
        return new ChatbotResponse("ai", reply);
    }
}
