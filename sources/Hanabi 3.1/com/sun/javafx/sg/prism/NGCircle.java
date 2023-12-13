package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.shape.ShapeRep;

public class NGCircle extends NGShape {
   static final float HALF_SQRT_HALF = 0.353F;
   private Ellipse2D ellipse = new Ellipse2D();
   private float cx;
   private float cy;

   public void updateCircle(float var1, float var2, float var3) {
      this.ellipse.x = var1 - var3;
      this.ellipse.y = var2 - var3;
      this.ellipse.width = var3 * 2.0F;
      this.ellipse.height = this.ellipse.width;
      this.cx = var1;
      this.cy = var2;
      this.geometryChanged();
   }

   public Shape getShape() {
      return this.ellipse;
   }

   protected boolean supportsOpaqueRegions() {
      return true;
   }

   protected boolean hasOpaqueRegion() {
      return super.hasOpaqueRegion() && this.ellipse.width > 0.0F;
   }

   protected RectBounds computeOpaqueRegion(RectBounds var1) {
      float var2 = this.ellipse.width * 0.353F;
      return (RectBounds)var1.deriveWithNewBounds(this.cx - var2, this.cy - var2, 0.0F, this.cx + var2, this.cy + var2, 0.0F);
   }

   protected ShapeRep createShapeRep(Graphics var1) {
      return var1.getResourceFactory().createEllipseRep();
   }
}
