package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityAIOpenDoor extends EntityAIDoorInteract {
  boolean closeDoor;
  
  int closeDoorTemporisation;
  
  public EntityAIOpenDoor(EntityLiving entitylivingIn, boolean shouldClose) {
    super(entitylivingIn);
    this.theEntity = entitylivingIn;
    this.closeDoor = shouldClose;
  }
  
  public boolean continueExecuting() {
    return (this.closeDoor && this.closeDoorTemporisation > 0 && super.continueExecuting());
  }
  
  public void startExecuting() {
    this.closeDoorTemporisation = 20;
    this.doorBlock.toggleDoor(this.theEntity.worldObj, this.doorPosition, true);
  }
  
  public void resetTask() {
    if (this.closeDoor)
      this.doorBlock.toggleDoor(this.theEntity.worldObj, this.doorPosition, false); 
  }
  
  public void updateTask() {
    this.closeDoorTemporisation--;
    super.updateTask();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityAIOpenDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */