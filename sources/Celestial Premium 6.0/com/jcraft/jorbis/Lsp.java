/*
 * Decompiled with CFR 0.150.
 */
package com.jcraft.jorbis;

import com.jcraft.jorbis.Lookup;

class Lsp {
    static final float M_PI = (float)Math.PI;

    Lsp() {
    }

    static void lsp_to_curve(float[] curve, int[] map, int n, int ln, float[] lsp, int m, float amp, float ampoffset) {
        int i;
        float wdel = (float)Math.PI / (float)ln;
        for (i = 0; i < m; ++i) {
            lsp[i] = Lookup.coslook(lsp[i]);
        }
        int m2 = m / 2 * 2;
        i = 0;
        while (i < n) {
            int k = map[i];
            float p = 0.70710677f;
            float q = 0.70710677f;
            float w = Lookup.coslook(wdel * (float)k);
            for (int j = 0; j < m2; j += 2) {
                q *= lsp[j] - w;
                p *= lsp[j + 1] - w;
            }
            if ((m & 1) != 0) {
                q *= lsp[m - 1] - w;
                q *= q;
                p *= p * (1.0f - w * w);
            } else {
                q *= q * (1.0f + w);
                p *= p * (1.0f - w);
            }
            q = p + q;
            int hx = Float.floatToIntBits(q);
            int ix = Integer.MAX_VALUE & hx;
            int qexp = 0;
            if (ix < 2139095040 && ix != 0) {
                if (ix < 0x800000) {
                    q = (float)((double)q * 3.3554432E7);
                    hx = Float.floatToIntBits(q);
                    ix = Integer.MAX_VALUE & hx;
                    qexp = -25;
                }
                qexp += (ix >>> 23) - 126;
                hx = hx & 0x807FFFFF | 0x3F000000;
                q = Float.intBitsToFloat(hx);
            }
            q = Lookup.fromdBlook(amp * Lookup.invsqlook(q) * Lookup.invsq2explook(qexp + m) - ampoffset);
            do {
                int n2 = i++;
                curve[n2] = curve[n2] * q;
            } while (i < n && map[i] == k);
        }
    }
}

