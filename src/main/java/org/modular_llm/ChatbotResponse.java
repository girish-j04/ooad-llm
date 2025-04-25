package org.modular_llm;

public class ChatbotResponse {
    private final String source;   // "plugin" or "ai"
    private final String content;

    public ChatbotResponse(String source, String content) {
        this.source = source;
        this.content = content;
    }

    public String getSource() {
        return source;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "[" + source.toUpperCase() + "] " + content;
    }
}
