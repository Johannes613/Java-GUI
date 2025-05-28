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
import model.Order;
import model.Product;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
public class FarmerDashboard extends Application {
    ArrayList<Product> productList = new ArrayList<>();
    ArrayList<Order> ordersList = new ArrayList<>();
    ArrayList<Product> orderProductList = new ArrayList<>();

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
            eachBtn.setMaxWidth(150);
            eachBtn.setPrefHeight(33);
            eachBtn.setStyle("-fx-background-color:#2e7d32; -fx-text-fill:white;-fx-font-weight:bold; -fx-font-size:13px;");
        }
        sideNav.getChildren().addAll(addProductBtn, listProductBtn, ordersBtn, logoutBtn);

        VBox addProductContent = new VBox(15);
        VBox listProductContent = new VBox(15);
        VBox orderProductContent = new VBox(15);
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
        //product category
        Label categoryLabel=new Label("Product Category: ");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(    "Milk Products",
                "Fruits",
                "Vegetables",
                "Cereal Products",
                "Cereal",
                "Meat & Poultry",
                "Fish & Seafood",
                "Bakery Products",
                "Eggs",
                "Spices & Condiments",
                "Oils & Fats",
                "Snacks & Chips",
                "Beverages",
                "Dry Fruits & Nuts",
                "Grains & Pulses",
                "Legumes",
                "Herbs",
                "Frozen Foods",
                "Organic Products",
                "Dairy Alternatives",
                "Baby Products",
                "Health & Nutrition",
                "Cleaning Supplies",
                "Personal Care",
                "Pet Supplies",
                "Ready-to-Eat Foods",
                "Canned Goods",
                "Sauces & Pickles",
                "Flours",
                "Sugars & Sweeteners"
        );
        categoryComboBox.setPromptText("Select Product Category");
        categoryComboBox.setStyle("-fx-background-color: #e8f5e9; -fx-text-fill: black; -fx-font-size: 14px;");
        categoryComboBox.setPrefWidth(200);
        categoryLabel.setLabelFor(categoryComboBox);
//        season selection
        ComboBox<String> seasonComboBox = new ComboBox<>();
        seasonComboBox.getItems().addAll("Spring", "Summer", "Autumn", "Winter");
        Label seasonLabel = new Label("Select Season: ");
        seasonComboBox.setPromptText("Select Season");
        seasonComboBox.setStyle("-fx-background-color: #e8f5e9; -fx-text-fill: black; -fx-font-size: 14px;");
        seasonComboBox.setPrefWidth(200);
        seasonLabel.setLabelFor(seasonComboBox);
        // description and price
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
                String productCategory = categoryComboBox.getValue();
                String season = seasonComboBox.getValue();
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int quantity = Integer.parseInt(quantityField.getText());
                String imgUrl = filePath[0];
                int productId = getNumOfProducts() + 1;
                String desc = descArea.getText();
                LocalDate dateSelected = datePicker.getValue();
                Product product = new Product(productId, name, desc, price, quantity, dateSelected, imgUrl, season, productCategory);
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
        listProduct.setStyle("-fx-font-weight: bold;-fx-font-size:22px");
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
                productNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: rgb(230, 151, 32);");
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
        orderProductContent.setPadding(new Insets(30));
        Label listOrders = new Label("Orders Section");
        listOrders.setStyle("-fx-font-weight: bold;-fx-font-size:22px");
        ordersBtn.setOnAction(e->{
            fetchOrders();
            orderProductContent.getChildren().clear();
            orderProductContent.getChildren().add(listOrders);
            for(Order eachOrder: ordersList){
                String path = eachOrder.getProduct().getImageUrl();
                System.out.println("The image path: " + path);
                ImageView imageView = new ImageView(new Image(path));
                imageView.setFitWidth(150);
                imageView.setFitHeight(100);
                Label productNameLabel = new Label(eachOrder.getProduct().getName());
                productNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill:rgb(230, 151, 32);");
                Label priceNameLabel = new Label("Price: " + eachOrder.getProduct().getPrice() + " AED");
                priceNameLabel.setStyle("-fx-font-size: 14px; -fx-text-fill:  white; -fx-font-weight: bold;");
                Label quantityLabel = new Label("Quantity: " + eachOrder.getQuantity());
                quantityLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: white;");
                VBox infoBox = new VBox(5, productNameLabel, priceNameLabel, quantityLabel);
                HBox productCard = new HBox(15, imageView, infoBox);
                productCard.setPadding(new Insets(10));
                productCard.setStyle("-fx-background-color: rgb(111, 139, 111); -fx-background-radius: 10;-fx-border-radius:10px");
                productCard.setAlignment(Pos.CENTER_LEFT);
                orderProductContent.getChildren().add(productCard);
            }
            layout.setCenter(orderProductContent);

        });
        logoutBtn.setOnAction(e->{
            LoginPage loginPage = new LoginPage();
            loginPage.start(primaryStage);
        });
        addProductContent.getChildren().addAll(formTitle, nameLabel, nameField, quantityLabelInput, quantityField,categoryComboBox,seasonComboBox, descLabel, descArea, priceLabel, priceField, selectDateLabel,datePicker,selectDateBtn, imageUploadLabel, imgUploadBtn,saveBtn);
        orderProductContent.getChildren().add(orders);
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
        System.out.println("Writing file....");
        try(FileWriter writer =new FileWriter("product.txt",true)){
            writer.write(product.toFileString()+"\n");
            System.out.println("File written successfully");
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
                int prdId = Integer.parseInt(parameters[0]);
                String prdName = parameters[1];
                String prdDsc = parameters[5];
                double prdPrice = Double.parseDouble(parameters[2]);
                int quantity = Integer.parseInt(parameters[4]);
              LocalDate dateSelected = LocalDate.parse(parameters[6]);
//                LocalDate dateSelected = LocalDate.now();
                String imgUrl = parameters[3];
         String season = parameters[7];
                String productCategory = parameters[8];
                Product newProduct = new Product(prdId, prdName, prdDsc, prdPrice, quantity, dateSelected, imgUrl, season, productCategory);
                productList.add(newProduct);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
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
                String userName = parameters[0];
                int prdId = Integer.parseInt(parameters[2]);
                String prdName = parameters[3];
                double prdPrice = Double.parseDouble(parameters[4]);
                String imgUrl = parameters[5];
                Product newProduct = new Product(prdId, prdName, prdPrice, imgUrl);
                Order newOrder = new Order(ordersList.size()+1,prdId,1,userName,newProduct);
                orderProductList.add(newProduct);
                boolean isFound = false;
                for(Order eachOrder:ordersList){
                    if(eachOrder.getProductId() == newProduct.getProductId()){
                        isFound = true;
                        eachOrder.setQuantity(eachOrder.getQuantity() + 1);
                        break;
                    }
                }
                if(!isFound){
                    ordersList.add(newOrder);
                }
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