module org.example.farmdelivery {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.json;

    exports model;
    exports app;

    opens app to javafx.fxml;
}
