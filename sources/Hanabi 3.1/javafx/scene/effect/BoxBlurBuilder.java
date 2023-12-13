package javafx.scene.effect;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class BoxBlurBuilder implements Builder {
   private int __set;
   private double height;
   private Effect input;
   private int iterations;
   private double width;

   protected BoxBlurBuilder() {
   }

   public static BoxBlurBuilder create() {
      return new BoxBlurBuilder();
   }

   public void applyTo(BoxBlur var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setHeight(this.height);
      }

      if ((var2 & 2) != 0) {
         var1.setInput(this.input);
      }

      if ((var2 & 4) != 0) {
         var1.setIterations(this.iterations);
      }

      if ((var2 & 8) != 0) {
         var1.setWidth(this.width);
      }

   }

   public BoxBlurBuilder height(double var1) {
      this.height = var1;
      this.__set |= 1;
      return this;
   }

   public BoxBlurBuilder input(Effect var1) {
      this.input = var1;
      this.__set |= 2;
      return this;
   }

   public BoxBlurBuilder iterations(int var1) {
      this.iterations = var1;
      this.__set |= 4;
      return this;
   }

   public BoxBlurBuilder width(double var1) {
      this.width = var1;
      this.__set |= 8;
      return this;
   }

   public BoxBlur build() {
      BoxBlur var1 = new BoxBlur();
      this.applyTo(var1);
      return var1;
   }
}
