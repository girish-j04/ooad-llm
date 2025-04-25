
package org.modular_llm;

public interface Plugin {
    String execute(String input);  // Every plugin must have this function
}
