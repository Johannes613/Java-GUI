module org.example.farmdelivery {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.farmdelivery to javafx.fxml;
    exports org.example.farmdelivery;
    exports model;
    exports app;
    opens app to javafx.fxml;
}