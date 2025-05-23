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
import model.Product;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class FarmerDashboard extends Application {
    ArrayList<Product> productList = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {

        // header section
        Label headerLabel = new Label("Farmer Dashboard");
        headerLabel.setStyle("-fx-text-fill: white; -fx-font-size:20px; -fx-font-weight: bold;");
        HBox header = new HBox(1);
        header.setStyle("-fx-background-color: #2e7d32;");
        header.setPadding(new Insets(7));
        header.getChildren().add(headerLabel);
        header.setAlignment(Pos.CENTER);

        // side nav section
        VBox sideNav = new VBox(10);

        sideNav.setPadding(new Insets(20));
        sideNav.setStyle("-fx-background-color:#e8f5e9;");
        sideNav.setPrefWidth(180);

        Button addProductBtn = new Button("Add Product");
        Button listProductBtn = new Button("List products");
        Button ordersBtn = new Button("Orders");
        Button logoutBtn = new Button("Log out");

        Button[] navButtons = {addProductBtn, listProductBtn, ordersBtn, logoutBtn};

        for (Button eachBtn : navButtons) {
            eachBtn.setMaxWidth(140);
            eachBtn.setPrefHeight(30);
            eachBtn.setStyle("-fx-background-color:#2e7d32; -fx-text-fill:white;-fx-font-weight:bold; -fx-font-size:13px;");
        }
        sideNav.getChildren().addAll(addProductBtn, listProductBtn, ordersBtn, logoutBtn);


        VBox addProductContent = new VBox(15);
        VBox listProductContent = new VBox(15);
        VBox orderProductContent = new VBox(10);

        // add product content
        addProductContent.setStyle("-fx-background-color: #f1f8e9;");
        addProductContent.setPadding(new Insets(20));
        Label addProduct = new Label("Add Product section");

        Label orders = new Label("Orders section");

        Label formTitle = new Label("Add New Product");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Label nameLabel = new Label("Product Name: ");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter product name: ");

        Label quantityLabelInput = new Label("Quantity: ");
        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter quantity: ");

        Label descLabel = new Label("Description: ");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Enter product description: ");
        descArea.setPrefRowCount(4);

        Label priceLabel = new Label("Product Price: ");
        TextField priceField = new TextField();
        priceField.setPromptText("Enter product price: ");

        Label selectDateLabel = new Label("Select Calendar: ");
        Button selectDateBtn = new Button("Click to SelectDate");
        selectDateBtn.setStyle("-fx-background-color:#2e7d32; -fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:13px");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Harvest Date");
        datePicker.setVisible(false);
        selectDateBtn.setOnAction(e ->datePicker.setVisible(true));

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image of the product");

        String[] filePath = new String[1];
        Label imageUploadLabel = new Label("Upload Product Image: ");
        Button imgUploadBtn = new Button("Click to Upload Image");
        imgUploadBtn.setStyle("-fx-background-color:#2e7d32; -fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:13px");

        imgUploadBtn.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                filePath[0] = selectedFile.getAbsolutePath();
                imgUploadBtn.setText("Selected: " + selectedFile.getName());
            }
        });

        Button saveBtn = new Button("Save Product");
        saveBtn.setStyle("-fx-background-color:#2e7d10; -fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:13px");

        saveBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                String imgUrl = filePath[0];
                int productId = getNumOfProducts() + 1;
                String desc = descArea.getText();
                LocalDate dateSelected = datePicker.getValue();

                Product product = new Product(productId, name, desc, price, quantity, dateSelected, imgUrl);
                saveProduct(product);

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Product saved successfully!");
                alert.show();

            } catch (NumberFormatException ex) {
                System.out.println("Incorrect Number format");
                Alert alert = new Alert(Alert.AlertType.WARNING,"Invalid Number format");
                alert.show();
            }
        });








        // layout structure
        BorderPane layout = new BorderPane();
        layout.setLeft(sideNav);
        layout.setTop(header);
        layout.setCenter(addProductContent);
        addProductBtn.setOnAction(e -> layout.setCenter(addProductContent));

        // list product content
        listProductContent.setPadding(new Insets(30));
        Label listProduct = new Label("List Product Section");
        listProduct.setStyle("-fx-font-weight: bold;-fx-font-size:20px");

        listProductBtn.setOnAction(e -> {
            fetchProducts();
            listProductContent.getChildren().clear();
            listProductContent.getChildren().add(listProduct);
            System.out.println(productList.size() + "size of products");
            for(Product eachProduct: productList){
                String path = eachProduct.getImageUrl();
                System.out.println("The image path: " + path);
                Image image = new Image(new File(path).toURI().toString());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(150);
                imageView.setFitHeight(100);

                Label productNameLabel = new Label(eachProduct.getName());
                productNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

                Label priceNameLabel = new Label("Price: " + eachProduct.getPrice() + " AED");
                priceNameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill:  white; -fx-font-weight: bold;");

                Label quantityLabel = new Label("Quantity: " + eachProduct.getQuantityAvailable());
                quantityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");

                VBox infoBox = new VBox(5, productNameLabel, priceNameLabel, quantityLabel);
                HBox productCard = new HBox(15, imageView, infoBox);
                productCard.setPadding(new Insets(10));
                productCard.setStyle("-fx-background-color: rgb(111, 139, 111); -fx-background-radius: 10;-fx-border-radius:10px");
                productCard.setAlignment(Pos.CENTER_LEFT);
                listProductContent.getChildren().add(productCard);

            }
            layout.setCenter(listProductContent);
        });
        ordersBtn.setOnAction(e -> layout.setCenter(orderProductContent));
        addProductContent.getChildren().addAll(formTitle, nameLabel, nameField, quantityLabelInput, quantityField, descLabel, descArea, priceLabel, priceField, selectDateLabel,datePicker,selectDateBtn, imageUploadLabel, imgUploadBtn,saveBtn);
        orderProductContent.getChildren().add(orders);

        Scene page = new Scene(layout, 1000, 800);
        primaryStage.setTitle("Farmer Dashboard");
        primaryStage.setScene(page);
        primaryStage.show();
    }



    public int getNumOfProducts() {
        int length = 0;
        try {
            Scanner input = new Scanner(new File("farmer.txt"));

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
        System.out.println("Writing file....");
        try(FileWriter writer =new FileWriter("farmer.txt",true)){
            writer.write(product.toFileString());
            System.out.println("File written successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void fetchProducts(){
        productList.clear();
        System.out.println("started fetching....");
        try(BufferedReader reader = new BufferedReader(new FileReader("farmer.txt"))){
            String line;
            System.out.println("fetching....");
            while((line = reader.readLine()) != null){
                String[] parameters = line.split(",");

                int prdId = Integer.parseInt(parameters[0]);
                String prdName = parameters[1];
                String prdDsc = parameters[2];
                double prdPrice = Double.parseDouble(parameters[3]);
                int quantity = Integer.parseInt(parameters[4]);
//                LocalDate dateSelected = LocalDate.parse(parameters[5]);
                LocalDate dateSelected = LocalDate.now();
                String imgUrl = parameters[6];
                Product newProduct = new Product(prdId, prdName, prdDsc, prdPrice, quantity, dateSelected, imgUrl);
                productList.add(newProduct);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(FarmerDashboard.FarmerApp.class);
    }
    // This class wraps the JavaFX Application launch
    public static class FarmerApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            new FarmerDashboard().start(primaryStage);
        }
    }

}

