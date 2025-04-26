package org.modular_llm;

import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.LlamaOutput;
import de.kherud.llama.args.MiroStat;

public class MistralAdapter implements AIModel {

    private final LlamaModel model;
    private String prompt;

    public MistralAdapter(LlamaModel model) {
        this.model = model;
        this.prompt = "You are Mistral, a helpful and concise AI assistant.\n";
    }

    @Override
    public String generateResponse(String input) {
        prompt += "\nUser: " + input + "\nMistral: ";

        InferenceParameters params = new InferenceParameters(prompt)
                .setTemperature(0.7f)
                .setPenalizeNl(true)
                .setMiroStat(MiroStat.V2)
                .setStopStrings("User:");

        StringBuilder outputText = new StringBuilder();
        for (LlamaOutput output : model.generate(params)) {
            outputText.append(output);
        }

        prompt += outputText;
        return outputText.toString().trim();
    }
}
