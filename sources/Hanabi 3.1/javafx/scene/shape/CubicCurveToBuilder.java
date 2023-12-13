package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class CubicCurveToBuilder extends PathElementBuilder implements Builder {
   private int __set;
   private double controlX1;
   private double controlX2;
   private double controlY1;
   private double controlY2;
   private double x;
   private double y;

   protected CubicCurveToBuilder() {
   }

   public static CubicCurveToBuilder create() {
      return new CubicCurveToBuilder();
   }

   public void applyTo(CubicCurveTo var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setControlX1(this.controlX1);
      }

      if ((var2 & 2) != 0) {
         var1.setControlX2(this.controlX2);
      }

      if ((var2 & 4) != 0) {
         var1.setControlY1(this.controlY1);
      }

      if ((var2 & 8) != 0) {
         var1.setControlY2(this.controlY2);
      }

      if ((var2 & 16) != 0) {
         var1.setX(this.x);
      }

      if ((var2 & 32) != 0) {
         var1.setY(this.y);
      }

   }

   public CubicCurveToBuilder controlX1(double var1) {
      this.controlX1 = var1;
      this.__set |= 1;
      return this;
   }

   public CubicCurveToBuilder controlX2(double var1) {
      this.controlX2 = var1;
      this.__set |= 2;
      return this;
   }

   public CubicCurveToBuilder controlY1(double var1) {
      this.controlY1 = var1;
      this.__set |= 4;
      return this;
   }

   public CubicCurveToBuilder controlY2(double var1) {
      this.controlY2 = var1;
      this.__set |= 8;
      return this;
   }

   public CubicCurveToBuilder x(double var1) {
      this.x = var1;
      this.__set |= 16;
      return this;
   }

   public CubicCurveToBuilder y(double var1) {
      this.y = var1;
      this.__set |= 32;
      return this;
   }

   public CubicCurveTo build() {
      CubicCurveTo var1 = new CubicCurveTo();
      this.applyTo(var1);
      return var1;
   }
}
