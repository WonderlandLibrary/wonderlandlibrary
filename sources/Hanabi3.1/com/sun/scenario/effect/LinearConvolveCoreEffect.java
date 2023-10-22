package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;

public abstract class LinearConvolveCoreEffect extends CoreEffect {
   public LinearConvolveCoreEffect(Effect var1) {
      super(var1);
   }

   public final LinearConvolveRenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return this.getState().getRenderState(var2);
   }

   abstract LinearConvolveKernel getState();

   public ImageData filterImageDatas(FilterContext var1, BaseTransform var2, Rectangle var3, LinearConvolveRenderState var4, ImageData... var5) {
      ImageData var6 = var5[0];
      var6.addref();
      if (var4.isNop()) {
         return var6;
      } else {
         Rectangle var7 = var5[0].getUntransformedBounds();
         int var8 = var7.width;
         int var9 = var7.height;
         Rectangle var10 = var3;
         Renderer var11 = Renderer.getRenderer(var1, this, var8, var9);

         for(int var12 = 0; var12 < 2; ++var12) {
            var6 = var4.validatePassInput(var6, var12);
            EffectPeer var13 = var4.getPassPeer(var11, var1);
            if (var13 != null) {
               var13.setPass(var12);
               ImageData var14 = var13.filter(this, var4, var2, var10, var6);
               var6.unref();
               var6 = var14;
               if (!var14.validate(var1)) {
                  var14.unref();
                  return var14;
               }
            }
         }

         return var6;
      }
   }
}
