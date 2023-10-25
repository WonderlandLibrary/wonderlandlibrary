package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IBlockAccess;

public class EntityAIBreakDoor extends EntityAIDoorInteract {
  private int breakingTime;
  
  private int previousBreakProgress = -1;
  
  public EntityAIBreakDoor(EntityLiving entityIn) {
    super(entityIn);
  }
  
  public boolean shouldExecute() {
    if (!super.shouldExecute())
      return false; 
    if (!this.theEntity.worldObj.getGameRules().getBoolean("mobGriefing"))
      return false; 
    BlockDoor blockdoor = this.doorBlock;
    return !BlockDoor.isOpen((IBlockAccess)this.theEntity.worldObj, this.doorPosition);
  }
  
  public void startExecuting() {
    super.startExecuting();
    this.breakingTime = 0;
  }
  
  public boolean continueExecuting() {
    double d0 = this.theEntity.getDistanceSq(this.doorPosition);
    if (this.breakingTime <= 240) {
      BlockDoor blockdoor = this.doorBlock;
      if (!BlockDoor.isOpen((IBlockAccess)this.theEntity.worldObj, this.doorPosition) && d0 < 4.0D) {
        boolean bool = true;
        return bool;
      } 
    } 
    boolean flag = false;
    return flag;
  }
  
  public void resetTask() {
    super.resetTask();
    this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, -1);
  }
  
  public void updateTask() {
    super.updateTask();
    if (this.theEntity.getRNG().nextInt(20) == 0)
      this.theEntity.worldObj.playAuxSFX(1010, this.doorPosition, 0); 
    this.breakingTime++;
    int i = (int)(this.breakingTime / 240.0F * 10.0F);
    if (i != this.previousBreakProgress) {
      this.theEntity.worldObj.sendBlockBreakProgress(this.theEntity.getEntityId(), this.doorPosition, i);
      this.previousBreakProgress = i;
    } 
    if (this.breakingTime == 240 && this.theEntity.worldObj.getDifficulty() == EnumDifficulty.HARD) {
      this.theEntity.worldObj.setBlockToAir(this.doorPosition);
      this.theEntity.worldObj.playAuxSFX(1012, this.doorPosition, 0);
      this.theEntity.worldObj.playAuxSFX(2001, this.doorPosition, Block.getIdFromBlock((Block)this.doorBlock));
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAIBreakDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */