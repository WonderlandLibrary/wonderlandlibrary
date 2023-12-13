package javafx.scene.transform;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ShearBuilder implements Builder {
   private int __set;
   private double pivotX;
   private double pivotY;
   private double x;
   private double y;

   protected ShearBuilder() {
   }

   public static ShearBuilder create() {
      return new ShearBuilder();
   }

   public void applyTo(Shear var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setPivotX(this.pivotX);
      }

      if ((var2 & 2) != 0) {
         var1.setPivotY(this.pivotY);
      }

      if ((var2 & 4) != 0) {
         var1.setX(this.x);
      }

      if ((var2 & 8) != 0) {
         var1.setY(this.y);
      }

   }

   public ShearBuilder pivotX(double var1) {
      this.pivotX = var1;
      this.__set |= 1;
      return this;
   }

   public ShearBuilder pivotY(double var1) {
      this.pivotY = var1;
      this.__set |= 2;
      return this;
   }

   public ShearBuilder x(double var1) {
      this.x = var1;
      this.__set |= 4;
      return this;
   }

   public ShearBuilder y(double var1) {
      this.y = var1;
      this.__set |= 8;
      return this;
   }

   public Shear build() {
      Shear var1 = new Shear();
      this.applyTo(var1);
      return var1;
   }
}
