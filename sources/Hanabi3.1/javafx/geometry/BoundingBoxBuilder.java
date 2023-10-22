package javafx.geometry;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class BoundingBoxBuilder implements Builder {
   private double depth;
   private double height;
   private double minX;
   private double minY;
   private double minZ;
   private double width;

   protected BoundingBoxBuilder() {
   }

   public static BoundingBoxBuilder create() {
      return new BoundingBoxBuilder();
   }

   public BoundingBoxBuilder depth(double var1) {
      this.depth = var1;
      return this;
   }

   public BoundingBoxBuilder height(double var1) {
      this.height = var1;
      return this;
   }

   public BoundingBoxBuilder minX(double var1) {
      this.minX = var1;
      return this;
   }

   public BoundingBoxBuilder minY(double var1) {
      this.minY = var1;
      return this;
   }

   public BoundingBoxBuilder minZ(double var1) {
      this.minZ = var1;
      return this;
   }

   public BoundingBoxBuilder width(double var1) {
      this.width = var1;
      return this;
   }

   public BoundingBox build() {
      BoundingBox var1 = new BoundingBox(this.minX, this.minY, this.minZ, this.width, this.height, this.depth);
      return var1;
   }
}
