/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.impl.paint.MultipleGradientContext;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.RadialGradient;

final class RadialGradientContext
extends MultipleGradientContext {
    private boolean isSimpleFocus = false;
    private boolean isNonCyclic = false;
    private float radius;
    private float centerX;
    private float centerY;
    private float focusX;
    private float focusY;
    private float radiusSq;
    private float constA;
    private float constB;
    private float gDeltaDelta;
    private float trivial;
    private static final float SCALEBACK = 0.99f;
    private static final int SQRT_LUT_SIZE = 2048;
    private static float[] sqrtLut = new float[2049];

    RadialGradientContext(RadialGradient radialGradient, BaseTransform baseTransform, float f, float f2, float f3, float f4, float f5, float[] arrf, Color[] arrcolor, int n) {
        super(radialGradient, baseTransform, arrf, arrcolor, n);
        this.centerX = f;
        this.centerY = f2;
        this.focusX = f4;
        this.focusY = f5;
        this.radius = f3;
        this.isSimpleFocus = this.focusX == this.centerX && this.focusY == this.centerY;
        this.isNonCyclic = n == 0;
        this.radiusSq = this.radius * this.radius;
        float f6 = this.focusX - this.centerX;
        float f7 = this.focusY - this.centerY;
        double d = f6 * f6 + f7 * f7;
        if (d > (double)(this.radiusSq * 0.99f)) {
            float f8 = (float)Math.sqrt((double)(this.radiusSq * 0.99f) / d);
            this.focusX = this.centerX + (f6 *= f8);
            this.focusY = this.centerY + (f7 *= f8);
        }
        this.trivial = (float)Math.sqrt(this.radiusSq - f6 * f6);
        this.constA = this.a02 - this.centerX;
        this.constB = this.a12 - this.centerY;
        this.gDeltaDelta = 2.0f * (this.a00 * this.a00 + this.a10 * this.a10) / this.radiusSq;
    }

    @Override
    protected void fillRaster(int[] arrn, int n, int n2, int n3, int n4, int n5, int n6) {
        if (this.isSimpleFocus && this.isNonCyclic && this.isSimpleLookup) {
            this.simpleNonCyclicFillRaster(arrn, n, n2, n3, n4, n5, n6);
        } else {
            this.cyclicCircularGradientFillRaster(arrn, n, n2, n3, n4, n5, n6);
        }
    }

    private void simpleNonCyclicFillRaster(int[] arrn, int n, int n2, int n3, int n4, int n5, int n6) {
        float f = this.a00 * (float)n3 + this.a01 * (float)n4 + this.constA;
        float f2 = this.a10 * (float)n3 + this.a11 * (float)n4 + this.constB;
        float f3 = this.gDeltaDelta;
        n2 += n5;
        int n7 = this.gradient[this.fastGradientArraySize];
        for (int i = 0; i < n6; ++i) {
            int n8;
            float f4 = (f * f + f2 * f2) / this.radiusSq;
            float f5 = 2.0f * (this.a00 * f + this.a10 * f2) / this.radiusSq + f3 / 2.0f;
            for (n8 = 0; n8 < n5 && f4 >= 1.0f; ++n8) {
                arrn[n + n8] = n7;
                f4 += f5;
                f5 += f3;
            }
            while (n8 < n5 && f4 < 1.0f) {
                int n9;
                if (f4 <= 0.0f) {
                    n9 = 0;
                } else {
                    float f6 = f4 * 2048.0f;
                    int n10 = (int)f6;
                    float f7 = sqrtLut[n10];
                    float f8 = sqrtLut[n10 + 1] - f7;
                    f6 = f7 + (f6 - (float)n10) * f8;
                    n9 = (int)(f6 * (float)this.fastGradientArraySize);
                }
                arrn[n + n8] = this.gradient[n9];
                f4 += f5;
                f5 += f3;
                ++n8;
            }
            while (n8 < n5) {
                arrn[n + n8] = n7;
                ++n8;
            }
            n += n2;
            f += this.a01;
            f2 += this.a11;
        }
    }

    private void cyclicCircularGradientFillRaster(int[] arrn, int n, int n2, int n3, int n4, int n5, int n6) {
        double d = -this.radiusSq + this.centerX * this.centerX + this.centerY * this.centerY;
        float f = this.a00 * (float)n3 + this.a01 * (float)n4 + this.a02;
        float f2 = this.a10 * (float)n3 + this.a11 * (float)n4 + this.a12;
        float f3 = 2.0f * this.centerY;
        float f4 = -2.0f * this.centerX;
        int n7 = n;
        int n8 = n5 + n2;
        for (int i = 0; i < n6; ++i) {
            float f5 = this.a01 * (float)i + f;
            float f6 = this.a11 * (float)i + f2;
            for (int j = 0; j < n5; ++j) {
                double d2;
                double d3;
                if (f5 == this.focusX) {
                    d3 = this.focusX;
                    d2 = this.centerY;
                    d2 += f6 > this.focusY ? (double)this.trivial : (double)(-this.trivial);
                } else {
                    double d4 = (f6 - this.focusY) / (f5 - this.focusX);
                    double d5 = (double)f6 - d4 * (double)f5;
                    double d6 = d4 * d4 + 1.0;
                    double d7 = (double)f4 + -2.0 * d4 * ((double)this.centerY - d5);
                    double d8 = d + d5 * (d5 - (double)f3);
                    float f7 = (float)Math.sqrt(d7 * d7 - 4.0 * d6 * d8);
                    d3 = -d7;
                    d3 += f5 < this.focusX ? (double)(-f7) : (double)f7;
                    d2 = d4 * (d3 /= 2.0 * d6) + d5;
                }
                float f8 = f5 - this.focusX;
                f8 *= f8;
                float f9 = f6 - this.focusY;
                f9 *= f9;
                float f10 = f8 + f9;
                f8 = (float)d3 - this.focusX;
                f8 *= f8;
                f9 = (float)d2 - this.focusY;
                f9 *= f9;
                float f11 = f8 + f9;
                float f12 = (float)Math.sqrt(f10 / f11);
                arrn[n7 + j] = this.indexIntoGradientsArrays(f12);
                f5 += this.a00;
                f6 += this.a10;
            }
            n7 += n8;
        }
    }

    static {
        for (int i = 0; i < sqrtLut.length; ++i) {
            RadialGradientContext.sqrtLut[i] = (float)Math.sqrt((float)i / 2048.0f);
        }
    }
}

