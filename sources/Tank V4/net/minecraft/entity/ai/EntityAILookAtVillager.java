package net.minecraft.entity.ai;

import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAILookAtVillager extends EntityAIBase {
   private EntityIronGolem theGolem;
   private int lookTime;
   private EntityVillager theVillager;

   public boolean shouldExecute() {
      if (!this.theGolem.worldObj.isDaytime()) {
         return false;
      } else if (this.theGolem.getRNG().nextInt(8000) != 0) {
         return false;
      } else {
         this.theVillager = (EntityVillager)this.theGolem.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.theGolem.getEntityBoundingBox().expand(6.0D, 2.0D, 6.0D), this.theGolem);
         return this.theVillager != null;
      }
   }

   public void startExecuting() {
      this.lookTime = 400;
      this.theGolem.setHoldingRose(true);
   }

   public boolean continueExecuting() {
      return this.lookTime > 0;
   }

   public void resetTask() {
      this.theGolem.setHoldingRose(false);
      this.theVillager = null;
   }

   public void updateTask() {
      this.theGolem.getLookHelper().setLookPositionWithEntity(this.theVillager, 30.0F, 30.0F);
      --this.lookTime;
   }

   public EntityAILookAtVillager(EntityIronGolem var1) {
      this.theGolem = var1;
      this.setMutexBits(3);
   }
}
