package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Vec3;

public class EntityAIMoveTowardsTarget extends EntityAIBase {
  private EntityCreature theEntity;
  
  private EntityLivingBase targetEntity;
  
  private double movePosX;
  
  private double movePosY;
  
  private double movePosZ;
  
  private double speed;
  
  private float maxTargetDistance;
  
  public EntityAIMoveTowardsTarget(EntityCreature creature, double speedIn, float targetMaxDistance) {
    this.theEntity = creature;
    this.speed = speedIn;
    this.maxTargetDistance = targetMaxDistance;
    setMutexBits(1);
  }
  
  public boolean shouldExecute() {
    this.targetEntity = this.theEntity.getAttackTarget();
    if (this.targetEntity == null)
      return false; 
    if (this.targetEntity.getDistanceSqToEntity((Entity)this.theEntity) > (this.maxTargetDistance * this.maxTargetDistance))
      return false; 
    Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ));
    if (vec3 == null)
      return false; 
    this.movePosX = vec3.xCoord;
    this.movePosY = vec3.yCoord;
    this.movePosZ = vec3.zCoord;
    return true;
  }
  
  public boolean continueExecuting() {
    return (!this.theEntity.getNavigator().noPath() && this.targetEntity.isEntityAlive() && this.targetEntity.getDistanceSqToEntity((Entity)this.theEntity) < (this.maxTargetDistance * this.maxTargetDistance));
  }
  
  public void resetTask() {
    this.targetEntity = null;
  }
  
  public void startExecuting() {
    this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAIMoveTowardsTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */