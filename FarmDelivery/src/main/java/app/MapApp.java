package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
public class MapApp extends Application {
    private static String farmerLocation = "Abu Dhabi University";
    private static String customerLocation = "Sheikh Zayed Grand Mosque";
    public MapApp(String customerLoc) {
        customerLocation = customerLoc;
    }
    public MapApp(){}
    public MapApp(String farmerLoc, String customerLoc) {
        farmerLocation = farmerLoc;
        customerLocation = customerLoc;
    }
    @Override
    public void start(Stage primaryStage) {
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        engine.setJavaScriptEnabled(true);
        engine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                "(KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        engine.setOnError(event -> System.err.println("WebEngine Error: " + event.getMessage()));
        engine.setOnAlert(event -> System.out.println("Alert: " + event.getData()));
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == javafx.concurrent.Worker.State.SUCCEEDED) {
                System.out.println("Map loaded. Sending locations...");
                engine.executeScript("setFarmerLocation('" + farmerLocation.replace("'", "\\'") + "')");
                engine.executeScript("setCustomerLocation('" + customerLocation.replace("'", "\\'") + "')");
            }
        });
        try {
            String url = getClass().getResource("/delivery-map.html").toExternalForm();
            engine.load(url);
        } catch (Exception e) {
            System.err.println("Error loading HTML file: " + e.getMessage());
        }
        primaryStage.setScene(new Scene(webView, 1024, 768));
        primaryStage.setTitle("Delivery Tracking Map");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
    public void navigateToCartPage(Stage stage) throws Exception {
        CartPage cartPage = new CartPage();
        cartPage.start(stage);
    }
}

