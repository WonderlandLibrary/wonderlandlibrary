// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.entity.player;

import net.minecraft.item.ItemBlock;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemArmor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.LockCode;
import com.google.common.base.Charsets;
import java.util.UUID;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.WorldSettings;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.IChatComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockBed;
import net.minecraft.init.Blocks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDirectional;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.world.IInteractionObject;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.IMerchant;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.scoreboard.Team;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.block.material.Material;
import net.minecraft.potion.Potion;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.scoreboard.ScorePlayerTeam;
import com.google.common.collect.Lists;
import net.minecraft.scoreboard.Score;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.init.Items;
import exhibition.event.impl.EventDeath;
import net.minecraft.util.DamageSource;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Vec3;
import exhibition.event.Event;
import net.minecraft.util.MathHelper;
import net.minecraft.stats.StatList;
import exhibition.event.EventSystem;
import exhibition.event.impl.EventTick;
import net.minecraft.item.EnumAction;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.projectile.EntityFishHook;
import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.FoodStats;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.entity.EntityLivingBase;

public abstract class EntityPlayer extends EntityLivingBase
{
    public InventoryPlayer inventory;
    private InventoryEnderChest theInventoryEnderChest;
    public Container inventoryContainer;
    public Container openContainer;
    protected FoodStats foodStats;
    protected int flyToggleTimer;
    public float prevCameraYaw;
    public float cameraYaw;
    public int xpCooldown;
    public double field_71091_bM;
    public double field_71096_bN;
    public double field_71097_bO;
    public double field_71094_bP;
    public double field_71095_bQ;
    public double field_71085_bR;
    protected boolean sleeping;
    public BlockPos playerLocation;
    private int sleepTimer;
    public float field_71079_bU;
    public float field_71082_cx;
    public float field_71089_bV;
    private BlockPos spawnChunk;
    private boolean spawnForced;
    private BlockPos startMinecartRidingCoordinate;
    public PlayerCapabilities capabilities;
    public int experienceLevel;
    public int experienceTotal;
    public float experience;
    private int field_175152_f;
    private ItemStack itemInUse;
    private int itemInUseCount;
    protected float speedOnGround;
    protected float speedInAir;
    private int field_82249_h;
    private final GameProfile gameProfile;
    private boolean field_175153_bG;
    public EntityFishHook fishEntity;
    private static final String __OBFID = "CL_00001711";
    
    public EntityPlayer(final World worldIn, final GameProfile p_i45324_2_) {
        super(worldIn);
        this.inventory = new InventoryPlayer(this);
        this.theInventoryEnderChest = new InventoryEnderChest();
        this.foodStats = new FoodStats();
        this.capabilities = new PlayerCapabilities();
        this.speedOnGround = 0.1f;
        this.speedInAir = 0.02f;
        this.field_175153_bG = false;
        this.entityUniqueID = getUUID(p_i45324_2_);
        this.gameProfile = p_i45324_2_;
        this.inventoryContainer = new ContainerPlayer(this.inventory, !worldIn.isRemote, this);
        this.openContainer = this.inventoryContainer;
        final BlockPos var3 = worldIn.getSpawnPoint();
        this.setLocationAndAngles(var3.getX() + 0.5, var3.getY() + 1, var3.getZ() + 0.5, 0.0f, 0.0f);
        this.field_70741_aB = 180.0f;
        this.fireResistance = 20;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.10000000149011612);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (byte)0);
        this.dataWatcher.addObject(17, 0.0f);
        this.dataWatcher.addObject(18, 0);
        this.dataWatcher.addObject(10, (byte)0);
    }
    
    public ItemStack getItemInUse() {
        return this.itemInUse;
    }
    
    public int getItemInUseCount() {
        return this.itemInUseCount;
    }
    
    public boolean isUsingItem() {
        return this.itemInUse != null;
    }
    
    public int getItemInUseDuration() {
        return this.isUsingItem() ? (this.itemInUse.getMaxItemUseDuration() - this.itemInUseCount) : 0;
    }
    
    public void stopUsingItem() {
        if (this.itemInUse != null) {
            this.itemInUse.onPlayerStoppedUsing(this.worldObj, this, this.itemInUseCount);
        }
        this.clearItemInUse();
    }
    
    public void clearItemInUse() {
        this.itemInUse = null;
        this.itemInUseCount = 0;
        if (!this.worldObj.isRemote) {
            this.setEating(false);
        }
    }
    
    public boolean isBlocking() {
        return this.isUsingItem() && this.itemInUse.getItem().getItemUseAction(this.itemInUse) == EnumAction.BLOCK;
    }
    
    @Override
    public void onUpdate() {
        final Event e = EventSystem.getInstance(EventTick.class);
        e.fire();
        if (e.isCancelled()) {
            return;
        }
        this.noClip = this.func_175149_v();
        if (this.func_175149_v()) {
            this.onGround = false;
        }
        if (this.itemInUse != null) {
            final ItemStack var1 = this.inventory.getCurrentItem();
            if (var1 == this.itemInUse) {
                if (this.itemInUseCount <= 25 && this.itemInUseCount % 4 == 0) {
                    this.updateItemUse(var1, 5);
                }
                if (--this.itemInUseCount == 0 && !this.worldObj.isRemote) {
                    this.onItemUseFinish();
                }
            }
            else {
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
                if (!this.func_175143_p()) {
                    this.wakeUpPlayer(true, true, false);
                }
                else if (this.worldObj.isDaytime()) {
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
        if (!this.worldObj.isRemote && this.openContainer != null && !this.openContainer.canInteractWith(this)) {
            this.closeScreen();
            this.openContainer = this.inventoryContainer;
        }
        if (this.isBurning() && this.capabilities.disableDamage) {
            this.extinguish();
        }
        this.field_71091_bM = this.field_71094_bP;
        this.field_71096_bN = this.field_71095_bQ;
        this.field_71097_bO = this.field_71085_bR;
        final double var2 = this.posX - this.field_71094_bP;
        final double var3 = this.posY - this.field_71095_bQ;
        final double var4 = this.posZ - this.field_71085_bR;
        final double var5 = 10.0;
        if (var2 > var5) {
            final double posX = this.posX;
            this.field_71094_bP = posX;
            this.field_71091_bM = posX;
        }
        if (var4 > var5) {
            final double posZ = this.posZ;
            this.field_71085_bR = posZ;
            this.field_71097_bO = posZ;
        }
        if (var3 > var5) {
            final double posY = this.posY;
            this.field_71095_bQ = posY;
            this.field_71096_bN = posY;
        }
        if (var2 < -var5) {
            final double posX2 = this.posX;
            this.field_71094_bP = posX2;
            this.field_71091_bM = posX2;
        }
        if (var4 < -var5) {
            final double posZ2 = this.posZ;
            this.field_71085_bR = posZ2;
            this.field_71097_bO = posZ2;
        }
        if (var3 < -var5) {
            final double posY2 = this.posY;
            this.field_71095_bQ = posY2;
            this.field_71096_bN = posY2;
        }
        this.field_71094_bP += var2 * 0.25;
        this.field_71085_bR += var4 * 0.25;
        this.field_71095_bQ += var3 * 0.25;
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
        final int var6 = 29999999;
        final double var7 = MathHelper.clamp_double(this.posX, -2.9999999E7, 2.9999999E7);
        final double var8 = MathHelper.clamp_double(this.posZ, -2.9999999E7, 2.9999999E7);
        if (var7 != this.posX || var8 != this.posZ) {
            this.setPosition(var7, this.posY, var8);
        }
    }
    
    @Override
    public int getMaxInPortalTime() {
        return this.capabilities.disableDamage ? 0 : 80;
    }
    
    @Override
    protected String getSwimSound() {
        return "game.player.swim";
    }
    
    @Override
    protected String getSplashSound() {
        return "game.player.swim.splash";
    }
    
    @Override
    public int getPortalCooldown() {
        return 10;
    }
    
    @Override
    public void playSound(final String name, final float volume, final float pitch) {
        this.worldObj.playSoundToNearExcept(this, name, volume, pitch);
    }
    
    protected void updateItemUse(final ItemStack itemStackIn, final int p_71010_2_) {
        if (itemStackIn.getItemUseAction() == EnumAction.DRINK) {
            this.playSound("random.drink", 0.5f, this.worldObj.rand.nextFloat() * 0.1f + 0.9f);
        }
        if (itemStackIn.getItemUseAction() == EnumAction.EAT) {
            for (int var3 = 0; var3 < p_71010_2_; ++var3) {
                Vec3 var4 = new Vec3((this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
                var4 = var4.rotatePitch(-this.rotationPitch * 3.1415927f / 180.0f);
                var4 = var4.rotateYaw(-this.rotationYaw * 3.1415927f / 180.0f);
                final double var5 = -this.rand.nextFloat() * 0.6 - 0.3;
                Vec3 var6 = new Vec3((this.rand.nextFloat() - 0.5) * 0.3, var5, 0.6);
                var6 = var6.rotatePitch(-this.rotationPitch * 3.1415927f / 180.0f);
                var6 = var6.rotateYaw(-this.rotationYaw * 3.1415927f / 180.0f);
                var6 = var6.addVector(this.posX, this.posY + this.getEyeHeight(), this.posZ);
                if (itemStackIn.getHasSubtypes()) {
                    this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, var6.xCoord, var6.yCoord, var6.zCoord, var4.xCoord, var4.yCoord + 0.05, var4.zCoord, Item.getIdFromItem(itemStackIn.getItem()), itemStackIn.getMetadata());
                }
                else {
                    this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, var6.xCoord, var6.yCoord, var6.zCoord, var4.xCoord, var4.yCoord + 0.05, var4.zCoord, Item.getIdFromItem(itemStackIn.getItem()));
                }
            }
            this.playSound("random.eat", 0.5f + 0.5f * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
        }
    }
    
    protected void onItemUseFinish() {
        if (this.itemInUse != null) {
            this.updateItemUse(this.itemInUse, 16);
            final int var1 = this.itemInUse.stackSize;
            final ItemStack var2 = this.itemInUse.onItemUseFinish(this.worldObj, this);
            if (var2 != this.itemInUse || (var2 != null && var2.stackSize != var1)) {
                this.inventory.mainInventory[this.inventory.currentItem] = var2;
                if (var2.stackSize == 0) {
                    this.inventory.mainInventory[this.inventory.currentItem] = null;
                }
            }
            this.clearItemInUse();
        }
    }
    
    @Override
    public void handleHealthUpdate(final byte p_70103_1_) {
        if (p_70103_1_ == 9) {
            this.onItemUseFinish();
        }
        else if (p_70103_1_ == 23) {
            this.field_175153_bG = false;
        }
        else if (p_70103_1_ == 22) {
            this.field_175153_bG = true;
        }
        else {
            super.handleHealthUpdate(p_70103_1_);
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
        if (!this.worldObj.isRemote && this.isSneaking()) {
            this.mountEntity(null);
            this.setSneaking(false);
        }
        else {
            final double var1 = this.posX;
            final double var2 = this.posY;
            final double var3 = this.posZ;
            final float var4 = this.rotationYaw;
            final float var5 = this.rotationPitch;
            super.updateRidden();
            this.prevCameraYaw = this.cameraYaw;
            this.cameraYaw = 0.0f;
            this.addMountedMovementStat(this.posX - var1, this.posY - var2, this.posZ - var3);
            if (this.ridingEntity instanceof EntityPig) {
                this.rotationPitch = var5;
                this.rotationYaw = var4;
                this.renderYawOffset = ((EntityPig)this.ridingEntity).renderYawOffset;
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
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.flyToggleTimer > 0) {
            --this.flyToggleTimer;
        }
        if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration")) {
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
        final IAttributeInstance var1 = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
        if (!this.worldObj.isRemote) {
            var1.setBaseValue(this.capabilities.getWalkSpeed());
        }
        this.jumpMovementFactor = this.speedInAir;
        if (this.isSprinting()) {
            this.jumpMovementFactor += (float)(this.speedInAir * 0.3);
        }
        this.setAIMoveSpeed((float)var1.getAttributeValue());
        float var2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        float var3 = (float)(Math.atan(-this.motionY * 0.20000000298023224) * 15.0);
        if (var2 > 0.1f) {
            var2 = 0.1f;
        }
        if (!this.onGround || this.getHealth() <= 0.0f) {
            var2 = 0.0f;
        }
        if (this.onGround || this.getHealth() <= 0.0f) {
            var3 = 0.0f;
        }
        this.cameraYaw += (var2 - this.cameraYaw) * 0.4f;
        this.cameraPitch += (var3 - this.cameraPitch) * 0.8f;
        if (this.getHealth() > 0.0f && !this.func_175149_v()) {
            AxisAlignedBB var4 = null;
            if (this.ridingEntity != null && !this.ridingEntity.isDead) {
                var4 = this.getEntityBoundingBox().union(this.ridingEntity.getEntityBoundingBox()).expand(1.0, 0.0, 1.0);
            }
            else {
                var4 = this.getEntityBoundingBox().expand(1.0, 0.5, 1.0);
            }
            final List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, var4);
            for (int var6 = 0; var6 < var5.size(); ++var6) {
                final Entity var7 = var5.get(var6);
                if (!var7.isDead) {
                    this.collideWithPlayer(var7);
                }
            }
        }
    }
    
    private void collideWithPlayer(final Entity p_71044_1_) {
        p_71044_1_.onCollideWithPlayer(this);
    }
    
    public int getScore() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }
    
    public void setScore(final int p_85040_1_) {
        this.dataWatcher.updateObject(18, p_85040_1_);
    }
    
    public void addScore(final int p_85039_1_) {
        final int var2 = this.getScore();
        this.dataWatcher.updateObject(18, var2 + p_85039_1_);
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        final Event event = EventSystem.getInstance(EventDeath.class);
        event.fire();
        if (event.isCancelled()) {
            return;
        }
        super.onDeath(cause);
        this.setSize(0.2f, 0.2f);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionY = 0.10000000149011612;
        if (this.getName().equals("Notch")) {
            this.func_146097_a(new ItemStack(Items.apple, 1), true, false);
        }
        if (!this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
            this.inventory.dropAllItems();
        }
        if (cause != null) {
            this.motionX = -MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * 3.1415927f / 180.0f) * 0.1f;
            this.motionZ = -MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * 3.1415927f / 180.0f) * 0.1f;
        }
        else {
            final double n = 0.0;
            this.motionZ = n;
            this.motionX = n;
        }
        this.triggerAchievement(StatList.deathsStat);
        this.func_175145_a(StatList.timeSinceDeathStat);
    }
    
    @Override
    protected String getHurtSound() {
        return "game.player.hurt";
    }
    
    @Override
    protected String getDeathSound() {
        return "game.player.die";
    }
    
    @Override
    public void addToPlayerScore(final Entity entityIn, final int amount) {
        this.addScore(amount);
        final Collection var3 = this.getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.totalKillCount);
        if (entityIn instanceof EntityPlayer) {
            this.triggerAchievement(StatList.playerKillsStat);
            var3.addAll(this.getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.playerKillCount));
            var3.addAll(this.func_175137_e(entityIn));
        }
        else {
            this.triggerAchievement(StatList.mobKillsStat);
        }
        for (final ScoreObjective var5 : var3) {
            final Score var6 = this.getWorldScoreboard().getValueFromObjective(this.getName(), var5);
            var6.func_96648_a();
        }
    }
    
    private Collection func_175137_e(final Entity p_175137_1_) {
        final ScorePlayerTeam var2 = this.getWorldScoreboard().getPlayersTeam(this.getName());
        if (var2 != null) {
            final int var3 = var2.func_178775_l().func_175746_b();
            if (var3 >= 0 && var3 < IScoreObjectiveCriteria.field_178793_i.length) {
                for (final ScoreObjective var5 : this.getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.field_178793_i[var3])) {
                    final Score var6 = this.getWorldScoreboard().getValueFromObjective(p_175137_1_.getName(), var5);
                    var6.func_96648_a();
                }
            }
        }
        final ScorePlayerTeam var7 = this.getWorldScoreboard().getPlayersTeam(p_175137_1_.getName());
        if (var7 != null) {
            final int var8 = var7.func_178775_l().func_175746_b();
            if (var8 >= 0 && var8 < IScoreObjectiveCriteria.field_178792_h.length) {
                return this.getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.field_178792_h[var8]);
            }
        }
        return Lists.newArrayList();
    }
    
    public EntityItem dropOneItem(final boolean p_71040_1_) {
        return this.func_146097_a(this.inventory.decrStackSize(this.inventory.currentItem, (p_71040_1_ && this.inventory.getCurrentItem() != null) ? this.inventory.getCurrentItem().stackSize : 1), false, true);
    }
    
    public EntityItem dropPlayerItemWithRandomChoice(final ItemStack itemStackIn, final boolean p_71019_2_) {
        return this.func_146097_a(itemStackIn, false, false);
    }
    
    public EntityItem func_146097_a(final ItemStack p_146097_1_, final boolean p_146097_2_, final boolean p_146097_3_) {
        if (p_146097_1_ == null) {
            return null;
        }
        if (p_146097_1_.stackSize == 0) {
            return null;
        }
        final double var4 = this.posY - 0.30000001192092896 + this.getEyeHeight();
        final EntityItem var5 = new EntityItem(this.worldObj, this.posX, var4, this.posZ, p_146097_1_);
        var5.setPickupDelay(40);
        if (p_146097_3_) {
            var5.setThrower(this.getName());
        }
        if (p_146097_2_) {
            final float var6 = this.rand.nextFloat() * 0.5f;
            final float var7 = this.rand.nextFloat() * 3.1415927f * 2.0f;
            var5.motionX = -MathHelper.sin(var7) * var6;
            var5.motionZ = MathHelper.cos(var7) * var6;
            var5.motionY = 0.20000000298023224;
        }
        else {
            float var6 = 0.3f;
            var5.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f) * var6;
            var5.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f) * var6;
            var5.motionY = -MathHelper.sin(this.rotationPitch / 180.0f * 3.1415927f) * var6 + 0.1f;
            final float var7 = this.rand.nextFloat() * 3.1415927f * 2.0f;
            var6 = 0.02f * this.rand.nextFloat();
            final EntityItem entityItem = var5;
            entityItem.motionX += Math.cos(var7) * var6;
            final EntityItem entityItem2 = var5;
            entityItem2.motionY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1f;
            final EntityItem entityItem3 = var5;
            entityItem3.motionZ += Math.sin(var7) * var6;
        }
        this.joinEntityItemWithWorld(var5);
        if (p_146097_3_) {
            this.triggerAchievement(StatList.dropStat);
        }
        return var5;
    }
    
    protected void joinEntityItemWithWorld(final EntityItem p_71012_1_) {
        this.worldObj.spawnEntityInWorld(p_71012_1_);
    }
    
    public float func_180471_a(final Block p_180471_1_) {
        float var2 = this.inventory.getStrVsBlock(p_180471_1_);
        if (var2 > 1.0f) {
            final int var3 = EnchantmentHelper.getEfficiencyModifier(this);
            final ItemStack var4 = this.inventory.getCurrentItem();
            if (var3 > 0 && var4 != null) {
                var2 += var3 * var3 + 1;
            }
        }
        if (this.isPotionActive(Potion.digSpeed)) {
            var2 *= 1.0f + (this.getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2f;
        }
        if (this.isPotionActive(Potion.digSlowdown)) {
            float var5 = 1.0f;
            switch (this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) {
                case 0: {
                    var5 = 0.3f;
                    break;
                }
                case 1: {
                    var5 = 0.09f;
                    break;
                }
                case 2: {
                    var5 = 0.0027f;
                    break;
                }
                default: {
                    var5 = 8.1E-4f;
                    break;
                }
            }
            var2 *= var5;
        }
        if (this.isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
            var2 /= 5.0f;
        }
        if (!this.onGround) {
            var2 /= 5.0f;
        }
        return var2;
    }
    
    public boolean canHarvestBlock(final Block p_146099_1_) {
        return this.inventory.func_146025_b(p_146099_1_);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.entityUniqueID = getUUID(this.gameProfile);
        final NBTTagList var2 = tagCompund.getTagList("Inventory", 10);
        this.inventory.readFromNBT(var2);
        this.inventory.currentItem = tagCompund.getInteger("SelectedItemSlot");
        this.sleeping = tagCompund.getBoolean("Sleeping");
        this.sleepTimer = tagCompund.getShort("SleepTimer");
        this.experience = tagCompund.getFloat("XpP");
        this.experienceLevel = tagCompund.getInteger("XpLevel");
        this.experienceTotal = tagCompund.getInteger("XpTotal");
        this.field_175152_f = tagCompund.getInteger("XpSeed");
        if (this.field_175152_f == 0) {
            this.field_175152_f = this.rand.nextInt();
        }
        this.setScore(tagCompund.getInteger("Score"));
        if (this.sleeping) {
            this.playerLocation = new BlockPos(this);
            this.wakeUpPlayer(true, true, false);
        }
        if (tagCompund.hasKey("SpawnX", 99) && tagCompund.hasKey("SpawnY", 99) && tagCompund.hasKey("SpawnZ", 99)) {
            this.spawnChunk = new BlockPos(tagCompund.getInteger("SpawnX"), tagCompund.getInteger("SpawnY"), tagCompund.getInteger("SpawnZ"));
            this.spawnForced = tagCompund.getBoolean("SpawnForced");
        }
        this.foodStats.readNBT(tagCompund);
        this.capabilities.readCapabilitiesFromNBT(tagCompund);
        if (tagCompund.hasKey("EnderItems", 9)) {
            final NBTTagList var3 = tagCompund.getTagList("EnderItems", 10);
            this.theInventoryEnderChest.loadInventoryFromNBT(var3);
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
        tagCompound.setInteger("SelectedItemSlot", this.inventory.currentItem);
        tagCompound.setBoolean("Sleeping", this.sleeping);
        tagCompound.setShort("SleepTimer", (short)this.sleepTimer);
        tagCompound.setFloat("XpP", this.experience);
        tagCompound.setInteger("XpLevel", this.experienceLevel);
        tagCompound.setInteger("XpTotal", this.experienceTotal);
        tagCompound.setInteger("XpSeed", this.field_175152_f);
        tagCompound.setInteger("Score", this.getScore());
        if (this.spawnChunk != null) {
            tagCompound.setInteger("SpawnX", this.spawnChunk.getX());
            tagCompound.setInteger("SpawnY", this.spawnChunk.getY());
            tagCompound.setInteger("SpawnZ", this.spawnChunk.getZ());
            tagCompound.setBoolean("SpawnForced", this.spawnForced);
        }
        this.foodStats.writeNBT(tagCompound);
        this.capabilities.writeCapabilitiesToNBT(tagCompound);
        tagCompound.setTag("EnderItems", this.theInventoryEnderChest.saveInventoryToNBT());
        final ItemStack var2 = this.inventory.getCurrentItem();
        if (var2 != null && var2.getItem() != null) {
            tagCompound.setTag("SelectedItem", var2.writeToNBT(new NBTTagCompound()));
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, float amount) {
        if (this.func_180431_b(source)) {
            return false;
        }
        if (this.capabilities.disableDamage && !source.canHarmInCreative()) {
            return false;
        }
        this.entityAge = 0;
        if (this.getHealth() <= 0.0f) {
            return false;
        }
        if (this.isPlayerSleeping() && !this.worldObj.isRemote) {
            this.wakeUpPlayer(true, true, false);
        }
        if (source.isDifficultyScaled()) {
            if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
                amount = 0.0f;
            }
            if (this.worldObj.getDifficulty() == EnumDifficulty.EASY) {
                amount = amount / 2.0f + 1.0f;
            }
            if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                amount = amount * 3.0f / 2.0f;
            }
        }
        if (amount == 0.0f) {
            return false;
        }
        Entity var3 = source.getEntity();
        if (var3 instanceof EntityArrow && ((EntityArrow)var3).shootingEntity != null) {
            var3 = ((EntityArrow)var3).shootingEntity;
        }
        return super.attackEntityFrom(source, amount);
    }
    
    public boolean canAttackPlayer(final EntityPlayer other) {
        final Team var2 = this.getTeam();
        final Team var3 = other.getTeam();
        return var2 == null || !var2.isSameTeam(var3) || var2.getAllowFriendlyFire();
    }
    
    @Override
    protected void damageArmor(final float p_70675_1_) {
        this.inventory.damageArmor(p_70675_1_);
    }
    
    @Override
    public int getTotalArmorValue() {
        return this.inventory.getTotalArmorValue();
    }
    
    public float getArmorVisibility() {
        int var1 = 0;
        for (final ItemStack var5 : this.inventory.armorInventory) {
            if (var5 != null) {
                ++var1;
            }
        }
        return var1 / this.inventory.armorInventory.length;
    }
    
    @Override
    protected void damageEntity(final DamageSource p_70665_1_, float p_70665_2_) {
        if (!this.func_180431_b(p_70665_1_)) {
            if (!p_70665_1_.isUnblockable() && this.isBlocking() && p_70665_2_ > 0.0f) {
                p_70665_2_ = (1.0f + p_70665_2_) * 0.5f;
            }
            p_70665_2_ = this.applyArmorCalculations(p_70665_1_, p_70665_2_);
            final float var3;
            p_70665_2_ = (var3 = this.applyPotionDamageCalculations(p_70665_1_, p_70665_2_));
            p_70665_2_ = Math.max(p_70665_2_ - this.getAbsorptionAmount(), 0.0f);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (var3 - p_70665_2_));
            if (p_70665_2_ != 0.0f) {
                this.addExhaustion(p_70665_1_.getHungerDamage());
                final float var4 = this.getHealth();
                this.setHealth(this.getHealth() - p_70665_2_);
                this.getCombatTracker().func_94547_a(p_70665_1_, var4, p_70665_2_);
                if (p_70665_2_ < 3.4028235E37f) {
                    this.addStat(StatList.damageTakenStat, Math.round(p_70665_2_ * 10.0f));
                }
            }
        }
    }
    
    public void func_175141_a(final TileEntitySign p_175141_1_) {
    }
    
    public void func_146095_a(final CommandBlockLogic p_146095_1_) {
    }
    
    public void displayVillagerTradeGui(final IMerchant villager) {
    }
    
    public void displayGUIChest(final IInventory chestInventory) {
    }
    
    public void displayGUIHorse(final EntityHorse p_110298_1_, final IInventory p_110298_2_) {
    }
    
    public void displayGui(final IInteractionObject guiOwner) {
    }
    
    public void displayGUIBook(final ItemStack bookStack) {
    }
    
    public boolean interactWith(final Entity p_70998_1_) {
        if (this.func_175149_v()) {
            if (p_70998_1_ instanceof IInventory) {
                this.displayGUIChest((IInventory)p_70998_1_);
            }
            return false;
        }
        ItemStack var2 = this.getCurrentEquippedItem();
        final ItemStack var3 = (var2 != null) ? var2.copy() : null;
        if (!p_70998_1_.interactFirst(this)) {
            if (var2 != null && p_70998_1_ instanceof EntityLivingBase) {
                if (this.capabilities.isCreativeMode) {
                    var2 = var3;
                }
                if (var2.interactWithEntity(this, (EntityLivingBase)p_70998_1_)) {
                    if (var2.stackSize <= 0 && !this.capabilities.isCreativeMode) {
                        this.destroyCurrentEquippedItem();
                    }
                    return true;
                }
            }
            return false;
        }
        if (var2 != null && var2 == this.getCurrentEquippedItem()) {
            if (var2.stackSize <= 0 && !this.capabilities.isCreativeMode) {
                this.destroyCurrentEquippedItem();
            }
            else if (var2.stackSize < var3.stackSize && this.capabilities.isCreativeMode) {
                var2.stackSize = var3.stackSize;
            }
        }
        return true;
    }
    
    public ItemStack getCurrentEquippedItem() {
        return this.inventory.getCurrentItem();
    }
    
    public void destroyCurrentEquippedItem() {
        this.inventory.setInventorySlotContents(this.inventory.currentItem, null);
    }
    
    @Override
    public double getYOffset() {
        return -0.35;
    }
    
    public void attackTargetEntityWithCurrentItem(final Entity targetEntity) {
        if (targetEntity.canAttackWithItem() && !targetEntity.hitByEntity(this)) {
            float var2 = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
            final byte var3 = 0;
            float enchantedCrit = 0.0f;
            if (targetEntity instanceof EntityLivingBase) {
                enchantedCrit = EnchantmentHelper.func_152377_a(this.getHeldItem(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
            }
            else {
                enchantedCrit = EnchantmentHelper.func_152377_a(this.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
            }
            int knockback = var3 + EnchantmentHelper.getRespiration(this);
            if (this.isSprinting()) {
                ++knockback;
            }
            if (var2 > 0.0f || enchantedCrit > 0.0f) {
                final boolean shouldCrit = this.fallDistance > 0.0f && !this.onGround && !this.isOnLadder() && !this.isInWater() && !this.isPotionActive(Potion.blindness) && this.ridingEntity == null && targetEntity instanceof EntityLivingBase;
                if (shouldCrit && var2 > 0.0f) {
                    var2 *= 1.5f;
                }
                var2 += enchantedCrit;
                boolean var4 = false;
                final int fire = EnchantmentHelper.getFireAspectModifier(this);
                if (targetEntity instanceof EntityLivingBase && fire > 0 && !targetEntity.isBurning()) {
                    var4 = true;
                    targetEntity.setFire(1);
                }
                final double targetMotionX = targetEntity.motionX;
                final double targetMotionY = targetEntity.motionY;
                final double targetMotionZ = targetEntity.motionZ;
                final boolean hasDamaged = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(this), var2);
                if (hasDamaged) {
                    if (knockback > 0) {
                        targetEntity.addVelocity(-MathHelper.sin(this.rotationYaw * 3.1415927f / 180.0f) * knockback * 0.5f, 0.1, MathHelper.cos(this.rotationYaw * 3.1415927f / 180.0f) * knockback * 0.5f);
                    }
                    if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
                        ((EntityPlayerMP)targetEntity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(targetEntity));
                        targetEntity.velocityChanged = false;
                        targetEntity.motionX = targetMotionX;
                        targetEntity.motionY = targetMotionY;
                        targetEntity.motionZ = targetMotionZ;
                    }
                    if (shouldCrit) {
                        this.onCriticalHit(targetEntity);
                    }
                    if (enchantedCrit > 0.0f) {
                        this.onEnchantmentCritical(targetEntity);
                    }
                    if (var2 >= 18.0f) {
                        this.triggerAchievement(AchievementList.overkill);
                    }
                    this.setLastAttacker(targetEntity);
                    if (targetEntity instanceof EntityLivingBase) {
                        EnchantmentHelper.func_151384_a((EntityLivingBase)targetEntity, this);
                    }
                    EnchantmentHelper.func_151385_b(this, targetEntity);
                    final ItemStack currentItem = this.getCurrentEquippedItem();
                    Object targetEntityObject = targetEntity;
                    if (targetEntity instanceof EntityDragonPart) {
                        final IEntityMultiPart var5 = ((EntityDragonPart)targetEntity).entityDragonObj;
                        if (var5 instanceof EntityLivingBase) {
                            targetEntityObject = var5;
                        }
                    }
                    if (currentItem != null && targetEntityObject instanceof EntityLivingBase) {
                        currentItem.hitEntity((EntityLivingBase)targetEntityObject, this);
                        if (currentItem.stackSize <= 0) {
                            this.destroyCurrentEquippedItem();
                        }
                    }
                    if (targetEntity instanceof EntityLivingBase) {
                        this.addStat(StatList.damageDealtStat, Math.round(var2 * 10.0f));
                        if (fire > 0) {
                            targetEntity.setFire(fire * 4);
                        }
                    }
                    this.addExhaustion(0.3f);
                }
                else if (var4) {
                    targetEntity.extinguish();
                }
            }
        }
    }
    
    public void onCriticalHit(final Entity p_71009_1_) {
    }
    
    public void onEnchantmentCritical(final Entity p_71047_1_) {
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
    
    public boolean func_175144_cb() {
        return false;
    }
    
    public GameProfile getGameProfile() {
        return this.gameProfile;
    }
    
    public EnumStatus func_180469_a(final BlockPos p_180469_1_) {
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
            if (Math.abs(this.posX - p_180469_1_.getX()) > 3.0 || Math.abs(this.posY - p_180469_1_.getY()) > 2.0 || Math.abs(this.posZ - p_180469_1_.getZ()) > 3.0) {
                return EnumStatus.TOO_FAR_AWAY;
            }
            final double var2 = 8.0;
            final double var3 = 5.0;
            final List var4 = this.worldObj.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(p_180469_1_.getX() - var2, p_180469_1_.getY() - var3, p_180469_1_.getZ() - var2, p_180469_1_.getX() + var2, p_180469_1_.getY() + var3, p_180469_1_.getZ() + var2));
            if (!var4.isEmpty()) {
                return EnumStatus.NOT_SAFE;
            }
        }
        if (this.isRiding()) {
            this.mountEntity(null);
        }
        this.setSize(0.2f, 0.2f);
        if (this.worldObj.isBlockLoaded(p_180469_1_)) {
            final EnumFacing var5 = (EnumFacing)this.worldObj.getBlockState(p_180469_1_).getValue(BlockDirectional.AGE);
            float var6 = 0.5f;
            float var7 = 0.5f;
            switch (SwitchEnumFacing.field_179420_a[var5.ordinal()]) {
                case 1: {
                    var7 = 0.9f;
                    break;
                }
                case 2: {
                    var7 = 0.1f;
                    break;
                }
                case 3: {
                    var6 = 0.1f;
                    break;
                }
                case 4: {
                    var6 = 0.9f;
                    break;
                }
            }
            this.func_175139_a(var5);
            this.setPosition(p_180469_1_.getX() + var6, p_180469_1_.getY() + 0.6875f, p_180469_1_.getZ() + var7);
        }
        else {
            this.setPosition(p_180469_1_.getX() + 0.5f, p_180469_1_.getY() + 0.6875f, p_180469_1_.getZ() + 0.5f);
        }
        this.sleeping = true;
        this.sleepTimer = 0;
        this.playerLocation = p_180469_1_;
        final double motionX = 0.0;
        this.motionY = motionX;
        this.motionZ = motionX;
        this.motionX = motionX;
        if (!this.worldObj.isRemote) {
            this.worldObj.updateAllPlayersSleepingFlag();
        }
        return EnumStatus.OK;
    }
    
    private void func_175139_a(final EnumFacing p_175139_1_) {
        this.field_71079_bU = 0.0f;
        this.field_71089_bV = 0.0f;
        switch (SwitchEnumFacing.field_179420_a[p_175139_1_.ordinal()]) {
            case 1: {
                this.field_71089_bV = -1.8f;
                break;
            }
            case 2: {
                this.field_71089_bV = 1.8f;
                break;
            }
            case 3: {
                this.field_71079_bU = 1.8f;
                break;
            }
            case 4: {
                this.field_71079_bU = -1.8f;
                break;
            }
        }
    }
    
    public void wakeUpPlayer(final boolean p_70999_1_, final boolean updateWorldFlag, final boolean setSpawn) {
        this.setSize(0.6f, 1.8f);
        final IBlockState var4 = this.worldObj.getBlockState(this.playerLocation);
        if (this.playerLocation != null && var4.getBlock() == Blocks.bed) {
            this.worldObj.setBlockState(this.playerLocation, var4.withProperty(BlockBed.OCCUPIED_PROP, false), 4);
            BlockPos var5 = BlockBed.getSafeExitLocation(this.worldObj, this.playerLocation, 0);
            if (var5 == null) {
                var5 = this.playerLocation.offsetUp();
            }
            this.setPosition(var5.getX() + 0.5f, var5.getY() + 0.1f, var5.getZ() + 0.5f);
        }
        this.sleeping = false;
        if (!this.worldObj.isRemote && updateWorldFlag) {
            this.worldObj.updateAllPlayersSleepingFlag();
        }
        this.sleepTimer = (p_70999_1_ ? 0 : 100);
        if (setSpawn) {
            this.func_180473_a(this.playerLocation, false);
        }
    }
    
    private boolean func_175143_p() {
        return this.worldObj.getBlockState(this.playerLocation).getBlock() == Blocks.bed;
    }
    
    public static BlockPos func_180467_a(final World worldIn, final BlockPos p_180467_1_, final boolean p_180467_2_) {
        if (worldIn.getBlockState(p_180467_1_).getBlock() == Blocks.bed) {
            return BlockBed.getSafeExitLocation(worldIn, p_180467_1_, 0);
        }
        if (!p_180467_2_) {
            return null;
        }
        final Material var3 = worldIn.getBlockState(p_180467_1_).getBlock().getMaterial();
        final Material var4 = worldIn.getBlockState(p_180467_1_.offsetUp()).getBlock().getMaterial();
        final boolean var5 = !var3.isSolid() && !var3.isLiquid();
        final boolean var6 = !var4.isSolid() && !var4.isLiquid();
        return (var5 && var6) ? p_180467_1_ : null;
    }
    
    public float getBedOrientationInDegrees() {
        if (this.playerLocation != null) {
            final EnumFacing var1 = (EnumFacing)this.worldObj.getBlockState(this.playerLocation).getValue(BlockDirectional.AGE);
            switch (SwitchEnumFacing.field_179420_a[var1.ordinal()]) {
                case 1: {
                    return 90.0f;
                }
                case 2: {
                    return 270.0f;
                }
                case 3: {
                    return 0.0f;
                }
                case 4: {
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
    
    public void addChatComponentMessage(final IChatComponent p_146105_1_) {
    }
    
    public BlockPos func_180470_cg() {
        return this.spawnChunk;
    }
    
    public boolean isSpawnForced() {
        return this.spawnForced;
    }
    
    public void func_180473_a(final BlockPos p_180473_1_, final boolean p_180473_2_) {
        if (p_180473_1_ != null) {
            this.spawnChunk = p_180473_1_;
            this.spawnForced = p_180473_2_;
        }
        else {
            this.spawnChunk = null;
            this.spawnForced = false;
        }
    }
    
    public void triggerAchievement(final StatBase p_71029_1_) {
        this.addStat(p_71029_1_, 1);
    }
    
    public void addStat(final StatBase p_71064_1_, final int p_71064_2_) {
    }
    
    public void func_175145_a(final StatBase p_175145_1_) {
    }
    
    public void jump() {
        super.jump();
        this.triggerAchievement(StatList.jumpStat);
        if (this.isSprinting()) {
            this.addExhaustion(0.8f);
        }
        else {
            this.addExhaustion(0.2f);
        }
    }
    
    @Override
    public void moveEntityWithHeading(final float p_70612_1_, final float p_70612_2_) {
        final double var3 = this.posX;
        final double var4 = this.posY;
        final double var5 = this.posZ;
        if (this.capabilities.isFlying && this.ridingEntity == null) {
            final double var6 = this.motionY;
            final float var7 = this.jumpMovementFactor;
            this.jumpMovementFactor = this.capabilities.getFlySpeed() * (this.isSprinting() ? 2 : 1);
            super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
            this.motionY = var6 * 0.6;
            this.jumpMovementFactor = var7;
        }
        else {
            super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
        }
        this.addMovementStat(this.posX - var3, this.posY - var4, this.posZ - var5);
    }
    
    @Override
    public float getAIMoveSpeed() {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
    }
    
    public void addMovementStat(final double p_71000_1_, final double p_71000_3_, final double p_71000_5_) {
        if (this.ridingEntity == null) {
            if (this.isInsideOfMaterial(Material.water)) {
                final int var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (var7 > 0) {
                    this.addStat(StatList.distanceDoveStat, var7);
                    this.addExhaustion(0.015f * var7 * 0.01f);
                }
            }
            else if (this.isInWater()) {
                final int var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (var7 > 0) {
                    this.addStat(StatList.distanceSwumStat, var7);
                    this.addExhaustion(0.015f * var7 * 0.01f);
                }
            }
            else if (this.isOnLadder()) {
                if (p_71000_3_ > 0.0) {
                    this.addStat(StatList.distanceClimbedStat, (int)Math.round(p_71000_3_ * 100.0));
                }
            }
            else if (this.onGround) {
                final int var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (var7 > 0) {
                    this.addStat(StatList.distanceWalkedStat, var7);
                    if (this.isSprinting()) {
                        this.addStat(StatList.distanceSprintedStat, var7);
                        this.addExhaustion(0.099999994f * var7 * 0.01f);
                    }
                    else {
                        if (this.isSneaking()) {
                            this.addStat(StatList.distanceCrouchedStat, var7);
                        }
                        this.addExhaustion(0.01f * var7 * 0.01f);
                    }
                }
            }
            else {
                final int var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0f);
                if (var7 > 25) {
                    this.addStat(StatList.distanceFlownStat, var7);
                }
            }
        }
    }
    
    private void addMountedMovementStat(final double p_71015_1_, final double p_71015_3_, final double p_71015_5_) {
        if (this.ridingEntity != null) {
            final int var7 = Math.round(MathHelper.sqrt_double(p_71015_1_ * p_71015_1_ + p_71015_3_ * p_71015_3_ + p_71015_5_ * p_71015_5_) * 100.0f);
            if (var7 > 0) {
                if (this.ridingEntity instanceof EntityMinecart) {
                    this.addStat(StatList.distanceByMinecartStat, var7);
                    if (this.startMinecartRidingCoordinate == null) {
                        this.startMinecartRidingCoordinate = new BlockPos(this);
                    }
                    else if (this.startMinecartRidingCoordinate.distanceSq(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) >= 1000000.0) {
                        this.triggerAchievement(AchievementList.onARail);
                    }
                }
                else if (this.ridingEntity instanceof EntityBoat) {
                    this.addStat(StatList.distanceByBoatStat, var7);
                }
                else if (this.ridingEntity instanceof EntityPig) {
                    this.addStat(StatList.distanceByPigStat, var7);
                }
                else if (this.ridingEntity instanceof EntityHorse) {
                    this.addStat(StatList.distanceByHorseStat, var7);
                }
            }
        }
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        if (!this.capabilities.allowFlying) {
            if (distance >= 2.0f) {
                this.addStat(StatList.distanceFallenStat, (int)Math.round(distance * 100.0));
            }
            super.fall(distance, damageMultiplier);
        }
    }
    
    public void resetHeight() {
        if (!this.func_175149_v()) {
            super.resetHeight();
        }
    }
    
    @Override
    protected String func_146067_o(final int p_146067_1_) {
        return (p_146067_1_ > 4) ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
    }
    
    @Override
    public void onKillEntity(final EntityLivingBase entityLivingIn) {
        if (entityLivingIn instanceof IMob) {
            this.triggerAchievement(AchievementList.killEnemy);
        }
        final EntityList.EntityEggInfo var2 = EntityList.entityEggs.get(EntityList.getEntityID(entityLivingIn));
        if (var2 != null) {
            this.triggerAchievement(var2.field_151512_d);
        }
    }
    
    @Override
    public void setInWeb() {
        if (!this.capabilities.isFlying) {
            super.setInWeb();
        }
    }
    
    @Override
    public ItemStack getCurrentArmor(final int p_82169_1_) {
        return this.inventory.armorItemInSlot(p_82169_1_);
    }
    
    public void addExperience(int p_71023_1_) {
        this.addScore(p_71023_1_);
        final int var2 = Integer.MAX_VALUE - this.experienceTotal;
        if (p_71023_1_ > var2) {
            p_71023_1_ = var2;
        }
        this.experience += p_71023_1_ / this.xpBarCap();
        this.experienceTotal += p_71023_1_;
        while (this.experience >= 1.0f) {
            this.experience = (this.experience - 1.0f) * this.xpBarCap();
            this.addExperienceLevel(1);
            this.experience /= this.xpBarCap();
        }
    }
    
    public int func_175138_ci() {
        return this.field_175152_f;
    }
    
    public void func_71013_b(final int p_71013_1_) {
        this.experienceLevel -= p_71013_1_;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0f;
            this.experienceTotal = 0;
        }
        this.field_175152_f = this.rand.nextInt();
    }
    
    public void addExperienceLevel(final int p_82242_1_) {
        this.experienceLevel += p_82242_1_;
        if (this.experienceLevel < 0) {
            this.experienceLevel = 0;
            this.experience = 0.0f;
            this.experienceTotal = 0;
        }
        if (p_82242_1_ > 0 && this.experienceLevel % 5 == 0 && this.field_82249_h < this.ticksExisted - 100.0f) {
            final float var2 = (this.experienceLevel > 30) ? 1.0f : (this.experienceLevel / 30.0f);
            this.worldObj.playSoundAtEntity(this, "random.levelup", var2 * 0.75f, 1.0f);
            this.field_82249_h = this.ticksExisted;
        }
    }
    
    public int xpBarCap() {
        return (this.experienceLevel >= 30) ? (112 + (this.experienceLevel - 30) * 9) : ((this.experienceLevel >= 15) ? (37 + (this.experienceLevel - 15) * 5) : (7 + this.experienceLevel * 2));
    }
    
    public void addExhaustion(final float p_71020_1_) {
        if (!this.capabilities.disableDamage && !this.worldObj.isRemote) {
            this.foodStats.addExhaustion(p_71020_1_);
        }
    }
    
    public FoodStats getFoodStats() {
        return this.foodStats;
    }
    
    public boolean canEat(final boolean p_71043_1_) {
        return (p_71043_1_ || this.foodStats.needFood()) && !this.capabilities.disableDamage;
    }
    
    public boolean shouldHeal() {
        return this.getHealth() > 0.0f && this.getHealth() < this.getMaxHealth();
    }
    
    public void setItemInUse(final ItemStack p_71008_1_, final int p_71008_2_) {
        if (p_71008_1_ != this.itemInUse) {
            this.itemInUse = p_71008_1_;
            this.itemInUseCount = p_71008_2_;
            if (!this.worldObj.isRemote) {
                this.setEating(true);
            }
        }
    }
    
    public boolean func_175142_cm() {
        return this.capabilities.allowEdit;
    }
    
    public boolean func_175151_a(final BlockPos p_175151_1_, final EnumFacing p_175151_2_, final ItemStack p_175151_3_) {
        if (this.capabilities.allowEdit) {
            return true;
        }
        if (p_175151_3_ == null) {
            return false;
        }
        final BlockPos var4 = p_175151_1_.offset(p_175151_2_.getOpposite());
        final Block var5 = this.worldObj.getBlockState(var4).getBlock();
        return p_175151_3_.canPlaceOn(var5) || p_175151_3_.canEditBlocks();
    }
    
    @Override
    protected int getExperiencePoints(final EntityPlayer p_70693_1_) {
        if (this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
            return 0;
        }
        final int var2 = this.experienceLevel * 7;
        return (var2 > 100) ? 100 : var2;
    }
    
    @Override
    protected boolean isPlayer() {
        return true;
    }
    
    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return true;
    }
    
    public void clonePlayer(final EntityPlayer p_71049_1_, final boolean p_71049_2_) {
        if (p_71049_2_) {
            this.inventory.copyInventory(p_71049_1_.inventory);
            this.setHealth(p_71049_1_.getHealth());
            this.foodStats = p_71049_1_.foodStats;
            this.experienceLevel = p_71049_1_.experienceLevel;
            this.experienceTotal = p_71049_1_.experienceTotal;
            this.experience = p_71049_1_.experience;
            this.setScore(p_71049_1_.getScore());
            this.teleportDirection = p_71049_1_.teleportDirection;
        }
        else if (this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
            this.inventory.copyInventory(p_71049_1_.inventory);
            this.experienceLevel = p_71049_1_.experienceLevel;
            this.experienceTotal = p_71049_1_.experienceTotal;
            this.experience = p_71049_1_.experience;
            this.setScore(p_71049_1_.getScore());
        }
        this.theInventoryEnderChest = p_71049_1_.theInventoryEnderChest;
        this.getDataWatcher().updateObject(10, p_71049_1_.getDataWatcher().getWatchableObjectByte(10));
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return !this.capabilities.isFlying;
    }
    
    public void sendPlayerAbilities() {
    }
    
    public void setGameType(final WorldSettings.GameType gameType) {
    }
    
    @Override
    public String getName() {
        return this.gameProfile.getName();
    }
    
    public InventoryEnderChest getInventoryEnderChest() {
        return this.theInventoryEnderChest;
    }
    
    @Override
    public ItemStack getEquipmentInSlot(final int p_71124_1_) {
        return (p_71124_1_ == 0) ? this.inventory.getCurrentItem() : this.inventory.armorInventory[p_71124_1_ - 1];
    }
    
    @Override
    public ItemStack getHeldItem() {
        return this.inventory.getCurrentItem();
    }
    
    @Override
    public void setCurrentItemOrArmor(final int slotIn, final ItemStack itemStackIn) {
        this.inventory.armorInventory[slotIn] = itemStackIn;
    }
    
    @Override
    public boolean isInvisibleToPlayer(final EntityPlayer playerIn) {
        if (!this.isInvisible()) {
            return false;
        }
        if (playerIn.func_175149_v()) {
            return false;
        }
        final Team var2 = this.getTeam();
        return var2 == null || playerIn == null || playerIn.getTeam() != var2 || !var2.func_98297_h();
    }
    
    public abstract boolean func_175149_v();
    
    @Override
    public ItemStack[] getInventory() {
        return this.inventory.armorInventory;
    }
    
    @Override
    public boolean isPushedByWater() {
        return !this.capabilities.isFlying;
    }
    
    public Scoreboard getWorldScoreboard() {
        return this.worldObj.getScoreboard();
    }
    
    @Override
    public Team getTeam() {
        return this.getWorldScoreboard().getPlayersTeam(this.getName());
    }
    
    @Override
    public IChatComponent getDisplayName() {
        final ChatComponentText var1 = new ChatComponentText(ScorePlayerTeam.formatPlayerName(this.getTeam(), this.getName()));
        var1.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + this.getName() + " "));
        var1.getChatStyle().setChatHoverEvent(this.func_174823_aP());
        var1.getChatStyle().setInsertion(this.getName());
        return var1;
    }
    
    @Override
    public float getEyeHeight() {
        float var1 = 1.62f;
        if (this.isPlayerSleeping()) {
            var1 = 0.2f;
        }
        if (this.isSneaking()) {
            var1 -= 0.08f;
        }
        return var1;
    }
    
    @Override
    public void setAbsorptionAmount(float p_110149_1_) {
        if (p_110149_1_ < 0.0f) {
            p_110149_1_ = 0.0f;
        }
        this.getDataWatcher().updateObject(17, p_110149_1_);
    }
    
    @Override
    public float getAbsorptionAmount() {
        return this.getDataWatcher().getWatchableObjectFloat(17);
    }
    
    public static UUID getUUID(final GameProfile p_146094_0_) {
        UUID var1 = p_146094_0_.getId();
        if (var1 == null) {
            var1 = func_175147_b(p_146094_0_.getName());
        }
        return var1;
    }
    
    public static UUID func_175147_b(final String p_175147_0_) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + p_175147_0_).getBytes(Charsets.UTF_8));
    }
    
    public boolean func_175146_a(final LockCode p_175146_1_) {
        if (p_175146_1_.isEmpty()) {
            return true;
        }
        final ItemStack var2 = this.getCurrentEquippedItem();
        return var2 != null && var2.hasDisplayName() && var2.getDisplayName().equals(p_175146_1_.getLock());
    }
    
    public boolean func_175148_a(final EnumPlayerModelParts p_175148_1_) {
        return (this.getDataWatcher().getWatchableObjectByte(10) & p_175148_1_.func_179327_a()) == p_175148_1_.func_179327_a();
    }
    
    @Override
    public boolean sendCommandFeedback() {
        return MinecraftServer.getServer().worldServers[0].getGameRules().getGameRuleBooleanValue("sendCommandFeedback");
    }
    
    @Override
    public boolean func_174820_d(final int p_174820_1_, final ItemStack p_174820_2_) {
        if (p_174820_1_ >= 0 && p_174820_1_ < this.inventory.mainInventory.length) {
            this.inventory.setInventorySlotContents(p_174820_1_, p_174820_2_);
            return true;
        }
        final int var3 = p_174820_1_ - 100;
        if (var3 >= 0 && var3 < this.inventory.armorInventory.length) {
            final int var4 = var3 + 1;
            if (p_174820_2_ != null && p_174820_2_.getItem() != null) {
                if (p_174820_2_.getItem() instanceof ItemArmor) {
                    if (EntityLiving.getArmorPosition(p_174820_2_) != var4) {
                        return false;
                    }
                }
                else if (var4 != 4 || (p_174820_2_.getItem() != Items.skull && !(p_174820_2_.getItem() instanceof ItemBlock))) {
                    return false;
                }
            }
            this.inventory.setInventorySlotContents(var3 + this.inventory.mainInventory.length, p_174820_2_);
            return true;
        }
        final int var4 = p_174820_1_ - 200;
        if (var4 >= 0 && var4 < this.theInventoryEnderChest.getSizeInventory()) {
            this.theInventoryEnderChest.setInventorySlotContents(var4, p_174820_2_);
            return true;
        }
        return false;
    }
    
    public boolean func_175140_cp() {
        return this.field_175153_bG;
    }
    
    public void func_175150_k(final boolean p_175150_1_) {
        this.field_175153_bG = p_175150_1_;
    }
    
    public enum EnumChatVisibility
    {
        FULL("FULL", 0, 0, "options.chat.visibility.full"), 
        SYSTEM("SYSTEM", 1, 1, "options.chat.visibility.system"), 
        HIDDEN("HIDDEN", 2, 2, "options.chat.visibility.hidden");
        
        private static final EnumChatVisibility[] field_151432_d;
        private final int chatVisibility;
        private final String resourceKey;
        private static final EnumChatVisibility[] $VALUES;
        private static final String __OBFID = "CL_00001714";
        
        private EnumChatVisibility(final String p_i45323_1_, final int p_i45323_2_, final int p_i45323_3_, final String p_i45323_4_) {
            this.chatVisibility = p_i45323_3_;
            this.resourceKey = p_i45323_4_;
        }
        
        public int getChatVisibility() {
            return this.chatVisibility;
        }
        
        public static EnumChatVisibility getEnumChatVisibility(final int p_151426_0_) {
            return EnumChatVisibility.field_151432_d[p_151426_0_ % EnumChatVisibility.field_151432_d.length];
        }
        
        public String getResourceKey() {
            return this.resourceKey;
        }
        
        static {
            field_151432_d = new EnumChatVisibility[values().length];
            $VALUES = new EnumChatVisibility[] { EnumChatVisibility.FULL, EnumChatVisibility.SYSTEM, EnumChatVisibility.HIDDEN };
            for (final EnumChatVisibility var4 : values()) {
                EnumChatVisibility.field_151432_d[var4.chatVisibility] = var4;
            }
        }
    }
    
    public enum EnumStatus
    {
        OK("OK", 0), 
        NOT_POSSIBLE_HERE("NOT_POSSIBLE_HERE", 1), 
        NOT_POSSIBLE_NOW("NOT_POSSIBLE_NOW", 2), 
        TOO_FAR_AWAY("TOO_FAR_AWAY", 3), 
        OTHER_PROBLEM("OTHER_PROBLEM", 4), 
        NOT_SAFE("NOT_SAFE", 5);
        
        private static final EnumStatus[] $VALUES;
        private static final String __OBFID = "CL_00001712";
        
        private EnumStatus(final String p_i1751_1_, final int p_i1751_2_) {
        }
        
        static {
            $VALUES = new EnumStatus[] { EnumStatus.OK, EnumStatus.NOT_POSSIBLE_HERE, EnumStatus.NOT_POSSIBLE_NOW, EnumStatus.TOO_FAR_AWAY, EnumStatus.OTHER_PROBLEM, EnumStatus.NOT_SAFE };
        }
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_179420_a;
        private static final String __OBFID = "CL_00002188";
        
        static {
            field_179420_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_179420_a[EnumFacing.SOUTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_179420_a[EnumFacing.NORTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_179420_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_179420_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
