package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class LineBuilder extends ShapeBuilder implements Builder {
   private int __set;
   private double endX;
   private double endY;
   private double startX;
   private double startY;

   protected LineBuilder() {
   }

   public static LineBuilder create() {
      return new LineBuilder();
   }

   public void applyTo(Line var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setEndX(this.endX);
      }

      if ((var2 & 2) != 0) {
         var1.setEndY(this.endY);
      }

      if ((var2 & 4) != 0) {
         var1.setStartX(this.startX);
      }

      if ((var2 & 8) != 0) {
         var1.setStartY(this.startY);
      }

   }

   public LineBuilder endX(double var1) {
      this.endX = var1;
      this.__set |= 1;
      return this;
   }

   public LineBuilder endY(double var1) {
      this.endY = var1;
      this.__set |= 2;
      return this;
   }

   public LineBuilder startX(double var1) {
      this.startX = var1;
      this.__set |= 4;
      return this;
   }

   public LineBuilder startY(double var1) {
      this.startY = var1;
      this.__set |= 8;
      return this;
   }

   public Line build() {
      Line var1 = new Line();
      this.applyTo(var1);
      return var1;
   }
}
