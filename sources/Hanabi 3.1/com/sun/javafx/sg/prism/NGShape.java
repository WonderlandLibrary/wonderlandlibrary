package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.PrinterGraphics;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Paint;
import com.sun.prism.shape.ShapeRep;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

public abstract class NGShape extends NGNode {
   private RTTexture cached3D;
   private double cachedW;
   private double cachedH;
   protected Paint fillPaint;
   protected Paint drawPaint;
   protected BasicStroke drawStroke;
   protected Mode mode;
   protected ShapeRep shapeRep;
   private boolean smooth;
   static final double THRESHOLD = 0.00390625;

   public NGShape() {
      this.mode = NGShape.Mode.FILL;
   }

   public void setMode(Mode var1) {
      if (var1 != this.mode) {
         this.mode = var1;
         this.geometryChanged();
      }

   }

   public Mode getMode() {
      return this.mode;
   }

   public void setSmooth(boolean var1) {
      var1 = !PrismSettings.forceNonAntialiasedShape && var1;
      if (var1 != this.smooth) {
         this.smooth = var1;
         this.visualsChanged();
      }

   }

   public boolean isSmooth() {
      return this.smooth;
   }

   public void setFillPaint(Object var1) {
      if (var1 != this.fillPaint || this.fillPaint != null && this.fillPaint.isMutable()) {
         this.fillPaint = (Paint)var1;
         this.visualsChanged();
         this.invalidateOpaqueRegion();
      }

   }

   public Paint getFillPaint() {
      return this.fillPaint;
   }

   public void setDrawPaint(Object var1) {
      if (var1 != this.drawPaint || this.drawPaint != null && this.drawPaint.isMutable()) {
         this.drawPaint = (Paint)var1;
         this.visualsChanged();
      }

   }

   public void setDrawStroke(BasicStroke var1) {
      if (this.drawStroke != var1) {
         this.drawStroke = var1;
         this.geometryChanged();
      }

   }

   public void setDrawStroke(float var1, StrokeType var2, StrokeLineCap var3, StrokeLineJoin var4, float var5, float[] var6, float var7) {
      byte var8;
      if (var2 == StrokeType.CENTERED) {
         var8 = 0;
      } else if (var2 == StrokeType.INSIDE) {
         var8 = 1;
      } else {
         var8 = 2;
      }

      byte var9;
      if (var3 == StrokeLineCap.BUTT) {
         var9 = 0;
      } else if (var3 == StrokeLineCap.SQUARE) {
         var9 = 2;
      } else {
         var9 = 1;
      }

      byte var10;
      if (var4 == StrokeLineJoin.BEVEL) {
         var10 = 2;
      } else if (var4 == StrokeLineJoin.MITER) {
         var10 = 0;
      } else {
         var10 = 1;
      }

      if (this.drawStroke == null) {
         this.drawStroke = new BasicStroke(var8, var1, var9, var10, var5);
      } else {
         this.drawStroke.set(var8, var1, var9, var10, var5);
      }

      if (var6.length > 0) {
         this.drawStroke.set(var6, var7);
      } else {
         this.drawStroke.set((float[])null, 0.0F);
      }

      this.geometryChanged();
   }

   public abstract Shape getShape();

   protected ShapeRep createShapeRep(Graphics var1) {
      return var1.getResourceFactory().createPathRep();
   }

   protected void visualsChanged() {
      super.visualsChanged();
      if (this.cached3D != null) {
         this.cached3D.dispose();
         this.cached3D = null;
      }

   }

   private static double hypot(double var0, double var2, double var4) {
      return Math.sqrt(var0 * var0 + var2 * var2 + var4 * var4);
   }

   protected void renderContent(Graphics var1) {
      if (this.mode != NGShape.Mode.EMPTY) {
         boolean var2 = var1 instanceof PrinterGraphics;
         BaseTransform var3 = var1.getTransformNoClone();
         boolean var4 = !var3.is2D();
         if (!var4) {
            if (this.cached3D != null) {
               this.cached3D.dispose();
               this.cached3D = null;
            }

            this.renderContent2D(var1, var2);
         } else {
            double var5 = hypot(var3.getMxx(), var3.getMyx(), var3.getMzx());
            double var7 = hypot(var3.getMxy(), var3.getMyy(), var3.getMzy());
            double var9 = var5 * (double)this.contentBounds.getWidth();
            double var11 = var7 * (double)this.contentBounds.getHeight();
            if (this.cached3D != null) {
               this.cached3D.lock();
               if (this.cached3D.isSurfaceLost() || Math.max(Math.abs(var9 - this.cachedW), Math.abs(var11 - this.cachedH)) > 0.00390625) {
                  this.cached3D.unlock();
                  this.cached3D.dispose();
                  this.cached3D = null;
               }
            }

            int var13;
            int var14;
            if (this.cached3D == null) {
               var13 = (int)Math.ceil(var9);
               var14 = (int)Math.ceil(var11);
               this.cachedW = var9;
               this.cachedH = var11;
               if (var13 <= 0 || var14 <= 0) {
                  return;
               }

               this.cached3D = var1.getResourceFactory().createRTTexture(var13, var14, Texture.WrapMode.CLAMP_TO_ZERO, false);
               this.cached3D.setLinearFiltering(this.isSmooth());
               this.cached3D.contentsUseful();
               Graphics var15 = this.cached3D.createGraphics();
               var15.scale((float)var5, (float)var7);
               var15.translate(-this.contentBounds.getMinX(), -this.contentBounds.getMinY());
               this.renderContent2D(var15, var2);
            }

            var13 = this.cached3D.getContentWidth();
            var14 = this.cached3D.getContentHeight();
            float var19 = this.contentBounds.getMinX();
            float var16 = this.contentBounds.getMinY();
            float var17 = var19 + (float)((double)var13 / var5);
            float var18 = var16 + (float)((double)var14 / var7);
            var1.drawTexture(this.cached3D, var19, var16, var17, var18, 0.0F, 0.0F, (float)var13, (float)var14);
            this.cached3D.unlock();
         }

      }
   }

   protected void renderContent2D(Graphics var1, boolean var2) {
      boolean var3 = var1.isAntialiasedShape();
      boolean var4 = this.isSmooth();
      if (var4 != var3) {
         var1.setAntialiasedShape(var4);
      }

      ShapeRep var5 = var2 ? null : this.shapeRep;
      if (var5 == null) {
         var5 = this.createShapeRep(var1);
      }

      Shape var6 = this.getShape();
      if (this.mode != NGShape.Mode.STROKE) {
         var1.setPaint(this.fillPaint);
         var5.fill(var1, var6, this.contentBounds);
      }

      if (this.mode != NGShape.Mode.FILL && this.drawStroke.getLineWidth() > 0.0F) {
         var1.setPaint(this.drawPaint);
         var1.setStroke(this.drawStroke);
         var5.draw(var1, var6, this.contentBounds);
      }

      if (var4 != var3) {
         var1.setAntialiasedShape(var3);
      }

      if (!var2) {
         this.shapeRep = var5;
      }

   }

   protected boolean hasOverlappingContents() {
      return this.mode == NGShape.Mode.STROKE_FILL;
   }

   protected Shape getStrokeShape() {
      return this.drawStroke.createStrokedShape(this.getShape());
   }

   protected void geometryChanged() {
      super.geometryChanged();
      if (this.shapeRep != null) {
         this.shapeRep.invalidate(ShapeRep.InvalidationType.LOCATION_AND_GEOMETRY);
      }

      if (this.cached3D != null) {
         this.cached3D.dispose();
         this.cached3D = null;
      }

   }

   protected boolean hasOpaqueRegion() {
      Mode var1 = this.getMode();
      Paint var2 = this.getFillPaint();
      return super.hasOpaqueRegion() && (var1 == NGShape.Mode.FILL || var1 == NGShape.Mode.STROKE_FILL) && var2 != null && var2.isOpaque();
   }

   public static enum Mode {
      EMPTY,
      FILL,
      STROKE,
      STROKE_FILL;
   }
}
