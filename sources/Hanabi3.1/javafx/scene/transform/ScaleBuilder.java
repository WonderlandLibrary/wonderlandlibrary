package javafx.scene.transform;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ScaleBuilder implements Builder {
   private int __set;
   private double pivotX;
   private double pivotY;
   private double pivotZ;
   private double x;
   private double y;
   private double z;

   protected ScaleBuilder() {
   }

   public static ScaleBuilder create() {
      return new ScaleBuilder();
   }

   public void applyTo(Scale var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setPivotX(this.pivotX);
      }

      if ((var2 & 2) != 0) {
         var1.setPivotY(this.pivotY);
      }

      if ((var2 & 4) != 0) {
         var1.setPivotZ(this.pivotZ);
      }

      if ((var2 & 8) != 0) {
         var1.setX(this.x);
      }

      if ((var2 & 16) != 0) {
         var1.setY(this.y);
      }

      if ((var2 & 32) != 0) {
         var1.setZ(this.z);
      }

   }

   public ScaleBuilder pivotX(double var1) {
      this.pivotX = var1;
      this.__set |= 1;
      return this;
   }

   public ScaleBuilder pivotY(double var1) {
      this.pivotY = var1;
      this.__set |= 2;
      return this;
   }

   public ScaleBuilder pivotZ(double var1) {
      this.pivotZ = var1;
      this.__set |= 4;
      return this;
   }

   public ScaleBuilder x(double var1) {
      this.x = var1;
      this.__set |= 8;
      return this;
   }

   public ScaleBuilder y(double var1) {
      this.y = var1;
      this.__set |= 16;
      return this;
   }

   public ScaleBuilder z(double var1) {
      this.z = var1;
      this.__set |= 32;
      return this;
   }

   public Scale build() {
      Scale var1 = new Scale();
      this.applyTo(var1);
      return var1;
   }
}
