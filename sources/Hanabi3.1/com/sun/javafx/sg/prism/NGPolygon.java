package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Shape;

public class NGPolygon extends NGShape {
   private Path2D path = new Path2D();

   public void updatePolygon(float[] var1) {
      this.path.reset();
      if (var1 != null && var1.length != 0 && var1.length % 2 == 0) {
         this.path.moveTo(var1[0], var1[1]);

         for(int var2 = 1; var2 < var1.length / 2; ++var2) {
            float var3 = var1[var2 * 2 + 0];
            float var4 = var1[var2 * 2 + 1];
            this.path.lineTo(var3, var4);
         }

         this.path.closePath();
         this.geometryChanged();
      }
   }

   public Shape getShape() {
      return this.path;
   }
}
