package javafx.scene.text;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public final class FontBuilder implements Builder {
   private String name;
   private double size;

   protected FontBuilder() {
   }

   public static FontBuilder create() {
      return new FontBuilder();
   }

   public FontBuilder name(String var1) {
      this.name = var1;
      return this;
   }

   public FontBuilder size(double var1) {
      this.size = var1;
      return this;
   }

   public Font build() {
      Font var1 = new Font(this.name, this.size);
      return var1;
   }
}
