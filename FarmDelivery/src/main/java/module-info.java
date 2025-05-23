module org.example.farmdelivery {
    requires javafx.controls;
    requires javafx.fxml;
    exports model;
    exports app;
    opens app to javafx.fxml;
}