package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.shape.ShapeRep;
import javafx.scene.shape.ArcType;

public class NGArc extends NGShape {
   private Arc2D arc = new Arc2D();

   public void updateArc(float var1, float var2, float var3, float var4, float var5, float var6, ArcType var7) {
      this.arc.x = var1 - var3;
      this.arc.width = var3 * 2.0F;
      this.arc.y = var2 - var4;
      this.arc.height = var4 * 2.0F;
      this.arc.start = var5;
      this.arc.extent = var6;
      if (var7 == ArcType.CHORD) {
         this.arc.setArcType(1);
      } else if (var7 == ArcType.OPEN) {
         this.arc.setArcType(0);
      } else {
         if (var7 != ArcType.ROUND) {
            throw new AssertionError("Unknown arc type specified");
         }

         this.arc.setArcType(2);
      }

      this.geometryChanged();
   }

   public Shape getShape() {
      return this.arc;
   }

   protected ShapeRep createShapeRep(Graphics var1) {
      return var1.getResourceFactory().createArcRep();
   }
}
