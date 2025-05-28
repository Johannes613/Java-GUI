package app;
import javafx.application.Application;
import javafx.geometry.Insets;
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
public class DeliverySystem extends Application{
    ArrayList<Product> productCards= new ArrayList<>();
    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(1200, 800);
        VBox mainVbox = new VBox(20);
        AnchorPane.setTopAnchor(mainVbox, 20.0);
        AnchorPane.setLeftAnchor(mainVbox, 20.0);
        AnchorPane.setRightAnchor(mainVbox, 20.0);
        AnchorPane.setBottomAnchor(mainVbox, 20.0);
        // order tracking Section
        //cart details label
        Label label = new Label("Track Your Orders");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        //get cart items from cart
        Cart cart=new Cart();


        fetchOrders();
//      System.out.println(productCards);
        //scroll pane to hold VBOx
        ScrollPane cartScroll = new ScrollPane();
        cartScroll.setPrefHeight(600);
        cartScroll.setPadding(new Insets(20));
        cartScroll.setFitToWidth(true);  // Makes inner VBox expand to ScrollPane width
        VBox cartContainer = new VBox(); // Container for all cart items
        cartContainer.setSpacing(10);    // Spacing between products
//      System.out.println(productCards);
        for(Product pdc:productCards) {
            String imgPath= pdc.getImageUrl();
            Image image = new Image(new File(imgPath).toURI().toString());
            System.out.println("Image Path: " + imgPath);
            ImageView imageView =new ImageView(image);
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);
            Label productLabel = new Label(pdc.getName());
            productLabel.setTextFill(Color.WHITE);
            Label productPrice = new Label("AED " + (pdc.getPrice()*pdc.getQuantityAvailable()));
            productPrice.setTextFill(Color.WHITE);
            Label productQuantity = new Label("Quantity: " + pdc.getQuantityAvailable());
            productQuantity.setTextFill(Color.WHITE);
            Label orderStatus=new Label("Status: Ordered");
            orderStatus.setTextFill(Color.WHITE);
            VBox prodInfoVbox = new VBox(productLabel, productPrice, productQuantity,orderStatus);
            prodInfoVbox.setPadding(new Insets(20));
            prodInfoVbox.setSpacing(10);
            HBox cartProdInfo = new HBox(imageView, prodInfoVbox);
            cartProdInfo.setSpacing(20);  // Optional: add space between image and text
            VBox cartVbox = new VBox(cartProdInfo);
            cartVbox.setBackground(new Background(new BackgroundFill(Color.rgb(174, 173, 107), new CornerRadii(15), Insets.EMPTY)));
            cartVbox.setPadding(new Insets(10));
            cartVbox.setPrefWidth(500);
            cartContainer.getChildren().add(cartVbox);  // Correct way to add to ScrollPane
        }
        cartScroll.setContent(cartContainer); // Set the VBox as ScrollPane content
        Button loginBtn = new Button("Goto Shopping");
        loginBtn.setPrefWidth(200);
        loginBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        loginBtn.setPadding(new Insets(10));
        loginBtn.setOnAction(e -> {
//            handleLogin(e);
            ShoppingPage shp=new ShoppingPage();
            shp.start(primaryStage);
            System.out.println("Login button clicked");
        });
        // Add label,cartScroll,loginBtn to mainVBox
        mainVbox.getChildren().addAll(label,cartScroll,loginBtn);
        root.getChildren().add(mainVbox);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cart Page");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(DeliverySystem.OrderApp.class);
    }
    // This class wraps the JavaFX Application launch
    public static class OrderApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            new DeliverySystem().start(primaryStage);
        }
    }
    public void fetchOrders() {
        System.out.println("fetching orders started.....");
        try(BufferedReader reader = new BufferedReader(new FileReader("orders.txt"));
        BufferedReader bfr = new BufferedReader(new FileReader("current_user.txt"))){
            // Read the current user
            String currentUser = bfr.readLine();
            if (currentUser == null || currentUser.isEmpty()) {
                System.out.println("No current user found.");
                return;
            }
            String[] userCred = currentUser.split(",");
            String userName = userCred[0].trim(); // the first part is the username
            String password = userCred[1].trim(); // the second part is the password
            // Read each line from orders.txt
            String line;
            System.out.println("fetching orders....");
            while((line = reader.readLine()) != null){
                String[] parameters = line.split(",");
                  String userNameFromOrder = parameters[0];
                 String passwordFromOrder= parameters[1];
                    // Check if the username and password match the current user
                 if(userName.equalsIgnoreCase(userNameFromOrder) && password.equalsIgnoreCase(passwordFromOrder)){
                     int prdId = Integer.parseInt(parameters[2]);
                     String prdName = parameters[3];
                     double prdPrice = Double.parseDouble(parameters[4]);
                     String imgUrl = parameters[5];
                     Product newProduct = new Product(prdId, prdName, prdPrice, imgUrl);
                     productCards.add(newProduct);
                 }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}