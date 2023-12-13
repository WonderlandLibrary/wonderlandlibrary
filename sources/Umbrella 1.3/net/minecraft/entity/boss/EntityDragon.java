/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.entity.boss;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityDragon
extends EntityLiving
implements IBossDisplayData,
IEntityMultiPart,
IMob {
    public double targetX;
    public double targetY;
    public double targetZ;
    public double[][] ringBuffer = new double[64][3];
    public int ringBufferIndex = -1;
    public EntityDragonPart[] dragonPartArray;
    public EntityDragonPart dragonPartHead = new EntityDragonPart(this, "head", 6.0f, 6.0f);
    public EntityDragonPart dragonPartBody = new EntityDragonPart(this, "body", 8.0f, 8.0f);
    public EntityDragonPart dragonPartTail1 = new EntityDragonPart(this, "tail", 4.0f, 4.0f);
    public EntityDragonPart dragonPartTail2 = new EntityDragonPart(this, "tail", 4.0f, 4.0f);
    public EntityDragonPart dragonPartTail3 = new EntityDragonPart(this, "tail", 4.0f, 4.0f);
    public EntityDragonPart dragonPartWing1 = new EntityDragonPart(this, "wing", 4.0f, 4.0f);
    public EntityDragonPart dragonPartWing2 = new EntityDragonPart(this, "wing", 4.0f, 4.0f);
    public float prevAnimTime;
    public float animTime;
    public boolean forceNewTarget;
    public boolean slowed;
    private Entity target;
    public int deathTicks;
    public EntityEnderCrystal healingEnderCrystal;
    private static final String __OBFID = "CL_00001659";

    public EntityDragon(World worldIn) {
        super(worldIn);
        this.dragonPartArray = new EntityDragonPart[]{this.dragonPartHead, this.dragonPartBody, this.dragonPartTail1, this.dragonPartTail2, this.dragonPartTail3, this.dragonPartWing1, this.dragonPartWing2};
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

    public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_) {
        if (this.getHealth() <= 0.0f) {
            p_70974_2_ = 0.0f;
        }
        p_70974_2_ = 1.0f - p_70974_2_;
        int var3 = this.ringBufferIndex - p_70974_1_ * 1 & 0x3F;
        int var4 = this.ringBufferIndex - p_70974_1_ * 1 - 1 & 0x3F;
        double[] var5 = new double[3];
        double var6 = this.ringBuffer[var3][0];
        double var8 = MathHelper.wrapAngleTo180_double(this.ringBuffer[var4][0] - var6);
        var5[0] = var6 + var8 * (double)p_70974_2_;
        var6 = this.ringBuffer[var3][1];
        var8 = this.ringBuffer[var4][1] - var6;
        var5[1] = var6 + var8 * (double)p_70974_2_;
        var5[2] = this.ringBuffer[var3][2] + (this.ringBuffer[var4][2] - this.ringBuffer[var3][2]) * (double)p_70974_2_;
        return var5;
    }

    @Override
    public void onLivingUpdate() {
        float var2;
        float var1;
        if (this.worldObj.isRemote) {
            var1 = MathHelper.cos(this.animTime * (float)Math.PI * 2.0f);
            var2 = MathHelper.cos(this.prevAnimTime * (float)Math.PI * 2.0f);
            if (var2 <= -0.3f && var1 >= -0.3f && !this.isSlient()) {
                this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", 5.0f, 0.8f + this.rand.nextFloat() * 0.3f, false);
            }
        }
        this.prevAnimTime = this.animTime;
        if (this.getHealth() <= 0.0f) {
            var1 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            var2 = (this.rand.nextFloat() - 0.5f) * 4.0f;
            float var3 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + (double)var1, this.posY + 2.0 + (double)var2, this.posZ + (double)var3, 0.0, 0.0, 0.0, new int[0]);
        } else {
            float var33;
            double var8;
            double var6;
            double var28;
            this.updateDragonEnderCrystal();
            var1 = 0.2f / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0f + 1.0f);
            this.animTime = this.slowed ? (this.animTime += var1 * 0.5f) : (this.animTime += (var1 *= (float)Math.pow(2.0, this.motionY)));
            this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
            if (this.ringBufferIndex < 0) {
                for (int var27 = 0; var27 < this.ringBuffer.length; ++var27) {
                    this.ringBuffer[var27][0] = this.rotationYaw;
                    this.ringBuffer[var27][1] = this.posY;
                }
            }
            if (++this.ringBufferIndex == this.ringBuffer.length) {
                this.ringBufferIndex = 0;
            }
            this.ringBuffer[this.ringBufferIndex][0] = this.rotationYaw;
            this.ringBuffer[this.ringBufferIndex][1] = this.posY;
            if (this.worldObj.isRemote) {
                if (this.newPosRotationIncrements > 0) {
                    var28 = this.posX + (this.newPosX - this.posX) / (double)this.newPosRotationIncrements;
                    double var4 = this.posY + (this.newPosY - this.posY) / (double)this.newPosRotationIncrements;
                    var6 = this.posZ + (this.newPosZ - this.posZ) / (double)this.newPosRotationIncrements;
                    var8 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - (double)this.rotationYaw);
                    this.rotationYaw = (float)((double)this.rotationYaw + var8 / (double)this.newPosRotationIncrements);
                    this.rotationPitch = (float)((double)this.rotationPitch + (this.newRotationPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                    --this.newPosRotationIncrements;
                    this.setPosition(var28, var4, var6);
                    this.setRotation(this.rotationYaw, this.rotationPitch);
                }
            } else {
                double var16;
                var28 = this.targetX - this.posX;
                double var4 = this.targetY - this.posY;
                var6 = this.targetZ - this.posZ;
                var8 = var28 * var28 + var4 * var4 + var6 * var6;
                if (this.target != null) {
                    this.targetX = this.target.posX;
                    this.targetZ = this.target.posZ;
                    double var10 = this.targetX - this.posX;
                    double var12 = this.targetZ - this.posZ;
                    double var14 = Math.sqrt(var10 * var10 + var12 * var12);
                    var16 = (double)0.4f + var14 / 80.0 - 1.0;
                    if (var16 > 10.0) {
                        var16 = 10.0;
                    }
                    this.targetY = this.target.getEntityBoundingBox().minY + var16;
                } else {
                    this.targetX += this.rand.nextGaussian() * 2.0;
                    this.targetZ += this.rand.nextGaussian() * 2.0;
                }
                if (this.forceNewTarget || var8 < 100.0 || var8 > 22500.0 || this.isCollidedHorizontally || this.isCollidedVertically) {
                    this.setNewTarget();
                }
                var4 /= (double)MathHelper.sqrt_double(var28 * var28 + var6 * var6);
                var33 = 0.6f;
                var4 = MathHelper.clamp_double(var4, -var33, var33);
                this.motionY += var4 * (double)0.1f;
                this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
                double var11 = 180.0 - Math.atan2(var28, var6) * 180.0 / Math.PI;
                double var13 = MathHelper.wrapAngleTo180_double(var11 - (double)this.rotationYaw);
                if (var13 > 50.0) {
                    var13 = 50.0;
                }
                if (var13 < -50.0) {
                    var13 = -50.0;
                }
                Vec3 var15 = new Vec3(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ).normalize();
                var16 = -MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f);
                Vec3 var18 = new Vec3(MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f), this.motionY, var16).normalize();
                float var19 = ((float)var18.dotProduct(var15) + 0.5f) / 1.5f;
                if (var19 < 0.0f) {
                    var19 = 0.0f;
                }
                this.randomYawVelocity *= 0.8f;
                float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0f + 1.0f;
                double var21 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0 + 1.0;
                if (var21 > 40.0) {
                    var21 = 40.0;
                }
                this.randomYawVelocity = (float)((double)this.randomYawVelocity + var13 * ((double)0.7f / var21 / (double)var20));
                this.rotationYaw += this.randomYawVelocity * 0.1f;
                float var23 = (float)(2.0 / (var21 + 1.0));
                float var24 = 0.06f;
                this.moveFlying(0.0f, -1.0f, var24 * (var19 * var23 + (1.0f - var23)));
                if (this.slowed) {
                    this.moveEntity(this.motionX * (double)0.8f, this.motionY * (double)0.8f, this.motionZ * (double)0.8f);
                } else {
                    this.moveEntity(this.motionX, this.motionY, this.motionZ);
                }
                Vec3 var25 = new Vec3(this.motionX, this.motionY, this.motionZ).normalize();
                float var26 = ((float)var25.dotProduct(var18) + 1.0f) / 2.0f;
                var26 = 0.8f + 0.15f * var26;
                this.motionX *= (double)var26;
                this.motionZ *= (double)var26;
                this.motionY *= (double)0.91f;
            }
            this.renderYawOffset = this.rotationYaw;
            this.dragonPartHead.height = 3.0f;
            this.dragonPartHead.width = 3.0f;
            this.dragonPartTail1.height = 2.0f;
            this.dragonPartTail1.width = 2.0f;
            this.dragonPartTail2.height = 2.0f;
            this.dragonPartTail2.width = 2.0f;
            this.dragonPartTail3.height = 2.0f;
            this.dragonPartTail3.width = 2.0f;
            this.dragonPartBody.height = 3.0f;
            this.dragonPartBody.width = 5.0f;
            this.dragonPartWing1.height = 2.0f;
            this.dragonPartWing1.width = 4.0f;
            this.dragonPartWing2.height = 3.0f;
            this.dragonPartWing2.width = 4.0f;
            var2 = (float)(this.getMovementOffsets(5, 1.0f)[1] - this.getMovementOffsets(10, 1.0f)[1]) * 10.0f / 180.0f * (float)Math.PI;
            float var3 = MathHelper.cos(var2);
            float var29 = -MathHelper.sin(var2);
            float var5 = this.rotationYaw * (float)Math.PI / 180.0f;
            float var30 = MathHelper.sin(var5);
            float var7 = MathHelper.cos(var5);
            this.dragonPartBody.onUpdate();
            this.dragonPartBody.setLocationAndAngles(this.posX + (double)(var30 * 0.5f), this.posY, this.posZ - (double)(var7 * 0.5f), 0.0f, 0.0f);
            this.dragonPartWing1.onUpdate();
            this.dragonPartWing1.setLocationAndAngles(this.posX + (double)(var7 * 4.5f), this.posY + 2.0, this.posZ + (double)(var30 * 4.5f), 0.0f, 0.0f);
            this.dragonPartWing2.onUpdate();
            this.dragonPartWing2.setLocationAndAngles(this.posX - (double)(var7 * 4.5f), this.posY + 2.0, this.posZ - (double)(var30 * 4.5f), 0.0f, 0.0f);
            if (!this.worldObj.isRemote && this.hurtTime == 0) {
                this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().expand(1.0, 1.0, 1.0)));
            }
            double[] var31 = this.getMovementOffsets(5, 1.0f);
            double[] var9 = this.getMovementOffsets(0, 1.0f);
            var33 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f - this.randomYawVelocity * 0.01f);
            float var35 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f - this.randomYawVelocity * 0.01f);
            this.dragonPartHead.onUpdate();
            this.dragonPartHead.setLocationAndAngles(this.posX + (double)(var33 * 5.5f * var3), this.posY + (var9[1] - var31[1]) * 1.0 + (double)(var29 * 5.5f), this.posZ - (double)(var35 * 5.5f * var3), 0.0f, 0.0f);
            for (int var32 = 0; var32 < 3; ++var32) {
                EntityDragonPart var34 = null;
                if (var32 == 0) {
                    var34 = this.dragonPartTail1;
                }
                if (var32 == 1) {
                    var34 = this.dragonPartTail2;
                }
                if (var32 == 2) {
                    var34 = this.dragonPartTail3;
                }
                double[] var36 = this.getMovementOffsets(12 + var32 * 2, 1.0f);
                float var37 = this.rotationYaw * (float)Math.PI / 180.0f + this.simplifyAngle(var36[0] - var31[0]) * (float)Math.PI / 180.0f * 1.0f;
                float var38 = MathHelper.sin(var37);
                float var39 = MathHelper.cos(var37);
                float var40 = 1.5f;
                float var41 = (float)(var32 + 1) * 2.0f;
                var34.onUpdate();
                var34.setLocationAndAngles(this.posX - (double)((var30 * var40 + var38 * var41) * var3), this.posY + (var36[1] - var31[1]) * 1.0 - (double)((var41 + var40) * var29) + 1.5, this.posZ + (double)((var7 * var40 + var39 * var41) * var3), 0.0f, 0.0f);
            }
            if (!this.worldObj.isRemote) {
                this.slowed = this.destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox());
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
            } else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
                this.setHealth(this.getHealth() + 1.0f);
            }
        }
        if (this.rand.nextInt(10) == 0) {
            float var1 = 32.0f;
            List var2 = this.worldObj.getEntitiesWithinAABB(EntityEnderCrystal.class, this.getEntityBoundingBox().expand(var1, var1, var1));
            EntityEnderCrystal var3 = null;
            double var4 = Double.MAX_VALUE;
            for (EntityEnderCrystal var7 : var2) {
                double var8 = var7.getDistanceSqToEntity(this);
                if (!(var8 < var4)) continue;
                var4 = var8;
                var3 = var7;
            }
            this.healingEnderCrystal = var3;
        }
    }

    private void collideWithEntities(List p_70970_1_) {
        double var2 = (this.dragonPartBody.getEntityBoundingBox().minX + this.dragonPartBody.getEntityBoundingBox().maxX) / 2.0;
        double var4 = (this.dragonPartBody.getEntityBoundingBox().minZ + this.dragonPartBody.getEntityBoundingBox().maxZ) / 2.0;
        for (Entity var7 : p_70970_1_) {
            if (!(var7 instanceof EntityLivingBase)) continue;
            double var8 = var7.posX - var2;
            double var10 = var7.posZ - var4;
            double var12 = var8 * var8 + var10 * var10;
            var7.addVelocity(var8 / var12 * 4.0, 0.2f, var10 / var12 * 4.0);
        }
    }

    private void attackEntitiesInList(List p_70971_1_) {
        for (int var2 = 0; var2 < p_70971_1_.size(); ++var2) {
            Entity var3 = (Entity)p_70971_1_.get(var2);
            if (!(var3 instanceof EntityLivingBase)) continue;
            var3.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0f);
            this.func_174815_a(this, var3);
        }
    }

    private void setNewTarget() {
        this.forceNewTarget = false;
        ArrayList var1 = Lists.newArrayList((Iterable)this.worldObj.playerEntities);
        Iterator var2 = var1.iterator();
        while (var2.hasNext()) {
            if (!((EntityPlayer)var2.next()).func_175149_v()) continue;
            var2.remove();
        }
        if (this.rand.nextInt(2) == 0 && !var1.isEmpty()) {
            this.target = (Entity)var1.get(this.rand.nextInt(var1.size()));
        } else {
            double var8;
            double var6;
            double var4;
            boolean var3;
            do {
                this.targetX = 0.0;
                this.targetY = 70.0f + this.rand.nextFloat() * 50.0f;
                this.targetZ = 0.0;
                this.targetX += (double)(this.rand.nextFloat() * 120.0f - 60.0f);
                this.targetZ += (double)(this.rand.nextFloat() * 120.0f - 60.0f);
            } while (!(var3 = (var4 = this.posX - this.targetX) * var4 + (var6 = this.posY - this.targetY) * var6 + (var8 = this.posZ - this.targetZ) * var8 > 100.0));
            this.target = null;
        }
    }

    private float simplifyAngle(double p_70973_1_) {
        return (float)MathHelper.wrapAngleTo180_double(p_70973_1_);
    }

    private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_) {
        int var2 = MathHelper.floor_double(p_70972_1_.minX);
        int var3 = MathHelper.floor_double(p_70972_1_.minY);
        int var4 = MathHelper.floor_double(p_70972_1_.minZ);
        int var5 = MathHelper.floor_double(p_70972_1_.maxX);
        int var6 = MathHelper.floor_double(p_70972_1_.maxY);
        int var7 = MathHelper.floor_double(p_70972_1_.maxZ);
        boolean var8 = false;
        boolean var9 = false;
        for (int var10 = var2; var10 <= var5; ++var10) {
            for (int var11 = var3; var11 <= var6; ++var11) {
                for (int var12 = var4; var12 <= var7; ++var12) {
                    Block var13 = this.worldObj.getBlockState(new BlockPos(var10, var11, var12)).getBlock();
                    if (var13.getMaterial() == Material.air) continue;
                    if (var13 != Blocks.barrier && var13 != Blocks.obsidian && var13 != Blocks.end_stone && var13 != Blocks.bedrock && var13 != Blocks.command_block && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
                        var9 = this.worldObj.setBlockToAir(new BlockPos(var10, var11, var12)) || var9;
                        continue;
                    }
                    var8 = true;
                }
            }
        }
        if (var9) {
            double var16 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * (double)this.rand.nextFloat();
            double var17 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * (double)this.rand.nextFloat();
            double var14 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * (double)this.rand.nextFloat();
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, var16, var17, var14, 0.0, 0.0, 0.0, new int[0]);
        }
        return var8;
    }

    @Override
    public boolean attackEntityFromPart(EntityDragonPart p_70965_1_, DamageSource p_70965_2_, float p_70965_3_) {
        if (p_70965_1_ != this.dragonPartHead) {
            p_70965_3_ = p_70965_3_ / 4.0f + 1.0f;
        }
        float var4 = this.rotationYaw * (float)Math.PI / 180.0f;
        float var5 = MathHelper.sin(var4);
        float var6 = MathHelper.cos(var4);
        this.targetX = this.posX + (double)(var5 * 5.0f) + (double)((this.rand.nextFloat() - 0.5f) * 2.0f);
        this.targetY = this.posY + (double)(this.rand.nextFloat() * 3.0f) + 1.0;
        this.targetZ = this.posZ - (double)(var6 * 5.0f) + (double)((this.rand.nextFloat() - 0.5f) * 2.0f);
        this.target = null;
        if (p_70965_2_.getEntity() instanceof EntityPlayer || p_70965_2_.isExplosion()) {
            this.func_82195_e(p_70965_2_, p_70965_3_);
        }
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source instanceof EntityDamageSource && ((EntityDamageSource)source).func_180139_w()) {
            this.func_82195_e(source, amount);
        }
        return false;
    }

    protected boolean func_82195_e(DamageSource p_82195_1_, float p_82195_2_) {
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
            float var1 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            float var2 = (this.rand.nextFloat() - 0.5f) * 4.0f;
            float var3 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + (double)var1, this.posY + 2.0 + (double)var2, this.posZ + (double)var3, 0.0, 0.0, 0.0, new int[0]);
        }
        if (!this.worldObj.isRemote) {
            if (this.deathTicks > 150 && this.deathTicks % 5 == 0 && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
                int var5;
                for (int var4 = 1000; var4 > 0; var4 -= var5) {
                    var5 = EntityXPOrb.getXPSplit(var4);
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var5));
                }
            }
            if (this.deathTicks == 1) {
                this.worldObj.func_175669_a(1018, new BlockPos(this), 0);
            }
        }
        this.moveEntity(0.0, 0.1f, 0.0);
        this.renderYawOffset = this.rotationYaw += 20.0f;
        if (this.deathTicks == 200 && !this.worldObj.isRemote) {
            int var5;
            for (int var4 = 2000; var4 > 0; var4 -= var5) {
                var5 = EntityXPOrb.getXPSplit(var4);
                this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, var5));
            }
            this.func_175499_a(new BlockPos(this.posX, 64.0, this.posZ));
            this.setDead();
        }
    }

    private void func_175499_a(BlockPos p_175499_1_) {
        boolean var2 = true;
        double var3 = 12.25;
        double var5 = 6.25;
        for (int var7 = -1; var7 <= 32; ++var7) {
            for (int var8 = -4; var8 <= 4; ++var8) {
                for (int var9 = -4; var9 <= 4; ++var9) {
                    double var10 = var8 * var8 + var9 * var9;
                    if (!(var10 <= 12.25)) continue;
                    BlockPos var12 = p_175499_1_.add(var8, var7, var9);
                    if (var7 < 0) {
                        if (!(var10 <= 6.25)) continue;
                        this.worldObj.setBlockState(var12, Blocks.bedrock.getDefaultState());
                        continue;
                    }
                    if (var7 > 0) {
                        this.worldObj.setBlockState(var12, Blocks.air.getDefaultState());
                        continue;
                    }
                    if (var10 > 6.25) {
                        this.worldObj.setBlockState(var12, Blocks.bedrock.getDefaultState());
                        continue;
                    }
                    this.worldObj.setBlockState(var12, Blocks.end_portal.getDefaultState());
                }
            }
        }
        this.worldObj.setBlockState(p_175499_1_, Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(p_175499_1_.offsetUp(), Blocks.bedrock.getDefaultState());
        BlockPos var13 = p_175499_1_.offsetUp(2);
        this.worldObj.setBlockState(var13, Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(var13.offsetWest(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, (Comparable)((Object)EnumFacing.EAST)));
        this.worldObj.setBlockState(var13.offsetEast(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, (Comparable)((Object)EnumFacing.WEST)));
        this.worldObj.setBlockState(var13.offsetNorth(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, (Comparable)((Object)EnumFacing.SOUTH)));
        this.worldObj.setBlockState(var13.offsetSouth(), Blocks.torch.getDefaultState().withProperty(BlockTorch.FACING_PROP, (Comparable)((Object)EnumFacing.NORTH)));
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

