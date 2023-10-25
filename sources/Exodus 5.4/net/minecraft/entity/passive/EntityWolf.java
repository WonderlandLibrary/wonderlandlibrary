/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 */
package net.minecraft.entity.passive;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBeg;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityWolf
extends EntityTameable {
    private float headRotationCourse;
    private boolean isWet;
    private float prevTimeWolfIsShaking;
    private boolean isShaking;
    private float headRotationCourseOld;
    private float timeWolfIsShaking;

    public void setAngry(boolean bl) {
        byte by = this.dataWatcher.getWatchableObjectByte(16);
        if (bl) {
            this.dataWatcher.updateObject(16, (byte)(by | 2));
        } else {
            this.dataWatcher.updateObject(16, (byte)(by & 0xFFFFFFFD));
        }
    }

    @Override
    public boolean interact(EntityPlayer entityPlayer) {
        ItemStack itemStack = entityPlayer.inventory.getCurrentItem();
        if (this.isTamed()) {
            if (itemStack != null) {
                EnumDyeColor enumDyeColor;
                if (itemStack.getItem() instanceof ItemFood) {
                    ItemFood itemFood = (ItemFood)itemStack.getItem();
                    if (itemFood.isWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectFloat(18) < 20.0f) {
                        if (!entityPlayer.capabilities.isCreativeMode) {
                            --itemStack.stackSize;
                        }
                        this.heal(itemFood.getHealAmount(itemStack));
                        if (itemStack.stackSize <= 0) {
                            entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
                        }
                        return true;
                    }
                } else if (itemStack.getItem() == Items.dye && (enumDyeColor = EnumDyeColor.byDyeDamage(itemStack.getMetadata())) != this.getCollarColor()) {
                    this.setCollarColor(enumDyeColor);
                    if (!entityPlayer.capabilities.isCreativeMode && --itemStack.stackSize <= 0) {
                        entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
                    }
                    return true;
                }
            }
            if (this.isOwner(entityPlayer) && !this.worldObj.isRemote && !this.isBreedingItem(itemStack)) {
                this.aiSit.setSitting(!this.isSitting());
                this.isJumping = false;
                this.navigator.clearPathEntity();
                this.setAttackTarget(null);
            }
        } else if (itemStack != null && itemStack.getItem() == Items.bone && !this.isAngry()) {
            if (!entityPlayer.capabilities.isCreativeMode) {
                --itemStack.stackSize;
            }
            if (itemStack.stackSize <= 0) {
                entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
            }
            if (!this.worldObj.isRemote) {
                if (this.rand.nextInt(3) == 0) {
                    this.setTamed(true);
                    this.navigator.clearPathEntity();
                    this.setAttackTarget(null);
                    this.aiSit.setSitting(true);
                    this.setHealth(20.0f);
                    this.setOwnerId(entityPlayer.getUniqueID().toString());
                    this.playTameEffect(true);
                    this.worldObj.setEntityState(this, (byte)7);
                } else {
                    this.playTameEffect(false);
                    this.worldObj.setEntityState(this, (byte)6);
                }
            }
            return true;
        }
        return super.interact(entityPlayer);
    }

    public float getTailRotation() {
        return this.isAngry() ? 1.5393804f : (this.isTamed() ? (0.55f - (20.0f - this.dataWatcher.getWatchableObjectFloat(18)) * 0.02f) * (float)Math.PI : 0.62831855f);
    }

    @Override
    public boolean canMateWith(EntityAnimal entityAnimal) {
        if (entityAnimal == this) {
            return false;
        }
        if (!this.isTamed()) {
            return false;
        }
        if (!(entityAnimal instanceof EntityWolf)) {
            return false;
        }
        EntityWolf entityWolf = (EntityWolf)entityAnimal;
        return !entityWolf.isTamed() ? false : (entityWolf.isSitting() ? false : this.isInLove() && entityWolf.isInLove());
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setBoolean("Angry", this.isAngry());
        nBTTagCompound.setByte("CollarColor", (byte)this.getCollarColor().getDyeDamage());
    }

    @Override
    public boolean shouldAttackEntity(EntityLivingBase entityLivingBase, EntityLivingBase entityLivingBase2) {
        if (!(entityLivingBase instanceof EntityCreeper) && !(entityLivingBase instanceof EntityGhast)) {
            EntityWolf entityWolf;
            if (entityLivingBase instanceof EntityWolf && (entityWolf = (EntityWolf)entityLivingBase).isTamed() && entityWolf.getOwner() == entityLivingBase2) {
                return false;
            }
            return entityLivingBase instanceof EntityPlayer && entityLivingBase2 instanceof EntityPlayer && !((EntityPlayer)entityLivingBase2).canAttackPlayer((EntityPlayer)entityLivingBase) ? false : !(entityLivingBase instanceof EntityHorse) || !((EntityHorse)entityLivingBase).isTame();
        }
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.headRotationCourseOld = this.headRotationCourse;
        this.headRotationCourse = this.isBegging() ? (this.headRotationCourse += (1.0f - this.headRotationCourse) * 0.4f) : (this.headRotationCourse += (0.0f - this.headRotationCourse) * 0.4f);
        if (this.isWet()) {
            this.isWet = true;
            this.isShaking = false;
            this.timeWolfIsShaking = 0.0f;
            this.prevTimeWolfIsShaking = 0.0f;
        } else if ((this.isWet || this.isShaking) && this.isShaking) {
            if (this.timeWolfIsShaking == 0.0f) {
                this.playSound("mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
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
                float f = (float)this.getEntityBoundingBox().minY;
                int n = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4f) * (float)Math.PI) * 7.0f);
                int n2 = 0;
                while (n2 < n) {
                    float f2 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width * 0.5f;
                    float f3 = (this.rand.nextFloat() * 2.0f - 1.0f) * this.width * 0.5f;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + (double)f2, (double)(f + 0.8f), this.posZ + (double)f3, this.motionX, this.motionY, this.motionZ, new int[0]);
                    ++n2;
                }
            }
        }
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3f);
        if (this.isTamed()) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0);
        }
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0);
    }

    @Override
    protected boolean canDespawn() {
        return !this.isTamed() && this.ticksExisted > 2400;
    }

    @Override
    protected void playStepSound(BlockPos blockPos, Block block) {
        this.playSound("mob.wolf.step", 0.15f, 1.0f);
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 8) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0f;
            this.prevTimeWolfIsShaking = 0.0f;
        } else {
            super.handleStatusUpdate(by);
        }
    }

    @Override
    protected Item getDropItem() {
        return Item.getItemById(-1);
    }

    public void setCollarColor(EnumDyeColor enumDyeColor) {
        this.dataWatcher.updateObject(20, (byte)(enumDyeColor.getDyeDamage() & 0xF));
    }

    @Override
    public boolean attackEntityAsMob(Entity entity) {
        boolean bl = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (int)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
        if (bl) {
            this.applyEnchantments(this, entity);
        }
        return bl;
    }

    @Override
    public void setTamed(boolean bl) {
        super.setTamed(bl);
        if (bl) {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0);
        } else {
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0);
        }
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0);
    }

    @Override
    public boolean allowLeashing() {
        return !this.isAngry() && super.allowLeashing();
    }

    public float getShadingWhileWet(float f) {
        return 0.75f + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * f) / 2.0f * 0.25f;
    }

    @Override
    protected String getDeathSound() {
        return "mob.wolf.death";
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.setAngry(nBTTagCompound.getBoolean("Angry"));
        if (nBTTagCompound.hasKey("CollarColor", 99)) {
            this.setCollarColor(EnumDyeColor.byDyeDamage(nBTTagCompound.getByte("CollarColor")));
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack itemStack) {
        return itemStack == null ? false : (!(itemStack.getItem() instanceof ItemFood) ? false : ((ItemFood)itemStack.getItem()).isWolfsFavoriteMeat());
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    public boolean isBegging() {
        return this.dataWatcher.getWatchableObjectByte(19) == 1;
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        Entity entity = damageSource.getEntity();
        this.aiSit.setSitting(false);
        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow)) {
            f = (f + 1.0f) / 2.0f;
        }
        return super.attackEntityFrom(damageSource, f);
    }

    @Override
    protected String getHurtSound() {
        return "mob.wolf.hurt";
    }

    @Override
    public int getMaxSpawnedInChunk() {
        return 8;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(18, new Float(this.getHealth()));
        this.dataWatcher.addObject(19, new Byte(0));
        this.dataWatcher.addObject(20, new Byte((byte)EnumDyeColor.RED.getMetadata()));
    }

    public boolean isWolfWet() {
        return this.isWet;
    }

    @Override
    public void setAttackTarget(EntityLivingBase entityLivingBase) {
        super.setAttackTarget(entityLivingBase);
        if (entityLivingBase == null) {
            this.setAngry(false);
        } else if (!this.isTamed()) {
            this.setAngry(true);
        }
    }

    @Override
    public EntityWolf createChild(EntityAgeable entityAgeable) {
        EntityWolf entityWolf = new EntityWolf(this.worldObj);
        String string = this.getOwnerId();
        if (string != null && string.trim().length() > 0) {
            entityWolf.setOwnerId(string);
            entityWolf.setTamed(true);
        }
        return entityWolf;
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.8f;
    }

    public boolean isAngry() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    public float getShakeAngle(float f, float f2) {
        float f3 = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * f + f2) / 1.8f;
        if (f3 < 0.0f) {
            f3 = 0.0f;
        } else if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        return MathHelper.sin(f3 * (float)Math.PI) * MathHelper.sin(f3 * (float)Math.PI * 11.0f) * 0.15f * (float)Math.PI;
    }

    public EntityWolf(World world) {
        super(world);
        this.setSize(0.6f, 0.8f);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4f));
        this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0, true));
        this.tasks.addTask(5, new EntityAIFollowOwner(this, 1.0, 10.0f, 2.0f));
        this.tasks.addTask(6, new EntityAIMate(this, 1.0));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0));
        this.tasks.addTask(8, new EntityAIBeg(this, 8.0f));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(9, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        this.targetTasks.addTask(3, new EntityAIHurtByTarget((EntityCreature)this, true, new Class[0]));
        this.targetTasks.addTask(4, new EntityAITargetNonTamed<Entity>(this, EntityAnimal.class, false, new Predicate<Entity>(){

            public boolean apply(Entity entity) {
                return entity instanceof EntitySheep || entity instanceof EntityRabbit;
            }
        }));
        this.targetTasks.addTask(5, new EntityAINearestAttackableTarget<EntitySkeleton>((EntityCreature)this, EntitySkeleton.class, false));
        this.setTamed(false);
    }

    public EnumDyeColor getCollarColor() {
        return EnumDyeColor.byDyeDamage(this.dataWatcher.getWatchableObjectByte(20) & 0xF);
    }

    public void setBegging(boolean bl) {
        if (bl) {
            this.dataWatcher.updateObject(19, (byte)1);
        } else {
            this.dataWatcher.updateObject(19, (byte)0);
        }
    }

    @Override
    public int getVerticalFaceSpeed() {
        return this.isSitting() ? 20 : super.getVerticalFaceSpeed();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (!this.worldObj.isRemote && this.isWet && !this.isShaking && !this.hasPath() && this.onGround) {
            this.isShaking = true;
            this.timeWolfIsShaking = 0.0f;
            this.prevTimeWolfIsShaking = 0.0f;
            this.worldObj.setEntityState(this, (byte)8);
        }
        if (!this.worldObj.isRemote && this.getAttackTarget() == null && this.isAngry()) {
            this.setAngry(false);
        }
    }

    public float getInterestedAngle(float f) {
        return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * f) * 0.15f * (float)Math.PI;
    }

    @Override
    protected void updateAITasks() {
        this.dataWatcher.updateObject(18, Float.valueOf(this.getHealth()));
    }

    @Override
    protected String getLivingSound() {
        return this.isAngry() ? "mob.wolf.growl" : (this.rand.nextInt(3) == 0 ? (this.isTamed() && this.dataWatcher.getWatchableObjectFloat(18) < 10.0f ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
    }
}

