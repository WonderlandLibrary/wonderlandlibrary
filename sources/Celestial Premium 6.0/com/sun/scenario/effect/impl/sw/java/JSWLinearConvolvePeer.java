/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.java;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;
import java.nio.FloatBuffer;

public class JSWLinearConvolvePeer
extends JSWEffectPeer<LinearConvolveRenderState> {
    private static final float cmin = 1.0f;
    private static final float cmax = 254.9375f;

    public JSWLinearConvolvePeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    private Rectangle getResultBounds(LinearConvolveRenderState linearConvolveRenderState, Rectangle rectangle, ImageData ... arrimageData) {
        Rectangle rectangle2 = arrimageData[0].getTransformedBounds(null);
        rectangle2 = linearConvolveRenderState.getPassResultBounds(rectangle2, rectangle);
        return rectangle2;
    }

    @Override
    public ImageData filter(Effect effect, LinearConvolveRenderState linearConvolveRenderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        this.setRenderState(linearConvolveRenderState);
        Rectangle rectangle2 = this.getResultBounds(linearConvolveRenderState, null, arrimageData);
        Rectangle rectangle3 = new Rectangle(rectangle2);
        rectangle3.intersectWith(rectangle);
        this.setDestBounds(rectangle3);
        int n = rectangle3.width;
        int n2 = rectangle3.height;
        HeapImage heapImage = (HeapImage)arrimageData[0].getUntransformedImage();
        int n3 = heapImage.getPhysicalWidth();
        int n4 = heapImage.getPhysicalHeight();
        int n5 = heapImage.getScanlineStride();
        int[] arrn = heapImage.getPixelArray();
        Rectangle rectangle4 = arrimageData[0].getUntransformedBounds();
        BaseTransform baseTransform2 = arrimageData[0].getTransform();
        Rectangle rectangle5 = new Rectangle(0, 0, n3, n4);
        this.setInputBounds(0, rectangle4);
        this.setInputTransform(0, baseTransform2);
        this.setInputNativeBounds(0, rectangle5);
        HeapImage heapImage2 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n, n2));
        this.setDestNativeBounds(heapImage2.getPhysicalWidth(), heapImage2.getPhysicalHeight());
        int n6 = heapImage2.getScanlineStride();
        int[] arrn2 = heapImage2.getPixelArray();
        int n7 = linearConvolveRenderState.getPassKernelSize();
        FloatBuffer floatBuffer = linearConvolveRenderState.getPassWeights();
        LinearConvolveRenderState.PassType passType = linearConvolveRenderState.getPassType();
        if (!baseTransform2.isIdentity() || !rectangle3.contains(rectangle2.x, rectangle2.y)) {
            passType = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
        }
        if (n7 >= 0) {
            passType = LinearConvolveRenderState.PassType.GENERAL_VECTOR;
        }
        if (passType == LinearConvolveRenderState.PassType.HORIZONTAL_CENTERED) {
            float[] arrf = new float[n7 * 2];
            floatBuffer.get(arrf, 0, n7);
            floatBuffer.rewind();
            floatBuffer.get(arrf, n7, n7);
            this.filterHV(arrn2, n, n2, 1, n6, arrn, n3, n4, 1, n5, arrf);
        } else if (passType == LinearConvolveRenderState.PassType.VERTICAL_CENTERED) {
            float[] arrf = new float[n7 * 2];
            floatBuffer.get(arrf, 0, n7);
            floatBuffer.rewind();
            floatBuffer.get(arrf, n7, n7);
            this.filterHV(arrn2, n2, n, n6, 1, arrn, n4, n3, n5, 1, arrf);
        } else {
            float f;
            float f2;
            float f3;
            float f4;
            float[] arrf = new float[n7];
            floatBuffer.get(arrf, 0, n7);
            float[] arrf2 = new float[8];
            int n8 = this.getTextureCoordinates(0, arrf2, rectangle4.x, rectangle4.y, rectangle5.width, rectangle5.height, rectangle3, baseTransform2);
            float f5 = arrf2[0] * (float)n3;
            float f6 = arrf2[1] * (float)n4;
            if (n8 < 8) {
                f4 = (arrf2[2] - arrf2[0]) * (float)n3 / (float)rectangle3.width;
                f3 = 0.0f;
                f2 = 0.0f;
                f = (arrf2[3] - arrf2[1]) * (float)n4 / (float)rectangle3.height;
            } else {
                f4 = (arrf2[4] - arrf2[0]) * (float)n3 / (float)rectangle3.width;
                f3 = (arrf2[5] - arrf2[1]) * (float)n4 / (float)rectangle3.height;
                f2 = (arrf2[6] - arrf2[0]) * (float)n3 / (float)rectangle3.width;
                f = (arrf2[7] - arrf2[1]) * (float)n4 / (float)rectangle3.height;
            }
            float[] arrf3 = linearConvolveRenderState.getPassVector();
            float f7 = arrf3[0] * (float)n3;
            float f8 = arrf3[1] * (float)n4;
            float f9 = arrf3[2] * (float)n3;
            float f10 = arrf3[3] * (float)n4;
            this.filterVector(arrn2, n, n2, n6, arrn, n3, n4, n5, arrf, n7, f5, f6, f9, f10, f7, f8, f4, f3, f2, f);
        }
        return new ImageData(this.getFilterContext(), heapImage2, rectangle3);
    }

    protected void filterVector(int[] arrn, int n, int n2, int n3, int[] arrn2, int n4, int n5, int n6, float[] arrf, int n7, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10) {
        int n8 = 0;
        float[] arrf2 = new float[4];
        f += (f9 + f7) * 0.5f;
        f2 += (f10 + f8) * 0.5f;
        for (int i = 0; i < n2; ++i) {
            float f11 = f;
            float f12 = f2;
            for (int j = 0; j < n; ++j) {
                arrf2[3] = 0.0f;
                arrf2[2] = 0.0f;
                arrf2[1] = 0.0f;
                arrf2[0] = 0.0f;
                float f13 = f11 + f3;
                float f14 = f12 + f4;
                for (int k = 0; k < n7; ++k) {
                    this.laccumsample(arrn2, f13, f14, n4, n5, n6, arrf[k], arrf2);
                    f13 += f5;
                    f14 += f6;
                }
                arrn[n8 + j] = ((arrf2[3] < 1.0f ? 0 : (arrf2[3] > 254.9375f ? 255 : (int)arrf2[3])) << 24) + ((arrf2[0] < 1.0f ? 0 : (arrf2[0] > 254.9375f ? 255 : (int)arrf2[0])) << 16) + ((arrf2[1] < 1.0f ? 0 : (arrf2[1] > 254.9375f ? 255 : (int)arrf2[1])) << 8) + (arrf2[2] < 1.0f ? 0 : (arrf2[2] > 254.9375f ? 255 : (int)arrf2[2]));
                f11 += f7;
                f12 += f8;
            }
            f += f9;
            f2 += f10;
            n8 += n3;
        }
    }

    protected void filterHV(int[] arrn, int n, int n2, int n3, int n4, int[] arrn2, int n5, int n6, int n7, int n8, float[] arrf) {
        int n9 = arrf.length / 2;
        float[] arrf2 = new float[n9 * 4];
        int n10 = 0;
        int n11 = 0;
        for (int i = 0; i < n2; ++i) {
            int n12;
            int n13 = n10;
            int n14 = n11;
            for (n12 = 0; n12 < arrf2.length; ++n12) {
                arrf2[n12] = 0.0f;
            }
            n12 = n9;
            for (int j = 0; j < n; ++j) {
                int n15 = (n9 - n12) * 4;
                int n16 = j < n5 ? arrn2[n14] : 0;
                arrf2[n15 + 0] = n16 >>> 24;
                arrf2[n15 + 1] = n16 >> 16 & 0xFF;
                arrf2[n15 + 2] = n16 >> 8 & 0xFF;
                arrf2[n15 + 3] = n16 & 0xFF;
                if (--n12 <= 0) {
                    n12 += n9;
                }
                float f = 0.0f;
                float f2 = 0.0f;
                float f3 = 0.0f;
                float f4 = 0.0f;
                for (n15 = 0; n15 < arrf2.length; n15 += 4) {
                    float f5 = arrf[n12 + (n15 >> 2)];
                    f += arrf2[n15 + 0] * f5;
                    f2 += arrf2[n15 + 1] * f5;
                    f3 += arrf2[n15 + 2] * f5;
                    f4 += arrf2[n15 + 3] * f5;
                }
                arrn[n13] = ((f < 1.0f ? 0 : (f > 254.9375f ? 255 : (int)f)) << 24) + ((f2 < 1.0f ? 0 : (f2 > 254.9375f ? 255 : (int)f2)) << 16) + ((f3 < 1.0f ? 0 : (f3 > 254.9375f ? 255 : (int)f3)) << 8) + (f4 < 1.0f ? 0 : (f4 > 254.9375f ? 255 : (int)f4));
                n13 += n3;
                n14 += n7;
            }
            n10 += n4;
            n11 += n8;
        }
    }
}

