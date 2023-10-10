package net.minecraft.dispenser;

public class PositionImpl implements IPosition {
   protected final double x;
   protected final double y;
   protected final double z;

   public double getZ() {
      return this.z;
   }

   public double getX() {
      return this.x;
   }

   public PositionImpl(double var1, double var3, double var5) {
      this.x = var1;
      this.y = var3;
      this.z = var5;
   }

   public double getY() {
      return this.y;
   }
}
