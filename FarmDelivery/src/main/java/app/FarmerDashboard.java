package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Exceptions.HarvestDateException;
import model.Order;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Product;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects; // Added for Objects.requireNonNull
import java.util.Scanner;

public class FarmerDashboard extends Application {
    private ArrayList<Product> productList = new ArrayList<>();
    private ArrayList<Order> ordersList = new ArrayList<>();
    private ArrayList<Product> orderProductList = new ArrayList<>();
    private VBox subscriberContent = new VBox(15);
    private ScrollPane mainContentScrollPane; // Added ScrollPane for main content

    @Override
    public void start(Stage primaryStage) {
        // Root layout
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: #f8f8f8;"); // Overall light background

        // Header section (Top Bar)
        Label headerLabel = new Label("Farmer Dashboard");
        headerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");
        HBox header = new HBox(15); // Increased spacing
        header.setPadding(new Insets(15, 20, 15, 20)); // More balanced padding
        header.setAlignment(Pos.CENTER_LEFT); // Align to center-left
        header.setStyle("-fx-background-color: #2e7d32; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0.5, 0, 0);"); // Dark green with a subtle shadow

        // Placeholder for logo image, assuming path or removing if not available
        ImageView logoImage = new ImageView();
        // If you have a logo, ensure the path is correct. Example:
        // try {
        //     Image logo = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
        //     logoImage.setImage(logo);
        // } catch (NullPointerException e) {
        //     System.err.println("Logo image not found: /images/logo.png");
        // }
        logoImage.setFitWidth(40);
        logoImage.setFitHeight(40);
        logoImage.setStyle("-fx-border-color: #ffffff; -fx-border-width: 1px; -fx-border-radius: 5px;"); // Small border for placeholder

        header.getChildren().addAll(logoImage, headerLabel);
        layout.setTop(header);

        // Side Navigation Bar
        VBox sideNav = new VBox(12); // Slightly more spacing
        sideNav.setPadding(new Insets(25, 10, 25, 10)); // Adjusted padding
        sideNav.setPrefWidth(220); // Wider for better button text display
        sideNav.setStyle("-fx-background-color: #ffffff; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.5, 0, 0);"); // White, rounded, with shadow
        sideNav.setAlignment(Pos.TOP_CENTER); // Align buttons to center

        // Style for navigation buttons
        String navBtnStyle = "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 12 20; -fx-background-radius: 8; -fx-cursor: hand;";
        String navBtnHoverStyle = "-fx-background-color: #388E3C; -fx-scale: 1.02;"; // Darker green on hover, slight scale effect

        Button addProductBtn = new Button("Add Product");
        Button listProductBtn = new Button("List Products");
        Button ordersBtn = new Button("Orders");
        Button subscribersBtn = new Button("Subscribers");
        Button logoutBtn = new Button("Log Out");

        Button[] navButtons = {addProductBtn, listProductBtn, ordersBtn, subscribersBtn, logoutBtn};
        for (Button eachBtn : navButtons) {
            eachBtn.setMaxWidth(Double.MAX_VALUE); // Make buttons fill width
            eachBtn.setPrefHeight(45); // Taller buttons for easier clicking
            eachBtn.setStyle(navBtnStyle);
            eachBtn.setOnMouseEntered(e -> eachBtn.setStyle(navBtnHoverStyle));
            eachBtn.setOnMouseExited(e -> eachBtn.setStyle(navBtnStyle));
        }
        sideNav.getChildren().addAll(addProductBtn, listProductBtn, ordersBtn, subscribersBtn, logoutBtn);
        layout.setLeft(sideNav);

        // Main content area - wrapped in a ScrollPane
        mainContentScrollPane = new ScrollPane();
        mainContentScrollPane.setFitToWidth(true);
        mainContentScrollPane.setFitToHeight(true);
        mainContentScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;"); // Transparent background
        layout.setCenter(mainContentScrollPane);


        // Add Product Content
        VBox addProductContent = new VBox(20); // Increased spacing
        addProductContent.setPadding(new Insets(30));
        addProductContent.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10;"); // White background, rounded corners

        Label formTitle = new Label("Add New Product");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28)); // Larger, bolder title
        formTitle.setStyle("-fx-text-fill: #2e7d32; -fx-underline: true;"); // Green with underline

        // Styling for form labels
        String formLabelStyle = "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333333;"; // Darker, bolder labels

        // Styling for TextFields, TextAreas, and ComboBoxes
        String inputFieldStyle = "-fx-font-size: 15px; -fx-padding: 10px; -fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 1; -fx-border-radius: 8; -fx-background-radius: 8;";
        String inputFieldFocusStyle = "-fx-border-color: #4CAF50; -fx-background-color: #e8f5e9;"; // Green border on focus/hover

        TextField nameField = new TextField();
        nameField.setPromptText("Enter product name");
        nameField.setStyle(inputFieldStyle);
        nameField.focusedProperty().addListener((obs, oldVal, newVal) -> nameField.setStyle(newVal ? inputFieldStyle + inputFieldFocusStyle : inputFieldStyle));
        Label nameLabel = new Label("Product Name:");
        nameLabel.setStyle(formLabelStyle);

        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter quantity (e.g., 50)");
        quantityField.setStyle(inputFieldStyle);
        quantityField.focusedProperty().addListener((obs, oldVal, newVal) -> quantityField.setStyle(newVal ? inputFieldStyle + inputFieldFocusStyle : inputFieldStyle));
        Label quantityLabelInput = new Label("Quantity:");
        quantityLabelInput.setStyle(formLabelStyle);

        Label categoryLabel = new Label("Product Category:");
        categoryLabel.setStyle(formLabelStyle);
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(
                "Milk Products", "Fruits", "Vegetables", "Cereal Products", "Cereal", "Meat & Poultry",
                "Fish & Seafood", "Bakery Products", "Eggs", "Spices & Condiments", "Oils & Fats", "Snacks & Chips",
                "Beverages", "Dry Fruits & Nuts", "Grains & Pulses", "Legumes", "Herbs", "Frozen Foods",
                "Organic Products", "Dairy Alternatives", "Baby Products", "Health & Nutrition", "Cleaning Supplies",
                "Personal Care", "Pet Supplies", "Ready-to-Eat Foods", "Canned Goods", "Sauces & Pickles", "Flours", "Sugars & Sweeteners"
        );
        categoryComboBox.setPromptText("Select Product Category");
        categoryComboBox.setStyle(inputFieldStyle);
        categoryComboBox.setPrefWidth(250); // Set a reasonable preferred width
        categoryComboBox.setOnMouseEntered(e -> categoryComboBox.setStyle(inputFieldStyle + inputFieldFocusStyle));
        categoryComboBox.setOnMouseExited(e -> categoryComboBox.setStyle(inputFieldStyle));


        Label seasonLabel = new Label("Select Season:");
        seasonLabel.setStyle(formLabelStyle);
        ComboBox<String> seasonComboBox = new ComboBox<>();
        seasonComboBox.getItems().addAll("Spring", "Summer", "Autumn", "Winter");
        seasonComboBox.setPromptText("Select Season");
        seasonComboBox.setStyle(inputFieldStyle);
        seasonComboBox.setPrefWidth(250); // Set a reasonable preferred width
        seasonComboBox.setOnMouseEntered(e -> seasonComboBox.setStyle(inputFieldStyle + inputFieldFocusStyle));
        seasonComboBox.setOnMouseExited(e -> seasonComboBox.setStyle(inputFieldStyle));

        Label descLabel = new Label("Description:");
        descLabel.setStyle(formLabelStyle);
        TextArea descArea = new TextArea();
        descArea.setPromptText("Enter product description (e.g., Fresh organic tomatoes from our farm...)");
        descArea.setPrefRowCount(4);
        descArea.setWrapText(true); // Enable text wrapping
        descArea.setStyle(inputFieldStyle);
        descArea.focusedProperty().addListener((obs, oldVal, newVal) -> descArea.setStyle(newVal ? inputFieldStyle + inputFieldFocusStyle : inputFieldStyle));


        Label priceLabel = new Label("Product Price (AED):");
        priceLabel.setStyle(formLabelStyle);
        TextField priceField = new TextField();
        priceField.setPromptText("Enter product price (e.g., 15.50)");
        priceField.setStyle(inputFieldStyle);
        priceField.focusedProperty().addListener((obs, oldVal, newVal) -> priceField.setStyle(newVal ? inputFieldStyle + inputFieldFocusStyle : inputFieldStyle));


        Label selectDateLabel = new Label("Select Harvest Date:");
        selectDateLabel.setStyle(formLabelStyle);
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Harvest Date");
        datePicker.setStyle(inputFieldStyle); // Apply consistent input field style
        datePicker.setPrefWidth(250);
        datePicker.setOnMouseEntered(e -> datePicker.setStyle(inputFieldStyle + inputFieldFocusStyle));
        datePicker.setOnMouseExited(e -> datePicker.setStyle(inputFieldStyle));


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image of the Product");
        String[] filePath = new String[1];
        filePath[0] = ""; // Initialize to empty string

        Label imageUploadLabel = new Label("Upload Product Image:");
        imageUploadLabel.setStyle(formLabelStyle);
        Button imgUploadBtn = new Button("Upload Image");
        imgUploadBtn.setStyle("-fx-background-color: #388e3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-padding: 8 18; -fx-background-radius: 8; -fx-cursor: hand;");
        imgUploadBtn.setOnMouseEntered(e -> imgUploadBtn.setStyle("-fx-background-color: #2e7d32; -fx-scale: 1.02;"));
        imgUploadBtn.setOnMouseExited(e -> imgUploadBtn.setStyle("-fx-background-color: #388e3c;"));

        ImageView selectedImageView = new ImageView();
        selectedImageView.setFitWidth(100);
        selectedImageView.setFitHeight(100);
        selectedImageView.setPreserveRatio(true);
        selectedImageView.setStyle("-fx-border-color: #c5e1a5; -fx-border-width: 1;"); // Border for image preview
        selectedImageView.setVisible(false); // Initially hidden

        imgUploadBtn.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage); // Use primaryStage for FileChooser
            if (selectedFile != null) {
                filePath[0] = selectedFile.getAbsolutePath();
                imgUploadBtn.setText("Image Selected!");
                Image previewImage = new Image(selectedFile.toURI().toString());
                selectedImageView.setImage(previewImage);
                selectedImageView.setVisible(true); // Show image preview
            }
        });
        HBox imageUploadBox = new HBox(10, imgUploadBtn, selectedImageView);
        imageUploadBox.setAlignment(Pos.CENTER_LEFT);

        Button saveBtn = new Button("Save Product");
        saveBtn.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 12 25; -fx-background-radius: 10; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);");
        saveBtn.setOnMouseEntered(e -> saveBtn.setStyle("-fx-background-color: #218838; -fx-scale: 1.02; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 12 25; -fx-background-radius: 10; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 7, 0, 0, 3);"));
        saveBtn.setOnMouseExited(e -> saveBtn.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px; -fx-padding: 12 25; -fx-background-radius: 10; -fx-cursor: hand; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);"));

        saveBtn.setOnAction(e -> {
            try {
                String productCategory = categoryComboBox.getValue();
                String season = seasonComboBox.getValue();
                String name = nameField.getText();
                LocalDate dateSelected1 = datePicker.getValue();
                if (dateSelected1.isBefore(LocalDate.now())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Date");
                    alert.setHeaderText("The selected date cannot be in the past.");
                    alert.setContentText("Please choose a current or future date to proceed.");
                    alert.showAndWait();
                    new HarvestDateException("Invalid date selected");
                    return; // Stop the submission
                }
                // Check for empty fields or null selections
                if (name.isEmpty() || quantityField.getText().isEmpty() || priceField.getText().isEmpty() ||
                        productCategory == null || season == null || descArea.getText().isEmpty() || datePicker.getValue() == null ||
                        filePath[0].isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all fields and upload an image.");
                    alert.show();
                    return;
                }

                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                String imgUrl = filePath[0]; // Make sure this is properly set
                int productId = getNumOfProducts() + 1; // Assuming getNumOfProducts() works
                String desc = descArea.getText();
                LocalDate dateSelected = datePicker.getValue();

                Product product = new Product(productId, name, desc, price, quantity, dateSelected, imgUrl, season, productCategory);
                saveProduct(product); // Assuming saveProduct() works
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product saved successfully!");
                alert.show();

                // Clear form fields after successful save
                nameField.clear();
                quantityField.clear();
                categoryComboBox.getSelectionModel().clearSelection();
                seasonComboBox.getSelectionModel().clearSelection();
                descArea.clear();
                priceField.clear();
                datePicker.setValue(null);
                filePath[0] = "";
                imgUploadBtn.setText("Upload Image");
                selectedImageView.setImage(null);
                selectedImageView.setVisible(false);

            } catch (NumberFormatException ex) {
                System.out.println("Incorrect Number format: " + ex.getMessage());
                Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid Number format for price or quantity.");
                alert.show();
            } catch (NullPointerException ex) {
                System.out.println("Null Pointer Exception: " + ex.getMessage());
                Alert alert = new Alert(Alert.AlertType.WARNING, "Please ensure all fields are filled, especially image upload and date selection.");
                alert.show();
            }
        });


        // Arrange elements in a GridPane for better form alignment
        GridPane addProductGrid = new GridPane();
        addProductGrid.setVgap(15);
        addProductGrid.setHgap(15);
        addProductGrid.setPadding(new Insets(20)); // Padding around the grid
        addProductGrid.setAlignment(Pos.TOP_CENTER); // Center the grid

        int row = 0;
        addProductGrid.add(formTitle, 0, row++, 2, 1); // Span title across two columns
        GridPane.setHalignment(formTitle, javafx.geometry.HPos.CENTER);
        addProductGrid.add(nameLabel, 0, row);
        addProductGrid.add(nameField, 1, row++);
        addProductGrid.add(quantityLabelInput, 0, row);
        addProductGrid.add(quantityField, 1, row++);
        addProductGrid.add(categoryLabel, 0, row);
        addProductGrid.add(categoryComboBox, 1, row++);
        addProductGrid.add(seasonLabel, 0, row);
        addProductGrid.add(seasonComboBox, 1, row++);
        addProductGrid.add(descLabel, 0, row);
        addProductGrid.add(descArea, 1, row++);
        addProductGrid.add(priceLabel, 0, row);
        addProductGrid.add(priceField, 1, row++);
        addProductGrid.add(selectDateLabel, 0, row);
        addProductGrid.add(datePicker, 1, row++);
        addProductGrid.add(imageUploadLabel, 0, row);
        addProductGrid.add(imageUploadBox, 1, row++);
        addProductGrid.add(saveBtn, 0, row, 2, 1); // Span save button
        GridPane.setHalignment(saveBtn, javafx.geometry.HPos.CENTER);

        addProductContent.getChildren().add(addProductGrid);


        // List Product Content
        VBox listProductContent = new VBox(20);
        listProductContent.setPadding(new Insets(30));
        Label listProductTitle = new Label("Your Products");
        listProductTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        listProductTitle.setStyle("-fx-text-fill: #2e7d32; -fx-underline: true;");

        FlowPane productCardsFlowPane = new FlowPane();
        productCardsFlowPane.setHgap(20);
        productCardsFlowPane.setVgap(20);
        productCardsFlowPane.setAlignment(Pos.TOP_LEFT);

        listProductContent.getChildren().addAll(listProductTitle, productCardsFlowPane);


        // Orders Content
        VBox orderProductContent = new VBox(20);
        orderProductContent.setPadding(new Insets(30));
        Label listOrdersTitle = new Label("Customer Orders");
        listOrdersTitle.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        listOrdersTitle.setStyle("-fx-text-fill: #2e7d32; -fx-underline: true;");

        FlowPane orderCardsFlowPane = new FlowPane();
        orderCardsFlowPane.setHgap(20);
        orderCardsFlowPane.setVgap(20);
        orderCardsFlowPane.setAlignment(Pos.TOP_LEFT);

        orderProductContent.getChildren().addAll(listOrdersTitle, orderCardsFlowPane);


        // Subscribers Content
        subscriberContent.setPadding(new Insets(30));
        subscriberContent.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10;"); // White background, rounded corners
        // Ensure subscriberContent is scrollable if many subscribers
        ScrollPane subscriberScrollPane = new ScrollPane(subscriberContent);
        subscriberScrollPane.setFitToWidth(true);
        subscriberScrollPane.setFitToHeight(true);
        subscriberScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-insets: 0;");


        // Event Handlers for Side Navigation
        addProductBtn.setOnAction(e -> mainContentScrollPane.setContent(addProductContent)); // Set content in scroll pane
        listProductBtn.setOnAction(e -> {
            fetchProducts(); // Keep existing logic for fetching
            productCardsFlowPane.getChildren().clear(); // Clear previous cards
            for(Product eachProduct: productList){
                // Product Card Styling for List Products
                VBox productCard = new VBox(5);
                productCard.setPrefSize(200, 250); // Fixed size for consistent cards
                productCard.setAlignment(Pos.TOP_CENTER);
                productCard.setPadding(new Insets(10));
                productCard.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 10; -fx-border-color: #c5e1a5; -fx-border-width: 1; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0.5, 0, 0);");

                ImageView imageView = new ImageView();
                if (eachProduct.getImageUrl() != null && !eachProduct.getImageUrl().isEmpty()) {
                    try {
                        Image image = new Image(new File(eachProduct.getImageUrl()).toURI().toString());
                        imageView.setImage(image);
                        imageView.setFitWidth(180);
                        imageView.setFitHeight(120);
                        imageView.setPreserveRatio(false); // Fill the space
                        imageView.setStyle("-fx-border-radius: 8;"); // Rounded image corners
                    } catch (Exception ex) {
                        System.err.println("Error loading image for product " + eachProduct.getName() + ": " + ex.getMessage());
                        // Fallback: display a placeholder or skip image
                    }
                }

                Label productNameLabel = new Label(eachProduct.getName());
                productNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;"); // Green text
                productNameLabel.setWrapText(true);
                productNameLabel.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

                Label priceLabelCard = new Label("Price: AED " + String.format("%.2f", eachProduct.getPrice())); // Renamed to avoid confusion
                priceLabelCard.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50; -fx-font-weight: bold;");

                Label quantityLabel = new Label("Qty: " + eachProduct.getQuantityAvailable());
                quantityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");

                // Removed Edit and Delete buttons to avoid compilation issues if supporting methods are missing
                // Button editButton = new Button("Edit");
                // Button deleteButton = new Button("Delete");
                // HBox actionButtons = new HBox(10, editButton, deleteButton);
                // actionButtons.setAlignment(Pos.CENTER);

                productCard.getChildren().addAll(imageView, productNameLabel, priceLabelCard, quantityLabel); // Removed actionButtons
                // Removed VBox.setVgrow

                productCardsFlowPane.getChildren().add(productCard);
            }
            mainContentScrollPane.setContent(listProductContent);
        });

        ordersBtn.setOnAction(e->{
            fetchOrders(); // Keep existing logic for fetching
            orderCardsFlowPane.getChildren().clear();
            if (ordersList.isEmpty()) {
                Label noOrdersLabel = new Label("No customer orders received yet.");
                noOrdersLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #555555; -fx-padding: 20;");
                orderProductContent.getChildren().add(noOrdersLabel);
            } else {
                for(Order eachOrder: ordersList){
                    VBox orderCard = new VBox(8); // Increased spacing for better readability
                    orderCard.setPrefSize(260, 190); // Slightly larger for more elegance
                    orderCard.setAlignment(Pos.CENTER_LEFT);
                    orderCard.setPadding(new Insets(15));
                    orderCard.setStyle(
                            "-fx-background-color: #ffffff; " + // Clean white background
                                    "-fx-background-radius: 12; " + // More rounded corners
                                    "-fx-border-color: #b0c4de; " + // Light blue-grey border
                                    "-fx-border-width: 1; " +
                                    "-fx-border-radius: 12; " +
                                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0.4, 0, 3);" // Softer, more pronounced shadow
                    );

                    ImageView imageView = new ImageView();
                    if (eachOrder.getProduct() != null && eachOrder.getProduct().getImageUrl() != null && !eachOrder.getProduct().getImageUrl().isEmpty()) {
                        try {
                            // Ensure the path is a valid URI (file://...)
                            File imageFile = new File(eachOrder.getProduct().getImageUrl());
                            if (imageFile.exists()) {
                                Image image = new Image(imageFile.toURI().toString());
                                imageView.setImage(image);
                                imageView.setFitWidth(90); // Slightly larger image
                                imageView.setFitHeight(70);
                                imageView.setPreserveRatio(false);
                                imageView.setStyle("-fx-border-radius: 6; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0.2, 0, 1);"); // Soft shadow for image
                            } else {
                                System.err.println("Order image file not found: " + eachOrder.getProduct().getImageUrl());
                                // You might want to set a default placeholder image here
                            }
                        } catch (Exception ex) {
                            System.err.println("Error loading image for order product " + eachOrder.getProduct().getName() + ": " + ex.getMessage());
                        }
                    } else {
                        // Placeholder for products without an image
                        ImageView placeholder = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/placeholder.png")))); // Make sure you have a placeholder.png
                        placeholder.setFitWidth(90);
                        placeholder.setFitHeight(70);
                        imageView = placeholder;
                    }


                    HBox productVisual = new HBox(15, imageView); // Increased spacing
                    productVisual.setAlignment(Pos.CENTER_LEFT);

                    VBox productDetails = new VBox(4); // More space between details
                    Label productNameLabel = new Label(eachOrder.getProduct().getName());
                    productNameLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1a237e;"); // Darker blue for prominence
                    productNameLabel.setWrapText(true); // Ensure text wraps

                    Label orderIdLabel = new Label("Order ID: " + eachOrder.getOrderId()); // Display Order ID
                    orderIdLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #607d8b;"); // Muted grey for ID

                    Label priceLabelOrder = new Label("Price: AED " + String.format("%.2f", eachOrder.getProduct().getPrice()));
                    priceLabelOrder.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #4CAF50;"); // Green for price

                    Label quantityLabel = new Label("Quantity: " + eachOrder.getQuantity());
                    quantityLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #555555;");

                    productDetails.getChildren().addAll(productNameLabel, orderIdLabel, priceLabelOrder, quantityLabel);
                    productVisual.getChildren().add(productDetails); // Add details to the productVisual HBox

                    orderCard.getChildren().addAll(productVisual); // Only add productVisual, which now contains all details
                    orderCardsFlowPane.getChildren().add(orderCard);
                }
            }
            mainContentScrollPane.setContent(orderProductContent);
        });

        subscribersBtn.setOnAction(e -> {
            fetchSubscribers(); // Fetch and update the content dynamically
            mainContentScrollPane.setContent(subscriberScrollPane); // Set content to the scroll pane
        });

        logoutBtn.setOnAction(e->{
            // Assuming LoginPage is in the same 'app' package or properly imported
            // This might cause an error if LoginPage is not present or its start method has issues.
            // Keeping it as requested.
            LoginPage loginPage = new LoginPage();
            loginPage.start(primaryStage);
        });


        // Initial content display
        mainContentScrollPane.setContent(addProductContent); // Show Add Product form initially

        // Scene and stage configuration
        Scene page = new Scene(layout, 1200, 800);
        primaryStage.setTitle("Farmer Dashboard");
        primaryStage.setScene(page);
        primaryStage.show();
    }

    public int getNumOfProducts() {
        int length = 0;
        try {
            Scanner input = new Scanner(new File("product.txt"));
            while (input.hasNext()) {
                length++;
                input.nextLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return length;
    }

    public void saveProduct(Product product){
        try(FileWriter writer =new FileWriter("product.txt",true)){
            writer.write(product.toFileString()+"\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void fetchProducts(){
        productList.clear();
        System.out.println("fetching products started....");
        try(BufferedReader reader = new BufferedReader(new FileReader("product.txt"))){
            String line;
            System.out.println("fetching products....");
            while((line = reader.readLine()) != null){
                String[] parameters = line.split(",");
                // Ensure there are enough parameters before parsing
                if (parameters.length >= 9) {
                    int prdId = Integer.parseInt(parameters[0]);
                    String prdName = parameters[1];
                    String prdDsc = parameters[5];
                    double prdPrice = Double.parseDouble(parameters[2]);
                    int quantity = Integer.parseInt(parameters[4]);
                    LocalDate dateSelected = LocalDate.parse(parameters[6]);
                    String imgUrl = parameters[3];
                    String season = parameters[7];
                    String productCategory = parameters[8];
                    Product newProduct = new Product(prdId, prdName, prdDsc, prdPrice, quantity, dateSelected, imgUrl, season, productCategory);
                    productList.add(newProduct);
                } else {
                    System.err.println("Skipping malformed product line: " + line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading product file: " + e.getMessage());
        }
    }

    public void fetchOrders() {
        ordersList.clear();
        System.out.println("fetching orders started.....");
        try(BufferedReader reader = new BufferedReader(new FileReader("orders.txt"))){
            String line;
            System.out.println("fetching orders....");
            while((line = reader.readLine()) != null){
                String[] parameters = line.split(",");
                if (parameters.length >= 6) { // Ensure enough parameters for userName, prdId, prdName, prdPrice, imgUrl
                    String userName = parameters[0];
                    int orderId = ordersList.size() + 1; // Dummy order ID
                    int prdId = Integer.parseInt(parameters[2]);
                    String prdName = parameters[3];
                    double prdPrice = Double.parseDouble(parameters[4]);
                    String imgUrl = parameters[5];
                    int quantity = 1; // Default to 1, or read from file if available

                    Product orderedProduct = new Product(prdId, prdName, prdPrice, imgUrl);
                    // Assuming your Order constructor is: Order(int orderId, int productId, int quantity, String userName, Product product)
                    Order newOrder = new Order(orderId, prdId, quantity, userName, orderedProduct);

                    // Check if product already exists in ordersList to update quantity
                    boolean isFound = false;
                    for(Order existingOrder : ordersList){
                        if(existingOrder.getProductId() == newOrder.getProductId()){
                            existingOrder.setQuantity(existingOrder.getQuantity() + newOrder.getQuantity());
                            isFound = true;
                            break;
                        }
                    }
                    if(!isFound){
                        ordersList.add(newOrder);
                    }
                } else {
                    System.err.println("Skipping malformed order line: " + line);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading orders file: " + e.getMessage());
        }
    }

    private void fetchSubscribers() {
        subscriberContent.getChildren().clear();

        Label subscriberTitle = new Label("Subscriber List");
        subscriberTitle.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #2e7d32; -fx-underline: true;");
        subscriberTitle.setPadding(new Insets(10, 0, 20, 0));
        subscriberContent.getChildren().add(subscriberTitle);

        try (BufferedReader reader = new BufferedReader(new FileReader("subscription.txt"))) {
            // Mark the reader position before checking if empty
            reader.mark(1000); // Mark up to 1000 characters

            // Read a line to check if empty, then reset
            String line = reader.readLine();
            if (line == null) {
                Label noSubscribersLabel = new Label("No subscribers found yet.");
                noSubscribersLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #555555;");
                subscriberContent.getChildren().add(noSubscribersLabel);
                return; // Exit if empty
            }
            reader.reset(); // Reset stream to read from the beginning if not empty

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                // Based on your provided subscription.txt format: Adam,rte@gmail.com,Fruits,Daily for AED 20
                // This has 4 parts: Name, Email, Product Category, Subscription Plan/Price
                if (tokens.length >= 4) { // Adjusted check to 4
                    String name = tokens[0].trim();
                    String email = tokens[1].trim();
                    String productCategory = tokens[2].trim();
                    String subPlanAndPrice = tokens[3].trim(); // This token contains "Daily for AED 20"

                    Label nameLabel = new Label("Name: " + name);
                    Label emailLabel = new Label("Email: " + email);
                    Label productCatLabel = new Label("Product Category: " + productCategory);
                    Label planLabel = new Label("Subscription: " + subPlanAndPrice); // Label for the combined plan and price

                    nameLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #333333;");
                    emailLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");
                    productCatLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #555555;");
                    planLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;"); // Highlight subscription plan

                    VBox subscriberBox = new VBox(5, nameLabel, emailLabel, productCatLabel, planLabel);
                    subscriberBox.setPadding(new Insets(15));
                    subscriberBox.setStyle(
                            "-fx-background-color: #e8f5e9;" + // Light green background
                                    "-fx-background-radius: 10;" +
                                    "-fx-border-color: #c5e1a5;" + // Light green border
                                    "-fx-border-width: 1;" +
                                    "-fx-border-radius: 10;" +
                                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 5, 0.5, 0, 0);" // Subtle shadow
                    );

                    VBox.setMargin(subscriberBox, new Insets(8, 0, 8, 0)); // Reduced margin
                    subscriberContent.getChildren().add(subscriberBox);
                } else {
                    System.err.println("Skipping malformed subscriber line: " + line);
                }
            }
        } catch (IOException e) {
            Label errorLabel = new Label("Error reading subscription file: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
            subscriberContent.getChildren().add(errorLabel);
        }
    }


    public static void main(String[] args) {
        launch(FarmerDashboard.FarmerApp.class);
    }
    public static class FarmerApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            new FarmerDashboard().start(primaryStage);
        }
    }
}