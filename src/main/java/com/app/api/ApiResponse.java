package com.app.api;

import com.app.model.Artist;
import com.app.model.Venue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.app.model.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApiResponse {
    public List<Event> mapEvents(String json) {
        List<Event> events = new ArrayList<>();

        if (json == null || json.isBlank()) {
            System.out.println("json has no data");
            return events;
        }

        try {
            JsonObject root = new JsonParser().parseString(json).getAsJsonObject();

            int error = root.get("error").getAsInt();
            if (error != 0) {
                System.out.println("Skiddle API returned an error code " + error);
                return events;
            }
            JsonArray results = root.getAsJsonArray("results");
            for (JsonElement element : results ) {
                JsonObject result = element.getAsJsonObject();

                try {
                    String eventName = result.get("eventname").getAsString();
                    String description = result.has("description") && !result.get("description").isJsonNull() ? result.get("description").getAsString() : null;
                    String dataStr = result.get("date").getAsString();
                    LocalDate date = LocalDate.parse(dataStr);

                    JsonObject venueJson = result.getAsJsonObject("venue");
                    String venueName = venueJson.get("name").getAsString();
                    String address = venueJson.get("address").getAsString();
                    String city = venueJson.get("town").getAsString();
                    Venue venue = new Venue(venueName, address, city);

                    double price = 0.00;
                    if (result.has("ticketpricing") && !result.get("ticketpricing").isJsonNull()) {
                        JsonObject pricing = result.getAsJsonObject("ticketpricing");
                        if (pricing.has("minPrice")) {
                            price = pricing.get("minPrice").getAsDouble();
                        }
                    }

                    String ticketUrl = result.has("link") && !result.get("link").isJsonNull()
                            ? result.get("link").getAsString()
                            : "";

                    List<Artist> lineup = new ArrayList<>();

                    Event event = new Event(eventName, description, date, city, venue, lineup, price, ticketUrl);
                    events.add(event);

                } catch (Exception e) {
                    System.out.println("Skipped one event due to missing data: " + e.getMessage());
                }
            }

            System.out.println(events.size() + " event(s) retrieved from Skiddle.");

        } catch (Exception e) {
            System.out.println("Failed to load Skiddle response: " + e.getMessage());
        }

        return events;
    }
}


