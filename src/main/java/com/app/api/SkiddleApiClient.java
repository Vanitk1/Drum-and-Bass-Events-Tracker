package com.app.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class SkiddleApiClient {

    private static final String BASE_URL = "https://www.skiddle.com/api/v1/events/search/";
    private static final String CONFIG_PATH = "src/main/resources/config.properties";
    private final HttpClient httpClient;
    private final String apiKey;

    public SkiddleApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.apiKey = loadApiKey();
    }

    public String searchEvents(String keyword, int limit) {
        String url = BASE_URL + "?api_key=" + apiKey
                + "&keyword=" + keyword.replace(" ", "+")
                + "&limit=" + limit;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.out.println("API error: HTTP " + response.statusCode());
                return null;
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Failed to connect to Skiddle API: " + e.getMessage());
            return null;
        }
    }

    public String searchEventsByCity(String city, int limit) {
        return searchEvents("drum+and+bass+" + city, limit);
    }

    private String loadApiKey() {
        try (FileInputStream fis = new FileInputStream(CONFIG_PATH)) {
            Properties props = new Properties();
            props.load(fis);
            return props.getProperty("skiddle.api.key");
        } catch (IOException e) {
            System.out.println("Could not load API key from config.properties: " + e.getMessage());
            return "";
        }
    }
}
