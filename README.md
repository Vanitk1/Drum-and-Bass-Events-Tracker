**Drum and Bass Events Tracker**

A Drum and Bass–focused events platform that helps users discover, track, and explore DnB events in their area. Features include event search, filtering, and detailed listings, with plans for live data integration and user interaction.

**Features**

- View all DnB events in a sortable table
- Search events by city, artist, date, and venue
- Add new events manually via a form dialog
- Remove events from the tracker
- Fetch live events directly from the Skiddle API
- Data persists between sessions using JSON file storage
- Sort events chronologically

**Prerequisites**

- Java 17 or higher
- Maven
- IntelliJ IDEA
- A free Skiddle API key

**Setup**

Clone the repository:

bash git clone https://github.com/yourusername/drum-and-bass-events-tracker.git

Create src/main/resources/config.properties and add your API key:

skiddle.api.key=YOUR_KEY_HERE

Open the project in IntelliJ and reload Maven dependencies.

Add the following VM options to your run configuration:

--module-path "path/to/javafx/jars" --add-modules javafx.controls,javafx.fxml

Run com.app.ui.MainApp.


