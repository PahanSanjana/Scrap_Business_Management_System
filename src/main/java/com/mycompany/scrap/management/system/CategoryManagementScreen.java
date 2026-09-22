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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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

    public CategoryManagementScreen(User authenticatedUser) {

        this.authenticatedUser = authenticatedUser;

        this.categoryDAO = new CategoryDAO();

        this.categoryTable = new TableView<>();

        this.categoryNameField = new TextField();

        this.descriptionField = new TextField();

        this.searchField = new TextField();

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

        categoryNameField.setPrefHeight(40);

        categoryNameField.setMaxWidth(
                Double.MAX_VALUE
        );

        descriptionField.setPromptText(
                "Enter category description"
        );

        descriptionField.setPrefHeight(40);

        descriptionField.setMaxWidth(
                Double.MAX_VALUE
        );

        searchField.setPromptText(
                "Search by category name or description"
        );

        searchField.setPrefHeight(40);

        searchField.setMaxWidth(
                Double.MAX_VALUE
        );

        selectedCategoryLabel.setStyle(
                "-fx-text-fill: #475569;"
                + "-fx-font-size: 13px;"
        );
    }

    // =========================================================
    // CONFIGURE BUTTONS
    // =========================================================

    private void configureButtons() {

        addButton.setPrefHeight(40);

        addButton.setPrefWidth(150);

        updateButton.setPrefHeight(40);

        updateButton.setPrefWidth(165);

        clearButton.setPrefHeight(40);

        clearButton.setPrefWidth(100);

        activateButton.setPrefHeight(40);

        activateButton.setPrefWidth(125);

        deactivateButton.setPrefHeight(40);

        deactivateButton.setPrefWidth(135);

        addButton.setStyle(
                "-fx-background-color: #166534;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 6px;"
                + "-fx-cursor: hand;"
        );

        updateButton.setStyle(
                "-fx-background-color: #1d4ed8;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 6px;"
                + "-fx-cursor: hand;"
        );

        clearButton.setStyle(
                "-fx-background-color: #64748b;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 6px;"
                + "-fx-cursor: hand;"
        );

        activateButton.setStyle(
                "-fx-background-color: #15803d;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 6px;"
                + "-fx-cursor: hand;"
        );

        deactivateButton.setStyle(
                "-fx-background-color: #b91c1c;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 6px;"
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

        categoryTable.setMinHeight(300);

        categoryTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        categoryTable.setPlaceholder(
                new Label("No categories found")
        );

        categoryTable.setStyle(
                "-fx-background-color: white;"
                + "-fx-control-inner-background: white;"
                + "-fx-table-cell-border-color: #e2e8f0;"
                + "-fx-border-color: #e2e8f0;"
                + "-fx-border-radius: 6px;"
        );

        // -----------------------------------------------------
        // ID COLUMN
        // -----------------------------------------------------

        TableColumn<Category, Integer> idColumn =
                new TableColumn<>("ID");

        idColumn.setPrefWidth(70);

        idColumn.setMinWidth(60);

        idColumn.setMaxWidth(90);

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

        descriptionColumn.setPrefWidth(350);

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

        statusColumn.setMinWidth(110);

        statusColumn.setMaxWidth(160);

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
                                "-fx-font-size: 13px;"
                                + "-fx-font-weight: bold;"
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
                "-fx-background-color: #f1f5f9;"
        );

        // =====================================================
        // HEADER
        // =====================================================

        HBox header =
                new HBox();

        header.setPadding(
                new Insets(20, 28, 20, 28)
        );

        header.setAlignment(
                Pos.CENTER_LEFT
        );

        header.setSpacing(15);

        header.setStyle(
                "-fx-background-color: #0f172a;"
        );

        VBox headingBox =
                new VBox(4);

        Label titleLabel =
                new Label(
                        "Category Management"
                );

        titleLabel.setStyle(
                "-fx-font-size: 25px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: white;"
        );

        Label subtitleLabel =
                new Label(
                        "Manage scrap material categories"
                );

        subtitleLabel.setStyle(
                "-fx-font-size: 13px;"
                + "-fx-text-fill: #cbd5e1;"
        );

        headingBox.getChildren().addAll(
                titleLabel,
                subtitleLabel
        );

        Region headerSpacer =
                new Region();

        HBox.setHgrow(
                headerSpacer,
                Priority.ALWAYS
        );

        Button backButton =
                new Button(
                        "← Back to Dashboard"
                );

        backButton.setPrefHeight(38);

        backButton.setPrefWidth(180);

        backButton.setStyle(
                "-fx-background-color: #334155;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 6px;"
                + "-fx-cursor: hand;"
        );

        backButton.setOnAction(event -> {

            AdminDashboard dashboard =
                    new AdminDashboard(
                            authenticatedUser
                    );

            dashboard.show(stage);
        });

        header.getChildren().addAll(
                headingBox,
                headerSpacer,
                backButton
        );

        root.setTop(header);

        // =====================================================
        // MAIN CONTENT
        // =====================================================

        VBox mainContent =
                new VBox(18);

        mainContent.setPadding(
                new Insets(25, 28, 28, 28)
        );

        mainContent.setFillWidth(true);

        // =====================================================
        // CATEGORY FORM CARD
        // =====================================================

        VBox formCard =
                new VBox(15);

        formCard.setPadding(
                new Insets(20)
        );

        formCard.setMaxWidth(
                Double.MAX_VALUE
        );

        formCard.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 10px;"
                + "-fx-border-color: #e2e8f0;"
                + "-fx-border-radius: 10px;"
        );

        Label formTitle =
                new Label(
                        "Category Details"
                );

        formTitle.setStyle(
                "-fx-font-size: 17px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #0f172a;"
        );

        Label formSubtitle =
                new Label(
                        "Create a new category or update an existing category"
                );

        formSubtitle.setStyle(
                "-fx-font-size: 12px;"
                + "-fx-text-fill: #64748b;"
        );

        // -----------------------------------------------------
        // FORM GRID
        // -----------------------------------------------------

        GridPane formGrid =
                new GridPane();

        formGrid.setHgap(18);

        formGrid.setVgap(8);

        formGrid.setMaxWidth(
                Double.MAX_VALUE
        );

        ColumnConstraintsHelper.configureFormColumns(
                formGrid
        );

        Label categoryNameLabel =
                new Label(
                        "Category Name"
                );

        categoryNameLabel.setStyle(
                "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #334155;"
        );

        Label descriptionLabel =
                new Label(
                        "Description"
                );

        descriptionLabel.setStyle(
                "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #334155;"
        );

        formGrid.add(
                categoryNameLabel,
                0,
                0
        );

        formGrid.add(
                descriptionLabel,
                1,
                0
        );

        formGrid.add(
                categoryNameField,
                0,
                1
        );

        formGrid.add(
                descriptionField,
                1,
                1
        );

        GridPane.setHgrow(
                categoryNameField,
                Priority.ALWAYS
        );

        GridPane.setHgrow(
                descriptionField,
                Priority.ALWAYS
        );

        // -----------------------------------------------------
        // FORM BUTTONS
        // -----------------------------------------------------

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

        formCard.getChildren().addAll(
                formTitle,
                formSubtitle,
                formGrid,
                formButtonBox,
                selectedCategoryLabel
        );

        // =====================================================
        // SEARCH CARD
        // =====================================================

        VBox searchCard =
                new VBox(12);

        searchCard.setPadding(
                new Insets(20)
        );

        searchCard.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 10px;"
                + "-fx-border-color: #e2e8f0;"
                + "-fx-border-radius: 10px;"
        );

        Label searchTitle =
                new Label(
                        "Search Categories"
                );

        searchTitle.setStyle(
                "-fx-font-size: 17px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #0f172a;"
        );

        Label searchSubtitle =
                new Label(
                        "Search for categories using the name or description"
                );

        searchSubtitle.setStyle(
                "-fx-font-size: 12px;"
                + "-fx-text-fill: #64748b;"
        );

        HBox searchControls =
                new HBox(12);

        searchControls.setAlignment(
                Pos.CENTER_LEFT
        );

        HBox.setHgrow(
                searchField,
                Priority.ALWAYS
        );

        Button showAllButton =
                new Button(
                        "Show All"
                );

        showAllButton.setPrefHeight(40);

        showAllButton.setPrefWidth(110);

        showAllButton.setStyle(
                "-fx-background-color: #334155;"
                + "-fx-text-fill: white;"
                + "-fx-font-size: 13px;"
                + "-fx-font-weight: bold;"
                + "-fx-background-radius: 6px;"
                + "-fx-cursor: hand;"
        );

        showAllButton.setOnAction(event -> {

            searchField.clear();

            loadAllCategories();
        });

        searchControls.getChildren().addAll(
                searchField,
                showAllButton
        );

        searchCard.getChildren().addAll(
                searchTitle,
                searchSubtitle,
                searchControls
        );

        // =====================================================
        // STATUS CARD
        // =====================================================

        VBox statusCard =
                new VBox(12);

        statusCard.setPadding(
                new Insets(20)
        );

        statusCard.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 10px;"
                + "-fx-border-color: #e2e8f0;"
                + "-fx-border-radius: 10px;"
        );

        Label statusTitle =
                new Label(
                        "Category Status"
                );

        statusTitle.setStyle(
                "-fx-font-size: 17px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #0f172a;"
        );

        Label statusSubtitle =
                new Label(
                        "Select a category from the table before changing its status"
                );

        statusSubtitle.setStyle(
                "-fx-font-size: 12px;"
                + "-fx-text-fill: #64748b;"
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

        statusCard.getChildren().addAll(
                statusTitle,
                statusSubtitle,
                statusButtons
        );

        // =====================================================
        // TABLE CARD
        // =====================================================

        VBox tableCard =
                new VBox(12);

        tableCard.setPadding(
                new Insets(20)
        );

        tableCard.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 10px;"
                + "-fx-border-color: #e2e8f0;"
                + "-fx-border-radius: 10px;"
        );

        Label tableTitle =
                new Label(
                        "Category List"
                );

        tableTitle.setStyle(
                "-fx-font-size: 17px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #0f172a;"
        );

        Label tableSubtitle =
                new Label(
                        "Select a row to view or edit category information"
                );

        tableSubtitle.setStyle(
                "-fx-font-size: 12px;"
                + "-fx-text-fill: #64748b;"
        );

        VBox.setVgrow(
                categoryTable,
                Priority.ALWAYS
        );

        tableCard.getChildren().addAll(
                tableTitle,
                tableSubtitle,
                categoryTable
        );

        // =====================================================
        // ADD ALL CONTENT
        // =====================================================

        mainContent.getChildren().addAll(
                formCard,
                searchCard,
                statusCard,
                tableCard
        );

        VBox.setVgrow(
                tableCard,
                Priority.ALWAYS
        );

        // =====================================================
        // SCROLL PANE
        // =====================================================

        ScrollPane scrollPane =
                new ScrollPane();

        scrollPane.setContent(
                mainContent
        );

        scrollPane.setFitToWidth(true);

        scrollPane.setFitToHeight(false);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                "-fx-background-color: #f1f5f9;"
                + "-fx-border-color: transparent;"
        );

        root.setCenter(
                scrollPane
        );

        // =====================================================
        // SCENE
        // =====================================================

        Scene scene =
                new Scene(
                        root,
                        1150,
                        800
                );

        stage.setTitle(
                "Category Management - Scrap Business System"
        );

        stage.setScene(
                scene
        );

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

    // =========================================================
    // FORM GRID COLUMN HELPER
    // =========================================================

    private static class ColumnConstraintsHelper {

        private static void configureFormColumns(
                GridPane grid
        ) {

            javafx.scene.layout.ColumnConstraints firstColumn =
                    new javafx.scene.layout.ColumnConstraints();

            javafx.scene.layout.ColumnConstraints secondColumn =
                    new javafx.scene.layout.ColumnConstraints();

            firstColumn.setPercentWidth(50);

            secondColumn.setPercentWidth(50);

            firstColumn.setHgrow(
                    Priority.ALWAYS
            );

            secondColumn.setHgrow(
                    Priority.ALWAYS
            );

            grid.getColumnConstraints().addAll(
                    firstColumn,
                    secondColumn
            );
        }
    }
}