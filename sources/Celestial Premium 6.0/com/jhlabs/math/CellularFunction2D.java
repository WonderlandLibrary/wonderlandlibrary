/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.math;

import com.jhlabs.math.Function2D;
import java.util.Random;

public class CellularFunction2D
implements Function2D {
    public float distancePower = 2.0f;
    public boolean cells = false;
    public boolean angular = false;
    private float[] coefficients = new float[]{1.0f, 0.0f, 0.0f, 0.0f};
    private Random random = new Random();
    private Point[] results = new Point[2];

    public CellularFunction2D() {
        for (int j = 0; j < this.results.length; ++j) {
            this.results[j] = new Point();
        }
    }

    public void setCoefficient(int c, float v) {
        this.coefficients[c] = v;
    }

    public float getCoefficient(int c) {
        return this.coefficients[c];
    }

    private float checkCube(float x, float y, int cubeX, int cubeY, Point[] results) {
        this.random.setSeed(571 * cubeX + 23 * cubeY);
        int numPoints = 3 + this.random.nextInt() % 4;
        numPoints = 4;
        block0: for (int i = 0; i < numPoints; ++i) {
            float px = this.random.nextFloat();
            float py = this.random.nextFloat();
            float dx = Math.abs(x - px);
            float dy = Math.abs(y - py);
            float d = this.distancePower == 1.0f ? dx + dy : (this.distancePower == 2.0f ? (float)Math.sqrt(dx * dx + dy * dy) : (float)Math.pow(Math.pow(dx, this.distancePower) + Math.pow(dy, this.distancePower), 1.0f / this.distancePower));
            for (int j = 0; j < results.length; ++j) {
                Point last;
                if ((double)results[j].distance == Double.POSITIVE_INFINITY) {
                    last = results[j];
                    last.distance = d;
                    last.x = px;
                    last.y = py;
                    results[j] = last;
                    continue block0;
                }
                if (!(d < results[j].distance)) continue;
                last = results[results.length - 1];
                for (int k = results.length - 1; k > j; --k) {
                    results[k] = results[k - 1];
                }
                last.distance = d;
                last.x = px;
                last.y = py;
                results[j] = last;
                continue block0;
            }
        }
        return results[1].distance;
    }

    @Override
    public float evaluate(float x, float y) {
        for (int j = 0; j < this.results.length; ++j) {
            this.results[j].distance = Float.POSITIVE_INFINITY;
        }
        int ix = (int)x;
        float fx = x - (float)ix;
        int iy = (int)y;
        float fy = y - (float)iy;
        float d = this.checkCube(fx, fy, ix, iy, this.results);
        if (d > fy) {
            d = this.checkCube(fx, fy + 1.0f, ix, iy - 1, this.results);
        }
        if (d > 1.0f - fy) {
            d = this.checkCube(fx, fy - 1.0f, ix, iy + 1, this.results);
        }
        if (d > fx) {
            this.checkCube(fx + 1.0f, fy, ix - 1, iy, this.results);
            if (d > fy) {
                d = this.checkCube(fx + 1.0f, fy + 1.0f, ix - 1, iy - 1, this.results);
            }
            if (d > 1.0f - fy) {
                d = this.checkCube(fx + 1.0f, fy - 1.0f, ix - 1, iy + 1, this.results);
            }
        }
        if (d > 1.0f - fx) {
            d = this.checkCube(fx - 1.0f, fy, ix + 1, iy, this.results);
            if (d > fy) {
                d = this.checkCube(fx - 1.0f, fy + 1.0f, ix + 1, iy - 1, this.results);
            }
            if (d > 1.0f - fy) {
                d = this.checkCube(fx - 1.0f, fy - 1.0f, ix + 1, iy + 1, this.results);
            }
        }
        float t = 0.0f;
        for (int i = 0; i < 2; ++i) {
            t += this.coefficients[i] * this.results[i].distance;
        }
        if (this.angular) {
            t = (float)((double)t + (Math.atan2(fy - this.results[0].y, fx - this.results[0].x) / (Math.PI * 2) + 0.5));
        }
        return t;
    }

    class Point {
        int index;
        float x;
        float y;
        float distance;

        Point() {
        }
    }
}

