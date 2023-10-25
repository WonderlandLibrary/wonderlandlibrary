/*
 * Decompiled with CFR 0.150.
 */
package com.jcraft.jorbis;

import com.jcraft.jorbis.Drft;

class Lpc {
    Drft fft = new Drft();
    int ln;
    int m;

    Lpc() {
    }

    static float lpc_from_data(float[] data, float[] lpc, int n, int m) {
        int i;
        float[] aut = new float[m + 1];
        int j = m + 1;
        while (j-- != 0) {
            float d = 0.0f;
            for (i = j; i < n; ++i) {
                d += data[i] * data[i - j];
            }
            aut[j] = d;
        }
        float error = aut[0];
        for (i = 0; i < m; ++i) {
            float r = -aut[i + 1];
            if (error == 0.0f) {
                for (int k = 0; k < m; ++k) {
                    lpc[k] = 0.0f;
                }
                return 0.0f;
            }
            for (j = 0; j < i; ++j) {
                r -= lpc[j] * aut[i - j];
            }
            lpc[i] = r /= error;
            for (j = 0; j < i / 2; ++j) {
                float tmp = lpc[j];
                int n2 = j;
                lpc[n2] = lpc[n2] + r * lpc[i - 1 - j];
                int n3 = i - 1 - j;
                lpc[n3] = lpc[n3] + r * tmp;
            }
            if (i % 2 != 0) {
                int n4 = j;
                lpc[n4] = lpc[n4] + lpc[j] * r;
            }
            error = (float)((double)error * (1.0 - (double)(r * r)));
        }
        return error;
    }

    float lpc_from_curve(float[] curve, float[] lpc) {
        int i;
        int n = this.ln;
        float[] work = new float[n + n];
        float fscale = (float)(0.5 / (double)n);
        for (i = 0; i < n; ++i) {
            work[i * 2] = curve[i] * fscale;
            work[i * 2 + 1] = 0.0f;
        }
        work[n * 2 - 1] = curve[n - 1] * fscale;
        this.fft.backward(work);
        i = 0;
        int j = (n *= 2) / 2;
        while (i < n / 2) {
            float temp = work[i];
            work[i++] = work[j];
            work[j++] = temp;
        }
        return Lpc.lpc_from_data(work, lpc, n, this.m);
    }

    void init(int mapped, int m) {
        this.ln = mapped;
        this.m = m;
        this.fft.init(mapped * 2);
    }

    void clear() {
        this.fft.clear();
    }

    static float FAST_HYPOT(float a, float b) {
        return (float)Math.sqrt(a * a + b * b);
    }

    void lpc_to_curve(float[] curve, float[] lpc, float amp) {
        int i;
        for (i = 0; i < this.ln * 2; ++i) {
            curve[i] = 0.0f;
        }
        if (amp == 0.0f) {
            return;
        }
        for (i = 0; i < this.m; ++i) {
            curve[i * 2 + 1] = lpc[i] / (4.0f * amp);
            curve[i * 2 + 2] = -lpc[i] / (4.0f * amp);
        }
        this.fft.backward(curve);
        int l2 = this.ln * 2;
        float unit = (float)(1.0 / (double)amp);
        curve[0] = (float)(1.0 / (double)(curve[0] * 2.0f + unit));
        for (int i2 = 1; i2 < this.ln; ++i2) {
            float real = curve[i2] + curve[l2 - i2];
            float imag = curve[i2] - curve[l2 - i2];
            float a = real + unit;
            curve[i2] = (float)(1.0 / (double)Lpc.FAST_HYPOT(a, imag));
        }
    }
}

