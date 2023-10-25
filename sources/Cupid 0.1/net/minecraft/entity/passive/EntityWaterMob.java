package net.minecraft.entity.passive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityWaterMob extends EntityLiving implements IAnimals {
  public EntityWaterMob(World worldIn) {
    super(worldIn);
  }
  
  public boolean canBreatheUnderwater() {
    return true;
  }
  
  public boolean getCanSpawnHere() {
    return true;
  }
  
  public boolean isNotColliding() {
    return this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), (Entity)this);
  }
  
  public int getTalkInterval() {
    return 120;
  }
  
  protected boolean canDespawn() {
    return true;
  }
  
  protected int getExperiencePoints(EntityPlayer player) {
    return 1 + this.worldObj.rand.nextInt(3);
  }
  
  public void onEntityUpdate() {
    int i = getAir();
    super.onEntityUpdate();
    if (isEntityAlive() && !isInWater()) {
      i--;
      setAir(i);
      if (getAir() == -20) {
        setAir(0);
        attackEntityFrom(DamageSource.drown, 2.0F);
      } 
    } else {
      setAir(300);
    } 
  }
  
  public boolean isPushedByWater() {
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\passive\EntityWaterMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */