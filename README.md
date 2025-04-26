# Modular Chatbot Framework

A modern, adaptable chatbot framework powered by large language models (LLMs) with plugin-based extensibility — now with a beautiful web interface.

This project reimagines the traditional chatbot experience using a modular Java backend, a plugin system, and a sleek React-based web frontend. Users can interact with the bot in real time while dynamically using plugin-powered commands like `weather:`, `finance:`, and more.

---

## Features

- **Plugin-Based Design** — Add or remove functionality like weather or finance data retrieval at runtime
- **LLM Integration** — Interact with transformer-based models like TinyLLaMA
- **Hot-Swappable Modules** — Switch models or extend functionality without restarting the backend
- **Modern Web UI** — Built with React and plain CSS, including light/dark theme toggle
- **Command Routing** — Use natural commands like `weather: New York` or `finance: AAPL`

---

## Screenshots

![Screenshot 1](./images/light_mode.png)

![Screenshot 2](./images/hotswap_dark.png)

---

## Tech Stack

| Layer        | Technology                         |
|--------------|-------------------------------------|
| **Backend**  | Java, Spring Boot, Llama4J (TinyLLaMA wrapper) |
| **Frontend** | React, Plain CSS (no framework)    |
| **LLM**      | TinyLLaMA (quantized GGUF format)  |
| **Plugins**  | Custom Java plugins (e.g., WeatherPlugin, FinancePlugin) |
| **API**      | RESTful JSON interface via Spring Boot |

---

