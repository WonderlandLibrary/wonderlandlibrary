package javafx.geometry;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class Dimension2DBuilder implements Builder {
   private double height;
   private double width;

   protected Dimension2DBuilder() {
   }

   public static Dimension2DBuilder create() {
      return new Dimension2DBuilder();
   }

   public Dimension2DBuilder height(double var1) {
      this.height = var1;
      return this;
   }

   public Dimension2DBuilder width(double var1) {
      this.width = var1;
      return this;
   }

   public Dimension2D build() {
      Dimension2D var1 = new Dimension2D(this.width, this.height);
      return var1;
   }
}
