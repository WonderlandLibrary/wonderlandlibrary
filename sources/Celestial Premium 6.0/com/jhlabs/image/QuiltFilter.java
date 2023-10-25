/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Colormap;
import com.jhlabs.image.LinearColormap;
import com.jhlabs.image.WholeImageFilter;
import java.awt.Rectangle;
import java.util.Date;
import java.util.Random;

public class QuiltFilter
extends WholeImageFilter {
    private Random randomGenerator;
    private long seed = 567L;
    private int iterations = 25000;
    private float a = -0.59f;
    private float b = 0.2f;
    private float c = 0.1f;
    private float d = 0.0f;
    private int k = 0;
    private Colormap colormap = new LinearColormap();

    public QuiltFilter() {
        this.randomGenerator = new Random();
    }

    public void randomize() {
        this.seed = new Date().getTime();
        this.randomGenerator.setSeed(this.seed);
        this.a = this.randomGenerator.nextFloat();
        this.b = this.randomGenerator.nextFloat();
        this.c = this.randomGenerator.nextFloat();
        this.d = this.randomGenerator.nextFloat();
        this.k = this.randomGenerator.nextInt() % 20 - 10;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getIterations() {
        return this.iterations;
    }

    public void setA(float a) {
        this.a = a;
    }

    public float getA() {
        return this.a;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getB() {
        return this.b;
    }

    public void setC(float c) {
        this.c = c;
    }

    public float getC() {
        return this.c;
    }

    public void setD(float d) {
        this.d = d;
    }

    public float getD() {
        return this.d;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getK() {
        return this.k;
    }

    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    public Colormap getColormap() {
        return this.colormap;
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        float my;
        float mx;
        int n;
        int[] outPixels = new int[width * height];
        boolean i = false;
        int max = 0;
        float x = 0.1f;
        float y = 0.3f;
        for (n = 0; n < 20; ++n) {
            mx = (float)Math.PI * x;
            my = (float)Math.PI * y;
            float smx2 = (float)Math.sin(2.0f * mx);
            float smy2 = (float)Math.sin(2.0f * my);
            float x1 = (float)((double)(this.a * smx2) + (double)(this.b * smx2) * Math.cos(2.0f * my) + (double)this.c * Math.sin(4.0f * mx) + (double)this.d * Math.sin(6.0f * mx) * Math.cos(4.0f * my) + (double)((float)this.k * x));
            x1 = x1 >= 0.0f ? x1 - (float)((int)x1) : x1 - (float)((int)x1) + 1.0f;
            float y1 = (float)((double)(this.a * smy2) + (double)(this.b * smy2) * Math.cos(2.0f * mx) + (double)this.c * Math.sin(4.0f * my) + (double)this.d * Math.sin(6.0f * my) * Math.cos(4.0f * mx) + (double)((float)this.k * y));
            y1 = y1 >= 0.0f ? y1 - (float)((int)y1) : y1 - (float)((int)y1) + 1.0f;
            x = x1;
            y = y1;
        }
        for (n = 0; n < this.iterations; ++n) {
            int t;
            mx = (float)Math.PI * x;
            my = (float)Math.PI * y;
            float x1 = (float)((double)this.a * Math.sin(2.0f * mx) + (double)this.b * Math.sin(2.0f * mx) * Math.cos(2.0f * my) + (double)this.c * Math.sin(4.0f * mx) + (double)this.d * Math.sin(6.0f * mx) * Math.cos(4.0f * my) + (double)((float)this.k * x));
            x1 = x1 >= 0.0f ? x1 - (float)((int)x1) : x1 - (float)((int)x1) + 1.0f;
            float y1 = (float)((double)this.a * Math.sin(2.0f * my) + (double)this.b * Math.sin(2.0f * my) * Math.cos(2.0f * mx) + (double)this.c * Math.sin(4.0f * my) + (double)this.d * Math.sin(6.0f * my) * Math.cos(4.0f * mx) + (double)((float)this.k * y));
            y1 = y1 >= 0.0f ? y1 - (float)((int)y1) : y1 - (float)((int)y1) + 1.0f;
            x = x1;
            y = y1;
            int ix = (int)((float)width * x);
            int iy = (int)((float)height * y);
            if (ix < 0 || ix >= width || iy < 0 || iy >= height) continue;
            int n2 = width * iy + ix;
            outPixels[n2] = outPixels[n2] + 1;
            if (t <= max) continue;
            max = t;
        }
        if (this.colormap != null) {
            int index = 0;
            for (y = 0.0f; y < (float)height; y += 1.0f) {
                for (x = 0.0f; x < (float)width; x += 1.0f) {
                    outPixels[index] = this.colormap.getColor((float)outPixels[index] / (float)max);
                    ++index;
                }
            }
        }
        return outPixels;
    }

    public String toString() {
        return "Texture/Chaotic Quilt...";
    }
}

