// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import java.util.Random;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.potion.Potion;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.entity.EnumCreatureAttribute;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public class EntitySpider extends EntityMob
{
    private static final DataParameter<Byte> CLIMBING;
    
    public EntitySpider(final World worldIn) {
        super(worldIn);
        this.setSize(1.4f, 0.9f);
    }
    
    public static void registerFixesSpider(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntitySpider.class);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4f));
        this.tasks.addTask(4, new AISpiderAttack(this));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 0.8));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, (Class<?>[])new Class[0]));
        this.targetTasks.addTask(2, new AISpiderTarget<Object>(this, EntityPlayer.class));
        this.targetTasks.addTask(3, new AISpiderTarget<Object>(this, EntityIronGolem.class));
    }
    
    @Override
    public double getMountedYOffset() {
        return this.height * 0.5f;
    }
    
    @Override
    protected PathNavigate createNavigator(final World worldIn) {
        return new PathNavigateClimber(this, worldIn);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntitySpider.CLIMBING, (Byte)0);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally);
        }
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SPIDER_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_SPIDER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SPIDER_DEATH;
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound(SoundEvents.ENTITY_SPIDER_STEP, 0.15f, 1.0f);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_SPIDER;
    }
    
    @Override
    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }
    
    @Override
    public void setInWeb() {
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ARTHROPOD;
    }
    
    @Override
    public boolean isPotionApplicable(final PotionEffect potioneffectIn) {
        return potioneffectIn.getPotion() != MobEffects.POISON && super.isPotionApplicable(potioneffectIn);
    }
    
    public boolean isBesideClimbableBlock() {
        return (this.dataManager.get(EntitySpider.CLIMBING) & 0x1) != 0x0;
    }
    
    public void setBesideClimbableBlock(final boolean climbing) {
        byte b0 = this.dataManager.get(EntitySpider.CLIMBING);
        if (climbing) {
            b0 |= 0x1;
        }
        else {
            b0 &= 0xFFFFFFFE;
        }
        this.dataManager.set(EntitySpider.CLIMBING, b0);
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        if (this.world.rand.nextInt(100) == 0) {
            final EntitySkeleton entityskeleton = new EntitySkeleton(this.world);
            entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
            entityskeleton.onInitialSpawn(difficulty, null);
            this.world.spawnEntity(entityskeleton);
            entityskeleton.startRiding(this);
        }
        if (livingdata == null) {
            livingdata = new GroupData();
            if (this.world.getDifficulty() == EnumDifficulty.HARD && this.world.rand.nextFloat() < 0.1f * difficulty.getClampedAdditionalDifficulty()) {
                ((GroupData)livingdata).setRandomEffect(this.world.rand);
            }
        }
        if (livingdata instanceof GroupData) {
            final Potion potion = ((GroupData)livingdata).effect;
            if (potion != null) {
                this.addPotionEffect(new PotionEffect(potion, Integer.MAX_VALUE));
            }
        }
        return livingdata;
    }
    
    @Override
    public float getEyeHeight() {
        return 0.65f;
    }
    
    static {
        CLIMBING = EntityDataManager.createKey(EntitySpider.class, DataSerializers.BYTE);
    }
    
    static class AISpiderAttack extends EntityAIAttackMelee
    {
        public AISpiderAttack(final EntitySpider spider) {
            super(spider, 1.0, true);
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            final float f = this.attacker.getBrightness();
            if (f >= 0.5f && this.attacker.getRNG().nextInt(100) == 0) {
                this.attacker.setAttackTarget(null);
                return false;
            }
            return super.shouldContinueExecuting();
        }
        
        @Override
        protected double getAttackReachSqr(final EntityLivingBase attackTarget) {
            return 4.0f + attackTarget.width;
        }
    }
    
    static class AISpiderTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T>
    {
        public AISpiderTarget(final EntitySpider spider, final Class<T> classTarget) {
            super(spider, classTarget, true);
        }
        
        @Override
        public boolean shouldExecute() {
            final float f = this.taskOwner.getBrightness();
            return f < 0.5f && super.shouldExecute();
        }
    }
    
    public static class GroupData implements IEntityLivingData
    {
        public Potion effect;
        
        public void setRandomEffect(final Random rand) {
            final int i = rand.nextInt(5);
            if (i <= 1) {
                this.effect = MobEffects.SPEED;
            }
            else if (i <= 2) {
                this.effect = MobEffects.STRENGTH;
            }
            else if (i <= 3) {
                this.effect = MobEffects.REGENERATION;
            }
            else if (i <= 4) {
                this.effect = MobEffects.INVISIBILITY;
            }
        }
    }
}
