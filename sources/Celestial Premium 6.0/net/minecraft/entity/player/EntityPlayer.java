/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity.player;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.util.datafix.IDataFixer;
import net.minecraft.util.datafix.IDataWalker;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.celestial.client.Celestial;
import org.celestial.client.cmd.impl.FakeNameCommand;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.events.impl.player.EventUpdateLiving;
import org.celestial.client.feature.impl.combat.KeepSprint;
import org.celestial.client.feature.impl.misc.StreamerMode;
import org.celestial.client.feature.impl.player.AntiPush;

public abstract class EntityPlayer
extends EntityLivingBase {
    private static final DataParameter<Float> ABSORPTION = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> PLAYER_SCORE = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.VARINT);
    protected static final DataParameter<Byte> PLAYER_MODEL_FLAG = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BYTE);
    protected static final DataParameter<Byte> MAIN_HAND = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.BYTE);
    protected static final DataParameter<NBTTagCompound> field_192032_bt = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.field_192734_n);
    protected static final DataParameter<NBTTagCompound> field_192033_bu = EntityDataManager.createKey(EntityPlayer.class, DataSerializers.field_192734_n);
    public InventoryPlayer inventory = new InventoryPlayer(this);
    protected InventoryEnderChest theInventoryEnderChest = new InventoryEnderChest();
    public Container inventoryContainer;
    public Container openContainer;
    protected FoodStats foodStats = new FoodStats();
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
    private BlockPos spawnChunk;
    private boolean spawnForced;
    public PlayerCapabilities capabilities = new PlayerCapabilities();
    public int experienceLevel;
    public int experienceTotal;
    public float experience;
    protected int xpSeed;
    public float speedInAir = 0.02f;
    private int lastXPSound;
    private final GameProfile gameProfile;
    private boolean hasReducedDebug;
    private ItemStack itemStackMainHand = ItemStack.field_190927_a;
    private final CooldownTracker cooldownTracker = this.createCooldownTracker();
    @Nullable
    public EntityFishHook fishEntity;
    public long airTicks = 0L;
    public long groundTicks = 0L;

    protected CooldownTracker createCooldownTracker() {
        return new CooldownTracker();
    }

    public EntityPlayer(World worldIn, GameProfile gameProfileIn) {
        super(worldIn);
        this.setUniqueId(EntityPlayer.getUUID(gameProfileIn));
        this.gameProfile = gameProfileIn;
        this.openContainer = this.inventoryContainer = new ContainerPlayer(this.inventory, !worldIn.isRemote, this);
        BlockPos blockpos = worldIn.getSpawnPoint();
        this.setLocationAndAngles((double)blockpos.getX() + 0.5, blockpos.getY() + 1, (double)blockpos.getZ() + 0.5, 0.0f, 0.0f);
        this.unused180 = 180.0f;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1f);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.LUCK);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(ABSORPTION, Float.valueOf(0.0f));
        this.dataManager.register(PLAYER_SCORE, 0);
        this.dataManager.register(PLAYER_MODEL_FLAG, (byte)0);
        this.dataManager.register(MAIN_HAND, (byte)1);
        this.dataManager.register(field_192032_bt, new NBTTagCompound());
        this.dataManager.register(field_192033_bu, new NBTTagCompound());
    }

    @Override
    public void onUpdate() {
        if (this.onGround) {
            ++this.groundTicks;
            this.airTicks = 0L;
        } else {
            this.groundTicks = 0L;
            ++this.airTicks;
        }
        this.noClip = this.isSpectator();
        EventUpdateLiving event = new EventUpdateLiving();
        EventManager.call(event);
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
                } else if (this.world.isDaytime()) {
                    this.wakeUpPlayer(false, true, true);
                }
            }
        } else if (this.sleepTimer > 0) {
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
        int i = 29999999;
        double d0 = MathHelper.clamp(this.posX, -2.9999999E7, 2.9999999E7);
        double d1 = MathHelper.clamp(this.posZ, -2.9999999E7, 2.9999999E7);
        if (d0 != this.posX || d1 != this.posZ) {
            this.setPosition(d0, this.posY, d1);
        }
        ++this.ticksSinceLastSwing;
        ItemStack itemstack = this.getHeldItemMainhand();
        if (!ItemStack.areItemStacksEqual(this.itemStackMainHand, itemstack)) {
            if (!ItemStack.areItemsEqualIgnoreDurability(this.itemStackMainHand, itemstack)) {
                this.resetCooldown();
            }
            this.itemStackMainHand = itemstack.isEmpty() ? ItemStack.field_190927_a : itemstack.copy();
        }
        this.cooldownTracker.tick();
        this.updateSize();
    }

    private void updateCape() {
        this.prevChasingPosX = this.chasingPosX;
        this.prevChasingPosY = this.chasingPosY;
        this.prevChasingPosZ = this.chasingPosZ;
        double d0 = this.posX - this.chasingPosX;
        double d1 = this.posY - this.chasingPosY;
        double d2 = this.posZ - this.chasingPosZ;
        double d3 = 10.0;
        if (d0 > 10.0) {
            this.prevChasingPosX = this.chasingPosX = this.posX;
        }
        if (d2 > 10.0) {
            this.prevChasingPosZ = this.chasingPosZ = this.posZ;
        }
        if (d1 > 10.0) {
            this.prevChasingPosY = this.chasingPosY = this.posY;
        }
        if (d0 < -10.0) {
            this.prevChasingPosX = this.chasingPosX = this.posX;
        }
        if (d2 < -10.0) {
            this.prevChasingPosZ = this.chasingPosZ = this.posZ;
        }
        if (d1 < -10.0) {
            this.prevChasingPosY = this.chasingPosY = this.posY;
        }
        this.chasingPosX += d0 * 0.25;
        this.chasingPosZ += d2 * 0.25;
        this.chasingPosY += d1 * 0.25;
    }

    protected void updateSize() {
        float f1;
        float f;
        if (this.isElytraFlying()) {
            f = 0.6f;
            f1 = 0.6f;
        } else if (this.isPlayerSleeping()) {
            f = 0.2f;
            f1 = 0.2f;
        } else if (this.isSneaking()) {
            f = 0.6f;
            f1 = 1.65f;
        } else {
            f = 0.6f;
            f1 = 1.8f;
        }
        if (f != this.width || f1 != this.height) {
            AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
            axisalignedbb = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)f, axisalignedbb.minY + (double)f1, axisalignedbb.minZ + (double)f);
            if (!this.world.collidesWithAnyBlock(axisalignedbb)) {
                this.setSize(f, f1);
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
    public void playSound(SoundEvent soundIn, float volume, float pitch) {
        this.world.playSound(this, this.posX, this.posY, this.posZ, soundIn, this.getSoundCategory(), volume, pitch);
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.PLAYERS;
    }

    @Override
    protected int func_190531_bD() {
        return 20;
    }

    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 9) {
            this.onItemUseFinish();
        } else if (id == 23) {
            this.hasReducedDebug = false;
        } else if (id == 22) {
            this.hasReducedDebug = true;
        } else {
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
        } else {
            double d0 = this.posX;
            double d1 = this.posY;
            double d2 = this.posZ;
            float f = this.rotationYaw;
            float f1 = this.rotationPitch;
            super.updateRidden();
            this.prevCameraYaw = this.cameraYaw;
            this.cameraYaw = 0.0f;
            this.addMountedMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
            if (this.getRidingEntity() instanceof EntityPig) {
                this.rotationPitch = f1;
                this.rotationYaw = f;
                this.renderYawOffset = ((EntityPig)this.getRidingEntity()).renderYawOffset;
            }
        }
    }

    @Override
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
        this.rotationPitchHead = this.rotationPitch;
        this.rotationYawHead = this.rotationYaw;
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
        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        if (!this.world.isRemote) {
            iattributeinstance.setBaseValue(this.capabilities.getWalkSpeed());
        }
        this.jumpMovementFactor = this.speedInAir;
        if (this.isSprinting()) {
            this.jumpMovementFactor = (float)((double)this.jumpMovementFactor + (double)this.speedInAir * 0.3);
        }
        this.setAIMoveSpeed((float)iattributeinstance.getAttributeValue());
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float f1 = (float)(Math.atan(-this.motionY * (double)0.2f) * 15.0);
        if (f > 0.1f) {
            f = 0.1f;
        }
        if (!this.onGround || this.getHealth() <= 0.0f) {
            f = 0.0f;
        }
        if (this.onGround || this.getHealth() <= 0.0f) {
            f1 = 0.0f;
        }
        this.cameraYaw += (f - this.cameraYaw) * 0.4f;
        this.cameraPitch += (f1 - this.cameraPitch) * 0.8f;
        if (this.getHealth() > 0.0f && !this.isSpectator()) {
            AxisAlignedBB axisalignedbb = this.isRiding() && !this.getRidingEntity().isDead ? this.getEntityBoundingBox().union(this.getRidingEntity().getEntityBoundingBox()).expand(1.0, 0.0, 1.0) : this.getEntityBoundingBox().expand(1.0, 0.5, 1.0);
            List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb);
            for (int i = 0; i < list.size(); ++i) {
                Entity entity = list.get(i);
                if (entity.isDead) continue;
                this.collideWithPlayer(entity);
            }
        }
        this.func_192028_j(this.func_192023_dk());
        this.func_192028_j(this.func_192025_dl());
        if (!this.world.isRemote && (this.fallDistance > 0.5f || this.isInWater() || this.isRiding()) || this.capabilities.isFlying) {
            this.func_192030_dh();
        }
    }

    private void func_192028_j(@Nullable NBTTagCompound p_192028_1_) {
        String s;
        if ((p_192028_1_ != null && !p_192028_1_.hasKey("Silent") || !p_192028_1_.getBoolean("Silent")) && (s = p_192028_1_.getString("id")).equals(EntityList.getKey(EntityParrot.class).toString())) {
            EntityParrot.func_192005_a(this.world, this);
        }
    }

    private void collideWithPlayer(Entity entityIn) {
        entityIn.onCollideWithPlayer(this);
    }

    public int getScore() {
        return this.dataManager.get(PLAYER_SCORE);
    }

    public void setScore(int scoreIn) {
        this.dataManager.set(PLAYER_SCORE, scoreIn);
    }

    public void addScore(int scoreIn) {
        int i = this.getScore();
        this.dataManager.set(PLAYER_SCORE, i + scoreIn);
    }

    @Override
    public void onDeath(DamageSource cause) {
        super.onDeath(cause);
        this.setSize(0.2f, 0.2f);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionY = 0.1f;
        if ("Notch".equals(this.getName())) {
            this.dropItem(new ItemStack(Items.APPLE, 1), true, false);
        }
        if (!this.world.getGameRules().getBoolean("keepInventory") && !this.isSpectator()) {
            this.func_190776_cN();
            this.inventory.dropAllItems();
        }
        if (cause != null) {
            this.motionX = -MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * ((float)Math.PI / 180)) * 0.1f;
            this.motionZ = -MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * ((float)Math.PI / 180)) * 0.1f;
        } else {
            this.motionX = 0.0;
            this.motionZ = 0.0;
        }
        this.addStat(StatList.DEATHS);
        this.takeStat(StatList.TIME_SINCE_DEATH);
        this.extinguish();
        this.setFlag(0, false);
    }

    protected void func_190776_cN() {
        for (int i = 0; i < this.inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = this.inventory.getStackInSlot(i);
            if (itemstack.isEmpty() || !EnchantmentHelper.func_190939_c(itemstack)) continue;
            this.inventory.removeStackFromSlot(i);
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        if (p_184601_1_ == DamageSource.onFire) {
            return SoundEvents.field_193806_fH;
        }
        return p_184601_1_ == DamageSource.drown ? SoundEvents.field_193805_fG : SoundEvents.ENTITY_PLAYER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PLAYER_DEATH;
    }

    @Nullable
    public EntityItem dropItem(boolean dropAll) {
        return this.dropItem(this.inventory.decrStackSize(this.inventory.currentItem, dropAll && !this.inventory.getCurrentItem().isEmpty() ? this.inventory.getCurrentItem().getCount() : 1), false, true);
    }

    @Nullable
    public EntityItem dropItem(ItemStack itemStackIn, boolean unused) {
        return this.dropItem(itemStackIn, false, unused);
    }

    @Nullable
    public EntityItem dropItem(ItemStack droppedItem, boolean dropAround, boolean traceItem) {
        if (droppedItem.isEmpty()) {
            return null;
        }
        double d0 = this.posY - (double)0.3f + (double)this.getEyeHeight();
        EntityItem entityitem = new EntityItem(this.world, this.posX, d0, this.posZ, droppedItem);
        entityitem.setPickupDelay(40);
        if (traceItem) {
            entityitem.setThrower(this.getName());
        }
        if (dropAround) {
            float f = this.rand.nextFloat() * 0.5f;
            float f1 = this.rand.nextFloat() * ((float)Math.PI * 2);
            entityitem.motionX = -MathHelper.sin(f1) * f;
            entityitem.motionZ = MathHelper.cos(f1) * f;
            entityitem.motionY = 0.2f;
        } else {
            float f2 = 0.3f;
            entityitem.motionX = -MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180)) * MathHelper.cos(this.rotationPitch * ((float)Math.PI / 180)) * f2;
            entityitem.motionZ = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180)) * MathHelper.cos(this.rotationPitch * ((float)Math.PI / 180)) * f2;
            entityitem.motionY = -MathHelper.sin(this.rotationPitch * ((float)Math.PI / 180)) * f2 + 0.1f;
            float f3 = this.rand.nextFloat() * ((float)Math.PI * 2);
            f2 = 0.02f * this.rand.nextFloat();
            entityitem.motionX += Math.cos(f3) * (double)f2;
            entityitem.motionY += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f);
            entityitem.motionZ += Math.sin(f3) * (double)f2;
        }
        ItemStack itemstack = this.dropItemAndGetStack(entityitem);
        if (traceItem) {
            if (!itemstack.isEmpty()) {
                this.addStat(StatList.getDroppedObjectStats(itemstack.getItem()), droppedItem.getCount());
            }
            this.addStat(StatList.DROP);
        }
        return entityitem;
    }

    protected ItemStack dropItemAndGetStack(EntityItem p_184816_1_) {
        this.world.spawnEntityInWorld(p_184816_1_);
        return p_184816_1_.getItem();
    }

    public float getDigSpeed(IBlockState state) {
        float f = this.inventory.getStrVsBlock(state);
        if (f > 1.0f) {
            int i = EnchantmentHelper.getEfficiencyModifier(this);
            ItemStack itemstack = this.getHeldItemMainhand();
            if (i > 0 && !itemstack.isEmpty()) {
                f += (float)(i * i + 1);
            }
        }
        if (this.isPotionActive(MobEffects.HASTE)) {
            f *= 1.0f + (float)(this.getActivePotionEffect(MobEffects.HASTE).getAmplifier() + 1) * 0.2f;
        }
        if (this.isPotionActive(MobEffects.MINING_FATIGUE)) {
            float f1;
            switch (this.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) {
                case 0: {
                    f1 = 0.3f;
                    break;
                }
                case 1: {
                    f1 = 0.09f;
                    break;
                }
                case 2: {
                    f1 = 0.0027f;
                    break;
                }
                default: {
                    f1 = 8.1E-4f;
                }
            }
            f *= f1;
        }
        if (this.isInsideOfMaterial(Material.WATER) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
            f /= 5.0f;
        }
        if (!this.onGround) {
            f /= 5.0f;
        }
        return f;
    }

    public boolean canHarvestBlock(IBlockState state) {
        return this.inventory.canHarvestBlock(state);
    }

    public static void registerFixesPlayer(DataFixer fixer) {
        fixer.registerWalker(FixTypes.PLAYER, new IDataWalker(){

            @Override
            public NBTTagCompound process(IDataFixer fixer, NBTTagCompound compound, int versionIn) {
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
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.setUniqueId(EntityPlayer.getUUID(this.gameProfile));
        NBTTagList nbttaglist = compound.getTagList("Inventory", 10);
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
            this.spawnChunk = new BlockPos(compound.getInteger("SpawnX"), compound.getInteger("SpawnY"), compound.getInteger("SpawnZ"));
            this.spawnForced = compound.getBoolean("SpawnForced");
        }
        this.foodStats.readNBT(compound);
        this.capabilities.readCapabilitiesFromNBT(compound);
        if (compound.hasKey("EnderItems", 9)) {
            NBTTagList nbttaglist1 = compound.getTagList("EnderItems", 10);
            this.theInventoryEnderChest.loadInventoryFromNBT(nbttaglist1);
        }
        if (compound.hasKey("ShoulderEntityLeft", 10)) {
            this.func_192029_h(compound.getCompoundTag("ShoulderEntityLeft"));
        }
        if (compound.hasKey("ShoulderEntityRight", 10)) {
            this.func_192031_i(compound.getCompoundTag("ShoulderEntityRight"));
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
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
        if (this.spawnChunk != null) {
            compound.setInteger("SpawnX", this.spawnChunk.getX());
            compound.setInteger("SpawnY", this.spawnChunk.getY());
            compound.setInteger("SpawnZ", this.spawnChunk.getZ());
            compound.setBoolean("SpawnForced", this.spawnForced);
        }
        this.foodStats.writeNBT(compound);
        this.capabilities.writeCapabilitiesToNBT(compound);
        compound.setTag("EnderItems", this.theInventoryEnderChest.saveInventoryToNBT());
        if (!this.func_192023_dk().hasNoTags()) {
            compound.setTag("ShoulderEntityLeft", this.func_192023_dk());
        }
        if (!this.func_192025_dl().hasNoTags()) {
            compound.setTag("ShoulderEntityRight", this.func_192025_dl());
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (this.capabilities.disableDamage && !source.canHarmInCreative()) {
            return false;
        }
        this.entityAge = 0;
        if (this.getHealth() <= 0.0f) {
            return false;
        }
        if (this.isPlayerSleeping() && !this.world.isRemote) {
            this.wakeUpPlayer(true, true, false);
        }
        this.func_192030_dh();
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
        return amount == 0.0f ? false : super.attackEntityFrom(source, amount);
    }

    @Override
    protected void func_190629_c(EntityLivingBase p_190629_1_) {
        super.func_190629_c(p_190629_1_);
        if (p_190629_1_.getHeldItemMainhand().getItem() instanceof ItemAxe) {
            this.func_190777_m(true);
        }
    }

    public boolean canAttackPlayer(EntityPlayer other) {
        Team team = this.getTeam();
        Team team1 = other.getTeam();
        if (team == null) {
            return true;
        }
        return !team.isSameTeam(team1) ? true : team.getAllowFriendlyFire();
    }

    @Override
    protected void damageArmor(float damage) {
        this.inventory.damageArmor(damage);
    }

    @Override
    public void damageShield(float damage) {
        if (damage >= 3.0f && this.activeItemStack.getItem() == Items.SHIELD) {
            int i = 1 + MathHelper.floor(damage);
            this.activeItemStack.damageItem(i, this);
            if (this.activeItemStack.isEmpty()) {
                EnumHand enumhand = this.getActiveHand();
                if (enumhand == EnumHand.MAIN_HAND) {
                    this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.field_190927_a);
                } else {
                    this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.field_190927_a);
                }
                this.activeItemStack = ItemStack.field_190927_a;
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8f, 0.8f + this.world.rand.nextFloat() * 0.4f);
            }
        }
    }

    public float getArmorVisibility() {
        int i = 0;
        for (ItemStack itemstack : this.inventory.armorInventory) {
            if (itemstack.isEmpty()) continue;
            ++i;
        }
        return (float)i / (float)this.inventory.armorInventory.size();
    }

    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
        if (!this.isEntityInvulnerable(damageSrc)) {
            damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
            float f = damageAmount = this.applyPotionDamageCalculations(damageSrc, damageAmount);
            damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0f);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - damageAmount));
            if (damageAmount != 0.0f) {
                this.addExhaustion(damageSrc.getHungerDamage());
                float f1 = this.getHealth();
                this.setHealth(this.getHealth() - damageAmount);
                this.getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
                if (damageAmount < 3.4028235E37f) {
                    this.addStat(StatList.DAMAGE_TAKEN, Math.round(damageAmount * 10.0f));
                }
            }
        }
    }

    public void openEditSign(TileEntitySign signTile) {
    }

    public void displayGuiEditCommandCart(CommandBlockBaseLogic commandBlock) {
    }

    public void displayGuiCommandBlock(TileEntityCommandBlock commandBlock) {
    }

    public void openEditStructure(TileEntityStructure structure) {
    }

    public void displayVillagerTradeGui(IMerchant villager) {
    }

    public void displayGUIChest(IInventory chestInventory) {
    }

    public void openGuiHorseInventory(AbstractHorse horse, IInventory inventoryIn) {
    }

    public void displayGui(IInteractionObject guiOwner) {
    }

    public void openBook(ItemStack stack, EnumHand hand) {
    }

    public EnumActionResult func_190775_a(Entity p_190775_1_, EnumHand p_190775_2_) {
        ItemStack itemstack1;
        if (this.isSpectator()) {
            if (p_190775_1_ instanceof IInventory) {
                this.displayGUIChest((IInventory)((Object)p_190775_1_));
            }
            return EnumActionResult.PASS;
        }
        ItemStack itemstack = this.getHeldItem(p_190775_2_);
        ItemStack itemStack = itemstack1 = itemstack.isEmpty() ? ItemStack.field_190927_a : itemstack.copy();
        if (p_190775_1_.processInitialInteract(this, p_190775_2_)) {
            if (this.capabilities.isCreativeMode && itemstack == this.getHeldItem(p_190775_2_) && itemstack.getCount() < itemstack1.getCount()) {
                itemstack.func_190920_e(itemstack1.getCount());
            }
            return EnumActionResult.SUCCESS;
        }
        if (!itemstack.isEmpty() && p_190775_1_ instanceof EntityLivingBase) {
            if (this.capabilities.isCreativeMode) {
                itemstack = itemstack1;
            }
            if (itemstack.interactWithEntity(this, (EntityLivingBase)p_190775_1_, p_190775_2_)) {
                if (itemstack.isEmpty() && !this.capabilities.isCreativeMode) {
                    this.setHeldItem(p_190775_2_, ItemStack.field_190927_a);
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

    public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
        if (targetEntity.canBeAttackedWithItem() && !targetEntity.hitByEntity(this)) {
            float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
            float f1 = targetEntity instanceof EntityLivingBase ? EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)targetEntity).getCreatureAttribute()) : EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), EnumCreatureAttribute.UNDEFINED);
            float f2 = this.getCooledAttackStrength(0.5f);
            f1 *= f2;
            this.resetCooldown();
            if ((f *= 0.2f + f2 * f2 * 0.8f) > 0.0f || f1 > 0.0f) {
                ItemStack itemstack;
                boolean flag = f2 > 0.9f;
                boolean flag1 = false;
                int i = 0;
                i += EnchantmentHelper.getKnockbackModifier(this);
                if (this.isSprinting() && flag) {
                    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, this.getSoundCategory(), 1.0f, 1.0f);
                    ++i;
                    flag1 = true;
                }
                boolean flag2 = flag && this.fallDistance > 0.0f && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(MobEffects.BLINDNESS) && !this.isRiding() && targetEntity instanceof EntityLivingBase;
                boolean bl = flag2 = flag2 && !this.isSprinting();
                if (flag2) {
                    f *= 1.5f;
                }
                f += f1;
                boolean flag3 = false;
                double d0 = this.distanceWalkedModified - this.prevDistanceWalkedModified;
                if (flag && !flag2 && !flag1 && this.onGround && d0 < (double)this.getAIMoveSpeed() && (itemstack = this.getHeldItem(EnumHand.MAIN_HAND)).getItem() instanceof ItemSword) {
                    flag3 = true;
                }
                float f4 = 0.0f;
                boolean flag4 = false;
                int j = EnchantmentHelper.getFireAspectModifier(this);
                if (targetEntity instanceof EntityLivingBase) {
                    f4 = ((EntityLivingBase)targetEntity).getHealth();
                    if (j > 0 && !targetEntity.isBurning()) {
                        flag4 = true;
                        targetEntity.setFire(1);
                    }
                }
                double d1 = targetEntity.motionX;
                double d2 = targetEntity.motionY;
                double d3 = targetEntity.motionZ;
                boolean flag5 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(this), f);
                if (flag5) {
                    IEntityMultiPart ientitymultipart;
                    if (i > 0) {
                        if (targetEntity instanceof EntityLivingBase) {
                            ((EntityLivingBase)targetEntity).knockBack(this, (float)i * 0.5f, MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180)), -MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180)));
                        } else {
                            targetEntity.addVelocity(-MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180)) * (float)i * 0.5f, 0.1, MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180)) * (float)i * 0.5f);
                        }
                        if (Celestial.instance.featureManager.getFeatureByClass(KeepSprint.class).getState()) {
                            this.motionX *= (double)KeepSprint.speed.getCurrentValue();
                            this.motionZ *= (double)KeepSprint.speed.getCurrentValue();
                            this.setSprinting(KeepSprint.setSprinting.getCurrentValue());
                        } else {
                            this.motionX *= 0.6;
                            this.motionZ *= 0.6;
                            this.setSprinting(false);
                        }
                    }
                    if (flag3) {
                        float f3 = 1.0f + EnchantmentHelper.func_191527_a(this) * f;
                        for (EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB(EntityLivingBase.class, targetEntity.getEntityBoundingBox().expand(1.0, 0.25, 1.0))) {
                            if (entitylivingbase == this || entitylivingbase == targetEntity || this.isOnSameTeam(entitylivingbase) || !(this.getDistanceSqToEntity(entitylivingbase) < 9.0)) continue;
                            entitylivingbase.knockBack(this, 0.4f, MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180)), -MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180)));
                            entitylivingbase.attackEntityFrom(DamageSource.causePlayerDamage(this), f3);
                        }
                        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0f, 1.0f);
                        this.spawnSweepParticles();
                    }
                    if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
                        ((EntityPlayerMP)targetEntity).connection.sendPacket(new SPacketEntityVelocity(targetEntity));
                        targetEntity.velocityChanged = false;
                        targetEntity.motionX = d1;
                        targetEntity.motionY = d2;
                        targetEntity.motionZ = d3;
                    }
                    if (flag2) {
                        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0f, 1.0f);
                        this.onCriticalHit(targetEntity);
                    }
                    if (!flag2 && !flag3) {
                        if (flag) {
                            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0f, 1.0f);
                        } else {
                            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, this.getSoundCategory(), 1.0f, 1.0f);
                        }
                    }
                    if (f1 > 0.0f) {
                        this.onEnchantmentCritical(targetEntity);
                    }
                    this.setLastAttacker(targetEntity);
                    if (targetEntity instanceof EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments((EntityLivingBase)targetEntity, this);
                    }
                    EnchantmentHelper.applyArthropodEnchantments(this, targetEntity);
                    ItemStack itemstack1 = this.getHeldItemMainhand();
                    Entity entity = targetEntity;
                    if (targetEntity instanceof MultiPartEntityPart && (ientitymultipart = ((MultiPartEntityPart)targetEntity).entityDragonObj) instanceof EntityLivingBase) {
                        entity = (EntityLivingBase)((Object)ientitymultipart);
                    }
                    if (!itemstack1.isEmpty() && entity instanceof EntityLivingBase) {
                        itemstack1.hitEntity((EntityLivingBase)entity, this);
                        if (itemstack1.isEmpty()) {
                            this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.field_190927_a);
                        }
                    }
                    if (targetEntity instanceof EntityLivingBase) {
                        float f5 = f4 - ((EntityLivingBase)targetEntity).getHealth();
                        this.addStat(StatList.DAMAGE_DEALT, Math.round(f5 * 10.0f));
                        if (j > 0) {
                            targetEntity.setFire(j * 4);
                        }
                        if (this.world instanceof WorldServer && f5 > 2.0f) {
                            int k = (int)((double)f5 * 0.5);
                            ((WorldServer)this.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR, targetEntity.posX, targetEntity.posY + (double)(targetEntity.height * 0.5f), targetEntity.posZ, k, 0.1, 0.0, 0.1, 0.2, new int[0]);
                        }
                    }
                    this.addExhaustion(0.1f);
                } else {
                    this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0f, 1.0f);
                    if (flag4) {
                        targetEntity.extinguish();
                    }
                }
            }
        }
    }

    public void func_190777_m(boolean p_190777_1_) {
        float f = 0.25f + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05f;
        if (p_190777_1_) {
            f += 0.75f;
        }
        if (this.rand.nextFloat() < f) {
            this.getCooldownTracker().setCooldown(Items.SHIELD, 100);
            this.resetActiveHand();
            this.world.setEntityState(this, (byte)30);
        }
    }

    public void onCriticalHit(Entity entityHit) {
    }

    public void onEnchantmentCritical(Entity entityHit) {
    }

    public void spawnSweepParticles() {
        double d0 = -MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180));
        double d1 = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180));
        if (this.world instanceof WorldServer) {
            ((WorldServer)this.world).spawnParticle(EnumParticleTypes.SWEEP_ATTACK, this.posX + d0, this.posY + (double)this.height * 0.5, this.posZ + d1, 0, d0, 0.0, d1, 0.0, new int[0]);
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

    public SleepResult trySleep(BlockPos bedLocation) {
        EnumFacing enumfacing = this.world.getBlockState(bedLocation).getValue(BlockHorizontal.FACING);
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
            if (!this.func_190774_a(bedLocation, enumfacing)) {
                return SleepResult.TOO_FAR_AWAY;
            }
            double d0 = 8.0;
            double d1 = 5.0;
            List<EntityMob> list = this.world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB((double)bedLocation.getX() - 8.0, (double)bedLocation.getY() - 5.0, (double)bedLocation.getZ() - 8.0, (double)bedLocation.getX() + 8.0, (double)bedLocation.getY() + 5.0, (double)bedLocation.getZ() + 8.0), new SleepEnemyPredicate(this));
            if (!list.isEmpty()) {
                return SleepResult.NOT_SAFE;
            }
        }
        if (this.isRiding()) {
            this.dismountRidingEntity();
        }
        this.func_192030_dh();
        this.setSize(0.2f, 0.2f);
        if (this.world.isBlockLoaded(bedLocation)) {
            float f1 = 0.5f + (float)enumfacing.getXOffset() * 0.4f;
            float f = 0.5f + (float)enumfacing.getZOffset() * 0.4f;
            this.setRenderOffsetForSleep(enumfacing);
            this.setPosition((float)bedLocation.getX() + f1, (float)bedLocation.getY() + 0.6875f, (float)bedLocation.getZ() + f);
        } else {
            this.setPosition((float)bedLocation.getX() + 0.5f, (float)bedLocation.getY() + 0.6875f, (float)bedLocation.getZ() + 0.5f);
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

    private boolean func_190774_a(BlockPos p_190774_1_, EnumFacing p_190774_2_) {
        if (Math.abs(this.posX - (double)p_190774_1_.getX()) <= 3.0 && Math.abs(this.posY - (double)p_190774_1_.getY()) <= 2.0 && Math.abs(this.posZ - (double)p_190774_1_.getZ()) <= 3.0) {
            return true;
        }
        BlockPos blockpos = p_190774_1_.offset(p_190774_2_.getOpposite());
        return Math.abs(this.posX - (double)blockpos.getX()) <= 3.0 && Math.abs(this.posY - (double)blockpos.getY()) <= 2.0 && Math.abs(this.posZ - (double)blockpos.getZ()) <= 3.0;
    }

    private void setRenderOffsetForSleep(EnumFacing p_175139_1_) {
        this.renderOffsetX = -1.8f * (float)p_175139_1_.getXOffset();
        this.renderOffsetZ = -1.8f * (float)p_175139_1_.getZOffset();
    }

    public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn) {
        this.setSize(0.6f, 1.8f);
        IBlockState iblockstate = this.world.getBlockState(this.bedLocation);
        if (this.bedLocation != null && iblockstate.getBlock() == Blocks.BED) {
            this.world.setBlockState(this.bedLocation, iblockstate.withProperty(BlockBed.OCCUPIED, false), 4);
            BlockPos blockpos = BlockBed.getSafeExitLocation(this.world, this.bedLocation, 0);
            if (blockpos == null) {
                blockpos = this.bedLocation.up();
            }
            this.setPosition((float)blockpos.getX() + 0.5f, (float)blockpos.getY() + 0.1f, (float)blockpos.getZ() + 0.5f);
        }
        this.sleeping = false;
        if (!this.world.isRemote && updateWorldFlag) {
            this.world.updateAllPlayersSleepingFlag();
        }
        int n = this.sleepTimer = immediately ? 0 : 100;
        if (setSpawn) {
            this.setSpawnPoint(this.bedLocation, false);
        }
    }

    private boolean isInBed() {
        return this.world.getBlockState(this.bedLocation).getBlock() == Blocks.BED;
    }

    @Nullable
    public static BlockPos getBedSpawnLocation(World worldIn, BlockPos bedLocation, boolean forceSpawn) {
        Block block = worldIn.getBlockState(bedLocation).getBlock();
        if (block != Blocks.BED) {
            if (!forceSpawn) {
                return null;
            }
            boolean flag = block.canSpawnInBlock();
            boolean flag1 = worldIn.getBlockState(bedLocation.up()).getBlock().canSpawnInBlock();
            return flag && flag1 ? bedLocation : null;
        }
        return BlockBed.getSafeExitLocation(worldIn, bedLocation, 0);
    }

    public float getBedOrientationInDegrees() {
        if (this.bedLocation != null) {
            EnumFacing enumfacing = this.world.getBlockState(this.bedLocation).getValue(BlockHorizontal.FACING);
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

    public void addChatComponentMessage(ITextComponent chatComponent, boolean p_146105_2_) {
    }

    public BlockPos getBedLocation() {
        return this.spawnChunk;
    }

    public boolean isSpawnForced() {
        return this.spawnForced;
    }

    public void setSpawnPoint(BlockPos pos, boolean forced) {
        if (pos != null) {
            this.spawnChunk = pos;
            this.spawnForced = forced;
        } else {
            this.spawnChunk = null;
            this.spawnForced = false;
        }
    }

    public void addStat(StatBase stat) {
        this.addStat(stat, 1);
    }

    public void addStat(StatBase stat, int amount) {
    }

    public void takeStat(StatBase stat) {
    }

    public void func_192021_a(List<IRecipe> p_192021_1_) {
    }

    public void func_193102_a(ResourceLocation[] p_193102_1_) {
    }

    public void func_192022_b(List<IRecipe> p_192022_1_) {
    }

    @Override
    public void jump() {
        super.jump();
        this.addStat(StatList.JUMP);
        if (this.isSprinting()) {
            this.addExhaustion(0.2f);
        } else {
            this.addExhaustion(0.05f);
        }
    }

    @Override
    public void travel(float p_191986_1_, float p_191986_2_, float p_191986_3_) {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        if (this.capabilities.isFlying && !this.isRiding()) {
            double d3 = this.motionY;
            float f = this.jumpMovementFactor;
            this.jumpMovementFactor = this.capabilities.getFlySpeed() * (float)(this.isSprinting() ? 2 : 1);
            super.travel(p_191986_1_, p_191986_2_, p_191986_3_);
            this.motionY = d3 * 0.6;
            this.jumpMovementFactor = f;
            this.fallDistance = 0.0f;
            this.setFlag(7, false);
        } else {
            super.travel(p_191986_1_, p_191986_2_, p_191986_3_);
        }
        this.addMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
    }

    @Override
    public float getAIMoveSpeed() {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
    }

    public void addMovementStat(double p_71000_1_, double p_71000_3_, double p_71000_5_) {
        if (!this.isRiding()) {
            if (this.isInsideOfMaterial(Material.WATER)) {
                int i = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (i > 0) {
                    this.addStat(StatList.DIVE_ONE_CM, i);
                    this.addExhaustion(0.01f * (float)i * 0.01f);
                }
            } else if (this.isInWater()) {
                int j = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (j > 0) {
                    this.addStat(StatList.SWIM_ONE_CM, j);
                    this.addExhaustion(0.01f * (float)j * 0.01f);
                }
            } else if (this.isOnLadder()) {
                if (p_71000_3_ > 0.0) {
                    this.addStat(StatList.CLIMB_ONE_CM, (int)Math.round(p_71000_3_ * 100.0));
                }
            } else if (this.onGround) {
                int k = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (k > 0) {
                    if (this.isSprinting()) {
                        this.addStat(StatList.SPRINT_ONE_CM, k);
                        this.addExhaustion(0.1f * (float)k * 0.01f);
                    } else if (this.isSneaking()) {
                        this.addStat(StatList.CROUCH_ONE_CM, k);
                        this.addExhaustion(0.0f * (float)k * 0.01f);
                    } else {
                        this.addStat(StatList.WALK_ONE_CM, k);
                        this.addExhaustion(0.0f * (float)k * 0.01f);
                    }
                }
            } else if (this.isElytraFlying()) {
                int l = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0f);
                this.addStat(StatList.AVIATE_ONE_CM, l);
            } else {
                int i1 = Math.round(MathHelper.sqrt(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (i1 > 25) {
                    this.addStat(StatList.FLY_ONE_CM, i1);
                }
            }
        }
    }

    private void addMountedMovementStat(double p_71015_1_, double p_71015_3_, double p_71015_5_) {
        int i;
        if (this.isRiding() && (i = Math.round(MathHelper.sqrt(p_71015_1_ * p_71015_1_ + p_71015_3_ * p_71015_3_ + p_71015_5_ * p_71015_5_) * 100.0f)) > 0) {
            if (this.getRidingEntity() instanceof EntityMinecart) {
                this.addStat(StatList.MINECART_ONE_CM, i);
            } else if (this.getRidingEntity() instanceof EntityBoat) {
                this.addStat(StatList.BOAT_ONE_CM, i);
            } else if (this.getRidingEntity() instanceof EntityPig) {
                this.addStat(StatList.PIG_ONE_CM, i);
            } else if (this.getRidingEntity() instanceof AbstractHorse) {
                this.addStat(StatList.HORSE_ONE_CM, i);
            }
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if (!this.capabilities.allowFlying) {
            if (distance >= 2.0f) {
                this.addStat(StatList.FALL_ONE_CM, (int)Math.round((double)distance * 100.0));
            }
            super.fall(distance, damageMultiplier);
        }
    }

    @Override
    protected void resetHeight() {
        if (!this.isSpectator()) {
            super.resetHeight();
        }
    }

    @Override
    protected SoundEvent getFallSound(int heightIn) {
        return heightIn > 4 ? SoundEvents.ENTITY_PLAYER_BIG_FALL : SoundEvents.ENTITY_PLAYER_SMALL_FALL;
    }

    @Override
    public void onKillEntity(EntityLivingBase entityLivingIn) {
        EntityList.EntityEggInfo entitylist$entityegginfo = EntityList.ENTITY_EGGS.get(EntityList.func_191301_a(entityLivingIn));
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
        int i = Integer.MAX_VALUE - this.experienceTotal;
        if (amount > i) {
            amount = i;
        }
        this.experience += (float)amount / (float)this.xpBarCap();
        this.experienceTotal += amount;
        while (this.experience >= 1.0f) {
            this.experience = (this.experience - 1.0f) * (float)this.xpBarCap();
            this.addExperienceLevel(1);
            this.experience /= (float)this.xpBarCap();
        }
    }

    public int getXPSeed() {
        return this.xpSeed;
    }

    public void func_192024_a(ItemStack p_192024_1_, int p_192024_2_) {
        this.experienceLevel -= p_192024_2_;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0f;
            this.experienceTotal = 0;
        }
        this.xpSeed = this.rand.nextInt();
    }

    public void addExperienceLevel(int levels) {
        this.experienceLevel += levels;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0f;
            this.experienceTotal = 0;
        }
        if (levels > 0 && this.experienceLevel % 5 == 0 && (float)this.lastXPSound < (float)this.ticksExisted - 100.0f) {
            float f = this.experienceLevel > 30 ? 1.0f : (float)this.experienceLevel / 30.0f;
            this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP, this.getSoundCategory(), f * 0.75f, 1.0f);
            this.lastXPSound = this.ticksExisted;
        }
    }

    public int xpBarCap() {
        if (this.experienceLevel >= 30) {
            return 112 + (this.experienceLevel - 30) * 9;
        }
        return this.experienceLevel >= 15 ? 37 + (this.experienceLevel - 15) * 5 : 7 + this.experienceLevel * 2;
    }

    public void addExhaustion(float exhaustion) {
        if (!this.capabilities.disableDamage && !this.world.isRemote) {
            this.foodStats.addExhaustion(exhaustion);
        }
    }

    public FoodStats getFoodStats() {
        return this.foodStats;
    }

    public boolean canEat(boolean ignoreHunger) {
        return (ignoreHunger || this.foodStats.needFood()) && !this.capabilities.disableDamage;
    }

    public boolean shouldHeal() {
        return this.getHealth() > 0.0f && this.getHealth() < this.getMaxHealth();
    }

    public boolean isAllowEdit() {
        return this.capabilities.allowEdit;
    }

    public boolean canPlayerEdit(BlockPos pos, EnumFacing facing, ItemStack stack) {
        if (this.capabilities.allowEdit) {
            return true;
        }
        if (stack.isEmpty()) {
            return false;
        }
        BlockPos blockpos = pos.offset(facing.getOpposite());
        Block block = this.world.getBlockState(blockpos).getBlock();
        return stack.canPlaceOn(block) || stack.canEditBlocks();
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        if (!this.world.getGameRules().getBoolean("keepInventory") && !this.isSpectator()) {
            int i = this.experienceLevel * 7;
            return i > 100 ? 100 : i;
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

    public void setGameType(GameType gameType) {
    }

    @Override
    public String getName() {
        String str = this == Minecraft.getMinecraft().player && FakeNameCommand.canChange ? FakeNameCommand.currentName : (Celestial.instance.featureManager.getFeatureByClass(StreamerMode.class).getState() && StreamerMode.ownName.getCurrentValue() && this == Minecraft.getMinecraft().player ? "Protected" : this.gameProfile.getName());
        return str;
    }

    public InventoryEnderChest getInventoryEnderChest() {
        return this.theInventoryEnderChest;
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn) {
        if (slotIn == EntityEquipmentSlot.MAINHAND) {
            return this.inventory.getCurrentItem();
        }
        if (slotIn == EntityEquipmentSlot.OFFHAND) {
            return this.inventory.offHandInventory.get(0);
        }
        return slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR ? this.inventory.armorInventory.get(slotIn.getIndex()) : ItemStack.field_190927_a;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack) {
        if (slotIn == EntityEquipmentSlot.MAINHAND) {
            this.playEquipSound(stack);
            this.inventory.mainInventory.set(this.inventory.currentItem, stack);
        } else if (slotIn == EntityEquipmentSlot.OFFHAND) {
            this.playEquipSound(stack);
            this.inventory.offHandInventory.set(0, stack);
        } else if (slotIn.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
            this.playEquipSound(stack);
            this.inventory.armorInventory.set(slotIn.getIndex(), stack);
        }
    }

    public boolean func_191521_c(ItemStack p_191521_1_) {
        this.playEquipSound(p_191521_1_);
        return this.inventory.addItemStackToInventory(p_191521_1_);
    }

    @Override
    public Iterable<ItemStack> getHeldEquipment() {
        return Lists.newArrayList(this.getHeldItemMainhand(), this.getHeldItemOffhand());
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return this.inventory.armorInventory;
    }

    public boolean func_192027_g(NBTTagCompound p_192027_1_) {
        if (!this.isRiding() && this.onGround && !this.isInWater()) {
            if (this.func_192023_dk().hasNoTags()) {
                this.func_192029_h(p_192027_1_);
                return true;
            }
            if (this.func_192025_dl().hasNoTags()) {
                this.func_192031_i(p_192027_1_);
                return true;
            }
            return false;
        }
        return false;
    }

    protected void func_192030_dh() {
        this.func_192026_k(this.func_192023_dk());
        this.func_192029_h(new NBTTagCompound());
        this.func_192026_k(this.func_192025_dl());
        this.func_192031_i(new NBTTagCompound());
    }

    private void func_192026_k(@Nullable NBTTagCompound p_192026_1_) {
        if (!this.world.isRemote && !p_192026_1_.hasNoTags()) {
            Entity entity = EntityList.createEntityFromNBT(p_192026_1_, this.world);
            if (entity instanceof EntityTameable) {
                ((EntityTameable)entity).setOwnerId(this.entityUniqueID);
            }
            entity.setPosition(this.posX, this.posY + (double)0.7f, this.posZ);
            this.world.spawnEntityInWorld(entity);
        }
    }

    @Override
    public boolean isInvisibleToPlayer(EntityPlayer player) {
        if (!this.isInvisible()) {
            return false;
        }
        if (player.isSpectator()) {
            return false;
        }
        Team team = this.getTeam();
        return team == null || player == null || player.getTeam() != team || !team.getSeeFriendlyInvisiblesEnabled();
    }

    public abstract boolean isSpectator();

    public abstract boolean isCreative();

    @Override
    public boolean isPushedByWater() {
        if (Celestial.instance.featureManager.getFeatureByClass(AntiPush.class).getState() && AntiPush.water.getCurrentValue()) {
            return false;
        }
        return !this.capabilities.isFlying;
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
        TextComponentString itextcomponent = new TextComponentString(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));
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
        } else if (!this.isSneaking() && this.height != 1.65f) {
            if (this.isElytraFlying() || this.height == 0.6f) {
                f = 0.4f;
            }
        } else {
            f -= 0.08f;
        }
        return f;
    }

    @Override
    public void setAbsorptionAmount(float amount) {
        if (amount < 0.0f) {
            amount = 0.0f;
        }
        this.getDataManager().set(ABSORPTION, Float.valueOf(amount));
    }

    @Override
    public float getAbsorptionAmount() {
        return this.getDataManager().get(ABSORPTION).floatValue();
    }

    public static UUID getUUID(GameProfile profile) {
        UUID uuid = profile.getId();
        if (uuid == null) {
            uuid = EntityPlayer.getOfflineUUID(profile.getName());
        }
        return uuid;
    }

    public static UUID getOfflineUUID(String username) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(StandardCharsets.UTF_8));
    }

    public boolean canOpen(LockCode code) {
        if (code.isEmpty()) {
            return true;
        }
        ItemStack itemstack = this.getHeldItemMainhand();
        return !itemstack.isEmpty() && itemstack.hasDisplayName() ? itemstack.getDisplayName().equals(code.getLock()) : false;
    }

    public boolean isWearing(EnumPlayerModelParts part) {
        return (this.getDataManager().get(PLAYER_MODEL_FLAG) & part.getPartMask()) == part.getPartMask();
    }

    @Override
    public boolean sendCommandFeedback() {
        return this.getServer().worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
    }

    @Override
    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        if (inventorySlot >= 0 && inventorySlot < this.inventory.mainInventory.size()) {
            this.inventory.setInventorySlotContents(inventorySlot, itemStackIn);
            return true;
        }
        EntityEquipmentSlot entityequipmentslot = inventorySlot == 100 + EntityEquipmentSlot.HEAD.getIndex() ? EntityEquipmentSlot.HEAD : (inventorySlot == 100 + EntityEquipmentSlot.CHEST.getIndex() ? EntityEquipmentSlot.CHEST : (inventorySlot == 100 + EntityEquipmentSlot.LEGS.getIndex() ? EntityEquipmentSlot.LEGS : (inventorySlot == 100 + EntityEquipmentSlot.FEET.getIndex() ? EntityEquipmentSlot.FEET : null)));
        if (inventorySlot == 98) {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, itemStackIn);
            return true;
        }
        if (inventorySlot == 99) {
            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, itemStackIn);
            return true;
        }
        if (entityequipmentslot == null) {
            int i = inventorySlot - 200;
            if (i >= 0 && i < this.theInventoryEnderChest.getSizeInventory()) {
                this.theInventoryEnderChest.setInventorySlotContents(i, itemStackIn);
                return true;
            }
            return false;
        }
        if (!itemStackIn.isEmpty() && (!(itemStackIn.getItem() instanceof ItemArmor) && !(itemStackIn.getItem() instanceof ItemElytra) ? entityequipmentslot != EntityEquipmentSlot.HEAD : EntityLiving.getSlotForItemStack(itemStackIn) != entityequipmentslot)) {
            return false;
        }
        this.inventory.setInventorySlotContents(entityequipmentslot.getIndex() + this.inventory.mainInventory.size(), itemStackIn);
        return true;
    }

    public boolean hasReducedDebug() {
        return this.hasReducedDebug;
    }

    public void setReducedDebug(boolean reducedDebug) {
        this.hasReducedDebug = reducedDebug;
    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return this.dataManager.get(MAIN_HAND) == 0 ? EnumHandSide.LEFT : EnumHandSide.RIGHT;
    }

    public void setPrimaryHand(EnumHandSide hand) {
        this.dataManager.set(MAIN_HAND, (byte)(hand != EnumHandSide.LEFT ? 1 : 0));
    }

    public NBTTagCompound func_192023_dk() {
        return this.dataManager.get(field_192032_bt);
    }

    protected void func_192029_h(NBTTagCompound p_192029_1_) {
        this.dataManager.set(field_192032_bt, p_192029_1_);
    }

    public NBTTagCompound func_192025_dl() {
        return this.dataManager.get(field_192033_bu);
    }

    protected void func_192031_i(NBTTagCompound p_192031_1_) {
        this.dataManager.set(field_192033_bu, p_192031_1_);
    }

    public float getCooldownPeriod() {
        return (float)(1.0 / this.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue() * 20.0);
    }

    public float getCooledAttackStrength(float adjustTicks) {
        return MathHelper.clamp(((float)this.ticksSinceLastSwing + adjustTicks) / this.getCooldownPeriod(), 0.0f, 1.0f);
    }

    public void resetCooldown() {
        this.ticksSinceLastSwing = 0;
    }

    public CooldownTracker getCooldownTracker() {
        return this.cooldownTracker;
    }

    @Override
    public void applyEntityCollision(Entity entityIn) {
        if (!this.isPlayerSleeping()) {
            super.applyEntityCollision(entityIn);
        }
    }

    public float getLuck() {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.LUCK).getAttributeValue();
    }

    public boolean canUseCommandBlock() {
        return this.capabilities.isCreativeMode && this.canCommandSenderUseCommand(2, "");
    }

    public ItemStack getEquipmentInSlot(int item) {
        return item == 0 ? this.inventory.getCurrentItem() : this.inventory.armorInventory.get(item - 1);
    }

    public static enum SleepResult {
        OK,
        NOT_POSSIBLE_HERE,
        NOT_POSSIBLE_NOW,
        TOO_FAR_AWAY,
        OTHER_PROBLEM,
        NOT_SAFE;

    }

    static class SleepEnemyPredicate
    implements Predicate<EntityMob> {
        private final EntityPlayer field_192387_a;

        private SleepEnemyPredicate(EntityPlayer p_i47461_1_) {
            this.field_192387_a = p_i47461_1_;
        }

        @Override
        public boolean apply(@Nullable EntityMob p_apply_1_) {
            return p_apply_1_.func_191990_c(this.field_192387_a);
        }
    }

    public static enum EnumChatVisibility {
        FULL(0, "options.chat.visibility.full"),
        SYSTEM(1, "options.chat.visibility.system"),
        HIDDEN(2, "options.chat.visibility.hidden");

        private static final EnumChatVisibility[] ID_LOOKUP;
        private final int chatVisibility;
        private final String resourceKey;

        private EnumChatVisibility(int id, String resourceKey) {
            this.chatVisibility = id;
            this.resourceKey = resourceKey;
        }

        public int getChatVisibility() {
            return this.chatVisibility;
        }

        public static EnumChatVisibility getEnumChatVisibility(int id) {
            return ID_LOOKUP[id % ID_LOOKUP.length];
        }

        public String getResourceKey() {
            return this.resourceKey;
        }

        static {
            ID_LOOKUP = new EnumChatVisibility[EnumChatVisibility.values().length];
            EnumChatVisibility[] arrenumChatVisibility = EnumChatVisibility.values();
            int n = arrenumChatVisibility.length;
            for (int i = 0; i < n; ++i) {
                EnumChatVisibility entityplayer$enumchatvisibility;
                EnumChatVisibility.ID_LOOKUP[entityplayer$enumchatvisibility.chatVisibility] = entityplayer$enumchatvisibility = arrenumChatVisibility[i];
            }
        }
    }
}

