package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAISit extends EntityAIBase {
  private EntityTameable theEntity;
  
  private boolean isSitting;
  
  public EntityAISit(EntityTameable entityIn) {
    this.theEntity = entityIn;
    setMutexBits(5);
  }
  
  public boolean shouldExecute() {
    if (!this.theEntity.isTamed())
      return false; 
    if (this.theEntity.isInWater())
      return false; 
    if (!this.theEntity.onGround)
      return false; 
    EntityLivingBase entitylivingbase = this.theEntity.getOwner();
    return (entitylivingbase == null) ? true : ((this.theEntity.getDistanceSqToEntity((Entity)entitylivingbase) < 144.0D && entitylivingbase.getAITarget() != null) ? false : this.isSitting);
  }
  
  public void startExecuting() {
    this.theEntity.getNavigator().clearPathEntity();
    this.theEntity.setSitting(true);
  }
  
  public void resetTask() {
    this.theEntity.setSitting(false);
  }
  
  public void setSitting(boolean sitting) {
    this.isSitting = sitting;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAISit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */