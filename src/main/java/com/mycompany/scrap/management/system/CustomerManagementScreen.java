
package com.mycompany.scrap.management.system;

import com.mycompany.scrap.management.system.dao.CustomerDAO;
import com.mycompany.scrap.management.system.model.Customer;
import com.mycompany.scrap.management.system.model.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.sql.SQLException;

public class CustomerManagementScreen {

    private final Stage stage;
    private final User authenticatedUser;

    private final CustomerDAO customerDAO = new CustomerDAO();

    private final TableView<Customer> customerTable = new TableView<>();

    private final ObservableList<Customer> customerList =
            FXCollections.observableArrayList();

    private final TextField customerCodeField = new TextField();
    private final TextField fullNameField = new TextField();
    private final TextField nicNumberField = new TextField();
    private final TextField phoneNumberField = new TextField();
    private final TextField addressField = new TextField();
    private final TextField searchField = new TextField();

    private final CheckBox activeCheckBox =
            new CheckBox("Active Customer");

    private Customer selectedCustomer;

    public CustomerManagementScreen(
            Stage stage,
            User authenticatedUser
    ) {
        this.stage = stage;
        this.authenticatedUser = authenticatedUser;
    }

    public void show() {

        BorderPane root = new BorderPane();

        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #f4f6f9;");

        VBox header = createHeader();

        VBox formCard = createCustomerForm();

        VBox tableCard = createCustomerTable();

        SplitPane splitPane = new SplitPane();

        splitPane.getItems().addAll(
                formCard,
                tableCard
        );

        splitPane.setDividerPositions(0.30);
        splitPane.setStyle(
                "-fx-background-color: transparent;"
        );

        root.setTop(header);
        root.setCenter(splitPane);

        BorderPane.setMargin(
                splitPane,
                new Insets(20, 0, 0, 0)
        );

        Scene scene = new Scene(root, 1350, 750);

        stage.setTitle("Customer Management");
        stage.setScene(scene);
        stage.show();

        loadCustomers();
    }

    // ---------------------------------------------------------
    // HEADER
    // ---------------------------------------------------------

    private VBox createHeader() {

        Label titleLabel = new Label("Customer Management");

        titleLabel.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1f2937;"
        );

        Label subtitleLabel = new Label(
                "Manage customer details, records and account status"
        );

        subtitleLabel.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #6b7280;"
        );

        VBox header = new VBox(
                6,
                titleLabel,
                subtitleLabel
        );

        header.setPadding(
                new Insets(0, 0, 5, 0)
        );

        return header;
    }

    // ---------------------------------------------------------
    // CUSTOMER FORM
    // ---------------------------------------------------------

    private VBox createCustomerForm() {

        Label formTitle = new Label("Customer Details");

        formTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1f2937;"
        );

        Label formSubtitle = new Label(
                "Add or update customer information"
        );

        formSubtitle.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #6b7280;"
        );

        customerCodeField.setPromptText("Enter customer code");
        fullNameField.setPromptText("Enter full name");
        nicNumberField.setPromptText("Enter NIC number");
        phoneNumberField.setPromptText("Enter phone number");
        addressField.setPromptText("Enter address");

        styleTextField(customerCodeField);
        styleTextField(fullNameField);
        styleTextField(nicNumberField);
        styleTextField(phoneNumberField);
        styleTextField(addressField);

        activeCheckBox.setSelected(true);

        activeCheckBox.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #374151;"
        );

        VBox formContent = new VBox(8);

        formContent.getChildren().addAll(
                createFieldLabel("Customer Code"),
                customerCodeField,

                createFieldLabel("Full Name"),
                fullNameField,

                createFieldLabel("NIC Number"),
                nicNumberField,

                createFieldLabel("Phone Number"),
                phoneNumberField,

                createFieldLabel("Address"),
                addressField,

                activeCheckBox
        );

        VBox buttonBox = createButtonBox();

        VBox formCard = new VBox(
                15,
                formTitle,
                formSubtitle,
                formContent,
                buttonBox
        );

        formCard.setPadding(new Insets(22));
        formCard.setPrefWidth(350);
        formCard.setMinWidth(300);

        formCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: #e5e7eb;"
        );

        VBox.setVgrow(formContent, Priority.ALWAYS);

        return formCard;
    }

    private Label createFieldLabel(String text) {

        Label label = new Label(text);

        label.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #374151;"
        );

        return label;
    }

    private void styleTextField(TextField field) {

        field.setPrefHeight(36);

        field.setStyle(
                "-fx-background-color: #f9fafb;" +
                "-fx-border-color: #d1d5db;" +
                "-fx-border-radius: 6;" +
                "-fx-background-radius: 6;" +
                "-fx-padding: 8px;"
        );
    }

    // ---------------------------------------------------------
    // BUTTONS
    // ---------------------------------------------------------

    private VBox createButtonBox() {

        Button addButton = new Button("Add Customer");
        Button updateButton = new Button("Update");
        Button clearButton = new Button("Clear");
        Button backButton = new Button("Back");

        stylePrimaryButton(addButton);
        styleSecondaryButton(updateButton);
        styleSecondaryButton(clearButton);
        styleBackButton(backButton);

        addButton.setMaxWidth(Double.MAX_VALUE);
        updateButton.setMaxWidth(Double.MAX_VALUE);
        clearButton.setMaxWidth(Double.MAX_VALUE);
        backButton.setMaxWidth(Double.MAX_VALUE);

        addButton.setOnAction(event -> addCustomer());

        updateButton.setOnAction(event -> updateCustomer());

        clearButton.setOnAction(event -> clearForm());

        backButton.setOnAction(event -> goBackToDashboard());

        VBox buttonBox = new VBox(
                8,
                addButton,
                updateButton,
                clearButton,
                backButton
        );

        buttonBox.setFillWidth(true);

        return buttonBox;
    }

    private void stylePrimaryButton(Button button) {

        button.setPrefHeight(36);

        button.setStyle(
                "-fx-background-color: #2563eb;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;"
        );
    }

    private void styleSecondaryButton(Button button) {

        button.setPrefHeight(36);

        button.setStyle(
                "-fx-background-color: #e5e7eb;" +
                "-fx-text-fill: #1f2937;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;"
        );
    }

    private void styleBackButton(Button button) {

        button.setPrefHeight(36);

        button.setStyle(
                "-fx-background-color: #374151;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 6;" +
                "-fx-cursor: hand;"
        );
    }

    // ---------------------------------------------------------
    // CUSTOMER TABLE
    // ---------------------------------------------------------

    private VBox createCustomerTable() {

        Label tableTitle = new Label("Customer List");

        tableTitle.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1f2937;"
        );

        Label tableSubtitle = new Label(
                "View and manage registered customers"
        );

        tableSubtitle.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-text-fill: #6b7280;"
        );

        searchField.setPromptText(
                "Search by code, name, NIC or phone number"
        );

        styleTextField(searchField);

        Button searchButton = new Button("Search");
        Button refreshButton = new Button("Refresh");

        stylePrimaryButton(searchButton);
        styleSecondaryButton(refreshButton);

        searchButton.setPrefWidth(90);
        refreshButton.setPrefWidth(90);

        searchButton.setOnAction(
                event -> searchCustomers()
        );

        refreshButton.setOnAction(event -> {

            searchField.clear();
            loadCustomers();

        });

        HBox searchBox = new HBox(
                10,
                searchField,
                searchButton,
                refreshButton
        );

        searchBox.setAlignment(Pos.CENTER_LEFT);

        HBox.setHgrow(
                searchField,
                Priority.ALWAYS
        );

        createTableColumns();

        customerTable.setItems(customerList);

        customerTable.setPlaceholder(
                new Label("No customers found")
        );

        customerTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        customerTable.setPrefHeight(500);
        customerTable.setMinHeight(400);

        customerTable.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #e5e7eb;" +
                "-fx-border-radius: 8;" +
                "-fx-background-radius: 8;"
        );

        customerTable.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {

                            if (newValue != null) {

                                selectedCustomer = newValue;

                                populateForm(newValue);
                            }
                        }
                );

        VBox tableCard = new VBox(
                8,
                tableTitle,
                tableSubtitle,
                searchBox,
                customerTable
        );

        tableCard.setPadding(new Insets(22));

        tableCard.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-radius: 12;" +
                "-fx-border-color: #e5e7eb;"
        );

        VBox.setVgrow(
                customerTable,
                Priority.ALWAYS
        );

        return tableCard;
    }

    private void createTableColumns() {

        // Prevent duplicate columns if the screen is opened again
        customerTable.getColumns().clear();

        TableColumn<Customer, Integer> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleObjectProperty<>(
                                cellData.getValue().getId()
                        )
        );

        TableColumn<Customer, String> codeColumn =
                new TableColumn<>("Customer Code");

        codeColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleStringProperty(
                                safeText(
                                        cellData.getValue().getCustomerCode()
                                )
                        )
        );

        TableColumn<Customer, String> nameColumn =
                new TableColumn<>("Full Name");

        nameColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleStringProperty(
                                safeText(
                                        cellData.getValue().getFullName()
                                )
                        )
        );

        TableColumn<Customer, String> nicColumn =
                new TableColumn<>("NIC Number");

        nicColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleStringProperty(
                                safeText(
                                        cellData.getValue().getNicNumber()
                                )
                        )
        );

        TableColumn<Customer, String> phoneColumn =
                new TableColumn<>("Phone Number");

        phoneColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleStringProperty(
                                safeText(
                                        cellData.getValue().getPhoneNumber()
                                )
                        )
        );

        TableColumn<Customer, String> addressColumn =
                new TableColumn<>("Address");

        addressColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleStringProperty(
                                safeText(
                                        cellData.getValue().getAddress()
                                )
                        )
        );

        TableColumn<Customer, String> statusColumn =
                new TableColumn<>("Status");

        statusColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleStringProperty(
                                cellData.getValue().isActive()
                                        ? "Active"
                                        : "Inactive"
                        )
        );

        statusColumn.setCellFactory(column ->
                new TableCell<Customer, String>() {

                    @Override
                    protected void updateItem(
                            String status,
                            boolean empty
                    ) {

                        super.updateItem(status, empty);

                        if (empty || status == null) {

                            setText(null);
                            setStyle("");

                        } else {

                            setText(status);

                            if (status.equals("Active")) {

                                setStyle(
                                        "-fx-text-fill: #15803d;" +
                                        "-fx-font-weight: bold;"
                                );

                            } else {

                                setStyle(
                                        "-fx-text-fill: #dc2626;" +
                                        "-fx-font-weight: bold;"
                                );
                            }
                        }
                    }
                }
        );

        customerTable.getColumns().addAll(
                idColumn,
                codeColumn,
                nameColumn,
                nicColumn,
                phoneColumn,
                addressColumn,
                statusColumn
        );
    }

    private String safeText(String value) {

        return value == null ? "" : value;
    }

    // ---------------------------------------------------------
    // ADD CUSTOMER
    // ---------------------------------------------------------

    private void addCustomer() {

        if (!validateFields()) {
            return;
        }

        try {

            String customerCode =
                    customerCodeField.getText().trim();

            if (customerDAO.customerCodeExists(customerCode)) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Duplicate Customer Code",
                        "This customer code already exists."
                );

                return;
            }

            Customer customer = new Customer(
                    customerCode,
                    fullNameField.getText().trim(),
                    nicNumberField.getText().trim(),
                    phoneNumberField.getText().trim(),
                    addressField.getText().trim()
            );

            customer.setActive(
                    activeCheckBox.isSelected()
            );

            boolean created =
                    customerDAO.createCustomer(customer);

            if (created) {

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Customer added successfully."
                );

                clearForm();
                loadCustomers();
            }

        } catch (SQLException exception) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Database Error",
                    exception.getMessage()
            );
        }
    }

    // ---------------------------------------------------------
    // UPDATE CUSTOMER
    // ---------------------------------------------------------

    private void updateCustomer() {

        if (selectedCustomer == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "No Customer Selected",
                    "Please select a customer to update."
            );

            return;
        }

        if (!validateFields()) {
            return;
        }

        try {

            String customerCode =
                    customerCodeField.getText().trim();

            if (customerDAO.customerCodeExists(
                    customerCode,
                    selectedCustomer.getId()
            )) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Duplicate Customer Code",
                        "This customer code already exists."
                );

                return;
            }

            selectedCustomer.setCustomerCode(
                    customerCode
            );

            selectedCustomer.setFullName(
                    fullNameField.getText().trim()
            );

            selectedCustomer.setNicNumber(
                    nicNumberField.getText().trim()
            );

            selectedCustomer.setPhoneNumber(
                    phoneNumberField.getText().trim()
            );

            selectedCustomer.setAddress(
                    addressField.getText().trim()
            );

            selectedCustomer.setActive(
                    activeCheckBox.isSelected()
            );

            boolean updated =
                    customerDAO.updateCustomer(
                            selectedCustomer
                    );

            if (updated) {

                showAlert(
                        Alert.AlertType.INFORMATION,
                        "Success",
                        "Customer updated successfully."
                );

                clearForm();
                loadCustomers();
            }

        } catch (SQLException exception) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Database Error",
                    exception.getMessage()
            );
        }
    }

    // ---------------------------------------------------------
    // SEARCH CUSTOMERS
    // ---------------------------------------------------------

    private void searchCustomers() {

        String searchText =
                searchField.getText().trim();

        if (searchText.isEmpty()) {

            loadCustomers();
            return;
        }

        try {

            customerList.setAll(
                    customerDAO.searchCustomers(searchText)
            );

        } catch (SQLException exception) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Search Error",
                    exception.getMessage()
            );
        }
    }

    // ---------------------------------------------------------
    // LOAD CUSTOMERS
    // ---------------------------------------------------------

    private void loadCustomers() {

        try {

            customerList.setAll(
                    customerDAO.getAllCustomers()
            );

            customerTable.refresh();

        } catch (SQLException exception) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Database Error",
                    exception.getMessage()
            );
        }
    }

    // ---------------------------------------------------------
    // FORM POPULATION
    // ---------------------------------------------------------

    private void populateForm(Customer customer) {

        customerCodeField.setText(
                safeText(customer.getCustomerCode())
        );

        fullNameField.setText(
                safeText(customer.getFullName())
        );

        nicNumberField.setText(
                safeText(customer.getNicNumber())
        );

        phoneNumberField.setText(
                safeText(customer.getPhoneNumber())
        );

        addressField.setText(
                safeText(customer.getAddress())
        );

        activeCheckBox.setSelected(
                customer.isActive()
        );
    }

    // ---------------------------------------------------------
    // VALIDATION
    // ---------------------------------------------------------

    private boolean validateFields() {

        if (customerCodeField.getText().trim().isEmpty()
                || fullNameField.getText().trim().isEmpty()
                || phoneNumberField.getText().trim().isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Information",
                    "Customer Code, Full Name and Phone Number are required."
            );

            return false;
        }

        return true;
    }

    // ---------------------------------------------------------
    // CLEAR FORM
    // ---------------------------------------------------------

    private void clearForm() {

        selectedCustomer = null;

        customerCodeField.clear();
        fullNameField.clear();
        nicNumberField.clear();
        phoneNumberField.clear();
        addressField.clear();

        activeCheckBox.setSelected(true);

        customerTable.getSelectionModel()
                .clearSelection();
    }

    // ---------------------------------------------------------
    // BACK TO DASHBOARD
    // ---------------------------------------------------------

    private void goBackToDashboard() {

        AdminDashboard dashboard =
                new AdminDashboard(authenticatedUser);

        dashboard.show(stage);
    }

    // ---------------------------------------------------------
    // ALERTS
    // ---------------------------------------------------------

    private void showAlert(
            Alert.AlertType alertType,
            String title,
            String message
    ) {

        Alert alert = new Alert(alertType);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }
}