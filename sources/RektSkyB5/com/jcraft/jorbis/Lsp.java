/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jorbis.Lookup;

class Lsp {
    static final float M_PI = (float)Math.PI;

    Lsp() {
    }

    static void lsp_to_curve(float[] curve, int[] map, int n2, int ln, float[] lsp, int m2, float amp, float ampoffset) {
        int i2;
        float wdel = (float)Math.PI / (float)ln;
        for (i2 = 0; i2 < m2; ++i2) {
            lsp[i2] = Lookup.coslook(lsp[i2]);
        }
        int m22 = m2 / 2 * 2;
        i2 = 0;
        while (i2 < n2) {
            int k2 = map[i2];
            float p2 = 0.70710677f;
            float q2 = 0.70710677f;
            float w2 = Lookup.coslook(wdel * (float)k2);
            for (int j2 = 0; j2 < m22; j2 += 2) {
                q2 *= lsp[j2] - w2;
                p2 *= lsp[j2 + 1] - w2;
            }
            if ((m2 & 1) != 0) {
                q2 *= lsp[m2 - 1] - w2;
                q2 *= q2;
                p2 *= p2 * (1.0f - w2 * w2);
            } else {
                q2 *= q2 * (1.0f + w2);
                p2 *= p2 * (1.0f - w2);
            }
            q2 = p2 + q2;
            int hx = Float.floatToIntBits(q2);
            int ix = Integer.MAX_VALUE & hx;
            int qexp = 0;
            if (ix < 2139095040 && ix != 0) {
                if (ix < 0x800000) {
                    q2 = (float)((double)q2 * 3.3554432E7);
                    hx = Float.floatToIntBits(q2);
                    ix = Integer.MAX_VALUE & hx;
                    qexp = -25;
                }
                qexp += (ix >>> 23) - 126;
                hx = hx & 0x807FFFFF | 0x3F000000;
                q2 = Float.intBitsToFloat(hx);
            }
            q2 = Lookup.fromdBlook(amp * Lookup.invsqlook(q2) * Lookup.invsq2explook(qexp + m2) - ampoffset);
            do {
                int n3 = i2++;
                curve[n3] = curve[n3] * q2;
            } while (i2 < n2 && map[i2] == k2);
        }
    }
}

