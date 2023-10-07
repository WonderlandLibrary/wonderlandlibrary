/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class MathUtils {
    public static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static int randomize(int max, int min) {
        return - min + (int)(Math.random() * (double)(max - (- min) + 1));
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0 / inc;
        return (double)Math.round(val * one) / one;
    }

    public static boolean isInteger(Double variable) {
        if (variable == Math.floor(variable) && !Double.isInfinite(variable)) {
            return true;
        }
        return false;
    }

    public static double square(double in)
    {
      return in * in;
    }
  }

