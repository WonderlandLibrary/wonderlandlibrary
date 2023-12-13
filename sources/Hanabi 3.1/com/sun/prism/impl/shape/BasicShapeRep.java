package com.sun.prism.impl.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.shape.ShapeRep;

public class BasicShapeRep implements ShapeRep {
   public boolean is3DCapable() {
      return false;
   }

   public void invalidate(ShapeRep.InvalidationType var1) {
   }

   public void fill(Graphics var1, Shape var2, BaseBounds var3) {
      var1.fill(var2);
   }

   public void draw(Graphics var1, Shape var2, BaseBounds var3) {
      var1.draw(var2);
   }

   public void dispose() {
   }
}
