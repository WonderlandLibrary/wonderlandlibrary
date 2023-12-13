package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAISit extends EntityAIBase {
   private EntityTameable theEntity;
   private boolean isSitting;

   public void setSitting(boolean var1) {
      this.isSitting = var1;
   }

   public void resetTask() {
      this.theEntity.setSitting(false);
   }

   public boolean shouldExecute() {
      if (!this.theEntity.isTamed()) {
         return false;
      } else if (this.theEntity.isInWater()) {
         return false;
      } else if (!this.theEntity.onGround) {
         return false;
      } else {
         EntityLivingBase var1 = this.theEntity.getOwner();
         return var1 == null ? true : (this.theEntity.getDistanceSqToEntity(var1) < 144.0D && var1.getAITarget() != null ? false : this.isSitting);
      }
   }

   public EntityAISit(EntityTameable var1) {
      this.theEntity = var1;
      this.setMutexBits(5);
   }

   public void startExecuting() {
      this.theEntity.getNavigator().clearPathEntity();
      this.theEntity.setSitting(true);
   }
}
