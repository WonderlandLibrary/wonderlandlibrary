// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.MoverType;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import javax.annotation.Nullable;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.SharedMonsterAttributes;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;

public class EntityGuardian extends EntityMob
{
    private static final DataParameter<Boolean> MOVING;
    private static final DataParameter<Integer> TARGET_ENTITY;
    protected float clientSideTailAnimation;
    protected float clientSideTailAnimationO;
    protected float clientSideTailAnimationSpeed;
    protected float clientSideSpikesAnimation;
    protected float clientSideSpikesAnimationO;
    private EntityLivingBase targetedEntity;
    private int clientSideAttackTime;
    private boolean clientSideTouchedGround;
    protected EntityAIWander wander;
    
    public EntityGuardian(final World worldIn) {
        super(worldIn);
        this.experienceValue = 10;
        this.setSize(0.85f, 0.85f);
        this.moveHelper = new GuardianMoveHelper(this);
        this.clientSideTailAnimation = this.rand.nextFloat();
        this.clientSideTailAnimationO = this.clientSideTailAnimation;
    }
    
    @Override
    protected void initEntityAI() {
        final EntityAIMoveTowardsRestriction entityaimovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0);
        this.wander = new EntityAIWander(this, 1.0, 80);
        this.tasks.addTask(4, new AIGuardianAttack(this));
        this.tasks.addTask(5, entityaimovetowardsrestriction);
        this.tasks.addTask(7, this.wander);
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityGuardian.class, 12.0f, 0.01f));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.wander.setMutexBits(3);
        entityaimovetowardsrestriction.setMutexBits(3);
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<Object>(this, EntityLivingBase.class, 10, true, false, (com.google.common.base.Predicate<?>)new GuardianTargetSelector(this)));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(16.0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0);
    }
    
    public static void registerFixesGuardian(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityGuardian.class);
    }
    
    @Override
    protected PathNavigate createNavigator(final World worldIn) {
        return new PathNavigateSwimmer(this, worldIn);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityGuardian.MOVING, false);
        this.dataManager.register(EntityGuardian.TARGET_ENTITY, 0);
    }
    
    public boolean isMoving() {
        return this.dataManager.get(EntityGuardian.MOVING);
    }
    
    private void setMoving(final boolean moving) {
        this.dataManager.set(EntityGuardian.MOVING, moving);
    }
    
    public int getAttackDuration() {
        return 80;
    }
    
    private void setTargetedEntity(final int entityId) {
        this.dataManager.set(EntityGuardian.TARGET_ENTITY, entityId);
    }
    
    public boolean hasTargetedEntity() {
        return this.dataManager.get(EntityGuardian.TARGET_ENTITY) != 0;
    }
    
    @Nullable
    public EntityLivingBase getTargetedEntity() {
        if (!this.hasTargetedEntity()) {
            return null;
        }
        if (!this.world.isRemote) {
            return this.getAttackTarget();
        }
        if (this.targetedEntity != null) {
            return this.targetedEntity;
        }
        final Entity entity = this.world.getEntityByID(this.dataManager.get(EntityGuardian.TARGET_ENTITY));
        if (entity instanceof EntityLivingBase) {
            return this.targetedEntity = (EntityLivingBase)entity;
        }
        return null;
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        super.notifyDataManagerChange(key);
        if (EntityGuardian.TARGET_ENTITY.equals(key)) {
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
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
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
    public float getBlockPathWeight(final BlockPos pos) {
        return (this.world.getBlockState(pos).getMaterial() == Material.WATER) ? (10.0f + this.world.getLightBrightness(pos) - 0.5f) : super.getBlockPathWeight(pos);
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.world.isRemote) {
            this.clientSideTailAnimationO = this.clientSideTailAnimation;
            if (!this.isInWater()) {
                this.clientSideTailAnimationSpeed = 2.0f;
                if (this.motionY > 0.0 && this.clientSideTouchedGround && !this.isSilent()) {
                    this.world.playSound(this.posX, this.posY, this.posZ, this.getFlopSound(), this.getSoundCategory(), 1.0f, 1.0f, false);
                }
                this.clientSideTouchedGround = (this.motionY < 0.0 && this.world.isBlockNormalCube(new BlockPos(this).down(), false));
            }
            else if (this.isMoving()) {
                if (this.clientSideTailAnimationSpeed < 0.5f) {
                    this.clientSideTailAnimationSpeed = 4.0f;
                }
                else {
                    this.clientSideTailAnimationSpeed += (0.5f - this.clientSideTailAnimationSpeed) * 0.1f;
                }
            }
            else {
                this.clientSideTailAnimationSpeed += (0.125f - this.clientSideTailAnimationSpeed) * 0.2f;
            }
            this.clientSideTailAnimation += this.clientSideTailAnimationSpeed;
            this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
            if (!this.isInWater()) {
                this.clientSideSpikesAnimation = this.rand.nextFloat();
            }
            else if (this.isMoving()) {
                this.clientSideSpikesAnimation += (0.0f - this.clientSideSpikesAnimation) * 0.25f;
            }
            else {
                this.clientSideSpikesAnimation += (1.0f - this.clientSideSpikesAnimation) * 0.06f;
            }
            if (this.isMoving() && this.isInWater()) {
                final Vec3d vec3d = this.getLook(0.0f);
                for (int i = 0; i < 2; ++i) {
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (this.rand.nextDouble() - 0.5) * this.width - vec3d.x * 1.5, this.posY + this.rand.nextDouble() * this.height - vec3d.y * 1.5, this.posZ + (this.rand.nextDouble() - 0.5) * this.width - vec3d.z * 1.5, 0.0, 0.0, 0.0, new int[0]);
                }
            }
            if (this.hasTargetedEntity()) {
                if (this.clientSideAttackTime < this.getAttackDuration()) {
                    ++this.clientSideAttackTime;
                }
                final EntityLivingBase entitylivingbase = this.getTargetedEntity();
                if (entitylivingbase != null) {
                    this.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0f, 90.0f);
                    this.getLookHelper().onUpdateLook();
                    final double d5 = this.getAttackAnimationScale(0.0f);
                    double d6 = entitylivingbase.posX - this.posX;
                    double d7 = entitylivingbase.posY + entitylivingbase.height * 0.5f - (this.posY + this.getEyeHeight());
                    double d8 = entitylivingbase.posZ - this.posZ;
                    final double d9 = Math.sqrt(d6 * d6 + d7 * d7 + d8 * d8);
                    d6 /= d9;
                    d7 /= d9;
                    d8 /= d9;
                    double d10 = this.rand.nextDouble();
                    while (d10 < d9) {
                        d10 += 1.8 - d5 + this.rand.nextDouble() * (1.7 - d5);
                        this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + d6 * d10, this.posY + d7 * d10 + this.getEyeHeight(), this.posZ + d8 * d10, 0.0, 0.0, 0.0, new int[0]);
                    }
                }
            }
        }
        if (this.inWater) {
            this.setAir(300);
        }
        else if (this.onGround) {
            this.motionY += 0.5;
            this.motionX += (this.rand.nextFloat() * 2.0f - 1.0f) * 0.4f;
            this.motionZ += (this.rand.nextFloat() * 2.0f - 1.0f) * 0.4f;
            this.rotationYaw = this.rand.nextFloat() * 360.0f;
            this.onGround = false;
            this.isAirBorne = true;
        }
        if (this.hasTargetedEntity()) {
            this.rotationYaw = this.rotationYawHead;
        }
        super.onLivingUpdate();
    }
    
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_GUARDIAN_FLOP;
    }
    
    public float getTailAnimation(final float p_175471_1_) {
        return this.clientSideTailAnimationO + (this.clientSideTailAnimation - this.clientSideTailAnimationO) * p_175471_1_;
    }
    
    public float getSpikesAnimation(final float p_175469_1_) {
        return this.clientSideSpikesAnimationO + (this.clientSideSpikesAnimation - this.clientSideSpikesAnimationO) * p_175469_1_;
    }
    
    public float getAttackAnimationScale(final float p_175477_1_) {
        return (this.clientSideAttackTime + p_175477_1_) / this.getAttackDuration();
    }
    
    @Nullable
    @Override
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
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (!this.isMoving() && !source.isMagicDamage() && source.getImmediateSource() instanceof EntityLivingBase) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase)source.getImmediateSource();
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
    public void travel(final float strafe, final float vertical, final float forward) {
        if (this.isServerWorld() && this.isInWater()) {
            this.moveRelative(strafe, vertical, forward, 0.1f);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.8999999761581421;
            this.motionY *= 0.8999999761581421;
            this.motionZ *= 0.8999999761581421;
            if (!this.isMoving() && this.getAttackTarget() == null) {
                this.motionY -= 0.005;
            }
        }
        else {
            super.travel(strafe, vertical, forward);
        }
    }
    
    static {
        MOVING = EntityDataManager.createKey(EntityGuardian.class, DataSerializers.BOOLEAN);
        TARGET_ENTITY = EntityDataManager.createKey(EntityGuardian.class, DataSerializers.VARINT);
    }
    
    static class AIGuardianAttack extends EntityAIBase
    {
        private final EntityGuardian guardian;
        private int tickCounter;
        private final boolean isElder;
        
        public AIGuardianAttack(final EntityGuardian guardian) {
            this.guardian = guardian;
            this.isElder = (guardian instanceof EntityElderGuardian);
            this.setMutexBits(3);
        }
        
        @Override
        public boolean shouldExecute() {
            final EntityLivingBase entitylivingbase = this.guardian.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            return super.shouldContinueExecuting() && (this.isElder || this.guardian.getDistanceSq(this.guardian.getAttackTarget()) > 9.0);
        }
        
        @Override
        public void startExecuting() {
            this.tickCounter = -10;
            this.guardian.getNavigator().clearPath();
            this.guardian.getLookHelper().setLookPositionWithEntity(this.guardian.getAttackTarget(), 90.0f, 90.0f);
            this.guardian.isAirBorne = true;
        }
        
        @Override
        public void resetTask() {
            this.guardian.setTargetedEntity(0);
            this.guardian.setAttackTarget(null);
            this.guardian.wander.makeUpdate();
        }
        
        @Override
        public void updateTask() {
            final EntityLivingBase entitylivingbase = this.guardian.getAttackTarget();
            this.guardian.getNavigator().clearPath();
            this.guardian.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0f, 90.0f);
            if (!this.guardian.canEntityBeSeen(entitylivingbase)) {
                this.guardian.setAttackTarget(null);
            }
            else {
                ++this.tickCounter;
                if (this.tickCounter == 0) {
                    this.guardian.setTargetedEntity(this.guardian.getAttackTarget().getEntityId());
                    this.guardian.world.setEntityState(this.guardian, (byte)21);
                }
                else if (this.tickCounter >= this.guardian.getAttackDuration()) {
                    float f = 1.0f;
                    if (this.guardian.world.getDifficulty() == EnumDifficulty.HARD) {
                        f += 2.0f;
                    }
                    if (this.isElder) {
                        f += 2.0f;
                    }
                    entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.guardian, this.guardian), f);
                    entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(this.guardian), (float)this.guardian.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
                    this.guardian.setAttackTarget(null);
                }
                super.updateTask();
            }
        }
    }
    
    static class GuardianMoveHelper extends EntityMoveHelper
    {
        private final EntityGuardian entityGuardian;
        
        public GuardianMoveHelper(final EntityGuardian guardian) {
            super(guardian);
            this.entityGuardian = guardian;
        }
        
        @Override
        public void onUpdateMoveHelper() {
            if (this.action == Action.MOVE_TO && !this.entityGuardian.getNavigator().noPath()) {
                final double d0 = this.posX - this.entityGuardian.posX;
                double d2 = this.posY - this.entityGuardian.posY;
                final double d3 = this.posZ - this.entityGuardian.posZ;
                final double d4 = MathHelper.sqrt(d0 * d0 + d2 * d2 + d3 * d3);
                d2 /= d4;
                final float f = (float)(MathHelper.atan2(d3, d0) * 57.29577951308232) - 90.0f;
                this.entityGuardian.rotationYaw = this.limitAngle(this.entityGuardian.rotationYaw, f, 90.0f);
                this.entityGuardian.renderYawOffset = this.entityGuardian.rotationYaw;
                final float f2 = (float)(this.speed * this.entityGuardian.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                this.entityGuardian.setAIMoveSpeed(this.entityGuardian.getAIMoveSpeed() + (f2 - this.entityGuardian.getAIMoveSpeed()) * 0.125f);
                double d5 = Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.5) * 0.05;
                final double d6 = Math.cos(this.entityGuardian.rotationYaw * 0.017453292f);
                final double d7 = Math.sin(this.entityGuardian.rotationYaw * 0.017453292f);
                final EntityGuardian entityGuardian = this.entityGuardian;
                entityGuardian.motionX += d5 * d6;
                final EntityGuardian entityGuardian2 = this.entityGuardian;
                entityGuardian2.motionZ += d5 * d7;
                d5 = Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.75) * 0.05;
                final EntityGuardian entityGuardian3 = this.entityGuardian;
                entityGuardian3.motionY += d5 * (d7 + d6) * 0.25;
                final EntityGuardian entityGuardian4 = this.entityGuardian;
                entityGuardian4.motionY += this.entityGuardian.getAIMoveSpeed() * d2 * 0.1;
                final EntityLookHelper entitylookhelper = this.entityGuardian.getLookHelper();
                final double d8 = this.entityGuardian.posX + d0 / d4 * 2.0;
                final double d9 = this.entityGuardian.getEyeHeight() + this.entityGuardian.posY + d2 / d4;
                final double d10 = this.entityGuardian.posZ + d3 / d4 * 2.0;
                double d11 = entitylookhelper.getLookPosX();
                double d12 = entitylookhelper.getLookPosY();
                double d13 = entitylookhelper.getLookPosZ();
                if (!entitylookhelper.getIsLooking()) {
                    d11 = d8;
                    d12 = d9;
                    d13 = d10;
                }
                this.entityGuardian.getLookHelper().setLookPosition(d11 + (d8 - d11) * 0.125, d12 + (d9 - d12) * 0.125, d13 + (d10 - d13) * 0.125, 10.0f, 40.0f);
                this.entityGuardian.setMoving(true);
            }
            else {
                this.entityGuardian.setAIMoveSpeed(0.0f);
                this.entityGuardian.setMoving(false);
            }
        }
    }
    
    static class GuardianTargetSelector implements Predicate<EntityLivingBase>
    {
        private final EntityGuardian parentEntity;
        
        public GuardianTargetSelector(final EntityGuardian guardian) {
            this.parentEntity = guardian;
        }
        
        public boolean apply(@Nullable final EntityLivingBase p_apply_1_) {
            return (p_apply_1_ instanceof EntityPlayer || p_apply_1_ instanceof EntitySquid) && p_apply_1_.getDistanceSq(this.parentEntity) > 9.0;
        }
    }
}
