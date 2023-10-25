/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.animation.Interpolator
 */
package com.sun.scenario.animation;

import javafx.animation.Interpolator;

public class SplineInterpolator
extends Interpolator {
    private final double x1;
    private final double y1;
    private final double x2;
    private final double y2;
    private final boolean isCurveLinear;
    private static final int SAMPLE_SIZE = 16;
    private static final double SAMPLE_INCREMENT = 0.0625;
    private final double[] xSamples = new double[17];

    public SplineInterpolator(double d, double d2, double d3, double d4) {
        if (d < 0.0 || d > 1.0 || d2 < 0.0 || d2 > 1.0 || d3 < 0.0 || d3 > 1.0 || d4 < 0.0 || d4 > 1.0) {
            throw new IllegalArgumentException("Control point coordinates must all be in range [0,1]");
        }
        this.x1 = d;
        this.y1 = d2;
        this.x2 = d3;
        this.y2 = d4;
        boolean bl = this.isCurveLinear = this.x1 == this.y1 && this.x2 == this.y2;
        if (!this.isCurveLinear) {
            for (int i = 0; i < 17; ++i) {
                this.xSamples[i] = this.eval((double)i * 0.0625, this.x1, this.x2);
            }
        }
    }

    public double getX1() {
        return this.x1;
    }

    public double getY1() {
        return this.y1;
    }

    public double getX2() {
        return this.x2;
    }

    public double getY2() {
        return this.y2;
    }

    public int hashCode() {
        int n = 7;
        n = 19 * n + (int)(Double.doubleToLongBits(this.x1) ^ Double.doubleToLongBits(this.x1) >>> 32);
        n = 19 * n + (int)(Double.doubleToLongBits(this.y1) ^ Double.doubleToLongBits(this.y1) >>> 32);
        n = 19 * n + (int)(Double.doubleToLongBits(this.x2) ^ Double.doubleToLongBits(this.x2) >>> 32);
        n = 19 * n + (int)(Double.doubleToLongBits(this.y2) ^ Double.doubleToLongBits(this.y2) >>> 32);
        return n;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (((Object)((Object)this)).getClass() != object.getClass()) {
            return false;
        }
        SplineInterpolator splineInterpolator = (SplineInterpolator)((Object)object);
        if (Double.doubleToLongBits(this.x1) != Double.doubleToLongBits(splineInterpolator.x1)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y1) != Double.doubleToLongBits(splineInterpolator.y1)) {
            return false;
        }
        if (Double.doubleToLongBits(this.x2) != Double.doubleToLongBits(splineInterpolator.x2)) {
            return false;
        }
        return Double.doubleToLongBits(this.y2) == Double.doubleToLongBits(splineInterpolator.y2);
    }

    public double curve(double d) {
        if (d < 0.0 || d > 1.0) {
            throw new IllegalArgumentException("x must be in range [0,1]");
        }
        if (this.isCurveLinear || d == 0.0 || d == 1.0) {
            return d;
        }
        return this.eval(this.findTForX(d), this.y1, this.y2);
    }

    private double eval(double d, double d2, double d3) {
        double d4 = 1.0 - d;
        return d * (3.0 * d4 * (d4 * d2 + d * d3) + d * d);
    }

    private double evalDerivative(double d, double d2, double d3) {
        double d4 = 1.0 - d;
        return 3.0 * (d4 * (d4 * d2 + 2.0 * d * (d3 - d2)) + d * d * (1.0 - d3));
    }

    private double getInitialGuessForT(double d) {
        for (int i = 1; i < 17; ++i) {
            if (!(this.xSamples[i] >= d)) continue;
            double d2 = this.xSamples[i] - this.xSamples[i - 1];
            if (d2 == 0.0) {
                return (double)(i - 1) * 0.0625;
            }
            return ((double)(i - 1) + (d - this.xSamples[i - 1]) / d2) * 0.0625;
        }
        return 1.0;
    }

    private double findTForX(double d) {
        double d2;
        double d3;
        double d4 = this.getInitialGuessForT(d);
        for (int i = 0; i < 4 && (d3 = this.eval(d4, this.x1, this.x2) - d) != 0.0 && (d2 = this.evalDerivative(d4, this.x1, this.x2)) != 0.0; ++i) {
            d4 -= d3 / d2;
        }
        return d4;
    }

    public String toString() {
        return "SplineInterpolator [x1=" + this.x1 + ", y1=" + this.y1 + ", x2=" + this.x2 + ", y2=" + this.y2 + "]";
    }
}

