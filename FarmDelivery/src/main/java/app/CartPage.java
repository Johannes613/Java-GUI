package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Cart;
import model.Product;
//import org.example.farmdelivery.ProductCard;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
public class CartPage extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(1200, 800);
        Cart cart=new Cart();
        ArrayList<Product> productCards=cart.getCartItems();

        HBox mainHBox = new HBox(20);
        AnchorPane.setTopAnchor(mainHBox, 20.0);
        AnchorPane.setLeftAnchor(mainHBox, 20.0);
        AnchorPane.setRightAnchor(mainHBox, 20.0);
        AnchorPane.setBottomAnchor(mainHBox, 20.0);
        // Left Section
        VBox leftSection = new VBox();
        leftSection.setAlignment(Pos.CENTER_LEFT);
        leftSection.setPadding(new Insets(25));
        leftSection.setSpacing(10);
        leftSection.setPrefWidth(600);
        leftSection.setStyle("-fx-background-color: #f0f0f0;"); // Example styling
        Label title = new Label("Oder Information details");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        VBox.setMargin(title, new Insets(60, 0, 20, 0));
        Label loginHeader = new Label("Farm Delivery Login");
        loginHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        VBox.setMargin(loginHeader, new Insets(0, 0, 60, 0));
//         userName field
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Your Name");
        userNameField.setPadding(new Insets(10));
        VBox userNameBox = new VBox(20, new Label("Username"), userNameField);
        userNameBox.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(userNameBox, new Insets(0, 0, 30, 0));
//phone number field
        TextField phoneNumber = new TextField();
        phoneNumber.setPromptText("Enter Your Phone Number");
//        TextField locationField = new TextField();
        ComboBox<String> locationComboBox = new ComboBox<>();
        locationComboBox.setPrefWidth(400);
        locationComboBox.setPrefHeight(40);
//        locationComboBox.setPadding(new Insets(15));
        locationComboBox.setEditable(true);
        locationComboBox.setPromptText("Start typing your location...");
        locationComboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            // Ignore changes when the value is selected from suggestions
            if (locationComboBox.isShowing()) return;
            if (newText.length() >= 3) {
                new Thread(() -> {
                    try {
                        List<String> suggestions = fetchLocationSuggestions(newText);
                        Platform.runLater(() -> {
                            locationComboBox.getItems().setAll(suggestions);
                            locationComboBox.show();
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        VBox locationBox = new VBox(20, new Label("Location"),locationComboBox);

        phoneNumber.setPadding(new Insets(10));
        VBox phoneNumberBox = new VBox(20, new Label("Phone Number"), phoneNumber);
        phoneNumberBox.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(phoneNumberBox, new Insets(0, 0, 30, 0));
        //email address
        TextField email = new TextField();
        email.setPromptText("Enter Your Email");
        email.setPadding(new Insets(10));
        VBox emailAddressBox = new VBox(20, new Label("Email Address"), email);
        emailAddressBox.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(emailAddressBox, new Insets(0, 0, 30, 0));
//        payment method selection
        ComboBox<String> paymentMethod = new ComboBox<>();
        paymentMethod.getItems().addAll("Cash", "Bank Transfer","Credit Card");
        paymentMethod.setPromptText("Cash");
        paymentMethod.setPadding(new Insets(10));
        paymentMethod.setPrefWidth(185);
        VBox paymentComboBox = new VBox(20, new Label("Select Payment Method:"), paymentMethod);
        paymentComboBox.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(paymentComboBox, new Insets(0, 0, 30, 0));
//        Complete Order Button
        Button completeOrder = new Button("Complete Order");
        completeOrder.setPadding(new Insets(10));
        completeOrder.setPrefWidth(400);
        completeOrder.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        Button trackOrder = new Button("Track Your Order");
        trackOrder.setPadding(new Insets(10));
        trackOrder.setPrefWidth(400);
        trackOrder.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        trackOrder.setVisible(false);

        VBox orderBtns = new VBox(0, completeOrder,trackOrder);
        VBox.setMargin(orderBtns,new Insets(50,0,0,0));

        leftSection.getChildren().addAll(title,userNameBox,phoneNumberBox,emailAddressBox,locationBox,orderBtns);
        // Right Section
        VBox rightSection = new VBox();
        rightSection.setAlignment(Pos.CENTER_LEFT);
        rightSection.setSpacing(10);
        rightSection.setPrefWidth(600);
        //cart details label
        Label label = new Label("Cart Product details");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label totalPriceLabel=new Label("Total Price: AED " + cart.getTotalPrice());
        totalPriceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox totalPriceBox=new HBox(totalPriceLabel);

        int total=6;
        ScrollPane cartScroll = new ScrollPane();
        cartScroll.setPrefHeight(600);
        cartScroll.setPadding(new Insets(20));
        cartScroll.setFitToWidth(true);
        VBox cartContainer = new VBox();
        cartContainer.setSpacing(10);
        for(Product pdc:productCards) {
            String imgPath= pdc.getImageUrl();
            Image image = new Image(new File(imgPath).toURI().toString());
            System.out.println("Image Path: " + imgPath);
            ImageView imageView =new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            Label productLabel = new Label(pdc.getName());
            productLabel.setTextFill(Color.WHITE);
            Label productPrice = new Label("AED " + (pdc.getPrice()*pdc.getQuantity()));
            productPrice.setTextFill(Color.WHITE);
            Label productQuantity = new Label("Quantity: " + pdc.getQuantity());
            productQuantity.setTextFill(Color.WHITE);
            VBox prodInfoVbox = new VBox(productLabel, productPrice, productQuantity);
            prodInfoVbox.setPadding(new Insets(20));
            prodInfoVbox.setSpacing(10);
            HBox cartProdInfo = new HBox(imageView, prodInfoVbox);
            cartProdInfo.setSpacing(20);
            VBox cartVbox = new VBox(cartProdInfo);
            cartVbox.setBackground(new Background(new BackgroundFill(Color.rgb(174, 173, 107), new CornerRadii(15), Insets.EMPTY)));
            cartVbox.setPadding(new Insets(10));
            cartVbox.setPrefWidth(500);
            cartContainer.getChildren().add(cartVbox);
        }
        cartScroll.setContent(cartContainer);
        completeOrder.setOnAction(e -> {
            if (!locationComboBox.getItems().contains(locationComboBox.getEditor().getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a valid location from the suggestions.");
                alert.show();
                return;
            }

            if (userNameField.getText().isEmpty() || phoneNumber.getText().isEmpty() || email.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill the form");
                alert.show();
                return;
            }
            cart.setCartItemsClear();
            try {
                String[] currentUserCred;
                try (BufferedReader credReader = new BufferedReader(new FileReader("current_user.txt"))) {
                    String line = credReader.readLine();
                    if (line == null || line.isEmpty()) throw new IOException("User not logged in.");
                    currentUserCred = line.trim().split(",");
                }
                try (
                        BufferedReader cartReader = new BufferedReader(new FileReader("cart.txt"));
                        BufferedWriter orderWriter = new BufferedWriter(new FileWriter("orders.txt", true))
                ) {
                    String line;
                    while ((line = cartReader.readLine()) != null) {
                        orderWriter.write(line);
                        orderWriter.newLine();
                    }
                }
                File cartFile = new File("cart.txt");
                File tempFile = new File("temp_cart.txt");
                try (
                        BufferedReader cartReader = new BufferedReader(new FileReader(cartFile));
                        BufferedWriter tempWriter = new BufferedWriter(new FileWriter(tempFile))
                ) {
                    String line;
                    while ((line = cartReader.readLine()) != null) {
                        String[] orderData = line.split(",");
                        if (orderData.length < 2) continue;
                        if (!orderData[0].trim().equals(currentUserCred[0].trim()) ||
                                !orderData[1].trim().equals(currentUserCred[1].trim())) {
                            tempWriter.write(line);
                            tempWriter.newLine();
                        }
                    }
                }
                if (cartFile.delete()) {
                    if (!tempFile.renameTo(cartFile)) {
                        System.out.println("Failed to rename temp file to cart.txt");
                    }
                } else {
                    System.out.println("Failed to delete original cart.txt");
                }
                cartContainer.getChildren().clear();
                userNameField.setText("");
                email.setText("");
                phoneNumber.setText("");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Items ordered successfully! We will contact you soon!");
                alert.show();
                trackOrder.setVisible(true);
                completeOrder.setVisible(false);
            } catch (IOException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred during order processing.");
                alert.show();
            }
        });
        trackOrder.setOnAction(e->{
            MapApp mapApp = new MapApp(locationComboBox.getValue());
            try {
                mapApp.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Button gotoShoppingBtn = new Button("Goto Shopping");
        gotoShoppingBtn.setPrefWidth(200);
        gotoShoppingBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        gotoShoppingBtn.setPadding(new Insets(10));
        gotoShoppingBtn.setOnAction(e -> {
//            handleLogin(e);
            ShoppingPage shp=new ShoppingPage();
            shp.start(primaryStage);
            System.out.println("Login button clicked");
        });
        rightSection.getChildren().addAll(label,totalPriceBox,cartScroll,gotoShoppingBtn);
        // Add both sections to HBox
        mainHBox.getChildren().addAll(leftSection, rightSection);
        root.getChildren().add(mainHBox);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cart Page");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
    public static class CartApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            new CartPage().start(primaryStage);
        }
    }
    private List<String> fetchLocationSuggestions(String query) throws IOException {
        String apiKey = "pk.efa12dc8917606ab9e0edadb149c6e5d";
        String urlString = "https://api.locationiq.com/v1/autocomplete?key=" + apiKey +
                "&q=" + URLEncoder.encode(query, "UTF-8") + "&limit=5&format=json";
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return parseSuggestions(response.toString());
    }
    private List<String> parseSuggestions(String json) {
        List<String> results = new ArrayList<>();
        JSONArray arr = new JSONArray(json);
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            results.add(obj.getString("display_name"));
        }
        return results;
    }

}