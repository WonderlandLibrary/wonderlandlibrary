package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Line2D;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;

public class NGLine extends NGShape {
   private Line2D line = new Line2D();

   public void updateLine(float var1, float var2, float var3, float var4) {
      this.line.x1 = var1;
      this.line.y1 = var2;
      this.line.x2 = var3;
      this.line.y2 = var4;
      this.geometryChanged();
   }

   protected void renderContent2D(Graphics var1, boolean var2) {
      if ((this.mode == NGShape.Mode.STROKE || this.mode == NGShape.Mode.STROKE_FILL) && this.drawStroke.getLineWidth() > 0.0F && this.drawStroke.getType() != 1) {
         var1.setPaint(this.drawPaint);
         var1.setStroke(this.drawStroke);
         var1.drawLine(this.line.x1, this.line.y1, this.line.x2, this.line.y2);
      }

   }

   public final Shape getShape() {
      return this.line;
   }
}
