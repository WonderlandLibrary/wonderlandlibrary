package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.Reflection;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

public class PrReflectionPeer extends EffectPeer {
   public PrReflectionPeer(FilterContext var1, Renderer var2, String var3) {
      super(var1, var2, var3);
   }

   public ImageData filter(Effect var1, RenderState var2, BaseTransform var3, Rectangle var4, ImageData... var5) {
      FilterContext var6 = this.getFilterContext();
      Reflection var7 = (Reflection)var1;
      Rectangle var8 = var5[0].getUntransformedBounds();
      int var9 = var8.width;
      int var10 = var8.height;
      float var11 = (float)var10 + var7.getTopOffset();
      float var12 = var7.getFraction() * (float)var10;
      int var13 = (int)Math.floor((double)var11);
      int var14 = (int)Math.ceil((double)(var11 + var12));
      int var15 = var14 - var13;
      int var16 = var14 > var10 ? var14 : var10;
      PrDrawable var17 = (PrDrawable)this.getRenderer().getCompatibleImage(var9, var16);
      if (var5[0].validate(var6) && var17 != null) {
         PrDrawable var18 = (PrDrawable)var5[0].getUntransformedImage();
         Texture var19 = var18.getTextureObject();
         Graphics var20 = var17.createGraphics();
         var20.transform(var5[0].getTransform());
         float var21 = 0.0F;
         float var22 = (float)(var10 - var15);
         float var23 = (float)var9;
         float var24 = (float)var10;
         var20.drawTextureVO(var19, var7.getBottomOpacity(), var7.getTopOpacity(), 0.0F, (float)var14, (float)var9, (float)var13, var21, var22, var23, var24);
         var20.drawTexture(var19, 0.0F, 0.0F, (float)var9, (float)var10);
         Rectangle var25 = new Rectangle(var8.x, var8.y, var9, var16);
         return new ImageData(var6, var17, var25);
      } else {
         return new ImageData(var6, (Filterable)null, var8);
      }
   }
}
