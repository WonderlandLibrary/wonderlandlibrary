package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.Merge;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public class PrMergePeer extends EffectPeer {
   public PrMergePeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   public ImageData filter(Effect var1, RenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5) {
      FilterContext var6 = this.getFilterContext();
      Merge var7 = (Merge)var1;
      Rectangle var8 = var7.getResultBounds(var3, var4, var5);
      PrDrawable var9 = (PrDrawable)this.getRenderer().getCompatibleImage(var8.width, var8.height);
      if (var9 == null) {
         return new ImageData(var6, (Filterable)null, var8);
      } else {
         Graphics var10 = var9.createGraphics();
         ImageData[] var11 = var5;
         int var12 = var5.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            ImageData var14 = var11[var13];
            PrEffectHelper.renderImageData(var10, var14, var8);
         }

         return new ImageData(var6, var9, var8);
      }
   }
}
