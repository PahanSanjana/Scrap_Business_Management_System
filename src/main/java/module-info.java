module com.mycompany.scrap.management.system {

    // JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // Database support
    requires java.sql;

    // Allow JavaFX FXML to access application classes
    opens com.mycompany.scrap.management.system
            to javafx.fxml;

    // Export application package
    exports com.mycompany.scrap.management.system;
}