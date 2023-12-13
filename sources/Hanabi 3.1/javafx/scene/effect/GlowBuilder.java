package javafx.scene.effect;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class GlowBuilder implements Builder {
   private int __set;
   private Effect input;
   private double level;

   protected GlowBuilder() {
   }

   public static GlowBuilder create() {
      return new GlowBuilder();
   }

   public void applyTo(Glow var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setInput(this.input);
      }

      if ((var2 & 2) != 0) {
         var1.setLevel(this.level);
      }

   }

   public GlowBuilder input(Effect var1) {
      this.input = var1;
      this.__set |= 1;
      return this;
   }

   public GlowBuilder level(double var1) {
      this.level = var1;
      this.__set |= 2;
      return this;
   }

   public Glow build() {
      Glow var1 = new Glow();
      this.applyTo(var1);
      return var1;
   }
}
