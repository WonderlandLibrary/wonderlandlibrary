// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.init.Biomes;
import net.minecraft.world.WorldType;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.EntityLiving;

public class EntitySlime extends EntityLiving implements IMob
{
    private static final DataParameter<Integer> SLIME_SIZE;
    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private boolean wasOnGround;
    
    public EntitySlime(final World worldIn) {
        super(worldIn);
        this.moveHelper = new SlimeMoveHelper(this);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new AISlimeFloat(this));
        this.tasks.addTask(2, new AISlimeAttack(this));
        this.tasks.addTask(3, new AISlimeFaceRandom(this));
        this.tasks.addTask(5, new AISlimeHop(this));
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
        this.targetTasks.addTask(3, new EntityAIFindEntityNearest(this, EntityIronGolem.class));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntitySlime.SLIME_SIZE, 1);
    }
    
    protected void setSlimeSize(final int size, final boolean resetHealth) {
        this.dataManager.set(EntitySlime.SLIME_SIZE, size);
        this.setSize(0.51000005f * size, 0.51000005f * size);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(size * size);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2f + 0.1f * size);
        if (resetHealth) {
            this.setHealth(this.getMaxHealth());
        }
        this.experienceValue = size;
    }
    
    public int getSlimeSize() {
        return this.dataManager.get(EntitySlime.SLIME_SIZE);
    }
    
    public static void registerFixesSlime(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntitySlime.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Size", this.getSlimeSize() - 1);
        compound.setBoolean("wasOnGround", this.wasOnGround);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        int i = compound.getInteger("Size");
        if (i < 0) {
            i = 0;
        }
        this.setSlimeSize(i + 1, false);
        this.wasOnGround = compound.getBoolean("wasOnGround");
    }
    
    public boolean isSmallSlime() {
        return this.getSlimeSize() <= 1;
    }
    
    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.SLIME;
    }
    
    @Override
    public void onUpdate() {
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.getSlimeSize() > 0) {
            this.isDead = true;
        }
        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5f;
        this.prevSquishFactor = this.squishFactor;
        super.onUpdate();
        if (this.onGround && !this.wasOnGround) {
            for (int i = this.getSlimeSize(), j = 0; j < i * 8; ++j) {
                final float f = this.rand.nextFloat() * 6.2831855f;
                final float f2 = this.rand.nextFloat() * 0.5f + 0.5f;
                final float f3 = MathHelper.sin(f) * i * 0.5f * f2;
                final float f4 = MathHelper.cos(f) * i * 0.5f * f2;
                final World world = this.world;
                final EnumParticleTypes enumparticletypes = this.getParticleType();
                final double d0 = this.posX + f3;
                final double d2 = this.posZ + f4;
                world.spawnParticle(enumparticletypes, d0, this.getEntityBoundingBox().minY, d2, 0.0, 0.0, 0.0, new int[0]);
            }
            this.playSound(this.getSquishSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) / 0.8f);
            this.squishAmount = -0.5f;
        }
        else if (!this.onGround && this.wasOnGround) {
            this.squishAmount = 1.0f;
        }
        this.wasOnGround = this.onGround;
        this.alterSquishAmount();
    }
    
    protected void alterSquishAmount() {
        this.squishAmount *= 0.6f;
    }
    
    protected int getJumpDelay() {
        return this.rand.nextInt(20) + 10;
    }
    
    protected EntitySlime createInstance() {
        return new EntitySlime(this.world);
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        if (EntitySlime.SLIME_SIZE.equals(key)) {
            final int i = this.getSlimeSize();
            this.setSize(0.51000005f * i, 0.51000005f * i);
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;
            if (this.isInWater() && this.rand.nextInt(20) == 0) {
                this.doWaterSplashEffect();
            }
        }
        super.notifyDataManagerChange(key);
    }
    
    @Override
    public void setDead() {
        final int i = this.getSlimeSize();
        if (!this.world.isRemote && i > 1 && this.getHealth() <= 0.0f) {
            for (int j = 2 + this.rand.nextInt(3), k = 0; k < j; ++k) {
                final float f = (k % 2 - 0.5f) * i / 4.0f;
                final float f2 = (k / 2 - 0.5f) * i / 4.0f;
                final EntitySlime entityslime = this.createInstance();
                if (this.hasCustomName()) {
                    entityslime.setCustomNameTag(this.getCustomNameTag());
                }
                if (this.isNoDespawnRequired()) {
                    entityslime.enablePersistence();
                }
                entityslime.setSlimeSize(i / 2, true);
                entityslime.setLocationAndAngles(this.posX + f, this.posY + 0.5, this.posZ + f2, this.rand.nextFloat() * 360.0f, 0.0f);
                this.world.spawnEntity(entityslime);
            }
        }
        super.setDead();
    }
    
    @Override
    public void applyEntityCollision(final Entity entityIn) {
        super.applyEntityCollision(entityIn);
        if (entityIn instanceof EntityIronGolem && this.canDamagePlayer()) {
            this.dealDamage((EntityLivingBase)entityIn);
        }
    }
    
    @Override
    public void onCollideWithPlayer(final EntityPlayer entityIn) {
        if (this.canDamagePlayer()) {
            this.dealDamage(entityIn);
        }
    }
    
    protected void dealDamage(final EntityLivingBase entityIn) {
        final int i = this.getSlimeSize();
        if (this.canEntityBeSeen(entityIn) && this.getDistanceSq(entityIn) < 0.6 * i * 0.6 * i && entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getAttackStrength())) {
            this.playSound(SoundEvents.ENTITY_SLIME_ATTACK, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            this.applyEnchantments(this, entityIn);
        }
    }
    
    @Override
    public float getEyeHeight() {
        return 0.625f * this.height;
    }
    
    protected boolean canDamagePlayer() {
        return !this.isSmallSlime();
    }
    
    protected int getAttackStrength() {
        return this.getSlimeSize();
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_HURT : SoundEvents.ENTITY_SLIME_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_DEATH : SoundEvents.ENTITY_SLIME_DEATH;
    }
    
    protected SoundEvent getSquishSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_SQUISH : SoundEvents.ENTITY_SLIME_SQUISH;
    }
    
    @Override
    protected Item getDropItem() {
        return (this.getSlimeSize() == 1) ? Items.SLIME_BALL : null;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return (this.getSlimeSize() == 1) ? LootTableList.ENTITIES_SLIME : LootTableList.EMPTY;
    }
    
    @Override
    public boolean getCanSpawnHere() {
        final BlockPos blockpos = new BlockPos(MathHelper.floor(this.posX), 0, MathHelper.floor(this.posZ));
        final Chunk chunk = this.world.getChunk(blockpos);
        if (this.world.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1) {
            return false;
        }
        if (this.world.getDifficulty() != EnumDifficulty.PEACEFUL) {
            final Biome biome = this.world.getBiome(blockpos);
            if (biome == Biomes.SWAMPLAND && this.posY > 50.0 && this.posY < 70.0 && this.rand.nextFloat() < 0.5f && this.rand.nextFloat() < this.world.getCurrentMoonPhaseFactor() && this.world.getLightFromNeighbors(new BlockPos(this)) <= this.rand.nextInt(8)) {
                return super.getCanSpawnHere();
            }
            if (this.rand.nextInt(10) == 0 && chunk.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0) {
                return super.getCanSpawnHere();
            }
        }
        return false;
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4f * this.getSlimeSize();
    }
    
    @Override
    public int getVerticalFaceSpeed() {
        return 0;
    }
    
    protected boolean makesSoundOnJump() {
        return this.getSlimeSize() > 0;
    }
    
    @Override
    protected void jump() {
        this.motionY = 0.41999998688697815;
        this.isAirBorne = true;
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata) {
        int i = this.rand.nextInt(3);
        if (i < 2 && this.rand.nextFloat() < 0.5f * difficulty.getClampedAdditionalDifficulty()) {
            ++i;
        }
        final int j = 1 << i;
        this.setSlimeSize(j, true);
        return super.onInitialSpawn(difficulty, livingdata);
    }
    
    protected SoundEvent getJumpSound() {
        return this.isSmallSlime() ? SoundEvents.ENTITY_SMALL_SLIME_JUMP : SoundEvents.ENTITY_SLIME_JUMP;
    }
    
    static {
        SLIME_SIZE = EntityDataManager.createKey(EntitySlime.class, DataSerializers.VARINT);
    }
    
    static class AISlimeAttack extends EntityAIBase
    {
        private final EntitySlime slime;
        private int growTieredTimer;
        
        public AISlimeAttack(final EntitySlime slimeIn) {
            this.slime = slimeIn;
            this.setMutexBits(2);
        }
        
        @Override
        public boolean shouldExecute() {
            final EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive() && (!(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).capabilities.disableDamage);
        }
        
        @Override
        public void startExecuting() {
            this.growTieredTimer = 300;
            super.startExecuting();
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            final EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive() && (!(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).capabilities.disableDamage) && --this.growTieredTimer > 0;
        }
        
        @Override
        public void updateTask() {
            this.slime.faceEntity(this.slime.getAttackTarget(), 10.0f, 10.0f);
            ((SlimeMoveHelper)this.slime.getMoveHelper()).setDirection(this.slime.rotationYaw, this.slime.canDamagePlayer());
        }
    }
    
    static class AISlimeFaceRandom extends EntityAIBase
    {
        private final EntitySlime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;
        
        public AISlimeFaceRandom(final EntitySlime slimeIn) {
            this.slime = slimeIn;
            this.setMutexBits(2);
        }
        
        @Override
        public boolean shouldExecute() {
            return this.slime.getAttackTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava() || this.slime.isPotionActive(MobEffects.LEVITATION));
        }
        
        @Override
        public void updateTask() {
            final int nextRandomizeTime = this.nextRandomizeTime - 1;
            this.nextRandomizeTime = nextRandomizeTime;
            if (nextRandomizeTime <= 0) {
                this.nextRandomizeTime = 40 + this.slime.getRNG().nextInt(60);
                this.chosenDegrees = (float)this.slime.getRNG().nextInt(360);
            }
            ((SlimeMoveHelper)this.slime.getMoveHelper()).setDirection(this.chosenDegrees, false);
        }
    }
    
    static class AISlimeFloat extends EntityAIBase
    {
        private final EntitySlime slime;
        
        public AISlimeFloat(final EntitySlime slimeIn) {
            this.slime = slimeIn;
            this.setMutexBits(5);
            ((PathNavigateGround)slimeIn.getNavigator()).setCanSwim(true);
        }
        
        @Override
        public boolean shouldExecute() {
            return this.slime.isInWater() || this.slime.isInLava();
        }
        
        @Override
        public void updateTask() {
            if (this.slime.getRNG().nextFloat() < 0.8f) {
                this.slime.getJumpHelper().setJumping();
            }
            ((SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.2);
        }
    }
    
    static class AISlimeHop extends EntityAIBase
    {
        private final EntitySlime slime;
        
        public AISlimeHop(final EntitySlime slimeIn) {
            this.slime = slimeIn;
            this.setMutexBits(5);
        }
        
        @Override
        public boolean shouldExecute() {
            return true;
        }
        
        @Override
        public void updateTask() {
            ((SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.0);
        }
    }
    
    static class SlimeMoveHelper extends EntityMoveHelper
    {
        private float yRot;
        private int jumpDelay;
        private final EntitySlime slime;
        private boolean isAggressive;
        
        public SlimeMoveHelper(final EntitySlime slimeIn) {
            super(slimeIn);
            this.slime = slimeIn;
            this.yRot = 180.0f * slimeIn.rotationYaw / 3.1415927f;
        }
        
        public void setDirection(final float p_179920_1_, final boolean p_179920_2_) {
            this.yRot = p_179920_1_;
            this.isAggressive = p_179920_2_;
        }
        
        public void setSpeed(final double speedIn) {
            this.speed = speedIn;
            this.action = Action.MOVE_TO;
        }
        
        @Override
        public void onUpdateMoveHelper() {
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, this.yRot, 90.0f);
            this.entity.rotationYawHead = this.entity.rotationYaw;
            this.entity.renderYawOffset = this.entity.rotationYaw;
            if (this.action != Action.MOVE_TO) {
                this.entity.setMoveForward(0.0f);
            }
            else {
                this.action = Action.WAIT;
                if (this.entity.onGround) {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.slime.getJumpDelay();
                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }
                        this.slime.getJumpHelper().setJumping();
                        if (this.slime.makesSoundOnJump()) {
                            this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2f + 1.0f) * 0.8f);
                        }
                    }
                    else {
                        this.slime.moveStrafing = 0.0f;
                        this.slime.moveForward = 0.0f;
                        this.entity.setAIMoveSpeed(0.0f);
                    }
                }
                else {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
                }
            }
        }
    }
}
