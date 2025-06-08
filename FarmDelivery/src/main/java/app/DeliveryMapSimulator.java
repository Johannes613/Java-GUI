package app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects; // Needed for Objects.requireNonNull

public class DeliveryMapSimulator extends Application {

    // UI Elements
    private Pane mapPane;
    private Circle deliveryVehicle;
    private Label trackingStatusLabel;
    private Timeline timeline;
    private List<Line> pathLines = new ArrayList<>(); // To draw the zigzag path

    // Map and Delivery Parameters
    private static final double MAP_WIDTH = 1200; // Updated to match ImageView size
    private static final double MAP_HEIGHT = 800; // Updated to match ImageView size
    private static final double PADDING = 50; // Padding from map edges for start/end points
    private static final int ZIGZAG_SEGMENTS = 20; // Number of segments in the zigzag path
    private static final double ZIGZAG_AMPLITUDE = 30; // How much the path deviates from straight
    private static final double DELIVERY_SPEED_PIXELS_PER_SEC = 50; // Speed of the delivery vehicle

    // Local map image path as provided by the user
    // IMPORTANT: Ensure 'map.png' is in 'src/main/resources/org/example/farmdelivery/images/'
    private static final String MAP_IMAGE_PATH = "/org/example/farmdelivery/images/map.png";

    @Override
    public void start(Stage primaryStage) {
        // --- 1. Root Pane and Background Map Image (using ImageView) ---
        mapPane = new Pane();
        mapPane.setPrefSize(MAP_WIDTH, MAP_HEIGHT);
        // Add a temporary background color to mapPane for debugging its boundaries
        mapPane.setStyle("-fx-border-color: #AAAAAA; -fx-border-width: 1; -fx-background-color: #E0E0E0;");

        // Load map image using ImageView as specified by the user
        ImageView backgroundImageView;
        try {
            System.out.println("Attempting to load image from: " + MAP_IMAGE_PATH);
            backgroundImageView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(MAP_IMAGE_PATH))));
        } catch (NullPointerException e) {
            System.err.println("Error: 'map.png' not found at '" + MAP_IMAGE_PATH + "'. Make sure it's in the correct resource path. Falling back to placeholder.");
            // Use a placeholder image if the local file is not found
            backgroundImageView = new ImageView(new Image("https://placehold.co/" + (int)MAP_WIDTH + "x" + (int)MAP_HEIGHT + "/FF0000/FFFFFF?text=MAP+NOT+FOUND"));
        } catch (Exception e) {
            System.err.println("Error loading map image: " + e.getMessage() + ". Falling back to placeholder.");
            backgroundImageView = new ImageView(new Image("https://placehold.co/" + (int)MAP_WIDTH + "x" + (int)MAP_HEIGHT + "/FF0000/FFFFFF?text=ERROR+LOADING+MAP"));
        }

        backgroundImageView.setFitWidth(MAP_WIDTH);
        backgroundImageView.setFitHeight(MAP_HEIGHT);
        backgroundImageView.setPreserveRatio(false);

        // Add the ImageView as the first child of mapPane to ensure it's at the very bottom layer within mapPane
        mapPane.getChildren().add(backgroundImageView);


        // --- 2. Define Start and End Points for Delivery ---
        double startX = MAP_WIDTH - PADDING;
        double startY = PADDING;
        double endX = PADDING;
        double endY = MAP_HEIGHT - PADDING;


        // --- 3. Create Delivery Vehicle (Red Circle) ---
        deliveryVehicle = new Circle(10, Color.RED);
        deliveryVehicle.setStroke(Color.DARKRED);
        deliveryVehicle.setStrokeWidth(1.5);
        deliveryVehicle.setCenterX(startX);
        deliveryVehicle.setCenterY(startY);
        mapPane.getChildren().add(deliveryVehicle);


        // --- 4. Generate Zigzag Path Points and Draw Path Lines ---
        // Using a separate list for visual lines added to mapPane
        List<Line> visualPathSegments = new ArrayList<>();
        List<javafx.geometry.Point2D> pathPoints = generateZigzagPath(startX, startY, endX, endY, ZIGZAG_SEGMENTS, ZIGZAG_AMPLITUDE);

        for (int i = 0; i < pathPoints.size() - 1; i++) {
            Line segment = new Line(pathPoints.get(i).getX(), pathPoints.get(i).getY(),
                    pathPoints.get(i + 1).getX(), pathPoints.get(i + 1).getY());
            segment.setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.5));
            segment.setStrokeWidth(2);
            visualPathSegments.add(segment);
            mapPane.getChildren().add(segment); // Add each segment to mapPane
        }

        // Ensure the delivery vehicle and path lines are on top of the background image
        visualPathSegments.forEach(Line::toFront); // Bring all path lines to front
        deliveryVehicle.toFront(); // Bring vehicle to front (on top of lines as well)


        // --- 5. Animate the Circle along the Zigzag Path ---
        timeline = new Timeline();
        Duration currentAccumulatedDuration = Duration.ZERO;

        for (int i = 0; i < pathPoints.size() - 1; i++) {
            javafx.geometry.Point2D startPoint = pathPoints.get(i);
            javafx.geometry.Point2D endPoint = pathPoints.get(i + 1);

            double segmentDistance = startPoint.distance(endPoint);
            Duration segmentDuration = Duration.seconds(segmentDistance / DELIVERY_SPEED_PIXELS_PER_SEC);

            KeyFrame keyFrame = new KeyFrame(currentAccumulatedDuration.add(segmentDuration),
                    e -> {
                        deliveryVehicle.setCenterX(endPoint.getX());
                        deliveryVehicle.setCenterY(endPoint.getY());
                        updateTrackingStatus(timeline.getCurrentTime().toMillis() / timeline.getTotalDuration().toMillis());
                    });
            timeline.getKeyFrames().add(keyFrame);
            currentAccumulatedDuration = currentAccumulatedDuration.add(segmentDuration);
        }

        timeline.getKeyFrames().add(new KeyFrame(timeline.getTotalDuration(), e -> {
            updateTrackingStatus(1.0);
        }));

        // --- 6. Display Tracking Information Label ---
        trackingStatusLabel = new Label("Delivery Status: Initiating...");
        trackingStatusLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        trackingStatusLabel.setTextFill(Color.DARKBLUE);
        trackingStatusLabel.setPadding(new Insets(10));
        trackingStatusLabel.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8); -fx-background-radius: 8; -fx-border-color: #6B5B95; -fx-border-radius: 8; -fx-border-width: 1;");

        StackPane statusPane = new StackPane(trackingStatusLabel);
        StackPane.setAlignment(statusPane, Pos.TOP_CENTER);
        StackPane.setMargin(statusPane, new Insets(20, 0, 0, 0));


        // --- 7. Add "Back to Shopping" Button ---
        Button backToShoppingBtn = new Button("Back to Shopping");
        backToShoppingBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        // Use a distinct, temporary style to ensure it's visible during debugging
        backToShoppingBtn.setStyle("-fx-background-color: #FF00FF; -fx-text-fill: yellow; -fx-background-radius: 10; -fx-padding: 10 20; -fx-border-color: black; -fx-border-width: 3;");
        backToShoppingBtn.setOnMouseEntered(e -> backToShoppingBtn.setStyle("-fx-background-color: #CC00CC; -fx-text-fill: white; -fx-background-radius: 10; -fx-padding: 10 20; -fx-border-color: black; -fx-border-width: 3;"));
        backToShoppingBtn.setOnMouseExited(e -> backToShoppingBtn.setStyle("-fx-background-color: #FF00FF; -fx-text-fill: yellow; -fx-background-radius: 10; -fx-padding: 10 20; -fx-border-color: black; -fx-border-width: 3;"));

        StackPane.setAlignment(backToShoppingBtn, Pos.TOP_LEFT);
        StackPane.setMargin(backToShoppingBtn, new Insets(0, 20, 20, 0)); // Margin from bottom and right edges of StackPane

        backToShoppingBtn.setOnAction(event -> {
            if (timeline != null) {
                timeline.stop(); // Stop the animation before navigating
            }
            // Assuming ShoppingPage exists in the same 'app' package or is properly imported
            ShoppingPage shoppingPage = new ShoppingPage();
            try {
                shoppingPage.start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
                // Optionally show an alert if ShoppingPage fails to load
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "Failed to go to Shopping Page. Error: " + e.getMessage()).show();
            }
        });


        // --- Layout ---
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #DDDDDD;"); // Overall window background

        // Add children to the root StackPane in the correct order for layering:
        root.getChildren().add(mapPane); // Add mapPane first (bottom layer)
        root.getChildren().add(statusPane); // Add statusPane on top of mapPane
        root.getChildren().add(backToShoppingBtn); // Add button on top of everything else

        Scene scene = new Scene(root, MAP_WIDTH + 80, MAP_HEIGHT + 80); // Scene size larger than map to show root's background
        primaryStage.setTitle("Real-time Delivery Tracking Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();

        timeline.play();
    }


    private List<javafx.geometry.Point2D> generateZigzagPath(double startX, double startY, double endX, double endY, int segments, double amplitude) {
        List<javafx.geometry.Point2D> points = new ArrayList<>();
        points.add(new javafx.geometry.Point2D(startX, startY));

        double dx = endX - startX;
        double dy = endY - startY;
        double totalDistance = Math.sqrt(dx * dx + dy * dy);

        double perpDx = -dy;
        double perpDy = dx;
        double perpLength = Math.sqrt(perpDx * perpDx + perpDy * perpDy);

        if (perpLength != 0) {
            perpDx /= perpLength;
            perpDy /= perpLength;
        } else {
            if (dx == 0) {
                perpDx = 1;
                perpDy = 0;
            } else if (dy == 0) {
                perpDx = 0;
                perpDy = 1;
            }
        }

        boolean oscillateUp = true;

        for (int i = 1; i <= segments; i++) {
            double straightX = startX + dx * ((double) i / segments);
            double straightY = startY + dy * ((double) i / segments);

            double offset = (oscillateUp ? 1 : -1) * amplitude * Math.sin((double)i / segments * Math.PI);

            double zigzagX = straightX + offset * perpDx;
            double zigzagY = straightY + offset * perpDy;

            points.add(new javafx.geometry.Point2D(zigzagX, zigzagY));
            oscillateUp = !oscillateUp;
        }

        // Ensure the last point is exactly the end point, not a zigzag offset
        // This handles cases where segments calculation might not perfectly align
        // with the exact end point after zigzagging.
        if (points.size() > segments) { // This check prevents IndexOutOfBounds if segments is 0 or 1
            points.set(points.size() - 1, new javafx.geometry.Point2D(endX, endY));
        } else if (segments >= 1) { // If segments is 0, path is just start point
            points.add(new javafx.geometry.Point2D(endX, endY));
        }


        return points;
    }


    private void updateTrackingStatus(double progress) {
        if (progress < 0.01) {
            trackingStatusLabel.setText("Delivery Status: Initiating...");
            trackingStatusLabel.setTextFill(Color.DARKBLUE);
        } else if (progress < 0.25) {
            trackingStatusLabel.setText("Delivery Status: Picking up product...");
            trackingStatusLabel.setTextFill(Color.DARKORANGE);
        } else if (progress < 0.5) {
            trackingStatusLabel.setText("Delivery Status: On the way!");
            trackingStatusLabel.setTextFill(Color.BLUE);
        } else if (progress < 0.75) {
            trackingStatusLabel.setText("Delivery Status: Almost there!");
            trackingStatusLabel.setTextFill(Color.GREEN);
        } else if (progress < 0.99) {
            trackingStatusLabel.setText("Delivery Status: Arriving soon!");
            trackingStatusLabel.setTextFill(Color.DARKGREEN);
        } else {
            trackingStatusLabel.setText("Delivery Status: Delivered! Thank you!");
            trackingStatusLabel.setTextFill(Color.DARKRED);
        }
    }

    @Override
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}