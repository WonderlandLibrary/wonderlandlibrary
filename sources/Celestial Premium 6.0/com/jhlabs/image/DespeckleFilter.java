/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.WholeImageFilter;
import java.awt.Rectangle;

public class DespeckleFilter
extends WholeImageFilter {
    private short pepperAndSalt(short c, short v1, short v2) {
        if (c < v1) {
            c = (short)(c + 1);
        }
        if (c < v2) {
            c = (short)(c + 1);
        }
        if (c > v1) {
            c = (short)(c - 1);
        }
        if (c > v2) {
            c = (short)(c - 1);
        }
        return c;
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int index = 0;
        short[][] r = new short[3][width];
        short[][] g = new short[3][width];
        short[][] b = new short[3][width];
        int[] outPixels = new int[width * height];
        for (int x = 0; x < width; ++x) {
            int rgb = inPixels[x];
            r[1][x] = (short)(rgb >> 16 & 0xFF);
            g[1][x] = (short)(rgb >> 8 & 0xFF);
            b[1][x] = (short)(rgb & 0xFF);
        }
        for (int y = 0; y < height; ++y) {
            int x;
            boolean yIn = y > 0 && y < height - 1;
            int nextRowIndex = index + width;
            if (y < height - 1) {
                for (x = 0; x < width; ++x) {
                    int rgb = inPixels[nextRowIndex++];
                    r[2][x] = (short)(rgb >> 16 & 0xFF);
                    g[2][x] = (short)(rgb >> 8 & 0xFF);
                    b[2][x] = (short)(rgb & 0xFF);
                }
            }
            for (x = 0; x < width; ++x) {
                boolean xIn = x > 0 && x < width - 1;
                short or = r[1][x];
                short og = g[1][x];
                short ob = b[1][x];
                int w = x - 1;
                int e = x + 1;
                if (yIn) {
                    or = this.pepperAndSalt(or, r[0][x], r[2][x]);
                    og = this.pepperAndSalt(og, g[0][x], g[2][x]);
                    ob = this.pepperAndSalt(ob, b[0][x], b[2][x]);
                }
                if (xIn) {
                    or = this.pepperAndSalt(or, r[1][w], r[1][e]);
                    og = this.pepperAndSalt(og, g[1][w], g[1][e]);
                    ob = this.pepperAndSalt(ob, b[1][w], b[1][e]);
                }
                if (yIn && xIn) {
                    or = this.pepperAndSalt(or, r[0][w], r[2][e]);
                    og = this.pepperAndSalt(og, g[0][w], g[2][e]);
                    ob = this.pepperAndSalt(ob, b[0][w], b[2][e]);
                    or = this.pepperAndSalt(or, r[2][w], r[0][e]);
                    og = this.pepperAndSalt(og, g[2][w], g[0][e]);
                    ob = this.pepperAndSalt(ob, b[2][w], b[0][e]);
                }
                outPixels[index] = inPixels[index] & 0xFF000000 | or << 16 | og << 8 | ob;
                ++index;
            }
            short[] t = r[0];
            r[0] = r[1];
            r[1] = r[2];
            r[2] = t;
            t = g[0];
            g[0] = g[1];
            g[1] = g[2];
            g[2] = t;
            t = b[0];
            b[0] = b[1];
            b[1] = b[2];
            b[2] = t;
        }
        return outPixels;
    }

    public String toString() {
        return "Blur/Despeckle...";
    }
}

