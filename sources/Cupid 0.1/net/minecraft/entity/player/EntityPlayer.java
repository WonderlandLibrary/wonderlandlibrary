package net.minecraft.entity.player;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
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
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
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

public abstract class EntityPlayer extends EntityLivingBase {
  public InventoryPlayer inventory = new InventoryPlayer(this);
  
  private InventoryEnderChest theInventoryEnderChest = new InventoryEnderChest();
  
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
  
  public BlockPos playerLocation;
  
  private int sleepTimer;
  
  public float renderOffsetX;
  
  public float renderOffsetY;
  
  public float renderOffsetZ;
  
  private BlockPos spawnChunk;
  
  private boolean spawnForced;
  
  private BlockPos startMinecartRidingCoordinate;
  
  public PlayerCapabilities capabilities = new PlayerCapabilities();
  
  public int experienceLevel;
  
  public int experienceTotal;
  
  public float experience;
  
  private int xpSeed;
  
  private ItemStack itemInUse;
  
  public int itemInUseCount;
  
  public float speedOnGround = 0.1F;
  
  public float speedInAir = 0.02F;
  
  private int lastXPSound;
  
  private final GameProfile gameProfile;
  
  private boolean hasReducedDebug = false;
  
  public EntityFishHook fishEntity;
  
  public EntityPlayer(World worldIn, GameProfile gameProfileIn) {
    super(worldIn);
    this.entityUniqueID = getUUID(gameProfileIn);
    this.gameProfile = gameProfileIn;
    this.inventoryContainer = (Container)new ContainerPlayer(this.inventory, !worldIn.isRemote, this);
    this.openContainer = this.inventoryContainer;
    BlockPos blockpos = worldIn.getSpawnPoint();
    setLocationAndAngles(blockpos.getX() + 0.5D, (blockpos.getY() + 1), blockpos.getZ() + 0.5D, 0.0F, 0.0F);
    this.field_70741_aB = 180.0F;
    this.fireResistance = 20;
  }
  
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
    getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.10000000149011612D);
  }
  
  protected void entityInit() {
    super.entityInit();
    this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
    this.dataWatcher.addObject(17, Float.valueOf(0.0F));
    this.dataWatcher.addObject(18, Integer.valueOf(0));
    this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
  }
  
  public ItemStack getItemInUse() {
    return this.itemInUse;
  }
  
  public int getItemInUseCount() {
    return this.itemInUseCount;
  }
  
  public boolean isUsingItem() {
    return (this.itemInUse != null);
  }
  
  public int getItemInUseDuration() {
    return isUsingItem() ? (this.itemInUse.getMaxItemUseDuration() - this.itemInUseCount) : 0;
  }
  
  public void stopUsingItem() {
    if (this.itemInUse != null)
      this.itemInUse.onPlayerStoppedUsing(this.worldObj, this, this.itemInUseCount); 
    clearItemInUse();
  }
  
  public void clearItemInUse() {
    this.itemInUse = null;
    this.itemInUseCount = 0;
    if (!this.worldObj.isRemote)
      setEating(false); 
  }
  
  public boolean isBlocking() {
    return (isUsingItem() && this.itemInUse.getItem().getItemUseAction(this.itemInUse) == EnumAction.BLOCK);
  }
  
  public void onUpdate() {
    this.noClip = isSpectator();
    if (isSpectator())
      this.onGround = false; 
    if (this.itemInUse != null) {
      ItemStack itemstack = this.inventory.getCurrentItem();
      if (itemstack == this.itemInUse) {
        if (this.itemInUseCount <= 25 && this.itemInUseCount % 4 == 0)
          updateItemUse(itemstack, 5); 
        if (--this.itemInUseCount == 0 && !this.worldObj.isRemote)
          onItemUseFinish(); 
      } else {
        clearItemInUse();
      } 
    } 
    if (this.xpCooldown > 0)
      this.xpCooldown--; 
    if (isPlayerSleeping()) {
      this.sleepTimer++;
      if (this.sleepTimer > 100)
        this.sleepTimer = 100; 
      if (!this.worldObj.isRemote)
        if (!isInBed()) {
          wakeUpPlayer(true, true, false);
        } else if (this.worldObj.isDaytime()) {
          wakeUpPlayer(false, true, true);
        }  
    } else if (this.sleepTimer > 0) {
      this.sleepTimer++;
      if (this.sleepTimer >= 110)
        this.sleepTimer = 0; 
    } 
    super.onUpdate();
    if (!this.worldObj.isRemote && this.openContainer != null && !this.openContainer.canInteractWith(this)) {
      closeScreen();
      this.openContainer = this.inventoryContainer;
    } 
    if (isBurning() && this.capabilities.disableDamage)
      extinguish(); 
    this.prevChasingPosX = this.chasingPosX;
    this.prevChasingPosY = this.chasingPosY;
    this.prevChasingPosZ = this.chasingPosZ;
    double d5 = this.posX - this.chasingPosX;
    double d0 = this.posY - this.chasingPosY;
    double d1 = this.posZ - this.chasingPosZ;
    double d2 = 10.0D;
    if (d5 > d2)
      this.prevChasingPosX = this.chasingPosX = this.posX; 
    if (d1 > d2)
      this.prevChasingPosZ = this.chasingPosZ = this.posZ; 
    if (d0 > d2)
      this.prevChasingPosY = this.chasingPosY = this.posY; 
    if (d5 < -d2)
      this.prevChasingPosX = this.chasingPosX = this.posX; 
    if (d1 < -d2)
      this.prevChasingPosZ = this.chasingPosZ = this.posZ; 
    if (d0 < -d2)
      this.prevChasingPosY = this.chasingPosY = this.posY; 
    this.chasingPosX += d5 * 0.25D;
    this.chasingPosZ += d1 * 0.25D;
    this.chasingPosY += d0 * 0.25D;
    if (this.ridingEntity == null)
      this.startMinecartRidingCoordinate = null; 
    if (!this.worldObj.isRemote) {
      this.foodStats.onUpdate(this);
      triggerAchievement(StatList.minutesPlayedStat);
      if (isEntityAlive())
        triggerAchievement(StatList.timeSinceDeathStat); 
    } 
    int i = 29999999;
    double d3 = MathHelper.clamp_double(this.posX, -2.9999999E7D, 2.9999999E7D);
    double d4 = MathHelper.clamp_double(this.posZ, -2.9999999E7D, 2.9999999E7D);
    if (d3 != this.posX || d4 != this.posZ)
      setPosition(d3, this.posY, d4); 
  }
  
  public int getMaxInPortalTime() {
    return this.capabilities.disableDamage ? 0 : 80;
  }
  
  protected String getSwimSound() {
    return "game.player.swim";
  }
  
  protected String getSplashSound() {
    return "game.player.swim.splash";
  }
  
  public int getPortalCooldown() {
    return 10;
  }
  
  public void playSound(String name, float volume, float pitch) {
    this.worldObj.playSoundToNearExcept(this, name, volume, pitch);
  }
  
  protected void updateItemUse(ItemStack itemStackIn, int p_71010_2_) {
    if (itemStackIn.getItemUseAction() == EnumAction.DRINK)
      playSound("random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F); 
    if (itemStackIn.getItemUseAction() == EnumAction.EAT) {
      for (int i = 0; i < p_71010_2_; i++) {
        Vec3 vec3 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
        vec3 = vec3.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
        vec3 = vec3.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
        double d0 = -this.rand.nextFloat() * 0.6D - 0.3D;
        Vec3 vec31 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
        vec31 = vec31.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
        vec31 = vec31.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
        vec31 = vec31.addVector(this.posX, this.posY + getEyeHeight(), this.posZ);
        if (itemStackIn.getHasSubtypes()) {
          this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord, new int[] { Item.getIdFromItem(itemStackIn.getItem()), itemStackIn.getMetadata() });
        } else {
          this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord, new int[] { Item.getIdFromItem(itemStackIn.getItem()) });
        } 
      } 
      playSound("random.eat", 0.5F + 0.5F * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    } 
  }
  
  protected void onItemUseFinish() {
    if (this.itemInUse != null) {
      updateItemUse(this.itemInUse, 16);
      int i = this.itemInUse.stackSize;
      ItemStack itemstack = this.itemInUse.onItemUseFinish(this.worldObj, this);
      if (itemstack != this.itemInUse || (itemstack != null && itemstack.stackSize != i)) {
        this.inventory.mainInventory[this.inventory.currentItem] = itemstack;
        if (itemstack.stackSize == 0)
          this.inventory.mainInventory[this.inventory.currentItem] = null; 
      } 
      clearItemInUse();
    } 
  }
  
  public void handleStatusUpdate(byte id) {
    if (id == 9) {
      onItemUseFinish();
    } else if (id == 23) {
      this.hasReducedDebug = false;
    } else if (id == 22) {
      this.hasReducedDebug = true;
    } else {
      super.handleStatusUpdate(id);
    } 
  }
  
  protected boolean isMovementBlocked() {
    return (getHealth() <= 0.0F || isPlayerSleeping());
  }
  
  protected void closeScreen() {
    this.openContainer = this.inventoryContainer;
  }
  
  public void updateRidden() {
    if (!this.worldObj.isRemote && isSneaking()) {
      mountEntity((Entity)null);
      setSneaking(false);
    } else {
      double d0 = this.posX;
      double d1 = this.posY;
      double d2 = this.posZ;
      float f = this.rotationYaw;
      float f1 = this.rotationPitch;
      super.updateRidden();
      this.prevCameraYaw = this.cameraYaw;
      this.cameraYaw = 0.0F;
      addMountedMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
      if (this.ridingEntity instanceof EntityPig) {
        this.rotationPitch = f1;
        this.rotationYaw = f;
        this.renderYawOffset = ((EntityPig)this.ridingEntity).renderYawOffset;
      } 
    } 
  }
  
  public void preparePlayerToSpawn() {
    setSize(0.6F, 1.8F);
    super.preparePlayerToSpawn();
    setHealth(getMaxHealth());
    this.deathTime = 0;
  }
  
  protected void updateEntityActionState() {
    super.updateEntityActionState();
    updateArmSwingProgress();
    this.rotationYawHead = this.rotationYaw;
  }
  
  public void onLivingUpdate() {
    this.renderPitchRotation = this.rotationPitch;
    if (this.flyToggleTimer > 0)
      this.flyToggleTimer--; 
    if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.worldObj.getGameRules().getBoolean("naturalRegeneration")) {
      if (getHealth() < getMaxHealth() && this.ticksExisted % 20 == 0)
        heal(1.0F); 
      if (this.foodStats.needFood() && this.ticksExisted % 10 == 0)
        this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1); 
    } 
    this.inventory.decrementAnimations();
    this.prevCameraYaw = this.cameraYaw;
    super.onLivingUpdate();
    IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
    if (!this.worldObj.isRemote)
      iattributeinstance.setBaseValue(this.capabilities.getWalkSpeed()); 
    this.jumpMovementFactor = this.speedInAir;
    if (isSprinting())
      this.jumpMovementFactor = (float)(this.jumpMovementFactor + this.speedInAir * 0.3D); 
    setAIMoveSpeed((float)iattributeinstance.getAttributeValue());
    float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
    float f1 = (float)(Math.atan(-this.motionY * 0.20000000298023224D) * 15.0D);
    if (f > 0.1F)
      f = 0.1F; 
    if (!this.onGround || getHealth() <= 0.0F)
      f = 0.0F; 
    if (this.onGround || getHealth() <= 0.0F)
      f1 = 0.0F; 
    this.cameraYaw += (f - this.cameraYaw) * 0.4F;
    this.cameraPitch += (f1 - this.cameraPitch) * 0.8F;
    if (getHealth() > 0.0F && !isSpectator()) {
      AxisAlignedBB axisalignedbb = null;
      if (this.ridingEntity != null && !this.ridingEntity.isDead) {
        axisalignedbb = getEntityBoundingBox().union(this.ridingEntity.getEntityBoundingBox()).expand(1.0D, 0.0D, 1.0D);
      } else {
        axisalignedbb = getEntityBoundingBox().expand(1.0D, 0.5D, 1.0D);
      } 
      List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)this, axisalignedbb);
      for (int i = 0; i < list.size(); i++) {
        Entity entity = list.get(i);
        if (!entity.isDead)
          collideWithPlayer(entity); 
      } 
    } 
  }
  
  private void collideWithPlayer(Entity p_71044_1_) {
    p_71044_1_.onCollideWithPlayer(this);
  }
  
  public int getScore() {
    return this.dataWatcher.getWatchableObjectInt(18);
  }
  
  public void setScore(int p_85040_1_) {
    this.dataWatcher.updateObject(18, Integer.valueOf(p_85040_1_));
  }
  
  public void addScore(int p_85039_1_) {
    int i = getScore();
    this.dataWatcher.updateObject(18, Integer.valueOf(i + p_85039_1_));
  }
  
  public void onDeath(DamageSource cause) {
    super.onDeath(cause);
    setSize(0.2F, 0.2F);
    setPosition(this.posX, this.posY, this.posZ);
    this.motionY = 0.10000000149011612D;
    if (getName().equals("Notch"))
      dropItem(new ItemStack(Items.apple, 1), true, false); 
    if (!this.worldObj.getGameRules().getBoolean("keepInventory"))
      this.inventory.dropAllItems(); 
    if (cause != null) {
      this.motionX = (-MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * 3.1415927F / 180.0F) * 0.1F);
      this.motionZ = (-MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * 3.1415927F / 180.0F) * 0.1F);
    } else {
      this.motionX = this.motionZ = 0.0D;
    } 
    triggerAchievement(StatList.deathsStat);
    func_175145_a(StatList.timeSinceDeathStat);
  }
  
  protected String getHurtSound() {
    return "game.player.hurt";
  }
  
  protected String getDeathSound() {
    return "game.player.die";
  }
  
  public void addToPlayerScore(Entity entityIn, int amount) {
    addScore(amount);
    Collection<ScoreObjective> collection = getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.totalKillCount);
    if (entityIn instanceof EntityPlayer) {
      triggerAchievement(StatList.playerKillsStat);
      collection.addAll(getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.playerKillCount));
      collection.addAll(func_175137_e(entityIn));
    } else {
      triggerAchievement(StatList.mobKillsStat);
    } 
    for (ScoreObjective scoreobjective : collection) {
      Score score = getWorldScoreboard().getValueFromObjective(getName(), scoreobjective);
      score.func_96648_a();
    } 
  }
  
  private Collection<ScoreObjective> func_175137_e(Entity p_175137_1_) {
    ScorePlayerTeam scoreplayerteam = getWorldScoreboard().getPlayersTeam(getName());
    if (scoreplayerteam != null) {
      int i = scoreplayerteam.getChatFormat().getColorIndex();
      if (i >= 0 && i < IScoreObjectiveCriteria.field_178793_i.length)
        for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.field_178793_i[i])) {
          Score score = getWorldScoreboard().getValueFromObjective(p_175137_1_.getName(), scoreobjective);
          score.func_96648_a();
        }  
    } 
    ScorePlayerTeam scoreplayerteam1 = getWorldScoreboard().getPlayersTeam(p_175137_1_.getName());
    if (scoreplayerteam1 != null) {
      int j = scoreplayerteam1.getChatFormat().getColorIndex();
      if (j >= 0 && j < IScoreObjectiveCriteria.field_178792_h.length)
        return getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.field_178792_h[j]); 
    } 
    return Lists.newArrayList();
  }
  
  public EntityItem dropOneItem(boolean dropAll) {
    return dropItem(this.inventory.decrStackSize(this.inventory.currentItem, (dropAll && this.inventory.getCurrentItem() != null) ? (this.inventory.getCurrentItem()).stackSize : 1), false, true);
  }
  
  public EntityItem dropPlayerItemWithRandomChoice(ItemStack itemStackIn, boolean unused) {
    return dropItem(itemStackIn, false, false);
  }
  
  public EntityItem dropItem(ItemStack droppedItem, boolean dropAround, boolean traceItem) {
    if (droppedItem == null)
      return null; 
    if (droppedItem.stackSize == 0)
      return null; 
    double d0 = this.posY - 0.30000001192092896D + getEyeHeight();
    EntityItem entityitem = new EntityItem(this.worldObj, this.posX, d0, this.posZ, droppedItem);
    entityitem.setPickupDelay(40);
    if (traceItem)
      entityitem.setThrower(getName()); 
    if (dropAround) {
      float f = this.rand.nextFloat() * 0.5F;
      float f1 = this.rand.nextFloat() * 3.1415927F * 2.0F;
      entityitem.motionX = (-MathHelper.sin(f1) * f);
      entityitem.motionZ = (MathHelper.cos(f1) * f);
      entityitem.motionY = 0.20000000298023224D;
    } else {
      float f2 = 0.3F;
      entityitem.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f2);
      entityitem.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f2);
      entityitem.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F) * f2 + 0.1F);
      float f3 = this.rand.nextFloat() * 3.1415927F * 2.0F;
      f2 = 0.02F * this.rand.nextFloat();
      entityitem.motionX += Math.cos(f3) * f2;
      entityitem.motionY += ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
      entityitem.motionZ += Math.sin(f3) * f2;
    } 
    joinEntityItemWithWorld(entityitem);
    if (traceItem)
      triggerAchievement(StatList.dropStat); 
    return entityitem;
  }
  
  protected void joinEntityItemWithWorld(EntityItem itemIn) {
    this.worldObj.spawnEntityInWorld((Entity)itemIn);
  }
  
  public float getToolDigEfficiency(Block p_180471_1_) {
    float f = this.inventory.getStrVsBlock(p_180471_1_);
    if (f > 1.0F) {
      int i = EnchantmentHelper.getEfficiencyModifier(this);
      ItemStack itemstack = this.inventory.getCurrentItem();
      if (i > 0 && itemstack != null)
        f += (i * i + 1); 
    } 
    if (isPotionActive(Potion.digSpeed))
      f *= 1.0F + (getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F; 
    if (isPotionActive(Potion.digSlowdown)) {
      float f1 = 1.0F;
      switch (getActivePotionEffect(Potion.digSlowdown).getAmplifier()) {
        case 0:
          f1 = 0.3F;
          break;
        case 1:
          f1 = 0.09F;
          break;
        case 2:
          f1 = 0.0027F;
          break;
        default:
          f1 = 8.1E-4F;
          break;
      } 
      f *= f1;
    } 
    if (isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this))
      f /= 5.0F; 
    if (!this.onGround)
      f /= 5.0F; 
    return f;
  }
  
  public boolean canHarvestBlock(Block blockToHarvest) {
    return this.inventory.canHeldItemHarvest(blockToHarvest);
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    this.entityUniqueID = getUUID(this.gameProfile);
    NBTTagList nbttaglist = tagCompund.getTagList("Inventory", 10);
    this.inventory.readFromNBT(nbttaglist);
    this.inventory.currentItem = tagCompund.getInteger("SelectedItemSlot");
    this.sleeping = tagCompund.getBoolean("Sleeping");
    this.sleepTimer = tagCompund.getShort("SleepTimer");
    this.experience = tagCompund.getFloat("XpP");
    this.experienceLevel = tagCompund.getInteger("XpLevel");
    this.experienceTotal = tagCompund.getInteger("XpTotal");
    this.xpSeed = tagCompund.getInteger("XpSeed");
    if (this.xpSeed == 0)
      this.xpSeed = this.rand.nextInt(); 
    setScore(tagCompund.getInteger("Score"));
    if (this.sleeping) {
      this.playerLocation = new BlockPos((Entity)this);
      wakeUpPlayer(true, true, false);
    } 
    if (tagCompund.hasKey("SpawnX", 99) && tagCompund.hasKey("SpawnY", 99) && tagCompund.hasKey("SpawnZ", 99)) {
      this.spawnChunk = new BlockPos(tagCompund.getInteger("SpawnX"), tagCompund.getInteger("SpawnY"), tagCompund.getInteger("SpawnZ"));
      this.spawnForced = tagCompund.getBoolean("SpawnForced");
    } 
    this.foodStats.readNBT(tagCompund);
    this.capabilities.readCapabilitiesFromNBT(tagCompund);
    if (tagCompund.hasKey("EnderItems", 9)) {
      NBTTagList nbttaglist1 = tagCompund.getTagList("EnderItems", 10);
      this.theInventoryEnderChest.loadInventoryFromNBT(nbttaglist1);
    } 
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setTag("Inventory", (NBTBase)this.inventory.writeToNBT(new NBTTagList()));
    tagCompound.setInteger("SelectedItemSlot", this.inventory.currentItem);
    tagCompound.setBoolean("Sleeping", this.sleeping);
    tagCompound.setShort("SleepTimer", (short)this.sleepTimer);
    tagCompound.setFloat("XpP", this.experience);
    tagCompound.setInteger("XpLevel", this.experienceLevel);
    tagCompound.setInteger("XpTotal", this.experienceTotal);
    tagCompound.setInteger("XpSeed", this.xpSeed);
    tagCompound.setInteger("Score", getScore());
    if (this.spawnChunk != null) {
      tagCompound.setInteger("SpawnX", this.spawnChunk.getX());
      tagCompound.setInteger("SpawnY", this.spawnChunk.getY());
      tagCompound.setInteger("SpawnZ", this.spawnChunk.getZ());
      tagCompound.setBoolean("SpawnForced", this.spawnForced);
    } 
    this.foodStats.writeNBT(tagCompound);
    this.capabilities.writeCapabilitiesToNBT(tagCompound);
    tagCompound.setTag("EnderItems", (NBTBase)this.theInventoryEnderChest.saveInventoryToNBT());
    ItemStack itemstack = this.inventory.getCurrentItem();
    if (itemstack != null && itemstack.getItem() != null)
      tagCompound.setTag("SelectedItem", (NBTBase)itemstack.writeToNBT(new NBTTagCompound())); 
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    if (this.capabilities.disableDamage && !source.canHarmInCreative())
      return false; 
    this.entityAge = 0;
    if (getHealth() <= 0.0F)
      return false; 
    if (isPlayerSleeping() && !this.worldObj.isRemote)
      wakeUpPlayer(true, true, false); 
    if (source.isDifficultyScaled()) {
      if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
        amount = 0.0F; 
      if (this.worldObj.getDifficulty() == EnumDifficulty.EASY)
        amount = amount / 2.0F + 1.0F; 
      if (this.worldObj.getDifficulty() == EnumDifficulty.HARD)
        amount = amount * 3.0F / 2.0F; 
    } 
    if (amount == 0.0F)
      return false; 
    Entity entity = source.getEntity();
    if (entity instanceof EntityArrow && ((EntityArrow)entity).shootingEntity != null)
      entity = ((EntityArrow)entity).shootingEntity; 
    return super.attackEntityFrom(source, amount);
  }
  
  public boolean canAttackPlayer(EntityPlayer other) {
    Team team = getTeam();
    Team team1 = other.getTeam();
    return (team == null) ? true : (!team.isSameTeam(team1) ? true : team.getAllowFriendlyFire());
  }
  
  protected void damageArmor(float p_70675_1_) {
    this.inventory.damageArmor(p_70675_1_);
  }
  
  public int getTotalArmorValue() {
    return this.inventory.getTotalArmorValue();
  }
  
  public float getArmorVisibility() {
    int i = 0;
    for (ItemStack itemstack : this.inventory.armorInventory) {
      if (itemstack != null)
        i++; 
    } 
    return i / this.inventory.armorInventory.length;
  }
  
  protected void damageEntity(DamageSource damageSrc, float damageAmount) {
    if (!isEntityInvulnerable(damageSrc)) {
      if (!damageSrc.isUnblockable() && isBlocking() && damageAmount > 0.0F)
        damageAmount = (1.0F + damageAmount) * 0.5F; 
      damageAmount = applyArmorCalculations(damageSrc, damageAmount);
      damageAmount = applyPotionDamageCalculations(damageSrc, damageAmount);
      float f = damageAmount;
      damageAmount = Math.max(damageAmount - getAbsorptionAmount(), 0.0F);
      setAbsorptionAmount(getAbsorptionAmount() - f - damageAmount);
      if (damageAmount != 0.0F) {
        addExhaustion(damageSrc.getHungerDamage());
        float f1 = getHealth();
        setHealth(getHealth() - damageAmount);
        getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
        if (damageAmount < 3.4028235E37F)
          addStat(StatList.damageTakenStat, Math.round(damageAmount * 10.0F)); 
      } 
    } 
  }
  
  public void openEditSign(TileEntitySign signTile) {}
  
  public void openEditCommandBlock(CommandBlockLogic cmdBlockLogic) {}
  
  public void displayVillagerTradeGui(IMerchant villager) {}
  
  public void displayGUIChest(IInventory chestInventory) {}
  
  public void displayGUIHorse(EntityHorse horse, IInventory horseInventory) {}
  
  public void displayGui(IInteractionObject guiOwner) {}
  
  public void displayGUIBook(ItemStack bookStack) {}
  
  public boolean interactWith(Entity p_70998_1_) {
    if (isSpectator()) {
      if (p_70998_1_ instanceof IInventory)
        displayGUIChest((IInventory)p_70998_1_); 
      return false;
    } 
    ItemStack itemstack = getCurrentEquippedItem();
    ItemStack itemstack1 = (itemstack != null) ? itemstack.copy() : null;
    if (!p_70998_1_.interactFirst(this)) {
      if (itemstack != null && p_70998_1_ instanceof EntityLivingBase) {
        if (this.capabilities.isCreativeMode)
          itemstack = itemstack1; 
        if (itemstack.interactWithEntity(this, (EntityLivingBase)p_70998_1_)) {
          if (itemstack.stackSize <= 0 && !this.capabilities.isCreativeMode)
            destroyCurrentEquippedItem(); 
          return true;
        } 
      } 
      return false;
    } 
    if (itemstack != null && itemstack == getCurrentEquippedItem())
      if (itemstack.stackSize <= 0 && !this.capabilities.isCreativeMode) {
        destroyCurrentEquippedItem();
      } else if (itemstack.stackSize < itemstack1.stackSize && this.capabilities.isCreativeMode) {
        itemstack.stackSize = itemstack1.stackSize;
      }  
    return true;
  }
  
  public ItemStack getCurrentEquippedItem() {
    return this.inventory.getCurrentItem();
  }
  
  public void destroyCurrentEquippedItem() {
    this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack)null);
  }
  
  public double getYOffset() {
    return -0.35D;
  }
  
  public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
    if (targetEntity.canAttackWithItem())
      if (!targetEntity.hitByEntity((Entity)this)) {
        float f = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int i = 0;
        float f1 = 0.0F;
        if (targetEntity instanceof EntityLivingBase) {
          f1 = EnchantmentHelper.func_152377_a(getHeldItem(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
        } else {
          f1 = EnchantmentHelper.func_152377_a(getHeldItem(), EnumCreatureAttribute.UNDEFINED);
        } 
        i += EnchantmentHelper.getKnockbackModifier(this);
        if (isSprinting())
          i++; 
        if (f > 0.0F || f1 > 0.0F) {
          boolean flag = (this.fallDistance > 0.0F && !this.onGround && !isOnLadder() && !isInWater() && !isPotionActive(Potion.blindness) && this.ridingEntity == null && targetEntity instanceof EntityLivingBase);
          if (flag && f > 0.0F)
            f *= 1.5F; 
          f += f1;
          boolean flag1 = false;
          int j = EnchantmentHelper.getFireAspectModifier(this);
          if (targetEntity instanceof EntityLivingBase && j > 0 && !targetEntity.isBurning()) {
            flag1 = true;
            targetEntity.setFire(1);
          } 
          double d0 = targetEntity.motionX;
          double d1 = targetEntity.motionY;
          double d2 = targetEntity.motionZ;
          boolean flag2 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(this), f);
          if (flag2) {
            EntityLivingBase entityLivingBase;
            if (i > 0) {
              targetEntity.addVelocity((-MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F), 0.1D, (MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F));
              this.motionX *= 0.6D;
              this.motionZ *= 0.6D;
              setSprinting(false);
            } 
            if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
              ((EntityPlayerMP)targetEntity).playerNetServerHandler.sendPacket((Packet)new S12PacketEntityVelocity(targetEntity));
              targetEntity.velocityChanged = false;
              targetEntity.motionX = d0;
              targetEntity.motionY = d1;
              targetEntity.motionZ = d2;
            } 
            if (flag)
              onCriticalHit(targetEntity); 
            if (f1 > 0.0F)
              onEnchantmentCritical(targetEntity); 
            if (f >= 18.0F)
              triggerAchievement((StatBase)AchievementList.overkill); 
            setLastAttacker(targetEntity);
            if (targetEntity instanceof EntityLivingBase)
              EnchantmentHelper.applyThornEnchantments((EntityLivingBase)targetEntity, (Entity)this); 
            EnchantmentHelper.applyArthropodEnchantments(this, targetEntity);
            ItemStack itemstack = getCurrentEquippedItem();
            Entity entity = targetEntity;
            if (targetEntity instanceof EntityDragonPart) {
              IEntityMultiPart ientitymultipart = ((EntityDragonPart)targetEntity).entityDragonObj;
              if (ientitymultipart instanceof EntityLivingBase)
                entityLivingBase = (EntityLivingBase)ientitymultipart; 
            } 
            if (itemstack != null && entityLivingBase instanceof EntityLivingBase) {
              itemstack.hitEntity(entityLivingBase, this);
              if (itemstack.stackSize <= 0)
                destroyCurrentEquippedItem(); 
            } 
            if (targetEntity instanceof EntityLivingBase) {
              addStat(StatList.damageDealtStat, Math.round(f * 10.0F));
              if (j > 0)
                targetEntity.setFire(j * 4); 
            } 
            addExhaustion(0.3F);
          } else if (flag1) {
            targetEntity.extinguish();
          } 
        } 
      }  
  }
  
  public void onCriticalHit(Entity entityHit) {}
  
  public void onEnchantmentCritical(Entity entityHit) {}
  
  public void respawnPlayer() {}
  
  public void setDead() {
    super.setDead();
    this.inventoryContainer.onContainerClosed(this);
    if (this.openContainer != null)
      this.openContainer.onContainerClosed(this); 
  }
  
  public boolean isEntityInsideOpaqueBlock() {
    return (!this.sleeping && super.isEntityInsideOpaqueBlock());
  }
  
  public boolean isUser() {
    return false;
  }
  
  public GameProfile getGameProfile() {
    return this.gameProfile;
  }
  
  public EnumStatus trySleep(BlockPos bedLocation) {
    if (!this.worldObj.isRemote) {
      if (isPlayerSleeping() || !isEntityAlive())
        return EnumStatus.OTHER_PROBLEM; 
      if (!this.worldObj.provider.isSurfaceWorld())
        return EnumStatus.NOT_POSSIBLE_HERE; 
      if (this.worldObj.isDaytime())
        return EnumStatus.NOT_POSSIBLE_NOW; 
      if (Math.abs(this.posX - bedLocation.getX()) > 3.0D || Math.abs(this.posY - bedLocation.getY()) > 2.0D || Math.abs(this.posZ - bedLocation.getZ()) > 3.0D)
        return EnumStatus.TOO_FAR_AWAY; 
      double d0 = 8.0D;
      double d1 = 5.0D;
      List<EntityMob> list = this.worldObj.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(bedLocation.getX() - d0, bedLocation.getY() - d1, bedLocation.getZ() - d0, bedLocation.getX() + d0, bedLocation.getY() + d1, bedLocation.getZ() + d0));
      if (!list.isEmpty())
        return EnumStatus.NOT_SAFE; 
    } 
    if (isRiding())
      mountEntity((Entity)null); 
    setSize(0.2F, 0.2F);
    if (this.worldObj.isBlockLoaded(bedLocation)) {
      EnumFacing enumfacing = (EnumFacing)this.worldObj.getBlockState(bedLocation).getValue((IProperty)BlockDirectional.FACING);
      float f = 0.5F;
      float f1 = 0.5F;
      switch (enumfacing) {
        case SOUTH:
          f1 = 0.9F;
          break;
        case NORTH:
          f1 = 0.1F;
          break;
        case WEST:
          f = 0.1F;
          break;
        case EAST:
          f = 0.9F;
          break;
      } 
      func_175139_a(enumfacing);
      setPosition((bedLocation.getX() + f), (bedLocation.getY() + 0.6875F), (bedLocation.getZ() + f1));
    } else {
      setPosition((bedLocation.getX() + 0.5F), (bedLocation.getY() + 0.6875F), (bedLocation.getZ() + 0.5F));
    } 
    this.sleeping = true;
    this.sleepTimer = 0;
    this.playerLocation = bedLocation;
    this.motionX = this.motionZ = this.motionY = 0.0D;
    if (!this.worldObj.isRemote)
      this.worldObj.updateAllPlayersSleepingFlag(); 
    return EnumStatus.OK;
  }
  
  private void func_175139_a(EnumFacing p_175139_1_) {
    this.renderOffsetX = 0.0F;
    this.renderOffsetZ = 0.0F;
    switch (p_175139_1_) {
      case SOUTH:
        this.renderOffsetZ = -1.8F;
        break;
      case NORTH:
        this.renderOffsetZ = 1.8F;
        break;
      case WEST:
        this.renderOffsetX = 1.8F;
        break;
      case EAST:
        this.renderOffsetX = -1.8F;
        break;
    } 
  }
  
  public void wakeUpPlayer(boolean p_70999_1_, boolean updateWorldFlag, boolean setSpawn) {
    setSize(0.6F, 1.8F);
    IBlockState iblockstate = this.worldObj.getBlockState(this.playerLocation);
    if (this.playerLocation != null && iblockstate.getBlock() == Blocks.bed) {
      this.worldObj.setBlockState(this.playerLocation, iblockstate.withProperty((IProperty)BlockBed.OCCUPIED, Boolean.valueOf(false)), 4);
      BlockPos blockpos = BlockBed.getSafeExitLocation(this.worldObj, this.playerLocation, 0);
      if (blockpos == null)
        blockpos = this.playerLocation.up(); 
      setPosition((blockpos.getX() + 0.5F), (blockpos.getY() + 0.1F), (blockpos.getZ() + 0.5F));
    } 
    this.sleeping = false;
    if (!this.worldObj.isRemote && updateWorldFlag)
      this.worldObj.updateAllPlayersSleepingFlag(); 
    this.sleepTimer = p_70999_1_ ? 0 : 100;
    if (setSpawn)
      setSpawnPoint(this.playerLocation, false); 
  }
  
  private boolean isInBed() {
    return (this.worldObj.getBlockState(this.playerLocation).getBlock() == Blocks.bed);
  }
  
  public static BlockPos getBedSpawnLocation(World worldIn, BlockPos bedLocation, boolean forceSpawn) {
    Block block = worldIn.getBlockState(bedLocation).getBlock();
    if (block != Blocks.bed) {
      if (!forceSpawn)
        return null; 
      boolean flag = block.func_181623_g();
      boolean flag1 = worldIn.getBlockState(bedLocation.up()).getBlock().func_181623_g();
      return (flag && flag1) ? bedLocation : null;
    } 
    return BlockBed.getSafeExitLocation(worldIn, bedLocation, 0);
  }
  
  public float getBedOrientationInDegrees() {
    if (this.playerLocation != null) {
      EnumFacing enumfacing = (EnumFacing)this.worldObj.getBlockState(this.playerLocation).getValue((IProperty)BlockDirectional.FACING);
      switch (enumfacing) {
        case SOUTH:
          return 90.0F;
        case NORTH:
          return 270.0F;
        case WEST:
          return 0.0F;
        case EAST:
          return 180.0F;
      } 
    } 
    return 0.0F;
  }
  
  public boolean isPlayerSleeping() {
    return this.sleeping;
  }
  
  public boolean isPlayerFullyAsleep() {
    return (this.sleeping && this.sleepTimer >= 100);
  }
  
  public int getSleepTimer() {
    return this.sleepTimer;
  }
  
  public void addChatComponentMessage(IChatComponent chatComponent) {}
  
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
  
  public void triggerAchievement(StatBase achievementIn) {
    addStat(achievementIn, 1);
  }
  
  public void addStat(StatBase stat, int amount) {}
  
  public void func_175145_a(StatBase p_175145_1_) {}
  
  public void jump() {
    super.jump();
    triggerAchievement(StatList.jumpStat);
    if (isSprinting()) {
      addExhaustion(0.8F);
    } else {
      addExhaustion(0.2F);
    } 
  }
  
  public void moveEntityWithHeading(float strafe, float forward) {
    double d0 = this.posX;
    double d1 = this.posY;
    double d2 = this.posZ;
    if (this.capabilities.isFlying && this.ridingEntity == null) {
      double d3 = this.motionY;
      float f = this.jumpMovementFactor;
      this.jumpMovementFactor = this.capabilities.getFlySpeed() * (isSprinting() ? 2 : true);
      super.moveEntityWithHeading(strafe, forward);
      this.motionY = d3 * 0.6D;
      this.jumpMovementFactor = f;
    } else {
      super.moveEntityWithHeading(strafe, forward);
    } 
    addMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
  }
  
  public float getAIMoveSpeed() {
    return (float)getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
  }
  
  public void addMovementStat(double p_71000_1_, double p_71000_3_, double p_71000_5_) {
    if (this.ridingEntity == null)
      if (isInsideOfMaterial(Material.water)) {
        int i = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0F);
        if (i > 0) {
          addStat(StatList.distanceDoveStat, i);
          addExhaustion(0.015F * i * 0.01F);
        } 
      } else if (isInWater()) {
        int j = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
        if (j > 0) {
          addStat(StatList.distanceSwumStat, j);
          addExhaustion(0.015F * j * 0.01F);
        } 
      } else if (isOnLadder()) {
        if (p_71000_3_ > 0.0D)
          addStat(StatList.distanceClimbedStat, (int)Math.round(p_71000_3_ * 100.0D)); 
      } else if (this.onGround) {
        int k = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
        if (k > 0) {
          addStat(StatList.distanceWalkedStat, k);
          if (isSprinting()) {
            addStat(StatList.distanceSprintedStat, k);
            addExhaustion(0.099999994F * k * 0.01F);
          } else {
            if (isSneaking())
              addStat(StatList.distanceCrouchedStat, k); 
            addExhaustion(0.01F * k * 0.01F);
          } 
        } 
      } else {
        int l = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
        if (l > 25)
          addStat(StatList.distanceFlownStat, l); 
      }  
  }
  
  private void addMountedMovementStat(double p_71015_1_, double p_71015_3_, double p_71015_5_) {
    if (this.ridingEntity != null) {
      int i = Math.round(MathHelper.sqrt_double(p_71015_1_ * p_71015_1_ + p_71015_3_ * p_71015_3_ + p_71015_5_ * p_71015_5_) * 100.0F);
      if (i > 0)
        if (this.ridingEntity instanceof net.minecraft.entity.item.EntityMinecart) {
          addStat(StatList.distanceByMinecartStat, i);
          if (this.startMinecartRidingCoordinate == null) {
            this.startMinecartRidingCoordinate = new BlockPos((Entity)this);
          } else if (this.startMinecartRidingCoordinate.distanceSq(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) >= 1000000.0D) {
            triggerAchievement((StatBase)AchievementList.onARail);
          } 
        } else if (this.ridingEntity instanceof net.minecraft.entity.item.EntityBoat) {
          addStat(StatList.distanceByBoatStat, i);
        } else if (this.ridingEntity instanceof EntityPig) {
          addStat(StatList.distanceByPigStat, i);
        } else if (this.ridingEntity instanceof EntityHorse) {
          addStat(StatList.distanceByHorseStat, i);
        }  
    } 
  }
  
  public void fall(float distance, float damageMultiplier) {
    if (!this.capabilities.allowFlying) {
      if (distance >= 2.0F)
        addStat(StatList.distanceFallenStat, (int)Math.round(distance * 100.0D)); 
      super.fall(distance, damageMultiplier);
    } 
  }
  
  protected void resetHeight() {
    if (!isSpectator())
      super.resetHeight(); 
  }
  
  protected String getFallSoundString(int damageValue) {
    return (damageValue > 4) ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
  }
  
  public void onKillEntity(EntityLivingBase entityLivingIn) {
    if (entityLivingIn instanceof net.minecraft.entity.monster.IMob)
      triggerAchievement((StatBase)AchievementList.killEnemy); 
    EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(EntityList.getEntityID((Entity)entityLivingIn)));
    if (entitylist$entityegginfo != null)
      triggerAchievement(entitylist$entityegginfo.field_151512_d); 
  }
  
  public void setInWeb() {
    if (!this.capabilities.isFlying)
      super.setInWeb(); 
  }
  
  public ItemStack getCurrentArmor(int slotIn) {
    return this.inventory.armorItemInSlot(slotIn);
  }
  
  public void addExperience(int amount) {
    addScore(amount);
    int i = Integer.MAX_VALUE - this.experienceTotal;
    if (amount > i)
      amount = i; 
    this.experience += amount / xpBarCap();
    for (this.experienceTotal += amount; this.experience >= 1.0F; this.experience /= xpBarCap()) {
      this.experience = (this.experience - 1.0F) * xpBarCap();
      addExperienceLevel(1);
    } 
  }
  
  public int getXPSeed() {
    return this.xpSeed;
  }
  
  public void removeExperienceLevel(int levels) {
    this.experienceLevel -= levels;
    if (this.experienceLevel < 0) {
      this.experienceLevel = 0;
      this.experience = 0.0F;
      this.experienceTotal = 0;
    } 
    this.xpSeed = this.rand.nextInt();
  }
  
  public void addExperienceLevel(int levels) {
    this.experienceLevel += levels;
    if (this.experienceLevel < 0) {
      this.experienceLevel = 0;
      this.experience = 0.0F;
      this.experienceTotal = 0;
    } 
    if (levels > 0 && this.experienceLevel % 5 == 0 && this.lastXPSound < this.ticksExisted - 100.0F) {
      float f = (this.experienceLevel > 30) ? 1.0F : (this.experienceLevel / 30.0F);
      this.worldObj.playSoundAtEntity((Entity)this, "random.levelup", f * 0.75F, 1.0F);
      this.lastXPSound = this.ticksExisted;
    } 
  }
  
  public int xpBarCap() {
    return (this.experienceLevel >= 30) ? (112 + (this.experienceLevel - 30) * 9) : ((this.experienceLevel >= 15) ? (37 + (this.experienceLevel - 15) * 5) : (7 + this.experienceLevel * 2));
  }
  
  public void addExhaustion(float p_71020_1_) {
    if (!this.capabilities.disableDamage)
      if (!this.worldObj.isRemote)
        this.foodStats.addExhaustion(p_71020_1_);  
  }
  
  public FoodStats getFoodStats() {
    return this.foodStats;
  }
  
  public boolean canEat(boolean ignoreHunger) {
    return ((ignoreHunger || this.foodStats.needFood()) && !this.capabilities.disableDamage);
  }
  
  public boolean shouldHeal() {
    return (getHealth() > 0.0F && getHealth() < getMaxHealth());
  }
  
  public void setItemInUse(ItemStack stack, int duration) {
    if (stack != this.itemInUse) {
      this.itemInUse = stack;
      this.itemInUseCount = duration;
      if (!this.worldObj.isRemote)
        setEating(true); 
    } 
  }
  
  public boolean isAllowEdit() {
    return this.capabilities.allowEdit;
  }
  
  public boolean canPlayerEdit(BlockPos p_175151_1_, EnumFacing p_175151_2_, ItemStack p_175151_3_) {
    if (this.capabilities.allowEdit)
      return true; 
    if (p_175151_3_ == null)
      return false; 
    BlockPos blockpos = p_175151_1_.offset(p_175151_2_.getOpposite());
    Block block = this.worldObj.getBlockState(blockpos).getBlock();
    return (p_175151_3_.canPlaceOn(block) || p_175151_3_.canEditBlocks());
  }
  
  protected int getExperiencePoints(EntityPlayer player) {
    if (this.worldObj.getGameRules().getBoolean("keepInventory"))
      return 0; 
    int i = this.experienceLevel * 7;
    return (i > 100) ? 100 : i;
  }
  
  protected boolean isPlayer() {
    return true;
  }
  
  public boolean getAlwaysRenderNameTagForRender() {
    return true;
  }
  
  public void clonePlayer(EntityPlayer oldPlayer, boolean respawnFromEnd) {
    if (respawnFromEnd) {
      this.inventory.copyInventory(oldPlayer.inventory);
      setHealth(oldPlayer.getHealth());
      this.foodStats = oldPlayer.foodStats;
      this.experienceLevel = oldPlayer.experienceLevel;
      this.experienceTotal = oldPlayer.experienceTotal;
      this.experience = oldPlayer.experience;
      setScore(oldPlayer.getScore());
      this.field_181016_an = oldPlayer.field_181016_an;
      this.field_181017_ao = oldPlayer.field_181017_ao;
      this.field_181018_ap = oldPlayer.field_181018_ap;
    } else if (this.worldObj.getGameRules().getBoolean("keepInventory")) {
      this.inventory.copyInventory(oldPlayer.inventory);
      this.experienceLevel = oldPlayer.experienceLevel;
      this.experienceTotal = oldPlayer.experienceTotal;
      this.experience = oldPlayer.experience;
      setScore(oldPlayer.getScore());
    } 
    this.xpSeed = oldPlayer.xpSeed;
    this.theInventoryEnderChest = oldPlayer.theInventoryEnderChest;
    getDataWatcher().updateObject(10, Byte.valueOf(oldPlayer.getDataWatcher().getWatchableObjectByte(10)));
  }
  
  protected boolean canTriggerWalking() {
    return !this.capabilities.isFlying;
  }
  
  public void sendPlayerAbilities() {}
  
  public void setGameType(WorldSettings.GameType gameType) {}
  
  public String getName() {
    return this.gameProfile.getName();
  }
  
  public InventoryEnderChest getInventoryEnderChest() {
    return this.theInventoryEnderChest;
  }
  
  public ItemStack getEquipmentInSlot(int slotIn) {
    return (slotIn == 0) ? this.inventory.getCurrentItem() : this.inventory.armorInventory[slotIn - 1];
  }
  
  public ItemStack getHeldItem() {
    return this.inventory.getCurrentItem();
  }
  
  public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
    this.inventory.armorInventory[slotIn] = stack;
  }
  
  public boolean isInvisibleToPlayer(EntityPlayer player) {
    if (!isInvisible())
      return false; 
    if (player.isSpectator())
      return false; 
    Team team = getTeam();
    return (team == null || player == null || player.getTeam() != team || !team.getSeeFriendlyInvisiblesEnabled());
  }
  
  public abstract boolean isSpectator();
  
  public ItemStack[] getInventory() {
    return this.inventory.armorInventory;
  }
  
  public boolean isPushedByWater() {
    return !this.capabilities.isFlying;
  }
  
  public Scoreboard getWorldScoreboard() {
    return this.worldObj.getScoreboard();
  }
  
  public Team getTeam() {
    return (Team)getWorldScoreboard().getPlayersTeam(getName());
  }
  
  public IChatComponent getDisplayName() {
    ChatComponentText chatComponentText = new ChatComponentText(ScorePlayerTeam.formatPlayerName(getTeam(), getName()));
    chatComponentText.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + getName() + " "));
    chatComponentText.getChatStyle().setChatHoverEvent(getHoverEvent());
    chatComponentText.getChatStyle().setInsertion(getName());
    return (IChatComponent)chatComponentText;
  }
  
  public float getEyeHeight() {
    float f = 1.62F;
    if (isPlayerSleeping())
      f = 0.2F; 
    if (isSneaking())
      f -= 0.08F; 
    return f;
  }
  
  public void setAbsorptionAmount(float amount) {
    if (amount < 0.0F)
      amount = 0.0F; 
    getDataWatcher().updateObject(17, Float.valueOf(amount));
  }
  
  public float getAbsorptionAmount() {
    return getDataWatcher().getWatchableObjectFloat(17);
  }
  
  public static UUID getUUID(GameProfile profile) {
    UUID uuid = profile.getId();
    if (uuid == null)
      uuid = getOfflineUUID(profile.getName()); 
    return uuid;
  }
  
  public static UUID getOfflineUUID(String username) {
    return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8));
  }
  
  public boolean canOpen(LockCode code) {
    if (code.isEmpty())
      return true; 
    ItemStack itemstack = getCurrentEquippedItem();
    return (itemstack != null && itemstack.hasDisplayName()) ? itemstack.getDisplayName().equals(code.getLock()) : false;
  }
  
  public boolean isWearing(EnumPlayerModelParts p_175148_1_) {
    return ((getDataWatcher().getWatchableObjectByte(10) & p_175148_1_.getPartMask()) == p_175148_1_.getPartMask());
  }
  
  public boolean sendCommandFeedback() {
    return (MinecraftServer.getServer()).worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
  }
  
  public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
    if (inventorySlot >= 0 && inventorySlot < this.inventory.mainInventory.length) {
      this.inventory.setInventorySlotContents(inventorySlot, itemStackIn);
      return true;
    } 
    int i = inventorySlot - 100;
    if (i >= 0 && i < this.inventory.armorInventory.length) {
      int k = i + 1;
      if (itemStackIn != null && itemStackIn.getItem() != null)
        if (itemStackIn.getItem() instanceof net.minecraft.item.ItemArmor) {
          if (EntityLiving.getArmorPosition(itemStackIn) != k)
            return false; 
        } else if (k != 4 || (itemStackIn.getItem() != Items.skull && !(itemStackIn.getItem() instanceof net.minecraft.item.ItemBlock))) {
          return false;
        }  
      this.inventory.setInventorySlotContents(i + this.inventory.mainInventory.length, itemStackIn);
      return true;
    } 
    int j = inventorySlot - 200;
    if (j >= 0 && j < this.theInventoryEnderChest.getSizeInventory()) {
      this.theInventoryEnderChest.setInventorySlotContents(j, itemStackIn);
      return true;
    } 
    return false;
  }
  
  public boolean hasReducedDebug() {
    return this.hasReducedDebug;
  }
  
  public void setReducedDebug(boolean reducedDebug) {
    this.hasReducedDebug = reducedDebug;
  }
  
  public enum EnumChatVisibility {
    FULL(0, "options.chat.visibility.full"),
    SYSTEM(1, "options.chat.visibility.system"),
    HIDDEN(2, "options.chat.visibility.hidden");
    
    private static final EnumChatVisibility[] ID_LOOKUP = new EnumChatVisibility[(values()).length];
    
    private final int chatVisibility;
    
    private final String resourceKey;
    
    static {
      for (EnumChatVisibility entityplayer$enumchatvisibility : values())
        ID_LOOKUP[entityplayer$enumchatvisibility.chatVisibility] = entityplayer$enumchatvisibility; 
    }
    
    EnumChatVisibility(int id, String resourceKey) {
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
  }
  
  public enum EnumStatus {
    OK, NOT_POSSIBLE_HERE, NOT_POSSIBLE_NOW, TOO_FAR_AWAY, OTHER_PROBLEM, NOT_SAFE;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\player\EntityPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */