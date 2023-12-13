package net.minecraft.pathfinding;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

public class PathNavigateGround extends PathNavigate {
   private boolean shouldAvoidSun;
   protected WalkNodeProcessor nodeProcessor;

   public void setAvoidsWater(boolean var1) {
      this.nodeProcessor.setAvoidsWater(var1);
   }

   public void setEnterDoors(boolean var1) {
      this.nodeProcessor.setEnterDoors(var1);
   }

   protected PathFinder getPathFinder() {
      this.nodeProcessor = new WalkNodeProcessor();
      this.nodeProcessor.setEnterDoors(true);
      return new PathFinder(this.nodeProcessor);
   }

   protected Vec3 getEntityPosition() {
      return new Vec3(this.theEntity.posX, (double)this.getPathablePosY(), this.theEntity.posZ);
   }

   private int getPathablePosY() {
      if (this.theEntity.isInWater() && this.getCanSwim()) {
         int var1 = (int)this.theEntity.getEntityBoundingBox().minY;
         Block var2 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), var1, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
         int var3 = 0;

         do {
            if (var2 != Blocks.flowing_water && var2 != Blocks.water) {
               return var1;
            }

            ++var1;
            var2 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), var1, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
            ++var3;
         } while(var3 <= 16);

         return (int)this.theEntity.getEntityBoundingBox().minY;
      } else {
         return (int)(this.theEntity.getEntityBoundingBox().minY + 0.5D);
      }
   }

   public void setCanSwim(boolean var1) {
      this.nodeProcessor.setCanSwim(var1);
   }

   public boolean getAvoidsWater() {
      return this.nodeProcessor.getAvoidsWater();
   }

   public boolean getCanSwim() {
      return this.nodeProcessor.getCanSwim();
   }

   public void setAvoidSun(boolean var1) {
      this.shouldAvoidSun = var1;
   }

   public PathNavigateGround(EntityLiving var1, World var2) {
      super(var1, var2);
   }

   protected boolean isDirectPathBetweenPoints(Vec3 var1, Vec3 var2, int var3, int var4, int var5) {
      int var6 = MathHelper.floor_double(var1.xCoord);
      int var7 = MathHelper.floor_double(var1.zCoord);
      double var8 = var2.xCoord - var1.xCoord;
      double var10 = var2.zCoord - var1.zCoord;
      double var12 = var8 * var8 + var10 * var10;
      if (var12 < 1.0E-8D) {
         return false;
      } else {
         double var14 = 1.0D / Math.sqrt(var12);
         var8 *= var14;
         var10 *= var14;
         var3 += 2;
         var5 += 2;
         int var10002 = (int)var1.yCoord;
         if (var10 != false) {
            return false;
         } else {
            var3 -= 2;
            var5 -= 2;
            double var16 = 1.0D / Math.abs(var8);
            double var18 = 1.0D / Math.abs(var10);
            double var20 = (double)(var6 * 1) - var1.xCoord;
            double var22 = (double)(var7 * 1) - var1.zCoord;
            if (var8 >= 0.0D) {
               ++var20;
            }

            if (var10 >= 0.0D) {
               ++var22;
            }

            var20 /= var8;
            var22 /= var10;
            int var24 = var8 < 0.0D ? -1 : 1;
            int var25 = var10 < 0.0D ? -1 : 1;
            int var26 = MathHelper.floor_double(var2.xCoord);
            int var27 = MathHelper.floor_double(var2.zCoord);
            int var28 = var26 - var6;
            int var29 = var27 - var7;

            do {
               if (var28 * var24 <= 0 && var29 * var25 <= 0) {
                  return true;
               }

               if (var20 < var22) {
                  var20 += var16;
                  var6 += var24;
                  var28 = var26 - var6;
               } else {
                  var22 += var18;
                  var7 += var25;
                  var29 = var27 - var7;
               }

               int var10011 = (int)var1.yCoord;
            } while(var10 == false);

            return false;
         }
      }
   }

   public void setBreakDoors(boolean var1) {
      this.nodeProcessor.setBreakDoors(var1);
   }

   protected boolean canNavigate() {
      return this.theEntity.onGround || this.getCanSwim() && this.isInLiquid() || this.theEntity.isRiding() && this.theEntity instanceof EntityZombie && this.theEntity.ridingEntity instanceof EntityChicken;
   }

   protected void removeSunnyPath() {
      super.removeSunnyPath();
      if (this.shouldAvoidSun) {
         if (this.worldObj.canSeeSky(new BlockPos(MathHelper.floor_double(this.theEntity.posX), (int)(this.theEntity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor_double(this.theEntity.posZ)))) {
            return;
         }

         for(int var1 = 0; var1 < this.currentPath.getCurrentPathLength(); ++var1) {
            PathPoint var2 = this.currentPath.getPathPointFromIndex(var1);
            if (this.worldObj.canSeeSky(new BlockPos(var2.xCoord, var2.yCoord, var2.zCoord))) {
               this.currentPath.setCurrentPathLength(var1 - 1);
               return;
            }
         }
      }

   }

   public boolean getEnterDoors() {
      return this.nodeProcessor.getEnterDoors();
   }
}
