/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.Colormap;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.LinearColormap;
import com.jhlabs.image.WholeImageFilter;
import java.awt.Rectangle;
import java.util.Date;
import java.util.Random;

public class SmearFilter
extends WholeImageFilter {
    public static final int CROSSES = 0;
    public static final int LINES = 1;
    public static final int CIRCLES = 2;
    public static final int SQUARES = 3;
    private Colormap colormap = new LinearColormap();
    private float angle = 0.0f;
    private float density = 0.5f;
    private float scatter = 0.0f;
    private int distance = 8;
    private Random randomGenerator = new Random();
    private long seed = 567L;
    private int shape = 1;
    private float mix = 0.5f;
    private int fadeout = 0;
    private boolean background = false;

    public void setShape(int shape) {
        this.shape = shape;
    }

    public int getShape() {
        return this.shape;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getDensity() {
        return this.density;
    }

    public void setScatter(float scatter) {
        this.scatter = scatter;
    }

    public float getScatter() {
        return this.scatter;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setMix(float mix) {
        this.mix = mix;
    }

    public float getMix() {
        return this.mix;
    }

    public void setFadeout(int fadeout) {
        this.fadeout = fadeout;
    }

    public int getFadeout() {
        return this.fadeout;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }

    public boolean getBackground() {
        return this.background;
    }

    public void randomize() {
        this.seed = new Date().getTime();
    }

    private float random(float low, float high) {
        return low + (high - low) * this.randomGenerator.nextFloat();
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int[] outPixels = new int[width * height];
        this.randomGenerator.setSeed(this.seed);
        float sinAngle = (float)Math.sin(this.angle);
        float cosAngle = (float)Math.cos(this.angle);
        int i = 0;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                outPixels[i] = this.background ? -1 : inPixels[i];
                ++i;
            }
        }
        switch (this.shape) {
            case 0: {
                int numShapes = (int)(2.0f * this.density * (float)width * (float)height / (float)(this.distance + 1));
                for (i = 0; i < numShapes; ++i) {
                    int rgb2;
                    int x = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % width;
                    int y = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % height;
                    int length = this.randomGenerator.nextInt() % this.distance + 1;
                    int rgb = inPixels[y * width + x];
                    for (int x1 = x - length; x1 < x + length + 1; ++x1) {
                        if (x1 < 0 || x1 >= width) continue;
                        rgb2 = this.background ? -1 : outPixels[y * width + x1];
                        outPixels[y * width + x1] = ImageMath.mixColors(this.mix, rgb2, rgb);
                    }
                    for (int y1 = y - length; y1 < y + length + 1; ++y1) {
                        if (y1 < 0 || y1 >= height) continue;
                        rgb2 = this.background ? -1 : outPixels[y1 * width + x];
                        outPixels[y1 * width + x] = ImageMath.mixColors(this.mix, rgb2, rgb);
                    }
                }
                break;
            }
            case 1: {
                int numShapes = (int)(2.0f * this.density * (float)width * (float)height / 2.0f);
                for (i = 0; i < numShapes; ++i) {
                    int incrNE;
                    int incrE;
                    int d;
                    int rgb2;
                    int sx = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % width;
                    int sy = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % height;
                    int rgb = inPixels[sy * width + sx];
                    int length = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % this.distance;
                    int dx = (int)((float)length * cosAngle);
                    int dy = (int)((float)length * sinAngle);
                    int x0 = sx - dx;
                    int y0 = sy - dy;
                    int x1 = sx + dx;
                    int y1 = sy + dy;
                    int ddx = x1 < x0 ? -1 : 1;
                    int ddy = y1 < y0 ? -1 : 1;
                    dx = x1 - x0;
                    dy = y1 - y0;
                    dx = Math.abs(dx);
                    dy = Math.abs(dy);
                    int x = x0;
                    int y = y0;
                    if (x < width && x >= 0 && y < height && y >= 0) {
                        rgb2 = this.background ? -1 : outPixels[y * width + x];
                        outPixels[y * width + x] = ImageMath.mixColors(this.mix, rgb2, rgb);
                    }
                    if (Math.abs(dx) > Math.abs(dy)) {
                        d = 2 * dy - dx;
                        incrE = 2 * dy;
                        incrNE = 2 * (dy - dx);
                        while (x != x1) {
                            if (d <= 0) {
                                d += incrE;
                            } else {
                                d += incrNE;
                                y += ddy;
                            }
                            if ((x += ddx) >= width || x < 0 || y >= height || y < 0) continue;
                            rgb2 = this.background ? -1 : outPixels[y * width + x];
                            outPixels[y * width + x] = ImageMath.mixColors(this.mix, rgb2, rgb);
                        }
                        continue;
                    }
                    d = 2 * dx - dy;
                    incrE = 2 * dx;
                    incrNE = 2 * (dx - dy);
                    while (y != y1) {
                        if (d <= 0) {
                            d += incrE;
                        } else {
                            d += incrNE;
                            x += ddx;
                        }
                        if (x >= width || x < 0 || (y += ddy) >= height || y < 0) continue;
                        rgb2 = this.background ? -1 : outPixels[y * width + x];
                        outPixels[y * width + x] = ImageMath.mixColors(this.mix, rgb2, rgb);
                    }
                }
                break;
            }
            case 2: 
            case 3: {
                int radius = this.distance + 1;
                int radius2 = radius * radius;
                int numShapes = (int)(2.0f * this.density * (float)width * (float)height / (float)radius);
                for (i = 0; i < numShapes; ++i) {
                    int sx = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % width;
                    int sy = (this.randomGenerator.nextInt() & Integer.MAX_VALUE) % height;
                    int rgb = inPixels[sy * width + sx];
                    for (int x = sx - radius; x < sx + radius + 1; ++x) {
                        for (int y = sy - radius; y < sy + radius + 1; ++y) {
                            int f = this.shape == 2 ? (x - sx) * (x - sx) + (y - sy) * (y - sy) : 0;
                            if (x < 0 || x >= width || y < 0 || y >= height || f > radius2) continue;
                            int rgb2 = this.background ? -1 : outPixels[y * width + x];
                            outPixels[y * width + x] = ImageMath.mixColors(this.mix, rgb2, rgb);
                        }
                    }
                }
                break;
            }
        }
        return outPixels;
    }

    public String toString() {
        return "Effects/Smear...";
    }
}

