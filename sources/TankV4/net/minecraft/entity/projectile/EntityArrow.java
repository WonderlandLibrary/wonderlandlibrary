package net.minecraft.entity.projectile;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityArrow extends Entity implements IProjectile {
   private Block inTile;
   private int ticksInAir;
   public int canBePickedUp;
   public Entity shootingEntity;
   private int inData;
   private int yTile = -1;
   private int knockbackStrength;
   private double damage = 2.0D;
   private boolean inGround;
   private int xTile = -1;
   private int ticksInGround;
   public int arrowShake;
   private int zTile = -1;

   public EntityArrow(World var1, double var2, double var4, double var6) {
      super(var1);
      this.renderDistanceWeight = 10.0D;
      this.setSize(0.5F, 0.5F);
      this.setPosition(var2, var4, var6);
   }

   public void setKnockbackStrength(int var1) {
      this.knockbackStrength = var1;
   }

   public void setIsCritical(boolean var1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (var1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 1));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -2));
      }

   }

   public void onCollideWithPlayer(EntityPlayer var1) {
      if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
         boolean var2 = this.canBePickedUp == 1 || this.canBePickedUp == 2 && var1.capabilities.isCreativeMode;
         if (this.canBePickedUp == 1 && !var1.inventory.addItemStackToInventory(new ItemStack(Items.arrow, 1))) {
            var2 = false;
         }

         if (var2) {
            this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            var1.onItemPickup(this, 1);
            this.setDead();
         }
      }

   }

   protected void entityInit() {
      this.dataWatcher.addObject(16, (byte)0);
   }

   public void setPositionAndRotation2(double var1, double var3, double var5, float var7, float var8, int var9, boolean var10) {
      this.setPosition(var1, var3, var5);
      this.setRotation(var7, var8);
   }

   public EntityArrow(World var1, EntityLivingBase var2, float var3) {
      super(var1);
      this.renderDistanceWeight = 10.0D;
      this.shootingEntity = var2;
      if (var2 instanceof EntityPlayer) {
         this.canBePickedUp = 1;
      }

      this.setSize(0.5F, 0.5F);
      this.setLocationAndAngles(var2.posX, var2.posY + (double)var2.getEyeHeight(), var2.posZ, var2.rotationYaw, var2.rotationPitch);
      this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
      this.posY -= 0.10000000149011612D;
      this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
      this.setPosition(this.posX, this.posY, this.posZ);
      this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
      this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
      this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F));
      this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, var3 * 1.5F, 1.0F);
   }

   public void readEntityFromNBT(NBTTagCompound var1) {
      this.xTile = var1.getShort("xTile");
      this.yTile = var1.getShort("yTile");
      this.zTile = var1.getShort("zTile");
      this.ticksInGround = var1.getShort("life");
      if (var1.hasKey("inTile", 8)) {
         this.inTile = Block.getBlockFromName(var1.getString("inTile"));
      } else {
         this.inTile = Block.getBlockById(var1.getByte("inTile") & 255);
      }

      this.inData = var1.getByte("inData") & 255;
      this.arrowShake = var1.getByte("shake") & 255;
      this.inGround = var1.getByte("inGround") == 1;
      if (var1.hasKey("damage", 99)) {
         this.damage = var1.getDouble("damage");
      }

      if (var1.hasKey("pickup", 99)) {
         this.canBePickedUp = var1.getByte("pickup");
      } else if (var1.hasKey("player", 99)) {
         this.canBePickedUp = var1.getBoolean("player") ? 1 : 0;
      }

   }

   public void setVelocity(double var1, double var3, double var5) {
      this.motionX = var1;
      this.motionY = var3;
      this.motionZ = var5;
      if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
         float var7 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
         this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.func_181159_b(var1, var5) * 180.0D / 3.141592653589793D);
         this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.func_181159_b(var3, (double)var7) * 180.0D / 3.141592653589793D);
         this.prevRotationPitch = this.rotationPitch;
         this.prevRotationYaw = this.rotationYaw;
         this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
         this.ticksInGround = 0;
      }

   }

   public EntityArrow(World var1) {
      super(var1);
      this.renderDistanceWeight = 10.0D;
      this.setSize(0.5F, 0.5F);
   }

   public void writeEntityToNBT(NBTTagCompound var1) {
      var1.setShort("xTile", (short)this.xTile);
      var1.setShort("yTile", (short)this.yTile);
      var1.setShort("zTile", (short)this.zTile);
      var1.setShort("life", (short)this.ticksInGround);
      ResourceLocation var2 = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
      var1.setString("inTile", var2 == null ? "" : var2.toString());
      var1.setByte("inData", (byte)this.inData);
      var1.setByte("shake", (byte)this.arrowShake);
      var1.setByte("inGround", (byte)(this.inGround ? 1 : 0));
      var1.setByte("pickup", (byte)this.canBePickedUp);
      var1.setDouble("damage", this.damage);
   }

   public float getEyeHeight() {
      return 0.0F;
   }

   public boolean canAttackWithItem() {
      return false;
   }

   public void setThrowableHeading(double var1, double var3, double var5, float var7, float var8) {
      float var9 = MathHelper.sqrt_double(var1 * var1 + var3 * var3 + var5 * var5);
      var1 /= (double)var9;
      var3 /= (double)var9;
      var5 /= (double)var9;
      var1 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)var8;
      var3 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)var8;
      var5 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)var8;
      var1 *= (double)var7;
      var3 *= (double)var7;
      var5 *= (double)var7;
      this.motionX = var1;
      this.motionY = var3;
      this.motionZ = var5;
      float var10 = MathHelper.sqrt_double(var1 * var1 + var5 * var5);
      this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.func_181159_b(var1, var5) * 180.0D / 3.141592653589793D);
      this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.func_181159_b(var3, (double)var10) * 180.0D / 3.141592653589793D);
      this.ticksInGround = 0;
   }

   protected boolean canTriggerWalking() {
      return false;
   }

   public void onUpdate() {
      super.onUpdate();
      if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
         float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
         this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D);
         this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, (double)var1) * 180.0D / 3.141592653589793D);
      }

      BlockPos var18 = new BlockPos(this.xTile, this.yTile, this.zTile);
      IBlockState var2 = this.worldObj.getBlockState(var18);
      Block var3 = var2.getBlock();
      if (var3.getMaterial() != Material.air) {
         var3.setBlockBoundsBasedOnState(this.worldObj, var18);
         AxisAlignedBB var4 = var3.getCollisionBoundingBox(this.worldObj, var18, var2);
         if (var4 != null && var4.isVecInside(new Vec3(this.posX, this.posY, this.posZ))) {
            this.inGround = true;
         }
      }

      if (this.arrowShake > 0) {
         --this.arrowShake;
      }

      if (this.inGround) {
         int var19 = var3.getMetaFromState(var2);
         if (var3 == this.inTile && var19 == this.inData) {
            ++this.ticksInGround;
            if (this.ticksInGround >= 1200) {
               this.setDead();
            }
         } else {
            this.inGround = false;
            this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
            this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
            this.ticksInGround = 0;
            this.ticksInAir = 0;
         }
      } else {
         ++this.ticksInAir;
         Vec3 var20 = new Vec3(this.posX, this.posY, this.posZ);
         Vec3 var5 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         MovingObjectPosition var6 = this.worldObj.rayTraceBlocks(var20, var5, false, true, false);
         var20 = new Vec3(this.posX, this.posY, this.posZ);
         var5 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         if (var6 != null) {
            var5 = new Vec3(var6.hitVec.xCoord, var6.hitVec.yCoord, var6.hitVec.zCoord);
         }

         Entity var7 = null;
         List var8 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
         double var9 = 0.0D;

         int var11;
         float var13;
         for(var11 = 0; var11 < var8.size(); ++var11) {
            Entity var12 = (Entity)var8.get(var11);
            if (var12.canBeCollidedWith() && (var12 != this.shootingEntity || this.ticksInAir >= 5)) {
               var13 = 0.3F;
               AxisAlignedBB var14 = var12.getEntityBoundingBox().expand((double)var13, (double)var13, (double)var13);
               MovingObjectPosition var15 = var14.calculateIntercept(var20, var5);
               if (var15 != null) {
                  double var16 = var20.squareDistanceTo(var15.hitVec);
                  if (var16 < var9 || var9 == 0.0D) {
                     var7 = var12;
                     var9 = var16;
                  }
               }
            }
         }

         if (var7 != null) {
            var6 = new MovingObjectPosition(var7);
         }

         if (var6 != null && var6.entityHit != null && var6.entityHit instanceof EntityPlayer) {
            EntityPlayer var21 = (EntityPlayer)var6.entityHit;
            if (var21.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(var21)) {
               var6 = null;
            }
         }

         float var22;
         float var30;
         if (var6 != null) {
            if (var6.entityHit != null) {
               var22 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
               int var24 = MathHelper.ceiling_double_int((double)var22 * this.damage);
               if (this != false) {
                  var24 += this.rand.nextInt(var24 / 2 + 2);
               }

               DamageSource var26;
               if (this.shootingEntity == null) {
                  var26 = DamageSource.causeArrowDamage(this, this);
               } else {
                  var26 = DamageSource.causeArrowDamage(this, this.shootingEntity);
               }

               if (this.isBurning() && !(var6.entityHit instanceof EntityEnderman)) {
                  var6.entityHit.setFire(5);
               }

               if (var6.entityHit.attackEntityFrom(var26, (float)var24)) {
                  if (var6.entityHit instanceof EntityLivingBase) {
                     EntityLivingBase var28 = (EntityLivingBase)var6.entityHit;
                     if (!this.worldObj.isRemote) {
                        var28.setArrowCountInEntity(var28.getArrowCountInEntity() + 1);
                     }

                     if (this.knockbackStrength > 0) {
                        var30 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                        if (var30 > 0.0F) {
                           var6.entityHit.addVelocity(this.motionX * (double)this.knockbackStrength * 0.6000000238418579D / (double)var30, 0.1D, this.motionZ * (double)this.knockbackStrength * 0.6000000238418579D / (double)var30);
                        }
                     }

                     if (this.shootingEntity instanceof EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments(var28, this.shootingEntity);
                        EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, var28);
                     }

                     if (this.shootingEntity != null && var6.entityHit != this.shootingEntity && var6.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                     }
                  }

                  this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                  if (!(var6.entityHit instanceof EntityEnderman)) {
                     this.setDead();
                  }
               } else {
                  this.motionX *= -0.10000000149011612D;
                  this.motionY *= -0.10000000149011612D;
                  this.motionZ *= -0.10000000149011612D;
                  this.rotationYaw += 180.0F;
                  this.prevRotationYaw += 180.0F;
                  this.ticksInAir = 0;
               }
            } else {
               BlockPos var23 = var6.getBlockPos();
               this.xTile = var23.getX();
               this.yTile = var23.getY();
               this.zTile = var23.getZ();
               IBlockState var25 = this.worldObj.getBlockState(var23);
               this.inTile = var25.getBlock();
               this.inData = this.inTile.getMetaFromState(var25);
               this.motionX = (double)((float)(var6.hitVec.xCoord - this.posX));
               this.motionY = (double)((float)(var6.hitVec.yCoord - this.posY));
               this.motionZ = (double)((float)(var6.hitVec.zCoord - this.posZ));
               var13 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
               this.posX -= this.motionX / (double)var13 * 0.05000000074505806D;
               this.posY -= this.motionY / (double)var13 * 0.05000000074505806D;
               this.posZ -= this.motionZ / (double)var13 * 0.05000000074505806D;
               this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
               this.inGround = true;
               this.arrowShake = 7;
               this.setIsCritical(false);
               if (this.inTile.getMaterial() != Material.air) {
                  this.inTile.onEntityCollidedWithBlock(this.worldObj, var23, var25, this);
               }
            }
         }

         if (this != false) {
            for(var11 = 0; var11 < 4; ++var11) {
               this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double)var11 / 4.0D, this.posY + this.motionY * (double)var11 / 4.0D, this.posZ + this.motionZ * (double)var11 / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
            }
         }

         this.posX += this.motionX;
         this.posY += this.motionY;
         this.posZ += this.motionZ;
         var22 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
         this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0D / 3.141592653589793D);

         for(this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, (double)var22) * 180.0D / 3.141592653589793D); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
         }

         while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
         }

         while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
         }

         while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
         }

         this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
         this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
         float var27 = 0.99F;
         var13 = 0.05F;
         if (this.isInWater()) {
            for(int var29 = 0; var29 < 4; ++var29) {
               var30 = 0.25F;
               this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double)var30, this.posY - this.motionY * (double)var30, this.posZ - this.motionZ * (double)var30, this.motionX, this.motionY, this.motionZ);
            }

            var27 = 0.6F;
         }

         if (this.isWet()) {
            this.extinguish();
         }

         this.motionX *= (double)var27;
         this.motionY *= (double)var27;
         this.motionZ *= (double)var27;
         this.motionY -= (double)var13;
         this.setPosition(this.posX, this.posY, this.posZ);
         this.doBlockCollisions();
      }

   }

   public EntityArrow(World var1, EntityLivingBase var2, EntityLivingBase var3, float var4, float var5) {
      super(var1);
      this.renderDistanceWeight = 10.0D;
      this.shootingEntity = var2;
      if (var2 instanceof EntityPlayer) {
         this.canBePickedUp = 1;
      }

      this.posY = var2.posY + (double)var2.getEyeHeight() - 0.10000000149011612D;
      double var6 = var3.posX - var2.posX;
      double var8 = var3.getEntityBoundingBox().minY + (double)(var3.height / 3.0F) - this.posY;
      double var10 = var3.posZ - var2.posZ;
      double var12 = (double)MathHelper.sqrt_double(var6 * var6 + var10 * var10);
      if (var12 >= 1.0E-7D) {
         float var14 = (float)(MathHelper.func_181159_b(var10, var6) * 180.0D / 3.141592653589793D) - 90.0F;
         float var15 = (float)(-(MathHelper.func_181159_b(var8, var12) * 180.0D / 3.141592653589793D));
         double var16 = var6 / var12;
         double var18 = var10 / var12;
         this.setLocationAndAngles(var2.posX + var16, this.posY, var2.posZ + var18, var14, var15);
         float var20 = (float)(var12 * 0.20000000298023224D);
         this.setThrowableHeading(var6, var8 + (double)var20, var10, var4, var5);
      }

   }

   public void setDamage(double var1) {
      this.damage = var1;
   }

   public double getDamage() {
      return this.damage;
   }
}
