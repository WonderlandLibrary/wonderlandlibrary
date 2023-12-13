package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.RectangularShape;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.RectShadowGraphics;
import com.sun.prism.paint.Color;
import com.sun.prism.shape.ShapeRep;
import com.sun.scenario.effect.Effect;

public class NGRectangle extends NGShape {
   private RoundRectangle2D rrect = new RoundRectangle2D();
   static final float HALF_MINUS_HALF_SQRT_HALF = 0.14700001F;
   private static final double SQRT_2 = Math.sqrt(2.0);

   public void updateRectangle(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.rrect.x = var1;
      this.rrect.y = var2;
      this.rrect.width = var3;
      this.rrect.height = var4;
      this.rrect.arcWidth = var5;
      this.rrect.arcHeight = var6;
      this.geometryChanged();
   }

   protected boolean supportsOpaqueRegions() {
      return true;
   }

   protected boolean hasOpaqueRegion() {
      return super.hasOpaqueRegion() && this.rrect.width > 1.0F && this.rrect.height > 1.0F;
   }

   protected RectBounds computeOpaqueRegion(RectBounds var1) {
      float var2 = this.rrect.x;
      float var3 = this.rrect.y;
      float var4 = this.rrect.width;
      float var5 = this.rrect.height;
      float var6 = this.rrect.arcWidth;
      float var7 = this.rrect.arcHeight;
      if (!(var6 <= 0.0F) && !(var7 <= 0.0F)) {
         float var8 = Math.min(var4, var6) * 0.14700001F;
         float var9 = Math.min(var5, var7) * 0.14700001F;
         return (RectBounds)var1.deriveWithNewBounds(var2 + var8, var3 + var9, 0.0F, var2 + var4 - var8, var3 + var5 - var9, 0.0F);
      } else {
         return (RectBounds)var1.deriveWithNewBounds(var2, var3, 0.0F, var2 + var4, var3 + var5, 0.0F);
      }
   }

   boolean isRounded() {
      return this.rrect.arcWidth > 0.0F && this.rrect.arcHeight > 0.0F;
   }

   protected void renderEffect(Graphics var1) {
      if (!(var1 instanceof RectShadowGraphics) || !this.renderEffectDirectly(var1)) {
         super.renderEffect(var1);
      }

   }

   private boolean renderEffectDirectly(Graphics var1) {
      if (this.mode == NGShape.Mode.FILL && !this.isRounded()) {
         float var2 = var1.getExtraAlpha();
         if (this.fillPaint instanceof Color) {
            var2 *= ((Color)this.fillPaint).getAlpha();
            Effect var3 = this.getEffect();
            return EffectUtil.renderEffectForRectangularNode(this, var1, var3, var2, true, this.rrect.x, this.rrect.y, this.rrect.width, this.rrect.height);
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public final Shape getShape() {
      return this.rrect;
   }

   protected ShapeRep createShapeRep(Graphics var1) {
      return var1.getResourceFactory().createRoundRectRep();
   }

   private static boolean hasRightAngleMiterAndNoDashes(BasicStroke var0) {
      return var0.getLineJoin() == 0 && (double)var0.getMiterLimit() >= SQRT_2 && var0.getDashArray() == null;
   }

   static boolean rectContains(float var0, float var1, NGShape var2, RectangularShape var3) {
      double var4 = (double)var3.getWidth();
      double var6 = (double)var3.getHeight();
      if (!(var4 < 0.0) && !(var6 < 0.0)) {
         NGShape.Mode var8 = var2.mode;
         if (var8 == NGShape.Mode.EMPTY) {
            return false;
         } else {
            double var9 = (double)var3.getX();
            double var11 = (double)var3.getY();
            if (var8 != NGShape.Mode.FILL) {
               float var13 = -1.0F;
               float var14 = -1.0F;
               boolean var15 = false;
               BasicStroke var16 = var2.drawStroke;
               int var17 = var16.getType();
               if (var17 == 1) {
                  if (var8 == NGShape.Mode.STROKE_FILL) {
                     var13 = 0.0F;
                  } else if (var16.getDashArray() == null) {
                     var13 = 0.0F;
                     var14 = var16.getLineWidth();
                  } else {
                     var15 = true;
                  }
               } else if (var17 == 2) {
                  if (hasRightAngleMiterAndNoDashes(var16)) {
                     var13 = var16.getLineWidth();
                     if (var8 == NGShape.Mode.STROKE) {
                        var14 = 0.0F;
                     }
                  } else {
                     if (var8 == NGShape.Mode.STROKE_FILL) {
                        var13 = 0.0F;
                     }

                     var15 = true;
                  }
               } else if (var17 == 0) {
                  if (hasRightAngleMiterAndNoDashes(var16)) {
                     var13 = var16.getLineWidth() / 2.0F;
                     if (var8 == NGShape.Mode.STROKE) {
                        var14 = var13;
                     }
                  } else {
                     if (var8 == NGShape.Mode.STROKE_FILL) {
                        var13 = 0.0F;
                     }

                     var15 = true;
                  }
               } else {
                  if (var8 == NGShape.Mode.STROKE_FILL) {
                     var13 = 0.0F;
                  }

                  var15 = true;
               }

               if (var13 >= 0.0F && (double)var0 >= var9 - (double)var13 && (double)var1 >= var11 - (double)var13 && (double)var0 < var9 + var4 + (double)var13 && (double)var1 < var11 + var6 + (double)var13) {
                  return !(var14 >= 0.0F) || !((double)var14 < var4 / 2.0) || !((double)var14 < var6 / 2.0) || !((double)var0 >= var9 + (double)var14) || !((double)var1 >= var11 + (double)var14) || !((double)var0 < var9 + var4 - (double)var14) || !((double)var1 < var11 + var6 - (double)var14);
               } else {
                  return var15 ? var2.getStrokeShape().contains(var0, var1) : false;
               }
            } else {
               return (double)var0 >= var9 && (double)var1 >= var11 && (double)var0 < var9 + var4 && (double)var1 < var11 + var6;
            }
         }
      } else {
         return false;
      }
   }

   protected final boolean isRectClip(BaseTransform var1, boolean var2) {
      if (this.mode == NGShape.Mode.FILL && this.getClipNode() == null && (this.getEffect() == null || !this.getEffect().reducesOpaquePixels()) && !(this.getOpacity() < 1.0F) && (var2 || !this.isRounded()) && this.fillPaint.isOpaque()) {
         BaseTransform var3 = this.getTransform();
         if (!var3.isIdentity()) {
            if (!((BaseTransform)var1).isIdentity()) {
               TEMP_TRANSFORM.setTransform((BaseTransform)var1);
               TEMP_TRANSFORM.concatenate(var3);
               var1 = TEMP_TRANSFORM;
            } else {
               var1 = var3;
            }
         }

         long var4 = (long)((BaseTransform)var1).getType();
         return (var4 & -16L) == 0L;
      } else {
         return false;
      }
   }
}
