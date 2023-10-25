/*
 * Decompiled with CFR 0.150.
 */
package com.jhlabs.composite;

import com.jhlabs.composite.RGBComposite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;

public final class NegationComposite
extends RGBComposite {
    public NegationComposite(float alpha) {
        super(alpha);
    }

    @Override
    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return new Context(this.extraAlpha, srcColorModel, dstColorModel);
    }

    static class Context
    extends RGBComposite.RGBCompositeContext {
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
                int dor = 255 - Math.abs(255 - sr - dir);
                int dog = 255 - Math.abs(255 - sg - dig);
                int dob = 255 - Math.abs(255 - sb - dib);
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

