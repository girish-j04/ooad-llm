package org.modular_llm;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherPlugin implements Plugin {
    // API key for accessing the OpenWeatherMap service (replace with a real key)
    private static final String API_KEY = "API KEY";

    // This method fetches the weather data for a given location
    @Override
    public String execute(String location) {
        try {
            // Build the API URL using the provided location and API key
            String api = "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + API_KEY;
            // Open a connection to the API
            HttpURLConnection conn = (HttpURLConnection) new URL(api).openConnection();
            conn.setRequestMethod("GET");

            // If the request is successful (HTTP 200 OK)
            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder sb = new StringBuilder();
                // Read the response data line by line
                while (scanner.hasNext()) {
                    sb.append(scanner.nextLine());
                }
                scanner.close();
                return "Weather in " + location + ": " + sb.toString();
            } else {
                // If the response code is not 200, indicate a failure
                return "Failed to fetch data.";
            }
        } catch (IOException e) {
            // Return any exception messages that occur during the process
            return "Exception: " + e.getMessage();
        }
    }
}
