/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ImageMath;

public class Curve {
    public float[] x;
    public float[] y;

    public Curve() {
        this.x = new float[]{0.0f, 1.0f};
        this.y = new float[]{0.0f, 1.0f};
    }

    public Curve(Curve curve) {
        this.x = (float[])curve.x.clone();
        this.y = (float[])curve.y.clone();
    }

    public int addKnot(float kx, float ky) {
        int pos = -1;
        int numKnots = this.x.length;
        float[] nx = new float[numKnots + 1];
        float[] ny = new float[numKnots + 1];
        int j = 0;
        for (int i = 0; i < numKnots; ++i) {
            if (pos == -1 && this.x[i] > kx) {
                pos = j++;
                nx[j] = kx;
                ny[j] = ky;
            }
            nx[j] = this.x[i];
            ny[j] = this.y[i];
            ++j;
        }
        if (pos == -1) {
            pos = j;
            nx[j] = kx;
            ny[j] = ky;
        }
        this.x = nx;
        this.y = ny;
        return pos;
    }

    public void removeKnot(int n) {
        int numKnots = this.x.length;
        if (numKnots <= 2) {
            return;
        }
        float[] nx = new float[numKnots - 1];
        float[] ny = new float[numKnots - 1];
        int j = 0;
        for (int i = 0; i < numKnots - 1; ++i) {
            if (i == n) {
                ++j;
            }
            nx[i] = this.x[j];
            ny[i] = this.y[j];
            ++j;
        }
        this.x = nx;
        this.y = ny;
    }

    private void sortKnots() {
        int numKnots = this.x.length;
        for (int i = 1; i < numKnots - 1; ++i) {
            for (int j = 1; j < i; ++j) {
                if (!(this.x[i] < this.x[j])) continue;
                float t = this.x[i];
                this.x[i] = this.x[j];
                this.x[j] = t;
                t = this.y[i];
                this.y[i] = this.y[j];
                this.y[j] = t;
            }
        }
    }

    protected int[] makeTable() {
        int numKnots = this.x.length;
        float[] nx = new float[numKnots + 2];
        float[] ny = new float[numKnots + 2];
        System.arraycopy(this.x, 0, nx, 1, numKnots);
        System.arraycopy(this.y, 0, ny, 1, numKnots);
        nx[0] = nx[1];
        ny[0] = ny[1];
        nx[numKnots + 1] = nx[numKnots];
        ny[numKnots + 1] = ny[numKnots];
        int[] table = new int[256];
        for (int i = 0; i < 1024; ++i) {
            float f = (float)i / 1024.0f;
            int x = (int)(255.0f * ImageMath.spline(f, nx.length, nx) + 0.5f);
            int y = (int)(255.0f * ImageMath.spline(f, nx.length, ny) + 0.5f);
            x = ImageMath.clamp(x, 0, 255);
            table[x] = y = ImageMath.clamp(y, 0, 255);
        }
        return table;
    }
}

