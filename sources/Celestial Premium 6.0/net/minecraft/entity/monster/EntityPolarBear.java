/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityPolarBear
extends EntityAnimal {
    private static final DataParameter<Boolean> IS_STANDING = EntityDataManager.createKey(EntityPolarBear.class, DataSerializers.BOOLEAN);
    private float clientSideStandAnimation0;
    private float clientSideStandAnimation;
    private int warningSoundTicks;

    public EntityPolarBear(World worldIn) {
        super(worldIn);
        this.setSize(1.3f, 1.4f);
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityPolarBear(this.world);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new AIMeleeAttack());
        this.tasks.addTask(1, new AIPanic());
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.25));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new AIHurtByTarget());
        this.targetTasks.addTask(2, new AIAttackPlayer());
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isChild() ? SoundEvents.ENTITY_POLAR_BEAR_BABY_AMBIENT : SoundEvents.ENTITY_POLAR_BEAR_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_POLAR_BEAR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_POLAR_BEAR_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        this.playSound(SoundEvents.ENTITY_POLAR_BEAR_STEP, 0.15f, 1.0f);
    }

    protected void playWarningSound() {
        if (this.warningSoundTicks <= 0) {
            this.playSound(SoundEvents.ENTITY_POLAR_BEAR_WARNING, 1.0f, 1.0f);
            this.warningSoundTicks = 40;
        }
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_POLAR_BEAR;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(IS_STANDING, false);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote) {
            this.clientSideStandAnimation0 = this.clientSideStandAnimation;
            this.clientSideStandAnimation = this.isStanding() ? MathHelper.clamp(this.clientSideStandAnimation + 1.0f, 0.0f, 6.0f) : MathHelper.clamp(this.clientSideStandAnimation - 1.0f, 0.0f, 6.0f);
        }
        if (this.warningSoundTicks > 0) {
            --this.warningSoundTicks;
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (int)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        if (flag) {
            this.applyEnchantments(this, entityIn);
        }
        return flag;
    }

    public boolean isStanding() {
        return this.dataManager.get(IS_STANDING);
    }

    public void setStanding(boolean standing) {
        this.dataManager.set(IS_STANDING, standing);
    }

    public float getStandingAnimationScale(float p_189795_1_) {
        return (this.clientSideStandAnimation0 + (this.clientSideStandAnimation - this.clientSideStandAnimation0) * p_189795_1_) / 6.0f;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98f;
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        if (livingdata instanceof GroupData) {
            if (((GroupData)livingdata).madeParent) {
                this.setGrowingAge(-24000);
            }
        } else {
            GroupData entitypolarbear$groupdata = new GroupData();
            entitypolarbear$groupdata.madeParent = true;
            livingdata = entitypolarbear$groupdata;
        }
        return livingdata;
    }

    static class GroupData
    implements IEntityLivingData {
        public boolean madeParent;

        private GroupData() {
        }
    }

    class AIPanic
    extends EntityAIPanic {
        public AIPanic() {
            super(EntityPolarBear.this, 2.0);
        }

        @Override
        public boolean shouldExecute() {
            return !EntityPolarBear.this.isChild() && !EntityPolarBear.this.isBurning() ? false : super.shouldExecute();
        }
    }

    class AIMeleeAttack
    extends EntityAIAttackMelee {
        public AIMeleeAttack() {
            super(EntityPolarBear.this, 1.25, true);
        }

        @Override
        protected void checkAndPerformAttack(EntityLivingBase p_190102_1_, double p_190102_2_) {
            double d0 = this.getAttackReachSqr(p_190102_1_);
            if (p_190102_2_ <= d0 && this.attackTick <= 0) {
                this.attackTick = 20;
                this.attacker.attackEntityAsMob(p_190102_1_);
                EntityPolarBear.this.setStanding(false);
            } else if (p_190102_2_ <= d0 * 2.0) {
                if (this.attackTick <= 0) {
                    EntityPolarBear.this.setStanding(false);
                    this.attackTick = 20;
                }
                if (this.attackTick <= 10) {
                    EntityPolarBear.this.setStanding(true);
                    EntityPolarBear.this.playWarningSound();
                }
            } else {
                this.attackTick = 20;
                EntityPolarBear.this.setStanding(false);
            }
        }

        @Override
        public void resetTask() {
            EntityPolarBear.this.setStanding(false);
            super.resetTask();
        }

        @Override
        protected double getAttackReachSqr(EntityLivingBase attackTarget) {
            return 4.0f + attackTarget.width;
        }
    }

    class AIHurtByTarget
    extends EntityAIHurtByTarget {
        public AIHurtByTarget() {
            super((EntityCreature)EntityPolarBear.this, false, new Class[0]);
        }

        @Override
        public void startExecuting() {
            super.startExecuting();
            if (EntityPolarBear.this.isChild()) {
                this.alertOthers();
                this.resetTask();
            }
        }

        @Override
        protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
            if (creatureIn instanceof EntityPolarBear && !creatureIn.isChild()) {
                super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
            }
        }
    }

    class AIAttackPlayer
    extends EntityAINearestAttackableTarget<EntityPlayer> {
        public AIAttackPlayer() {
            super(EntityPolarBear.this, EntityPlayer.class, 20, true, true, null);
        }

        @Override
        public boolean shouldExecute() {
            if (EntityPolarBear.this.isChild()) {
                return false;
            }
            if (super.shouldExecute()) {
                for (EntityPolarBear entitypolarbear : EntityPolarBear.this.world.getEntitiesWithinAABB(EntityPolarBear.class, EntityPolarBear.this.getEntityBoundingBox().expand(8.0, 4.0, 8.0))) {
                    if (!entitypolarbear.isChild()) continue;
                    return true;
                }
            }
            EntityPolarBear.this.setAttackTarget(null);
            return false;
        }

        @Override
        protected double getTargetDistance() {
            return super.getTargetDistance() * 0.5;
        }
    }
}

