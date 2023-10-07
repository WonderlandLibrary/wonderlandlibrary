// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.item;

import net.minecraft.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;

public class EntityBoat extends Entity
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
    
    public EntityBoat(final World worldIn) {
        super(worldIn);
        this.isBoatEmpty = true;
        this.speedMultiplier = 0.07;
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
    public AxisAlignedBB getCollisionBox(final Entity entityIn) {
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
    
    public EntityBoat(final World worldIn, final double p_i1705_2_, final double p_i1705_4_, final double p_i1705_6_) {
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
        return this.height * 0.0 - 0.30000001192092896;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        if (this.worldObj.isRemote || this.isDead) {
            return true;
        }
        if (this.riddenByEntity != null && this.riddenByEntity == source.getEntity() && source instanceof EntityDamageSourceIndirect) {
            return false;
        }
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() + amount * 10.0f);
        this.setBeenAttacked();
        final boolean var3 = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;
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
    public void func_180426_a(final double p_180426_1_, final double p_180426_3_, final double p_180426_5_, final float p_180426_7_, final float p_180426_8_, final int p_180426_9_, final boolean p_180426_10_) {
        if (p_180426_10_ && this.riddenByEntity != null) {
            this.posX = p_180426_1_;
            this.prevPosX = p_180426_1_;
            this.posY = p_180426_3_;
            this.prevPosY = p_180426_3_;
            this.posZ = p_180426_5_;
            this.prevPosZ = p_180426_5_;
            this.rotationYaw = p_180426_7_;
            this.rotationPitch = p_180426_8_;
            this.boatPosRotationIncrements = 0;
            this.setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
            final double n = 0.0;
            this.velocityX = n;
            this.motionX = n;
            final double n2 = 0.0;
            this.velocityY = n2;
            this.motionY = n2;
            final double n3 = 0.0;
            this.velocityZ = n3;
            this.motionZ = n3;
        }
        else {
            if (this.isBoatEmpty) {
                this.boatPosRotationIncrements = p_180426_9_ + 5;
            }
            else {
                final double var11 = p_180426_1_ - this.posX;
                final double var12 = p_180426_3_ - this.posY;
                final double var13 = p_180426_5_ - this.posZ;
                final double var14 = var11 * var11 + var12 * var12 + var13 * var13;
                if (var14 <= 1.0) {
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
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.velocityX = x;
        this.motionY = y;
        this.velocityY = y;
        this.motionZ = z;
        this.velocityZ = z;
    }
    
    @Override
    public void onUpdate() {
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
        final byte var1 = 5;
        double var2 = 0.0;
        for (int var3 = 0; var3 < var1; ++var3) {
            final double var4 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (var3 + 0) / var1 - 0.125;
            final double var5 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (var3 + 1) / var1 - 0.125;
            final AxisAlignedBB var6 = new AxisAlignedBB(this.getEntityBoundingBox().minX, var4, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().maxX, var5, this.getEntityBoundingBox().maxZ);
            if (this.worldObj.isAABBInMaterial(var6, Material.water)) {
                var2 += 1.0 / var1;
            }
        }
        final double var7 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        if (var7 > 0.2975) {
            final double var8 = Math.cos(this.rotationYaw * 3.141592653589793 / 180.0);
            final double var9 = Math.sin(this.rotationYaw * 3.141592653589793 / 180.0);
            for (int var10 = 0; var10 < 1.0 + var7 * 60.0; ++var10) {
                final double var11 = this.rand.nextFloat() * 2.0f - 1.0f;
                final double var12 = (this.rand.nextInt(2) * 2 - 1) * 0.7;
                if (this.rand.nextBoolean()) {
                    final double var13 = this.posX - var8 * var11 * 0.8 + var9 * var12;
                    final double var14 = this.posZ - var9 * var11 * 0.8 - var8 * var12;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, var13, this.posY - 0.125, var14, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
                else {
                    final double var13 = this.posX + var8 + var9 * var11 * 0.7;
                    final double var14 = this.posZ + var9 - var8 * var11 * 0.7;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, var13, this.posY - 0.125, var14, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
            }
        }
        if (this.worldObj.isRemote && this.isBoatEmpty) {
            if (this.boatPosRotationIncrements > 0) {
                final double var8 = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
                final double var9 = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
                final double var15 = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
                final double var16 = MathHelper.wrapAngleTo180_double(this.boatYaw - this.rotationYaw);
                this.rotationYaw += (float)(var16 / this.boatPosRotationIncrements);
                this.rotationPitch += (float)((this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPosition(var8, var9, var15);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else {
                final double var8 = this.posX + this.motionX;
                final double var9 = this.posY + this.motionY;
                final double var15 = this.posZ + this.motionZ;
                this.setPosition(var8, var9, var15);
                if (this.onGround) {
                    this.motionX *= 0.5;
                    this.motionY *= 0.5;
                    this.motionZ *= 0.5;
                }
                this.motionX *= 0.9900000095367432;
                this.motionY *= 0.949999988079071;
                this.motionZ *= 0.9900000095367432;
            }
        }
        else {
            if (var2 < 1.0) {
                final double var8 = var2 * 2.0 - 1.0;
                this.motionY += 0.03999999910593033 * var8;
            }
            else {
                if (this.motionY < 0.0) {
                    this.motionY /= 2.0;
                }
                this.motionY += 0.007000000216066837;
            }
            if (this.riddenByEntity instanceof EntityLivingBase) {
                final EntityLivingBase var17 = (EntityLivingBase)this.riddenByEntity;
                final float var18 = this.riddenByEntity.rotationYaw + -var17.moveStrafing * 90.0f;
                this.motionX += -Math.sin(var18 * 3.1415927f / 180.0f) * this.speedMultiplier * var17.moveForward * 0.05000000074505806;
                this.motionZ += Math.cos(var18 * 3.1415927f / 180.0f) * this.speedMultiplier * var17.moveForward * 0.05000000074505806;
            }
            double var8 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (var8 > 0.35) {
                final double var9 = 0.35 / var8;
                this.motionX *= var9;
                this.motionZ *= var9;
                var8 = 0.35;
            }
            if (var8 > var7 && this.speedMultiplier < 0.35) {
                this.speedMultiplier += (0.35 - this.speedMultiplier) / 35.0;
                if (this.speedMultiplier > 0.35) {
                    this.speedMultiplier = 0.35;
                }
            }
            else {
                this.speedMultiplier -= (this.speedMultiplier - 0.07) / 35.0;
                if (this.speedMultiplier < 0.07) {
                    this.speedMultiplier = 0.07;
                }
            }
            for (int var19 = 0; var19 < 4; ++var19) {
                final int var20 = MathHelper.floor_double(this.posX + (var19 % 2 - 0.5) * 0.8);
                final int var10 = MathHelper.floor_double(this.posZ + (var19 / 2 - 0.5) * 0.8);
                for (int var21 = 0; var21 < 2; ++var21) {
                    final int var22 = MathHelper.floor_double(this.posY) + var21;
                    final BlockPos var23 = new BlockPos(var20, var22, var10);
                    final Block var24 = this.worldObj.getBlockState(var23).getBlock();
                    if (var24 == Blocks.snow_layer) {
                        this.worldObj.setBlockToAir(var23);
                        this.isCollidedHorizontally = false;
                    }
                    else if (var24 == Blocks.waterlily) {
                        this.worldObj.destroyBlock(var23, true);
                        this.isCollidedHorizontally = false;
                    }
                }
            }
            if (this.onGround) {
                this.motionX *= 0.5;
                this.motionY *= 0.5;
                this.motionZ *= 0.5;
            }
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            if (this.isCollidedHorizontally && var7 > 0.2) {
                if (!this.worldObj.isRemote && !this.isDead) {
                    this.setDead();
                    for (int var19 = 0; var19 < 3; ++var19) {
                        this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0f);
                    }
                    for (int var19 = 0; var19 < 2; ++var19) {
                        this.dropItemWithOffset(Items.stick, 1, 0.0f);
                    }
                }
            }
            else {
                this.motionX *= 0.9900000095367432;
                this.motionY *= 0.949999988079071;
                this.motionZ *= 0.9900000095367432;
            }
            this.rotationPitch = 0.0f;
            double var9 = this.rotationYaw;
            final double var15 = this.prevPosX - this.posX;
            final double var16 = this.prevPosZ - this.posZ;
            if (var15 * var15 + var16 * var16 > 0.001) {
                var9 = (float)(Math.atan2(var16, var15) * 180.0 / 3.141592653589793);
            }
            double var25 = MathHelper.wrapAngleTo180_double(var9 - this.rotationYaw);
            if (var25 > 20.0) {
                var25 = 20.0;
            }
            if (var25 < -20.0) {
                var25 = -20.0;
            }
            this.setRotation(this.rotationYaw += (float)var25, this.rotationPitch);
            if (!this.worldObj.isRemote) {
                final List var26 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224, 0.0, 0.20000000298023224));
                if (var26 != null && !var26.isEmpty()) {
                    for (int var27 = 0; var27 < var26.size(); ++var27) {
                        final Entity var28 = var26.get(var27);
                        if (var28 != this.riddenByEntity && var28.canBePushed() && var28 instanceof EntityBoat) {
                            var28.applyEntityCollision(this);
                        }
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
            final double var1 = Math.cos(this.rotationYaw * 3.141592653589793 / 180.0) * 0.4;
            final double var2 = Math.sin(this.rotationYaw * 3.141592653589793 / 180.0) * 0.4;
            this.riddenByEntity.setPosition(this.posX + var1, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + var2);
        }
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
    }
    
    @Override
    public boolean interactFirst(final EntityPlayer playerIn) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != playerIn) {
            return true;
        }
        if (!this.worldObj.isRemote) {
            playerIn.mountEntity(this);
        }
        return true;
    }
    
    @Override
    protected void func_180433_a(final double p_180433_1_, final boolean p_180433_3_, final Block p_180433_4_, final BlockPos p_180433_5_) {
        if (p_180433_3_) {
            if (this.fallDistance > 3.0f) {
                this.fall(this.fallDistance, 1.0f);
                if (!this.worldObj.isRemote && !this.isDead) {
                    this.setDead();
                    for (int var6 = 0; var6 < 3; ++var6) {
                        this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0f);
                    }
                    for (int var6 = 0; var6 < 2; ++var6) {
                        this.dropItemWithOffset(Items.stick, 1, 0.0f);
                    }
                }
                this.fallDistance = 0.0f;
            }
        }
        else if (this.worldObj.getBlockState(new BlockPos(this).offsetDown()).getBlock().getMaterial() != Material.water && p_180433_1_ < 0.0) {
            this.fallDistance -= (float)p_180433_1_;
        }
    }
    
    public void setDamageTaken(final float p_70266_1_) {
        this.dataWatcher.updateObject(19, p_70266_1_);
    }
    
    public float getDamageTaken() {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }
    
    public void setTimeSinceHit(final int p_70265_1_) {
        this.dataWatcher.updateObject(17, p_70265_1_);
    }
    
    public int getTimeSinceHit() {
        return this.dataWatcher.getWatchableObjectInt(17);
    }
    
    public void setForwardDirection(final int p_70269_1_) {
        this.dataWatcher.updateObject(18, p_70269_1_);
    }
    
    public int getForwardDirection() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }
    
    public void setIsBoatEmpty(final boolean p_70270_1_) {
        this.isBoatEmpty = p_70270_1_;
    }
}
