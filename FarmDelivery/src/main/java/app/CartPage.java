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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Cart;
import model.Product;
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
    // Define common styles for input fields and buttons
    private final String inputFieldStyle = "-fx-padding: 12px; -fx-background-color: #F7F7F7; -fx-border-color: #E0E0E0; -fx-border-width: 1px; -fx-border-radius: 8px; -fx-font-size: 14px; -fx-prompt-text-fill: #BBBBBB;";
    private final String labelStyle = "-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #333333;";
    // Updated button styles for green color
    private final String buttonStyle = "-fx-background-color: linear-gradient(to right, #4CAF50, #8BC34A);" + // Green gradient
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0.5, 0, 3);";

    private final String buttonHoverStyle = "-fx-background-color: linear-gradient(to right, #409044, #7CB03D);" + // Darker green on hover
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 15 30;" +
            "-fx-background-radius: 8px;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0.6, 0, 4);";


    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(1200, 900); // Increased root height to accommodate all fields
        root.setStyle("-fx-background-color: #f4f7f6;"); // Light grey background

        Cart cart = new Cart();
        ArrayList<Product> productCards = cart.getCartItems();

        HBox mainHBox = new HBox(40); // Increased spacing between sections
        AnchorPane.setTopAnchor(mainHBox, 30.0); // More padding from top
        AnchorPane.setLeftAnchor(mainHBox, 30.0);
        AnchorPane.setRightAnchor(mainHBox, 30.0);
        AnchorPane.setBottomAnchor(mainHBox, 30.0);

        // Left Section - Checkout Form
        VBox leftSection = new VBox(20); // Spacing between form elements
        leftSection.setPadding(new Insets(30, 40, 30, 40)); // Generous padding
        leftSection.setPrefWidth(600);
        // Removed fixed prefHeight, allowing it to grow naturally with content
        leftSection.setStyle("-fx-background-color: #FFFFFF;" + // White background
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.5, 0, 0);"); // Subtle shadow

        Label title = new Label("Checkout");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setStyle("-fx-text-fill: #333333;");
        Label subtitle = new Label("Fill in the form below to complete your order");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setStyle("-fx-text-fill: #888888;");

        // Form Fields (using VBox for label and field, then adding to main VBox)
        // Name field
        Label userNameLabel = new Label("Name");
        userNameLabel.setStyle(labelStyle);
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter your name");
        userNameField.setStyle(inputFieldStyle);
        userNameField.setPrefHeight(45);
        VBox nameBox = new VBox(5, userNameLabel, userNameField);

        // Email field
        Label emailLabel = new Label("Email");
        emailLabel.setStyle(labelStyle);
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setStyle(inputFieldStyle);
        emailField.setPrefHeight(45);
        VBox emailBox = new VBox(5, emailLabel, emailField);

        // Phone Number field
        Label phoneNumberLabel = new Label("Phone");
        phoneNumberLabel.setStyle(labelStyle);
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Enter your phone number");
        phoneNumberField.setStyle(inputFieldStyle);
        phoneNumberField.setPrefHeight(45);
        VBox phoneBox = new VBox(5, phoneNumberLabel, phoneNumberField);

        // Address field (Added as per image)
        Label addressLabel = new Label("Address");
        addressLabel.setStyle(labelStyle);
        TextField addressField = new TextField();
        addressField.setPromptText("Enter your address");
        addressField.setStyle(inputFieldStyle);
        addressField.setPrefHeight(45);
        VBox addressBox = new VBox(5, addressLabel, addressField);
        addressField.setText("Abu Dhabi"); // Set default address

        // City field (Added as per image)
        Label cityLabel = new Label("City");
        cityLabel.setStyle(labelStyle);
        TextField cityField = new TextField();
        cityField.setPromptText("Enter your city");
        cityField.setStyle(inputFieldStyle);
        cityField.setPrefHeight(30);
        VBox cityBox = new VBox(5, cityLabel, cityField);

        // Location ComboBox (Original code already has this, styling it)
        Label locationLabel = new Label("Location");
        locationLabel.setStyle(labelStyle);
        ComboBox<String> locationComboBox = new ComboBox<>();
        locationComboBox.setPrefHeight(45);
        locationComboBox.setStyle(inputFieldStyle); // Apply common style
        locationComboBox.setEditable(true);
        locationComboBox.setPromptText("Start typing your location...");
        locationComboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (locationComboBox.isShowing()) return; // Ignore changes when the value is selected from suggestions
            if (newText.length() >= 3) {
                new Thread(() -> {
                    try {
                        List<String> suggestions = fetchLocationSuggestions(newText);
                        Platform.runLater(() -> {
                            locationComboBox.getItems().setAll(suggestions);
                            if (!suggestions.isEmpty()) {
                                locationComboBox.show();
                            } else {
                                locationComboBox.hide();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                Platform.runLater(locationComboBox::hide);
            }
        });
        VBox locationBox = new VBox(5, locationLabel, locationComboBox);


        // Complete Order Button
        Button completeOrderButton = new Button("Complete Order");
        completeOrderButton.setStyle(buttonStyle);
        completeOrderButton.setOnMouseEntered(e -> completeOrderButton.setStyle(buttonHoverStyle));
        completeOrderButton.setOnMouseExited(e -> completeOrderButton.setStyle(buttonStyle));
        completeOrderButton.setPrefWidth(Double.MAX_VALUE); // Fill width
        completeOrderButton.setPrefHeight(50); // Taller button

        // Track Order Button (Keep it hidden initially)
        Button trackOrderButton = new Button("Track Your Order");
        trackOrderButton.setStyle(buttonStyle);
        trackOrderButton.setOnMouseEntered(e -> trackOrderButton.setStyle(buttonHoverStyle));
        trackOrderButton.setOnMouseExited(e -> trackOrderButton.setStyle(buttonStyle));
        trackOrderButton.setPrefWidth(Double.MAX_VALUE);
        trackOrderButton.setPrefHeight(50);
        trackOrderButton.setVisible(false); // Initially hidden

        leftSection.getChildren().addAll(
                title, subtitle,
                nameBox,
                emailBox,
                phoneBox,
                cityBox,    // Added
                locationBox,
                // Removed paymentBox
                completeOrderButton,
                trackOrderButton
        );

        // Right Section - Order Summary
        VBox rightSection = new VBox(15); // Spacing between summary elements
        rightSection.setPadding(new Insets(30)); // Generous padding
        rightSection.setPrefWidth(600);
        rightSection.setStyle("-fx-background-color: #FFFFFF;" + // White background
                "-fx-background-radius: 10;" +
                "-fx-border-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.5, 0, 0);"); // Subtle shadow

        Label orderSummaryTitle = new Label("Order Summary");
        orderSummaryTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        orderSummaryTitle.setStyle("-fx-text-fill: #333333;");

        Label reviewOrderSubtitle = new Label("Review your order before completing the checkout");
        reviewOrderSubtitle.setFont(Font.font("Arial", 12));
        reviewOrderSubtitle.setStyle("-fx-text-fill: #888888;");
        VBox.setMargin(orderSummaryTitle, new Insets(0, 0, 0, 0)); // No extra margin needed here

        // ScrollPane for cart items
        ScrollPane cartScroll = new ScrollPane();
        cartScroll.setPrefHeight(450); // Fixed height for scrollable area
        cartScroll.setFitToWidth(true);
        cartScroll.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;"); // Make scroll pane transparent
        cartScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Show scrollbar only when needed
        cartScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        VBox cartContainer = new VBox(10); // Spacing between product cards
        cartContainer.setPadding(new Insets(10, 0, 10, 0)); // Padding inside scrollpane
        cartContainer.setStyle("-fx-background-color: transparent;"); // Ensure background transparency

        for (Product pdc : productCards) {
            if (pdc == null || pdc.getImageUrl() == null || pdc.getImageUrl().isEmpty()) {
                continue; // Skip if product or image URL is invalid
            }
            try {
                // Individual product card styling
                HBox productCard = new HBox(15); // Spacing between image and text
                productCard.setAlignment(Pos.CENTER_LEFT);
                productCard.setPadding(new Insets(10));
                productCard.setStyle("-fx-background-color: #FFFFFF;" +
                        "-fx-background-radius: 8;" +
                        "-fx-border-radius: 8;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 5, 0.3, 0, 0);");

                Image image = new Image(new File(pdc.getImageUrl()).toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;"); // Slightly rounded image corners

                VBox prodInfoVbox = new VBox(5);
                Label productLabel = new Label(pdc.getName());
                productLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));
                productLabel.setStyle("-fx-text-fill: #333333;");

                Label productQuantity = new Label("Quantity: " + pdc.getQuantity());
                productQuantity.setFont(Font.font("Arial", 13));
                productQuantity.setStyle("-fx-text-fill: #666666;");
                prodInfoVbox.getChildren().addAll(productLabel, productQuantity);

                Region spacer = new Region(); // Spacer to push price to the right
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Label productPrice = new Label("AED " + String.format("%.2f", pdc.getPrice() * pdc.getQuantity()));
                productPrice.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                productPrice.setStyle("-fx-text-fill: #333333;");

                productCard.getChildren().addAll(imageView, prodInfoVbox, spacer, productPrice);
                cartContainer.getChildren().add(productCard);

            } catch (Exception e) {
                System.out.println("Error displaying product card: " + e.getMessage());
            }
        }
        cartScroll.setContent(cartContainer);

        // Total Price
        Label totalPriceLabel = new Label("Total: AED " + String.format("%.2f", cart.getTotalPrice()));
        totalPriceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        totalPriceLabel.setStyle("-fx-text-fill: #333333;");
        HBox totalPriceBox = new HBox();
        totalPriceBox.setAlignment(Pos.CENTER_RIGHT); // Align total price to the right
        totalPriceBox.getChildren().add(totalPriceLabel);
        VBox.setMargin(totalPriceBox, new Insets(10, 0, 20, 0)); // Margin below total price

        // Continue Shopping Button
        Button continueShoppingBtn = new Button("Continue Shopping");
        continueShoppingBtn.setStyle(buttonStyle);
        continueShoppingBtn.setOnMouseEntered(e -> continueShoppingBtn.setStyle(buttonHoverStyle));
        continueShoppingBtn.setOnMouseExited(e -> continueShoppingBtn.setStyle(buttonStyle));
        continueShoppingBtn.setPrefWidth(Double.MAX_VALUE); // Fill width
        continueShoppingBtn.setPrefHeight(50); // Taller button


        rightSection.getChildren().addAll(orderSummaryTitle, reviewOrderSubtitle, cartScroll, totalPriceBox, continueShoppingBtn);

        // Add both sections to HBox
        mainHBox.getChildren().addAll(leftSection, rightSection);
        root.getChildren().add(mainHBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Checkout"); // Changed title to Checkout
        primaryStage.show();

        // --- Event Handlers (Unchanged Functionality) ---
        completeOrderButton.setOnAction(e -> {
            // Check if cart is empty
            if (cart.getCartItems().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Your cart is empty. Please add items before completing your order.");
                alert.show();
                return; // Stop the order process
            }

            if (!locationComboBox.getItems().contains(locationComboBox.getEditor().getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a valid location from the suggestions.");
                alert.show();
                return;
            }

            // Validating all new fields
            if (userNameField.getText().isEmpty() || phoneNumberField.getText().isEmpty() || emailField.getText().isEmpty() ||
                    addressField.getText().isEmpty() || cityField.getText().isEmpty()) { // Removed payment method check
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill all required fields to complete your order.");
                alert.show();
                return;
            }

            cart.setCartItemsClear(); // Clear cart logic
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
                cartContainer.getChildren().clear(); // Clear displayed cart items
                userNameField.setText("");
                emailField.setText("");
                phoneNumberField.setText("");
                addressField.setText(""); // Clear address
                cityField.setText("");      // Clear city
                locationComboBox.setValue(null); // Clear location combo box
                // paymentMethodComboBox.setValue(null); // Removed since the section is removed

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Items ordered successfully! We will contact you soon!");
                alert.show();
                trackOrderButton.setVisible(true);
                completeOrderButton.setVisible(false);
            } catch (IOException ex) {
                ex.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "An error occurred during order processing.");
                alert.show();
            }
        });

        trackOrderButton.setOnAction(e -> {
            DeliveryMapSimulator deliveryMapSimulator = new DeliveryMapSimulator();
            try {
                deliveryMapSimulator.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        continueShoppingBtn.setOnAction(e -> {
            // Assuming ShoppingPage is correctly implemented and handles scene switching
            ShoppingPage shp = new ShoppingPage();
            try {
                shp.start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("Continue Shopping button clicked");
        });
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