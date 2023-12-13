package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.CubicCurve2D;
import com.sun.javafx.geom.Shape;

public class NGCubicCurve extends NGShape {
   private CubicCurve2D curve = new CubicCurve2D();

   public final Shape getShape() {
      return this.curve;
   }

   public void updateCubicCurve(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8) {
      this.curve.x1 = var1;
      this.curve.y1 = var2;
      this.curve.x2 = var3;
      this.curve.y2 = var4;
      this.curve.ctrlx1 = var5;
      this.curve.ctrly1 = var6;
      this.curve.ctrlx2 = var7;
      this.curve.ctrly2 = var8;
      this.geometryChanged();
   }
}
