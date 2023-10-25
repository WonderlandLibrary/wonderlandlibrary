package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;

public class EntityJumpHelper {
  private EntityLiving entity;
  
  protected boolean isJumping;
  
  public EntityJumpHelper(EntityLiving entityIn) {
    this.entity = entityIn;
  }
  
  public void setJumping() {
    this.isJumping = true;
  }
  
  public void doJump() {
    this.entity.setJumping(this.isJumping);
    this.isJumping = false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\ai\EntityJumpHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */