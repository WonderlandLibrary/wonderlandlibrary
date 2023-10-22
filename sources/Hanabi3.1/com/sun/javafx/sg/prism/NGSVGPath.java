package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Shape;

public class NGSVGPath extends NGShape {
   private Shape path;

   public void setContent(Object var1) {
      this.path = (Shape)var1;
      this.geometryChanged();
   }

   public Object getGeometry() {
      return this.path;
   }

   public Shape getShape() {
      return this.path;
   }

   public boolean acceptsPath2dOnUpdate() {
      return true;
   }
}
