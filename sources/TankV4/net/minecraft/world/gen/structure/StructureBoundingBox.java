package net.minecraft.world.gen.structure;

import com.google.common.base.Objects;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

public class StructureBoundingBox {
   public int minZ;
   public int minX;
   public int maxY;
   public int maxX;
   private static volatile int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing;
   public int minY;
   public int maxZ;

   public static StructureBoundingBox getComponentToAddBoundingBox(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, EnumFacing var9) {
      switch($SWITCH_TABLE$net$minecraft$util$EnumFacing()[var9.ordinal()]) {
      case 3:
         return new StructureBoundingBox(var0 + var3, var1 + var4, var2 - var8 + 1 + var5, var0 + var6 - 1 + var3, var1 + var7 - 1 + var4, var2 + var5);
      case 4:
         return new StructureBoundingBox(var0 + var3, var1 + var4, var2 + var5, var0 + var6 - 1 + var3, var1 + var7 - 1 + var4, var2 + var8 - 1 + var5);
      case 5:
         return new StructureBoundingBox(var0 - var8 + 1 + var5, var1 + var4, var2 + var3, var0 + var5, var1 + var7 - 1 + var4, var2 + var6 - 1 + var3);
      case 6:
         return new StructureBoundingBox(var0 + var5, var1 + var4, var2 + var3, var0 + var8 - 1 + var5, var1 + var7 - 1 + var4, var2 + var6 - 1 + var3);
      default:
         return new StructureBoundingBox(var0 + var3, var1 + var4, var2 + var5, var0 + var6 - 1 + var3, var1 + var7 - 1 + var4, var2 + var8 - 1 + var5);
      }
   }

   public void expandTo(StructureBoundingBox var1) {
      this.minX = Math.min(this.minX, var1.minX);
      this.minY = Math.min(this.minY, var1.minY);
      this.minZ = Math.min(this.minZ, var1.minZ);
      this.maxX = Math.max(this.maxX, var1.maxX);
      this.maxY = Math.max(this.maxY, var1.maxY);
      this.maxZ = Math.max(this.maxZ, var1.maxZ);
   }

   public StructureBoundingBox(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.minX = var1;
      this.minY = var2;
      this.minZ = var3;
      this.maxX = var4;
      this.maxY = var5;
      this.maxZ = var6;
   }

   public StructureBoundingBox(int[] var1) {
      if (var1.length == 6) {
         this.minX = var1[0];
         this.minY = var1[1];
         this.minZ = var1[2];
         this.maxX = var1[3];
         this.maxY = var1[4];
         this.maxZ = var1[5];
      }

   }

   public int getYSize() {
      return this.maxY - this.minY + 1;
   }

   public String toString() {
      return Objects.toStringHelper((Object)this).add("x0", this.minX).add("y0", this.minY).add("z0", this.minZ).add("x1", this.maxX).add("y1", this.maxY).add("z1", this.maxZ).toString();
   }

   public int getZSize() {
      return this.maxZ - this.minZ + 1;
   }

   public boolean intersectsWith(int var1, int var2, int var3, int var4) {
      return this.maxX >= var1 && this.minX <= var3 && this.maxZ >= var2 && this.minZ <= var4;
   }

   public static StructureBoundingBox getNewBoundingBox() {
      return new StructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
   }

   public int getXSize() {
      return this.maxX - this.minX + 1;
   }

   public Vec3i func_175896_b() {
      return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
   }

   public static StructureBoundingBox func_175899_a(int var0, int var1, int var2, int var3, int var4, int var5) {
      return new StructureBoundingBox(Math.min(var0, var3), Math.min(var1, var4), Math.min(var2, var5), Math.max(var0, var3), Math.max(var1, var4), Math.max(var2, var5));
   }

   static int[] $SWITCH_TABLE$net$minecraft$util$EnumFacing() {
      int[] var10000 = $SWITCH_TABLE$net$minecraft$util$EnumFacing;
      if (var10000 != null) {
         return var10000;
      } else {
         int[] var0 = new int[EnumFacing.values().length];

         try {
            var0[EnumFacing.DOWN.ordinal()] = 1;
         } catch (NoSuchFieldError var6) {
         }

         try {
            var0[EnumFacing.EAST.ordinal()] = 6;
         } catch (NoSuchFieldError var5) {
         }

         try {
            var0[EnumFacing.NORTH.ordinal()] = 3;
         } catch (NoSuchFieldError var4) {
         }

         try {
            var0[EnumFacing.SOUTH.ordinal()] = 4;
         } catch (NoSuchFieldError var3) {
         }

         try {
            var0[EnumFacing.UP.ordinal()] = 2;
         } catch (NoSuchFieldError var2) {
         }

         try {
            var0[EnumFacing.WEST.ordinal()] = 5;
         } catch (NoSuchFieldError var1) {
         }

         $SWITCH_TABLE$net$minecraft$util$EnumFacing = var0;
         return var0;
      }
   }

   public Vec3i getCenter() {
      return new BlockPos(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2, this.minZ + (this.maxZ - this.minZ + 1) / 2);
   }

   public StructureBoundingBox(int var1, int var2, int var3, int var4) {
      this.minX = var1;
      this.minZ = var2;
      this.maxX = var3;
      this.maxZ = var4;
      this.minY = 1;
      this.maxY = 512;
   }

   public NBTTagIntArray toNBTTagIntArray() {
      return new NBTTagIntArray(new int[]{this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ});
   }

   public boolean intersectsWith(StructureBoundingBox var1) {
      return this.maxX >= var1.minX && this.minX <= var1.maxX && this.maxZ >= var1.minZ && this.minZ <= var1.maxZ && this.maxY >= var1.minY && this.minY <= var1.maxY;
   }

   public boolean isVecInside(Vec3i var1) {
      return var1.getX() >= this.minX && var1.getX() <= this.maxX && var1.getZ() >= this.minZ && var1.getZ() <= this.maxZ && var1.getY() >= this.minY && var1.getY() <= this.maxY;
   }

   public void offset(int var1, int var2, int var3) {
      this.minX += var1;
      this.minY += var2;
      this.minZ += var3;
      this.maxX += var1;
      this.maxY += var2;
      this.maxZ += var3;
   }

   public StructureBoundingBox() {
   }

   public StructureBoundingBox(Vec3i var1, Vec3i var2) {
      this.minX = Math.min(var1.getX(), var2.getX());
      this.minY = Math.min(var1.getY(), var2.getY());
      this.minZ = Math.min(var1.getZ(), var2.getZ());
      this.maxX = Math.max(var1.getX(), var2.getX());
      this.maxY = Math.max(var1.getY(), var2.getY());
      this.maxZ = Math.max(var1.getZ(), var2.getZ());
   }

   public StructureBoundingBox(StructureBoundingBox var1) {
      this.minX = var1.minX;
      this.minY = var1.minY;
      this.minZ = var1.minZ;
      this.maxX = var1.maxX;
      this.maxY = var1.maxY;
      this.maxZ = var1.maxZ;
   }
}
