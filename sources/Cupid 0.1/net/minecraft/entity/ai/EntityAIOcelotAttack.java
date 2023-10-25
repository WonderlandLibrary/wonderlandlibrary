package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class EntityAIOcelotAttack extends EntityAIBase {
  World theWorld;
  
  EntityLiving theEntity;
  
  EntityLivingBase theVictim;
  
  int attackCountdown;
  
  public EntityAIOcelotAttack(EntityLiving theEntityIn) {
    this.theEntity = theEntityIn;
    this.theWorld = theEntityIn.worldObj;
    setMutexBits(3);
  }
  
  public boolean shouldExecute() {
    EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
    if (entitylivingbase == null)
      return false; 
    this.theVictim = entitylivingbase;
    return true;
  }
  
  public boolean continueExecuting() {
    return !this.theVictim.isEntityAlive() ? false : ((this.theEntity.getDistanceSqToEntity((Entity)this.theVictim) > 225.0D) ? false : ((!this.theEntity.getNavigator().noPath() || shouldExecute())));
  }
  
  public void resetTask() {
    this.theVictim = null;
    this.theEntity.getNavigator().clearPathEntity();
  }
  
  public void updateTask() {
    this.theEntity.getLookHelper().setLookPositionWithEntity((Entity)this.theVictim, 30.0F, 30.0F);
    double d0 = (this.theEntity.width * 2.0F * this.theEntity.width * 2.0F);
    double d1 = this.theEntity.getDistanceSq(this.theVictim.posX, (this.theVictim.getEntityBoundingBox()).minY, this.theVictim.posZ);
    double d2 = 0.8D;
    if (d1 > d0 && d1 < 16.0D) {
      d2 = 1.33D;
    } else if (d1 < 225.0D) {
      d2 = 0.6D;
    } 
    this.theEntity.getNavigator().tryMoveToEntityLiving((Entity)this.theVictim, d2);
    this.attackCountdown = Math.max(this.attackCountdown - 1, 0);
    if (d1 <= d0)
      if (this.attackCountdown <= 0) {
        this.attackCountdown = 20;
        this.theEntity.attackEntityAsMob((Entity)this.theVictim);
      }  
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAIOcelotAttack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */