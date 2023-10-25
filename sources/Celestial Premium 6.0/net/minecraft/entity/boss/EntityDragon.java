/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.dragon.phase.IPhase;
import net.minecraft.entity.boss.dragon.phase.PhaseList;
import net.minecraft.entity.boss.dragon.phase.PhaseManager;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathHeap;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.feature.WorldGenEndPodium;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityDragon
extends EntityLiving
implements IEntityMultiPart,
IMob {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final DataParameter<Integer> PHASE = EntityDataManager.createKey(EntityDragon.class, DataSerializers.VARINT);
    public double[][] ringBuffer = new double[64][3];
    public int ringBufferIndex = -1;
    public MultiPartEntityPart[] dragonPartArray;
    public MultiPartEntityPart dragonPartHead = new MultiPartEntityPart(this, "head", 6.0f, 6.0f);
    public MultiPartEntityPart dragonPartNeck = new MultiPartEntityPart(this, "neck", 6.0f, 6.0f);
    public MultiPartEntityPart dragonPartBody = new MultiPartEntityPart(this, "body", 8.0f, 8.0f);
    public MultiPartEntityPart dragonPartTail1 = new MultiPartEntityPart(this, "tail", 4.0f, 4.0f);
    public MultiPartEntityPart dragonPartTail2 = new MultiPartEntityPart(this, "tail", 4.0f, 4.0f);
    public MultiPartEntityPart dragonPartTail3 = new MultiPartEntityPart(this, "tail", 4.0f, 4.0f);
    public MultiPartEntityPart dragonPartWing1 = new MultiPartEntityPart(this, "wing", 4.0f, 4.0f);
    public MultiPartEntityPart dragonPartWing2 = new MultiPartEntityPart(this, "wing", 4.0f, 4.0f);
    public float prevAnimTime;
    public float animTime;
    public boolean slowed;
    public int deathTicks;
    public EntityEnderCrystal healingEnderCrystal;
    private final DragonFightManager fightManager;
    private final PhaseManager phaseManager;
    private int growlTime = 200;
    private int sittingDamageReceived;
    private final PathPoint[] pathPoints = new PathPoint[24];
    private final int[] neighbors = new int[24];
    private final PathHeap pathFindQueue = new PathHeap();

    public EntityDragon(World worldIn) {
        super(worldIn);
        this.dragonPartArray = new MultiPartEntityPart[]{this.dragonPartHead, this.dragonPartNeck, this.dragonPartBody, this.dragonPartTail1, this.dragonPartTail2, this.dragonPartTail3, this.dragonPartWing1, this.dragonPartWing2};
        this.setHealth(this.getMaxHealth());
        this.setSize(16.0f, 8.0f);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.growlTime = 100;
        this.ignoreFrustumCheck = true;
        this.fightManager = !worldIn.isRemote && worldIn.provider instanceof WorldProviderEnd ? ((WorldProviderEnd)worldIn.provider).getDragonFightManager() : null;
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
        this.getDataManager().register(PHASE, PhaseList.HOVER.getId());
    }

    public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_) {
        if (this.getHealth() <= 0.0f) {
            p_70974_2_ = 0.0f;
        }
        p_70974_2_ = 1.0f - p_70974_2_;
        int i = this.ringBufferIndex - p_70974_1_ & 0x3F;
        int j = this.ringBufferIndex - p_70974_1_ - 1 & 0x3F;
        double[] adouble = new double[3];
        double d0 = this.ringBuffer[i][0];
        double d1 = MathHelper.wrapDegrees(this.ringBuffer[j][0] - d0);
        adouble[0] = d0 + d1 * (double)p_70974_2_;
        d0 = this.ringBuffer[i][1];
        d1 = this.ringBuffer[j][1] - d0;
        adouble[1] = d0 + d1 * (double)p_70974_2_;
        adouble[2] = this.ringBuffer[i][2] + (this.ringBuffer[j][2] - this.ringBuffer[i][2]) * (double)p_70974_2_;
        return adouble;
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.setHealth(this.getHealth());
            if (!this.isSilent()) {
                float f = MathHelper.cos(this.animTime * ((float)Math.PI * 2));
                float f1 = MathHelper.cos(this.prevAnimTime * ((float)Math.PI * 2));
                if (f1 <= -0.3f && f >= -0.3f) {
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
            float f12 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            float f13 = (this.rand.nextFloat() - 0.5f) * 4.0f;
            float f15 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + (double)f12, this.posY + 2.0 + (double)f13, this.posZ + (double)f15, 0.0, 0.0, 0.0, new int[0]);
        } else {
            this.updateDragonEnderCrystal();
            float f11 = 0.2f / (MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0f + 1.0f);
            this.animTime = this.phaseManager.getCurrentPhase().getIsStationary() ? (this.animTime += 0.1f) : (this.slowed ? (this.animTime += f11 * 0.5f) : (this.animTime += (f11 *= (float)Math.pow(2.0, this.motionY))));
            this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
            if (this.isAIDisabled()) {
                this.animTime = 0.5f;
            } else {
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
                        double d5 = this.posX + (this.interpTargetX - this.posX) / (double)this.newPosRotationIncrements;
                        double d0 = this.posY + (this.interpTargetY - this.posY) / (double)this.newPosRotationIncrements;
                        double d1 = this.posZ + (this.interpTargetZ - this.posZ) / (double)this.newPosRotationIncrements;
                        double d2 = MathHelper.wrapDegrees(this.interpTargetYaw - (double)this.rotationYaw);
                        this.rotationYaw = (float)((double)this.rotationYaw + d2 / (double)this.newPosRotationIncrements);
                        this.rotationPitch = (float)((double)this.rotationPitch + (this.interpTargetPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
                        --this.newPosRotationIncrements;
                        this.setPosition(d5, d0, d1);
                        this.setRotation(this.rotationYaw, this.rotationPitch);
                    }
                    this.phaseManager.getCurrentPhase().doClientRenderEffects();
                } else {
                    Vec3d vec3d;
                    IPhase iphase = this.phaseManager.getCurrentPhase();
                    iphase.doLocalUpdate();
                    if (this.phaseManager.getCurrentPhase() != iphase) {
                        iphase = this.phaseManager.getCurrentPhase();
                        iphase.doLocalUpdate();
                    }
                    if ((vec3d = iphase.getTargetLocation()) != null) {
                        double d6 = vec3d.x - this.posX;
                        double d7 = vec3d.y - this.posY;
                        double d8 = vec3d.z - this.posZ;
                        double d3 = d6 * d6 + d7 * d7 + d8 * d8;
                        float f5 = iphase.getMaxRiseOrFall();
                        d7 = MathHelper.clamp(d7 / (double)MathHelper.sqrt(d6 * d6 + d8 * d8), (double)(-f5), (double)f5);
                        this.motionY += d7 * (double)0.1f;
                        this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
                        double d4 = MathHelper.clamp(MathHelper.wrapDegrees(180.0 - MathHelper.atan2(d6, d8) * 57.29577951308232 - (double)this.rotationYaw), -50.0, 50.0);
                        Vec3d vec3d1 = new Vec3d(vec3d.x - this.posX, vec3d.y - this.posY, vec3d.z - this.posZ).normalize();
                        Vec3d vec3d2 = new Vec3d(MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180)), this.motionY, -MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180))).normalize();
                        float f7 = Math.max(((float)vec3d2.dotProduct(vec3d1) + 0.5f) / 1.5f, 0.0f);
                        this.randomYawVelocity *= 0.8f;
                        this.randomYawVelocity = (float)((double)this.randomYawVelocity + d4 * (double)iphase.getYawFactor());
                        this.rotationYaw += this.randomYawVelocity * 0.1f;
                        float f8 = (float)(2.0 / (d3 + 1.0));
                        float f9 = 0.06f;
                        this.moveFlying(0.0f, 0.0f, -1.0f, 0.06f * (f7 * f8 + (1.0f - f8)));
                        if (this.slowed) {
                            this.moveEntity(MoverType.SELF, this.motionX * (double)0.8f, this.motionY * (double)0.8f, this.motionZ * (double)0.8f);
                        } else {
                            this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                        }
                        Vec3d vec3d3 = new Vec3d(this.motionX, this.motionY, this.motionZ).normalize();
                        float f10 = ((float)vec3d3.dotProduct(vec3d2) + 1.0f) / 2.0f;
                        f10 = 0.8f + 0.15f * f10;
                        this.motionX *= (double)f10;
                        this.motionZ *= (double)f10;
                        this.motionY *= (double)0.91f;
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
                Vec3d[] avec3d = new Vec3d[this.dragonPartArray.length];
                for (int j = 0; j < this.dragonPartArray.length; ++j) {
                    avec3d[j] = new Vec3d(this.dragonPartArray[j].posX, this.dragonPartArray[j].posY, this.dragonPartArray[j].posZ);
                }
                float f14 = (float)(this.getMovementOffsets(5, 1.0f)[1] - this.getMovementOffsets(10, 1.0f)[1]) * 10.0f * ((float)Math.PI / 180);
                float f16 = MathHelper.cos(f14);
                float f2 = MathHelper.sin(f14);
                float f17 = this.rotationYaw * ((float)Math.PI / 180);
                float f3 = MathHelper.sin(f17);
                float f18 = MathHelper.cos(f17);
                this.dragonPartBody.onUpdate();
                this.dragonPartBody.setLocationAndAngles(this.posX + (double)(f3 * 0.5f), this.posY, this.posZ - (double)(f18 * 0.5f), 0.0f, 0.0f);
                this.dragonPartWing1.onUpdate();
                this.dragonPartWing1.setLocationAndAngles(this.posX + (double)(f18 * 4.5f), this.posY + 2.0, this.posZ + (double)(f3 * 4.5f), 0.0f, 0.0f);
                this.dragonPartWing2.onUpdate();
                this.dragonPartWing2.setLocationAndAngles(this.posX - (double)(f18 * 4.5f), this.posY + 2.0, this.posZ - (double)(f3 * 4.5f), 0.0f, 0.0f);
                if (!this.world.isRemote && this.hurtTime == 0) {
                    this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                    this.collideWithEntities(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                    this.attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().expandXyz(1.0)));
                    this.attackEntitiesInList(this.world.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartNeck.getEntityBoundingBox().expandXyz(1.0)));
                }
                double[] adouble = this.getMovementOffsets(5, 1.0f);
                float f19 = MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180) - this.randomYawVelocity * 0.01f);
                float f4 = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180) - this.randomYawVelocity * 0.01f);
                this.dragonPartHead.onUpdate();
                this.dragonPartNeck.onUpdate();
                float f20 = this.getHeadYOffset(1.0f);
                this.dragonPartHead.setLocationAndAngles(this.posX + (double)(f19 * 6.5f * f16), this.posY + (double)f20 + (double)(f2 * 6.5f), this.posZ - (double)(f4 * 6.5f * f16), 0.0f, 0.0f);
                this.dragonPartNeck.setLocationAndAngles(this.posX + (double)(f19 * 5.5f * f16), this.posY + (double)f20 + (double)(f2 * 5.5f), this.posZ - (double)(f4 * 5.5f * f16), 0.0f, 0.0f);
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
                    double[] adouble1 = this.getMovementOffsets(12 + k * 2, 1.0f);
                    float f21 = this.rotationYaw * ((float)Math.PI / 180) + this.simplifyAngle(adouble1[0] - adouble[0]) * ((float)Math.PI / 180);
                    float f6 = MathHelper.sin(f21);
                    float f22 = MathHelper.cos(f21);
                    float f23 = 1.5f;
                    float f24 = (float)(k + 1) * 2.0f;
                    multipartentitypart.onUpdate();
                    multipartentitypart.setLocationAndAngles(this.posX - (double)((f3 * 1.5f + f6 * f24) * f16), this.posY + (adouble1[1] - adouble[1]) - (double)((f24 + 1.5f) * f2) + 1.5, this.posZ + (double)((f18 * 1.5f + f22 * f24) * f16), 0.0f, 0.0f);
                }
                if (!this.world.isRemote) {
                    this.slowed = this.destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartNeck.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox());
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

    private float getHeadYOffset(float p_184662_1_) {
        double d0;
        if (this.phaseManager.getCurrentPhase().getIsStationary()) {
            d0 = -1.0;
        } else {
            double[] adouble = this.getMovementOffsets(5, 1.0f);
            double[] adouble1 = this.getMovementOffsets(0, 1.0f);
            d0 = adouble[1] - adouble1[1];
        }
        return (float)d0;
    }

    private void updateDragonEnderCrystal() {
        if (this.healingEnderCrystal != null) {
            if (this.healingEnderCrystal.isDead) {
                this.healingEnderCrystal = null;
            } else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
                this.setHealth(this.getHealth() + 1.0f);
            }
        }
        if (this.rand.nextInt(10) == 0) {
            List<EntityEnderCrystal> list = this.world.getEntitiesWithinAABB(EntityEnderCrystal.class, this.getEntityBoundingBox().expandXyz(32.0));
            EntityEnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;
            for (EntityEnderCrystal entityendercrystal1 : list) {
                double d1 = entityendercrystal1.getDistanceSqToEntity(this);
                if (!(d1 < d0)) continue;
                d0 = d1;
                entityendercrystal = entityendercrystal1;
            }
            this.healingEnderCrystal = entityendercrystal;
        }
    }

    private void collideWithEntities(List<Entity> p_70970_1_) {
        double d0 = (this.dragonPartBody.getEntityBoundingBox().minX + this.dragonPartBody.getEntityBoundingBox().maxX) / 2.0;
        double d1 = (this.dragonPartBody.getEntityBoundingBox().minZ + this.dragonPartBody.getEntityBoundingBox().maxZ) / 2.0;
        for (Entity entity : p_70970_1_) {
            if (!(entity instanceof EntityLivingBase)) continue;
            double d2 = entity.posX - d0;
            double d3 = entity.posZ - d1;
            double d4 = d2 * d2 + d3 * d3;
            entity.addVelocity(d2 / d4 * 4.0, 0.2f, d3 / d4 * 4.0);
            if (this.phaseManager.getCurrentPhase().getIsStationary() || ((EntityLivingBase)entity).getRevengeTimer() >= entity.ticksExisted - 2) continue;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 5.0f);
            this.applyEnchantments(this, entity);
        }
    }

    private void attackEntitiesInList(List<Entity> p_70971_1_) {
        for (int i = 0; i < p_70971_1_.size(); ++i) {
            Entity entity = p_70971_1_.get(i);
            if (!(entity instanceof EntityLivingBase)) continue;
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0f);
            this.applyEnchantments(this, entity);
        }
    }

    private float simplifyAngle(double p_70973_1_) {
        return (float)MathHelper.wrapDegrees(p_70973_1_);
    }

    private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_) {
        int i = MathHelper.floor(p_70972_1_.minX);
        int j = MathHelper.floor(p_70972_1_.minY);
        int k = MathHelper.floor(p_70972_1_.minZ);
        int l = MathHelper.floor(p_70972_1_.maxX);
        int i1 = MathHelper.floor(p_70972_1_.maxY);
        int j1 = MathHelper.floor(p_70972_1_.maxZ);
        boolean flag = false;
        boolean flag1 = false;
        for (int k1 = i; k1 <= l; ++k1) {
            for (int l1 = j; l1 <= i1; ++l1) {
                for (int i2 = k; i2 <= j1; ++i2) {
                    BlockPos blockpos = new BlockPos(k1, l1, i2);
                    IBlockState iblockstate = this.world.getBlockState(blockpos);
                    Block block = iblockstate.getBlock();
                    if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.FIRE) continue;
                    if (!this.world.getGameRules().getBoolean("mobGriefing")) {
                        flag = true;
                        continue;
                    }
                    if (block != Blocks.BARRIER && block != Blocks.OBSIDIAN && block != Blocks.END_STONE && block != Blocks.BEDROCK && block != Blocks.END_PORTAL && block != Blocks.END_PORTAL_FRAME) {
                        if (block != Blocks.COMMAND_BLOCK && block != Blocks.REPEATING_COMMAND_BLOCK && block != Blocks.CHAIN_COMMAND_BLOCK && block != Blocks.IRON_BARS && block != Blocks.END_GATEWAY) {
                            flag1 = this.world.setBlockToAir(blockpos) || flag1;
                            continue;
                        }
                        flag = true;
                        continue;
                    }
                    flag = true;
                }
            }
        }
        if (flag1) {
            double d0 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * (double)this.rand.nextFloat();
            double d1 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * (double)this.rand.nextFloat();
            double d2 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * (double)this.rand.nextFloat();
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, 0.0, 0.0, 0.0, new int[0]);
        }
        return flag;
    }

    @Override
    public boolean attackEntityFromPart(MultiPartEntityPart dragonPart, DamageSource source, float damage) {
        damage = this.phaseManager.getCurrentPhase().getAdjustedDamage(dragonPart, source, damage);
        if (dragonPart != this.dragonPartHead) {
            damage = damage / 4.0f + Math.min(damage, 1.0f);
        }
        if (damage < 0.01f) {
            return false;
        }
        if (source.getEntity() instanceof EntityPlayer || source.isExplosion()) {
            float f = this.getHealth();
            this.attackDragonFrom(source, damage);
            if (this.getHealth() <= 0.0f && !this.phaseManager.getCurrentPhase().getIsStationary()) {
                this.setHealth(1.0f);
                this.phaseManager.setPhase(PhaseList.DYING);
            }
            if (this.phaseManager.getCurrentPhase().getIsStationary()) {
                this.sittingDamageReceived = (int)((float)this.sittingDamageReceived + (f - this.getHealth()));
                if ((float)this.sittingDamageReceived > 0.25f * this.getMaxHealth()) {
                    this.sittingDamageReceived = 0;
                    this.phaseManager.setPhase(PhaseList.TAKEOFF);
                }
            }
        }
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage()) {
            this.attackEntityFromPart(this.dragonPartBody, source, amount);
        }
        return false;
    }

    protected boolean attackDragonFrom(DamageSource source, float amount) {
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
            float f = (this.rand.nextFloat() - 0.5f) * 8.0f;
            float f1 = (this.rand.nextFloat() - 0.5f) * 4.0f;
            float f2 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + (double)f, this.posY + 2.0 + (double)f1, this.posZ + (double)f2, 0.0, 0.0, 0.0, new int[0]);
        }
        boolean flag = this.world.getGameRules().getBoolean("doMobLoot");
        int i = 500;
        if (this.fightManager != null && !this.fightManager.hasPreviouslyKilledDragon()) {
            i = 12000;
        }
        if (!this.world.isRemote) {
            if (this.deathTicks > 150 && this.deathTicks % 5 == 0 && flag) {
                this.dropExperience(MathHelper.floor((float)i * 0.08f));
            }
            if (this.deathTicks == 1) {
                this.world.playBroadcastSound(1028, new BlockPos(this), 0);
            }
        }
        this.moveEntity(MoverType.SELF, 0.0, 0.1f, 0.0);
        this.rotationYaw += 20.0f;
        this.renderYawOffset = this.rotationYaw;
        if (this.deathTicks == 200 && !this.world.isRemote) {
            if (flag) {
                this.dropExperience(MathHelper.floor((float)i * 0.2f));
            }
            if (this.fightManager != null) {
                this.fightManager.processDragonDeath(this);
            }
            this.setDead();
        }
    }

    private void dropExperience(int p_184668_1_) {
        while (p_184668_1_ > 0) {
            int i = EntityXPOrb.getXPSplit(p_184668_1_);
            p_184668_1_ -= i;
            this.world.spawnEntityInWorld(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, i));
        }
    }

    public int initPathPoints() {
        if (this.pathPoints[0] == null) {
            for (int i = 0; i < 24; ++i) {
                int i1;
                int l;
                int j = 5;
                if (i < 12) {
                    l = (int)(60.0f * MathHelper.cos(2.0f * ((float)(-Math.PI) + 0.2617994f * (float)i)));
                    i1 = (int)(60.0f * MathHelper.sin(2.0f * ((float)(-Math.PI) + 0.2617994f * (float)i)));
                } else if (i < 20) {
                    int lvt_3_1_ = i - 12;
                    l = (int)(40.0f * MathHelper.cos(2.0f * ((float)(-Math.PI) + 0.3926991f * (float)lvt_3_1_)));
                    i1 = (int)(40.0f * MathHelper.sin(2.0f * ((float)(-Math.PI) + 0.3926991f * (float)lvt_3_1_)));
                    j += 10;
                } else {
                    int k1 = i - 20;
                    l = (int)(20.0f * MathHelper.cos(2.0f * ((float)(-Math.PI) + 0.7853982f * (float)k1)));
                    i1 = (int)(20.0f * MathHelper.sin(2.0f * ((float)(-Math.PI) + 0.7853982f * (float)k1)));
                }
                int j1 = Math.max(this.world.getSeaLevel() + 10, this.world.getTopSolidOrLiquidBlock(new BlockPos(l, 0, i1)).getY() + j);
                this.pathPoints[i] = new PathPoint(l, j1, i1);
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
            this.neighbors[21] = 0xD0E000;
            this.neighbors[22] = 11763712;
            this.neighbors[23] = 0x7E0000;
        }
        return this.getNearestPpIdx(this.posX, this.posY, this.posZ);
    }

    public int getNearestPpIdx(double x, double y, double z) {
        float f = 10000.0f;
        int i = 0;
        PathPoint pathpoint = new PathPoint(MathHelper.floor(x), MathHelper.floor(y), MathHelper.floor(z));
        int j = 0;
        if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0) {
            j = 12;
        }
        for (int k = j; k < 24; ++k) {
            float f1;
            if (this.pathPoints[k] == null || !((f1 = this.pathPoints[k].distanceToSquared(pathpoint)) < f)) continue;
            f = f1;
            i = k;
        }
        return i;
    }

    @Nullable
    public Path findPath(int startIdx, int finishIdx, @Nullable PathPoint andThen) {
        for (int i = 0; i < 24; ++i) {
            PathPoint pathpoint = this.pathPoints[i];
            pathpoint.visited = false;
            pathpoint.distanceToTarget = 0.0f;
            pathpoint.totalPathDistance = 0.0f;
            pathpoint.distanceToNext = 0.0f;
            pathpoint.previous = null;
            pathpoint.index = -1;
        }
        PathPoint pathpoint4 = this.pathPoints[startIdx];
        PathPoint pathpoint5 = this.pathPoints[finishIdx];
        pathpoint4.totalPathDistance = 0.0f;
        pathpoint4.distanceToTarget = pathpoint4.distanceToNext = pathpoint4.distanceTo(pathpoint5);
        this.pathFindQueue.clearPath();
        this.pathFindQueue.addPoint(pathpoint4);
        PathPoint pathpoint1 = pathpoint4;
        int j = 0;
        if (this.fightManager == null || this.fightManager.getNumAliveCrystals() == 0) {
            j = 12;
        }
        while (!this.pathFindQueue.isPathEmpty()) {
            PathPoint pathpoint2 = this.pathFindQueue.dequeue();
            if (pathpoint2.equals(pathpoint5)) {
                if (andThen != null) {
                    andThen.previous = pathpoint5;
                    pathpoint5 = andThen;
                }
                return this.makePath(pathpoint4, pathpoint5);
            }
            if (pathpoint2.distanceTo(pathpoint5) < pathpoint1.distanceTo(pathpoint5)) {
                pathpoint1 = pathpoint2;
            }
            pathpoint2.visited = true;
            int k = 0;
            for (int l = 0; l < 24; ++l) {
                if (this.pathPoints[l] != pathpoint2) continue;
                k = l;
                break;
            }
            for (int i1 = j; i1 < 24; ++i1) {
                if ((this.neighbors[k] & 1 << i1) <= 0) continue;
                PathPoint pathpoint3 = this.pathPoints[i1];
                if (pathpoint3.visited) continue;
                float f = pathpoint2.totalPathDistance + pathpoint2.distanceTo(pathpoint3);
                if (pathpoint3.isAssigned() && !(f < pathpoint3.totalPathDistance)) continue;
                pathpoint3.previous = pathpoint2;
                pathpoint3.totalPathDistance = f;
                pathpoint3.distanceToNext = pathpoint3.distanceTo(pathpoint5);
                if (pathpoint3.isAssigned()) {
                    this.pathFindQueue.changeDistance(pathpoint3, pathpoint3.totalPathDistance + pathpoint3.distanceToNext);
                    continue;
                }
                pathpoint3.distanceToTarget = pathpoint3.totalPathDistance + pathpoint3.distanceToNext;
                this.pathFindQueue.addPoint(pathpoint3);
            }
        }
        if (pathpoint1 == pathpoint4) {
            return null;
        }
        LOGGER.debug("Failed to find path from {} to {}", startIdx, finishIdx);
        if (andThen != null) {
            andThen.previous = pathpoint1;
            pathpoint1 = andThen;
        }
        return this.makePath(pathpoint4, pathpoint1);
    }

    private Path makePath(PathPoint start, PathPoint finish) {
        int i = 1;
        PathPoint pathpoint = finish;
        while (pathpoint.previous != null) {
            ++i;
            pathpoint = pathpoint.previous;
        }
        PathPoint[] apathpoint = new PathPoint[i];
        PathPoint pathpoint1 = finish;
        apathpoint[--i] = finish;
        while (pathpoint1.previous != null) {
            pathpoint1 = pathpoint1.previous;
            apathpoint[--i] = pathpoint1;
        }
        return new Path(apathpoint);
    }

    public static void registerFixesDragon(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityDragon.class);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("DragonPhase", this.phaseManager.getCurrentPhase().getPhaseList().getId());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
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
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_ENDERDRAGON_HURT;
    }

    @Override
    protected float getSoundVolume() {
        return 5.0f;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.field_191189_ay;
    }

    public float getHeadPartYOffset(int p_184667_1_, double[] p_184667_2_, double[] p_184667_3_) {
        double d0;
        IPhase iphase = this.phaseManager.getCurrentPhase();
        PhaseList<? extends IPhase> phaselist = iphase.getPhaseList();
        if (phaselist != PhaseList.LANDING && phaselist != PhaseList.TAKEOFF) {
            d0 = iphase.getIsStationary() ? (double)p_184667_1_ : (p_184667_1_ == 6 ? 0.0 : p_184667_3_[1] - p_184667_2_[1]);
        } else {
            BlockPos blockpos = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
            float f = Math.max(MathHelper.sqrt(this.getDistanceSqToCenter(blockpos)) / 4.0f, 1.0f);
            d0 = (float)p_184667_1_ / f;
        }
        return (float)d0;
    }

    public Vec3d getHeadLookVec(float p_184665_1_) {
        Vec3d vec3d;
        IPhase iphase = this.phaseManager.getCurrentPhase();
        PhaseList<? extends IPhase> phaselist = iphase.getPhaseList();
        if (phaselist != PhaseList.LANDING && phaselist != PhaseList.TAKEOFF) {
            if (iphase.getIsStationary()) {
                float f4 = this.rotationPitch;
                float f5 = 1.5f;
                this.rotationPitch = -45.0f;
                vec3d = this.getLook(p_184665_1_);
                this.rotationPitch = f4;
            } else {
                vec3d = this.getLook(p_184665_1_);
            }
        } else {
            BlockPos blockpos = this.world.getTopSolidOrLiquidBlock(WorldGenEndPodium.END_PODIUM_LOCATION);
            float f = Math.max(MathHelper.sqrt(this.getDistanceSqToCenter(blockpos)) / 4.0f, 1.0f);
            float f1 = 6.0f / f;
            float f2 = this.rotationPitch;
            float f3 = 1.5f;
            this.rotationPitch = -f1 * 1.5f * 5.0f;
            vec3d = this.getLook(p_184665_1_);
            this.rotationPitch = f2;
        }
        return vec3d;
    }

    public void onCrystalDestroyed(EntityEnderCrystal crystal, BlockPos pos, DamageSource dmgSrc) {
        EntityPlayer entityplayer = dmgSrc.getEntity() instanceof EntityPlayer ? (EntityPlayer)dmgSrc.getEntity() : this.world.getNearestAttackablePlayer(pos, 64.0, 64.0);
        if (crystal == this.healingEnderCrystal) {
            this.attackEntityFromPart(this.dragonPartHead, DamageSource.causeExplosionDamage(entityplayer), 10.0f);
        }
        this.phaseManager.getCurrentPhase().onCrystalDestroyed(crystal, pos, dmgSrc, entityplayer);
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        if (PHASE.equals(key) && this.world.isRemote) {
            this.phaseManager.setPhase(PhaseList.getById(this.getDataManager().get(PHASE)));
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
    public void addPotionEffect(PotionEffect potioneffectIn) {
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return false;
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }
}

