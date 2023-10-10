package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAILookIdle extends EntityAIBase {
   private double lookX;
   private int idleTime;
   private double lookZ;
   private EntityLiving idleEntity;

   public void startExecuting() {
      double var1 = 6.283185307179586D * this.idleEntity.getRNG().nextDouble();
      this.lookX = Math.cos(var1);
      this.lookZ = Math.sin(var1);
      this.idleTime = 20 + this.idleEntity.getRNG().nextInt(20);
   }

   public boolean continueExecuting() {
      return this.idleTime >= 0;
   }

   public EntityAILookIdle(EntityLiving var1) {
      this.idleEntity = var1;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      return this.idleEntity.getRNG().nextFloat() < 0.02F;
   }

   public void updateTask() {
      --this.idleTime;
      this.idleEntity.getLookHelper().setLookPosition(this.idleEntity.posX + this.lookX, this.idleEntity.posY + (double)this.idleEntity.getEyeHeight(), this.idleEntity.posZ + this.lookZ, 10.0F, (float)this.idleEntity.getVerticalFaceSpeed());
   }
}
