package org.modular_llm.plugins;

import org.modular_llm.Plugin;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class FinancePlugin implements Plugin {
    @Override
    public String execute(String stockSymbol) {
        try {
            String api = "https://finnhub.io/api/v1/quote?symbol=" + stockSymbol.toUpperCase() + "&token=d05u67hr01qgqsu9jgigd05u67hr01qgqsu9jgj0";
            HttpURLConnection conn = (HttpURLConnection) new URL(api).openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNext()) sb.append(scanner.nextLine());
                scanner.close();

                JSONObject response = new JSONObject(sb.toString());
                double current = response.getDouble("c");
                double high = response.getDouble("h");
                double low = response.getDouble("l");

                return String.format("Stock: %s\nCurrent: $%.2f\nHigh: $%.2f\nLow: $%.2f", stockSymbol.toUpperCase(), current, high, low);
            } else {
                return "Failed to fetch finance data.";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
