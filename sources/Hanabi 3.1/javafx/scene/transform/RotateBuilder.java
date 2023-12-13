package javafx.scene.transform;

import javafx.geometry.Point3D;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class RotateBuilder implements Builder {
   private int __set;
   private double angle;
   private Point3D axis;
   private double pivotX;
   private double pivotY;
   private double pivotZ;

   protected RotateBuilder() {
   }

   public static RotateBuilder create() {
      return new RotateBuilder();
   }

   public void applyTo(Rotate var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setAngle(this.angle);
      }

      if ((var2 & 2) != 0) {
         var1.setAxis(this.axis);
      }

      if ((var2 & 4) != 0) {
         var1.setPivotX(this.pivotX);
      }

      if ((var2 & 8) != 0) {
         var1.setPivotY(this.pivotY);
      }

      if ((var2 & 16) != 0) {
         var1.setPivotZ(this.pivotZ);
      }

   }

   public RotateBuilder angle(double var1) {
      this.angle = var1;
      this.__set |= 1;
      return this;
   }

   public RotateBuilder axis(Point3D var1) {
      this.axis = var1;
      this.__set |= 2;
      return this;
   }

   public RotateBuilder pivotX(double var1) {
      this.pivotX = var1;
      this.__set |= 4;
      return this;
   }

   public RotateBuilder pivotY(double var1) {
      this.pivotY = var1;
      this.__set |= 8;
      return this;
   }

   public RotateBuilder pivotZ(double var1) {
      this.pivotZ = var1;
      this.__set |= 16;
      return this;
   }

   public Rotate build() {
      Rotate var1 = new Rotate();
      this.applyTo(var1);
      return var1;
   }
}
