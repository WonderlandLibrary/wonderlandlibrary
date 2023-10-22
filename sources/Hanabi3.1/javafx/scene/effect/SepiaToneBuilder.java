package javafx.scene.effect;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class SepiaToneBuilder implements Builder {
   private int __set;
   private Effect input;
   private double level;

   protected SepiaToneBuilder() {
   }

   public static SepiaToneBuilder create() {
      return new SepiaToneBuilder();
   }

   public void applyTo(SepiaTone var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setInput(this.input);
      }

      if ((var2 & 2) != 0) {
         var1.setLevel(this.level);
      }

   }

   public SepiaToneBuilder input(Effect var1) {
      this.input = var1;
      this.__set |= 1;
      return this;
   }

   public SepiaToneBuilder level(double var1) {
      this.level = var1;
      this.__set |= 2;
      return this;
   }

   public SepiaTone build() {
      SepiaTone var1 = new SepiaTone();
      this.applyTo(var1);
      return var1;
   }
}
