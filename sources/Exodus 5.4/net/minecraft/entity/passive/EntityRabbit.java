/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIMoveToBlock;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityJumpHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityRabbit
extends EntityAnimal {
    private int currentMoveTypeDuration = 0;
    private boolean field_175537_bp = false;
    private int carrotTicks = 0;
    private int field_175540_bm = 0;
    private AIAvoidEntity<EntityWolf> aiAvoidWolves;
    private boolean field_175536_bo = false;
    private int field_175535_bn = 0;
    private EnumMoveType moveType = EnumMoveType.HOP;
    private EntityPlayer field_175543_bt = null;

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        if (this.getRabbitType() == 99) {
            this.playSound("mob.attack", 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 8.0f);
        }
        return entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0f);
    }

    public void setMoveType(EnumMoveType enumMoveType) {
        this.moveType = enumMoveType;
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.field_175540_bm != this.field_175535_bn) {
            if (this.field_175540_bm == 0 && !this.worldObj.isRemote) {
                this.worldObj.setEntityState(this, (byte)1);
            }
            ++this.field_175540_bm;
        } else if (this.field_175535_bn != 0) {
            this.field_175540_bm = 0;
            this.field_175535_bn = 0;
        }
    }

    @Override
    protected void dropFewItems(boolean bl, int n) {
        int n2 = this.rand.nextInt(2) + this.rand.nextInt(1 + n);
        int n3 = 0;
        while (n3 < n2) {
            this.dropItem(Items.rabbit_hide, 1);
            ++n3;
        }
        n2 = this.rand.nextInt(2);
        n3 = 0;
        while (n3 < n2) {
            if (this.isBurning()) {
                this.dropItem(Items.cooked_rabbit, 1);
            } else {
                this.dropItem(Items.rabbit, 1);
            }
            ++n3;
        }
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 1) {
            this.createRunningParticles();
            this.field_175535_bn = 10;
            this.field_175540_bm = 0;
        } else {
            super.handleStatusUpdate(by);
        }
    }

    public float func_175521_o(float f) {
        return this.field_175535_bn == 0 ? 0.0f : ((float)this.field_175540_bm + f) / (float)this.field_175535_bn;
    }

    protected void createEatingParticles() {
        this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.5 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, 0.0, 0.0, 0.0, Block.getStateId(Blocks.carrots.getStateFromMeta(7)));
        this.carrotTicks = 100;
    }

    public EntityRabbit(World world) {
        super(world);
        this.setSize(0.6f, 0.7f);
        this.jumpHelper = new RabbitJumpHelper(this);
        this.moveHelper = new RabbitMoveHelper(this);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.navigator.setHeightRequirement(2.5f);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(1, new AIPanic(this, 1.33));
        this.tasks.addTask(2, new EntityAITempt(this, 1.0, Items.carrot, false));
        this.tasks.addTask(2, new EntityAITempt(this, 1.0, Items.golden_carrot, false));
        this.tasks.addTask(2, new EntityAITempt(this, 1.0, Item.getItemFromBlock(Blocks.yellow_flower), false));
        this.tasks.addTask(3, new EntityAIMate(this, 0.8));
        this.tasks.addTask(5, new AIRaidFarm(this));
        this.tasks.addTask(5, new EntityAIWander(this, 0.6));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0f));
        this.aiAvoidWolves = new AIAvoidEntity<EntityWolf>(this, EntityWolf.class, 16.0f, 1.33, 1.33);
        this.tasks.addTask(4, this.aiAvoidWolves);
        this.setMovementSpeed(0.0);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3f);
    }

    public boolean func_175523_cj() {
        return this.field_175536_bo;
    }

    public void setRabbitType(int n) {
        if (n == 99) {
            this.tasks.removeTask(this.aiAvoidWolves);
            this.tasks.addTask(4, new AIEvilAttack(this));
            this.targetTasks.addTask(1, new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityPlayer>((EntityCreature)this, EntityPlayer.class, true));
            this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<EntityWolf>((EntityCreature)this, EntityWolf.class, true));
            if (!this.hasCustomName()) {
                this.setCustomNameTag(StatCollector.translateToLocal("entity.KillerBunny.name"));
            }
        }
        this.dataWatcher.updateObject(18, (byte)n);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.setRabbitType(nBTTagCompound.getInteger("RabbitType"));
        this.carrotTicks = nBTTagCompound.getInteger("MoreCarrotTicks");
    }

    public void doMovementAction(EnumMoveType enumMoveType) {
        this.setJumping(true, enumMoveType);
        this.field_175535_bn = enumMoveType.func_180073_d();
        this.field_175540_bm = 0;
    }

    @Override
    public void spawnRunningParticles() {
    }

    private void func_175517_cu() {
        this.updateMoveTypeDuration();
        this.func_175520_cs();
    }

    private void func_175518_cr() {
        ((RabbitJumpHelper)this.jumpHelper).func_180066_a(true);
    }

    @Override
    protected float getJumpUpwardsMotion() {
        return this.moveHelper.isUpdating() && this.moveHelper.getY() > this.posY + 0.5 ? 0.5f : this.moveType.func_180074_b();
    }

    @Override
    public int getTotalArmorValue() {
        return this.getRabbitType() == 99 ? 8 : super.getTotalArmorValue();
    }

    @Override
    public void updateAITasks() {
        if (this.moveHelper.getSpeed() > 0.8) {
            this.setMoveType(EnumMoveType.SPRINT);
        } else if (this.moveType != EnumMoveType.ATTACK) {
            this.setMoveType(EnumMoveType.HOP);
        }
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
            Object object;
            if (!this.field_175537_bp) {
                this.setJumping(false, EnumMoveType.NONE);
                this.func_175517_cu();
            }
            if (this.getRabbitType() == 99 && this.currentMoveTypeDuration == 0 && (object = this.getAttackTarget()) != null && this.getDistanceSqToEntity((Entity)object) < 16.0) {
                this.calculateRotationYaw(((EntityLivingBase)object).posX, ((EntityLivingBase)object).posZ);
                this.moveHelper.setMoveTo(((EntityLivingBase)object).posX, ((EntityLivingBase)object).posY, ((EntityLivingBase)object).posZ, this.moveHelper.getSpeed());
                this.doMovementAction(EnumMoveType.ATTACK);
                this.field_175537_bp = true;
            }
            if (!((RabbitJumpHelper)(object = (RabbitJumpHelper)this.jumpHelper)).getIsJumping()) {
                if (this.moveHelper.isUpdating() && this.currentMoveTypeDuration == 0) {
                    PathEntity pathEntity = this.navigator.getPath();
                    Vec3 vec3 = new Vec3(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ());
                    if (pathEntity != null && pathEntity.getCurrentPathIndex() < pathEntity.getCurrentPathLength()) {
                        vec3 = pathEntity.getPosition(this);
                    }
                    this.calculateRotationYaw(vec3.xCoord, vec3.zCoord);
                    this.doMovementAction(this.moveType);
                }
            } else if (!((RabbitJumpHelper)object).func_180065_d()) {
                this.func_175518_cr();
            }
        }
        this.field_175537_bp = this.onGround;
    }

    private void calculateRotationYaw(double d, double d2) {
        this.rotationYaw = (float)(MathHelper.func_181159_b(d2 - this.posZ, d - this.posX) * 180.0 / Math.PI) - 90.0f;
    }

    public void setMovementSpeed(double d) {
        this.getNavigator().setSpeed(d);
        this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), d);
    }

    @Override
    protected String getDeathSound() {
        return "mob.rabbit.death";
    }

    private void updateMoveTypeDuration() {
        this.currentMoveTypeDuration = this.getMoveTypeDuration();
    }

    @Override
    protected void addRandomDrop() {
        this.entityDropItem(new ItemStack(Items.rabbit_foot, 1), 0.0f);
    }

    protected int getMoveTypeDuration() {
        return this.moveType.getDuration();
    }

    public void setJumping(boolean bl, EnumMoveType enumMoveType) {
        super.setJumping(bl);
        if (!bl) {
            if (this.moveType == EnumMoveType.ATTACK) {
                this.moveType = EnumMoveType.HOP;
            }
        } else {
            this.setMovementSpeed(1.5 * (double)enumMoveType.getSpeed());
            this.playSound(this.getJumpingSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) * 0.8f);
        }
        this.field_175536_bo = bl;
    }

    public int getRabbitType() {
        return this.dataWatcher.getWatchableObjectByte(18);
    }

    private boolean isCarrotEaten() {
        return this.carrotTicks == 0;
    }

    @Override
    public boolean isBreedingItem(ItemStack itemStack) {
        return itemStack != null && this.isRabbitBreedingItem(itemStack.getItem());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setInteger("RabbitType", this.getRabbitType());
        nBTTagCompound.setInteger("MoreCarrotTicks", this.carrotTicks);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(18, (byte)0);
    }

    @Override
    protected String getHurtSound() {
        return "mob.rabbit.hurt";
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        return this.isEntityInvulnerable(damageSource) ? false : super.attackEntityFrom(damageSource, f);
    }

    @Override
    public EntityRabbit createChild(EntityAgeable entityAgeable) {
        EntityRabbit entityRabbit = new EntityRabbit(this.worldObj);
        if (entityAgeable instanceof EntityRabbit) {
            entityRabbit.setRabbitType(this.rand.nextBoolean() ? this.getRabbitType() : ((EntityRabbit)entityAgeable).getRabbitType());
        }
        return entityRabbit;
    }

    private boolean isRabbitBreedingItem(Item item) {
        return item == Items.carrot || item == Items.golden_carrot || item == Item.getItemFromBlock(Blocks.yellow_flower);
    }

    private void func_175520_cs() {
        ((RabbitJumpHelper)this.jumpHelper).func_180066_a(false);
    }

    protected String getJumpingSound() {
        return "mob.rabbit.hop";
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficultyInstance, IEntityLivingData iEntityLivingData) {
        iEntityLivingData = super.onInitialSpawn(difficultyInstance, iEntityLivingData);
        int n = this.rand.nextInt(6);
        boolean bl = false;
        if (iEntityLivingData instanceof RabbitTypeData) {
            n = ((RabbitTypeData)iEntityLivingData).typeData;
            bl = true;
        } else {
            iEntityLivingData = new RabbitTypeData(n);
        }
        this.setRabbitType(n);
        if (bl) {
            this.setGrowingAge(-24000);
        }
        return iEntityLivingData;
    }

    @Override
    protected String getLivingSound() {
        return "mob.rabbit.idle";
    }

    public static class RabbitTypeData
    implements IEntityLivingData {
        public int typeData;

        public RabbitTypeData(int n) {
            this.typeData = n;
        }
    }

    static class AIAvoidEntity<T extends Entity>
    extends EntityAIAvoidEntity<T> {
        private EntityRabbit entityInstance;

        @Override
        public void updateTask() {
            super.updateTask();
        }

        public AIAvoidEntity(EntityRabbit entityRabbit, Class<T> clazz, float f, double d, double d2) {
            super(entityRabbit, clazz, f, d, d2);
            this.entityInstance = entityRabbit;
        }
    }

    static class RabbitMoveHelper
    extends EntityMoveHelper {
        private EntityRabbit theEntity;

        @Override
        public void onUpdateMoveHelper() {
            if (this.theEntity.onGround && !this.theEntity.func_175523_cj()) {
                this.theEntity.setMovementSpeed(0.0);
            }
            super.onUpdateMoveHelper();
        }

        public RabbitMoveHelper(EntityRabbit entityRabbit) {
            super(entityRabbit);
            this.theEntity = entityRabbit;
        }
    }

    static enum EnumMoveType {
        NONE(0.0f, 0.0f, 30, 1),
        HOP(0.8f, 0.2f, 20, 10),
        STEP(1.0f, 0.45f, 14, 14),
        SPRINT(1.75f, 0.4f, 1, 8),
        ATTACK(2.0f, 0.7f, 7, 8);

        private final int duration;
        private final int field_180085_i;
        private final float speed;
        private final float field_180077_g;

        public int getDuration() {
            return this.duration;
        }

        public float getSpeed() {
            return this.speed;
        }

        private EnumMoveType(float f, float f2, int n2, int n3) {
            this.speed = f;
            this.field_180077_g = f2;
            this.duration = n2;
            this.field_180085_i = n3;
        }

        public int func_180073_d() {
            return this.field_180085_i;
        }

        public float func_180074_b() {
            return this.field_180077_g;
        }
    }

    static class AIRaidFarm
    extends EntityAIMoveToBlock {
        private boolean field_179499_e = false;
        private boolean field_179498_d;
        private final EntityRabbit field_179500_c;

        public AIRaidFarm(EntityRabbit entityRabbit) {
            super(entityRabbit, 0.7f, 16);
            this.field_179500_c = entityRabbit;
        }

        @Override
        public boolean shouldExecute() {
            if (this.runDelay <= 0) {
                if (!this.field_179500_c.worldObj.getGameRules().getBoolean("mobGriefing")) {
                    return false;
                }
                this.field_179499_e = false;
                this.field_179498_d = this.field_179500_c.isCarrotEaten();
            }
            return super.shouldExecute();
        }

        @Override
        public void updateTask() {
            super.updateTask();
            this.field_179500_c.getLookHelper().setLookPosition((double)this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, (double)this.destinationBlock.getZ() + 0.5, 10.0f, this.field_179500_c.getVerticalFaceSpeed());
            if (this.getIsAboveDestination()) {
                World world = this.field_179500_c.worldObj;
                BlockPos blockPos = this.destinationBlock.up();
                IBlockState iBlockState = world.getBlockState(blockPos);
                Block block = iBlockState.getBlock();
                if (this.field_179499_e && block instanceof BlockCarrot && iBlockState.getValue(BlockCarrot.AGE) == 7) {
                    world.setBlockState(blockPos, Blocks.air.getDefaultState(), 2);
                    world.destroyBlock(blockPos, true);
                    this.field_179500_c.createEatingParticles();
                }
                this.field_179499_e = false;
                this.runDelay = 10;
            }
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        @Override
        public void startExecuting() {
            super.startExecuting();
        }

        @Override
        protected boolean shouldMoveTo(World world, BlockPos blockPos) {
            IBlockState iBlockState;
            Block block = world.getBlockState(blockPos).getBlock();
            if (block == Blocks.farmland && (block = (iBlockState = world.getBlockState(blockPos = blockPos.up())).getBlock()) instanceof BlockCarrot && iBlockState.getValue(BlockCarrot.AGE) == 7 && this.field_179498_d && !this.field_179499_e) {
                this.field_179499_e = true;
                return true;
            }
            return false;
        }

        @Override
        public boolean continueExecuting() {
            return this.field_179499_e && super.continueExecuting();
        }
    }

    public class RabbitJumpHelper
    extends EntityJumpHelper {
        private EntityRabbit theEntity;
        private boolean field_180068_d;

        public RabbitJumpHelper(EntityRabbit entityRabbit2) {
            super(entityRabbit2);
            this.field_180068_d = false;
            this.theEntity = entityRabbit2;
        }

        public boolean getIsJumping() {
            return this.isJumping;
        }

        @Override
        public void doJump() {
            if (this.isJumping) {
                this.theEntity.doMovementAction(EnumMoveType.STEP);
                this.isJumping = false;
            }
        }

        public boolean func_180065_d() {
            return this.field_180068_d;
        }

        public void func_180066_a(boolean bl) {
            this.field_180068_d = bl;
        }
    }

    static class AIPanic
    extends EntityAIPanic {
        private EntityRabbit theEntity;

        @Override
        public void updateTask() {
            super.updateTask();
            this.theEntity.setMovementSpeed(this.speed);
        }

        public AIPanic(EntityRabbit entityRabbit, double d) {
            super(entityRabbit, d);
            this.theEntity = entityRabbit;
        }
    }

    static class AIEvilAttack
    extends EntityAIAttackOnCollide {
        @Override
        protected double func_179512_a(EntityLivingBase entityLivingBase) {
            return 4.0f + entityLivingBase.width;
        }

        public AIEvilAttack(EntityRabbit entityRabbit) {
            super(entityRabbit, EntityLivingBase.class, 1.4, true);
        }
    }
}

