package cn.hanabi.utils.pathfinder;

public class Vec3 {
   private final double x;
   private final double y;
   private final double z;

   public Vec3(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public Vec3 addVector(double x, double y, double z) {
      return new Vec3(this.x + x, this.y + y, this.z + z);
   }

   public Vec3 floor() {
      return new Vec3(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
   }

   public double squareDistanceTo(Vec3 v) {
      return Math.pow(v.x - this.x, 2.0) + Math.pow(v.y - this.y, 2.0) + Math.pow(v.z - this.z, 2.0);
   }

   public Vec3 add(Vec3 v) {
      return this.addVector(v.getX(), v.getY(), v.getZ());
   }

   public net.minecraft.util.Vec3 mc() {
      return new net.minecraft.util.Vec3(this.x, this.y, this.z);
   }

   public String toString() {
      return "[" + this.x + ";" + this.y + ";" + this.z + "]";
   }
}
