/*
masta codda - exotic
*/
package dev.zprestige.prestige.client.util.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {

    public static float findMiddleValue(float f, float f2, float f3) {
        return f < f2 ? f2 : Math.min(f, f3);
    }

    public static float interpolate(float n, float n2, float n3) {
        return n - (n - n2) * findMiddleValue(n3, 0, 1);
    }

    public static float scaleAndRoundFloat(float n, int newScale) {
        if (newScale >= 0) {
            return BigDecimal.valueOf(n).setScale(newScale, RoundingMode.FLOOR).floatValue();
        }
        return -1;
    }
}
