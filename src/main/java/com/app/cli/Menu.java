package com.app.cli;
import com.app.api.ApiResponse;
import com.app.api.SkiddleApiClient;
import com.app.model.Artist;
import com.app.model.Venue;
import com.app.model.Event;
import com.app.service.EventService;
import com.app.service.SearchService;
import com.app.service.SortService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Menu {

    private final EventService eventService;
    private final SearchService searchService;
    private final SortService sortService;
    private final Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


    public Menu(EventService eventService, SearchService searchService, SortService sortService) {
        this.eventService = eventService;
        this.searchService = searchService;
        this.sortService = sortService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("=========================================");
        System.out.println("           DnB Events Tracker            ");
        System.out.println("=========================================");

        boolean running = true;

        while(running){
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice){
                case 1 -> viewAllEvents();
                case 2 -> searchByCity();
                case 3 -> searchByArtist();
                case 4 -> searchByDate();
                case 5 -> searchByVenue();
                case 6 -> addEvent();
                case 7 -> removeEvent();
                case 8 -> viewSortedByDate();
                case 9 -> fetchFromSkiddle();
                case 10 -> fetchFromSkiddleByCity();
                case 11 -> {
                    System.out.println("Bye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice, please pick a number between 1-9");

            }
        }
        scanner.close();
    }
    private void printMenu() {
        System.out.println("\n-----------------------------------------");
        System.out.println(" MAIN MENU");
        System.out.println("-----------------------------------------");
        System.out.println(" 1. View all events");
        System.out.println(" 2. Search by city");
        System.out.println(" 3. Search by artist");
        System.out.println(" 4. Search by date");
        System.out.println(" 5. Search by venue");
        System.out.println(" 6. Add new event");
        System.out.println(" 7. Remove event");
        System.out.println(" 8. View events sorted by date");
        System.out.println(" 9. Fetch events from Skiddle");
        System.out.println(" 10. Fetch events from Skiddle by city");
        System.out.println(" 11. Exit");
        System.out.println("-----------------------------------------");
    }

    private void viewAllEvents() {
        System.out.println("\n--- All Events (" + eventService.getTotalEvents() + ") ---");
        eventService.displayEvents();
    }

    private void searchByCity() {
        String city = readString("Enter city: ");
        List<Event> results = searchService.searchByCity(eventService.getEvents(), city);
        searchService.displaySearchEvents(results, city);
    }

    private void searchByArtist() {
        String artistName = readString("Enter artist name: ");
        List<Event> results = searchService.searchByArtist(eventService.getEvents(), artistName);
        searchService.displaySearchEvents(results, artistName);
    }

    private void searchByDate() {
        LocalDate date = readDate("Enter date (dd/MM/yyyy): ");
        if (date == null) return;
        List<Event> results = searchService.searchByDate(eventService.getEvents(), date);
        searchService.displaySearchEvents(results, date.toString());
    }

    private void searchByVenue() {
        String venueName = readString("Enter venue name: ");
        List<Event> results = searchService.searchEventsByVenue(eventService.getEvents(), venueName);
        searchService.displaySearchEvents(results, venueName);
    }

    private void addEvent() {
        System.out.println("\n--- Add New Event ---");

        String name = readString("Event name: ");
        String description = readString("Description: ");
        LocalDate date = readDate("Date (dd/MM/yyyy): ");
        if (date == null) return;
        String city = readString("City: ");

        String venueName = readString("Venue name: ");
        String address = readString("Venue address: ");
        Venue venue = new Venue(venueName, address, city);

        double price = readDouble("Ticket price (0 for free): ");
        String ticketUrl = readString("Ticket URL (leave blank if none): ");

        Event event = new Event(name, description, date, city, venue, new ArrayList<>(), price, ticketUrl);

        System.out.println("Add one artist or multiple to lineup (enter 'done' when finished):");
        while (true) {
            String artistName = readString("Artist name (or press Enter to finish): ");
            if (artistName.isEmpty() || artistName.equalsIgnoreCase("done")) break;

            Artist check = new Artist(artistName, "");
            if (event.getLineup().contains(check)) {
                System.out.println(artistName + " is already in the lineup, skipping.");
                continue;
            }

            String genre = readString("Artist's genre: ");
            event.addArtist(new Artist(artistName, genre));
        }

        eventService.addEvent(event);
    }

    private void fetchFromSkiddle() {
        SkiddleApiClient client = new SkiddleApiClient();
        ApiResponse mapper = new ApiResponse();
        String json = client.searchEvents("drum and bass", 20);
        List<Event> apiEvents = mapper.mapEvents(json);
        eventService.loadEvents(apiEvents);
    }

    private void fetchFromSkiddleByCity() {
        String city = readString("Enter city: ");
        SkiddleApiClient client = new SkiddleApiClient();
        ApiResponse mapper = new ApiResponse();
        String json = client.searchEventsByCity(city, 20);
        List<Event> apiEvents = mapper.mapEvents(json);
        eventService.loadEvents(apiEvents);
    }

    private void removeEvent() {
        String eventName = readString("Enter the exact event name to remove: ");
        eventService.removeEvent(eventName);
    }

    private void viewSortedByDate() {
        List<Event> sorted = sortService.sortByDateAsc(eventService.getEvents());
        System.out.println("\n--- Events Sorted by Date ---");
        sortService.displaySortedEvents(sorted);
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid price.");
            }
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd/MM/yyyy (e.g. 25/12/2025).");
            }
        }
    }

}
