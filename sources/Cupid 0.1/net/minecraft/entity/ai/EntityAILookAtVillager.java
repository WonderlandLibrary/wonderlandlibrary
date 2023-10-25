package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAILookAtVillager extends EntityAIBase {
  private EntityIronGolem theGolem;
  
  private EntityVillager theVillager;
  
  private int lookTime;
  
  public EntityAILookAtVillager(EntityIronGolem theGolemIn) {
    this.theGolem = theGolemIn;
    setMutexBits(3);
  }
  
  public boolean shouldExecute() {
    if (!this.theGolem.worldObj.isDaytime())
      return false; 
    if (this.theGolem.getRNG().nextInt(8000) != 0)
      return false; 
    this.theVillager = (EntityVillager)this.theGolem.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.theGolem.getEntityBoundingBox().expand(6.0D, 2.0D, 6.0D), (Entity)this.theGolem);
    return (this.theVillager != null);
  }
  
  public boolean continueExecuting() {
    return (this.lookTime > 0);
  }
  
  public void startExecuting() {
    this.lookTime = 400;
    this.theGolem.setHoldingRose(true);
  }
  
  public void resetTask() {
    this.theGolem.setHoldingRose(false);
    this.theVillager = null;
  }
  
  public void updateTask() {
    this.theGolem.getLookHelper().setLookPositionWithEntity((Entity)this.theVillager, 30.0F, 30.0F);
    this.lookTime--;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAILookAtVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */