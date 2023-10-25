/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.ConvolveFilter;
import com.jhlabs.image.GaussianFilter;
import com.jhlabs.image.ImageMath;
import com.jhlabs.image.WholeImageFilter;
import com.jhlabs.math.Function2D;
import com.jhlabs.math.ImageFunction2D;
import com.jhlabs.vecmath.Color4f;
import com.jhlabs.vecmath.Vector3f;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Kernel;

public class ShadeFilter
extends WholeImageFilter {
    public static final int COLORS_FROM_IMAGE = 0;
    public static final int COLORS_CONSTANT = 1;
    public static final int BUMPS_FROM_IMAGE = 0;
    public static final int BUMPS_FROM_IMAGE_ALPHA = 1;
    public static final int BUMPS_FROM_MAP = 2;
    public static final int BUMPS_FROM_BEVEL = 3;
    private float bumpHeight = 1.0f;
    private float bumpSoftness = 5.0f;
    private float viewDistance = 10000.0f;
    private int colorSource = 0;
    private int bumpSource = 0;
    private Function2D bumpFunction;
    private BufferedImage environmentMap;
    private int[] envPixels;
    private int envWidth = 1;
    private int envHeight = 1;
    private Vector3f l = new Vector3f();
    private Vector3f v = new Vector3f();
    private Vector3f n = new Vector3f();
    private Color4f shadedColor = new Color4f();
    private Color4f diffuse_color = new Color4f();
    private Color4f specular_color = new Color4f();
    private Vector3f tmpv = new Vector3f();
    private Vector3f tmpv2 = new Vector3f();
    protected static final float r255 = 0.003921569f;

    public void setBumpFunction(Function2D bumpFunction) {
        this.bumpFunction = bumpFunction;
    }

    public Function2D getBumpFunction() {
        return this.bumpFunction;
    }

    public void setBumpHeight(float bumpHeight) {
        this.bumpHeight = bumpHeight;
    }

    public float getBumpHeight() {
        return this.bumpHeight;
    }

    public void setBumpSoftness(float bumpSoftness) {
        this.bumpSoftness = bumpSoftness;
    }

    public float getBumpSoftness() {
        return this.bumpSoftness;
    }

    public void setEnvironmentMap(BufferedImage environmentMap) {
        this.environmentMap = environmentMap;
        if (environmentMap != null) {
            this.envWidth = environmentMap.getWidth();
            this.envHeight = environmentMap.getHeight();
            this.envPixels = this.getRGB(environmentMap, 0, 0, this.envWidth, this.envHeight, null);
        } else {
            this.envHeight = 1;
            this.envWidth = 1;
            this.envPixels = null;
        }
    }

    public BufferedImage getEnvironmentMap() {
        return this.environmentMap;
    }

    public void setBumpSource(int bumpSource) {
        this.bumpSource = bumpSource;
    }

    public int getBumpSource() {
        return this.bumpSource;
    }

    protected void setFromRGB(Color4f c, int argb) {
        c.set((float)(argb >> 16 & 0xFF) * 0.003921569f, (float)(argb >> 8 & 0xFF) * 0.003921569f, (float)(argb & 0xFF) * 0.003921569f, (float)(argb >> 24 & 0xFF) * 0.003921569f);
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        int index = 0;
        int[] outPixels = new int[width * height];
        float width45 = Math.abs(6.0f * this.bumpHeight);
        boolean invertBumps = this.bumpHeight < 0.0f;
        Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
        Vector3f viewpoint = new Vector3f((float)width / 2.0f, (float)height / 2.0f, this.viewDistance);
        Vector3f normal = new Vector3f();
        Color4f c = new Color4f();
        Function2D bump = this.bumpFunction;
        if (this.bumpSource == 0 || this.bumpSource == 1 || this.bumpSource == 2 || bump == null) {
            if (this.bumpSoftness != 0.0f) {
                int bumpWidth = width;
                int bumpHeight = height;
                int[] bumpPixels = inPixels;
                if (this.bumpSource == 2 && this.bumpFunction instanceof ImageFunction2D) {
                    ImageFunction2D if2d = (ImageFunction2D)this.bumpFunction;
                    bumpWidth = if2d.getWidth();
                    bumpHeight = if2d.getHeight();
                    bumpPixels = if2d.getPixels();
                }
                Kernel kernel = GaussianFilter.makeKernel(this.bumpSoftness);
                int[] tmpPixels = new int[bumpWidth * bumpHeight];
                int[] softPixels = new int[bumpWidth * bumpHeight];
                GaussianFilter.convolveAndTranspose(kernel, bumpPixels, tmpPixels, bumpWidth, bumpHeight, true, false, false, ConvolveFilter.CLAMP_EDGES);
                GaussianFilter.convolveAndTranspose(kernel, tmpPixels, softPixels, bumpHeight, bumpWidth, true, false, false, ConvolveFilter.CLAMP_EDGES);
                bump = new ImageFunction2D(softPixels, bumpWidth, bumpHeight, 1, this.bumpSource == 1);
            } else {
                bump = new ImageFunction2D(inPixels, width, height, 1, this.bumpSource == 1);
            }
        }
        Vector3f v1 = new Vector3f();
        Vector3f v2 = new Vector3f();
        Vector3f n = new Vector3f();
        for (int y = 0; y < height; ++y) {
            float ny = y;
            position.y = y;
            for (int x = 0; x < width; ++x) {
                float nx = x;
                if (this.bumpSource != 3) {
                    float m4;
                    int count = 0;
                    normal.z = 0.0f;
                    normal.y = 0.0f;
                    normal.x = 0.0f;
                    float m0 = width45 * bump.evaluate(nx, ny);
                    float m1 = x > 0 ? width45 * bump.evaluate(nx - 1.0f, ny) - m0 : -2.0f;
                    float m2 = y > 0 ? width45 * bump.evaluate(nx, ny - 1.0f) - m0 : -2.0f;
                    float m3 = x < width - 1 ? width45 * bump.evaluate(nx + 1.0f, ny) - m0 : -2.0f;
                    float f = m4 = y < height - 1 ? width45 * bump.evaluate(nx, ny + 1.0f) - m0 : -2.0f;
                    if (m1 != -2.0f && m4 != -2.0f) {
                        v1.x = -1.0f;
                        v1.y = 0.0f;
                        v1.z = m1;
                        v2.x = 0.0f;
                        v2.y = 1.0f;
                        v2.z = m4;
                        n.cross(v1, v2);
                        n.normalize();
                        if ((double)n.z < 0.0) {
                            n.z = -n.z;
                        }
                        normal.add(n);
                        ++count;
                    }
                    if (m1 != -2.0f && m2 != -2.0f) {
                        v1.x = -1.0f;
                        v1.y = 0.0f;
                        v1.z = m1;
                        v2.x = 0.0f;
                        v2.y = -1.0f;
                        v2.z = m2;
                        n.cross(v1, v2);
                        n.normalize();
                        if ((double)n.z < 0.0) {
                            n.z = -n.z;
                        }
                        normal.add(n);
                        ++count;
                    }
                    if (m2 != -2.0f && m3 != -2.0f) {
                        v1.x = 0.0f;
                        v1.y = -1.0f;
                        v1.z = m2;
                        v2.x = 1.0f;
                        v2.y = 0.0f;
                        v2.z = m3;
                        n.cross(v1, v2);
                        n.normalize();
                        if ((double)n.z < 0.0) {
                            n.z = -n.z;
                        }
                        normal.add(n);
                        ++count;
                    }
                    if (m3 != -2.0f && m4 != -2.0f) {
                        v1.x = 1.0f;
                        v1.y = 0.0f;
                        v1.z = m3;
                        v2.x = 0.0f;
                        v2.y = 1.0f;
                        v2.z = m4;
                        n.cross(v1, v2);
                        n.normalize();
                        if ((double)n.z < 0.0) {
                            n.z = -n.z;
                        }
                        normal.add(n);
                        ++count;
                    }
                    normal.x /= (float)count;
                    normal.y /= (float)count;
                    normal.z /= (float)count;
                }
                if (invertBumps) {
                    normal.x = -normal.x;
                    normal.y = -normal.y;
                }
                position.x = x;
                if (normal.z >= 0.0f) {
                    if (this.environmentMap != null) {
                        this.tmpv2.set(viewpoint);
                        this.tmpv2.sub(position);
                        this.tmpv2.normalize();
                        this.tmpv.set(normal);
                        this.tmpv.normalize();
                        this.tmpv.scale(2.0f * this.tmpv.dot(this.tmpv2));
                        this.tmpv.sub(this.v);
                        this.tmpv.normalize();
                        this.setFromRGB(c, this.getEnvironmentMapP(normal, inPixels, width, height));
                        int alpha = inPixels[index] & 0xFF000000;
                        int rgb = (int)(c.x * 255.0f) << 16 | (int)(c.y * 255.0f) << 8 | (int)(c.z * 255.0f);
                        outPixels[index++] = alpha | rgb;
                        continue;
                    }
                    outPixels[index++] = 0;
                    continue;
                }
                outPixels[index++] = 0;
            }
        }
        return outPixels;
    }

    private int getEnvironmentMapP(Vector3f normal, int[] inPixels, int width, int height) {
        if (this.environmentMap != null) {
            float x = 0.5f * (1.0f + normal.x);
            float y = 0.5f * (1.0f + normal.y);
            x = ImageMath.clamp(x * (float)this.envWidth, 0.0f, (float)(this.envWidth - 1));
            y = ImageMath.clamp(y * (float)this.envHeight, 0.0f, (float)(this.envHeight - 1));
            int ix = (int)x;
            int iy = (int)y;
            float xWeight = x - (float)ix;
            float yWeight = y - (float)iy;
            int i = this.envWidth * iy + ix;
            int dx = ix == this.envWidth - 1 ? 0 : 1;
            int dy = iy == this.envHeight - 1 ? 0 : this.envWidth;
            return ImageMath.bilinearInterpolate(xWeight, yWeight, this.envPixels[i], this.envPixels[i + dx], this.envPixels[i + dy], this.envPixels[i + dx + dy]);
        }
        return 0;
    }

    public String toString() {
        return "Stylize/Shade...";
    }
}

