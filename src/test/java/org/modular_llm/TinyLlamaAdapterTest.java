package org.modular_llm;

import de.kherud.llama.LlamaModel;
import de.kherud.llama.ModelParameters;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TinyLlamaAdapterTest {

    private static LlamaModel llamaModel;
    private static TinyLlamaAdapter adapter;

    @BeforeAll
    static void initModel() throws Exception {
        llamaModel = new LlamaModel(new ModelParameters()
                .setModel("models/tinyllama-1.1b-chat-v1.0.Q6_K.gguf")
                .setGpuLayers(43));
        adapter = new TinyLlamaAdapter(llamaModel);
    }

    @AfterAll
    static void tearDown() {
        llamaModel.close();
    }

    @Test
    void testPromptUpdatesAcrossCalls() {
        String first = adapter.generateResponse("Hi");
        String second = adapter.generateResponse("Tell me a joke");

        assertNotNull(first);
        assertNotNull(second);
        assertNotEquals(first, second);
    }
}
