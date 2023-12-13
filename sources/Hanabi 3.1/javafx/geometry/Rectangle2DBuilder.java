package javafx.geometry;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class Rectangle2DBuilder implements Builder {
   private double height;
   private double minX;
   private double minY;
   private double width;

   protected Rectangle2DBuilder() {
   }

   public static Rectangle2DBuilder create() {
      return new Rectangle2DBuilder();
   }

   public Rectangle2DBuilder height(double var1) {
      this.height = var1;
      return this;
   }

   public Rectangle2DBuilder minX(double var1) {
      this.minX = var1;
      return this;
   }

   public Rectangle2DBuilder minY(double var1) {
      this.minY = var1;
      return this;
   }

   public Rectangle2DBuilder width(double var1) {
      this.width = var1;
      return this;
   }

   public Rectangle2D build() {
      Rectangle2D var1 = new Rectangle2D(this.minX, this.minY, this.width, this.height);
      return var1;
   }
}
