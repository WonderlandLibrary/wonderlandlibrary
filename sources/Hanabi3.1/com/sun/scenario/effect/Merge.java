package com.sun.scenario.effect;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.RenderState;

public class Merge extends CoreEffect {
   public Merge(Effect var1, Effect var2) {
      super(var1, var2);
      this.updatePeerKey("Merge");
   }

   public final Effect getBottomInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setBottomInput(Effect var1) {
      this.setInput(0, var1);
   }

   public final Effect getTopInput() {
      return (Effect)this.getInputs().get(1);
   }

   public void setTopInput(Effect var1) {
      this.setInput(1, var1);
   }

   public Point2D transform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(1, var2).transform(var1, var2);
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(1, var2).untransform(var1, var2);
   }

   public ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      Effect var6 = this.getDefaultedInput(0, var5);
      Effect var7 = this.getDefaultedInput(1, var5);
      ImageData var8 = var6.filter(var1, var2, var3, var4, var5);
      if (var8 != null) {
         if (!var8.validate(var1)) {
            return new ImageData(var1, (Filterable)null, (Rectangle)null);
         }

         if (var4 instanceof ImageDataRenderer) {
            ImageDataRenderer var9 = (ImageDataRenderer)var4;
            var9.renderImage(var8, BaseTransform.IDENTITY_TRANSFORM, var1);
            var8.unref();
            var8 = null;
         }
      }

      if (var8 == null) {
         return var7.filter(var1, var2, var3, var4, var5);
      } else {
         ImageData var12 = var7.filter(var1, var2, var3, (Object)null, var5);
         if (!var12.validate(var1)) {
            return new ImageData(var1, (Filterable)null, (Rectangle)null);
         } else {
            RenderState var10 = this.getRenderState(var1, var2, var3, var4, var5);
            ImageData var11 = this.filterImageDatas(var1, var2, var3, var10, new ImageData[]{var8, var12});
            var8.unref();
            var12.unref();
            return var11;
         }
      }
   }

   public RenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return RenderState.RenderSpaceRenderState;
   }

   public boolean reducesOpaquePixels() {
      Effect var1 = this.getTopInput();
      Effect var2 = this.getBottomInput();
      return var1 != null && var1.reducesOpaquePixels() && var2 != null && var2.reducesOpaquePixels();
   }
}
