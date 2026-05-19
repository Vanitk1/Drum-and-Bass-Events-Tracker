package com.app.ui;

import com.app.api.ApiResponse;
import com.app.api.SkiddleApiClient;
import com.app.persistence.JsonFileHandler;
import com.app.service.EventService;
import com.app.service.SearchService;
import com.app.service.SortService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class MainApp extends Application {
    private static EventService eventService;
    private static SearchService searchService;
    private static SortService sortService;
    private static JsonFileHandler jsonFileHandler;
    private static SkiddleApiClient skiddleApiClient;
    private static ApiResponse apiResponseMapper;

    public static void main(String[] args) {
        eventService = new EventService();
        searchService = new SearchService();
        sortService = new SortService();
        jsonFileHandler = new JsonFileHandler();
        skiddleApiClient = new SkiddleApiClient();
        apiResponseMapper = new ApiResponse();

        eventService.loadEvents(jsonFileHandler.loadEvents());

        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getClassLoader().getResource("MainView.fxml"));
        Scene scene = new Scene(loader.load(), 900, 600);

        MainViewController controller = loader.getController();
        controller.initServices(eventService, searchService, sortService,
                jsonFileHandler, skiddleApiClient, apiResponseMapper);
        controller.loadEventsTable();

        stage.setTitle("DnB Events Tracker");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> jsonFileHandler.saveEvents(eventService.getEvents()));
    }
}

