/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.impl.paint.MultipleGradientContext;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.LinearGradient;

final class LinearGradientContext
extends MultipleGradientContext {
    private float dgdX;
    private float dgdY;
    private float gc;

    LinearGradientContext(LinearGradient linearGradient, BaseTransform baseTransform, float f, float f2, float f3, float f4, float[] arrf, Color[] arrcolor, int n) {
        super(linearGradient, baseTransform, arrf, arrcolor, n);
        float f5 = f3 - f;
        float f6 = f4 - f2;
        float f7 = f5 * f5 + f6 * f6;
        float f8 = f5 / f7;
        float f9 = f6 / f7;
        this.dgdX = this.a00 * f8 + this.a10 * f9;
        this.dgdY = this.a01 * f8 + this.a11 * f9;
        this.gc = (this.a02 - f) * f8 + (this.a12 - f2) * f9;
    }

    @Override
    protected void fillRaster(int[] arrn, int n, int n2, int n3, int n4, int n5, int n6) {
        float f = 0.0f;
        int n7 = n + n5;
        float f2 = this.dgdX * (float)n3 + this.gc;
        for (int i = 0; i < n6; ++i) {
            f = f2 + this.dgdY * (float)(n4 + i);
            while (n < n7) {
                arrn[n++] = this.indexIntoGradientsArrays(f);
                f += this.dgdX;
            }
            n7 = (n += n2) + n5;
        }
    }
}

