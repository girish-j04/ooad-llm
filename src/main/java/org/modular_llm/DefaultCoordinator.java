package org.modular_llm;


import de.kherud.llama.LlamaModel;
import de.kherud.llama.ModelParameters;

public class DefaultCoordinator implements ResponseCoordinator {
    private final PluginManager pluginManager;
    private AIModel aiModel;

    public DefaultCoordinator(PluginManager pluginManager, AIModel aiModel) {
        this.pluginManager = pluginManager;
        this.aiModel = aiModel;
    }

    public void switchModel(AIModel newModel) {
        this.aiModel = newModel;
    }

    @Override
    public ChatbotResponse handleInput(String input) {
        if (input.toLowerCase().startsWith("weather:")) {
            String city = input.substring("weather:".length()).trim();
            String result = pluginManager.executePlugin("WeatherPlugin", city);
            return new ChatbotResponse("plugin", result);
        }

        if (input.toLowerCase().startsWith("finance:")) {
            String stock = input.substring("finance:".length()).trim();
            String result = pluginManager.executePlugin("FinancePlugin", stock);
            return new ChatbotResponse("plugin", result);
        }

        if (input.toLowerCase().startsWith("llm:")) {
            String modelName = input.substring("llm:".length()).trim().toLowerCase();
            switch (modelName) {
                case "tinyllama":
                    try {
                        LlamaModel llama = new LlamaModel(new ModelParameters()
                                .setModel("models/tinyllama-1.1b-chat-v1.0.Q6_K.gguf")
                                .setGpuLayers(43));
                        this.switchModel(new TinyLlamaAdapter(llama));
                        return new ChatbotResponse("system", "Switched to TinyLlama.");
                    } catch (Exception e) {
                        return new ChatbotResponse("system", "Failed to load TinyLlama.");
                    }
                    // case "gpt4": implement other adapters later
                default:
                    return new ChatbotResponse("system", "Unknown model: " + modelName);
            }
        }

        String reply = aiModel.generateResponse(input);
        return new ChatbotResponse("ai", reply);
    }
}

