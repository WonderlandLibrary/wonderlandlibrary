// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import java.util.Calendar;
import java.util.List;
import net.minecraft.init.Blocks;
import com.google.common.base.Predicate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.init.Items;
import net.minecraft.world.DifficultyInstance;
import javax.annotation.Nullable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.block.Block;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIZombieAttack;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAIBreakDoor;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import java.util.UUID;
import net.minecraft.entity.ai.attributes.IAttribute;

public class EntityZombie extends EntityMob
{
    protected static final IAttribute SPAWN_REINFORCEMENTS_CHANCE;
    private static final UUID BABY_SPEED_BOOST_ID;
    private static final AttributeModifier BABY_SPEED_BOOST;
    private static final DataParameter<Boolean> IS_CHILD;
    private static final DataParameter<Integer> VILLAGER_TYPE;
    private static final DataParameter<Boolean> ARMS_RAISED;
    private final EntityAIBreakDoor breakDoor;
    private boolean isBreakDoorsTaskSet;
    private float zombieWidth;
    private float zombieHeight;
    
    public EntityZombie(final World worldIn) {
        super(worldIn);
        this.breakDoor = new EntityAIBreakDoor(this);
        this.zombieWidth = -1.0f;
        this.setSize(0.6f, 1.95f);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIZombieAttack(this, 1.0, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(7, new EntityAIWanderAvoidWater(this, 1.0));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.applyEntityAI();
    }
    
    protected void applyEntityAI() {
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0, false));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, (Class<?>[])new Class[] { EntityPigZombie.class }));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityVillager.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityIronGolem.class, true));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0);
        this.getAttributeMap().registerAttribute(EntityZombie.SPAWN_REINFORCEMENTS_CHANCE).setBaseValue(this.rand.nextDouble() * 0.10000000149011612);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(EntityZombie.IS_CHILD, false);
        this.getDataManager().register(EntityZombie.VILLAGER_TYPE, 0);
        this.getDataManager().register(EntityZombie.ARMS_RAISED, false);
    }
    
    public void setArmsRaised(final boolean armsRaised) {
        this.getDataManager().set(EntityZombie.ARMS_RAISED, armsRaised);
    }
    
    public boolean isArmsRaised() {
        return this.getDataManager().get(EntityZombie.ARMS_RAISED);
    }
    
    public boolean isBreakDoorsTaskSet() {
        return this.isBreakDoorsTaskSet;
    }
    
    public void setBreakDoorsAItask(final boolean enabled) {
        if (this.isBreakDoorsTaskSet != enabled) {
            this.isBreakDoorsTaskSet = enabled;
            ((PathNavigateGround)this.getNavigator()).setBreakDoors(enabled);
            if (enabled) {
                this.tasks.addTask(1, this.breakDoor);
            }
            else {
                this.tasks.removeTask(this.breakDoor);
            }
        }
    }
    
    @Override
    public boolean isChild() {
        return this.getDataManager().get(EntityZombie.IS_CHILD);
    }
    
    @Override
    protected int getExperiencePoints(final EntityPlayer player) {
        if (this.isChild()) {
            this.experienceValue *= (int)2.5f;
        }
        return super.getExperiencePoints(player);
    }
    
    public void setChild(final boolean childZombie) {
        this.getDataManager().set(EntityZombie.IS_CHILD, childZombie);
        if (this.world != null && !this.world.isRemote) {
            final IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            iattributeinstance.removeModifier(EntityZombie.BABY_SPEED_BOOST);
            if (childZombie) {
                iattributeinstance.applyModifier(EntityZombie.BABY_SPEED_BOOST);
            }
        }
        this.setChildSize(childZombie);
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        if (EntityZombie.IS_CHILD.equals(key)) {
            this.setChildSize(this.isChild());
        }
        super.notifyDataManagerChange(key);
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.world.isDaytime() && !this.world.isRemote && !this.isChild() && this.shouldBurnInDay()) {
            final float f = this.getBrightness();
            if (f > 0.5f && this.rand.nextFloat() * 30.0f < (f - 0.4f) * 2.0f && this.world.canSeeSky(new BlockPos(this.posX, this.posY + this.getEyeHeight(), this.posZ))) {
                boolean flag = true;
                final ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isItemStackDamageable()) {
                        itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));
                        if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
                            this.renderBrokenItemStack(itemstack);
                            this.setItemStackToSlot(EntityEquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }
                    flag = false;
                }
                if (flag) {
                    this.setFire(8);
                }
            }
        }
        super.onLivingUpdate();
    }
    
    protected boolean shouldBurnInDay() {
        return true;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (super.attackEntityFrom(source, amount)) {
            EntityLivingBase entitylivingbase = this.getAttackTarget();
            if (entitylivingbase == null && source.getTrueSource() instanceof EntityLivingBase) {
                entitylivingbase = (EntityLivingBase)source.getTrueSource();
            }
            if (entitylivingbase != null && this.world.getDifficulty() == EnumDifficulty.HARD && this.rand.nextFloat() < this.getEntityAttribute(EntityZombie.SPAWN_REINFORCEMENTS_CHANCE).getAttributeValue() && this.world.getGameRules().getBoolean("doMobSpawning")) {
                final int i = MathHelper.floor(this.posX);
                final int j = MathHelper.floor(this.posY);
                final int k = MathHelper.floor(this.posZ);
                final EntityZombie entityzombie = new EntityZombie(this.world);
                for (int l = 0; l < 50; ++l) {
                    final int i2 = i + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
                    final int j2 = j + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
                    final int k2 = k + MathHelper.getInt(this.rand, 7, 40) * MathHelper.getInt(this.rand, -1, 1);
                    if (this.world.getBlockState(new BlockPos(i2, j2 - 1, k2)).isTopSolid() && this.world.getLightFromNeighbors(new BlockPos(i2, j2, k2)) < 10) {
                        entityzombie.setPosition(i2, j2, k2);
                        if (!this.world.isAnyPlayerWithinRangeAt(i2, j2, k2, 7.0) && this.world.checkNoEntityCollision(entityzombie.getEntityBoundingBox(), entityzombie) && this.world.getCollisionBoxes(entityzombie, entityzombie.getEntityBoundingBox()).isEmpty() && !this.world.containsAnyLiquid(entityzombie.getEntityBoundingBox())) {
                            this.world.spawnEntity(entityzombie);
                            entityzombie.setAttackTarget(entitylivingbase);
                            entityzombie.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombie)), null);
                            this.getEntityAttribute(EntityZombie.SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806, 0));
                            entityzombie.getEntityAttribute(EntityZombie.SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806, 0));
                            break;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        final boolean flag = super.attackEntityAsMob(entityIn);
        if (flag) {
            final float f = this.world.getDifficultyForLocation(new BlockPos(this)).getAdditionalDifficulty();
            if (this.getHeldItemMainhand().isEmpty() && this.isBurning() && this.rand.nextFloat() < f * 0.3f) {
                entityIn.setFire(2 * (int)f);
            }
        }
        return flag;
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }
    
    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_ZOMBIE_STEP;
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound(this.getStepSound(), 0.15f, 1.0f);
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_ZOMBIE;
    }
    
    @Override
    protected void setEquipmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        if (this.rand.nextFloat() < ((this.world.getDifficulty() == EnumDifficulty.HARD) ? 0.05f : 0.01f)) {
            final int i = this.rand.nextInt(3);
            if (i == 0) {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            }
            else {
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SHOVEL));
            }
        }
    }
    
    public static void registerFixesZombie(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityZombie.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.isChild()) {
            compound.setBoolean("IsBaby", true);
        }
        compound.setBoolean("CanBreakDoors", this.isBreakDoorsTaskSet());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.getBoolean("IsBaby")) {
            this.setChild(true);
        }
        this.setBreakDoorsAItask(compound.getBoolean("CanBreakDoors"));
    }
    
    @Override
    public void onKillEntity(final EntityLivingBase entityLivingIn) {
        super.onKillEntity(entityLivingIn);
        if ((this.world.getDifficulty() == EnumDifficulty.NORMAL || this.world.getDifficulty() == EnumDifficulty.HARD) && entityLivingIn instanceof EntityVillager) {
            if (this.world.getDifficulty() != EnumDifficulty.HARD && this.rand.nextBoolean()) {
                return;
            }
            final EntityVillager entityvillager = (EntityVillager)entityLivingIn;
            final EntityZombieVillager entityzombievillager = new EntityZombieVillager(this.world);
            entityzombievillager.copyLocationAndAnglesFrom(entityvillager);
            this.world.removeEntity(entityvillager);
            entityzombievillager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombievillager)), new GroupData(false));
            entityzombievillager.setProfession(entityvillager.getProfession());
            entityzombievillager.setChild(entityvillager.isChild());
            entityzombievillager.setNoAI(entityvillager.isAIDisabled());
            if (entityvillager.hasCustomName()) {
                entityzombievillager.setCustomNameTag(entityvillager.getCustomNameTag());
                entityzombievillager.setAlwaysRenderNameTag(entityvillager.getAlwaysRenderNameTag());
            }
            this.world.spawnEntity(entityzombievillager);
            this.world.playEvent(null, 1026, new BlockPos(this), 0);
        }
    }
    
    @Override
    public float getEyeHeight() {
        float f = 1.74f;
        if (this.isChild()) {
            f -= (float)0.81;
        }
        return f;
    }
    
    @Override
    protected boolean canEquipItem(final ItemStack stack) {
        return (stack.getItem() != Items.EGG || !this.isChild() || !this.isRiding()) && super.canEquipItem(stack);
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        final float f = difficulty.getClampedAdditionalDifficulty();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55f * f);
        if (livingdata == null) {
            livingdata = new GroupData(this.world.rand.nextFloat() < 0.05f);
        }
        if (livingdata instanceof GroupData) {
            final GroupData entityzombie$groupdata = (GroupData)livingdata;
            if (entityzombie$groupdata.isChild) {
                this.setChild(true);
                if (this.world.rand.nextFloat() < 0.05) {
                    final List<EntityChicken> list = this.world.getEntitiesWithinAABB((Class<? extends EntityChicken>)EntityChicken.class, this.getEntityBoundingBox().grow(5.0, 3.0, 5.0), (com.google.common.base.Predicate<? super EntityChicken>)EntitySelectors.IS_STANDALONE);
                    if (!list.isEmpty()) {
                        final EntityChicken entitychicken = list.get(0);
                        entitychicken.setChickenJockey(true);
                        this.startRiding(entitychicken);
                    }
                }
                else if (this.world.rand.nextFloat() < 0.05) {
                    final EntityChicken entitychicken2 = new EntityChicken(this.world);
                    entitychicken2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0f);
                    entitychicken2.onInitialSpawn(difficulty, null);
                    entitychicken2.setChickenJockey(true);
                    this.world.spawnEntity(entitychicken2);
                    this.startRiding(entitychicken2);
                }
            }
        }
        this.setBreakDoorsAItask(this.rand.nextFloat() < f * 0.1f);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            final Calendar calendar = this.world.getCurrentDate();
            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25f) {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack((this.rand.nextFloat() < 0.1f) ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0f;
            }
        }
        this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806, 0));
        final double d0 = this.rand.nextDouble() * 1.5 * f;
        if (d0 > 1.0) {
            this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
        }
        if (this.rand.nextFloat() < f * 0.05f) {
            this.getEntityAttribute(EntityZombie.SPAWN_REINFORCEMENTS_CHANCE).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25 + 0.5, 0));
            this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0 + 1.0, 2));
            this.setBreakDoorsAItask(true);
        }
        return livingdata;
    }
    
    public void setChildSize(final boolean isChild) {
        this.multiplySize(isChild ? 0.5f : 1.0f);
    }
    
    @Override
    protected final void setSize(final float width, final float height) {
        final boolean flag = this.zombieWidth > 0.0f && this.zombieHeight > 0.0f;
        this.zombieWidth = width;
        this.zombieHeight = height;
        if (!flag) {
            this.multiplySize(1.0f);
        }
    }
    
    protected final void multiplySize(final float size) {
        super.setSize(this.zombieWidth * size, this.zombieHeight * size);
    }
    
    @Override
    public double getYOffset() {
        return this.isChild() ? 0.0 : -0.45;
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        super.onDeath(cause);
        if (cause.getTrueSource() instanceof EntityCreeper) {
            final EntityCreeper entitycreeper = (EntityCreeper)cause.getTrueSource();
            if (entitycreeper.getPowered() && entitycreeper.ableToCauseSkullDrop()) {
                entitycreeper.incrementDroppedSkulls();
                final ItemStack itemstack = this.getSkullDrop();
                if (!itemstack.isEmpty()) {
                    this.entityDropItem(itemstack, 0.0f);
                }
            }
        }
    }
    
    protected ItemStack getSkullDrop() {
        return new ItemStack(Items.SKULL, 1, 2);
    }
    
    static {
        SPAWN_REINFORCEMENTS_CHANCE = new RangedAttribute(null, "zombie.spawnReinforcements", 0.0, 0.0, 1.0).setDescription("Spawn Reinforcements Chance");
        BABY_SPEED_BOOST_ID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
        BABY_SPEED_BOOST = new AttributeModifier(EntityZombie.BABY_SPEED_BOOST_ID, "Baby speed boost", 0.5, 1);
        IS_CHILD = EntityDataManager.createKey(EntityZombie.class, DataSerializers.BOOLEAN);
        VILLAGER_TYPE = EntityDataManager.createKey(EntityZombie.class, DataSerializers.VARINT);
        ARMS_RAISED = EntityDataManager.createKey(EntityZombie.class, DataSerializers.BOOLEAN);
    }
    
    class GroupData implements IEntityLivingData
    {
        public boolean isChild;
        
        private GroupData(final boolean p_i47328_2_) {
            this.isChild = p_i47328_2_;
        }
    }
}
