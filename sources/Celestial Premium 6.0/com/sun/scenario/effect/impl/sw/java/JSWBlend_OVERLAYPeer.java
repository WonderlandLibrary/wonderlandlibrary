/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Blend;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWBlend_OVERLAYPeer
extends JSWEffectPeer {
    public JSWBlend_OVERLAYPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final Blend getEffect() {
        return (Blend)super.getEffect();
    }

    private float getOpacity() {
        return this.getEffect().getOpacity();
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
        HeapImage heapImage2 = (HeapImage)arrimageData[1].getTransformedImage(rectangle2);
        int n6 = 0;
        int n7 = 0;
        int n8 = heapImage2.getPhysicalWidth();
        int n9 = heapImage2.getPhysicalHeight();
        int n10 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        Rectangle rectangle5 = new Rectangle(n6, n7, n8, n9);
        Rectangle rectangle6 = arrimageData[1].getTransformedBounds(rectangle2);
        BaseTransform baseTransform3 = BaseTransform.IDENTITY_TRANSFORM;
        this.setInputBounds(1, rectangle6);
        this.setInputNativeBounds(1, rectangle5);
        float[] arrf = new float[4];
        this.getTextureCoordinates(0, arrf, rectangle4.x, rectangle4.y, n3, n4, rectangle2, baseTransform2);
        float[] arrf2 = new float[4];
        this.getTextureCoordinates(1, arrf2, rectangle6.x, rectangle6.y, n8, n9, rectangle2, baseTransform3);
        int n11 = rectangle2.width;
        int n12 = rectangle2.height;
        HeapImage heapImage3 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n11, n12));
        this.setDestNativeBounds(heapImage3.getPhysicalWidth(), heapImage3.getPhysicalHeight());
        int n13 = heapImage3.getScanlineStride();
        int[] arrn3 = heapImage3.getPixelArray();
        float f = this.getOpacity();
        float f2 = (arrf[2] - arrf[0]) / (float)n11;
        float f3 = (arrf[3] - arrf[1]) / (float)n12;
        float f4 = (arrf2[2] - arrf2[0]) / (float)n11;
        float f5 = (arrf2[3] - arrf2[1]) / (float)n12;
        float f6 = arrf[1] + f3 * 0.5f;
        float f7 = arrf2[1] + f5 * 0.5f;
        for (int i = 0; i < 0 + n12; ++i) {
            float f8 = i;
            int n14 = i * n13;
            float f9 = arrf[0] + f2 * 0.5f;
            float f10 = arrf2[0] + f4 * 0.5f;
            for (int j = 0; j < 0 + n11; ++j) {
                int n15;
                int n16;
                float f11 = j;
                float f12 = f9;
                float f13 = f6;
                if (f12 >= 0.0f && f13 >= 0.0f) {
                    int n17 = (int)(f12 * (float)n3);
                    int n18 = (int)(f13 * (float)n4);
                    boolean bl = n17 >= n3 || n18 >= n4;
                    n16 = bl ? 0 : arrn[n18 * n5 + n17];
                } else {
                    n16 = 0;
                }
                float f14 = (float)(n16 >> 16 & 0xFF) / 255.0f;
                float f15 = (float)(n16 >> 8 & 0xFF) / 255.0f;
                float f16 = (float)(n16 & 0xFF) / 255.0f;
                float f17 = (float)(n16 >>> 24) / 255.0f;
                f12 = f14;
                f13 = f15;
                float f18 = f16;
                float f19 = f17;
                float f20 = f10;
                float f21 = f7;
                if (f20 >= 0.0f && f21 >= 0.0f) {
                    int n19 = (int)(f20 * (float)n8);
                    int n20 = (int)(f21 * (float)n9);
                    boolean bl = n19 >= n8 || n20 >= n9;
                    n15 = bl ? 0 : arrn2[n20 * n10 + n19];
                } else {
                    n15 = 0;
                }
                f14 = (float)(n15 >> 16 & 0xFF) / 255.0f;
                f15 = (float)(n15 >> 8 & 0xFF) / 255.0f;
                f16 = (float)(n15 & 0xFF) / 255.0f;
                f17 = (float)(n15 >>> 24) / 255.0f;
                f20 = f14 * f;
                f21 = f15 * f;
                float f22 = f16 * f;
                float f23 = f17 * f;
                float f24 = f12;
                float f25 = f13;
                float f26 = f18;
                float f27 = f19;
                float f28 = f20;
                float f29 = f21;
                float f30 = f22;
                float f31 = f23;
                float f32 = f27 + f31 - f27 * f31;
                float f33 = f24 - f27 * 0.5f;
                float f34 = f25 - f27 * 0.5f;
                float f35 = f26 - f27 * 0.5f;
                float f36 = (float)Math.ceil(f33);
                float f37 = (float)Math.ceil(f34);
                float f38 = (float)Math.ceil(f35);
                f33 = f36;
                f34 = f37;
                f35 = f38;
                float f39 = f24 - f33 * f27;
                float f40 = f25 - f34 * f27;
                float f41 = f26 - f35 * f27;
                float f42 = Math.abs(f39);
                float f43 = Math.abs(f40);
                float f44 = Math.abs(f41);
                f39 = f42;
                f40 = f43;
                f41 = f44;
                float f45 = f28 - f33 * f31;
                float f46 = f29 - f34 * f31;
                float f47 = f30 - f35 * f31;
                f42 = Math.abs(f45);
                f43 = Math.abs(f46);
                f44 = Math.abs(f47);
                f45 = f42;
                f46 = f43;
                f47 = f44;
                float f48 = (2.0f * f39 + 1.0f - f27) * f45 + (1.0f - f31) * f39;
                float f49 = (2.0f * f40 + 1.0f - f27) * f46 + (1.0f - f31) * f40;
                float f50 = (2.0f * f41 + 1.0f - f27) * f47 + (1.0f - f31) * f41;
                float f51 = f48 - f33 * f32;
                float f52 = f49 - f34 * f32;
                float f53 = f50 - f35 * f32;
                f42 = Math.abs(f51);
                f43 = Math.abs(f52);
                f44 = Math.abs(f53);
                f48 = f42;
                f49 = f43;
                f50 = f44;
                float f54 = f48;
                float f55 = f49;
                float f56 = f50;
                float f57 = f32;
                float f58 = f54;
                float f59 = f55;
                float f60 = f56;
                float f61 = f57;
                if (f61 < 0.0f) {
                    f61 = 0.0f;
                } else if (f61 > 1.0f) {
                    f61 = 1.0f;
                }
                if (f58 < 0.0f) {
                    f58 = 0.0f;
                } else if (f58 > f61) {
                    f58 = f61;
                }
                if (f59 < 0.0f) {
                    f59 = 0.0f;
                } else if (f59 > f61) {
                    f59 = f61;
                }
                if (f60 < 0.0f) {
                    f60 = 0.0f;
                } else if (f60 > f61) {
                    f60 = f61;
                }
                arrn3[n14 + j] = (int)(f58 * 255.0f) << 16 | (int)(f59 * 255.0f) << 8 | (int)(f60 * 255.0f) << 0 | (int)(f61 * 255.0f) << 24;
                f9 += f2;
                f10 += f4;
            }
            f6 += f3;
            f7 += f5;
        }
        arrimageData[0].releaseTransformedImage(heapImage);
        arrimageData[1].releaseTransformedImage(heapImage2);
        return new ImageData(this.getFilterContext(), heapImage3, rectangle2);
    }
}

