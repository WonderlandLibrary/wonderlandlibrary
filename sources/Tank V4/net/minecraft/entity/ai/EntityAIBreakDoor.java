package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.EnumDifficulty;

public class EntityAIBreakDoor extends EntityAIDoorInteract {
   private int previousBreakProgress = -1;
   private int breakingTime;

   public boolean continueExecuting() {
      double var1 = this.theEntity.getDistanceSq(this.doorPosition);
      boolean var3;
      if (this.breakingTime <= 240) {
         BlockDoor var4 = this.doorBlock;
         if (!BlockDoor.isOpen(this.theEntity.worldObj, this.doorPosition) && var1 < 4.0D) {
            var3 = true;
            return var3;
         }
      }

      var3 = false;
      return var3;
   }

   public void updateTask() {
      super.updateTask();
      if (this.theEntity.getRNG().nextInt(20) == 0) {
         this.theEntity.worldObj.playAuxSFX(1010, this.doorPosition, 0);
      }

      ++this.breakingTime;
      int var1 = (int)((float)this.breakingTime / 240.0F * 10.0F);
      if (var1 != this.previousBreakProgress) {
         this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, var1);
         this.previousBreakProgress = var1;
      }

      if (this.breakingTime == 240 && this.theEntity.worldObj.getDifficulty() == EnumDifficulty.HARD) {
         this.theEntity.worldObj.setBlockToAir(this.doorPosition);
         this.theEntity.worldObj.playAuxSFX(1012, this.doorPosition, 0);
         this.theEntity.worldObj.playAuxSFX(2001, this.doorPosition, Block.getIdFromBlock(this.doorBlock));
      }

   }

   public boolean shouldExecute() {
      if (!super.shouldExecute()) {
         return false;
      } else if (!this.theEntity.worldObj.getGameRules().getBoolean("mobGriefing")) {
         return false;
      } else {
         BlockDoor var1 = this.doorBlock;
         return !BlockDoor.isOpen(this.theEntity.worldObj, this.doorPosition);
      }
   }

   public void resetTask() {
      super.resetTask();
      this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, -1);
   }

   public EntityAIBreakDoor(EntityLiving var1) {
      super(var1);
   }

   public void startExecuting() {
      super.startExecuting();
      this.breakingTime = 0;
   }
}
