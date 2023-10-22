package com.sun.prism.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;

public interface ShapeRep {
   boolean is3DCapable();

   void invalidate(InvalidationType var1);

   void fill(Graphics var1, Shape var2, BaseBounds var3);

   void draw(Graphics var1, Shape var2, BaseBounds var3);

   void dispose();

   public static enum InvalidationType {
      LOCATION,
      LOCATION_AND_GEOMETRY;
   }
}
