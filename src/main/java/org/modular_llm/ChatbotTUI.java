package org.modular_llm;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

public class ChatbotTUI {
    private final ResponseCoordinator coordinator;

    public ChatbotTUI(ResponseCoordinator coordinator) {
        this.coordinator = coordinator;
    }

    public void run() throws Exception {
        Screen screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();

        WindowBasedTextGUI gui = new MultiWindowTextGUI(screen);
        BasicWindow window = new BasicWindow("🧠 Modular Chatbot Terminal");

        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

        // Chat display area
        TextBox chatLog = new TextBox(new TerminalSize(80, 20), TextBox.Style.MULTI_LINE);
        chatLog.setReadOnly(true);
        chatLog.setText("🤖 Welcome to Modular Chatbot!\nType below and press Enter to chat.\n");

        // Input box
        TextBox inputBox = new TextBox(new TerminalSize(80, 1));

        mainPanel.addComponent(chatLog);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1))); // Spacer
        mainPanel.addComponent(inputBox);

        window.setComponent(mainPanel);
        gui.addWindow(window);

        gui.getGUIThread().invokeLater(inputBox::takeFocus);

        // Input loop
        while (true) {
            KeyStroke key = screen.readInput();
            if (key == null) continue;

            // Exit on Esc or Ctrl+C
            if (key.getKeyType() == KeyType.Escape ||
                    (key.getKeyType() == KeyType.Character && key.isCtrlDown() && key.getCharacter() == 'c')) {
                screen.stopScreen();
                break;
            }

            // Handle Enter key
            if (key.getKeyType() == KeyType.Enter) {
                String input = inputBox.getText().trim();
                if (!input.isEmpty()) {
                    chatLog.addLine("You: " + input);
                    ChatbotResponse response = coordinator.handleInput(input);
                    chatLog.addLine(response.toString());
                    chatLog.addLine(""); // Add spacing after response
                    inputBox.setText("");
                }
            } else {
                inputBox.handleKeyStroke(key);
            }

            gui.updateScreen();
        }
    }
}
