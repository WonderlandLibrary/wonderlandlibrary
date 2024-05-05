package net.minecraft.entity.passive;

import java.util.Random;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityWaterMob
  extends EntityLiving
  implements IAnimals
{
  private static final String __OBFID = "CL_00001653";
  
  public EntityWaterMob(World worldIn)
  {
    super(worldIn);
  }
  
  public boolean canBreatheUnderwater()
  {
    return true;
  }
  
  public boolean getCanSpawnHere()
  {
    return true;
  }
  
  public boolean handleLavaMovement()
  {
    return this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), this);
  }
  
  public int getTalkInterval()
  {
    return 120;
  }
  
  protected boolean canDespawn()
  {
    return true;
  }
  
  protected int getExperiencePoints(EntityPlayer p_70693_1_)
  {
    return 1 + this.worldObj.rand.nextInt(3);
  }
  
  public void onEntityUpdate()
  {
    int var1 = getAir();
    super.onEntityUpdate();
    if ((isEntityAlive()) && (!isInWater()))
    {
      var1--;
      setAir(var1);
      if (getAir() == -20)
      {
        setAir(0);
        attackEntityFrom(DamageSource.drown, 2.0F);
      }
    }
    else
    {
      setAir(300);
    }
  }
  
  public boolean isPushedByWater()
  {
    return false;
  }
}
