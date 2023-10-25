package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.pathfinder.NodeProcessor;
import net.minecraft.world.pathfinder.SwimNodeProcessor;

public class PathNavigateSwimmer extends PathNavigate {
  public PathNavigateSwimmer(EntityLiving entitylivingIn, World worldIn) {
    super(entitylivingIn, worldIn);
  }
  
  protected PathFinder getPathFinder() {
    return new PathFinder((NodeProcessor)new SwimNodeProcessor());
  }
  
  protected boolean canNavigate() {
    return isInLiquid();
  }
  
  protected Vec3 getEntityPosition() {
    return new Vec3(this.theEntity.posX, this.theEntity.posY + this.theEntity.height * 0.5D, this.theEntity.posZ);
  }
  
  protected void pathFollow() {
    Vec3 vec3 = getEntityPosition();
    float f = this.theEntity.width * this.theEntity.width;
    int i = 6;
    if (vec3.squareDistanceTo(this.currentPath.getVectorFromIndex((Entity)this.theEntity, this.currentPath.getCurrentPathIndex())) < f)
      this.currentPath.incrementPathIndex(); 
    for (int j = Math.min(this.currentPath.getCurrentPathIndex() + i, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); j--) {
      Vec3 vec31 = this.currentPath.getVectorFromIndex((Entity)this.theEntity, j);
      if (vec31.squareDistanceTo(vec3) <= 36.0D && isDirectPathBetweenPoints(vec3, vec31, 0, 0, 0)) {
        this.currentPath.setCurrentPathIndex(j);
        break;
      } 
    } 
    checkForStuck(vec3);
  }
  
  protected void removeSunnyPath() {
    super.removeSunnyPath();
  }
  
  protected boolean isDirectPathBetweenPoints(Vec3 posVec31, Vec3 posVec32, int sizeX, int sizeY, int sizeZ) {
    MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(posVec31, new Vec3(posVec32.xCoord, posVec32.yCoord + this.theEntity.height * 0.5D, posVec32.zCoord), false, true, false);
    return (movingobjectposition == null || movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.MISS);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\pathfinding\PathNavigateSwimmer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */