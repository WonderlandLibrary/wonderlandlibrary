// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import com.google.common.collect.Sets;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.DataFixer;
import javax.annotation.Nullable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.init.Items;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import java.util.Set;
import net.minecraft.network.datasync.DataParameter;

public class EntityPig extends EntityAnimal
{
    private static final DataParameter<Boolean> SADDLED;
    private static final DataParameter<Integer> BOOST_TIME;
    private static final Set<Item> TEMPTATION_ITEMS;
    private boolean boosting;
    private int boostTime;
    private int totalBoostTime;
    
    public EntityPig(final World worldIn) {
        super(worldIn);
        this.setSize(0.9f, 0.9f);
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25));
        this.tasks.addTask(3, new EntityAIMate(this, 1.0));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2, Items.CARROT_ON_A_STICK, false));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2, false, EntityPig.TEMPTATION_ITEMS));
        this.tasks.addTask(5, new EntityAIFollowParent(this, 1.1));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 1.0));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }
    
    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }
    
    @Override
    public boolean canBeSteered() {
        final Entity entity = this.getControllingPassenger();
        if (!(entity instanceof EntityPlayer)) {
            return false;
        }
        final EntityPlayer entityplayer = (EntityPlayer)entity;
        return entityplayer.getHeldItemMainhand().getItem() == Items.CARROT_ON_A_STICK || entityplayer.getHeldItemOffhand().getItem() == Items.CARROT_ON_A_STICK;
    }
    
    @Override
    public void notifyDataManagerChange(final DataParameter<?> key) {
        if (EntityPig.BOOST_TIME.equals(key) && this.world.isRemote) {
            this.boosting = true;
            this.boostTime = 0;
            this.totalBoostTime = this.dataManager.get(EntityPig.BOOST_TIME);
        }
        super.notifyDataManagerChange(key);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityPig.SADDLED, false);
        this.dataManager.register(EntityPig.BOOST_TIME, 0);
    }
    
    public static void registerFixesPig(final DataFixer fixer) {
        EntityLiving.registerFixesMob(fixer, EntityPig.class);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("Saddle", this.getSaddled());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setSaddled(compound.getBoolean("Saddle"));
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PIG_AMBIENT;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_PIG_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PIG_DEATH;
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15f, 1.0f);
    }
    
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        if (super.processInteract(player, hand)) {
            return true;
        }
        final ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.NAME_TAG) {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        }
        if (this.getSaddled() && !this.isBeingRidden()) {
            if (!this.world.isRemote) {
                player.startRiding(this);
            }
            return true;
        }
        if (itemstack.getItem() == Items.SADDLE) {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        }
        return false;
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        super.onDeath(cause);
        if (!this.world.isRemote && this.getSaddled()) {
            this.dropItem(Items.SADDLE, 1);
        }
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_PIG;
    }
    
    public boolean getSaddled() {
        return this.dataManager.get(EntityPig.SADDLED);
    }
    
    public void setSaddled(final boolean saddled) {
        if (saddled) {
            this.dataManager.set(EntityPig.SADDLED, true);
        }
        else {
            this.dataManager.set(EntityPig.SADDLED, false);
        }
    }
    
    @Override
    public void onStruckByLightning(final EntityLightningBolt lightningBolt) {
        if (!this.world.isRemote && !this.isDead) {
            final EntityPigZombie entitypigzombie = new EntityPigZombie(this.world);
            entitypigzombie.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
            entitypigzombie.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entitypigzombie.setNoAI(this.isAIDisabled());
            if (this.hasCustomName()) {
                entitypigzombie.setCustomNameTag(this.getCustomNameTag());
                entitypigzombie.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }
            this.world.spawnEntity(entitypigzombie);
            this.setDead();
        }
    }
    
    @Override
    public void travel(final float strafe, final float vertical, final float forward) {
        final Entity entity = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
        if (this.isBeingRidden() && this.canBeSteered()) {
            this.rotationYaw = entity.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entity.rotationPitch * 0.5f;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.rotationYaw;
            this.stepHeight = 1.0f;
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1f;
            if (this.boosting && this.boostTime++ > this.totalBoostTime) {
                this.boosting = false;
            }
            if (this.canPassengerSteer()) {
                float f = (float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * 0.225f;
                if (this.boosting) {
                    f += f * 1.15f * MathHelper.sin(this.boostTime / (float)this.totalBoostTime * 3.1415927f);
                }
                this.setAIMoveSpeed(f);
                super.travel(0.0f, 0.0f, 1.0f);
            }
            else {
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }
            this.prevLimbSwingAmount = this.limbSwingAmount;
            final double d1 = this.posX - this.prevPosX;
            final double d2 = this.posZ - this.prevPosZ;
            float f2 = MathHelper.sqrt(d1 * d1 + d2 * d2) * 4.0f;
            if (f2 > 1.0f) {
                f2 = 1.0f;
            }
            this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4f;
            this.limbSwing += this.limbSwingAmount;
        }
        else {
            this.stepHeight = 0.5f;
            this.jumpMovementFactor = 0.02f;
            super.travel(strafe, vertical, forward);
        }
    }
    
    public boolean boost() {
        if (this.boosting) {
            return false;
        }
        this.boosting = true;
        this.boostTime = 0;
        this.totalBoostTime = this.getRNG().nextInt(841) + 140;
        this.getDataManager().set(EntityPig.BOOST_TIME, this.totalBoostTime);
        return true;
    }
    
    @Override
    public EntityPig createChild(final EntityAgeable ageable) {
        return new EntityPig(this.world);
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return EntityPig.TEMPTATION_ITEMS.contains(stack.getItem());
    }
    
    static {
        SADDLED = EntityDataManager.createKey(EntityPig.class, DataSerializers.BOOLEAN);
        BOOST_TIME = EntityDataManager.createKey(EntityPig.class, DataSerializers.VARINT);
        TEMPTATION_ITEMS = Sets.newHashSet((Object[])new Item[] { Items.CARROT, Items.POTATO, Items.BEETROOT });
    }
}
