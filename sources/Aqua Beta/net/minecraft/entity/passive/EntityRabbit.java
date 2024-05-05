package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class EntityRabbit
extends EntityAnimal {
    private AIAvoidEntity<EntityWolf> aiAvoidWolves;
    private int field_175540_bm = 0;
    private int field_175535_bn = 0;
    private boolean field_175536_bo = false;
    private boolean field_175537_bp = false;
    private int currentMoveTypeDuration = 0;
    private EnumMoveType moveType = EnumMoveType.HOP;
    private int carrotTicks = 0;
    private EntityPlayer field_175543_bt = null;

    public EntityRabbit(World worldIn) {
        super(worldIn);
        this.setSize(0.6f, 0.7f);
        this.jumpHelper = new RabbitJumpHelper(this, this);
        this.moveHelper = new RabbitMoveHelper(this);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.navigator.setHeightRequirement(2.5f);
        this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
        this.tasks.addTask(1, (EntityAIBase)new AIPanic(this, 1.33));
        this.tasks.addTask(2, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.0, Items.carrot, false));
        this.tasks.addTask(2, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.0, Items.golden_carrot, false));
        this.tasks.addTask(2, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.0, Item.getItemFromBlock((Block)Blocks.yellow_flower), false));
        this.tasks.addTask(3, (EntityAIBase)new EntityAIMate((EntityAnimal)this, 0.8));
        this.tasks.addTask(5, (EntityAIBase)new AIRaidFarm(this));
        this.tasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)this, 0.6));
        this.tasks.addTask(11, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 10.0f));
        this.aiAvoidWolves = new AIAvoidEntity(this, EntityWolf.class, 16.0f, 1.33, 1.33);
        this.tasks.addTask(4, this.aiAvoidWolves);
        this.setMovementSpeed(0.0);
    }

    protected float getJumpUpwardsMotion() {
        return this.moveHelper.isUpdating() && this.moveHelper.getY() > this.posY + 0.5 ? 0.5f : this.moveType.func_180074_b();
    }

    public void setMoveType(EnumMoveType type) {
        this.moveType = type;
    }

    public float func_175521_o(float p_175521_1_) {
        return this.field_175535_bn == 0 ? 0.0f : ((float)this.field_175540_bm + p_175521_1_) / (float)this.field_175535_bn;
    }

    public void setMovementSpeed(double newSpeed) {
        this.getNavigator().setSpeed(newSpeed);
        this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), newSpeed);
    }

    public void setJumping(boolean jump, EnumMoveType moveTypeIn) {
        super.setJumping(jump);
        if (!jump) {
            if (this.moveType == EnumMoveType.ATTACK) {
                this.moveType = EnumMoveType.HOP;
            }
        } else {
            this.setMovementSpeed(1.5 * (double)moveTypeIn.getSpeed());
            this.playSound(this.getJumpingSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) * 0.8f);
        }
        this.field_175536_bo = jump;
    }

    public void doMovementAction(EnumMoveType movetype) {
        this.setJumping(true, movetype);
        this.field_175535_bn = movetype.func_180073_d();
        this.field_175540_bm = 0;
    }

    public boolean func_175523_cj() {
        return this.field_175536_bo;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(18, (Object)0);
    }

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
            RabbitJumpHelper entityrabbit$rabbitjumphelper;
            EntityLivingBase entitylivingbase;
            if (!this.field_175537_bp) {
                this.setJumping(false, EnumMoveType.NONE);
                this.func_175517_cu();
            }
            if (this.getRabbitType() == 99 && this.currentMoveTypeDuration == 0 && (entitylivingbase = this.getAttackTarget()) != null && this.getDistanceSqToEntity((Entity)entitylivingbase) < 16.0) {
                this.calculateRotationYaw(entitylivingbase.posX, entitylivingbase.posZ);
                this.moveHelper.setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, this.moveHelper.getSpeed());
                this.doMovementAction(EnumMoveType.ATTACK);
                this.field_175537_bp = true;
            }
            if (!(entityrabbit$rabbitjumphelper = (RabbitJumpHelper)this.jumpHelper).getIsJumping()) {
                if (this.moveHelper.isUpdating() && this.currentMoveTypeDuration == 0) {
                    PathEntity pathentity = this.navigator.getPath();
                    Vec3 vec3 = new Vec3(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ());
                    if (pathentity != null && pathentity.getCurrentPathIndex() < pathentity.getCurrentPathLength()) {
                        vec3 = pathentity.getPosition((Entity)this);
                    }
                    this.calculateRotationYaw(vec3.xCoord, vec3.zCoord);
                    this.doMovementAction(this.moveType);
                }
            } else if (!entityrabbit$rabbitjumphelper.func_180065_d()) {
                this.func_175518_cr();
            }
        }
        this.field_175537_bp = this.onGround;
    }

    public void spawnRunningParticles() {
    }

    private void calculateRotationYaw(double x, double z) {
        this.rotationYaw = (float)(MathHelper.atan2((double)(z - this.posZ), (double)(x - this.posX)) * 180.0 / Math.PI) - 90.0f;
    }

    private void func_175518_cr() {
        ((RabbitJumpHelper)this.jumpHelper).func_180066_a(true);
    }

    private void func_175520_cs() {
        ((RabbitJumpHelper)this.jumpHelper).func_180066_a(false);
    }

    private void updateMoveTypeDuration() {
        this.currentMoveTypeDuration = this.getMoveTypeDuration();
    }

    private void func_175517_cu() {
        this.updateMoveTypeDuration();
        this.func_175520_cs();
    }

    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.field_175540_bm != this.field_175535_bn) {
            if (this.field_175540_bm == 0 && !this.worldObj.isRemote) {
                this.worldObj.setEntityState((Entity)this, (byte)1);
            }
            ++this.field_175540_bm;
        } else if (this.field_175535_bn != 0) {
            this.field_175540_bm = 0;
            this.field_175535_bn = 0;
        }
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)0.3f);
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("RabbitType", this.getRabbitType());
        tagCompound.setInteger("MoreCarrotTicks", this.carrotTicks);
    }

    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setRabbitType(tagCompund.getInteger("RabbitType"));
        this.carrotTicks = tagCompund.getInteger("MoreCarrotTicks");
    }

    protected String getJumpingSound() {
        return "mob.rabbit.hop";
    }

    protected String getLivingSound() {
        return "mob.rabbit.idle";
    }

    protected String getHurtSound() {
        return "mob.rabbit.hurt";
    }

    protected String getDeathSound() {
        return "mob.rabbit.death";
    }

    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getRabbitType() == 99) {
            this.playSound("mob.attack", 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            return entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), 8.0f);
        }
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), 3.0f);
    }

    public int getTotalArmorValue() {
        return this.getRabbitType() == 99 ? 8 : super.getTotalArmorValue();
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        return this.isEntityInvulnerable(source) ? false : super.attackEntityFrom(source, amount);
    }

    protected void addRandomDrop() {
        this.entityDropItem(new ItemStack(Items.rabbit_foot, 1), 0.0f);
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        int i = this.rand.nextInt(2) + this.rand.nextInt(1 + lootingModifier);
        for (int j = 0; j < i; ++j) {
            this.dropItem(Items.rabbit_hide, 1);
        }
        i = this.rand.nextInt(2);
        for (int k = 0; k < i; ++k) {
            if (this.isBurning()) {
                this.dropItem(Items.cooked_rabbit, 1);
                continue;
            }
            this.dropItem(Items.rabbit, 1);
        }
    }

    private boolean isRabbitBreedingItem(Item itemIn) {
        return itemIn == Items.carrot || itemIn == Items.golden_carrot || itemIn == Item.getItemFromBlock((Block)Blocks.yellow_flower);
    }

    public EntityRabbit createChild(EntityAgeable ageable) {
        EntityRabbit entityrabbit = new EntityRabbit(this.worldObj);
        if (ageable instanceof EntityRabbit) {
            entityrabbit.setRabbitType(this.rand.nextBoolean() ? this.getRabbitType() : ((EntityRabbit)ageable).getRabbitType());
        }
        return entityrabbit;
    }

    public boolean isBreedingItem(ItemStack stack) {
        return stack != null && this.isRabbitBreedingItem(stack.getItem());
    }

    public int getRabbitType() {
        return this.dataWatcher.getWatchableObjectByte(18);
    }

    public void setRabbitType(int rabbitTypeId) {
        if (rabbitTypeId == 99) {
            this.tasks.removeTask(this.aiAvoidWolves);
            this.tasks.addTask(4, (EntityAIBase)new AIEvilAttack(this));
            this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
            this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityPlayer.class, true));
            this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityWolf.class, true));
            if (!this.hasCustomName()) {
                this.setCustomNameTag(StatCollector.translateToLocal((String)"entity.KillerBunny.name"));
            }
        }
        this.dataWatcher.updateObject(18, (Object)((byte)rabbitTypeId));
    }

    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        int i = this.rand.nextInt(6);
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

    private boolean isCarrotEaten() {
        return this.carrotTicks == 0;
    }

    protected int getMoveTypeDuration() {
        return this.moveType.getDuration();
    }

    protected void createEatingParticles() {
        this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, this.posY + 0.5 + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0f) - (double)this.width, 0.0, 0.0, 0.0, new int[]{Block.getStateId((IBlockState)Blocks.carrots.getStateFromMeta(7))});
        this.carrotTicks = 100;
    }

    public void handleStatusUpdate(byte id) {
        if (id == 1) {
            this.createRunningParticles();
            this.field_175535_bn = 10;
            this.field_175540_bm = 0;
        } else {
            super.handleStatusUpdate(id);
        }
    }

    static /* synthetic */ boolean access$000(EntityRabbit x0) {
        return x0.isCarrotEaten();
    }
}
