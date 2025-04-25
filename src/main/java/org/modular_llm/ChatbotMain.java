package org.modular_llm;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.LlamaOutput;
import de.kherud.llama.ModelParameters;
import de.kherud.llama.args.MiroStat;


import java.io.*;
import java.nio.charset.StandardCharsets;

public class ChatbotMain {

    public static void main(String... args) {
        new ChatbotMain().startChat();
    }

    public void startChat() {
        ModelParameters modelParams = new ModelParameters()
                .setModel("models/tinyllama-1.1b-chat-v1.0.Q6_K.gguf") // Set the model path correctly
                .setGpuLayers(43);

        String system = "This is a conversation between User and Llama, a friendly chatbot.\n" +
                "Llama is helpful, kind, honest, good at writing, and never fails to answer any " +
                "requests immediately and with precision.\n";

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
             LlamaModel model = new LlamaModel(modelParams)) {

            System.out.print(system);
            String prompt = system;

            while (true) {
                System.out.print("\nUser: ");
                String input = reader.readLine();
                if (input == null || input.equalsIgnoreCase("exit")) {
                    System.out.println("\nExiting chat...");
                    break;
                }

                prompt += "\nUser: " + input + "\nLlama: ";
                System.out.print("Llama: ");

                InferenceParameters inferParams = new InferenceParameters(prompt)
                        .setTemperature(0.7f)
                        .setPenalizeNl(true)
                        .setMiroStat(MiroStat.V2)
                        .setStopStrings("User:");

                for (LlamaOutput output : model.generate(inferParams)) {
                    System.out.print(output);
                    prompt += output;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }
}
