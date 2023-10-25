/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.SepiaTone;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWSepiaTonePeer
extends JSWEffectPeer {
    public JSWSepiaTonePeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final SepiaTone getEffect() {
        return (SepiaTone)super.getEffect();
    }

    private float getLevel() {
        return this.getEffect().getLevel();
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
        float f = this.getLevel();
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
                float f9 = 0.3f;
                float f10 = 0.59f;
                float f11 = 0.11f;
                float f12 = 1.6f;
                float f13 = 1.2f;
                float f14 = 0.9f;
                float f15 = f6;
                float f16 = f4;
                if (f15 >= 0.0f && f16 >= 0.0f) {
                    int n11 = (int)(f15 * (float)n3);
                    int n12 = (int)(f16 * (float)n4);
                    boolean bl = n11 >= n3 || n12 >= n4;
                    n10 = bl ? 0 : arrn[n12 * n5 + n11];
                } else {
                    n10 = 0;
                }
                float f17 = (float)(n10 >> 16 & 0xFF) / 255.0f;
                float f18 = (float)(n10 >> 8 & 0xFF) / 255.0f;
                float f19 = (float)(n10 & 0xFF) / 255.0f;
                float f20 = (float)(n10 >>> 24) / 255.0f;
                f15 = f17;
                f16 = f18;
                float f21 = f19;
                float f22 = f20;
                float f23 = f15;
                float f24 = f16;
                float f25 = f21;
                float f26 = f9;
                float f27 = f10;
                float f28 = f11;
                f24 = f23 = (f7 = f23 * f26 + f24 * f27 + f25 * f28);
                f25 = f23;
                f26 = f23;
                f27 = f24 * f12;
                f28 = f25 * f13;
                float f29 = f26 * f14;
                float f30 = f27;
                float f31 = f28;
                float f32 = f29;
                float f33 = f15;
                float f34 = f16;
                float f35 = f21;
                float f36 = 1.0f - f;
                float f37 = f30 * (1.0f - f36) + f33 * f36;
                float f38 = f31 * (1.0f - f36) + f34 * f36;
                float f39 = f32 * (1.0f - f36) + f35 * f36;
                float f40 = f37;
                float f41 = f38;
                float f42 = f39;
                float f43 = f22;
                if (f43 < 0.0f) {
                    f43 = 0.0f;
                } else if (f43 > 1.0f) {
                    f43 = 1.0f;
                }
                if (f40 < 0.0f) {
                    f40 = 0.0f;
                } else if (f40 > f43) {
                    f40 = f43;
                }
                if (f41 < 0.0f) {
                    f41 = 0.0f;
                } else if (f41 > f43) {
                    f41 = f43;
                }
                if (f42 < 0.0f) {
                    f42 = 0.0f;
                } else if (f42 > f43) {
                    f42 = f43;
                }
                arrn2[n9 + j] = (int)(f40 * 255.0f) << 16 | (int)(f41 * 255.0f) << 8 | (int)(f42 * 255.0f) << 0 | (int)(f43 * 255.0f) << 24;
                f6 += f2;
            }
            f4 += f3;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        return new ImageData(this.getFilterContext(), heapImage2, rectangle2);
    }
}

