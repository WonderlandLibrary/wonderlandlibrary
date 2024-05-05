package net.minecraft.entity;

import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;

public abstract class EntityHanging
  extends Entity
{
  private int tickCounter1;
  protected BlockPos field_174861_a;
  public EnumFacing field_174860_b;
  private static final String __OBFID = "CL_00001546";
  
  public EntityHanging(World worldIn)
  {
    super(worldIn);
    setSize(0.5F, 0.5F);
  }
  
  public EntityHanging(World worldIn, BlockPos p_i45853_2_)
  {
    this(worldIn);
    this.field_174861_a = p_i45853_2_;
  }
  
  protected void entityInit() {}
  
  protected void func_174859_a(EnumFacing p_174859_1_)
  {
    Validate.notNull(p_174859_1_);
    Validate.isTrue(p_174859_1_.getAxis().isHorizontal());
    this.field_174860_b = p_174859_1_;
    this.prevRotationYaw = (this.rotationYaw = this.field_174860_b.getHorizontalIndex() * 90);
    func_174856_o();
  }
  
  private void func_174856_o()
  {
    if (this.field_174860_b != null)
    {
      double var1 = this.field_174861_a.getX() + 0.5D;
      double var3 = this.field_174861_a.getY() + 0.5D;
      double var5 = this.field_174861_a.getZ() + 0.5D;
      double var7 = 0.46875D;
      double var9 = func_174858_a(getWidthPixels());
      double var11 = func_174858_a(getHeightPixels());
      var1 -= this.field_174860_b.getFrontOffsetX() * 0.46875D;
      var5 -= this.field_174860_b.getFrontOffsetZ() * 0.46875D;
      var3 += var11;
      EnumFacing var13 = this.field_174860_b.rotateYCCW();
      var1 += var9 * var13.getFrontOffsetX();
      var5 += var9 * var13.getFrontOffsetZ();
      this.posX = var1;
      this.posY = var3;
      this.posZ = var5;
      double var14 = getWidthPixels();
      double var16 = getHeightPixels();
      double var18 = getWidthPixels();
      if (this.field_174860_b.getAxis() == EnumFacing.Axis.Z) {
        var18 = 1.0D;
      } else {
        var14 = 1.0D;
      }
      var14 /= 32.0D;
      var16 /= 32.0D;
      var18 /= 32.0D;
      func_174826_a(new AxisAlignedBB(var1 - var14, var3 - var16, var5 - var18, var1 + var14, var3 + var16, var5 + var18));
    }
  }
  
  private double func_174858_a(int p_174858_1_)
  {
    return p_174858_1_ % 32 == 0 ? 0.5D : 0.0D;
  }
  
  public void onUpdate()
  {
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    if ((this.tickCounter1++ == 100) && (!this.worldObj.isRemote))
    {
      this.tickCounter1 = 0;
      if ((!this.isDead) && (!onValidSurface()))
      {
        setDead();
        onBroken((Entity)null);
      }
    }
  }
  
  public boolean onValidSurface()
  {
    if (!this.worldObj.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty()) {
      return false;
    }
    int var1 = Math.max(1, getWidthPixels() / 16);
    int var2 = Math.max(1, getHeightPixels() / 16);
    BlockPos var3 = this.field_174861_a.offset(this.field_174860_b.getOpposite());
    EnumFacing var4 = this.field_174860_b.rotateYCCW();
    for (int var5 = 0; var5 < var1; var5++) {
      for (int var6 = 0; var6 < var2; var6++)
      {
        BlockPos var7 = var3.offset(var4, var5).offsetUp(var6);
        Block var8 = this.worldObj.getBlockState(var7).getBlock();
        if ((!var8.getMaterial().isSolid()) && (!BlockRedstoneDiode.isRedstoneRepeaterBlockID(var8))) {
          return false;
        }
      }
    }
    List var9 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox());
    Iterator var10 = var9.iterator();
    Entity var11;
    do
    {
      if (!var10.hasNext()) {
        return true;
      }
      var11 = (Entity)var10.next();
    } while (!(var11 instanceof EntityHanging));
    return false;
  }
  
  public boolean canBeCollidedWith()
  {
    return true;
  }
  
  public boolean hitByEntity(Entity entityIn)
  {
    return (entityIn instanceof EntityPlayer) ? attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer)entityIn), 0.0F) : false;
  }
  
  public EnumFacing func_174811_aO()
  {
    return this.field_174860_b;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount)
  {
    if (func_180431_b(source)) {
      return false;
    }
    if ((!this.isDead) && (!this.worldObj.isRemote))
    {
      setDead();
      setBeenAttacked();
      onBroken(source.getEntity());
    }
    return true;
  }
  
  public void moveEntity(double x, double y, double z)
  {
    if ((!this.worldObj.isRemote) && (!this.isDead) && (x * x + y * y + z * z > 0.0D))
    {
      setDead();
      onBroken((Entity)null);
    }
  }
  
  public void addVelocity(double x, double y, double z)
  {
    if ((!this.worldObj.isRemote) && (!this.isDead) && (x * x + y * y + z * z > 0.0D))
    {
      setDead();
      onBroken((Entity)null);
    }
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound)
  {
    tagCompound.setByte("Facing", (byte)this.field_174860_b.getHorizontalIndex());
    tagCompound.setInteger("TileX", func_174857_n().getX());
    tagCompound.setInteger("TileY", func_174857_n().getY());
    tagCompound.setInteger("TileZ", func_174857_n().getZ());
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund)
  {
    this.field_174861_a = new BlockPos(tagCompund.getInteger("TileX"), tagCompund.getInteger("TileY"), tagCompund.getInteger("TileZ"));
    EnumFacing var2;
    if (tagCompund.hasKey("Direction", 99))
    {
      EnumFacing var2 = EnumFacing.getHorizontal(tagCompund.getByte("Direction"));
      this.field_174861_a = this.field_174861_a.offset(var2);
    }
    else
    {
      EnumFacing var2;
      if (tagCompund.hasKey("Facing", 99)) {
        var2 = EnumFacing.getHorizontal(tagCompund.getByte("Facing"));
      } else {
        var2 = EnumFacing.getHorizontal(tagCompund.getByte("Dir"));
      }
    }
    func_174859_a(var2);
  }
  
  public abstract int getWidthPixels();
  
  public abstract int getHeightPixels();
  
  public abstract void onBroken(Entity paramEntity);
  
  protected boolean shouldSetPosAfterLoading()
  {
    return false;
  }
  
  public void setPosition(double x, double y, double z)
  {
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    BlockPos var7 = this.field_174861_a;
    this.field_174861_a = new BlockPos(x, y, z);
    if (!this.field_174861_a.equals(var7))
    {
      func_174856_o();
      this.isAirBorne = true;
    }
  }
  
  public BlockPos func_174857_n()
  {
    return this.field_174861_a;
  }
}
