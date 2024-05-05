package net.minecraft.entity.item;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBoat
  extends Entity
{
  private boolean isBoatEmpty;
  private double speedMultiplier;
  private int boatPosRotationIncrements;
  private double boatX;
  private double boatY;
  private double boatZ;
  private double boatYaw;
  private double boatPitch;
  private double velocityX;
  private double velocityY;
  private double velocityZ;
  private static final String __OBFID = "CL_00001667";
  
  public EntityBoat(World worldIn)
  {
    super(worldIn);
    this.isBoatEmpty = true;
    this.speedMultiplier = 0.07D;
    this.preventEntitySpawning = true;
    setSize(1.5F, 0.6F);
  }
  
  protected boolean canTriggerWalking()
  {
    return false;
  }
  
  protected void entityInit()
  {
    this.dataWatcher.addObject(17, new Integer(0));
    this.dataWatcher.addObject(18, new Integer(1));
    this.dataWatcher.addObject(19, new Float(0.0F));
  }
  
  public AxisAlignedBB getCollisionBox(Entity entityIn)
  {
    return entityIn.getEntityBoundingBox();
  }
  
  public AxisAlignedBB getBoundingBox()
  {
    return getEntityBoundingBox();
  }
  
  public boolean canBePushed()
  {
    return true;
  }
  
  public EntityBoat(World worldIn, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_)
  {
    this(worldIn);
    setPosition(p_i1705_2_, p_i1705_4_, p_i1705_6_);
    this.motionX = 0.0D;
    this.motionY = 0.0D;
    this.motionZ = 0.0D;
    this.prevPosX = p_i1705_2_;
    this.prevPosY = p_i1705_4_;
    this.prevPosZ = p_i1705_6_;
  }
  
  public double getMountedYOffset()
  {
    return this.height * 0.0D - 0.30000001192092896D;
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount)
  {
    if (func_180431_b(source)) {
      return false;
    }
    if ((!this.worldObj.isRemote) && (!this.isDead))
    {
      if ((this.riddenByEntity != null) && (this.riddenByEntity == source.getEntity()) && ((source instanceof EntityDamageSourceIndirect))) {
        return false;
      }
      setForwardDirection(-getForwardDirection());
      setTimeSinceHit(10);
      setDamageTaken(getDamageTaken() + amount * 10.0F);
      setBeenAttacked();
      boolean var3 = ((source.getEntity() instanceof EntityPlayer)) && (((EntityPlayer)source.getEntity()).capabilities.isCreativeMode);
      if ((var3) || (getDamageTaken() > 40.0F))
      {
        if (this.riddenByEntity != null) {
          this.riddenByEntity.mountEntity(this);
        }
        if (!var3) {
          dropItemWithOffset(Items.boat, 1, 0.0F);
        }
        setDead();
      }
      return true;
    }
    return true;
  }
  
  public void performHurtAnimation()
  {
    setForwardDirection(-getForwardDirection());
    setTimeSinceHit(10);
    setDamageTaken(getDamageTaken() * 11.0F);
  }
  
  public boolean canBeCollidedWith()
  {
    return !this.isDead;
  }
  
  public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_)
  {
    if ((p_180426_10_) && (this.riddenByEntity != null))
    {
      this.prevPosX = (this.posX = p_180426_1_);
      this.prevPosY = (this.posY = p_180426_3_);
      this.prevPosZ = (this.posZ = p_180426_5_);
      this.rotationYaw = p_180426_7_;
      this.rotationPitch = p_180426_8_;
      this.boatPosRotationIncrements = 0;
      setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
      this.motionX = (this.velocityX = 0.0D);
      this.motionY = (this.velocityY = 0.0D);
      this.motionZ = (this.velocityZ = 0.0D);
    }
    else
    {
      if (this.isBoatEmpty)
      {
        this.boatPosRotationIncrements = (p_180426_9_ + 5);
      }
      else
      {
        double var11 = p_180426_1_ - this.posX;
        double var13 = p_180426_3_ - this.posY;
        double var15 = p_180426_5_ - this.posZ;
        double var17 = var11 * var11 + var13 * var13 + var15 * var15;
        if (var17 <= 1.0D) {
          return;
        }
        this.boatPosRotationIncrements = 3;
      }
      this.boatX = p_180426_1_;
      this.boatY = p_180426_3_;
      this.boatZ = p_180426_5_;
      this.boatYaw = p_180426_7_;
      this.boatPitch = p_180426_8_;
      this.motionX = this.velocityX;
      this.motionY = this.velocityY;
      this.motionZ = this.velocityZ;
    }
  }
  
  public void setVelocity(double x, double y, double z)
  {
    this.velocityX = (this.motionX = x);
    this.velocityY = (this.motionY = y);
    this.velocityZ = (this.motionZ = z);
  }
  
  public void onUpdate()
  {
    super.onUpdate();
    if (getTimeSinceHit() > 0) {
      setTimeSinceHit(getTimeSinceHit() - 1);
    }
    if (getDamageTaken() > 0.0F) {
      setDamageTaken(getDamageTaken() - 1.0F);
    }
    this.prevPosX = this.posX;
    this.prevPosY = this.posY;
    this.prevPosZ = this.posZ;
    byte var1 = 5;
    double var2 = 0.0D;
    for (int var4 = 0; var4 < var1; var4++)
    {
      double var5 = getEntityBoundingBox().minY + (getEntityBoundingBox().maxY - getEntityBoundingBox().minY) * (var4 + 0) / var1 - 0.125D;
      double var7 = getEntityBoundingBox().minY + (getEntityBoundingBox().maxY - getEntityBoundingBox().minY) * (var4 + 1) / var1 - 0.125D;
      AxisAlignedBB var9 = new AxisAlignedBB(getEntityBoundingBox().minX, var5, getEntityBoundingBox().minZ, getEntityBoundingBox().maxX, var7, getEntityBoundingBox().maxZ);
      if (this.worldObj.isAABBInMaterial(var9, Material.water)) {
        var2 += 1.0D / var1;
      }
    }
    double var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
    if (var19 > 0.2975D)
    {
      double var6 = Math.cos(this.rotationYaw * 3.141592653589793D / 180.0D);
      double var8 = Math.sin(this.rotationYaw * 3.141592653589793D / 180.0D);
      for (int var10 = 0; var10 < 1.0D + var19 * 60.0D; var10++)
      {
        double var11 = this.rand.nextFloat() * 2.0F - 1.0F;
        double var13 = (this.rand.nextInt(2) * 2 - 1) * 0.7D;
        if (this.rand.nextBoolean())
        {
          double var15 = this.posX - var6 * var11 * 0.8D + var8 * var13;
          double var17 = this.posZ - var8 * var11 * 0.8D - var6 * var13;
          this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, var15, this.posY - 0.125D, var17, this.motionX, this.motionY, this.motionZ, new int[0]);
        }
        else
        {
          double var15 = this.posX + var6 + var8 * var11 * 0.7D;
          double var17 = this.posZ + var8 - var6 * var11 * 0.7D;
          this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, var15, this.posY - 0.125D, var17, this.motionX, this.motionY, this.motionZ, new int[0]);
        }
      }
    }
    if ((this.worldObj.isRemote) && (this.isBoatEmpty))
    {
      if (this.boatPosRotationIncrements > 0)
      {
        double var6 = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
        double var8 = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
        double var24 = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
        double var26 = MathHelper.wrapAngleTo180_double(this.boatYaw - this.rotationYaw);
        this.rotationYaw = ((float)(this.rotationYaw + var26 / this.boatPosRotationIncrements));
        this.rotationPitch = ((float)(this.rotationPitch + (this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements));
        this.boatPosRotationIncrements -= 1;
        setPosition(var6, var8, var24);
        setRotation(this.rotationYaw, this.rotationPitch);
      }
      else
      {
        double var6 = this.posX + this.motionX;
        double var8 = this.posY + this.motionY;
        double var24 = this.posZ + this.motionZ;
        setPosition(var6, var8, var24);
        if (this.onGround)
        {
          this.motionX *= 0.5D;
          this.motionY *= 0.5D;
          this.motionZ *= 0.5D;
        }
        this.motionX *= 0.9900000095367432D;
        this.motionY *= 0.949999988079071D;
        this.motionZ *= 0.9900000095367432D;
      }
    }
    else
    {
      if (var2 < 1.0D)
      {
        double var6 = var2 * 2.0D - 1.0D;
        this.motionY += 0.03999999910593033D * var6;
      }
      else
      {
        if (this.motionY < 0.0D) {
          this.motionY /= 2.0D;
        }
        this.motionY += 0.007000000216066837D;
      }
      if ((this.riddenByEntity instanceof EntityLivingBase))
      {
        EntityLivingBase var20 = (EntityLivingBase)this.riddenByEntity;
        float var21 = this.riddenByEntity.rotationYaw + -var20.moveStrafing * 90.0F;
        this.motionX += -Math.sin(var21 * 3.1415927F / 180.0F) * this.speedMultiplier * var20.moveForward * 0.05000000074505806D;
        this.motionZ += Math.cos(var21 * 3.1415927F / 180.0F) * this.speedMultiplier * var20.moveForward * 0.05000000074505806D;
      }
      double var6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      if (var6 > 0.35D)
      {
        double var8 = 0.35D / var6;
        this.motionX *= var8;
        this.motionZ *= var8;
        var6 = 0.35D;
      }
      if ((var6 > var19) && (this.speedMultiplier < 0.35D))
      {
        this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;
        if (this.speedMultiplier > 0.35D) {
          this.speedMultiplier = 0.35D;
        }
      }
      else
      {
        this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;
        if (this.speedMultiplier < 0.07D) {
          this.speedMultiplier = 0.07D;
        }
      }
      for (int var22 = 0; var22 < 4; var22++)
      {
        int var23 = MathHelper.floor_double(this.posX + (var22 % 2 - 0.5D) * 0.8D);
        int var10 = MathHelper.floor_double(this.posZ + (var22 / 2 - 0.5D) * 0.8D);
        for (int var25 = 0; var25 < 2; var25++)
        {
          int var12 = MathHelper.floor_double(this.posY) + var25;
          BlockPos var27 = new BlockPos(var23, var12, var10);
          Block var14 = this.worldObj.getBlockState(var27).getBlock();
          if (var14 == Blocks.snow_layer)
          {
            this.worldObj.setBlockToAir(var27);
            this.isCollidedHorizontally = false;
          }
          else if (var14 == Blocks.waterlily)
          {
            this.worldObj.destroyBlock(var27, true);
            this.isCollidedHorizontally = false;
          }
        }
      }
      if (this.onGround)
      {
        this.motionX *= 0.5D;
        this.motionY *= 0.5D;
        this.motionZ *= 0.5D;
      }
      moveEntity(this.motionX, this.motionY, this.motionZ);
      if ((this.isCollidedHorizontally) && (var19 > 0.2D))
      {
        if ((this.worldObj.isRemote) || (this.isDead)) {
          break label1529;
        }
        setDead();
        for (var22 = 0; var22 < 3; var22++) {
          dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
        }
        for (var22 = 0; var22 < 2;)
        {
          dropItemWithOffset(Items.stick, 1, 0.0F);var22++; continue;
          
          this.motionX *= 0.9900000095367432D;
          this.motionY *= 0.949999988079071D;
          this.motionZ *= 0.9900000095367432D;
        }
      }
      label1529:
      this.rotationPitch = 0.0F;
      double var8 = this.rotationYaw;
      double var24 = this.prevPosX - this.posX;
      double var26 = this.prevPosZ - this.posZ;
      if (var24 * var24 + var26 * var26 > 0.001D) {
        var8 = (float)(Math.atan2(var26, var24) * 180.0D / 3.141592653589793D);
      }
      double var28 = MathHelper.wrapAngleTo180_double(var8 - this.rotationYaw);
      if (var28 > 20.0D) {
        var28 = 20.0D;
      }
      if (var28 < -20.0D) {
        var28 = -20.0D;
      }
      this.rotationYaw = ((float)(this.rotationYaw + var28));
      setRotation(this.rotationYaw, this.rotationPitch);
      if (!this.worldObj.isRemote)
      {
        List var16 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
        if ((var16 != null) && (!var16.isEmpty())) {
          for (int var29 = 0; var29 < var16.size(); var29++)
          {
            Entity var18 = (Entity)var16.get(var29);
            if ((var18 != this.riddenByEntity) && (var18.canBePushed()) && ((var18 instanceof EntityBoat))) {
              var18.applyEntityCollision(this);
            }
          }
        }
        if ((this.riddenByEntity != null) && (this.riddenByEntity.isDead)) {
          this.riddenByEntity = null;
        }
      }
    }
  }
  
  public void updateRiderPosition()
  {
    if (this.riddenByEntity != null)
    {
      double var1 = Math.cos(this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;
      double var3 = Math.sin(this.rotationYaw * 3.141592653589793D / 180.0D) * 0.4D;
      this.riddenByEntity.setPosition(this.posX + var1, this.posY + getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + var3);
    }
  }
  
  protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
  
  protected void readEntityFromNBT(NBTTagCompound tagCompund) {}
  
  public boolean interactFirst(EntityPlayer playerIn)
  {
    if ((this.riddenByEntity != null) && ((this.riddenByEntity instanceof EntityPlayer)) && (this.riddenByEntity != playerIn)) {
      return true;
    }
    if (!this.worldObj.isRemote) {
      playerIn.mountEntity(this);
    }
    return true;
  }
  
  protected void func_180433_a(double p_180433_1_, boolean p_180433_3_, Block p_180433_4_, BlockPos p_180433_5_)
  {
    if (p_180433_3_)
    {
      if (this.fallDistance > 3.0F)
      {
        fall(this.fallDistance, 1.0F);
        if ((!this.worldObj.isRemote) && (!this.isDead))
        {
          setDead();
          for (int var6 = 0; var6 < 3; var6++) {
            dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
          }
          for (var6 = 0; var6 < 2; var6++) {
            dropItemWithOffset(Items.stick, 1, 0.0F);
          }
        }
        this.fallDistance = 0.0F;
      }
    }
    else if ((this.worldObj.getBlockState(new BlockPos(this).offsetDown()).getBlock().getMaterial() != Material.water) && (p_180433_1_ < 0.0D)) {
      this.fallDistance = ((float)(this.fallDistance - p_180433_1_));
    }
  }
  
  public void setDamageTaken(float p_70266_1_)
  {
    this.dataWatcher.updateObject(19, Float.valueOf(p_70266_1_));
  }
  
  public float getDamageTaken()
  {
    return this.dataWatcher.getWatchableObjectFloat(19);
  }
  
  public void setTimeSinceHit(int p_70265_1_)
  {
    this.dataWatcher.updateObject(17, Integer.valueOf(p_70265_1_));
  }
  
  public int getTimeSinceHit()
  {
    return this.dataWatcher.getWatchableObjectInt(17);
  }
  
  public void setForwardDirection(int p_70269_1_)
  {
    this.dataWatcher.updateObject(18, Integer.valueOf(p_70269_1_));
  }
  
  public int getForwardDirection()
  {
    return this.dataWatcher.getWatchableObjectInt(18);
  }
  
  public void setIsBoatEmpty(boolean p_70270_1_)
  {
    this.isBoatEmpty = p_70270_1_;
  }
}
