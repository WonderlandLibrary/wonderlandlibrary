package net.minecraft.entity.projectile;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemFishFood.FishType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityFishHook
  extends Entity
{
  private static final List JUNK = Arrays.asList(new WeightedRandomFishable[] { new WeightedRandomFishable(new ItemStack(Items.leather_boots), 10).setMaxDamagePercent(0.9F), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack(Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 2).setMaxDamagePercent(0.9F), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, EnumDyeColor.BLACK.getDyeColorDamage()), 1), new WeightedRandomFishable(new ItemStack(Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10) });
  private static final List VALUABLES = Arrays.asList(new WeightedRandomFishable[] { new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), new WeightedRandomFishable(new ItemStack(Items.bow), 1).setMaxDamagePercent(0.25F).setEnchantable(), new WeightedRandomFishable(new ItemStack(Items.fishing_rod), 1).setMaxDamagePercent(0.25F).setEnchantable(), new WeightedRandomFishable(new ItemStack(Items.book), 1).setEnchantable() });
  private static final List FISH = Arrays.asList(new WeightedRandomFishable[] { new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getItemDamage()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.getItemDamage()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.getItemDamage()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.getItemDamage()), 13) });
  private int xTile;
  private int yTile;
  private int zTile;
  private Block inTile;
  private boolean inGround;
  public int shake;
  public EntityPlayer angler;
  private int ticksInGround;
  private int ticksInAir;
  private int ticksCatchable;
  private int ticksCaughtDelay;
  private int ticksCatchableDelay;
  private float fishApproachAngle;
  public Entity caughtEntity;
  private int fishPosRotationIncrements;
  private double fishX;
  private double fishY;
  private double fishZ;
  private double fishYaw;
  private double fishPitch;
  private double clientMotionX;
  private double clientMotionY;
  private double clientMotionZ;
  private static final String __OBFID = "CL_00001663";
  
  public static List func_174855_j()
  {
    return FISH;
  }
  
  public EntityFishHook(World worldIn)
  {
    super(worldIn);
    this.xTile = -1;
    this.yTile = -1;
    this.zTile = -1;
    setSize(0.25F, 0.25F);
    this.ignoreFrustumCheck = true;
  }
  
  public EntityFishHook(World worldIn, double p_i1765_2_, double p_i1765_4_, double p_i1765_6_, EntityPlayer p_i1765_8_)
  {
    this(worldIn);
    setPosition(p_i1765_2_, p_i1765_4_, p_i1765_6_);
    this.ignoreFrustumCheck = true;
    this.angler = p_i1765_8_;
    p_i1765_8_.fishEntity = this;
  }
  
  public EntityFishHook(World worldIn, EntityPlayer p_i1766_2_)
  {
    super(worldIn);
    this.xTile = -1;
    this.yTile = -1;
    this.zTile = -1;
    this.ignoreFrustumCheck = true;
    this.angler = p_i1766_2_;
    this.angler.fishEntity = this;
    setSize(0.25F, 0.25F);
    setLocationAndAngles(p_i1766_2_.posX, p_i1766_2_.posY + p_i1766_2_.getEyeHeight(), p_i1766_2_.posZ, p_i1766_2_.rotationYaw, p_i1766_2_.rotationPitch);
    this.posX -= MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F;
    this.posY -= 0.10000000149011612D;
    this.posZ -= MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F;
    setPosition(this.posX, this.posY, this.posZ);
    float var3 = 0.4F;
    this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * var3);
    this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * var3);
    this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F) * var3);
    handleHookCasting(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
  }
  
  protected void entityInit() {}
  
  public boolean isInRangeToRenderDist(double distance)
  {
    double var3 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
    var3 *= 64.0D;
    return distance < var3 * var3;
  }
  
  public void handleHookCasting(double p_146035_1_, double p_146035_3_, double p_146035_5_, float p_146035_7_, float p_146035_8_)
  {
    float var9 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_3_ * p_146035_3_ + p_146035_5_ * p_146035_5_);
    p_146035_1_ /= var9;
    p_146035_3_ /= var9;
    p_146035_5_ /= var9;
    p_146035_1_ += this.rand.nextGaussian() * 0.007499999832361937D * p_146035_8_;
    p_146035_3_ += this.rand.nextGaussian() * 0.007499999832361937D * p_146035_8_;
    p_146035_5_ += this.rand.nextGaussian() * 0.007499999832361937D * p_146035_8_;
    p_146035_1_ *= p_146035_7_;
    p_146035_3_ *= p_146035_7_;
    p_146035_5_ *= p_146035_7_;
    this.motionX = p_146035_1_;
    this.motionY = p_146035_3_;
    this.motionZ = p_146035_5_;
    float var10 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_5_ * p_146035_5_);
    this.prevRotationYaw = (this.rotationYaw = (float)(Math.atan2(p_146035_1_, p_146035_5_) * 180.0D / 3.141592653589793D));
    this.prevRotationPitch = (this.rotationPitch = (float)(Math.atan2(p_146035_3_, var10) * 180.0D / 3.141592653589793D));
    this.ticksInGround = 0;
  }
  
  public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_)
  {
    this.fishX = p_180426_1_;
    this.fishY = p_180426_3_;
    this.fishZ = p_180426_5_;
    this.fishYaw = p_180426_7_;
    this.fishPitch = p_180426_8_;
    this.fishPosRotationIncrements = p_180426_9_;
    this.motionX = this.clientMotionX;
    this.motionY = this.clientMotionY;
    this.motionZ = this.clientMotionZ;
  }
  
  public void setVelocity(double x, double y, double z)
  {
    this.clientMotionX = (this.motionX = x);
    this.clientMotionY = (this.motionY = y);
    this.clientMotionZ = (this.motionZ = z);
  }
  
  public void onUpdate()
  {
    super.onUpdate();
    if (this.fishPosRotationIncrements > 0)
    {
      double var28 = this.posX + (this.fishX - this.posX) / this.fishPosRotationIncrements;
      double var29 = this.posY + (this.fishY - this.posY) / this.fishPosRotationIncrements;
      double var30 = this.posZ + (this.fishZ - this.posZ) / this.fishPosRotationIncrements;
      double var7 = MathHelper.wrapAngleTo180_double(this.fishYaw - this.rotationYaw);
      this.rotationYaw = ((float)(this.rotationYaw + var7 / this.fishPosRotationIncrements));
      this.rotationPitch = ((float)(this.rotationPitch + (this.fishPitch - this.rotationPitch) / this.fishPosRotationIncrements));
      this.fishPosRotationIncrements -= 1;
      setPosition(var28, var29, var30);
      setRotation(this.rotationYaw, this.rotationPitch);
    }
    else
    {
      if (!this.worldObj.isRemote)
      {
        ItemStack var1 = this.angler.getCurrentEquippedItem();
        if ((this.angler.isDead) || (!this.angler.isEntityAlive()) || (var1 == null) || (var1.getItem() != Items.fishing_rod) || (getDistanceSqToEntity(this.angler) > 1024.0D))
        {
          setDead();
          this.angler.fishEntity = null;
          return;
        }
        if (this.caughtEntity != null)
        {
          if (!this.caughtEntity.isDead)
          {
            this.posX = this.caughtEntity.posX;
            double var10002 = this.caughtEntity.height;
            this.posY = (this.caughtEntity.getEntityBoundingBox().minY + var10002 * 0.8D);
            this.posZ = this.caughtEntity.posZ;
            return;
          }
          this.caughtEntity = null;
        }
      }
      if (this.shake > 0) {
        this.shake -= 1;
      }
      if (this.inGround)
      {
        if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile)
        {
          this.ticksInGround += 1;
          if (this.ticksInGround == 1200) {
            setDead();
          }
          return;
        }
        this.inGround = false;
        this.motionX *= this.rand.nextFloat() * 0.2F;
        this.motionY *= this.rand.nextFloat() * 0.2F;
        this.motionZ *= this.rand.nextFloat() * 0.2F;
        this.ticksInGround = 0;
        this.ticksInAir = 0;
      }
      else
      {
        this.ticksInAir += 1;
      }
      Vec3 var27 = new Vec3(this.posX, this.posY, this.posZ);
      Vec3 var2 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var27, var2);
      var27 = new Vec3(this.posX, this.posY, this.posZ);
      var2 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      if (var3 != null) {
        var2 = new Vec3(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
      }
      Entity var4 = null;
      List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
      double var6 = 0.0D;
      for (int var8 = 0; var8 < var5.size(); var8++)
      {
        Entity var9 = (Entity)var5.get(var8);
        if ((var9.canBeCollidedWith()) && ((var9 != this.angler) || (this.ticksInAir >= 5)))
        {
          float var10 = 0.3F;
          AxisAlignedBB var11 = var9.getEntityBoundingBox().expand(var10, var10, var10);
          MovingObjectPosition var12 = var11.calculateIntercept(var27, var2);
          if (var12 != null)
          {
            double var13 = var27.distanceTo(var12.hitVec);
            if ((var13 < var6) || (var6 == 0.0D))
            {
              var4 = var9;
              var6 = var13;
            }
          }
        }
      }
      if (var4 != null) {
        var3 = new MovingObjectPosition(var4);
      }
      if (var3 != null) {
        if (var3.entityHit != null)
        {
          if (var3.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.angler), 0.0F)) {
            this.caughtEntity = var3.entityHit;
          }
        }
        else {
          this.inGround = true;
        }
      }
      if (!this.inGround)
      {
        moveEntity(this.motionX, this.motionY, this.motionZ);
        float var31 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = ((float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D));
        for (this.rotationPitch = ((float)(Math.atan2(this.motionY, var31) * 180.0D / 3.141592653589793D)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {}
        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
          this.prevRotationPitch += 360.0F;
        }
        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
          this.prevRotationYaw -= 360.0F;
        }
        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
          this.prevRotationYaw += 360.0F;
        }
        this.rotationPitch = (this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F);
        this.rotationYaw = (this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F);
        float var32 = 0.92F;
        if ((this.onGround) || (this.isCollidedHorizontally)) {
          var32 = 0.5F;
        }
        byte var33 = 5;
        double var34 = 0.0D;
        for (int var35 = 0; var35 < var33; var35++)
        {
          AxisAlignedBB var14 = getEntityBoundingBox();
          double var15 = var14.maxY - var14.minY;
          double var17 = var14.minY + var15 * var35 / var33;
          double var19 = var14.minY + var15 * (var35 + 1) / var33;
          AxisAlignedBB var21 = new AxisAlignedBB(var14.minX, var17, var14.minZ, var14.maxX, var19, var14.maxZ);
          if (this.worldObj.isAABBInMaterial(var21, Material.water)) {
            var34 += 1.0D / var33;
          }
        }
        if ((!this.worldObj.isRemote) && (var34 > 0.0D))
        {
          WorldServer var36 = (WorldServer)this.worldObj;
          int var37 = 1;
          BlockPos var38 = new BlockPos(this).offsetUp();
          if ((this.rand.nextFloat() < 0.25F) && (this.worldObj.func_175727_C(var38))) {
            var37 = 2;
          }
          if ((this.rand.nextFloat() < 0.5F) && (!this.worldObj.isAgainstSky(var38))) {
            var37--;
          }
          if (this.ticksCatchable > 0)
          {
            this.ticksCatchable -= 1;
            if (this.ticksCatchable <= 0)
            {
              this.ticksCaughtDelay = 0;
              this.ticksCatchableDelay = 0;
            }
          }
          else if (this.ticksCatchableDelay > 0)
          {
            this.ticksCatchableDelay -= var37;
            if (this.ticksCatchableDelay <= 0)
            {
              this.motionY -= 0.20000000298023224D;
              playSound("random.splash", 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
              float var16 = MathHelper.floor_double(getEntityBoundingBox().minY);
              var36.func_175739_a(EnumParticleTypes.WATER_BUBBLE, this.posX, var16 + 1.0F, this.posZ, (int)(1.0F + this.width * 20.0F), this.width, 0.0D, this.width, 0.20000000298023224D, new int[0]);
              var36.func_175739_a(EnumParticleTypes.WATER_WAKE, this.posX, var16 + 1.0F, this.posZ, (int)(1.0F + this.width * 20.0F), this.width, 0.0D, this.width, 0.20000000298023224D, new int[0]);
              this.ticksCatchable = MathHelper.getRandomIntegerInRange(this.rand, 10, 30);
            }
            else
            {
              this.fishApproachAngle = ((float)(this.fishApproachAngle + this.rand.nextGaussian() * 4.0D));
              float var16 = this.fishApproachAngle * 0.017453292F;
              float var39 = MathHelper.sin(var16);
              float var18 = MathHelper.cos(var16);
              double var19 = this.posX + var39 * this.ticksCatchableDelay * 0.1F;
              double var40 = MathHelper.floor_double(getEntityBoundingBox().minY) + 1.0F;
              double var23 = this.posZ + var18 * this.ticksCatchableDelay * 0.1F;
              if (this.rand.nextFloat() < 0.15F) {
                var36.func_175739_a(EnumParticleTypes.WATER_BUBBLE, var19, var40 - 0.10000000149011612D, var23, 1, var39, 0.1D, var18, 0.0D, new int[0]);
              }
              float var25 = var39 * 0.04F;
              float var26 = var18 * 0.04F;
              var36.func_175739_a(EnumParticleTypes.WATER_WAKE, var19, var40, var23, 0, var26, 0.01D, -var25, 1.0D, new int[0]);
              var36.func_175739_a(EnumParticleTypes.WATER_WAKE, var19, var40, var23, 0, -var26, 0.01D, var25, 1.0D, new int[0]);
            }
          }
          else if (this.ticksCaughtDelay > 0)
          {
            this.ticksCaughtDelay -= var37;
            float var16 = 0.15F;
            if (this.ticksCaughtDelay < 20) {
              var16 = (float)(var16 + (20 - this.ticksCaughtDelay) * 0.05D);
            } else if (this.ticksCaughtDelay < 40) {
              var16 = (float)(var16 + (40 - this.ticksCaughtDelay) * 0.02D);
            } else if (this.ticksCaughtDelay < 60) {
              var16 = (float)(var16 + (60 - this.ticksCaughtDelay) * 0.01D);
            }
            if (this.rand.nextFloat() < var16)
            {
              float var39 = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F) * 0.017453292F;
              float var18 = MathHelper.randomFloatClamp(this.rand, 25.0F, 60.0F);
              double var19 = this.posX + MathHelper.sin(var39) * var18 * 0.1F;
              double var40 = MathHelper.floor_double(getEntityBoundingBox().minY) + 1.0F;
              double var23 = this.posZ + MathHelper.cos(var39) * var18 * 0.1F;
              var36.func_175739_a(EnumParticleTypes.WATER_SPLASH, var19, var40, var23, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D, new int[0]);
            }
            if (this.ticksCaughtDelay <= 0)
            {
              this.fishApproachAngle = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F);
              this.ticksCatchableDelay = MathHelper.getRandomIntegerInRange(this.rand, 20, 80);
            }
          }
          else
          {
            this.ticksCaughtDelay = MathHelper.getRandomIntegerInRange(this.rand, 100, 900);
            this.ticksCaughtDelay -= EnchantmentHelper.func_151387_h(this.angler) * 20 * 5;
          }
          if (this.ticksCatchable > 0) {
            this.motionY -= this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat() * 0.2D;
          }
        }
        double var13 = var34 * 2.0D - 1.0D;
        this.motionY += 0.03999999910593033D * var13;
        if (var34 > 0.0D)
        {
          var32 = (float)(var32 * 0.9D);
          this.motionY *= 0.8D;
        }
        this.motionX *= var32;
        this.motionY *= var32;
        this.motionZ *= var32;
        setPosition(this.posX, this.posY, this.posZ);
      }
    }
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound)
  {
    tagCompound.setShort("xTile", (short)this.xTile);
    tagCompound.setShort("yTile", (short)this.yTile);
    tagCompound.setShort("zTile", (short)this.zTile);
    ResourceLocation var2 = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
    tagCompound.setString("inTile", var2 == null ? "" : var2.toString());
    tagCompound.setByte("shake", (byte)this.shake);
    tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund)
  {
    this.xTile = tagCompund.getShort("xTile");
    this.yTile = tagCompund.getShort("yTile");
    this.zTile = tagCompund.getShort("zTile");
    if (tagCompund.hasKey("inTile", 8)) {
      this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
    } else {
      this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
    }
    this.shake = (tagCompund.getByte("shake") & 0xFF);
    this.inGround = (tagCompund.getByte("inGround") == 1);
  }
  
  public int handleHookRetraction()
  {
    if (this.worldObj.isRemote) {
      return 0;
    }
    byte var1 = 0;
    if (this.caughtEntity != null)
    {
      double var2 = this.angler.posX - this.posX;
      double var4 = this.angler.posY - this.posY;
      double var6 = this.angler.posZ - this.posZ;
      double var8 = MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
      double var10 = 0.1D;
      this.caughtEntity.motionX += var2 * var10;
      this.caughtEntity.motionY += var4 * var10 + MathHelper.sqrt_double(var8) * 0.08D;
      this.caughtEntity.motionZ += var6 * var10;
      var1 = 3;
    }
    else if (this.ticksCatchable > 0)
    {
      EntityItem var13 = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, func_146033_f());
      double var3 = this.angler.posX - this.posX;
      double var5 = this.angler.posY - this.posY;
      double var7 = this.angler.posZ - this.posZ;
      double var9 = MathHelper.sqrt_double(var3 * var3 + var5 * var5 + var7 * var7);
      double var11 = 0.1D;
      var13.motionX = (var3 * var11);
      var13.motionY = (var5 * var11 + MathHelper.sqrt_double(var9) * 0.08D);
      var13.motionZ = (var7 * var11);
      this.worldObj.spawnEntityInWorld(var13);
      this.angler.worldObj.spawnEntityInWorld(new EntityXPOrb(this.angler.worldObj, this.angler.posX, this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));
      var1 = 1;
    }
    if (this.inGround) {
      var1 = 2;
    }
    setDead();
    this.angler.fishEntity = null;
    return var1;
  }
  
  private ItemStack func_146033_f()
  {
    float var1 = this.worldObj.rand.nextFloat();
    int var2 = EnchantmentHelper.func_151386_g(this.angler);
    int var3 = EnchantmentHelper.func_151387_h(this.angler);
    float var4 = 0.1F - var2 * 0.025F - var3 * 0.01F;
    float var5 = 0.05F + var2 * 0.01F - var3 * 0.01F;
    var4 = MathHelper.clamp_float(var4, 0.0F, 1.0F);
    var5 = MathHelper.clamp_float(var5, 0.0F, 1.0F);
    if (var1 < var4)
    {
      this.angler.triggerAchievement(StatList.junkFishedStat);
      return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, JUNK)).getItemStack(this.rand);
    }
    var1 -= var4;
    if (var1 < var5)
    {
      this.angler.triggerAchievement(StatList.treasureFishedStat);
      return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, VALUABLES)).getItemStack(this.rand);
    }
    float var10000 = var1 - var5;
    this.angler.triggerAchievement(StatList.fishCaughtStat);
    return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, FISH)).getItemStack(this.rand);
  }
  
  public void setDead()
  {
    super.setDead();
    if (this.angler != null) {
      this.angler.fishEntity = null;
    }
  }
}
