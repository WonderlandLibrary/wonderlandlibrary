package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.Vec3;

public class EntityAIPlay extends EntityAIBase {
  private EntityVillager villagerObj;
  
  private EntityLivingBase targetVillager;
  
  private double speed;
  
  private int playTime;
  
  public EntityAIPlay(EntityVillager villagerObjIn, double speedIn) {
    this.villagerObj = villagerObjIn;
    this.speed = speedIn;
    setMutexBits(1);
  }
  
  public boolean shouldExecute() {
    if (this.villagerObj.getGrowingAge() >= 0)
      return false; 
    if (this.villagerObj.getRNG().nextInt(400) != 0)
      return false; 
    List<EntityVillager> list = this.villagerObj.worldObj.getEntitiesWithinAABB(EntityVillager.class, this.villagerObj.getEntityBoundingBox().expand(6.0D, 3.0D, 6.0D));
    double d0 = Double.MAX_VALUE;
    for (EntityVillager entityvillager : list) {
      if (entityvillager != this.villagerObj && !entityvillager.isPlaying() && entityvillager.getGrowingAge() < 0) {
        double d1 = entityvillager.getDistanceSqToEntity((Entity)this.villagerObj);
        if (d1 <= d0) {
          d0 = d1;
          this.targetVillager = (EntityLivingBase)entityvillager;
        } 
      } 
    } 
    if (this.targetVillager == null) {
      Vec3 vec3 = RandomPositionGenerator.findRandomTarget((EntityCreature)this.villagerObj, 16, 3);
      if (vec3 == null)
        return false; 
    } 
    return true;
  }
  
  public boolean continueExecuting() {
    return (this.playTime > 0);
  }
  
  public void startExecuting() {
    if (this.targetVillager != null)
      this.villagerObj.setPlaying(true); 
    this.playTime = 1000;
  }
  
  public void resetTask() {
    this.villagerObj.setPlaying(false);
    this.targetVillager = null;
  }
  
  public void updateTask() {
    this.playTime--;
    if (this.targetVillager != null) {
      if (this.villagerObj.getDistanceSqToEntity((Entity)this.targetVillager) > 4.0D)
        this.villagerObj.getNavigator().tryMoveToEntityLiving((Entity)this.targetVillager, this.speed); 
    } else if (this.villagerObj.getNavigator().noPath()) {
      Vec3 vec3 = RandomPositionGenerator.findRandomTarget((EntityCreature)this.villagerObj, 16, 3);
      if (vec3 == null)
        return; 
      this.villagerObj.getNavigator().tryMoveToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord, this.speed);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAIPlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */