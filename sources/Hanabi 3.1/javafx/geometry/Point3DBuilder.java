package javafx.geometry;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class Point3DBuilder implements Builder {
   private double x;
   private double y;
   private double z;

   protected Point3DBuilder() {
   }

   public static Point3DBuilder create() {
      return new Point3DBuilder();
   }

   public Point3DBuilder x(double var1) {
      this.x = var1;
      return this;
   }

   public Point3DBuilder y(double var1) {
      this.y = var1;
      return this;
   }

   public Point3DBuilder z(double var1) {
      this.z = var1;
      return this;
   }

   public Point3D build() {
      Point3D var1 = new Point3D(this.x, this.y, this.z);
      return var1;
   }
}
