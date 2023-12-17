// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import org.apache.logging.log4j.LogManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundCategory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.pathfinding.Path;
import javax.annotation.Nullable;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.boss.dragon.phase.IPhase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.World;
import net.minecraft.pathfinding.PathHeap;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.network.datasync.DataParameter;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.EntityLiving;

public class EntityDragon extends EntityLiving implements IEntityMultiPart, IMob
{
    private static final Logger LOGGER;
    public static final DataParameter<Integer> PHASE;
    public double[][] ringBuffer;
    public int ringBufferIndex;
    public MultiPartEntityPart[] dragonPartArray;
    public MultiPartEntityPart dragonPartHead;
    public MultiPartEntityPart dragonPartNeck;
    public MultiPartEntityPart dragonPartBody;
    public MultiPartEntityPart dragonPartTail1;
    public MultiPartEntityPart dragonPartTail2;
    public MultiPartEntityPart dragonPartTail3;
    public MultiPartEntityPart dragonPartWing1;
    public MultiPartEntityPart dragonPartWing2;
    public float prevAnimTime;
    public float animTime;
    public boolean slowed;
    public int deathTicks;
    public EntityEnderCrystal healingEnderCrystal;
    private final DragonFightManager fightManager;
    private final PhaseManager phaseManager;
    private int growlTime;
    private int sittingDamageReceived;
    private final PathPoint[] pathPoints;
    private final int[] neighbors;
    private final PathHeap pathFindQueue;
    
    public EntityDragon(final World worldIn) {
        super(worldIn);
        this.ringBuffer = new double[64][3];
        this.ringBufferIndex = -1;
        this.dragonPartHead = new MultiPartEntityPart(this, "head", 6.0f, 6.0f);
        this.dragonPartNeck = new MultiPartEntityPart(this, "neck", 6.0f, 6.0f);
        this.dragonPartBody = new MultiPartEntityPart(this, "body", 8.0f, 8.0f);
        this.dragonPartTail1 = new MultiPartEntityPart(this, "tail", 4.0f, 4.0f);
        this.dragonPartTail2 = new MultiPartEntityPart(this, "tail", 4.0f, 4.0f);
        this.dragonPartTail3 = new MultiPartEntityPart(this, "tail", 4.0f, 4.0f);
        this.dragonPartWing1 = new MultiPartEntityPart(this, "wing", 4.0f, 4.0f);
        this.dragonPartWing2 = new MultiPartEntityPart(this, "wing", 4.0f, 4.0f);
        this.growlTime = 200;
        this.pathPoints = new PathPoint[24];
        this.neighbors = new int[24];
        this.pathFindQueue = new PathHeap();
        this.dragonPartArray = new MultiPartEntityPart[] { this.dragonPartHead, this.dragonPartNeck, this.dragonPartBody, this.dragonPartTail1, this.dragonPartTail2, this.dragonPartTail3, this.dragonPartWing1, this.dragonPartWing2 };
        this.setHealth(this.getMaxHealth());
        this.setSize(16.0f, 8.0f);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.growlTime = 100;
        this.ignoreFrustumCheck = true;
        if (!worldIn.isRemote && worldIn.provider instanceof WorldProviderEnd) {
            this.fightManager = ((WorldProviderEnd)worldIn.provider).getDragonFightManager();
        }
        else {
            this.fightManager = null;
        }
        this.phaseManager = new PhaseManager(this);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(200.0);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(EntityDragon.PHASE, PhaseList.HOVER.getId());
    }
    
    public double[] getMovementOffsets(final int p_70974_1_, float p_70974_2_) {
        if (this.getHealth() <= 0.0f) {
            p_70974_2_ = 0.0f;
        }
        p_70974_2_ = 1.0f - p_70974_2_;
        final int i = this.ringBufferIndex - p_70974_1_ & 0x3F;
        final int j = this.ringBufferIndex - p_70974_1_ - 1 & 0x3F;
        final double[] adouble = new double[3];
        double d0 = this.ringBuffer[i][0];
        double d2 = MathHelper.wrapDegrees(this.ringBuffer[j][0] - d0);
        adouble[0] = d0 + d2 * p_70974_2_;
        d0 = this.ringBuffer[i][1];
        d2 = this.ringBuffer[j][1] - d0;
        adouble[1] = d0 + d2 * p_70974_2_;
        adouble[2] = this.ringBuffer[i][2] + (this.ringBuffer[j][2] - this.ringBuffer[i][2]) * p_70974_2_;
        return adouble;
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.setHealth(this.getHealth());
            if (!this.isSilent()) {
                final float f = MathHelper.cos(this.animTime * 6.2831855f);
                final float f2 = MathHelper.cos(this.prevAnimTime * 6.2831855f);
                if (f2 <= -0.3f && f >= -0.3f) {
                    this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_FLAP, this.getSoundCategory(), 5.0f, 0.8f + this.rand.nextFloat() * 0.3f, false);
                }
                if (!this.phaseManager.getCurrentPhase().getIsStationary() && --this.growlTime < 0) {
                    this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_GROWL, this.getSoundCategory(), 2.5f, 0.8f + this.rand.nextFloat() * 0.3f, false);
                    this.growlTime = 200 + this.rand.nextInt(200);
                }
            }
        }
        this.prevAnimTime = this.animTime;
        if (this.getHealth() <= 0.0f) {
            final float f3 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            final float f4 = (this.rand.nextFloat() - 0.5f) * 4.0f;
            final float f5 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + f3, this.posY + 2.0 + f4, this.posZ + f5, 0.0, 0.0, 0.0, new int[0]);
        }
        else {
            this.updateDragonEnderCrystal();
            float f6 = 0.2f / (MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0f + 1.0f);
            f6 *= (float)Math.pow(2.0, this.motionY);
            if (this.phaseManager.getCurrentPhase().getIsStationary()) {
                this.animTime += 0.1f;
            }
            else if (this.slowed) {
                this.animTime += f6 * 0.5f;
            }
            else {
                this.animTime += f6;
            }
            this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
            if (this.isAIDisabled()) {
                this.animTime = 0.5f;
            }
            else {
                if (this.ringBufferIndex < 0) {
                    for (int i = 0; i < this.ringBuffer.length; ++i) {
                        this.ringBuffer[i][0] = this.rotationYaw;
                        this.ringBuffer[i][1] = this.posY;
                    }
                }
                if (++this.ringBufferIndex == this.ringBuffer.length) {
                    this.ringBufferIndex = 0;
                }
                this.ringBuffer[this.ringBufferIndex][0] = this.rotationYaw;
                this.ringBuffer[this.ringBufferIndex][1] = this.posY;
                if (this.world.isRemote) {
                    if (this.newPosRotationIncrements > 0) {
                        final double d5 = this.posX + (this.interpTargetX - this.posX) / this.newPosRotationIncrements;
                        final double d6 = this.posY + (this.interpTargetY - this.posY) / this.newPosRotationIncrements;
                        final double d7 = this.posZ + (this.interpTargetZ - this.posZ) / this.newPosRotationIncrements;
                        final double d8 = MathHelper.wrapDegrees(this.interpTargetYaw - this.rotationYaw);
                        this.rotationYaw += (float)(d8 / this.newPosRotationIncrements);
                        this.rotationPitch += (float)((this.interpTargetPitch - this.rotationPitch) / this.newPosRotationIncrements);
                        --this.newPosRotationIncrements;
                        this.setPosition(d5, d6, d7);
                        this.setRotation(this.rotationYaw, this.rotationPitch);
                    }
                    this.phaseManager.getCurrentPhase().doClientRenderEffects();
                }
                else {
                    IPhase iphase = this.phaseManager.getCurrentPhase();
                    iphase.doLocalUpdate();
                    if (this.phaseManager.getCurrentPhase() != iphase) {
                        iphase = this.phaseManager.getCurrentPhase();
                        iphase.doLocalUpdate();
                    }
                    final Vec3d vec3d = iphase.getTargetLocation();
                    if (vec3d != null) {
                        final double d9 = vec3d.x - this.posX;
                        double d10 = vec3d.y - this.posY;
                        final double d11 = vec3d.z - this.posZ;
                        final double d12 = d9 * d9 + d10 * d10 + d11 * d11;
                        final float f7 = iphase.getMaxRiseOrFall();
                        d10 = MathHelper.clamp(d10 / MathHelper.sqrt(d9 * d9 + d11 * d11), -f7, f7);
                        this.motionY += d10 * 0.10000000149011612;
                        this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
                        final double d13 = MathHelper.clamp(MathHelper.wrapDegrees(180.0 - MathHelper.atan2(d9, d11) * 57.29577951308232 - this.rotationYaw), -50.0, 50.0);
                        final Vec3d vec3d2 = new Vec3d(vec3d.x - this.posX, vec3d.y - this.posY, vec3d.z - this.posZ).normalize();
                        final Vec3d vec3d3 = new Vec3d(MathHelper.sin(this.rotationYaw * 0.017453292f), this.motionY, -MathHelper.cos(this.rotationYaw * 0.017453292f)).normalize();
                        final float f8 = Math.max(((float)vec3d3.dotProduct(vec3d2) + 0.5f) / 1.5f, 0.0f);
                        this.randomYawVelocity *= 0.8f;
                        this.randomYawVelocity += (float)(d13 * iphase.getYawFactor());
                        this.rotationYaw += this.randomYawVelocity * 0.1f;
                        final float f9 = (float)(2.0 / (d12 + 1.0));
                        final float f10 = 0.06f;
                        this.moveRelative(0.0f, 0.0f, -1.0f, 0.06f * (f8 * f9 + (1.0f - f9)));
                        if (this.slowed) {
                            this.move(MoverType.SELF, this.motionX * 0.800000011920929, this.motionY * 0.800000011920929, this.motionZ * 0.800000011920929);
                        }
                        else {
                            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                        }
                        final Vec3d vec3d4 = new Vec3d(this.motionX, this.motionY, this.motionZ).normalize();
                        float f11 = ((float)vec3d4.dotProduct(vec3d3) + 1.0f) / 2.0f;
                        f11 = 0.8f + 0.15f * f11;
                        this.motionX *= f11;
                        this.motionZ *= f11;
                        this.motionY *= 0.9100000262260437;
                    }
                }
                this.renderYawOffset = this.rotationYaw;
                this.dragonPartHead.width = 1.0f;
                this.dragonPartHead.height = 1.0f;
                this.dragonPartNeck.width = 3.0f;
                this.dragonPartNeck.height = 3.0f;
                this.dragonPartTail1.width = 2.0f;
                this.dragonPartTail1.height = 2.0f;
                this.dragonPartTail2.width = 2.0f;
                this.dragonPartTail2.height = 2.0f;
                this.dragonPartTail3.width = 2.0f;
                this.dragonPartTail3.height = 2.0f;
                this.dragonPartBody.height = 3.0f;
                this.dragonPartBody.width = 5.0f;
                this.dragonPartWing1.height = 2.0f;
                this.dragonPartWing1.width = 4.0f;
                this.dragonPartWing2.height = 3.0f;
                this.dragonPartWing2.width = 4.0f;
                final Vec3d[] avec3d = new Vec3d[this.dragonPartArray.length];
                for (int j = 0; j < this.dragonPartArray.length; ++j) {
                    avec3d[j] = new Vec3d(this.dragonPartArray[j].posX, this.dragonPartArray[j].posY, this.dragonPartArray[j].posZ);
                }
                final float f12 = (float)(this.getMovementOffsets(5, 1.0f)[1] - this.getMovementOffsets(10, 1.0f)[1]) * 10.0f * 0.017453292f;
                final float f13 = MathHelper.cos(f12);
                final float f14 = MathHelper.sin(f12);
                final float f15 = this.rotationYaw * 0.017453292f;
                final float f16 = MathHelper.sin(f15);
                final float f17 = MathHelper.cos(f15);
                this.dragonPartBody.onUpdate();
                this.dragonPartBody.setLocationAndAngles(this.posX + f16 * 0.5f, this.posY, this.posZ - f17 * 0.5f, 0.0f, 0.0f);
                this.dragonPartWing1.onUpdate();
                this.dragonPartWing1.setLocationAndAngles(this.posX + f17 * 4.5f, this.posY + 2.0, this.posZ + f16 * 4.5f, 0.0f, 0.0f);
                this.dragonPartWing2.onUpdate();
                this.dragonPartWing2.setLocationAndAngles(this.posX - f17 * 4.5f, this.posY + 2.0, this.posZ - f16 * 4.5f, 0.0f, 0.0f);
                if (!this.world.isRemote && this.hurtTime == 0) {
                    this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().grow(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                    this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().grow(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                    this.attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().grow(1.0)));
                    this.attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartNeck.getEntityBoundingBox().grow(1.0)));
                }
                final double[] adouble = this.getMovementOffsets(5, 1.0f);
                final float f18 = MathHelper.sin(this.rotationYaw * 0.017453292f - this.randomYawVelocity * 0.01f);
                final float f19 = MathHelper.cos(this.rotationYaw * 0.017453292f - this.randomYawVelocity * 0.01f);
                this.dragonPartHead.onUpdate();
                this.dragonPartNeck.onUpdate();
                final float f20 = this.getHeadYOffset(1.0f);
                this.dragonPartHead.setLocationAndAngles(this.posX + f18 * 6.5f * f13, this.posY + f20 + f14 * 6.5f, this.posZ - f19 * 6.5f * f13, 0.0f, 0.0f);
                this.dragonPartNeck.setLocationAndAngles(this.posX + f18 * 5.5f * f13, this.posY + f20 + f14 * 5.5f, this.posZ - f19 * 5.5f * f13, 0.0f, 0.0f);
                for (int k = 0; k < 3; ++k) {
                    MultiPartEntityPart multipartentitypart = null;
                    if (k == 0) {
                        multipartentitypart = this.dragonPartTail1;
                    }
                    if (k == 1) {
                        multipartentitypart = this.dragonPartTail2;
                    }
                    if (k == 2) {
                        multipartentitypart = this.dragonPartTail3;
                    }
                    final double[] adouble2 = this.getMovementOffsets(12 + k * 2, 1.0f);
                    final float f21 = this.rotationYaw * 0.017453292f + this.simplifyAngle(adouble2[0] - adouble[0]) * 0.017453292f;
                    final float f22 = MathHelper.sin(f21);
                    final float f23 = MathHelper.cos(f21);
                    final float f24 = 1.5f;
                    final float f25 = (k + 1) * 2.0f;
                    multipartentitypart.onUpdate();
                    multipartentitypart.setLocationAndAngles(this.posX - (f16 * 1.5f + f22 * f25) * f13, this.posY + (adouble2[1] - adouble[1]) - (f25 + 1.5f) * f14 + 1.5, this.posZ + (f17 * 1.5f + f23 * f25) * f13, 0.0f, 0.0f);
                }
                if (!this.world.isRemote) {
                    this.slowed = (this.destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartNeck.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox()));
                    if (this.fightManager != null) {
                        this.fightManager.dragonUpdate(this);
                    }
                }
                for (int l = 0; l < this.dragonPartArray.length; ++l) {
                    this.dragonPartArray[l].prevPosX = avec3d[l].x;
                    this.dragonPartArray[l].prevPosY = avec3d[l].y;
                    this.dragonPartArray[l].prevPosZ = avec3d[l].z;
                }
            }
        }
    }
    
    private float getHeadYOffset(final float p_184662_1_) {
        double d0;
        if (this.phaseManager.getCurrentPhase().getIsStationary()) {
            d0 = -1.0;
        }
        else {
            final double[] adouble = this.getMovementOffsets(5, 1.0f);
            final double[] adouble2 = this.getMovementOffsets(0, 1.0f);
            d0 = adouble[1] - adouble2[1];
        }
        return (float)d0;
    }
    
    private void updateDragonEnderCrystal() {
        if (this.healingEnderCrystal != null) {
            if (this.healingEnderCrystal.isDead) {
                this.healingEnderCrystal = null;
            }
            else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
                this.setHealth(this.getHealth() + 1.0f);
            }
        }
        if (this.rand.nextInt(10) == 0) {
            final List<EntityEnderCrystal> list = this.world.getEntitiesWithinAABB((Class<? extends EntityEnderCrystal>)EntityEnderCrystal.class, this.getEntityBoundingBox().grow(32.0));
            EntityEnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;
            for (final EntityEnderCrystal entityendercrystal2 : list) {
                final double d2 = entityendercrystal2.getDistanceSq(this);
                if (d2 < d0) {
                    d0 = d2;
                    entityendercrystal = entityendercrystal2;
                }
            }
            this.healingEnderCrystal = entityendercrystal;
        }
    }
    
    private void collideWithEntities(final List<Entity> p_70970_1_) {
        final double d0 = (this.dragonPartBody.getEntityBoundingBox().minX + this.dragonPartBody.getEntityBoundingBox().maxX) / 2.0;
        final double d2 = (this.dragonPartBody.getEntityBoundingBox().minZ + this.dragonPartBody.getEntityBoundingBox().maxZ) / 2.0;
        for (final Entity entity : p_70970_1_) {
            if (entity instanceof EntityLivingBase) {
                final double d3 = entity.posX - d0;
                final double d4 = entity.posZ - d2;
                final double d5 = d3 * d3 + d4 * d4;
                entity.addVelocity(d3 / d5 * 4.0, 0.20000000298023224, d4 / d5 * 4.0);
                if (this.phaseManager.getCurrentPhase().getIsStationary() || ((EntityLivingBase)entity).getRevengeTimer() >= entity.ticksExisted - 2) {
                    continue;
                }
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0f);
                this.applyEnchantments(this, entity);
            }
        }
    }
    
    private void attackEntitiesInList(final List<Entity> p_70971_1_) {
        for (int i = 0; i < p_70971_1_.size(); ++i) {
            final Entity entity = p_70971_1_.get(i);
            if (entity instanceof EntityLivingBase) {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0f);
                this.applyEnchantments(this, entity);
            }
        }
    }
    
    private float simplifyAngle(final double p_70973_1_) {
        return (float)MathHelper.wrapDegrees(p_70973_1_);
    }
    
    private boolean destroyBlocksInAABB(final AxisAlignedBB p_70972_1_) {
        final int i = MathHelper.floor(p_70972_1_.minX);
        final int j = MathHelper.floor(p_70972_1_.minY);
        final int k = MathHelper.floor(p_70972_1_.minZ);
        final int l = MathHelper.floor(p_70972_1_.maxX);
        final int i2 = MathHelper.floor(p_70972_1_.maxY);
        final int j2 = MathHelper.floor(p_70972_1_.maxZ);
        boolean flag = false;
        boolean flag2 = false;
        for (int k2 = i; k2 <= l; ++k2) {
            for (int l2 = j; l2 <= i2; ++l2) {
                for (int i3 = k; i3 <= j2; ++i3) {
                    final BlockPos blockpos = new BlockPos(k2, l2, i3);
                    final IBlockState iblockstate = this.world.getBlockState(blockpos);
                    final Block block = iblockstate.getBlock();
                    if (iblockstate.getMaterial() != Material.AIR && iblockstate.getMaterial() != Material.FIRE) {
                        if (!this.world.getGameRules().getBoolean("mobGriefing")) {
                            flag = true;
                        }
                        else if (block != Blocks.BARRIER && block != Blocks.OBSIDIAN && block != Blocks.END_STONE && block != Blocks.BEDROCK && block != Blocks.END_PORTAL && block != Blocks.END_PORTAL_FRAME) {
                            if (block != Blocks.COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK && block != Blocks.IRON_BARS && block != Blocks.END_GATEWAY) {
                                flag2 = (this.world.setBlockToAir(blockpos) || flag2);
                            }
                            else {
                                flag = true;
                            }
                        }
                        else {
                            flag = true;
                        }
                    }
                }
            }
        }
        if (flag2) {
            final double d0 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * this.rand.nextFloat();
            final double d2 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * this.rand.nextFloat();
            final double d3 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * this.rand.nextFloat();
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
        }
        return flag;
    }
    
    @Override
    public boolean attackEntityFromPart(final MultiPartEntityPart part, final DamageSource source, float damage) {
        damage = this.phaseManager.getCurrentPhase().getAdjustedDamage(part, source, damage);
        if (part != this.dragonPartHead) {
            damage = damage / 4.0f + Math.min(damage, 1.0f);
        }
        if (damage < 0.01f) {
            return false;
        }
        if (source.getTrueSource() instanceof EntityPlayer || source.isExplosion()) {
            final float f = this.getHealth();
            this.attackDragonFrom(source, damage);
            if (this.getHealth() <= 0.0f && !this.phaseManager.getCurrentPhase().getIsStationary()) {
                this.setHealth(1.0f);
                this.phaseManager.setPhase(PhaseList.DYING);
            }
            if (this.phaseManager.getCurrentPhase().getIsStationary()) {
                this.sittingDamageReceived += (int)(f - this.getHealth());
                if (this.sittingDamageReceived > 0.25f * this.getMaxHealth()) {
                    this.sittingDamageReceived = 0;
                    this.phaseManager.setPhase(PhaseList.TAKEOFF);
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage()) {
            this.attackEntityFromPart(this.dragonPartBody, source, amount);
        }
        return false;
    }
    
    protected boolean attackDragonFrom(final DamageSource source, final float amount) {
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public void onKillCommand() {
        this.setDead();
        if (this.fightManager != null) {
            this.fightManager.dragonUpdate(this);
            this.fightManager.processDragonDeath(this);
        }
    }
    
    @Override
    protected void onDeathUpdate() {
        if (this.fightManager != null) {
            this.fightManager.dragonUpdate(this);
        }
        ++this.deathTicks;
        if (this.deathTicks >= 180 && this.deathTicks <= 200) {
            final float f = (this.rand.nextFloat() - 0.5f) * 8.0f;
            final float f2 = (this.rand.nextFloat() - 0.5f) * 4.0f;
            final float f3 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f, this.posY + 2.0 + f2, this.posZ + f3, 0.0, 0.0, 0.0, new int[0]);
        }
        final boolean flag = this.world.getGameRules().getBoolean("doMobLoot");
        int i = 500;
        if (this.fightManager != null && !this.fightManager.hasPreviouslyKilledDragon()) {
            i = 12000;
        }
        if (!this.world.isRemote) {
            if (this.deathTicks > 150 && this.deathTicks % 5 == 0 && flag) {
                this.dropExperience(MathHelper.floor(i * 0.08f));
            }
            if (this.deathTicks == 1) {
                this.world.playBroadcastSound(1028, new BlockPos(this), 0);
            }
        }
        this.move(MoverType.SELF, 0.0, 0.10000000149011612, 0.0);
        this.rotationYaw += 20.0f;
        this.renderYawOffset = this.rotationYaw;
        if (this.deathTicks == 200 && !this.world.isRemote) {
            if (flag) {
                this.dropExperience(MathHelper.floor(i * 0.2f));
            }
            if (this.fightManager != null) {
                this.fightManager.processDragonDeath(this);
            }
            this.setDead();
        }
    }
    
    private void dropExperience(int p_184668_1_) {
        while (p_184668_1_ > 0) {
            final int i = EntityXPOrb.getXPSplit(p_184668_1_);
            p_184668_1_ -= i;
            this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, i));
        }
    }
    
    public int initPathPoints() {
        if (this.pathPoints[0] == null) {
            for (int i = 0; i < 24; ++i) {
                int j = 5;
                int l;
                int i2;
                if (i < 12) {
                    l = (int)(60.0f * MathHelper.cos(2.0f * (-3.1415927f + 0.2617994f * i)));
                    i2 = (int)(60.0f * MathHelper.sin(2.0f * (-3.1415927f + 0.2617994f * i)));
                }
                else if (i < 20) {
                    final int lvt_3_1_ = i - 12;
                    l = (int)(40.0f * MathHelper.cos(2.0f * (-3.1415927f + 0.3926991f * lvt_3_1_)));
                    i2 = (int)(40.0f * MathHelper.sin(2.0f * (-3.1415927f + 0.3926991f * lvt_3_1_)));
                    j += 10;
                }
                else {
                    final int k1 = i - 20;
                    l = (int)(20.0f * MathHelper.cos(2.0f * (-3.1415927f + 0.7853982f * k1)));
                    i2 = (int)(20.0f * MathHelper.sin(2.0f * (-3.1415927f + 0.7853982f * k1)));
                }
                final int j2 = Math.max(this.world.getSeaLevel() + 10, this.world.getTopSolidOrLiquidBlock(new BlockPos(l, 0, i2)).getY() + j);
                this.pathPoints[i] = new PathPoint(l, j2, i2);
            }
            this.neighbors[0] = 6146;
            this.neighbors[1] = 8197;
            this.neighbors[2] = 8202;
            this.neighbors[3] = 16404;
            this.neighbors[4] = 32808;
            this.neighbors[5] = 32848;
            this.neighbors[6] = 65696;
            this.neighbors[7] = 131392;
            this.neighbors[8] = 131712;
            this.neighbors[9] = 263424;
            this.neighbors[10] = 526848;
            this.neighbors[11] = 525313;
            this.neighbors[12] = 1581057;
            this.neighbors[13] = 3166214;
            this.neighbors[14] = 2138120;
            this.neighbors[15] = 6373424;
            this.neighbors[16] = 4358208;
            this.neighbors[17] = 12910976;
            this.neighbors[18] = 9044480;
            this.neighbors[19] = 9706496;
            this.neighbors[20] = 15216640;
            this.neighbors[21] = 13688832;
            this.neighbors[22] = 11763712;
            this.neighbors[23] = 8257536;
        }
        return this.getNearestPpIdx(this.posX, this.posY, this.posZ);
    }
    
    public int getNearestPpIdx(final double x, final double y, final double z) {
        float f = 10000.0f;
        int i = 0;
        final PathPoint pathpoint = new PathPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
        int j = 0;
        if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0) {
            j = 12;
        }
        for (int k = j; k < 24; ++k) {
            if (this.pathPoints[k] != null) {
                final float f2 = this.pathPoints[k].distanceToSquared(pathpoint);
                if (f2 < f) {
                    f = f2;
                    i = k;
                }
            }
        }
        return i;
    }
    
    @Nullable
    public Path findPath(final int startIdx, final int finishIdx, @Nullable final PathPoint andThen) {
        for (int i = 0; i < 24; ++i) {
            final PathPoint pathpoint = this.pathPoints[i];
            pathpoint.visited = false;
            pathpoint.distanceToTarget = 0.0f;
            pathpoint.totalPathDistance = 0.0f;
            pathpoint.distanceToNext = 0.0f;
            pathpoint.previous = null;
            pathpoint.index = -1;
        }
        final PathPoint pathpoint2 = this.pathPoints[startIdx];
        PathPoint pathpoint3 = this.pathPoints[finishIdx];
        pathpoint2.totalPathDistance = 0.0f;
        pathpoint2.distanceToNext = pathpoint2.distanceTo(pathpoint3);
        pathpoint2.distanceToTarget = pathpoint2.distanceToNext;
        this.pathFindQueue.clearPath();
        this.pathFindQueue.addPoint(pathpoint2);
        PathPoint pathpoint4 = pathpoint2;
        int j = 0;
        if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0) {
            j = 12;
        }
        while (!this.pathFindQueue.isPathEmpty()) {
            final PathPoint pathpoint5 = this.pathFindQueue.dequeue();
            if (pathpoint5.equals(pathpoint3)) {
                if (andThen != null) {
                    andThen.previous = pathpoint3;
                    pathpoint3 = andThen;
                }
                return this.makePath(pathpoint2, pathpoint3);
            }
            if (pathpoint5.distanceTo(pathpoint3) < pathpoint4.distanceTo(pathpoint3)) {
                pathpoint4 = pathpoint5;
            }
            pathpoint5.visited = true;
            int k = 0;
            for (int l = 0; l < 24; ++l) {
                if (this.pathPoints[l] == pathpoint5) {
                    k = l;
                    break;
                }
            }
            for (int i2 = j; i2 < 24; ++i2) {
                if ((this.neighbors[k] & 1 << i2) > 0) {
                    final PathPoint pathpoint6 = this.pathPoints[i2];
                    if (!pathpoint6.visited) {
                        final float f = pathpoint5.totalPathDistance + pathpoint5.distanceTo(pathpoint6);
                        if (!pathpoint6.isAssigned() || f < pathpoint6.totalPathDistance) {
                            pathpoint6.previous = pathpoint5;
                            pathpoint6.totalPathDistance = f;
                            pathpoint6.distanceToNext = pathpoint6.distanceTo(pathpoint3);
                            if (pathpoint6.isAssigned()) {
                                this.pathFindQueue.changeDistance(pathpoint6, pathpoint6.totalPathDistance + pathpoint6.distanceToNext);
                            }
                            else {
                                pathpoint6.distanceToTarget = pathpoint6.totalPathDistance + pathpoint6.distanceToNext;
                                this.pathFindQueue.addPoint(pathpoint6);
                            }
                        }
                    }
                }
            }
        }
        if (pathpoint4 == pathpoint2) {
            return null;
        }
        EntityDragon.LOGGER.debug("Failed to find path from {} to {}", (Object)startIdx, (Object)finishIdx);
        if (andThen != null) {
            andThen.previous = pathpoint4;
            pathpoint4 = andThen;
        }
        return this.makePath(pathpoint2, pathpoint4);
    }
    
    private Path makePath(final PathPoint start, final PathPoint finish) {
        int i = 1;
        for (PathPoint pathpoint = finish; pathpoint.previous != null; pathpoint = pathpoint.previous) {
            ++i;
        }
        final PathPoint[] apathpoint = new PathPoint[i];
        PathPoint pathpoint2 = finish;
        --i;
        apathpoint[i] = finish;
        while (pathpoint2.previous != null) {
            pathpoint2 = pathpoint2.previous;
            --i;
            apathpoint[i] = pathpoint2;
        }
        return new Path(apathpoint);
    }
    
    public static void registerFixesDragon(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityDragon.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("DragonPhase", this.phaseManager.getCurrentPhase().getType().getId());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("DragonPhase")) {
            this.phaseManager.setPhase(PhaseList.getById(compound.getInteger("DragonPhase")));
        }
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
    public World getWorld() {
        return this.world;
    }
    
    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ENDERDRAGON_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ENDERDRAGON_HURT;
    }
    
    @Override
    protected float getSoundVolume() {
        return 5.0f;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ENDER_DRAGON;
    }
    
    public float getHeadPartYOffset(final int p_184667_1_, final double[] p_184667_2_, final double[] p_184667_3_) {
        final IPhase iphase = this.phaseManager.getCurrentPhase();
        final PhaseList<? extends IPhase> phaselist = iphase.getType();
        double d0;
        if (phaselist != PhaseList.LANDING && phaselist != PhaseList.TAKEOFF) {
            if (iphase.getIsStationary()) {
                d0 = p_184667_1_;
            }
            else if (p_184667_1_ == 6) {
                d0 = 0.0;
            }
            else {
                d0 = p_184667_3_[1] - p_184667_2_[1];
            }
        }
        else {
            final BlockPos blockpos = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
            final float f = Math.max(MathHelper.sqrt(this.getDistanceSqToCenter(blockpos)) / 4.0f, 1.0f);
            d0 = p_184667_1_ / f;
        }
        return (float)d0;
    }
    
    public Vec3d getHeadLookVec(final float p_184665_1_) {
        final IPhase iphase = this.phaseManager.getCurrentPhase();
        final PhaseList<? extends IPhase> phaselist = iphase.getType();
        Vec3d vec3d;
        if (phaselist != PhaseList.LANDING && phaselist != PhaseList.TAKEOFF) {
            if (iphase.getIsStationary()) {
                final float f4 = this.rotationPitch;
                final float f5 = 1.5f;
                this.rotationPitch = -45.0f;
                vec3d = this.getLook(p_184665_1_);
                this.rotationPitch = f4;
            }
            else {
                vec3d = this.getLook(p_184665_1_);
            }
        }
        else {
            final BlockPos blockpos = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
            final float f6 = Math.max(MathHelper.sqrt(this.getDistanceSqToCenter(blockpos)) / 4.0f, 1.0f);
            final float f7 = 6.0f / f6;
            final float f8 = this.rotationPitch;
            final float f9 = 1.5f;
            this.rotationPitch = -f7 * 1.5f * 5.0f;
            vec3d = this.getLook(p_184665_1_);
            this.rotationPitch = f8;
        }
        return vec3d;
    }
    
    public void onCrystalDestroyed(final EntityEnderCrystal crystal, final BlockPos pos, final DamageSource dmgSrc) {
        EntityPlayer entityplayer;
        if (dmgSrc.getTrueSource() instanceof EntityPlayer) {
            entityplayer = (EntityPlayer)dmgSrc.getTrueSource();
        }
        else {
            entityplayer = this.world.getNearestAttackablePlayer(pos, 64.0, 64.0);
        }
        if (crystal == this.healingEnderCrystal) {
            this.attackEntityFromPart(this.dragonPartHead, DamageSource.causeExplosionDamage(entityplayer), 10.0f);
        }
        this.phaseManager.getCurrentPhase().onCrystalDestroyed(crystal, pos, dmgSrc, entityplayer);
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        if (EntityDragon.PHASE.equals(key) && this.world.isRemote) {
            this.phaseManager.setPhase(PhaseList.getById(this.getDataManager().get(EntityDragon.PHASE)));
        }
        super.notifyDataManagerChange(key);
    }
    
    public PhaseManager getPhaseManager() {
        return this.phaseManager;
    }
    
    @Nullable
    public DragonFightManager getFightManager() {
        return this.fightManager;
    }
    
    @Override
    public void addPotionEffect(final PotionEffect potioneffectIn) {
    }
    
    @Override
    protected boolean canBeRidden(final Entity entityIn) {
        return false;
    }
    
    @Override
    public boolean isNonBoss() {
        return false;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        PHASE = EntityDataManager.createKey(EntityDragon.class, DataSerializers.VARINT);
    }
}
