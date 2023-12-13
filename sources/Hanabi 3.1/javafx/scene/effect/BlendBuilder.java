package javafx.scene.effect;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class BlendBuilder implements Builder {
   private int __set;
   private Effect bottomInput;
   private BlendMode mode;
   private double opacity;
   private Effect topInput;

   protected BlendBuilder() {
   }

   public static BlendBuilder create() {
      return new BlendBuilder();
   }

   public void applyTo(Blend var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setBottomInput(this.bottomInput);
      }

      if ((var2 & 2) != 0) {
         var1.setMode(this.mode);
      }

      if ((var2 & 4) != 0) {
         var1.setOpacity(this.opacity);
      }

      if ((var2 & 8) != 0) {
         var1.setTopInput(this.topInput);
      }

   }

   public BlendBuilder bottomInput(Effect var1) {
      this.bottomInput = var1;
      this.__set |= 1;
      return this;
   }

   public BlendBuilder mode(BlendMode var1) {
      this.mode = var1;
      this.__set |= 2;
      return this;
   }

   public BlendBuilder opacity(double var1) {
      this.opacity = var1;
      this.__set |= 4;
      return this;
   }

   public BlendBuilder topInput(Effect var1) {
      this.topInput = var1;
      this.__set |= 8;
      return this;
   }

   public Blend build() {
      Blend var1 = new Blend();
      this.applyTo(var1);
      return var1;
   }
}
