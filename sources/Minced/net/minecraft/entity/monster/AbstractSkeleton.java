// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.EnumDifficulty;
import java.util.Calendar;
import net.minecraft.init.Blocks;
import javax.annotation.Nullable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.SoundEvent;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.IRangedAttackMob;

public abstract class AbstractSkeleton extends EntityMob implements IRangedAttackMob
{
    private static final DataParameter<Boolean> SWINGING_ARMS;
    private final EntityAIAttackRangedBow<AbstractSkeleton> aiArrowAttack;
    private final EntityAIAttackMelee aiAttackOnCollide;
    
    public AbstractSkeleton(final World worldIn) {
        super(worldIn);
        this.aiArrowAttack = new EntityAIAttackRangedBow<AbstractSkeleton>(this, 1.0, 20, 15.0f);
        this.aiAttackOnCollide = new EntityAIAttackMelee((EntityCreature)this, 1.2, false) {
            @Override
            public void resetTask() {
                super.resetTask();
                AbstractSkeleton.this.setSwingingArms(false);
            }
            
            @Override
            public void startExecuting() {
                super.startExecuting();
                AbstractSkeleton.this.setSwingingArms(true);
            }
        };
        this.setSize(0.6f, 1.99f);
        this.setCombatTask();
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0));
        this.tasks.addTask(3, new EntityAIAvoidEntity<Object>(this, EntityWolf.class, 6.0f, 1.0, 1.2));
        this.tasks.addTask(5, new EntityAIWanderAvoidWater(this, 1.0));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, (Class<?>[])new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityIronGolem.class, true));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(AbstractSkeleton.SWINGING_ARMS, false);
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound(this.getStepSound(), 0.15f, 1.0f);
    }
    
    abstract SoundEvent getStepSound();
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.UNDEAD;
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.world.isDaytime() && !this.world.isRemote) {
            final float f = this.getBrightness();
            final BlockPos blockpos = (this.getRidingEntity() instanceof EntityBoat) ? new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ).up() : new BlockPos(this.posX, (double)Math.round(this.posY), this.posZ);
            if (f > 0.5f && this.rand.nextFloat() * 30.0f < (f - 0.4f) * 2.0f && this.world.canSeeSky(blockpos)) {
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
    
    @Override
    public void updateRidden() {
        super.updateRidden();
        if (this.getRidingEntity() instanceof EntityCreature) {
            final EntityCreature entitycreature = (EntityCreature)this.getRidingEntity();
            this.renderYawOffset = entitycreature.renderYawOffset;
        }
    }
    
    @Override
    protected void setEquipmentBasedOnDifficulty(final DifficultyInstance difficulty) {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        this.setEquipmentBasedOnDifficulty(difficulty);
        this.setEnchantmentBasedOnDifficulty(difficulty);
        this.setCombatTask();
        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55f * difficulty.getClampedAdditionalDifficulty());
        if (this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
            final Calendar calendar = this.world.getCurrentDate();
            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25f) {
                this.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack((this.rand.nextFloat() < 0.1f) ? Blocks.LIT_PUMPKIN : Blocks.PUMPKIN));
                this.inventoryArmorDropChances[EntityEquipmentSlot.HEAD.getIndex()] = 0.0f;
            }
        }
        return livingdata;
    }
    
    public void setCombatTask() {
        if (this.world != null && !this.world.isRemote) {
            this.tasks.removeTask(this.aiAttackOnCollide);
            this.tasks.removeTask(this.aiArrowAttack);
            final ItemStack itemstack = this.getHeldItemMainhand();
            if (itemstack.getItem() == Items.BOW) {
                int i = 20;
                if (this.world.getDifficulty() != EnumDifficulty.HARD) {
                    i = 40;
                }
                this.aiArrowAttack.setAttackCooldown(i);
                this.tasks.addTask(4, this.aiArrowAttack);
            }
            else {
                this.tasks.addTask(4, this.aiAttackOnCollide);
            }
        }
    }
    
    @Override
    public void attackEntityWithRangedAttack(final EntityLivingBase target, final float distanceFactor) {
        final EntityArrow entityarrow = this.getArrow(distanceFactor);
        final double d0 = target.posX - this.posX;
        final double d2 = target.getEntityBoundingBox().minY + target.height / 3.0f - entityarrow.posY;
        final double d3 = target.posZ - this.posZ;
        final double d4 = MathHelper.sqrt(d0 * d0 + d3 * d3);
        entityarrow.shoot(d0, d2 + d4 * 0.20000000298023224, d3, 1.6f, (float)(14 - this.world.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
        this.world.spawnEntity(entityarrow);
    }
    
    protected EntityArrow getArrow(final float p_190726_1_) {
        final EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);
        entitytippedarrow.setEnchantmentEffectsFromEntity(this, p_190726_1_);
        return entitytippedarrow;
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setCombatTask();
    }
    
    @Override
    public void setItemStackToSlot(final EntityEquipmentSlot slotIn, final ItemStack stack) {
        super.setItemStackToSlot(slotIn, stack);
        if (!this.world.isRemote && slotIn == EntityEquipmentSlot.MAINHAND) {
            this.setCombatTask();
        }
    }
    
    @Override
    public float getEyeHeight() {
        return 1.74f;
    }
    
    @Override
    public double getYOffset() {
        return -0.6;
    }
    
    public boolean isSwingingArms() {
        return this.dataManager.get(AbstractSkeleton.SWINGING_ARMS);
    }
    
    @Override
    public void setSwingingArms(final boolean swingingArms) {
        this.dataManager.set(AbstractSkeleton.SWINGING_ARMS, swingingArms);
    }
    
    static {
        SWINGING_ARMS = EntityDataManager.createKey(AbstractSkeleton.class, DataSerializers.BOOLEAN);
    }
}
