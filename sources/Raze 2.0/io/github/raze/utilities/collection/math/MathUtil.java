package io.github.raze.utilities.collection.math;

import io.github.raze.utilities.system.Methods;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil implements Methods {

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal decimal = new BigDecimal(value);
        decimal = decimal.setScale(places, RoundingMode.HALF_UP);

        return decimal.doubleValue();
    }

    public static double square(double squareX) {
        squareX *= squareX;
        return squareX;
    }

    public static double clamp(double value, double minimum, double maximum) {
        return value < minimum ? minimum : (Math.min(value, maximum));
    }

    public static float clamp(float value, float minimum, float maximum) {
        return value < minimum ? minimum : (Math.min(value, maximum));
    }

    public static int clamp(int value, int minimum, int maximum) {
        return value < minimum ? minimum : (Math.min(value, maximum));
    }

    public static double interpolate(final double newPos, final double oldPos) {
        return oldPos + (newPos - oldPos) * mc.timer.renderPartialTicks;
    }

}