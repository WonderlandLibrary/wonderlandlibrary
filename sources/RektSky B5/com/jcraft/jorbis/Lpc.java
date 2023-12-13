/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jorbis.Drft;

class Lpc {
    Drft fft = new Drft();
    int ln;
    int m;

    Lpc() {
    }

    static float lpc_from_data(float[] data, float[] lpc, int n2, int m2) {
        int i2;
        float[] aut = new float[m2 + 1];
        int j2 = m2 + 1;
        while (j2-- != 0) {
            float d2 = 0.0f;
            for (i2 = j2; i2 < n2; ++i2) {
                d2 += data[i2] * data[i2 - j2];
            }
            aut[j2] = d2;
        }
        float error = aut[0];
        for (i2 = 0; i2 < m2; ++i2) {
            float r2 = -aut[i2 + 1];
            if (error == 0.0f) {
                for (int k2 = 0; k2 < m2; ++k2) {
                    lpc[k2] = 0.0f;
                }
                return 0.0f;
            }
            for (j2 = 0; j2 < i2; ++j2) {
                r2 -= lpc[j2] * aut[i2 - j2];
            }
            lpc[i2] = r2 /= error;
            for (j2 = 0; j2 < i2 / 2; ++j2) {
                float tmp = lpc[j2];
                int n3 = j2;
                lpc[n3] = lpc[n3] + r2 * lpc[i2 - 1 - j2];
                int n4 = i2 - 1 - j2;
                lpc[n4] = lpc[n4] + r2 * tmp;
            }
            if (i2 % 2 != 0) {
                int n5 = j2;
                lpc[n5] = lpc[n5] + lpc[j2] * r2;
            }
            error = (float)((double)error * (1.0 - (double)(r2 * r2)));
        }
        return error;
    }

    float lpc_from_curve(float[] curve, float[] lpc) {
        int i2;
        int n2 = this.ln;
        float[] work = new float[n2 + n2];
        float fscale = (float)(0.5 / (double)n2);
        for (i2 = 0; i2 < n2; ++i2) {
            work[i2 * 2] = curve[i2] * fscale;
            work[i2 * 2 + 1] = 0.0f;
        }
        work[n2 * 2 - 1] = curve[n2 - 1] * fscale;
        this.fft.backward(work);
        i2 = 0;
        int j2 = (n2 *= 2) / 2;
        while (i2 < n2 / 2) {
            float temp = work[i2];
            work[i2++] = work[j2];
            work[j2++] = temp;
        }
        return Lpc.lpc_from_data(work, lpc, n2, this.m);
    }

    void init(int mapped, int m2) {
        this.ln = mapped;
        this.m = m2;
        this.fft.init(mapped * 2);
    }

    void clear() {
        this.fft.clear();
    }

    static float FAST_HYPOT(float a2, float b2) {
        return (float)Math.sqrt(a2 * a2 + b2 * b2);
    }

    void lpc_to_curve(float[] curve, float[] lpc, float amp) {
        int i2;
        for (i2 = 0; i2 < this.ln * 2; ++i2) {
            curve[i2] = 0.0f;
        }
        if (amp == 0.0f) {
            return;
        }
        for (i2 = 0; i2 < this.m; ++i2) {
            curve[i2 * 2 + 1] = lpc[i2] / (4.0f * amp);
            curve[i2 * 2 + 2] = -lpc[i2] / (4.0f * amp);
        }
        this.fft.backward(curve);
        int l2 = this.ln * 2;
        float unit = (float)(1.0 / (double)amp);
        curve[0] = (float)(1.0 / (double)(curve[0] * 2.0f + unit));
        for (int i3 = 1; i3 < this.ln; ++i3) {
            float real = curve[i3] + curve[l2 - i3];
            float imag = curve[i3] - curve[l2 - i3];
            float a2 = real + unit;
            curve[i3] = (float)(1.0 / (double)Lpc.FAST_HYPOT(a2, imag));
        }
    }
}

