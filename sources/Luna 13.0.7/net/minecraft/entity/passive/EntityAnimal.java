package net.minecraft.entity.passive;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityAnimal
  extends EntityAgeable
  implements IAnimals
{
  protected Block field_175506_bl;
  private int inLove;
  private EntityPlayer playerInLove;
  private static final String __OBFID = "CL_00001638";
  
  public EntityAnimal(World worldIn)
  {
    super(worldIn);
    this.field_175506_bl = Blocks.grass;
  }
  
  protected void updateAITasks()
  {
    if (getGrowingAge() != 0) {
      this.inLove = 0;
    }
    super.updateAITasks();
  }
  
  public void onLivingUpdate()
  {
    super.onLivingUpdate();
    if (getGrowingAge() != 0) {
      this.inLove = 0;
    }
    if (this.inLove > 0)
    {
      this.inLove -= 1;
      if (this.inLove % 10 == 0)
      {
        double var1 = this.rand.nextGaussian() * 0.02D;
        double var3 = this.rand.nextGaussian() * 0.02D;
        double var5 = this.rand.nextGaussian() * 0.02D;
        this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, var1, var3, var5, new int[0]);
      }
    }
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount)
  {
    if (func_180431_b(source)) {
      return false;
    }
    this.inLove = 0;
    return super.attackEntityFrom(source, amount);
  }
  
  public float func_180484_a(BlockPos p_180484_1_)
  {
    return this.worldObj.getBlockState(p_180484_1_.offsetDown()).getBlock() == Blocks.grass ? 10.0F : this.worldObj.getLightBrightness(p_180484_1_) - 0.5F;
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound)
  {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setInteger("InLove", this.inLove);
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund)
  {
    super.readEntityFromNBT(tagCompund);
    this.inLove = tagCompund.getInteger("InLove");
  }
  
  public boolean getCanSpawnHere()
  {
    int var1 = MathHelper.floor_double(this.posX);
    int var2 = MathHelper.floor_double(getEntityBoundingBox().minY);
    int var3 = MathHelper.floor_double(this.posZ);
    BlockPos var4 = new BlockPos(var1, var2, var3);
    return (this.worldObj.getBlockState(var4.offsetDown()).getBlock() == this.field_175506_bl) && (this.worldObj.getLight(var4) > 8) && (super.getCanSpawnHere());
  }
  
  public int getTalkInterval()
  {
    return 120;
  }
  
  protected boolean canDespawn()
  {
    return false;
  }
  
  protected int getExperiencePoints(EntityPlayer p_70693_1_)
  {
    return 1 + this.worldObj.rand.nextInt(3);
  }
  
  public boolean isBreedingItem(ItemStack p_70877_1_)
  {
    return p_70877_1_ != null;
  }
  
  public boolean interact(EntityPlayer p_70085_1_)
  {
    ItemStack var2 = p_70085_1_.inventory.getCurrentItem();
    if (var2 != null)
    {
      if ((isBreedingItem(var2)) && (getGrowingAge() == 0) && (this.inLove <= 0))
      {
        func_175505_a(p_70085_1_, var2);
        setInLove(p_70085_1_);
        return true;
      }
      if ((isChild()) && (isBreedingItem(var2)))
      {
        func_175505_a(p_70085_1_, var2);
        func_175501_a((int)(-getGrowingAge() / 20 * 0.1F), true);
        return true;
      }
    }
    return super.interact(p_70085_1_);
  }
  
  protected void func_175505_a(EntityPlayer p_175505_1_, ItemStack p_175505_2_)
  {
    if (!p_175505_1_.capabilities.isCreativeMode)
    {
      p_175505_2_.stackSize -= 1;
      if (p_175505_2_.stackSize <= 0) {
        p_175505_1_.inventory.setInventorySlotContents(p_175505_1_.inventory.currentItem, (ItemStack)null);
      }
    }
  }
  
  public void setInLove(EntityPlayer p_146082_1_)
  {
    this.inLove = 600;
    this.playerInLove = p_146082_1_;
    this.worldObj.setEntityState(this, (byte)18);
  }
  
  public EntityPlayer func_146083_cb()
  {
    return this.playerInLove;
  }
  
  public boolean isInLove()
  {
    return this.inLove > 0;
  }
  
  public void resetInLove()
  {
    this.inLove = 0;
  }
  
  public boolean canMateWith(EntityAnimal p_70878_1_)
  {
    return p_70878_1_ != this;
  }
  
  public void handleHealthUpdate(byte p_70103_1_)
  {
    if (p_70103_1_ == 18) {
      for (int var2 = 0; var2 < 7; var2++)
      {
        double var3 = this.rand.nextGaussian() * 0.02D;
        double var5 = this.rand.nextGaussian() * 0.02D;
        double var7 = this.rand.nextGaussian() * 0.02D;
        this.worldObj.spawnParticle(EnumParticleTypes.HEART, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, var3, var5, var7, new int[0]);
      }
    } else {
      super.handleHealthUpdate(p_70103_1_);
    }
  }
}
