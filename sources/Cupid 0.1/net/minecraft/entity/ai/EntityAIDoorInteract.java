package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;

public abstract class EntityAIDoorInteract extends EntityAIBase {
  protected EntityLiving theEntity;
  
  protected BlockPos doorPosition = BlockPos.ORIGIN;
  
  protected BlockDoor doorBlock;
  
  boolean hasStoppedDoorInteraction;
  
  float entityPositionX;
  
  float entityPositionZ;
  
  public EntityAIDoorInteract(EntityLiving entityIn) {
    this.theEntity = entityIn;
    if (!(entityIn.getNavigator() instanceof PathNavigateGround))
      throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal"); 
  }
  
  public boolean shouldExecute() {
    if (!this.theEntity.isCollidedHorizontally)
      return false; 
    PathNavigateGround pathnavigateground = (PathNavigateGround)this.theEntity.getNavigator();
    PathEntity pathentity = pathnavigateground.getPath();
    if (pathentity != null && !pathentity.isFinished() && pathnavigateground.getEnterDoors()) {
      for (int i = 0; i < Math.min(pathentity.getCurrentPathIndex() + 2, pathentity.getCurrentPathLength()); i++) {
        PathPoint pathpoint = pathentity.getPathPointFromIndex(i);
        this.doorPosition = new BlockPos(pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord);
        if (this.theEntity.getDistanceSq(this.doorPosition.getX(), this.theEntity.posY, this.doorPosition.getZ()) <= 2.25D) {
          this.doorBlock = getBlockDoor(this.doorPosition);
          if (this.doorBlock != null)
            return true; 
        } 
      } 
      this.doorPosition = (new BlockPos((Entity)this.theEntity)).up();
      this.doorBlock = getBlockDoor(this.doorPosition);
      return (this.doorBlock != null);
    } 
    return false;
  }
  
  public boolean continueExecuting() {
    return !this.hasStoppedDoorInteraction;
  }
  
  public void startExecuting() {
    this.hasStoppedDoorInteraction = false;
    this.entityPositionX = (float)((this.doorPosition.getX() + 0.5F) - this.theEntity.posX);
    this.entityPositionZ = (float)((this.doorPosition.getZ() + 0.5F) - this.theEntity.posZ);
  }
  
  public void updateTask() {
    float f = (float)((this.doorPosition.getX() + 0.5F) - this.theEntity.posX);
    float f1 = (float)((this.doorPosition.getZ() + 0.5F) - this.theEntity.posZ);
    float f2 = this.entityPositionX * f + this.entityPositionZ * f1;
    if (f2 < 0.0F)
      this.hasStoppedDoorInteraction = true; 
  }
  
  private BlockDoor getBlockDoor(BlockPos pos) {
    Block block = this.theEntity.worldObj.getBlockState(pos).getBlock();
    return (block instanceof BlockDoor && block.getMaterial() == Material.wood) ? (BlockDoor)block : null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAIDoorInteract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */