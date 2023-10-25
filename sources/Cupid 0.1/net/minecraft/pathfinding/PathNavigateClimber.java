package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PathNavigateClimber extends PathNavigateGround {
  private BlockPos targetPosition;
  
  public PathNavigateClimber(EntityLiving entityLivingIn, World worldIn) {
    super(entityLivingIn, worldIn);
  }
  
  public PathEntity getPathToPos(BlockPos pos) {
    this.targetPosition = pos;
    return super.getPathToPos(pos);
  }
  
  public PathEntity getPathToEntityLiving(Entity entityIn) {
    this.targetPosition = new BlockPos(entityIn);
    return super.getPathToEntityLiving(entityIn);
  }
  
  public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
    PathEntity pathentity = getPathToEntityLiving(entityIn);
    if (pathentity != null)
      return setPath(pathentity, speedIn); 
    this.targetPosition = new BlockPos(entityIn);
    this.speed = speedIn;
    return true;
  }
  
  public void onUpdateNavigation() {
    if (!noPath()) {
      super.onUpdateNavigation();
    } else if (this.targetPosition != null) {
      double d0 = (this.theEntity.width * this.theEntity.width);
      if (this.theEntity.getDistanceSqToCenter(this.targetPosition) >= d0 && (this.theEntity.posY <= this.targetPosition.getY() || this.theEntity.getDistanceSqToCenter(new BlockPos(this.targetPosition.getX(), MathHelper.floor_double(this.theEntity.posY), this.targetPosition.getZ())) >= d0)) {
        this.theEntity.getMoveHelper().setMoveTo(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ(), this.speed);
      } else {
        this.targetPosition = null;
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\pathfinding\PathNavigateClimber.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */