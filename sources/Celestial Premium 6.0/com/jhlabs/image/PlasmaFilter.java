/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Colormap;
import com.jhlabs.image.LinearColormap;
import com.jhlabs.image.PixelUtils;
import com.jhlabs.image.WholeImageFilter;
import java.awt.Rectangle;
import java.util.Date;
import java.util.Random;

public class PlasmaFilter
extends WholeImageFilter {
    public float turbulence = 1.0f;
    private float scaling = 0.0f;
    private Colormap colormap = new LinearColormap();
    private Random randomGenerator = new Random();
    private long seed = 567L;
    private boolean useColormap = false;
    private boolean useImageColors = false;

    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
    }

    public float getTurbulence() {
        return this.turbulence;
    }

    public void setScaling(float scaling) {
        this.scaling = scaling;
    }

    public float getScaling() {
        return this.scaling;
    }

    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    public Colormap getColormap() {
        return this.colormap;
    }

    public void setUseColormap(boolean useColormap) {
        this.useColormap = useColormap;
    }

    public boolean getUseColormap() {
        return this.useColormap;
    }

    public void setUseImageColors(boolean useImageColors) {
        this.useImageColors = useImageColors;
    }

    public boolean getUseImageColors() {
        return this.useImageColors;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getSeed() {
        return (int)this.seed;
    }

    public void randomize() {
        this.seed = new Date().getTime();
    }

    private int randomRGB(int[] inPixels, int x, int y) {
        if (this.useImageColors) {
            return inPixels[y * this.originalSpace.width + x];
        }
        int r = (int)(255.0f * this.randomGenerator.nextFloat());
        int g = (int)(255.0f * this.randomGenerator.nextFloat());
        int b = (int)(255.0f * this.randomGenerator.nextFloat());
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    private int displace(int rgb, float amount) {
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        r = PixelUtils.clamp(r + (int)((double)amount * ((double)this.randomGenerator.nextFloat() - 0.5)));
        g = PixelUtils.clamp(g + (int)((double)amount * ((double)this.randomGenerator.nextFloat() - 0.5)));
        b = PixelUtils.clamp(b + (int)((double)amount * ((double)this.randomGenerator.nextFloat() - 0.5)));
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    private int average(int rgb1, int rgb2) {
        return PixelUtils.combinePixels(rgb1, rgb2, 13);
    }

    private int getPixel(int x, int y, int[] pixels, int stride) {
        return pixels[y * stride + x];
    }

    private void putPixel(int x, int y, int rgb, int[] pixels, int stride) {
        pixels[y * stride + x] = rgb;
    }

    private boolean doPixel(int x1, int y1, int x2, int y2, int[] pixels, int stride, int depth, int scale) {
        if (depth == 0) {
            int tl = this.getPixel(x1, y1, pixels, stride);
            int bl = this.getPixel(x1, y2, pixels, stride);
            int tr = this.getPixel(x2, y1, pixels, stride);
            int br = this.getPixel(x2, y2, pixels, stride);
            float amount = 256.0f / (2.0f * (float)scale) * this.turbulence;
            int mx = (x1 + x2) / 2;
            int my = (y1 + y2) / 2;
            if (mx == x1 && mx == x2 && my == y1 && my == y2) {
                return true;
            }
            if (mx != x1 || mx != x2) {
                int ml = this.average(tl, bl);
                ml = this.displace(ml, amount);
                this.putPixel(x1, my, ml, pixels, stride);
                if (x1 != x2) {
                    int mr = this.average(tr, br);
                    mr = this.displace(mr, amount);
                    this.putPixel(x2, my, mr, pixels, stride);
                }
            }
            if (my != y1 || my != y2) {
                if (x1 != mx || my != y2) {
                    int mb = this.average(bl, br);
                    mb = this.displace(mb, amount);
                    this.putPixel(mx, y2, mb, pixels, stride);
                }
                if (y1 != y2) {
                    int mt = this.average(tl, tr);
                    mt = this.displace(mt, amount);
                    this.putPixel(mx, y1, mt, pixels, stride);
                }
            }
            if (y1 != y2 || x1 != x2) {
                int mm = this.average(tl, br);
                int t = this.average(bl, tr);
                mm = this.average(mm, t);
                mm = this.displace(mm, amount);
                this.putPixel(mx, my, mm, pixels, stride);
            }
            return x2 - x1 >= 3 || y2 - y1 >= 3;
        }
        int mx = (x1 + x2) / 2;
        int my = (y1 + y2) / 2;
        this.doPixel(x1, y1, mx, my, pixels, stride, depth - 1, scale + 1);
        this.doPixel(x1, my, mx, y2, pixels, stride, depth - 1, scale + 1);
        this.doPixel(mx, y1, x2, my, pixels, stride, depth - 1, scale + 1);
        return this.doPixel(mx, my, x2, y2, pixels, stride, depth - 1, scale + 1);
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int[] outPixels = new int[width * height];
        this.randomGenerator.setSeed(this.seed);
        int w1 = width - 1;
        int h1 = height - 1;
        this.putPixel(0, 0, this.randomRGB(inPixels, 0, 0), outPixels, width);
        this.putPixel(w1, 0, this.randomRGB(inPixels, w1, 0), outPixels, width);
        this.putPixel(0, h1, this.randomRGB(inPixels, 0, h1), outPixels, width);
        this.putPixel(w1, h1, this.randomRGB(inPixels, w1, h1), outPixels, width);
        this.putPixel(w1 / 2, h1 / 2, this.randomRGB(inPixels, w1 / 2, h1 / 2), outPixels, width);
        this.putPixel(0, h1 / 2, this.randomRGB(inPixels, 0, h1 / 2), outPixels, width);
        this.putPixel(w1, h1 / 2, this.randomRGB(inPixels, w1, h1 / 2), outPixels, width);
        this.putPixel(w1 / 2, 0, this.randomRGB(inPixels, w1 / 2, 0), outPixels, width);
        this.putPixel(w1 / 2, h1, this.randomRGB(inPixels, w1 / 2, h1), outPixels, width);
        int depth = 1;
        while (this.doPixel(0, 0, width - 1, height - 1, outPixels, width, depth, 0)) {
            ++depth;
        }
        if (this.useColormap && this.colormap != null) {
            int index = 0;
            for (int y = 0; y < height; ++y) {
                for (int x = 0; x < width; ++x) {
                    outPixels[index] = this.colormap.getColor((float)(outPixels[index] & 0xFF) / 255.0f);
                    ++index;
                }
            }
        }
        return outPixels;
    }

    public String toString() {
        return "Texture/Plasma...";
    }
}

