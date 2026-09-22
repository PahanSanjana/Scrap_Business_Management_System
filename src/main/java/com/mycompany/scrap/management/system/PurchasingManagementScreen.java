package com.mycompany.scrap.management.system;

import com.mycompany.scrap.management.system.dao.MaterialDAO;
import com.mycompany.scrap.management.system.dao.PurchaseDAO;
import com.mycompany.scrap.management.system.dao.SupplierDAO;
import com.mycompany.scrap.management.system.model.Material;
import com.mycompany.scrap.management.system.model.Purchase;
import com.mycompany.scrap.management.system.model.PurchaseItem;
import com.mycompany.scrap.management.system.model.Supplier;
import com.mycompany.scrap.management.system.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchasingManagementScreen {

    private final Stage stage;
    private final User authenticatedUser;

    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final MaterialDAO materialDAO = new MaterialDAO();
    private final PurchaseDAO purchaseDAO = new PurchaseDAO();

    private final ComboBox<Supplier> supplierCombo = new ComboBox<>();
    private final ComboBox<Material> materialCombo = new ComboBox<>();
    private final TextField purchaseCodeField = new TextField();
    private final TextField purchaseDateField = new TextField(LocalDate.now().toString());
    private final TextField invoiceField = new TextField();
    private final ComboBox<String> paymentMethodCombo =
            new ComboBox<>(FXCollections.observableArrayList("Cash", "Bank Transfer", "Credit", "Other"));
    private final ComboBox<String> paymentStatusCombo =
            new ComboBox<>(FXCollections.observableArrayList("PENDING", "PAID", "PARTIAL"));
    private final TextField quantityField = new TextField();
    private final TextField unitPriceField = new TextField();
    private final TextArea notesArea = new TextArea();

    private final TableView<PurchaseItem> itemTable = new TableView<>();
    private final TableView<Purchase> purchaseTable = new TableView<>();
    private final ObservableList<PurchaseItem> items = FXCollections.observableArrayList();
    private final ObservableList<Purchase> purchases = FXCollections.observableArrayList();

    private final Map<Integer, String> materialNames = new HashMap<>();
    private final Map<Integer, String> supplierNames = new HashMap<>();
    private final Label totalLabel = new Label("Total: 0.00");
    private Integer editingPurchaseId = null;

    public PurchasingManagementScreen(Stage stage, User authenticatedUser) {
        this.stage = stage;
        this.authenticatedUser = authenticatedUser;
        purchaseCodeField.setText("PUR-" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        paymentMethodCombo.setValue("Cash");
        paymentStatusCombo.setValue("PENDING");
        notesArea.setPrefRowCount(3);
        configureTables();
        loadData();
    }

    public void show() {

        BorderPane mainRoot = new BorderPane();
        mainRoot.setStyle("-fx-background-color: #f3f4f6;");

        Label pageTitle = new Label("Purchasing Management");
        pageTitle.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #111827;"
        );

        Label pageSubtitle = new Label(
                "Create purchases, add materials, and manage purchase records."
        );
        pageSubtitle.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #6b7280;"
        );

        Button backButton = createActionButton("Back to Dashboard", "#4b5563");
        backButton.setOnAction(event -> {
            AdminDashboard dashboard = new AdminDashboard(authenticatedUser);
            dashboard.show(stage);
        });

        VBox heading = new VBox(6, pageTitle, pageSubtitle);
        HBox topBar = new HBox(20, heading, backButton);
        topBar.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        HBox.setHgrow(heading, Priority.ALWAYS);
        topBar.setPadding(new Insets(22, 28, 18, 28));
        topBar.setStyle("-fx-background-color: white;");

        VBox purchaseFormCard = createPurchaseFormCard();
        VBox itemCard = createItemCard();
        VBox historyCard = createHistoryCard();

        VBox content = new VBox(18, purchaseFormCard, itemCard, historyCard);
        content.setPadding(new Insets(22, 28, 30, 28));
        content.setFillWidth(true);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setStyle("-fx-background-color: #f3f4f6;");

        // Improve mouse-wheel scrolling behavior.
        scrollPane.addEventFilter(javafx.scene.input.ScrollEvent.SCROLL, event -> {
            // Let TableView handle its own internal scrolling.
            if (event.getTarget() instanceof javafx.scene.control.TableView) {
                return;
            }

            if (event.getDeltaY() != 0) {
                double contentHeight = content.getBoundsInLocal().getHeight();
                double viewportHeight = scrollPane.getViewportBounds().getHeight();
                double scrollRange = Math.max(1, contentHeight - viewportHeight);
                double nextValue = scrollPane.getVvalue()
                        - (event.getDeltaY() / scrollRange) * 0.75;

                scrollPane.setVvalue(Math.max(0, Math.min(1, nextValue)));
                event.consume();
            }
        });

        mainRoot.setTop(topBar);
        mainRoot.setCenter(scrollPane);

        Scene scene = new Scene(mainRoot, 1450, 900);
        stage.setTitle("Purchasing Management");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createPurchaseFormCard() {

        Label heading = createSectionHeading("Purchase Information");

        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(14);
        grid.setPadding(new Insets(5, 0, 0, 0));

        configureInput(purchaseCodeField, "Purchase code");
        configureInput(purchaseDateField, "YYYY-MM-DD");
        configureInput(invoiceField, "Invoice number");
        configureInput(supplierCombo, "Select supplier");
        configureInput(paymentMethodCombo, "Select payment method");
        configureInput(paymentStatusCombo, "Select payment status");
        configureInput(notesArea, "Additional notes");

        grid.add(createFormLabel("Purchase Code"), 0, 0);
        grid.add(purchaseCodeField, 1, 0);
        grid.add(createFormLabel("Purchase Date"), 2, 0);
        grid.add(purchaseDateField, 3, 0);

        grid.add(createFormLabel("Supplier"), 0, 1);
        grid.add(supplierCombo, 1, 1);
        grid.add(createFormLabel("Invoice Number"), 2, 1);
        grid.add(invoiceField, 3, 1);

        grid.add(createFormLabel("Payment Method"), 0, 2);
        grid.add(paymentMethodCombo, 1, 2);
        grid.add(createFormLabel("Payment Status"), 2, 2);
        grid.add(paymentStatusCombo, 3, 2);

        grid.add(createFormLabel("Notes"), 0, 3);
        grid.add(notesArea, 1, 3, 3, 1);

        for (int i = 0; i < 4; i++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setHgrow(Priority.ALWAYS);
            constraints.setFillWidth(true);
            grid.getColumnConstraints().add(constraints);
        }

        VBox card = createCard();
        card.getChildren().addAll(heading, new Separator(), grid);
        return card;
    }

    private VBox createItemCard() {

        Label heading = createSectionHeading("Purchase Items");

        GridPane itemGrid = new GridPane();
        itemGrid.setHgap(18);
        itemGrid.setVgap(14);

        configureInput(materialCombo, "Select material");
        configureInput(quantityField, "Enter quantity");
        configureInput(unitPriceField, "Enter unit price");

        itemGrid.add(createFormLabel("Material"), 0, 0);
        itemGrid.add(materialCombo, 1, 0);
        itemGrid.add(createFormLabel("Quantity"), 2, 0);
        itemGrid.add(quantityField, 3, 0);
        itemGrid.add(createFormLabel("Unit Price"), 4, 0);
        itemGrid.add(unitPriceField, 5, 0);

        for (int i = 0; i < 6; i++) {
            ColumnConstraints constraints = new ColumnConstraints();
            constraints.setHgrow(Priority.ALWAYS);
            constraints.setFillWidth(true);
            itemGrid.getColumnConstraints().add(constraints);
        }

        Button addButton = createActionButton("Add Item", "#2563eb");
        Button removeButton = createActionButton("Remove Selected", "#d97706");
        Button clearItemsButton = createActionButton("Clear Items", "#6b7280");

        addButton.setOnAction(event -> addItem());
        removeButton.setOnAction(event -> {
            PurchaseItem selected = itemTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                items.remove(selected);
                updateTotal();
            }
        });
        clearItemsButton.setOnAction(event -> {
            items.clear();
            updateTotal();
        });

        HBox buttons = new HBox(12, addButton, removeButton, clearItemsButton);
        buttons.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        totalLabel.setStyle(
                "-fx-font-size: 16px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #111827;"
        );

        HBox totalBox = new HBox(totalLabel);
        totalBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        Button saveButton = createActionButton("Save Purchase", "#16a34a");
        saveButton.setOnAction(event -> savePurchase());

        Button resetButton = createActionButton("Reset Form", "#6b7280");
        resetButton.setOnAction(event -> clearForm());

        HBox saveActions = new HBox(12, resetButton, saveButton);
        saveActions.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        itemTable.setPrefHeight(240);
        itemTable.setMinHeight(180);
        itemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        itemTable.setPlaceholder(new Label("No purchase items added."));
        itemTable.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #d1d5db;" +
                "-fx-border-radius: 6;"
        );

        VBox card = createCard();
        card.getChildren().addAll(
                heading,
                new Separator(),
                itemGrid,
                buttons,
                itemTable,
                totalBox,
                saveActions
        );

        return card;
    }

    private VBox createHistoryCard() {

        Label heading = createSectionHeading("Purchase History");

        purchaseTable.setPrefHeight(360);
        purchaseTable.setMinHeight(260);
        purchaseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        purchaseTable.setPlaceholder(new Label("No purchases found."));
        purchaseTable.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #d1d5db;" +
                "-fx-border-radius: 6;"
        );

        Button viewButton = createActionButton("View Selected", "#0891b2");
        viewButton.setOnAction(event -> viewSelectedPurchase());

        Button editButton = createActionButton("Edit Selected", "#2563eb");
        editButton.setOnAction(event -> editSelectedPurchase());

        Button deleteButton = createActionButton("Delete Selected", "#dc2626");
        deleteButton.setOnAction(event -> deleteSelectedPurchase());

        Button refreshButton = createActionButton("Refresh", "#6b7280");
        refreshButton.setOnAction(event -> loadData());

        HBox actions = new HBox(12, viewButton, editButton, deleteButton, refreshButton);
        actions.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        VBox card = createCard();
        card.getChildren().addAll(heading, new Separator(), purchaseTable, actions);
        return card;
    }

    private VBox createCard() {
        VBox card = new VBox(16);
        card.setPadding(new Insets(22));
        card.setFillWidth(true);
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #e5e7eb;" +
                "-fx-border-radius: 8;"
        );
        return card;
    }

    private Label createSectionHeading(String text) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #111827;"
        );
        return label;
    }

    private Label createFormLabel(String text) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #374151;"
        );
        return label;
    }

    private void configureInput(Control control, String prompt) {
        control.setMaxWidth(Double.MAX_VALUE);

        if (control instanceof TextInputControl input) {
            input.setPromptText(prompt);
        }

        control.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: #d1d5db;" +
                "-fx-border-radius: 5;" +
                "-fx-background-radius: 5;" +
                "-fx-padding: 9;"
        );
    }

    private Button createActionButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 10 16 10 16;" +
                "-fx-background-radius: 5;"
        );
        button.setCursor(javafx.scene.Cursor.HAND);
        return button;
    }

    private VBox labeled(String text, Control control) {
        Label label = new Label(text);
        label.setStyle("-fx-font-weight: bold;");
        control.setMaxWidth(Double.MAX_VALUE);
        return new VBox(4, label, control);
    }

    private void configureTables() {
        TableColumn<PurchaseItem, String> materialCol = new TableColumn<>("Material");
        materialCol.setCellValueFactory(c ->
                new SimpleStringProperty(materialNames.getOrDefault(c.getValue().getMaterialId(),
                        "Material #" + c.getValue().getMaterialId())));

        TableColumn<PurchaseItem, String> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(c ->
                new SimpleStringProperty(String.valueOf(c.getValue().getQuantity())));

        TableColumn<PurchaseItem, String> priceCol = new TableColumn<>("Unit Price");
        priceCol.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("%.2f", c.getValue().getUnitPrice())));

        TableColumn<PurchaseItem, String> totalCol = new TableColumn<>("Total");
        totalCol.setCellValueFactory(c ->
                new SimpleStringProperty(String.format("%.2f", c.getValue().getTotalPrice())));

        itemTable.getColumns().addAll(materialCol, qtyCol, priceCol, totalCol);
        itemTable.setItems(items);

        TableColumn<Purchase, String> codeCol = textColumn("Code", p -> p.getPurchaseCode());
        TableColumn<Purchase, String> supplierCol = textColumn("Supplier",
                p -> supplierNames.getOrDefault(p.getSupplierId(), "Supplier #" + p.getSupplierId()));
        TableColumn<Purchase, String> dateCol = textColumn("Date", p -> p.getPurchaseDate());
        TableColumn<Purchase, String> statusCol = textColumn("Status", p -> p.getPaymentStatus());
        TableColumn<Purchase, String> amountCol = textColumn("Amount",
                p -> String.format("%.2f", p.getTotalAmount()));

        purchaseTable.getColumns().addAll(codeCol, supplierCol, dateCol, statusCol, amountCol);
        purchaseTable.setItems(purchases);
    }

    private TableColumn<Purchase, String> textColumn(String title,
                                                       java.util.function.Function<Purchase, String> value) {
        TableColumn<Purchase, String> column = new TableColumn<>(title);
        column.setCellValueFactory(c -> new SimpleStringProperty(value.apply(c.getValue())));
        return column;
    }

    private void loadData() {
        try {
            List<Supplier> suppliers = supplierDAO.getAllSuppliers();
            supplierCombo.setItems(FXCollections.observableArrayList(suppliers));
            supplierCombo.setCellFactory(list -> new ListCell<>() {
                @Override protected void updateItem(Supplier item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getSupplierName());
                }
            });
            supplierCombo.setButtonCell(new ListCell<>() {
                @Override protected void updateItem(Supplier item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getSupplierName());
                }
            });
            supplierNames.clear();
            for (Supplier s : suppliers) supplierNames.put(s.getId(), s.getSupplierName());

            List<Material> materials = materialDAO.getAllMaterials();
            materialCombo.setItems(FXCollections.observableArrayList(materials));
            materialCombo.setCellFactory(list -> materialCell());
            materialCombo.setButtonCell(materialCell());
            materialNames.clear();
            for (Material m : materials) materialNames.put(m.getId(), m.getMaterialName());

            purchases.setAll(purchaseDAO.getAllPurchases());
            itemTable.refresh();
            purchaseTable.refresh();
        } catch (Exception ex) {
            showError("Loading Error", ex.getMessage());
        }
    }

    private ListCell<Material> materialCell() {
        return new ListCell<>() {
            @Override protected void updateItem(Material item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getMaterialName());
            }
        };
    }

    private void addItem() {
        try {
            Material material = materialCombo.getValue();
            double quantity = Double.parseDouble(quantityField.getText().trim());
            double price = Double.parseDouble(unitPriceField.getText().trim());

            if (material == null || quantity <= 0 || price < 0) {
                showError("Validation", "Select a material and enter valid values.");
                return;
            }

            items.add(new PurchaseItem(0, 0, material.getId(), quantity, price, quantity * price));
            quantityField.clear();
            unitPriceField.clear();
            updateTotal();
        } catch (NumberFormatException ex) {
            showError("Validation", "Quantity and unit price must be numbers.");
        }
    }

    private void updateTotal() {
        double total = items.stream().mapToDouble(PurchaseItem::getTotalPrice).sum();
        totalLabel.setText(String.format("Total: %.2f", total));
    }

    private void savePurchase() {
        try {
            Supplier supplier = supplierCombo.getValue();
            if (supplier == null || items.isEmpty()) {
                showError("Validation", "Select a supplier and add at least one item.");
                return;
            }

            String purchaseCode = purchaseCodeField.getText().trim();
            String purchaseDate = purchaseDateField.getText().trim();

            if (purchaseCode.isBlank()) {
                showError("Validation", "Purchase code is required.");
                return;
            }

            if (purchaseDate.isBlank()) {
                showError("Validation", "Purchase date is required.");
                return;
            }

            try {
                LocalDate.parse(purchaseDate);
            } catch (java.time.format.DateTimeParseException ex) {
                showError("Validation", "Purchase date must use YYYY-MM-DD format.");
                return;
            }

            if (paymentMethodCombo.getValue() == null ||
                    paymentStatusCombo.getValue() == null) {
                showError("Validation", "Select payment method and payment status.");
                return;
            }

            double total = items.stream().mapToDouble(PurchaseItem::getTotalPrice).sum();
            Purchase purchase = new Purchase(
                    purchaseCode,
                    supplier.getId(),
                    purchaseDate,
                    invoiceField.getText().trim(),
                    paymentMethodCombo.getValue(),
                    paymentStatusCombo.getValue(),
                    total,
                    notesArea.getText().trim(),
                    authenticatedUser.getId()
            );

            boolean saved;

            if (editingPurchaseId == null) {
                saved = purchaseDAO.savePurchase(purchase, items);
            } else {
                purchase.setId(editingPurchaseId);
                saved = purchaseDAO.updatePurchase(purchase, items);
            }

            if (saved) {
                showInfo("Success",
                        editingPurchaseId == null
                                ? "Purchase saved successfully."
                                : "Purchase updated successfully.");
                clearForm();
                loadData();
            } else {
                showError("Save Error", "Purchase could not be saved.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            String errorMessage = ex.getMessage();
            if (errorMessage == null || errorMessage.isBlank()) {
                errorMessage = ex.getClass().getSimpleName();
            }

            showError(
                    "Save Error",
                    "The purchase could not be saved.\\n\\n" + errorMessage
            );
        }
    }


    private void viewSelectedPurchase() {
        Purchase selected = purchaseTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("View Purchase",
                    "Please select a purchase from Purchase History first.");
            return;
        }

        try {
            List<PurchaseItem> selectedItems =
                    purchaseDAO.getPurchaseItems(selected.getId());

            Stage detailsStage = new Stage();
            detailsStage.initOwner(stage);
            detailsStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
            detailsStage.setTitle("Purchase Details");
            detailsStage.setMinWidth(900);
            detailsStage.setMinHeight(650);

            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: #f8fafc;");

            // Header
            Label title = new Label("Purchase Details");
            title.setStyle(
                    "-fx-font-size: 28px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #172554;"
            );

            Label subtitle = new Label(
                    "View complete information about this purchase record."
            );
            subtitle.setStyle(
                    "-fx-font-size: 14px;" +
                    "-fx-text-fill: #64748b;"
            );

            VBox headerText = new VBox(5, title, subtitle);
            Label documentIcon = new Label("▣");
            documentIcon.setStyle(
                    "-fx-font-size: 32px;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;"
            );

            StackPane iconBox = new StackPane(documentIcon);
            iconBox.setPrefSize(64, 64);
            iconBox.setStyle(
                    "-fx-background-color: #2563eb;" +
                    "-fx-background-radius: 32;"
            );

            HBox header = new HBox(18, iconBox, headerText);
            header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            header.setPadding(new Insets(24, 28, 22, 28));
            header.setStyle("-fx-background-color: white;");

            // Summary panel
            Label codeCaption = detailCaption("PURCHASE CODE");
            Label codeValue = new Label(safeText(selected.getPurchaseCode()));
            codeValue.setStyle(
                    "-fx-font-size: 24px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1e3a8a;"
            );

            Label statusBadge = new Label(
                    safeText(selected.getPaymentStatus()).toUpperCase()
            );
            statusBadge.setStyle(
                    "-fx-background-color: #dcfce7;" +
                    "-fx-text-fill: #166534;" +
                    "-fx-font-weight: bold;" +
                    "-fx-padding: 6 14 6 14;" +
                    "-fx-background-radius: 18;"
            );

            VBox codeBox = new VBox(6, codeCaption, codeValue, statusBadge);
            codeBox.setPrefWidth(360);

            VBox dateBox = createSummaryBox(
                    "PURCHASE DATE",
                    safeText(selected.getPurchaseDate())
            );

            VBox supplierBox = createSummaryBox(
                    "SUPPLIER",
                    supplierNames.getOrDefault(
                            selected.getSupplierId(),
                            "Supplier #" + selected.getSupplierId()
                    )
            );

            HBox summary = new HBox(28, codeBox, dateBox, supplierBox);
            summary.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            summary.setPadding(new Insets(22));
            summary.setStyle(
                    "-fx-background-color: #eff6ff;" +
                    "-fx-background-radius: 12;" +
                    "-fx-border-color: #dbeafe;" +
                    "-fx-border-radius: 12;"
            );

            // Purchase information panel
            GridPane informationGrid = new GridPane();
            informationGrid.setHgap(32);
            informationGrid.setVgap(14);
            informationGrid.setPadding(new Insets(18, 20, 20, 20));

            addDetailRow(informationGrid, "Invoice Number",
                    emptyAsNA(selected.getInvoiceNumber()), 0, 0);
            addDetailRow(informationGrid, "Payment Method",
                    emptyAsNA(selected.getPaymentMethod()), 0, 1);
            addDetailRow(informationGrid, "Payment Status",
                    emptyAsNA(selected.getPaymentStatus()), 0, 2);
            addDetailRow(informationGrid, "Total Amount",
                    String.format("%.2f", selected.getTotalAmount()), 0, 3);

            addDetailRow(informationGrid, "Purchase ID",
                    String.valueOf(selected.getId()), 2, 0);
            addDetailRow(informationGrid, "Created By",
                    String.valueOf(selected.getCreatedBy()), 2, 1);

            Label notesLabel = detailCaption("NOTES");
            Label notesValue = new Label(emptyAsNA(selected.getNotes()));
            notesValue.setWrapText(true);
            notesValue.setMaxWidth(Double.MAX_VALUE);
            notesValue.setStyle(
                    "-fx-background-color: #f1f5f9;" +
                    "-fx-text-fill: #334155;" +
                    "-fx-padding: 12;" +
                    "-fx-background-radius: 8;"
            );

            VBox notesBox = new VBox(6, notesLabel, notesValue);
            notesBox.setMaxWidth(Double.MAX_VALUE);
            GridPane.setColumnSpan(notesBox, 3);
            informationGrid.add(notesBox, 0, 4, 4, 1);

            VBox informationCard = createDetailsCard(
                    "Purchase Information",
                    informationGrid
            );

            // Items table
            TableView<PurchaseItem> detailsItemsTable = new TableView<>();
            detailsItemsTable.setItems(
                    FXCollections.observableArrayList(selectedItems)
            );
            detailsItemsTable.setColumnResizePolicy(
                    TableView.CONSTRAINED_RESIZE_POLICY
            );
            detailsItemsTable.setPrefHeight(190);
            detailsItemsTable.setPlaceholder(
                    new Label("No purchase items found.")
            );

            TableColumn<PurchaseItem, String> numberColumn =
                    new TableColumn<>("#");
            numberColumn.setCellValueFactory(cell ->
                    new SimpleStringProperty(
                            String.valueOf(
                                    detailsItemsTable.getItems().indexOf(cell.getValue()) + 1
                            )
                    )
            );

            TableColumn<PurchaseItem, String> materialColumn =
                    new TableColumn<>("Material");
            materialColumn.setCellValueFactory(cell ->
                    new SimpleStringProperty(
                            materialNames.getOrDefault(
                                    cell.getValue().getMaterialId(),
                                    "Material #" + cell.getValue().getMaterialId()
                            )
                    )
            );

            TableColumn<PurchaseItem, String> quantityColumn =
                    new TableColumn<>("Quantity");
            quantityColumn.setCellValueFactory(cell ->
                    new SimpleStringProperty(
                            String.format("%.2f", cell.getValue().getQuantity())
                    )
            );

            TableColumn<PurchaseItem, String> unitPriceColumn =
                    new TableColumn<>("Unit Price");
            unitPriceColumn.setCellValueFactory(cell ->
                    new SimpleStringProperty(
                            String.format("%.2f", cell.getValue().getUnitPrice())
                    )
            );

            TableColumn<PurchaseItem, String> totalPriceColumn =
                    new TableColumn<>("Total Price");
            totalPriceColumn.setCellValueFactory(cell ->
                    new SimpleStringProperty(
                            String.format("%.2f", cell.getValue().getTotalPrice())
                    )
            );

            detailsItemsTable.getColumns().addAll(
                    numberColumn,
                    materialColumn,
                    quantityColumn,
                    unitPriceColumn,
                    totalPriceColumn
            );

            Label itemsHeading = new Label(
                    "Purchase Items (" + selectedItems.size() + ")"
            );
            itemsHeading.setStyle(
                    "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #172554;"
            );

            Label totalCaption = new Label("Total Amount:");
            totalCaption.setStyle(
                    "-fx-font-size: 16px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1e40af;"
            );

            Label totalValue = new Label(
                    String.format("%.2f", selected.getTotalAmount())
            );
            totalValue.setStyle(
                    "-fx-font-size: 22px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1e40af;"
            );

            Region totalSpacer = new Region();
            HBox.setHgrow(totalSpacer, Priority.ALWAYS);

            HBox totalBar = new HBox(
                    12, totalSpacer, totalCaption, totalValue
            );
            totalBar.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            totalBar.setPadding(new Insets(14, 18, 14, 18));
            totalBar.setStyle(
                    "-fx-background-color: #eff6ff;" +
                    "-fx-background-radius: 8;"
            );

            VBox itemsContent = new VBox(
                    14, itemsHeading, new Separator(),
                    detailsItemsTable, totalBar
            );
            itemsContent.setPadding(new Insets(18, 20, 20, 20));

            VBox itemsCard = createDetailsCard(
                    "Purchase Items",
                    itemsContent
            );

            VBox content = new VBox(18, summary, informationCard, itemsCard);
            content.setPadding(new Insets(22, 28, 22, 28));
            content.setFillWidth(true);

            ScrollPane scrollPane = new ScrollPane(content);
            scrollPane.setFitToWidth(true);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setPannable(true);
            scrollPane.setStyle("-fx-background-color: #f8fafc;");

            Button closeButton = createActionButton("Close", "#2563eb");
            closeButton.setOnAction(event -> detailsStage.close());

            HBox footer = new HBox(closeButton);
            footer.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
            footer.setPadding(new Insets(16, 28, 16, 28));
            footer.setStyle(
                    "-fx-background-color: #f1f5f9;" +
                    "-fx-border-color: #e2e8f0;"
            );

            root.setTop(header);
            root.setCenter(scrollPane);
            root.setBottom(footer);

            Scene scene = new Scene(root, 1050, 760);
            detailsStage.setScene(scene);
            detailsStage.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
            showError(
                    "View Error",
                    ex.getMessage() == null
                            ? ex.getClass().getSimpleName()
                            : ex.getMessage()
            );
        }
    }

    private Label detailCaption(String text) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-font-size: 11px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #64748b;"
        );
        return label;
    }

    private VBox createSummaryBox(String caption, String value) {
        Label captionLabel = detailCaption(caption);
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #172554;"
        );

        VBox box = new VBox(8, captionLabel, valueLabel);
        box.setPrefWidth(230);
        return box;
    }

    private void addDetailRow(
            GridPane grid,
            String caption,
            String value,
            int column,
            int row
    ) {
        Label captionLabel = detailCaption(caption);
        Label valueLabel = new Label(value);
        valueLabel.setWrapText(true);
        valueLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #172554;"
        );

        VBox box = new VBox(5, captionLabel, valueLabel);
        box.setMinWidth(220);
        grid.add(box, column, row);
    }

    private VBox createDetailsCard(String title, javafx.scene.Node content) {
        Label heading = new Label(title);
        heading.setStyle(
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #172554;"
        );

        VBox card = new VBox(0, heading, new Separator(), content);
        card.setFillWidth(true);
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #dbe3ef;" +
                "-fx-border-radius: 12;"
        );
        return card;
    }

    private String safeText(String value) {
        return value == null ? "N/A" : value;
    }

    private String emptyAsNA(String value) {
        return value == null || value.isBlank() ? "N/A" : value;
    }

    private void editSelectedPurchase() {
        Purchase selected = purchaseTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Edit Purchase", "Please select a purchase record first.");
            return;
        }

        try {
            editingPurchaseId = selected.getId();

            purchaseCodeField.setText(selected.getPurchaseCode());
            purchaseDateField.setText(selected.getPurchaseDate());
            invoiceField.setText(selected.getInvoiceNumber());
            paymentMethodCombo.setValue(selected.getPaymentMethod());
            paymentStatusCombo.setValue(selected.getPaymentStatus());
            notesArea.setText(selected.getNotes() == null ? "" : selected.getNotes());

            supplierCombo.getItems().stream()
                    .filter(supplier -> supplier.getId() == selected.getSupplierId())
                    .findFirst()
                    .ifPresent(supplierCombo::setValue);

            items.setAll(purchaseDAO.getPurchaseItems(selected.getId()));
            updateTotal();

            showInfo("Edit Mode",
                    "Purchase loaded for editing. Update the fields and click Save Purchase.");
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Edit Error", safeErrorMessage(ex));
        }
    }

    private void deleteSelectedPurchase() {
        Purchase selected = purchaseTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Delete Purchase", "Please select a purchase record first.");
            return;
        }

        Alert confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Delete purchase " + selected.getPurchaseCode()
                        + "? This will also delete its purchase items.",
                ButtonType.YES,
                ButtonType.NO
        );
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText("Delete Purchase Record");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    if (purchaseDAO.deletePurchase(selected.getId())) {
                        showInfo("Deleted", "Purchase deleted successfully.");
                        clearForm();
                        loadData();
                    } else {
                        showError("Delete Error", "The purchase could not be deleted.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showError("Delete Error", safeErrorMessage(ex));
                }
            }
        });
    }

    private String safeErrorMessage(Exception ex) {
        return ex.getMessage() == null || ex.getMessage().isBlank()
                ? ex.getClass().getSimpleName()
                : ex.getMessage();
    }

    private void clearForm() {
        editingPurchaseId = null;
        purchaseCodeField.setText("PUR-" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        purchaseDateField.setText(LocalDate.now().toString());
        supplierCombo.setValue(null);
        invoiceField.clear();
        paymentMethodCombo.setValue("Cash");
        paymentStatusCombo.setValue("PENDING");
        notesArea.clear();
        items.clear();
        updateTotal();
    }

    private void showInfo(String title, String message) {
        new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK).showAndWait();
    }

    private void showError(String title, String message) {
        new Alert(Alert.AlertType.ERROR, message == null ? "Unknown error" : message,
                ButtonType.OK).showAndWait();
    }
}
