package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget {
  private boolean entityCallsForHelp;
  
  private int revengeTimerOld;
  
  private final Class[] targetClasses;
  
  public EntityAIHurtByTarget(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class... targetClassesIn) {
    super(creatureIn, false);
    this.entityCallsForHelp = entityCallsForHelpIn;
    this.targetClasses = targetClassesIn;
    setMutexBits(1);
  }
  
  public boolean shouldExecute() {
    int i = this.taskOwner.getRevengeTimer();
    return (i != this.revengeTimerOld && isSuitableTarget(this.taskOwner.getAITarget(), false));
  }
  
  public void startExecuting() {
    this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
    this.revengeTimerOld = this.taskOwner.getRevengeTimer();
    if (this.entityCallsForHelp) {
      double d0 = getTargetDistance();
      for (EntityCreature entitycreature : this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(), (new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D)).expand(d0, 10.0D, d0))) {
        if (this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null && !entitycreature.isOnSameTeam(this.taskOwner.getAITarget())) {
          boolean flag = false;
          for (Class<?> oclass : this.targetClasses) {
            if (entitycreature.getClass() == oclass) {
              flag = true;
              break;
            } 
          } 
          if (!flag)
            setEntityAttackTarget(entitycreature, this.taskOwner.getAITarget()); 
        } 
      } 
    } 
    super.startExecuting();
  }
  
  protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
    creatureIn.setAttackTarget(entityLivingBaseIn);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAIHurtByTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */