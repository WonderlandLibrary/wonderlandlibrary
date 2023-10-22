package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class QuadCurveToBuilder extends PathElementBuilder implements Builder {
   private int __set;
   private double controlX;
   private double controlY;
   private double x;
   private double y;

   protected QuadCurveToBuilder() {
   }

   public static QuadCurveToBuilder create() {
      return new QuadCurveToBuilder();
   }

   public void applyTo(QuadCurveTo var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setControlX(this.controlX);
      }

      if ((var2 & 2) != 0) {
         var1.setControlY(this.controlY);
      }

      if ((var2 & 4) != 0) {
         var1.setX(this.x);
      }

      if ((var2 & 8) != 0) {
         var1.setY(this.y);
      }

   }

   public QuadCurveToBuilder controlX(double var1) {
      this.controlX = var1;
      this.__set |= 1;
      return this;
   }

   public QuadCurveToBuilder controlY(double var1) {
      this.controlY = var1;
      this.__set |= 2;
      return this;
   }

   public QuadCurveToBuilder x(double var1) {
      this.x = var1;
      this.__set |= 4;
      return this;
   }

   public QuadCurveToBuilder y(double var1) {
      this.y = var1;
      this.__set |= 8;
      return this;
   }

   public QuadCurveTo build() {
      QuadCurveTo var1 = new QuadCurveTo();
      this.applyTo(var1);
      return var1;
   }
}
