package net.minecraft.entity.ai;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.pathfinding.PathNavigate;

public class EntityAITradePlayer
  extends EntityAIBase
{
  private EntityVillager villager;
  private static final String __OBFID = "CL_00001617";
  
  public EntityAITradePlayer(EntityVillager p_i1658_1_)
  {
    this.villager = p_i1658_1_;
    setMutexBits(5);
  }
  
  public boolean shouldExecute()
  {
    if (!this.villager.isEntityAlive()) {
      return false;
    }
    if (this.villager.isInWater()) {
      return false;
    }
    if (!this.villager.onGround) {
      return false;
    }
    if (this.villager.velocityChanged) {
      return false;
    }
    EntityPlayer var1 = this.villager.getCustomer();
    return this.villager.getDistanceSqToEntity(var1) > 16.0D ? false : var1 == null ? false : var1.openContainer instanceof Container;
  }
  
  public void startExecuting()
  {
    this.villager.getNavigator().clearPathEntity();
  }
  
  public void resetTask()
  {
    this.villager.setCustomer((EntityPlayer)null);
  }
}
