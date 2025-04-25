package org.modular_llm.api;

import de.kherud.llama.LlamaModel;
import de.kherud.llama.ModelParameters;
import org.modular_llm.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")  // Allows frontend to talk to this server
public class ChatController {
    private final PluginManager pm;
    private final ResponseCoordinator coordinator;

    public ChatController() throws Exception {
        pm = new PluginManager();
        pm.loadPlugin("WeatherPlugin");
        pm.loadPlugin("FinancePlugin");

        LlamaModel model = new LlamaModel(new ModelParameters()
                .setModel("models/tinyllama-1.1b-chat-v1.0.Q6_K.gguf")
                .setGpuLayers(43));
        coordinator = new DefaultCoordinator(pm, new TinyLlamaAdapter(model));
    }

    @PostMapping("/chat")
    public ChatbotResponse chat(@RequestBody ChatRequest input) {
        return coordinator.handleInput(input.getMessage());
    }
}

class ChatRequest {
    private String message;
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
