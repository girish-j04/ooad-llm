package org.modular_llm;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.LlamaOutput;
import de.kherud.llama.ModelParameters;
import de.kherud.llama.args.MiroStat;

import java.io.IOException;
import java.util.Scanner;

public class ChatbotMain {
    public static void main(String[] args) throws Exception {
        PluginManager pm = new PluginManager();
        pm.loadPlugin("WeatherPlugin");
        pm.loadPlugin("FinancePlugin");

        LlamaModel model = new LlamaModel(new ModelParameters()
                .setModel("models/tinyllama-1.1b-chat-v1.0.Q6_K.gguf")
                .setGpuLayers(43));

        AIModel aiAdapter = new TinyLlamaAdapter(model);
        ResponseCoordinator coordinator = new DefaultCoordinator(pm, aiAdapter);

        // Run terminal UI
        new ChatbotTUI(coordinator).run();

        // Cleanup
        pm.unloadPlugin("WeatherPlugin");
        pm.unloadPlugin("FinancePlugin");
        model.close();
    }
}
