package org.modular_llm;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChatbotResponseTest {

    @Test
    void testConstructionAndGetters() {
        ChatbotResponse res = new ChatbotResponse("ai", "hello world");
        assertEquals("ai", res.getSource());
        assertEquals("hello world", res.getContent());
    }

    @Test
    void testToStringFormat() {
        ChatbotResponse res = new ChatbotResponse("plugin", "weather result");
        assertEquals("[PLUGIN] weather result", res.toString());
    }
}
