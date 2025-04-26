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
                        return new ChatbotResponse("system", "✅ Switched to TinyLLaMA.");
                    } catch (Exception e) {
                        return new ChatbotResponse("system", "❌ Failed to load TinyLLaMA.");
                    }

                case "mistral":
                    try {
                        LlamaModel mistral = new LlamaModel(new ModelParameters()
                                .setModel("models/mistral-7b-instruct-v0.1.Q4_K_M.gguf")
                                .setGpuLayers(43));
                        this.switchModel(new MistralAdapter(mistral));
                        return new ChatbotResponse("system", "✅ Switched to Mistral-7B.");
                    } catch (Exception e) {
                        return new ChatbotResponse("system", "❌ Failed to load Mistral-7B.");
                    }

                default:
                    return new ChatbotResponse("system", "❓ Unknown model: " + modelName);
            }
        }

        String reply = aiModel.generateResponse(input);
        return new ChatbotResponse("ai", reply);
    }
}
