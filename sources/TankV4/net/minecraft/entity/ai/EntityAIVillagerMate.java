package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityAIVillagerMate extends EntityAIBase {
   private int matingTimeout;
   private EntityVillager villagerObj;
   private EntityVillager mate;
   private World worldObj;
   Village villageObj;

   public EntityAIVillagerMate(EntityVillager var1) {
      this.villagerObj = var1;
      this.worldObj = var1.worldObj;
      this.setMutexBits(3);
   }

   public boolean continueExecuting() {
      return this.matingTimeout >= 0 && this != false && this.villagerObj.getGrowingAge() == 0 && this.villagerObj.getIsWillingToMate(false);
   }

   public boolean shouldExecute() {
      if (this.villagerObj.getGrowingAge() != 0) {
         return false;
      } else if (this.villagerObj.getRNG().nextInt(500) != 0) {
         return false;
      } else {
         this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(new BlockPos(this.villagerObj), 0);
         if (this.villageObj == null) {
            return false;
         } else if (this == false && this.villagerObj.getIsWillingToMate(true)) {
            Entity var1 = this.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.villagerObj.getEntityBoundingBox().expand(8.0D, 3.0D, 8.0D), this.villagerObj);
            if (var1 == null) {
               return false;
            } else {
               this.mate = (EntityVillager)var1;
               return this.mate.getGrowingAge() == 0 && this.mate.getIsWillingToMate(true);
            }
         } else {
            return false;
         }
      }
   }

   private void giveBirth() {
      EntityVillager var1 = this.villagerObj.createChild(this.mate);
      this.mate.setGrowingAge(6000);
      this.villagerObj.setGrowingAge(6000);
      this.mate.setIsWillingToMate(false);
      this.villagerObj.setIsWillingToMate(false);
      var1.setGrowingAge(-24000);
      var1.setLocationAndAngles(this.villagerObj.posX, this.villagerObj.posY, this.villagerObj.posZ, 0.0F, 0.0F);
      this.worldObj.spawnEntityInWorld(var1);
      this.worldObj.setEntityState(var1, (byte)12);
   }

   public void startExecuting() {
      this.matingTimeout = 300;
      this.villagerObj.setMating(true);
   }

   public void resetTask() {
      this.villageObj = null;
      this.mate = null;
      this.villagerObj.setMating(false);
   }

   public void updateTask() {
      --this.matingTimeout;
      this.villagerObj.getLookHelper().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);
      if (this.villagerObj.getDistanceSqToEntity(this.mate) > 2.25D) {
         this.villagerObj.getNavigator().tryMoveToEntityLiving(this.mate, 0.25D);
      } else if (this.matingTimeout == 0 && this.mate.isMating()) {
         this.giveBirth();
      }

      if (this.villagerObj.getRNG().nextInt(35) == 0) {
         this.worldObj.setEntityState(this.villagerObj, (byte)12);
      }

   }
}
