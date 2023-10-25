/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityRabbit
extends EntityAnimal {
    private static final DataParameter<Integer> RABBIT_TYPE = EntityDataManager.createKey(EntityRabbit.class, DataSerializers.VARINT);
    private int jumpTicks;
    private int jumpDuration;
    private boolean wasOnGround;
    private int currentMoveTypeDuration;
    private int carrotTicks;

    public EntityRabbit(World worldIn) {
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
        this.tasks.addTask(3, new EntityAITempt((EntityCreature)this, 1.0, Items.CARROT, false));
        this.tasks.addTask(3, new EntityAITempt((EntityCreature)this, 1.0, Items.GOLDEN_CARROT, false));
        this.tasks.addTask(3, new EntityAITempt((EntityCreature)this, 1.0, Item.getItemFromBlock(Blocks.YELLOW_FLOWER), false));
        this.tasks.addTask(4, new AIAvoidEntity<EntityPlayer>(this, EntityPlayer.class, 8.0f, 2.2, 2.2));
        this.tasks.addTask(4, new AIAvoidEntity<EntityWolf>(this, EntityWolf.class, 10.0f, 2.2, 2.2));
        this.tasks.addTask(4, new AIAvoidEntity<EntityMob>(this, EntityMob.class, 4.0f, 2.2, 2.2));
        this.tasks.addTask(5, new AIRaidFarm(this));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.6));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0f));
    }

    @Override
    public float getJumpUpwardsMotion() {
        if (!(this.isCollidedHorizontally || this.moveHelper.isUpdating() && !(this.moveHelper.getY() <= this.posY + 0.5))) {
            Path path = this.navigator.getPath();
            if (path != null && path.getCurrentPathIndex() < path.getCurrentPathLength()) {
                Vec3d vec3d = path.getPosition(this);
                if (vec3d.y > this.posY + 0.5) {
                    return 0.5f;
                }
            }
            return this.moveHelper.getSpeed() <= 0.6 ? 0.2f : 0.3f;
        }
        return 0.5f;
    }

    @Override
    protected void jump() {
        double d1;
        super.jump();
        double d0 = this.moveHelper.getSpeed();
        if (d0 > 0.0 && (d1 = this.motionX * this.motionX + this.motionZ * this.motionZ) < 0.010000000000000002) {
            this.moveFlying(0.0f, 0.0f, 1.0f, 0.1f);
        }
        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)1);
        }
    }

    public float setJumpCompletion(float p_175521_1_) {
        return this.jumpDuration == 0 ? 0.0f : ((float)this.jumpTicks + p_175521_1_) / (float)this.jumpDuration;
    }

    public void setMovementSpeed(double newSpeed) {
        this.getNavigator().setSpeed(newSpeed);
        this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), newSpeed);
    }

    @Override
    public void setJumping(boolean jumping) {
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
        this.dataManager.register(RABBIT_TYPE, 0);
    }

    @Override
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
            RabbitJumpHelper entityrabbit$rabbitjumphelper;
            EntityLivingBase entitylivingbase;
            if (!this.wasOnGround) {
                this.setJumping(false);
                this.checkLandingDelay();
            }
            if (this.getRabbitType() == 99 && this.currentMoveTypeDuration == 0 && (entitylivingbase = this.getAttackTarget()) != null && this.getDistanceSqToEntity(entitylivingbase) < 16.0) {
                this.calculateRotationYaw(entitylivingbase.posX, entitylivingbase.posZ);
                this.moveHelper.setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, this.moveHelper.getSpeed());
                this.startJumping();
                this.wasOnGround = true;
            }
            if (!(entityrabbit$rabbitjumphelper = (RabbitJumpHelper)this.jumpHelper).getIsJumping()) {
                if (this.moveHelper.isUpdating() && this.currentMoveTypeDuration == 0) {
                    Path path = this.navigator.getPath();
                    Vec3d vec3d = new Vec3d(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ());
                    if (path != null && path.getCurrentPathIndex() < path.getCurrentPathLength()) {
                        vec3d = path.getPosition(this);
                    }
                    this.calculateRotationYaw(vec3d.x, vec3d.z);
                    this.startJumping();
                }
            } else if (!entityrabbit$rabbitjumphelper.canJump()) {
                this.enableJumpControl();
            }
        }
        this.wasOnGround = this.onGround;
    }

    @Override
    public void spawnRunningParticles() {
    }

    private void calculateRotationYaw(double x, double z) {
        this.rotationYaw = (float)(MathHelper.atan2(z - this.posZ, x - this.posX) * 57.29577951308232) - 90.0f;
    }

    private void enableJumpControl() {
        ((RabbitJumpHelper)this.jumpHelper).setCanJump(true);
    }

    private void disableJumpControl() {
        ((RabbitJumpHelper)this.jumpHelper).setCanJump(false);
    }

    private void updateMoveTypeDuration() {
        this.currentMoveTypeDuration = this.moveHelper.getSpeed() < 2.2 ? 10 : 1;
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
        } else if (this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            this.setJumping(false);
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3f);
    }

    public static void registerFixesRabbit(DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityRabbit.class);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("RabbitType", this.getRabbitType());
        compound.setInteger("MoreCarrotTicks", this.carrotTicks);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
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
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.ENTITY_RABBIT_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_RABBIT_DEATH;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getRabbitType() == 99) {
            this.playSound(SoundEvents.ENTITY_RABBIT_ATTACK, 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0f);
        }
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0f);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return this.getRabbitType() == 99 ? SoundCategory.HOSTILE : SoundCategory.NEUTRAL;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return this.isEntityInvulnerable(source) ? false : super.attackEntityFrom(source, amount);
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_RABBIT;
    }

    private boolean isRabbitBreedingItem(Item itemIn) {
        return itemIn == Items.CARROT || itemIn == Items.GOLDEN_CARROT || itemIn == Item.getItemFromBlock(Blocks.YELLOW_FLOWER);
    }

    @Override
    public EntityRabbit createChild(EntityAgeable ageable) {
        EntityRabbit entityrabbit = new EntityRabbit(this.world);
        int i = this.getRandomRabbitType();
        if (this.rand.nextInt(20) != 0) {
            i = ageable instanceof EntityRabbit && this.rand.nextBoolean() ? ((EntityRabbit)ageable).getRabbitType() : this.getRabbitType();
        }
        entityrabbit.setRabbitType(i);
        return entityrabbit;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return this.isRabbitBreedingItem(stack.getItem());
    }

    public int getRabbitType() {
        return this.dataManager.get(RABBIT_TYPE);
    }

    public void setRabbitType(int rabbitTypeId) {
        if (rabbitTypeId == 99) {
            this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(8.0);
            this.tasks.addTask(4, new AIEvilAttack(this));
            this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>((EntityCreature)this, EntityPlayer.class, true));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityWolf>((EntityCreature)this, EntityWolf.class, true));
            if (!this.hasCustomName()) {
                this.setCustomNameTag(I18n.translateToLocal("entity.KillerBunny.name"));
            }
        }
        this.dataManager.set(RABBIT_TYPE, rabbitTypeId);
    }

    @Override
    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        int i = this.getRandomRabbitType();
        boolean flag = false;
        if (livingdata instanceof RabbitTypeData) {
            i = ((RabbitTypeData)livingdata).typeData;
            flag = true;
        } else {
            livingdata = new RabbitTypeData(i);
        }
        this.setRabbitType(i);
        if (flag) {
            this.setGrowingAge(-24000);
        }
        return livingdata;
    }

    private int getRandomRabbitType() {
        Biome biome = this.world.getBiome(new BlockPos(this));
        int i = this.rand.nextInt(100);
        if (biome.isSnowyBiome()) {
            return i < 80 ? 1 : 3;
        }
        if (biome instanceof BiomeDesert) {
            return 4;
        }
        return i < 50 ? 0 : (i < 90 ? 5 : 2);
    }

    private boolean isCarrotEaten() {
        return this.carrotTicks == 0;
    }

    protected void createEatingParticles() {
        BlockCarrot blockcarrot = (BlockCarrot)Blocks.CARROTS;
        IBlockState iblockstate = blockcarrot.withAge(blockcarrot.getMaxAge());
        this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.5 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, 0.0, 0.0, 0.0, Block.getStateId(iblockstate));
        this.carrotTicks = 40;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 1) {
            this.createRunningParticles();
            this.jumpDuration = 10;
            this.jumpTicks = 0;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    public static class RabbitTypeData
    implements IEntityLivingData {
        public int typeData;

        public RabbitTypeData(int type) {
            this.typeData = type;
        }
    }

    static class RabbitMoveHelper
    extends EntityMoveHelper {
        private final EntityRabbit theEntity;
        private double nextJumpSpeed;

        public RabbitMoveHelper(EntityRabbit rabbit) {
            super(rabbit);
            this.theEntity = rabbit;
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.theEntity.onGround && !this.theEntity.isJumping && !((RabbitJumpHelper)this.theEntity.jumpHelper).getIsJumping()) {
                this.theEntity.setMovementSpeed(0.0);
            } else if (this.isUpdating()) {
                this.theEntity.setMovementSpeed(this.nextJumpSpeed);
            }
            super.onUpdateMoveHelper();
        }

        @Override
        public void setMoveTo(double x, double y, double z, double speedIn) {
            if (this.theEntity.isInWater()) {
                speedIn = 1.5;
            }
            super.setMoveTo(x, y, z, speedIn);
            if (speedIn > 0.0) {
                this.nextJumpSpeed = speedIn;
            }
        }
    }

    public class RabbitJumpHelper
    extends EntityJumpHelper {
        private final EntityRabbit theEntity;
        private boolean canJump;

        public RabbitJumpHelper(EntityRabbit rabbit) {
            super(rabbit);
            this.theEntity = rabbit;
        }

        public boolean getIsJumping() {
            return this.isJumping;
        }

        public boolean canJump() {
            return this.canJump;
        }

        public void setCanJump(boolean canJumpIn) {
            this.canJump = canJumpIn;
        }

        @Override
        public void doJump() {
            if (this.isJumping) {
                this.theEntity.startJumping();
                this.isJumping = false;
            }
        }
    }

    static class AIRaidFarm
    extends EntityAIMoveToBlock {
        private final EntityRabbit rabbit;
        private boolean wantsToRaid;
        private boolean canRaid;

        public AIRaidFarm(EntityRabbit rabbitIn) {
            super(rabbitIn, 0.7f, 16);
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
        public boolean continueExecuting() {
            return this.canRaid && super.continueExecuting();
        }

        @Override
        public void updateTask() {
            super.updateTask();
            this.rabbit.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, (double)this.destinationBlock.getZ() + 0.5, 10.0f, this.rabbit.getVerticalFaceSpeed());
            if (this.getIsAboveDestination()) {
                World world = this.rabbit.world;
                BlockPos blockpos = this.destinationBlock.up();
                IBlockState iblockstate = world.getBlockState(blockpos);
                Block block = iblockstate.getBlock();
                if (this.canRaid && block instanceof BlockCarrot) {
                    Integer integer = iblockstate.getValue(BlockCarrot.AGE);
                    if (integer == 0) {
                        world.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 2);
                        world.destroyBlock(blockpos, true);
                    } else {
                        world.setBlockState(blockpos, iblockstate.withProperty(BlockCarrot.AGE, integer - 1), 2);
                        world.playEvent(2001, blockpos, Block.getStateId(iblockstate));
                    }
                    this.rabbit.createEatingParticles();
                }
                this.canRaid = false;
                this.runDelay = 10;
            }
        }

        @Override
        protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
            IBlockState iblockstate;
            Block block = worldIn.getBlockState(pos).getBlock();
            if (block == Blocks.FARMLAND && this.wantsToRaid && !this.canRaid && (block = (iblockstate = worldIn.getBlockState(pos = pos.up())).getBlock()) instanceof BlockCarrot && ((BlockCarrot)block).isMaxAge(iblockstate)) {
                this.canRaid = true;
                return true;
            }
            return false;
        }
    }

    static class AIPanic
    extends EntityAIPanic {
        private final EntityRabbit theEntity;

        public AIPanic(EntityRabbit rabbit, double speedIn) {
            super(rabbit, speedIn);
            this.theEntity = rabbit;
        }

        @Override
        public void updateTask() {
            super.updateTask();
            this.theEntity.setMovementSpeed(this.speed);
        }
    }

    static class AIEvilAttack
    extends EntityAIAttackMelee {
        public AIEvilAttack(EntityRabbit rabbit) {
            super(rabbit, 1.4, true);
        }

        @Override
        protected double getAttackReachSqr(EntityLivingBase attackTarget) {
            return 4.0f + attackTarget.width;
        }
    }

    static class AIAvoidEntity<T extends Entity>
    extends EntityAIAvoidEntity<T> {
        private final EntityRabbit entityInstance;

        public AIAvoidEntity(EntityRabbit rabbit, Class<T> p_i46403_2_, float p_i46403_3_, double p_i46403_4_, double p_i46403_6_) {
            super(rabbit, p_i46403_2_, p_i46403_3_, p_i46403_4_, p_i46403_6_);
            this.entityInstance = rabbit;
        }

        @Override
        public boolean shouldExecute() {
            return this.entityInstance.getRabbitType() != 99 && super.shouldExecute();
        }
    }
}

