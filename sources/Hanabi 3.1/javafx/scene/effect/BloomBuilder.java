package javafx.scene.effect;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class BloomBuilder implements Builder {
   private int __set;
   private Effect input;
   private double threshold;

   protected BloomBuilder() {
   }

   public static BloomBuilder create() {
      return new BloomBuilder();
   }

   public void applyTo(Bloom var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setInput(this.input);
      }

      if ((var2 & 2) != 0) {
         var1.setThreshold(this.threshold);
      }

   }

   public BloomBuilder input(Effect var1) {
      this.input = var1;
      this.__set |= 1;
      return this;
   }

   public BloomBuilder threshold(double var1) {
      this.threshold = var1;
      this.__set |= 2;
      return this;
   }

   public Bloom build() {
      Bloom var1 = new Bloom();
      this.applyTo(var1);
      return var1;
   }
}
