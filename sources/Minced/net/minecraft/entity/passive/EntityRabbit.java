// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.BlockCarrot;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityAgeable;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.pathfinding.Path;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.init.Items;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public class EntityRabbit extends EntityAnimal
{
    private static final DataParameter<Integer> RABBIT_TYPE;
    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int currentMoveTypeDuration;
    private int carrotTicks;
    
    public EntityRabbit(final World worldIn) {
        super(worldIn);
        this.setSize(0.4f, 0.5f);
        this.jumpHelper = new RabbitJumpHelper(this);
        this.moveHelper = new RabbitMoveHelper(this);
        this.setMovementSpeed(0.0);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new AIPanic(this, 2.2));
        this.tasks.addTask(2, new EntityAIMate(this, 0.8));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0, Items.CARROT, false));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0, Items.GOLDEN_CARROT, false));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0, Item.getItemFromBlock(Blocks.YELLOW_FLOWER), false));
        this.tasks.addTask(4, new AIAvoidEntity<Object>(this, EntityPlayer.class, 8.0f, 2.2, 2.2));
        this.tasks.addTask(4, new AIAvoidEntity<Object>(this, EntityWolf.class, 10.0f, 2.2, 2.2));
        this.tasks.addTask(4, new AIAvoidEntity<Object>(this, EntityMob.class, 4.0f, 2.2, 2.2));
        this.tasks.addTask(5, new AIRaidFarm(this));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0f));
    }
    
    @Override
    protected float getJumpUpwardsMotion() {
        if (!this.collidedHorizontally && (!this.moveHelper.isUpdating() || this.moveHelper.getY() <= this.posY + 0.5)) {
            final Path path = this.navigator.getPath();
            if (path != null && path.getCurrentPathIndex() < path.getCurrentPathLength()) {
                final Vec3d vec3d = path.getPosition(this);
                if (vec3d.y > this.posY + 0.5) {
                    return 0.5f;
                }
            }
            return (this.moveHelper.getSpeed() <= 0.6) ? 0.2f : 0.3f;
        }
        return 0.5f;
    }
    
    @Override
    protected void jump() {
        super.jump();
        final double d0 = this.moveHelper.getSpeed();
        if (d0 > 0.0) {
            final double d2 = this.motionX * this.motionX + this.motionZ * this.motionZ;
            if (d2 < 0.010000000000000002) {
                this.moveRelative(0.0f, 0.0f, 1.0f, 0.1f);
            }
        }
        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)1);
        }
    }
    
    public float getJumpCompletion(final float p_175521_1_) {
        return (this.jumpDuration == 0) ? 0.0f : ((this.jumpTicks + p_175521_1_) / this.jumpDuration);
    }
    
    public void setMovementSpeed(final double newSpeed) {
        this.getNavigator().setSpeed(newSpeed);
        this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), newSpeed);
    }
    
    @Override
    public void setJumping(final boolean jumping) {
        super.setJumping(jumping);
        if (jumping) {
            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) * 0.8f);
        }
    }
    
    public void startJumping() {
        this.setJumping(true);
        this.jumpDuration = 10;
        this.jumpTicks = 0;
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityRabbit.RABBIT_TYPE, 0);
    }
    
    public void updateAITasks() {
        if (this.currentMoveTypeDuration > 0) {
            --this.currentMoveTypeDuration;
        }
        if (this.carrotTicks > 0) {
            this.carrotTicks -= this.rand.nextInt(3);
            if (this.carrotTicks < 0) {
                this.carrotTicks = 0;
            }
        }
        if (this.onGround) {
            if (!this.wasOnGround) {
                this.setJumping(false);
                this.checkLandingDelay();
            }
            if (this.getRabbitType() == 99 && this.currentMoveTypeDuration == 0) {
                final EntityLivingBase entitylivingbase = this.getAttackTarget();
                if (entitylivingbase != null && this.getDistanceSq(entitylivingbase) < 16.0) {
                    this.calculateRotationYaw(entitylivingbase.posX, entitylivingbase.posZ);
                    this.moveHelper.setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, this.moveHelper.getSpeed());
                    this.startJumping();
                    this.wasOnGround = true;
                }
            }
            final RabbitJumpHelper entityrabbit$rabbitjumphelper = (RabbitJumpHelper)this.jumpHelper;
            if (!entityrabbit$rabbitjumphelper.getIsJumping()) {
                if (this.moveHelper.isUpdating() && this.currentMoveTypeDuration == 0) {
                    final Path path = this.navigator.getPath();
                    Vec3d vec3d = new Vec3d(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ());
                    if (path != null && path.getCurrentPathIndex() < path.getCurrentPathLength()) {
                        vec3d = path.getPosition(this);
                    }
                    this.calculateRotationYaw(vec3d.x, vec3d.z);
                    this.startJumping();
                }
            }
            else if (!entityrabbit$rabbitjumphelper.canJump()) {
                this.enableJumpControl();
            }
        }
        this.wasOnGround = this.onGround;
    }
    
    @Override
    public void spawnRunningParticles() {
    }
    
    private void calculateRotationYaw(final double x, final double z) {
        this.rotationYaw = (float)(MathHelper.atan2(z - this.posZ, x - this.posX) * 57.29577951308232) - 90.0f;
    }
    
    private void enableJumpControl() {
        ((RabbitJumpHelper)this.jumpHelper).setCanJump(true);
    }
    
    private void disableJumpControl() {
        ((RabbitJumpHelper)this.jumpHelper).setCanJump(false);
    }
    
    private void updateMoveTypeDuration() {
        if (this.moveHelper.getSpeed() < 2.2) {
            this.currentMoveTypeDuration = 10;
        }
        else {
            this.currentMoveTypeDuration = 1;
        }
    }
    
    private void checkLandingDelay() {
        this.updateMoveTypeDuration();
        this.disableJumpControl();
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.jumpTicks != this.jumpDuration) {
            ++this.jumpTicks;
        }
        else if (this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.setJumping(false);
        }
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.30000001192092896);
    }
    
    public static void registerFixesRabbit(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityRabbit.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("RabbitType", this.getRabbitType());
        compound.setInteger("MoreCarrotTicks", this.carrotTicks);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setRabbitType(compound.getInteger("RabbitType"));
        this.carrotTicks = compound.getInteger("MoreCarrotTicks");
    }
    
    protected SoundEvent getJumpSound() {
        return SoundEvents.ENTITY_RABBIT_JUMP;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_RABBIT_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_RABBIT_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_RABBIT_DEATH;
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        if (this.getRabbitType() == 99) {
            this.playSound(SoundEvents.ENTITY_RABBIT_ATTACK, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0f);
        }
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0f);
    }
    
    @Override
    public SoundCategory getSoundCategory() {
        return (this.getRabbitType() == 99) ? SoundCategory.HOSTILE : SoundCategory.NEUTRAL;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        return !this.isEntityInvulnerable(source) && super.attackEntityFrom(source, amount);
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_RABBIT;
    }
    
    private boolean isRabbitBreedingItem(final Item itemIn) {
        return itemIn == Items.CARROT || itemIn == Items.GOLDEN_CARROT || itemIn == Item.getItemFromBlock(Blocks.YELLOW_FLOWER);
    }
    
    @Override
    public EntityRabbit createChild(final EntityAgeable ageable) {
        final EntityRabbit entityrabbit = new EntityRabbit(this.world);
        int i = this.getRandomRabbitType();
        if (this.rand.nextInt(20) != 0) {
            if (ageable instanceof EntityRabbit && this.rand.nextBoolean()) {
                i = ((EntityRabbit)ageable).getRabbitType();
            }
            else {
                i = this.getRabbitType();
            }
        }
        entityrabbit.setRabbitType(i);
        return entityrabbit;
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return this.isRabbitBreedingItem(stack.getItem());
    }
    
    public int getRabbitType() {
        return this.dataManager.get(EntityRabbit.RABBIT_TYPE);
    }
    
    public void setRabbitType(final int rabbitTypeId) {
        if (rabbitTypeId == 99) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0);
            this.tasks.addTask(4, new AIEvilAttack(this));
            this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, (Class<?>[])new Class[0]));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityWolf.class, true));
            if (!this.hasCustomName()) {
                this.setCustomNameTag(I18n.translateToLocal("entity.KillerBunny.name"));
            }
        }
        this.dataManager.set(EntityRabbit.RABBIT_TYPE, rabbitTypeId);
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        int i = this.getRandomRabbitType();
        boolean flag = false;
        if (livingdata instanceof RabbitTypeData) {
            i = ((RabbitTypeData)livingdata).typeData;
            flag = true;
        }
        else {
            livingdata = new RabbitTypeData(i);
        }
        this.setRabbitType(i);
        if (flag) {
            this.setGrowingAge(-24000);
        }
        return livingdata;
    }
    
    private int getRandomRabbitType() {
        final Biome biome = this.world.getBiome(new BlockPos(this));
        final int i = this.rand.nextInt(100);
        if (biome.isSnowyBiome()) {
            return (i < 80) ? 1 : 3;
        }
        if (biome instanceof BiomeDesert) {
            return 4;
        }
        return (i < 50) ? 0 : ((i < 90) ? 5 : 2);
    }
    
    private boolean isCarrotEaten() {
        return this.carrotTicks == 0;
    }
    
    protected void createEatingParticles() {
        final BlockCarrot blockcarrot = (BlockCarrot)Blocks.CARROTS;
        final IBlockState iblockstate = blockcarrot.withAge(blockcarrot.getMaxAge());
        this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, 0.0, 0.0, 0.0, Block.getStateId(iblockstate));
        this.carrotTicks = 40;
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 1) {
            this.createRunningParticles();
            this.jumpDuration = 10;
            this.jumpTicks = 0;
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    static {
        RABBIT_TYPE = EntityDataManager.createKey(EntityRabbit.class, DataSerializers.VARINT);
    }
    
    static class AIAvoidEntity<T extends Entity> extends EntityAIAvoidEntity<T>
    {
        private final EntityRabbit rabbit;
        
        public AIAvoidEntity(final EntityRabbit rabbit, final Class<T> p_i46403_2_, final float p_i46403_3_, final double p_i46403_4_, final double p_i46403_6_) {
            super(rabbit, p_i46403_2_, p_i46403_3_, p_i46403_4_, p_i46403_6_);
            this.rabbit = rabbit;
        }
        
        @Override
        public boolean shouldExecute() {
            return this.rabbit.getRabbitType() != 99 && super.shouldExecute();
        }
    }
    
    static class AIEvilAttack extends EntityAIAttackMelee
    {
        public AIEvilAttack(final EntityRabbit rabbit) {
            super(rabbit, 1.4, true);
        }
        
        @Override
        protected double getAttackReachSqr(final EntityLivingBase attackTarget) {
            return 4.0f + attackTarget.width;
        }
    }
    
    static class AIPanic extends EntityAIPanic
    {
        private final EntityRabbit rabbit;
        
        public AIPanic(final EntityRabbit rabbit, final double speedIn) {
            super(rabbit, speedIn);
            this.rabbit = rabbit;
        }
        
        @Override
        public void updateTask() {
            super.updateTask();
            this.rabbit.setMovementSpeed(this.speed);
        }
    }
    
    static class AIRaidFarm extends EntityAIMoveToBlock
    {
        private final EntityRabbit rabbit;
        private boolean wantsToRaid;
        private boolean canRaid;
        
        public AIRaidFarm(final EntityRabbit rabbitIn) {
            super(rabbitIn, 0.699999988079071, 16);
            this.rabbit = rabbitIn;
        }
        
        @Override
        public boolean shouldExecute() {
            if (this.runDelay <= 0) {
                if (!this.rabbit.world.getGameRules().getBoolean("mobGriefing")) {
                    return false;
                }
                this.canRaid = false;
                this.wantsToRaid = this.rabbit.isCarrotEaten();
                this.wantsToRaid = true;
            }
            return super.shouldExecute();
        }
        
        @Override
        public boolean shouldContinueExecuting() {
            return this.canRaid && super.shouldContinueExecuting();
        }
        
        @Override
        public void updateTask() {
            super.updateTask();
            this.rabbit.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5, 10.0f, (float)this.rabbit.getVerticalFaceSpeed());
            if (this.getIsAboveDestination()) {
                final World world = this.rabbit.world;
                final BlockPos blockpos = this.destinationBlock.up();
                final IBlockState iblockstate = world.getBlockState(blockpos);
                final Block block = iblockstate.getBlock();
                if (this.canRaid && block instanceof BlockCarrot) {
                    final Integer integer = iblockstate.getValue((IProperty<Integer>)BlockCarrot.AGE);
                    if (integer == 0) {
                        world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
                        world.destroyBlock(blockpos, true);
                    }
                    else {
                        world.setBlockState(blockpos, iblockstate.withProperty((IProperty<Comparable>)BlockCarrot.AGE, integer - 1), 2);
                        world.playEvent(2001, blockpos, Block.getStateId(iblockstate));
                    }
                    this.rabbit.createEatingParticles();
                }
                this.canRaid = false;
                this.runDelay = 10;
            }
        }
        
        @Override
        protected boolean shouldMoveTo(final World worldIn, BlockPos pos) {
            Block block = worldIn.getBlockState(pos).getBlock();
            if (block == Blocks.FARMLAND && this.wantsToRaid && !this.canRaid) {
                pos = pos.up();
                final IBlockState iblockstate = worldIn.getBlockState(pos);
                block = iblockstate.getBlock();
                if (block instanceof BlockCarrot && ((BlockCarrot)block).isMaxAge(iblockstate)) {
                    return this.canRaid = true;
                }
            }
            return false;
        }
    }
    
    public class RabbitJumpHelper extends EntityJumpHelper
    {
        private final EntityRabbit rabbit;
        private boolean canJump;
        
        public RabbitJumpHelper(final EntityRabbit rabbit) {
            super(rabbit);
            this.rabbit = rabbit;
        }
        
        public boolean getIsJumping() {
            return this.isJumping;
        }
        
        public boolean canJump() {
            return this.canJump;
        }
        
        public void setCanJump(final boolean canJumpIn) {
            this.canJump = canJumpIn;
        }
        
        @Override
        public void doJump() {
            if (this.isJumping) {
                this.rabbit.startJumping();
                this.isJumping = false;
            }
        }
    }
    
    static class RabbitMoveHelper extends EntityMoveHelper
    {
        private final EntityRabbit rabbit;
        private double nextJumpSpeed;
        
        public RabbitMoveHelper(final EntityRabbit rabbit) {
            super(rabbit);
            this.rabbit = rabbit;
        }
        
        @Override
        public void onUpdateMoveHelper() {
            if (this.rabbit.onGround && !this.rabbit.isJumping && !((RabbitJumpHelper)this.rabbit.jumpHelper).getIsJumping()) {
                this.rabbit.setMovementSpeed(0.0);
            }
            else if (this.isUpdating()) {
                this.rabbit.setMovementSpeed(this.nextJumpSpeed);
            }
            super.onUpdateMoveHelper();
        }
        
        @Override
        public void setMoveTo(final double x, final double y, final double z, double speedIn) {
            if (this.rabbit.isInWater()) {
                speedIn = 1.5;
            }
            super.setMoveTo(x, y, z, speedIn);
            if (speedIn > 0.0) {
                this.nextJumpSpeed = speedIn;
            }
        }
    }
    
    public static class RabbitTypeData implements IEntityLivingData
    {
        public int typeData;
        
        public RabbitTypeData(final int type) {
            this.typeData = type;
        }
    }
}
