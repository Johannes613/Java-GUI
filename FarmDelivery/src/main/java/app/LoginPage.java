package app;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.*;
public class LoginPage extends Application {
    private boolean userHasAccount = true;
    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(1200, 800);
        HBox mainHBox = new HBox();
        AnchorPane.setTopAnchor(mainHBox, 20.0);
        AnchorPane.setBottomAnchor(mainHBox, 20.0);
        AnchorPane.setLeftAnchor(mainHBox, 20.0);
        AnchorPane.setRightAnchor(mainHBox, 20.0);
        mainHBox.setSpacing(20);
        // Left Section
        VBox leftSection = new VBox();
        leftSection.setAlignment(Pos.CENTER);
        leftSection.setSpacing(20);
        leftSection.setPrefWidth(600);
        leftSection.setStyle("-fx-background-color: #f0f0f0;"); // Example styling
        Label title = new Label("Welcome To Farm Delivery");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        VBox.setMargin(title, new Insets(60, 0, 20, 0));
        Label tagline = new Label("Delivering Freshness to Your Doorstep");
        tagline.setStyle("-fx-font-size: 16px;");
        VBox.setMargin(tagline, new Insets(0, 0, 40, 0));
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/org/example/farmdelivery/images/farm.jpg")));
        imageView.setFitWidth(300);
        imageView.setFitHeight(400);
        Button createAccBtn = new Button("Create Account");
        createAccBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;-fx-padding:10px 25px;-fx-font-size:14px;");

        VBox.setMargin(createAccBtn, new Insets(40, 0, 20, 0));

        leftSection.getChildren().addAll(title, tagline, imageView, createAccBtn);
        // Right Section
        VBox rightSection = new VBox();
        rightSection.setAlignment(Pos.CENTER);
        rightSection.setSpacing(20);
        rightSection.setPrefWidth(600);
        Label loginHeader = new Label("Farm Delivery Login");
        loginHeader.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        VBox.setMargin(loginHeader, new Insets(0, 0, 60, 0));
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        HBox emailBox = new HBox(20, new Label("Email Acc"), emailField);
        emailBox.setAlignment(Pos.CENTER);
        VBox.setMargin(emailBox, new Insets(0, 0, 30, 0));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        HBox usernameBox = new HBox(20, new Label("Username"), usernameField);
        usernameBox.setAlignment(Pos.CENTER);
        VBox.setMargin(usernameBox, new Insets(0, 0, 30, 0));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        HBox passwordBox = new HBox(20, new Label("Password"), passwordField);
        passwordBox.setAlignment(Pos.CENTER);
        VBox.setMargin(passwordBox, new Insets(0, 0, 30, 0));
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Farmer", "Customer");
        comboBox.setPromptText("Customer");
        comboBox.setValue("Customer");
        comboBox.setPrefWidth(185);
        HBox comboBoxBox = new HBox(20, new Label("User Type:"), comboBox);
        comboBoxBox.setAlignment(Pos.CENTER);
        VBox.setMargin(comboBoxBox, new Insets(0, 0, 30, 0));
        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(200);
        loginBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        loginBtn.setOnAction(e -> {
            String path;
            if(comboBox.getValue().equals("Farmer")){
                path = "farmer.txt";
            }
            else{
                path = "customer.txt";
            }
            if(userHasAccount){
                try(BufferedReader reader = new BufferedReader(new FileReader(path))){
                    String line;
                    boolean userFound = false;
                    while((line = reader.readLine()) != null && !userFound){
                        String[] userData = line.split(",");
                        if(userData[1].equals(usernameField.getText().trim()) && userData[2].equals(passwordField.getText().trim())){
                            userFound = true;
                            if(comboBox.getValue().equals("Farmer")){
                                FarmerDashboard farmerDashboard = new FarmerDashboard();
                                farmerDashboard.start(primaryStage);
                            }
                            else{
                                try(FileWriter writer = new FileWriter("current_user.txt")){
                                    writer.write(usernameField.getText().trim() + "," + passwordField.getText().trim());
                                }
                                catch (IOException ex){
                                    System.out.println(ex.getMessage());
                                }
                                ShoppingPage shp = new ShoppingPage();
                                shp.start(primaryStage);
                            }
                        }
                    }
                    if(!userFound){
                        Alert alert = new Alert(Alert.AlertType.ERROR,"Username or Password is wrong, please check again");
                        alert.show();
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
            else {
                try(FileWriter writer = new FileWriter(path,true)){
                    writer.write(emailField.getText().trim() + "," + usernameField.getText().trim() +"," + passwordField.getText().trim()+"\n");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Account Created successfully");
                alert.show();
                if(comboBox.getValue().equals("Farmer")){
                    FarmerDashboard farmerDashboard = new FarmerDashboard();
                    farmerDashboard.start(primaryStage);
                }
                else{
                    try(FileWriter writer = new FileWriter("current_user.txt")){
                        writer.write(usernameField.getText().trim() + "," + passwordField.getText().trim());
                    }
                    catch (IOException ex){
                        System.out.println(ex.getMessage());
                    }
                    ShoppingPage shp = new ShoppingPage();
                    shp.start(primaryStage);
                }

            }

        });
        rightSection.getChildren().addAll(loginHeader, usernameBox, passwordBox, comboBoxBox, loginBtn);
        createAccBtn.setOnAction(e -> {
            userHasAccount = !userHasAccount;
            if(!userHasAccount){
                createAccBtn.setText("Login");
                loginBtn.setText("Create Account");
            }
            else{
                createAccBtn.setText("Create Account");
                loginBtn.setText("Login");
            }
            if(userHasAccount){
                rightSection.getChildren().clear();
                rightSection.getChildren().addAll(loginHeader, usernameBox, passwordBox, comboBoxBox, loginBtn);
            }
            else{
                rightSection.getChildren().clear();
                rightSection.getChildren().addAll(loginHeader, emailBox, usernameBox, passwordBox, comboBoxBox, loginBtn);
            }
        });

        // Add both sections to HBox
        mainHBox.getChildren().addAll(leftSection, rightSection);
        root.getChildren().add(mainHBox);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Farm Delivery Login");
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
    public void handleLoginUser(){
    }
}