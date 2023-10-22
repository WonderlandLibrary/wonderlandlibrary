package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.RenderState;

public class Crop extends CoreEffect {
   public Crop(Effect var1) {
      this(var1, DefaultInput);
   }

   public Crop(Effect var1, Effect var2) {
      super(var1, var2);
      this.updatePeerKey("Crop");
   }

   public final Effect getInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setInput(Effect var1) {
      this.setInput(0, var1);
   }

   public final Effect getBoundsInput() {
      return (Effect)this.getInputs().get(1);
   }

   public void setBoundsInput(Effect var1) {
      this.setInput(1, var1);
   }

   private Effect getBoundsInput(Effect var1) {
      return this.getDefaultedInput(1, var1);
   }

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      return this.getBoundsInput(var2).getBounds(var1, var2);
   }

   public Point2D transform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(0, var2).transform(var1, var2);
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(0, var2).untransform(var1, var2);
   }

   public ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      Effect var6 = this.getDefaultedInput(1, var5);
      BaseBounds var7 = var6.getBounds(var2, var5);
      Rectangle var8 = new Rectangle(var7);
      var8.intersectWith(var3);
      Effect var9 = this.getDefaultedInput(0, var5);
      ImageData var10 = var9.filter(var1, var2, var8, (Object)null, var5);
      if (!var10.validate(var1)) {
         return new ImageData(var1, (Filterable)null, (Rectangle)null);
      } else {
         RenderState var11 = this.getRenderState(var1, var2, var8, var4, var5);
         ImageData var12 = this.filterImageDatas(var1, var2, var8, var11, new ImageData[]{var10});
         var10.unref();
         return var12;
      }
   }

   public RenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return RenderState.RenderSpaceRenderState;
   }

   public boolean reducesOpaquePixels() {
      return true;
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      Effect var3 = this.getDefaultedInput(0, var1);
      DirtyRegionContainer var4 = var3.getDirtyRegions(var1, var2);
      Effect var5 = this.getDefaultedInput(1, var1);
      BaseBounds var6 = var5.getBounds(BaseTransform.IDENTITY_TRANSFORM, var1);

      for(int var7 = 0; var7 < var4.size(); ++var7) {
         var4.getDirtyRegion(var7).intersectWith(var6);
         if (var4.checkAndClearRegion(var7)) {
            --var7;
         }
      }

      return var4;
   }
}
