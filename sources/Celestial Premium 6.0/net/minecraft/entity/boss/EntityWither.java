/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.boss;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityWither
extends EntityMob
implements IRangedAttackMob {
    private static final DataParameter<Integer> FIRST_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> SECOND_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> THIRD_HEAD_TARGET = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
    private static final DataParameter<Integer>[] HEAD_TARGETS = new DataParameter[]{FIRST_HEAD_TARGET, SECOND_HEAD_TARGET, THIRD_HEAD_TARGET};
    private static final DataParameter<Integer> INVULNERABILITY_TIME = EntityDataManager.createKey(EntityWither.class, DataSerializers.VARINT);
    private final float[] xRotationHeads = new float[2];
    private final float[] yRotationHeads = new float[2];
    private final float[] xRotOHeads = new float[2];
    private final float[] yRotOHeads = new float[2];
    private final int[] nextHeadUpdate = new int[2];
    private final int[] idleHeadUpdates = new int[2];
    private int blockBreakCounter;
    private final BossInfoServer bossInfo = (BossInfoServer)new BossInfoServer(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS).setDarkenSky(true);
    private static final Predicate<Entity> NOT_UNDEAD = new Predicate<Entity>(){

        @Override
        public boolean apply(@Nullable Entity p_apply_1_) {
            return p_apply_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_apply_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD && ((EntityLivingBase)p_apply_1_).func_190631_cK();
        }
    };

    public EntityWither(World worldIn) {
        super(worldIn);
        this.setHealth(this.getMaxHealth());
        this.setSize(0.9f, 3.5f);
        this.isImmuneToFire = true;
        ((PathNavigateGround)this.getNavigator()).setCanSwim(true);
        this.experienceValue = 50;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new AIDoNothing());
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIAttackRanged(this, 1.0, 40, 20.0f));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Entity>(this, EntityLiving.class, 0, false, false, NOT_UNDEAD));
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(FIRST_HEAD_TARGET, 0);
        this.dataManager.register(SECOND_HEAD_TARGET, 0);
        this.dataManager.register(THIRD_HEAD_TARGET, 0);
        this.dataManager.register(INVULNERABILITY_TIME, 0);
    }

    public static void registerFixesWither(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityWither.class);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Invul", this.getInvulTime());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setInvulTime(compound.getInteger("Invul"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomNameTag(String name) {
        super.setCustomNameTag(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_WITHER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_WITHER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WITHER_DEATH;
    }

    @Override
    public void onLivingUpdate() {
        Entity entity;
        this.motionY *= (double)0.6f;
        if (!this.world.isRemote && this.getWatchedTargetId(0) > 0 && (entity = this.world.getEntityByID(this.getWatchedTargetId(0))) != null) {
            double d1;
            double d0;
            double d3;
            if (this.posY < entity.posY || !this.isArmored() && this.posY < entity.posY + 5.0) {
                if (this.motionY < 0.0) {
                    this.motionY = 0.0;
                }
                this.motionY += (0.5 - this.motionY) * (double)0.6f;
            }
            if ((d3 = (d0 = entity.posX - this.posX) * d0 + (d1 = entity.posZ - this.posZ) * d1) > 9.0) {
                double d5 = MathHelper.sqrt(d3);
                this.motionX += (d0 / d5 * 0.5 - this.motionX) * (double)0.6f;
                this.motionZ += (d1 / d5 * 0.5 - this.motionZ) * (double)0.6f;
            }
        }
        if (this.motionX * this.motionX + this.motionZ * this.motionZ > (double)0.05f) {
            this.rotationYaw = (float)MathHelper.atan2(this.motionZ, this.motionX) * 57.295776f - 90.0f;
        }
        super.onLivingUpdate();
        for (int i = 0; i < 2; ++i) {
            this.yRotOHeads[i] = this.yRotationHeads[i];
            this.xRotOHeads[i] = this.xRotationHeads[i];
        }
        for (int j = 0; j < 2; ++j) {
            int k = this.getWatchedTargetId(j + 1);
            Entity entity1 = null;
            if (k > 0) {
                entity1 = this.world.getEntityByID(k);
            }
            if (entity1 != null) {
                double d11 = this.getHeadX(j + 1);
                double d12 = this.getHeadY(j + 1);
                double d13 = this.getHeadZ(j + 1);
                double d6 = entity1.posX - d11;
                double d7 = entity1.posY + (double)entity1.getEyeHeight() - d12;
                double d8 = entity1.posZ - d13;
                double d9 = MathHelper.sqrt(d6 * d6 + d8 * d8);
                float f = (float)(MathHelper.atan2(d8, d6) * 57.29577951308232) - 90.0f;
                float f1 = (float)(-(MathHelper.atan2(d7, d9) * 57.29577951308232));
                this.xRotationHeads[j] = this.rotlerp(this.xRotationHeads[j], f1, 40.0f);
                this.yRotationHeads[j] = this.rotlerp(this.yRotationHeads[j], f, 10.0f);
                continue;
            }
            this.yRotationHeads[j] = this.rotlerp(this.yRotationHeads[j], this.renderYawOffset, 10.0f);
        }
        boolean flag = this.isArmored();
        for (int l = 0; l < 3; ++l) {
            double d10 = this.getHeadX(l);
            double d2 = this.getHeadY(l);
            double d4 = this.getHeadZ(l);
            this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d10 + this.rand.nextGaussian() * (double)0.3f, d2 + this.rand.nextGaussian() * (double)0.3f, d4 + this.rand.nextGaussian() * (double)0.3f, 0.0, 0.0, 0.0, new int[0]);
            if (!flag || this.world.rand.nextInt(4) != 0) continue;
            this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, d10 + this.rand.nextGaussian() * (double)0.3f, d2 + this.rand.nextGaussian() * (double)0.3f, d4 + this.rand.nextGaussian() * (double)0.3f, (double)0.7f, (double)0.7f, 0.5, new int[0]);
        }
        if (this.getInvulTime() > 0) {
            for (int i1 = 0; i1 < 3; ++i1) {
                this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian(), this.posY + (double)(this.rand.nextFloat() * 3.3f), this.posZ + this.rand.nextGaussian(), (double)0.7f, (double)0.7f, (double)0.9f, new int[0]);
            }
        }
    }

    @Override
    protected void updateAITasks() {
        if (this.getInvulTime() > 0) {
            int j1 = this.getInvulTime() - 1;
            if (j1 <= 0) {
                this.world.newExplosion(this, this.posX, this.posY + (double)this.getEyeHeight(), this.posZ, 7.0f, false, this.world.getGameRules().getBoolean("mobGriefing"));
                this.world.playBroadcastSound(1023, new BlockPos(this), 0);
            }
            this.setInvulTime(j1);
            if (this.ticksExisted % 10 == 0) {
                this.heal(10.0f);
            }
        } else {
            super.updateAITasks();
            block0: for (int i = 1; i < 3; ++i) {
                int k1;
                if (this.ticksExisted < this.nextHeadUpdate[i - 1]) continue;
                this.nextHeadUpdate[i - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);
                if (this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) {
                    int j3 = i - 1;
                    int k3 = this.idleHeadUpdates[i - 1];
                    this.idleHeadUpdates[j3] = this.idleHeadUpdates[i - 1] + 1;
                    if (k3 > 15) {
                        float f = 10.0f;
                        float f1 = 5.0f;
                        double d0 = MathHelper.nextDouble(this.rand, this.posX - 10.0, this.posX + 10.0);
                        double d1 = MathHelper.nextDouble(this.rand, this.posY - 5.0, this.posY + 5.0);
                        double d2 = MathHelper.nextDouble(this.rand, this.posZ - 10.0, this.posZ + 10.0);
                        this.launchWitherSkullToCoords(i + 1, d0, d1, d2, true);
                        this.idleHeadUpdates[i - 1] = 0;
                    }
                }
                if ((k1 = this.getWatchedTargetId(i)) > 0) {
                    Entity entity = this.world.getEntityByID(k1);
                    if (entity != null && entity.isEntityAlive() && this.getDistanceSqToEntity(entity) <= 900.0 && this.canEntityBeSeen(entity)) {
                        if (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.disableDamage) {
                            this.updateWatchedTargetId(i, 0);
                            continue;
                        }
                        this.launchWitherSkullToEntity(i + 1, (EntityLivingBase)entity);
                        this.nextHeadUpdate[i - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                        this.idleHeadUpdates[i - 1] = 0;
                        continue;
                    }
                    this.updateWatchedTargetId(i, 0);
                    continue;
                }
                List<Entity> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(20.0, 8.0, 20.0), Predicates.and(NOT_UNDEAD, EntitySelectors.NOT_SPECTATING));
                for (int j2 = 0; j2 < 10 && !list.isEmpty(); ++j2) {
                    EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(this.rand.nextInt(list.size()));
                    if (entitylivingbase != this && entitylivingbase.isEntityAlive() && this.canEntityBeSeen(entitylivingbase)) {
                        if (entitylivingbase instanceof EntityPlayer) {
                            if (((EntityPlayer)entitylivingbase).capabilities.disableDamage) continue block0;
                            this.updateWatchedTargetId(i, entitylivingbase.getEntityId());
                            continue block0;
                        }
                        this.updateWatchedTargetId(i, entitylivingbase.getEntityId());
                        continue block0;
                    }
                    list.remove(entitylivingbase);
                }
            }
            if (this.getAttackTarget() != null) {
                this.updateWatchedTargetId(0, this.getAttackTarget().getEntityId());
            } else {
                this.updateWatchedTargetId(0, 0);
            }
            if (this.blockBreakCounter > 0) {
                --this.blockBreakCounter;
                if (this.blockBreakCounter == 0 && this.world.getGameRules().getBoolean("mobGriefing")) {
                    int i1 = MathHelper.floor(this.posY);
                    int l1 = MathHelper.floor(this.posX);
                    int i2 = MathHelper.floor(this.posZ);
                    boolean flag = false;
                    for (int k2 = -1; k2 <= 1; ++k2) {
                        for (int l2 = -1; l2 <= 1; ++l2) {
                            for (int j = 0; j <= 3; ++j) {
                                int i3 = l1 + k2;
                                int k = i1 + j;
                                int l = i2 + l2;
                                BlockPos blockpos = new BlockPos(i3, k, l);
                                IBlockState iblockstate = this.world.getBlockState(blockpos);
                                Block block = iblockstate.getBlock();
                                if (iblockstate.getMaterial() == Material.AIR || !EntityWither.canDestroyBlock(block)) continue;
                                flag = this.world.destroyBlock(blockpos, true) || flag;
                            }
                        }
                    }
                    if (flag) {
                        this.world.playEvent(null, 1022, new BlockPos(this), 0);
                    }
                }
            }
            if (this.ticksExisted % 20 == 0) {
                this.heal(1.0f);
            }
            this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        }
    }

    public static boolean canDestroyBlock(Block blockIn) {
        return blockIn != Blocks.BEDROCK && blockIn != Blocks.END_PORTAL && blockIn != Blocks.END_PORTAL_FRAME && blockIn != Blocks.COMMAND_BLOCK && blockIn != Blocks.REPEATING_COMMAND_BLOCK && blockIn != Blocks.CHAIN_COMMAND_BLOCK && blockIn != Blocks.BARRIER && blockIn != Blocks.STRUCTURE_BLOCK && blockIn != Blocks.STRUCTURE_VOID && blockIn != Blocks.PISTON_EXTENSION && blockIn != Blocks.END_GATEWAY;
    }

    public void ignite() {
        this.setInvulTime(220);
        this.setHealth(this.getMaxHealth() / 3.0f);
    }

    @Override
    public void setInWeb() {
    }

    @Override
    public void addTrackingPlayer(EntityPlayerMP player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(EntityPlayerMP player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    private double getHeadX(int p_82214_1_) {
        if (p_82214_1_ <= 0) {
            return this.posX;
        }
        float f = (this.renderYawOffset + (float)(180 * (p_82214_1_ - 1))) * ((float)Math.PI / 180);
        float f1 = MathHelper.cos(f);
        return this.posX + (double)f1 * 1.3;
    }

    private double getHeadY(int p_82208_1_) {
        return p_82208_1_ <= 0 ? this.posY + 3.0 : this.posY + 2.2;
    }

    private double getHeadZ(int p_82213_1_) {
        if (p_82213_1_ <= 0) {
            return this.posZ;
        }
        float f = (this.renderYawOffset + (float)(180 * (p_82213_1_ - 1))) * ((float)Math.PI / 180);
        float f1 = MathHelper.sin(f);
        return this.posZ + (double)f1 * 1.3;
    }

    private float rotlerp(float p_82204_1_, float p_82204_2_, float p_82204_3_) {
        float f = MathHelper.wrapDegrees(p_82204_2_ - p_82204_1_);
        if (f > p_82204_3_) {
            f = p_82204_3_;
        }
        if (f < -p_82204_3_) {
            f = -p_82204_3_;
        }
        return p_82204_1_ + f;
    }

    private void launchWitherSkullToEntity(int p_82216_1_, EntityLivingBase p_82216_2_) {
        this.launchWitherSkullToCoords(p_82216_1_, p_82216_2_.posX, p_82216_2_.posY + (double)p_82216_2_.getEyeHeight() * 0.5, p_82216_2_.posZ, p_82216_1_ == 0 && this.rand.nextFloat() < 0.001f);
    }

    private void launchWitherSkullToCoords(int p_82209_1_, double x, double y, double z, boolean invulnerable) {
        this.world.playEvent(null, 1024, new BlockPos(this), 0);
        double d0 = this.getHeadX(p_82209_1_);
        double d1 = this.getHeadY(p_82209_1_);
        double d2 = this.getHeadZ(p_82209_1_);
        double d3 = x - d0;
        double d4 = y - d1;
        double d5 = z - d2;
        EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, this, d3, d4, d5);
        if (invulnerable) {
            entitywitherskull.setInvulnerable(true);
        }
        entitywitherskull.posY = d1;
        entitywitherskull.posX = d0;
        entitywitherskull.posZ = d2;
        this.world.spawnEntityInWorld(entitywitherskull);
    }

    @Override
    public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
        this.launchWitherSkullToEntity(0, target);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (source != DamageSource.drown && !(source.getEntity() instanceof EntityWither)) {
            Entity entity;
            if (this.getInvulTime() > 0 && source != DamageSource.outOfWorld) {
                return false;
            }
            if (this.isArmored() && (entity = source.getSourceOfDamage()) instanceof EntityArrow) {
                return false;
            }
            Entity entity1 = source.getEntity();
            if (entity1 != null && !(entity1 instanceof EntityPlayer) && entity1 instanceof EntityLivingBase && ((EntityLivingBase)entity1).getCreatureAttribute() == this.getCreatureAttribute()) {
                return false;
            }
            if (this.blockBreakCounter <= 0) {
                this.blockBreakCounter = 20;
            }
            int i = 0;
            while (i < this.idleHeadUpdates.length) {
                int n = i++;
                this.idleHeadUpdates[n] = this.idleHeadUpdates[n] + 3;
            }
            return super.attackEntityFrom(source, amount);
        }
        return false;
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        EntityItem entityitem = this.dropItem(Items.NETHER_STAR, 1);
        if (entityitem != null) {
            entityitem.setNoDespawn();
        }
    }

    @Override
    protected void despawnEntity() {
        this.entityAge = 0;
    }

    @Override
    public int getBrightnessForRender() {
        return 0xF000F0;
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void addPotionEffect(PotionEffect potioneffectIn) {
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.6f);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(40.0);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(4.0);
    }

    public float getHeadYRotation(int p_82207_1_) {
        return this.yRotationHeads[p_82207_1_];
    }

    public float getHeadXRotation(int p_82210_1_) {
        return this.xRotationHeads[p_82210_1_];
    }

    public int getInvulTime() {
        return this.dataManager.get(INVULNERABILITY_TIME);
    }

    public void setInvulTime(int time) {
        this.dataManager.set(INVULNERABILITY_TIME, time);
    }

    public int getWatchedTargetId(int head) {
        return this.dataManager.get(HEAD_TARGETS[head]);
    }

    public void updateWatchedTargetId(int targetOffset, int newId) {
        this.dataManager.set(HEAD_TARGETS[targetOffset], newId);
    }

    public boolean isArmored() {
        return this.getHealth() <= this.getMaxHealth() / 2.0f;
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }

    @Override
    protected boolean canBeRidden(Entity entityIn) {
        return false;
    }

    @Override
    public boolean isNonBoss() {
        return false;
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    }

    class AIDoNothing
    extends EntityAIBase {
        public AIDoNothing() {
            this.setMutexBits(7);
        }

        @Override
        public boolean shouldExecute() {
            return EntityWither.this.getInvulTime() > 0;
        }
    }
}

