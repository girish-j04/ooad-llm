package org.modular_llm;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherPlugin implements Plugin {

    // This method will be called by PluginManager
    @Override
    public String execute(String location) {
        double[] coords = getCoordinates(location);
        if (coords == null) {
            return "Unable to get coordinates for: " + location;
        }

        return getWeather(coords[0], coords[1], location);
    }

    // Gets weather from Open-Meteo given lat/lon
    private String getWeather(double latitude, double longitude, String location) {
        try {
            String api = String.format(
                    "https://api.open-meteo.com/v1/forecast?latitude=%.4f&longitude=%.4f&current_weather=true",
                    latitude, longitude
            );
            HttpURLConnection conn = (HttpURLConnection) new URL(api).openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNext()) {
                    sb.append(scanner.nextLine());
                }
                scanner.close();

                JSONObject response = new JSONObject(sb.toString());
                JSONObject weather = response.getJSONObject("current_weather");

                return String.format(
                        "Weather in %s: %.1f°C, Windspeed %.1f km/h",
                        location,
                        weather.getDouble("temperature"),
                        weather.getDouble("windspeed")
                );
            } else {
                return "Failed to fetch weather data.";
            }

        } catch (Exception e) {
            return "Exception while fetching weather: " + e.getMessage();
        }
    }

    // Uses OpenStreetMap's Nominatim API to get lat/lon from a city name
    private double[] getCoordinates(String city) {
        try {
            String encodedCity = URLEncoder.encode(city, "UTF-8");
            String api = "https://nominatim.openstreetmap.org/search?q=" + encodedCity + "&format=json&limit=1";

            HttpURLConnection conn = (HttpURLConnection) new URL(api).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");  // Required by Nominatim

            if (conn.getResponseCode() == 200) {
                Scanner scanner = new Scanner(conn.getInputStream());
                StringBuilder sb = new StringBuilder();
                while (scanner.hasNext()) {
                    sb.append(scanner.nextLine());
                }
                scanner.close();

                JSONArray results = new JSONArray(sb.toString());
                if (results.length() == 0) {
                    return null;
                }

                JSONObject firstResult = results.getJSONObject(0);
                double lat = Double.parseDouble(firstResult.getString("lat"));
                double lon = Double.parseDouble(firstResult.getString("lon"));
                return new double[]{lat, lon};
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }
}
