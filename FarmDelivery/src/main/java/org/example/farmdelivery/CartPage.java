package org.example.farmdelivery;

import app.ShoppingPage;
import javafx.application.Application;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CartPage extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        AnchorPane root = new AnchorPane();
        root.setPrefSize(1200, 800);

        HBox mainHBox = new HBox(20);
        AnchorPane.setTopAnchor(mainHBox, 20.0);
        AnchorPane.setLeftAnchor(mainHBox, 20.0);
        AnchorPane.setRightAnchor(mainHBox, 20.0);
        AnchorPane.setBottomAnchor(mainHBox, 20.0);

        // Left Section
        VBox leftSection = new VBox();
        leftSection.setAlignment(Pos.CENTER_LEFT);
        leftSection.setPadding(new Insets(20));
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

        leftSection.getChildren().addAll(title,userNameBox,phoneNumberBox,emailAddressBox,paymentComboBox,completeOrder);

        // Right Section
        VBox rightSection = new VBox();
        rightSection.setAlignment(Pos.CENTER_LEFT);
        rightSection.setSpacing(10);
        rightSection.setPrefWidth(600);
        //cart details label
        Label label = new Label("Cart Product details");
        label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label totalPriceLabel=new Label("Total Price: AED 2500");
        totalPriceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        HBox totalPriceBox=new HBox(totalPriceLabel);
        //cart product
         //product Image
        //get cart items from cart
        Cart cart=new Cart();
        ArrayList<ProductCard> productCards=cart.getCartItems();
//        System.out.println(productCards);
        int total=6;
        //scroll pane to hold VBOx
        ScrollPane cartScroll = new ScrollPane();
        cartScroll.setPrefHeight(600);
        cartScroll.setPadding(new Insets(20));
        cartScroll.setFitToWidth(true);  // Makes inner VBox expand to ScrollPane width

        VBox cartContainer = new VBox(); // Container for all cart items
        cartContainer.setSpacing(10);    // Spacing between products
//        System.out.println(productCards);

        for(ProductCard pdc:productCards) {
            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(pdc.getImgUrl()))));
            imageView.setFitWidth(100);
            imageView.setFitHeight(100);

            Label productLabel = new Label(pdc.getProductName());
            productLabel.setTextFill(Color.WHITE);

            Label productPrice = new Label("AED " + (pdc.getProductPrice()*pdc.getQuantity()));
            productPrice.setTextFill(Color.WHITE);

            Label productQuantity = new Label("Quantity: " + pdc.getQuantity());
            productQuantity.setTextFill(Color.WHITE);

            VBox prodInfoVbox = new VBox(productLabel, productPrice, productQuantity);
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

        cartScroll.setContent(cartContainer);  // Set the VBox as ScrollPane content


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
        rightSection.getChildren().addAll(label,totalPriceBox,cartScroll,loginBtn);
        // Add both sections to HBox
        mainHBox.getChildren().addAll(leftSection, rightSection);
        root.getChildren().add(mainHBox);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cart Page");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(CartPage.CartApp.class);
    }
    // This class wraps the JavaFX Application launch
    public static class CartApp extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            new CartPage().start(primaryStage);
        }
    }

}



