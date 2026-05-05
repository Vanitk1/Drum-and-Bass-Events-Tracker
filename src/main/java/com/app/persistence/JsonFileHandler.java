package com.app.persistence;

import com.app.model.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JsonFileHandler {

    private static final String FILE_PATH = "src/main/resources/events.json";

    private final Gson gson;

    public JsonFileHandler() {
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).setPrettyPrinting().create();
    }

    public void saveEvents(List<Event> events) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(events, writer);
            System.out.println(events.size() + " event(s) saved to " + FILE_PATH);
        } catch (IOException e) {
            System.out.println("Error saving events: " + e.getMessage());
        }
    }

    public List<Event> loadEvents() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<List<Event>>() {}.getType();
            List<Event> events = gson.fromJson(reader, listType);
            if (events == null) {
                return new ArrayList<>();
            }
            System.out.println(events.size() + " event(s) loaded from " + FILE_PATH);
            return events;
        } catch (IOException e) {
            System.out.println("No saved events found. Starting fresh.");
            return new ArrayList<>();
        }
    }

}
