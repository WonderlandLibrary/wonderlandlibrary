/*
 * Decompiled with CFR 0.150.
 */
package org.celestial.client.helpers.math;

import java.math.BigDecimal;
import java.math.RoundingMode;
import net.minecraft.util.math.MathHelper;
import org.celestial.client.helpers.Helper;

public class MathematicHelper
implements Helper {
    public static int middleRandomize(int max, int min) {
        return (int)(Math.random() * (double)(max - min)) + min;
    }

    public static int spikeRandomize(int max, int min) {
        return -min + (int)(Math.random() * (double)(max - -min + 1));
    }

    public static double spikeRandomize(double max, double min) {
        return -min + Math.random() * (max - -min + 1.0);
    }

    public static BigDecimal round(float f, int times) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(times, 4);
        return bd;
    }

    public static float abs(float num) {
        return num < 0.0f ? 0.0f - num : num;
    }

    public static float nabs(float num) {
        return MathematicHelper.abs(num) * -1.0f;
    }

    public static double round(double num, double increment) {
        double v = (double)Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static float checkAngle(float oneVar, float twoVar, float threeVar) {
        float f = MathHelper.wrapDegrees(oneVar - twoVar);
        if (f < -threeVar) {
            f = -threeVar;
        }
        if (f >= threeVar) {
            f = threeVar;
        }
        return oneVar - f;
    }

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public static float clamp(float val, float min, float max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }

    public static float randomizeFloat(float startInclusive, float endInclusive) {
        if (startInclusive == endInclusive || endInclusive - startInclusive <= 0.0f) {
            return startInclusive;
        }
        return (float)((double)startInclusive + (double)(endInclusive - startInclusive) * Math.random());
    }
}

