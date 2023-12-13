package net.minecraft.world.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class WalkNodeProcessor extends NodeProcessor {
   private boolean avoidsWater;
   private boolean shouldAvoidWater;
   private boolean canEnterDoors;
   private boolean canBreakDoors;
   private boolean canSwim;

   private int getVerticalOffset(Entity var1, int var2, int var3, int var4) {
      return func_176170_a(this.blockaccess, var1, var2, var3, var4, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.avoidsWater, this.canBreakDoors, this.canEnterDoors);
   }

   public boolean getEnterDoors() {
      return this.canEnterDoors;
   }

   public void initProcessor(IBlockAccess var1, Entity var2) {
      super.initProcessor(var1, var2);
      this.shouldAvoidWater = this.avoidsWater;
   }

   public PathPoint getPathPointTo(Entity var1) {
      int var2;
      if (this.canSwim && var1.isInWater()) {
         var2 = (int)var1.getEntityBoundingBox().minY;
         BlockPos.MutableBlockPos var3 = new BlockPos.MutableBlockPos(MathHelper.floor_double(var1.posX), var2, MathHelper.floor_double(var1.posZ));

         for(Block var4 = this.blockaccess.getBlockState(var3).getBlock(); var4 == Blocks.flowing_water || var4 == Blocks.water; var4 = this.blockaccess.getBlockState(var3).getBlock()) {
            ++var2;
            var3.func_181079_c(MathHelper.floor_double(var1.posX), var2, MathHelper.floor_double(var1.posZ));
         }

         this.avoidsWater = false;
      } else {
         var2 = MathHelper.floor_double(var1.getEntityBoundingBox().minY + 0.5D);
      }

      return this.openPoint(MathHelper.floor_double(var1.getEntityBoundingBox().minX), var2, MathHelper.floor_double(var1.getEntityBoundingBox().minZ));
   }

   public void postProcess() {
      super.postProcess();
      this.avoidsWater = this.shouldAvoidWater;
   }

   public void setCanSwim(boolean var1) {
      this.canSwim = var1;
   }

   public void setBreakDoors(boolean var1) {
      this.canBreakDoors = var1;
   }

   public int findPathOptions(PathPoint[] var1, Entity var2, PathPoint var3, PathPoint var4, float var5) {
      int var6 = 0;
      byte var7 = 0;
      if (this.getVerticalOffset(var2, var3.xCoord, var3.yCoord + 1, var3.zCoord) == 1) {
         var7 = 1;
      }

      PathPoint var8 = this.getSafePoint(var2, var3.xCoord, var3.yCoord, var3.zCoord + 1, var7);
      PathPoint var9 = this.getSafePoint(var2, var3.xCoord - 1, var3.yCoord, var3.zCoord, var7);
      PathPoint var10 = this.getSafePoint(var2, var3.xCoord + 1, var3.yCoord, var3.zCoord, var7);
      PathPoint var11 = this.getSafePoint(var2, var3.xCoord, var3.yCoord, var3.zCoord - 1, var7);
      if (var8 != null && !var8.visited && var8.distanceTo(var4) < var5) {
         var1[var6++] = var8;
      }

      if (var9 != null && !var9.visited && var9.distanceTo(var4) < var5) {
         var1[var6++] = var9;
      }

      if (var10 != null && !var10.visited && var10.distanceTo(var4) < var5) {
         var1[var6++] = var10;
      }

      if (var11 != null && !var11.visited && var11.distanceTo(var4) < var5) {
         var1[var6++] = var11;
      }

      return var6;
   }

   public boolean getCanSwim() {
      return this.canSwim;
   }

   public static int func_176170_a(IBlockAccess var0, Entity var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, boolean var9, boolean var10) {
      boolean var11 = false;
      BlockPos var12 = new BlockPos(var1);
      BlockPos.MutableBlockPos var13 = new BlockPos.MutableBlockPos();

      for(int var14 = var2; var14 < var2 + var5; ++var14) {
         for(int var15 = var3; var15 < var3 + var6; ++var15) {
            for(int var16 = var4; var16 < var4 + var7; ++var16) {
               var13.func_181079_c(var14, var15, var16);
               Block var17 = var0.getBlockState(var13).getBlock();
               if (var17.getMaterial() != Material.air) {
                  if (var17 != Blocks.trapdoor && var17 != Blocks.iron_trapdoor) {
                     if (var17 != Blocks.flowing_water && var17 != Blocks.water) {
                        if (!var10 && var17 instanceof BlockDoor && var17.getMaterial() == Material.wood) {
                           return 0;
                        }
                     } else {
                        if (var8) {
                           return -1;
                        }

                        var11 = true;
                     }
                  } else {
                     var11 = true;
                  }

                  if (var1.worldObj.getBlockState(var13).getBlock() instanceof BlockRailBase) {
                     if (!(var1.worldObj.getBlockState(var12).getBlock() instanceof BlockRailBase) && !(var1.worldObj.getBlockState(var12.down()).getBlock() instanceof BlockRailBase)) {
                        return -3;
                     }
                  } else if (!var17.isPassable(var0, var13) && (!var9 || !(var17 instanceof BlockDoor) || var17.getMaterial() != Material.wood)) {
                     if (!(var17 instanceof BlockFence) && !(var17 instanceof BlockFenceGate) && !(var17 instanceof BlockWall)) {
                        if (var17 != Blocks.trapdoor && var17 != Blocks.iron_trapdoor) {
                           Material var18 = var17.getMaterial();
                           if (var18 != Material.lava) {
                              return 0;
                           }

                           if (!var1.isInLava()) {
                              return -2;
                           }
                           continue;
                        }

                        return -4;
                     }

                     return -3;
                  }
               }
            }
         }
      }

      return var11 ? 2 : 1;
   }

   public void setAvoidsWater(boolean var1) {
      this.avoidsWater = var1;
   }

   public boolean getAvoidsWater() {
      return this.avoidsWater;
   }

   private PathPoint getSafePoint(Entity var1, int var2, int var3, int var4, int var5) {
      PathPoint var6 = null;
      int var7 = this.getVerticalOffset(var1, var2, var3, var4);
      if (var7 == 2) {
         return this.openPoint(var2, var3, var4);
      } else {
         if (var7 == 1) {
            var6 = this.openPoint(var2, var3, var4);
         }

         if (var6 == null && var5 > 0 && var7 != -3 && var7 != -4 && this.getVerticalOffset(var1, var2, var3 + var5, var4) == 1) {
            var6 = this.openPoint(var2, var3 + var5, var4);
            var3 += var5;
         }

         if (var6 != null) {
            int var8 = 0;

            int var9;
            for(var9 = 0; var3 > 0; var6 = this.openPoint(var2, var3, var4)) {
               var9 = this.getVerticalOffset(var1, var2, var3 - 1, var4);
               if (this.avoidsWater && var9 == -1) {
                  return null;
               }

               if (var9 != 1) {
                  break;
               }

               if (var8++ >= var1.getMaxFallHeight()) {
                  return null;
               }

               --var3;
               if (var3 <= 0) {
                  return null;
               }
            }

            if (var9 == -2) {
               return null;
            }
         }

         return var6;
      }
   }

   public void setEnterDoors(boolean var1) {
      this.canEnterDoors = var1;
   }

   public PathPoint getPathPointToCoords(Entity var1, double var2, double var4, double var6) {
      return this.openPoint(MathHelper.floor_double(var2 - (double)(var1.width / 2.0F)), MathHelper.floor_double(var4), MathHelper.floor_double(var6 - (double)(var1.width / 2.0F)));
   }
}
