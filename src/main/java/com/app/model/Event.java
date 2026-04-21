package main.java.com.app.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Event {

    private String eventName;
    private String eventDescription;
    private LocalDate date;
    private String city;
    private Venue venue;
    private List<Artist> lineup;
    private double price;
    private String ticketUrl;


    public Event(String eventName, String eventDescription, LocalDate date,  String city, Venue venue,
                 List<Artist> lineup, double price, String ticketUrl) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.date = date;
        this.city = city;
        this.venue = venue;
        this.lineup = lineup != null ? lineup : new ArrayList<>();
        this.price = price;
        this.ticketUrl = ticketUrl;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public LocalDate getDate() {
        return date;
    }
    public String getCity() {
        return city;
    }
    public Venue getVenue() {
        return venue;
    }
    public List<Artist> getLineup() {
        return lineup;
    }
    public double getPrice() {
        return price;
    }
    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setVenue(Venue venue) {
        this.venue = venue;
    }
    public void setLineup(List<Artist> lineup) {
        this.lineup = lineup;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public void addArtist(Artist artist) {
        if(!lineup.contains(artist)) {
            lineup.add(artist);
        }
    }

    @Override
    public String toString() {
        String venueName = (venue != null) ? venue.getVenueName() : "TBA";
        String eventPrice = (price == 0.0) ? "Free" : String.format("£%.2f", price);
        String ticketLink = (ticketUrl != null && !ticketUrl.isBlank()) ? ticketUrl : "No link available";

        StringBuilder sb = new StringBuilder();
        sb.append("-----------------------------------------\n");
        sb.append("Event Name: ").append(eventName).append("\n");
        sb.append("Date: ").append(date.toString()).append("\n");
        sb.append("City: ").append(city).append("\n");
        sb.append("Event Description: ").append(eventDescription).append("\n");
        sb.append("Venue: ").append(venueName).append("\n");
        sb.append("Price: ").append(eventPrice).append("\n");
        sb.append("Ticket Link: ").append(ticketLink).append("\n");

        sb.append("Lineup: ");
        if (lineup == null || lineup.isEmpty()) {
            sb.append("TBA");
        } else {
            for (int i = 0; i < lineup.size(); i++) {
                sb.append(lineup.get(i).getArtistName());
                if(i < lineup.size() - 1) {
                    sb.append(", ");
                }
            }
        }

        sb.append("-----------------------------------\n");
        return sb.toString();
    }
}
