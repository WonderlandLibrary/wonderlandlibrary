package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract class EntityAIMoveToBlock extends EntityAIBase {
   private int field_179490_f;
   private final double movementSpeed;
   protected int runDelay;
   private int timeoutCounter;
   protected BlockPos destinationBlock;
   private final EntityCreature theEntity;
   private boolean isAboveDestination;
   private int searchLength;

   protected abstract boolean shouldMoveTo(World var1, BlockPos var2);

   public void resetTask() {
   }

   public EntityAIMoveToBlock(EntityCreature var1, double var2, int var4) {
      this.destinationBlock = BlockPos.ORIGIN;
      this.theEntity = var1;
      this.movementSpeed = var2;
      this.searchLength = var4;
      this.setMutexBits(5);
   }

   public void startExecuting() {
      this.theEntity.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
      this.timeoutCounter = 0;
      this.field_179490_f = this.theEntity.getRNG().nextInt(this.theEntity.getRNG().nextInt(1200) + 1200) + 1200;
   }

   private boolean searchForDestination() {
      int var1 = this.searchLength;
      boolean var2 = true;
      BlockPos var3 = new BlockPos(this.theEntity);

      for(int var4 = 0; var4 <= 1; var4 = var4 > 0 ? -var4 : 1 - var4) {
         for(int var5 = 0; var5 < var1; ++var5) {
            for(int var6 = 0; var6 <= var5; var6 = var6 > 0 ? -var6 : 1 - var6) {
               for(int var7 = var6 < var5 && var6 > -var5 ? var5 : 0; var7 <= var5; var7 = var7 > 0 ? -var7 : 1 - var7) {
                  BlockPos var8 = var3.add(var6, var4 - 1, var7);
                  if (this.theEntity.isWithinHomeDistanceFromPosition(var8) && this.shouldMoveTo(this.theEntity.worldObj, var8)) {
                     this.destinationBlock = var8;
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   protected boolean getIsAboveDestination() {
      return this.isAboveDestination;
   }

   public boolean continueExecuting() {
      return this.timeoutCounter >= -this.field_179490_f && this.timeoutCounter <= 1200 && this.shouldMoveTo(this.theEntity.worldObj, this.destinationBlock);
   }

   public void updateTask() {
      if (this.theEntity.getDistanceSqToCenter(this.destinationBlock.up()) > 1.0D) {
         this.isAboveDestination = false;
         ++this.timeoutCounter;
         if (this.timeoutCounter % 40 == 0) {
            this.theEntity.getNavigator().tryMoveToXYZ((double)((float)this.destinationBlock.getX()) + 0.5D, (double)(this.destinationBlock.getY() + 1), (double)((float)this.destinationBlock.getZ()) + 0.5D, this.movementSpeed);
         }
      } else {
         this.isAboveDestination = true;
         --this.timeoutCounter;
      }

   }

   public boolean shouldExecute() {
      if (this.runDelay > 0) {
         --this.runDelay;
         return false;
      } else {
         this.runDelay = 200 + this.theEntity.getRNG().nextInt(200);
         return this.searchForDestination();
      }
   }
}
