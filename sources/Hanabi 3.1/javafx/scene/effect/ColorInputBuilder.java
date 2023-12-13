package javafx.scene.effect;

import javafx.scene.paint.Paint;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ColorInputBuilder implements Builder {
   private int __set;
   private double height;
   private Paint paint;
   private double width;
   private double x;
   private double y;

   protected ColorInputBuilder() {
   }

   public static ColorInputBuilder create() {
      return new ColorInputBuilder();
   }

   public void applyTo(ColorInput var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setHeight(this.height);
      }

      if ((var2 & 2) != 0) {
         var1.setPaint(this.paint);
      }

      if ((var2 & 4) != 0) {
         var1.setWidth(this.width);
      }

      if ((var2 & 8) != 0) {
         var1.setX(this.x);
      }

      if ((var2 & 16) != 0) {
         var1.setY(this.y);
      }

   }

   public ColorInputBuilder height(double var1) {
      this.height = var1;
      this.__set |= 1;
      return this;
   }

   public ColorInputBuilder paint(Paint var1) {
      this.paint = var1;
      this.__set |= 2;
      return this;
   }

   public ColorInputBuilder width(double var1) {
      this.width = var1;
      this.__set |= 4;
      return this;
   }

   public ColorInputBuilder x(double var1) {
      this.x = var1;
      this.__set |= 8;
      return this;
   }

   public ColorInputBuilder y(double var1) {
      this.y = var1;
      this.__set |= 16;
      return this;
   }

   public ColorInput build() {
      ColorInput var1 = new ColorInput();
      this.applyTo(var1);
      return var1;
   }
}
