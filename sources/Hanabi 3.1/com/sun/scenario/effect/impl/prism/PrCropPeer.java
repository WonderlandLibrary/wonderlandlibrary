package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public class PrCropPeer extends EffectPeer {
   public PrCropPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   public ImageData filter(Effect var1, RenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5) {
      FilterContext var6 = this.getFilterContext();
      ImageData var7 = var5[0];
      Rectangle var8 = var7.getTransformedBounds((Rectangle)null);
      if (var4.contains(var8)) {
         var7.addref();
         return var7;
      } else {
         Rectangle var9 = new Rectangle(var8);
         var9.intersectWith(var4);
         int var10 = var9.width;
         int var11 = var9.height;
         PrDrawable var12 = (PrDrawable)this.getRenderer().getCompatibleImage(var10, var11);
         if (var7.validate(var6) && var12 != null) {
            Graphics var13 = var12.createGraphics();
            PrEffectHelper.renderImageData(var13, var7, var9);
         } else {
            var12 = null;
         }

         return new ImageData(var6, var12, var9);
      }
   }
}
