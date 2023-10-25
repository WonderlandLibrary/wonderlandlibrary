/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.math;

import com.jhlabs.math.Function1D;
import com.jhlabs.math.Function2D;
import com.jhlabs.math.Function3D;
import java.util.Random;

public class Noise
implements Function1D,
Function2D,
Function3D {
    private static Random randomGenerator = new Random();
    private static final int B = 256;
    private static final int BM = 255;
    private static final int N = 4096;
    static int[] p = new int[514];
    static float[][] g3 = new float[514][3];
    static float[][] g2 = new float[514][2];
    static float[] g1 = new float[514];
    static boolean start = true;

    @Override
    public float evaluate(float x) {
        return Noise.noise1(x);
    }

    @Override
    public float evaluate(float x, float y) {
        return Noise.noise2(x, y);
    }

    @Override
    public float evaluate(float x, float y, float z) {
        return Noise.noise3(x, y, z);
    }

    public static float turbulence2(float x, float y, float octaves) {
        float t = 0.0f;
        for (float f = 1.0f; f <= octaves; f *= 2.0f) {
            t += Math.abs(Noise.noise2(f * x, f * y)) / f;
        }
        return t;
    }

    public static float turbulence3(float x, float y, float z, float octaves) {
        float t = 0.0f;
        for (float f = 1.0f; f <= octaves; f *= 2.0f) {
            t += Math.abs(Noise.noise3(f * x, f * y, f * z)) / f;
        }
        return t;
    }

    private static float sCurve(float t) {
        return t * t * (3.0f - 2.0f * t);
    }

    public static float noise1(float x) {
        if (start) {
            start = false;
            Noise.init();
        }
        float t = x + 4096.0f;
        int bx0 = (int)t & 0xFF;
        int bx1 = bx0 + 1 & 0xFF;
        float rx0 = t - (float)((int)t);
        float rx1 = rx0 - 1.0f;
        float sx = Noise.sCurve(rx0);
        float u = rx0 * g1[p[bx0]];
        float v = rx1 * g1[p[bx1]];
        return 2.3f * Noise.lerp(sx, u, v);
    }

    public static float noise2(float x, float y) {
        if (start) {
            start = false;
            Noise.init();
        }
        float t = x + 4096.0f;
        int bx0 = (int)t & 0xFF;
        int bx1 = bx0 + 1 & 0xFF;
        float rx0 = t - (float)((int)t);
        float rx1 = rx0 - 1.0f;
        t = y + 4096.0f;
        int by0 = (int)t & 0xFF;
        int by1 = by0 + 1 & 0xFF;
        float ry0 = t - (float)((int)t);
        float ry1 = ry0 - 1.0f;
        int i = p[bx0];
        int j = p[bx1];
        int b00 = p[i + by0];
        int b10 = p[j + by0];
        int b01 = p[i + by1];
        int b11 = p[j + by1];
        float sx = Noise.sCurve(rx0);
        float sy = Noise.sCurve(ry0);
        float[] q = g2[b00];
        float u = rx0 * q[0] + ry0 * q[1];
        q = g2[b10];
        float v = rx1 * q[0] + ry0 * q[1];
        float a = Noise.lerp(sx, u, v);
        q = g2[b01];
        u = rx0 * q[0] + ry1 * q[1];
        q = g2[b11];
        v = rx1 * q[0] + ry1 * q[1];
        float b = Noise.lerp(sx, u, v);
        return 1.5f * Noise.lerp(sy, a, b);
    }

    public static float noise3(float x, float y, float z) {
        if (start) {
            start = false;
            Noise.init();
        }
        float t = x + 4096.0f;
        int bx0 = (int)t & 0xFF;
        int bx1 = bx0 + 1 & 0xFF;
        float rx0 = t - (float)((int)t);
        float rx1 = rx0 - 1.0f;
        t = y + 4096.0f;
        int by0 = (int)t & 0xFF;
        int by1 = by0 + 1 & 0xFF;
        float ry0 = t - (float)((int)t);
        float ry1 = ry0 - 1.0f;
        t = z + 4096.0f;
        int bz0 = (int)t & 0xFF;
        int bz1 = bz0 + 1 & 0xFF;
        float rz0 = t - (float)((int)t);
        float rz1 = rz0 - 1.0f;
        int i = p[bx0];
        int j = p[bx1];
        int b00 = p[i + by0];
        int b10 = p[j + by0];
        int b01 = p[i + by1];
        int b11 = p[j + by1];
        t = Noise.sCurve(rx0);
        float sy = Noise.sCurve(ry0);
        float sz = Noise.sCurve(rz0);
        float[] q = g3[b00 + bz0];
        float u = rx0 * q[0] + ry0 * q[1] + rz0 * q[2];
        q = g3[b10 + bz0];
        float v = rx1 * q[0] + ry0 * q[1] + rz0 * q[2];
        float a = Noise.lerp(t, u, v);
        q = g3[b01 + bz0];
        u = rx0 * q[0] + ry1 * q[1] + rz0 * q[2];
        q = g3[b11 + bz0];
        v = rx1 * q[0] + ry1 * q[1] + rz0 * q[2];
        float b = Noise.lerp(t, u, v);
        float c = Noise.lerp(sy, a, b);
        q = g3[b00 + bz1];
        u = rx0 * q[0] + ry0 * q[1] + rz1 * q[2];
        q = g3[b10 + bz1];
        v = rx1 * q[0] + ry0 * q[1] + rz1 * q[2];
        a = Noise.lerp(t, u, v);
        q = g3[b01 + bz1];
        u = rx0 * q[0] + ry1 * q[1] + rz1 * q[2];
        q = g3[b11 + bz1];
        v = rx1 * q[0] + ry1 * q[1] + rz1 * q[2];
        b = Noise.lerp(t, u, v);
        float d = Noise.lerp(sy, a, b);
        return 1.5f * Noise.lerp(sz, c, d);
    }

    public static float lerp(float t, float a, float b) {
        return a + t * (b - a);
    }

    private static void normalize2(float[] v) {
        float s = (float)Math.sqrt(v[0] * v[0] + v[1] * v[1]);
        v[0] = v[0] / s;
        v[1] = v[1] / s;
    }

    static void normalize3(float[] v) {
        float s = (float)Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
        v[0] = v[0] / s;
        v[1] = v[1] / s;
        v[2] = v[2] / s;
    }

    private static int random() {
        return randomGenerator.nextInt() & Integer.MAX_VALUE;
    }

    private static void init() {
        int j;
        int i;
        for (i = 0; i < 256; ++i) {
            Noise.p[i] = i;
            Noise.g1[i] = (float)(Noise.random() % 512 - 256) / 256.0f;
            for (j = 0; j < 2; ++j) {
                Noise.g2[i][j] = (float)(Noise.random() % 512 - 256) / 256.0f;
            }
            Noise.normalize2(g2[i]);
            for (j = 0; j < 3; ++j) {
                Noise.g3[i][j] = (float)(Noise.random() % 512 - 256) / 256.0f;
            }
            Noise.normalize3(g3[i]);
        }
        for (i = 255; i >= 0; --i) {
            int k = p[i];
            j = Noise.random() % 256;
            Noise.p[i] = p[j];
            Noise.p[j] = k;
        }
        for (i = 0; i < 258; ++i) {
            Noise.p[256 + i] = p[i];
            Noise.g1[256 + i] = g1[i];
            for (j = 0; j < 2; ++j) {
                Noise.g2[256 + i][j] = g2[i][j];
            }
            for (j = 0; j < 3; ++j) {
                Noise.g3[256 + i][j] = g3[i][j];
            }
        }
    }

    public static float[] findRange(Function1D f, float[] minmax) {
        if (minmax == null) {
            minmax = new float[2];
        }
        float min = 0.0f;
        float max = 0.0f;
        float x = -100.0f;
        while (x < 100.0f) {
            float n = f.evaluate(x);
            min = Math.min(min, n);
            max = Math.max(max, n);
            x = (float)((double)x + 1.27139);
        }
        minmax[0] = min;
        minmax[1] = max;
        return minmax;
    }

    public static float[] findRange(Function2D f, float[] minmax) {
        if (minmax == null) {
            minmax = new float[2];
        }
        float min = 0.0f;
        float max = 0.0f;
        float y = -100.0f;
        while (y < 100.0f) {
            float x = -100.0f;
            while (x < 100.0f) {
                float n = f.evaluate(x, y);
                min = Math.min(min, n);
                max = Math.max(max, n);
                x = (float)((double)x + 10.77139);
            }
            y = (float)((double)y + 10.35173);
        }
        minmax[0] = min;
        minmax[1] = max;
        return minmax;
    }
}

