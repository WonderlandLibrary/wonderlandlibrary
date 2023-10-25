/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.image;

public class ImageMath {
    public static final float PI = (float)Math.PI;
    public static final float HALF_PI = 1.5707964f;
    public static final float QUARTER_PI = 0.7853982f;
    public static final float TWO_PI = (float)Math.PI * 2;
    private static final float m00 = -0.5f;
    private static final float m01 = 1.5f;
    private static final float m02 = -1.5f;
    private static final float m03 = 0.5f;
    private static final float m10 = 1.0f;
    private static final float m11 = -2.5f;
    private static final float m12 = 2.0f;
    private static final float m13 = -0.5f;
    private static final float m20 = -0.5f;
    private static final float m21 = 0.0f;
    private static final float m22 = 0.5f;
    private static final float m23 = 0.0f;
    private static final float m30 = 0.0f;
    private static final float m31 = 1.0f;
    private static final float m32 = 0.0f;
    private static final float m33 = 0.0f;

    public static float bias(float a, float b) {
        return a / ((1.0f / b - 2.0f) * (1.0f - a) + 1.0f);
    }

    public static float gain(float a, float b) {
        float c = (1.0f / b - 2.0f) * (1.0f - 2.0f * a);
        if ((double)a < 0.5) {
            return a / (c + 1.0f);
        }
        return (c - a) / (c - 1.0f);
    }

    public static float step(float a, float x) {
        return x < a ? 0.0f : 1.0f;
    }

    public static float pulse(float a, float b, float x) {
        return x < a || x >= b ? 0.0f : 1.0f;
    }

    public static float smoothPulse(float a1, float a2, float b1, float b2, float x) {
        if (x < a1 || x >= b2) {
            return 0.0f;
        }
        if (x >= a2) {
            if (x < b1) {
                return 1.0f;
            }
            x = (x - b1) / (b2 - b1);
            return 1.0f - x * x * (3.0f - 2.0f * x);
        }
        x = (x - a1) / (a2 - a1);
        return x * x * (3.0f - 2.0f * x);
    }

    public static float smoothStep(float a, float b, float x) {
        if (x < a) {
            return 0.0f;
        }
        if (x >= b) {
            return 1.0f;
        }
        x = (x - a) / (b - a);
        return x * x * (3.0f - 2.0f * x);
    }

    public static float circleUp(float x) {
        x = 1.0f - x;
        return (float)Math.sqrt(1.0f - x * x);
    }

    public static float circleDown(float x) {
        return 1.0f - (float)Math.sqrt(1.0f - x * x);
    }

    public static float clamp(float x, float a, float b) {
        return x < a ? a : (x > b ? b : x);
    }

    public static int clamp(int x, int a, int b) {
        return x < a ? a : (x > b ? b : x);
    }

    public static double mod(double a, double b) {
        int n;
        if ((a -= (double)(n = (int)(a / b)) * b) < 0.0) {
            return a + b;
        }
        return a;
    }

    public static float mod(float a, float b) {
        int n;
        if ((a -= (float)(n = (int)(a / b)) * b) < 0.0f) {
            return a + b;
        }
        return a;
    }

    public static int mod(int a, int b) {
        int n;
        if ((a -= (n = a / b) * b) < 0) {
            return a + b;
        }
        return a;
    }

    public static float triangle(float x) {
        float r = ImageMath.mod(x, 1.0f);
        return 2.0f * ((double)r < 0.5 ? r : 1.0f - r);
    }

    public static float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    public static int lerp(float t, int a, int b) {
        return (int)((float)a + t * (float)(b - a));
    }

    public static int mixColors(float t, int rgb1, int rgb2) {
        int a1 = rgb1 >> 24 & 0xFF;
        int r1 = rgb1 >> 16 & 0xFF;
        int g1 = rgb1 >> 8 & 0xFF;
        int b1 = rgb1 & 0xFF;
        int a2 = rgb2 >> 24 & 0xFF;
        int r2 = rgb2 >> 16 & 0xFF;
        int g2 = rgb2 >> 8 & 0xFF;
        int b2 = rgb2 & 0xFF;
        a1 = ImageMath.lerp(t, a1, a2);
        r1 = ImageMath.lerp(t, r1, r2);
        g1 = ImageMath.lerp(t, g1, g2);
        b1 = ImageMath.lerp(t, b1, b2);
        return a1 << 24 | r1 << 16 | g1 << 8 | b1;
    }

    public static int bilinearInterpolate(float x, float y, int nw, int ne, int sw, int se) {
        int a0 = nw >> 24 & 0xFF;
        int r0 = nw >> 16 & 0xFF;
        int g0 = nw >> 8 & 0xFF;
        int b0 = nw & 0xFF;
        int a1 = ne >> 24 & 0xFF;
        int r1 = ne >> 16 & 0xFF;
        int g1 = ne >> 8 & 0xFF;
        int b1 = ne & 0xFF;
        int a2 = sw >> 24 & 0xFF;
        int r2 = sw >> 16 & 0xFF;
        int g2 = sw >> 8 & 0xFF;
        int b2 = sw & 0xFF;
        int a3 = se >> 24 & 0xFF;
        int r3 = se >> 16 & 0xFF;
        int g3 = se >> 8 & 0xFF;
        int b3 = se & 0xFF;
        float cx = 1.0f - x;
        float cy = 1.0f - y;
        float m0 = cx * (float)a0 + x * (float)a1;
        float m1 = cx * (float)a2 + x * (float)a3;
        int a = (int)(cy * m0 + y * m1);
        m0 = cx * (float)r0 + x * (float)r1;
        m1 = cx * (float)r2 + x * (float)r3;
        int r = (int)(cy * m0 + y * m1);
        m0 = cx * (float)g0 + x * (float)g1;
        m1 = cx * (float)g2 + x * (float)g3;
        int g = (int)(cy * m0 + y * m1);
        m0 = cx * (float)b0 + x * (float)b1;
        m1 = cx * (float)b2 + x * (float)b3;
        int b = (int)(cy * m0 + y * m1);
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int brightnessNTSC(int rgb) {
        int r = rgb >> 16 & 0xFF;
        int g = rgb >> 8 & 0xFF;
        int b = rgb & 0xFF;
        return (int)((float)r * 0.299f + (float)g * 0.587f + (float)b * 0.114f);
    }

    public static float spline(float x, int numKnots, float[] knots) {
        int numSpans = numKnots - 3;
        if (numSpans < 1) {
            throw new IllegalArgumentException("Too few knots in spline");
        }
        int span = (int)(x = ImageMath.clamp(x, 0.0f, 1.0f) * (float)numSpans);
        if (span > numKnots - 4) {
            span = numKnots - 4;
        }
        float k0 = knots[span];
        float k1 = knots[span + 1];
        float k2 = knots[span + 2];
        float k3 = knots[span + 3];
        float c3 = -0.5f * k0 + 1.5f * k1 + -1.5f * k2 + 0.5f * k3;
        float c2 = 1.0f * k0 + -2.5f * k1 + 2.0f * k2 + -0.5f * k3;
        float c1 = -0.5f * k0 + 0.0f * k1 + 0.5f * k2 + 0.0f * k3;
        float c0 = 0.0f * k0 + 1.0f * k1 + 0.0f * k2 + 0.0f * k3;
        return ((c3 * (x -= (float)span) + c2) * x + c1) * x + c0;
    }

    public static float spline(float x, int numKnots, int[] xknots, int[] yknots) {
        int span;
        int numSpans = numKnots - 3;
        if (numSpans < 1) {
            throw new IllegalArgumentException("Too few knots in spline");
        }
        for (span = 0; span < numSpans; ++span) {
            if ((float)xknots[span + 1] > x) break;
        }
        if (span > numKnots - 3) {
            span = numKnots - 3;
        }
        float t = (x - (float)xknots[span]) / (float)(xknots[span + 1] - xknots[span]);
        if (--span < 0) {
            span = 0;
            t = 0.0f;
        }
        float k0 = yknots[span];
        float k1 = yknots[span + 1];
        float k2 = yknots[span + 2];
        float k3 = yknots[span + 3];
        float c3 = -0.5f * k0 + 1.5f * k1 + -1.5f * k2 + 0.5f * k3;
        float c2 = 1.0f * k0 + -2.5f * k1 + 2.0f * k2 + -0.5f * k3;
        float c1 = -0.5f * k0 + 0.0f * k1 + 0.5f * k2 + 0.0f * k3;
        float c0 = 0.0f * k0 + 1.0f * k1 + 0.0f * k2 + 0.0f * k3;
        return ((c3 * t + c2) * t + c1) * t + c0;
    }

    public static int colorSpline(float x, int numKnots, int[] knots) {
        int numSpans = numKnots - 3;
        if (numSpans < 1) {
            throw new IllegalArgumentException("Too few knots in spline");
        }
        int span = (int)(x = ImageMath.clamp(x, 0.0f, 1.0f) * (float)numSpans);
        if (span > numKnots - 4) {
            span = numKnots - 4;
        }
        x -= (float)span;
        int v = 0;
        for (int i = 0; i < 4; ++i) {
            int shift = i * 8;
            float k0 = knots[span] >> shift & 0xFF;
            float k1 = knots[span + 1] >> shift & 0xFF;
            float k2 = knots[span + 2] >> shift & 0xFF;
            float k3 = knots[span + 3] >> shift & 0xFF;
            float c3 = -0.5f * k0 + 1.5f * k1 + -1.5f * k2 + 0.5f * k3;
            float c2 = 1.0f * k0 + -2.5f * k1 + 2.0f * k2 + -0.5f * k3;
            float c1 = -0.5f * k0 + 0.0f * k1 + 0.5f * k2 + 0.0f * k3;
            float c0 = 0.0f * k0 + 1.0f * k1 + 0.0f * k2 + 0.0f * k3;
            int n = (int)(((c3 * x + c2) * x + c1) * x + c0);
            if (n < 0) {
                n = 0;
            } else if (n > 255) {
                n = 255;
            }
            v |= n << shift;
        }
        return v;
    }

    public static int colorSpline(int x, int numKnots, int[] xknots, int[] yknots) {
        int span;
        int numSpans = numKnots - 3;
        if (numSpans < 1) {
            throw new IllegalArgumentException("Too few knots in spline");
        }
        for (span = 0; span < numSpans; ++span) {
            if (xknots[span + 1] > x) break;
        }
        if (span > numKnots - 3) {
            span = numKnots - 3;
        }
        float t = (float)(x - xknots[span]) / (float)(xknots[span + 1] - xknots[span]);
        if (--span < 0) {
            span = 0;
            t = 0.0f;
        }
        int v = 0;
        for (int i = 0; i < 4; ++i) {
            int shift = i * 8;
            float k0 = yknots[span] >> shift & 0xFF;
            float k1 = yknots[span + 1] >> shift & 0xFF;
            float k2 = yknots[span + 2] >> shift & 0xFF;
            float k3 = yknots[span + 3] >> shift & 0xFF;
            float c3 = -0.5f * k0 + 1.5f * k1 + -1.5f * k2 + 0.5f * k3;
            float c2 = 1.0f * k0 + -2.5f * k1 + 2.0f * k2 + -0.5f * k3;
            float c1 = -0.5f * k0 + 0.0f * k1 + 0.5f * k2 + 0.0f * k3;
            float c0 = 0.0f * k0 + 1.0f * k1 + 0.0f * k2 + 0.0f * k3;
            int n = (int)(((c3 * t + c2) * t + c1) * t + c0);
            if (n < 0) {
                n = 0;
            } else if (n > 255) {
                n = 255;
            }
            v |= n << shift;
        }
        return v;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static void resample(int[] source, int[] dest, int length, int offset, int stride, float[] out) {
        srcIndex = offset;
        destIndex = offset;
        lastIndex = source.length;
        in = new float[length + 2];
        i = 0;
        j = 0;
        ** GOTO lbl13
        {
            ++i;
            do {
                if (out[i + 1] < (float)j) continue block0;
                in[j] = (float)i + ((float)j - out[i]) / (out[i + 1] - out[i]);
                ++j;
lbl13:
                // 2 sources

            } while (j < length);
        }
        in[length] = length;
        in[length + 1] = length;
        inSegment = 1.0f;
        sizfac = outSegment = in[1];
        bSum = 0.0f;
        gSum = 0.0f;
        rSum = 0.0f;
        aSum = 0.0f;
        rgb = source[srcIndex];
        a = rgb >> 24 & 255;
        r = rgb >> 16 & 255;
        g = rgb >> 8 & 255;
        b = rgb & 255;
        rgb = source[srcIndex += stride];
        nextA = rgb >> 24 & 255;
        nextR = rgb >> 16 & 255;
        nextG = rgb >> 8 & 255;
        nextB = rgb & 255;
        srcIndex += stride;
        i = 1;
        while (i <= length) {
            aIntensity = inSegment * (float)a + (1.0f - inSegment) * (float)nextA;
            rIntensity = inSegment * (float)r + (1.0f - inSegment) * (float)nextR;
            gIntensity = inSegment * (float)g + (1.0f - inSegment) * (float)nextG;
            bIntensity = inSegment * (float)b + (1.0f - inSegment) * (float)nextB;
            if (inSegment < outSegment) {
                aSum += aIntensity * inSegment;
                rSum += rIntensity * inSegment;
                gSum += gIntensity * inSegment;
                bSum += bIntensity * inSegment;
                outSegment -= inSegment;
                inSegment = 1.0f;
                a = nextA;
                r = nextR;
                g = nextG;
                b = nextB;
                if (srcIndex < lastIndex) {
                    rgb = source[srcIndex];
                }
                nextA = rgb >> 24 & 255;
                nextR = rgb >> 16 & 255;
                nextG = rgb >> 8 & 255;
                nextB = rgb & 255;
                srcIndex += stride;
                continue;
            }
            dest[destIndex] = (int)Math.min((aSum += aIntensity * outSegment) / sizfac, 255.0f) << 24 | (int)Math.min((rSum += rIntensity * outSegment) / sizfac, 255.0f) << 16 | (int)Math.min((gSum += gIntensity * outSegment) / sizfac, 255.0f) << 8 | (int)Math.min((bSum += bIntensity * outSegment) / sizfac, 255.0f);
            destIndex += stride;
            bSum = 0.0f;
            gSum = 0.0f;
            rSum = 0.0f;
            aSum = 0.0f;
            inSegment -= outSegment;
            sizfac = outSegment = in[i + 1] - in[i];
            ++i;
        }
    }

    public static void premultiply(int[] p, int offset, int length) {
        for (int i = offset; i < (length += offset); ++i) {
            int rgb = p[i];
            int a = rgb >> 24 & 0xFF;
            int r = rgb >> 16 & 0xFF;
            int g = rgb >> 8 & 0xFF;
            int b = rgb & 0xFF;
            float f = (float)a * 0.003921569f;
            r = (int)((float)r * f);
            g = (int)((float)g * f);
            b = (int)((float)b * f);
            p[i] = a << 24 | r << 16 | g << 8 | b;
        }
    }

    public static void unpremultiply(int[] p, int offset, int length) {
        for (int i = offset; i < (length += offset); ++i) {
            int rgb = p[i];
            int a = rgb >> 24 & 0xFF;
            int r = rgb >> 16 & 0xFF;
            int g = rgb >> 8 & 0xFF;
            int b = rgb & 0xFF;
            if (a == 0 || a == 255) continue;
            float f = 255.0f / (float)a;
            r = (int)((float)r * f);
            g = (int)((float)g * f);
            b = (int)((float)b * f);
            if (r > 255) {
                r = 255;
            }
            if (g > 255) {
                g = 255;
            }
            if (b > 255) {
                b = 255;
            }
            p[i] = a << 24 | r << 16 | g << 8 | b;
        }
    }
}

