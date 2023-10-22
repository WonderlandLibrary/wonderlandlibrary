package com.sun.prism.impl.ps;

import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.impl.shape.BasicRoundRectRep;

class CachingRoundRectRepState extends CachingShapeRepState {
   void fillNoCache(Graphics var1, Shape var2) {
      BasicRoundRectRep.fillRoundRect(var1, (RoundRectangle2D)var2);
   }

   void drawNoCache(Graphics var1, Shape var2) {
      BasicRoundRectRep.drawRoundRect(var1, (RoundRectangle2D)var2);
   }
}
