package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.village.Village;
import net.minecraft.village.VillageCollection;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;

public class EntityAIRestrictOpenDoor
  extends EntityAIBase
{
  private EntityCreature entityObj;
  private VillageDoorInfo frontDoor;
  private static final String __OBFID = "CL_00001610";
  
  public EntityAIRestrictOpenDoor(EntityCreature p_i1651_1_)
  {
    this.entityObj = p_i1651_1_;
    if (!(p_i1651_1_.getNavigator() instanceof PathNavigateGround)) {
      throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
    }
  }
  
  public boolean shouldExecute()
  {
    if (this.entityObj.worldObj.isDaytime()) {
      return false;
    }
    BlockPos var1 = new BlockPos(this.entityObj);
    Village var2 = this.entityObj.worldObj.getVillageCollection().func_176056_a(var1, 16);
    if (var2 == null) {
      return false;
    }
    this.frontDoor = var2.func_179865_b(var1);
    return this.frontDoor != null;
  }
  
  public boolean continueExecuting()
  {
    return !this.entityObj.worldObj.isDaytime();
  }
  
  public void startExecuting()
  {
    ((PathNavigateGround)this.entityObj.getNavigator()).func_179688_b(false);
    ((PathNavigateGround)this.entityObj.getNavigator()).func_179691_c(false);
  }
  
  public void resetTask()
  {
    ((PathNavigateGround)this.entityObj.getNavigator()).func_179688_b(true);
    ((PathNavigateGround)this.entityObj.getNavigator()).func_179691_c(true);
    this.frontDoor = null;
  }
  
  public void updateTask()
  {
    this.frontDoor.incrementDoorOpeningRestrictionCounter();
  }
}
