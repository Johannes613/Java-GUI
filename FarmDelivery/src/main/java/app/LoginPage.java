package app;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.io.*;
public class LoginPage extends Application {
    private boolean userHasAccount = true;
    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        // Background Image
        ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/org/example/farmdelivery/images/fruit.jpg")));
        background.setFitWidth(1200);
        background.setFitHeight(800);
        background.setPreserveRatio(false);
        // Glass Pane (Login Card)
        VBox loginCard = new VBox(20);
        loginCard.setMaxHeight(400);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setPadding(new Insets(40));
        loginCard.setMaxWidth(400);
        loginCard.setBackground(new Background(new BackgroundFill(Color.rgb(0, 180, 216), new CornerRadii(20), Insets.EMPTY)));
        loginCard.setEffect(new BoxBlur(5, 5, 3));
        loginCard.setBorder(new Border(new BorderStroke(Color.rgb(255, 255, 255, 0.3), BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(1))));
        DropShadow cardShadow = new DropShadow();
        cardShadow.setRadius(10);
        cardShadow.setOffsetY(4);
        cardShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        loginCard.setEffect(cardShadow);
        // Title
        Label loginHeader = new Label("Welcome To EatFresh");
        loginHeader.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));
        loginHeader.setTextFill(Color.WHITE);
        // Username Field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        styleGlassInput(usernameField);
        // Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        styleGlassInput(passwordField);
        // Role Selection
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Farmer", "Customer");
        comboBox.setValue("Customer");
        comboBox.setPromptText("User Type");
        comboBox.setPrefWidth(400.0);
        styleGlassComboBox(comboBox);
        // Email field (only shown when creating account)
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        styleGlassInput(emailField);
        // Button
        Button loginBtn = new Button("Login");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        styleGlassButton(loginBtn);
        // Switch between login/register
        Hyperlink switchLink = new Hyperlink("Don't have an account? Create one");
        switchLink.setStyle("-fx-text-fill: white;");
        switchLink.setOnAction(e -> {
            userHasAccount = !userHasAccount;
            if (userHasAccount) {
                loginBtn.setText("Login");
                switchLink.setText("Don't have an account? Create one");
                loginCard.getChildren().setAll(loginHeader, usernameField, passwordField, comboBox, loginBtn, switchLink);
            } else {
                loginBtn.setText("Create Account");
                switchLink.setText("Already have an account? Login");
                loginCard.getChildren().setAll(loginHeader, emailField, usernameField, passwordField, comboBox, loginBtn, switchLink);
            }
        });
        loginBtn.setOnAction(e -> {
            String path = comboBox.getValue().equals("Farmer") ? "farmer.txt" : "customer.txt";
            if (userHasAccount) {
                try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                    String line;
                    boolean userFound = false;
                    while ((line = reader.readLine()) != null) {
                        String[] userData = line.split(",");
                        if (userData[1].equals(usernameField.getText().trim()) && userData[2].equals(passwordField.getText().trim())) {
                            userFound = true;
                            if (comboBox.getValue().equals("Farmer")) {
                                new FarmerDashboard().start(primaryStage);
                            } else {
                                try (FileWriter writer = new FileWriter("current_user.txt")) {
                                    writer.write(usernameField.getText().trim() + "," + passwordField.getText().trim());
                                }
                                new ShoppingPage().start(primaryStage);
                            }
                            break;
                        }
                    }
                    if (!userFound) {
                        showAlert("Login Failed", "Username or Password is incorrect.");
                    }
                } catch (IOException ex) {
                    showAlert("Error", "File read error: " + ex.getMessage());
                }
            } else {
                try (FileWriter writer = new FileWriter(path, true)) {
                    if(emailField.getText().trim().isEmpty() || usernameField.getText().trim().isEmpty() || passwordField.getText().isEmpty()){
                        Alert accAlert  = new Alert(Alert.AlertType.ERROR,"Please make sure your inputs are complete");
                        accAlert.show();
                    }
                    else{
                        writer.write(emailField.getText().trim() + "," + usernameField.getText().trim() + "," + passwordField.getText().trim() + "\n");
                        showAlert("Account Created", "Your account was successfully created.");
                    }
                } catch (IOException ex) {
                    showAlert("Error", "File write error: " + ex.getMessage());
                }
            }
        });
        // Initial State
        loginCard.getChildren().addAll(loginHeader, usernameField, passwordField, comboBox, loginBtn, switchLink);
        root.getChildren().addAll(background, loginCard);
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Farm Delivery - Login");
        primaryStage.show();
    }
    private void styleGlassInput(TextField field) {
        field.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2);" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: rgba(255,255,255,0.7);" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 10 14;" +
                        "-fx-font-size: 14px;"
        );
    }
    private void styleGlassComboBox(ComboBox<String> comboBox) {
        comboBox.setStyle(
                "-fx-background-color: rgba(255,255,255,0.2);" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: rgba(255,255,255,0.7);" +
                        "-fx-border-color: rgba(255,255,255,0.4);" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8 12;" +
                        "-fx-font-size: 14px;"
        );
    }
    private void styleGlassButton(Button button) {
        button.setStyle(
                "-fx-background-color: linear-gradient(to right, #4f46e5, #3b82f6);" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 12;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 10 20;"
        );
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    public static void main(String[] args) {
        launch(args);
    }
}