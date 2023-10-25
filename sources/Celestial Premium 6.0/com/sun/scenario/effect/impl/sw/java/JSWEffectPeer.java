/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public abstract class JSWEffectPeer<T extends RenderState>
extends EffectPeer<T> {
    protected static final int FVALS_A = 3;
    protected static final int FVALS_R = 0;
    protected static final int FVALS_G = 1;
    protected static final int FVALS_B = 2;

    protected JSWEffectPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    protected final void laccum(int n, float f, float[] arrf) {
        arrf[0] = arrf[0] + (float)(n >> 16 & 0xFF) * (f /= 255.0f);
        arrf[1] = arrf[1] + (float)(n >> 8 & 0xFF) * f;
        arrf[2] = arrf[2] + (float)(n & 0xFF) * f;
        arrf[3] = arrf[3] + (float)(n >>> 24) * f;
    }

    protected final void lsample(int[] arrn, float f, float f2, int n, int n2, int n3, float[] arrf) {
        arrf[0] = 0.0f;
        arrf[1] = 0.0f;
        arrf[2] = 0.0f;
        arrf[3] = 0.0f;
        f = f * (float)n + 0.5f;
        f2 = f2 * (float)n2 + 0.5f;
        int n4 = (int)f;
        int n5 = (int)f2;
        if (f > 0.0f && f2 > 0.0f && n4 <= n && n5 <= n2) {
            int n6 = n5 * n3 + n4;
            float f3 = (f -= (float)n4) * (f2 -= (float)n5);
            if (n5 < n2) {
                if (n4 < n) {
                    this.laccum(arrn[n6], f3, arrf);
                }
                if (n4 > 0) {
                    this.laccum(arrn[n6 - 1], f2 - f3, arrf);
                }
            }
            if (n5 > 0) {
                if (n4 < n) {
                    this.laccum(arrn[n6 - n3], f - f3, arrf);
                }
                if (n4 > 0) {
                    this.laccum(arrn[n6 - n3 - 1], 1.0f - f - f2 + f3, arrf);
                }
            }
        }
    }

    protected final void laccumsample(int[] arrn, float f, float f2, int n, int n2, int n3, float f3, float[] arrf) {
        f3 *= 255.0f;
        int n4 = (int)(f += 0.5f);
        int n5 = (int)(f2 += 0.5f);
        if (f > 0.0f && f2 > 0.0f && n4 <= n && n5 <= n2) {
            int n6 = n5 * n3 + n4;
            float f4 = (f -= (float)n4) * (f2 -= (float)n5);
            if (n5 < n2) {
                if (n4 < n) {
                    this.laccum(arrn[n6], f4 * f3, arrf);
                }
                if (n4 > 0) {
                    this.laccum(arrn[n6 - 1], (f2 - f4) * f3, arrf);
                }
            }
            if (n5 > 0) {
                if (n4 < n) {
                    this.laccum(arrn[n6 - n3], (f - f4) * f3, arrf);
                }
                if (n4 > 0) {
                    this.laccum(arrn[n6 - n3 - 1], (1.0f - f - f2 + f4) * f3, arrf);
                }
            }
        }
    }

    protected final void faccum(float[] arrf, int n, float f, float[] arrf2) {
        arrf2[0] = arrf2[0] + arrf[n] * f;
        arrf2[1] = arrf2[1] + arrf[n + 1] * f;
        arrf2[2] = arrf2[2] + arrf[n + 2] * f;
        arrf2[3] = arrf2[3] + arrf[n + 3] * f;
    }

    protected final void fsample(float[] arrf, float f, float f2, int n, int n2, int n3, float[] arrf2) {
        arrf2[0] = 0.0f;
        arrf2[1] = 0.0f;
        arrf2[2] = 0.0f;
        arrf2[3] = 0.0f;
        f = f * (float)n + 0.5f;
        f2 = f2 * (float)n2 + 0.5f;
        int n4 = (int)f;
        int n5 = (int)f2;
        if (f > 0.0f && f2 > 0.0f && n4 <= n && n5 <= n2) {
            int n6 = 4 * (n5 * n3 + n4);
            float f3 = (f -= (float)n4) * (f2 -= (float)n5);
            if (n5 < n2) {
                if (n4 < n) {
                    this.faccum(arrf, n6, f3, arrf2);
                }
                if (n4 > 0) {
                    this.faccum(arrf, n6 - 4, f2 - f3, arrf2);
                }
            }
            if (n5 > 0) {
                if (n4 < n) {
                    this.faccum(arrf, n6 - n3 * 4, f - f3, arrf2);
                }
                if (n4 > 0) {
                    this.faccum(arrf, n6 - n3 * 4 - 4, 1.0f - f - f2 + f3, arrf2);
                }
            }
        }
    }
}

