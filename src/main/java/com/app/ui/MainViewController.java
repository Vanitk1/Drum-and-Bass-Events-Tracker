package com.app.ui;

import com.app.api.ApiResponse;
import com.app.api.SkiddleApiClient;
import com.app.model.Event;
import com.app.persistence.JsonFileHandler;
import com.app.service.EventService;
import com.app.service.SearchService;
import com.app.service.SortService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.util.List;

public class MainViewController {

    private EventService eventService;
    private SearchService searchService;
    private SortService sortService;
    private JsonFileHandler jsonFileHandler;
    private SkiddleApiClient skiddleApiClient;
    private ApiResponse apiResponseMapper;

    @FXML private TableView<Event> eventsTable;
    @FXML private TableColumn<Event, String> colName;
    @FXML private TableColumn<Event, LocalDate> colDate;
    @FXML private TableColumn<Event, String> colCity;
    @FXML private TableColumn<Event, String> colVenue;
    @FXML private TableColumn<Event, Double> colPrice;
    @FXML private Label eventsCountLabel;

    @FXML private TextField searchCityField;
    @FXML private TextField searchArtistField;
    @FXML private DatePicker searchDatePicker;
    @FXML private TextField searchVenueField;

    @FXML private TableView<Event> searchResultsTable;
    @FXML private TableColumn<Event, String> colSearchName;
    @FXML private TableColumn<Event, LocalDate> colSearchDate;
    @FXML private TableColumn<Event, String> colSearchCity;
    @FXML private TableColumn<Event, String> colSearchVenue;
    @FXML private TableColumn<Event, Double> colSearchPrice;
    @FXML private Label searchResultsLabel;

    public void initServices(EventService eventService, SearchService searchService, SortService sortService, JsonFileHandler jsonFileHandler,
                             SkiddleApiClient skiddleApiClient, ApiResponse apiResponseMapper) {
        this.eventService = eventService;
        this.searchService = searchService;
        this.sortService = sortService;
        this.jsonFileHandler = jsonFileHandler;
        this.skiddleApiClient = skiddleApiClient;
        this.apiResponseMapper = apiResponseMapper;

        setupTableColumns();
        setupSearchTableColumns();
    }

    private void formatDateColumn(TableColumn<Event, LocalDate> column) {
        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });
    }

    private void setupTableColumns() {
        colName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colVenue.setCellValueFactory(cellData -> {
            Event event = cellData.getValue();
            String venueName = (event.getVenue() != null) ? event.getVenue().getVenueName() : "TBA";
            return new javafx.beans.property.SimpleStringProperty(venueName);
        });
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        formatDateColumn(colDate);
    }

    private void setupSearchTableColumns() {
        colSearchName.setCellValueFactory(new PropertyValueFactory<>("eventName"));
        colSearchDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colSearchCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colSearchVenue.setCellValueFactory(cellData -> {
            Event event = cellData.getValue();
            String venueName = (event.getVenue() != null) ? event.getVenue().getVenueName() : "TBA";
            return new javafx.beans.property.SimpleStringProperty(venueName);
        });
        colSearchPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        formatDateColumn(colSearchDate);
    }

    public void loadEventsTable() {
        List<Event> sorted = sortService.sortByDateAsc(eventService.getEvents());
        ObservableList<Event> data = FXCollections.observableArrayList(sorted);
        eventsTable.setItems(data);
        eventsCountLabel.setText(sorted.size() + " event(s)");
    }

    @FXML
    private void onFetchFromSkiddle() {
        String json = skiddleApiClient.searchEvents("drum and bass", 100);
        List<Event> apiEvents = apiResponseMapper.mapEvents(json);
        eventService.loadEvents(apiEvents);
        loadEventsTable();
        showAlert("Skiddle", apiEvents.size() + " event(s) fetched from Skiddle.");
    }

    @FXML
    private void onRemoveSelected() {
        Event selected = eventsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Remove Event", "Please select an event to remove.");
            return;
        }
        eventService.removeEvent(selected.getEventName());
        loadEventsTable();
    }

    @FXML
    private void onRefresh() {
        loadEventsTable();
    }

    @FXML
    private void onSearch() {
        String city    = searchCityField.getText().trim();
        String artist  = searchArtistField.getText().trim();
        String venue   = searchVenueField.getText().trim();
        LocalDate date = searchDatePicker.getValue();

        List<Event> results = eventService.getEvents();

        if (!city.isEmpty())   results = searchService.searchByCity(results, city);
        if (!artist.isEmpty()) results = searchService.searchByArtist(results, artist);
        if (!venue.isEmpty())  results = searchService.searchEventsByVenue(results, venue);
        if (date != null)      results = searchService.searchByDate(results, date);

        ObservableList<Event> data = FXCollections.observableArrayList(results);
        searchResultsTable.setItems(data);
        searchResultsLabel.setText(results.size() + " result(s) found");
    }

    @FXML
    private void onClearSearch() {
        searchCityField.clear();
        searchArtistField.clear();
        searchVenueField.clear();
        searchDatePicker.setValue(null);
        searchResultsTable.setItems(FXCollections.observableArrayList());
        searchResultsLabel.setText("");
    }

    @FXML
    private void onAddEvent() {
        AddEvent dialog = new AddEvent();
        dialog.showAndGetResult().ifPresent(event -> {
            eventService.addEvent(event);
            loadEventsTable();
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}