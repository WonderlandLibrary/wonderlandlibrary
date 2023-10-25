/*
 * Decompiled with CFR 0.150.
 */
package com.sun.prism.j2d.paint;

import java.awt.Color;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.lang.ref.SoftReference;

public abstract class MultipleGradientPaint
implements Paint {
    final int transparency;
    final float[] fractions;
    final Color[] colors;
    final AffineTransform gradientTransform;
    final CycleMethod cycleMethod;
    final ColorSpaceType colorSpace;
    ColorModel model;
    float[] normalizedIntervals;
    boolean isSimpleLookup;
    SoftReference<int[][]> gradients;
    SoftReference<int[]> gradient;
    int fastGradientArraySize;

    MultipleGradientPaint(float[] arrf, Color[] arrcolor, CycleMethod cycleMethod, ColorSpaceType colorSpaceType, AffineTransform affineTransform) {
        if (arrf == null) {
            throw new NullPointerException("Fractions array cannot be null");
        }
        if (arrcolor == null) {
            throw new NullPointerException("Colors array cannot be null");
        }
        if (cycleMethod == null) {
            throw new NullPointerException("Cycle method cannot be null");
        }
        if (colorSpaceType == null) {
            throw new NullPointerException("Color space cannot be null");
        }
        if (affineTransform == null) {
            throw new NullPointerException("Gradient transform cannot be null");
        }
        if (arrf.length != arrcolor.length) {
            throw new IllegalArgumentException("Colors and fractions must have equal size");
        }
        if (arrcolor.length < 2) {
            throw new IllegalArgumentException("User must specify at least 2 colors");
        }
        float f = -1.0f;
        for (float f2 : arrf) {
            if (f2 < 0.0f || f2 > 1.0f) {
                throw new IllegalArgumentException("Fraction values must be in the range 0 to 1: " + f2);
            }
            if (f2 <= f) {
                throw new IllegalArgumentException("Keyframe fractions must be increasing: " + f2);
            }
            f = f2;
        }
        boolean bl = false;
        int n = 0;
        int n2 = arrf.length;
        int n3 = 0;
        if (arrf[0] != 0.0f) {
            bl = true;
            ++n2;
            ++n3;
        }
        if (arrf[arrf.length - 1] != 1.0f) {
            n = 1;
            ++n2;
        }
        this.fractions = new float[n2];
        System.arraycopy(arrf, 0, this.fractions, n3, arrf.length);
        this.colors = new Color[n2];
        System.arraycopy(arrcolor, 0, this.colors, n3, arrcolor.length);
        if (bl) {
            this.fractions[0] = 0.0f;
            this.colors[0] = arrcolor[0];
        }
        if (n != 0) {
            this.fractions[n2 - 1] = 1.0f;
            this.colors[n2 - 1] = arrcolor[arrcolor.length - 1];
        }
        this.colorSpace = colorSpaceType;
        this.cycleMethod = cycleMethod;
        this.gradientTransform = new AffineTransform(affineTransform);
        boolean bl2 = true;
        for (int i = 0; i < arrcolor.length; ++i) {
            bl2 = bl2 && arrcolor[i].getAlpha() == 255;
        }
        this.transparency = bl2 ? 1 : 3;
    }

    public final float[] getFractions() {
        float[] arrf = new float[this.fractions.length];
        System.arraycopy(this.fractions, 0, arrf, 0, this.fractions.length);
        return arrf;
    }

    public final Color[] getColors() {
        Color[] arrcolor = new Color[this.fractions.length];
        System.arraycopy(this.fractions, 0, arrcolor, 0, this.fractions.length);
        return arrcolor;
    }

    public final CycleMethod getCycleMethod() {
        return this.cycleMethod;
    }

    public final ColorSpaceType getColorSpace() {
        return this.colorSpace;
    }

    public final AffineTransform getTransform() {
        return new AffineTransform(this.gradientTransform);
    }

    @Override
    public final int getTransparency() {
        return this.transparency;
    }

    public static final class ColorSpaceType
    extends Enum<ColorSpaceType> {
        public static final /* enum */ ColorSpaceType SRGB = new ColorSpaceType();
        public static final /* enum */ ColorSpaceType LINEAR_RGB = new ColorSpaceType();
        private static final /* synthetic */ ColorSpaceType[] $VALUES;

        public static ColorSpaceType[] values() {
            return (ColorSpaceType[])$VALUES.clone();
        }

        public static ColorSpaceType valueOf(String string) {
            return Enum.valueOf(ColorSpaceType.class, string);
        }

        private static /* synthetic */ ColorSpaceType[] $values() {
            return new ColorSpaceType[]{SRGB, LINEAR_RGB};
        }

        static {
            $VALUES = ColorSpaceType.$values();
        }
    }

    public static final class CycleMethod
    extends Enum<CycleMethod> {
        public static final /* enum */ CycleMethod NO_CYCLE = new CycleMethod();
        public static final /* enum */ CycleMethod REFLECT = new CycleMethod();
        public static final /* enum */ CycleMethod REPEAT = new CycleMethod();
        private static final /* synthetic */ CycleMethod[] $VALUES;

        public static CycleMethod[] values() {
            return (CycleMethod[])$VALUES.clone();
        }

        public static CycleMethod valueOf(String string) {
            return Enum.valueOf(CycleMethod.class, string);
        }

        private static /* synthetic */ CycleMethod[] $values() {
            return new CycleMethod[]{NO_CYCLE, REFLECT, REPEAT};
        }

        static {
            $VALUES = CycleMethod.$values();
        }
    }
}

