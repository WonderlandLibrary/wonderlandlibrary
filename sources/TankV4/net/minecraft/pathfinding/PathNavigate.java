package net.minecraft.pathfinding;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.World;

public abstract class PathNavigate {
   private final IAttributeInstance pathSearchRange;
   private float heightRequirement = 1.0F;
   protected World worldObj;
   private int ticksAtLastPos;
   private int totalTicks;
   private final PathFinder pathFinder;
   protected double speed;
   protected EntityLiving theEntity;
   protected PathEntity currentPath;
   private Vec3 lastPosCheck = new Vec3(0.0D, 0.0D, 0.0D);

   public PathEntity getPath() {
      return this.currentPath;
   }

   public final PathEntity getPathToXYZ(double var1, double var3, double var5) {
      return this.getPathToPos(new BlockPos(MathHelper.floor_double(var1), (int)var3, MathHelper.floor_double(var5)));
   }

   protected abstract Vec3 getEntityPosition();

   protected abstract boolean canNavigate();

   protected void pathFollow() {
      Vec3 var1 = this.getEntityPosition();
      int var2 = this.currentPath.getCurrentPathLength();

      for(int var3 = this.currentPath.getCurrentPathIndex(); var3 < this.currentPath.getCurrentPathLength(); ++var3) {
         if (this.currentPath.getPathPointFromIndex(var3).yCoord != (int)var1.yCoord) {
            var2 = var3;
            break;
         }
      }

      float var8 = this.theEntity.width * this.theEntity.width * this.heightRequirement;

      int var4;
      for(var4 = this.currentPath.getCurrentPathIndex(); var4 < var2; ++var4) {
         Vec3 var5 = this.currentPath.getVectorFromIndex(this.theEntity, var4);
         if (var1.squareDistanceTo(var5) < (double)var8) {
            this.currentPath.setCurrentPathIndex(var4 + 1);
         }
      }

      var4 = MathHelper.ceiling_float_int(this.theEntity.width);
      int var9 = (int)this.theEntity.height + 1;
      int var6 = var4;

      for(int var7 = var2 - 1; var7 >= this.currentPath.getCurrentPathIndex(); --var7) {
         if (this.isDirectPathBetweenPoints(var1, this.currentPath.getVectorFromIndex(this.theEntity, var7), var4, var9, var6)) {
            this.currentPath.setCurrentPathIndex(var7);
            break;
         }
      }

      this.checkForStuck(var1);
   }

   public void setSpeed(double var1) {
      this.speed = var1;
   }

   public void clearPathEntity() {
      this.currentPath = null;
   }

   protected void removeSunnyPath() {
   }

   public void setHeightRequirement(float var1) {
      this.heightRequirement = var1;
   }

   public PathEntity getPathToPos(BlockPos var1) {
      if (!this.canNavigate()) {
         return null;
      } else {
         float var2 = this.getPathSearchRange();
         this.worldObj.theProfiler.startSection("pathfind");
         BlockPos var3 = new BlockPos(this.theEntity);
         int var4 = (int)(var2 + 8.0F);
         ChunkCache var5 = new ChunkCache(this.worldObj, var3.add(-var4, -var4, -var4), var3.add(var4, var4, var4), 0);
         PathEntity var6 = this.pathFinder.createEntityPathTo(var5, this.theEntity, (BlockPos)var1, var2);
         this.worldObj.theProfiler.endSection();
         return var6;
      }
   }

   public PathNavigate(EntityLiving var1, World var2) {
      this.theEntity = var1;
      this.worldObj = var2;
      this.pathSearchRange = var1.getEntityAttribute(SharedMonsterAttributes.followRange);
      this.pathFinder = this.getPathFinder();
   }

   public boolean tryMoveToEntityLiving(Entity var1, double var2) {
      PathEntity var4 = this.getPathToEntityLiving(var1);
      return var4 != null ? this.setPath(var4, var2) : false;
   }

   public boolean tryMoveToXYZ(double var1, double var3, double var5, double var7) {
      PathEntity var9 = this.getPathToXYZ((double)MathHelper.floor_double(var1), (double)((int)var3), (double)MathHelper.floor_double(var5));
      return this.setPath(var9, var7);
   }

   public float getPathSearchRange() {
      return (float)this.pathSearchRange.getAttributeValue();
   }

   public PathEntity getPathToEntityLiving(Entity var1) {
      if (!this.canNavigate()) {
         return null;
      } else {
         float var2 = this.getPathSearchRange();
         this.worldObj.theProfiler.startSection("pathfind");
         BlockPos var3 = (new BlockPos(this.theEntity)).up();
         int var4 = (int)(var2 + 16.0F);
         ChunkCache var5 = new ChunkCache(this.worldObj, var3.add(-var4, -var4, -var4), var3.add(var4, var4, var4), 0);
         PathEntity var6 = this.pathFinder.createEntityPathTo(var5, this.theEntity, (Entity)var1, var2);
         this.worldObj.theProfiler.endSection();
         return var6;
      }
   }

   protected void checkForStuck(Vec3 var1) {
      if (this.totalTicks - this.ticksAtLastPos > 100) {
         if (var1.squareDistanceTo(this.lastPosCheck) < 2.25D) {
            this.clearPathEntity();
         }

         this.ticksAtLastPos = this.totalTicks;
         this.lastPosCheck = var1;
      }

   }

   protected abstract boolean isDirectPathBetweenPoints(Vec3 var1, Vec3 var2, int var3, int var4, int var5);

   public boolean setPath(PathEntity var1, double var2) {
      if (var1 == null) {
         this.currentPath = null;
         return false;
      } else {
         if (!var1.isSamePath(this.currentPath)) {
            this.currentPath = var1;
         }

         this.removeSunnyPath();
         if (this.currentPath.getCurrentPathLength() == 0) {
            return false;
         } else {
            this.speed = var2;
            Vec3 var4 = this.getEntityPosition();
            this.ticksAtLastPos = this.totalTicks;
            this.lastPosCheck = var4;
            return true;
         }
      }
   }

   protected boolean isInLiquid() {
      return this.theEntity.isInWater() || this.theEntity.isInLava();
   }

   public void onUpdateNavigation() {
      ++this.totalTicks;
      if (this != null) {
         Vec3 var1;
         if (this.canNavigate()) {
            this.pathFollow();
         } else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength()) {
            var1 = this.getEntityPosition();
            Vec3 var2 = this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex());
            if (var1.yCoord > var2.yCoord && !this.theEntity.onGround && MathHelper.floor_double(var1.xCoord) == MathHelper.floor_double(var2.xCoord) && MathHelper.floor_double(var1.zCoord) == MathHelper.floor_double(var2.zCoord)) {
               this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
            }
         }

         if (this != null) {
            var1 = this.currentPath.getPosition(this.theEntity);
            if (var1 != null) {
               AxisAlignedBB var8 = (new AxisAlignedBB(var1.xCoord, var1.yCoord, var1.zCoord, var1.xCoord, var1.yCoord, var1.zCoord)).expand(0.5D, 0.5D, 0.5D);
               List var3 = this.worldObj.getCollidingBoundingBoxes(this.theEntity, var8.addCoord(0.0D, -1.0D, 0.0D));
               double var4 = -1.0D;
               var8 = var8.offset(0.0D, 1.0D, 0.0D);

               AxisAlignedBB var6;
               for(Iterator var7 = var3.iterator(); var7.hasNext(); var4 = var6.calculateYOffset(var8, var4)) {
                  var6 = (AxisAlignedBB)var7.next();
               }

               this.theEntity.getMoveHelper().setMoveTo(var1.xCoord, var1.yCoord + var4, var1.zCoord, this.speed);
            }
         }
      }

   }

   protected abstract PathFinder getPathFinder();
}
