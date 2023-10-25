/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Brightpass;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWBrightpassPeer
extends JSWEffectPeer {
    public JSWBrightpassPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final Brightpass getEffect() {
        return (Brightpass)super.getEffect();
    }

    private float getThreshold() {
        return this.getEffect().getThreshold();
    }

    @Override
    public ImageData filter(Effect effect, RenderState renderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        this.setEffect(effect);
        Rectangle rectangle2 = this.getResultBounds(baseTransform, rectangle, arrimageData);
        this.setDestBounds(rectangle2);
        HeapImage heapImage = (HeapImage)arrimageData[0].getTransformedImage(rectangle2);
        int n = 0;
        int n2 = 0;
        int n3 = heapImage.getPhysicalWidth();
        int n4 = heapImage.getPhysicalHeight();
        int n5 = heapImage.getScanlineStride();
        int[] arrn = heapImage.getPixelArray();
        Rectangle rectangle3 = new Rectangle(n, n2, n3, n4);
        Rectangle rectangle4 = arrimageData[0].getTransformedBounds(rectangle2);
        BaseTransform baseTransform2 = BaseTransform.IDENTITY_TRANSFORM;
        this.setInputBounds(0, rectangle4);
        this.setInputNativeBounds(0, rectangle3);
        float[] arrf = new float[4];
        this.getTextureCoordinates(0, arrf, rectangle4.x, rectangle4.y, n3, n4, rectangle2, baseTransform2);
        int n6 = rectangle2.width;
        int n7 = rectangle2.height;
        HeapImage heapImage2 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n6, n7));
        this.setDestNativeBounds(heapImage2.getPhysicalWidth(), heapImage2.getPhysicalHeight());
        int n8 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        float f = this.getThreshold();
        float f2 = (arrf[2] - arrf[0]) / (float)n6;
        float f3 = (arrf[3] - arrf[1]) / (float)n7;
        float f4 = arrf[1] + f3 * 0.5f;
        for (int i = 0; i < 0 + n7; ++i) {
            float f5 = i;
            int n9 = i * n8;
            float f6 = arrf[0] + f2 * 0.5f;
            for (int j = 0; j < 0 + n6; ++j) {
                float f7;
                int n10;
                float f8 = j;
                float f9 = 0.2125f;
                float f10 = 0.7154f;
                float f11 = 0.0721f;
                float f12 = f6;
                float f13 = f4;
                if (f12 >= 0.0f && f13 >= 0.0f) {
                    int n11 = (int)(f12 * (float)n3);
                    int n12 = (int)(f13 * (float)n4);
                    boolean bl = n11 >= n3 || n12 >= n4;
                    n10 = bl ? 0 : arrn[n12 * n5 + n11];
                } else {
                    n10 = 0;
                }
                float f14 = (float)(n10 >> 16 & 0xFF) / 255.0f;
                float f15 = (float)(n10 >> 8 & 0xFF) / 255.0f;
                float f16 = (float)(n10 & 0xFF) / 255.0f;
                float f17 = (float)(n10 >>> 24) / 255.0f;
                f12 = f14;
                f13 = f15;
                float f18 = f16;
                float f19 = f17;
                float f20 = f9;
                float f21 = f10;
                float f22 = f11;
                float f23 = f12;
                float f24 = f13;
                float f25 = f18;
                f20 = f7 = f20 * f23 + f21 * f24 + f22 * f25;
                f22 = 0.0f;
                f23 = f20 - f19 * f;
                f23 = f20 = (f21 = f22 > f23 ? f22 : f23);
                f22 = Math.signum(f23);
                float f26 = f12 * f22;
                float f27 = f13 * f22;
                float f28 = f18 * f22;
                float f29 = f19 * f22;
                if (f29 < 0.0f) {
                    f29 = 0.0f;
                } else if (f29 > 1.0f) {
                    f29 = 1.0f;
                }
                if (f26 < 0.0f) {
                    f26 = 0.0f;
                } else if (f26 > f29) {
                    f26 = f29;
                }
                if (f27 < 0.0f) {
                    f27 = 0.0f;
                } else if (f27 > f29) {
                    f27 = f29;
                }
                if (f28 < 0.0f) {
                    f28 = 0.0f;
                } else if (f28 > f29) {
                    f28 = f29;
                }
                arrn2[n9 + j] = (int)(f26 * 255.0f) << 16 | (int)(f27 * 255.0f) << 8 | (int)(f28 * 255.0f) << 0 | (int)(f29 * 255.0f) << 24;
                f6 += f2;
            }
            f4 += f3;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        return new ImageData(this.getFilterContext(), heapImage2, rectangle2);
    }
}

