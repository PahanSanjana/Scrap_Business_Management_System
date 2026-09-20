
package com.mycompany.scrap.management.system;

import com.mycompany.scrap.management.system.model.User;
import com.mycompany.scrap.management.system.security.AuthenticationService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.scene.paint.Color;

import javafx.stage.Stage;

public class LoginScreen {

    private final AuthenticationService authenticationService;

    private TextField usernameField;
    private PasswordField passwordField;
    private Label messageLabel;

    public LoginScreen() {

        this.authenticationService =
                new AuthenticationService();
    }

    /**
     * Display the login screen.
     *
     * @param stage Main JavaFX stage
     */
    public void show(Stage stage) {

        // Main title
        Label titleLabel = new Label(
                "Scrap Business Management System"
        );

        titleLabel.setStyle(
                "-fx-font-size: 24px;"
                + "-fx-font-weight: bold;"
        );

        // Subtitle
        Label subtitleLabel = new Label(
                "Please sign in to continue"
        );

        subtitleLabel.setStyle(
                "-fx-font-size: 14px;"
                + "-fx-text-fill: #666666;"
        );

        // Username label
        Label usernameLabel = new Label("Username");

        usernameLabel.setStyle(
                "-fx-font-weight: bold;"
        );

        // Username input
        usernameField = new TextField();

        usernameField.setPromptText(
                "Enter your username"
        );

        usernameField.setPrefHeight(40);
        usernameField.setMaxWidth(350);

        // Password label
        Label passwordLabel = new Label("Password");

        passwordLabel.setStyle(
                "-fx-font-weight: bold;"
        );

        // Password input
        passwordField = new PasswordField();

        passwordField.setPromptText(
                "Enter your password"
        );

        passwordField.setPrefHeight(40);
        passwordField.setMaxWidth(350);

        // Login button
        Button loginButton = new Button("Login");

        loginButton.setPrefWidth(160);
        loginButton.setPrefHeight(40);

        loginButton.setStyle(
                "-fx-background-color: #2563eb;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;"
        );

        // Clear button
        Button clearButton = new Button("Clear");

        clearButton.setPrefWidth(160);
        clearButton.setPrefHeight(40);

        clearButton.setStyle(
                "-fx-background-color: #e5e7eb;"
                + "-fx-text-fill: #111827;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;"
        );

        // Message label
        messageLabel = new Label();

        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(350);

        // Login button action
        loginButton.setOnAction(event -> {

            handleLogin(stage);
        });

        // Clear button action
        clearButton.setOnAction(event -> {

            usernameField.clear();
            passwordField.clear();

            messageLabel.setText("");
            messageLabel.setTextFill(Color.BLACK);

            usernameField.requestFocus();
        });

        // Allow pressing Enter in the password field
        passwordField.setOnAction(event -> {

            handleLogin(stage);
        });

        // Button container
        HBox buttonContainer = new HBox(10);

        buttonContainer.setAlignment(Pos.CENTER);

        buttonContainer.getChildren().addAll(
                loginButton,
                clearButton
        );

        // Main layout
        VBox mainLayout = new VBox(12);

        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(35));
        mainLayout.setMaxWidth(450);

        mainLayout.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                usernameLabel,
                usernameField,
                passwordLabel,
                passwordField,
                buttonContainer,
                messageLabel
        );

        // Scene
        Scene scene = new Scene(
                mainLayout,
                600,
                500
        );

        // Stage configuration
        stage.setTitle(
                "Scrap Business Management System - Login"
        );

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        usernameField.requestFocus();
    }

    /**
     * Handle the login process.
     *
     * @param stage Current JavaFX stage
     */
    private void handleLogin(Stage stage) {

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Validate empty fields
        if (username.isBlank()) {

            showErrorMessage(
                    "Please enter your username."
            );

            usernameField.requestFocus();

            return;
        }

        if (password.isBlank()) {

            showErrorMessage(
                    "Please enter your password."
            );

            passwordField.requestFocus();

            return;
        }

        // Authenticate the user
        User authenticatedUser =
                authenticationService.authenticate(
                        username,
                        password
                );

        if (authenticatedUser != null) {

    /*
     * Admin role ID is 1.
     *
     * Currently, only the Admin dashboard
     * is enabled in this project.
     */
    if (authenticatedUser.getRoleId() == 1) {

        AdminDashboard adminDashboard =
                new AdminDashboard(authenticatedUser);

        adminDashboard.show(stage);

    } else {

        showErrorMessage(
                "Only Admin access is enabled currently."
        );

        passwordField.clear();
        passwordField.requestFocus();
    }

} else {

            showErrorMessage(
                    "Invalid username or password."
            );

            passwordField.clear();
            passwordField.requestFocus();
        }
    }

    /**
     * Display an error message.
     *
     * @param message Error message
     */
    private void showErrorMessage(String message) {

        messageLabel.setText(message);
        messageLabel.setTextFill(Color.RED);
    }

    /**
     * Display a success message.
     *
     * @param message Success message
     */
    private void showSuccessMessage(String message) {

        messageLabel.setText(message);
        messageLabel.setTextFill(Color.GREEN);
    }
}