/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.util.Random;

public class BrushedMetalFilter
implements BufferedImageOp {
    private int radius = 10;
    private float amount = 0.1f;
    private int color = -7829368;
    private float shine = 0.1f;
    private boolean monochrome = true;
    private Random randomNumbers;

    public BrushedMetalFilter() {
    }

    public BrushedMetalFilter(int color, int radius, float amount, boolean monochrome, float shine) {
        this.color = color;
        this.radius = radius;
        this.amount = amount;
        this.monochrome = monochrome;
        this.shine = shine;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        int[] inPixels = new int[width];
        int[] outPixels = new int[width];
        this.randomNumbers = new Random(0L);
        int a = this.color & 0xFF000000;
        int r = this.color >> 16 & 0xFF;
        int g = this.color >> 8 & 0xFF;
        int b = this.color & 0xFF;
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int tr = r;
                int tg = g;
                int tb = b;
                if (this.shine != 0.0f) {
                    int f = (int)((double)(255.0f * this.shine) * Math.sin((double)x / (double)width * Math.PI));
                    tr += f;
                    tg += f;
                    tb += f;
                }
                if (this.monochrome) {
                    int n = (int)(255.0f * (2.0f * this.randomNumbers.nextFloat() - 1.0f) * this.amount);
                    inPixels[x] = a | BrushedMetalFilter.clamp(tr + n) << 16 | BrushedMetalFilter.clamp(tg + n) << 8 | BrushedMetalFilter.clamp(tb + n);
                    continue;
                }
                inPixels[x] = a | this.random(tr) << 16 | this.random(tg) << 8 | this.random(tb);
            }
            if (this.radius != 0) {
                this.blur(inPixels, outPixels, width, this.radius);
                this.setRGB(dst, 0, y, width, 1, outPixels);
                continue;
            }
            this.setRGB(dst, 0, y, width, 1, inPixels);
        }
        return dst;
    }

    private int random(int x) {
        if ((x += (int)(255.0f * (2.0f * this.randomNumbers.nextFloat() - 1.0f) * this.amount)) < 0) {
            x = 0;
        } else if (x > 255) {
            x = 255;
        }
        return x;
    }

    private static int clamp(int c) {
        if (c < 0) {
            return 0;
        }
        if (c > 255) {
            return 255;
        }
        return c;
    }

    private static int mod(int a, int b) {
        int n;
        if ((a -= (n = a / b) * b) < 0) {
            return a + b;
        }
        return a;
    }

    public void blur(int[] in, int[] out, int width, int radius) {
        int widthMinus1 = width - 1;
        int r2 = 2 * radius + 1;
        int tr = 0;
        int tg = 0;
        int tb = 0;
        for (int i = -radius; i <= radius; ++i) {
            int rgb = in[BrushedMetalFilter.mod(i, width)];
            tr += rgb >> 16 & 0xFF;
            tg += rgb >> 8 & 0xFF;
            tb += rgb & 0xFF;
        }
        for (int x = 0; x < width; ++x) {
            int i2;
            out[x] = 0xFF000000 | tr / r2 << 16 | tg / r2 << 8 | tb / r2;
            int i1 = x + radius + 1;
            if (i1 > widthMinus1) {
                i1 = BrushedMetalFilter.mod(i1, width);
            }
            if ((i2 = x - radius) < 0) {
                i2 = BrushedMetalFilter.mod(i2, width);
            }
            int rgb1 = in[i1];
            int rgb2 = in[i2];
            tr += (rgb1 & 0xFF0000) - (rgb2 & 0xFF0000) >> 16;
            tg += (rgb1 & 0xFF00) - (rgb2 & 0xFF00) >> 8;
            tb += (rgb1 & 0xFF) - (rgb2 & 0xFF);
        }
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return this.radius;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setShine(float shine) {
        this.shine = shine;
    }

    public float getShine() {
        return this.shine;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public void setMonochrome(boolean monochrome) {
        this.monochrome = monochrome;
    }

    public boolean getMonochrome() {
        return this.monochrome;
    }

    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel dstCM) {
        if (dstCM == null) {
            dstCM = src.getColorModel();
        }
        return new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), dstCM.isAlphaPremultiplied(), null);
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage src) {
        return new Rectangle(0, 0, src.getWidth(), src.getHeight());
    }

    @Override
    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {
        if (dstPt == null) {
            dstPt = new Point2D.Double();
        }
        dstPt.setLocation(srcPt.getX(), srcPt.getY());
        return dstPt;
    }

    @Override
    public RenderingHints getRenderingHints() {
        return null;
    }

    private void setRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
        int type = image.getType();
        if (type == 2 || type == 1) {
            image.getRaster().setDataElements(x, y, width, height, pixels);
        } else {
            image.setRGB(x, y, width, height, pixels, 0, width);
        }
    }

    public String toString() {
        return "Texture/Brushed Metal...";
    }
}

