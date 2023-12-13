package com.sun.scenario.effect.impl.sw.sse;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;

public class SSELinearConvolveShadowPeer extends SSELinearConvolvePeer {
   public SSELinearConvolveShadowPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   private float[] getShadowColor() {
      return ((LinearConvolveRenderState)this.getRenderState()).getPassShadowColorComponents();
   }

   private static native void filterVector(int[] var0, int var1, int var2, int var3, int[] var4, int var5, int var6, int var7, float[] var8, int var9, float var10, float var11, float var12, float var13, float var14, float var15, float[] var16, float var17, float var18, float var19, float var20);

   void filterVector(int[] var1, int var2, int var3, int var4, int[] var5, int var6, int var7, int var8, float[] var9, int var10, float var11, float var12, float var13, float var14, float var15, float var16, float var17, float var18, float var19, float var20) {
      filterVector(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, this.getShadowColor(), var17, var18, var19, var20);
   }

   private static native void filterHV(int[] var0, int var1, int var2, int var3, int var4, int[] var5, int var6, int var7, int var8, int var9, float[] var10, float[] var11);

   void filterHV(int[] var1, int var2, int var3, int var4, int var5, int[] var6, int var7, int var8, int var9, int var10, float[] var11) {
      filterHV(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, this.getShadowColor());
   }
}
