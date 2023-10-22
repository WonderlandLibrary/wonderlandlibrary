package javafx.scene.effect;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class FloatMapBuilder implements Builder {
   private int __set;
   private int height;
   private int width;

   protected FloatMapBuilder() {
   }

   public static FloatMapBuilder create() {
      return new FloatMapBuilder();
   }

   public void applyTo(FloatMap var1) {
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setHeight(this.height);
      }

      if ((var2 & 2) != 0) {
         var1.setWidth(this.width);
      }

   }

   public FloatMapBuilder height(int var1) {
      this.height = var1;
      this.__set |= 1;
      return this;
   }

   public FloatMapBuilder width(int var1) {
      this.width = var1;
      this.__set |= 2;
      return this;
   }

   public FloatMap build() {
      FloatMap var1 = new FloatMap();
      this.applyTo(var1);
      return var1;
   }
}
