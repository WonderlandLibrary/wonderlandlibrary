package net.minecraft.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityFlying
  extends EntityLiving
{
  private static final String __OBFID = "CL_00001545";
  
  public EntityFlying(World worldIn)
  {
    super(worldIn);
  }
  
  public void fall(float distance, float damageMultiplier) {}
  
  protected void func_180433_a(double p_180433_1_, boolean p_180433_3_, Block p_180433_4_, BlockPos p_180433_5_) {}
  
  public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
  {
    if (isInWater())
    {
      moveFlying(p_70612_1_, p_70612_2_, 0.02F);
      moveEntity(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.800000011920929D;
      this.motionY *= 0.800000011920929D;
      this.motionZ *= 0.800000011920929D;
    }
    else if (func_180799_ab())
    {
      moveFlying(p_70612_1_, p_70612_2_, 0.02F);
      moveEntity(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.5D;
      this.motionY *= 0.5D;
      this.motionZ *= 0.5D;
    }
    else
    {
      float var3 = 0.91F;
      if (this.onGround) {
        var3 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
      }
      float var4 = 0.16277136F / (var3 * var3 * var3);
      moveFlying(p_70612_1_, p_70612_2_, this.onGround ? 0.1F * var4 : 0.02F);
      var3 = 0.91F;
      if (this.onGround) {
        var3 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91F;
      }
      moveEntity(this.motionX, this.motionY, this.motionZ);
      this.motionX *= var3;
      this.motionY *= var3;
      this.motionZ *= var3;
    }
    this.prevLimbSwingAmount = this.limbSwingAmount;
    double var8 = this.posX - this.prevPosX;
    double var5 = this.posZ - this.prevPosZ;
    float var7 = MathHelper.sqrt_double(var8 * var8 + var5 * var5) * 4.0F;
    if (var7 > 1.0F) {
      var7 = 1.0F;
    }
    this.limbSwingAmount += (var7 - this.limbSwingAmount) * 0.4F;
    this.limbSwing += this.limbSwingAmount;
  }
  
  public boolean isOnLadder()
  {
    return false;
  }
}
