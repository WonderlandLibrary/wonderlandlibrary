package net.minecraft.world;

import net.minecraft.util.BlockPos;

public class ChunkCoordIntPair {
   public final int chunkZPos;
   private static final String __OBFID = "CL_00000133";
   public final int chunkXPos;
   private int cachedHashCode = 0;

   public int getCenterXPos() {
      return (this.chunkXPos << 4) + 8;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof ChunkCoordIntPair)) {
         return false;
      } else {
         ChunkCoordIntPair var2 = (ChunkCoordIntPair)var1;
         return this.chunkXPos == var2.chunkXPos && this.chunkZPos == var2.chunkZPos;
      }
   }

   public BlockPos getCenterBlock(int var1) {
      return new BlockPos(this.getCenterXPos(), var1, this.getCenterZPosition());
   }

   public int getCenterZPosition() {
      return (this.chunkZPos << 4) + 8;
   }

   public int hashCode() {
      if (this.cachedHashCode == 0) {
         int var1 = 1664525 * this.chunkXPos + 1013904223;
         int var2 = 1664525 * (this.chunkZPos ^ -559038737) + 1013904223;
         this.cachedHashCode = var1 ^ var2;
      }

      return this.cachedHashCode;
   }

   public String toString() {
      return "[" + this.chunkXPos + ", " + this.chunkZPos + "]";
   }

   public int getXEnd() {
      return (this.chunkXPos << 4) + 15;
   }

   public ChunkCoordIntPair(int var1, int var2) {
      this.chunkXPos = var1;
      this.chunkZPos = var2;
   }

   public int getZStart() {
      return this.chunkZPos << 4;
   }

   public int getXStart() {
      return this.chunkXPos << 4;
   }

   public int getZEnd() {
      return (this.chunkZPos << 4) + 15;
   }

   public BlockPos getBlock(int var1, int var2, int var3) {
      return new BlockPos((this.chunkXPos << 4) + var1, var2, (this.chunkZPos << 4) + var3);
   }

   public static long chunkXZ2Int(int var0, int var1) {
      return (long)var0 & 4294967295L | ((long)var1 & 4294967295L) << 32;
   }
}
