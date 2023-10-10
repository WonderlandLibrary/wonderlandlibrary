/*
 * Decompiled with CFR 0.152.
 */
package org.apache.commons.lang3.math;

import org.apache.commons.lang3.Validate;

public class IEEE754rUtils {
    public static double min(double ... array) {
        Validate.isTrue(array != null, "The Array must not be null", new Object[0]);
        Validate.isTrue(array.length != 0, "Array cannot be empty.", new Object[0]);
        double min = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            min = IEEE754rUtils.min(array[i2], min);
        }
        return min;
    }

    public static float min(float ... array) {
        Validate.isTrue(array != null, "The Array must not be null", new Object[0]);
        Validate.isTrue(array.length != 0, "Array cannot be empty.", new Object[0]);
        float min = array[0];
        for (int i2 = 1; i2 < array.length; ++i2) {
            min = IEEE754rUtils.min(array[i2], min);
        }
        return min;
    }

    public static double min(double a2, double b2, double c2) {
        return IEEE754rUtils.min(IEEE754rUtils.min(a2, b2), c2);
    }

    public static double min(double a2, double b2) {
        if (Double.isNaN(a2)) {
            return b2;
        }
        if (Double.isNaN(b2)) {
            return a2;
        }
        return Math.min(a2, b2);
    }

    public static float min(float a2, float b2, float c2) {
        return IEEE754rUtils.min(IEEE754rUtils.min(a2, b2), c2);
    }

    public static float min(float a2, float b2) {
        if (Float.isNaN(a2)) {
            return b2;
        }
        if (Float.isNaN(b2)) {
            return a2;
        }
        return Math.min(a2, b2);
    }

    public static double max(double ... array) {
        Validate.isTrue(array != null, "The Array must not be null", new Object[0]);
        Validate.isTrue(array.length != 0, "Array cannot be empty.", new Object[0]);
        double max = array[0];
        for (int j2 = 1; j2 < array.length; ++j2) {
            max = IEEE754rUtils.max(array[j2], max);
        }
        return max;
    }

    public static float max(float ... array) {
        Validate.isTrue(array != null, "The Array must not be null", new Object[0]);
        Validate.isTrue(array.length != 0, "Array cannot be empty.", new Object[0]);
        float max = array[0];
        for (int j2 = 1; j2 < array.length; ++j2) {
            max = IEEE754rUtils.max(array[j2], max);
        }
        return max;
    }

    public static double max(double a2, double b2, double c2) {
        return IEEE754rUtils.max(IEEE754rUtils.max(a2, b2), c2);
    }

    public static double max(double a2, double b2) {
        if (Double.isNaN(a2)) {
            return b2;
        }
        if (Double.isNaN(b2)) {
            return a2;
        }
        return Math.max(a2, b2);
    }

    public static float max(float a2, float b2, float c2) {
        return IEEE754rUtils.max(IEEE754rUtils.max(a2, b2), c2);
    }

    public static float max(float a2, float b2) {
        if (Float.isNaN(a2)) {
            return b2;
        }
        if (Float.isNaN(b2)) {
            return a2;
        }
        return Math.max(a2, b2);
    }
}

