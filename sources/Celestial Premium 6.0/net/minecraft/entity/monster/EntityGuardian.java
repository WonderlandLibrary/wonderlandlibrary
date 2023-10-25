/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import com.google.common.base.Predicate;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityGuardian
extends EntityMob {
    private static final DataParameter<Boolean> field_190766_bz = EntityDataManager.createKey(EntityGuardian.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TARGET_ENTITY = EntityDataManager.createKey(EntityGuardian.class, DataSerializers.VARINT);
    protected float clientSideTailAnimation;
    protected float clientSideTailAnimationO;
    protected float clientSideTailAnimationSpeed;
    protected float clientSideSpikesAnimation;
    protected float clientSideSpikesAnimationO;
    private EntityLivingBase targetedEntity;
    private int clientSideAttackTime;
    private boolean clientSideTouchedGround;
    protected EntityAIWander wander;

    public EntityGuardian(World worldIn) {
        super(worldIn);
        this.experienceValue = 10;
        this.setSize(0.85f, 0.85f);
        this.moveHelper = new GuardianMoveHelper(this);
        this.clientSideTailAnimationO = this.clientSideTailAnimation = this.rand.nextFloat();
    }

    @Override
    protected void initEntityAI() {
        EntityAIMoveTowardsRestriction entityaimovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0);
        this.wander = new EntityAIWander(this, 1.0, 80);
        this.tasks.addTask(4, new AIGuardianAttack(this));
        this.tasks.addTask(5, entityaimovetowardsrestriction);
        this.tasks.addTask(7, this.wander);
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityGuardian.class, 12.0f, 0.01f));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.wander.setMutexBits(3);
        entityaimovetowardsrestriction.setMutexBits(3);
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<EntityLivingBase>(this, EntityLivingBase.class, 10, true, false, new GuardianTargetSelector(this)));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0);
    }

    public static void registerFixesGuardian(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityGuardian.class);
    }

    @Override
    protected PathNavigate getNewNavigator(World worldIn) {
        return new PathNavigateSwimmer(this, worldIn);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(field_190766_bz, false);
        this.dataManager.register(TARGET_ENTITY, 0);
    }

    public boolean isMoving() {
        return this.dataManager.get(field_190766_bz);
    }

    private void setMoving(boolean moving) {
        this.dataManager.set(field_190766_bz, moving);
    }

    public int getAttackDuration() {
        return 80;
    }

    private void setTargetedEntity(int entityId) {
        this.dataManager.set(TARGET_ENTITY, entityId);
    }

    public boolean hasTargetedEntity() {
        return this.dataManager.get(TARGET_ENTITY) != 0;
    }

    @Nullable
    public EntityLivingBase getTargetedEntity() {
        if (!this.hasTargetedEntity()) {
            return null;
        }
        if (this.world.isRemote) {
            if (this.targetedEntity != null) {
                return this.targetedEntity;
            }
            Entity entity = this.world.getEntityByID(this.dataManager.get(TARGET_ENTITY));
            if (entity instanceof EntityLivingBase) {
                this.targetedEntity = (EntityLivingBase)entity;
                return this.targetedEntity;
            }
            return null;
        }
        return this.getAttackTarget();
    }

    @Override
    public void notifyDataManagerChange(DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (TARGET_ENTITY.equals(key)) {
            this.clientSideAttackTime = 0;
            this.targetedEntity = null;
        }
    }

    @Override
    public int getTalkInterval() {
        return 160;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_AMBIENT : SoundEvents.ENTITY_GUARDIAN_AMBIENT_LAND;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_HURT : SoundEvents.ENTITY_GUARDIAN_HURT_LAND;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isInWater() ? SoundEvents.ENTITY_GUARDIAN_DEATH : SoundEvents.ENTITY_GUARDIAN_DEATH_LAND;
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.5f;
    }

    @Override
    public float getBlockPathWeight(BlockPos pos) {
        return this.world.getBlockState(pos).getMaterial() == Material.WATER ? 10.0f + this.world.getLightBrightness(pos) - 0.5f : super.getBlockPathWeight(pos);
    }

    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.clientSideTailAnimationO = this.clientSideTailAnimation;
            if (!this.isInWater()) {
                this.clientSideTailAnimationSpeed = 2.0f;
                if (this.motionY > 0.0 && this.clientSideTouchedGround && !this.isSilent()) {
                    this.world.playSound(this.posX, this.posY, this.posZ, this.func_190765_dj(), this.getSoundCategory(), 1.0f, 1.0f, false);
                }
                this.clientSideTouchedGround = this.motionY < 0.0 && this.world.isBlockNormalCube(new BlockPos(this).down(), false);
            } else {
                this.clientSideTailAnimationSpeed = this.isMoving() ? (this.clientSideTailAnimationSpeed < 0.5f ? 4.0f : (this.clientSideTailAnimationSpeed += (0.5f - this.clientSideTailAnimationSpeed) * 0.1f)) : (this.clientSideTailAnimationSpeed += (0.125f - this.clientSideTailAnimationSpeed) * 0.2f);
            }
            this.clientSideTailAnimation += this.clientSideTailAnimationSpeed;
            this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
            this.clientSideSpikesAnimation = !this.isInWater() ? this.rand.nextFloat() : (this.isMoving() ? (this.clientSideSpikesAnimation += (0.0f - this.clientSideSpikesAnimation) * 0.25f) : (this.clientSideSpikesAnimation += (1.0f - this.clientSideSpikesAnimation) * 0.06f));
            if (this.isMoving() && this.isInWater()) {
                Vec3d vec3d = this.getLook(0.0f);
                for (int i = 0; i < 2; ++i) {
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (this.rand.nextDouble() - 0.5) * (double)this.width - vec3d.x * 1.5, this.posY + this.rand.nextDouble() * (double)this.height - vec3d.y * 1.5, this.posZ + (this.rand.nextDouble() - 0.5) * (double)this.width - vec3d.z * 1.5, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (this.hasTargetedEntity()) {
                EntityLivingBase entitylivingbase;
                if (this.clientSideAttackTime < this.getAttackDuration()) {
                    ++this.clientSideAttackTime;
                }
                if ((entitylivingbase = this.getTargetedEntity()) != null) {
                    this.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0f, 90.0f);
                    this.getLookHelper().onUpdateLook();
                    double d5 = this.getAttackAnimationScale(0.0f);
                    double d0 = entitylivingbase.posX - this.posX;
                    double d1 = entitylivingbase.posY + (double)(entitylivingbase.height * 0.5f) - (this.posY + (double)this.getEyeHeight());
                    double d2 = entitylivingbase.posZ - this.posZ;
                    double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    d0 /= d3;
                    d1 /= d3;
                    d2 /= d3;
                    double d4 = this.rand.nextDouble();
                    while (d4 < d3) {
                        this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + d0 * (d4 += 1.8 - d5 + this.rand.nextDouble() * (1.7 - d5)), this.posY + d1 * d4 + (double)this.getEyeHeight(), this.posZ + d2 * d4, 0.0, 0.0, 0.0, new int[0]);
                    }
                }
            }
        }
        if (this.inWater) {
            this.setAir(300);
        } else if (this.onGround) {
            this.motionY += 0.5;
            this.motionX += (double)((this.rand.nextFloat() * 2.0f - 1.0f) * 0.4f);
            this.motionZ += (double)((this.rand.nextFloat() * 2.0f - 1.0f) * 0.4f);
            this.rotationYaw = this.rand.nextFloat() * 360.0f;
            this.onGround = false;
            this.isAirBorne = true;
        }
        if (this.hasTargetedEntity()) {
            this.rotationYaw = this.rotationYawHead;
        }
        super.onLivingUpdate();
    }

    protected SoundEvent func_190765_dj() {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }

    public float getTailAnimation(float p_175471_1_) {
        return this.clientSideTailAnimationO + (this.clientSideTailAnimation - this.clientSideTailAnimationO) * p_175471_1_;
    }

    public float getSpikesAnimation(float p_175469_1_) {
        return this.clientSideSpikesAnimationO + (this.clientSideSpikesAnimation - this.clientSideSpikesAnimationO) * p_175469_1_;
    }

    public float getAttackAnimationScale(float p_175477_1_) {
        return ((float)this.clientSideAttackTime + p_175477_1_) / (float)this.getAttackDuration();
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_GUARDIAN;
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    @Override
    public boolean isNotColliding() {
        return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty();
    }

    @Override
    public boolean getCanSpawnHere() {
        return (this.rand.nextInt(20) == 0 || !this.world.canBlockSeeSky(new BlockPos(this))) && super.getCanSpawnHere();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (!this.isMoving() && !source.isMagicDamage() && source.getSourceOfDamage() instanceof EntityLivingBase) {
            EntityLivingBase entitylivingbase = (EntityLivingBase)source.getSourceOfDamage();
            if (!source.isExplosion()) {
                entitylivingbase.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0f);
            }
        }
        if (this.wander != null) {
            this.wander.makeUpdate();
        }
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public int getVerticalFaceSpeed() {
        return 180;
    }

    @Override
    public void travel(float p_191986_1_, float p_191986_2_, float p_191986_3_) {
        if (this.isServerWorld() && this.isInWater()) {
            this.moveFlying(p_191986_1_, p_191986_2_, p_191986_3_, 0.1f);
            this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)0.9f;
            this.motionY *= (double)0.9f;
            this.motionZ *= (double)0.9f;
            if (!this.isMoving() && this.getAttackTarget() == null) {
                this.motionY -= 0.005;
            }
        } else {
            super.travel(p_191986_1_, p_191986_2_, p_191986_3_);
        }
    }

    static class GuardianTargetSelector
    implements Predicate<EntityLivingBase> {
        private final EntityGuardian parentEntity;

        public GuardianTargetSelector(EntityGuardian guardian) {
            this.parentEntity = guardian;
        }

        @Override
        public boolean apply(@Nullable EntityLivingBase p_apply_1_) {
            return (p_apply_1_ instanceof EntityPlayer || p_apply_1_ instanceof EntitySquid) && p_apply_1_.getDistanceSqToEntity(this.parentEntity) > 9.0;
        }
    }

    static class GuardianMoveHelper
    extends EntityMoveHelper {
        private final EntityGuardian entityGuardian;

        public GuardianMoveHelper(EntityGuardian guardian) {
            super(guardian);
            this.entityGuardian = guardian;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.entityGuardian.getNavigator().noPath()) {
                double d0 = this.posX - this.entityGuardian.posX;
                double d1 = this.posY - this.entityGuardian.posY;
                double d2 = this.posZ - this.entityGuardian.posZ;
                double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                float f = (float)(MathHelper.atan2(d2, d0) * 57.29577951308232) - 90.0f;
                this.entityGuardian.renderYawOffset = this.entityGuardian.rotationYaw = this.limitAngle(this.entityGuardian.rotationYaw, f, 90.0f);
                float f1 = (float)(this.speed * this.entityGuardian.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                this.entityGuardian.setAIMoveSpeed(this.entityGuardian.getAIMoveSpeed() + (f1 - this.entityGuardian.getAIMoveSpeed()) * 0.125f);
                double d4 = Math.sin((double)(this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.5) * 0.05;
                double d5 = Math.cos(this.entityGuardian.rotationYaw * ((float)Math.PI / 180));
                double d6 = Math.sin(this.entityGuardian.rotationYaw * ((float)Math.PI / 180));
                this.entityGuardian.motionX += d4 * d5;
                this.entityGuardian.motionZ += d4 * d6;
                d4 = Math.sin((double)(this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.75) * 0.05;
                this.entityGuardian.motionY += d4 * (d6 + d5) * 0.25;
                this.entityGuardian.motionY += (double)this.entityGuardian.getAIMoveSpeed() * (d1 /= d3) * 0.1;
                EntityLookHelper entitylookhelper = this.entityGuardian.getLookHelper();
                double d7 = this.entityGuardian.posX + d0 / d3 * 2.0;
                double d8 = (double)this.entityGuardian.getEyeHeight() + this.entityGuardian.posY + d1 / d3;
                double d9 = this.entityGuardian.posZ + d2 / d3 * 2.0;
                double d10 = entitylookhelper.getLookPosX();
                double d11 = entitylookhelper.getLookPosY();
                double d12 = entitylookhelper.getLookPosZ();
                if (!entitylookhelper.getIsLooking()) {
                    d10 = d7;
                    d11 = d8;
                    d12 = d9;
                }
                this.entityGuardian.getLookHelper().setLookPosition(d10 + (d7 - d10) * 0.125, d11 + (d8 - d11) * 0.125, d12 + (d9 - d12) * 0.125, 10.0f, 40.0f);
                this.entityGuardian.setMoving(true);
            } else {
                this.entityGuardian.setAIMoveSpeed(0.0f);
                this.entityGuardian.setMoving(false);
            }
        }
    }

    static class AIGuardianAttack
    extends EntityAIBase {
        private final EntityGuardian theEntity;
        private int tickCounter;
        private final boolean field_190881_c;

        public AIGuardianAttack(EntityGuardian guardian) {
            this.theEntity = guardian;
            this.field_190881_c = guardian instanceof EntityElderGuardian;
            this.setMutexBits(3);
        }

        @Override
        public boolean shouldExecute() {
            EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }

        @Override
        public boolean continueExecuting() {
            return super.continueExecuting() && (this.field_190881_c || this.theEntity.getDistanceSqToEntity(this.theEntity.getAttackTarget()) > 9.0);
        }

        @Override
        public void startExecuting() {
            this.tickCounter = -10;
            this.theEntity.getNavigator().clearPathEntity();
            this.theEntity.getLookHelper().setLookPositionWithEntity(this.theEntity.getAttackTarget(), 90.0f, 90.0f);
            this.theEntity.isAirBorne = true;
        }

        @Override
        public void resetTask() {
            this.theEntity.setTargetedEntity(0);
            this.theEntity.setAttackTarget(null);
            this.theEntity.wander.makeUpdate();
        }

        @Override
        public void updateTask() {
            EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
            this.theEntity.getNavigator().clearPathEntity();
            this.theEntity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0f, 90.0f);
            if (!this.theEntity.canEntityBeSeen(entitylivingbase)) {
                this.theEntity.setAttackTarget(null);
            } else {
                ++this.tickCounter;
                if (this.tickCounter == 0) {
                    this.theEntity.setTargetedEntity(this.theEntity.getAttackTarget().getEntityId());
                    this.theEntity.world.setEntityState(this.theEntity, (byte)21);
                } else if (this.tickCounter >= this.theEntity.getAttackDuration()) {
                    float f = 1.0f;
                    if (this.theEntity.world.getDifficulty() == EnumDifficulty.HARD) {
                        f += 2.0f;
                    }
                    if (this.field_190881_c) {
                        f += 2.0f;
                    }
                    entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.theEntity, this.theEntity), f);
                    entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(this.theEntity), (float)this.theEntity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
                    this.theEntity.setAttackTarget(null);
                }
                super.updateTask();
            }
        }
    }
}

