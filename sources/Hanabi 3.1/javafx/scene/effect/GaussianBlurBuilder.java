package javafx.scene.effect;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class GaussianBlurBuilder implements Builder {
   private int __set;
   private Effect input;
   private double radius;

   protected GaussianBlurBuilder() {
   }

   public static GaussianBlurBuilder create() {
      return new GaussianBlurBuilder();
   }

   public void applyTo(GaussianBlur var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setInput(this.input);
      }

      if ((var2 & 2) != 0) {
         var1.setRadius(this.radius);
      }

   }

   public GaussianBlurBuilder input(Effect var1) {
      this.input = var1;
      this.__set |= 1;
      return this;
   }

   public GaussianBlurBuilder radius(double var1) {
      this.radius = var1;
      this.__set |= 2;
      return this;
   }

   public GaussianBlur build() {
      GaussianBlur var1 = new GaussianBlur();
      this.applyTo(var1);
      return var1;
   }
}
