/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.composite;

import com.jhlabs.composite.RGBComposite;
import java.awt.Color;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;

public final class SaturationComposite
extends RGBComposite {
    public SaturationComposite(float alpha) {
        super(alpha);
    }

    @Override
    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return new Context(this.extraAlpha, srcColorModel, dstColorModel);
    }

    static class Context
    extends RGBComposite.RGBCompositeContext {
        private float[] sHSB = new float[3];
        private float[] dHSB = new float[3];

        public Context(float alpha, ColorModel srcColorModel, ColorModel dstColorModel) {
            super(alpha, srcColorModel, dstColorModel);
        }

        @Override
        public void composeRGB(int[] src, int[] dst, float alpha) {
            int w = src.length;
            for (int i = 0; i < w; i += 4) {
                int sr = src[i];
                int dir = dst[i];
                int sg = src[i + 1];
                int dig = dst[i + 1];
                int sb = src[i + 2];
                int dib = dst[i + 2];
                int sa = src[i + 3];
                int dia = dst[i + 3];
                Color.RGBtoHSB(sr, sg, sb, this.sHSB);
                Color.RGBtoHSB(dir, dig, dib, this.dHSB);
                this.dHSB[1] = this.sHSB[1];
                int doRGB = Color.HSBtoRGB(this.dHSB[0], this.dHSB[1], this.dHSB[2]);
                int dor = (doRGB & 0xFF0000) >> 16;
                int dog = (doRGB & 0xFF00) >> 8;
                int dob = doRGB & 0xFF;
                float a = alpha * (float)sa / 255.0f;
                float ac = 1.0f - a;
                dst[i] = (int)(a * (float)dor + ac * (float)dir);
                dst[i + 1] = (int)(a * (float)dog + ac * (float)dig);
                dst[i + 2] = (int)(a * (float)dob + ac * (float)dib);
                dst[i + 3] = (int)((float)sa * alpha + (float)dia * ac);
            }
        }
    }
}

