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

public class SSEBoxBlurPeer
extends SSEEffectPeer<BoxRenderState> {
    public SSEBoxBlurPeer(FilterContext filterContext, Renderer renderer, String string) {
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
                SSEBoxBlurPeer.filterHorizontal(arrn2, n11, n12, n13, arrn, n6, n7, n8);
            } else {
                SSEBoxBlurPeer.filterVertical(arrn2, n11, n12, n13, arrn, n6, n7, n8);
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

    private static native void filterHorizontal(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7);

    private static native void filterVertical(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7);
}

