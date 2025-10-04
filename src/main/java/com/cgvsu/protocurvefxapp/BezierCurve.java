package com.cgvsu.protocurvefxapp;

import javafx.geometry.Point2D;
import java.util.ArrayList;
import java.util.List;

public class BezierCurve {

    public static List<Point2D> calculateBezierCurve(List<Point2D> controlPoints, double step) {
        List<Point2D> curvePoints = new ArrayList<>();
        int n = controlPoints.size() - 1;

        if (n < 1) return curvePoints;

        for (double t = 0; t <= 1; t += step) {
            Point2D point = calculateBezierPoint(controlPoints, t, n);
            curvePoints.add(point);
        }

        curvePoints.add(controlPoints.get(n));

        return curvePoints;
    }

    private static Point2D calculateBezierPoint(List<Point2D> controlPoints, double t, int n) {
        double x = 0;
        double y = 0;

        for (int k = 0; k <= n; k++) {
            double binomial = binomialCoefficient(n, k);
            double basis = binomial * Math.pow(t, k) * Math.pow(1 - t, n - k);

            x += basis * controlPoints.get(k).getX();
            y += basis * controlPoints.get(k).getY();
        }

        return new Point2D(x, y);
    }

    private static double binomialCoefficient(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1;

        double result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - k + i) / i;
        }
        return result;
    }
}