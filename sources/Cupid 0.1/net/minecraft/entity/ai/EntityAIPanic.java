package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.Vec3;

public class EntityAIPanic extends EntityAIBase {
  private EntityCreature theEntityCreature;
  
  protected double speed;
  
  private double randPosX;
  
  private double randPosY;
  
  private double randPosZ;
  
  public EntityAIPanic(EntityCreature creature, double speedIn) {
    this.theEntityCreature = creature;
    this.speed = speedIn;
    setMutexBits(1);
  }
  
  public boolean shouldExecute() {
    if (this.theEntityCreature.getAITarget() == null && !this.theEntityCreature.isBurning())
      return false; 
    Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.theEntityCreature, 5, 4);
    if (vec3 == null)
      return false; 
    this.randPosX = vec3.xCoord;
    this.randPosY = vec3.yCoord;
    this.randPosZ = vec3.zCoord;
    return true;
  }
  
  public void startExecuting() {
    this.theEntityCreature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
  }
  
  public boolean continueExecuting() {
    return !this.theEntityCreature.getNavigator().noPath();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAIPanic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */