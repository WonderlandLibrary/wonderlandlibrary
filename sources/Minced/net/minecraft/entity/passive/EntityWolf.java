// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import java.util.Random;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityCreeper;
import java.util.UUID;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.init.SoundEvents;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public class EntityWolf extends EntityTameable
{
    private static final DataParameter<Float> DATA_HEALTH_ID;
    private static final DataParameter<Boolean> BEGGING;
    private static final DataParameter<Integer> COLLAR_COLOR;
    private float headRotationCourse;
    private float headRotationCourseOld;
    private boolean isWet;
    private boolean isShaking;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;
    
    public EntityWolf(final World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 0.85f);
        this.setTamed(false);
    }
    
    @Override
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new AIAvoidEntity<Object>(this, EntityLlama.class, 24.0f, 1.5, 1.5));
        this.tasks.addTask(4, new EntityAILeapAtTarget(this, 0.4f));
        this.tasks.addTask(5, new EntityAIAttackMelee(this, 1.0, true));
        this.tasks.addTask(6, new EntityAIFollowOwner(this, 1.0, 10.0f, 2.0f));
        this.tasks.addTask(7, new EntityAIMate(this, 1.0));
        this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 1.0));
        this.tasks.addTask(9, new EntityAIBeg(this, 8.0f));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(10, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true, (Class<?>[])new Class[0]));
        this.targetTasks.addTask(4, new EntityAITargetNonTamed<Object>(this, EntityAnimal.class, false, (com.google.common.base.Predicate<?>)new Predicate<Entity>() {
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return p_apply_1_ instanceof EntitySheep || p_apply_1_ instanceof EntityRabbit;
            }
        }));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<Object>(this, AbstractSkeleton.class, false));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896);
        if (this.isTamed()) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0);
        }
        else {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0);
        }
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0);
    }
    
    @Override
    public void setAttackTarget(@Nullable final EntityLivingBase entitylivingbaseIn) {
        super.setAttackTarget(entitylivingbaseIn);
        if (entitylivingbaseIn == null) {
            this.setAngry(false);
        }
        else if (!this.isTamed()) {
            this.setAngry(true);
        }
    }
    
    @Override
    protected void updateAITasks() {
        this.dataManager.set(EntityWolf.DATA_HEALTH_ID, this.getHealth());
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityWolf.DATA_HEALTH_ID, this.getHealth());
        this.dataManager.register(EntityWolf.BEGGING, false);
        this.dataManager.register(EntityWolf.COLLAR_COLOR, EnumDyeColor.RED.getDyeDamage());
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.15f, 1.0f);
    }
    
    public static void registerFixesWolf(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityWolf.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Angry", this.isAngry());
        compound.setByte("CollarColor", (byte)this.getCollarColor().getDyeDamage());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setAngry(compound.getBoolean("Angry"));
        if (compound.hasKey("CollarColor", 99)) {
            this.setCollarColor(EnumDyeColor.byDyeDamage(compound.getByte("CollarColor")));
        }
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isAngry()) {
            return SoundEvents.ENTITY_WOLF_GROWL;
        }
        if (this.rand.nextInt(3) == 0) {
            return (this.isTamed() && this.dataManager.get(EntityWolf.DATA_HEALTH_ID) < 10.0f) ? SoundEvents.ENTITY_WOLF_WHINE : SoundEvents.ENTITY_WOLF_PANT;
        }
        return SoundEvents.ENTITY_WOLF_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_WOLF_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_WOLF_DEATH;
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_WOLF;
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.world.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0f;
            this.prevTimeWolfIsShaking = 0.0f;
            this.world.setEntityState(this, (byte)8);
        }
        if (!this.world.isRemote && this.getAttackTarget() == null && this.isAngry()) {
            this.setAngry(false);
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.headRotationCourseOld = this.headRotationCourse;
        if (this.isBegging()) {
            this.headRotationCourse += (1.0f - this.headRotationCourse) * 0.4f;
        }
        else {
            this.headRotationCourse += (0.0f - this.headRotationCourse) * 0.4f;
        }
        if (this.isWet()) {
            this.isWet = true;
            this.isShaking = false;
            this.timeWolfIsShaking = 0.0f;
            this.prevTimeWolfIsShaking = 0.0f;
        }
        else if ((this.isWet || this.isShaking) && this.isShaking) {
            if (this.timeWolfIsShaking == 0.0f) {
                this.playSound(SoundEvents.ENTITY_WOLF_SHAKE, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            }
            this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
            this.timeWolfIsShaking += 0.05f;
            if (this.prevTimeWolfIsShaking >= 2.0f) {
                this.isWet = false;
                this.isShaking = false;
                this.prevTimeWolfIsShaking = 0.0f;
                this.timeWolfIsShaking = 0.0f;
            }
            if (this.timeWolfIsShaking > 0.4f) {
                final float f = (float)this.getEntityBoundingBox().minY;
                for (int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4f) * 3.1415927f) * 7.0f), j = 0; j < i; ++j) {
                    final float f2 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width * 0.5f;
                    final float f3 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width * 0.5f;
                    this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + f2, f + 0.8f, this.posZ + f3, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
            }
        }
    }
    
    public boolean isWolfWet() {
        return this.isWet;
    }
    
    public float getShadingWhileWet(final float p_70915_1_) {
        return 0.75f + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0f * 0.25f;
    }
    
    public float getShakeAngle(final float p_70923_1_, final float p_70923_2_) {
        float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8f;
        if (f < 0.0f) {
            f = 0.0f;
        }
        else if (f > 1.0f) {
            f = 1.0f;
        }
        return MathHelper.sin(f * 3.1415927f) * MathHelper.sin(f * 3.1415927f * 11.0f) * 0.15f * 3.1415927f;
    }
    
    public float getInterestedAngle(final float p_70917_1_) {
        return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * p_70917_1_) * 0.15f * 3.1415927f;
    }
    
    @Override
    public float getEyeHeight() {
        return this.height * 0.8f;
    }
    
    @Override
    public int getVerticalFaceSpeed() {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        final Entity entity = source.getTrueSource();
        if (this.aiSit != null) {
            this.aiSit.setSitting(false);
        }
        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
            amount = (amount + 1.0f) / 2.0f;
        }
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        final boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)(int)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue());
        if (flag) {
            this.applyEnchantments(this, entityIn);
        }
        return flag;
    }
    
    @Override
    public void setTamed(final boolean tamed) {
        super.setTamed(tamed);
        if (tamed) {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0);
        }
        else {
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(8.0);
        }
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4.0);
    }
    
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (this.isTamed()) {
            if (!itemstack.isEmpty()) {
                if (itemstack.getItem() instanceof ItemFood) {
                    final ItemFood itemfood = (ItemFood)itemstack.getItem();
                    if (itemfood.isWolfsFavoriteMeat() && this.dataManager.get(EntityWolf.DATA_HEALTH_ID) < 20.0f) {
                        if (!player.capabilities.isCreativeMode) {
                            itemstack.shrink(1);
                        }
                        this.heal((float)itemfood.getHealAmount(itemstack));
                        return true;
                    }
                }
                else if (itemstack.getItem() == Items.DYE) {
                    final EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());
                    if (enumdyecolor != this.getCollarColor()) {
                        this.setCollarColor(enumdyecolor);
                        if (!player.capabilities.isCreativeMode) {
                            itemstack.shrink(1);
                        }
                        return true;
                    }
                }
            }
            if (this.isOwner(player) && !this.world.isRemote && !this.isBreedingItem(itemstack)) {
                this.aiSit.setSitting(!this.isSitting());
                this.isJumping = false;
                this.navigator.clearPath();
                this.setAttackTarget(null);
            }
        }
        else if (itemstack.getItem() == Items.BONE && !this.isAngry()) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.shrink(1);
            }
            if (!this.world.isRemote) {
                if (this.rand.nextInt(3) == 0) {
                    this.setTamedBy(player);
                    this.navigator.clearPath();
                    this.setAttackTarget(null);
                    this.aiSit.setSitting(true);
                    this.setHealth(20.0f);
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte)7);
                }
                else {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte)6);
                }
            }
            return true;
        }
        return super.processInteract(player, hand);
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 8) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0f;
            this.prevTimeWolfIsShaking = 0.0f;
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    public float getTailRotation() {
        if (this.isAngry()) {
            return 1.5393804f;
        }
        return this.isTamed() ? ((0.55f - (this.getMaxHealth() - this.dataManager.get(EntityWolf.DATA_HEALTH_ID)) * 0.02f) * 3.1415927f) : 0.62831855f;
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return stack.getItem() instanceof ItemFood && ((ItemFood)stack.getItem()).isWolfsFavoriteMeat();
    }
    
    @Override
    public int getMaxSpawnedInChunk() {
        return 8;
    }
    
    public boolean isAngry() {
        return (this.dataManager.get(EntityWolf.TAMED) & 0x2) != 0x0;
    }
    
    public void setAngry(final boolean angry) {
        final byte b0 = this.dataManager.get(EntityWolf.TAMED);
        if (angry) {
            this.dataManager.set(EntityWolf.TAMED, (byte)(b0 | 0x2));
        }
        else {
            this.dataManager.set(EntityWolf.TAMED, (byte)(b0 & 0xFFFFFFFD));
        }
    }
    
    public EnumDyeColor getCollarColor() {
        return EnumDyeColor.byDyeDamage(this.dataManager.get(EntityWolf.COLLAR_COLOR) & 0xF);
    }
    
    public void setCollarColor(final EnumDyeColor collarcolor) {
        this.dataManager.set(EntityWolf.COLLAR_COLOR, collarcolor.getDyeDamage());
    }
    
    @Override
    public EntityWolf createChild(final EntityAgeable ageable) {
        final EntityWolf entitywolf = new EntityWolf(this.world);
        final UUID uuid = this.getOwnerId();
        if (uuid != null) {
            entitywolf.setOwnerId(uuid);
            entitywolf.setTamed(true);
        }
        return entitywolf;
    }
    
    public void setBegging(final boolean beg) {
        this.dataManager.set(EntityWolf.BEGGING, beg);
    }
    
    @Override
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        if (otherAnimal == this) {
            return false;
        }
        if (!this.isTamed()) {
            return false;
        }
        if (!(otherAnimal instanceof EntityWolf)) {
            return false;
        }
        final EntityWolf entitywolf = (EntityWolf)otherAnimal;
        return entitywolf.isTamed() && !entitywolf.isSitting() && this.isInLove() && entitywolf.isInLove();
    }
    
    public boolean isBegging() {
        return this.dataManager.get(EntityWolf.BEGGING);
    }
    
    @Override
    public boolean shouldAttackEntity(final EntityLivingBase target, final EntityLivingBase owner) {
        if (!(target instanceof EntityCreeper) && !(target instanceof EntityGhast)) {
            if (target instanceof EntityWolf) {
                final EntityWolf entitywolf = (EntityWolf)target;
                if (entitywolf.isTamed() && entitywolf.getOwner() == owner) {
                    return false;
                }
            }
            return (!(target instanceof EntityPlayer) || !(owner instanceof EntityPlayer) || ((EntityPlayer)owner).canAttackPlayer((EntityPlayer)target)) && (!(target instanceof AbstractHorse) || !((AbstractHorse)target).isTame());
        }
        return false;
    }
    
    @Override
    public boolean canBeLeashedTo(final EntityPlayer player) {
        return !this.isAngry() && super.canBeLeashedTo(player);
    }
    
    static {
        DATA_HEALTH_ID = EntityDataManager.createKey(EntityWolf.class, DataSerializers.FLOAT);
        BEGGING = EntityDataManager.createKey(EntityWolf.class, DataSerializers.BOOLEAN);
        COLLAR_COLOR = EntityDataManager.createKey(EntityWolf.class, DataSerializers.VARINT);
    }
    
    class AIAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T>
    {
        private final EntityWolf wolf;
        
        public AIAvoidEntity(final EntityWolf wolfIn, final Class<T> p_i47251_3_, final float p_i47251_4_, final double p_i47251_5_, final double p_i47251_7_) {
            super(wolfIn, p_i47251_3_, p_i47251_4_, p_i47251_5_, p_i47251_7_);
            this.wolf = wolfIn;
        }
        
        @Override
        public boolean shouldExecute() {
            return super.shouldExecute() && this.closestLivingEntity instanceof EntityLlama && !this.wolf.isTamed() && this.avoidLlama((EntityLlama)this.closestLivingEntity);
        }
        
        private boolean avoidLlama(final EntityLlama p_190854_1_) {
            return p_190854_1_.getStrength() >= EntityWolf.this.rand.nextInt(5);
        }
        
        @Override
        public void startExecuting() {
            EntityWolf.this.setAttackTarget(null);
            super.startExecuting();
        }
        
        @Override
        public void updateTask() {
            EntityWolf.this.setAttackTarget(null);
            super.updateTask();
        }
    }
}
