package org.modular_llm;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.LlamaOutput;
import de.kherud.llama.ModelParameters;
import de.kherud.llama.args.MiroStat;

import java.io.IOException;
import java.util.Scanner;

public class ChatbotMain {
    public static void main(String[] args) {
        // Initialize plugin system
        PluginManager pm = new PluginManager();
        System.out.println("Loading plugins...");
        pm.loadPlugin("WeatherPlugin");

        // Initialize AI model
        LlamaModel llamaModel = null;
        try {
            ModelParameters modelParams = new ModelParameters()
                    .setModel("models/tinyllama-1.1b-chat-v1.0.Q6_K.gguf")
                    .setGpuLayers(43); // optional: tune based on system

            llamaModel = new LlamaModel(modelParams);
        } catch (Exception e) {
            System.err.println("Failed to load model: " + e.getMessage());
            return;
        }

        // Create coordinator that uses actual model
        AIModel modelWrapper = new TinyLlamaAdapter(llamaModel);
        ResponseCoordinator coordinator = new DefaultCoordinator(pm, modelWrapper);

        // Chat loop
        Scanner scanner = new Scanner(System.in);
        System.out.println("Chatbot is ready. Type your message (type 'exit' to quit):");

        while (true) {
            System.out.print("You: ");
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input)) break;

            ChatbotResponse response = coordinator.handleInput(input);
            System.out.println(response);
        }

        pm.unloadPlugin("WeatherPlugin");
        scanner.close();

        if (llamaModel != null) {
            llamaModel.close(); // No need to catch IOException
        }
    }
}
