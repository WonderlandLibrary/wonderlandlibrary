package net.minecraft.world.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class SwimNodeProcessor extends NodeProcessor {
   public PathPoint getPathPointTo(Entity var1) {
      return this.openPoint(MathHelper.floor_double(var1.getEntityBoundingBox().minX), MathHelper.floor_double(var1.getEntityBoundingBox().minY + 0.5D), MathHelper.floor_double(var1.getEntityBoundingBox().minZ));
   }

   public void postProcess() {
      super.postProcess();
   }

   public void initProcessor(IBlockAccess var1, Entity var2) {
      super.initProcessor(var1, var2);
   }

   public PathPoint getPathPointToCoords(Entity var1, double var2, double var4, double var6) {
      return this.openPoint(MathHelper.floor_double(var2 - (double)(var1.width / 2.0F)), MathHelper.floor_double(var4 + 0.5D), MathHelper.floor_double(var6 - (double)(var1.width / 2.0F)));
   }

   private PathPoint getSafePoint(Entity var1, int var2, int var3, int var4) {
      int var5 = this.func_176186_b(var1, var2, var3, var4);
      return var5 == -1 ? this.openPoint(var2, var3, var4) : null;
   }

   public int findPathOptions(PathPoint[] var1, Entity var2, PathPoint var3, PathPoint var4, float var5) {
      int var6 = 0;
      EnumFacing[] var10;
      int var9 = (var10 = EnumFacing.values()).length;

      for(int var8 = 0; var8 < var9; ++var8) {
         EnumFacing var7 = var10[var8];
         PathPoint var11 = this.getSafePoint(var2, var3.xCoord + var7.getFrontOffsetX(), var3.yCoord + var7.getFrontOffsetY(), var3.zCoord + var7.getFrontOffsetZ());
         if (var11 != null && !var11.visited && var11.distanceTo(var4) < var5) {
            var1[var6++] = var11;
         }
      }

      return var6;
   }

   private int func_176186_b(Entity var1, int var2, int var3, int var4) {
      BlockPos.MutableBlockPos var5 = new BlockPos.MutableBlockPos();

      for(int var6 = var2; var6 < var2 + this.entitySizeX; ++var6) {
         for(int var7 = var3; var7 < var3 + this.entitySizeY; ++var7) {
            for(int var8 = var4; var8 < var4 + this.entitySizeZ; ++var8) {
               Block var9 = this.blockaccess.getBlockState(var5.func_181079_c(var6, var7, var8)).getBlock();
               if (var9.getMaterial() != Material.water) {
                  return 0;
               }
            }
         }
      }

      return -1;
   }
}
