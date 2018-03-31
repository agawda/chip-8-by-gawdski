package com.gawdski.chip8bygawdski.graphics;


import com.gawdski.chip8bygawdski.Keyboard;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class Screen {
    private static final double SC_WIDTH = 64;
    private static final double SC_HEIGHT = 32;

    private final int scale;

    private Color foregroundColor;
    private Color backgroundColor;

    private List<Point> pointsToDraw;

    private Keyboard keyboard;

    public Screen(Keyboard keyboard, int scale) {
        this.keyboard = keyboard;
        this.scale = scale;
        this.foregroundColor = Color.WHITE;
        this.backgroundColor = Color.BLACK;
        pointsToDraw = new ArrayList<>();
    }

    public void run() {
        Stage primaryStage = new Stage();
        primaryStage.setTitle("chip-8 emulator");
        Group root = new Group();
        DoubleProperty x = new SimpleDoubleProperty();
        DoubleProperty y = new SimpleDoubleProperty();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0),
                        new KeyValue(x, 0),
                        new KeyValue(y, 0)
                ),
                new KeyFrame(Duration.seconds(3),
                        new KeyValue(x, 200),
                        new KeyValue(y, 200)
                )
        );
        timeline.setAutoReverse(true);
        timeline.setCycleCount(Timeline.INDEFINITE);
        Canvas canvas = new Canvas(SC_WIDTH * scale, SC_HEIGHT * scale);
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                GraphicsContext gc = canvas.getGraphicsContext2D();
                draw(gc);
            }
        };
        root.getChildren().add(canvas);

        Scene scene = new Scene(root, backgroundColor);

        createKeybindings(scene);

        primaryStage.setScene(scene);
        primaryStage.show();
        timer.start();
        timeline.play();
    }

    private void createKeybindings(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, key -> keyboard.handlePressed(key.getCode()));
        scene.addEventHandler(KeyEvent.KEY_RELEASED, key -> keyboard.handleReleased(key.getCode()));
    }

    public void drawOnePixel(double x, double y) {
        Point point = new Point(x * scale, y * scale, scale, scale);
        pointsToDraw.add(point);
    }

    public void drawSprite(double x, double y, double w, double h) {
        Point point = new Point(x * scale, y * scale, w * scale, h * scale);
        pointsToDraw.add(point);
    }

    public void clearDisplay() {
        pointsToDraw = new ArrayList<>();
    }

    private void draw(GraphicsContext graphicsContext) {
        graphicsContext.setFill(foregroundColor);
        pointsToDraw.forEach(point ->
                graphicsContext.fillRect(
                        point.getX() * scale,
                        point.getY() * scale,
                        point.getWidth() * scale,
                        point.getHeight() * scale));
    }

    private class Point {
        private double x;
        private double y;
        private double width;
        private double height;

        Point(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        double getX() {
            return x;
        }

        double getY() {
            return y;
        }

        double getHeight() {
            return height;
        }

        double getWidth() {
            return width;
        }

    }

}
