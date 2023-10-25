/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.collect.Lists
 *  com.mojang.authlib.GameProfile
 */
package net.minecraft.entity.player;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.module.combat.KillAura;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;

public abstract class EntityPlayer
extends EntityLivingBase {
    public Container inventoryContainer;
    protected float speedOnGround = 0.1f;
    private boolean spawnForced;
    public double prevChasingPosY;
    protected FoodStats foodStats;
    private int lastXPSound;
    private BlockPos spawnChunk;
    public float renderOffsetY;
    public BlockPos playerLocation;
    public float speedInAir = 0.02f;
    public InventoryPlayer inventory = new InventoryPlayer(this);
    public int xpCooldown;
    public float experience;
    private ItemStack itemInUse;
    public float renderOffsetX;
    public float renderOffsetZ;
    private InventoryEnderChest theInventoryEnderChest = new InventoryEnderChest();
    public float cameraYaw;
    public double prevChasingPosZ;
    private int xpSeed;
    public float prevCameraYaw;
    public double chasingPosZ;
    private boolean hasReducedDebug = false;
    private int sleepTimer;
    private final GameProfile gameProfile;
    public int experienceLevel;
    public double prevChasingPosX;
    private int itemInUseCount;
    public PlayerCapabilities capabilities;
    public double chasingPosY;
    public int experienceTotal;
    public EntityFishHook fishEntity;
    public double chasingPosX;
    protected boolean sleeping;
    public Container openContainer;
    private BlockPos startMinecartRidingCoordinate;
    protected int flyToggleTimer;

    public void onCriticalHit(Entity entity) {
    }

    public boolean interactWith(Entity entity) {
        ItemStack itemStack;
        if (this.isSpectator()) {
            if (entity instanceof IInventory) {
                this.displayGUIChest((IInventory)((Object)entity));
            }
            return false;
        }
        ItemStack itemStack2 = this.getCurrentEquippedItem();
        ItemStack itemStack3 = itemStack = itemStack2 != null ? itemStack2.copy() : null;
        if (!entity.interactFirst(this)) {
            if (itemStack2 != null && entity instanceof EntityLivingBase) {
                if (this.capabilities.isCreativeMode) {
                    itemStack2 = itemStack;
                }
                if (itemStack2.interactWithEntity(this, (EntityLivingBase)entity)) {
                    if (itemStack2.stackSize <= 0 && !this.capabilities.isCreativeMode) {
                        this.destroyCurrentEquippedItem();
                    }
                    return true;
                }
            }
            return false;
        }
        if (itemStack2 != null && itemStack2 == this.getCurrentEquippedItem()) {
            if (itemStack2.stackSize <= 0 && !this.capabilities.isCreativeMode) {
                this.destroyCurrentEquippedItem();
            } else if (itemStack2.stackSize < itemStack.stackSize && this.capabilities.isCreativeMode) {
                itemStack2.stackSize = itemStack.stackSize;
            }
        }
        return true;
    }

    protected void joinEntityItemWithWorld(EntityItem entityItem) {
        this.worldObj.spawnEntityInWorld(entityItem);
    }

    @Override
    public ItemStack getHeldItem() {
        return this.inventory.getCurrentItem();
    }

    @Override
    public int getMaxInPortalTime() {
        return this.capabilities.disableDamage ? 0 : 80;
    }

    public boolean canAttackPlayer(EntityPlayer entityPlayer) {
        Team team = this.getTeam();
        Team team2 = entityPlayer.getTeam();
        return team == null ? true : (!team.isSameTeam(team2) ? true : team.getAllowFriendlyFire());
    }

    public void attackTargetEntityWithCurrentItem(Entity entity) {
        if (entity.canAttackWithItem() && !entity.hitByEntity(this)) {
            float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
            int n = 0;
            float f2 = 0.0f;
            f2 = entity instanceof EntityLivingBase ? EnchantmentHelper.func_152377_a(this.getHeldItem(), ((EntityLivingBase)entity).getCreatureAttribute()) : EnchantmentHelper.func_152377_a(this.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
            n += EnchantmentHelper.getKnockbackModifier(this);
            if (this.isSprinting()) {
                ++n;
            }
            if (f > 0.0f || f2 > 0.0f) {
                boolean bl;
                boolean bl2 = bl = this.fallDistance > 0.0f && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(Potion.blindness) && this.ridingEntity == null && entity instanceof EntityLivingBase;
                if (bl && f > 0.0f) {
                    f *= 1.5f;
                }
                f += f2;
                boolean bl3 = false;
                int n2 = EnchantmentHelper.getFireAspectModifier(this);
                if (entity instanceof EntityLivingBase && n2 > 0 && !entity.isBurning()) {
                    bl3 = true;
                    entity.setFire(1);
                }
                double d = entity.motionX;
                double d2 = entity.motionY;
                double d3 = entity.motionZ;
                boolean bl4 = entity.attackEntityFrom(DamageSource.causePlayerDamage(this), f);
                if (bl4) {
                    IEntityMultiPart iEntityMultiPart;
                    if (n > 0) {
                        entity.addVelocity(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0f) * (float)n * 0.5f, 0.1, MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0f) * (float)n * 0.5f);
                        if (!Exodus.INSTANCE.settingsManager.getSettingByClass("KeepSprint", KillAura.class).getValBoolean()) {
                            this.motionX *= 0.6;
                            this.motionZ *= 0.6;
                            this.setSprinting(false);
                        }
                    }
                    if (entity instanceof EntityPlayerMP && entity.velocityChanged) {
                        ((EntityPlayerMP)entity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(entity));
                        entity.velocityChanged = false;
                        entity.motionX = d;
                        entity.motionY = d2;
                        entity.motionZ = d3;
                    }
                    if (bl) {
                        this.onCriticalHit(entity);
                    }
                    if (f2 > 0.0f) {
                        this.onEnchantmentCritical(entity);
                    }
                    if (f >= 18.0f) {
                        this.triggerAchievement(AchievementList.overkill);
                    }
                    this.setLastAttacker(entity);
                    if (entity instanceof EntityLivingBase) {
                        EnchantmentHelper.applyThornEnchantments((EntityLivingBase)entity, this);
                    }
                    EnchantmentHelper.applyArthropodEnchantments(this, entity);
                    ItemStack itemStack = this.getCurrentEquippedItem();
                    Entity entity2 = entity;
                    if (entity instanceof EntityDragonPart && (iEntityMultiPart = ((EntityDragonPart)entity).entityDragonObj) instanceof EntityLivingBase) {
                        entity2 = (EntityLivingBase)((Object)iEntityMultiPart);
                    }
                    if (itemStack != null && entity2 instanceof EntityLivingBase) {
                        itemStack.hitEntity((EntityLivingBase)entity2, this);
                        if (itemStack.stackSize <= 0) {
                            this.destroyCurrentEquippedItem();
                        }
                    }
                    if (entity instanceof EntityLivingBase) {
                        this.addStat(StatList.damageDealtStat, Math.round(f * 10.0f));
                        if (n2 > 0) {
                            entity.setFire(n2 * 4);
                        }
                    }
                    this.addExhaustion(0.3f);
                } else if (bl3) {
                    entity.extinguish();
                }
            }
        }
    }

    public EntityItem dropOneItem(boolean bl) {
        return this.dropItem(this.inventory.decrStackSize(this.inventory.currentItem, bl && this.inventory.getCurrentItem() != null ? this.inventory.getCurrentItem().stackSize : 1), false, true);
    }

    public static UUID getOfflineUUID(String string) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + string).getBytes(Charsets.UTF_8));
    }

    public void addStat(StatBase statBase, int n) {
    }

    @Override
    public int getPortalCooldown() {
        return 10;
    }

    public boolean isPlayerFullyAsleep() {
        return this.sleeping && this.sleepTimer >= 100;
    }

    @Override
    public void preparePlayerToSpawn() {
        this.setSize(0.6f, 1.8f);
        super.preparePlayerToSpawn();
        this.setHealth(this.getMaxHealth());
        this.deathTime = 0;
    }

    public EntityPlayer(World world, GameProfile gameProfile) {
        super(world);
        this.foodStats = new FoodStats();
        this.capabilities = new PlayerCapabilities();
        this.entityUniqueID = EntityPlayer.getUUID(gameProfile);
        this.gameProfile = gameProfile;
        this.openContainer = this.inventoryContainer = new ContainerPlayer(this.inventory, !world.isRemote, this);
        BlockPos blockPos = world.getSpawnPoint();
        this.setLocationAndAngles((double)blockPos.getX() + 0.5, blockPos.getY() + 1, (double)blockPos.getZ() + 0.5, 0.0f, 0.0f);
        this.field_70741_aB = 180.0f;
        this.fireResistance = 20;
    }

    private boolean isInBed() {
        return this.worldObj.getBlockState(this.playerLocation).getBlock() == Blocks.bed;
    }

    @Override
    protected String getFallSoundString(int n) {
        return n > 4 ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
    }

    public int getXPSeed() {
        return this.xpSeed;
    }

    public void sendPlayerAbilities() {
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
    public void playSound(String string, float f, float f2) {
        this.worldObj.playSoundToNearExcept(this, string, f, f2);
    }

    public boolean isBlocking() {
        return this.isUsingItem() && this.itemInUse.getItem().getItemUseAction(this.itemInUse) == EnumAction.BLOCK;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nBTTagCompound) {
        super.writeEntityToNBT(nBTTagCompound);
        nBTTagCompound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
        nBTTagCompound.setInteger("SelectedItemSlot", this.inventory.currentItem);
        nBTTagCompound.setBoolean("Sleeping", this.sleeping);
        nBTTagCompound.setShort("SleepTimer", (short)this.sleepTimer);
        nBTTagCompound.setFloat("XpP", this.experience);
        nBTTagCompound.setInteger("XpLevel", this.experienceLevel);
        nBTTagCompound.setInteger("XpTotal", this.experienceTotal);
        nBTTagCompound.setInteger("XpSeed", this.xpSeed);
        nBTTagCompound.setInteger("Score", this.getScore());
        if (this.spawnChunk != null) {
            nBTTagCompound.setInteger("SpawnX", this.spawnChunk.getX());
            nBTTagCompound.setInteger("SpawnY", this.spawnChunk.getY());
            nBTTagCompound.setInteger("SpawnZ", this.spawnChunk.getZ());
            nBTTagCompound.setBoolean("SpawnForced", this.spawnForced);
        }
        this.foodStats.writeNBT(nBTTagCompound);
        this.capabilities.writeCapabilitiesToNBT(nBTTagCompound);
        nBTTagCompound.setTag("EnderItems", this.theInventoryEnderChest.saveInventoryToNBT());
        ItemStack itemStack = this.inventory.getCurrentItem();
        if (itemStack != null && itemStack.getItem() != null) {
            nBTTagCompound.setTag("SelectedItem", itemStack.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    protected boolean canTriggerWalking() {
        return !this.capabilities.isFlying;
    }

    @Override
    public boolean isInvisibleToPlayer(EntityPlayer entityPlayer) {
        if (!this.isInvisible()) {
            return false;
        }
        if (entityPlayer.isSpectator()) {
            return false;
        }
        Team team = this.getTeam();
        return team == null || entityPlayer == null || entityPlayer.getTeam() != team || !team.getSeeFriendlyInvisiblesEnabled();
    }

    @Override
    public void setInWeb() {
        if (!this.capabilities.isFlying) {
            super.setInWeb();
        }
    }

    @Override
    public boolean isPushedByWater() {
        return !this.capabilities.isFlying;
    }

    public GameProfile getGameProfile() {
        return this.gameProfile;
    }

    @Override
    public void moveEntityWithHeading(float f, float f2) {
        double d = this.posX;
        double d2 = this.posY;
        double d3 = this.posZ;
        if (this.capabilities.isFlying && this.ridingEntity == null) {
            double d4 = this.motionY;
            float f3 = this.jumpMovementFactor;
            this.jumpMovementFactor = this.capabilities.getFlySpeed() * (float)(this.isSprinting() ? 2 : 1);
            super.moveEntityWithHeading(f, f2);
            this.motionY = d4 * 0.6;
            this.jumpMovementFactor = f3;
        } else {
            super.moveEntityWithHeading(f, f2);
        }
        this.addMovementStat(this.posX - d, this.posY - d2, this.posZ - d3);
    }

    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return true;
    }

    @Override
    protected String getHurtSound() {
        return "game.player.hurt";
    }

    public float getBedOrientationInDegrees() {
        if (this.playerLocation != null) {
            EnumFacing enumFacing = this.worldObj.getBlockState(this.playerLocation).getValue(BlockDirectional.FACING);
            switch (enumFacing) {
                case SOUTH: {
                    return 90.0f;
                }
                case NORTH: {
                    return 270.0f;
                }
                case WEST: {
                    return 0.0f;
                }
                case EAST: {
                    return 180.0f;
                }
            }
        }
        return 0.0f;
    }

    protected void updateItemUse(ItemStack itemStack, int n) {
        if (itemStack.getItemUseAction() == EnumAction.DRINK) {
            this.playSound("random.drink", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
        }
        if (itemStack.getItemUseAction() == EnumAction.EAT) {
            int n2 = 0;
            while (n2 < n) {
                Vec3 vec3 = new Vec3(((double)this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
                vec3 = vec3.rotatePitch(-this.rotationPitch * (float)Math.PI / 180.0f);
                vec3 = vec3.rotateYaw(-this.rotationYaw * (float)Math.PI / 180.0f);
                double d = (double)(-this.rand.nextFloat()) * 0.6 - 0.3;
                Vec3 vec32 = new Vec3(((double)this.rand.nextFloat() - 0.5) * 0.3, d, 0.6);
                vec32 = vec32.rotatePitch(-this.rotationPitch * (float)Math.PI / 180.0f);
                vec32 = vec32.rotateYaw(-this.rotationYaw * (float)Math.PI / 180.0f);
                vec32 = vec32.addVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
                if (itemStack.getHasSubtypes()) {
                    this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec32.xCoord, vec32.yCoord, vec32.zCoord, vec3.xCoord, vec3.yCoord + 0.05, vec3.zCoord, Item.getIdFromItem(itemStack.getItem()), itemStack.getMetadata());
                } else {
                    this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec32.xCoord, vec32.yCoord, vec32.zCoord, vec3.xCoord, vec3.yCoord + 0.05, vec3.zCoord, Item.getIdFromItem(itemStack.getItem()));
                }
                ++n2;
            }
            this.playSound("random.eat", 0.5f + 0.5f * (float)this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
        }
    }

    @Override
    public void setAbsorptionAmount(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        }
        this.getDataWatcher().updateObject(17, Float.valueOf(f));
    }

    public void displayGUIHorse(EntityHorse entityHorse, IInventory iInventory) {
    }

    public void stopUsingItem() {
        if (this.itemInUse != null) {
            this.itemInUse.onPlayerStoppedUsing(this.worldObj, this, this.itemInUseCount);
        }
        this.clearItemInUse();
    }

    public void setSpawnPoint(BlockPos blockPos, boolean bl) {
        if (blockPos != null) {
            this.spawnChunk = blockPos;
            this.spawnForced = bl;
        } else {
            this.spawnChunk = null;
            this.spawnForced = false;
        }
    }

    public int getSleepTimer() {
        return this.sleepTimer;
    }

    public boolean canEat(boolean bl) {
        return (bl || this.foodStats.needFood()) && !this.capabilities.disableDamage;
    }

    @Override
    public void handleStatusUpdate(byte by) {
        if (by == 9) {
            this.onItemUseFinish();
        } else if (by == 23) {
            this.hasReducedDebug = false;
        } else if (by == 22) {
            this.hasReducedDebug = true;
        } else {
            super.handleStatusUpdate(by);
        }
    }

    public void setItemInUse(ItemStack itemStack, int n) {
        if (itemStack != this.itemInUse) {
            this.itemInUse = itemStack;
            this.itemInUseCount = n;
            if (!this.worldObj.isRemote) {
                this.setEating(true);
            }
        }
    }

    public EntityItem dropItem(ItemStack itemStack, boolean bl, boolean bl2) {
        if (itemStack == null) {
            return null;
        }
        if (itemStack.stackSize == 0) {
            return null;
        }
        double d = this.posY - (double)0.3f + (double)this.getEyeHeight();
        EntityItem entityItem = new EntityItem(this.worldObj, this.posX, d, this.posZ, itemStack);
        entityItem.setPickupDelay(40);
        if (bl2) {
            entityItem.setThrower(this.getName());
        }
        if (bl) {
            float f = this.rand.nextFloat() * 0.5f;
            float f2 = this.rand.nextFloat() * (float)Math.PI * 2.0f;
            entityItem.motionX = -MathHelper.sin(f2) * f;
            entityItem.motionZ = MathHelper.cos(f2) * f;
            entityItem.motionY = 0.2f;
        } else {
            float f = 0.3f;
            entityItem.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float)Math.PI) * f;
            entityItem.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0f * (float)Math.PI) * f;
            entityItem.motionY = -MathHelper.sin(this.rotationPitch / 180.0f * (float)Math.PI) * f + 0.1f;
            float f3 = this.rand.nextFloat() * (float)Math.PI * 2.0f;
            f = 0.02f * this.rand.nextFloat();
            entityItem.motionX += Math.cos(f3) * (double)f;
            entityItem.motionY += (double)((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f);
            entityItem.motionZ += Math.sin(f3) * (double)f;
        }
        this.joinEntityItemWithWorld(entityItem);
        if (bl2) {
            this.triggerAchievement(StatList.dropStat);
        }
        return entityItem;
    }

    public EnumStatus trySleep(BlockPos blockPos) {
        if (!this.worldObj.isRemote) {
            if (this.isPlayerSleeping() || !this.isEntityAlive()) {
                return EnumStatus.OTHER_PROBLEM;
            }
            if (!this.worldObj.provider.isSurfaceWorld()) {
                return EnumStatus.NOT_POSSIBLE_HERE;
            }
            if (this.worldObj.isDaytime()) {
                return EnumStatus.NOT_POSSIBLE_NOW;
            }
            if (Math.abs(this.posX - (double)blockPos.getX()) > 3.0 || Math.abs(this.posY - (double)blockPos.getY()) > 2.0 || Math.abs(this.posZ - (double)blockPos.getZ()) > 3.0) {
                return EnumStatus.TOO_FAR_AWAY;
            }
            double d = 8.0;
            double d2 = 5.0;
            List<EntityMob> list = this.worldObj.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB((double)blockPos.getX() - d, (double)blockPos.getY() - d2, (double)blockPos.getZ() - d, (double)blockPos.getX() + d, (double)blockPos.getY() + d2, (double)blockPos.getZ() + d));
            if (!list.isEmpty()) {
                return EnumStatus.NOT_SAFE;
            }
        }
        if (this.isRiding()) {
            this.mountEntity(null);
        }
        this.setSize(0.2f, 0.2f);
        if (this.worldObj.isBlockLoaded(blockPos)) {
            EnumFacing enumFacing = this.worldObj.getBlockState(blockPos).getValue(BlockDirectional.FACING);
            float f = 0.5f;
            float f2 = 0.5f;
            switch (enumFacing) {
                case SOUTH: {
                    f2 = 0.9f;
                    break;
                }
                case NORTH: {
                    f2 = 0.1f;
                    break;
                }
                case WEST: {
                    f = 0.1f;
                    break;
                }
                case EAST: {
                    f = 0.9f;
                }
            }
            this.func_175139_a(enumFacing);
            this.setPosition((float)blockPos.getX() + f, (float)blockPos.getY() + 0.6875f, (float)blockPos.getZ() + f2);
        } else {
            this.setPosition((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.6875f, (float)blockPos.getZ() + 0.5f);
        }
        this.sleeping = true;
        this.sleepTimer = 0;
        this.playerLocation = blockPos;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.motionX = 0.0;
        if (!this.worldObj.isRemote) {
            this.worldObj.updateAllPlayersSleepingFlag();
        }
        return EnumStatus.OK;
    }

    @Override
    public double getYOffset() {
        return -0.35;
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);
        this.setSize(0.2f, 0.2f);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionY = 0.1f;
        if (this.getName().equals("Notch")) {
            this.dropItem(new ItemStack(Items.apple, 1), true, false);
        }
        if (!this.worldObj.getGameRules().getBoolean("keepInventory")) {
            this.inventory.dropAllItems();
        }
        if (damageSource != null) {
            this.motionX = -MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0f) * 0.1f;
            this.motionZ = -MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * (float)Math.PI / 180.0f) * 0.1f;
        } else {
            this.motionZ = 0.0;
            this.motionX = 0.0;
        }
        this.triggerAchievement(StatList.deathsStat);
        this.func_175145_a(StatList.timeSinceDeathStat);
    }

    public void destroyCurrentEquippedItem() {
        this.inventory.setInventorySlotContents(this.inventory.currentItem, null);
    }

    public boolean shouldHeal() {
        return this.getHealth() > 0.0f && this.getHealth() < this.getMaxHealth();
    }

    @Override
    protected void damageEntity(DamageSource damageSource, float f) {
        if (!this.isEntityInvulnerable(damageSource)) {
            if (!damageSource.isUnblockable() && this.isBlocking() && f > 0.0f) {
                f = (1.0f + f) * 0.5f;
            }
            f = this.applyArmorCalculations(damageSource, f);
            float f2 = f = this.applyPotionDamageCalculations(damageSource, f);
            f = Math.max(f - this.getAbsorptionAmount(), 0.0f);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (f2 - f));
            if (f != 0.0f) {
                this.addExhaustion(damageSource.getHungerDamage());
                float f3 = this.getHealth();
                this.setHealth(this.getHealth() - f);
                this.getCombatTracker().trackDamage(damageSource, f3, f);
                if (f < 3.4028235E37f) {
                    this.addStat(StatList.damageTakenStat, Math.round(f * 10.0f));
                }
            }
        }
    }

    private void func_175139_a(EnumFacing enumFacing) {
        this.renderOffsetX = 0.0f;
        this.renderOffsetZ = 0.0f;
        switch (enumFacing) {
            case SOUTH: {
                this.renderOffsetZ = -1.8f;
                break;
            }
            case NORTH: {
                this.renderOffsetZ = 1.8f;
                break;
            }
            case WEST: {
                this.renderOffsetX = 1.8f;
                break;
            }
            case EAST: {
                this.renderOffsetX = -1.8f;
            }
        }
    }

    @Override
    protected String getSwimSound() {
        return "game.player.swim";
    }

    public void respawnPlayer() {
    }

    @Override
    public float getAbsorptionAmount() {
        return this.getDataWatcher().getWatchableObjectFloat(17);
    }

    public void setScore(int n) {
        this.dataWatcher.updateObject(18, n);
    }

    public void removeExperienceLevel(int n) {
        this.experienceLevel -= n;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0f;
            this.experienceTotal = 0;
        }
        this.xpSeed = this.rand.nextInt();
    }

    public BlockPos getBedLocation() {
        return this.spawnChunk;
    }

    @Override
    public void setCurrentItemOrArmor(int n, ItemStack itemStack) {
        this.inventory.armorInventory[n] = itemStack;
    }

    public int xpBarCap() {
        return this.experienceLevel >= 30 ? 112 + (this.experienceLevel - 30) * 9 : (this.experienceLevel >= 15 ? 37 + (this.experienceLevel - 15) * 5 : 7 + this.experienceLevel * 2);
    }

    public abstract boolean isSpectator();

    @Override
    public float getAIMoveSpeed() {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
    }

    public void onEnchantmentCritical(Entity entity) {
    }

    public void displayGUIChest(IInventory iInventory) {
    }

    @Override
    public float getEyeHeight() {
        float f = 1.62f;
        if (this.isPlayerSleeping()) {
            f = 0.2f;
        }
        if (this.isSneaking()) {
            f -= 0.08f;
        }
        return f;
    }

    @Override
    public void onKillEntity(EntityLivingBase entityLivingBase) {
        EntityList.EntityEggInfo entityEggInfo;
        if (entityLivingBase instanceof IMob) {
            this.triggerAchievement(AchievementList.killEnemy);
        }
        if ((entityEggInfo = EntityList.entityEggs.get(EntityList.getEntityID(entityLivingBase))) != null) {
            this.triggerAchievement(entityEggInfo.field_151512_d);
        }
    }

    public void clearItemInUse() {
        this.itemInUse = null;
        this.itemInUseCount = 0;
        if (!this.worldObj.isRemote) {
            this.setEating(false);
        }
    }

    @Override
    protected String getDeathSound() {
        return "game.player.die";
    }

    public void addMovementStat(double d, double d2, double d3) {
        if (this.ridingEntity == null) {
            if (this.isInsideOfMaterial(Material.water)) {
                int n = Math.round(MathHelper.sqrt_double(d * d + d2 * d2 + d3 * d3) * 100.0f);
                if (n > 0) {
                    this.addStat(StatList.distanceDoveStat, n);
                    this.addExhaustion(0.015f * (float)n * 0.01f);
                }
            } else if (this.isInWater()) {
                int n = Math.round(MathHelper.sqrt_double(d * d + d3 * d3) * 100.0f);
                if (n > 0) {
                    this.addStat(StatList.distanceSwumStat, n);
                    this.addExhaustion(0.015f * (float)n * 0.01f);
                }
            } else if (this.isOnLadder()) {
                if (d2 > 0.0) {
                    this.addStat(StatList.distanceClimbedStat, (int)Math.round(d2 * 100.0));
                }
            } else if (this.onGround) {
                int n = Math.round(MathHelper.sqrt_double(d * d + d3 * d3) * 100.0f);
                if (n > 0) {
                    this.addStat(StatList.distanceWalkedStat, n);
                    if (this.isSprinting()) {
                        this.addStat(StatList.distanceSprintedStat, n);
                        this.addExhaustion(0.099999994f * (float)n * 0.01f);
                    } else {
                        if (this.isSneaking()) {
                            this.addStat(StatList.distanceCrouchedStat, n);
                        }
                        this.addExhaustion(0.01f * (float)n * 0.01f);
                    }
                }
            } else {
                int n = Math.round(MathHelper.sqrt_double(d * d + d3 * d3) * 100.0f);
                if (n > 25) {
                    this.addStat(StatList.distanceFlownStat, n);
                }
            }
        }
    }

    public void openEditSign(TileEntitySign tileEntitySign) {
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float f) {
        if (this.isEntityInvulnerable(damageSource)) {
            return false;
        }
        if (this.capabilities.disableDamage && !damageSource.canHarmInCreative()) {
            return false;
        }
        this.entityAge = 0;
        if (this.getHealth() <= 0.0f) {
            return false;
        }
        if (this.isPlayerSleeping() && !this.worldObj.isRemote) {
            this.wakeUpPlayer(true, true, false);
        }
        if (damageSource.isDifficultyScaled()) {
            if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
                f = 0.0f;
            }
            if (this.worldObj.getDifficulty() == EnumDifficulty.EASY) {
                f = f / 2.0f + 1.0f;
            }
            if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                f = f * 3.0f / 2.0f;
            }
        }
        if (f == 0.0f) {
            return false;
        }
        Entity entity = damageSource.getEntity();
        if (entity instanceof EntityArrow && ((EntityArrow)entity).shootingEntity != null) {
            entity = ((EntityArrow)entity).shootingEntity;
        }
        return super.attackEntityFrom(damageSource, f);
    }

    public boolean isWearing(EnumPlayerModelParts enumPlayerModelParts) {
        return (this.getDataWatcher().getWatchableObjectByte(10) & enumPlayerModelParts.getPartMask()) == enumPlayerModelParts.getPartMask();
    }

    private void addMountedMovementStat(double d, double d2, double d3) {
        int n;
        if (this.ridingEntity != null && (n = Math.round(MathHelper.sqrt_double(d * d + d2 * d2 + d3 * d3) * 100.0f)) > 0) {
            if (this.ridingEntity instanceof EntityMinecart) {
                this.addStat(StatList.distanceByMinecartStat, n);
                if (this.startMinecartRidingCoordinate == null) {
                    this.startMinecartRidingCoordinate = new BlockPos(this);
                } else if (this.startMinecartRidingCoordinate.distanceSq(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) >= 1000000.0) {
                    this.triggerAchievement(AchievementList.onARail);
                }
            } else if (this.ridingEntity instanceof EntityBoat) {
                this.addStat(StatList.distanceByBoatStat, n);
            } else if (this.ridingEntity instanceof EntityPig) {
                this.addStat(StatList.distanceByPigStat, n);
            } else if (this.ridingEntity instanceof EntityHorse) {
                this.addStat(StatList.distanceByHorseStat, n);
            }
        }
    }

    public void addExperienceLevel(int n) {
        this.experienceLevel += n;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0f;
            this.experienceTotal = 0;
        }
        if (n > 0 && this.experienceLevel % 5 == 0 && (float)this.lastXPSound < (float)this.ticksExisted - 100.0f) {
            float f = this.experienceLevel > 30 ? 1.0f : (float)this.experienceLevel / 30.0f;
            this.worldObj.playSoundAtEntity(this, "random.levelup", f * 0.75f, 1.0f);
            this.lastXPSound = this.ticksExisted;
        }
    }

    public ItemStack getItemInUse() {
        return this.itemInUse;
    }

    public void addExhaustion(float f) {
        if (!this.capabilities.disableDamage && !this.worldObj.isRemote) {
            this.foodStats.addExhaustion(f);
        }
    }

    @Override
    public void onLivingUpdate() {
        if (this.flyToggleTimer > 0) {
            --this.flyToggleTimer;
        }
        if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.worldObj.getGameRules().getBoolean("naturalRegeneration")) {
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
        IAttributeInstance iAttributeInstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (!this.worldObj.isRemote) {
            iAttributeInstance.setBaseValue(this.capabilities.getWalkSpeed());
        }
        this.jumpMovementFactor = this.speedInAir;
        if (this.isSprinting()) {
            this.jumpMovementFactor = (float)((double)this.jumpMovementFactor + (double)this.speedInAir * 0.3);
        }
        this.setAIMoveSpeed((float)iAttributeInstance.getAttributeValue());
        float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float f2 = (float)(Math.atan(-this.motionY * (double)0.2f) * 15.0);
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
            AxisAlignedBB axisAlignedBB = null;
            axisAlignedBB = this.ridingEntity != null && !this.ridingEntity.isDead ? this.getEntityBoundingBox().union(this.ridingEntity.getEntityBoundingBox()).expand(1.0, 0.0, 1.0) : this.getEntityBoundingBox().expand(1.0, 0.5, 1.0);
            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, axisAlignedBB);
            int n = 0;
            while (n < list.size()) {
                Entity entity = list.get(n);
                if (!entity.isDead) {
                    this.collideWithPlayer(entity);
                }
                ++n;
            }
        }
    }

    @Override
    public ItemStack[] getInventory() {
        return this.inventory.armorInventory;
    }

    public boolean canHarvestBlock(Block block) {
        return this.inventory.canHeldItemHarvest(block);
    }

    public int getScore() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)0);
        this.dataWatcher.addObject(17, Float.valueOf(0.0f));
        this.dataWatcher.addObject(18, 0);
        this.dataWatcher.addObject(10, (byte)0);
    }

    public int getItemInUseDuration() {
        return this.isUsingItem() ? this.itemInUse.getMaxItemUseDuration() - this.itemInUseCount : 0;
    }

    public void addChatComponentMessage(IChatComponent iChatComponent) {
    }

    public void displayGUIBook(ItemStack itemStack) {
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        return !this.sleeping && super.isEntityInsideOpaqueBlock();
    }

    @Override
    public void addToPlayerScore(Entity entity, int n) {
        this.addScore(n);
        Collection<ScoreObjective> collection = this.getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.totalKillCount);
        if (entity instanceof EntityPlayer) {
            this.triggerAchievement(StatList.playerKillsStat);
            collection.addAll(this.getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.playerKillCount));
            collection.addAll(this.func_175137_e(entity));
        } else {
            this.triggerAchievement(StatList.mobKillsStat);
        }
        for (ScoreObjective scoreObjective : collection) {
            Score score = this.getWorldScoreboard().getValueFromObjective(this.getName(), scoreObjective);
            score.func_96648_a();
        }
    }

    public static BlockPos getBedSpawnLocation(World world, BlockPos blockPos, boolean bl) {
        Block block = world.getBlockState(blockPos).getBlock();
        if (block != Blocks.bed) {
            if (!bl) {
                return null;
            }
            boolean bl2 = block.func_181623_g();
            boolean bl3 = world.getBlockState(blockPos.up()).getBlock().func_181623_g();
            return bl2 && bl3 ? blockPos : null;
        }
        return BlockBed.getSafeExitLocation(world, blockPos, 0);
    }

    public void displayGui(IInteractionObject iInteractionObject) {
    }

    public void addScore(int n) {
        int n2 = this.getScore();
        this.dataWatcher.updateObject(18, n2 + n);
    }

    @Override
    protected void damageArmor(float f) {
        this.inventory.damageArmor(f);
    }

    public boolean isSpawnForced() {
        return this.spawnForced;
    }

    public void setGameType(WorldSettings.GameType gameType) {
    }

    public void openEditCommandBlock(CommandBlockLogic commandBlockLogic) {
    }

    @Override
    public void jump() {
        super.jump();
        this.triggerAchievement(StatList.jumpStat);
        if (this.isSprinting()) {
            this.addExhaustion(0.8f);
        } else {
            this.addExhaustion(0.2f);
        }
    }

    public boolean canOpen(LockCode lockCode) {
        if (lockCode.isEmpty()) {
            return true;
        }
        ItemStack itemStack = this.getCurrentEquippedItem();
        return itemStack != null && itemStack.hasDisplayName() ? itemStack.getDisplayName().equals(lockCode.getLock()) : false;
    }

    private void collideWithPlayer(Entity entity) {
        entity.onCollideWithPlayer(this);
    }

    public boolean canPlayerEdit(BlockPos blockPos, EnumFacing enumFacing, ItemStack itemStack) {
        if (this.capabilities.allowEdit) {
            return true;
        }
        if (itemStack == null) {
            return false;
        }
        BlockPos blockPos2 = blockPos.offset(enumFacing.getOpposite());
        Block block = this.worldObj.getBlockState(blockPos2).getBlock();
        return itemStack.canPlaceOn(block) || itemStack.canEditBlocks();
    }

    @Override
    protected String getSplashSound() {
        return "game.player.swim.splash";
    }

    public ItemStack getCurrentEquippedItem() {
        return this.inventory.getCurrentItem();
    }

    public boolean isUsingItem() {
        return this.itemInUse != null;
    }

    @Override
    protected int getExperiencePoints(EntityPlayer entityPlayer) {
        if (this.worldObj.getGameRules().getBoolean("keepInventory")) {
            return 0;
        }
        int n = this.experienceLevel * 7;
        return n > 100 ? 100 : n;
    }

    @Override
    protected boolean isMovementBlocked() {
        return this.getHealth() <= 0.0f || this.isPlayerSleeping();
    }

    private Collection<ScoreObjective> func_175137_e(Entity entity) {
        int n;
        ScorePlayerTeam scorePlayerTeam;
        int n2;
        ScorePlayerTeam scorePlayerTeam2 = this.getWorldScoreboard().getPlayersTeam(this.getName());
        if (scorePlayerTeam2 != null && (n2 = scorePlayerTeam2.getChatFormat().getColorIndex()) >= 0 && n2 < IScoreObjectiveCriteria.field_178793_i.length) {
            for (ScoreObjective scoreObjective : this.getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.field_178793_i[n2])) {
                Score score = this.getWorldScoreboard().getValueFromObjective(entity.getName(), scoreObjective);
                score.func_96648_a();
            }
        }
        if ((scorePlayerTeam = this.getWorldScoreboard().getPlayersTeam(entity.getName())) != null && (n = scorePlayerTeam.getChatFormat().getColorIndex()) >= 0 && n < IScoreObjectiveCriteria.field_178792_h.length) {
            return this.getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.field_178792_h[n]);
        }
        return Lists.newArrayList();
    }

    public void displayVillagerTradeGui(IMerchant iMerchant) {
    }

    @Override
    public ItemStack getEquipmentInSlot(int n) {
        return n == 0 ? this.inventory.getCurrentItem() : this.inventory.armorInventory[n - 1];
    }

    @Override
    public String getName() {
        return this.gameProfile.getName();
    }

    @Override
    protected boolean isPlayer() {
        return true;
    }

    protected void closeScreen() {
        this.openContainer = this.inventoryContainer;
    }

    public boolean hasReducedDebug() {
        return this.hasReducedDebug;
    }

    public FoodStats getFoodStats() {
        return this.foodStats;
    }

    @Override
    public Team getTeam() {
        return this.getWorldScoreboard().getPlayersTeam(this.getName());
    }

    public static UUID getUUID(GameProfile gameProfile) {
        UUID uUID = gameProfile.getId();
        if (uUID == null) {
            uUID = EntityPlayer.getOfflineUUID(gameProfile.getName());
        }
        return uUID;
    }

    public float getToolDigEfficiency(Block block) {
        float f = this.inventory.getStrVsBlock(block);
        if (f > 1.0f) {
            int n = EnchantmentHelper.getEfficiencyModifier(this);
            ItemStack itemStack = this.inventory.getCurrentItem();
            if (n > 0 && itemStack != null) {
                f += (float)(n * n + 1);
            }
        }
        if (this.isPotionActive(Potion.digSpeed)) {
            f *= 1.0f + (float)(this.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2f;
        }
        if (this.isPotionActive(Potion.digSlowdown)) {
            float f2 = 1.0f;
            switch (this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) {
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
                }
            }
            f *= f2;
        }
        if (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
            f /= 5.0f;
        }
        if (!this.onGround) {
            f /= 5.0f;
        }
        return f;
    }

    @Override
    public int getTotalArmorValue() {
        return this.inventory.getTotalArmorValue();
    }

    @Override
    public void fall(float f, float f2) {
        if (!this.capabilities.allowFlying) {
            if (f >= 2.0f) {
                this.addStat(StatList.distanceFallenStat, (int)Math.round((double)f * 100.0));
            }
            super.fall(f, f2);
        }
    }

    public void triggerAchievement(StatBase statBase) {
        this.addStat(statBase, 1);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nBTTagCompound) {
        super.readEntityFromNBT(nBTTagCompound);
        this.entityUniqueID = EntityPlayer.getUUID(this.gameProfile);
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Inventory", 10);
        this.inventory.readFromNBT(nBTTagList);
        this.inventory.currentItem = nBTTagCompound.getInteger("SelectedItemSlot");
        this.sleeping = nBTTagCompound.getBoolean("Sleeping");
        this.sleepTimer = nBTTagCompound.getShort("SleepTimer");
        this.experience = nBTTagCompound.getFloat("XpP");
        this.experienceLevel = nBTTagCompound.getInteger("XpLevel");
        this.experienceTotal = nBTTagCompound.getInteger("XpTotal");
        this.xpSeed = nBTTagCompound.getInteger("XpSeed");
        if (this.xpSeed == 0) {
            this.xpSeed = this.rand.nextInt();
        }
        this.setScore(nBTTagCompound.getInteger("Score"));
        if (this.sleeping) {
            this.playerLocation = new BlockPos(this);
            this.wakeUpPlayer(true, true, false);
        }
        if (nBTTagCompound.hasKey("SpawnX", 99) && nBTTagCompound.hasKey("SpawnY", 99) && nBTTagCompound.hasKey("SpawnZ", 99)) {
            this.spawnChunk = new BlockPos(nBTTagCompound.getInteger("SpawnX"), nBTTagCompound.getInteger("SpawnY"), nBTTagCompound.getInteger("SpawnZ"));
            this.spawnForced = nBTTagCompound.getBoolean("SpawnForced");
        }
        this.foodStats.readNBT(nBTTagCompound);
        this.capabilities.readCapabilitiesFromNBT(nBTTagCompound);
        if (nBTTagCompound.hasKey("EnderItems", 9)) {
            NBTTagList nBTTagList2 = nBTTagCompound.getTagList("EnderItems", 10);
            this.theInventoryEnderChest.loadInventoryFromNBT(nBTTagList2);
        }
    }

    public void addExperience(int n) {
        this.addScore(n);
        int n2 = Integer.MAX_VALUE - this.experienceTotal;
        if (n > n2) {
            n = n2;
        }
        this.experience += (float)n / (float)this.xpBarCap();
        this.experienceTotal += n;
        while (this.experience >= 1.0f) {
            this.experience = (this.experience - 1.0f) * (float)this.xpBarCap();
            this.addExperienceLevel(1);
            this.experience /= (float)this.xpBarCap();
        }
    }

    public EntityItem dropPlayerItemWithRandomChoice(ItemStack itemStack, boolean bl) {
        return this.dropItem(itemStack, false, false);
    }

    public void func_175145_a(StatBase statBase) {
    }

    public boolean isAllowEdit() {
        return this.capabilities.allowEdit;
    }

    @Override
    public boolean replaceItemInInventory(int n, ItemStack itemStack) {
        if (n >= 0 && n < this.inventory.mainInventory.length) {
            this.inventory.setInventorySlotContents(n, itemStack);
            return true;
        }
        int n2 = n - 100;
        if (n2 >= 0 && n2 < this.inventory.armorInventory.length) {
            int n3 = n2 + 1;
            if (itemStack != null && itemStack.getItem() != null && (itemStack.getItem() instanceof ItemArmor ? EntityLiving.getArmorPosition(itemStack) != n3 : n3 != 4 || itemStack.getItem() != Items.skull && !(itemStack.getItem() instanceof ItemBlock))) {
                return false;
            }
            this.inventory.setInventorySlotContents(n2 + this.inventory.mainInventory.length, itemStack);
            return true;
        }
        int n4 = n - 200;
        if (n4 >= 0 && n4 < this.theInventoryEnderChest.getSizeInventory()) {
            this.theInventoryEnderChest.setInventorySlotContents(n4, itemStack);
            return true;
        }
        return false;
    }

    protected void onItemUseFinish() {
        if (this.itemInUse != null) {
            this.updateItemUse(this.itemInUse, 16);
            int n = this.itemInUse.stackSize;
            ItemStack itemStack = this.itemInUse.onItemUseFinish(this.worldObj, this);
            if (itemStack != this.itemInUse || itemStack != null && itemStack.stackSize != n) {
                this.inventory.mainInventory[this.inventory.currentItem] = itemStack;
                if (itemStack.stackSize == 0) {
                    this.inventory.mainInventory[this.inventory.currentItem] = null;
                }
            }
            this.clearItemInUse();
        }
    }

    @Override
    public boolean isPlayerSleeping() {
        return this.sleeping;
    }

    public void wakeUpPlayer(boolean bl, boolean bl2, boolean bl3) {
        this.setSize(0.6f, 1.8f);
        IBlockState iBlockState = this.worldObj.getBlockState(this.playerLocation);
        if (this.playerLocation != null && iBlockState.getBlock() == Blocks.bed) {
            this.worldObj.setBlockState(this.playerLocation, iBlockState.withProperty(BlockBed.OCCUPIED, false), 4);
            BlockPos blockPos = BlockBed.getSafeExitLocation(this.worldObj, this.playerLocation, 0);
            if (blockPos == null) {
                blockPos = this.playerLocation.up();
            }
            this.setPosition((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.1f, (float)blockPos.getZ() + 0.5f);
        }
        this.sleeping = false;
        if (!this.worldObj.isRemote && bl2) {
            this.worldObj.updateAllPlayersSleepingFlag();
        }
        int n = this.sleepTimer = bl ? 0 : 100;
        if (bl3) {
            this.setSpawnPoint(this.playerLocation, false);
        }
    }

    public void clonePlayer(EntityPlayer entityPlayer, boolean bl) {
        if (bl) {
            this.inventory.copyInventory(entityPlayer.inventory);
            this.setHealth(entityPlayer.getHealth());
            this.foodStats = entityPlayer.foodStats;
            this.experienceLevel = entityPlayer.experienceLevel;
            this.experienceTotal = entityPlayer.experienceTotal;
            this.experience = entityPlayer.experience;
            this.setScore(entityPlayer.getScore());
            this.field_181016_an = entityPlayer.field_181016_an;
            this.field_181017_ao = entityPlayer.field_181017_ao;
            this.field_181018_ap = entityPlayer.field_181018_ap;
        } else if (this.worldObj.getGameRules().getBoolean("keepInventory")) {
            this.inventory.copyInventory(entityPlayer.inventory);
            this.experienceLevel = entityPlayer.experienceLevel;
            this.experienceTotal = entityPlayer.experienceTotal;
            this.experience = entityPlayer.experience;
            this.setScore(entityPlayer.getScore());
        }
        this.xpSeed = entityPlayer.xpSeed;
        this.theInventoryEnderChest = entityPlayer.theInventoryEnderChest;
        this.getDataWatcher().updateObject(10, entityPlayer.getDataWatcher().getWatchableObjectByte(10));
    }

    @Override
    public IChatComponent getDisplayName() {
        ChatComponentText chatComponentText = new ChatComponentText(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));
        chatComponentText.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + this.getName() + " "));
        chatComponentText.getChatStyle().setChatHoverEvent(this.getHoverEvent());
        chatComponentText.getChatStyle().setInsertion(this.getName());
        return chatComponentText;
    }

    @Override
    public void updateRidden() {
        if (!this.worldObj.isRemote && this.isSneaking()) {
            this.mountEntity(null);
            this.setSneaking(false);
        } else {
            double d = this.posX;
            double d2 = this.posY;
            double d3 = this.posZ;
            float f = this.rotationYaw;
            float f2 = this.rotationPitch;
            super.updateRidden();
            this.prevCameraYaw = this.cameraYaw;
            this.cameraYaw = 0.0f;
            this.addMountedMovementStat(this.posX - d, this.posY - d2, this.posZ - d3);
            if (this.ridingEntity instanceof EntityPig) {
                this.rotationPitch = f2;
                this.rotationYaw = f;
                this.renderYawOffset = ((EntityPig)this.ridingEntity).renderYawOffset;
            }
        }
    }

    @Override
    public ItemStack getCurrentArmor(int n) {
        return this.inventory.armorItemInSlot(n);
    }

    public int getItemInUseCount() {
        return this.itemInUseCount;
    }

    public InventoryEnderChest getInventoryEnderChest() {
        return this.theInventoryEnderChest;
    }

    @Override
    protected void updateEntityActionState() {
        super.updateEntityActionState();
        this.updateArmSwingProgress();
        this.rotationYawHead = this.rotationYaw;
        this.rotationPitchHead = this.rotationPitch;
    }

    public boolean isUser() {
        return false;
    }

    public Scoreboard getWorldScoreboard() {
        return this.worldObj.getScoreboard();
    }

    public void setReducedDebug(boolean bl) {
        this.hasReducedDebug = bl;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.1f);
    }

    @Override
    public boolean sendCommandFeedback() {
        return MinecraftServer.getServer().worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
    }

    @Override
    public void onUpdate() {
        this.noClip = this.isSpectator();
        if (this.isSpectator()) {
            this.onGround = false;
        }
        if (this.itemInUse != null) {
            ItemStack itemStack = this.inventory.getCurrentItem();
            if (itemStack == this.itemInUse) {
                if (this.itemInUseCount <= 25 && this.itemInUseCount % 4 == 0) {
                    this.updateItemUse(itemStack, 5);
                }
                if (--this.itemInUseCount == 0 && !this.worldObj.isRemote) {
                    this.onItemUseFinish();
                }
            } else {
                this.clearItemInUse();
            }
        }
        if (this.xpCooldown > 0) {
            --this.xpCooldown;
        }
        if (this.isPlayerSleeping()) {
            ++this.sleepTimer;
            if (this.sleepTimer > 100) {
                this.sleepTimer = 100;
            }
            if (!this.worldObj.isRemote) {
                if (!this.isInBed()) {
                    this.wakeUpPlayer(true, true, false);
                } else if (this.worldObj.isDaytime()) {
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
        if (!this.worldObj.isRemote && this.openContainer != null && !this.openContainer.canInteractWith(this)) {
            this.closeScreen();
            this.openContainer = this.inventoryContainer;
        }
        if (this.isBurning() && this.capabilities.disableDamage) {
            this.extinguish();
        }
        this.prevChasingPosX = this.chasingPosX;
        this.prevChasingPosY = this.chasingPosY;
        this.prevChasingPosZ = this.chasingPosZ;
        double d = this.posX - this.chasingPosX;
        double d2 = this.posY - this.chasingPosY;
        double d3 = this.posZ - this.chasingPosZ;
        double d4 = 10.0;
        if (d > d4) {
            this.prevChasingPosX = this.chasingPosX = this.posX;
        }
        if (d3 > d4) {
            this.prevChasingPosZ = this.chasingPosZ = this.posZ;
        }
        if (d2 > d4) {
            this.prevChasingPosY = this.chasingPosY = this.posY;
        }
        if (d < -d4) {
            this.prevChasingPosX = this.chasingPosX = this.posX;
        }
        if (d3 < -d4) {
            this.prevChasingPosZ = this.chasingPosZ = this.posZ;
        }
        if (d2 < -d4) {
            this.prevChasingPosY = this.chasingPosY = this.posY;
        }
        this.chasingPosX += d * 0.25;
        this.chasingPosZ += d3 * 0.25;
        this.chasingPosY += d2 * 0.25;
        if (this.ridingEntity == null) {
            this.startMinecartRidingCoordinate = null;
        }
        if (!this.worldObj.isRemote) {
            this.foodStats.onUpdate(this);
            this.triggerAchievement(StatList.minutesPlayedStat);
            if (this.isEntityAlive()) {
                this.triggerAchievement(StatList.timeSinceDeathStat);
            }
        }
        int n = 29999999;
        double d5 = MathHelper.clamp_double(this.posX, -2.9999999E7, 2.9999999E7);
        double d6 = MathHelper.clamp_double(this.posZ, -2.9999999E7, 2.9999999E7);
        if (d5 != this.posX || d6 != this.posZ) {
            this.setPosition(d5, this.posY, d6);
        }
    }

    public float getArmorVisibility() {
        int n = 0;
        ItemStack[] itemStackArray = this.inventory.armorInventory;
        int n2 = this.inventory.armorInventory.length;
        int n3 = 0;
        while (n3 < n2) {
            ItemStack itemStack = itemStackArray[n3];
            if (itemStack != null) {
                ++n;
            }
            ++n3;
        }
        return (float)n / (float)this.inventory.armorInventory.length;
    }

    @Override
    protected void resetHeight() {
        if (!this.isSpectator()) {
            super.resetHeight();
        }
    }

    public static enum EnumStatus {
        OK,
        NOT_POSSIBLE_HERE,
        NOT_POSSIBLE_NOW,
        TOO_FAR_AWAY,
        OTHER_PROBLEM,
        NOT_SAFE;

    }

    public static enum EnumChatVisibility {
        FULL(0, "options.chat.visibility.full"),
        SYSTEM(1, "options.chat.visibility.system"),
        HIDDEN(2, "options.chat.visibility.hidden");

        private final String resourceKey;
        private final int chatVisibility;
        private static final EnumChatVisibility[] ID_LOOKUP;

        private EnumChatVisibility(int n2, String string2) {
            this.chatVisibility = n2;
            this.resourceKey = string2;
        }

        public String getResourceKey() {
            return this.resourceKey;
        }

        static {
            ID_LOOKUP = new EnumChatVisibility[EnumChatVisibility.values().length];
            EnumChatVisibility[] enumChatVisibilityArray = EnumChatVisibility.values();
            int n = enumChatVisibilityArray.length;
            int n2 = 0;
            while (n2 < n) {
                EnumChatVisibility enumChatVisibility;
                EnumChatVisibility.ID_LOOKUP[enumChatVisibility.chatVisibility] = enumChatVisibility = enumChatVisibilityArray[n2];
                ++n2;
            }
        }

        public static EnumChatVisibility getEnumChatVisibility(int n) {
            return ID_LOOKUP[n % ID_LOOKUP.length];
        }

        public int getChatVisibility() {
            return this.chatVisibility;
        }
    }
}

