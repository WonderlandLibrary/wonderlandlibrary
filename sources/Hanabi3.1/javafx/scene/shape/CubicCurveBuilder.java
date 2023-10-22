package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class CubicCurveBuilder extends ShapeBuilder implements Builder {
   private int __set;
   private double controlX1;
   private double controlX2;
   private double controlY1;
   private double controlY2;
   private double endX;
   private double endY;
   private double startX;
   private double startY;

   protected CubicCurveBuilder() {
   }

   public static CubicCurveBuilder create() {
      return new CubicCurveBuilder();
   }

   private void __set(int var1) {
      this.__set |= 1 << var1;
   }

   public void applyTo(CubicCurve var1) {
      super.applyTo(var1);
      int var2 = this.__set;

      while(var2 != 0) {
         int var3 = Integer.numberOfTrailingZeros(var2);
         var2 &= ~(1 << var3);
         switch (var3) {
            case 0:
               var1.setControlX1(this.controlX1);
               break;
            case 1:
               var1.setControlX2(this.controlX2);
               break;
            case 2:
               var1.setControlY1(this.controlY1);
               break;
            case 3:
               var1.setControlY2(this.controlY2);
               break;
            case 4:
               var1.setEndX(this.endX);
               break;
            case 5:
               var1.setEndY(this.endY);
               break;
            case 6:
               var1.setStartX(this.startX);
               break;
            case 7:
               var1.setStartY(this.startY);
         }
      }

   }

   public CubicCurveBuilder controlX1(double var1) {
      this.controlX1 = var1;
      this.__set(0);
      return this;
   }

   public CubicCurveBuilder controlX2(double var1) {
      this.controlX2 = var1;
      this.__set(1);
      return this;
   }

   public CubicCurveBuilder controlY1(double var1) {
      this.controlY1 = var1;
      this.__set(2);
      return this;
   }

   public CubicCurveBuilder controlY2(double var1) {
      this.controlY2 = var1;
      this.__set(3);
      return this;
   }

   public CubicCurveBuilder endX(double var1) {
      this.endX = var1;
      this.__set(4);
      return this;
   }

   public CubicCurveBuilder endY(double var1) {
      this.endY = var1;
      this.__set(5);
      return this;
   }

   public CubicCurveBuilder startX(double var1) {
      this.startX = var1;
      this.__set(6);
      return this;
   }

   public CubicCurveBuilder startY(double var1) {
      this.startY = var1;
      this.__set(7);
      return this;
   }

   public CubicCurve build() {
      CubicCurve var1 = new CubicCurve();
      this.applyTo(var1);
      return var1;
   }
}
