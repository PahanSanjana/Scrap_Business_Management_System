
package com.mycompany.scrap.management.system;

import com.mycompany.scrap.management.system.model.User;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.stage.Stage;

public class AdminDashboard {

    private final User authenticatedUser;

    public AdminDashboard(User authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    /**
     * Display the Admin Dashboard.
     *
     * @param stage Current JavaFX stage
     */
    public void show(Stage stage) {

        // Main dashboard title
        Label titleLabel = new Label(
                "Scrap Business Management System"
        );

        titleLabel.setStyle(
                "-fx-font-size: 22px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: white;"
        );

        // Admin label
        Label adminLabel = new Label(
                "Administrator Panel"
        );

        adminLabel.setStyle(
                "-fx-font-size: 14px;"
                + "-fx-text-fill: #dbeafe;"
        );

        // User information
        Label userLabel = new Label(
                "Logged in as: "
                        + authenticatedUser.getFullName()
        );

        userLabel.setStyle(
                "-fx-font-size: 13px;"
                + "-fx-text-fill: #e5e7eb;"
        );

        // Logout button
        Button logoutButton = new Button("Logout");

        logoutButton.setPrefWidth(100);
        logoutButton.setPrefHeight(35);

        logoutButton.setStyle(
                "-fx-background-color: #dc2626;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;"
        );

        // Header text container
        VBox headerTextContainer = new VBox(4);

        headerTextContainer.getChildren().addAll(
                titleLabel,
                adminLabel,
                userLabel
        );

        // Header
        HBox header = new HBox();

        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));
        header.setSpacing(20);

        header.setStyle(
                "-fx-background-color: #1e3a8a;"
        );

        HBox.setHgrow(
                headerTextContainer,
                javafx.scene.layout.Priority.ALWAYS
        );

        header.getChildren().addAll(
                headerTextContainer,
                logoutButton
        );

        // Welcome heading
        Label welcomeLabel = new Label(
                "Admin Dashboard"
        );

        welcomeLabel.setStyle(
                "-fx-font-size: 26px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #111827;"
        );

        // Welcome description
        Label descriptionLabel = new Label(
                "Manage all purchasing, inventory, sales, "
                        + "customers, suppliers, and system settings."
        );

        descriptionLabel.setWrapText(true);

        descriptionLabel.setStyle(
                "-fx-font-size: 14px;"
                + "-fx-text-fill: #4b5563;"
        );

        // Dashboard cards
        GridPane dashboardGrid = new GridPane();

        dashboardGrid.setHgap(18);
        dashboardGrid.setVgap(18);
        dashboardGrid.setPadding(new Insets(20, 0, 20, 0));

        Button materialButton = createDashboardButton(
                "Material Management",
                "Manage scrap materials and categories"
        );

        Button purchasingButton = createDashboardButton(
                "Purchasing Management",
                "Record and manage material purchases"
        );

        Button inventoryButton = createDashboardButton(
                "Inventory Management",
                "View and manage available stock"
        );

        Button supplierButton = createDashboardButton(
                "Supplier Management",
                "Manage suppliers and supplier records"
        );

        Button customerButton = createDashboardButton(
                "Customer Management",
                "Manage customers and customer records"
        );

        Button salesButton = createDashboardButton(
                "Sales Management",
                "Record and manage scrap material sales"
        );

        Button reportsButton = createDashboardButton(
                "Reports",
                "View purchasing, sales, and inventory reports"
        );

        Button usersButton = createDashboardButton(
                "User Management",
                "Manage users and system access"
        );

        Button settingsButton = createDashboardButton(
                "Admin Settings",
                "Manage system settings and administration"
        );

        // Add buttons to the dashboard grid
        dashboardGrid.add(materialButton, 0, 0);
        dashboardGrid.add(purchasingButton, 1, 0);

        dashboardGrid.add(inventoryButton, 0, 1);
        dashboardGrid.add(supplierButton, 1, 1);

        dashboardGrid.add(customerButton, 0, 2);
        dashboardGrid.add(salesButton, 1, 2);

        dashboardGrid.add(reportsButton, 0, 3);
        dashboardGrid.add(usersButton, 1, 3);

        dashboardGrid.add(settingsButton, 0, 4);

        // Feature button actions
        materialButton.setOnAction(event -> {
            showInformation(
                    "Material Management",
                    "This feature will be implemented next."
            );
        });

        purchasingButton.setOnAction(event -> {
            showInformation(
                    "Purchasing Management",
                    "This feature will be implemented later."
            );
        });

        inventoryButton.setOnAction(event -> {
            showInformation(
                    "Inventory Management",
                    "This feature will be implemented later."
            );
        });

        supplierButton.setOnAction(event -> {
            showInformation(
                    "Supplier Management",
                    "This feature will be implemented later."
            );
        });

        customerButton.setOnAction(event -> {
            showInformation(
                    "Customer Management",
                    "This feature will be implemented later."
            );
        });

        salesButton.setOnAction(event -> {
            showInformation(
                    "Sales Management",
                    "This feature will be implemented later."
            );
        });

        reportsButton.setOnAction(event -> {
            showInformation(
                    "Reports",
                    "This feature will be implemented later."
            );
        });

        usersButton.setOnAction(event -> {
            showInformation(
                    "User Management",
                    "This feature will be implemented later."
            );
        });

        settingsButton.setOnAction(event -> {
            showInformation(
                    "Admin Settings",
                    "This feature will be implemented later."
            );
        });

        // Main content
        VBox content = new VBox(10);

        content.setPadding(new Insets(30));
        content.setAlignment(Pos.TOP_LEFT);

        content.getChildren().addAll(
                welcomeLabel,
                descriptionLabel,
                new Separator(),
                dashboardGrid
        );

        // Scroll-friendly central container
        BorderPane root = new BorderPane();

        root.setTop(header);
        root.setCenter(content);

        root.setStyle(
                "-fx-background-color: #f8fafc;"
        );

        // Logout action
        logoutButton.setOnAction(event -> {

            LoginScreen loginScreen = new LoginScreen();

            loginScreen.show(stage);
        });

        // Scene
        Scene scene = new Scene(
                root,
                1100,
                750
        );

        // Stage configuration
        stage.setTitle(
                "Scrap Business Management System - Admin Dashboard"
        );

        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    /**
     * Create a dashboard feature button.
     *
     * @param title Button title
     * @param description Button description
     * @return Configured dashboard button
     */
    private Button createDashboardButton(
            String title,
            String description
    ) {

        Button button = new Button();

        Label titleLabel = new Label(title);

        titleLabel.setStyle(
                "-fx-font-size: 15px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #111827;"
        );

        Label descriptionLabel = new Label(description);

        descriptionLabel.setWrapText(true);

        descriptionLabel.setStyle(
                "-fx-font-size: 12px;"
                + "-fx-text-fill: #6b7280;"
        );

        VBox buttonContent = new VBox(6);

        buttonContent.setAlignment(Pos.CENTER_LEFT);

        buttonContent.getChildren().addAll(
                titleLabel,
                descriptionLabel
        );

        button.setGraphic(buttonContent);

        button.setContentDisplay(
                javafx.scene.control.ContentDisplay.GRAPHIC_ONLY
        );

        button.setAlignment(Pos.CENTER_LEFT);

        button.setPrefWidth(340);
        button.setPrefHeight(100);

        button.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #d1d5db;"
                + "-fx-border-radius: 8px;"
                + "-fx-background-radius: 8px;"
                + "-fx-padding: 15px;"
                + "-fx-cursor: hand;"
        );

        return button;
    }

    /**
     * Display an information message.
     *
     * @param title Dialog title
     * @param message Dialog message
     */
    private void showInformation(
            String title,
            String message
    ) {

        javafx.scene.control.Alert alert =
                new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION
                );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}