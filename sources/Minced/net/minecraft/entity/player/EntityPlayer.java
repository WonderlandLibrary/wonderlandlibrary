// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.player;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.util.EnumHandSide;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemArmor;
import net.minecraft.world.LockCode;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import ru.tuskevich.util.animations.AnimationMath;
import ru.tuskevich.modules.impl.MOVEMENT.Strafe;
import ru.tuskevich.modules.impl.MOVEMENT.Speed;
import ru.tuskevich.modules.impl.MOVEMENT.Flight;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import ru.tuskevich.modules.impl.PLAYER.NoPush;
import net.minecraft.entity.passive.EntityTameable;
import com.google.common.collect.Lists;
import net.minecraft.world.GameType;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.stats.StatBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.init.Blocks;
import com.google.common.base.Predicate;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import ru.tuskevich.modules.impl.COMBAT.KillAura;
import net.minecraft.item.ItemSword;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.util.EnumActionResult;
import net.minecraft.world.IInteractionObject;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.IMerchant;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntitySign;
import java.util.Iterator;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHand;
import net.minecraft.scoreboard.Team;
import net.minecraft.item.ItemAxe;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.datafix.IFixType;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.block.material.Material;
import net.minecraft.init.MobEffects;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityParrot;
import java.util.List;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.Entity;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.stats.StatList;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.PLAYER.FreeCamera;
import ru.tuskevich.Minced;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.EventManager;
import ru.tuskevich.event.events.impl.ClipEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.CooldownTracker;
import net.minecraft.item.ItemStack;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.FoodStats;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.entity.EntityLivingBase;

public abstract class EntityPlayer extends EntityLivingBase
{
    private static final DataParameter<Float> ABSORPTION;
    private static final DataParameter<Integer> PLAYER_SCORE;
    protected static final DataParameter<Byte> PLAYER_MODEL_FLAG;
    protected static final DataParameter<Byte> MAIN_HAND;
    protected static final DataParameter<NBTTagCompound> LEFT_SHOULDER_ENTITY;
    protected static final DataParameter<NBTTagCompound> RIGHT_SHOULDER_ENTITY;
    public InventoryPlayer inventory;
    protected InventoryEnderChest enderChest;
    public Container inventoryContainer;
    public Container openContainer;
    protected FoodStats foodStats;
    protected int flyToggleTimer;
    public float prevCameraYaw;
    public float cameraYaw;
    public int xpCooldown;
    public double prevChasingPosX;
    public double prevChasingPosY;
    public double prevChasingPosZ;
    public double chasingPosX;
    public double chasingPosY;
    public double chasingPosZ;
    protected boolean sleeping;
    public BlockPos bedLocation;
    private int sleepTimer;
    public float renderOffsetX;
    public float renderOffsetY;
    public float renderOffsetZ;
    private BlockPos spawnPos;
    private boolean spawnForced;
    public PlayerCapabilities capabilities;
    public int experienceLevel;
    public int experienceTotal;
    public float experience;
    protected int xpSeed;
    public float speedInAir;
    private int lastXPSound;
    private final GameProfile gameProfile;
    private boolean hasReducedDebug;
    private ItemStack itemStackMainHand;
    private final CooldownTracker cooldownTracker;
    @Nullable
    public EntityFishHook fishEntity;
    float fLerp;
    
    protected CooldownTracker createCooldownTracker() {
        return new CooldownTracker();
    }
    
    public EntityPlayer(final World worldIn, final GameProfile gameProfileIn) {
        super(worldIn);
        this.inventory = new InventoryPlayer(this);
        this.enderChest = new InventoryEnderChest();
        this.foodStats = new FoodStats();
        this.capabilities = new PlayerCapabilities();
        this.speedInAir = 0.02f;
        this.itemStackMainHand = ItemStack.EMPTY;
        this.cooldownTracker = this.createCooldownTracker();
        this.setUniqueId(getUUID(gameProfileIn));
        this.gameProfile = gameProfileIn;
        this.inventoryContainer = new ContainerPlayer(this.inventory, !worldIn.isRemote, this);
        this.openContainer = this.inventoryContainer;
        final BlockPos blockpos = worldIn.getSpawnPoint();
        this.setLocationAndAngles(blockpos.getX() + 0.5, blockpos.getY() + 1, blockpos.getZ() + 0.5, 0.0f, 0.0f);
        this.unused180 = 180.0f;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.10000000149011612);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.LUCK);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityPlayer.ABSORPTION, 0.0f);
        this.dataManager.register(EntityPlayer.PLAYER_SCORE, 0);
        this.dataManager.register(EntityPlayer.PLAYER_MODEL_FLAG, (Byte)0);
        this.dataManager.register(EntityPlayer.MAIN_HAND, (Byte)1);
        this.dataManager.register(EntityPlayer.LEFT_SHOULDER_ENTITY, new NBTTagCompound());
        this.dataManager.register(EntityPlayer.RIGHT_SHOULDER_ENTITY, new NBTTagCompound());
    }
    
    @Override
    public void onUpdate() {
        final ClipEvent clipEvent = new ClipEvent();
        EventManager.call(clipEvent);
        this.noClip = (this.isSpectator() || clipEvent.isCanceled() || Minced.getInstance().manager.getModule(FreeCamera.class).state);
        if (this.isSpectator()) {
            this.onGround = false;
        }
        if (this.xpCooldown > 0) {
            --this.xpCooldown;
        }
        if (this.isPlayerSleeping()) {
            ++this.sleepTimer;
            if (this.sleepTimer > 100) {
                this.sleepTimer = 100;
            }
            if (!this.world.isRemote) {
                if (!this.isInBed()) {
                    this.wakeUpPlayer(true, true, false);
                }
                else if (this.world.isDaytime()) {
                    this.wakeUpPlayer(false, true, true);
                }
            }
        }
        else if (this.sleepTimer > 0) {
            ++this.sleepTimer;
            if (this.sleepTimer >= 110) {
                this.sleepTimer = 0;
            }
        }
        super.onUpdate();
        if (!this.world.isRemote && this.openContainer != null && !this.openContainer.canInteractWith(this)) {
            this.closeScreen();
            this.openContainer = this.inventoryContainer;
        }
        if (this.isBurning() && this.capabilities.disableDamage) {
            this.extinguish();
        }
        this.updateCape();
        if (!this.world.isRemote) {
            this.foodStats.onUpdate(this);
            this.addStat(StatList.PLAY_ONE_MINUTE);
            if (this.isEntityAlive()) {
                this.addStat(StatList.TIME_SINCE_DEATH);
            }
            if (this.isSneaking()) {
                this.addStat(StatList.SNEAK_TIME);
            }
        }
        final int i = 29999999;
        final double d0 = MathHelper.clamp(this.posX, -2.9999999E7, 2.9999999E7);
        final double d2 = MathHelper.clamp(this.posZ, -2.9999999E7, 2.9999999E7);
        if (d0 != this.posX || d2 != this.posZ) {
            this.setPosition(d0, this.posY, d2);
        }
        ++this.ticksSinceLastSwing;
        final ItemStack itemstack = this.getHeldItemMainhand();
        if (!ItemStack.areItemStacksEqual(this.itemStackMainHand, itemstack)) {
            if (!ItemStack.areItemsEqualIgnoreDurability(this.itemStackMainHand, itemstack)) {
                this.resetCooldown();
            }
            this.itemStackMainHand = (itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy());
        }
        this.cooldownTracker.tick();
        this.updateSize();
    }
    
    private void updateCape() {
        this.prevChasingPosX = this.chasingPosX;
        this.prevChasingPosY = this.chasingPosY;
        this.prevChasingPosZ = this.chasingPosZ;
        final double d0 = this.posX - this.chasingPosX;
        final double d2 = this.posY - this.chasingPosY;
        final double d3 = this.posZ - this.chasingPosZ;
        final double d4 = 10.0;
        if (d0 > 10.0) {
            this.chasingPosX = this.posX;
            this.prevChasingPosX = this.chasingPosX;
        }
        if (d3 > 10.0) {
            this.chasingPosZ = this.posZ;
            this.prevChasingPosZ = this.chasingPosZ;
        }
        if (d2 > 10.0) {
            this.chasingPosY = this.posY;
            this.prevChasingPosY = this.chasingPosY;
        }
        if (d0 < -10.0) {
            this.chasingPosX = this.posX;
            this.prevChasingPosX = this.chasingPosX;
        }
        if (d3 < -10.0) {
            this.chasingPosZ = this.posZ;
            this.prevChasingPosZ = this.chasingPosZ;
        }
        if (d2 < -10.0) {
            this.chasingPosY = this.posY;
            this.prevChasingPosY = this.chasingPosY;
        }
        this.chasingPosX += d0 * 0.25;
        this.chasingPosZ += d3 * 0.25;
        this.chasingPosY += d2 * 0.25;
    }
    
    protected void updateSize() {
        float f;
        float f2;
        if (this.isElytraFlying()) {
            f = 0.6f;
            f2 = 0.6f;
        }
        else if (this.isPlayerSleeping()) {
            f = 0.2f;
            f2 = 0.2f;
        }
        else if (this.isSneaking()) {
            f = 0.6f;
            f2 = 1.65f;
        }
        else {
            f = 0.6f;
            f2 = 1.8f;
        }
        if (f != this.width || f2 != this.height) {
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            axisalignedbb = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + f, axisalignedbb.minY + f2, axisalignedbb.minZ + f);
            if (!this.world.collidesWithAnyBlock(axisalignedbb)) {
                this.setSize(f, f2);
            }
        }
    }
    
    @Override
    public int getMaxInPortalTime() {
        return this.capabilities.disableDamage ? 1 : 80;
    }
    
    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_PLAYER_SWIM;
    }
    
    @Override
    protected SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_PLAYER_SPLASH;
    }
    
    @Override
    public int getPortalCooldown() {
        return 10;
    }
    
    @Override
    public void playSound(final SoundEvent soundIn, final float volume, final float pitch) {
        this.world.playSound(this, this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch);
    }
    
    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.PLAYERS;
    }
    
    @Override
    protected int getFireImmuneTicks() {
        return 20;
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 9) {
            this.onItemUseFinish();
        }
        else if (id == 23) {
            this.hasReducedDebug = false;
        }
        else if (id == 22) {
            this.hasReducedDebug = true;
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    @Override
    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0f || this.isPlayerSleeping();
    }
    
    protected void closeScreen() {
        this.openContainer = this.inventoryContainer;
    }
    
    @Override
    public void updateRidden() {
        if (!this.world.isRemote && this.isSneaking() && this.isRiding()) {
            this.dismountRidingEntity();
            this.setSneaking(false);
        }
        else {
            final double d0 = this.posX;
            final double d2 = this.posY;
            final double d3 = this.posZ;
            final float f = this.rotationYaw;
            final float f2 = this.rotationPitch;
            super.updateRidden();
            this.prevCameraYaw = this.cameraYaw;
            this.cameraYaw = 0.0f;
            this.addMountedMovementStat(this.posX - d0, this.posY - d2, this.posZ - d3);
            if (this.getRidingEntity() instanceof EntityPig) {
                this.rotationPitch = f2;
                this.rotationYaw = f;
                this.renderYawOffset = ((EntityPig)this.getRidingEntity()).renderYawOffset;
            }
        }
    }
    
    public void preparePlayerToSpawn() {
        this.setSize(0.6f, 1.8f);
        super.preparePlayerToSpawn();
        this.setHealth(this.getMaxHealth());
        this.deathTime = 0;
    }
    
    @Override
    protected void updateEntityActionState() {
        super.updateEntityActionState();
        this.updateArmSwingProgress();
        this.rotationYawHead = this.rotationYaw;
        this.rotationPitchHead = this.rotationPitch;
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.flyToggleTimer > 0) {
            --this.flyToggleTimer;
        }
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL && this.world.getGameRules().getBoolean("naturalRegeneration")) {
            if (this.getHealth() < this.getMaxHealth() && this.ticksExisted % 20 == 0) {
                this.heal(1.0f);
            }
            if (this.foodStats.needFood() && this.ticksExisted % 10 == 0) {
                this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
            }
        }
        this.inventory.decrementAnimations();
        this.prevCameraYaw = this.cameraYaw;
        super.onLivingUpdate();
        final IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (!this.world.isRemote) {
            iattributeinstance.setBaseValue(this.capabilities.getWalkSpeed());
        }
        this.jumpMovementFactor = this.speedInAir;
        if (this.isSprinting()) {
            this.jumpMovementFactor += (float)(this.speedInAir * 0.3);
        }
        this.setAIMoveSpeed((float)iattributeinstance.getAttributeValue());
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float f2 = (float)(Math.atan(-this.motionY * 0.20000000298023224) * 15.0);
        if (f > 0.1f) {
            f = 0.1f;
        }
        if (!this.onGround || this.getHealth() <= 0.0f) {
            f = 0.0f;
        }
        if (this.onGround || this.getHealth() <= 0.0f) {
            f2 = 0.0f;
        }
        this.cameraYaw += (f - this.cameraYaw) * 0.4f;
        this.cameraPitch += (f2 - this.cameraPitch) * 0.8f;
        if (this.getHealth() > 0.0f && !this.isSpectator()) {
            AxisAlignedBB axisalignedbb;
            if (this.isRiding() && !this.getRidingEntity().isDead) {
                axisalignedbb = this.getEntityBoundingBox().union(this.getRidingEntity().getEntityBoundingBox()).grow(1.0, 0.0, 1.0);
            }
            else {
                axisalignedbb = this.getEntityBoundingBox().grow(1.0, 0.5, 1.0);
            }
            final List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb);
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity = list.get(i);
                if (!entity.isDead) {
                    this.collideWithPlayer(entity);
                }
            }
        }
        this.playShoulderEntityAmbientSound(this.getLeftShoulderEntity());
        this.playShoulderEntityAmbientSound(this.getRightShoulderEntity());
        if ((!this.world.isRemote && (this.fallDistance > 0.5f || this.isInWater() || this.isRiding())) || this.capabilities.isFlying) {
            this.spawnShoulderEntities();
        }
    }
    
    private void playShoulderEntityAmbientSound(@Nullable final NBTTagCompound p_192028_1_) {
        if ((p_192028_1_ != null && !p_192028_1_.hasKey("Silent")) || !p_192028_1_.getBoolean("Silent")) {
            final String s = p_192028_1_.getString("id");
            if (s.equals(EntityList.getKey(EntityParrot.class).toString())) {
                EntityParrot.playAmbientSound(this.world, this);
            }
        }
    }
    
    private void collideWithPlayer(final Entity entityIn) {
        entityIn.onCollideWithPlayer(this);
    }
    
    public int getScore() {
        return this.dataManager.get(EntityPlayer.PLAYER_SCORE);
    }
    
    public void setScore(final int scoreIn) {
        this.dataManager.set(EntityPlayer.PLAYER_SCORE, scoreIn);
    }
    
    public void addScore(final int scoreIn) {
        final int i = this.getScore();
        this.dataManager.set(EntityPlayer.PLAYER_SCORE, i + scoreIn);
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        super.onDeath(cause);
        this.setSize(0.2f, 0.2f);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionY = 0.10000000149011612;
        if ("Notch".equals(this.getName())) {
            this.dropItem(new ItemStack(Items.APPLE, 1), true, false);
        }
        if (!this.world.getGameRules().getBoolean("keepInventory") && !this.isSpectator()) {
            this.destroyVanishingCursedItems();
            this.inventory.dropAllItems();
        }
        if (cause != null) {
            this.motionX = -MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * 0.017453292f) * 0.1f;
            this.motionZ = -MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * 0.017453292f) * 0.1f;
        }
        else {
            this.motionX = 0.0;
            this.motionZ = 0.0;
        }
        this.addStat(StatList.DEATHS);
        this.takeStat(StatList.TIME_SINCE_DEATH);
        this.extinguish();
        this.setFlag(0, false);
    }
    
    protected void destroyVanishingCursedItems() {
        for (int i = 0; i < this.inventory.getSizeInventory(); ++i) {
            final ItemStack itemstack = this.inventory.getStackInSlot(i);
            if (!itemstack.isEmpty() && EnchantmentHelper.hasVanishingCurse(itemstack)) {
                this.inventory.removeStackFromSlot(i);
            }
        }
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource damageSourceIn) {
        if (damageSourceIn == DamageSource.ON_FIRE) {
            return SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE;
        }
        return (damageSourceIn == DamageSource.DROWN) ? SoundEvents.ENTITY_PLAYER_HURT_DROWN : SoundEvents.ENTITY_PLAYER_HURT;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PLAYER_DEATH;
    }
    
    @Nullable
    public EntityItem dropItem(final boolean dropAll) {
        return this.dropItem(this.inventory.decrStackSize(this.inventory.currentItem, (dropAll && !this.inventory.getCurrentItem().isEmpty()) ? this.inventory.getCurrentItem().getCount() : 1), false, true);
    }
    
    public ItemStack getEquipmentInSlot(final int item) {
        return (item == 0) ? this.inventory.getCurrentItem() : this.inventory.armorInventory.get(item - 1);
    }
    
    @Nullable
    public EntityItem dropItem(final ItemStack itemStackIn, final boolean unused) {
        return this.dropItem(itemStackIn, false, unused);
    }
    
    @Nullable
    public EntityItem dropItem(final ItemStack droppedItem, final boolean dropAround, final boolean traceItem) {
        if (droppedItem.isEmpty()) {
            return null;
        }
        final double d0 = this.posY - 0.30000001192092896 + this.getEyeHeight();
        final EntityItem entityitem = new EntityItem(this.world, this.posX, d0, this.posZ, droppedItem);
        entityitem.setPickupDelay(40);
        if (traceItem) {
            entityitem.setThrower(this.getName());
        }
        if (dropAround) {
            final float f = this.rand.nextFloat() * 0.5f;
            final float f2 = this.rand.nextFloat() * 6.2831855f;
            entityitem.motionX = -MathHelper.sin(f2) * f;
            entityitem.motionZ = MathHelper.cos(f2) * f;
            entityitem.motionY = 0.20000000298023224;
        }
        else {
            float f3 = 0.3f;
            entityitem.motionX = -MathHelper.sin(this.rotationYaw * 0.017453292f) * MathHelper.cos(this.rotationPitch * 0.017453292f) * f3;
            entityitem.motionZ = MathHelper.cos(this.rotationYaw * 0.017453292f) * MathHelper.cos(this.rotationPitch * 0.017453292f) * f3;
            entityitem.motionY = -MathHelper.sin(this.rotationPitch * 0.017453292f) * f3 + 0.1f;
            final float f4 = this.rand.nextFloat() * 6.2831855f;
            f3 = 0.02f * this.rand.nextFloat();
            final EntityItem entityItem = entityitem;
            entityItem.motionX += Math.cos(f4) * f3;
            final EntityItem entityItem2 = entityitem;
            entityItem2.motionY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f;
            final EntityItem entityItem3 = entityitem;
            entityItem3.motionZ += Math.sin(f4) * f3;
        }
        final ItemStack itemstack = this.dropItemAndGetStack(entityitem);
        if (traceItem) {
            if (!itemstack.isEmpty()) {
                this.addStat(StatList.getDroppedObjectStats(itemstack.getItem()), droppedItem.getCount());
            }
            this.addStat(StatList.DROP);
        }
        return entityitem;
    }
    
    protected ItemStack dropItemAndGetStack(final EntityItem p_184816_1_) {
        this.world.spawnEntity(p_184816_1_);
        return p_184816_1_.getItem();
    }
    
    public float getDigSpeed(final IBlockState state) {
        float f = this.inventory.getDestroySpeed(state);
        if (f > 1.0f) {
            final int i = EnchantmentHelper.getEfficiencyModifier(this);
            final ItemStack itemstack = this.getHeldItemMainhand();
            if (i > 0 && !itemstack.isEmpty()) {
                f += i * i + 1;
            }
        }
        if (this.isPotionActive(MobEffects.HASTE)) {
            f *= 1.0f + (this.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2f;
        }
        if (this.isPotionActive(MobEffects.MINING_FATIGUE)) {
            float f2 = 0.0f;
            switch (this.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
                case 0: {
                    f2 = 0.3f;
                    break;
                }
                case 1: {
                    f2 = 0.09f;
                    break;
                }
                case 2: {
                    f2 = 0.0027f;
                    break;
                }
                default: {
                    f2 = 8.1E-4f;
                    break;
                }
            }
            f *= f2;
        }
        if (this.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
            f /= 5.0f;
        }
        if (!this.onGround) {
            f /= 5.0f;
        }
        return f;
    }
    
    public boolean canHarvestBlock(final IBlockState state) {
        return this.inventory.canHarvestBlock(state);
    }
    
    public static void registerFixesPlayer(final DataFixer fixer) {
        fixer.registerWalker(FixTypes.PLAYER, new IDataWalker() {
            @Override
            public NBTTagCompound process(final IDataFixer fixer, final NBTTagCompound compound, final int versionIn) {
                DataFixesManager.processInventory(fixer, compound, versionIn, "Inventory");
                DataFixesManager.processInventory(fixer, compound, versionIn, "EnderItems");
                if (compound.hasKey("ShoulderEntityLeft", 10)) {
                    compound.setTag("ShoulderEntityLeft", fixer.process(FixTypes.ENTITY, compound.getCompoundTag("ShoulderEntityLeft"), versionIn));
                }
                if (compound.hasKey("ShoulderEntityRight", 10)) {
                    compound.setTag("ShoulderEntityRight", fixer.process(FixTypes.ENTITY, compound.getCompoundTag("ShoulderEntityRight"), versionIn));
                }
                return compound;
            }
        });
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setUniqueId(getUUID(this.gameProfile));
        final NBTTagList nbttaglist = compound.getTagList("Inventory", 10);
        this.inventory.readFromNBT(nbttaglist);
        this.inventory.currentItem = compound.getInteger("SelectedItemSlot");
        this.sleeping = compound.getBoolean("Sleeping");
        this.sleepTimer = compound.getShort("SleepTimer");
        this.experience = compound.getFloat("XpP");
        this.experienceLevel = compound.getInteger("XpLevel");
        this.experienceTotal = compound.getInteger("XpTotal");
        this.xpSeed = compound.getInteger("XpSeed");
        if (this.xpSeed == 0) {
            this.xpSeed = this.rand.nextInt();
        }
        this.setScore(compound.getInteger("Score"));
        if (this.sleeping) {
            this.bedLocation = new BlockPos(this);
            this.wakeUpPlayer(true, true, false);
        }
        if (compound.hasKey("SpawnX", 99) && compound.hasKey("SpawnY", 99) && compound.hasKey("SpawnZ", 99)) {
            this.spawnPos = new BlockPos(compound.getInteger("SpawnX"), compound.getInteger("SpawnY"), compound.getInteger("SpawnZ"));
            this.spawnForced = compound.getBoolean("SpawnForced");
        }
        this.foodStats.readNBT(compound);
        this.capabilities.readCapabilitiesFromNBT(compound);
        if (compound.hasKey("EnderItems", 9)) {
            final NBTTagList nbttaglist2 = compound.getTagList("EnderItems", 10);
            this.enderChest.loadInventoryFromNBT(nbttaglist2);
        }
        if (compound.hasKey("ShoulderEntityLeft", 10)) {
            this.setLeftShoulderEntity(compound.getCompoundTag("ShoulderEntityLeft"));
        }
        if (compound.hasKey("ShoulderEntityRight", 10)) {
            this.setRightShoulderEntity(compound.getCompoundTag("ShoulderEntityRight"));
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("DataVersion", 1343);
        compound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
        compound.setInteger("SelectedItemSlot", this.inventory.currentItem);
        compound.setBoolean("Sleeping", this.sleeping);
        compound.setShort("SleepTimer", (short)this.sleepTimer);
        compound.setFloat("XpP", this.experience);
        compound.setInteger("XpLevel", this.experienceLevel);
        compound.setInteger("XpTotal", this.experienceTotal);
        compound.setInteger("XpSeed", this.xpSeed);
        compound.setInteger("Score", this.getScore());
        if (this.spawnPos != null) {
            compound.setInteger("SpawnX", this.spawnPos.getX());
            compound.setInteger("SpawnY", this.spawnPos.getY());
            compound.setInteger("SpawnZ", this.spawnPos.getZ());
            compound.setBoolean("SpawnForced", this.spawnForced);
        }
        this.foodStats.writeNBT(compound);
        this.capabilities.writeCapabilitiesToNBT(compound);
        compound.setTag("EnderItems", this.enderChest.saveInventoryToNBT());
        if (!this.getLeftShoulderEntity().isEmpty()) {
            compound.setTag("ShoulderEntityLeft", this.getLeftShoulderEntity());
        }
        if (!this.getRightShoulderEntity().isEmpty()) {
            compound.setTag("ShoulderEntityRight", this.getRightShoulderEntity());
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (this.capabilities.disableDamage && !source.canHarmInCreative()) {
            return false;
        }
        this.idleTime = 0;
        if (this.getHealth() <= 0.0f) {
            return false;
        }
        if (this.isPlayerSleeping() && !this.world.isRemote) {
            this.wakeUpPlayer(true, true, false);
        }
        this.spawnShoulderEntities();
        if (source.isDifficultyScaled()) {
            if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
                amount = 0.0f;
            }
            if (this.world.getDifficulty() == EnumDifficulty.EASY) {
                amount = Math.min(amount / 2.0f + 1.0f, amount);
            }
            if (this.world.getDifficulty() == EnumDifficulty.HARD) {
                amount = amount * 3.0f / 2.0f;
            }
        }
        return amount != 0.0f && super.attackEntityFrom(source, amount);
    }
    
    @Override
    protected void blockUsingShield(final EntityLivingBase p_190629_1_) {
        super.blockUsingShield(p_190629_1_);
        if (p_190629_1_.getHeldItemMainhand().getItem() instanceof ItemAxe) {
            this.disableShield(true);
        }
    }
    
    public boolean canAttackPlayer(final EntityPlayer other) {
        final Team team = this.getTeam();
        final Team team2 = other.getTeam();
        return team == null || !team.isSameTeam(team2) || team.getAllowFriendlyFire();
    }
    
    @Override
    protected void damageArmor(final float damage) {
        this.inventory.damageArmor(damage);
    }
    
    @Override
    protected void damageShield(final float damage) {
        if (damage >= 3.0f && this.activeItemStack.getItem() == Items.SHIELD) {
            final int i = 1 + MathHelper.floor(damage);
            this.activeItemStack.damageItem(i, this);
            if (this.activeItemStack.isEmpty()) {
                final EnumHand enumhand = this.getActiveHand();
                if (enumhand == EnumHand.MAIN_HAND) {
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                }
                else {
                    this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
                }
                this.activeItemStack = ItemStack.EMPTY;
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8f, 0.8f + this.world.rand.nextFloat() * 0.4f);
            }
        }
    }
    
    public float getArmorVisibility() {
        int i = 0;
        for (final ItemStack itemstack : this.inventory.armorInventory) {
            if (!itemstack.isEmpty()) {
                ++i;
            }
        }
        return i / (float)this.inventory.armorInventory.size();
    }
    
    @Override
    protected void damageEntity(final DamageSource damageSrc, float damageAmount) {
        if (!this.isEntityInvulnerable(damageSrc)) {
            damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
            final float f;
            damageAmount = (f = this.applyPotionDamageCalculations(damageSrc, damageAmount));
            damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0f);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - damageAmount));
            if (damageAmount != 0.0f) {
                this.addExhaustion(damageSrc.getHungerDamage());
                final float f2 = this.getHealth();
                this.setHealth(this.getHealth() - damageAmount);
                this.getCombatTracker().trackDamage(damageSrc, f2, damageAmount);
                if (damageAmount < 3.4028235E37f) {
                    this.addStat(StatList.DAMAGE_TAKEN, Math.round(damageAmount * 10.0f));
                }
            }
        }
    }
    
    public void openEditSign(final TileEntitySign signTile) {
    }
    
    public void displayGuiEditCommandCart(final CommandBlockBaseLogic commandBlock) {
    }
    
    public void displayGuiCommandBlock(final TileEntityCommandBlock commandBlock) {
    }
    
    public void openEditStructure(final TileEntityStructure structure) {
    }
    
    public void displayVillagerTradeGui(final IMerchant villager) {
    }
    
    public void displayGUIChest(final IInventory chestInventory) {
    }
    
    public void openGuiHorseInventory(final AbstractHorse horse, final IInventory inventoryIn) {
    }
    
    public void displayGui(final IInteractionObject guiOwner) {
    }
    
    public void openBook(final ItemStack stack, final EnumHand hand) {
    }
    
    public EnumActionResult interactOn(final Entity entityToInteractOn, final EnumHand hand) {
        if (this.isSpectator()) {
            if (entityToInteractOn instanceof IInventory) {
                this.displayGUIChest((IInventory)entityToInteractOn);
            }
            return EnumActionResult.PASS;
        }
        ItemStack itemstack = this.getHeldItem(hand);
        final ItemStack itemstack2 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy();
        if (entityToInteractOn.processInitialInteract(this, hand)) {
            if (this.capabilities.isCreativeMode && itemstack == this.getHeldItem(hand) && itemstack.getCount() < itemstack2.getCount()) {
                itemstack.setCount(itemstack2.getCount());
            }
            return EnumActionResult.SUCCESS;
        }
        if (!itemstack.isEmpty() && entityToInteractOn instanceof EntityLivingBase) {
            if (this.capabilities.isCreativeMode) {
                itemstack = itemstack2;
            }
            if (itemstack.interactWithEntity(this, (EntityLivingBase)entityToInteractOn, hand)) {
                if (itemstack.isEmpty() && !this.capabilities.isCreativeMode) {
                    this.setHeldItem(hand, ItemStack.EMPTY);
                }
                return EnumActionResult.SUCCESS;
            }
        }
        return EnumActionResult.PASS;
    }
    
    @Override
    public double getYOffset() {
        return -0.35;
    }
    
    @Override
    public void dismountRidingEntity() {
        super.dismountRidingEntity();
        this.rideCooldown = 0;
    }
    
    public void attackTargetEntityWithCurrentItem(final Entity targetEntity) {
        if (targetEntity.canBeAttackedWithItem() && !targetEntity.hitByEntity(this)) {
            float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
            float f2;
            if (targetEntity instanceof EntityLivingBase) {
                f2 = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
            }
            else {
                f2 = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), EnumCreatureAttribute.UNDEFINED);
            }
            final float f3 = this.getCooledAttackStrength(0.5f);
            f *= 0.2f + f3 * f3 * 0.8f;
            f2 *= f3;
            this.resetCooldown();
            if (f > 0.0f || f2 > 0.0f) {
                final boolean flag = f3 > 0.9f;
                boolean flag2 = false;
                int i = 0;
                i += EnchantmentHelper.getKnockbackModifier(this);
                if (this.isSprinting() && flag) {
                    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, this.getSoundCategory(), 1.0f, 1.0f);
                    ++i;
                    flag2 = true;
                }
                boolean flag3 = flag && this.fallDistance > 0.0f && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(MobEffects.BLINDNESS) && !this.isRiding() && targetEntity instanceof EntityLivingBase;
                flag3 = (flag3 && !this.isSprinting());
                if (flag3) {
                    f *= 1.5f;
                }
                f += f2;
                boolean flag4 = false;
                final double d0 = this.distanceWalkedModified - this.prevDistanceWalkedModified;
                if (flag && !flag3 && !flag2 && this.onGround && d0 < this.getAIMoveSpeed()) {
                    final ItemStack itemstack = this.getHeldItem(EnumHand.MAIN_HAND);
                    if (itemstack.getItem() instanceof ItemSword) {
                        flag4 = true;
                    }
                }
                float f4 = 0.0f;
                boolean flag5 = false;
                final int j = EnchantmentHelper.getFireAspectModifier(this);
                if (targetEntity instanceof EntityLivingBase) {
                    f4 = ((EntityLivingBase)targetEntity).getHealth();
                    if (j > 0 && !targetEntity.isBurning()) {
                        flag5 = true;
                        targetEntity.setFire(1);
                    }
                }
                final double d2 = targetEntity.motionX;
                final double d3 = targetEntity.motionY;
                final double d4 = targetEntity.motionZ;
                final boolean flag6 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(this), f);
                if (flag6) {
                    if (i > 0) {
                        if (targetEntity instanceof EntityLivingBase) {
                            ((EntityLivingBase)targetEntity).knockBack(this, i * 0.5f, MathHelper.sin(this.rotationYaw * 0.017453292f), -MathHelper.cos(this.rotationYaw * 0.017453292f));
                        }
                        else {
                            targetEntity.addVelocity(-MathHelper.sin(this.rotationYaw * 0.017453292f) * i * 0.5f, 0.1, MathHelper.cos(this.rotationYaw * 0.017453292f) * i * 0.5f);
                        }
                        if (Minced.getInstance().manager.getModule(KillAura.class).state) {
                            this.motionX *= 1.0;
                            this.motionZ *= 1.0;
                            this.setSprinting(true);
                        }
                        else {
                            this.motionX *= 0.6;
                            this.motionZ *= 0.6;
                            this.setSprinting(false);
                        }
                    }
                    if (flag4) {
                        final float f5 = 1.0f + EnchantmentHelper.getSweepingDamageRatio(this) * f;
                        for (final EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, targetEntity.getEntityBoundingBox().grow(1.0, 0.25, 1.0))) {
                            if (entitylivingbase != this && entitylivingbase != targetEntity && !this.isOnSameTeam(entitylivingbase) && this.getDistanceSq(entitylivingbase) < 9.0) {
                                entitylivingbase.knockBack(this, 0.4f, MathHelper.sin(this.rotationYaw * 0.017453292f), -MathHelper.cos(this.rotationYaw * 0.017453292f));
                                entitylivingbase.attackEntityFrom(DamageSource.causePlayerDamage(this), f5);
                            }
                        }
                        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0f, 1.0f);
                        this.spawnSweepParticles();
                    }
                    if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
                        ((EntityPlayerMP)targetEntity).connection.sendPacket(new SPacketEntityVelocity(targetEntity));
                        targetEntity.velocityChanged = false;
                        targetEntity.motionX = d2;
                        targetEntity.motionY = d3;
                        targetEntity.motionZ = d4;
                    }
                    if (flag3) {
                        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0f, 1.0f);
                        this.onCriticalHit(targetEntity);
                    }
                    if (!flag3 && !flag4) {
                        if (flag) {
                            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0f, 1.0f);
                        }
                        else {
                            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, this.getSoundCategory(), 1.0f, 1.0f);
                        }
                    }
                    if (f2 > 0.0f) {
                        this.onEnchantmentCritical(targetEntity);
                    }
                    this.setLastAttackedEntity(targetEntity);
                    if (targetEntity instanceof EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments((EntityLivingBase)targetEntity, this);
                    }
                    EnchantmentHelper.applyArthropodEnchantments(this, targetEntity);
                    final ItemStack itemstack2 = this.getHeldItemMainhand();
                    Entity entity = targetEntity;
                    if (targetEntity instanceof MultiPartEntityPart) {
                        final IEntityMultiPart ientitymultipart = ((MultiPartEntityPart)targetEntity).parent;
                        if (ientitymultipart instanceof EntityLivingBase) {
                            entity = (EntityLivingBase)ientitymultipart;
                        }
                    }
                    if (!itemstack2.isEmpty() && entity instanceof EntityLivingBase) {
                        itemstack2.hitEntity((EntityLivingBase)entity, this);
                        if (itemstack2.isEmpty()) {
                            this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
                        }
                    }
                    if (targetEntity instanceof EntityLivingBase) {
                        final float f6 = f4 - ((EntityLivingBase)targetEntity).getHealth();
                        this.addStat(StatList.DAMAGE_DEALT, Math.round(f6 * 10.0f));
                        if (j > 0) {
                            targetEntity.setFire(j * 4);
                        }
                        if (this.world instanceof WorldServer && f6 > 2.0f) {
                            final int k = (int)(f6 * 0.5);
                            ((WorldServer)this.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, targetEntity.posX, targetEntity.posY + targetEntity.height * 0.5f, targetEntity.posZ, k, 0.1, 0.0, 0.1, 0.2, new int[0]);
                        }
                    }
                    this.addExhaustion(0.1f);
                }
                else {
                    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0f, 1.0f);
                    if (flag5) {
                        targetEntity.extinguish();
                    }
                }
            }
        }
    }
    
    public void disableShield(final boolean p_190777_1_) {
        float f = 0.25f + EnchantmentHelper.getEfficiencyModifier(this) * 0.05f;
        if (p_190777_1_) {
            f += 0.75f;
        }
        if (this.rand.nextFloat() < f) {
            this.getCooldownTracker().setCooldown(Items.SHIELD, 100);
            this.resetActiveHand();
            this.world.setEntityState(this, (byte)30);
        }
    }
    
    public void onCriticalHit(final Entity entityHit) {
    }
    
    public void onEnchantmentCritical(final Entity entityHit) {
    }
    
    public void spawnSweepParticles() {
        final double d0 = -MathHelper.sin(this.rotationYaw * 0.017453292f);
        final double d2 = MathHelper.cos(this.rotationYaw * 0.017453292f);
        if (this.world instanceof WorldServer) {
            ((WorldServer)this.world).spawnParticle(EnumParticleTypes.SWEEP_ATTACK, this.posX + d0, this.posY + this.height * 0.5, this.posZ + d2, 0, d0, 0.0, d2, 0.0, new int[0]);
        }
    }
    
    public void respawnPlayer() {
    }
    
    @Override
    public void setDead() {
        super.setDead();
        this.inventoryContainer.onContainerClosed(this);
        if (this.openContainer != null) {
            this.openContainer.onContainerClosed(this);
        }
    }
    
    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return !this.sleeping && super.isEntityInsideOpaqueBlock();
    }
    
    public boolean isUser() {
        return false;
    }
    
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }
    
    public SleepResult trySleep(final BlockPos bedLocation) {
        final EnumFacing enumfacing = this.world.getBlockState(bedLocation).getValue((IProperty<EnumFacing>)BlockHorizontal.FACING);
        if (!this.world.isRemote) {
            if (this.isPlayerSleeping() || !this.isEntityAlive()) {
                return SleepResult.OTHER_PROBLEM;
            }
            if (!this.world.provider.isSurfaceWorld()) {
                return SleepResult.NOT_POSSIBLE_HERE;
            }
            if (this.world.isDaytime()) {
                return SleepResult.NOT_POSSIBLE_NOW;
            }
            if (!this.bedInRange(bedLocation, enumfacing)) {
                return SleepResult.TOO_FAR_AWAY;
            }
            final double d0 = 8.0;
            final double d2 = 5.0;
            final List<EntityMob> list = this.world.getEntitiesWithinAABB((Class<? extends EntityMob>)EntityMob.class, new AxisAlignedBB(bedLocation.getX() - 8.0, bedLocation.getY() - 5.0, bedLocation.getZ() - 8.0, bedLocation.getX() + 8.0, bedLocation.getY() + 5.0, bedLocation.getZ() + 8.0), (com.google.common.base.Predicate<? super EntityMob>)new SleepEnemyPredicate(this));
            if (!list.isEmpty()) {
                return SleepResult.NOT_SAFE;
            }
        }
        if (this.isRiding()) {
            this.dismountRidingEntity();
        }
        this.spawnShoulderEntities();
        this.setSize(0.2f, 0.2f);
        if (this.world.isBlockLoaded(bedLocation)) {
            final float f1 = 0.5f + enumfacing.getXOffset() * 0.4f;
            final float f2 = 0.5f + enumfacing.getZOffset() * 0.4f;
            this.setRenderOffsetForSleep(enumfacing);
            this.setPosition(bedLocation.getX() + f1, bedLocation.getY() + 0.6875f, bedLocation.getZ() + f2);
        }
        else {
            this.setPosition(bedLocation.getX() + 0.5f, bedLocation.getY() + 0.6875f, bedLocation.getZ() + 0.5f);
        }
        this.sleeping = true;
        this.sleepTimer = 0;
        this.bedLocation = bedLocation;
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        if (!this.world.isRemote) {
            this.world.updateAllPlayersSleepingFlag();
        }
        return SleepResult.OK;
    }
    
    private boolean bedInRange(final BlockPos p_190774_1_, final EnumFacing p_190774_2_) {
        if (Math.abs(this.posX - p_190774_1_.getX()) <= 3.0 && Math.abs(this.posY - p_190774_1_.getY()) <= 2.0 && Math.abs(this.posZ - p_190774_1_.getZ()) <= 3.0) {
            return true;
        }
        final BlockPos blockpos = p_190774_1_.offset(p_190774_2_.getOpposite());
        return Math.abs(this.posX - blockpos.getX()) <= 3.0 && Math.abs(this.posY - blockpos.getY()) <= 2.0 && Math.abs(this.posZ - blockpos.getZ()) <= 3.0;
    }
    
    private void setRenderOffsetForSleep(final EnumFacing bedDirection) {
        this.renderOffsetX = -1.8f * bedDirection.getXOffset();
        this.renderOffsetZ = -1.8f * bedDirection.getZOffset();
    }
    
    public void wakeUpPlayer(final boolean immediately, final boolean updateWorldFlag, final boolean setSpawn) {
        this.setSize(0.6f, 1.8f);
        final IBlockState iblockstate = this.world.getBlockState(this.bedLocation);
        if (this.bedLocation != null && iblockstate.getBlock() == Blocks.BED) {
            this.world.setBlockState(this.bedLocation, iblockstate.withProperty((IProperty<Comparable>)BlockBed.OCCUPIED, false), 4);
            BlockPos blockpos = BlockBed.getSafeExitLocation(this.world, this.bedLocation, 0);
            if (blockpos == null) {
                blockpos = this.bedLocation.up();
            }
            this.setPosition(blockpos.getX() + 0.5f, blockpos.getY() + 0.1f, blockpos.getZ() + 0.5f);
        }
        this.sleeping = false;
        if (!this.world.isRemote && updateWorldFlag) {
            this.world.updateAllPlayersSleepingFlag();
        }
        this.sleepTimer = (immediately ? 0 : 100);
        if (setSpawn) {
            this.setSpawnPoint(this.bedLocation, false);
        }
    }
    
    private boolean isInBed() {
        return this.world.getBlockState(this.bedLocation).getBlock() == Blocks.BED;
    }
    
    @Nullable
    public static BlockPos getBedSpawnLocation(final World worldIn, final BlockPos bedLocation, final boolean forceSpawn) {
        final Block block = worldIn.getBlockState(bedLocation).getBlock();
        if (block == Blocks.BED) {
            return BlockBed.getSafeExitLocation(worldIn, bedLocation, 0);
        }
        if (!forceSpawn) {
            return null;
        }
        final boolean flag = block.canSpawnInBlock();
        final boolean flag2 = worldIn.getBlockState(bedLocation.up()).getBlock().canSpawnInBlock();
        return (flag && flag2) ? bedLocation : null;
    }
    
    public float getBedOrientationInDegrees() {
        if (this.bedLocation != null) {
            final EnumFacing enumfacing = this.world.getBlockState(this.bedLocation).getValue((IProperty<EnumFacing>)BlockHorizontal.FACING);
            switch (enumfacing) {
                case SOUTH: {
                    return 90.0f;
                }
                case WEST: {
                    return 0.0f;
                }
                case NORTH: {
                    return 270.0f;
                }
                case EAST: {
                    return 180.0f;
                }
            }
        }
        return 0.0f;
    }
    
    @Override
    public boolean isPlayerSleeping() {
        return this.sleeping;
    }
    
    public boolean isPlayerFullyAsleep() {
        return this.sleeping && this.sleepTimer >= 100;
    }
    
    public int getSleepTimer() {
        return this.sleepTimer;
    }
    
    public void sendStatusMessage(final ITextComponent chatComponent, final boolean actionBar) {
    }
    
    public BlockPos getBedLocation() {
        return this.spawnPos;
    }
    
    public boolean isSpawnForced() {
        return this.spawnForced;
    }
    
    public void setSpawnPoint(final BlockPos pos, final boolean forced) {
        if (pos != null) {
            this.spawnPos = pos;
            this.spawnForced = forced;
        }
        else {
            this.spawnPos = null;
            this.spawnForced = false;
        }
    }
    
    public void addStat(final StatBase stat) {
        this.addStat(stat, 1);
    }
    
    public void addStat(final StatBase stat, final int amount) {
    }
    
    public void takeStat(final StatBase stat) {
    }
    
    public void unlockRecipes(final List<IRecipe> p_192021_1_) {
    }
    
    public void unlockRecipes(final ResourceLocation[] p_193102_1_) {
    }
    
    public void resetRecipes(final List<IRecipe> p_192022_1_) {
    }
    
    public void jump() {
        super.jump();
        this.addStat(StatList.JUMP);
        if (this.isSprinting()) {
            this.addExhaustion(0.2f);
        }
        else {
            this.addExhaustion(0.05f);
        }
    }
    
    @Override
    public void travel(final float strafe, final float vertical, final float forward) {
        final double d0 = this.posX;
        final double d2 = this.posY;
        final double d3 = this.posZ;
        if (this.capabilities.isFlying && !this.isRiding()) {
            final double d4 = this.motionY;
            final float f = this.jumpMovementFactor;
            this.jumpMovementFactor = this.capabilities.getFlySpeed() * (this.isSprinting() ? 2 : 1);
            super.travel(strafe, vertical, forward);
            this.motionY = d4 * 0.6;
            this.jumpMovementFactor = f;
            this.fallDistance = 0.0f;
            this.setFlag(7, false);
        }
        else {
            super.travel(strafe, vertical, forward);
        }
        this.addMovementStat(this.posX - d0, this.posY - d2, this.posZ - d3);
    }
    
    @Override
    public float getAIMoveSpeed() {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
    }
    
    public void addMovementStat(final double p_71000_1_, final double p_71000_3_, final double p_71000_5_) {
        if (!this.isRiding()) {
            if (this.isInsideOfMaterial(Material.WATER)) {
                final int i = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (i > 0) {
                    this.addStat(StatList.DIVE_ONE_CM, i);
                    this.addExhaustion(0.01f * i * 0.01f);
                }
            }
            else if (this.isInWater()) {
                final int j = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (j > 0) {
                    this.addStat(StatList.SWIM_ONE_CM, j);
                    this.addExhaustion(0.01f * j * 0.01f);
                }
            }
            else if (this.isOnLadder()) {
                if (p_71000_3_ > 0.0) {
                    this.addStat(StatList.CLIMB_ONE_CM, (int)Math.round(p_71000_3_ * 100.0));
                }
            }
            else if (this.onGround) {
                final int k = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (k > 0) {
                    if (this.isSprinting()) {
                        this.addStat(StatList.SPRINT_ONE_CM, k);
                        this.addExhaustion(0.1f * k * 0.01f);
                    }
                    else if (this.isSneaking()) {
                        this.addStat(StatList.CROUCH_ONE_CM, k);
                        this.addExhaustion(0.0f * k * 0.01f);
                    }
                    else {
                        this.addStat(StatList.WALK_ONE_CM, k);
                        this.addExhaustion(0.0f * k * 0.01f);
                    }
                }
            }
            else if (this.isElytraFlying()) {
                final int l = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0f);
                this.addStat(StatList.AVIATE_ONE_CM, l);
            }
            else {
                final int i2 = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (i2 > 25) {
                    this.addStat(StatList.FLY_ONE_CM, i2);
                }
            }
        }
    }
    
    private void addMountedMovementStat(final double p_71015_1_, final double p_71015_3_, final double p_71015_5_) {
        if (this.isRiding()) {
            final int i = Math.round(MathHelper.sqrt(p_71015_1_ * p_71015_1_ + p_71015_3_ * p_71015_3_ + p_71015_5_ * p_71015_5_) * 100.0f);
            if (i > 0) {
                if (this.getRidingEntity() instanceof EntityMinecart) {
                    this.addStat(StatList.MINECART_ONE_CM, i);
                }
                else if (this.getRidingEntity() instanceof EntityBoat) {
                    this.addStat(StatList.BOAT_ONE_CM, i);
                }
                else if (this.getRidingEntity() instanceof EntityPig) {
                    this.addStat(StatList.PIG_ONE_CM, i);
                }
                else if (this.getRidingEntity() instanceof AbstractHorse) {
                    this.addStat(StatList.HORSE_ONE_CM, i);
                }
            }
        }
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        if (!this.capabilities.allowFlying) {
            if (distance >= 2.0f) {
                this.addStat(StatList.FALL_ONE_CM, (int)Math.round(distance * 100.0));
            }
            super.fall(distance, damageMultiplier);
        }
    }
    
    @Override
    protected void doWaterSplashEffect() {
        if (!this.isSpectator()) {
            super.doWaterSplashEffect();
        }
    }
    
    @Override
    protected SoundEvent getFallSound(final int heightIn) {
        return (heightIn > 4) ? SoundEvents.ENTITY_PLAYER_BIG_FALL : SoundEvents.ENTITY_PLAYER_SMALL_FALL;
    }
    
    @Override
    public void onKillEntity(final EntityLivingBase entityLivingIn) {
        final EntityList.EntityEggInfo entitylist$entityegginfo = EntityList.ENTITY_EGGS.get(EntityList.getKey(entityLivingIn));
        if (entitylist$entityegginfo != null) {
            this.addStat(entitylist$entityegginfo.killEntityStat);
        }
    }
    
    @Override
    public void setInWeb() {
        if (!this.capabilities.isFlying) {
            super.setInWeb();
        }
    }
    
    public void addExperience(int amount) {
        this.addScore(amount);
        final int i = Integer.MAX_VALUE - this.experienceTotal;
        if (amount > i) {
            amount = i;
        }
        this.experience += amount / (float)this.xpBarCap();
        this.experienceTotal += amount;
        while (this.experience >= 1.0f) {
            this.experience = (this.experience - 1.0f) * this.xpBarCap();
            this.addExperienceLevel(1);
            this.experience /= this.xpBarCap();
        }
    }
    
    public int getXPSeed() {
        return this.xpSeed;
    }
    
    public void onEnchant(final ItemStack enchantedItem, final int cost) {
        this.experienceLevel -= cost;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0f;
            this.experienceTotal = 0;
        }
        this.xpSeed = this.rand.nextInt();
    }
    
    public void addExperienceLevel(final int levels) {
        this.experienceLevel += levels;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0f;
            this.experienceTotal = 0;
        }
        if (levels > 0 && this.experienceLevel % 5 == 0 && this.lastXPSound < this.ticksExisted - 100.0f) {
            final float f = (this.experienceLevel > 30) ? 1.0f : (this.experienceLevel / 30.0f);
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, this.getSoundCategory(), f * 0.75f, 1.0f);
            this.lastXPSound = this.ticksExisted;
        }
    }
    
    public int xpBarCap() {
        if (this.experienceLevel >= 30) {
            return 112 + (this.experienceLevel - 30) * 9;
        }
        return (this.experienceLevel >= 15) ? (37 + (this.experienceLevel - 15) * 5) : (7 + this.experienceLevel * 2);
    }
    
    public void addExhaustion(final float exhaustion) {
        if (!this.capabilities.disableDamage && !this.world.isRemote) {
            this.foodStats.addExhaustion(exhaustion);
        }
    }
    
    public FoodStats getFoodStats() {
        return this.foodStats;
    }
    
    public boolean canEat(final boolean ignoreHunger) {
        return (ignoreHunger || this.foodStats.needFood()) && !this.capabilities.disableDamage;
    }
    
    public boolean shouldHeal() {
        return this.getHealth() > 0.0f && this.getHealth() < this.getMaxHealth();
    }
    
    public boolean isAllowEdit() {
        return this.capabilities.allowEdit;
    }
    
    public boolean canPlayerEdit(final BlockPos pos, final EnumFacing facing, final ItemStack stack) {
        if (this.capabilities.allowEdit) {
            return true;
        }
        if (stack.isEmpty()) {
            return false;
        }
        final BlockPos blockpos = pos.offset(facing.getOpposite());
        final Block block = this.world.getBlockState(blockpos).getBlock();
        return stack.canPlaceOn(block) || stack.canEditBlocks();
    }
    
    @Override
    protected int getExperiencePoints(final EntityPlayer player) {
        if (!this.world.getGameRules().getBoolean("keepInventory") && !this.isSpectator()) {
            final int i = this.experienceLevel * 7;
            return (i > 100) ? 100 : i;
        }
        return 0;
    }
    
    @Override
    protected boolean isPlayer() {
        return true;
    }
    
    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return true;
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return !this.capabilities.isFlying;
    }
    
    public void sendPlayerAbilities() {
    }
    
    public void setGameType(final GameType gameType) {
    }
    
    @Override
    public String getName() {
        return this.gameProfile.getName();
    }
    
    public InventoryEnderChest getInventoryEnderChest() {
        return this.enderChest;
    }
    
    @Override
    public ItemStack getItemStackFromSlot(final EntityEquipmentSlot slotIn) {
        if (slotIn == EntityEquipmentSlot.MAINHAND) {
            return this.inventory.getCurrentItem();
        }
        if (slotIn == EntityEquipmentSlot.OFFHAND) {
            return this.inventory.offHandInventory.get(0);
        }
        return (slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR) ? this.inventory.armorInventory.get(slotIn.getIndex()) : ItemStack.EMPTY;
    }
    
    @Override
    public void setItemStackToSlot(final EntityEquipmentSlot slotIn, final ItemStack stack) {
        if (slotIn == EntityEquipmentSlot.MAINHAND) {
            this.playEquipSound(stack);
            this.inventory.mainInventory.set(this.inventory.currentItem, stack);
        }
        else if (slotIn == EntityEquipmentSlot.OFFHAND) {
            this.playEquipSound(stack);
            this.inventory.offHandInventory.set(0, stack);
        }
        else if (slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
            this.playEquipSound(stack);
            this.inventory.armorInventory.set(slotIn.getIndex(), stack);
        }
    }
    
    public boolean addItemStackToInventory(final ItemStack p_191521_1_) {
        this.playEquipSound(p_191521_1_);
        return this.inventory.addItemStackToInventory(p_191521_1_);
    }
    
    @Override
    public Iterable<ItemStack> getHeldEquipment() {
        return (Iterable<ItemStack>)Lists.newArrayList((Object[])new ItemStack[] { this.getHeldItemMainhand(), this.getHeldItemOffhand() });
    }
    
    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return this.inventory.armorInventory;
    }
    
    public boolean addShoulderEntity(final NBTTagCompound p_192027_1_) {
        if (this.isRiding() || !this.onGround || this.isInWater()) {
            return false;
        }
        if (this.getLeftShoulderEntity().isEmpty()) {
            this.setLeftShoulderEntity(p_192027_1_);
            return true;
        }
        if (this.getRightShoulderEntity().isEmpty()) {
            this.setRightShoulderEntity(p_192027_1_);
            return true;
        }
        return false;
    }
    
    protected void spawnShoulderEntities() {
        this.spawnShoulderEntity(this.getLeftShoulderEntity());
        this.setLeftShoulderEntity(new NBTTagCompound());
        this.spawnShoulderEntity(this.getRightShoulderEntity());
        this.setRightShoulderEntity(new NBTTagCompound());
    }
    
    private void spawnShoulderEntity(@Nullable final NBTTagCompound p_192026_1_) {
        if (!this.world.isRemote && !p_192026_1_.isEmpty()) {
            final Entity entity = EntityList.createEntityFromNBT(p_192026_1_, this.world);
            if (entity instanceof EntityTameable) {
                ((EntityTameable)entity).setOwnerId(this.entityUniqueID);
            }
            entity.setPosition(this.posX, this.posY + 0.699999988079071, this.posZ);
            this.world.spawnEntity(entity);
        }
    }
    
    @Override
    public boolean isInvisibleToPlayer(final EntityPlayer player) {
        if (!this.isInvisible()) {
            return false;
        }
        if (player.isSpectator()) {
            return false;
        }
        final Team team = this.getTeam();
        return team == null || player == null || player.getTeam() != team || !team.getSeeFriendlyInvisiblesEnabled();
    }
    
    public abstract boolean isSpectator();
    
    public abstract boolean isCreative();
    
    @Override
    public boolean isPushedByWater() {
        return !Minced.getInstance().manager.getModule(NoPush.class).state && !this.capabilities.isFlying;
    }
    
    public Scoreboard getWorldScoreboard() {
        return this.world.getScoreboard();
    }
    
    @Override
    public Team getTeam() {
        return this.getWorldScoreboard().getPlayersTeam(this.getName());
    }
    
    @Override
    public ITextComponent getDisplayName() {
        final ITextComponent itextcomponent = new TextComponentString(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));
        itextcomponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + this.getName() + " "));
        itextcomponent.getStyle().setHoverEvent(this.getHoverEvent());
        itextcomponent.getStyle().setInsertion(this.getName());
        return itextcomponent;
    }
    
    @Override
    public float getEyeHeight() {
        float f = 1.62f;
        if (this.isPlayerSleeping()) {
            f = 0.2f;
        }
        else if (!this.isSneaking() && this.height != 1.65f) {
            if (this.isElytraFlying() || this.height == 0.6f) {
                if (Minced.getInstance().manager.getModule(Flight.class).state) {
                    f = 1.62f;
                }
                else if (Minced.getInstance().manager.getModule(Speed.class).state) {
                    f = 1.62f;
                }
                else if (Minced.getInstance().manager.getModule(Strafe.class).state) {
                    f = 1.62f;
                }
                else {
                    f = 0.4f;
                }
            }
        }
        else {
            f -= 0.08f;
        }
        return this.fLerp = AnimationMath.fast(this.fLerp, f, 1.0f);
    }
    
    @Override
    public void setAbsorptionAmount(float amount) {
        if (amount < 0.0f) {
            amount = 0.0f;
        }
        this.getDataManager().set(EntityPlayer.ABSORPTION, amount);
    }
    
    @Override
    public float getAbsorptionAmount() {
        return this.getDataManager().get(EntityPlayer.ABSORPTION);
    }
    
    public static UUID getUUID(final GameProfile profile) {
        UUID uuid = profile.getId();
        if (uuid == null) {
            uuid = getOfflineUUID(profile.getName());
        }
        return uuid;
    }
    
    public static UUID getOfflineUUID(final String username) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
    }
    
    public boolean canOpen(final LockCode code) {
        if (code.isEmpty()) {
            return true;
        }
        final ItemStack itemstack = this.getHeldItemMainhand();
        return !itemstack.isEmpty() && itemstack.hasDisplayName() && itemstack.getDisplayName().equals(code.getLock());
    }
    
    public boolean isWearing(final EnumPlayerModelParts part) {
        return (this.getDataManager().get(EntityPlayer.PLAYER_MODEL_FLAG) & part.getPartMask()) == part.getPartMask();
    }
    
    @Override
    public boolean sendCommandFeedback() {
        return this.getServer().worlds[0].getGameRules().getBoolean("sendCommandFeedback");
    }
    
    @Override
    public boolean replaceItemInInventory(final int inventorySlot, final ItemStack itemStackIn) {
        if (inventorySlot >= 0 && inventorySlot < this.inventory.mainInventory.size()) {
            this.inventory.setInventorySlotContents(inventorySlot, itemStackIn);
            return true;
        }
        EntityEquipmentSlot entityequipmentslot;
        if (inventorySlot == 100 + EntityEquipmentSlot.HEAD.getIndex()) {
            entityequipmentslot = EntityEquipmentSlot.HEAD;
        }
        else if (inventorySlot == 100 + EntityEquipmentSlot.CHEST.getIndex()) {
            entityequipmentslot = EntityEquipmentSlot.CHEST;
        }
        else if (inventorySlot == 100 + EntityEquipmentSlot.LEGS.getIndex()) {
            entityequipmentslot = EntityEquipmentSlot.LEGS;
        }
        else if (inventorySlot == 100 + EntityEquipmentSlot.FEET.getIndex()) {
            entityequipmentslot = EntityEquipmentSlot.FEET;
        }
        else {
            entityequipmentslot = null;
        }
        if (inventorySlot == 98) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemStackIn);
            return true;
        }
        if (inventorySlot == 99) {
            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, itemStackIn);
            return true;
        }
        if (entityequipmentslot != null) {
            if (!itemStackIn.isEmpty()) {
                if (!(itemStackIn.getItem() instanceof ItemArmor) && !(itemStackIn.getItem() instanceof ItemElytra)) {
                    if (entityequipmentslot != EntityEquipmentSlot.HEAD) {
                        return false;
                    }
                }
                else if (EntityLiving.getSlotForItemStack(itemStackIn) != entityequipmentslot) {
                    return false;
                }
            }
            this.inventory.setInventorySlotContents(entityequipmentslot.getIndex() + this.inventory.mainInventory.size(), itemStackIn);
            return true;
        }
        final int i = inventorySlot - 200;
        if (i >= 0 && i < this.enderChest.getSizeInventory()) {
            this.enderChest.setInventorySlotContents(i, itemStackIn);
            return true;
        }
        return false;
    }
    
    public boolean hasReducedDebug() {
        return this.hasReducedDebug;
    }
    
    public void setReducedDebug(final boolean reducedDebug) {
        this.hasReducedDebug = reducedDebug;
    }
    
    @Override
    public EnumHandSide getPrimaryHand() {
        return (this.dataManager.get(EntityPlayer.MAIN_HAND) == 0) ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
    }
    
    public void setPrimaryHand(final EnumHandSide hand) {
        this.dataManager.set(EntityPlayer.MAIN_HAND, (byte)((hand != EnumHandSide.LEFT) ? 1 : 0));
    }
    
    public NBTTagCompound getLeftShoulderEntity() {
        return this.dataManager.get(EntityPlayer.LEFT_SHOULDER_ENTITY);
    }
    
    protected void setLeftShoulderEntity(final NBTTagCompound tag) {
        this.dataManager.set(EntityPlayer.LEFT_SHOULDER_ENTITY, tag);
    }
    
    public NBTTagCompound getRightShoulderEntity() {
        return this.dataManager.get(EntityPlayer.RIGHT_SHOULDER_ENTITY);
    }
    
    protected void setRightShoulderEntity(final NBTTagCompound tag) {
        this.dataManager.set(EntityPlayer.RIGHT_SHOULDER_ENTITY, tag);
    }
    
    public float getCooldownPeriod() {
        return (float)(1.0 / this.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue() * 20.0);
    }
    
    public float getCooledAttackStrength(final float adjustTicks) {
        return MathHelper.clamp((this.ticksSinceLastSwing + adjustTicks) / this.getCooldownPeriod(), 0.0f, 1.0f);
    }
    
    public void resetCooldown() {
        this.ticksSinceLastSwing = 0;
    }
    
    public CooldownTracker getCooldownTracker() {
        return this.cooldownTracker;
    }
    
    @Override
    public void applyEntityCollision(final Entity entityIn) {
        if (!this.isPlayerSleeping()) {
            super.applyEntityCollision(entityIn);
        }
    }
    
    public float getLuck() {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.LUCK).getAttributeValue();
    }
    
    public boolean canUseCommandBlock() {
        return this.capabilities.isCreativeMode && this.canUseCommand(2, "");
    }
    
    static {
        ABSORPTION = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.FLOAT);
        PLAYER_SCORE = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
        PLAYER_MODEL_FLAG = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BYTE);
        MAIN_HAND = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BYTE);
        LEFT_SHOULDER_ENTITY = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.COMPOUND_TAG);
        RIGHT_SHOULDER_ENTITY = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.COMPOUND_TAG);
    }
    
    public enum EnumChatVisibility
    {
        FULL(0, "options.chat.visibility.full"), 
        SYSTEM(1, "options.chat.visibility.system"), 
        HIDDEN(2, "options.chat.visibility.hidden");
        
        private static final EnumChatVisibility[] ID_LOOKUP;
        private final int chatVisibility;
        private final String resourceKey;
        
        private EnumChatVisibility(final int id, final String resourceKey) {
            this.chatVisibility = id;
            this.resourceKey = resourceKey;
        }
        
        public int getChatVisibility() {
            return this.chatVisibility;
        }
        
        public static EnumChatVisibility getEnumChatVisibility(final int id) {
            return EnumChatVisibility.ID_LOOKUP[id % EnumChatVisibility.ID_LOOKUP.length];
        }
        
        public String getResourceKey() {
            return this.resourceKey;
        }
        
        static {
            ID_LOOKUP = new EnumChatVisibility[values().length];
            for (final EnumChatVisibility entityplayer$enumchatvisibility : values()) {
                EnumChatVisibility.ID_LOOKUP[entityplayer$enumchatvisibility.chatVisibility] = entityplayer$enumchatvisibility;
            }
        }
    }
    
    static class SleepEnemyPredicate implements Predicate<EntityMob>
    {
        private final EntityPlayer player;
        
        private SleepEnemyPredicate(final EntityPlayer playerIn) {
            this.player = playerIn;
        }
        
        public boolean apply(@Nullable final EntityMob p_apply_1_) {
            return p_apply_1_.isPreventingPlayerRest(this.player);
        }
    }
    
    public enum SleepResult
    {
        OK, 
        NOT_POSSIBLE_HERE, 
        NOT_POSSIBLE_NOW, 
        TOO_FAR_AWAY, 
        OTHER_PROBLEM, 
        NOT_SAFE;
    }
}
