package main.java.com.app.service;

import main.java.com.app.model.Event;

import java.util.ArrayList;
import java.util.List;

public class SortService {

    public List<Event> sortByDateAsc(List<Event> events) {
        List<Event> sortedEvents = new ArrayList<>(events);
        for (int i = 0; i < sortedEvents.size() - 1; i++) {
            for (int j = 0; j < sortedEvents.size() - 1 - i; j++) {
                if (sortedEvents.get(j).getDate().isAfter(sortedEvents.get(j + 1).getDate())) {
                    Event temp = sortedEvents.get(j);
                    sortedEvents.set(j, sortedEvents.get(j + 1));
                    sortedEvents.set(j + 1, temp);
                }
            }

        }
        return sortedEvents;
    }

    public List<Event> sortByDateDesc(List<Event> events) {
        List<Event> sortedEvents = sortByDateAsc(events);

        List<Event> reversed = new ArrayList<>();
        for (int i = sortedEvents.size() - 1; i >= 0; i--) {
            reversed.add(sortedEvents.get(i));
        }
        return reversed;
    }

    public List<Event> sortByNameAsc(List<Event> events) {
        List<Event> sortedEvents = new ArrayList<>(events);

        for (int i = 0; i < sortedEvents.size() - 1; i++) {
            for (int j = 0; j < sortedEvents.size() - 1 - i; j++) {
                String nameA = sortedEvents.get(j).getEventName().toLowerCase();
                String nameB = sortedEvents.get(j + 1).getEventName().toLowerCase();
                if (nameA.compareTo(nameB) > 0) {
                    Event temp = sortedEvents.get(j);
                    sortedEvents.set(j, sortedEvents.get(j + 1));
                    sortedEvents.set(j + 1, temp);
                }
            }
        }

        return sortedEvents;
    }

    public void displaySortedEvents(List<Event> events) {
        if(events == null || events.isEmpty()) {
            System.out.println("No events found");
            return;
        }
        for (Event event : events) {
            System.out.println(event);
        }
    }
}
