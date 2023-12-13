package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.QuadCurve2D;
import com.sun.javafx.geom.Shape;

public class NGQuadCurve extends NGShape {
   private QuadCurve2D curve = new QuadCurve2D();

   public final Shape getShape() {
      return this.curve;
   }

   public void updateQuadCurve(float var1, float var2, float var3, float var4, float var5, float var6) {
      this.curve.x1 = var1;
      this.curve.y1 = var2;
      this.curve.x2 = var3;
      this.curve.y2 = var4;
      this.curve.ctrlx = var5;
      this.curve.ctrly = var6;
      this.geometryChanged();
   }
}
