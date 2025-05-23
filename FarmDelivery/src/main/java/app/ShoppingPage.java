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
import javafx.stage.Stage;
import model.ProductInventory;
import org.example.farmdelivery.CartPage;
import org.example.farmdelivery.ProductCard;
import java.util.Comparator;
import java.util.Objects;
public class ShoppingPage {
    private ComboBox<String> categoryComboBox;
//    private  ArrayList<ProductCard> productCards;
    ObservableList<ProductCard> productCards;
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
        navBar.getStyleClass().add("nav-bar");

        Label titleLabel = new Label("UAE FARM");
        titleLabel.getStyleClass().add("nav-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button dashboardBtn = new Button("Farmer Dashboard");
        dashboardBtn.setPrefHeight(40);
        dashboardBtn.setOnAction(e ->{
            FarmerDashboard farmerDashboard=new FarmerDashboard();
            farmerDashboard.start(primaryStage);
        });

        Button ordersBtn = new Button("Orders");
        ordersBtn.setOnAction(e -> handleProducts());

        Button profileBtn = new Button("Profile");
        profileBtn.setOnAction(e -> handleProfile());

        Button cartBtn = new Button("View Cart");
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

        navBar.getChildren().addAll(titleLabel, spacer, dashboardBtn, ordersBtn, profileBtn, cartBtn, logoutBtn);

        // Banner
        VBox banner = new VBox(10);
        banner.setAlignment(Pos.CENTER);

        Label bannerTitle = new Label("Welcome to UAE Farm Product Delivery App!");

        Label bannerText = new Label("Get the freshest produce delivered to your door!");;

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

        // Price Sort
        HBox priceSortBox = new HBox(10);
        priceSortBox.setAlignment(Pos.CENTER_LEFT);
        Label sortLabel = new Label("Sort by Price:");
        Button incPriceBtn = new Button("Increasing");
        incPriceBtn.setOnAction((e)->{
            FXCollections.sort(productCards, Comparator.comparingDouble(ProductCard::getProductPrice)); // Ascending
            System.out.println("increasing");
            for(ProductCard pd:productCards){
                System.out.println(pd.getProductPrice());
            }
        });
        Button decPriceBtn = new Button("Decreasing");
        decPriceBtn.setOnAction(e ->{
            FXCollections.sort(productCards, Comparator.comparingDouble(ProductCard::getProductPrice).reversed()); // Descending
            System.out.println("decreasing");
            for(ProductCard pd:productCards){
                System.out.println(pd.getProductPrice());
            }

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

        //give background color for the vb
        productList.getChildren().clear(); // Clear old cards
          for(ProductCard pdc:productCards){
              try{
                  VBox vb=new VBox();
                  vb.setAlignment(Pos.CENTER);
                  ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(pdc.getImgUrl()))));
                  imageView.setFitWidth(200);
                  imageView.setFitHeight(200);
                  Label productLabel=new Label(pdc.getProductName());
                  Label productPrice=new Label("AED "+pdc.getProductPrice());
                  Button addToCart=new Button("ADD TO CART");
                  addToCart.setOnAction((event)->{
                      pi.addToCart(pdc.getProductId());
                  });
                  VBox.setMargin(productLabel,new Insets(10,0,0,0));
                  productLabel.setAlignment(Pos.CENTER);
                  vb.getChildren().addAll(imageView,productLabel,productPrice,addToCart);
                  vb.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE,new CornerRadii(15),Insets.EMPTY)));
                  vb.setPadding(new Insets(10,10,10,10));

                  //add product card to vb
                  productList.getChildren().add(vb);
              }catch (Exception e){
                  System.out.println("not written");;
              }

          }




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
//        scene.getStylesheets().add(getClass().getResource("/org/example/farmdelivery/shopping.css").toExternalForm());

        primaryStage.setTitle("Shopping Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Placeholder handlers
    private void handleDashBoard() {
        System.out.println("Dashboard clicked");
    }

    private void handleProducts() {
        System.out.println("Orders clicked");
    }

    private void handleProfile() {
        System.out.println("Profile clicked");
    }

    private void handleCart() {

    }

    private void handleLogout() {
        System.out.println("Logout clicked");
    }

    private void handleSearch() {
        String query = searchBox.getText();
        System.out.println("Search: " + query);
    }

    private void sortInIncreasingPrice() {
        System.out.println("Sorting increasing...");
    }

    private void sortDecreasingPrice() {
        System.out.println("Sorting decreasing...");
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

