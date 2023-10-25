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
import com.sun.scenario.effect.impl.state.BoxRenderState;
import com.sun.scenario.effect.impl.sw.java.JSWEffectPeer;

public class JSWBoxShadowPeer
extends JSWEffectPeer<BoxRenderState> {
    public JSWBoxShadowPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    public ImageData filter(Effect effect, BoxRenderState boxRenderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        boolean bl;
        int n;
        this.setRenderState(boxRenderState);
        boolean bl2 = this.getPass() == 0;
        int n2 = bl2 ? boxRenderState.getBoxPixelSize(0) - 1 : 0;
        int n3 = n = bl2 ? 0 : boxRenderState.getBoxPixelSize(1) - 1;
        if (n2 < 0) {
            n2 = 0;
        }
        if (n < 0) {
            n = 0;
        }
        int n4 = boxRenderState.getBlurPasses();
        float f = boxRenderState.getSpread();
        if (bl2 && (n4 < 1 || n2 < 1 && n < 1)) {
            arrimageData[0].addref();
            return arrimageData[0];
        }
        int n5 = n2 * n4 + 1 & 0xFFFFFFFE;
        int n6 = n * n4 + 1 & 0xFFFFFFFE;
        HeapImage heapImage = (HeapImage)arrimageData[0].getUntransformedImage();
        Rectangle rectangle2 = arrimageData[0].getUntransformedBounds();
        HeapImage heapImage2 = heapImage;
        int n7 = rectangle2.width;
        int n8 = rectangle2.height;
        int n9 = heapImage2.getScanlineStride();
        int[] arrn = heapImage2.getPixelArray();
        int n10 = n7 + n5;
        int n11 = n8 + n6;
        boolean bl3 = bl = !bl2;
        while (bl || n7 < n10 || n8 < n11) {
            int n12 = n7 + n2;
            int n13 = n8 + n;
            if (n12 > n10) {
                n12 = n10;
            }
            if (n13 > n11) {
                n13 = n11;
            }
            HeapImage heapImage3 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n12, n13));
            int n14 = heapImage3.getScanlineStride();
            int[] arrn2 = heapImage3.getPixelArray();
            if (n4 == 0) {
                f = 0.0f;
            }
            if (bl2) {
                this.filterHorizontalBlack(arrn2, n12, n13, n14, arrn, n7, n8, n9, f);
            } else if (n12 < n10 || n13 < n11) {
                this.filterVerticalBlack(arrn2, n12, n13, n14, arrn, n7, n8, n9, f);
            } else {
                float[] arrf = boxRenderState.getShadowColor().getPremultipliedRGBComponents();
                if (arrf[3] == 1.0f && arrf[0] == 0.0f && arrf[1] == 0.0f && arrf[2] == 0.0f) {
                    this.filterVerticalBlack(arrn2, n12, n13, n14, arrn, n7, n8, n9, f);
                } else {
                    this.filterVertical(arrn2, n12, n13, n14, arrn, n7, n8, n9, f, arrf);
                }
            }
            if (heapImage2 != heapImage) {
                this.getRenderer().releaseCompatibleImage(heapImage2);
            }
            --n4;
            bl = false;
            heapImage2 = heapImage3;
            n7 = n12;
            n8 = n13;
            arrn = arrn2;
            n9 = n14;
        }
        Rectangle rectangle3 = new Rectangle(rectangle2.x - n5 / 2, rectangle2.y - n6 / 2, n7, n8);
        return new ImageData(this.getFilterContext(), heapImage2, rectangle3);
    }

    protected void filterHorizontalBlack(int[] arrn, int n, int n2, int n3, int[] arrn2, int n4, int n5, int n6, float f) {
        int n7 = n - n4 + 1;
        int n8 = n7 * 255;
        n8 = (int)((float)n8 + (float)(255 - n8) * f);
        int n9 = Integer.MAX_VALUE / n8;
        int n10 = n8 / 255;
        int n11 = 0;
        int n12 = 0;
        for (int i = 0; i < n2; ++i) {
            int n13 = 0;
            for (int j = 0; j < n; ++j) {
                int n14 = j >= n7 ? arrn2[n11 + j - n7] : 0;
                n13 -= n14 >>> 24;
                int n15 = n14 = j < n4 ? arrn2[n11 + j] : 0;
                arrn[n12 + j] = (n13 += n14 >>> 24) < n10 ? 0 : (n13 >= n8 ? -16777216 : n13 * n9 >> 23 << 24);
            }
            n11 += n6;
            n12 += n3;
        }
    }

    protected void filterVerticalBlack(int[] arrn, int n, int n2, int n3, int[] arrn2, int n4, int n5, int n6, float f) {
        int n7 = n2 - n5 + 1;
        int n8 = n7 * 255;
        n8 = (int)((float)n8 + (float)(255 - n8) * f);
        int n9 = Integer.MAX_VALUE / n8;
        int n10 = n8 / 255;
        int n11 = n7 * n6;
        for (int i = 0; i < n; ++i) {
            int n12 = 0;
            int n13 = i;
            int n14 = i;
            for (int j = 0; j < n2; ++j) {
                int n15 = n13 >= n11 ? arrn2[n13 - n11] : 0;
                n12 -= n15 >>> 24;
                int n16 = n15 = j < n5 ? arrn2[n13] : 0;
                arrn[n14] = (n12 += n15 >>> 24) < n10 ? 0 : (n12 >= n8 ? -16777216 : n12 * n9 >> 23 << 24);
                n13 += n6;
                n14 += n3;
            }
        }
    }

    protected void filterVertical(int[] arrn, int n, int n2, int n3, int[] arrn2, int n4, int n5, int n6, float f, float[] arrf) {
        int n7 = n2 - n5 + 1;
        int n8 = n7 * 255;
        n8 = (int)((float)n8 + (float)(255 - n8) * f);
        int n9 = Integer.MAX_VALUE / n8;
        int n10 = (int)((float)n9 * arrf[0]);
        int n11 = (int)((float)n9 * arrf[1]);
        int n12 = (int)((float)n9 * arrf[2]);
        n9 = (int)((float)n9 * arrf[3]);
        int n13 = n8 / 255;
        int n14 = n7 * n6;
        int n15 = (int)(arrf[0] * 255.0f) << 16 | (int)(arrf[1] * 255.0f) << 8 | (int)(arrf[2] * 255.0f) | (int)(arrf[3] * 255.0f) << 24;
        for (int i = 0; i < n; ++i) {
            int n16 = 0;
            int n17 = i;
            int n18 = i;
            for (int j = 0; j < n2; ++j) {
                int n19 = n17 >= n14 ? arrn2[n17 - n14] : 0;
                n16 -= n19 >>> 24;
                int n20 = n19 = j < n5 ? arrn2[n17] : 0;
                arrn[n18] = (n16 += n19 >>> 24) < n13 ? 0 : (n16 >= n8 ? n15 : n16 * n9 >> 23 << 24 | n16 * n10 >> 23 << 16 | n16 * n11 >> 23 << 8 | n16 * n12 >> 23);
                n17 += n6;
                n18 += n3;
            }
        }
    }
}

