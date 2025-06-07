package app;

import javafx.collections.FXCollections;

import javafx.collections.ObservableList;

import javafx.geometry.Insets;

import javafx.geometry.Pos;

import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

import javafx.scene.layout.*;

import javafx.scene.paint.Color;

import javafx.scene.text.Font;

import javafx.stage.Stage;

import model.*;

import model.Exceptions.OutOfSeasonException;

import java.io.*;

import java.util.ArrayList;

import java.util.Comparator;

import java.util.Objects;

import javafx.stage.Modality;

public class ShoppingPage {

    private ArrayList<Farmer> farmers=new ArrayList<>();

    Alert alert;//to inform if the item is added to cart

    private ComboBox<String> categoryComboBox;

    ObservableList<Product> productCards;

    private TextField searchBox;

    private Button searchButton;

    private FlowPane productList;

    public void start(Stage primaryStage) {

        ProductInventory pi=new ProductInventory();

        productCards=  pi.getProducts();

        AnchorPane root = new AnchorPane();

        root.setPrefSize(1200, 800);

        VBox mainVBox = new VBox(20);

        AnchorPane.setTopAnchor(mainVBox, 20.0);

        AnchorPane.setLeftAnchor(mainVBox, 20.0);

        AnchorPane.setRightAnchor(mainVBox, 20.0);

        AnchorPane.setBottomAnchor(mainVBox, 20.0);

        // Nav Bar

        HBox navBar = new HBox(20);

        navBar.setAlignment(Pos.CENTER);

        navBar.setPadding(new Insets(20));

        navBar.setBackground(new Background(new BackgroundFill(Color.rgb(144,238,144),new CornerRadii(15),Insets.EMPTY)));

//        Label titleLabel = new Label("UAE FARM");

        ImageView logoImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/org/example/farmdelivery/images/logo.png"))));

        logoImage.setFitWidth(100);

        logoImage.setFitHeight(100);

//     titleLabel.getStyleClass().add("nav-title");

        Region spacer = new Region();

        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button dashboardBtn = new Button("Farmer Dashboard");

        dashboardBtn.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(10),Insets.EMPTY)));

        dashboardBtn.setPadding(new Insets(10));

        dashboardBtn.setStyle("-fx-cursor:hand");

        dashboardBtn.setPrefHeight(40);

        dashboardBtn.setOnAction(e ->{

            FarmerDashboard farmerDashboard=new FarmerDashboard();

            farmerDashboard.start(primaryStage);

        });

        Button ordersBtn = new Button("Orders");

        ordersBtn.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(10),Insets.EMPTY)));

        ordersBtn.setPadding(new Insets(10));

        ordersBtn.setStyle("-fx-cursor:hand");

        ordersBtn.setOnAction(e ->{

            DeliverySystem orderPage=new DeliverySystem();

            try {

                orderPage.start(primaryStage);

            } catch (Exception ex) {

                throw new RuntimeException(ex);

            }

        });

        Button cartBtn = new Button("View Cart");

        cartBtn.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(10),Insets.EMPTY)));

        cartBtn.setPadding(new Insets(10));

        cartBtn.setStyle("-fx-cursor:hand");

        cartBtn.setOnAction(e->{

            CartPage cartPage=new CartPage();

            try {

                cartPage.start(primaryStage);

            } catch (Exception ex) {

                throw new RuntimeException(ex);

            }

        });

        Button logoutBtn = new Button("Logout");

        logoutBtn.setOnAction(e -> handleLogout());

        logoutBtn.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(10),Insets.EMPTY)));

        logoutBtn.setPadding(new Insets(10));

        logoutBtn.setStyle("-fx-cursor:hand");

        navBar.getChildren().addAll(logoImage, spacer, dashboardBtn, ordersBtn, cartBtn, logoutBtn);

        // Banner

        VBox banner = new VBox(10);

        banner.setBackground(new Background(new BackgroundFill(Color.rgb(173, 216, 230),new CornerRadii(10),Insets.EMPTY)));

        banner.setAlignment(Pos.CENTER);

        banner.setPadding(new Insets(30));

        Label bannerTitle = new Label("Welcome to UAE Farm Product Delivery App!");

        bannerTitle.setTextFill(Color.GREEN);

        bannerTitle.setFont(Font.font(40));

        bannerTitle.setStyle("-fx-font-weight:700");

        Label bannerText = new Label("Get the freshest produce delivered to your door!");

        bannerText.setFont(Font.font(20));

        bannerText.setStyle("-fx-font-weight:600");

        bannerText.setTextFill(Color.DARKGREEN);

        Button subscribeBtn = new Button("Subscribe to get fresh products");

        subscribeBtn.setStyle(

                "-fx-background-color: linear-gradient(to right, #00b4db, #0083b0);" +  // Gradient blue

                        "-fx-text-fill: white;" +

                        "-fx-font-size: 14px;" +

                        "-fx-font-weight: bold;" +

                        "-fx-padding: 10 20;" +

                        "-fx-background-radius: 25;" +

                        "-fx-cursor: hand;" +

                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);"

        );

        subscribeBtn.setOnAction(e -> showSubscriptionForm());

        banner.getChildren().addAll(bannerTitle, bannerText,subscribeBtn);

        // Filters

        HBox filters = new HBox(20);

        filters.getStyleClass().add("filter-section");

        //seasonal filter

        HBox seasonalBox = new HBox(10);

        seasonalBox.setAlignment(Pos.CENTER_LEFT);

        Label seasonalLabel = new Label("Filter by Season:");

        seasonalLabel.setFont(new Font(16));

        seasonalLabel.setTextFill(Color.DARKBLUE);

        seasonalLabel.setStyle("-fx-font-weight:600");

        ComboBox<String> seasonalComboBox = new ComboBox<>();

        seasonalComboBox.setMinWidth(150);

        seasonalComboBox.setMaxWidth(150);

//      // Set the style for the ComboBox

        seasonalComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        seasonalComboBox.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(10),Insets.EMPTY)));

        seasonalComboBox.setPadding(new Insets(5));

        seasonalComboBox.setFocusTraversable(false);

        seasonalComboBox.setStyle("-fx-cursor:hand");

        seasonalComboBox.setEditable(false);

        seasonalComboBox.setPromptText("All Seasons");

        seasonalComboBox.setItems(FXCollections.observableArrayList(

                "All Seasons", "Spring", "Summer", "Autumn", "Winter"

        ));

        seasonalBox.getChildren().addAll(seasonalLabel, seasonalComboBox);

        //add functionality to each combox item

        seasonalComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {

            try{

                ObservableList<Product> filtered = pi.filterBySeason(newVal);

                refreshProductList(filtered);

            }catch (OutOfSeasonException e){

                alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Out of Season");

                alert.setHeaderText("No products available for " + newVal);

                alert.setContentText("Please select a different season.");

                alert.showAndWait();

            }

        });

        // Category Filter

        HBox categoryBox = new HBox(10);

        categoryBox.setAlignment(Pos.CENTER_LEFT);

        Label categoryLabel = new Label("Filter by Category:");

        categoryLabel.setFont(new Font(16));

        categoryLabel.setTextFill(Color.DARKBLUE);

        categoryLabel.setStyle("-fx-font-weight:600");

        categoryLabel.setPadding(new Insets(0, 0, 0, 10));

        categoryComboBox = new ComboBox<>();

        categoryComboBox.setMinWidth(150);

        categoryComboBox.setMaxWidth(150);

        // Set the style for the ComboBox

        categoryComboBox.setStyle("-fx-font-size: 14px; -fx-padding: 5px;");

        categoryComboBox.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(10),Insets.EMPTY)));

        categoryComboBox.setPadding(new Insets(5));

        categoryComboBox.setFocusTraversable(false);

        categoryComboBox.setPromptText("All");

        categoryComboBox.setItems(FXCollections.observableArrayList(

                "All", "Milk Products", "Fruits", "Vegetables", "Cereal Products"

        ));

        categoryBox.getChildren().addAll(categoryLabel, categoryComboBox);

        //add functionality to each combox item

        categoryComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {

            ObservableList<Product> filtered = pi.getFilteredProducts(newVal);

            refreshProductList(filtered);

        });

        // Price Sort

        HBox priceSortBox = new HBox(10);

        priceSortBox.setAlignment(Pos.CENTER_LEFT);

        Label sortLabel = new Label("Sort by Price:");

        sortLabel.setFont(new Font(16));

        sortLabel.setTextFill(Color.DARKBLUE);

        sortLabel.setStyle("-fx-font-weight:600");

        sortLabel.setPadding(new Insets(0, 0, 0, 10));

        priceSortBox.setPadding(new Insets(0, 0, 0, 10));

        Button incPriceBtn = new Button("Increasing");

        incPriceBtn.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(10),Insets.EMPTY)));

        incPriceBtn.setPadding(new Insets(10));

        incPriceBtn.setStyle("-fx-cursor:hand");

        incPriceBtn.setPrefHeight(40);

        //sort products in increasing price order

        incPriceBtn.setOnAction(e -> {

            FXCollections.sort(productCards, Comparator.comparingDouble(Product::getPrice));

            refreshProductList(productCards);

        });


        Button decPriceBtn = new Button("Decreasing");

        decPriceBtn.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(10),Insets.EMPTY)));

        decPriceBtn.setPadding(new Insets(10));

        decPriceBtn.setStyle("-fx-cursor:hand");

        decPriceBtn.setPrefHeight(40);

        //sort products in decreasing price order

        decPriceBtn.setOnAction(e -> {

            FXCollections.sort(productCards, Comparator.comparingDouble(Product::getPrice).reversed());

            refreshProductList(productCards);

        });

        priceSortBox.getChildren().addAll(sortLabel, incPriceBtn, decPriceBtn);

        // Search Box

        HBox searchBoxContainer = new HBox(10);

        searchBoxContainer.setAlignment(Pos.CENTER_LEFT);

        Label searchLabel = new Label("Search:");

        searchLabel.setFont(new Font(16));

        searchLabel.setTextFill(Color.DARKBLUE);

        searchLabel.setStyle("-fx-font-weight:600");

        searchLabel.setPadding(new Insets(0, 0, 0, 10));

        searchBoxContainer.setPadding(new Insets(0, 0, 0, 10));

        searchBoxContainer.setAlignment(Pos.CENTER_LEFT);

        searchBox = new TextField();

        searchBox.setMinWidth(200);

        searchBox.setMaxWidth(200);

        // Set the style for the TextField

        searchBox.setStyle("-fx-font-size: 14px; -fx-padding: 10px;");

        searchBox.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(10),Insets.EMPTY)));

        searchBox.setPadding(new Insets(20));

        searchBox.setFocusTraversable(false);

        searchBox.setPromptText("Search product");

        searchButton = new Button("Search");

        searchButton.setBackground(new Background(new BackgroundFill(Color.WHITE,new CornerRadii(10),Insets.EMPTY)));

        searchButton.setPadding(new Insets(10));

        searchButton.setStyle("-fx-cursor:hand");

        searchButton.setPrefWidth(100);

        //add functionality to search button

        searchButton.setOnAction(e -> handleSearch());

        searchBoxContainer.getChildren().addAll(searchLabel, searchBox, searchButton);

        filters.getChildren().addAll(seasonalBox,categoryBox, priceSortBox, searchBoxContainer);

        // Product List

        productList = new FlowPane();

        productList.setHgap(10);

        productList.setVgap(10);

        productList.setPrefHeight(900);

        productList.setPadding(new Insets(10));

        //product card

        refreshProductList(productCards);

        ScrollPane scrollPane = new ScrollPane(productList);

        scrollPane.setFitToWidth(true);

        scrollPane.setFitToHeight(true);

        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        HBox scrollContainer = new HBox(scrollPane);

        HBox.setHgrow(scrollPane, Priority.ALWAYS);

        mainVBox.getChildren().addAll(navBar, banner, filters, scrollContainer);

        root.getChildren().add(mainVBox);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Shopping Page");

        primaryStage.setScene(scene);

        primaryStage.show();

        logoutBtn.setOnAction(e->{

            LoginPage loginPage = new LoginPage();

            loginPage.start(primaryStage);

        });

    }

    //refresh product lists

    private void refreshProductList(ObservableList<Product> products) {

        productList.getChildren().clear();

        for (Product pdc : products) {

            if (pdc != null && pdc.getImageUrl() != null && !pdc.getImageUrl().isEmpty()) {

                try {

                    VBox vb = new VBox();

                    vb.setAlignment(Pos.CENTER);

                    String imgPath= pdc.getImageUrl();

                    Image image = new Image(new File(imgPath).toURI().toString());

                    System.out.println("Image Path: " + imgPath);

                    ImageView imageView =new ImageView(image);

                    imageView.setFitWidth(200);

                    imageView.setFitHeight(200);

                    Label productLabel = new Label(pdc.getName());

                    Label productPrice = new Label("AED " + pdc.getPrice());

                    Button addToCart = new Button("ADD TO CART");

                    addToCart.setPadding(new Insets(10));

                    addToCart.setTextFill(Color.WHITE);

                    addToCart.setStyle("-fx-cursor:hand");

                    addToCart.setBackground(new Background(new BackgroundFill(Color.rgb(33, 150, 243),new CornerRadii(10),Insets.EMPTY)));

                    addToCart.setOnAction(event -> {

                        alert = new Alert(Alert.AlertType.INFORMATION);

                        alert.setTitle(pdc.getName());

                        alert.setHeaderText(pdc.getName()+" has been Added to Cart");

                        alert.setContentText("Thank you for choosing our product");

                        alert.showAndWait();

                        ProductInventory pi = new ProductInventory();

                        pi.addToCart(pdc.getProductId());

                    });

                    VBox.setMargin(productLabel, new Insets(10, 0, 0, 0));

                    vb.getChildren().addAll( imageView,productLabel, productPrice, addToCart);

                    vb.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(15), Insets.EMPTY)));

                    vb.setPadding(new Insets(10, 10, 10, 10));

                    productList.getChildren().add(vb);

                } catch (Exception e) {

                    System.out.println("Error displaying product: " + e.getMessage());

                }

            }

        }

    }

    private void handleLogout() {

        System.out.println("Logout clicked");

    }

    //searching functionality

    private void handleSearch() {

        String query = searchBox.getText().trim();

        if (query.isEmpty()) return;

        ProductInventory pi = new ProductInventory();

        ObservableList<Product> filtered = pi.searchProducts(query);

        refreshProductList(filtered);

    }

//    show subscription modal

    private void showSubscriptionForm() {

        farmers.clear();

        File file=new File("farmer.txt");

        try(BufferedReader bf=new BufferedReader(new FileReader(file))) {

            String line;

            while ((line=bf.readLine())!=null){

                String[] farmerData=line.split(",");

                Farmer farmer=new Farmer(Integer.parseInt(farmerData[2]),farmerData[1],farmerData[0]);

                farmers.add(farmer);

            }

        }catch (IOException e){

            System.out.println(e.getMessage());

        }

        Stage popup = new Stage();

        popup.initModality(Modality.APPLICATION_MODAL);

        popup.setTitle("Fresh Farm Product Subscription");

        Label titleLabel = new Label("Subscribe to Fresh Farm Products");

        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");

        // Form fields

        TextField nameField = new TextField();

        nameField.setPromptText("Full Name");

        nameField.setStyle("-fx-padding: 8; -fx-background-radius: 5;");

        TextField emailField = new TextField();

        emailField.setPromptText("Email Address");

        emailField.setStyle("-fx-padding: 8; -fx-background-radius: 5;");

        ComboBox<String> farmerCombo = new ComboBox<>();

        for(Farmer farmer:farmers){

            farmerCombo.getItems().addAll(farmer.getName());

        }

        farmerCombo.setPromptText("Select Farmer");

        farmerCombo.setStyle("-fx-padding: 8; -fx-background-radius: 5;");

        farmerCombo.setPrefWidth(300.0);

        ComboBox<String> categoryCombo = new ComboBox<>();

        categoryCombo.getItems().addAll("Vegetables", "Fruits", "Dairy", "Meat");

        categoryCombo.setPromptText("Product Category");

        categoryCombo.setStyle("-fx-padding: 8; -fx-background-radius: 5;");

        categoryCombo.setPrefWidth(300.0);

        ComboBox<String> planCombo = new ComboBox<>();

        planCombo.getItems().addAll("Daily for AED 20", "Weekly AED 35", "Biweekly AED 50", "Monthly AED 100");

        planCombo.setPromptText("Subscription Plan");

        planCombo.setStyle("-fx-padding: 8; -fx-background-radius: 5;");

        planCombo.setPrefWidth(300.0);


        TextField cardField = new TextField();

        cardField.setPromptText("Card Number");

        cardField.setStyle("-fx-padding: 8; -fx-background-radius: 5;");

        PasswordField cvcField = new PasswordField();

        cvcField.setPromptText("CVC");

        cvcField.setStyle("-fx-padding: 8; -fx-background-radius: 5;");

        Button subscribeBtn = new Button("Subscribe Now");

        subscribeBtn.setStyle(

                "-fx-background-color: linear-gradient(to right, #00c9a7, #005aa7);" +

                        "-fx-text-fill: white;" +

                        "-fx-font-size: 14px;" +

                        "-fx-font-weight: bold;" +

                        "-fx-padding: 10 20;" +

                        "-fx-background-radius: 25;" +

                        "-fx-cursor: hand;" +

                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);"

        );

        subscribeBtn.setOnMouseEntered(e -> subscribeBtn.setStyle(

                "-fx-background-color: linear-gradient(to right, #36d1dc, #5b86e5);" +

                        "-fx-text-fill: white;" +

                        "-fx-font-size: 14px;" +

                        "-fx-font-weight: bold;" +

                        "-fx-padding: 10 20;" +

                        "-fx-background-radius: 25;" +

                        "-fx-cursor: hand;" +

                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 6, 0, 0, 3);"

        ));

        subscribeBtn.setOnMouseExited(e -> subscribeBtn.setStyle(

                "-fx-background-color: linear-gradient(to right, #00c9a7, #005aa7);" +

                        "-fx-text-fill: white;" +

                        "-fx-font-size: 14px;" +

                        "-fx-font-weight: bold;" +

                        "-fx-padding: 10 20;" +

                        "-fx-background-radius: 25;" +

                        "-fx-cursor: hand;" +

                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 2);"

        ));

        subscribeBtn.setOnAction(e -> {

//        Alice Johnson,alice@example.com,Organic Tomatoes,Monthly

            saveSubscribers(nameField.getText(),emailField.getText(),farmerCombo.getValue(),categoryCombo.getValue(),planCombo.getValue(),Integer.parseInt(cardField.getText()),Integer.parseInt(cvcField.getText()));

            System.out.println("Subscribed: " + nameField.getText());

            popup.close();

//            Alert feedAl=new Alert(Alert.AlertType.CONFIRMATION);
//
//            feedAl.setTitle("Thank You");
//
//            feedAl.setHeaderText("Thank you subscribing to our product");
//
//            feedAl.showAndWait();

        });

        VBox formLayout = new VBox(10,

                titleLabel,

                nameField,

                emailField,

                farmerCombo,

                categoryCombo,

                planCombo,

                cardField,

                cvcField,

                subscribeBtn

        );

        formLayout.setAlignment(Pos.CENTER);

        formLayout.setPadding(new Insets(25));

        formLayout.setStyle(

                "-fx-background-color: #ffffff;" +

                        "-fx-border-radius: 12;" +

                        "-fx-background-radius: 12;" +

                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0.5, 0, 0);"

        );

        Scene scene = new Scene(formLayout, 350, 520);

        popup.setScene(scene);

        popup.showAndWait();

    }

//a method to save subscribers in subscribers.txt file

    public void saveSubscribers(String name,String email,String farmer,String prodCategory,String subPlan,int cardNum,int cvcNum){

        File file=new File("subscription.txt");

        try(FileWriter fileWriter=new FileWriter(file,true)) {

            Subscribers sub=new Subscribers(name,email,farmer,prodCategory,subPlan,cardNum,cvcNum);

            fileWriter.write(sub.toString()+"\n");

        }catch (IOException e){

            System.out.println(e.getMessage());

        }

    }

    public static void main(String[] args) {

        javafx.application.Application.launch(ShoppingApp.class);

    }

    // This class wraps the JavaFX Application launch

    public static class ShoppingApp extends javafx.application.Application {

        @Override

        public void start(Stage primaryStage) {

            new ShoppingPage().start(primaryStage);

        }

    }

}

