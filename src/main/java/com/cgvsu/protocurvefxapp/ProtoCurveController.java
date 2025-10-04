package com.cgvsu.protocurvefxapp;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class ProtoCurveController {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private Canvas canvas;

    ArrayList<Point2D> points = new ArrayList<Point2D>();
    private final double STEP = 0.001;
    final int POINT_RADIUS = 3;

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> {
            canvas.setWidth(newValue.doubleValue());
            redrawAll();
        });
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> {
            canvas.setHeight(newValue.doubleValue());
            redrawAll();
        });

        canvas.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY -> handlePrimaryClick(canvas.getGraphicsContext2D(), event);
                case SECONDARY -> clear(canvas.getGraphicsContext2D());
            }
        });
    }

    private void clear(GraphicsContext graphicsContext){
        points.clear();
        redrawAll();
    }

    private void redrawAll(){
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        drawPointsAndLine(graphicsContext);
        if (points.size() > 2) {
            drawBezierCurve(graphicsContext);
        }
    }

    private void handlePrimaryClick(GraphicsContext graphicsContext, MouseEvent event) {
        final Point2D clickPoint = new Point2D(event.getX(), event.getY());
        points.add(clickPoint);
        redrawAll();
    }

    private void drawPointsAndLine(GraphicsContext graphicsContext){
        for (Point2D point : points) {
            graphicsContext.fillOval(
                    point.getX() - POINT_RADIUS, point.getY() - POINT_RADIUS,
                    2 * POINT_RADIUS, 2 * POINT_RADIUS);
        }

        if (points.size() > 0) {
            graphicsContext.setStroke(Color.BLACK);

            for (int i = 0; i< points.size()-1; i++){
                Point2D curr = points.get(i);
                Point2D next = points.get(i+1);
                graphicsContext.strokeLine(curr.getX(), curr.getY(), next.getX(), next.getY());
            }
        }
    }
    private void drawBezierCurve(GraphicsContext graphicsContext){
        List<Point2D> bezierPoints = BezierCurve.calculateBezierCurve(points, STEP);

        graphicsContext.setStroke(Color.RED);
        graphicsContext.setLineWidth(2);

        for (int i = 0; i < bezierPoints.size() - 1; i++) {
            Point2D current = bezierPoints.get(i);
            Point2D next = bezierPoints.get(i + 1);
            graphicsContext.strokeLine(current.getX(), current.getY(), next.getX(), next.getY());
        }
    }
}