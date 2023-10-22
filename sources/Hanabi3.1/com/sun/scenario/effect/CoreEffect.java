package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

abstract class CoreEffect extends FilterEffect {
   private String peerKey;
   private int peerCount = -1;

   CoreEffect() {
   }

   CoreEffect(Effect var1) {
      super(var1);
   }

   CoreEffect(Effect var1, Effect var2) {
      super(var1, var2);
   }

   final void updatePeerKey(String var1) {
      this.updatePeerKey(var1, -1);
   }

   final void updatePeerKey(String var1, int var2) {
      this.peerKey = var1;
      this.peerCount = var2;
   }

   private EffectPeer getPeer(FilterContext var1, int var2, int var3) {
      return Renderer.getRenderer(var1, this, var2, var3).getPeerInstance(var1, this.peerKey, this.peerCount);
   }

   final EffectPeer getPeer(FilterContext var1, ImageData[] var2) {
      int var3;
      int var4;
      if (var2.length > 0) {
         Rectangle var5 = var2[0].getUntransformedBounds();
         var3 = var5.width;
         var4 = var5.height;
      } else {
         var4 = 500;
         var3 = 500;
      }

      return this.getPeer(var1, var3, var4);
   }

   public ImageData filterImageDatas(FilterContext var1, BaseTransform var2, Rectangle var3, RenderState var4, ImageData... var5) {
      return this.getPeer(var1, var5).filter(this, var4, var2, var3, var5);
   }

   public Effect.AccelType getAccelType(FilterContext var1) {
      EffectPeer var2 = this.getPeer(var1, 1024, 1024);
      return var2.getAccelType();
   }
}
