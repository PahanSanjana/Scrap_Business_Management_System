package com.mycompany.scrap.management.system;

import com.mycompany.scrap.management.system.dao.SupplierDAO;
import com.mycompany.scrap.management.system.model.Supplier;
import com.mycompany.scrap.management.system.model.User;

import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;


/**
 * Supplier Management Screen.
 *
 * Handles supplier creation, updating, searching,
 * activation, and deactivation.
 */
public class SupplierManagementScreen {

    // =========================================================
    // FIELDS
    // =========================================================

    private final Stage stage;
    
    private final User authenticatedUser;

    private final SupplierDAO supplierDAO;

    private final TableView<Supplier> supplierTable;

    private final ObservableList<Supplier> supplierList;

    private final TextField supplierCodeField;

    private final TextField supplierNameField;

    private final TextField contactPersonField;

    private final TextField phoneNumberField;

    private final TextField emailField;

    private final TextArea addressField;

    private final ComboBox<String> paymentTermsComboBox;

    private final TextField searchField;

    private Supplier selectedSupplier;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

 public SupplierManagementScreen(
        Stage stage,
        User authenticatedUser
) {

    this.stage = stage;
    this.authenticatedUser = authenticatedUser;

    this.supplierDAO = new SupplierDAO();

        this.supplierTable =
                new TableView<>();

        this.supplierList =
                FXCollections.observableArrayList();

        this.supplierCodeField =
                new TextField();

        this.supplierNameField =
                new TextField();

        this.contactPersonField =
                new TextField();

        this.phoneNumberField =
                new TextField();

        this.emailField =
                new TextField();

        this.addressField =
                new TextArea();

        this.paymentTermsComboBox =
                new ComboBox<>();

        this.searchField =
                new TextField();

        this.selectedSupplier =
                null;

        configureFields();

        loadSuppliers();
    }

    // =========================================================
    // SHOW SCREEN
    // =========================================================

    public void show() {

        BorderPane root =
                new BorderPane();

        root.setStyle(
                "-fx-background-color: #f4f6f8;"
        );

        VBox mainLayout =
                new VBox(15);

        mainLayout.setPadding(
                new Insets(20)
        );

        mainLayout.getChildren().addAll(
                createHeader(),
                createSupplierForm(),
                createSearchSection(),
                createTableSection()
        );

        VBox.setVgrow(
                mainLayout,
                Priority.ALWAYS
        );

        ScrollPane scrollPane =
                new ScrollPane(mainLayout);

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background-color: transparent;"
        );

        root.setCenter(scrollPane);

        Scene scene =
                new Scene(
                        root,
                        1350,
                        850
                );

        stage.setTitle(
                "Supplier Management"
        );

        stage.setScene(scene);

        stage.show();
    }

    // =========================================================
    // CONFIGURE FIELDS
    // =========================================================

    private void configureFields() {

        supplierCodeField.setPromptText(
                "Enter supplier code"
        );

        supplierNameField.setPromptText(
                "Enter supplier name"
        );

        contactPersonField.setPromptText(
                "Enter contact person"
        );

        phoneNumberField.setPromptText(
                "Enter phone number"
        );

        emailField.setPromptText(
                "Enter email address"
        );

        addressField.setPromptText(
                "Enter supplier address"
        );

        addressField.setWrapText(true);

        addressField.setPrefRowCount(3);

        paymentTermsComboBox.getItems().addAll(
                "Cash",
                "Credit",
                "7 Days",
                "15 Days",
                "30 Days",
                "60 Days",
                "Other"
        );

        paymentTermsComboBox.setPromptText(
                "Select payment terms"
        );

        paymentTermsComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        applyFieldStyle(
                supplierCodeField
        );

        applyFieldStyle(
                supplierNameField
        );

        applyFieldStyle(
                contactPersonField
        );

        applyFieldStyle(
                phoneNumberField
        );

        applyFieldStyle(
                emailField
        );

        applyFieldStyle(
                addressField
        );

        applyFieldStyle(
                paymentTermsComboBox
        );
    }

    // =========================================================
    // HEADER
    // =========================================================

    private VBox createHeader() {

        Label title =
                new Label(
                        "Supplier Management"
                );

        title.setStyle(
                "-fx-font-size: 28px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #1f2937;"
        );

        Label subtitle =
                new Label(
                        "Create, update, search, and manage suppliers"
                );

        subtitle.setStyle(
                "-fx-font-size: 14px;"
                + "-fx-text-fill: #6b7280;"
        );

        Button backButton =
                new Button(
                        "Back to Dashboard"
                );

        backButton.setStyle(
                "-fx-background-color: #374151;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-padding: 9 16 9 16;"
        );

        backButton.setOnAction(
                event -> goBackToDashboard()
        );

        HBox headerButtons =
                new HBox(
                        10,
                        backButton
                );

        headerButtons.setAlignment(
                Pos.CENTER_RIGHT
        );

        BorderPane header =
                new BorderPane();

        VBox titleBox =
                new VBox(
                        5,
                        title,
                        subtitle
                );

        header.setLeft(titleBox);

        header.setRight(headerButtons);

        VBox container =
                new VBox(
                        header
                );

        container.setPadding(
                new Insets(
                        0,
                        0,
                        5,
                        0
                )
        );

        return container;
    }

    // =========================================================
    // SUPPLIER FORM
    // =========================================================

    private VBox createSupplierForm() {

        Label formTitle =
                new Label(
                        "Supplier Details"
                );

        formTitle.setStyle(
                "-fx-font-size: 19px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #111827;"
        );

        GridPane formGrid =
                new GridPane();

        formGrid.setHgap(15);

        formGrid.setVgap(12);

        formGrid.setPadding(
                new Insets(15)
        );

        ColumnConstraints firstColumn =
                new ColumnConstraints();

        firstColumn.setPercentWidth(25);

        ColumnConstraints secondColumn =
                new ColumnConstraints();

        secondColumn.setPercentWidth(25);

        ColumnConstraints thirdColumn =
                new ColumnConstraints();

        thirdColumn.setPercentWidth(25);

        ColumnConstraints fourthColumn =
                new ColumnConstraints();

        fourthColumn.setPercentWidth(25);

        formGrid.getColumnConstraints().addAll(
                firstColumn,
                secondColumn,
                thirdColumn,
                fourthColumn
        );

        Label supplierCodeLabel =
                createFieldLabel(
                        "Supplier Code *"
                );

        Label supplierNameLabel =
                createFieldLabel(
                        "Supplier Name *"
                );

        Label contactPersonLabel =
                createFieldLabel(
                        "Contact Person"
                );

        Label phoneNumberLabel =
                createFieldLabel(
                        "Phone Number *"
                );

        Label emailLabel =
                createFieldLabel(
                        "Email"
                );

        Label paymentTermsLabel =
                createFieldLabel(
                        "Payment Terms"
                );

        Label addressLabel =
                createFieldLabel(
                        "Address"
                );

        formGrid.add(
                supplierCodeLabel,
                0,
                0
        );

        formGrid.add(
                supplierCodeField,
                0,
                1
        );

        GridPane.setColumnSpan(
                supplierCodeField,
                1
        );

        formGrid.add(
                supplierNameLabel,
                1,
                0
        );

        formGrid.add(
                supplierNameField,
                1,
                1
        );

        formGrid.add(
                contactPersonLabel,
                2,
                0
        );

        formGrid.add(
                contactPersonField,
                2,
                1
        );

        formGrid.add(
                phoneNumberLabel,
                3,
                0
        );

        formGrid.add(
                phoneNumberField,
                3,
                1
        );

        formGrid.add(
                emailLabel,
                0,
                2
        );

        formGrid.add(
                emailField,
                0,
                3
        );

        formGrid.add(
                paymentTermsLabel,
                1,
                2
        );

        formGrid.add(
                paymentTermsComboBox,
                1,
                3
        );

        formGrid.add(
                addressLabel,
                2,
                2
        );

        formGrid.add(
                addressField,
                2,
                3
        );

        GridPane.setColumnSpan(
                addressField,
                2
        );

        HBox buttons =
                createFormButtons();

        VBox card =
                new VBox(
                        10,
                        formTitle,
                        formGrid,
                        buttons
                );

        card.setPadding(
                new Insets(15)
        );

        card.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 10;"
                + "-fx-border-color: #e5e7eb;"
                + "-fx-border-radius: 10;"
        );

        return card;
    }

    // =========================================================
    // FORM BUTTONS
    // =========================================================

    private HBox createFormButtons() {

        Button saveButton =
                new Button(
                        "Save Supplier"
                );

        saveButton.setStyle(
                "-fx-background-color: #2563eb;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-padding: 10 18 10 18;"
        );

        saveButton.setOnAction(
                event -> saveSupplier()
        );

        Button updateButton =
                new Button(
                        "Update Supplier"
                );

        updateButton.setStyle(
                "-fx-background-color: #059669;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-padding: 10 18 10 18;"
        );

        updateButton.setOnAction(
                event -> updateSupplier()
        );

        Button clearButton =
                new Button(
                        "Clear"
                );

        clearButton.setStyle(
                "-fx-background-color: #6b7280;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-padding: 10 18 10 18;"
        );

        clearButton.setOnAction(
                event -> clearForm()
        );

        HBox buttons =
                new HBox(
                        10,
                        saveButton,
                        updateButton,
                        clearButton
                );

        buttons.setAlignment(
                Pos.CENTER_LEFT
        );

        return buttons;
    }

    // =========================================================
    // SEARCH SECTION
    // =========================================================

    private VBox createSearchSection() {

        Label searchTitle =
                new Label(
                        "Search Suppliers"
                );

        searchTitle.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #111827;"
        );

        searchField.setPromptText(
                "Search by code, name, phone, email, or address"
        );

        searchField.setPrefHeight(
                38
        );

        HBox.setHgrow(
                searchField,
                Priority.ALWAYS
        );

        Button searchButton =
                new Button(
                        "Search"
                );

        searchButton.setStyle(
                "-fx-background-color: #2563eb;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-padding: 9 18 9 18;"
        );

        searchButton.setOnAction(
                event -> searchSuppliers()
        );

        Button showAllButton =
                new Button(
                        "Show All"
                );

        showAllButton.setStyle(
                "-fx-background-color: #374151;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-padding: 9 18 9 18;"
        );

        showAllButton.setOnAction(
                event -> loadSuppliers()
        );

        HBox searchBox =
                new HBox(
                        10,
                        searchField,
                        searchButton,
                        showAllButton
                );

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        VBox container =
                new VBox(
                        10,
                        searchTitle,
                        searchBox
                );

        container.setPadding(
                new Insets(15)
        );

        container.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 10;"
                + "-fx-border-color: #e5e7eb;"
                + "-fx-border-radius: 10;"
        );

        return container;
    }

    // =========================================================
    // TABLE SECTION
    // =========================================================

    private VBox createTableSection() {

        createTableColumns();

        supplierTable.setItems(
                supplierList
        );

        supplierTable.setPlaceholder(
                new Label(
                        "No suppliers found"
                )
        );

        supplierTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        supplierTable.setPrefHeight(
                400
        );

        supplierTable.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #d1d5db;"
        );

        supplierTable.setOnMouseClicked(
                event -> {

                    if (event.getClickCount() == 2) {

                        loadSelectedSupplier();
                    }
                }
        );

        Button activateButton =
                new Button(
                        "Activate"
                );

        activateButton.setStyle(
                "-fx-background-color: #059669;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-padding: 9 18 9 18;"
        );

        activateButton.setOnAction(
                event -> changeSupplierStatus(true)
        );

        Button deactivateButton =
                new Button(
                        "Deactivate"
                );

        deactivateButton.setStyle(
                "-fx-background-color: #dc2626;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-padding: 9 18 9 18;"
        );

        deactivateButton.setOnAction(
                event -> changeSupplierStatus(false)
        );

        HBox statusButtons =
                new HBox(
                        10,
                        activateButton,
                        deactivateButton
                );

        statusButtons.setAlignment(
                Pos.CENTER_LEFT
        );

        Label instruction =
                new Label(
                        "Double-click a supplier row to load its details."
                );

        instruction.setStyle(
                "-fx-text-fill: #6b7280;"
                + "-fx-font-size: 12px;"
        );

        VBox container =
                new VBox(
                        10,
                        supplierTable,
                        statusButtons,
                        instruction
                );

        VBox.setVgrow(
                supplierTable,
                Priority.ALWAYS
        );

        container.setPadding(
                new Insets(15)
        );

        container.setStyle(
                "-fx-background-color: white;"
                + "-fx-background-radius: 10;"
                + "-fx-border-color: #e5e7eb;"
                + "-fx-border-radius: 10;"
        );

        return container;
    }

    // =========================================================
    // TABLE COLUMNS
    // =========================================================

    private void createTableColumns() {

        supplierTable.getColumns().clear();

        TableColumn<Supplier, String> idColumn =
                new TableColumn<>(
                        "ID"
                );

        idColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                String.valueOf(
                                        cellData.getValue().getId()
                                )
                        )
        );

        idColumn.setPrefWidth(
                55
        );

        TableColumn<Supplier, String> codeColumn =
                new TableColumn<>(
                        "Supplier Code"
                );

        codeColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                safeText(
                                        cellData.getValue()
                                                .getSupplierCode()
                                )
                        )
        );

        codeColumn.setPrefWidth(
                120
        );

        TableColumn<Supplier, String> nameColumn =
                new TableColumn<>(
                        "Supplier Name"
                );

        nameColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                safeText(
                                        cellData.getValue()
                                                .getSupplierName()
                                )
                        )
        );

        nameColumn.setPrefWidth(
                180
        );

        TableColumn<Supplier, String> contactColumn =
                new TableColumn<>(
                        "Contact Person"
                );

        contactColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                safeText(
                                        cellData.getValue()
                                                .getContactPerson()
                                )
                        )
        );

        contactColumn.setPrefWidth(
                150
        );

        TableColumn<Supplier, String> phoneColumn =
                new TableColumn<>(
                        "Phone Number"
                );

        phoneColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                safeText(
                                        cellData.getValue()
                                                .getPhoneNumber()
                                )
                        )
        );

        phoneColumn.setPrefWidth(
                130
        );

        TableColumn<Supplier, String> emailColumn =
                new TableColumn<>(
                        "Email"
                );

        emailColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                safeText(
                                        cellData.getValue()
                                                .getEmail()
                                )
                        )
        );

        emailColumn.setPrefWidth(
                180
        );

        TableColumn<Supplier, String> paymentColumn =
                new TableColumn<>(
                        "Payment Terms"
                );

        paymentColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                safeText(
                                        cellData.getValue()
                                                .getPaymentTerms()
                                )
                        )
        );

        paymentColumn.setPrefWidth(
                120
        );

        TableColumn<Supplier, String> statusColumn =
                new TableColumn<>(
                        "Status"
                );

        statusColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                cellData.getValue().isActive()
                                        ? "Active"
                                        : "Inactive"
                        )
        );

        statusColumn.setPrefWidth(
                90
        );

        statusColumn.setCellFactory(
                column ->
                        new TableCell<>() {

                            @Override
                            protected void updateItem(
                                    String item,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        item,
                                        empty
                                );

                                if (empty || item == null) {

                                    setText(null);

                                    setStyle("");

                                } else {

                                    setText(item);

                                    if (item.equals("Active")) {

                                        setStyle(
                                                "-fx-text-fill: #059669;"
                                                + "-fx-font-weight: bold;"
                                        );

                                    } else {

                                        setStyle(
                                                "-fx-text-fill: #dc2626;"
                                                + "-fx-font-weight: bold;"
                                        );
                                    }
                                }
                            }
                        }
        );

        supplierTable.getColumns().addAll(
                idColumn,
                codeColumn,
                nameColumn,
                contactColumn,
                phoneColumn,
                emailColumn,
                paymentColumn,
                statusColumn
        );
    }

    // =========================================================
    // SAVE SUPPLIER
    // =========================================================

    private void saveSupplier() {

        if (!validateForm()) {

            return;
        }

        try {

            String supplierCode =
                    supplierCodeField.getText().trim();

            String supplierName =
                    supplierNameField.getText().trim();

            if (
                    supplierDAO.supplierCodeExists(
                            supplierCode
                    )
            ) {

                showWarning(
                        "Duplicate Supplier Code",
                        "This supplier code already exists."
                );

                return;
            }

            Supplier supplier =
                    new Supplier(
                            supplierCode,
                            supplierName,
                            contactPersonField.getText().trim(),
                            phoneNumberField.getText().trim(),
                            emailField.getText().trim(),
                            addressField.getText().trim(),
                            getPaymentTerms()
                    );

            supplier.setActive(
                    true
            );

            boolean saved =
                    supplierDAO.createSupplier(
                            supplier
                    );

            if (saved) {

                showInformation(
                        "Success",
                        "Supplier saved successfully."
                );

                clearForm();

                loadSuppliers();
            }

        } catch (SQLException exception) {

            showError(
                    "Database Error",
                    exception.getMessage()
            );
        }
    }

    // =========================================================
    // UPDATE SUPPLIER
    // =========================================================

    private void updateSupplier() {

        if (selectedSupplier == null) {

            showWarning(
                    "No Supplier Selected",
                    "Please double-click a supplier row first."
            );

            return;
        }

        if (!validateForm()) {

            return;
        }

        try {

            String supplierCode =
                    supplierCodeField.getText().trim();

            if (
                    supplierDAO.supplierCodeExists(
                            supplierCode,
                            selectedSupplier.getId()
                    )
            ) {

                showWarning(
                        "Duplicate Supplier Code",
                        "Another supplier already uses this code."
                );

                return;
            }

            selectedSupplier.setSupplierCode(
                    supplierCode
            );

            selectedSupplier.setSupplierName(
                    supplierNameField.getText().trim()
            );

            selectedSupplier.setContactPerson(
                    contactPersonField.getText().trim()
            );

            selectedSupplier.setPhoneNumber(
                    phoneNumberField.getText().trim()
            );

            selectedSupplier.setEmail(
                    emailField.getText().trim()
            );

            selectedSupplier.setAddress(
                    addressField.getText().trim()
            );

            selectedSupplier.setPaymentTerms(
                    getPaymentTerms()
            );

            boolean updated =
                    supplierDAO.updateSupplier(
                            selectedSupplier
                    );

            if (updated) {

                showInformation(
                        "Success",
                        "Supplier updated successfully."
                );

                clearForm();

                loadSuppliers();
            }

        } catch (SQLException exception) {

            showError(
                    "Database Error",
                    exception.getMessage()
            );
        }
    }

    // =========================================================
    // SEARCH SUPPLIERS
    // =========================================================

    private void searchSuppliers() {

        String searchText =
                searchField.getText().trim();

        if (searchText.isEmpty()) {

            loadSuppliers();

            return;
        }

        try {

            List<Supplier> results =
                    supplierDAO.searchSuppliers(
                            searchText
                    );

            supplierList.setAll(
                    results
            );

        } catch (SQLException exception) {

            showError(
                    "Search Error",
                    exception.getMessage()
            );
        }
    }

    // =========================================================
    // LOAD ALL SUPPLIERS
    // =========================================================

    private void loadSuppliers() {

        try {

            List<Supplier> suppliers =
                    supplierDAO.getAllSuppliers();

            supplierList.setAll(
                    suppliers
            );

        } catch (SQLException exception) {

            showError(
                    "Loading Error",
                    exception.getMessage()
            );
        }
    }

    // =========================================================
    // LOAD SELECTED SUPPLIER
    // =========================================================

    private void loadSelectedSupplier() {

        Supplier supplier =
                supplierTable.getSelectionModel()
                        .getSelectedItem();

        if (supplier == null) {

            return;
        }

        selectedSupplier =
                supplier;

        supplierCodeField.setText(
                safeText(
                        supplier.getSupplierCode()
                )
        );

        supplierNameField.setText(
                safeText(
                        supplier.getSupplierName()
                )
        );

        contactPersonField.setText(
                safeText(
                        supplier.getContactPerson()
                )
        );

        phoneNumberField.setText(
                safeText(
                        supplier.getPhoneNumber()
                )
        );

        emailField.setText(
                safeText(
                        supplier.getEmail()
                )
        );

        addressField.setText(
                safeText(
                        supplier.getAddress()
                )
        );

        paymentTermsComboBox.setValue(
                safeText(
                        supplier.getPaymentTerms()
                )
        );
    }

    // =========================================================
    // CHANGE SUPPLIER STATUS
    // =========================================================

    private void changeSupplierStatus(
            boolean active
    ) {

        Supplier supplier =
                supplierTable.getSelectionModel()
                        .getSelectedItem();

        if (supplier == null) {

            showWarning(
                    "No Supplier Selected",
                    "Please select a supplier from the table."
            );

            return;
        }

        try {

            boolean updated =
                    supplierDAO.updateSupplierStatus(
                            supplier.getId(),
                            active
                    );

            if (updated) {

                showInformation(
                        "Success",
                        active
                                ? "Supplier activated successfully."
                                : "Supplier deactivated successfully."
                );

                loadSuppliers();

                clearForm();
            }

        } catch (SQLException exception) {

            showError(
                    "Status Update Error",
                    exception.getMessage()
            );
        }
    }

    // =========================================================
    // VALIDATE FORM
    // =========================================================

    private boolean validateForm() {

        String supplierCode =
                supplierCodeField.getText().trim();

        String supplierName =
                supplierNameField.getText().trim();

        String phoneNumber =
                phoneNumberField.getText().trim();

        String email =
                emailField.getText().trim();

        if (supplierCode.isEmpty()) {

            showWarning(
                    "Validation Error",
                    "Please enter a supplier code."
            );

            supplierCodeField.requestFocus();

            return false;
        }

        if (supplierName.isEmpty()) {

            showWarning(
                    "Validation Error",
                    "Please enter a supplier name."
            );

            supplierNameField.requestFocus();

            return false;
        }

        if (phoneNumber.isEmpty()) {

            showWarning(
                    "Validation Error",
                    "Please enter a phone number."
            );

            phoneNumberField.requestFocus();

            return false;
        }

        if (
                !email.isEmpty()
                && !isValidEmail(email)
        ) {

            showWarning(
                    "Validation Error",
                    "Please enter a valid email address."
            );

            emailField.requestFocus();

            return false;
        }

        return true;
    }

    // =========================================================
    // VALIDATE EMAIL
    // =========================================================

    private boolean isValidEmail(
            String email
    ) {

        return email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        );
    }

    // =========================================================
    // GET PAYMENT TERMS
    // =========================================================

    private String getPaymentTerms() {

        String paymentTerms =
                paymentTermsComboBox.getValue();

        if (paymentTerms == null) {

            return "";
        }

        return paymentTerms.trim();
    }

    // =========================================================
    // CLEAR FORM
    // =========================================================

    private void clearForm() {

        supplierCodeField.clear();

        supplierNameField.clear();

        contactPersonField.clear();

        phoneNumberField.clear();

        emailField.clear();

        addressField.clear();

        paymentTermsComboBox.getSelectionModel()
                .clearSelection();

        searchField.clear();

        selectedSupplier =
                null;

        supplierTable.getSelectionModel()
                .clearSelection();
    }

    // =========================================================
    // BACK TO DASHBOARD
    // =========================================================

    private void goBackToDashboard() {

        AdminDashboard dashboard =
                new AdminDashboard(
                        authenticatedUser
                );

        dashboard.show(stage);
    }

    // =========================================================
    // FIELD LABEL
    // =========================================================

    private Label createFieldLabel(
            String text
    ) {

        Label label =
                new Label(
                        text
                );

        label.setStyle(
                "-fx-font-size: 12px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #374151;"
        );

        return label;
    }

    // =========================================================
    // FIELD STYLE
    // =========================================================

    private void applyFieldStyle(
            javafx.scene.control.Control field
    ) {

        field.setStyle(
                "-fx-font-size: 13px;"
                + "-fx-background-color: #ffffff;"
                + "-fx-border-color: #d1d5db;"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5;"
                + "-fx-padding: 8;"
        );

        field.setMaxWidth(
                Double.MAX_VALUE
        );
    }

    // =========================================================
    // SAFE TEXT
    // =========================================================

    private String safeText(
            String value
    ) {

        if (value == null) {

            return "";
        }

        return value;
    }

    // =========================================================
    // INFORMATION ALERT
    // =========================================================

    private void showInformation(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
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
    // WARNING ALERT
    // =========================================================

    private void showWarning(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.WARNING
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
    // ERROR ALERT
    // =========================================================

    private void showError(
            String title,
            String message
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.setTitle(
                title
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                message == null
                        ? "An unexpected error occurred."
                        : message
        );

        alert.showAndWait();
    }
}