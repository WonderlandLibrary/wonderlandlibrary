package javafx.scene.shape;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class RectangleBuilder extends ShapeBuilder implements Builder {
   private int __set;
   private double arcHeight;
   private double arcWidth;
   private double height;
   private double width;
   private double x;
   private double y;

   protected RectangleBuilder() {
   }

   public static RectangleBuilder create() {
      return new RectangleBuilder();
   }

   public void applyTo(Rectangle var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setArcHeight(this.arcHeight);
      }

      if ((var2 & 2) != 0) {
         var1.setArcWidth(this.arcWidth);
      }

      if ((var2 & 4) != 0) {
         var1.setHeight(this.height);
      }

      if ((var2 & 8) != 0) {
         var1.setWidth(this.width);
      }

      if ((var2 & 16) != 0) {
         var1.setX(this.x);
      }

      if ((var2 & 32) != 0) {
         var1.setY(this.y);
      }

   }

   public RectangleBuilder arcHeight(double var1) {
      this.arcHeight = var1;
      this.__set |= 1;
      return this;
   }

   public RectangleBuilder arcWidth(double var1) {
      this.arcWidth = var1;
      this.__set |= 2;
      return this;
   }

   public RectangleBuilder height(double var1) {
      this.height = var1;
      this.__set |= 4;
      return this;
   }

   public RectangleBuilder width(double var1) {
      this.width = var1;
      this.__set |= 8;
      return this;
   }

   public RectangleBuilder x(double var1) {
      this.x = var1;
      this.__set |= 16;
      return this;
   }

   public RectangleBuilder y(double var1) {
      this.y = var1;
      this.__set |= 32;
      return this;
   }

   public Rectangle build() {
      Rectangle var1 = new Rectangle();
      this.applyTo(var1);
      return var1;
   }
}
