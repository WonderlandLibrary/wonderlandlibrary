/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

import com.jhlabs.image.WholeImageFilter;
import com.jhlabs.math.Noise;
import java.awt.Rectangle;
import java.util.Random;

public class CausticsFilter
extends WholeImageFilter {
    private float scale = 32.0f;
    private float angle = 0.0f;
    private int brightness = 10;
    private float amount = 1.0f;
    private float turbulence = 1.0f;
    private float dispersion = 0.0f;
    private float time = 0.0f;
    private int samples = 2;
    private int bgColor = -8806401;
    private float s;
    private float c;

    public void setScale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return this.scale;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getBrightness() {
        return this.brightness;
    }

    public void setTurbulence(float turbulence) {
        this.turbulence = turbulence;
    }

    public float getTurbulence() {
        return this.turbulence;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setDispersion(float dispersion) {
        this.dispersion = dispersion;
    }

    public float getDispersion() {
        return this.dispersion;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public float getTime() {
        return this.time;
    }

    public void setSamples(int samples) {
        this.samples = samples;
    }

    public int getSamples() {
        return this.samples;
    }

    public void setBgColor(int c) {
        this.bgColor = c;
    }

    public int getBgColor() {
        return this.bgColor;
    }

    @Override
    protected int[] filterPixels(int width, int height, int[] inPixels, Rectangle transformedSpace) {
        Random random = new Random(0L);
        this.s = (float)Math.sin(0.1);
        this.c = (float)Math.cos(0.1);
        int srcWidth = this.originalSpace.width;
        int srcHeight = this.originalSpace.height;
        int outWidth = transformedSpace.width;
        int outHeight = transformedSpace.height;
        int index = 0;
        int[] pixels = new int[outWidth * outHeight];
        for (int y = 0; y < outHeight; ++y) {
            for (int x = 0; x < outWidth; ++x) {
                pixels[index++] = this.bgColor;
            }
        }
        int v = this.brightness / this.samples;
        if (v == 0) {
            v = 1;
        }
        float rs = 1.0f / this.scale;
        float d = 0.95f;
        index = 0;
        for (int y = 0; y < outHeight; ++y) {
            for (int x = 0; x < outWidth; ++x) {
                for (int s = 0; s < this.samples; ++s) {
                    float sx = (float)x + random.nextFloat();
                    float sy = (float)y + random.nextFloat();
                    float nx = sx * rs;
                    float ny = sy * rs;
                    float focus = 0.1f + this.amount;
                    float xDisplacement = this.evaluate(nx - d, ny) - this.evaluate(nx + d, ny);
                    float yDisplacement = this.evaluate(nx, ny + d) - this.evaluate(nx, ny - d);
                    if (this.dispersion > 0.0f) {
                        for (int c = 0; c < 3; ++c) {
                            float ca = 1.0f + (float)c * this.dispersion;
                            float srcX = sx + this.scale * focus * xDisplacement * ca;
                            float srcY = sy + this.scale * focus * yDisplacement * ca;
                            if (srcX < 0.0f || srcX >= (float)(outWidth - 1) || srcY < 0.0f || srcY >= (float)(outHeight - 1)) continue;
                            int i = (int)srcY * outWidth + (int)srcX;
                            int rgb = pixels[i];
                            int r = rgb >> 16 & 0xFF;
                            int g = rgb >> 8 & 0xFF;
                            int b = rgb & 0xFF;
                            if (c == 2) {
                                r += v;
                            } else if (c == 1) {
                                g += v;
                            } else {
                                b += v;
                            }
                            if (r > 255) {
                                r = 255;
                            }
                            if (g > 255) {
                                g = 255;
                            }
                            if (b > 255) {
                                b = 255;
                            }
                            pixels[i] = 0xFF000000 | r << 16 | g << 8 | b;
                        }
                        continue;
                    }
                    float srcX = sx + this.scale * focus * xDisplacement;
                    float srcY = sy + this.scale * focus * yDisplacement;
                    if (srcX < 0.0f || srcX >= (float)(outWidth - 1) || srcY < 0.0f || srcY >= (float)(outHeight - 1)) continue;
                    int i = (int)srcY * outWidth + (int)srcX;
                    int rgb = pixels[i];
                    int r = rgb >> 16 & 0xFF;
                    int g = rgb >> 8 & 0xFF;
                    int b = rgb & 0xFF;
                    g += v;
                    b += v;
                    if ((r += v) > 255) {
                        r = 255;
                    }
                    if (g > 255) {
                        g = 255;
                    }
                    if (b > 255) {
                        b = 255;
                    }
                    pixels[i] = 0xFF000000 | r << 16 | g << 8 | b;
                }
            }
        }
        return pixels;
    }

    private static int add(int rgb, float brightness) {
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        r = (int)((float)r + brightness);
        g = (int)((float)g + brightness);
        b = (int)((float)b + brightness);
        if (r > 255) {
            r = 255;
        }
        if (g > 255) {
            g = 255;
        }
        if (b > 255) {
            b = 255;
        }
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    private static int add(int rgb, float brightness, int c) {
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        if (c == 2) {
            r = (int)((float)r + brightness);
        } else if (c == 1) {
            g = (int)((float)g + brightness);
        } else {
            b = (int)((float)b + brightness);
        }
        if (r > 255) {
            r = 255;
        }
        if (g > 255) {
            g = 255;
        }
        if (b > 255) {
            b = 255;
        }
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    private static float turbulence2(float x, float y, float time, float octaves) {
        float value = 0.0f;
        float lacunarity = 2.0f;
        float f = 1.0f;
        x += 371.0f;
        y += 529.0f;
        for (int i = 0; i < (int)octaves; ++i) {
            value += Noise.noise3(x, y, time) / f;
            x *= lacunarity;
            y *= lacunarity;
            f *= 2.0f;
        }
        float remainder = octaves - (float)((int)octaves);
        if (remainder != 0.0f) {
            value += remainder * Noise.noise3(x, y, time) / f;
        }
        return value;
    }

    private float evaluate(float x, float y) {
        float xt = this.s * x + this.c * this.time;
        float tt = this.c * x - this.c * this.time;
        float f = (double)this.turbulence == 0.0 ? Noise.noise3(xt, y, tt) : CausticsFilter.turbulence2(xt, y, tt, this.turbulence);
        return f;
    }

    public String toString() {
        return "Texture/Caustics...";
    }
}

