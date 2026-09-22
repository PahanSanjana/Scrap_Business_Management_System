package com.mycompany.scrap.management.system;

import com.mycompany.scrap.management.system.database.DatabaseInitializer;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application class for the Scrap Business Management System.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Initialize the SQLite database and create required tables
        DatabaseInitializer.initializeDatabase();

        // Create and display the login screen
        LoginScreen loginScreen = new LoginScreen();

        loginScreen.show(stage);
    }

    public static void main(String[] args) {

        launch(args);
    }
}