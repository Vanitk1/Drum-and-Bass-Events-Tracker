package main.java.com.app.service;

import main.java.com.app.model.Artist;
import main.java.com.app.model.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchService {

    public List<Event> searchByCity(List<Event> events, String city) {
        List<Event> results = new ArrayList<>();
        for (Event event : events) {
            if (event.getCity().equalsIgnoreCase(city)) {
                results.add(event);
            }
        }
        return results;
    }

    public List<Event> searchByDate(List<Event> events, LocalDate date) {
        List<Event> results = new ArrayList<>();

        for (Event event : events) {
            if (event.getDate().equals(date)) {
                results.add(event);
            }
        }
        return results;
    }

    public List<Event> searchByArtist(List<Event> events, String artistName) {
        List<Event> results = new ArrayList<>();
        for (Event event : events) {
            for (Artist artist : event.getLineup()) {
                if (artist.getArtistName().equalsIgnoreCase(artistName)) {
                    results.add(event);
                    break;
                }
            }
        }
        return results;
    }

    public List<Event> searchEventsByVenue(List<Event> events, String venueName) {
        List<Event> results = new ArrayList<>();
        for (Event event : events) {
            if (event.getVenue() != null && event.getVenue().getVenueName().equalsIgnoreCase(venueName)) {
                results.add(event);
            }
        }
        return results;
    }

    public void displaySearchEvents(List<Event> results, String search) {
        if  (results == null || results.isEmpty()) {
            System.out.println("No events found for " + search);
            return;
        }
        System.out.println(results.size() + " events found for " + search);
        for (Event event : results) {
            System.out.println(event);
        }
    }

}
