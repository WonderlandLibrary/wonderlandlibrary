package net.minecraft.entity;

import java.util.UUID;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;

public abstract class EntityCreature extends EntityLiving {
  public static final UUID FLEEING_SPEED_MODIFIER_UUID = UUID.fromString("E199AD21-BA8A-4C53-8D13-6182D5C69D3A");
  
  public static final AttributeModifier FLEEING_SPEED_MODIFIER = (new AttributeModifier(FLEEING_SPEED_MODIFIER_UUID, "Fleeing speed bonus", 2.0D, 2)).setSaved(false);
  
  private BlockPos homePosition = BlockPos.ORIGIN;
  
  private float maximumHomeDistance = -1.0F;
  
  private EntityAIBase aiBase = (EntityAIBase)new EntityAIMoveTowardsRestriction(this, 1.0D);
  
  private boolean isMovementAITaskSet;
  
  public EntityCreature(World worldIn) {
    super(worldIn);
  }
  
  public float getBlockPathWeight(BlockPos pos) {
    return 0.0F;
  }
  
  public boolean getCanSpawnHere() {
    return (super.getCanSpawnHere() && getBlockPathWeight(new BlockPos(this.posX, (getEntityBoundingBox()).minY, this.posZ)) >= 0.0F);
  }
  
  public boolean hasPath() {
    return !this.navigator.noPath();
  }
  
  public boolean isWithinHomeDistanceCurrentPosition() {
    return isWithinHomeDistanceFromPosition(new BlockPos(this));
  }
  
  public boolean isWithinHomeDistanceFromPosition(BlockPos pos) {
    return (this.maximumHomeDistance == -1.0F) ? true : ((this.homePosition.distanceSq((Vec3i)pos) < (this.maximumHomeDistance * this.maximumHomeDistance)));
  }
  
  public void setHomePosAndDistance(BlockPos pos, int distance) {
    this.homePosition = pos;
    this.maximumHomeDistance = distance;
  }
  
  public BlockPos getHomePosition() {
    return this.homePosition;
  }
  
  public float getMaximumHomeDistance() {
    return this.maximumHomeDistance;
  }
  
  public void detachHome() {
    this.maximumHomeDistance = -1.0F;
  }
  
  public boolean hasHome() {
    return (this.maximumHomeDistance != -1.0F);
  }
  
  protected void updateLeashedState() {
    super.updateLeashedState();
    if (getLeashed() && getLeashedToEntity() != null && (getLeashedToEntity()).worldObj == this.worldObj) {
      Entity entity = getLeashedToEntity();
      setHomePosAndDistance(new BlockPos((int)entity.posX, (int)entity.posY, (int)entity.posZ), 5);
      float f = getDistanceToEntity(entity);
      if (this instanceof EntityTameable && ((EntityTameable)this).isSitting()) {
        if (f > 10.0F)
          clearLeashed(true, true); 
        return;
      } 
      if (!this.isMovementAITaskSet) {
        this.tasks.addTask(2, this.aiBase);
        if (getNavigator() instanceof PathNavigateGround)
          ((PathNavigateGround)getNavigator()).setAvoidsWater(false); 
        this.isMovementAITaskSet = true;
      } 
      func_142017_o(f);
      if (f > 4.0F)
        getNavigator().tryMoveToEntityLiving(entity, 1.0D); 
      if (f > 6.0F) {
        double d0 = (entity.posX - this.posX) / f;
        double d1 = (entity.posY - this.posY) / f;
        double d2 = (entity.posZ - this.posZ) / f;
        this.motionX += d0 * Math.abs(d0) * 0.4D;
        this.motionY += d1 * Math.abs(d1) * 0.4D;
        this.motionZ += d2 * Math.abs(d2) * 0.4D;
      } 
      if (f > 10.0F)
        clearLeashed(true, true); 
    } else if (!getLeashed() && this.isMovementAITaskSet) {
      this.isMovementAITaskSet = false;
      this.tasks.removeTask(this.aiBase);
      if (getNavigator() instanceof PathNavigateGround)
        ((PathNavigateGround)getNavigator()).setAvoidsWater(true); 
      detachHome();
    } 
  }
  
  protected void func_142017_o(float p_142017_1_) {}
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\EntityCreature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */