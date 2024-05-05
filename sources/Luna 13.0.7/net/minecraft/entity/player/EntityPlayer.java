package net.minecraft.entity.player;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.DataWatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityList.EntityEggInfo;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
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
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.FoodStats;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings.GameType;

public abstract class EntityPlayer
  extends EntityLivingBase
{
  int drinkTimer;
  public InventoryPlayer inventory = new InventoryPlayer(this);
  private InventoryEnderChest theInventoryEnderChest = new InventoryEnderChest();
  public Container inventoryContainer;
  public Container openContainer;
  protected FoodStats foodStats = new FoodStats();
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
  public PlayerCapabilities capabilities = new PlayerCapabilities();
  public int experienceLevel;
  public int experienceTotal;
  public float experience;
  private int field_175152_f;
  private ItemStack itemInUse;
  private int itemInUseCount;
  protected float speedOnGround = 0.1F;
  public float speedInAir = 0.02F;
  private int field_82249_h;
  private final GameProfile gameProfile;
  private boolean field_175153_bG = false;
  public EntityFishHook fishEntity;
  private static final String __OBFID = "CL_00001711";
  
  public EntityPlayer(World worldIn, GameProfile p_i45324_2_)
  {
    super(worldIn);
    this.entityUniqueID = getUUID(p_i45324_2_);
    this.gameProfile = p_i45324_2_;
    this.inventoryContainer = new ContainerPlayer(this.inventory, !worldIn.isRemote, this);
    this.openContainer = this.inventoryContainer;
    BlockPos var3 = worldIn.getSpawnPoint();
    setLocationAndAngles(var3.getX() + 0.5D, var3.getY() + 1, var3.getZ() + 0.5D, 0.0F, 0.0F);
    this.field_70741_aB = 180.0F;
    this.fireResistance = 20;
  }
  
  protected void applyEntityAttributes()
  {
    super.applyEntityAttributes();
    getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.10000000149011612D);
  }
  
  protected void entityInit()
  {
    super.entityInit();
    this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    this.dataWatcher.addObject(17, Float.valueOf(0.0F));
    this.dataWatcher.addObject(18, Integer.valueOf(0));
    this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
  }
  
  public ItemStack getItemInUse()
  {
    return this.itemInUse;
  }
  
  public int getItemInUseCount()
  {
    return this.itemInUseCount;
  }
  
  public boolean isUsingItem()
  {
    return this.itemInUse != null;
  }
  
  public int getItemInUseDuration()
  {
    return isUsingItem() ? this.itemInUse.getMaxItemUseDuration() - this.itemInUseCount : 0;
  }
  
  public void stopUsingItem()
  {
    if (this.itemInUse != null) {
      this.itemInUse.onPlayerStoppedUsing(this.worldObj, this, this.itemInUseCount);
    }
    clearItemInUse();
  }
  
  public void clearItemInUse()
  {
    this.itemInUse = null;
    this.itemInUseCount = 0;
    if (!this.worldObj.isRemote) {
      setEating(false);
    }
  }
  
  public boolean isBlocking()
  {
    return (isUsingItem()) && (this.itemInUse.getItem().getItemUseAction(this.itemInUse) == EnumAction.BLOCK);
  }
  
  public void onUpdate()
  {
    this.noClip = func_175149_v();
    if (func_175149_v()) {
      this.onGround = false;
    }
    if (this.itemInUse != null)
    {
      ItemStack var1 = this.inventory.getCurrentItem();
      if (var1 == this.itemInUse)
      {
        if ((this.itemInUseCount <= 25) && (this.itemInUseCount % 4 == 0)) {
          updateItemUse(var1, 5);
        }
        if ((--this.itemInUseCount == 0) && (!this.worldObj.isRemote)) {
          onItemUseFinish();
        }
      }
      else
      {
        clearItemInUse();
      }
    }
    if (this.xpCooldown > 0) {
      this.xpCooldown -= 1;
    }
    if (isPlayerSleeping())
    {
      this.sleepTimer += 1;
      if (this.sleepTimer > 100) {
        this.sleepTimer = 100;
      }
      if (!this.worldObj.isRemote) {
        if (!func_175143_p()) {
          wakeUpPlayer(true, true, false);
        } else if (this.worldObj.isDaytime()) {
          wakeUpPlayer(false, true, true);
        }
      }
    }
    else if (this.sleepTimer > 0)
    {
      this.sleepTimer += 1;
      if (this.sleepTimer >= 110) {
        this.sleepTimer = 0;
      }
    }
    super.onUpdate();
    if ((!this.worldObj.isRemote) && (this.openContainer != null) && (!this.openContainer.canInteractWith(this)))
    {
      closeScreen();
      this.openContainer = this.inventoryContainer;
    }
    if ((isBurning()) && (this.capabilities.disableDamage)) {
      extinguish();
    }
    this.field_71091_bM = this.field_71094_bP;
    this.field_71096_bN = this.field_71095_bQ;
    this.field_71097_bO = this.field_71085_bR;
    double var14 = this.posX - this.field_71094_bP;
    double var3 = this.posY - this.field_71095_bQ;
    double var5 = this.posZ - this.field_71085_bR;
    double var7 = 10.0D;
    if (var14 > var7) {
      this.field_71091_bM = (this.field_71094_bP = this.posX);
    }
    if (var5 > var7) {
      this.field_71097_bO = (this.field_71085_bR = this.posZ);
    }
    if (var3 > var7) {
      this.field_71096_bN = (this.field_71095_bQ = this.posY);
    }
    if (var14 < -var7) {
      this.field_71091_bM = (this.field_71094_bP = this.posX);
    }
    if (var5 < -var7) {
      this.field_71097_bO = (this.field_71085_bR = this.posZ);
    }
    if (var3 < -var7) {
      this.field_71096_bN = (this.field_71095_bQ = this.posY);
    }
    this.field_71094_bP += var14 * 0.25D;
    this.field_71085_bR += var5 * 0.25D;
    this.field_71095_bQ += var3 * 0.25D;
    if (this.ridingEntity == null) {
      this.startMinecartRidingCoordinate = null;
    }
    if (!this.worldObj.isRemote)
    {
      this.foodStats.onUpdate(this);
      triggerAchievement(StatList.minutesPlayedStat);
      if (isEntityAlive()) {
        triggerAchievement(StatList.timeSinceDeathStat);
      }
    }
    int var9 = 29999999;
    double var10 = MathHelper.clamp_double(this.posX, -2.9999999E7D, 2.9999999E7D);
    double var12 = MathHelper.clamp_double(this.posZ, -2.9999999E7D, 2.9999999E7D);
    if ((var10 != this.posX) || (var12 != this.posZ)) {
      setPosition(var10, this.posY, var12);
    }
  }
  
  public int getMaxInPortalTime()
  {
    return this.capabilities.disableDamage ? 0 : 80;
  }
  
  protected String getSwimSound()
  {
    return "game.player.swim";
  }
  
  protected String getSplashSound()
  {
    return "game.player.swim.splash";
  }
  
  public int getPortalCooldown()
  {
    return 10;
  }
  
  public void playSound(String name, float volume, float pitch)
  {
    this.worldObj.playSoundToNearExcept(this, name, volume, pitch);
  }
  
  protected void updateItemUse(ItemStack itemStackIn, int p_71010_2_)
  {
    if (itemStackIn.getItemUseAction() == EnumAction.DRINK) {
      playSound("random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
    }
    if (itemStackIn.getItemUseAction() == EnumAction.EAT)
    {
      for (int var3 = 0; var3 < p_71010_2_; var3++)
      {
        Vec3 var4 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
        var4 = var4.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
        var4 = var4.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
        double var5 = -this.rand.nextFloat() * 0.6D - 0.3D;
        Vec3 var7 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.3D, var5, 0.6D);
        var7 = var7.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
        var7 = var7.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
        var7 = var7.addVector(this.posX, this.posY + getEyeHeight(), this.posZ);
        if (itemStackIn.getHasSubtypes()) {
          this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, var7.xCoord, var7.yCoord, var7.zCoord, var4.xCoord, var4.yCoord + 0.05D, var4.zCoord, new int[] { Item.getIdFromItem(itemStackIn.getItem()), itemStackIn.getMetadata() });
        } else {
          this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, var7.xCoord, var7.yCoord, var7.zCoord, var4.xCoord, var4.yCoord + 0.05D, var4.zCoord, new int[] { Item.getIdFromItem(itemStackIn.getItem()) });
        }
      }
      playSound("random.eat", 0.5F + 0.5F * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    }
  }
  
  protected void onItemUseFinish()
  {
    if (this.itemInUse != null)
    {
      updateItemUse(this.itemInUse, 16);
      int var1 = this.itemInUse.stackSize;
      ItemStack var2 = this.itemInUse.onItemUseFinish(this.worldObj, this);
      if ((var2 != this.itemInUse) || ((var2 != null) && (var2.stackSize != var1)))
      {
        this.inventory.mainInventory[this.inventory.currentItem] = var2;
        if (var2.stackSize == 0) {
          this.inventory.mainInventory[this.inventory.currentItem] = null;
        }
      }
      clearItemInUse();
    }
  }
  
  public void handleHealthUpdate(byte p_70103_1_)
  {
    if (p_70103_1_ == 9) {
      onItemUseFinish();
    } else if (p_70103_1_ == 23) {
      this.field_175153_bG = false;
    } else if (p_70103_1_ == 22) {
      this.field_175153_bG = true;
    } else {
      super.handleHealthUpdate(p_70103_1_);
    }
  }
  
  protected boolean isMovementBlocked()
  {
    return (getHealth() <= 0.0F) || (isPlayerSleeping());
  }
  
  protected void closeScreen()
  {
    this.openContainer = this.inventoryContainer;
  }
  
  public void updateRidden()
  {
    if ((!this.worldObj.isRemote) && (isSneaking()))
    {
      mountEntity((Entity)null);
      setSneaking(false);
    }
    else
    {
      double var1 = this.posX;
      double var3 = this.posY;
      double var5 = this.posZ;
      float var7 = this.rotationYaw;
      float var8 = this.rotationPitch;
      super.updateRidden();
      this.prevCameraYaw = this.cameraYaw;
      this.cameraYaw = 0.0F;
      addMountedMovementStat(this.posX - var1, this.posY - var3, this.posZ - var5);
      if ((this.ridingEntity instanceof EntityPig))
      {
        this.rotationPitch = var8;
        this.rotationYaw = var7;
        this.renderYawOffset = ((EntityPig)this.ridingEntity).renderYawOffset;
      }
    }
  }
  
  public void preparePlayerToSpawn()
  {
    setSize(0.6F, 1.8F);
    super.preparePlayerToSpawn();
    setHealth(getMaxHealth());
    this.deathTime = 0;
  }
  
  protected void updateEntityActionState()
  {
    super.updateEntityActionState();
    updateArmSwingProgress();
    this.rotationYawHead = this.rotationYaw;
  }
  
  public void onLivingUpdate()
  {
    if (this.flyToggleTimer > 0) {
      this.flyToggleTimer -= 1;
    }
    if ((this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) && (this.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration")))
    {
      if ((getHealth() < getMaxHealth()) && (this.ticksExisted % 20 == 0)) {
        heal(1.0F);
      }
      if ((this.foodStats.needFood()) && (this.ticksExisted % 10 == 0)) {
        this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
      }
    }
    this.inventory.decrementAnimations();
    this.prevCameraYaw = this.cameraYaw;
    super.onLivingUpdate();
    IAttributeInstance var1 = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
    if (!this.worldObj.isRemote) {
      var1.setBaseValue(this.capabilities.getWalkSpeed());
    }
    this.jumpMovementFactor = this.speedInAir;
    if (isSprinting()) {
      this.jumpMovementFactor = ((float)(this.jumpMovementFactor + this.speedInAir * 0.3D));
    }
    setAIMoveSpeed((float)var1.getAttributeValue());
    float var2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
    float var3 = (float)(Math.atan(-this.motionY * 0.20000000298023224D) * 15.0D);
    if (var2 > 0.1F) {
      var2 = 0.1F;
    }
    if ((!this.onGround) || (getHealth() <= 0.0F)) {
      var2 = 0.0F;
    }
    if ((this.onGround) || (getHealth() <= 0.0F)) {
      var3 = 0.0F;
    }
    this.cameraYaw += (var2 - this.cameraYaw) * 0.4F;
    this.cameraPitch += (var3 - this.cameraPitch) * 0.8F;
    if ((getHealth() > 0.0F) && (!func_175149_v()))
    {
      AxisAlignedBB var4 = null;
      if ((this.ridingEntity != null) && (!this.ridingEntity.isDead)) {
        var4 = getEntityBoundingBox().union(this.ridingEntity.getEntityBoundingBox()).expand(1.0D, 0.0D, 1.0D);
      } else {
        var4 = getEntityBoundingBox().expand(1.0D, 0.5D, 1.0D);
      }
      List var5 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, var4);
      for (int var6 = 0; var6 < var5.size(); var6++)
      {
        Entity var7 = (Entity)var5.get(var6);
        if (!var7.isDead) {
          collideWithPlayer(var7);
        }
      }
    }
  }
  
  private void collideWithPlayer(Entity p_71044_1_)
  {
    p_71044_1_.onCollideWithPlayer(this);
  }
  
  public int getScore()
  {
    return this.dataWatcher.getWatchableObjectInt(18);
  }
  
  public void setScore(int p_85040_1_)
  {
    this.dataWatcher.updateObject(18, Integer.valueOf(p_85040_1_));
  }
  
  public void addScore(int p_85039_1_)
  {
    int var2 = getScore();
    this.dataWatcher.updateObject(18, Integer.valueOf(var2 + p_85039_1_));
  }
  
  public void onDeath(DamageSource cause)
  {
    super.onDeath(cause);
    setSize(0.2F, 0.2F);
    setPosition(this.posX, this.posY, this.posZ);
    this.motionY = 0.10000000149011612D;
    if (getName().equals("Notch")) {
      func_146097_a(new ItemStack(Items.apple, 1), true, false);
    }
    if (!this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
      this.inventory.dropAllItems();
    }
    if (cause != null)
    {
      this.motionX = (-MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * 3.1415927F / 180.0F) * 0.1F);
      this.motionZ = (-MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * 3.1415927F / 180.0F) * 0.1F);
    }
    else
    {
      this.motionX = (this.motionZ = 0.0D);
    }
    triggerAchievement(StatList.deathsStat);
    func_175145_a(StatList.timeSinceDeathStat);
  }
  
  protected String getHurtSound()
  {
    return "game.player.hurt";
  }
  
  protected String getDeathSound()
  {
    return "game.player.die";
  }
  
  public void addToPlayerScore(Entity entityIn, int amount)
  {
    addScore(amount);
    Collection var3 = getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.totalKillCount);
    if ((entityIn instanceof EntityPlayer))
    {
      triggerAchievement(StatList.playerKillsStat);
      var3.addAll(getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.playerKillCount));
      var3.addAll(func_175137_e(entityIn));
    }
    else
    {
      triggerAchievement(StatList.mobKillsStat);
    }
    Iterator var4 = var3.iterator();
    while (var4.hasNext())
    {
      ScoreObjective var5 = (ScoreObjective)var4.next();
      Score var6 = getWorldScoreboard().getValueFromObjective(getName(), var5);
      var6.func_96648_a();
    }
  }
  
  private Collection func_175137_e(Entity p_175137_1_)
  {
    ScorePlayerTeam var2 = getWorldScoreboard().getPlayersTeam(getName());
    if (var2 != null)
    {
      int var3 = var2.func_178775_l().func_175746_b();
      if ((var3 >= 0) && (var3 < IScoreObjectiveCriteria.field_178793_i.length))
      {
        Iterator var4 = getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.field_178793_i[var3]).iterator();
        while (var4.hasNext())
        {
          ScoreObjective var5 = (ScoreObjective)var4.next();
          Score var6 = getWorldScoreboard().getValueFromObjective(p_175137_1_.getName(), var5);
          var6.func_96648_a();
        }
      }
    }
    ScorePlayerTeam var7 = getWorldScoreboard().getPlayersTeam(p_175137_1_.getName());
    if (var7 != null)
    {
      int var8 = var7.func_178775_l().func_175746_b();
      if ((var8 >= 0) && (var8 < IScoreObjectiveCriteria.field_178792_h.length)) {
        return getWorldScoreboard().func_96520_a(IScoreObjectiveCriteria.field_178792_h[var8]);
      }
    }
    return Lists.newArrayList();
  }
  
  public EntityItem dropOneItem(boolean p_71040_1_)
  {
    return func_146097_a(this.inventory.decrStackSize(this.inventory.currentItem, (p_71040_1_) && (this.inventory.getCurrentItem() != null) ? this.inventory.getCurrentItem().stackSize : 1), false, true);
  }
  
  public EntityItem dropPlayerItemWithRandomChoice(ItemStack itemStackIn, boolean p_71019_2_)
  {
    return func_146097_a(itemStackIn, false, false);
  }
  
  public EntityItem func_146097_a(ItemStack p_146097_1_, boolean p_146097_2_, boolean p_146097_3_)
  {
    if (p_146097_1_ == null) {
      return null;
    }
    if (p_146097_1_.stackSize == 0) {
      return null;
    }
    double var4 = this.posY - 0.30000001192092896D + getEyeHeight();
    EntityItem var6 = new EntityItem(this.worldObj, this.posX, var4, this.posZ, p_146097_1_);
    var6.setPickupDelay(40);
    if (p_146097_3_) {
      var6.setThrower(getName());
    }
    if (p_146097_2_)
    {
      float var7 = this.rand.nextFloat() * 0.5F;
      float var8 = this.rand.nextFloat() * 3.1415927F * 2.0F;
      var6.motionX = (-MathHelper.sin(var8) * var7);
      var6.motionZ = (MathHelper.cos(var8) * var7);
      var6.motionY = 0.20000000298023224D;
    }
    else
    {
      float var7 = 0.3F;
      var6.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * var7);
      var6.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * var7);
      var6.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F) * var7 + 0.1F);
      float var8 = this.rand.nextFloat() * 3.1415927F * 2.0F;
      var7 = 0.02F * this.rand.nextFloat();
      var6.motionX += Math.cos(var8) * var7;
      var6.motionY += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
      var6.motionZ += Math.sin(var8) * var7;
    }
    joinEntityItemWithWorld(var6);
    if (p_146097_3_) {
      triggerAchievement(StatList.dropStat);
    }
    return var6;
  }
  
  protected void joinEntityItemWithWorld(EntityItem p_71012_1_)
  {
    this.worldObj.spawnEntityInWorld(p_71012_1_);
  }
  
  public float func_180471_a(Block p_180471_1_)
  {
    float var2 = this.inventory.getStrVsBlock(p_180471_1_);
    if (var2 > 1.0F)
    {
      int var3 = EnchantmentHelper.getEfficiencyModifier(this);
      ItemStack var4 = this.inventory.getCurrentItem();
      if ((var3 > 0) && (var4 != null)) {
        var2 += var3 * var3 + 1;
      }
    }
    if (isPotionActive(Potion.digSpeed)) {
      var2 *= (1.0F + (getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F);
    }
    if (isPotionActive(Potion.digSlowdown))
    {
      float var5 = 1.0F;
      switch (getActivePotionEffect(Potion.digSlowdown).getAmplifier())
      {
      case 0: 
        var5 = 0.3F;
        break;
      case 1: 
        var5 = 0.09F;
        break;
      case 2: 
        var5 = 0.0027F;
        break;
      case 3: 
      default: 
        var5 = 8.1E-4F;
      }
      var2 *= var5;
    }
    if ((isInsideOfMaterial(Material.water)) && (!EnchantmentHelper.getAquaAffinityModifier(this))) {
      var2 /= 5.0F;
    }
    if (!this.onGround) {
      var2 /= 5.0F;
    }
    return var2;
  }
  
  public boolean canHarvestBlock(Block p_146099_1_)
  {
    return this.inventory.func_146025_b(p_146099_1_);
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund)
  {
    super.readEntityFromNBT(tagCompund);
    this.entityUniqueID = getUUID(this.gameProfile);
    NBTTagList var2 = tagCompund.getTagList("Inventory", 10);
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
    setScore(tagCompund.getInteger("Score"));
    if (this.sleeping)
    {
      this.playerLocation = new BlockPos(this);
      wakeUpPlayer(true, true, false);
    }
    if ((tagCompund.hasKey("SpawnX", 99)) && (tagCompund.hasKey("SpawnY", 99)) && (tagCompund.hasKey("SpawnZ", 99)))
    {
      this.spawnChunk = new BlockPos(tagCompund.getInteger("SpawnX"), tagCompund.getInteger("SpawnY"), tagCompund.getInteger("SpawnZ"));
      this.spawnForced = tagCompund.getBoolean("SpawnForced");
    }
    this.foodStats.readNBT(tagCompund);
    this.capabilities.readCapabilitiesFromNBT(tagCompund);
    if (tagCompund.hasKey("EnderItems", 9))
    {
      NBTTagList var3 = tagCompund.getTagList("EnderItems", 10);
      this.theInventoryEnderChest.loadInventoryFromNBT(var3);
    }
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound)
  {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
    tagCompound.setInteger("SelectedItemSlot", this.inventory.currentItem);
    tagCompound.setBoolean("Sleeping", this.sleeping);
    tagCompound.setShort("SleepTimer", (short)this.sleepTimer);
    tagCompound.setFloat("XpP", this.experience);
    tagCompound.setInteger("XpLevel", this.experienceLevel);
    tagCompound.setInteger("XpTotal", this.experienceTotal);
    tagCompound.setInteger("XpSeed", this.field_175152_f);
    tagCompound.setInteger("Score", getScore());
    if (this.spawnChunk != null)
    {
      tagCompound.setInteger("SpawnX", this.spawnChunk.getX());
      tagCompound.setInteger("SpawnY", this.spawnChunk.getY());
      tagCompound.setInteger("SpawnZ", this.spawnChunk.getZ());
      tagCompound.setBoolean("SpawnForced", this.spawnForced);
    }
    this.foodStats.writeNBT(tagCompound);
    this.capabilities.writeCapabilitiesToNBT(tagCompound);
    tagCompound.setTag("EnderItems", this.theInventoryEnderChest.saveInventoryToNBT());
    ItemStack var2 = this.inventory.getCurrentItem();
    if ((var2 != null) && (var2.getItem() != null)) {
      tagCompound.setTag("SelectedItem", var2.writeToNBT(new NBTTagCompound()));
    }
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount)
  {
    if (func_180431_b(source)) {
      return false;
    }
    if ((this.capabilities.disableDamage) && (!source.canHarmInCreative())) {
      return false;
    }
    this.entityAge = 0;
    if (getHealth() <= 0.0F) {
      return false;
    }
    if ((isPlayerSleeping()) && (!this.worldObj.isRemote)) {
      wakeUpPlayer(true, true, false);
    }
    if (source.isDifficultyScaled())
    {
      if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
        amount = 0.0F;
      }
      if (this.worldObj.getDifficulty() == EnumDifficulty.EASY) {
        amount = amount / 2.0F + 1.0F;
      }
      if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
        amount = amount * 3.0F / 2.0F;
      }
    }
    if (amount == 0.0F) {
      return false;
    }
    Entity var3 = source.getEntity();
    if (((var3 instanceof EntityArrow)) && (((EntityArrow)var3).shootingEntity != null)) {
      var3 = ((EntityArrow)var3).shootingEntity;
    }
    return super.attackEntityFrom(source, amount);
  }
  
  public boolean canAttackPlayer(EntityPlayer other)
  {
    Team var2 = getTeam();
    Team var3 = other.getTeam();
    return !var2.isSameTeam(var3) ? true : var2 == null ? true : var2.getAllowFriendlyFire();
  }
  
  protected void damageArmor(float p_70675_1_)
  {
    this.inventory.damageArmor(p_70675_1_);
  }
  
  public int getTotalArmorValue()
  {
    return this.inventory.getTotalArmorValue();
  }
  
  public float getArmorVisibility()
  {
    int var1 = 0;
    ItemStack[] var2 = this.inventory.armorInventory;
    int var3 = var2.length;
    for (int var4 = 0; var4 < var3; var4++)
    {
      ItemStack var5 = var2[var4];
      if (var5 != null) {
        var1++;
      }
    }
    return var1 / this.inventory.armorInventory.length;
  }
  
  protected void damageEntity(DamageSource p_70665_1_, float p_70665_2_)
  {
    if (!func_180431_b(p_70665_1_))
    {
      if ((!p_70665_1_.isUnblockable()) && (isBlocking()) && (p_70665_2_ > 0.0F)) {
        p_70665_2_ = (1.0F + p_70665_2_) * 0.5F;
      }
      p_70665_2_ = applyArmorCalculations(p_70665_1_, p_70665_2_);
      p_70665_2_ = applyPotionDamageCalculations(p_70665_1_, p_70665_2_);
      float var3 = p_70665_2_;
      p_70665_2_ = Math.max(p_70665_2_ - getAbsorptionAmount(), 0.0F);
      setAbsorptionAmount(getAbsorptionAmount() - (var3 - p_70665_2_));
      if (p_70665_2_ != 0.0F)
      {
        addExhaustion(p_70665_1_.getHungerDamage());
        float var4 = getHealth();
        setHealth(getHealth() - p_70665_2_);
        getCombatTracker().func_94547_a(p_70665_1_, var4, p_70665_2_);
        if (p_70665_2_ < 3.4028235E37F) {
          addStat(StatList.damageTakenStat, Math.round(p_70665_2_ * 10.0F));
        }
      }
    }
  }
  
  public void func_175141_a(TileEntitySign p_175141_1_) {}
  
  public void func_146095_a(CommandBlockLogic p_146095_1_) {}
  
  public void displayVillagerTradeGui(IMerchant villager) {}
  
  public void displayGUIChest(IInventory chestInventory) {}
  
  public void displayGUIHorse(EntityHorse p_110298_1_, IInventory p_110298_2_) {}
  
  public void displayGui(IInteractionObject guiOwner) {}
  
  public void displayGUIBook(ItemStack bookStack) {}
  
  public boolean interactWith(Entity p_70998_1_)
  {
    if (func_175149_v())
    {
      if ((p_70998_1_ instanceof IInventory)) {
        displayGUIChest((IInventory)p_70998_1_);
      }
      return false;
    }
    ItemStack var2 = getCurrentEquippedItem();
    ItemStack var3 = var2 != null ? var2.copy() : null;
    if (!p_70998_1_.interactFirst(this))
    {
      if ((var2 != null) && ((p_70998_1_ instanceof EntityLivingBase)))
      {
        if (this.capabilities.isCreativeMode) {
          var2 = var3;
        }
        if (var2.interactWithEntity(this, (EntityLivingBase)p_70998_1_))
        {
          if ((var2.stackSize <= 0) && (!this.capabilities.isCreativeMode)) {
            destroyCurrentEquippedItem();
          }
          return true;
        }
      }
      return false;
    }
    if ((var2 != null) && (var2 == getCurrentEquippedItem())) {
      if ((var2.stackSize <= 0) && (!this.capabilities.isCreativeMode)) {
        destroyCurrentEquippedItem();
      } else if ((var2.stackSize < var3.stackSize) && (this.capabilities.isCreativeMode)) {
        var2.stackSize = var3.stackSize;
      }
    }
    return true;
  }
  
  public ItemStack getCurrentEquippedItem()
  {
    return this.inventory.getCurrentItem();
  }
  
  public void destroyCurrentEquippedItem()
  {
    this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack)null);
  }
  
  public double getYOffset()
  {
    return -0.35D;
  }
  
  public void attackTargetEntityWithCurrentItem(Entity targetEntity)
  {
    if ((targetEntity.canAttackWithItem()) && 
      (!targetEntity.hitByEntity(this)))
    {
      float var2 = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
      byte var3 = 0;
      float var4 = 0.0F;
      if ((targetEntity instanceof EntityLivingBase)) {
        var4 = EnchantmentHelper.func_152377_a(getHeldItem(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
      } else {
        var4 = EnchantmentHelper.func_152377_a(getHeldItem(), EnumCreatureAttribute.UNDEFINED);
      }
      int var18 = var3 + EnchantmentHelper.getRespiration(this);
      if ((var2 > 0.0F) || (var4 > 0.0F))
      {
        boolean var5 = (this.fallDistance > 0.0F) && (!this.onGround) && (!isOnLadder()) && (!isInWater()) && (!isPotionActive(Potion.blindness)) && (this.ridingEntity == null) && ((targetEntity instanceof EntityLivingBase));
        if ((var5) && (var2 > 0.0F)) {
          var2 *= 1.5F;
        }
        var2 += var4;
        boolean var6 = false;
        int var7 = EnchantmentHelper.getFireAspectModifier(this);
        if (((targetEntity instanceof EntityLivingBase)) && (var7 > 0) && (!targetEntity.isBurning()))
        {
          var6 = true;
          targetEntity.setFire(1);
        }
        double var8 = targetEntity.motionX;
        double var10 = targetEntity.motionY;
        double var12 = targetEntity.motionZ;
        boolean var14 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(this), var2);
        if (var14)
        {
          if (var18 > 0)
          {
            targetEntity.addVelocity(-MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F) * var18 * 0.5F, 0.1D, MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F) * var18 * 0.5F);
            this.motionX *= 0.6D;
            this.motionZ *= 0.6D;
            setSprinting(false);
          }
          if (((targetEntity instanceof EntityPlayerMP)) && (targetEntity.velocityChanged))
          {
            ((EntityPlayerMP)targetEntity).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(targetEntity));
            targetEntity.velocityChanged = false;
            targetEntity.motionX = var8;
            targetEntity.motionY = var10;
            targetEntity.motionZ = var12;
          }
          if (var5) {
            onCriticalHit(targetEntity);
          }
          if (var4 > 0.0F) {
            onEnchantmentCritical(targetEntity);
          }
          if (var2 >= 18.0F) {
            triggerAchievement(AchievementList.overkill);
          }
          setLastAttacker(targetEntity);
          if ((targetEntity instanceof EntityLivingBase)) {
            EnchantmentHelper.func_151384_a((EntityLivingBase)targetEntity, this);
          }
          EnchantmentHelper.func_151385_b(this, targetEntity);
          ItemStack var15 = getCurrentEquippedItem();
          Object var16 = targetEntity;
          if ((targetEntity instanceof EntityDragonPart))
          {
            IEntityMultiPart var17 = ((EntityDragonPart)targetEntity).entityDragonObj;
            if ((var17 instanceof EntityLivingBase)) {
              var16 = (EntityLivingBase)var17;
            }
          }
          if ((var15 != null) && ((var16 instanceof EntityLivingBase)))
          {
            var15.hitEntity((EntityLivingBase)var16, this);
            if (var15.stackSize <= 0) {
              destroyCurrentEquippedItem();
            }
          }
          if ((targetEntity instanceof EntityLivingBase))
          {
            addStat(StatList.damageDealtStat, Math.round(var2 * 10.0F));
            if (var7 > 0) {
              targetEntity.setFire(var7 * 4);
            }
          }
          addExhaustion(0.3F);
        }
        else if (var6)
        {
          targetEntity.extinguish();
        }
      }
    }
  }
  
  public void onCriticalHit(Entity p_71009_1_) {}
  
  public void onEnchantmentCritical(Entity p_71047_1_) {}
  
  public void respawnPlayer() {}
  
  public void setDead()
  {
    super.setDead();
    this.inventoryContainer.onContainerClosed(this);
    if (this.openContainer != null) {
      this.openContainer.onContainerClosed(this);
    }
  }
  
  public boolean isEntityInsideOpaqueBlock()
  {
    return (!this.sleeping) && (super.isEntityInsideOpaqueBlock());
  }
  
  public boolean func_175144_cb()
  {
    return false;
  }
  
  public GameProfile getGameProfile()
  {
    return this.gameProfile;
  }
  
  public EnumStatus func_180469_a(BlockPos p_180469_1_)
  {
    if (!this.worldObj.isRemote)
    {
      if ((isPlayerSleeping()) || (!isEntityAlive())) {
        return EnumStatus.OTHER_PROBLEM;
      }
      if (!this.worldObj.provider.isSurfaceWorld()) {
        return EnumStatus.NOT_POSSIBLE_HERE;
      }
      if (this.worldObj.isDaytime()) {
        return EnumStatus.NOT_POSSIBLE_NOW;
      }
      if ((Math.abs(this.posX - p_180469_1_.getX()) > 3.0D) || (Math.abs(this.posY - p_180469_1_.getY()) > 2.0D) || (Math.abs(this.posZ - p_180469_1_.getZ()) > 3.0D)) {
        return EnumStatus.TOO_FAR_AWAY;
      }
      double var2 = 8.0D;
      double var4 = 5.0D;
      List var6 = this.worldObj.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(p_180469_1_.getX() - var2, p_180469_1_.getY() - var4, p_180469_1_.getZ() - var2, p_180469_1_.getX() + var2, p_180469_1_.getY() + var4, p_180469_1_.getZ() + var2));
      if (!var6.isEmpty()) {
        return EnumStatus.NOT_SAFE;
      }
    }
    if (isRiding()) {
      mountEntity((Entity)null);
    }
    setSize(0.2F, 0.2F);
    if (this.worldObj.isBlockLoaded(p_180469_1_))
    {
      EnumFacing var7 = (EnumFacing)this.worldObj.getBlockState(p_180469_1_).getValue(BlockDirectional.AGE);
      float var3 = 0.5F;
      float var8 = 0.5F;
      switch (SwitchEnumFacing.field_179420_a[var7.ordinal()])
      {
      case 1: 
        var8 = 0.9F;
        break;
      case 2: 
        var8 = 0.1F;
        break;
      case 3: 
        var3 = 0.1F;
        break;
      case 4: 
        var3 = 0.9F;
      }
      func_175139_a(var7);
      setPosition(p_180469_1_.getX() + var3, p_180469_1_.getY() + 0.6875F, p_180469_1_.getZ() + var8);
    }
    else
    {
      setPosition(p_180469_1_.getX() + 0.5F, p_180469_1_.getY() + 0.6875F, p_180469_1_.getZ() + 0.5F);
    }
    this.sleeping = true;
    this.sleepTimer = 0;
    this.playerLocation = p_180469_1_;
    this.motionX = (this.motionZ = this.motionY = 0.0D);
    if (!this.worldObj.isRemote) {
      this.worldObj.updateAllPlayersSleepingFlag();
    }
    return EnumStatus.OK;
  }
  
  private void func_175139_a(EnumFacing p_175139_1_)
  {
    this.field_71079_bU = 0.0F;
    this.field_71089_bV = 0.0F;
    switch (SwitchEnumFacing.field_179420_a[p_175139_1_.ordinal()])
    {
    case 1: 
      this.field_71089_bV = -1.8F;
      break;
    case 2: 
      this.field_71089_bV = 1.8F;
      break;
    case 3: 
      this.field_71079_bU = 1.8F;
      break;
    case 4: 
      this.field_71079_bU = -1.8F;
    }
  }
  
  public void wakeUpPlayer(boolean p_70999_1_, boolean updateWorldFlag, boolean setSpawn)
  {
    setSize(0.6F, 1.8F);
    IBlockState var4 = this.worldObj.getBlockState(this.playerLocation);
    if ((this.playerLocation != null) && (var4.getBlock() == Blocks.bed))
    {
      this.worldObj.setBlockState(this.playerLocation, var4.withProperty(BlockBed.OCCUPIED_PROP, Boolean.valueOf(false)), 4);
      BlockPos var5 = BlockBed.getSafeExitLocation(this.worldObj, this.playerLocation, 0);
      if (var5 == null) {
        var5 = this.playerLocation.offsetUp();
      }
      setPosition(var5.getX() + 0.5F, var5.getY() + 0.1F, var5.getZ() + 0.5F);
    }
    this.sleeping = false;
    if ((!this.worldObj.isRemote) && (updateWorldFlag)) {
      this.worldObj.updateAllPlayersSleepingFlag();
    }
    this.sleepTimer = (p_70999_1_ ? 0 : 100);
    if (setSpawn) {
      func_180473_a(this.playerLocation, false);
    }
  }
  
  private boolean func_175143_p()
  {
    return this.worldObj.getBlockState(this.playerLocation).getBlock() == Blocks.bed;
  }
  
  public static BlockPos func_180467_a(World worldIn, BlockPos p_180467_1_, boolean p_180467_2_)
  {
    if (worldIn.getBlockState(p_180467_1_).getBlock() != Blocks.bed)
    {
      if (!p_180467_2_) {
        return null;
      }
      Material var3 = worldIn.getBlockState(p_180467_1_).getBlock().getMaterial();
      Material var4 = worldIn.getBlockState(p_180467_1_.offsetUp()).getBlock().getMaterial();
      boolean var5 = (!var3.isSolid()) && (!var3.isLiquid());
      boolean var6 = (!var4.isSolid()) && (!var4.isLiquid());
      return (var5) && (var6) ? p_180467_1_ : null;
    }
    return BlockBed.getSafeExitLocation(worldIn, p_180467_1_, 0);
  }
  
  public float getBedOrientationInDegrees()
  {
    if (this.playerLocation != null)
    {
      EnumFacing var1 = (EnumFacing)this.worldObj.getBlockState(this.playerLocation).getValue(BlockDirectional.AGE);
      switch (SwitchEnumFacing.field_179420_a[var1.ordinal()])
      {
      case 1: 
        return 90.0F;
      case 2: 
        return 270.0F;
      case 3: 
        return 0.0F;
      case 4: 
        return 180.0F;
      }
    }
    return 0.0F;
  }
  
  public boolean isPlayerSleeping()
  {
    return this.sleeping;
  }
  
  public boolean isPlayerFullyAsleep()
  {
    return (this.sleeping) && (this.sleepTimer >= 100);
  }
  
  public int getSleepTimer()
  {
    return this.sleepTimer;
  }
  
  public void addChatComponentMessage(IChatComponent p_146105_1_) {}
  
  public BlockPos func_180470_cg()
  {
    return this.spawnChunk;
  }
  
  public boolean isSpawnForced()
  {
    return this.spawnForced;
  }
  
  public void func_180473_a(BlockPos p_180473_1_, boolean p_180473_2_)
  {
    if (p_180473_1_ != null)
    {
      this.spawnChunk = p_180473_1_;
      this.spawnForced = p_180473_2_;
    }
    else
    {
      this.spawnChunk = null;
      this.spawnForced = false;
    }
  }
  
  public void triggerAchievement(StatBase p_71029_1_)
  {
    addStat(p_71029_1_, 1);
  }
  
  public void addStat(StatBase p_71064_1_, int p_71064_2_) {}
  
  public void func_175145_a(StatBase p_175145_1_) {}
  
  public void jump()
  {
    super.jump();
    triggerAchievement(StatList.jumpStat);
    if (isSprinting()) {
      addExhaustion(0.8F);
    } else {
      addExhaustion(0.2F);
    }
  }
  
  public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
  {
    double var3 = this.posX;
    double var5 = this.posY;
    double var7 = this.posZ;
    if ((this.capabilities.isFlying) && (this.ridingEntity == null))
    {
      double var9 = this.motionY;
      float var11 = this.jumpMovementFactor;
      this.jumpMovementFactor = (this.capabilities.getFlySpeed() * (isSprinting() ? 2 : 1));
      super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
      this.motionY = (var9 * 0.6D);
      this.jumpMovementFactor = var11;
    }
    else
    {
      super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
    }
    addMovementStat(this.posX - var3, this.posY - var5, this.posZ - var7);
  }
  
  public float getAIMoveSpeed()
  {
    return (float)getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
  }
  
  public void addMovementStat(double p_71000_1_, double p_71000_3_, double p_71000_5_)
  {
    if (this.ridingEntity == null) {
      if (isInsideOfMaterial(Material.water))
      {
        int var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0F);
        if (var7 > 0)
        {
          addStat(StatList.distanceDoveStat, var7);
          addExhaustion(0.015F * var7 * 0.01F);
        }
      }
      else if (isInWater())
      {
        int var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
        if (var7 > 0)
        {
          addStat(StatList.distanceSwumStat, var7);
          addExhaustion(0.015F * var7 * 0.01F);
        }
      }
      else if (isOnLadder())
      {
        if (p_71000_3_ > 0.0D) {
          addStat(StatList.distanceClimbedStat, (int)Math.round(p_71000_3_ * 100.0D));
        }
      }
      else if (this.onGround)
      {
        int var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
        if (var7 > 0)
        {
          addStat(StatList.distanceWalkedStat, var7);
          if (isSprinting())
          {
            addStat(StatList.distanceSprintedStat, var7);
            addExhaustion(0.099999994F * var7 * 0.01F);
          }
          else
          {
            if (isSneaking()) {
              addStat(StatList.distanceCrouchedStat, var7);
            }
            addExhaustion(0.01F * var7 * 0.01F);
          }
        }
      }
      else
      {
        int var7 = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
        if (var7 > 25) {
          addStat(StatList.distanceFlownStat, var7);
        }
      }
    }
  }
  
  private void addMountedMovementStat(double p_71015_1_, double p_71015_3_, double p_71015_5_)
  {
    if (this.ridingEntity != null)
    {
      int var7 = Math.round(MathHelper.sqrt_double(p_71015_1_ * p_71015_1_ + p_71015_3_ * p_71015_3_ + p_71015_5_ * p_71015_5_) * 100.0F);
      if (var7 > 0) {
        if ((this.ridingEntity instanceof EntityMinecart))
        {
          addStat(StatList.distanceByMinecartStat, var7);
          if (this.startMinecartRidingCoordinate == null) {
            this.startMinecartRidingCoordinate = new BlockPos(this);
          } else if (this.startMinecartRidingCoordinate.distanceSq(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) >= 1000000.0D) {
            triggerAchievement(AchievementList.onARail);
          }
        }
        else if ((this.ridingEntity instanceof EntityBoat))
        {
          addStat(StatList.distanceByBoatStat, var7);
        }
        else if ((this.ridingEntity instanceof EntityPig))
        {
          addStat(StatList.distanceByPigStat, var7);
        }
        else if ((this.ridingEntity instanceof EntityHorse))
        {
          addStat(StatList.distanceByHorseStat, var7);
        }
      }
    }
  }
  
  public void fall(float distance, float damageMultiplier)
  {
    if (!this.capabilities.allowFlying)
    {
      if (distance >= 2.0F) {
        addStat(StatList.distanceFallenStat, (int)Math.round(distance * 100.0D));
      }
      super.fall(distance, damageMultiplier);
    }
  }
  
  protected void resetHeight()
  {
    if (!func_175149_v()) {
      super.resetHeight();
    }
  }
  
  protected String func_146067_o(int p_146067_1_)
  {
    return p_146067_1_ > 4 ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
  }
  
  public void onKillEntity(EntityLivingBase entityLivingIn)
  {
    if ((entityLivingIn instanceof IMob)) {
      triggerAchievement(AchievementList.killEnemy);
    }
    EntityList.EntityEggInfo var2 = (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(EntityList.getEntityID(entityLivingIn)));
    if (var2 != null) {
      triggerAchievement(var2.field_151512_d);
    }
  }
  
  public void setInWeb()
  {
    if (!this.capabilities.isFlying) {
      super.setInWeb();
    }
  }
  
  public ItemStack getCurrentArmor(int p_82169_1_)
  {
    return this.inventory.armorItemInSlot(p_82169_1_);
  }
  
  public void addExperience(int p_71023_1_)
  {
    addScore(p_71023_1_);
    int var2 = Integer.MAX_VALUE - this.experienceTotal;
    if (p_71023_1_ > var2) {
      p_71023_1_ = var2;
    }
    this.experience += p_71023_1_ / xpBarCap();
    for (this.experienceTotal += p_71023_1_; this.experience >= 1.0F; this.experience /= xpBarCap())
    {
      this.experience = ((this.experience - 1.0F) * xpBarCap());
      addExperienceLevel(1);
    }
  }
  
  public int func_175138_ci()
  {
    return this.field_175152_f;
  }
  
  public void func_71013_b(int p_71013_1_)
  {
    this.experienceLevel -= p_71013_1_;
    if (this.experienceLevel < 0)
    {
      this.experienceLevel = 0;
      this.experience = 0.0F;
      this.experienceTotal = 0;
    }
    this.field_175152_f = this.rand.nextInt();
  }
  
  public void addExperienceLevel(int p_82242_1_)
  {
    this.experienceLevel += p_82242_1_;
    if (this.experienceLevel < 0)
    {
      this.experienceLevel = 0;
      this.experience = 0.0F;
      this.experienceTotal = 0;
    }
    if ((p_82242_1_ > 0) && (this.experienceLevel % 5 == 0) && (this.field_82249_h < this.ticksExisted - 100.0F))
    {
      float var2 = this.experienceLevel > 30 ? 1.0F : this.experienceLevel / 30.0F;
      this.worldObj.playSoundAtEntity(this, "random.levelup", var2 * 0.75F, 1.0F);
      this.field_82249_h = this.ticksExisted;
    }
  }
  
  public int xpBarCap()
  {
    return this.experienceLevel >= 15 ? 37 + (this.experienceLevel - 15) * 5 : this.experienceLevel >= 30 ? 112 + (this.experienceLevel - 30) * 9 : 7 + this.experienceLevel * 2;
  }
  
  public void addExhaustion(float p_71020_1_)
  {
    if ((!this.capabilities.disableDamage) && 
      (!this.worldObj.isRemote)) {
      this.foodStats.addExhaustion(p_71020_1_);
    }
  }
  
  public FoodStats getFoodStats()
  {
    return this.foodStats;
  }
  
  public boolean canEat(boolean p_71043_1_)
  {
    return ((p_71043_1_) || (this.foodStats.needFood())) && (!this.capabilities.disableDamage);
  }
  
  public boolean shouldHeal()
  {
    return (getHealth() > 0.0F) && (getHealth() < getMaxHealth());
  }
  
  public void setItemInUse(ItemStack p_71008_1_, int p_71008_2_)
  {
    if (p_71008_1_ != this.itemInUse)
    {
      this.itemInUse = p_71008_1_;
      this.itemInUseCount = p_71008_2_;
      if (!this.worldObj.isRemote) {
        setEating(true);
      }
    }
  }
  
  public boolean func_175142_cm()
  {
    return this.capabilities.allowEdit;
  }
  
  public boolean func_175151_a(BlockPos p_175151_1_, EnumFacing p_175151_2_, ItemStack p_175151_3_)
  {
    if (this.capabilities.allowEdit) {
      return true;
    }
    if (p_175151_3_ == null) {
      return false;
    }
    BlockPos var4 = p_175151_1_.offset(p_175151_2_.getOpposite());
    Block var5 = this.worldObj.getBlockState(var4).getBlock();
    return (p_175151_3_.canPlaceOn(var5)) || (p_175151_3_.canEditBlocks());
  }
  
  protected int getExperiencePoints(EntityPlayer p_70693_1_)
  {
    if (this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
      return 0;
    }
    int var2 = this.experienceLevel * 7;
    return var2 > 100 ? 100 : var2;
  }
  
  protected boolean isPlayer()
  {
    return true;
  }
  
  public boolean getAlwaysRenderNameTagForRender()
  {
    return true;
  }
  
  public void clonePlayer(EntityPlayer p_71049_1_, boolean p_71049_2_)
  {
    if (p_71049_2_)
    {
      this.inventory.copyInventory(p_71049_1_.inventory);
      setHealth(p_71049_1_.getHealth());
      this.foodStats = p_71049_1_.foodStats;
      this.experienceLevel = p_71049_1_.experienceLevel;
      this.experienceTotal = p_71049_1_.experienceTotal;
      this.experience = p_71049_1_.experience;
      setScore(p_71049_1_.getScore());
      this.teleportDirection = p_71049_1_.teleportDirection;
    }
    else if (this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
    {
      this.inventory.copyInventory(p_71049_1_.inventory);
      this.experienceLevel = p_71049_1_.experienceLevel;
      this.experienceTotal = p_71049_1_.experienceTotal;
      this.experience = p_71049_1_.experience;
      setScore(p_71049_1_.getScore());
    }
    this.theInventoryEnderChest = p_71049_1_.theInventoryEnderChest;
    getDataWatcher().updateObject(10, Byte.valueOf(p_71049_1_.getDataWatcher().getWatchableObjectByte(10)));
  }
  
  protected boolean canTriggerWalking()
  {
    return !this.capabilities.isFlying;
  }
  
  public void sendPlayerAbilities() {}
  
  public void setGameType(WorldSettings.GameType gameType) {}
  
  public String getName()
  {
    return this.gameProfile.getName();
  }
  
  public InventoryEnderChest getInventoryEnderChest()
  {
    return this.theInventoryEnderChest;
  }
  
  public ItemStack getEquipmentInSlot(int p_71124_1_)
  {
    return p_71124_1_ == 0 ? this.inventory.getCurrentItem() : this.inventory.armorInventory[(p_71124_1_ - 1)];
  }
  
  public ItemStack getHeldItem()
  {
    return this.inventory.getCurrentItem();
  }
  
  public void setItemInUseCount(int inputCount)
  {
    this.itemInUseCount = inputCount;
  }
  
  public void setCurrentItemOrArmor(int slotIn, ItemStack itemStackIn)
  {
    this.inventory.armorInventory[slotIn] = itemStackIn;
  }
  
  public boolean isInvisibleToPlayer(EntityPlayer playerIn)
  {
    if (!isInvisible()) {
      return false;
    }
    if (playerIn.func_175149_v()) {
      return false;
    }
    Team var2 = getTeam();
    return (var2 == null) || (playerIn == null) || (playerIn.getTeam() != var2) || (!var2.func_98297_h());
  }
  
  public abstract boolean func_175149_v();
  
  public ItemStack[] getInventory()
  {
    return this.inventory.armorInventory;
  }
  
  public boolean isPushedByWater()
  {
    return !this.capabilities.isFlying;
  }
  
  public Scoreboard getWorldScoreboard()
  {
    return this.worldObj.getScoreboard();
  }
  
  public Team getTeam()
  {
    return getWorldScoreboard().getPlayersTeam(getName());
  }
  
  public IChatComponent getDisplayName()
  {
    ChatComponentText var1 = new ChatComponentText(ScorePlayerTeam.formatPlayerName(getTeam(), getName()));
    var1.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + getName() + " "));
    var1.getChatStyle().setChatHoverEvent(func_174823_aP());
    var1.getChatStyle().setInsertion(getName());
    return var1;
  }
  
  public float getEyeHeight()
  {
    float var1 = 1.62F;
    if (isPlayerSleeping()) {
      var1 = 0.2F;
    }
    if (isSneaking()) {
      var1 -= 0.08F;
    }
    return var1;
  }
  
  public void setAbsorptionAmount(float p_110149_1_)
  {
    if (p_110149_1_ < 0.0F) {
      p_110149_1_ = 0.0F;
    }
    getDataWatcher().updateObject(17, Float.valueOf(p_110149_1_));
  }
  
  public float getAbsorptionAmount()
  {
    return getDataWatcher().getWatchableObjectFloat(17);
  }
  
  public static UUID getUUID(GameProfile p_146094_0_)
  {
    UUID var1 = p_146094_0_.getId();
    if (var1 == null) {
      var1 = func_175147_b(p_146094_0_.getName());
    }
    return var1;
  }
  
  public static UUID func_175147_b(String p_175147_0_)
  {
    return UUID.nameUUIDFromBytes(("OfflinePlayer:" + p_175147_0_).getBytes(Charsets.UTF_8));
  }
  
  public boolean func_175146_a(LockCode p_175146_1_)
  {
    if (p_175146_1_.isEmpty()) {
      return true;
    }
    ItemStack var2 = getCurrentEquippedItem();
    return (var2 != null) && (var2.hasDisplayName()) ? var2.getDisplayName().equals(p_175146_1_.getLock()) : false;
  }
  
  public boolean func_175148_a(EnumPlayerModelParts p_175148_1_)
  {
    return (getDataWatcher().getWatchableObjectByte(10) & p_175148_1_.func_179327_a()) == p_175148_1_.func_179327_a();
  }
  
  public boolean sendCommandFeedback()
  {
    return net.minecraft.server.MinecraftServer.getServer().worldServers[0].getGameRules().getGameRuleBooleanValue("sendCommandFeedback");
  }
  
  public boolean func_174820_d(int p_174820_1_, ItemStack p_174820_2_)
  {
    if ((p_174820_1_ >= 0) && (p_174820_1_ < this.inventory.mainInventory.length))
    {
      this.inventory.setInventorySlotContents(p_174820_1_, p_174820_2_);
      return true;
    }
    int var3 = p_174820_1_ - 100;
    if ((var3 >= 0) && (var3 < this.inventory.armorInventory.length))
    {
      int var4 = var3 + 1;
      if ((p_174820_2_ != null) && (p_174820_2_.getItem() != null)) {
        if ((p_174820_2_.getItem() instanceof ItemArmor))
        {
          if (EntityLiving.getArmorPosition(p_174820_2_) != var4) {
            return false;
          }
        }
        else if ((var4 != 4) || ((p_174820_2_.getItem() != Items.skull) && (!(p_174820_2_.getItem() instanceof ItemBlock)))) {
          return false;
        }
      }
      this.inventory.setInventorySlotContents(var3 + this.inventory.mainInventory.length, p_174820_2_);
      return true;
    }
    int var4 = p_174820_1_ - 200;
    if ((var4 >= 0) && (var4 < this.theInventoryEnderChest.getSizeInventory()))
    {
      this.theInventoryEnderChest.setInventorySlotContents(var4, p_174820_2_);
      return true;
    }
    return false;
  }
  
  public boolean func_175140_cp()
  {
    return this.field_175153_bG;
  }
  
  public void func_175150_k(boolean p_175150_1_)
  {
    this.field_175153_bG = p_175150_1_;
  }
  
  public static enum EnumChatVisibility
  {
    private static final EnumChatVisibility[] field_151432_d;
    private final int chatVisibility;
    private final String resourceKey;
    private static final EnumChatVisibility[] $VALUES;
    private static final String __OBFID = "CL_00001714";
    
    private EnumChatVisibility(String p_i45323_1_, int p_i45323_2_, int p_i45323_3_, String p_i45323_4_)
    {
      this.chatVisibility = p_i45323_3_;
      this.resourceKey = p_i45323_4_;
    }
    
    public int getChatVisibility()
    {
      return this.chatVisibility;
    }
    
    public static EnumChatVisibility getEnumChatVisibility(int p_151426_0_)
    {
      return field_151432_d[(p_151426_0_ % field_151432_d.length)];
    }
    
    public String getResourceKey()
    {
      return this.resourceKey;
    }
    
    static
    {
      field_151432_d = new EnumChatVisibility[values().length];
      
      $VALUES = new EnumChatVisibility[] { FULL, SYSTEM, HIDDEN };
      
      EnumChatVisibility[] var0 = values();
      int var1 = var0.length;
      for (int var2 = 0; var2 < var1; var2++)
      {
        EnumChatVisibility var3 = var0[var2];
        field_151432_d[var3.chatVisibility] = var3;
      }
    }
  }
  
  public static enum EnumStatus
  {
    private static final EnumStatus[] $VALUES = { OK, NOT_POSSIBLE_HERE, NOT_POSSIBLE_NOW, TOO_FAR_AWAY, OTHER_PROBLEM, NOT_SAFE };
    private static final String __OBFID = "CL_00001712";
  }
  
  static final class SwitchEnumFacing
  {
    static final int[] field_179420_a = new int[EnumFacing.values().length];
    private static final String __OBFID = "CL_00002188";
    
    SwitchEnumFacing() {}
    
    static
    {
      try
      {
        field_179420_a[EnumFacing.SOUTH.ordinal()] = 1;
      }
      catch (NoSuchFieldError localNoSuchFieldError1) {}
      try
      {
        field_179420_a[EnumFacing.NORTH.ordinal()] = 2;
      }
      catch (NoSuchFieldError localNoSuchFieldError2) {}
      try
      {
        field_179420_a[EnumFacing.WEST.ordinal()] = 3;
      }
      catch (NoSuchFieldError localNoSuchFieldError3) {}
      try
      {
        field_179420_a[EnumFacing.EAST.ordinal()] = 4;
      }
      catch (NoSuchFieldError localNoSuchFieldError4) {}
    }
  }
}
