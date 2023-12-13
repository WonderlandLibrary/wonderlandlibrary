package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.paint.Paint;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Flood;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public class PrFloodPeer extends EffectPeer {
   public PrFloodPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   public ImageData filter(Effect var1, RenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5) {
      FilterContext var6 = this.getFilterContext();
      Flood var7 = (Flood)var1;
      RectBounds var8 = var7.getFloodBounds();
      int var9 = (int)var8.getMinX();
      int var10 = (int)var8.getMinY();
      int var11 = (int)var8.getWidth();
      int var12 = (int)var8.getHeight();
      BaseBounds var13 = Effect.transformBounds(var3, var8);
      Rectangle var14 = new Rectangle(var13);
      var14.intersectWith(var4);
      int var15 = var14.width;
      int var16 = var14.height;
      PrDrawable var17 = (PrDrawable)this.getRenderer().getCompatibleImage(var15, var16);
      if (var17 != null) {
         Graphics var18 = var17.createGraphics();
         var18.translate((float)(-var14.x), (float)(-var14.y));
         if (var3 != null && !var3.isIdentity()) {
            var18.transform(var3);
         }

         var18.setPaint((Paint)var7.getPaint());
         var18.fillQuad((float)var9, (float)var10, (float)(var9 + var11), (float)(var10 + var12));
      }

      return new ImageData(var6, var17, var14);
   }
}
