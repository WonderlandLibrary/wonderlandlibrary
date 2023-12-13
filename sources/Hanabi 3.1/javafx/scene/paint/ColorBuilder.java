package javafx.scene.paint;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ColorBuilder implements Builder {
   private double blue;
   private double green;
   private double opacity = 1.0;
   private double red;

   protected ColorBuilder() {
   }

   public static ColorBuilder create() {
      return new ColorBuilder();
   }

   public ColorBuilder blue(double var1) {
      this.blue = var1;
      return this;
   }

   public ColorBuilder green(double var1) {
      this.green = var1;
      return this;
   }

   public ColorBuilder opacity(double var1) {
      this.opacity = var1;
      return this;
   }

   public ColorBuilder red(double var1) {
      this.red = var1;
      return this;
   }

   public Color build() {
      Color var1 = new Color(this.red, this.green, this.blue, this.opacity);
      return var1;
   }
}
