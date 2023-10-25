/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ArrayColormap;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.PixelUtils;

public class SplineColormap
extends ArrayColormap {
    private int numKnots = 4;
    private int[] xKnots;
    private int[] yKnots;

    public SplineColormap() {
        int[] arrn = new int[4];
        arrn[2] = 255;
        arrn[3] = 255;
        this.xKnots = arrn;
        this.yKnots = new int[]{-16777216, -16777216, -1, -1};
        this.rebuildGradient();
    }

    public SplineColormap(int[] xKnots, int[] yKnots) {
        int[] arrn = new int[4];
        arrn[2] = 255;
        arrn[3] = 255;
        this.xKnots = arrn;
        this.yKnots = new int[]{-16777216, -16777216, -1, -1};
        this.xKnots = xKnots;
        this.yKnots = yKnots;
        this.numKnots = xKnots.length;
        this.rebuildGradient();
    }

    public void setKnot(int n, int color) {
        this.yKnots[n] = color;
        this.rebuildGradient();
    }

    public int getKnot(int n) {
        return this.yKnots[n];
    }

    public void addKnot(int x, int color) {
        int[] nx = new int[this.numKnots + 1];
        int[] ny = new int[this.numKnots + 1];
        System.arraycopy(this.xKnots, 0, nx, 0, this.numKnots);
        System.arraycopy(this.yKnots, 0, ny, 0, this.numKnots);
        this.xKnots = nx;
        this.yKnots = ny;
        this.xKnots[this.numKnots] = x;
        this.yKnots[this.numKnots] = color;
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
        }
        --this.numKnots;
        this.rebuildGradient();
    }

    public void setKnotPosition(int n, int x) {
        this.xKnots[n] = PixelUtils.clamp(x);
        this.sortKnots();
        this.rebuildGradient();
    }

    private void rebuildGradient() {
        this.xKnots[0] = -1;
        this.xKnots[this.numKnots - 1] = 256;
        this.yKnots[0] = this.yKnots[1];
        this.yKnots[this.numKnots - 1] = this.yKnots[this.numKnots - 2];
        for (int i = 0; i < 256; ++i) {
            this.map[i] = ImageMath.colorSpline(i, this.numKnots, this.xKnots, this.yKnots);
        }
    }

    private void sortKnots() {
        for (int i = 1; i < this.numKnots; ++i) {
            for (int j = 1; j < i; ++j) {
                if (this.xKnots[i] >= this.xKnots[j]) continue;
                int t = this.xKnots[i];
                this.xKnots[i] = this.xKnots[j];
                this.xKnots[j] = t;
                t = this.yKnots[i];
                this.yKnots[i] = this.yKnots[j];
                this.yKnots[j] = t;
            }
        }
    }
}

