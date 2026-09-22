
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

    public CustomerManagementScreen(Stage stage, User authenticatedUser) {
        this.stage = stage;
        this.authenticatedUser = authenticatedUser;
    }

    public void show() {

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Customer Management");
        titleLabel.setStyle(
                "-fx-font-size: 24px; -fx-font-weight: bold;"
        );

        Label subtitleLabel = new Label(
                "Manage customer information and customer status"
        );

        VBox headingBox = new VBox(5, titleLabel, subtitleLabel);
        headingBox.setPadding(new Insets(0, 0, 20, 0));

        root.setTop(headingBox);

        VBox formBox = createCustomerForm();
        VBox tableBox = createCustomerTable();

        SplitPane splitPane = new SplitPane();

        splitPane.getItems().addAll(formBox, tableBox);
        splitPane.setDividerPositions(0.35);

        root.setCenter(splitPane);

        Scene scene = new Scene(root, 1200, 700);

        stage.setTitle("Customer Management");
        stage.setScene(scene);
        stage.show();

        loadCustomers();
    }

    private VBox createCustomerForm() {

        Label formTitle = new Label("Customer Details");
        formTitle.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold;"
        );

        customerCodeField.setPromptText("Customer Code");
        fullNameField.setPromptText("Full Name");
        nicNumberField.setPromptText("NIC Number");
        phoneNumberField.setPromptText("Phone Number");
        addressField.setPromptText("Address");

        VBox formBox = new VBox(10);

        formBox.setPadding(new Insets(10));
        formBox.setPrefWidth(350);

        formBox.getChildren().addAll(
                formTitle,
                new Label("Customer Code"),
                customerCodeField,
                new Label("Full Name"),
                fullNameField,
                new Label("NIC Number"),
                nicNumberField,
                new Label("Phone Number"),
                phoneNumberField,
                new Label("Address"),
                addressField,
                activeCheckBox,
                createButtonBox()
        );

        activeCheckBox.setSelected(true);

        return formBox;
    }

    private HBox createButtonBox() {

        Button addButton = new Button("Add Customer");
        Button updateButton = new Button("Update");
        Button clearButton = new Button("Clear");
        Button backButton = new Button("Back");

        addButton.setOnAction(event -> addCustomer());

        updateButton.setOnAction(event -> updateCustomer());

        clearButton.setOnAction(event -> clearForm());

        backButton.setOnAction(event -> goBackToDashboard());

        HBox buttonBox = new HBox(
                8,
                addButton,
                updateButton,
                clearButton,
                backButton
        );

        buttonBox.setAlignment(Pos.CENTER_LEFT);

        return buttonBox;
    }

    private VBox createCustomerTable() {

        Label tableTitle = new Label("Customer List");
        tableTitle.setStyle(
                "-fx-font-size: 18px; -fx-font-weight: bold;"
        );

        searchField.setPromptText(
                "Search by code, name, NIC or phone number"
        );

        Button searchButton = new Button("Search");
        Button refreshButton = new Button("Refresh");

        searchButton.setOnAction(event -> searchCustomers());

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

        HBox.setHgrow(searchField, Priority.ALWAYS);

        createTableColumns();

        customerTable.setItems(customerList);
        customerTable.setPlaceholder(
                new Label("No customers found")
        );

        customerTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        customerTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {

                    if (newValue != null) {
                        selectedCustomer = newValue;
                        populateForm(newValue);
                    }
                });

        VBox tableBox = new VBox(
                10,
                tableTitle,
                searchBox,
                customerTable
        );

        tableBox.setPadding(new Insets(10));

        VBox.setVgrow(customerTable, Priority.ALWAYS);

        return tableBox;
    }

    private void createTableColumns() {

        TableColumn<Customer, Integer> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        TableColumn<Customer, String> codeColumn =
                new TableColumn<>("Customer Code");

        codeColumn.setCellValueFactory(
                new PropertyValueFactory<>("customerCode")
        );

        TableColumn<Customer, String> nameColumn =
                new TableColumn<>("Full Name");

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("fullName")
        );

        TableColumn<Customer, String> nicColumn =
                new TableColumn<>("NIC Number");

        nicColumn.setCellValueFactory(
                new PropertyValueFactory<>("nicNumber")
        );

        TableColumn<Customer, String> phoneColumn =
                new TableColumn<>("Phone Number");

        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<>("phoneNumber")
        );

        TableColumn<Customer, String> addressColumn =
                new TableColumn<>("Address");

        addressColumn.setCellValueFactory(
                new PropertyValueFactory<>("address")
        );

        TableColumn<Customer, Boolean> activeColumn =
                new TableColumn<>("Active");

        activeColumn.setCellValueFactory(
                new PropertyValueFactory<>("active")
        );

        customerTable.getColumns().addAll(
                idColumn,
                codeColumn,
                nameColumn,
                nicColumn,
                phoneColumn,
                addressColumn,
                activeColumn
        );
    }

    private void addCustomer() {

        if (!validateFields()) {
            return;
        }

        try {

            String customerCode = customerCodeField.getText().trim();

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

            customer.setActive(activeCheckBox.isSelected());

            boolean created = customerDAO.createCustomer(customer);

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

            String customerCode = customerCodeField.getText().trim();

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

            selectedCustomer.setCustomerCode(customerCode);
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

            boolean updated = customerDAO.updateCustomer(
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

    private void searchCustomers() {

        String searchText = searchField.getText().trim();

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

    private void loadCustomers() {

        try {

            customerList.setAll(
                    customerDAO.getAllCustomers()
            );

        } catch (SQLException exception) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Database Error",
                    exception.getMessage()
            );
        }
    }

    private void populateForm(Customer customer) {

        customerCodeField.setText(customer.getCustomerCode());
        fullNameField.setText(customer.getFullName());
        nicNumberField.setText(customer.getNicNumber());
        phoneNumberField.setText(customer.getPhoneNumber());
        addressField.setText(customer.getAddress());
        activeCheckBox.setSelected(customer.isActive());
    }

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

    private void clearForm() {

        selectedCustomer = null;

        customerCodeField.clear();
        fullNameField.clear();
        nicNumberField.clear();
        phoneNumberField.clear();
        addressField.clear();

        activeCheckBox.setSelected(true);

        customerTable.getSelectionModel().clearSelection();
    }

    private void goBackToDashboard() {

        AdminDashboard dashboard =
                new AdminDashboard(authenticatedUser);

        dashboard.show(stage);
    }

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