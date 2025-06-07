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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.Product;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
public class FarmerDashboard extends Application {
    private ArrayList<Product> productList = new ArrayList<>();
    private ArrayList<Order> ordersList = new ArrayList<>();
    private ArrayList<Product> orderProductList = new ArrayList<>();
    private VBox subscriberContent = new VBox(15);


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
        Button subscribersBtn = new Button("Subscribers");
        subscribersBtn.setMaxWidth(150);
        subscribersBtn.setPrefHeight(33);
        subscribersBtn.setStyle("-fx-background-color:#2e7d32; -fx-text-fill:white;-fx-font-weight:bold; -fx-font-size:13px;");
        sideNav.getChildren().addAll(addProductBtn, listProductBtn, ordersBtn,subscribersBtn, logoutBtn);



// add product content
        VBox addProductContent = new VBox(15);
        addProductContent.setStyle("-fx-background-color: #f1f8e9;");
        addProductContent.setPadding(new Insets(25));

        Label formTitle = new Label("Add New Product");
        formTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        formTitle.setStyle("-fx-text-fill: #2e7d32;");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter product name: ");
        nameField.setStyle("-fx-font-size: 14px; -fx-background-color: #ffffff; -fx-border-color: #c5e1a5; -fx-border-radius: 5;");
        Label nameLabel = new Label("Product Name:");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Enter quantity: ");
        quantityField.setStyle("-fx-font-size: 14px; -fx-background-color: #ffffff; -fx-border-color: #c5e1a5; -fx-border-radius: 5;");
        Label quantityLabelInput = new Label("Quantity:");

        Label categoryLabel = new Label("Product Category:");
        ComboBox<String> categoryComboBox = new ComboBox<>();
        categoryComboBox.getItems().addAll(
                "Milk Products", "Fruits", "Vegetables", "Cereal Products", "Cereal", "Meat & Poultry",
                "Fish & Seafood", "Bakery Products", "Eggs", "Spices & Condiments", "Oils & Fats", "Snacks & Chips",
                "Beverages", "Dry Fruits & Nuts", "Grains & Pulses", "Legumes", "Herbs", "Frozen Foods",
                "Organic Products", "Dairy Alternatives", "Baby Products", "Health & Nutrition", "Cleaning Supplies",
                "Personal Care", "Pet Supplies", "Ready-to-Eat Foods", "Canned Goods", "Sauces & Pickles", "Flours", "Sugars & Sweeteners"
        );
        categoryComboBox.setPromptText("Select Product Category");
        categoryComboBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #c5e1a5; -fx-font-size: 14px;");
        categoryComboBox.setPrefWidth(220);

        Label seasonLabel = new Label("Select Season:");
        ComboBox<String> seasonComboBox = new ComboBox<>();
        seasonComboBox.getItems().addAll("Spring", "Summer", "Autumn", "Winter");
        seasonComboBox.setPromptText("Select Season");
        seasonComboBox.setStyle("-fx-background-color: #ffffff; -fx-border-color: #c5e1a5; -fx-font-size: 14px;");
        seasonComboBox.setPrefWidth(220);

        Label descLabel = new Label("Description:");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Enter product description:");
        descArea.setPrefRowCount(4);
        descArea.setStyle("-fx-font-size: 14px; -fx-border-color: #c5e1a5; -fx-border-radius: 5;");

        Label priceLabel = new Label("Product Price:");
        TextField priceField = new TextField();
        priceField.setPromptText("Enter product price:");
        priceField.setStyle("-fx-font-size: 14px; -fx-background-color: #ffffff; -fx-border-color: #c5e1a5; -fx-border-radius: 5;");

        Label selectDateLabel = new Label("Select Calendar:");
        Button selectDateBtn = new Button("Click to Select Date");
        selectDateBtn.setStyle("-fx-background-color: #388e3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Harvest Date");
        datePicker.setVisible(false);
        datePicker.setStyle("-fx-font-size: 13px;");

        selectDateBtn.setOnAction(e -> datePicker.setVisible(true));


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image of the Product");
        String[] filePath = new String[1];

        Label imageUploadLabel = new Label("Upload Product Image:");
        Button imgUploadBtn = new Button("Click to Upload Image");
        imgUploadBtn.setStyle("-fx-background-color: #388e3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");
        imgUploadBtn.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                filePath[0] = selectedFile.getAbsolutePath();
                imgUploadBtn.setText("Selected: " + selectedFile.getName());
            }
        });


        Button saveBtn = new Button("Save Product");
        saveBtn.setStyle("-fx-background-color: #2e7d10; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
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
                Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid Number format");
                alert.show();
            }
        });


        // layout structure
        BorderPane layout = new BorderPane();
        layout.setLeft(sideNav);
        layout.setTop(header);
        layout.setCenter(addProductContent);

        subscribersBtn.setOnAction(e -> {
            fetchSubscribers();
            subscriberContent.setPadding(new Insets(30));
            layout.setCenter(subscriberContent);
        });

        // list product content
        VBox listProductContent = new VBox(15);
        listProductContent.setPadding(new Insets(30));
        Label listProduct = new Label("List Product Section");
        listProduct.setStyle("-fx-font-weight: bold;-fx-font-size:22px");

        // orders content
        VBox orderProductContent = new VBox(15);
        orderProductContent.setPadding(new Insets(30));
        Label listOrders = new Label("Orders Section");
        listOrders.setStyle("-fx-font-weight: bold;-fx-font-size:22px");


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
        ordersBtn.setOnAction(e->{
            fetchOrders();
            orderProductContent.getChildren().clear();
            orderProductContent.getChildren().add(listOrders);
            for(Order eachOrder: ordersList){
                String path = eachOrder.getProduct().getImageUrl();
                System.out.println("The image path: " + path);
                Image image = new Image(new File(path).toURI().toString());
                ImageView imageView = new ImageView(image);
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
        addProductBtn.setOnAction(e -> layout.setCenter(addProductContent));
        logoutBtn.setOnAction(e->{
            LoginPage loginPage = new LoginPage();
            loginPage.start(primaryStage);
        });


        addProductContent.getChildren().addAll(formTitle, nameLabel, nameField, quantityLabelInput, quantityField,categoryComboBox,seasonComboBox, descLabel, descArea, priceLabel, priceField, selectDateLabel,datePicker,selectDateBtn, imageUploadLabel, imgUploadBtn,saveBtn);
        orderProductContent.getChildren().add(listOrders);


        // scene and stage configuration
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

    private void fetchSubscribers() {
        subscriberContent.getChildren().clear();

        Label subscriberTitle = new Label("Subscriber List");
        subscriberTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        subscriberTitle.setPadding(new Insets(10, 0, 20, 0));
        subscriberContent.getChildren().add(subscriberTitle);

        try (BufferedReader reader = new BufferedReader(new FileReader("subscription.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 4) {
                    String name = tokens[0];
                    String email = tokens[1];
                    String product = tokens[2];
                    String plan = tokens[3];

                    Label nameLabel = new Label("Name: " + name);
                    Label emailLabel = new Label("Email: " + email);
                    Label productLabel = new Label("Product: " + product);
                    Label planLabel = new Label("Plan: " + plan);

                    nameLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
                    emailLabel.setStyle("-fx-font-size: 14px;");
                    productLabel.setStyle("-fx-font-size: 14px;");
                    planLabel.setStyle("-fx-font-size: 14px;");

                    VBox subscriberBox = new VBox(5, nameLabel, emailLabel, productLabel, planLabel);
                    subscriberBox.setPadding(new Insets(15));
                    subscriberBox.setStyle(
                            "-fx-background-color: #f1f8e9;" +
                                    "-fx-background-radius: 10;" +
                                    "-fx-border-color: #c5e1a5;" +
                                    "-fx-border-width: 1;" +
                                    "-fx-border-radius: 10;"
                    );

                    VBox.setMargin(subscriberBox, new Insets(10, 0, 10, 0));
                    subscriberContent.getChildren().add(subscriberBox);
                }
            }
        } catch (IOException e) {
            Label errorLabel = new Label("Error reading subscription file.");
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