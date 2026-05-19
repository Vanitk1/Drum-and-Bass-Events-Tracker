package com.app.ui;

import com.app.model.Artist;
import com.app.model.Event;
import com.app.model.Venue;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddEvent extends Dialog<Event> {

    private final TextField nameField        = new TextField();
    private final TextArea  descriptionField = new TextArea();
    private final DatePicker datePicker      = new DatePicker();
    private final TextField cityField        = new TextField();
    private final TextField venueNameField   = new TextField();
    private final TextField addressField     = new TextField();
    private final TextField priceField       = new TextField();
    private final TextField ticketUrlField   = new TextField();

    // Artist fields
    private final TextField artistNameField  = new TextField();
    private final TextField genreField       = new TextField();
    private final ListView<String> artistListView = new ListView<>();
    private final List<Artist> lineup        = new ArrayList<>();

    public AddEvent() {
        setTitle("Add New Event");
        setHeaderText("Enter the details for the new event");

        ButtonType addButtonType = new ButtonType("Add Event", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        getDialogPane().setContent(buildLayout());
        getDialogPane().setPrefWidth(520);

        setResultConverter(button -> {
            if (button == addButtonType) {
                return buildEvent();
            }
            return null;
        });
    }

    private VBox buildLayout() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(10));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);

        descriptionField.setPrefRowCount(2);
        descriptionField.setWrapText(true);

        grid.add(new Label("Event name:"),   0, 0); grid.add(nameField,        1, 0);
        grid.add(new Label("Description:"),  0, 1); grid.add(descriptionField, 1, 1);
        grid.add(new Label("Date:"),         0, 2); grid.add(datePicker,       1, 2);
        grid.add(new Label("City:"),         0, 3); grid.add(cityField,        1, 3);
        grid.add(new Label("Venue name:"),   0, 4); grid.add(venueNameField,   1, 4);
        grid.add(new Label("Address:"),      0, 5); grid.add(addressField,     1, 5);
        grid.add(new Label("Price (£):"),    0, 6); grid.add(priceField,       1, 6);
        grid.add(new Label("Ticket URL:"),   0, 7); grid.add(ticketUrlField,   1, 7);

        nameField.setPrefWidth(320);
        cityField.setPrefWidth(320);
        venueNameField.setPrefWidth(320);
        addressField.setPrefWidth(320);
        priceField.setPrefWidth(320);
        ticketUrlField.setPrefWidth(320);

        Label artistLabel = new Label("Lineup:");
        artistLabel.setStyle("-fx-font-weight: bold;");

        artistNameField.setPromptText("Artist name");
        genreField.setPromptText("Genre");
        artistNameField.setPrefWidth(180);
        genreField.setPrefWidth(140);

        Button addArtistBtn = new Button("Add Artist");
        addArtistBtn.setStyle("-fx-background-color: #e94560; -fx-text-fill: white;");
        addArtistBtn.setOnAction(e -> addArtistToLineup());

        Button removeArtistBtn = new Button("Remove");
        removeArtistBtn.setStyle("-fx-background-color: #444; -fx-text-fill: white;");
        removeArtistBtn.setOnAction(e -> removeSelectedArtist());

        HBox artistInputRow = new HBox(8, artistNameField, genreField, addArtistBtn, removeArtistBtn);
        artistListView.setPrefHeight(90);

        root.getChildren().addAll(grid, artistLabel, artistInputRow, artistListView);
        return root;
    }

    private void addArtistToLineup() {
        String name  = artistNameField.getText().trim();
        String genre = genreField.getText().trim();

        if (name.isEmpty()) {
            showError("Please enter an artist name.");
            return;
        }

        Artist newArtist = new Artist(name, genre);

        for (Artist a : lineup) {
            if (a.getArtistName().equalsIgnoreCase(name)) {
                showError(name + " is already in the lineup.");
                return;
            }
        }

        lineup.add(newArtist);
        artistListView.getItems().add(name + (genre.isEmpty() ? "" : " (" + genre + ")"));
        artistNameField.clear();
        genreField.clear();
    }

    private void removeSelectedArtist() {
        int index = artistListView.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            lineup.remove(index);
            artistListView.getItems().remove(index);
        }
    }

    private Event buildEvent() {
        String name        = nameField.getText().trim();
        String description = descriptionField.getText().trim();
        LocalDate date     = datePicker.getValue();
        String city        = cityField.getText().trim();
        String venueName   = venueNameField.getText().trim();
        String address     = addressField.getText().trim();
        String ticketUrl   = ticketUrlField.getText().trim();

        double price = 0.0;
        try {
            price = Double.parseDouble(priceField.getText().trim());
        } catch (NumberFormatException e) {
            price = 0.0;
        }

        if (name.isEmpty() || date == null || city.isEmpty()) {
            showError("Event name, date, and city are required.");
            return null;
        }

        Venue venue = new Venue(venueName.isEmpty() ? "TBA" : venueName,
                address.isEmpty() ? "" : address,
                city);

        return new Event(name, description, date, city, venue, lineup, price, ticketUrl);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Optional<Event> showAndGetResult() {
        return showAndWait();
    }
}