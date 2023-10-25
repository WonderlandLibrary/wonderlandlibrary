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

public class JSWBoxBlurPeer
extends JSWEffectPeer<BoxRenderState> {
    public JSWBoxBlurPeer(FilterContext filterContext, Renderer renderer, String string) {
        super(filterContext, renderer, string);
    }

    @Override
    public ImageData filter(Effect effect, BoxRenderState boxRenderState, BaseTransform baseTransform, Rectangle rectangle, ImageData ... arrimageData) {
        this.setRenderState(boxRenderState);
        boolean bl = this.getPass() == 0;
        int n = bl ? boxRenderState.getBoxPixelSize(0) - 1 : 0;
        int n2 = bl ? 0 : boxRenderState.getBoxPixelSize(1) - 1;
        int n3 = boxRenderState.getBlurPasses();
        if (n3 < 1 || n < 1 && n2 < 1) {
            arrimageData[0].addref();
            return arrimageData[0];
        }
        int n4 = n * n3 + 1 & 0xFFFFFFFE;
        int n5 = n2 * n3 + 1 & 0xFFFFFFFE;
        HeapImage heapImage = (HeapImage)arrimageData[0].getUntransformedImage();
        Rectangle rectangle2 = arrimageData[0].getUntransformedBounds();
        HeapImage heapImage2 = heapImage;
        int n6 = rectangle2.width;
        int n7 = rectangle2.height;
        int n8 = heapImage2.getScanlineStride();
        int[] arrn = heapImage2.getPixelArray();
        int n9 = n6 + n4;
        int n10 = n7 + n5;
        while (n6 < n9 || n7 < n10) {
            int n11 = n6 + n;
            int n12 = n7 + n2;
            if (n11 > n9) {
                n11 = n9;
            }
            if (n12 > n10) {
                n12 = n10;
            }
            HeapImage heapImage3 = (HeapImage)((Object)this.getRenderer().getCompatibleImage(n11, n12));
            int n13 = heapImage3.getScanlineStride();
            int[] arrn2 = heapImage3.getPixelArray();
            if (bl) {
                this.filterHorizontal(arrn2, n11, n12, n13, arrn, n6, n7, n8);
            } else {
                this.filterVertical(arrn2, n11, n12, n13, arrn, n6, n7, n8);
            }
            if (heapImage2 != heapImage) {
                this.getRenderer().releaseCompatibleImage(heapImage2);
            }
            heapImage2 = heapImage3;
            n6 = n11;
            n7 = n12;
            arrn = arrn2;
            n8 = n13;
        }
        Rectangle rectangle3 = new Rectangle(rectangle2.x - n4 / 2, rectangle2.y - n5 / 2, n6, n7);
        return new ImageData(this.getFilterContext(), heapImage2, rectangle3);
    }

    protected void filterHorizontal(int[] arrn, int n, int n2, int n3, int[] arrn2, int n4, int n5, int n6) {
        int n7 = n - n4 + 1;
        int n8 = Integer.MAX_VALUE / (n7 * 255);
        int n9 = 0;
        int n10 = 0;
        for (int i = 0; i < n2; ++i) {
            int n11 = 0;
            int n12 = 0;
            int n13 = 0;
            int n14 = 0;
            for (int j = 0; j < n; ++j) {
                int n15 = j >= n7 ? arrn2[n9 + j - n7] : 0;
                n11 -= n15 >>> 24;
                n12 -= n15 >> 16 & 0xFF;
                n13 -= n15 >> 8 & 0xFF;
                n14 -= n15 & 0xFF;
                n15 = j < n4 ? arrn2[n9 + j] : 0;
                arrn[n10 + j] = ((n11 += n15 >>> 24) * n8 >> 23 << 24) + ((n12 += n15 >> 16 & 0xFF) * n8 >> 23 << 16) + ((n13 += n15 >> 8 & 0xFF) * n8 >> 23 << 8) + ((n14 += n15 & 0xFF) * n8 >> 23);
            }
            n9 += n6;
            n10 += n3;
        }
    }

    protected void filterVertical(int[] arrn, int n, int n2, int n3, int[] arrn2, int n4, int n5, int n6) {
        int n7 = n2 - n5 + 1;
        int n8 = Integer.MAX_VALUE / (n7 * 255);
        int n9 = n7 * n6;
        for (int i = 0; i < n; ++i) {
            int n10 = 0;
            int n11 = 0;
            int n12 = 0;
            int n13 = 0;
            int n14 = i;
            int n15 = i;
            for (int j = 0; j < n2; ++j) {
                int n16 = n14 >= n9 ? arrn2[n14 - n9] : 0;
                n10 -= n16 >>> 24;
                n11 -= n16 >> 16 & 0xFF;
                n12 -= n16 >> 8 & 0xFF;
                n13 -= n16 & 0xFF;
                n16 = j < n5 ? arrn2[n14] : 0;
                arrn[n15] = ((n10 += n16 >>> 24) * n8 >> 23 << 24) + ((n11 += n16 >> 16 & 0xFF) * n8 >> 23 << 16) + ((n12 += n16 >> 8 & 0xFF) * n8 >> 23 << 8) + ((n13 += n16 & 0xFF) * n8 >> 23);
                n14 += n6;
                n15 += n3;
            }
        }
    }
}

