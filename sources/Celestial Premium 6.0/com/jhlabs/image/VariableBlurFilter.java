/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.ImageMath;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class VariableBlurFilter
extends AbstractBufferedImageOp {
    private int hRadius = 1;
    private int vRadius = 1;
    private int iterations = 1;
    private BufferedImage blurMask;
    private boolean premultiplyAlpha = true;

    public void setPremultiplyAlpha(boolean premultiplyAlpha) {
        this.premultiplyAlpha = premultiplyAlpha;
    }

    public boolean getPremultiplyAlpha() {
        return this.premultiplyAlpha;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        if (dst == null) {
            dst = new BufferedImage(width, height, 2);
        }
        int[] inPixels = new int[width * height];
        int[] outPixels = new int[width * height];
        this.getRGB(src, 0, 0, width, height, inPixels);
        if (this.premultiplyAlpha) {
            ImageMath.premultiply(inPixels, 0, inPixels.length);
        }
        for (int i = 0; i < this.iterations; ++i) {
            this.blur(inPixels, outPixels, width, height, this.hRadius, 1);
            this.blur(outPixels, inPixels, height, width, this.vRadius, 2);
        }
        if (this.premultiplyAlpha) {
            ImageMath.unpremultiply(inPixels, 0, inPixels.length);
        }
        this.setRGB(dst, 0, 0, width, height, inPixels);
        return dst;
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

    public void blur(int[] in, int[] out, int width, int height, int radius, int pass) {
        int widthMinus1 = width - 1;
        int[] r = new int[width];
        int[] g = new int[width];
        int[] b = new int[width];
        int[] a = new int[width];
        int[] mask = new int[width];
        int inIndex = 0;
        for (int y = 0; y < height; ++y) {
            int x;
            int outIndex = y;
            if (this.blurMask != null) {
                if (pass == 1) {
                    this.getRGB(this.blurMask, 0, y, width, 1, mask);
                } else {
                    this.getRGB(this.blurMask, y, 0, 1, width, mask);
                }
            }
            for (x = 0; x < width; ++x) {
                int argb = in[inIndex + x];
                a[x] = argb >> 24 & 0xFF;
                r[x] = argb >> 16 & 0xFF;
                g[x] = argb >> 8 & 0xFF;
                b[x] = argb & 0xFF;
                if (x == 0) continue;
                int n = x;
                a[n] = a[n] + a[x - 1];
                int n2 = x;
                r[n2] = r[n2] + r[x - 1];
                int n3 = x;
                g[n3] = g[n3] + g[x - 1];
                int n4 = x;
                b[n4] = b[n4] + b[x - 1];
            }
            for (x = 0; x < width; ++x) {
                int i2;
                int ra = this.blurMask != null ? (pass == 1 ? (int)((float)((mask[x] & 0xFF) * this.hRadius) / 255.0f) : (int)((float)((mask[x] & 0xFF) * this.vRadius) / 255.0f)) : (pass == 1 ? (int)(this.blurRadiusAt(x, y, width, height) * (float)this.hRadius) : (int)(this.blurRadiusAt(y, x, height, width) * (float)this.vRadius));
                int divisor = 2 * ra + 1;
                int ta = 0;
                int tr = 0;
                int tg = 0;
                int tb = 0;
                int i1 = x + ra;
                if (i1 > widthMinus1) {
                    int f = i1 - widthMinus1;
                    int l = widthMinus1;
                    ta += (a[l] - a[l - 1]) * f;
                    tr += (r[l] - r[l - 1]) * f;
                    tg += (g[l] - g[l - 1]) * f;
                    tb += (b[l] - b[l - 1]) * f;
                    i1 = widthMinus1;
                }
                if ((i2 = x - ra - 1) < 0) {
                    ta -= a[0] * i2;
                    tr -= r[0] * i2;
                    tg -= g[0] * i2;
                    tb -= b[0] * i2;
                    i2 = 0;
                }
                out[outIndex] = (ta += a[i1] - a[i2]) / divisor << 24 | (tr += r[i1] - r[i2]) / divisor << 16 | (tg += g[i1] - g[i2]) / divisor << 8 | (tb += b[i1] - b[i2]) / divisor;
                outIndex += height;
            }
            inIndex += width;
        }
    }

    protected float blurRadiusAt(int x, int y, int width, int height) {
        return (float)x / (float)width;
    }

    public void setHRadius(int hRadius) {
        this.hRadius = hRadius;
    }

    public int getHRadius() {
        return this.hRadius;
    }

    public void setVRadius(int vRadius) {
        this.vRadius = vRadius;
    }

    public int getVRadius() {
        return this.vRadius;
    }

    public void setRadius(int radius) {
        this.hRadius = this.vRadius = radius;
    }

    public int getRadius() {
        return this.hRadius;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getIterations() {
        return this.iterations;
    }

    public void setBlurMask(BufferedImage blurMask) {
        this.blurMask = blurMask;
    }

    public BufferedImage getBlurMask() {
        return this.blurMask;
    }

    public String toString() {
        return "Blur/Variable Blur...";
    }
}

