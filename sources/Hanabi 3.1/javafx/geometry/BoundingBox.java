package javafx.geometry;

import javafx.beans.NamedArg;

public class BoundingBox extends Bounds {
   private int hash = 0;

   public BoundingBox(@NamedArg("minX") double var1, @NamedArg("minY") double var3, @NamedArg("minZ") double var5, @NamedArg("width") double var7, @NamedArg("height") double var9, @NamedArg("depth") double var11) {
      super(var1, var3, var5, var7, var9, var11);
   }

   public BoundingBox(@NamedArg("minX") double var1, @NamedArg("minY") double var3, @NamedArg("width") double var5, @NamedArg("height") double var7) {
      super(var1, var3, 0.0, var5, var7, 0.0);
   }

   public boolean isEmpty() {
      return this.getMaxX() < this.getMinX() || this.getMaxY() < this.getMinY() || this.getMaxZ() < this.getMinZ();
   }

   public boolean contains(Point2D var1) {
      return var1 == null ? false : this.contains(var1.getX(), var1.getY(), 0.0);
   }

   public boolean contains(Point3D var1) {
      return var1 == null ? false : this.contains(var1.getX(), var1.getY(), var1.getZ());
   }

   public boolean contains(double var1, double var3) {
      return this.contains(var1, var3, 0.0);
   }

   public boolean contains(double var1, double var3, double var5) {
      if (this.isEmpty()) {
         return false;
      } else {
         return var1 >= this.getMinX() && var1 <= this.getMaxX() && var3 >= this.getMinY() && var3 <= this.getMaxY() && var5 >= this.getMinZ() && var5 <= this.getMaxZ();
      }
   }

   public boolean contains(Bounds var1) {
      return var1 != null && !var1.isEmpty() ? this.contains(var1.getMinX(), var1.getMinY(), var1.getMinZ(), var1.getWidth(), var1.getHeight(), var1.getDepth()) : false;
   }

   public boolean contains(double var1, double var3, double var5, double var7) {
      return this.contains(var1, var3) && this.contains(var1 + var5, var3 + var7);
   }

   public boolean contains(double var1, double var3, double var5, double var7, double var9, double var11) {
      return this.contains(var1, var3, var5) && this.contains(var1 + var7, var3 + var9, var5 + var11);
   }

   public boolean intersects(Bounds var1) {
      return var1 != null && !var1.isEmpty() ? this.intersects(var1.getMinX(), var1.getMinY(), var1.getMinZ(), var1.getWidth(), var1.getHeight(), var1.getDepth()) : false;
   }

   public boolean intersects(double var1, double var3, double var5, double var7) {
      return this.intersects(var1, var3, 0.0, var5, var7, 0.0);
   }

   public boolean intersects(double var1, double var3, double var5, double var7, double var9, double var11) {
      if (!this.isEmpty() && !(var7 < 0.0) && !(var9 < 0.0) && !(var11 < 0.0)) {
         return var1 + var7 >= this.getMinX() && var3 + var9 >= this.getMinY() && var5 + var11 >= this.getMinZ() && var1 <= this.getMaxX() && var3 <= this.getMaxY() && var5 <= this.getMaxZ();
      } else {
         return false;
      }
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof BoundingBox)) {
         return false;
      } else {
         BoundingBox var2 = (BoundingBox)var1;
         return this.getMinX() == var2.getMinX() && this.getMinY() == var2.getMinY() && this.getMinZ() == var2.getMinZ() && this.getWidth() == var2.getWidth() && this.getHeight() == var2.getHeight() && this.getDepth() == var2.getDepth();
      }
   }

   public int hashCode() {
      if (this.hash == 0) {
         long var1 = 7L;
         var1 = 31L * var1 + Double.doubleToLongBits(this.getMinX());
         var1 = 31L * var1 + Double.doubleToLongBits(this.getMinY());
         var1 = 31L * var1 + Double.doubleToLongBits(this.getMinZ());
         var1 = 31L * var1 + Double.doubleToLongBits(this.getWidth());
         var1 = 31L * var1 + Double.doubleToLongBits(this.getHeight());
         var1 = 31L * var1 + Double.doubleToLongBits(this.getDepth());
         this.hash = (int)(var1 ^ var1 >> 32);
      }

      return this.hash;
   }

   public String toString() {
      return "BoundingBox [minX:" + this.getMinX() + ", minY:" + this.getMinY() + ", minZ:" + this.getMinZ() + ", width:" + this.getWidth() + ", height:" + this.getHeight() + ", depth:" + this.getDepth() + ", maxX:" + this.getMaxX() + ", maxY:" + this.getMaxY() + ", maxZ:" + this.getMaxZ() + "]";
   }
}
