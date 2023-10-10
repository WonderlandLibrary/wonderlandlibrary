package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityAIBeg extends EntityAIBase {
   private World worldObject;
   private float minPlayerDistance;
   private EntityWolf theWolf;
   private int timeoutCounter;
   private EntityPlayer thePlayer;

   public void updateTask() {
      this.theWolf.getLookHelper().setLookPosition(this.thePlayer.posX, this.thePlayer.posY + (double)this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0F, (float)this.theWolf.getVerticalFaceSpeed());
      --this.timeoutCounter;
   }

   public EntityAIBeg(EntityWolf var1, float var2) {
      this.theWolf = var1;
      this.worldObject = var1.worldObj;
      this.minPlayerDistance = var2;
      this.setMutexBits(2);
   }

   public void resetTask() {
      this.theWolf.setBegging(false);
      this.thePlayer = null;
   }

   public void startExecuting() {
      this.theWolf.setBegging(true);
      this.timeoutCounter = 40 + this.theWolf.getRNG().nextInt(40);
   }

   public boolean shouldExecute() {
      this.thePlayer = this.worldObject.getClosestPlayerToEntity(this.theWolf, (double)this.minPlayerDistance);
      return this.thePlayer == null ? false : this.hasPlayerGotBoneInHand(this.thePlayer);
   }

   public boolean continueExecuting() {
      EntityAIBeg var10000;
      if (!this.thePlayer.isEntityAlive()) {
         var10000 = false;
      } else if (this.theWolf.getDistanceSqToEntity(this.thePlayer) > (double)(this.minPlayerDistance * this.minPlayerDistance)) {
         var10000 = false;
      } else {
         if (this.timeoutCounter > 0) {
            var10000 = this;
            if (this.thePlayer == null) {
               boolean var10001 = true;
               return (boolean)var10000;
            }
         }

         var10000 = false;
      }

      return (boolean)var10000;
   }
}
