package net.minecraft.entity.projectile;

import net.minecraft.block.BlockFire;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EntitySmallFireball
  extends EntityFireball
{
  private static final String __OBFID = "CL_00001721";
  
  public EntitySmallFireball(World worldIn)
  {
    super(worldIn);
    setSize(0.3125F, 0.3125F);
  }
  
  public EntitySmallFireball(World worldIn, EntityLivingBase p_i1771_2_, double p_i1771_3_, double p_i1771_5_, double p_i1771_7_)
  {
    super(worldIn, p_i1771_2_, p_i1771_3_, p_i1771_5_, p_i1771_7_);
    setSize(0.3125F, 0.3125F);
  }
  
  public EntitySmallFireball(World worldIn, double p_i1772_2_, double p_i1772_4_, double p_i1772_6_, double p_i1772_8_, double p_i1772_10_, double p_i1772_12_)
  {
    super(worldIn, p_i1772_2_, p_i1772_4_, p_i1772_6_, p_i1772_8_, p_i1772_10_, p_i1772_12_);
    setSize(0.3125F, 0.3125F);
  }
  
  protected void onImpact(MovingObjectPosition p_70227_1_)
  {
    if (!this.worldObj.isRemote)
    {
      if (p_70227_1_.entityHit != null)
      {
        boolean var2 = p_70227_1_.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0F);
        if (var2)
        {
          func_174815_a(this.shootingEntity, p_70227_1_.entityHit);
          if (!p_70227_1_.entityHit.isImmuneToFire()) {
            p_70227_1_.entityHit.setFire(5);
          }
        }
      }
      else
      {
        boolean var2 = true;
        if ((this.shootingEntity != null) && ((this.shootingEntity instanceof EntityLiving))) {
          var2 = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
        }
        if (var2)
        {
          BlockPos var3 = p_70227_1_.func_178782_a().offset(p_70227_1_.field_178784_b);
          if (this.worldObj.isAirBlock(var3)) {
            this.worldObj.setBlockState(var3, Blocks.fire.getDefaultState());
          }
        }
      }
      setDead();
    }
  }
  
  public boolean canBeCollidedWith()
  {
    return false;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount)
  {
    return false;
  }
}
