package com.hurricane;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * JavaFX App
 *
 * <p>The scene's root is a shell that never changes. Screens are swapped in and
 * out of it with an animation.</p>
 */
public class App extends Application {

    /** How a new screen comes in. */
    enum Transition {
        /** New screen comes in from the right, e.g. going forward from login to sign-up. */
        SLIDE_LEFT,
        /** New screen comes in from the left, e.g. going back from sign-up to login. */
        SLIDE_RIGHT,
        /** Old screen fades out and the new one fades and scales in, e.g. logging in or out. */
        FADE
    }

    private static final Duration TRANSITION_TIME = Duration.millis(380);
    private static final Interpolator EASE_OUT = Interpolator.SPLINE(0.2, 0.0, 0.0, 1.0);


    private static StackPane content;
    private static Node currentScreen;
    private static boolean animating;

    @Override
    public void start(Stage stage) throws IOException {
        content = new StackPane();
        content.getStyleClass().add("shell");
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(content.widthProperty());
        clip.heightProperty().bind(content.heightProperty());
        content.setClip(clip);

        currentScreen = loadFXML("login");
        content.getChildren().add(currentScreen);

        Scene scene = new Scene(content, 900, 640);
        scene.getStylesheets().add(App.class.getResource("styles.css").toExternalForm());
        stage.setTitle("The Keys Hurricane Relief");
        stage.setMinWidth(480);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        setRoot(fxml, Transition.FADE);
    }

    static void setRoot(String fxml, Transition transition) throws IOException {
        if (animating) {
            return;
        }
        Node oldScreen = currentScreen;
        Node newScreen = loadFXML(fxml);
        currentScreen = newScreen;
        content.getChildren().add(newScreen);

        ParallelTransition animation;
        if (transition == Transition.FADE) {
            animation = fadeBetween(oldScreen, newScreen);
        } else {
            double direction = transition == Transition.SLIDE_LEFT ? 1 : -1;
            animation = slideBetween(oldScreen, newScreen, direction);
        }

        // Draw each screen once and move the picture, instead of redrawing its shadows every frame
        setCached(oldScreen, true);
        setCached(newScreen, true);

        animating = true;
        animation.setOnFinished(event -> {
            content.getChildren().remove(oldScreen);
            setCached(newScreen, false);
            animating = false;
        });
        animation.play();
    }

    private static void setCached(Node node, boolean cached) {
        node.setCache(cached);
        node.setCacheHint(cached ? CacheHint.SPEED : CacheHint.DEFAULT);
    }

    /**
     * Slides the new screen over the old one, which drifts the other way and fades,
     * so it looks like it is sliding underneath.
     */
    private static ParallelTransition slideBetween(Node oldScreen, Node newScreen, double direction) {
        double width = content.getWidth();

        TranslateTransition slideIn = new TranslateTransition(TRANSITION_TIME, newScreen);
        slideIn.setFromX(direction * width);
        slideIn.setToX(0);

        TranslateTransition slideOut = new TranslateTransition(TRANSITION_TIME, oldScreen);
        slideOut.setToX(-direction * width * 0.3);

        FadeTransition fadeOut = new FadeTransition(TRANSITION_TIME, oldScreen);
        fadeOut.setToValue(0);

        return withEasing(new ParallelTransition(slideIn, slideOut, fadeOut));
    }

    /** Fades the old screen out while the new one fades in and grows slightly into place. */
    private static ParallelTransition fadeBetween(Node oldScreen, Node newScreen) {
        newScreen.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(TRANSITION_TIME, newScreen);
        fadeIn.setToValue(1);

        ScaleTransition grow = new ScaleTransition(TRANSITION_TIME, newScreen);
        grow.setFromX(0.96);
        grow.setFromY(0.96);
        grow.setToX(1);
        grow.setToY(1);

        FadeTransition fadeOut = new FadeTransition(TRANSITION_TIME.divide(2), oldScreen);
        fadeOut.setToValue(0);

        return withEasing(new ParallelTransition(fadeIn, grow, fadeOut));
    }

    private static ParallelTransition withEasing(ParallelTransition animation) {
        animation.getChildren().forEach(child -> {
            if (child instanceof javafx.animation.Transition) {
                ((javafx.animation.Transition) child).setInterpolator(EASE_OUT);
            }
        });
        return animation;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
