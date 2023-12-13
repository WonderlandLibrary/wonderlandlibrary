package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAIRestrictSun extends EntityAIBase {
   private EntityCreature theEntity;

   public boolean shouldExecute() {
      return this.theEntity.worldObj.isDaytime();
   }

   public void resetTask() {
      ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(false);
   }

   public EntityAIRestrictSun(EntityCreature var1) {
      this.theEntity = var1;
   }

   public void startExecuting() {
      ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(true);
   }
}
