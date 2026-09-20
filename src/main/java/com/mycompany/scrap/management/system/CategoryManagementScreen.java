
package com.mycompany.scrap.management.system;

import com.mycompany.scrap.management.system.dao.CategoryDAO;
import com.mycompany.scrap.management.system.model.Category;
import com.mycompany.scrap.management.system.model.User;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;

import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;

public class CategoryManagementScreen {

    // =========================================================
    // FIELDS
    // =========================================================

    private final User authenticatedUser;

    private final CategoryDAO categoryDAO;

    private final TableView<Category> categoryTable;

    private final TextField categoryNameField;

    private final TextField descriptionField;

    private final TextField searchField;

    private final Label selectedCategoryLabel;

    private final Button addButton;

    private final Button updateButton;

    private final Button clearButton;

    private final Button activateButton;

    private final Button deactivateButton;

    private Stage stage;

    private int selectedCategoryId = -1;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public CategoryManagementScreen(
            User authenticatedUser
    ) {

        this.authenticatedUser =
                authenticatedUser;

        this.categoryDAO =
                new CategoryDAO();

        this.categoryTable =
                new TableView<>();

        this.categoryNameField =
                new TextField();

        this.descriptionField =
                new TextField();

        this.searchField =
                new TextField();

        this.selectedCategoryLabel =
                new Label("No category selected");

        this.addButton =
                new Button("Add Category");

        this.updateButton =
                new Button("Update Category");

        this.clearButton =
                new Button("Clear");

        this.activateButton =
                new Button("Activate");

        this.deactivateButton =
                new Button("Deactivate");

        configureFields();

        configureButtons();

        configureTable();
    }

    // =========================================================
    // CONFIGURE TEXT FIELDS
    // =========================================================

    private void configureFields() {

        categoryNameField.setPromptText(
                "Enter category name"
        );

        categoryNameField.setPrefHeight(38);

        categoryNameField.setPrefWidth(280);

        descriptionField.setPromptText(
                "Enter category description"
        );

        descriptionField.setPrefHeight(38);

        descriptionField.setPrefWidth(280);

        searchField.setPromptText(
                "Search category..."
        );

        searchField.setPrefHeight(38);

        searchField.setPrefWidth(280);

        selectedCategoryLabel.setStyle(
                "-fx-text-fill: #374151;"
                + "-fx-font-size: 13px;"
        );
    }

    // =========================================================
    // CONFIGURE BUTTONS
    // =========================================================

    private void configureButtons() {

        addButton.setPrefHeight(38);

        addButton.setPrefWidth(150);

        updateButton.setPrefHeight(38);

        updateButton.setPrefWidth(160);

        clearButton.setPrefHeight(38);

        clearButton.setPrefWidth(100);

        activateButton.setPrefHeight(38);

        activateButton.setPrefWidth(110);

        deactivateButton.setPrefHeight(38);

        deactivateButton.setPrefWidth(120);

        addButton.setStyle(
                "-fx-background-color: #166534;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;"
        );

        updateButton.setStyle(
                "-fx-background-color: #1d4ed8;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;"
        );

        clearButton.setStyle(
                "-fx-background-color: #6b7280;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;"
        );

        activateButton.setStyle(
                "-fx-background-color: #15803d;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;"
        );

        deactivateButton.setStyle(
                "-fx-background-color: #b91c1c;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;"
        );

        addButton.setOnAction(
                event -> addCategory()
        );

        updateButton.setOnAction(
                event -> updateCategory()
        );

        clearButton.setOnAction(
                event -> clearForm()
        );

        activateButton.setOnAction(
                event -> changeCategoryStatus(true)
        );

        deactivateButton.setOnAction(
                event -> changeCategoryStatus(false)
        );

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    String keyword =
                            newValue == null
                                    ? ""
                                    : newValue.trim();

                    if (keyword.isEmpty()) {

                        loadAllCategories();

                    } else {

                        searchCategories(keyword);
                    }
                }
        );
    }

    // =========================================================
    // CONFIGURE TABLE
    // =========================================================

    private void configureTable() {

        categoryTable.setPrefHeight(430);

        categoryTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        categoryTable.setPlaceholder(
                new Label("No categories found")
        );

        categoryTable.setStyle(
                "-fx-background-color: white;"
                + "-fx-control-inner-background: white;"
                + "-fx-table-cell-border-color: #e5e7eb;"
        );

        // -----------------------------------------------------
        // ID COLUMN
        // -----------------------------------------------------

        TableColumn<Category, Integer> idColumn =
                new TableColumn<>("ID");

        idColumn.setPrefWidth(70);

        idColumn.setCellValueFactory(
                cellData ->
                        new SimpleIntegerProperty(
                                cellData.getValue().getId()
                        ).asObject()
        );

        idColumn.setCellFactory(
                column -> new TableCell<Category, Integer>() {

                    @Override
                    protected void updateItem(
                            Integer item,
                            boolean empty
                    ) {

                        super.updateItem(item, empty);

                        if (empty || item == null) {

                            setText(null);

                        } else {

                            setText(
                                    String.valueOf(item)
                            );
                        }

                        setTextFill(
                                Color.web("#111827")
                        );

                        setStyle(
                                "-fx-alignment: CENTER;"
                                + "-fx-font-size: 13px;"
                        );
                    }
                }
        );

        // -----------------------------------------------------
        // CATEGORY NAME COLUMN
        // -----------------------------------------------------

        TableColumn<Category, String> nameColumn =
                new TableColumn<>("Category Name");

        nameColumn.setPrefWidth(220);

        nameColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                safeString(
                                        cellData.getValue()
                                                .getCategoryName()
                                )
                        )
        );

        nameColumn.setCellFactory(
                column -> new TableCell<Category, String>() {

                    @Override
                    protected void updateItem(
                            String item,
                            boolean empty
                    ) {

                        super.updateItem(item, empty);

                        setText(
                                empty ? null : item
                        );

                        setTextFill(
                                Color.web("#111827")
                        );

                        setStyle(
                                "-fx-font-size: 13px;"
                                + "-fx-alignment: CENTER-LEFT;"
                        );
                    }
                }
        );

        // -----------------------------------------------------
        // DESCRIPTION COLUMN
        // -----------------------------------------------------

        TableColumn<Category, String> descriptionColumn =
                new TableColumn<>("Description");

        descriptionColumn.setPrefWidth(300);

        descriptionColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                safeString(
                                        cellData.getValue()
                                                .getDescription()
                                )
                        )
        );

        descriptionColumn.setCellFactory(
                column -> new TableCell<Category, String>() {

                    @Override
                    protected void updateItem(
                            String item,
                            boolean empty
                    ) {

                        super.updateItem(item, empty);

                        setText(
                                empty ? null : item
                        );

                        setTextFill(
                                Color.web("#111827")
                        );

                        setStyle(
                                "-fx-font-size: 13px;"
                                + "-fx-alignment: CENTER-LEFT;"
                        );
                    }
                }
        );

        // -----------------------------------------------------
        // STATUS COLUMN
        // -----------------------------------------------------

        TableColumn<Category, String> statusColumn =
                new TableColumn<>("Status");

        statusColumn.setPrefWidth(130);

        statusColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData.getValue().isActive()
                                        ? "Active"
                                        : "Inactive"
                        )
        );

        statusColumn.setCellFactory(
                column -> new TableCell<Category, String>() {

                    @Override
                    protected void updateItem(
                            String item,
                            boolean empty
                    ) {

                        super.updateItem(item, empty);

                        if (empty || item == null) {

                            setText(null);

                        } else {

                            setText(item);

                            if (item.equals("Active")) {

                                setTextFill(
                                        Color.web("#15803d")
                                );

                            } else {

                                setTextFill(
                                        Color.web("#b91c1c")
                                );
                            }
                        }

                        setStyle(
                                "-fx-font-weight: bold;"
                                + "-fx-alignment: CENTER;"
                        );
                    }
                }
        );

        categoryTable.getColumns().clear();

        categoryTable.getColumns().addAll(
                idColumn,
                nameColumn,
                descriptionColumn,
                statusColumn
        );

        // -----------------------------------------------------
        // TABLE ROW SELECTION
        // -----------------------------------------------------

        categoryTable.setRowFactory(
                tableView -> {

                    TableRow<Category> row =
                            new TableRow<>();

                    row.setOnMouseClicked(event -> {

                        if (event.getClickCount() == 1
                                && !row.isEmpty()) {

                            Category selectedCategory =
                                    row.getItem();

                            populateForm(
                                    selectedCategory
                            );
                        }
                    });

                    return row;
                }
        );
    }

    // =========================================================
    // SHOW SCREEN
    // =========================================================

    public void show(Stage stage) {

        this.stage = stage;

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color: #f3f4f6;"
        );

        VBox content =
                new VBox(15);

        content.setPadding(
                new Insets(25)
        );

        content.setAlignment(
                Pos.TOP_LEFT
        );

        // -----------------------------------------------------
        // BACK BUTTON
        // -----------------------------------------------------

        Button backButton =
                new Button(
                        "← Back to Admin Dashboard"
                );

        backButton.setPrefHeight(38);

        backButton.setPrefWidth(230);

        backButton.setStyle(
                "-fx-background-color: #1e3a8a;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;"
        );

        backButton.setOnAction(event -> {

            AdminDashboard dashboard =
                    new AdminDashboard(
                            authenticatedUser
                    );

            dashboard.show(stage);
        });

        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        Label titleLabel =
                new Label(
                        "Category Management"
                );

        titleLabel.setStyle(
                "-fx-font-size: 26px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #111827;"
        );

        Label subtitleLabel =
                new Label(
                        "Add, update, search, and manage scrap material categories"
                );

        subtitleLabel.setStyle(
                "-fx-font-size: 14px;"
                + "-fx-text-fill: #6b7280;"
        );

        // -----------------------------------------------------
        // CATEGORY FORM
        // -----------------------------------------------------

        VBox formBox =
                new VBox(8);

        formBox.setPadding(
                new Insets(18)
        );

        formBox.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 8;"
                + "-fx-border-color: #e5e7eb;"
                + "-fx-border-radius: 8;"
        );

        Label categoryNameLabel =
                new Label(
                        "Category Name"
                );

        categoryNameLabel.setStyle(
                "-fx-font-weight: bold;"
                + "-fx-text-fill: #374151;"
        );

        Label descriptionLabel =
                new Label(
                        "Description"
                );

        descriptionLabel.setStyle(
                "-fx-font-weight: bold;"
                + "-fx-text-fill: #374151;"
        );

        HBox nameBox =
                new HBox(10);

        nameBox.setAlignment(
                Pos.CENTER_LEFT
        );

        nameBox.getChildren().addAll(
                categoryNameLabel,
                categoryNameField
        );

        HBox descriptionBox =
                new HBox(10);

        descriptionBox.setAlignment(
                Pos.CENTER_LEFT
        );

        descriptionBox.getChildren().addAll(
                descriptionLabel,
                descriptionField
        );

        HBox formButtonBox =
                new HBox(10);

        formButtonBox.setAlignment(
                Pos.CENTER_LEFT
        );

        formButtonBox.getChildren().addAll(
                addButton,
                updateButton,
                clearButton
        );

        formBox.getChildren().addAll(
                nameBox,
                descriptionBox,
                formButtonBox,
                selectedCategoryLabel
        );

        // -----------------------------------------------------
        // SEARCH SECTION
        // -----------------------------------------------------

        VBox searchBox =
                new VBox(8);

        Label searchLabel =
                new Label(
                        "Search Categories"
                );

        searchLabel.setStyle(
                "-fx-font-weight: bold;"
                + "-fx-text-fill: #374151;"
        );

        Button showAllButton =
                new Button(
                        "Show All"
                );

        showAllButton.setPrefHeight(38);

        showAllButton.setPrefWidth(110);

        showAllButton.setStyle(
                "-fx-background-color: #374151;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-cursor: hand;"
        );

        showAllButton.setOnAction(event -> {

            searchField.clear();

            loadAllCategories();
        });

        HBox searchControls =
                new HBox(10);

        searchControls.setAlignment(
                Pos.CENTER_LEFT
        );

        searchControls.getChildren().addAll(
                searchField,
                showAllButton
        );

        searchBox.getChildren().addAll(
                searchLabel,
                searchControls
        );

        // -----------------------------------------------------
        // STATUS SECTION
        // -----------------------------------------------------

        VBox statusBox =
                new VBox(8);

        Label statusLabel =
                new Label(
                        "Category Status"
                );

        statusLabel.setStyle(
                "-fx-font-weight: bold;"
                + "-fx-text-fill: #374151;"
        );

        HBox statusButtons =
                new HBox(10);

        statusButtons.setAlignment(
                Pos.CENTER_LEFT
        );

        statusButtons.getChildren().addAll(
                activateButton,
                deactivateButton
        );

        statusBox.getChildren().addAll(
                statusLabel,
                statusButtons
        );

        // -----------------------------------------------------
        // ADD CONTENT TO SCREEN
        // -----------------------------------------------------

        content.getChildren().addAll(
                backButton,
                titleLabel,
                subtitleLabel,
                formBox,
                searchBox,
                statusBox,
                categoryTable
        );

        root.setCenter(content);

        Scene scene =
                new Scene(
                        root,
                        1050,
                        720
                );

        stage.setTitle(
                "Category Management - Scrap Business System"
        );

        stage.setScene(scene);

        stage.setResizable(true);

        stage.show();

        loadAllCategories();
    }

    // =========================================================
    // ADD CATEGORY
    // =========================================================

    private void addCategory() {

        String categoryName =
                categoryNameField.getText().trim();

        String description =
                descriptionField.getText().trim();

        if (categoryName.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Please enter a category name."
            );

            return;
        }

        if (categoryName.length() < 2) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Category name must contain at least 2 characters."
            );

            return;
        }

        Category category =
                new Category();

        category.setCategoryName(
                categoryName
        );

        category.setDescription(
                description.isEmpty()
                        ? null
                        : description
        );

        category.setActive(true);

        boolean created =
                categoryDAO.createCategory(
                        category
                );

        if (created) {

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Category added successfully."
            );

            clearForm();

            loadAllCategories();

        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Unable to add category. "
                    + "The category name may already exist."
            );
        }
    }

    // =========================================================
    // UPDATE CATEGORY
    // =========================================================

    private void updateCategory() {

        if (selectedCategoryId == -1) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "No Selection",
                    "Please select a category from the table first."
            );

            return;
        }

        String categoryName =
                categoryNameField.getText().trim();

        String description =
                descriptionField.getText().trim();

        if (categoryName.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Please enter a category name."
            );

            return;
        }

        Category selectedCategory =
                categoryTable.getSelectionModel()
                        .getSelectedItem();

        Category category =
                new Category();

        category.setId(
                selectedCategoryId
        );

        category.setCategoryName(
                categoryName
        );

        category.setDescription(
                description.isEmpty()
                        ? null
                        : description
        );

        if (selectedCategory != null) {

            category.setActive(
                    selectedCategory.isActive()
            );

        } else {

            category.setActive(true);
        }

        boolean updated =
                categoryDAO.updateCategory(
                        category
                );

        if (updated) {

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Category updated successfully."
            );

            clearForm();

            loadAllCategories();

        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Unable to update category."
            );
        }
    }

    // =========================================================
    // ACTIVATE / DEACTIVATE CATEGORY
    // =========================================================

    private void changeCategoryStatus(
            boolean active
    ) {

        if (selectedCategoryId == -1) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "No Selection",
                    "Please select a category from the table first."
            );

            return;
        }

        String statusText =
                active
                        ? "activate"
                        : "deactivate";

        Alert confirmation =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmation.setTitle(
                "Confirm Action"
        );

        confirmation.setHeaderText(
                null
        );

        confirmation.setContentText(
                "Are you sure you want to "
                + statusText
                + " this category?"
        );

        Optional<ButtonType> result =
                confirmation.showAndWait();

        if (result.isEmpty()
                || result.get() != ButtonType.OK) {

            return;
        }

        boolean updated =
                categoryDAO.updateCategoryStatus(
                        selectedCategoryId,
                        active
                );

        if (updated) {

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Category status updated successfully."
            );

            clearForm();

            loadAllCategories();

        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Unable to update category status."
            );
        }
    }

    // =========================================================
    // LOAD ALL CATEGORIES
    // =========================================================

    private void loadAllCategories() {

        List<Category> categories =
                categoryDAO.getAllCategories();

        ObservableList<Category> categoryList =
                FXCollections.observableArrayList(
                        categories
                );

        categoryTable.setItems(
                categoryList
        );
    }

    // =========================================================
    // SEARCH CATEGORIES
    // =========================================================

    private void searchCategories(
            String keyword
    ) {

        List<Category> categories =
                categoryDAO.searchCategories(
                        keyword
                );

        ObservableList<Category> categoryList =
                FXCollections.observableArrayList(
                        categories
                );

        categoryTable.setItems(
                categoryList
        );
    }

    // =========================================================
    // POPULATE FORM WHEN ROW IS SELECTED
    // =========================================================

    private void populateForm(
            Category category
    ) {

        if (category == null) {

            return;
        }

        selectedCategoryId =
                category.getId();

        categoryNameField.setText(
                safeString(
                        category.getCategoryName()
                )
        );

        descriptionField.setText(
                safeString(
                        category.getDescription()
                )
        );

        selectedCategoryLabel.setText(
                "Selected Category ID: "
                + selectedCategoryId
                + " | Status: "
                + (
                        category.isActive()
                                ? "Active"
                                : "Inactive"
                )
        );

        categoryTable.getSelectionModel()
                .select(category);
    }

    // =========================================================
    // CLEAR FORM
    // =========================================================

    private void clearForm() {

        selectedCategoryId = -1;

        categoryNameField.clear();

        descriptionField.clear();

        selectedCategoryLabel.setText(
                "No category selected"
        );

        categoryTable.getSelectionModel()
                .clearSelection();
    }

    // =========================================================
    // HANDLE NULL VALUES
    // =========================================================

    private String safeString(
            String value
    ) {

        return value == null
                ? ""
                : value;
    }

    // =========================================================
    // SHOW ALERT
    // =========================================================

    private void showAlert(
            Alert.AlertType alertType,
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        alertType
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message
        );

        alert.showAndWait();
    }
}