package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class QuadCurveBuilder extends ShapeBuilder implements Builder {
   private int __set;
   private double controlX;
   private double controlY;
   private double endX;
   private double endY;
   private double startX;
   private double startY;

   protected QuadCurveBuilder() {
   }

   public static QuadCurveBuilder create() {
      return new QuadCurveBuilder();
   }

   public void applyTo(QuadCurve var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setControlX(this.controlX);
      }

      if ((var2 & 2) != 0) {
         var1.setControlY(this.controlY);
      }

      if ((var2 & 4) != 0) {
         var1.setEndX(this.endX);
      }

      if ((var2 & 8) != 0) {
         var1.setEndY(this.endY);
      }

      if ((var2 & 16) != 0) {
         var1.setStartX(this.startX);
      }

      if ((var2 & 32) != 0) {
         var1.setStartY(this.startY);
      }

   }

   public QuadCurveBuilder controlX(double var1) {
      this.controlX = var1;
      this.__set |= 1;
      return this;
   }

   public QuadCurveBuilder controlY(double var1) {
      this.controlY = var1;
      this.__set |= 2;
      return this;
   }

   public QuadCurveBuilder endX(double var1) {
      this.endX = var1;
      this.__set |= 4;
      return this;
   }

   public QuadCurveBuilder endY(double var1) {
      this.endY = var1;
      this.__set |= 8;
      return this;
   }

   public QuadCurveBuilder startX(double var1) {
      this.startX = var1;
      this.__set |= 16;
      return this;
   }

   public QuadCurveBuilder startY(double var1) {
      this.startY = var1;
      this.__set |= 32;
      return this;
   }

   public QuadCurve build() {
      QuadCurve var1 = new QuadCurve();
      this.applyTo(var1);
      return var1;
   }
}
