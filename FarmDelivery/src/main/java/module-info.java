module org.example.farmdelivery {
    requires javafx.controls;
    requires org.json;
    requires javafx.fxml;
    requires javafx.web;
    exports model;
    exports app;
    opens app to javafx.fxml;
}