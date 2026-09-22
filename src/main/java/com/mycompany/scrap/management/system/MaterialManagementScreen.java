
package com.mycompany.scrap.management.system;

import com.mycompany.scrap.management.system.dao.CategoryDAO;
import com.mycompany.scrap.management.system.dao.MaterialDAO;
import com.mycompany.scrap.management.system.model.Category;
import com.mycompany.scrap.management.system.model.Material;
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
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
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
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Locale;

public class MaterialManagementScreen {

    // =========================================================
    // FIELDS
    // =========================================================

    private final User authenticatedUser;

    private final MaterialDAO materialDAO;
    private final CategoryDAO categoryDAO;

    private final TableView<Material> materialTable;

    private final ComboBox<Category> categoryComboBox;
    private final TextField materialNameField;
    private final TextArea descriptionArea;
    private final TextField measurementUnitField;
    private final TextField minimumStockField;
    private final TextField searchField;

    private Material selectedMaterial;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public MaterialManagementScreen(User authenticatedUser) {

        this.authenticatedUser = authenticatedUser;

        this.materialDAO = new MaterialDAO();
        this.categoryDAO = new CategoryDAO();

        this.materialTable = new TableView<>();

        this.categoryComboBox = new ComboBox<>();
        this.materialNameField = new TextField();
        this.descriptionArea = new TextArea();
        this.measurementUnitField = new TextField();
        this.minimumStockField = new TextField();
        this.searchField = new TextField();

        this.selectedMaterial = null;
    }

    // =========================================================
    // SHOW SCREEN
    // =========================================================

    public void show(Stage stage) {

        BorderPane root = new BorderPane();

        root.setStyle(
                "-fx-background-color: #f3f4f6;"
        );

        VBox header = createHeader(stage);

        root.setTop(header);

        VBox mainContent = createMainContent();

        ScrollPane scrollPane = new ScrollPane(mainContent);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setStyle(
                "-fx-background: #f3f4f6;"
                + "-fx-background-color: #f3f4f6;"
        );

        root.setCenter(scrollPane);

        Scene scene = new Scene(
                root,
                1300,
                850
        );

        stage.setTitle("Material Management");

        stage.setScene(scene);

        stage.setMinWidth(1100);
        stage.setMinHeight(720);

        stage.show();

        loadCategories();
        loadAllMaterials();
    }

    // =========================================================
    // HEADER
    // =========================================================

    private VBox createHeader(Stage stage) {

        Label titleLabel = new Label(
                "Material Management"
        );

        titleLabel.setStyle(
                "-fx-text-fill: white;"
                + "-fx-font-size: 25px;"
                + "-fx-font-weight: bold;"
        );

        Label subtitleLabel = new Label(
                "Manage scrap materials, measurement units, "
                + "and minimum stock levels"
        );

        subtitleLabel.setStyle(
                "-fx-text-fill: #d1d5db;"
                + "-fx-font-size: 13px;"
        );

        Button backButton = new Button(
                "Back to Dashboard"
        );

        backButton.setStyle(
                "-fx-background-color: #374151;"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-padding: 10 18 10 18;"
                + "-fx-background-radius: 6;"
                + "-fx-cursor: hand;"
        );

        backButton.setOnAction(event -> {

            AdminDashboard dashboard =
                    new AdminDashboard(authenticatedUser);

            dashboard.show(stage);
        });

        Region spacer = new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );

        VBox titleSection = new VBox(4);

        titleSection.getChildren().addAll(
                titleLabel,
                subtitleLabel
        );

        HBox titleBox = new HBox(15);

        titleBox.setAlignment(
                Pos.CENTER_LEFT
        );

        titleBox.getChildren().addAll(
                titleSection,
                spacer,
                backButton
        );

        VBox header = new VBox(titleBox);

        header.setPadding(
                new Insets(22, 30, 22, 30)
        );

        header.setStyle(
                "-fx-background-color: #111827;"
        );

        return header;
    }

    // =========================================================
    // MAIN CONTENT
    // =========================================================

    private VBox createMainContent() {

        VBox content = new VBox(20);

        content.setPadding(
                new Insets(25, 30, 30, 30)
        );

        VBox formCard = createFormCard();

        VBox searchCard = createSearchCard();

        VBox tableCard = createTableCard();

        content.getChildren().addAll(
                formCard,
                searchCard,
                tableCard
        );

        return content;
    }

    // =========================================================
    // FORM CARD
    // =========================================================

    private VBox createFormCard() {

        Label titleLabel = new Label(
                "Material Information"
        );

        titleLabel.setStyle(
                "-fx-font-size: 19px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #111827;"
        );

        Label subtitleLabel = new Label(
                "Add a new material or update an existing material"
        );

        subtitleLabel.setStyle(
                "-fx-text-fill: #6b7280;"
                + "-fx-font-size: 13px;"
        );

        VBox heading = new VBox(5);

        heading.getChildren().addAll(
                titleLabel,
                subtitleLabel
        );

        GridPane formGrid = new GridPane();

        formGrid.setHgap(20);
        formGrid.setVgap(14);

        ColumnConstraints firstColumn =
                new ColumnConstraints();

        firstColumn.setPercentWidth(18);

        ColumnConstraints secondColumn =
                new ColumnConstraints();

        secondColumn.setPercentWidth(32);

        ColumnConstraints thirdColumn =
                new ColumnConstraints();

        thirdColumn.setPercentWidth(18);

        ColumnConstraints fourthColumn =
                new ColumnConstraints();

        fourthColumn.setPercentWidth(32);

        formGrid.getColumnConstraints().addAll(
                firstColumn,
                secondColumn,
                thirdColumn,
                fourthColumn
        );

        Label categoryLabel =
                createFormLabel("Category");

        Label materialNameLabel =
                createFormLabel("Material Name");

        Label descriptionLabel =
                createFormLabel("Description");

        Label unitLabel =
                createFormLabel("Measurement Unit");

        Label minimumStockLabel =
                createFormLabel("Minimum Stock");

        categoryComboBox.setPromptText(
                "Select category"
        );

        categoryComboBox.setMaxWidth(
                Double.MAX_VALUE
        );

        configureCategoryComboBox();

        materialNameField.setPromptText(
                "Enter material name"
        );

        materialNameField.setMaxWidth(
                Double.MAX_VALUE
        );

        descriptionArea.setPromptText(
                "Enter material description"
        );

        descriptionArea.setPrefRowCount(3);

        descriptionArea.setWrapText(true);

        descriptionArea.setMaxWidth(
                Double.MAX_VALUE
        );

        measurementUnitField.setPromptText(
                "Example: kg, ton, piece"
        );

        measurementUnitField.setMaxWidth(
                Double.MAX_VALUE
        );

        minimumStockField.setPromptText(
                "Example: 100"
        );

        minimumStockField.setMaxWidth(
                Double.MAX_VALUE
        );

        styleInput(categoryComboBox);
        styleInput(materialNameField);
        styleInput(descriptionArea);
        styleInput(measurementUnitField);
        styleInput(minimumStockField);

        formGrid.add(
                categoryLabel,
                0,
                0
        );

        formGrid.add(
                categoryComboBox,
                1,
                0
        );

        formGrid.add(
                materialNameLabel,
                2,
                0
        );

        formGrid.add(
                materialNameField,
                3,
                0
        );

        formGrid.add(
                descriptionLabel,
                0,
                1
        );

        formGrid.add(
                descriptionArea,
                1,
                1,
                3,
                1
        );

        formGrid.add(
                unitLabel,
                0,
                2
        );

        formGrid.add(
                measurementUnitField,
                1,
                2
        );

        formGrid.add(
                minimumStockLabel,
                2,
                2
        );

        formGrid.add(
                minimumStockField,
                3,
                2
        );

        Button saveButton = createActionButton(
                "Save Material",
                "#2563eb"
        );

        saveButton.setOnAction(
                event -> saveMaterial()
        );

        Button updateButton = createActionButton(
                "Update Material",
                "#059669"
        );

        updateButton.setOnAction(
                event -> updateMaterial()
        );

        Button clearButton = createActionButton(
                "Clear",
                "#6b7280"
        );

        clearButton.setOnAction(
                event -> clearForm()
        );

        HBox buttonBox = new HBox(12);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonBox.getChildren().addAll(
                saveButton,
                updateButton,
                clearButton
        );

        VBox card = createCard();

        card.setSpacing(18);

        card.getChildren().addAll(
                heading,
                new Separator(),
                formGrid,
                buttonBox
        );

        return card;
    }

    // =========================================================
    // CATEGORY COMBOBOX CONFIGURATION
    // =========================================================

    private void configureCategoryComboBox() {

        categoryComboBox.setCellFactory(
                comboBox -> new ListCell<>() {

                    @Override
                    protected void updateItem(
                            Category category,
                            boolean empty
                    ) {

                        super.updateItem(
                                category,
                                empty
                        );

                        if (empty || category == null) {

                            setText(null);

                        } else {

                            setText(
                                    safeString(
                                            category.getCategoryName()
                                    )
                            );
                        }
                    }
                }
        );

        categoryComboBox.setButtonCell(
                new ListCell<>() {

                    @Override
                    protected void updateItem(
                            Category category,
                            boolean empty
                    ) {

                        super.updateItem(
                                category,
                                empty
                        );

                        if (empty || category == null) {

                            setText(null);

                        } else {

                            setText(
                                    safeString(
                                            category.getCategoryName()
                                    )
                            );
                        }
                    }
                }
        );
    }

    // =========================================================
    // SEARCH CARD
    // =========================================================

    private VBox createSearchCard() {

        Label titleLabel = new Label(
                "Search Materials"
        );

        titleLabel.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #111827;"
        );

        searchField.setPromptText(
                "Search by material name, category, "
                + "or description"
        );

        styleInput(searchField);

        HBox.setHgrow(
                searchField,
                Priority.ALWAYS
        );

        Button searchButton = createActionButton(
                "Search",
                "#2563eb"
        );

        searchButton.setOnAction(
                event -> searchMaterials()
        );

        Button showAllButton = createActionButton(
                "Show All",
                "#4b5563"
        );

        showAllButton.setOnAction(event -> {

            searchField.clear();

            loadAllMaterials();
        });

        HBox searchBox = new HBox(12);

        searchBox.setAlignment(
                Pos.CENTER_LEFT
        );

        searchBox.getChildren().addAll(
                searchField,
                searchButton,
                showAllButton
        );

        VBox card = createCard();

        card.setSpacing(15);

        card.getChildren().addAll(
                titleLabel,
                searchBox
        );

        return card;
    }

    // =========================================================
    // TABLE CARD
    // =========================================================

    private VBox createTableCard() {

        Label titleLabel = new Label(
                "Material List"
        );

        titleLabel.setStyle(
                "-fx-font-size: 18px;"
                + "-fx-font-weight: bold;"
                + "-fx-text-fill: #111827;"
        );

        createTableColumns();

        materialTable.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        materialTable.setPrefHeight(430);

        materialTable.setFixedCellSize(38);

        materialTable.setPlaceholder(
                new Label("No materials found")
        );

        materialTable.setStyle(
                "-fx-background-color: white;"
                + "-fx-border-color: #d1d5db;"
                + "-fx-border-radius: 6;"
        );

        materialTable.setOnMouseClicked(event -> {

            if (event.getClickCount() == 2) {

                Material material =
                        materialTable
                                .getSelectionModel()
                                .getSelectedItem();

                if (material != null) {

                    selectMaterial(material);
                }
            }
        });

        Button loadButton = createActionButton(
                "Load Selected",
                "#2563eb"
        );

        loadButton.setOnAction(event -> {

            Material material =
                    materialTable
                            .getSelectionModel()
                            .getSelectedItem();

            if (material == null) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Selection Required",
                        "Please select a material from the table."
                );

                return;
            }

            selectMaterial(material);
        });

        Button statusButton = createActionButton(
                "Activate / Deactivate",
                "#d97706"
        );

        statusButton.setOnAction(
                event -> changeMaterialStatus()
        );

        HBox buttonBox = new HBox(12);

        buttonBox.setAlignment(
                Pos.CENTER_RIGHT
        );

        buttonBox.getChildren().addAll(
                loadButton,
                statusButton
        );

        VBox card = createCard();

        card.setSpacing(15);

        card.getChildren().addAll(
                titleLabel,
                materialTable,
                buttonBox
        );

        return card;
    }

    // =========================================================
    // TABLE COLUMNS
    // =========================================================

    private void createTableColumns() {

        materialTable.getColumns().clear();

        TableColumn<Material, Integer> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                cellData ->
                        new javafx.beans.property.SimpleObjectProperty<>(
                                cellData.getValue().getId()
                        )
        );

        idColumn.setPrefWidth(65);

        idColumn.setMinWidth(55);

        idColumn.setMaxWidth(80);

        idColumn.setStyle(
                "-fx-alignment: CENTER;"
        );

        applyIntegerCellStyle(idColumn);

        TableColumn<Material, String> categoryColumn =
                new TableColumn<>("Category");

        categoryColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                safeString(
                                        cellData.getValue()
                                                .getCategoryName()
                                )
                        )
        );

        categoryColumn.setPrefWidth(150);

        applyTextCellStyle(categoryColumn);

        TableColumn<Material, String> nameColumn =
                new TableColumn<>("Material Name");

        nameColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                safeString(
                                        cellData.getValue()
                                                .getMaterialName()
                                )
                        )
        );

        nameColumn.setPrefWidth(180);

        applyTextCellStyle(nameColumn);

        TableColumn<Material, String> descriptionColumn =
                new TableColumn<>("Description");

        descriptionColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                safeString(
                                        cellData.getValue()
                                                .getDescription()
                                )
                        )
        );

        descriptionColumn.setPrefWidth(240);

        applyTextCellStyle(descriptionColumn);

        TableColumn<Material, String> unitColumn =
                new TableColumn<>("Unit");

        unitColumn.setCellValueFactory(
                cellData ->
                        new SimpleStringProperty(
                                safeString(
                                        cellData.getValue()
                                                .getMeasurementUnit()
                                )
                        )
        );

        unitColumn.setPrefWidth(100);

        applyTextCellStyle(unitColumn);

        TableColumn<Material, String> minimumStockColumn =
                new TableColumn<>("Minimum Stock");

        minimumStockColumn.setCellValueFactory(
                cellData -> {

                    double minimumStock =
                            cellData.getValue()
                                    .getMinimumStock();

                    return new SimpleStringProperty(
                            String.format(
                                    Locale.US,
                                    "%.2f",
                                    minimumStock
                            )
                    );
                }
        );

        minimumStockColumn.setPrefWidth(130);

        applyTextCellStyle(minimumStockColumn);

        TableColumn<Material, String> statusColumn =
                new TableColumn<>("Status");

        statusColumn.setCellValueFactory(
                cellData -> {

                    boolean active =
                            cellData.getValue().isActive();

                    return new SimpleStringProperty(
                            active
                                    ? "Active"
                                    : "Inactive"
                    );
                }
        );

        statusColumn.setPrefWidth(100);

        statusColumn.setCellFactory(
                column -> new TableCell<>() {

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

                            setStyle(
                                    "-fx-alignment: CENTER;"
                            );

                        } else {

                            setText(item);

                            setAlignment(
                                    Pos.CENTER
                            );

                            if (item.equals("Active")) {

                                setStyle(
                                        "-fx-text-fill: #047857;"
                                        + "-fx-font-weight: bold;"
                                        + "-fx-alignment: CENTER;"
                                );

                            } else {

                                setStyle(
                                        "-fx-text-fill: #b91c1c;"
                                        + "-fx-font-weight: bold;"
                                        + "-fx-alignment: CENTER;"
                                );
                            }
                        }
                    }
                }
        );

        materialTable.getColumns().addAll(
                idColumn,
                categoryColumn,
                nameColumn,
                descriptionColumn,
                unitColumn,
                minimumStockColumn,
                statusColumn
        );
    }

    // =========================================================
    // TEXT CELL STYLING
    // =========================================================

    private <T> void applyTextCellStyle(
            TableColumn<Material, T> column
    ) {

        column.setCellFactory(
                tableColumn -> new TableCell<>() {

                    @Override
                    protected void updateItem(
                            T item,
                            boolean empty
                    ) {

                        super.updateItem(
                                item,
                                empty
                        );

                        if (empty || item == null) {

                            setText(null);

                            setStyle(
                                    "-fx-alignment: CENTER-LEFT;"
                            );

                        } else {

                            setText(
                                    String.valueOf(item)
                            );

                            setTextFill(
                                    javafx.scene.paint.Color.web(
                                            "#111827"
                                    )
                            );

                            setStyle(
                                    "-fx-alignment: CENTER-LEFT;"
                                    + "-fx-font-size: 13px;"
                            );
                        }
                    }
                }
        );
    }

    // =========================================================
    // INTEGER CELL STYLING
    // =========================================================

    private void applyIntegerCellStyle(
            TableColumn<Material, Integer> column
    ) {

        column.setCellFactory(
                tableColumn -> new TableCell<>() {

                    @Override
                    protected void updateItem(
                            Integer item,
                            boolean empty
                    ) {

                        super.updateItem(
                                item,
                                empty
                        );

                        if (empty || item == null) {

                            setText(null);

                            setStyle(
                                    "-fx-alignment: CENTER;"
                            );

                        } else {

                            setText(
                                    String.valueOf(item)
                            );

                            setTextFill(
                                    javafx.scene.paint.Color.web(
                                            "#111827"
                                    )
                            );

                            setStyle(
                                    "-fx-alignment: CENTER;"
                                    + "-fx-font-size: 13px;"
                            );
                        }
                    }
                }
        );
    }

    // =========================================================
    // LOAD CATEGORIES
    // =========================================================

    private void loadCategories() {

        List<Category> categories =
                categoryDAO.getAllCategories();

        ObservableList<Category> categoryList =
                FXCollections.observableArrayList(
                        categories
                );

        categoryComboBox.setItems(
                categoryList
        );
    }

    // =========================================================
    // LOAD ALL MATERIALS
    // =========================================================

    private void loadAllMaterials() {

        List<Material> materials =
                materialDAO.getAllMaterials();

        ObservableList<Material> materialList =
                FXCollections.observableArrayList(
                        materials
                );

        materialTable.setItems(
                materialList
        );
    }

    // =========================================================
    // SEARCH MATERIALS
    // =========================================================

    private void searchMaterials() {

        String keyword =
                safeString(
                        searchField.getText()
                ).trim();

        if (keyword.isEmpty()) {

            loadAllMaterials();

            return;
        }

        List<Material> materials =
                materialDAO.searchMaterials(
                        keyword
                );

        ObservableList<Material> materialList =
                FXCollections.observableArrayList(
                        materials
                );

        materialTable.setItems(
                materialList
        );
    }

    // =========================================================
    // SAVE MATERIAL
    // =========================================================

    private void saveMaterial() {

        if (!validateForm()) {

            return;
        }

        Category selectedCategory =
                categoryComboBox.getValue();

        double minimumStock =
                Double.parseDouble(
                        minimumStockField.getText()
                                .trim()
                );

        Material material = new Material(
                selectedCategory.getId(),
                materialNameField.getText()
                        .trim(),
                descriptionArea.getText()
                        .trim(),
                measurementUnitField.getText()
                        .trim(),
                minimumStock
        );

        boolean created =
                materialDAO.createMaterial(
                        material
                );

        if (created) {

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Material created successfully."
            );

            clearForm();

            loadAllMaterials();

        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Unable to create material. "
                    + "The material name may already exist "
                    + "under this category."
            );
        }
    }

    // =========================================================
    // UPDATE MATERIAL
    // =========================================================

    private void updateMaterial() {

        if (selectedMaterial == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Selection Required",
                    "Please select a material to update."
            );

            return;
        }

        if (!validateForm()) {

            return;
        }

        Category selectedCategory =
                categoryComboBox.getValue();

        double minimumStock =
                Double.parseDouble(
                        minimumStockField.getText()
                                .trim()
                );

        selectedMaterial.setCategoryId(
                selectedCategory.getId()
        );

        selectedMaterial.setMaterialName(
                materialNameField.getText()
                        .trim()
        );

        selectedMaterial.setDescription(
                descriptionArea.getText()
                        .trim()
        );

        selectedMaterial.setMeasurementUnit(
                measurementUnitField.getText()
                        .trim()
        );

        selectedMaterial.setMinimumStock(
                minimumStock
        );

        boolean updated =
                materialDAO.updateMaterial(
                        selectedMaterial
                );

        if (updated) {

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    "Material updated successfully."
            );

            clearForm();

            loadAllMaterials();

        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Unable to update material."
            );
        }
    }

    // =========================================================
    // SELECT MATERIAL
    // =========================================================

    private void selectMaterial(
            Material material
    ) {

        selectedMaterial = material;

        categoryComboBox
                .getSelectionModel()
                .clearSelection();

        for (Category category :
                categoryComboBox.getItems()) {

            if (category.getId()
                    == material.getCategoryId()) {

                categoryComboBox.setValue(
                        category
                );

                break;
            }
        }

        materialNameField.setText(
                safeString(
                        material.getMaterialName()
                )
        );

        descriptionArea.setText(
                safeString(
                        material.getDescription()
                )
        );

        measurementUnitField.setText(
                safeString(
                        material.getMeasurementUnit()
                )
        );

        minimumStockField.setText(
                String.format(
                        Locale.US,
                        "%.2f",
                        material.getMinimumStock()
                )
        );
    }

    // =========================================================
    // CHANGE MATERIAL STATUS
    // =========================================================

    private void changeMaterialStatus() {

        Material material =
                materialTable
                        .getSelectionModel()
                        .getSelectedItem();

        if (material == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Selection Required",
                    "Please select a material first."
            );

            return;
        }

        boolean newStatus =
                !material.isActive();

        boolean updated =
                materialDAO.updateMaterialStatus(
                        material.getId(),
                        newStatus
                );

        if (updated) {

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Success",
                    newStatus
                            ? "Material activated successfully."
                            : "Material deactivated successfully."
            );

            clearForm();

            loadAllMaterials();

        } else {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Error",
                    "Unable to update material status."
            );
        }
    }

    // =========================================================
    // VALIDATE FORM
    // =========================================================

    private boolean validateForm() {

        if (categoryComboBox.getValue() == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Please select a category."
            );

            return false;
        }

        String materialName =
                safeString(
                        materialNameField.getText()
                ).trim();

        if (materialName.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Please enter a material name."
            );

            return false;
        }

        if (materialName.length() > 150) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Material name cannot exceed 150 characters."
            );

            return false;
        }

        String measurementUnit =
                safeString(
                        measurementUnitField.getText()
                ).trim();

        if (measurementUnit.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Please enter a measurement unit."
            );

            return false;
        }

        String minimumStockText =
                safeString(
                        minimumStockField.getText()
                ).trim();

        if (minimumStockText.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Please enter the minimum stock."
            );

            return false;
        }

        try {

            double minimumStock =
                    Double.parseDouble(
                            minimumStockText
                    );

            if (Double.isNaN(minimumStock)
                    || Double.isInfinite(minimumStock)) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation Error",
                        "Please enter a valid stock value."
                );

                return false;
            }

            if (minimumStock < 0) {

                showAlert(
                        Alert.AlertType.WARNING,
                        "Validation Error",
                        "Minimum stock cannot be negative."
                );

                return false;
            }

        } catch (NumberFormatException exception) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Validation Error",
                    "Minimum stock must be a valid number."
            );

            return false;
        }

        return true;
    }

    // =========================================================
    // CLEAR FORM
    // =========================================================

    private void clearForm() {

        selectedMaterial = null;

        categoryComboBox
                .getSelectionModel()
                .clearSelection();

        materialNameField.clear();

        descriptionArea.clear();

        measurementUnitField.clear();

        minimumStockField.clear();

        materialTable
                .getSelectionModel()
                .clearSelection();
    }

    // =========================================================
    // CREATE FORM LABEL
    // =========================================================

    private Label createFormLabel(
            String text
    ) {

        Label label = new Label(text);

        label.setStyle(
                "-fx-font-weight: bold;"
                + "-fx-text-fill: #374151;"
                + "-fx-font-size: 13px;"
        );

        return label;
    }

    // =========================================================
    // CREATE ACTION BUTTON
    // =========================================================

    private Button createActionButton(
            String text,
            String backgroundColor
    ) {

        Button button = new Button(text);

        button.setStyle(
                "-fx-background-color: "
                + backgroundColor
                + ";"
                + "-fx-text-fill: white;"
                + "-fx-font-weight: bold;"
                + "-fx-padding: 10 20 10 20;"
                + "-fx-background-radius: 6;"
                + "-fx-cursor: hand;"
        );

        return button;
    }

    // =========================================================
    // CREATE CARD
    // =========================================================

    private VBox createCard() {

        VBox card = new VBox();

        card.setPadding(
                new Insets(22)
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
    // INPUT STYLE
    // =========================================================

    private void styleInput(
            Control control
    ) {

        control.setStyle(
                "-fx-background-color: #f9fafb;"
                + "-fx-border-color: #d1d5db;"
                + "-fx-border-radius: 5;"
                + "-fx-background-radius: 5;"
                + "-fx-padding: 8;"
                + "-fx-text-fill: #111827;"
        );
    }

    // =========================================================
    // SAFE STRING
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

        Alert alert = new Alert(
                alertType
        );

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }
}