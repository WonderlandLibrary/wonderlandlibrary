package com.sun.prism.impl.ps;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.shape.ShapeRep;

public class CachingShapeRep implements ShapeRep {
   private CachingShapeRepState fillState;
   private CachingShapeRepState drawState;

   CachingShapeRepState createState() {
      return new CachingShapeRepState();
   }

   public boolean is3DCapable() {
      return false;
   }

   public void invalidate(ShapeRep.InvalidationType var1) {
      if (this.fillState != null) {
         this.fillState.invalidate();
      }

      if (this.drawState != null) {
         this.drawState.invalidate();
      }

   }

   public void fill(Graphics var1, Shape var2, BaseBounds var3) {
      if (this.fillState == null) {
         this.fillState = this.createState();
      }

      this.fillState.render(var1, var2, (RectBounds)var3, (BasicStroke)null);
   }

   public void draw(Graphics var1, Shape var2, BaseBounds var3) {
      if (this.drawState == null) {
         this.drawState = this.createState();
      }

      this.drawState.render(var1, var2, (RectBounds)var3, var1.getStroke());
   }

   public void dispose() {
      if (this.fillState != null) {
         this.fillState.dispose();
         this.fillState = null;
      }

      if (this.drawState != null) {
         this.drawState.dispose();
         this.drawState = null;
      }

   }
}
