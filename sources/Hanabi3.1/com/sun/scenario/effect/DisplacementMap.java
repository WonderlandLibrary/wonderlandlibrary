package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.RenderState;

public class DisplacementMap extends CoreEffect {
   private FloatMap mapData;
   private float scaleX;
   private float scaleY;
   private float offsetX;
   private float offsetY;
   private boolean wrap;

   public DisplacementMap(FloatMap var1) {
      this(var1, DefaultInput);
   }

   public DisplacementMap(FloatMap var1, Effect var2) {
      super(var2);
      this.scaleX = 1.0F;
      this.scaleY = 1.0F;
      this.offsetX = 0.0F;
      this.offsetY = 0.0F;
      this.setMapData(var1);
      this.updatePeerKey("DisplacementMap");
   }

   public final FloatMap getMapData() {
      return this.mapData;
   }

   public void setMapData(FloatMap var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Map data must be non-null");
      } else {
         FloatMap var2 = this.mapData;
         this.mapData = var1;
      }
   }

   public final Effect getContentInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setContentInput(Effect var1) {
      this.setInput(0, var1);
   }

   public float getScaleX() {
      return this.scaleX;
   }

   public void setScaleX(float var1) {
      float var2 = this.scaleX;
      this.scaleX = var1;
   }

   public float getScaleY() {
      return this.scaleY;
   }

   public void setScaleY(float var1) {
      float var2 = this.scaleY;
      this.scaleY = var1;
   }

   public float getOffsetX() {
      return this.offsetX;
   }

   public void setOffsetX(float var1) {
      float var2 = this.offsetX;
      this.offsetX = var1;
   }

   public float getOffsetY() {
      return this.offsetY;
   }

   public void setOffsetY(float var1) {
      float var2 = this.offsetY;
      this.offsetY = var1;
   }

   public boolean getWrap() {
      return this.wrap;
   }

   public void setWrap(boolean var1) {
      boolean var2 = this.wrap;
      this.wrap = var1;
   }

   public Point2D transform(Point2D var1, Effect var2) {
      return new Point2D(Float.NaN, Float.NaN);
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      BaseBounds var3 = this.getBounds(BaseTransform.IDENTITY_TRANSFORM, var2);
      float var4 = var3.getWidth();
      float var5 = var3.getHeight();
      float var6 = (var1.x - var3.getMinX()) / var4;
      float var7 = (var1.y - var3.getMinY()) / var5;
      if (var6 >= 0.0F && var7 >= 0.0F && var6 < 1.0F && var7 < 1.0F) {
         int var8 = (int)(var6 * (float)this.mapData.getWidth());
         int var9 = (int)(var7 * (float)this.mapData.getHeight());
         float var10 = this.mapData.getSample(var8, var9, 0);
         float var11 = this.mapData.getSample(var8, var9, 1);
         var6 += this.scaleX * (var10 + this.offsetX);
         var7 += this.scaleY * (var11 + this.offsetY);
         if (this.wrap) {
            var6 = (float)((double)var6 - Math.floor((double)var6));
            var7 = (float)((double)var7 - Math.floor((double)var7));
         }

         var1 = new Point2D(var6 * var4 + var3.getMinX(), var7 * var5 + var3.getMinY());
      }

      return this.getDefaultedInput(0, var2).untransform(var1, var2);
   }

   public ImageData filterImageDatas(FilterContext var1, BaseTransform var2, Rectangle var3, RenderState var4, ImageData... var5) {
      return super.filterImageDatas(var1, var2, (Rectangle)null, var4, var5);
   }

   public RenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return RenderState.UnclippedUserSpaceRenderState;
   }

   public boolean reducesOpaquePixels() {
      return true;
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      DirtyRegionContainer var3 = var2.checkOut();
      var3.deriveWithNewRegion((RectBounds)this.getBounds(BaseTransform.IDENTITY_TRANSFORM, var1));
      return var3;
   }
}
