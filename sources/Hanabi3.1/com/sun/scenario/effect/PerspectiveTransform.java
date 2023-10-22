package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.impl.state.PerspectiveTransformState;
import com.sun.scenario.effect.impl.state.RenderState;

public class PerspectiveTransform extends CoreEffect {
   private float[][] tx;
   private float ulx;
   private float uly;
   private float urx;
   private float ury;
   private float lrx;
   private float lry;
   private float llx;
   private float lly;
   private float[] devcoords;
   private final PerspectiveTransformState state;

   public PerspectiveTransform() {
      this(DefaultInput);
   }

   public PerspectiveTransform(Effect var1) {
      super(var1);
      this.tx = new float[3][3];
      this.devcoords = new float[8];
      this.state = new PerspectiveTransformState();
      this.setQuadMapping(0.0F, 0.0F, 100.0F, 0.0F, 100.0F, 100.0F, 0.0F, 100.0F);
      this.updatePeerKey("PerspectiveTransform");
   }

   Object getState() {
      return this.state;
   }

   public final Effect getInput() {
      return (Effect)this.getInputs().get(0);
   }

   public void setInput(Effect var1) {
      this.setInput(0, var1);
   }

   private void setUnitQuadMapping(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      float var9 = var1 - var3 + var5 - var7;
      float var10 = var2 - var4 + var6 - var8;
      this.tx[2][2] = 1.0F;
      if (var9 == 0.0F && var10 == 0.0F) {
         this.tx[0][0] = var3 - var1;
         this.tx[0][1] = var5 - var3;
         this.tx[0][2] = var1;
         this.tx[1][0] = var4 - var2;
         this.tx[1][1] = var6 - var4;
         this.tx[1][2] = var2;
         this.tx[2][0] = 0.0F;
         this.tx[2][1] = 0.0F;
      } else {
         float var11 = var3 - var5;
         float var12 = var4 - var6;
         float var13 = var7 - var5;
         float var14 = var8 - var6;
         float var15 = 1.0F / (var11 * var14 - var13 * var12);
         this.tx[2][0] = (var9 * var14 - var13 * var10) * var15;
         this.tx[2][1] = (var11 * var10 - var9 * var12) * var15;
         this.tx[0][0] = var3 - var1 + this.tx[2][0] * var3;
         this.tx[0][1] = var7 - var1 + this.tx[2][1] * var7;
         this.tx[0][2] = var1;
         this.tx[1][0] = var4 - var2 + this.tx[2][0] * var4;
         this.tx[1][1] = var8 - var2 + this.tx[2][1] * var8;
         this.tx[1][2] = var2;
      }

      this.state.updateTx(this.tx);
   }

   public final void setQuadMapping(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.ulx = var1;
      this.uly = var2;
      this.urx = var3;
      this.ury = var4;
      this.lrx = var5;
      this.lry = var6;
      this.llx = var7;
      this.lly = var8;
   }

   public RectBounds getBounds(BaseTransform var1, Effect var2) {
      this.setupDevCoords(var1);
      float var5;
      float var3 = var5 = this.devcoords[0];
      float var6;
      float var4 = var6 = this.devcoords[1];

      for(int var7 = 2; var7 < this.devcoords.length; var7 += 2) {
         if (var3 > this.devcoords[var7]) {
            var3 = this.devcoords[var7];
         } else if (var5 < this.devcoords[var7]) {
            var5 = this.devcoords[var7];
         }

         if (var4 > this.devcoords[var7 + 1]) {
            var4 = this.devcoords[var7 + 1];
         } else if (var6 < this.devcoords[var7 + 1]) {
            var6 = this.devcoords[var7 + 1];
         }
      }

      return new RectBounds(var3, var4, var5, var6);
   }

   private void setupDevCoords(BaseTransform var1) {
      this.devcoords[0] = this.ulx;
      this.devcoords[1] = this.uly;
      this.devcoords[2] = this.urx;
      this.devcoords[3] = this.ury;
      this.devcoords[4] = this.lrx;
      this.devcoords[5] = this.lry;
      this.devcoords[6] = this.llx;
      this.devcoords[7] = this.lly;
      var1.transform((float[])this.devcoords, 0, (float[])this.devcoords, 0, 4);
   }

   public ImageData filter(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      this.setupTransforms(var2);
      RenderState var6 = this.getRenderState(var1, var2, var3, var4, var5);
      Effect var7 = this.getDefaultedInput(0, var5);
      Rectangle var8 = var6.getInputClip(0, var3);
      ImageData var9 = var7.filter(var1, BaseTransform.IDENTITY_TRANSFORM, var8, (Object)null, var5);
      if (!var9.validate(var1)) {
         var9.unref();
         return new ImageData(var1, (Filterable)null, var9.getUntransformedBounds());
      } else {
         ImageData var10 = this.filterImageDatas(var1, var2, var3, var6, new ImageData[]{var9});
         var9.unref();
         return var10;
      }
   }

   public Rectangle getResultBounds(BaseTransform var1, Rectangle var2, ImageData... var3) {
      Rectangle var4 = new Rectangle(this.getBounds(var1, (Effect)null));
      var4.intersectWith(var2);
      return var4;
   }

   public Point2D transform(Point2D var1, Effect var2) {
      this.setupTransforms(BaseTransform.IDENTITY_TRANSFORM);
      Effect var3 = this.getDefaultedInput(0, var2);
      var1 = var3.transform(var1, var2);
      BaseBounds var4 = var3.getBounds(BaseTransform.IDENTITY_TRANSFORM, var2);
      float var5 = (var1.x - var4.getMinX()) / var4.getWidth();
      float var6 = (var1.y - var4.getMinY()) / var4.getHeight();
      float var7 = this.tx[0][0] * var5 + this.tx[0][1] * var6 + this.tx[0][2];
      float var8 = this.tx[1][0] * var5 + this.tx[1][1] * var6 + this.tx[1][2];
      float var9 = this.tx[2][0] * var5 + this.tx[2][1] * var6 + this.tx[2][2];
      var1 = new Point2D(var7 / var9, var8 / var9);
      return var1;
   }

   public Point2D untransform(Point2D var1, Effect var2) {
      this.setupTransforms(BaseTransform.IDENTITY_TRANSFORM);
      Effect var3 = this.getDefaultedInput(0, var2);
      float var4 = var1.x;
      float var5 = var1.y;
      float[][] var6 = this.state.getITX();
      float var7 = var6[0][0] * var4 + var6[0][1] * var5 + var6[0][2];
      float var8 = var6[1][0] * var4 + var6[1][1] * var5 + var6[1][2];
      float var9 = var6[2][0] * var4 + var6[2][1] * var5 + var6[2][2];
      BaseBounds var10 = var3.getBounds(BaseTransform.IDENTITY_TRANSFORM, var2);
      var1 = new Point2D(var10.getMinX() + var7 / var9 * var10.getWidth(), var10.getMinY() + var8 / var9 * var10.getHeight());
      var1 = this.getDefaultedInput(0, var2).untransform(var1, var2);
      return var1;
   }

   private void setupTransforms(BaseTransform var1) {
      this.setupDevCoords(var1);
      this.setUnitQuadMapping(this.devcoords[0], this.devcoords[1], this.devcoords[2], this.devcoords[3], this.devcoords[4], this.devcoords[5], this.devcoords[6], this.devcoords[7]);
   }

   public RenderState getRenderState(FilterContext var1, BaseTransform var2, Rectangle var3, Object var4, Effect var5) {
      return RenderState.UnclippedUserSpaceRenderState;
   }

   public boolean reducesOpaquePixels() {
      return true;
   }

   public DirtyRegionContainer getDirtyRegions(Effect var1, DirtyRegionPool var2) {
      DirtyRegionContainer var3 = var2.checkOut();
      var3.deriveWithNewRegion(this.getBounds(BaseTransform.IDENTITY_TRANSFORM, var1));
      return var3;
   }
}
