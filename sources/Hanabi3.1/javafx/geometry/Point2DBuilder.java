package javafx.geometry;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class Point2DBuilder implements Builder {
   private double x;
   private double y;

   protected Point2DBuilder() {
   }

   public static Point2DBuilder create() {
      return new Point2DBuilder();
   }

   public Point2DBuilder x(double var1) {
      this.x = var1;
      return this;
   }

   public Point2DBuilder y(double var1) {
      this.y = var1;
      return this;
   }

   public Point2D build() {
      Point2D var1 = new Point2D(this.x, this.y);
      return var1;
   }
}
