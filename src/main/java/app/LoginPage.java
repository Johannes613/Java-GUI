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
public class LoginPage extends Application {
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
        createAccBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        VBox.setMargin(createAccBtn, new Insets(40, 0, 20, 0));
        createAccBtn.setOnAction(e -> {
            // Handle create account logic here
            System.out.println("Create Account button clicked");
        });

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
        comboBox.setPrefWidth(185);
        HBox comboBoxBox = new HBox(20, new Label("User Type:"), comboBox);
        comboBoxBox.setAlignment(Pos.CENTER);
        VBox.setMargin(comboBoxBox, new Insets(0, 0, 30, 0));

        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(200);
        loginBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        loginBtn.setOnAction(e -> {
//            handleLogin(e);
            ShoppingPage shp=new ShoppingPage();
            shp.start(primaryStage);
            System.out.println("Login button clicked");
        });

        rightSection.getChildren().addAll(loginHeader, emailBox, usernameBox, passwordBox, comboBoxBox, loginBtn);

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
}

