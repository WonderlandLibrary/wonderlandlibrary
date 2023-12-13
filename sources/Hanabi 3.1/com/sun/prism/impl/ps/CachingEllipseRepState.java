package com.sun.prism.impl.ps;

import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;

class CachingEllipseRepState extends CachingShapeRepState {
   void fillNoCache(Graphics var1, Shape var2) {
      Ellipse2D var3 = (Ellipse2D)var2;
      var1.fillEllipse(var3.x, var3.y, var3.width, var3.height);
   }

   void drawNoCache(Graphics var1, Shape var2) {
      Ellipse2D var3 = (Ellipse2D)var2;
      var1.drawEllipse(var3.x, var3.y, var3.width, var3.height);
   }
}
