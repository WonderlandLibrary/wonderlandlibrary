/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
extends Entity {
    private boolean isBoatEmpty = true;
    private double speedMultiplier = 0.07;
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

    public EntityBoat(World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(1.5f, 0.6f);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Float(0.0f));
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return entityIn.getEntityBoundingBox();
    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        return this.getEntityBoundingBox();
    }

    @Override
    public boolean canBePushed() {
        return true;
    }

    public EntityBoat(World worldIn, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_) {
        this(worldIn);
        this.setPosition(p_i1705_2_, p_i1705_4_, p_i1705_6_);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = p_i1705_2_;
        this.prevPosY = p_i1705_4_;
        this.prevPosZ = p_i1705_6_;
    }

    @Override
    public double getMountedYOffset() {
        return (double)this.height * 0.0 - (double)0.3f;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        if (!this.worldObj.isRemote && !this.isDead) {
            boolean var3;
            if (this.riddenByEntity != null && this.riddenByEntity == source.getEntity() && source instanceof EntityDamageSourceIndirect) {
                return false;
            }
            this.setForwardDirection(-this.getForwardDirection());
            this.setTimeSinceHit(10);
            this.setDamageTaken(this.getDamageTaken() + amount * 10.0f);
            this.setBeenAttacked();
            boolean bl = var3 = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;
            if (var3 || this.getDamageTaken() > 40.0f) {
                if (this.riddenByEntity != null) {
                    this.riddenByEntity.mountEntity(this);
                }
                if (!var3) {
                    this.dropItemWithOffset(Items.boat, 1, 0.0f);
                }
                this.setDead();
            }
            return true;
        }
        return true;
    }

    @Override
    public void performHurtAnimation() {
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() * 11.0f);
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    @Override
    public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
        if (p_180426_10_ && this.riddenByEntity != null) {
            this.prevPosX = this.posX = p_180426_1_;
            this.prevPosY = this.posY = p_180426_3_;
            this.prevPosZ = this.posZ = p_180426_5_;
            this.rotationYaw = p_180426_7_;
            this.rotationPitch = p_180426_8_;
            this.boatPosRotationIncrements = 0;
            this.setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
            this.velocityX = 0.0;
            this.motionX = 0.0;
            this.velocityY = 0.0;
            this.motionY = 0.0;
            this.velocityZ = 0.0;
            this.motionZ = 0.0;
        } else {
            if (this.isBoatEmpty) {
                this.boatPosRotationIncrements = p_180426_9_ + 5;
            } else {
                double var11 = p_180426_1_ - this.posX;
                double var13 = p_180426_3_ - this.posY;
                double var15 = p_180426_5_ - this.posZ;
                double var17 = var11 * var11 + var13 * var13 + var15 * var15;
                if (var17 <= 1.0) {
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

    @Override
    public void setVelocity(double x, double y, double z) {
        this.velocityX = this.motionX = x;
        this.velocityY = this.motionY = y;
        this.velocityZ = this.motionZ = z;
    }

    @Override
    public void onUpdate() {
        double var26;
        double var24;
        int var10;
        double var8;
        double var6;
        super.onUpdate();
        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }
        if (this.getDamageTaken() > 0.0f) {
            this.setDamageTaken(this.getDamageTaken() - 1.0f);
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        int var1 = 5;
        double var2 = 0.0;
        for (int var4 = 0; var4 < var1; ++var4) {
            double var5 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double)(var4 + 0) / (double)var1 - 0.125;
            double var7 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double)(var4 + 1) / (double)var1 - 0.125;
            AxisAlignedBB var9 = new AxisAlignedBB(this.getEntityBoundingBox().minX, var5, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().maxX, var7, this.getEntityBoundingBox().maxZ);
            if (!this.worldObj.isAABBInMaterial(var9, Material.water)) continue;
            var2 += 1.0 / (double)var1;
        }
        double var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        if (var19 > 0.2975) {
            var6 = Math.cos((double)this.rotationYaw * Math.PI / 180.0);
            var8 = Math.sin((double)this.rotationYaw * Math.PI / 180.0);
            var10 = 0;
            while ((double)var10 < 1.0 + var19 * 60.0) {
                double var17;
                double var15;
                double var11 = this.rand.nextFloat() * 2.0f - 1.0f;
                double var13 = (double)(this.rand.nextInt(2) * 2 - 1) * 0.7;
                if (this.rand.nextBoolean()) {
                    var15 = this.posX - var6 * var11 * 0.8 + var8 * var13;
                    var17 = this.posZ - var8 * var11 * 0.8 - var6 * var13;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, var15, this.posY - 0.125, var17, this.motionX, this.motionY, this.motionZ, new int[0]);
                } else {
                    var15 = this.posX + var6 + var8 * var11 * 0.7;
                    var17 = this.posZ + var8 - var6 * var11 * 0.7;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, var15, this.posY - 0.125, var17, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
                ++var10;
            }
        }
        if (this.worldObj.isRemote && this.isBoatEmpty) {
            if (this.boatPosRotationIncrements > 0) {
                var6 = this.posX + (this.boatX - this.posX) / (double)this.boatPosRotationIncrements;
                var8 = this.posY + (this.boatY - this.posY) / (double)this.boatPosRotationIncrements;
                var24 = this.posZ + (this.boatZ - this.posZ) / (double)this.boatPosRotationIncrements;
                var26 = MathHelper.wrapAngleTo180_double(this.boatYaw - (double)this.rotationYaw);
                this.rotationYaw = (float)((double)this.rotationYaw + var26 / (double)this.boatPosRotationIncrements);
                this.rotationPitch = (float)((double)this.rotationPitch + (this.boatPitch - (double)this.rotationPitch) / (double)this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPosition(var6, var8, var24);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            } else {
                var6 = this.posX + this.motionX;
                var8 = this.posY + this.motionY;
                var24 = this.posZ + this.motionZ;
                this.setPosition(var6, var8, var24);
                if (this.onGround) {
                    this.motionX *= 0.5;
                    this.motionY *= 0.5;
                    this.motionZ *= 0.5;
                }
                this.motionX *= (double)0.99f;
                this.motionY *= (double)0.95f;
                this.motionZ *= (double)0.99f;
            }
        } else {
            double var28;
            int var22;
            if (var2 < 1.0) {
                var6 = var2 * 2.0 - 1.0;
                this.motionY += (double)0.04f * var6;
            } else {
                if (this.motionY < 0.0) {
                    this.motionY /= 2.0;
                }
                this.motionY += (double)0.007f;
            }
            if (this.riddenByEntity instanceof EntityLivingBase) {
                EntityLivingBase var20 = (EntityLivingBase)this.riddenByEntity;
                float var21 = this.riddenByEntity.rotationYaw + -var20.moveStrafing * 90.0f;
                this.motionX += -Math.sin(var21 * (float)Math.PI / 180.0f) * this.speedMultiplier * (double)var20.moveForward * (double)0.05f;
                this.motionZ += Math.cos(var21 * (float)Math.PI / 180.0f) * this.speedMultiplier * (double)var20.moveForward * (double)0.05f;
            }
            if ((var6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ)) > 0.35) {
                var8 = 0.35 / var6;
                this.motionX *= var8;
                this.motionZ *= var8;
                var6 = 0.35;
            }
            if (var6 > var19 && this.speedMultiplier < 0.35) {
                this.speedMultiplier += (0.35 - this.speedMultiplier) / 35.0;
                if (this.speedMultiplier > 0.35) {
                    this.speedMultiplier = 0.35;
                }
            } else {
                this.speedMultiplier -= (this.speedMultiplier - 0.07) / 35.0;
                if (this.speedMultiplier < 0.07) {
                    this.speedMultiplier = 0.07;
                }
            }
            for (var22 = 0; var22 < 4; ++var22) {
                int var23 = MathHelper.floor_double(this.posX + ((double)(var22 % 2) - 0.5) * 0.8);
                var10 = MathHelper.floor_double(this.posZ + ((double)(var22 / 2) - 0.5) * 0.8);
                for (int var25 = 0; var25 < 2; ++var25) {
                    int var12 = MathHelper.floor_double(this.posY) + var25;
                    BlockPos var27 = new BlockPos(var23, var12, var10);
                    Block var14 = this.worldObj.getBlockState(var27).getBlock();
                    if (var14 == Blocks.snow_layer) {
                        this.worldObj.setBlockToAir(var27);
                        this.isCollidedHorizontally = false;
                        continue;
                    }
                    if (var14 != Blocks.waterlily) continue;
                    this.worldObj.destroyBlock(var27, true);
                    this.isCollidedHorizontally = false;
                }
            }
            if (this.onGround) {
                this.motionX *= 0.5;
                this.motionY *= 0.5;
                this.motionZ *= 0.5;
            }
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            if (this.isCollidedHorizontally && var19 > 0.2) {
                if (!this.worldObj.isRemote && !this.isDead) {
                    this.setDead();
                    for (var22 = 0; var22 < 3; ++var22) {
                        this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0f);
                    }
                    for (var22 = 0; var22 < 2; ++var22) {
                        this.dropItemWithOffset(Items.stick, 1, 0.0f);
                    }
                }
            } else {
                this.motionX *= (double)0.99f;
                this.motionY *= (double)0.95f;
                this.motionZ *= (double)0.99f;
            }
            this.rotationPitch = 0.0f;
            var8 = this.rotationYaw;
            var24 = this.prevPosX - this.posX;
            var26 = this.prevPosZ - this.posZ;
            if (var24 * var24 + var26 * var26 > 0.001) {
                var8 = (float)(Math.atan2(var26, var24) * 180.0 / Math.PI);
            }
            if ((var28 = MathHelper.wrapAngleTo180_double(var8 - (double)this.rotationYaw)) > 20.0) {
                var28 = 20.0;
            }
            if (var28 < -20.0) {
                var28 = -20.0;
            }
            this.rotationYaw = (float)((double)this.rotationYaw + var28);
            this.setRotation(this.rotationYaw, this.rotationPitch);
            if (!this.worldObj.isRemote) {
                List var16 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.2f, 0.0, 0.2f));
                if (var16 != null && !var16.isEmpty()) {
                    for (int var29 = 0; var29 < var16.size(); ++var29) {
                        Entity var18 = (Entity)var16.get(var29);
                        if (var18 == this.riddenByEntity || !var18.canBePushed() || !(var18 instanceof EntityBoat)) continue;
                        var18.applyEntityCollision(this);
                    }
                }
                if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
                    this.riddenByEntity = null;
                }
            }
        }
    }

    @Override
    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            double var1 = Math.cos((double)this.rotationYaw * Math.PI / 180.0) * 0.4;
            double var3 = Math.sin((double)this.rotationYaw * Math.PI / 180.0) * 0.4;
            this.riddenByEntity.setPosition(this.posX + var1, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + var3);
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {
    }

    @Override
    public boolean interactFirst(EntityPlayer playerIn) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != playerIn) {
            return true;
        }
        if (!this.worldObj.isRemote) {
            playerIn.mountEntity(this);
        }
        return true;
    }

    @Override
    protected void func_180433_a(double p_180433_1_, boolean p_180433_3_, Block p_180433_4_, BlockPos p_180433_5_) {
        if (p_180433_3_) {
            if (this.fallDistance > 3.0f) {
                this.fall(this.fallDistance, 1.0f);
                if (!this.worldObj.isRemote && !this.isDead) {
                    int var6;
                    this.setDead();
                    for (var6 = 0; var6 < 3; ++var6) {
                        this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0f);
                    }
                    for (var6 = 0; var6 < 2; ++var6) {
                        this.dropItemWithOffset(Items.stick, 1, 0.0f);
                    }
                }
                this.fallDistance = 0.0f;
            }
        } else if (this.worldObj.getBlockState(new BlockPos(this).offsetDown()).getBlock().getMaterial() != Material.water && p_180433_1_ < 0.0) {
            this.fallDistance = (float)((double)this.fallDistance - p_180433_1_);
        }
    }

    public void setDamageTaken(float p_70266_1_) {
        this.dataWatcher.updateObject(19, Float.valueOf(p_70266_1_));
    }

    public float getDamageTaken() {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    public void setTimeSinceHit(int p_70265_1_) {
        this.dataWatcher.updateObject(17, p_70265_1_);
    }

    public int getTimeSinceHit() {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    public void setForwardDirection(int p_70269_1_) {
        this.dataWatcher.updateObject(18, p_70269_1_);
    }

    public int getForwardDirection() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    public void setIsBoatEmpty(boolean p_70270_1_) {
        this.isBoatEmpty = p_70270_1_;
    }
}

