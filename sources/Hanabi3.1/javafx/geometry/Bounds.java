package javafx.geometry;

public abstract class Bounds {
   private double minX;
   private double minY;
   private double minZ;
   private double width;
   private double height;
   private double depth;
   private double maxX;
   private double maxY;
   private double maxZ;

   public final double getMinX() {
      return this.minX;
   }

   public final double getMinY() {
      return this.minY;
   }

   public final double getMinZ() {
      return this.minZ;
   }

   public final double getWidth() {
      return this.width;
   }

   public final double getHeight() {
      return this.height;
   }

   public final double getDepth() {
      return this.depth;
   }

   public final double getMaxX() {
      return this.maxX;
   }

   public final double getMaxY() {
      return this.maxY;
   }

   public final double getMaxZ() {
      return this.maxZ;
   }

   public abstract boolean isEmpty();

   public abstract boolean contains(Point2D var1);

   public abstract boolean contains(Point3D var1);

   public abstract boolean contains(double var1, double var3);

   public abstract boolean contains(double var1, double var3, double var5);

   public abstract boolean contains(Bounds var1);

   public abstract boolean contains(double var1, double var3, double var5, double var7);

   public abstract boolean contains(double var1, double var3, double var5, double var7, double var9, double var11);

   public abstract boolean intersects(Bounds var1);

   public abstract boolean intersects(double var1, double var3, double var5, double var7);

   public abstract boolean intersects(double var1, double var3, double var5, double var7, double var9, double var11);

   protected Bounds(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.minX = var1;
      this.minY = var3;
      this.minZ = var5;
      this.width = var7;
      this.height = var9;
      this.depth = var11;
      this.maxX = var1 + var7;
      this.maxY = var3 + var9;
      this.maxZ = var5 + var11;
   }
}
