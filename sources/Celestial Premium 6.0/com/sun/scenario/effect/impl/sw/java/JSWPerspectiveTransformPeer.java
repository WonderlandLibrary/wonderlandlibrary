/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.PerspectiveTransform;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.AccessHelper;
import com.sun.scenario.effect.impl.state.PerspectiveTransformState;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWPerspectiveTransformPeer
extends JSWEffectPeer {
    public JSWPerspectiveTransformPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    protected final PerspectiveTransform getEffect() {
        return (PerspectiveTransform)super.getEffect();
    }

    private float[][] getITX() {
        PerspectiveTransformState perspectiveTransformState = (PerspectiveTransformState)AccessHelper.getState(this.getEffect());
        return perspectiveTransformState.getITX();
    }

    private float[] getTx0() {
        Rectangle rectangle = this.getInputBounds(0);
        Rectangle rectangle2 = this.getInputNativeBounds(0);
        float f = (float)rectangle.width / (float)rectangle2.width;
        float[] arrf = this.getITX()[0];
        return new float[]{arrf[0] * f, arrf[1] * f, arrf[2] * f};
    }

    private float[] getTx1() {
        Rectangle rectangle = this.getInputBounds(0);
        Rectangle rectangle2 = this.getInputNativeBounds(0);
        float f = (float)rectangle.height / (float)rectangle2.height;
        float[] arrf = this.getITX()[1];
        return new float[]{arrf[0] * f, arrf[1] * f, arrf[2] * f};
    }

    private float[] getTx2() {
        return this.getITX()[2];
    }

    @Override
    public int getTextureCoordinates(int n, float[] arrf, float f, float f2, float f3, float f4, Rectangle rectangle, BaseTransform baseTransform) {
        arrf[0] = rectangle.x;
        arrf[1] = rectangle.y;
        arrf[2] = rectangle.x + rectangle.width;
        arrf[3] = rectangle.y + rectangle.height;
        return 4;
    }

    @Override
    public ImageData filter(Effect effect, RenderState renderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        this.setEffect(effect);
        Rectangle rectangle2 = this.getResultBounds(baseTransform, rectangle, arrimageData);
        this.setDestBounds(rectangle2);
        HeapImage heapImage = (HeapImage)arrimageData[0].getUntransformedImage();
        int n = 0;
        int n2 = 0;
        int n3 = heapImage.getPhysicalWidth();
        int n4 = heapImage.getPhysicalHeight();
        int n5 = heapImage.getScanlineStride();
        int[] arrn = heapImage.getPixelArray();
        Rectangle rectangle3 = new Rectangle(n, n2, n3, n4);
        Rectangle rectangle4 = arrimageData[0].getUntransformedBounds();
        BaseTransform baseTransform2 = arrimageData[0].getTransform();
        this.setInputBounds(0, rectangle4);
        this.setInputNativeBounds(0, rectangle3);
        float[] arrf = new float[4];
        float[] arrf2 = new float[4];
        this.getTextureCoordinates(0, arrf2, rectangle4.x, rectangle4.y, n3, n4, rectangle2, baseTransform2);
        int n6 = rectangle2.width;
        int n7 = rectangle2.height;
        HeapImage heapImage2 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n6, n7));
        this.setDestNativeBounds(heapImage2.getPhysicalWidth(), heapImage2.getPhysicalHeight());
        int n8 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        float[] arrf3 = this.getTx1();
        float f = arrf3[0];
        float f2 = arrf3[1];
        float f3 = arrf3[2];
        float[] arrf4 = this.getTx0();
        float f4 = arrf4[0];
        float f5 = arrf4[1];
        float f6 = arrf4[2];
        float[] arrf5 = this.getTx2();
        float f7 = arrf5[0];
        float f8 = arrf5[1];
        float f9 = arrf5[2];
        float f10 = (arrf2[2] - arrf2[0]) / (float)n6;
        float f11 = (arrf2[3] - arrf2[1]) / (float)n7;
        float f12 = arrf2[1] + f11 * 0.5f;
        for (int i = 0; i < 0 + n7; ++i) {
            float f13 = i;
            int n9 = i * n8;
            float f14 = arrf2[0] + f10 * 0.5f;
            for (int j = 0; j < 0 + n6; ++j) {
                float f15;
                float f16 = j;
                float f17 = f14;
                float f18 = f12;
                float f19 = 1.0f;
                float f20 = f17;
                float f21 = f18;
                float f22 = f19;
                float f23 = f7;
                float f24 = f8;
                float f25 = f9;
                float f26 = f15 = f20 * f23 + f21 * f24 + f22 * f25;
                f20 = f17;
                f21 = f18;
                f22 = f19;
                f23 = f4;
                f24 = f5;
                f25 = f6;
                f15 = f20 * f23 + f21 * f24 + f22 * f25;
                float f27 = f15 / f26;
                f20 = f17;
                f21 = f18;
                f22 = f19;
                f23 = f;
                f24 = f2;
                f25 = f3;
                f15 = f20 * f23 + f21 * f24 + f22 * f25;
                float f28 = f15 / f26;
                f24 = f27;
                f25 = f28;
                this.lsample(arrn, f24, f25, n3, n4, n5, arrf);
                f20 = arrf[0];
                f21 = arrf[1];
                f22 = arrf[2];
                f23 = arrf[3];
                float f29 = f20;
                float f30 = f21;
                float f31 = f22;
                float f32 = f23;
                if (f32 < 0.0f) {
                    f32 = 0.0f;
                } else if (f32 > 1.0f) {
                    f32 = 1.0f;
                }
                if (f29 < 0.0f) {
                    f29 = 0.0f;
                } else if (f29 > f32) {
                    f29 = f32;
                }
                if (f30 < 0.0f) {
                    f30 = 0.0f;
                } else if (f30 > f32) {
                    f30 = f32;
                }
                if (f31 < 0.0f) {
                    f31 = 0.0f;
                } else if (f31 > f32) {
                    f31 = f32;
                }
                arrn2[n9 + j] = (int)(f29 * 255.0f) << 16 | (int)(f30 * 255.0f) << 8 | (int)(f31 * 255.0f) << 0 | (int)(f32 * 255.0f) << 24;
                f14 += f10;
            }
            f12 += f11;
        }
        return new ImageData(this.getFilterContext(), heapImage2, rectangle2);
    }
}

