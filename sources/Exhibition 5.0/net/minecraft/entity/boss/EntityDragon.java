// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.boss;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Explosion;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.EntityLiving;

public class EntityDragon extends EntityLiving implements IBossDisplayData, IEntityMultiPart, IMob
{
    public double targetX;
    public double targetY;
    public double targetZ;
    public double[][] ringBuffer;
    public int ringBufferIndex;
    public EntityDragonPart[] dragonPartArray;
    public EntityDragonPart dragonPartHead;
    public EntityDragonPart dragonPartBody;
    public EntityDragonPart dragonPartTail1;
    public EntityDragonPart dragonPartTail2;
    public EntityDragonPart dragonPartTail3;
    public EntityDragonPart dragonPartWing1;
    public EntityDragonPart dragonPartWing2;
    public float prevAnimTime;
    public float animTime;
    public boolean forceNewTarget;
    public boolean slowed;
    private Entity target;
    public int deathTicks;
    public EntityEnderCrystal healingEnderCrystal;
    private static final String __OBFID = "CL_00001659";
    
    public EntityDragon(final World worldIn) {
        super(worldIn);
        this.ringBuffer = new double[64][3];
        this.ringBufferIndex = -1;
        this.dragonPartArray = new EntityDragonPart[] { this.dragonPartHead = new EntityDragonPart(this, "head", 6.0f, 6.0f), this.dragonPartBody = new EntityDragonPart(this, "body", 8.0f, 8.0f), this.dragonPartTail1 = new EntityDragonPart(this, "tail", 4.0f, 4.0f), this.dragonPartTail2 = new EntityDragonPart(this, "tail", 4.0f, 4.0f), this.dragonPartTail3 = new EntityDragonPart(this, "tail", 4.0f, 4.0f), this.dragonPartWing1 = new EntityDragonPart(this, "wing", 4.0f, 4.0f), this.dragonPartWing2 = new EntityDragonPart(this, "wing", 4.0f, 4.0f) };
        this.setHealth(this.getMaxHealth());
        this.setSize(16.0f, 8.0f);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.targetY = 100.0;
        this.ignoreFrustumCheck = true;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
    }
    
    public double[] getMovementOffsets(final int p_70974_1_, float p_70974_2_) {
        if (this.getHealth() <= 0.0f) {
            p_70974_2_ = 0.0f;
        }
        p_70974_2_ = 1.0f - p_70974_2_;
        final int var3 = this.ringBufferIndex - p_70974_1_ * 1 & 0x3F;
        final int var4 = this.ringBufferIndex - p_70974_1_ * 1 - 1 & 0x3F;
        final double[] var5 = new double[3];
        double var6 = this.ringBuffer[var3][0];
        double var7 = MathHelper.wrapAngleTo180_double(this.ringBuffer[var4][0] - var6);
        var5[0] = var6 + var7 * p_70974_2_;
        var6 = this.ringBuffer[var3][1];
        var7 = this.ringBuffer[var4][1] - var6;
        var5[1] = var6 + var7 * p_70974_2_;
        var5[2] = this.ringBuffer[var3][2] + (this.ringBuffer[var4][2] - this.ringBuffer[var3][2]) * p_70974_2_;
        return var5;
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            final float var1 = MathHelper.cos(this.animTime * 3.1415927f * 2.0f);
            final float var2 = MathHelper.cos(this.prevAnimTime * 3.1415927f * 2.0f);
            if (var2 <= -0.3f && var1 >= -0.3f && !this.isSlient()) {
                this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", 5.0f, 0.8f + this.rand.nextFloat() * 0.3f, false);
            }
        }
        this.prevAnimTime = this.animTime;
        if (this.getHealth() <= 0.0f) {
            final float var1 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            final float var2 = (this.rand.nextFloat() - 0.5f) * 4.0f;
            final float var3 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + var1, this.posY + 2.0 + var2, this.posZ + var3, 0.0, 0.0, 0.0, new int[0]);
        }
        else {
            this.updateDragonEnderCrystal();
            float var1 = 0.2f / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0f + 1.0f);
            var1 *= (float)Math.pow(2.0, this.motionY);
            if (this.slowed) {
                this.animTime += var1 * 0.5f;
            }
            else {
                this.animTime += var1;
            }
            this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
            if (this.ringBufferIndex < 0) {
                for (int var4 = 0; var4 < this.ringBuffer.length; ++var4) {
                    this.ringBuffer[var4][0] = this.rotationYaw;
                    this.ringBuffer[var4][1] = this.posY;
                }
            }
            if (++this.ringBufferIndex == this.ringBuffer.length) {
                this.ringBufferIndex = 0;
            }
            this.ringBuffer[this.ringBufferIndex][0] = this.rotationYaw;
            this.ringBuffer[this.ringBufferIndex][1] = this.posY;
            if (this.worldObj.isRemote) {
                if (this.newPosRotationIncrements > 0) {
                    final double var5 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
                    final double var6 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
                    final double var7 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
                    final double var8 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
                    this.rotationYaw += (float)(var8 / this.newPosRotationIncrements);
                    this.rotationPitch += (float)((this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements);
                    --this.newPosRotationIncrements;
                    this.setPosition(var5, var6, var7);
                    this.setRotation(this.rotationYaw, this.rotationPitch);
                }
            }
            else {
                final double var5 = this.targetX - this.posX;
                double var6 = this.targetY - this.posY;
                final double var7 = this.targetZ - this.posZ;
                final double var8 = var5 * var5 + var6 * var6 + var7 * var7;
                if (this.target != null) {
                    this.targetX = this.target.posX;
                    this.targetZ = this.target.posZ;
                    final double var9 = this.targetX - this.posX;
                    final double var10 = this.targetZ - this.posZ;
                    final double var11 = Math.sqrt(var9 * var9 + var10 * var10);
                    double var12 = 0.4000000059604645 + var11 / 80.0 - 1.0;
                    if (var12 > 10.0) {
                        var12 = 10.0;
                    }
                    this.targetY = this.target.getEntityBoundingBox().minY + var12;
                }
                else {
                    this.targetX += this.rand.nextGaussian() * 2.0;
                    this.targetZ += this.rand.nextGaussian() * 2.0;
                }
                if (this.forceNewTarget || var8 < 100.0 || var8 > 22500.0 || this.isCollidedHorizontally || this.isCollidedVertically) {
                    this.setNewTarget();
                }
                var6 /= MathHelper.sqrt_double(var5 * var5 + var7 * var7);
                final float var13 = 0.6f;
                var6 = MathHelper.clamp_double(var6, -var13, var13);
                this.motionY += var6 * 0.10000000149011612;
                this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
                final double var14 = 180.0 - Math.atan2(var5, var7) * 180.0 / 3.141592653589793;
                double var15 = MathHelper.wrapAngleTo180_double(var14 - this.rotationYaw);
                if (var15 > 50.0) {
                    var15 = 50.0;
                }
                if (var15 < -50.0) {
                    var15 = -50.0;
                }
                final Vec3 var16 = new Vec3(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ).normalize();
                double var12 = -MathHelper.cos(this.rotationYaw * 3.1415927f / 180.0f);
                final Vec3 var17 = new Vec3(MathHelper.sin(this.rotationYaw * 3.1415927f / 180.0f), this.motionY, var12).normalize();
                float var18 = ((float)var17.dotProduct(var16) + 0.5f) / 1.5f;
                if (var18 < 0.0f) {
                    var18 = 0.0f;
                }
                this.randomYawVelocity *= 0.8f;
                final float var19 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0f + 1.0f;
                double var20 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0 + 1.0;
                if (var20 > 40.0) {
                    var20 = 40.0;
                }
                this.randomYawVelocity += (float)(var15 * (0.699999988079071 / var20 / var19));
                this.rotationYaw += this.randomYawVelocity * 0.1f;
                final float var21 = (float)(2.0 / (var20 + 1.0));
                final float var22 = 0.06f;
                this.moveFlying(0.0f, -1.0f, var22 * (var18 * var21 + (1.0f - var21)));
                if (this.slowed) {
                    this.moveEntity(this.motionX * 0.800000011920929, this.motionY * 0.800000011920929, this.motionZ * 0.800000011920929);
                }
                else {
                    this.moveEntity(this.motionX, this.motionY, this.motionZ);
                }
                final Vec3 var23 = new Vec3(this.motionX, this.motionY, this.motionZ).normalize();
                float var24 = ((float)var23.dotProduct(var17) + 1.0f) / 2.0f;
                var24 = 0.8f + 0.15f * var24;
                this.motionX *= var24;
                this.motionZ *= var24;
                this.motionY *= 0.9100000262260437;
            }
            this.renderYawOffset = this.rotationYaw;
            final EntityDragonPart dragonPartHead = this.dragonPartHead;
            final EntityDragonPart dragonPartHead2 = this.dragonPartHead;
            final float n = 3.0f;
            dragonPartHead2.height = n;
            dragonPartHead.width = n;
            final EntityDragonPart dragonPartTail1 = this.dragonPartTail1;
            final EntityDragonPart dragonPartTail2 = this.dragonPartTail1;
            final float n2 = 2.0f;
            dragonPartTail2.height = n2;
            dragonPartTail1.width = n2;
            final EntityDragonPart dragonPartTail3 = this.dragonPartTail2;
            final EntityDragonPart dragonPartTail4 = this.dragonPartTail2;
            final float n3 = 2.0f;
            dragonPartTail4.height = n3;
            dragonPartTail3.width = n3;
            final EntityDragonPart dragonPartTail5 = this.dragonPartTail3;
            final EntityDragonPart dragonPartTail6 = this.dragonPartTail3;
            final float n4 = 2.0f;
            dragonPartTail6.height = n4;
            dragonPartTail5.width = n4;
            this.dragonPartBody.height = 3.0f;
            this.dragonPartBody.width = 5.0f;
            this.dragonPartWing1.height = 2.0f;
            this.dragonPartWing1.width = 4.0f;
            this.dragonPartWing2.height = 3.0f;
            this.dragonPartWing2.width = 4.0f;
            final float var2 = (float)(this.getMovementOffsets(5, 1.0f)[1] - this.getMovementOffsets(10, 1.0f)[1]) * 10.0f / 180.0f * 3.1415927f;
            final float var3 = MathHelper.cos(var2);
            final float var25 = -MathHelper.sin(var2);
            final float var26 = this.rotationYaw * 3.1415927f / 180.0f;
            final float var27 = MathHelper.sin(var26);
            final float var28 = MathHelper.cos(var26);
            this.dragonPartBody.onUpdate();
            this.dragonPartBody.setLocationAndAngles(this.posX + var27 * 0.5f, this.posY, this.posZ - var28 * 0.5f, 0.0f, 0.0f);
            this.dragonPartWing1.onUpdate();
            this.dragonPartWing1.setLocationAndAngles(this.posX + var28 * 4.5f, this.posY + 2.0, this.posZ + var27 * 4.5f, 0.0f, 0.0f);
            this.dragonPartWing2.onUpdate();
            this.dragonPartWing2.setLocationAndAngles(this.posX - var28 * 4.5f, this.posY + 2.0, this.posZ - var27 * 4.5f, 0.0f, 0.0f);
            if (!this.worldObj.isRemote && this.hurtTime == 0) {
                this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().expand(1.0, 1.0, 1.0)));
            }
            final double[] var29 = this.getMovementOffsets(5, 1.0f);
            final double[] var30 = this.getMovementOffsets(0, 1.0f);
            final float var13 = MathHelper.sin(this.rotationYaw * 3.1415927f / 180.0f - this.randomYawVelocity * 0.01f);
            final float var31 = MathHelper.cos(this.rotationYaw * 3.1415927f / 180.0f - this.randomYawVelocity * 0.01f);
            this.dragonPartHead.onUpdate();
            this.dragonPartHead.setLocationAndAngles(this.posX + var13 * 5.5f * var3, this.posY + (var30[1] - var29[1]) * 1.0 + var25 * 5.5f, this.posZ - var31 * 5.5f * var3, 0.0f, 0.0f);
            for (int var32 = 0; var32 < 3; ++var32) {
                EntityDragonPart var33 = null;
                if (var32 == 0) {
                    var33 = this.dragonPartTail1;
                }
                if (var32 == 1) {
                    var33 = this.dragonPartTail2;
                }
                if (var32 == 2) {
                    var33 = this.dragonPartTail3;
                }
                final double[] var34 = this.getMovementOffsets(12 + var32 * 2, 1.0f);
                final float var35 = this.rotationYaw * 3.1415927f / 180.0f + this.simplifyAngle(var34[0] - var29[0]) * 3.1415927f / 180.0f * 1.0f;
                final float var36 = MathHelper.sin(var35);
                final float var37 = MathHelper.cos(var35);
                final float var38 = 1.5f;
                final float var39 = (var32 + 1) * 2.0f;
                var33.onUpdate();
                var33.setLocationAndAngles(this.posX - (var27 * var38 + var36 * var39) * var3, this.posY + (var34[1] - var29[1]) * 1.0 - (var39 + var38) * var25 + 1.5, this.posZ + (var28 * var38 + var37 * var39) * var3, 0.0f, 0.0f);
            }
            if (!this.worldObj.isRemote) {
                this.slowed = (this.destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox()));
            }
        }
    }
    
    private void updateDragonEnderCrystal() {
        if (this.healingEnderCrystal != null) {
            if (this.healingEnderCrystal.isDead) {
                if (!this.worldObj.isRemote) {
                    this.attackEntityFromPart(this.dragonPartHead, DamageSource.setExplosionSource(null), 10.0f);
                }
                this.healingEnderCrystal = null;
            }
            else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
                this.setHealth(this.getHealth() + 1.0f);
            }
        }
        if (this.rand.nextInt(10) == 0) {
            final float var1 = 32.0f;
            final List var2 = this.worldObj.getEntitiesWithinAABB(EntityEnderCrystal.class, this.getEntityBoundingBox().expand(var1, var1, var1));
            EntityEnderCrystal var3 = null;
            double var4 = Double.MAX_VALUE;
            for (final EntityEnderCrystal var6 : var2) {
                final double var7 = var6.getDistanceSqToEntity(this);
                if (var7 < var4) {
                    var4 = var7;
                    var3 = var6;
                }
            }
            this.healingEnderCrystal = var3;
        }
    }
    
    private void collideWithEntities(final List p_70970_1_) {
        final double var2 = (this.dragonPartBody.getEntityBoundingBox().minX + this.dragonPartBody.getEntityBoundingBox().maxX) / 2.0;
        final double var3 = (this.dragonPartBody.getEntityBoundingBox().minZ + this.dragonPartBody.getEntityBoundingBox().maxZ) / 2.0;
        for (final Entity var5 : p_70970_1_) {
            if (var5 instanceof EntityLivingBase) {
                final double var6 = var5.posX - var2;
                final double var7 = var5.posZ - var3;
                final double var8 = var6 * var6 + var7 * var7;
                var5.addVelocity(var6 / var8 * 4.0, 0.20000000298023224, var7 / var8 * 4.0);
            }
        }
    }
    
    private void attackEntitiesInList(final List p_70971_1_) {
        for (int var2 = 0; var2 < p_70971_1_.size(); ++var2) {
            final Entity var3 = p_70971_1_.get(var2);
            if (var3 instanceof EntityLivingBase) {
                var3.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0f);
                this.func_174815_a(this, var3);
            }
        }
    }
    
    private void setNewTarget() {
        this.forceNewTarget = false;
        final ArrayList var1 = Lists.newArrayList((Iterable)this.worldObj.playerEntities);
        final Iterator var2 = var1.iterator();
        while (var2.hasNext()) {
            if (var2.next().func_175149_v()) {
                var2.remove();
            }
        }
        if (this.rand.nextInt(2) == 0 && !var1.isEmpty()) {
            this.target = var1.get(this.rand.nextInt(var1.size()));
        }
        else {
            boolean var3;
            do {
                this.targetX = 0.0;
                this.targetY = 70.0f + this.rand.nextFloat() * 50.0f;
                this.targetZ = 0.0;
                this.targetX += this.rand.nextFloat() * 120.0f - 60.0f;
                this.targetZ += this.rand.nextFloat() * 120.0f - 60.0f;
                final double var4 = this.posX - this.targetX;
                final double var5 = this.posY - this.targetY;
                final double var6 = this.posZ - this.targetZ;
                var3 = (var4 * var4 + var5 * var5 + var6 * var6 > 100.0);
            } while (!var3);
            this.target = null;
        }
    }
    
    private float simplifyAngle(final double p_70973_1_) {
        return (float)MathHelper.wrapAngleTo180_double(p_70973_1_);
    }
    
    private boolean destroyBlocksInAABB(final AxisAlignedBB p_70972_1_) {
        final int var2 = MathHelper.floor_double(p_70972_1_.minX);
        final int var3 = MathHelper.floor_double(p_70972_1_.minY);
        final int var4 = MathHelper.floor_double(p_70972_1_.minZ);
        final int var5 = MathHelper.floor_double(p_70972_1_.maxX);
        final int var6 = MathHelper.floor_double(p_70972_1_.maxY);
        final int var7 = MathHelper.floor_double(p_70972_1_.maxZ);
        boolean var8 = false;
        boolean var9 = false;
        for (int var10 = var2; var10 <= var5; ++var10) {
            for (int var11 = var3; var11 <= var6; ++var11) {
                for (int var12 = var4; var12 <= var7; ++var12) {
                    final Block var13 = this.worldObj.getBlockState(new BlockPos(var10, var11, var12)).getBlock();
                    if (var13.getMaterial() != Material.air) {
                        if (var13 != Blocks.barrier && var13 != Blocks.obsidian && var13 != Blocks.end_stone && var13 != Blocks.bedrock && var13 != Blocks.command_block && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                            var9 = (this.worldObj.setBlockToAir(new BlockPos(var10, var11, var12)) || var9);
                        }
                        else {
                            var8 = true;
                        }
                    }
                }
            }
        }
        if (var9) {
            final double var14 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * this.rand.nextFloat();
            final double var15 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * this.rand.nextFloat();
            final double var16 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * this.rand.nextFloat();
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, var14, var15, var16, 0.0, 0.0, 0.0, new int[0]);
        }
        return var8;
    }
    
    @Override
    public boolean attackEntityFromPart(final EntityDragonPart p_70965_1_, final DamageSource p_70965_2_, float p_70965_3_) {
        if (p_70965_1_ != this.dragonPartHead) {
            p_70965_3_ = p_70965_3_ / 4.0f + 1.0f;
        }
        final float var4 = this.rotationYaw * 3.1415927f / 180.0f;
        final float var5 = MathHelper.sin(var4);
        final float var6 = MathHelper.cos(var4);
        this.targetX = this.posX + var5 * 5.0f + (this.rand.nextFloat() - 0.5f) * 2.0f;
        this.targetY = this.posY + this.rand.nextFloat() * 3.0f + 1.0;
        this.targetZ = this.posZ - var6 * 5.0f + (this.rand.nextFloat() - 0.5f) * 2.0f;
        this.target = null;
        if (p_70965_2_.getEntity() instanceof EntityPlayer || p_70965_2_.isExplosion()) {
            this.func_82195_e(p_70965_2_, p_70965_3_);
        }
        return true;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (source instanceof EntityDamageSource && ((EntityDamageSource)source).func_180139_w()) {
            this.func_82195_e(source, amount);
        }
        return false;
    }
    
    protected boolean func_82195_e(final DamageSource p_82195_1_, final float p_82195_2_) {
        return super.attackEntityFrom(p_82195_1_, p_82195_2_);
    }
    
    @Override
    public void func_174812_G() {
        this.setDead();
    }
    
    @Override
    protected void onDeathUpdate() {
        ++this.deathTicks;
        if (this.deathTicks >= 180 && this.deathTicks <= 200) {
            final float var1 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            final float var2 = (this.rand.nextFloat() - 0.5f) * 4.0f;
            final float var3 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + var1, this.posY + 2.0 + var2, this.posZ + var3, 0.0, 0.0, 0.0, new int[0]);
        }
        if (!this.worldObj.isRemote) {
            if (this.deathTicks > 150 && this.deathTicks % 5 == 0 && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
                int var4 = 1000;
                while (var4 > 0) {
                    final int var5 = EntityXPOrb.getXPSplit(var4);
                    var4 -= var5;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var5));
                }
            }
            if (this.deathTicks == 1) {
                this.worldObj.func_175669_a(1018, new BlockPos(this), 0);
            }
        }
        this.moveEntity(0.0, 0.10000000149011612, 0.0);
        final float n = this.rotationYaw + 20.0f;
        this.rotationYaw = n;
        this.renderYawOffset = n;
        if (this.deathTicks == 200 && !this.worldObj.isRemote) {
            int var4 = 2000;
            while (var4 > 0) {
                final int var5 = EntityXPOrb.getXPSplit(var4);
                var4 -= var5;
                this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var5));
            }
            this.func_175499_a(new BlockPos(this.posX, 64.0, this.posZ));
            this.setDead();
        }
    }
    
    private void func_175499_a(final BlockPos p_175499_1_) {
        final boolean var2 = true;
        final double var3 = 12.25;
        final double var4 = 6.25;
        for (int var5 = -1; var5 <= 32; ++var5) {
            for (int var6 = -4; var6 <= 4; ++var6) {
                for (int var7 = -4; var7 <= 4; ++var7) {
                    final double var8 = var6 * var6 + var7 * var7;
                    if (var8 <= 12.25) {
                        final BlockPos var9 = p_175499_1_.add(var6, var5, var7);
                        if (var5 < 0) {
                            if (var8 <= 6.25) {
                                this.worldObj.setBlockState(var9, Blocks.bedrock.getDefaultState());
                            }
                        }
                        else if (var5 > 0) {
                            this.worldObj.setBlockState(var9, Blocks.air.getDefaultState());
                        }
                        else if (var8 > 6.25) {
                            this.worldObj.setBlockState(var9, Blocks.bedrock.getDefaultState());
                        }
                        else {
                            this.worldObj.setBlockState(var9, Blocks.end_portal.getDefaultState());
                        }
                    }
                }
            }
        }
        this.worldObj.setBlockState(p_175499_1_, Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(p_175499_1_.offsetUp(), Blocks.bedrock.getDefaultState());
        final BlockPos var10 = p_175499_1_.offsetUp(2);
        this.worldObj.setBlockState(var10, Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(var10.offsetWest(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, EnumFacing.EAST));
        this.worldObj.setBlockState(var10.offsetEast(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, EnumFacing.WEST));
        this.worldObj.setBlockState(var10.offsetNorth(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, EnumFacing.SOUTH));
        this.worldObj.setBlockState(var10.offsetSouth(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, EnumFacing.NORTH));
        this.worldObj.setBlockState(p_175499_1_.offsetUp(3), Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(p_175499_1_.offsetUp(4), Blocks.dragon_egg.getDefaultState());
    }
    
    @Override
    protected void despawnEntity() {
    }
    
    @Override
    public Entity[] getParts() {
        return this.dragonPartArray;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    
    @Override
    public World func_82194_d() {
        return this.worldObj;
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.enderdragon.growl";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.enderdragon.hit";
    }
    
    @Override
    protected float getSoundVolume() {
        return 5.0f;
    }
}
