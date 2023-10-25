package net.minecraft.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.world.World;

public class EntityAIVillagerMate extends EntityAIBase {
  private EntityVillager villagerObj;
  
  private EntityVillager mate;
  
  private World worldObj;
  
  private int matingTimeout;
  
  Village villageObj;
  
  public EntityAIVillagerMate(EntityVillager villagerIn) {
    this.villagerObj = villagerIn;
    this.worldObj = villagerIn.worldObj;
    setMutexBits(3);
  }
  
  public boolean shouldExecute() {
    if (this.villagerObj.getGrowingAge() != 0)
      return false; 
    if (this.villagerObj.getRNG().nextInt(500) != 0)
      return false; 
    this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(new BlockPos((Entity)this.villagerObj), 0);
    if (this.villageObj == null)
      return false; 
    if (checkSufficientDoorsPresentForNewVillager() && this.villagerObj.getIsWillingToMate(true)) {
      Entity entity = this.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.villagerObj.getEntityBoundingBox().expand(8.0D, 3.0D, 8.0D), (Entity)this.villagerObj);
      if (entity == null)
        return false; 
      this.mate = (EntityVillager)entity;
      return (this.mate.getGrowingAge() == 0 && this.mate.getIsWillingToMate(true));
    } 
    return false;
  }
  
  public void startExecuting() {
    this.matingTimeout = 300;
    this.villagerObj.setMating(true);
  }
  
  public void resetTask() {
    this.villageObj = null;
    this.mate = null;
    this.villagerObj.setMating(false);
  }
  
  public boolean continueExecuting() {
    return (this.matingTimeout >= 0 && checkSufficientDoorsPresentForNewVillager() && this.villagerObj.getGrowingAge() == 0 && this.villagerObj.getIsWillingToMate(false));
  }
  
  public void updateTask() {
    this.matingTimeout--;
    this.villagerObj.getLookHelper().setLookPositionWithEntity((Entity)this.mate, 10.0F, 30.0F);
    if (this.villagerObj.getDistanceSqToEntity((Entity)this.mate) > 2.25D) {
      this.villagerObj.getNavigator().tryMoveToEntityLiving((Entity)this.mate, 0.25D);
    } else if (this.matingTimeout == 0 && this.mate.isMating()) {
      giveBirth();
    } 
    if (this.villagerObj.getRNG().nextInt(35) == 0)
      this.worldObj.setEntityState((Entity)this.villagerObj, (byte)12); 
  }
  
  private boolean checkSufficientDoorsPresentForNewVillager() {
    if (!this.villageObj.isMatingSeason())
      return false; 
    int i = (int)(this.villageObj.getNumVillageDoors() * 0.35D);
    return (this.villageObj.getNumVillagers() < i);
  }
  
  private void giveBirth() {
    EntityVillager entityvillager = this.villagerObj.createChild((EntityAgeable)this.mate);
    this.mate.setGrowingAge(6000);
    this.villagerObj.setGrowingAge(6000);
    this.mate.setIsWillingToMate(false);
    this.villagerObj.setIsWillingToMate(false);
    entityvillager.setGrowingAge(-24000);
    entityvillager.setLocationAndAngles(this.villagerObj.posX, this.villagerObj.posY, this.villagerObj.posZ, 0.0F, 0.0F);
    this.worldObj.spawnEntityInWorld((Entity)entityvillager);
    this.worldObj.setEntityState((Entity)entityvillager, (byte)12);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAIVillagerMate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */