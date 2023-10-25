/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.Colormap;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.LinearColormap;
import com.jhlabs.image.PixelUtils;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class GradientFilter
extends AbstractBufferedImageOp {
    public static final int LINEAR = 0;
    public static final int BILINEAR = 1;
    public static final int RADIAL = 2;
    public static final int CONICAL = 3;
    public static final int BICONICAL = 4;
    public static final int SQUARE = 5;
    public static final int INT_LINEAR = 0;
    public static final int INT_CIRCLE_UP = 1;
    public static final int INT_CIRCLE_DOWN = 2;
    public static final int INT_SMOOTH = 3;
    private float angle = 0.0f;
    private int color1 = -16777216;
    private int color2 = -1;
    private Point p1 = new Point(0, 0);
    private Point p2 = new Point(64, 64);
    private boolean repeat = false;
    private float x1;
    private float y1;
    private float dx;
    private float dy;
    private Colormap colormap = null;
    private int type;
    private int interpolation = 0;
    private int paintMode = 1;

    public GradientFilter() {
    }

    public GradientFilter(Point p1, Point p2, int color1, int color2, boolean repeat, int type, int interpolation) {
        this.p1 = p1;
        this.p2 = p2;
        this.color1 = color1;
        this.color2 = color2;
        this.repeat = repeat;
        this.type = type;
        this.interpolation = interpolation;
        this.colormap = new LinearColormap(color1, color2);
    }

    public void setPoint1(Point point1) {
        this.p1 = point1;
    }

    public Point getPoint1() {
        return this.p1;
    }

    public void setPoint2(Point point2) {
        this.p2 = point2;
    }

    public Point getPoint2() {
        return this.p2;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public void setInterpolation(int interpolation) {
        this.interpolation = interpolation;
    }

    public int getInterpolation() {
        return this.interpolation;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        this.p2 = new Point((int)(64.0 * Math.cos(angle)), (int)(64.0 * Math.sin(angle)));
    }

    public float getAngle() {
        return this.angle;
    }

    public void setColormap(Colormap colormap) {
        this.colormap = colormap;
    }

    public Colormap getColormap() {
        return this.colormap;
    }

    public void setPaintMode(int paintMode) {
        this.paintMode = paintMode;
    }

    public int getPaintMode() {
        return this.paintMode;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int rgb2;
        int rgb1;
        float y2;
        float y1;
        float x2;
        float x1;
        int width = src.getWidth();
        int height = src.getHeight();
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        if ((x1 = (float)this.p1.x) > (x2 = (float)this.p2.x) && this.type != 2) {
            y1 = x1;
            x1 = x2;
            x2 = y1;
            y1 = this.p2.y;
            y2 = this.p1.y;
            rgb1 = this.color2;
            rgb2 = this.color1;
        } else {
            y1 = this.p1.y;
            y2 = this.p2.y;
            rgb1 = this.color1;
            rgb2 = this.color2;
        }
        float dx = x2 - x1;
        float dy = y2 - y1;
        float lenSq = dx * dx + dy * dy;
        this.x1 = x1;
        this.y1 = y1;
        if (lenSq >= Float.MIN_VALUE) {
            dx /= lenSq;
            dy /= lenSq;
            if (this.repeat) {
                dx %= 1.0f;
                dy %= 1.0f;
            }
        }
        this.dx = dx;
        this.dy = dy;
        int[] pixels = new int[width];
        for (int y = 0; y < height; ++y) {
            this.getRGB(src, 0, y, width, 1, pixels);
            switch (this.type) {
                case 0: 
                case 1: {
                    this.linearGradient(pixels, y, width, 1);
                    break;
                }
                case 2: {
                    this.radialGradient(pixels, y, width, 1);
                    break;
                }
                case 3: 
                case 4: {
                    this.conicalGradient(pixels, y, width, 1);
                    break;
                }
                case 5: {
                    this.squareGradient(pixels, y, width, 1);
                }
            }
            this.setRGB(dst, 0, y, width, 1, pixels);
        }
        return dst;
    }

    private void repeatGradient(int[] pixels, int w, int h, float rowrel, float dx, float dy) {
        int off = 0;
        for (int y = 0; y < h; ++y) {
            float colrel = rowrel;
            int j = w;
            while (--j >= 0) {
                int rgb = this.type == 1 ? this.colormap.getColor(this.map(ImageMath.triangle(colrel))) : this.colormap.getColor(this.map(ImageMath.mod(colrel, 1.0f)));
                pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], this.paintMode);
                ++off;
                colrel += dx;
            }
            rowrel += dy;
        }
    }

    private void singleGradient(int[] pixels, int w, int h, float rowrel, float dx, float dy) {
        int off = 0;
        for (int y = 0; y < h; ++y) {
            int rgb;
            float colrel = rowrel;
            int j = w;
            if ((double)colrel <= 0.0) {
                rgb = this.colormap.getColor(0.0f);
                do {
                    pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], this.paintMode);
                    ++off;
                } while (--j > 0 && (double)(colrel += dx) <= 0.0);
            }
            while ((double)colrel < 1.0 && --j >= 0) {
                rgb = this.type == 1 ? this.colormap.getColor(this.map(ImageMath.triangle(colrel))) : this.colormap.getColor(this.map(colrel));
                pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], this.paintMode);
                ++off;
                colrel += dx;
            }
            if (j > 0) {
                rgb = this.type == 1 ? this.colormap.getColor(0.0f) : this.colormap.getColor(1.0f);
                do {
                    pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], this.paintMode);
                    ++off;
                } while (--j > 0);
            }
            rowrel += dy;
        }
    }

    private void linearGradient(int[] pixels, int y, int w, int h) {
        boolean x = false;
        float rowrel = ((float)x - this.x1) * this.dx + ((float)y - this.y1) * this.dy;
        if (this.repeat) {
            this.repeatGradient(pixels, w, h, rowrel, this.dx, this.dy);
        } else {
            this.singleGradient(pixels, w, h, rowrel, this.dx, this.dy);
        }
    }

    private void radialGradient(int[] pixels, int y, int w, int h) {
        int off = 0;
        float radius = this.distance(this.p2.x - this.p1.x, this.p2.y - this.p1.y);
        for (int x = 0; x < w; ++x) {
            float distance = this.distance(x - this.p1.x, y - this.p1.y);
            float ratio = distance / radius;
            if (this.repeat) {
                ratio %= 2.0f;
            } else if ((double)ratio > 1.0) {
                ratio = 1.0f;
            }
            int rgb = this.colormap.getColor(this.map(ratio));
            pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], this.paintMode);
            ++off;
        }
    }

    private void squareGradient(int[] pixels, int y, int w, int h) {
        int off = 0;
        float radius = Math.max(Math.abs(this.p2.x - this.p1.x), Math.abs(this.p2.y - this.p1.y));
        for (int x = 0; x < w; ++x) {
            float distance = Math.max(Math.abs(x - this.p1.x), Math.abs(y - this.p1.y));
            float ratio = distance / radius;
            if (this.repeat) {
                ratio %= 2.0f;
            } else if ((double)ratio > 1.0) {
                ratio = 1.0f;
            }
            int rgb = this.colormap.getColor(this.map(ratio));
            pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], this.paintMode);
            ++off;
        }
    }

    private void conicalGradient(int[] pixels, int y, int w, int h) {
        int off = 0;
        float angle0 = (float)Math.atan2(this.p2.x - this.p1.x, this.p2.y - this.p1.y);
        for (int x = 0; x < w; ++x) {
            float angle = (float)(Math.atan2(x - this.p1.x, y - this.p1.y) - (double)angle0) / ((float)Math.PI * 2);
            angle += 1.0f;
            angle %= 1.0f;
            if (this.type == 4) {
                angle = ImageMath.triangle(angle);
            }
            int rgb = this.colormap.getColor(this.map(angle));
            pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], this.paintMode);
            ++off;
        }
    }

    private float map(float v) {
        if (this.repeat) {
            v = (double)v > 1.0 ? 2.0f - v : v;
        }
        switch (this.interpolation) {
            case 1: {
                v = ImageMath.circleUp(ImageMath.clamp(v, 0.0f, 1.0f));
                break;
            }
            case 2: {
                v = ImageMath.circleDown(ImageMath.clamp(v, 0.0f, 1.0f));
                break;
            }
            case 3: {
                v = ImageMath.smoothStep(0.0f, 1.0f, v);
            }
        }
        return v;
    }

    private float distance(float a, float b) {
        return (float)Math.sqrt(a * a + b * b);
    }

    public String toString() {
        return "Other/Gradient Fill...";
    }
}

