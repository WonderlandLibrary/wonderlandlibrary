package net.minecraft.util;

public class AxisAlignedBB {
   public final double maxY;
   public final double maxX;
   public final double minY;
   public final double minX;
   public final double maxZ;
   public final double minZ;

   public double calculateYOffset(AxisAlignedBB var1, double var2) {
      if (var1.maxX > this.minX && var1.minX < this.maxX && var1.maxZ > this.minZ && var1.minZ < this.maxZ) {
         double var4;
         if (var2 > 0.0D && var1.maxY <= this.minY) {
            var4 = this.minY - var1.maxY;
            if (var4 < var2) {
               var2 = var4;
            }
         } else if (var2 < 0.0D && var1.minY >= this.maxY) {
            var4 = this.maxY - var1.minY;
            if (var4 > var2) {
               var2 = var4;
            }
         }

         return var2;
      } else {
         return var2;
      }
   }

   public AxisAlignedBB union(AxisAlignedBB var1) {
      double var2 = Math.min(this.minX, var1.minX);
      double var4 = Math.min(this.minY, var1.minY);
      double var6 = Math.min(this.minZ, var1.minZ);
      double var8 = Math.max(this.maxX, var1.maxX);
      double var10 = Math.max(this.maxY, var1.maxY);
      double var12 = Math.max(this.maxZ, var1.maxZ);
      return new AxisAlignedBB(var2, var4, var6, var8, var10, var12);
   }

   public AxisAlignedBB expand(double var1, double var3, double var5) {
      double var7 = this.minX - var1;
      double var9 = this.minY - var3;
      double var11 = this.minZ - var5;
      double var13 = this.maxX + var1;
      double var15 = this.maxY + var3;
      double var17 = this.maxZ + var5;
      return new AxisAlignedBB(var7, var9, var11, var13, var15, var17);
   }

   public AxisAlignedBB(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.minX = Math.min(var1, var7);
      this.minY = Math.min(var3, var9);
      this.minZ = Math.min(var5, var11);
      this.maxX = Math.max(var1, var7);
      this.maxY = Math.max(var3, var9);
      this.maxZ = Math.max(var5, var11);
   }

   public double calculateZOffset(AxisAlignedBB var1, double var2) {
      if (var1.maxX > this.minX && var1.minX < this.maxX && var1.maxY > this.minY && var1.minY < this.maxY) {
         double var4;
         if (var2 > 0.0D && var1.maxZ <= this.minZ) {
            var4 = this.minZ - var1.maxZ;
            if (var4 < var2) {
               var2 = var4;
            }
         } else if (var2 < 0.0D && var1.minZ >= this.maxZ) {
            var4 = this.maxZ - var1.minZ;
            if (var4 > var2) {
               var2 = var4;
            }
         }

         return var2;
      } else {
         return var2;
      }
   }

   public static AxisAlignedBB fromBounds(double var0, double var2, double var4, double var6, double var8, double var10) {
      double var12 = Math.min(var0, var6);
      double var14 = Math.min(var2, var8);
      double var16 = Math.min(var4, var10);
      double var18 = Math.max(var0, var6);
      double var20 = Math.max(var2, var8);
      double var22 = Math.max(var4, var10);
      return new AxisAlignedBB(var12, var14, var16, var18, var20, var22);
   }

   public AxisAlignedBB offset(double var1, double var3, double var5) {
      return new AxisAlignedBB(this.minX + var1, this.minY + var3, this.minZ + var5, this.maxX + var1, this.maxY + var3, this.maxZ + var5);
   }

   public AxisAlignedBB(BlockPos var1, BlockPos var2) {
      this.minX = (double)var1.getX();
      this.minY = (double)var1.getY();
      this.minZ = (double)var1.getZ();
      this.maxX = (double)var2.getX();
      this.maxY = (double)var2.getY();
      this.maxZ = (double)var2.getZ();
   }

   public boolean func_181656_b() {
      return Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ);
   }

   public AxisAlignedBB contract(double var1, double var3, double var5) {
      double var7 = this.minX + var1;
      double var9 = this.minY + var3;
      double var11 = this.minZ + var5;
      double var13 = this.maxX - var1;
      double var15 = this.maxY - var3;
      double var17 = this.maxZ - var5;
      return new AxisAlignedBB(var7, var9, var11, var13, var15, var17);
   }

   public double getAverageEdgeLength() {
      double var1 = this.maxX - this.minX;
      double var3 = this.maxY - this.minY;
      double var5 = this.maxZ - this.minZ;
      return (var1 + var3 + var5) / 3.0D;
   }

   public double calculateXOffset(AxisAlignedBB var1, double var2) {
      if (var1.maxY > this.minY && var1.minY < this.maxY && var1.maxZ > this.minZ && var1.minZ < this.maxZ) {
         double var4;
         if (var2 > 0.0D && var1.maxX <= this.minX) {
            var4 = this.minX - var1.maxX;
            if (var4 < var2) {
               var2 = var4;
            }
         } else if (var2 < 0.0D && var1.minX >= this.maxX) {
            var4 = this.maxX - var1.minX;
            if (var4 > var2) {
               var2 = var4;
            }
         }

         return var2;
      } else {
         return var2;
      }
   }

   public AxisAlignedBB addCoord(double var1, double var3, double var5) {
      double var7 = this.minX;
      double var9 = this.minY;
      double var11 = this.minZ;
      double var13 = this.maxX;
      double var15 = this.maxY;
      double var17 = this.maxZ;
      if (var1 < 0.0D) {
         var7 += var1;
      } else if (var1 > 0.0D) {
         var13 += var1;
      }

      if (var3 < 0.0D) {
         var9 += var3;
      } else if (var3 > 0.0D) {
         var15 += var3;
      }

      if (var5 < 0.0D) {
         var11 += var5;
      } else if (var5 > 0.0D) {
         var17 += var5;
      }

      return new AxisAlignedBB(var7, var9, var11, var13, var15, var17);
   }

   public String toString() {
      return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
   }

   public boolean isVecInside(Vec3 var1) {
      return var1.xCoord > this.minX && var1.xCoord < this.maxX ? (var1.yCoord > this.minY && var1.yCoord < this.maxY ? var1.zCoord > this.minZ && var1.zCoord < this.maxZ : false) : false;
   }

   public boolean intersectsWith(AxisAlignedBB var1) {
      return var1.maxX > this.minX && var1.minX < this.maxX ? (var1.maxY > this.minY && var1.minY < this.maxY ? var1.maxZ > this.minZ && var1.minZ < this.maxZ : false) : false;
   }

   public MovingObjectPosition calculateIntercept(Vec3 var1, Vec3 var2) {
      Vec3 var3 = var1.getIntermediateWithXValue(var2, this.minX);
      Vec3 var4 = var1.getIntermediateWithXValue(var2, this.maxX);
      Vec3 var5 = var1.getIntermediateWithYValue(var2, this.minY);
      Vec3 var6 = var1.getIntermediateWithYValue(var2, this.maxY);
      Vec3 var7 = var1.getIntermediateWithZValue(var2, this.minZ);
      Vec3 var8 = var1.getIntermediateWithZValue(var2, this.maxZ);
      if (var3 == null) {
         var3 = null;
      }

      if (var4 == null) {
         var4 = null;
      }

      if (var5 == null) {
         var5 = null;
      }

      if (var6 == null) {
         var6 = null;
      }

      if (var7 == null) {
         var7 = null;
      }

      if (var8 == null) {
         var8 = null;
      }

      Vec3 var9 = null;
      if (var3 != null) {
         var9 = var3;
      }

      if (var4 != null && (var9 == null || var1.squareDistanceTo(var4) < var1.squareDistanceTo(var9))) {
         var9 = var4;
      }

      if (var5 != null && (var9 == null || var1.squareDistanceTo(var5) < var1.squareDistanceTo(var9))) {
         var9 = var5;
      }

      if (var6 != null && (var9 == null || var1.squareDistanceTo(var6) < var1.squareDistanceTo(var9))) {
         var9 = var6;
      }

      if (var7 != null && (var9 == null || var1.squareDistanceTo(var7) < var1.squareDistanceTo(var9))) {
         var9 = var7;
      }

      if (var8 != null && (var9 == null || var1.squareDistanceTo(var8) < var1.squareDistanceTo(var9))) {
         var9 = var8;
      }

      if (var9 == null) {
         return null;
      } else {
         EnumFacing var10 = null;
         if (var9 == var3) {
            var10 = EnumFacing.WEST;
         } else if (var9 == var4) {
            var10 = EnumFacing.EAST;
         } else if (var9 == var5) {
            var10 = EnumFacing.DOWN;
         } else if (var9 == var6) {
            var10 = EnumFacing.UP;
         } else if (var9 == var7) {
            var10 = EnumFacing.NORTH;
         } else {
            var10 = EnumFacing.SOUTH;
         }

         return new MovingObjectPosition(var9, var10);
      }
   }
}
