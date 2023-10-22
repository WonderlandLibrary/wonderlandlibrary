package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.RenderState;

public class Reflection extends CoreEffect {
   private float topOffset;
   private float topOpacity;
   private float bottomOpacity;
   private float fraction;

   public Reflection() {
      this(DefaultInput);
   }

   public Reflection(Effect var1) {
      super(var1);
      this.topOffset = 0.0F;
      this.topOpacity = 0.5F;
      this.bottomOpacity = 0.0F;
      this.fraction = 0.75F;
      this.updatePeerKey("Reflection");
   }

   public final Effect getInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setInput(Effect var1) {
      this.setInput(0, var1);
   }

   public float getTopOffset() {
      return this.topOffset;
   }

   public void setTopOffset(float var1) {
      float var2 = this.topOffset;
      this.topOffset = var1;
   }

   public float getTopOpacity() {
      return this.topOpacity;
   }

   public void setTopOpacity(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 1.0F)) {
         float var2 = this.topOpacity;
         this.topOpacity = var1;
      } else {
         throw new IllegalArgumentException("Top opacity must be in the range [0,1]");
      }
   }

   public float getBottomOpacity() {
      return this.bottomOpacity;
   }

   public void setBottomOpacity(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 1.0F)) {
         float var2 = this.bottomOpacity;
         this.bottomOpacity = var1;
      } else {
         throw new IllegalArgumentException("Bottom opacity must be in the range [0,1]");
      }
   }

   public float getFraction() {
      return this.fraction;
   }

   public void setFraction(float var1) {
      if (!(var1 < 0.0F) && !(var1 > 1.0F)) {
         float var2 = this.fraction;
         this.fraction = var1;
      } else {
         throw new IllegalArgumentException("Fraction must be in the range [0,1]");
      }
   }

   public BaseBounds getBounds(BaseTransform var1, Effect var2) {
      Effect var3 = this.getDefaultedInput(0, var2);
      BaseBounds var4 = var3.getBounds(BaseTransform.IDENTITY_TRANSFORM, var2);
      var4.roundOut();
      float var5 = var4.getMinX();
      float var6 = var4.getMaxY() + this.topOffset;
      float var7 = var4.getMaxX();
      float var8 = var6 + this.fraction * var4.getHeight();
      RectBounds var9 = new RectBounds(var5, var6, var7, var8);
      BaseBounds var10 = var9.deriveWithUnion(var4);
      return transformBounds(var1, var10);
   }

   public Point2D transform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(0, var2).transform(var1, var2);
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      return this.getDefaultedInput(0, var2).untransform(var1, var2);
   }

   public RenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return RenderState.UnclippedUserSpaceRenderState;
   }

   public boolean reducesOpaquePixels() {
      Effect var1 = this.getInput();
      return var1 != null && var1.reducesOpaquePixels();
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      Effect var3 = this.getDefaultedInput(0, var1);
      DirtyRegionContainer var4 = var3.getDirtyRegions(var1, var2);
      BaseBounds var5 = var3.getBounds(BaseTransform.IDENTITY_TRANSFORM, var1);
      float var6 = var5.getMaxY();
      float var7 = 2.0F * var6 + this.getTopOffset();
      float var8 = var6 + this.getTopOffset() + this.fraction * var5.getHeight();
      DirtyRegionContainer var9 = var2.checkOut();

      for(int var10 = 0; var10 < var4.size(); ++var10) {
         RectBounds var11 = var4.getDirtyRegion(var10);
         float var12 = var7 - var11.getMaxY();
         float var13 = Math.min(var8, var12 + var11.getHeight());
         var9.addDirtyRegion(new RectBounds(var11.getMinX(), var12, var11.getMaxX(), var13));
      }

      var4.merge(var9);
      var2.checkIn(var9);
      return var4;
   }
}
