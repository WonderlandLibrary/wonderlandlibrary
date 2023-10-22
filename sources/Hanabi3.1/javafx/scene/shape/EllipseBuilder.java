package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class EllipseBuilder extends ShapeBuilder implements Builder {
   private int __set;
   private double centerX;
   private double centerY;
   private double radiusX;
   private double radiusY;

   protected EllipseBuilder() {
   }

   public static EllipseBuilder create() {
      return new EllipseBuilder();
   }

   public void applyTo(Ellipse var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setCenterX(this.centerX);
      }

      if ((var2 & 2) != 0) {
         var1.setCenterY(this.centerY);
      }

      if ((var2 & 4) != 0) {
         var1.setRadiusX(this.radiusX);
      }

      if ((var2 & 8) != 0) {
         var1.setRadiusY(this.radiusY);
      }

   }

   public EllipseBuilder centerX(double var1) {
      this.centerX = var1;
      this.__set |= 1;
      return this;
   }

   public EllipseBuilder centerY(double var1) {
      this.centerY = var1;
      this.__set |= 2;
      return this;
   }

   public EllipseBuilder radiusX(double var1) {
      this.radiusX = var1;
      this.__set |= 4;
      return this;
   }

   public EllipseBuilder radiusY(double var1) {
      this.radiusY = var1;
      this.__set |= 8;
      return this;
   }

   public Ellipse build() {
      Ellipse var1 = new Ellipse();
      this.applyTo(var1);
      return var1;
   }
}
