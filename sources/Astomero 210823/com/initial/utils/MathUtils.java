package com.initial.utils;

import java.util.*;
import java.math.*;

public class MathUtils
{
    public static double getRandomInRange(final double min, final double max) {
        final Random random = new Random();
        final double range = max - min;
        double scaled = random.nextDouble() * range;
        if (scaled > max) {
            scaled = max;
        }
        double shifted = scaled + min;
        if (shifted > max) {
            shifted = max;
        }
        return shifted;
    }
    
    public static double randomNumber(final double max, final double min) {
        return Math.random() * (max - min) + min;
    }
    
    public static double square(double squareMe) {
        squareMe *= squareMe;
        return squareMe;
    }
    
    public static double round(final double num, final double increment) {
        if (increment < 0.0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale((int)increment, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
