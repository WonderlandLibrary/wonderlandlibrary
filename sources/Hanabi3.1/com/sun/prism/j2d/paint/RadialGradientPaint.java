package com.sun.prism.j2d.paint;

import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

public final class RadialGradientPaint extends MultipleGradientPaint {
   private final Point2D focus;
   private final Point2D center;
   private final float radius;

   public RadialGradientPaint(float var1, float var2, float var3, float[] var4, Color[] var5) {
      this(var1, var2, var3, var1, var2, var4, var5, MultipleGradientPaint.CycleMethod.NO_CYCLE);
   }

   public RadialGradientPaint(Point2D var1, float var2, float[] var3, Color[] var4) {
      this(var1, var2, var1, var3, var4, MultipleGradientPaint.CycleMethod.NO_CYCLE);
   }

   public RadialGradientPaint(float var1, float var2, float var3, float[] var4, Color[] var5, MultipleGradientPaint.CycleMethod var6) {
      this(var1, var2, var3, var1, var2, var4, var5, var6);
   }

   public RadialGradientPaint(Point2D var1, float var2, float[] var3, Color[] var4, MultipleGradientPaint.CycleMethod var5) {
      this(var1, var2, var1, var3, var4, var5);
   }

   public RadialGradientPaint(float var1, float var2, float var3, float var4, float var5, float[] var6, Color[] var7, MultipleGradientPaint.CycleMethod var8) {
      this(new Point2D.Float(var1, var2), var3, new Point2D.Float(var4, var5), var6, var7, var8);
   }

   public RadialGradientPaint(Point2D var1, float var2, Point2D var3, float[] var4, Color[] var5, MultipleGradientPaint.CycleMethod var6) {
      this(var1, var2, var3, var4, var5, var6, MultipleGradientPaint.ColorSpaceType.SRGB, new AffineTransform());
   }

   public RadialGradientPaint(Point2D var1, float var2, Point2D var3, float[] var4, Color[] var5, MultipleGradientPaint.CycleMethod var6, MultipleGradientPaint.ColorSpaceType var7, AffineTransform var8) {
      super(var4, var5, var6, var7, var8);
      if (var1 == null) {
         throw new NullPointerException("Center point must be non-null");
      } else if (var3 == null) {
         throw new NullPointerException("Focus point must be non-null");
      } else if (var2 < 0.0F) {
         throw new IllegalArgumentException("Radius must be non-negative");
      } else {
         this.center = new Point2D.Double(var1.getX(), var1.getY());
         this.focus = new Point2D.Double(var3.getX(), var3.getY());
         this.radius = var2;
      }
   }

   public RadialGradientPaint(Rectangle2D var1, float[] var2, Color[] var3, MultipleGradientPaint.CycleMethod var4) {
      this(new Point2D.Double(var1.getCenterX(), var1.getCenterY()), 1.0F, new Point2D.Double(var1.getCenterX(), var1.getCenterY()), var2, var3, var4, MultipleGradientPaint.ColorSpaceType.SRGB, createGradientTransform(var1));
      if (var1.isEmpty()) {
         throw new IllegalArgumentException("Gradient bounds must be non-empty");
      }
   }

   private static AffineTransform createGradientTransform(Rectangle2D var0) {
      double var1 = var0.getCenterX();
      double var3 = var0.getCenterY();
      AffineTransform var5 = AffineTransform.getTranslateInstance(var1, var3);
      var5.scale(var0.getWidth() / 2.0, var0.getHeight() / 2.0);
      var5.translate(-var1, -var3);
      return var5;
   }

   public PaintContext createContext(ColorModel var1, Rectangle var2, Rectangle2D var3, AffineTransform var4, RenderingHints var5) {
      var4 = new AffineTransform(var4);
      var4.concatenate(this.gradientTransform);
      return new RadialGradientPaintContext(this, var1, var2, var3, var4, var5, (float)this.center.getX(), (float)this.center.getY(), this.radius, (float)this.focus.getX(), (float)this.focus.getY(), this.fractions, this.colors, this.cycleMethod, this.colorSpace);
   }

   public Point2D getCenterPoint() {
      return new Point2D.Double(this.center.getX(), this.center.getY());
   }

   public Point2D getFocusPoint() {
      return new Point2D.Double(this.focus.getX(), this.focus.getY());
   }

   public float getRadius() {
      return this.radius;
   }
}
