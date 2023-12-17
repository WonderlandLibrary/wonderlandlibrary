// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.walkers.ItemStackData;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.init.MobEffects;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundEvent;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.block.SoundType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import javax.annotation.Nullable;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.world.World;
import net.minecraft.inventory.ContainerHorseChest;
import java.util.UUID;
import com.google.common.base.Optional;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.inventory.IInventoryChangedListener;

public abstract class AbstractHorse extends EntityAnimal implements IInventoryChangedListener, IJumpingMount
{
    private static final Predicate<Entity> IS_HORSE_BREEDING;
    protected static final IAttribute JUMP_STRENGTH;
    private static final DataParameter<Byte> STATUS;
    private static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID;
    private int eatingCounter;
    private int openMouthCounter;
    private int jumpRearingCounter;
    public int tailCounter;
    public int sprintCounter;
    protected boolean horseJumping;
    protected ContainerHorseChest horseChest;
    protected int temper;
    protected float jumpPower;
    private boolean allowStandSliding;
    private float headLean;
    private float prevHeadLean;
    private float rearingAmount;
    private float prevRearingAmount;
    private float mouthOpenness;
    private float prevMouthOpenness;
    protected boolean canGallop;
    protected int gallopTime;
    
    public AbstractHorse(final World worldIn) {
        super(worldIn);
        this.canGallop = true;
        this.setSize(1.3964844f, 1.6f);
        this.stepHeight = 1.0f;
        this.initHorseChest();
    }
    
    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.2));
        this.tasks.addTask(1, new EntityAIRunAroundLikeCrazy(this, 1.2));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0, AbstractHorse.class));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.0));
        this.tasks.addTask(6, new EntityAIWanderAvoidWater(this, 0.7));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(AbstractHorse.STATUS, (Byte)0);
        this.dataManager.register(AbstractHorse.OWNER_UNIQUE_ID, (Optional<UUID>)Optional.absent());
    }
    
    protected boolean getHorseWatchableBoolean(final int p_110233_1_) {
        return (this.dataManager.get(AbstractHorse.STATUS) & p_110233_1_) != 0x0;
    }
    
    protected void setHorseWatchableBoolean(final int p_110208_1_, final boolean p_110208_2_) {
        final byte b0 = this.dataManager.get(AbstractHorse.STATUS);
        if (p_110208_2_) {
            this.dataManager.set(AbstractHorse.STATUS, (byte)(b0 | p_110208_1_));
        }
        else {
            this.dataManager.set(AbstractHorse.STATUS, (byte)(b0 & ~p_110208_1_));
        }
    }
    
    public boolean isTame() {
        return this.getHorseWatchableBoolean(2);
    }
    
    @Nullable
    public UUID getOwnerUniqueId() {
        return (UUID)this.dataManager.get(AbstractHorse.OWNER_UNIQUE_ID).orNull();
    }
    
    public void setOwnerUniqueId(@Nullable final UUID uniqueId) {
        this.dataManager.set(AbstractHorse.OWNER_UNIQUE_ID, (Optional<UUID>)Optional.fromNullable((Object)uniqueId));
    }
    
    public float getHorseSize() {
        return 0.5f;
    }
    
    @Override
    public void setScaleForAge(final boolean child) {
        this.setScale(child ? this.getHorseSize() : 1.0f);
    }
    
    public boolean isHorseJumping() {
        return this.horseJumping;
    }
    
    public void setHorseTamed(final boolean tamed) {
        this.setHorseWatchableBoolean(2, tamed);
    }
    
    public void setHorseJumping(final boolean jumping) {
        this.horseJumping = jumping;
    }
    
    @Override
    public boolean canBeLeashedTo(final EntityPlayer player) {
        return super.canBeLeashedTo(player) && this.getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
    }
    
    @Override
    protected void onLeashDistance(final float p_142017_1_) {
        if (p_142017_1_ > 6.0f && this.isEatingHaystack()) {
            this.setEatingHaystack(false);
        }
    }
    
    public boolean isEatingHaystack() {
        return this.getHorseWatchableBoolean(16);
    }
    
    public boolean isRearing() {
        return this.getHorseWatchableBoolean(32);
    }
    
    public boolean isBreeding() {
        return this.getHorseWatchableBoolean(8);
    }
    
    public void setBreeding(final boolean breeding) {
        this.setHorseWatchableBoolean(8, breeding);
    }
    
    public void setHorseSaddled(final boolean saddled) {
        this.setHorseWatchableBoolean(4, saddled);
    }
    
    public int getTemper() {
        return this.temper;
    }
    
    public void setTemper(final int temperIn) {
        this.temper = temperIn;
    }
    
    public int increaseTemper(final int p_110198_1_) {
        final int i = MathHelper.clamp(this.getTemper() + p_110198_1_, 0, this.getMaxTemper());
        this.setTemper(i);
        return i;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        final Entity entity = source.getTrueSource();
        return (!this.isBeingRidden() || entity == null || !this.isRidingOrBeingRiddenBy(entity)) && super.attackEntityFrom(source, amount);
    }
    
    @Override
    public boolean canBePushed() {
        return !this.isBeingRidden();
    }
    
    private void eatingHorse() {
        this.openHorseMouth();
        if (!this.isSilent()) {
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_HORSE_EAT, this.getSoundCategory(), 1.0f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f);
        }
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        if (distance > 1.0f) {
            this.playSound(SoundEvents.ENTITY_HORSE_LAND, 0.4f, 1.0f);
        }
        final int i = MathHelper.ceil((distance * 0.5f - 3.0f) * damageMultiplier);
        if (i > 0) {
            this.attackEntityFrom(DamageSource.FALL, (float)i);
            if (this.isBeingRidden()) {
                for (final Entity entity : this.getRecursivePassengers()) {
                    entity.attackEntityFrom(DamageSource.FALL, (float)i);
                }
            }
            final IBlockState iblockstate = this.world.getBlockState(new BlockPos(this.posX, this.posY - 0.2 - this.prevRotationYaw, this.posZ));
            final Block block = iblockstate.getBlock();
            if (iblockstate.getMaterial() != Material.AIR && !this.isSilent()) {
                final SoundType soundtype = block.getSoundType();
                this.world.playSound(null, this.posX, this.posY, this.posZ, soundtype.getStepSound(), this.getSoundCategory(), soundtype.getVolume() * 0.5f, soundtype.getPitch() * 0.75f);
            }
        }
    }
    
    protected int getInventorySize() {
        return 2;
    }
    
    protected void initHorseChest() {
        final ContainerHorseChest containerhorsechest = this.horseChest;
        (this.horseChest = new ContainerHorseChest("HorseChest", this.getInventorySize())).setCustomName(this.getName());
        if (containerhorsechest != null) {
            containerhorsechest.removeInventoryChangeListener(this);
            for (int i = Math.min(containerhorsechest.getSizeInventory(), this.horseChest.getSizeInventory()), j = 0; j < i; ++j) {
                final ItemStack itemstack = containerhorsechest.getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    this.horseChest.setInventorySlotContents(j, itemstack.copy());
                }
            }
        }
        this.horseChest.addInventoryChangeListener(this);
        this.updateHorseSlots();
    }
    
    protected void updateHorseSlots() {
        if (!this.world.isRemote) {
            this.setHorseSaddled(!this.horseChest.getStackInSlot(0).isEmpty() && this.canBeSaddled());
        }
    }
    
    @Override
    public void onInventoryChanged(final IInventory invBasic) {
        final boolean flag = this.isHorseSaddled();
        this.updateHorseSlots();
        if (this.ticksExisted > 20 && !flag && this.isHorseSaddled()) {
            this.playSound(SoundEvents.ENTITY_HORSE_SADDLE, 0.5f, 1.0f);
        }
    }
    
    @Nullable
    protected AbstractHorse getClosestHorse(final Entity entityIn, final double distance) {
        double d0 = Double.MAX_VALUE;
        Entity entity = null;
        for (final Entity entity2 : this.world.getEntitiesInAABBexcluding(entityIn, entityIn.getEntityBoundingBox().expand(distance, distance, distance), AbstractHorse.IS_HORSE_BREEDING)) {
            final double d2 = entity2.getDistanceSq(entityIn.posX, entityIn.posY, entityIn.posZ);
            if (d2 < d0) {
                entity = entity2;
                d0 = d2;
            }
        }
        return (AbstractHorse)entity;
    }
    
    public double getHorseJumpStrength() {
        return this.getEntityAttribute(AbstractHorse.JUMP_STRENGTH).getAttributeValue();
    }
    
    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        this.openHorseMouth();
        return null;
    }
    
    @Nullable
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        this.openHorseMouth();
        if (this.rand.nextInt(3) == 0) {
            this.makeHorseRear();
        }
        return null;
    }
    
    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        this.openHorseMouth();
        if (this.rand.nextInt(10) == 0 && !this.isMovementBlocked()) {
            this.makeHorseRear();
        }
        return null;
    }
    
    public boolean canBeSaddled() {
        return true;
    }
    
    public boolean isHorseSaddled() {
        return this.getHorseWatchableBoolean(4);
    }
    
    @Nullable
    protected SoundEvent getAngrySound() {
        this.openHorseMouth();
        this.makeHorseRear();
        return null;
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        if (!blockIn.getDefaultState().getMaterial().isLiquid()) {
            SoundType soundtype = blockIn.getSoundType();
            if (this.world.getBlockState(pos.up()).getBlock() == Blocks.SNOW_LAYER) {
                soundtype = Blocks.SNOW_LAYER.getSoundType();
            }
            if (this.isBeingRidden() && this.canGallop) {
                ++this.gallopTime;
                if (this.gallopTime > 5 && this.gallopTime % 3 == 0) {
                    this.playGallopSound(soundtype);
                }
                else if (this.gallopTime <= 5) {
                    this.playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, soundtype.getVolume() * 0.15f, soundtype.getPitch());
                }
            }
            else if (soundtype == SoundType.WOOD) {
                this.playSound(SoundEvents.ENTITY_HORSE_STEP_WOOD, soundtype.getVolume() * 0.15f, soundtype.getPitch());
            }
            else {
                this.playSound(SoundEvents.ENTITY_HORSE_STEP, soundtype.getVolume() * 0.15f, soundtype.getPitch());
            }
        }
    }
    
    protected void playGallopSound(final SoundType p_190680_1_) {
        this.playSound(SoundEvents.ENTITY_HORSE_GALLOP, p_190680_1_.getVolume() * 0.15f, p_190680_1_.getPitch());
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(AbstractHorse.JUMP_STRENGTH);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(53.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.22499999403953552);
    }
    
    @Override
    public int getMaxSpawnedInChunk() {
        return 6;
    }
    
    public int getMaxTemper() {
        return 100;
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.8f;
    }
    
    @Override
    public int getTalkInterval() {
        return 400;
    }
    
    public void openGUI(final EntityPlayer playerEntity) {
        if (!this.world.isRemote && (!this.isBeingRidden() || this.isPassenger(playerEntity)) && this.isTame()) {
            this.horseChest.setCustomName(this.getName());
            playerEntity.openGuiHorseInventory(this, this.horseChest);
        }
    }
    
    protected boolean handleEating(final EntityPlayer player, final ItemStack stack) {
        boolean flag = false;
        float f = 0.0f;
        int i = 0;
        int j = 0;
        final Item item = stack.getItem();
        if (item == Items.WHEAT) {
            f = 2.0f;
            i = 20;
            j = 3;
        }
        else if (item == Items.SUGAR) {
            f = 1.0f;
            i = 30;
            j = 3;
        }
        else if (item == Item.getItemFromBlock(Blocks.HAY_BLOCK)) {
            f = 20.0f;
            i = 180;
        }
        else if (item == Items.APPLE) {
            f = 3.0f;
            i = 60;
            j = 3;
        }
        else if (item == Items.GOLDEN_CARROT) {
            f = 4.0f;
            i = 60;
            j = 5;
            if (this.isTame() && this.getGrowingAge() == 0 && !this.isInLove()) {
                flag = true;
                this.setInLove(player);
            }
        }
        else if (item == Items.GOLDEN_APPLE) {
            f = 10.0f;
            i = 240;
            j = 10;
            if (this.isTame() && this.getGrowingAge() == 0 && !this.isInLove()) {
                flag = true;
                this.setInLove(player);
            }
        }
        if (this.getHealth() < this.getMaxHealth() && f > 0.0f) {
            this.heal(f);
            flag = true;
        }
        if (this.isChild() && i > 0) {
            this.world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, 0.0, 0.0, 0.0, new int[0]);
            if (!this.world.isRemote) {
                this.addGrowth(i);
            }
            flag = true;
        }
        if (j > 0 && (flag || !this.isTame()) && this.getTemper() < this.getMaxTemper()) {
            flag = true;
            if (!this.world.isRemote) {
                this.increaseTemper(j);
            }
        }
        if (flag) {
            this.eatingHorse();
        }
        return flag;
    }
    
    protected void mountTo(final EntityPlayer player) {
        player.rotationYaw = this.rotationYaw;
        player.rotationPitch = this.rotationPitch;
        this.setEatingHaystack(false);
        this.setRearing(false);
        if (!this.world.isRemote) {
            player.startRiding(this);
        }
    }
    
    @Override
    protected boolean isMovementBlocked() {
        return (super.isMovementBlocked() && this.isBeingRidden() && this.isHorseSaddled()) || this.isEatingHaystack() || this.isRearing();
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return false;
    }
    
    private void moveTail() {
        this.tailCounter = 1;
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        super.onDeath(cause);
        if (!this.world.isRemote && this.horseChest != null) {
            for (int i = 0; i < this.horseChest.getSizeInventory(); ++i) {
                final ItemStack itemstack = this.horseChest.getStackInSlot(i);
                if (!itemstack.isEmpty()) {
                    this.entityDropItem(itemstack, 0.0f);
                }
            }
        }
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.rand.nextInt(200) == 0) {
            this.moveTail();
        }
        super.onLivingUpdate();
        if (!this.world.isRemote) {
            if (this.rand.nextInt(900) == 0 && this.deathTime == 0) {
                this.heal(1.0f);
            }
            if (this.canEatGrass()) {
                if (!this.isEatingHaystack() && !this.isBeingRidden() && this.rand.nextInt(300) == 0 && this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY) - 1, MathHelper.floor(this.posZ))).getBlock() == Blocks.GRASS) {
                    this.setEatingHaystack(true);
                }
                if (this.isEatingHaystack() && ++this.eatingCounter > 50) {
                    this.eatingCounter = 0;
                    this.setEatingHaystack(false);
                }
            }
            this.followMother();
        }
    }
    
    protected void followMother() {
        if (this.isBreeding() && this.isChild() && !this.isEatingHaystack()) {
            final AbstractHorse abstracthorse = this.getClosestHorse(this, 16.0);
            if (abstracthorse != null && this.getDistanceSq(abstracthorse) > 4.0) {
                this.navigator.getPathToEntityLiving(abstracthorse);
            }
        }
    }
    
    public boolean canEatGrass() {
        return true;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.openMouthCounter > 0 && ++this.openMouthCounter > 30) {
            this.openMouthCounter = 0;
            this.setHorseWatchableBoolean(64, false);
        }
        if (this.canPassengerSteer() && this.jumpRearingCounter > 0 && ++this.jumpRearingCounter > 20) {
            this.jumpRearingCounter = 0;
            this.setRearing(false);
        }
        if (this.tailCounter > 0 && ++this.tailCounter > 8) {
            this.tailCounter = 0;
        }
        if (this.sprintCounter > 0) {
            ++this.sprintCounter;
            if (this.sprintCounter > 300) {
                this.sprintCounter = 0;
            }
        }
        this.prevHeadLean = this.headLean;
        if (this.isEatingHaystack()) {
            this.headLean += (1.0f - this.headLean) * 0.4f + 0.05f;
            if (this.headLean > 1.0f) {
                this.headLean = 1.0f;
            }
        }
        else {
            this.headLean += (0.0f - this.headLean) * 0.4f - 0.05f;
            if (this.headLean < 0.0f) {
                this.headLean = 0.0f;
            }
        }
        this.prevRearingAmount = this.rearingAmount;
        if (this.isRearing()) {
            this.headLean = 0.0f;
            this.prevHeadLean = this.headLean;
            this.rearingAmount += (1.0f - this.rearingAmount) * 0.4f + 0.05f;
            if (this.rearingAmount > 1.0f) {
                this.rearingAmount = 1.0f;
            }
        }
        else {
            this.allowStandSliding = false;
            this.rearingAmount += (0.8f * this.rearingAmount * this.rearingAmount * this.rearingAmount - this.rearingAmount) * 0.6f - 0.05f;
            if (this.rearingAmount < 0.0f) {
                this.rearingAmount = 0.0f;
            }
        }
        this.prevMouthOpenness = this.mouthOpenness;
        if (this.getHorseWatchableBoolean(64)) {
            this.mouthOpenness += (1.0f - this.mouthOpenness) * 0.7f + 0.05f;
            if (this.mouthOpenness > 1.0f) {
                this.mouthOpenness = 1.0f;
            }
        }
        else {
            this.mouthOpenness += (0.0f - this.mouthOpenness) * 0.7f - 0.05f;
            if (this.mouthOpenness < 0.0f) {
                this.mouthOpenness = 0.0f;
            }
        }
    }
    
    private void openHorseMouth() {
        if (!this.world.isRemote) {
            this.openMouthCounter = 1;
            this.setHorseWatchableBoolean(64, true);
        }
    }
    
    public void setEatingHaystack(final boolean p_110227_1_) {
        this.setHorseWatchableBoolean(16, p_110227_1_);
    }
    
    public void setRearing(final boolean rearing) {
        if (rearing) {
            this.setEatingHaystack(false);
        }
        this.setHorseWatchableBoolean(32, rearing);
    }
    
    private void makeHorseRear() {
        if (this.canPassengerSteer()) {
            this.jumpRearingCounter = 1;
            this.setRearing(true);
        }
    }
    
    public void makeMad() {
        this.makeHorseRear();
        final SoundEvent soundevent = this.getAngrySound();
        if (soundevent != null) {
            this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
        }
    }
    
    public boolean setTamedBy(final EntityPlayer player) {
        this.setOwnerUniqueId(player.getUniqueID());
        this.setHorseTamed(true);
        if (player instanceof EntityPlayerMP) {
            CriteriaTriggers.TAME_ANIMAL.trigger((EntityPlayerMP)player, this);
        }
        this.world.setEntityState(this, (byte)7);
        return true;
    }
    
    @Override
    public void travel(float strafe, final float vertical, float forward) {
        if (this.isBeingRidden() && this.canBeSteered() && this.isHorseSaddled()) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase)this.getControllingPassenger();
            this.rotationYaw = entitylivingbase.rotationYaw;
            this.prevRotationYaw = this.rotationYaw;
            this.rotationPitch = entitylivingbase.rotationPitch * 0.5f;
            this.setRotation(this.rotationYaw, this.rotationPitch);
            this.renderYawOffset = this.rotationYaw;
            this.rotationYawHead = this.renderYawOffset;
            strafe = entitylivingbase.moveStrafing * 0.5f;
            forward = entitylivingbase.moveForward;
            if (forward <= 0.0f) {
                forward *= 0.25f;
                this.gallopTime = 0;
            }
            if (this.onGround && this.jumpPower == 0.0f && this.isRearing() && !this.allowStandSliding) {
                strafe = 0.0f;
                forward = 0.0f;
            }
            if (this.jumpPower > 0.0f && !this.isHorseJumping() && this.onGround) {
                this.motionY = this.getHorseJumpStrength() * this.jumpPower;
                if (this.isPotionActive(MobEffects.JUMP_BOOST)) {
                    this.motionY += (this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1f;
                }
                this.setHorseJumping(true);
                this.isAirBorne = true;
                if (forward > 0.0f) {
                    final float f = MathHelper.sin(this.rotationYaw * 0.017453292f);
                    final float f2 = MathHelper.cos(this.rotationYaw * 0.017453292f);
                    this.motionX += -0.4f * f * this.jumpPower;
                    this.motionZ += 0.4f * f2 * this.jumpPower;
                    this.playSound(SoundEvents.ENTITY_HORSE_JUMP, 0.4f, 1.0f);
                }
                this.jumpPower = 0.0f;
            }
            this.jumpMovementFactor = this.getAIMoveSpeed() * 0.1f;
            if (this.canPassengerSteer()) {
                this.setAIMoveSpeed((float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
                super.travel(strafe, vertical, forward);
            }
            else if (entitylivingbase instanceof EntityPlayer) {
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }
            if (this.onGround) {
                this.jumpPower = 0.0f;
                this.setHorseJumping(false);
            }
            this.prevLimbSwingAmount = this.limbSwingAmount;
            final double d1 = this.posX - this.prevPosX;
            final double d2 = this.posZ - this.prevPosZ;
            float f3 = MathHelper.sqrt(d1 * d1 + d2 * d2) * 4.0f;
            if (f3 > 1.0f) {
                f3 = 1.0f;
            }
            this.limbSwingAmount += (f3 - this.limbSwingAmount) * 0.4f;
            this.limbSwing += this.limbSwingAmount;
        }
        else {
            this.jumpMovementFactor = 0.02f;
            super.travel(strafe, vertical, forward);
        }
    }
    
    public static void registerFixesAbstractHorse(final DataFixer fixer, final Class<?> entityClass) {
        EntityLiving.registerFixesMob(fixer, entityClass);
        fixer.registerWalker(FixTypes.ENTITY, new ItemStackData(entityClass, new String[] { "SaddleItem" }));
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("EatingHaystack", this.isEatingHaystack());
        compound.setBoolean("Bred", this.isBreeding());
        compound.setInteger("Temper", this.getTemper());
        compound.setBoolean("Tame", this.isTame());
        if (this.getOwnerUniqueId() != null) {
            compound.setString("OwnerUUID", this.getOwnerUniqueId().toString());
        }
        if (!this.horseChest.getStackInSlot(0).isEmpty()) {
            compound.setTag("SaddleItem", this.horseChest.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setEatingHaystack(compound.getBoolean("EatingHaystack"));
        this.setBreeding(compound.getBoolean("Bred"));
        this.setTemper(compound.getInteger("Temper"));
        this.setHorseTamed(compound.getBoolean("Tame"));
        String s;
        if (compound.hasKey("OwnerUUID", 8)) {
            s = compound.getString("OwnerUUID");
        }
        else {
            final String s2 = compound.getString("Owner");
            s = PreYggdrasilConverter.convertMobOwnerIfNeeded(this.getServer(), s2);
        }
        if (!s.isEmpty()) {
            this.setOwnerUniqueId(UUID.fromString(s));
        }
        final IAttributeInstance iattributeinstance = this.getAttributeMap().getAttributeInstanceByName("Speed");
        if (iattributeinstance != null) {
            this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(iattributeinstance.getBaseValue() * 0.25);
        }
        if (compound.hasKey("SaddleItem", 10)) {
            final ItemStack itemstack = new ItemStack(compound.getCompoundTag("SaddleItem"));
            if (itemstack.getItem() == Items.SADDLE) {
                this.horseChest.setInventorySlotContents(0, itemstack);
            }
        }
        this.updateHorseSlots();
    }
    
    @Override
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        return false;
    }
    
    protected boolean canMate() {
        return !this.isBeingRidden() && !this.isRiding() && this.isTame() && !this.isChild() && this.getHealth() >= this.getMaxHealth() && this.isInLove();
    }
    
    @Nullable
    @Override
    public EntityAgeable createChild(final EntityAgeable ageable) {
        return null;
    }
    
    protected void setOffspringAttributes(final EntityAgeable p_190681_1_, final AbstractHorse p_190681_2_) {
        final double d0 = this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + p_190681_1_.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() + this.getModifiedMaxHealth();
        p_190681_2_.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(d0 / 3.0);
        final double d2 = this.getEntityAttribute(AbstractHorse.JUMP_STRENGTH).getBaseValue() + p_190681_1_.getEntityAttribute(AbstractHorse.JUMP_STRENGTH).getBaseValue() + this.getModifiedJumpStrength();
        p_190681_2_.getEntityAttribute(AbstractHorse.JUMP_STRENGTH).setBaseValue(d2 / 3.0);
        final double d3 = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() + p_190681_1_.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() + this.getModifiedMovementSpeed();
        p_190681_2_.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(d3 / 3.0);
    }
    
    @Override
    public boolean canBeSteered() {
        return this.getControllingPassenger() instanceof EntityLivingBase;
    }
    
    public float getGrassEatingAmount(final float p_110258_1_) {
        return this.prevHeadLean + (this.headLean - this.prevHeadLean) * p_110258_1_;
    }
    
    public float getRearingAmount(final float p_110223_1_) {
        return this.prevRearingAmount + (this.rearingAmount - this.prevRearingAmount) * p_110223_1_;
    }
    
    public float getMouthOpennessAngle(final float p_110201_1_) {
        return this.prevMouthOpenness + (this.mouthOpenness - this.prevMouthOpenness) * p_110201_1_;
    }
    
    @Override
    public void setJumpPower(int jumpPowerIn) {
        if (this.isHorseSaddled()) {
            if (jumpPowerIn < 0) {
                jumpPowerIn = 0;
            }
            else {
                this.allowStandSliding = true;
                this.makeHorseRear();
            }
            if (jumpPowerIn >= 90) {
                this.jumpPower = 1.0f;
            }
            else {
                this.jumpPower = 0.4f + 0.4f * jumpPowerIn / 90.0f;
            }
        }
    }
    
    @Override
    public boolean canJump() {
        return this.isHorseSaddled();
    }
    
    @Override
    public void handleStartJump(final int p_184775_1_) {
        this.allowStandSliding = true;
        this.makeHorseRear();
    }
    
    @Override
    public void handleStopJump() {
    }
    
    protected void spawnHorseParticles(final boolean p_110216_1_) {
        final EnumParticleTypes enumparticletypes = p_110216_1_ ? EnumParticleTypes.HEART : EnumParticleTypes.SMOKE_NORMAL;
        for (int i = 0; i < 7; ++i) {
            final double d0 = this.rand.nextGaussian() * 0.02;
            final double d2 = this.rand.nextGaussian() * 0.02;
            final double d3 = this.rand.nextGaussian() * 0.02;
            this.world.spawnParticle(enumparticletypes, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, d0, d2, d3, new int[0]);
        }
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 7) {
            this.spawnHorseParticles(true);
        }
        else if (id == 6) {
            this.spawnHorseParticles(false);
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    @Override
    public void updatePassenger(final Entity passenger) {
        super.updatePassenger(passenger);
        if (passenger instanceof EntityLiving) {
            final EntityLiving entityliving = (EntityLiving)passenger;
            this.renderYawOffset = entityliving.renderYawOffset;
        }
        if (this.prevRearingAmount > 0.0f) {
            final float f3 = MathHelper.sin(this.renderYawOffset * 0.017453292f);
            final float f4 = MathHelper.cos(this.renderYawOffset * 0.017453292f);
            final float f5 = 0.7f * this.prevRearingAmount;
            final float f6 = 0.15f * this.prevRearingAmount;
            passenger.setPosition(this.posX + f5 * f3, this.posY + this.getMountedYOffset() + passenger.getYOffset() + f6, this.posZ - f5 * f4);
            if (passenger instanceof EntityLivingBase) {
                ((EntityLivingBase)passenger).renderYawOffset = this.renderYawOffset;
            }
        }
    }
    
    protected float getModifiedMaxHealth() {
        return 15.0f + this.rand.nextInt(8) + this.rand.nextInt(9);
    }
    
    protected double getModifiedJumpStrength() {
        return 0.4000000059604645 + this.rand.nextDouble() * 0.2 + this.rand.nextDouble() * 0.2 + this.rand.nextDouble() * 0.2;
    }
    
    protected double getModifiedMovementSpeed() {
        return (0.44999998807907104 + this.rand.nextDouble() * 0.3 + this.rand.nextDouble() * 0.3 + this.rand.nextDouble() * 0.3) * 0.25;
    }
    
    @Override
    public boolean isOnLadder() {
        return false;
    }
    
    @Override
    public float getEyeHeight() {
        return this.height;
    }
    
    public boolean wearsArmor() {
        return false;
    }
    
    public boolean isArmor(final ItemStack stack) {
        return false;
    }
    
    @Override
    public boolean replaceItemInInventory(final int inventorySlot, final ItemStack itemStackIn) {
        final int i = inventorySlot - 400;
        if (i >= 0 && i < 2 && i < this.horseChest.getSizeInventory()) {
            if (i == 0 && itemStackIn.getItem() != Items.SADDLE) {
                return false;
            }
            if (i != 1 || (this.wearsArmor() && this.isArmor(itemStackIn))) {
                this.horseChest.setInventorySlotContents(i, itemStackIn);
                this.updateHorseSlots();
                return true;
            }
            return false;
        }
        else {
            final int j = inventorySlot - 500 + 2;
            if (j >= 2 && j < this.horseChest.getSizeInventory()) {
                this.horseChest.setInventorySlotContents(j, itemStackIn);
                return true;
            }
            return false;
        }
    }
    
    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);
        if (this.rand.nextInt(5) == 0) {
            this.setGrowingAge(-24000);
        }
        return livingdata;
    }
    
    static {
        IS_HORSE_BREEDING = (Predicate)new Predicate<Entity>() {
            public boolean apply(@Nullable final Entity p_apply_1_) {
                return p_apply_1_ instanceof AbstractHorse && ((AbstractHorse)p_apply_1_).isBreeding();
            }
        };
        JUMP_STRENGTH = new RangedAttribute(null, "horse.jumpStrength", 0.7, 0.0, 2.0).setDescription("Jump Strength").setShouldWatch(true);
        STATUS = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.BYTE);
        OWNER_UNIQUE_ID = EntityDataManager.createKey(AbstractHorse.class, DataSerializers.OPTIONAL_UNIQUE_ID);
    }
}
