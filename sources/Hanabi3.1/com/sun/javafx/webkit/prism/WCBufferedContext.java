package com.sun.javafx.webkit.prism;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.sg.prism.NGRectangle;
import com.sun.javafx.sg.prism.NodeEffectInput;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.DropShadow;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCTransform;

final class WCBufferedContext extends WCGraphicsPrismContext {
   private final PrismImage img;
   private boolean isInitialized;
   private final RectBounds TEMP_BOUNDS = new RectBounds();
   private final NGRectangle TEMP_NGRECT = new NGRectangle();
   private final RoundRectangle2D TEMP_RECT = new RoundRectangle2D();
   private final float[] TEMP_COORDS = new float[6];

   WCBufferedContext(PrismImage var1) {
      this.img = var1;
   }

   public WCGraphicsPrismContext.Type type() {
      return WCGraphicsPrismContext.Type.DEDICATED;
   }

   public WCImage getImage() {
      return this.img;
   }

   Graphics getGraphics(boolean var1) {
      this.init();
      if (this.baseGraphics == null) {
         this.baseGraphics = this.img.getGraphics();
      }

      return super.getGraphics(var1);
   }

   protected boolean shouldCalculateIntersection() {
      return this.baseGraphics == null;
   }

   protected boolean shouldRenderRect(float var1, float var2, float var3, float var4, DropShadow var5, BasicStroke var6) {
      if (!this.shouldCalculateIntersection()) {
         return true;
      } else if (var5 != null) {
         this.TEMP_RECT.setFrame(var1, var2, var3, var4);
         return this.shouldRenderShape(this.TEMP_RECT, var5, var6);
      } else {
         if (var6 != null) {
            float var7 = 0.0F;
            float var8 = 0.0F;
            switch (var6.getType()) {
               case 0:
                  var8 = var6.getLineWidth();
                  var7 = var8 / 2.0F;
               case 1:
               default:
                  break;
               case 2:
                  var7 = var6.getLineWidth();
                  var8 = var7 * 2.0F;
            }

            var1 -= var7;
            var2 -= var7;
            var3 += var8;
            var4 += var8;
         }

         this.TEMP_BOUNDS.setBounds(var1, var2, var1 + var3, var2 + var4);
         return this.trIntersectsClip(this.TEMP_BOUNDS, this.getTransformNoClone());
      }
   }

   protected boolean shouldRenderShape(Shape var1, DropShadow var2, BasicStroke var3) {
      if (!this.shouldCalculateIntersection()) {
         return true;
      } else {
         Object var4 = var2 != null ? BaseTransform.IDENTITY_TRANSFORM : this.getTransformNoClone();
         this.TEMP_COORDS[0] = this.TEMP_COORDS[1] = Float.POSITIVE_INFINITY;
         this.TEMP_COORDS[2] = this.TEMP_COORDS[3] = Float.NEGATIVE_INFINITY;
         if (var3 == null) {
            Shape.accumulate(this.TEMP_COORDS, var1, (BaseTransform)var4);
         } else {
            var3.accumulateShapeBounds(this.TEMP_COORDS, var1, (BaseTransform)var4);
         }

         this.TEMP_BOUNDS.setBounds(this.TEMP_COORDS[0], this.TEMP_COORDS[1], this.TEMP_COORDS[2], this.TEMP_COORDS[3]);
         Affine3D var5 = null;
         if (var2 != null) {
            this.TEMP_NGRECT.updateRectangle(this.TEMP_BOUNDS.getMinX(), this.TEMP_BOUNDS.getMinY(), this.TEMP_BOUNDS.getWidth(), this.TEMP_BOUNDS.getHeight(), 0.0F, 0.0F);
            this.TEMP_NGRECT.setContentBounds(this.TEMP_BOUNDS);
            BaseBounds var6 = var2.getBounds(BaseTransform.IDENTITY_TRANSFORM, new NodeEffectInput(this.TEMP_NGRECT));

            assert var6.getBoundsType() == BaseBounds.BoundsType.RECTANGLE;

            this.TEMP_BOUNDS.setBounds((RectBounds)var6);
            var5 = this.getTransformNoClone();
         }

         return this.trIntersectsClip(this.TEMP_BOUNDS, var5);
      }
   }

   private boolean trIntersectsClip(RectBounds var1, BaseTransform var2) {
      if (var2 != null && !var2.isIdentity()) {
         var2.transform((BaseBounds)var1, (BaseBounds)var1);
      }

      Rectangle var3 = this.getClipRectNoClone();
      if (var3 != null) {
         return var1.intersects((float)var3.x, (float)var3.y, (float)(var3.x + var3.width), (float)(var3.y + var3.height));
      } else {
         return this.img != null ? var1.intersects(0.0F, 0.0F, (float)this.img.getWidth() * this.img.getPixelScale(), (float)this.img.getHeight() * this.img.getPixelScale()) : false;
      }
   }

   public void saveState() {
      this.init();
      super.saveState();
   }

   public void scale(float var1, float var2) {
      this.init();
      super.scale(var1, var2);
   }

   public void setTransform(WCTransform var1) {
      this.init();
      super.setTransform(var1);
   }

   private void init() {
      if (!this.isInitialized) {
         BaseTransform var1 = PrismGraphicsManager.getPixelScaleTransform();
         this.initBaseTransform(var1);
         this.setClip(0, 0, this.img.getWidth(), this.img.getHeight());
         this.isInitialized = true;
      }

   }

   public void dispose() {
   }
}
