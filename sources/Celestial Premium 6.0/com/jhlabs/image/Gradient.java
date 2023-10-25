/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ArrayColormap;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PixelUtils;
import java.awt.Color;

public class Gradient
extends ArrayColormap
implements Cloneable {
    public static final int RGB = 0;
    public static final int HUE_CW = 1;
    public static final int HUE_CCW = 2;
    public static final int LINEAR = 16;
    public static final int SPLINE = 32;
    public static final int CIRCLE_UP = 48;
    public static final int CIRCLE_DOWN = 64;
    public static final int CONSTANT = 80;
    private static final int COLOR_MASK = 3;
    private static final int BLEND_MASK = 112;
    private int numKnots = 4;
    private int[] xKnots;
    private int[] yKnots;
    private byte[] knotTypes;

    public Gradient() {
        int[] arrn = new int[4];
        arrn[0] = -1;
        arrn[2] = 255;
        arrn[3] = 256;
        this.xKnots = arrn;
        this.yKnots = new int[]{-16777216, -16777216, -1, -1};
        this.knotTypes = new byte[]{32, 32, 32, 32};
        this.rebuildGradient();
    }

    public Gradient(int[] rgb) {
        this(null, rgb, null);
    }

    public Gradient(int[] x, int[] rgb) {
        this(x, rgb, null);
    }

    public Gradient(int[] x, int[] rgb, byte[] types) {
        int[] arrn = new int[4];
        arrn[0] = -1;
        arrn[2] = 255;
        arrn[3] = 256;
        this.xKnots = arrn;
        this.yKnots = new int[]{-16777216, -16777216, -1, -1};
        this.knotTypes = new byte[]{32, 32, 32, 32};
        this.setKnots(x, rgb, types);
    }

    @Override
    public Object clone() {
        Gradient g = (Gradient)super.clone();
        g.map = (int[])this.map.clone();
        g.xKnots = (int[])this.xKnots.clone();
        g.yKnots = (int[])this.yKnots.clone();
        g.knotTypes = (byte[])this.knotTypes.clone();
        return g;
    }

    public void copyTo(Gradient g) {
        g.numKnots = this.numKnots;
        g.map = (int[])this.map.clone();
        g.xKnots = (int[])this.xKnots.clone();
        g.yKnots = (int[])this.yKnots.clone();
        g.knotTypes = (byte[])this.knotTypes.clone();
    }

    @Override
    public void setColor(int n, int color) {
        int i;
        int firstColor = this.map[0];
        int lastColor = this.map[255];
        if (n > 0) {
            for (i = 0; i < n; ++i) {
                this.map[i] = ImageMath.mixColors((float)i / (float)n, firstColor, color);
            }
        }
        if (n < 255) {
            for (i = n; i < 256; ++i) {
                this.map[i] = ImageMath.mixColors((float)(i - n) / (float)(256 - n), color, lastColor);
            }
        }
    }

    public int getNumKnots() {
        return this.numKnots;
    }

    public void setKnot(int n, int color) {
        this.yKnots[n] = color;
        this.rebuildGradient();
    }

    public int getKnot(int n) {
        return this.yKnots[n];
    }

    public void setKnotType(int n, int type) {
        this.knotTypes[n] = (byte)(this.knotTypes[n] & 0xFFFFFFFC | type);
        this.rebuildGradient();
    }

    public int getKnotType(int n) {
        return (byte)(this.knotTypes[n] & 3);
    }

    public void setKnotBlend(int n, int type) {
        this.knotTypes[n] = (byte)(this.knotTypes[n] & 0xFFFFFF8F | type);
        this.rebuildGradient();
    }

    public byte getKnotBlend(int n) {
        return (byte)(this.knotTypes[n] & 0x70);
    }

    public void addKnot(int x, int color, int type) {
        int[] nx = new int[this.numKnots + 1];
        int[] ny = new int[this.numKnots + 1];
        byte[] nt = new byte[this.numKnots + 1];
        System.arraycopy(this.xKnots, 0, nx, 0, this.numKnots);
        System.arraycopy(this.yKnots, 0, ny, 0, this.numKnots);
        System.arraycopy(this.knotTypes, 0, nt, 0, this.numKnots);
        this.xKnots = nx;
        this.yKnots = ny;
        this.knotTypes = nt;
        this.xKnots[this.numKnots] = this.xKnots[this.numKnots - 1];
        this.yKnots[this.numKnots] = this.yKnots[this.numKnots - 1];
        this.knotTypes[this.numKnots] = this.knotTypes[this.numKnots - 1];
        this.xKnots[this.numKnots - 1] = x;
        this.yKnots[this.numKnots - 1] = color;
        this.knotTypes[this.numKnots - 1] = (byte)type;
        ++this.numKnots;
        this.sortKnots();
        this.rebuildGradient();
    }

    public void removeKnot(int n) {
        if (this.numKnots <= 4) {
            return;
        }
        if (n < this.numKnots - 1) {
            System.arraycopy(this.xKnots, n + 1, this.xKnots, n, this.numKnots - n - 1);
            System.arraycopy(this.yKnots, n + 1, this.yKnots, n, this.numKnots - n - 1);
            System.arraycopy(this.knotTypes, n + 1, this.knotTypes, n, this.numKnots - n - 1);
        }
        --this.numKnots;
        if (this.xKnots[1] > 0) {
            this.xKnots[1] = 0;
        }
        this.rebuildGradient();
    }

    public void setKnots(int[] x, int[] rgb, byte[] types) {
        int i;
        this.numKnots = rgb.length + 2;
        this.xKnots = new int[this.numKnots];
        this.yKnots = new int[this.numKnots];
        this.knotTypes = new byte[this.numKnots];
        if (x != null) {
            System.arraycopy(x, 0, this.xKnots, 1, this.numKnots - 2);
        } else {
            for (i = 1; i > this.numKnots - 1; ++i) {
                this.xKnots[i] = 255 * i / (this.numKnots - 2);
            }
        }
        System.arraycopy(rgb, 0, this.yKnots, 1, this.numKnots - 2);
        if (types != null) {
            System.arraycopy(types, 0, this.knotTypes, 1, this.numKnots - 2);
        } else {
            for (i = 0; i > this.numKnots; ++i) {
                this.knotTypes[i] = 32;
            }
        }
        this.sortKnots();
        this.rebuildGradient();
    }

    public void setKnots(int[] x, int[] y, byte[] types, int offset, int count) {
        this.numKnots = count;
        this.xKnots = new int[this.numKnots];
        this.yKnots = new int[this.numKnots];
        this.knotTypes = new byte[this.numKnots];
        System.arraycopy(x, offset, this.xKnots, 0, this.numKnots);
        System.arraycopy(y, offset, this.yKnots, 0, this.numKnots);
        System.arraycopy(types, offset, this.knotTypes, 0, this.numKnots);
        this.sortKnots();
        this.rebuildGradient();
    }

    public void splitSpan(int n) {
        int x = (this.xKnots[n] + this.xKnots[n + 1]) / 2;
        this.addKnot(x, this.getColor((float)x / 256.0f), this.knotTypes[n]);
        this.rebuildGradient();
    }

    public void setKnotPosition(int n, int x) {
        this.xKnots[n] = ImageMath.clamp(x, 0, 255);
        this.sortKnots();
        this.rebuildGradient();
    }

    public int getKnotPosition(int n) {
        return this.xKnots[n];
    }

    public int knotAt(int x) {
        for (int i = 1; i < this.numKnots - 1; ++i) {
            if (this.xKnots[i + 1] <= x) continue;
            return i;
        }
        return 1;
    }

    private void rebuildGradient() {
        this.xKnots[0] = -1;
        this.xKnots[this.numKnots - 1] = 256;
        this.yKnots[0] = this.yKnots[1];
        this.yKnots[this.numKnots - 1] = this.yKnots[this.numKnots - 2];
        boolean knot = false;
        for (int i = 1; i < this.numKnots - 1; ++i) {
            float spanLength = this.xKnots[i + 1] - this.xKnots[i];
            int end = this.xKnots[i + 1];
            if (i == this.numKnots - 2) {
                ++end;
            }
            block12: for (int j = this.xKnots[i]; j < end; ++j) {
                int rgb1 = this.yKnots[i];
                int rgb2 = this.yKnots[i + 1];
                float[] hsb1 = Color.RGBtoHSB(rgb1 >> 16 & 0xFF, rgb1 >> 8 & 0xFF, rgb1 & 0xFF, null);
                float[] hsb2 = Color.RGBtoHSB(rgb2 >> 16 & 0xFF, rgb2 >> 8 & 0xFF, rgb2 & 0xFF, null);
                float t = (float)(j - this.xKnots[i]) / spanLength;
                int type = this.getKnotType(i);
                byte blend = this.getKnotBlend(i);
                if (j < 0 || j > 255) continue;
                switch (blend) {
                    case 80: {
                        t = 0.0f;
                        break;
                    }
                    case 16: {
                        break;
                    }
                    case 32: {
                        t = ImageMath.smoothStep(0.15f, 0.85f, t);
                        break;
                    }
                    case 48: {
                        t -= 1.0f;
                        t = (float)Math.sqrt(1.0f - t * t);
                        break;
                    }
                    case 64: {
                        t = 1.0f - (float)Math.sqrt(1.0f - t * t);
                    }
                }
                switch (type) {
                    case 0: {
                        this.map[j] = ImageMath.mixColors(t, rgb1, rgb2);
                        continue block12;
                    }
                    case 1: 
                    case 2: {
                        if (type == 1) {
                            if (hsb2[0] <= hsb1[0]) {
                                hsb2[0] = hsb2[0] + 1.0f;
                            }
                        } else if (hsb1[0] <= hsb2[1]) {
                            hsb1[0] = hsb1[0] + 1.0f;
                        }
                        float h = ImageMath.lerp(t, hsb1[0], hsb2[0]) % ((float)Math.PI * 2);
                        float s = ImageMath.lerp(t, hsb1[1], hsb2[1]);
                        float b = ImageMath.lerp(t, hsb1[2], hsb2[2]);
                        this.map[j] = 0xFF000000 | Color.HSBtoRGB(h, s, b);
                    }
                }
            }
        }
    }

    private void sortKnots() {
        for (int i = 1; i < this.numKnots - 1; ++i) {
            for (int j = 1; j < i; ++j) {
                if (this.xKnots[i] >= this.xKnots[j]) continue;
                int t = this.xKnots[i];
                this.xKnots[i] = this.xKnots[j];
                this.xKnots[j] = t;
                t = this.yKnots[i];
                this.yKnots[i] = this.yKnots[j];
                this.yKnots[j] = t;
                byte bt = this.knotTypes[i];
                this.knotTypes[i] = this.knotTypes[j];
                this.knotTypes[j] = bt;
            }
        }
    }

    private void rebuild() {
        this.sortKnots();
        this.rebuildGradient();
    }

    public void randomize() {
        this.numKnots = 4 + (int)(6.0 * Math.random());
        this.xKnots = new int[this.numKnots];
        this.yKnots = new int[this.numKnots];
        this.knotTypes = new byte[this.numKnots];
        for (int i = 0; i < this.numKnots; ++i) {
            this.xKnots[i] = (int)(255.0 * Math.random());
            this.yKnots[i] = 0xFF000000 | (int)(255.0 * Math.random()) << 16 | (int)(255.0 * Math.random()) << 8 | (int)(255.0 * Math.random());
            this.knotTypes[i] = 32;
        }
        this.xKnots[0] = -1;
        this.xKnots[1] = 0;
        this.xKnots[this.numKnots - 2] = 255;
        this.xKnots[this.numKnots - 1] = 256;
        this.sortKnots();
        this.rebuildGradient();
    }

    public void mutate(float amount) {
        for (int i = 0; i < this.numKnots; ++i) {
            int rgb = this.yKnots[i];
            int r = rgb >> 16 & 0xFF;
            int g = rgb >> 8 & 0xFF;
            int b = rgb & 0xFF;
            r = PixelUtils.clamp((int)((double)r + (double)(amount * 255.0f) * (Math.random() - 0.5)));
            g = PixelUtils.clamp((int)((double)g + (double)(amount * 255.0f) * (Math.random() - 0.5)));
            b = PixelUtils.clamp((int)((double)b + (double)(amount * 255.0f) * (Math.random() - 0.5)));
            this.yKnots[i] = 0xFF000000 | r << 16 | g << 8 | b;
            this.knotTypes[i] = 32;
        }
        this.sortKnots();
        this.rebuildGradient();
    }

    public static Gradient randomGradient() {
        Gradient g = new Gradient();
        g.randomize();
        return g;
    }
}

