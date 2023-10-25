/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect.impl.sw.sse;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.HeapImage;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxRenderState;
import com.sun.scenario.effect.impl.sw.sse.SSEEffectPeer;

public class SSEBoxShadowPeer
extends SSEEffectPeer<BoxRenderState> {
    public SSEBoxShadowPeer(FilterContext filterContext, Renderer renderer, String string) {
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
                SSEBoxShadowPeer.filterHorizontalBlack(arrn2, n12, n13, n14, arrn, n7, n8, n9, f);
            } else if (n12 < n10 || n13 < n11) {
                SSEBoxShadowPeer.filterVerticalBlack(arrn2, n12, n13, n14, arrn, n7, n8, n9, f);
            } else {
                float[] arrf = boxRenderState.getShadowColor().getPremultipliedRGBComponents();
                if (arrf[3] == 1.0f && arrf[0] == 0.0f && arrf[1] == 0.0f && arrf[2] == 0.0f) {
                    SSEBoxShadowPeer.filterVerticalBlack(arrn2, n12, n13, n14, arrn, n7, n8, n9, f);
                } else {
                    SSEBoxShadowPeer.filterVertical(arrn2, n12, n13, n14, arrn, n7, n8, n9, f, arrf);
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
        return new ImageData(this.getFilterContext(), heapImage2, rectangle3, arrimageData[0].getTransform());
    }

    private static native void filterHorizontalBlack(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7, float var8);

    private static native void filterVerticalBlack(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7, float var8);

    private static native void filterVertical(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7, float var8, float[] var9);
}

