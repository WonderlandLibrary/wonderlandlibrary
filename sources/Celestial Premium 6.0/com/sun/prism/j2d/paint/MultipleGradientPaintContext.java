/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d.paint;

import com.sun.prism.j2d.paint.MultipleGradientPaint;
import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.SinglePixelPackedSampleModel;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

abstract class MultipleGradientPaintContext
implements PaintContext {
    protected ColorModel model;
    private static ColorModel xrgbmodel = new DirectColorModel(24, 0xFF0000, 65280, 255);
    protected static ColorModel cachedModel;
    protected static WeakReference<Raster> cached;
    protected Raster saved;
    protected MultipleGradientPaint.CycleMethod cycleMethod;
    protected MultipleGradientPaint.ColorSpaceType colorSpace;
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
    private static final int[] SRGBtoLinearRGB;
    private static final int[] LinearRGBtoSRGB;
    protected static final int GRADIENT_SIZE = 256;
    protected static final int GRADIENT_SIZE_INDEX = 255;
    private static final int MAX_GRADIENT_ARRAY_SIZE = 5000;

    protected MultipleGradientPaintContext(MultipleGradientPaint multipleGradientPaint, ColorModel colorModel, Rectangle rectangle, Rectangle2D rectangle2D, AffineTransform affineTransform, RenderingHints renderingHints, float[] arrf, Color[] arrcolor, MultipleGradientPaint.CycleMethod cycleMethod, MultipleGradientPaint.ColorSpaceType colorSpaceType) {
        AffineTransform affineTransform2;
        if (rectangle == null) {
            throw new NullPointerException("Device bounds cannot be null");
        }
        if (rectangle2D == null) {
            throw new NullPointerException("User bounds cannot be null");
        }
        if (affineTransform == null) {
            throw new NullPointerException("Transform cannot be null");
        }
        try {
            affineTransform2 = affineTransform.createInverse();
        }
        catch (NoninvertibleTransformException noninvertibleTransformException) {
            affineTransform2 = new AffineTransform();
        }
        double[] arrd = new double[6];
        affineTransform2.getMatrix(arrd);
        this.a00 = (float)arrd[0];
        this.a10 = (float)arrd[1];
        this.a01 = (float)arrd[2];
        this.a11 = (float)arrd[3];
        this.a02 = (float)arrd[4];
        this.a12 = (float)arrd[5];
        this.cycleMethod = cycleMethod;
        this.colorSpace = colorSpaceType;
        this.fractions = arrf;
        this.gradient = multipleGradientPaint.gradient != null ? multipleGradientPaint.gradient.get() : null;
        int[][] arrn = this.gradients = multipleGradientPaint.gradients != null ? multipleGradientPaint.gradients.get() : null;
        if (this.gradient == null && this.gradients == null) {
            this.calculateLookupData(arrcolor);
            multipleGradientPaint.model = this.model;
            multipleGradientPaint.normalizedIntervals = this.normalizedIntervals;
            multipleGradientPaint.isSimpleLookup = this.isSimpleLookup;
            if (this.isSimpleLookup) {
                multipleGradientPaint.fastGradientArraySize = this.fastGradientArraySize;
                multipleGradientPaint.gradient = new SoftReference<int[]>(this.gradient);
            } else {
                multipleGradientPaint.gradients = new SoftReference<int[][]>(this.gradients);
            }
        } else {
            this.model = multipleGradientPaint.model;
            this.normalizedIntervals = multipleGradientPaint.normalizedIntervals;
            this.isSimpleLookup = multipleGradientPaint.isSimpleLookup;
            this.fastGradientArraySize = multipleGradientPaint.fastGradientArraySize;
        }
    }

    private void calculateLookupData(Color[] arrcolor) {
        int n;
        int n2;
        int n3;
        Color[] arrcolor2;
        if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
            arrcolor2 = new Color[arrcolor.length];
            for (n3 = 0; n3 < arrcolor.length; ++n3) {
                n2 = arrcolor[n3].getRGB();
                n = n2 >>> 24;
                int n4 = SRGBtoLinearRGB[n2 >> 16 & 0xFF];
                int n5 = SRGBtoLinearRGB[n2 >> 8 & 0xFF];
                int n6 = SRGBtoLinearRGB[n2 & 0xFF];
                arrcolor2[n3] = new Color(n4, n5, n6, n);
            }
        } else {
            arrcolor2 = arrcolor;
        }
        this.normalizedIntervals = new float[this.fractions.length - 1];
        for (n3 = 0; n3 < this.normalizedIntervals.length; ++n3) {
            this.normalizedIntervals[n3] = this.fractions[n3 + 1] - this.fractions[n3];
        }
        this.transparencyTest = -16777216;
        this.gradients = new int[this.normalizedIntervals.length][];
        float f = 1.0f;
        for (n2 = 0; n2 < this.normalizedIntervals.length; ++n2) {
            f = f > this.normalizedIntervals[n2] ? this.normalizedIntervals[n2] : f;
        }
        n2 = 0;
        for (n = 0; n < this.normalizedIntervals.length; ++n) {
            n2 = (int)((float)n2 + this.normalizedIntervals[n] / f * 256.0f);
        }
        if (n2 > 5000) {
            this.calculateMultipleArrayGradient(arrcolor2);
        } else {
            this.calculateSingleArrayGradient(arrcolor2, f);
        }
        this.model = this.transparencyTest >>> 24 == 255 ? xrgbmodel : ColorModel.getRGBdefault();
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
            int n4 = arrcolor[n2].getRGB();
            int n5 = arrcolor[n2 + 1].getRGB();
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
        this.gradient[this.gradient.length - 1] = arrcolor[arrcolor.length - 1].getRGB();
        if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
            for (n = 0; n < this.gradient.length; ++n) {
                this.gradient[n] = this.convertEntireColorLinearRGBtoSRGB(this.gradient[n]);
            }
        }
        this.fastGradientArraySize = this.gradient.length - 1;
    }

    private void calculateMultipleArrayGradient(Color[] arrcolor) {
        int n;
        this.isSimpleLookup = false;
        for (n = 0; n < this.gradients.length; ++n) {
            this.gradients[n] = new int[256];
            int n2 = arrcolor[n].getRGB();
            int n3 = arrcolor[n + 1].getRGB();
            this.interpolate(n2, n3, this.gradients[n]);
            this.transparencyTest &= n2;
            this.transparencyTest &= n3;
        }
        if (this.colorSpace == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
            for (n = 0; n < this.gradients.length; ++n) {
                for (int i = 0; i < this.gradients[n].length; ++i) {
                    this.gradients[n][i] = this.convertEntireColorLinearRGBtoSRGB(this.gradients[n][i]);
                }
            }
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

    private int convertEntireColorLinearRGBtoSRGB(int n) {
        int n2 = n >> 24 & 0xFF;
        int n3 = n >> 16 & 0xFF;
        int n4 = n >> 8 & 0xFF;
        int n5 = n & 0xFF;
        n3 = LinearRGBtoSRGB[n3];
        n4 = LinearRGBtoSRGB[n4];
        n5 = LinearRGBtoSRGB[n5];
        return n2 << 24 | n3 << 16 | n4 << 8 | n5;
    }

    protected final int indexIntoGradientsArrays(float f) {
        int n;
        if (this.cycleMethod == MultipleGradientPaint.CycleMethod.NO_CYCLE) {
            if (f > 1.0f) {
                f = 1.0f;
            } else if (f < 0.0f) {
                f = 0.0f;
            }
        } else if (this.cycleMethod == MultipleGradientPaint.CycleMethod.REPEAT) {
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
        for (n = 0; n < this.gradients.length; ++n) {
            if (!(f < this.fractions[n + 1])) continue;
            float f2 = f - this.fractions[n];
            int n2 = (int)(f2 / this.normalizedIntervals[n] * 255.0f);
            return this.gradients[n][n2];
        }
        return this.gradients[this.gradients.length - 1][255];
    }

    private static int convertSRGBtoLinearRGB(int n) {
        float f = (float)n / 255.0f;
        float f2 = f <= 0.04045f ? f / 12.92f : (float)Math.pow(((double)f + 0.055) / 1.055, 2.4);
        return Math.round(f2 * 255.0f);
    }

    private static int convertLinearRGBtoSRGB(int n) {
        float f = (float)n / 255.0f;
        float f2 = (double)f <= 0.0031308 ? f * 12.92f : 1.055f * (float)Math.pow(f, 0.4166666666666667) - 0.055f;
        return Math.round(f2 * 255.0f);
    }

    @Override
    public final Raster getRaster(int n, int n2, int n3, int n4) {
        Raster raster = this.saved;
        if (raster == null || raster.getWidth() < n3 || raster.getHeight() < n4) {
            this.saved = raster = MultipleGradientPaintContext.getCachedRaster(this.model, n3, n4);
        }
        DataBufferInt dataBufferInt = (DataBufferInt)raster.getDataBuffer();
        int[] arrn = dataBufferInt.getData(0);
        int n5 = dataBufferInt.getOffset();
        int n6 = ((SinglePixelPackedSampleModel)raster.getSampleModel()).getScanlineStride();
        int n7 = n6 - n3;
        this.fillRaster(arrn, n5, n7, n, n2, n3, n4);
        return raster;
    }

    protected abstract void fillRaster(int[] var1, int var2, int var3, int var4, int var5, int var6, int var7);

    private static synchronized Raster getCachedRaster(ColorModel colorModel, int n, int n2) {
        Raster raster;
        if (colorModel == cachedModel && cached != null && (raster = (Raster)cached.get()) != null && raster.getWidth() >= n && raster.getHeight() >= n2) {
            cached = null;
            return raster;
        }
        return colorModel.createCompatibleWritableRaster(n, n2);
    }

    private static synchronized void putCachedRaster(ColorModel colorModel, Raster raster) {
        Raster raster2;
        if (cached != null && (raster2 = (Raster)cached.get()) != null) {
            int n = raster2.getWidth();
            int n2 = raster2.getHeight();
            int n3 = raster.getWidth();
            int n4 = raster.getHeight();
            if (n >= n3 && n2 >= n4) {
                return;
            }
            if (n * n2 >= n3 * n4) {
                return;
            }
        }
        cachedModel = colorModel;
        cached = new WeakReference<Raster>(raster);
    }

    @Override
    public final void dispose() {
        if (this.saved != null) {
            MultipleGradientPaintContext.putCachedRaster(this.model, this.saved);
            this.saved = null;
        }
    }

    @Override
    public final ColorModel getColorModel() {
        return this.model;
    }

    static {
        SRGBtoLinearRGB = new int[256];
        LinearRGBtoSRGB = new int[256];
        for (int i = 0; i < 256; ++i) {
            MultipleGradientPaintContext.SRGBtoLinearRGB[i] = MultipleGradientPaintContext.convertSRGBtoLinearRGB(i);
            MultipleGradientPaintContext.LinearRGBtoSRGB[i] = MultipleGradientPaintContext.convertLinearRGBtoSRGB(i);
        }
    }
}

