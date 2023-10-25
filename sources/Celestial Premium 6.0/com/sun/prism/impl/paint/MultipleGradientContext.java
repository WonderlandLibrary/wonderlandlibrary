/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;

abstract class MultipleGradientContext {
    protected int cycleMethod;
    protected float a00;
    protected float a01;
    protected float a10;
    protected float a11;
    protected float a02;
    protected float a12;
    protected boolean isSimpleLookup;
    protected int fastGradientArraySize;
    protected int[] gradient;
    private int[][] gradients;
    private float[] normalizedIntervals;
    private float[] fractions;
    private int transparencyTest;
    protected static final int GRADIENT_SIZE = 256;
    protected static final int GRADIENT_SIZE_INDEX = 255;
    private static final int MAX_GRADIENT_ARRAY_SIZE = 5000;

    protected MultipleGradientContext(Gradient gradient, BaseTransform baseTransform, float[] arrf, Color[] arrcolor, int n) {
        BaseTransform baseTransform2;
        if (baseTransform == null) {
            throw new NullPointerException("Transform cannot be null");
        }
        try {
            baseTransform2 = baseTransform.createInverse();
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            baseTransform2 = BaseTransform.IDENTITY_TRANSFORM;
        }
        this.a00 = (float)baseTransform2.getMxx();
        this.a10 = (float)baseTransform2.getMyx();
        this.a01 = (float)baseTransform2.getMxy();
        this.a11 = (float)baseTransform2.getMyy();
        this.a02 = (float)baseTransform2.getMxt();
        this.a12 = (float)baseTransform2.getMyt();
        this.cycleMethod = n;
        this.fractions = arrf;
        this.calculateLookupData(arrcolor);
    }

    private void calculateLookupData(Color[] arrcolor) {
        Color[] arrcolor2 = arrcolor;
        this.normalizedIntervals = new float[this.fractions.length - 1];
        for (int i = 0; i < this.normalizedIntervals.length; ++i) {
            this.normalizedIntervals[i] = this.fractions[i + 1] - this.fractions[i];
        }
        this.transparencyTest = -16777216;
        this.gradients = new int[this.normalizedIntervals.length][];
        float f = 1.0f;
        for (int i = 0; i < this.normalizedIntervals.length; ++i) {
            f = f > this.normalizedIntervals[i] ? this.normalizedIntervals[i] : f;
        }
        float f2 = 0.0f;
        for (int i = 0; i < this.normalizedIntervals.length && Float.isFinite(f2); ++i) {
            f2 += this.normalizedIntervals[i] / f * 256.0f;
        }
        if (f2 <= 5000.0f) {
            this.calculateSingleArrayGradient(arrcolor2, f);
        } else {
            this.calculateMultipleArrayGradient(arrcolor2);
        }
    }

    private void calculateSingleArrayGradient(Color[] arrcolor, float f) {
        int n;
        int n2;
        this.isSimpleLookup = true;
        int n3 = 1;
        for (n2 = 0; n2 < this.gradients.length; ++n2) {
            n = (int)(this.normalizedIntervals[n2] / f * 255.0f);
            n3 += n;
            this.gradients[n2] = new int[n];
            int n4 = arrcolor[n2].getIntArgbPre();
            int n5 = arrcolor[n2 + 1].getIntArgbPre();
            this.interpolate(n4, n5, this.gradients[n2]);
            this.transparencyTest &= n4;
            this.transparencyTest &= n5;
        }
        this.gradient = new int[n3];
        n2 = 0;
        for (n = 0; n < this.gradients.length; ++n) {
            System.arraycopy(this.gradients[n], 0, this.gradient, n2, this.gradients[n].length);
            n2 += this.gradients[n].length;
        }
        this.gradient[this.gradient.length - 1] = arrcolor[arrcolor.length - 1].getIntArgbPre();
        this.fastGradientArraySize = this.gradient.length - 1;
    }

    private void calculateMultipleArrayGradient(Color[] arrcolor) {
        this.isSimpleLookup = false;
        for (int i = 0; i < this.gradients.length; ++i) {
            this.gradients[i] = new int[256];
            int n = arrcolor[i].getIntArgbPre();
            int n2 = arrcolor[i + 1].getIntArgbPre();
            this.interpolate(n, n2, this.gradients[i]);
            this.transparencyTest &= n;
            this.transparencyTest &= n2;
        }
    }

    private void interpolate(int n, int n2, int[] arrn) {
        float f = 1.0f / (float)arrn.length;
        int n3 = n >> 24 & 0xFF;
        int n4 = n >> 16 & 0xFF;
        int n5 = n >> 8 & 0xFF;
        int n6 = n & 0xFF;
        int n7 = (n2 >> 24 & 0xFF) - n3;
        int n8 = (n2 >> 16 & 0xFF) - n4;
        int n9 = (n2 >> 8 & 0xFF) - n5;
        int n10 = (n2 & 0xFF) - n6;
        for (int i = 0; i < arrn.length; ++i) {
            arrn[i] = (int)((double)((float)n3 + (float)(i * n7) * f) + 0.5) << 24 | (int)((double)((float)n4 + (float)(i * n8) * f) + 0.5) << 16 | (int)((double)((float)n5 + (float)(i * n9) * f) + 0.5) << 8 | (int)((double)((float)n6 + (float)(i * n10) * f) + 0.5);
        }
    }

    protected final int indexIntoGradientsArrays(float f) {
        int n;
        if (this.cycleMethod == 0) {
            if (f > 1.0f) {
                f = 1.0f;
            } else if (f < 0.0f) {
                f = 0.0f;
            }
        } else if (this.cycleMethod == 2) {
            if ((f -= (float)((int)f)) < 0.0f) {
                f += 1.0f;
            }
        } else {
            if (f < 0.0f) {
                f = -f;
            }
            n = (int)f;
            f -= (float)n;
            if ((n & 1) == 1) {
                f = 1.0f - f;
            }
        }
        if (this.isSimpleLookup) {
            return this.gradient[(int)(f * (float)this.fastGradientArraySize)];
        }
        if (f < this.fractions[0]) {
            return this.gradients[0][0];
        }
        for (n = 0; n < this.gradients.length; ++n) {
            if (!(f < this.fractions[n + 1])) continue;
            float f2 = f - this.fractions[n];
            int n2 = (int)(f2 / this.normalizedIntervals[n] * 255.0f);
            return this.gradients[n][n2];
        }
        return this.gradients[this.gradients.length - 1][255];
    }

    protected abstract void fillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7);
}

