module com.mycompany.scrap.management.system {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.scrap.management.system
            to javafx.fxml;

    exports com.mycompany.scrap.management.system;
}