/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.sg.prism;

import com.sun.javafx.sg.prism.NGNode;
import com.sun.prism.Graphics;
import com.sun.prism.Image;
import com.sun.prism.Texture;
import com.sun.prism.paint.Color;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.DropShadow;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.InnerShadow;

class EffectUtil {
    private static final int TEX_SIZE = 256;
    private static Texture itex;
    private static Texture dtex;

    static boolean renderEffectForRectangularNode(NGNode nGNode, Graphics graphics, Effect effect, float f, boolean bl, float f2, float f3, float f4, float f5) {
        DropShadow dropShadow;
        float f6;
        if (!graphics.getTransformNoClone().is2D() && graphics.isDepthBuffer() && graphics.isDepthTest()) {
            return false;
        }
        if (effect instanceof InnerShadow && !bl) {
            InnerShadow innerShadow = (InnerShadow)effect;
            float f7 = innerShadow.getRadius();
            if (f7 > 0.0f && f7 < f4 / 2.0f && f7 < f5 / 2.0f && innerShadow.getChoke() == 0.0f && innerShadow.getShadowSourceInput() == null && innerShadow.getContentInput() == null) {
                nGNode.renderContent(graphics);
                EffectUtil.renderRectInnerShadow(graphics, innerShadow, f, f2, f3, f4, f5);
                return true;
            }
        } else if (effect instanceof DropShadow && (f6 = (dropShadow = (DropShadow)effect).getRadius()) > 0.0f && f6 < f4 / 2.0f && f6 < f5 / 2.0f && dropShadow.getSpread() == 0.0f && dropShadow.getShadowSourceInput() == null && dropShadow.getContentInput() == null) {
            EffectUtil.renderRectDropShadow(graphics, dropShadow, f, f2, f3, f4, f5);
            nGNode.renderContent(graphics);
            return true;
        }
        return false;
    }

    static void renderRectInnerShadow(Graphics graphics, InnerShadow innerShadow, float f, float f2, float f3, float f4, float f5) {
        if (itex == null || itex.isSurfaceLost()) {
            byte[] arrby = new byte[65536];
            EffectUtil.fillGaussian(arrby, 256, 128.0f, innerShadow.getChoke(), true);
            Image image = Image.fromByteAlphaData(arrby, 256, 256);
            itex = graphics.getResourceFactory().createTexture(image, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_TO_EDGE);
            assert (itex.getWrapMode() == Texture.WrapMode.CLAMP_TO_EDGE);
            itex.contentsUseful();
            itex.makePermanent();
        }
        float f6 = innerShadow.getRadius();
        int n = itex.getPhysicalWidth();
        int n2 = itex.getContentX();
        int n3 = n2 + itex.getContentWidth();
        float f7 = ((float)n2 + 0.5f) / (float)n;
        float f8 = ((float)n3 - 0.5f) / (float)n;
        float f9 = f2;
        float f10 = f3;
        float f11 = f2 + f4;
        float f12 = f3 + f5;
        float f13 = f9 + (float)innerShadow.getOffsetX();
        float f14 = f10 + (float)innerShadow.getOffsetY();
        float f15 = f13 + f4;
        float f16 = f14 + f5;
        graphics.setPaint(EffectUtil.toPrismColor(innerShadow.getColor(), f));
        EffectUtil.drawClippedTexture(graphics, itex, f9, f10, f11, f12, f9, f10, f11, f14 - f6, f7, f7, f7, f7);
        EffectUtil.drawClippedTexture(graphics, itex, f9, f10, f11, f12, f13 - f6, f14 - f6, f13 + f6, f14 + f6, f7, f7, f8, f8);
        EffectUtil.drawClippedTexture(graphics, itex, f9, f10, f11, f12, f13 + f6, f14 - f6, f15 - f6, f14 + f6, f8, f7, f8, f8);
        EffectUtil.drawClippedTexture(graphics, itex, f9, f10, f11, f12, f15 - f6, f14 - f6, f15 + f6, f14 + f6, f8, f7, f7, f8);
        EffectUtil.drawClippedTexture(graphics, itex, f9, f10, f11, f12, f9, f14 - f6, f13 - f6, f16 + f6, f7, f7, f7, f7);
        EffectUtil.drawClippedTexture(graphics, itex, f9, f10, f11, f12, f13 - f6, f14 + f6, f13 + f6, f16 - f6, f7, f8, f8, f8);
        EffectUtil.drawClippedTexture(graphics, itex, f9, f10, f11, f12, f15 - f6, f14 + f6, f15 + f6, f16 - f6, f8, f8, f7, f8);
        EffectUtil.drawClippedTexture(graphics, itex, f9, f10, f11, f12, f15 + f6, f14 - f6, f11, f16 + f6, f7, f7, f7, f7);
        EffectUtil.drawClippedTexture(graphics, itex, f9, f10, f11, f12, f13 - f6, f16 - f6, f13 + f6, f16 + f6, f7, f8, f8, f7);
        EffectUtil.drawClippedTexture(graphics, itex, f9, f10, f11, f12, f13 + f6, f16 - f6, f15 - f6, f16 + f6, f8, f8, f8, f7);
        EffectUtil.drawClippedTexture(graphics, itex, f9, f10, f11, f12, f15 - f6, f16 - f6, f15 + f6, f16 + f6, f8, f8, f7, f7);
        EffectUtil.drawClippedTexture(graphics, itex, f9, f10, f11, f12, f9, f16 + f6, f11, f12, f7, f7, f7, f7);
    }

    static void drawClippedTexture(Graphics graphics, Texture texture, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        if (f5 >= f7 || f6 >= f8 || f >= f3 || f2 >= f4) {
            return;
        }
        if (f7 > f && f5 < f3) {
            if (f5 < f) {
                f9 += (f11 - f9) * (f - f5) / (f7 - f5);
                f5 = f;
            }
            if (f7 > f3) {
                f11 -= (f11 - f9) * (f7 - f3) / (f7 - f5);
                f7 = f3;
            }
        } else {
            return;
        }
        if (f8 > f2 && f6 < f4) {
            if (f6 < f2) {
                f10 += (f12 - f10) * (f2 - f6) / (f8 - f6);
                f6 = f2;
            }
            if (f8 > f4) {
                f12 -= (f12 - f10) * (f8 - f4) / (f8 - f6);
                f8 = f4;
            }
        } else {
            return;
        }
        graphics.drawTextureRaw(texture, f5, f6, f7, f8, f9, f10, f11, f12);
    }

    static void renderRectDropShadow(Graphics graphics, DropShadow dropShadow, float f, float f2, float f3, float f4, float f5) {
        if (dtex == null || dtex.isSurfaceLost()) {
            byte[] arrby = new byte[65536];
            EffectUtil.fillGaussian(arrby, 256, 128.0f, dropShadow.getSpread(), false);
            Image image = Image.fromByteAlphaData(arrby, 256, 256);
            dtex = graphics.getResourceFactory().createTexture(image, Texture.Usage.STATIC, Texture.WrapMode.CLAMP_TO_EDGE);
            assert (dtex.getWrapMode() == Texture.WrapMode.CLAMP_TO_EDGE);
            dtex.contentsUseful();
            dtex.makePermanent();
        }
        float f6 = dropShadow.getRadius();
        int n = dtex.getPhysicalWidth();
        int n2 = dtex.getContentX();
        int n3 = n2 + dtex.getContentWidth();
        float f7 = ((float)n2 + 0.5f) / (float)n;
        float f8 = ((float)n3 - 0.5f) / (float)n;
        float f9 = f2 + (float)dropShadow.getOffsetX();
        float f10 = f3 + (float)dropShadow.getOffsetY();
        float f11 = f9 + f4;
        float f12 = f10 + f5;
        graphics.setPaint(EffectUtil.toPrismColor(dropShadow.getColor(), f));
        graphics.drawTextureRaw(dtex, f9 - f6, f10 - f6, f9 + f6, f10 + f6, f7, f7, f8, f8);
        graphics.drawTextureRaw(dtex, f11 - f6, f10 - f6, f11 + f6, f10 + f6, f8, f7, f7, f8);
        graphics.drawTextureRaw(dtex, f11 - f6, f12 - f6, f11 + f6, f12 + f6, f8, f8, f7, f7);
        graphics.drawTextureRaw(dtex, f9 - f6, f12 - f6, f9 + f6, f12 + f6, f7, f8, f8, f7);
        graphics.drawTextureRaw(dtex, f9 + f6, f10 + f6, f11 - f6, f12 - f6, f8, f8, f8, f8);
        graphics.drawTextureRaw(dtex, f9 - f6, f10 + f6, f9 + f6, f12 - f6, f7, f8, f8, f8);
        graphics.drawTextureRaw(dtex, f11 - f6, f10 + f6, f11 + f6, f12 - f6, f8, f8, f7, f8);
        graphics.drawTextureRaw(dtex, f9 + f6, f10 - f6, f11 - f6, f10 + f6, f8, f7, f8, f8);
        graphics.drawTextureRaw(dtex, f9 + f6, f12 - f6, f11 - f6, f12 + f6, f8, f8, f8, f7);
    }

    private static void fillGaussian(byte[] arrby, int n, float f, float f2, boolean bl) {
        int n2;
        int n3;
        float f3 = f / 3.0f;
        float f4 = 2.0f * f3 * f3;
        if (f4 < Float.MIN_VALUE) {
            f4 = Float.MIN_VALUE;
        }
        float[] arrf = new float[n];
        int n4 = (n + 1) / 2;
        float f5 = 0.0f;
        for (n3 = 0; n3 < arrf.length; ++n3) {
            n2 = n4 - n3;
            arrf[n3] = f5 += (float)Math.exp((float)(-(n2 * n2)) / f4);
        }
        n3 = 0;
        while (n3 < arrf.length) {
            int n5 = n3++;
            arrf[n5] = arrf[n5] / f5;
        }
        for (n3 = 0; n3 < n; ++n3) {
            for (n2 = 0; n2 < n; ++n2) {
                int n6;
                float f6 = arrf[n3] * arrf[n2];
                if (bl) {
                    f6 = 1.0f - f6;
                }
                if ((n6 = (int)(f6 * 255.0f)) < 0) {
                    n6 = 0;
                } else if (n6 > 255) {
                    n6 = 255;
                }
                arrby[n3 * n + n2] = (byte)n6;
            }
        }
    }

    private static Color toPrismColor(Color4f color4f, float f) {
        float f2 = color4f.getRed();
        float f3 = color4f.getGreen();
        float f4 = color4f.getBlue();
        float f5 = color4f.getAlpha() * f;
        return new Color(f2, f3, f4, f5);
    }

    private EffectUtil() {
    }
}

