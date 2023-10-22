package javafx.scene.effect;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ReflectionBuilder implements Builder {
   private int __set;
   private double bottomOpacity;
   private double fraction;
   private Effect input;
   private double topOffset;
   private double topOpacity;

   protected ReflectionBuilder() {
   }

   public static ReflectionBuilder create() {
      return new ReflectionBuilder();
   }

   public void applyTo(Reflection var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setBottomOpacity(this.bottomOpacity);
      }

      if ((var2 & 2) != 0) {
         var1.setFraction(this.fraction);
      }

      if ((var2 & 4) != 0) {
         var1.setInput(this.input);
      }

      if ((var2 & 8) != 0) {
         var1.setTopOffset(this.topOffset);
      }

      if ((var2 & 16) != 0) {
         var1.setTopOpacity(this.topOpacity);
      }

   }

   public ReflectionBuilder bottomOpacity(double var1) {
      this.bottomOpacity = var1;
      this.__set |= 1;
      return this;
   }

   public ReflectionBuilder fraction(double var1) {
      this.fraction = var1;
      this.__set |= 2;
      return this;
   }

   public ReflectionBuilder input(Effect var1) {
      this.input = var1;
      this.__set |= 4;
      return this;
   }

   public ReflectionBuilder topOffset(double var1) {
      this.topOffset = var1;
      this.__set |= 8;
      return this;
   }

   public ReflectionBuilder topOpacity(double var1) {
      this.topOpacity = var1;
      this.__set |= 16;
      return this;
   }

   public Reflection build() {
      Reflection var1 = new Reflection();
      this.applyTo(var1);
      return var1;
   }
}
