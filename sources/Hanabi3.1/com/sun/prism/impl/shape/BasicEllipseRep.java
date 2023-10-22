package com.sun.prism.impl.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;

public class BasicEllipseRep extends BasicShapeRep {
   public void fill(Graphics var1, Shape var2, BaseBounds var3) {
      Ellipse2D var4 = (Ellipse2D)var2;
      var1.fillEllipse(var4.x, var4.y, var4.width, var4.height);
   }

   public void draw(Graphics var1, Shape var2, BaseBounds var3) {
      Ellipse2D var4 = (Ellipse2D)var2;
      var1.drawEllipse(var4.x, var4.y, var4.width, var4.height);
   }
}
