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
import model.Product;
import model.ProductInventory;
import java.util.Comparator;
import java.util.Objects;
public class ShoppingPage {
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
//        titleLabel.getStyleClass().add("nav-title");

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
            Orders orderPage=new Orders();
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
        bannerTitle.setTextFill(Color.RED);
        bannerTitle.setFont(Font.font(40));
        bannerTitle.setStyle("-fx-font-weight:700");
        Label bannerText = new Label("Get the freshest produce delivered to your door!");
        bannerText.setFont(Font.font(20));
        bannerText.setStyle("-fx-font-weight:600");
        bannerText.setTextFill(Color.rgb(255, 153, 0));

        banner.getChildren().addAll(bannerTitle, bannerText);

        // Filters
        HBox filters = new HBox(20);
        filters.getStyleClass().add("filter-section");

        // Category Filter
        HBox categoryBox = new HBox(10);
        categoryBox.setAlignment(Pos.CENTER_LEFT);
        Label categoryLabel = new Label("Filter by Category:");
        categoryComboBox = new ComboBox<>();
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
        Button incPriceBtn = new Button("Increasing");
        //sort products in increasing price order
        incPriceBtn.setOnAction(e -> {
            FXCollections.sort(productCards, Comparator.comparingDouble(Product::getPrice));
            refreshProductList(productCards);
        });



        Button decPriceBtn = new Button("Decreasing");
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
        searchBox = new TextField();
        searchBox.setPromptText("Search product");
        searchButton = new Button("Search");
        searchButton.setOnAction(e -> handleSearch());
        searchBoxContainer.getChildren().addAll(searchLabel, searchBox, searchButton);

        filters.getChildren().addAll(categoryBox, priceSortBox, searchBoxContainer);

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
    }
    //refresh product lists
    private void refreshProductList(ObservableList<Product> products) {
        productList.getChildren().clear();
        for (Product pdc : products) {
            try {
                VBox vb = new VBox();
                vb.setAlignment(Pos.CENTER);
                ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(pdc.getImageUrl()))));
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
                vb.getChildren().addAll(imageView, productLabel, productPrice, addToCart);
                vb.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(15), Insets.EMPTY)));
                vb.setPadding(new Insets(10, 10, 10, 10));
                productList.getChildren().add(vb);
            } catch (Exception e) {
                System.out.println("Error displaying product: " + e.getMessage());
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

