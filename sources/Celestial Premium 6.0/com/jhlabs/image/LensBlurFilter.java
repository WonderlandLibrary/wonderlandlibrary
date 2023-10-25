/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.ImageMath;
import com.jhlabs.math.FFT;
import java.awt.image.BufferedImage;

public class LensBlurFilter
extends AbstractBufferedImageOp {
    private float radius = 10.0f;
    private float bloom = 2.0f;
    private float bloomThreshold = 255.0f;
    private float angle = 0.0f;
    private int sides = 5;

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getRadius() {
        return this.radius;
    }

    public void setSides(int sides) {
        this.sides = sides;
    }

    public int getSides() {
        return this.sides;
    }

    public void setBloom(float bloom) {
        this.bloom = bloom;
    }

    public float getBloom() {
        return this.bloom;
    }

    public void setBloomThreshold(float bloomThreshold) {
        this.bloomThreshold = bloomThreshold;
    }

    public float getBloomThreshold() {
        return this.bloomThreshold;
    }

    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dst) {
        int x;
        int y;
        int tileWidth;
        int width = src.getWidth();
        int height = src.getHeight();
        int rows = 1;
        int cols = 1;
        int log2rows = 0;
        int log2cols = 0;
        int iradius = (int)Math.ceil(this.radius);
        int tileHeight = tileWidth = 128;
        int adjustedWidth = width + iradius * 2;
        int adjustedHeight = height + iradius * 2;
        tileWidth = iradius < 32 ? Math.min(128, width + 2 * iradius) : Math.min(256, width + 2 * iradius);
        int n = tileHeight = iradius < 32 ? Math.min(128, height + 2 * iradius) : Math.min(256, height + 2 * iradius);
        if (dst == null) {
            dst = new BufferedImage(width, height, 2);
        }
        while (rows < tileHeight) {
            rows *= 2;
            ++log2rows;
        }
        while (cols < tileWidth) {
            cols *= 2;
            ++log2cols;
        }
        int w = cols;
        int h = rows;
        tileWidth = w;
        tileHeight = h;
        FFT fft = new FFT(Math.max(log2rows, log2cols));
        int[] rgb = new int[w * h];
        float[][] mask = new float[2][w * h];
        float[][] gb = new float[2][w * h];
        float[][] ar = new float[2][w * h];
        double polyAngle = Math.PI / (double)this.sides;
        double polyScale = 1.0 / Math.cos(polyAngle);
        double r2 = this.radius * this.radius;
        double rangle = Math.toRadians(this.angle);
        float total = 0.0f;
        int i = 0;
        for (y = 0; y < h; ++y) {
            for (x = 0; x < w; ++x) {
                double dx = (float)x - (float)w / 2.0f;
                double dy = (float)y - (float)h / 2.0f;
                double r = dx * dx + dy * dy;
                double f = r < r2 ? 1 : 0;
                if (f != 0.0) {
                    r = Math.sqrt(r);
                    if (this.sides != 0) {
                        double a = Math.atan2(dy, dx) + rangle;
                        a = ImageMath.mod(a, polyAngle * 2.0) - polyAngle;
                        f = Math.cos(a) * polyScale;
                    } else {
                        f = 1.0;
                    }
                    f = f * r < (double)this.radius ? 1 : 0;
                }
                total += (float)f;
                mask[0][i] = (float)f;
                mask[1][i] = 0.0f;
                ++i;
            }
        }
        i = 0;
        for (y = 0; y < h; ++y) {
            for (x = 0; x < w; ++x) {
                float[] arrf = mask[0];
                int n2 = i++;
                arrf[n2] = arrf[n2] / total;
            }
        }
        fft.transform2D(mask[0], mask[1], w, h, true);
        for (int tileY = -iradius; tileY < height; tileY += tileHeight - 2 * iradius) {
            for (int tileX = -iradius; tileX < width; tileX += tileWidth - 2 * iradius) {
                int y2;
                int tx = tileX;
                int ty = tileY;
                int tw = tileWidth;
                int th = tileHeight;
                int fx = 0;
                int fy = 0;
                if (tx < 0) {
                    tw += tx;
                    fx -= tx;
                    tx = 0;
                }
                if (ty < 0) {
                    th += ty;
                    fy -= ty;
                    ty = 0;
                }
                if (tx + tw > width) {
                    tw = width - tx;
                }
                if (ty + th > height) {
                    th = height - ty;
                }
                src.getRGB(tx, ty, tw, th, rgb, fy * w + fx, w);
                i = 0;
                for (y2 = 0; y2 < h; ++y2) {
                    int imageY = y2 + tileY;
                    int j = imageY < 0 ? fy : (imageY > height ? fy + th - 1 : y2);
                    j *= w;
                    for (int x2 = 0; x2 < w; ++x2) {
                        int imageX = x2 + tileX;
                        int k = imageX < 0 ? fx : (imageX > width ? fx + tw - 1 : x2);
                        ar[0][i] = rgb[k += j] >> 24 & 0xFF;
                        float r = rgb[k] >> 16 & 0xFF;
                        float g = rgb[k] >> 8 & 0xFF;
                        float b = rgb[k] & 0xFF;
                        if (r > this.bloomThreshold) {
                            r *= this.bloom;
                        }
                        if (g > this.bloomThreshold) {
                            g *= this.bloom;
                        }
                        if (b > this.bloomThreshold) {
                            b *= this.bloom;
                        }
                        ar[1][i] = r;
                        gb[0][i] = g;
                        gb[1][i] = b;
                        ++i;
                        ++k;
                    }
                }
                fft.transform2D(ar[0], ar[1], cols, rows, true);
                fft.transform2D(gb[0], gb[1], cols, rows, true);
                i = 0;
                for (y2 = 0; y2 < h; ++y2) {
                    for (int x3 = 0; x3 < w; ++x3) {
                        float re = ar[0][i];
                        float im = ar[1][i];
                        float rem = mask[0][i];
                        float imm = mask[1][i];
                        ar[0][i] = re * rem - im * imm;
                        ar[1][i] = re * imm + im * rem;
                        re = gb[0][i];
                        im = gb[1][i];
                        gb[0][i] = re * rem - im * imm;
                        gb[1][i] = re * imm + im * rem;
                        ++i;
                    }
                }
                fft.transform2D(ar[0], ar[1], cols, rows, false);
                fft.transform2D(gb[0], gb[1], cols, rows, false);
                int row_flip = w >> 1;
                int col_flip = h >> 1;
                int index = 0;
                for (int y3 = 0; y3 < w; ++y3) {
                    int ym = y3 ^ row_flip;
                    int yi = ym * cols;
                    for (int x4 = 0; x4 < w; ++x4) {
                        int xm = yi + (x4 ^ col_flip);
                        int a = (int)ar[0][xm];
                        int r = (int)ar[1][xm];
                        int g = (int)gb[0][xm];
                        int b = (int)gb[1][xm];
                        if (r > 255) {
                            r = 255;
                        }
                        if (g > 255) {
                            g = 255;
                        }
                        if (b > 255) {
                            b = 255;
                        }
                        int argb = a << 24 | r << 16 | g << 8 | b;
                        rgb[index++] = argb;
                    }
                }
                tx = tileX + iradius;
                ty = tileY + iradius;
                tw = tileWidth - 2 * iradius;
                th = tileHeight - 2 * iradius;
                if (tx + tw > width) {
                    tw = width - tx;
                }
                if (ty + th > height) {
                    th = height - ty;
                }
                dst.setRGB(tx, ty, tw, th, rgb, iradius * w + iradius, w);
            }
        }
        return dst;
    }

    public String toString() {
        return "Blur/Lens Blur...";
    }
}

