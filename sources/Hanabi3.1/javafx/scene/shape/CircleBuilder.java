package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class CircleBuilder extends ShapeBuilder implements Builder {
   private int __set;
   private double centerX;
   private double centerY;
   private double radius;

   protected CircleBuilder() {
   }

   public static CircleBuilder create() {
      return new CircleBuilder();
   }

   public void applyTo(Circle var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setCenterX(this.centerX);
      }

      if ((var2 & 2) != 0) {
         var1.setCenterY(this.centerY);
      }

      if ((var2 & 4) != 0) {
         var1.setRadius(this.radius);
      }

   }

   public CircleBuilder centerX(double var1) {
      this.centerX = var1;
      this.__set |= 1;
      return this;
   }

   public CircleBuilder centerY(double var1) {
      this.centerY = var1;
      this.__set |= 2;
      return this;
   }

   public CircleBuilder radius(double var1) {
      this.radius = var1;
      this.__set |= 4;
      return this;
   }

   public Circle build() {
      Circle var1 = new Circle();
      this.applyTo(var1);
      return var1;
   }
}
