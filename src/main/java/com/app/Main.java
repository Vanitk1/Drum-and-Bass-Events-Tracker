package com.app;

import com.app.cli.Menu;
import com.app.persistence.JsonFileHandler;
import com.app.service.EventService;
import com.app.service.SearchService;
import com.app.service.SortService;

public class Main {

    public static void main(String[] args) {
        EventService eventService = new EventService();
        SearchService searchService = new SearchService();
        SortService sortService = new SortService();
        JsonFileHandler jsonFileHandler = new JsonFileHandler();

        Menu menu = new Menu(eventService, searchService, sortService);

        eventService.loadEvents(jsonFileHandler.loadEvents());
        menu.start();

        jsonFileHandler.saveEvents(eventService.getEvents());
    }
}
