package main.java.com.app;

import main.java.com.app.cli.Menu;
import main.java.com.app.service.EventService;
import main.java.com.app.service.SearchService;
import main.java.com.app.service.SortService;

public class Main {

    public static void main(String[] args) {
        EventService eventService = new EventService();
        SearchService searchService = new SearchService();
        SortService sortService = new SortService();

        Menu menu = new Menu(eventService, searchService, sortService);
        menu.start();
    }
}
