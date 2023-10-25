package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAIRestrictSun extends EntityAIBase {
  private EntityCreature theEntity;
  
  public EntityAIRestrictSun(EntityCreature p_i1652_1_) {
    this.theEntity = p_i1652_1_;
  }
  
  public boolean shouldExecute() {
    return this.theEntity.worldObj.isDaytime();
  }
  
  public void startExecuting() {
    ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(true);
  }
  
  public void resetTask() {
    ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(false);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAIRestrictSun.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */