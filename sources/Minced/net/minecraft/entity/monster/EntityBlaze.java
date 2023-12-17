// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public class EntityBlaze extends EntityMob
{
    private float heightOffset;
    private int heightOffsetUpdateTime;
    private static final DataParameter<Byte> ON_FIRE;
    
    public EntityBlaze(final World worldIn) {
        super(worldIn);
        this.heightOffset = 0.5f;
        this.setPathPriority(PathNodeType.WATER, -1.0f);
        this.setPathPriority(PathNodeType.LAVA, 8.0f);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0f);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0f);
        this.isImmuneToFire = true;
        this.experienceValue = 10;
    }
    
    public static void registerFixesBlaze(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityBlaze.class);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(4, new AIFireballAttack(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0, 0.0f));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, (Class<?>[])new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityBlaze.ON_FIRE, (Byte)0);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_BLAZE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_BLAZE_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BLAZE_DEATH;
    }
    
    @Override
    public int getBrightnessForRender() {
        return 15728880;
    }
    
    @Override
    public float getBrightness() {
        return 1.0f;
    }
    
    @Override
    public void onLivingUpdate() {
        if (!this.onGround && this.motionY < 0.0) {
            this.motionY *= 0.6;
        }
        if (this.world.isRemote) {
            if (this.rand.nextInt(24) == 0 && !this.isSilent()) {
                this.world.playSound(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, SoundEvents.ENTITY_BLAZE_BURN, this.getSoundCategory(), 1.0f + this.rand.nextFloat(), this.rand.nextFloat() * 0.7f + 0.3f, false);
            }
            for (int i = 0; i < 2; ++i) {
                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5) * this.width, 0.0, 0.0, 0.0, new int[0]);
            }
        }
        super.onLivingUpdate();
    }
    
    @Override
    protected void updateAITasks() {
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.DROWN, 1.0f);
        }
        --this.heightOffsetUpdateTime;
        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5f + (float)this.rand.nextGaussian() * 3.0f;
        }
        final EntityLivingBase entitylivingbase = this.getAttackTarget();
        if (entitylivingbase != null && entitylivingbase.posY + entitylivingbase.getEyeHeight() > this.posY + this.getEyeHeight() + this.heightOffset) {
            this.motionY += (0.30000001192092896 - this.motionY) * 0.30000001192092896;
            this.isAirBorne = true;
        }
        super.updateAITasks();
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    public boolean isBurning() {
        return this.isCharged();
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_BLAZE;
    }
    
    public boolean isCharged() {
        return (this.dataManager.get(EntityBlaze.ON_FIRE) & 0x1) != 0x0;
    }
    
    public void setOnFire(final boolean onFire) {
        byte b0 = this.dataManager.get(EntityBlaze.ON_FIRE);
        if (onFire) {
            b0 |= 0x1;
        }
        else {
            b0 &= 0xFFFFFFFE;
        }
        this.dataManager.set(EntityBlaze.ON_FIRE, b0);
    }
    
    @Override
    protected boolean isValidLightLevel() {
        return true;
    }
    
    static {
        ON_FIRE = EntityDataManager.createKey(EntityBlaze.class, DataSerializers.BYTE);
    }
    
    static class AIFireballAttack extends EntityAIBase
    {
        private final EntityBlaze blaze;
        private int attackStep;
        private int attackTime;
        
        public AIFireballAttack(final EntityBlaze blazeIn) {
            this.blaze = blazeIn;
            this.setMutexBits(3);
        }
        
        @Override
        public boolean shouldExecute() {
            final EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }
        
        @Override
        public void startExecuting() {
            this.attackStep = 0;
        }
        
        @Override
        public void resetTask() {
            this.blaze.setOnFire(false);
        }
        
        @Override
        public void updateTask() {
            --this.attackTime;
            final EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
            final double d0 = this.blaze.getDistanceSq(entitylivingbase);
            if (d0 < 4.0) {
                if (this.attackTime <= 0) {
                    this.attackTime = 20;
                    this.blaze.attackEntityAsMob(entitylivingbase);
                }
                this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0);
            }
            else if (d0 < this.getFollowDistance() * this.getFollowDistance()) {
                final double d2 = entitylivingbase.posX - this.blaze.posX;
                final double d3 = entitylivingbase.getEntityBoundingBox().minY + entitylivingbase.height / 2.0f - (this.blaze.posY + this.blaze.height / 2.0f);
                final double d4 = entitylivingbase.posZ - this.blaze.posZ;
                if (this.attackTime <= 0) {
                    ++this.attackStep;
                    if (this.attackStep == 1) {
                        this.attackTime = 60;
                        this.blaze.setOnFire(true);
                    }
                    else if (this.attackStep <= 4) {
                        this.attackTime = 6;
                    }
                    else {
                        this.attackTime = 100;
                        this.attackStep = 0;
                        this.blaze.setOnFire(false);
                    }
                    if (this.attackStep > 1) {
                        final float f = MathHelper.sqrt(MathHelper.sqrt(d0)) * 0.5f;
                        this.blaze.world.playEvent(null, 1018, new BlockPos((int)this.blaze.posX, (int)this.blaze.posY, (int)this.blaze.posZ), 0);
                        for (int i = 0; i < 1; ++i) {
                            final EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.blaze.world, this.blaze, d2 + this.blaze.getRNG().nextGaussian() * f, d3, d4 + this.blaze.getRNG().nextGaussian() * f);
                            entitysmallfireball.posY = this.blaze.posY + this.blaze.height / 2.0f + 0.5;
                            this.blaze.world.spawnEntity(entitysmallfireball);
                        }
                    }
                }
                this.blaze.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0f, 10.0f);
            }
            else {
                this.blaze.getNavigator().clearPath();
                this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0);
            }
            super.updateTask();
        }
        
        private double getFollowDistance() {
            final IAttributeInstance iattributeinstance = this.blaze.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
            return (iattributeinstance == null) ? 16.0 : iattributeinstance.getAttributeValue();
        }
    }
}
