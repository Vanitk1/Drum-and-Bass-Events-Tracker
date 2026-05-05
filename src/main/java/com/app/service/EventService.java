package com.app.service;

import com.app.model.Artist;
import com.app.model.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventService {
    private List<Event> events;

    public EventService() {
        this.events = new ArrayList<>();
    }

    public void addEvent(Event event) {
        events.add(event);
        System.out.println("Added event: " + event.getEventName());
    }

    public boolean removeEvent(String eventName) {
        for (int i = 0; i<this.events.size(); i++) {
            if (events.get(i).getEventName().equalsIgnoreCase(eventName)) {
                events.remove(i);
                System.out.println("Removed event: " + eventName);
                return true;
            }
        }
        System.out.println("Event not found: " + eventName);
        return false;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void displayEvents() {
        if (events.isEmpty()) {
            System.out.println("No events found");
            return;
        }
        for (Event event : events) {
            System.out.println(event);
        }
    }
    public int getTotalEvents() {
        return events.size();
    }


    public void loadEvents(List<Event> loadedEvents) {
        events.addAll(loadedEvents);
        System.out.println(loadedEvents.size() + " events loaded");
    }

    public void clearEvents() {
        events.clear();
        System.out.println("All events cleared");
    }
}
