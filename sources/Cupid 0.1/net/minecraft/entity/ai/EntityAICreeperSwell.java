package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;

public class EntityAICreeperSwell extends EntityAIBase {
  EntityCreeper swellingCreeper;
  
  EntityLivingBase creeperAttackTarget;
  
  public EntityAICreeperSwell(EntityCreeper entitycreeperIn) {
    this.swellingCreeper = entitycreeperIn;
    setMutexBits(1);
  }
  
  public boolean shouldExecute() {
    EntityLivingBase entitylivingbase = this.swellingCreeper.getAttackTarget();
    return (this.swellingCreeper.getCreeperState() > 0 || (entitylivingbase != null && this.swellingCreeper.getDistanceSqToEntity((Entity)entitylivingbase) < 9.0D));
  }
  
  public void startExecuting() {
    this.swellingCreeper.getNavigator().clearPathEntity();
    this.creeperAttackTarget = this.swellingCreeper.getAttackTarget();
  }
  
  public void resetTask() {
    this.creeperAttackTarget = null;
  }
  
  public void updateTask() {
    if (this.creeperAttackTarget == null) {
      this.swellingCreeper.setCreeperState(-1);
    } else if (this.swellingCreeper.getDistanceSqToEntity((Entity)this.creeperAttackTarget) > 49.0D) {
      this.swellingCreeper.setCreeperState(-1);
    } else if (!this.swellingCreeper.getEntitySenses().canSee((Entity)this.creeperAttackTarget)) {
      this.swellingCreeper.setCreeperState(-1);
    } else {
      this.swellingCreeper.setCreeperState(1);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAICreeperSwell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */