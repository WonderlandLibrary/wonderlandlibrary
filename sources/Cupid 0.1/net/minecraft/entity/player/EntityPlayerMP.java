package net.minecraft.entity.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.JsonSerializableSet;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityPlayerMP extends EntityPlayer implements ICrafting {
  private static final Logger logger = LogManager.getLogger();
  
  private String translator = "en_US";
  
  public NetHandlerPlayServer playerNetServerHandler;
  
  public final MinecraftServer mcServer;
  
  public final ItemInWorldManager theItemInWorldManager;
  
  public double managedPosX;
  
  public double managedPosZ;
  
  public final List<ChunkCoordIntPair> loadedChunks = Lists.newLinkedList();
  
  private final List<Integer> destroyedItemsNetCache = Lists.newLinkedList();
  
  private final StatisticsFile statsFile;
  
  private float combinedHealth = Float.MIN_VALUE;
  
  private float lastHealth = -1.0E8F;
  
  private int lastFoodLevel = -99999999;
  
  private boolean wasHungry = true;
  
  private int lastExperience = -99999999;
  
  private int respawnInvulnerabilityTicks = 60;
  
  private EntityPlayer.EnumChatVisibility chatVisibility;
  
  private boolean chatColours = true;
  
  private long playerLastActiveTime = System.currentTimeMillis();
  
  private Entity spectatingEntity = null;
  
  private int currentWindowId;
  
  public boolean isChangingQuantityOnly;
  
  public int ping;
  
  public boolean playerConqueredTheEnd;
  
  public EntityPlayerMP(MinecraftServer server, WorldServer worldIn, GameProfile profile, ItemInWorldManager interactionManager) {
    super((World)worldIn, profile);
    interactionManager.thisPlayerMP = this;
    this.theItemInWorldManager = interactionManager;
    BlockPos blockpos = worldIn.getSpawnPoint();
    if (!worldIn.provider.getHasNoSky() && worldIn.getWorldInfo().getGameType() != WorldSettings.GameType.ADVENTURE) {
      int i = Math.max(5, server.getSpawnProtectionSize() - 6);
      int j = MathHelper.floor_double(worldIn.getWorldBorder().getClosestDistance(blockpos.getX(), blockpos.getZ()));
      if (j < i)
        i = j; 
      if (j <= 1)
        i = 1; 
      blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos.add(this.rand.nextInt(i * 2) - i, 0, this.rand.nextInt(i * 2) - i));
    } 
    this.mcServer = server;
    this.statsFile = server.getConfigurationManager().getPlayerStatsFile(this);
    this.stepHeight = 0.0F;
    moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
    while (!worldIn.getCollidingBoundingBoxes((Entity)this, getEntityBoundingBox()).isEmpty() && this.posY < 255.0D)
      setPosition(this.posX, this.posY + 1.0D, this.posZ); 
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund) {
    super.readEntityFromNBT(tagCompund);
    if (tagCompund.hasKey("playerGameType", 99))
      if (MinecraftServer.getServer().getForceGamemode()) {
        this.theItemInWorldManager.setGameType(MinecraftServer.getServer().getGameType());
      } else {
        this.theItemInWorldManager.setGameType(WorldSettings.GameType.getByID(tagCompund.getInteger("playerGameType")));
      }  
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound) {
    super.writeEntityToNBT(tagCompound);
    tagCompound.setInteger("playerGameType", this.theItemInWorldManager.getGameType().getID());
  }
  
  public void addExperienceLevel(int levels) {
    super.addExperienceLevel(levels);
    this.lastExperience = -1;
  }
  
  public void removeExperienceLevel(int levels) {
    super.removeExperienceLevel(levels);
    this.lastExperience = -1;
  }
  
  public void addSelfToInternalCraftingInventory() {
    this.openContainer.onCraftGuiOpened(this);
  }
  
  public void sendEnterCombat() {
    super.sendEnterCombat();
    this.playerNetServerHandler.sendPacket((Packet)new S42PacketCombatEvent(getCombatTracker(), S42PacketCombatEvent.Event.ENTER_COMBAT));
  }
  
  public void sendEndCombat() {
    super.sendEndCombat();
    this.playerNetServerHandler.sendPacket((Packet)new S42PacketCombatEvent(getCombatTracker(), S42PacketCombatEvent.Event.END_COMBAT));
  }
  
  public void onUpdate() {
    this.theItemInWorldManager.updateBlockRemoving();
    this.respawnInvulnerabilityTicks--;
    if (this.hurtResistantTime > 0)
      this.hurtResistantTime--; 
    this.openContainer.detectAndSendChanges();
    if (!this.worldObj.isRemote && !this.openContainer.canInteractWith(this)) {
      closeScreen();
      this.openContainer = this.inventoryContainer;
    } 
    while (!this.destroyedItemsNetCache.isEmpty()) {
      int i = Math.min(this.destroyedItemsNetCache.size(), 2147483647);
      int[] aint = new int[i];
      Iterator<Integer> iterator = this.destroyedItemsNetCache.iterator();
      int j = 0;
      while (iterator.hasNext() && j < i) {
        aint[j++] = ((Integer)iterator.next()).intValue();
        iterator.remove();
      } 
      this.playerNetServerHandler.sendPacket((Packet)new S13PacketDestroyEntities(aint));
    } 
    if (!this.loadedChunks.isEmpty()) {
      List<Chunk> list = Lists.newArrayList();
      Iterator<ChunkCoordIntPair> iterator1 = this.loadedChunks.iterator();
      List<TileEntity> list1 = Lists.newArrayList();
      while (iterator1.hasNext() && list.size() < 10) {
        ChunkCoordIntPair chunkcoordintpair = iterator1.next();
        if (chunkcoordintpair != null) {
          if (this.worldObj.isBlockLoaded(new BlockPos(chunkcoordintpair.chunkXPos << 4, 0, chunkcoordintpair.chunkZPos << 4))) {
            Chunk chunk = this.worldObj.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
            if (chunk.isPopulated()) {
              list.add(chunk);
              list1.addAll(((WorldServer)this.worldObj).getTileEntitiesIn(chunkcoordintpair.chunkXPos * 16, 0, chunkcoordintpair.chunkZPos * 16, chunkcoordintpair.chunkXPos * 16 + 16, 256, chunkcoordintpair.chunkZPos * 16 + 16));
              iterator1.remove();
            } 
          } 
          continue;
        } 
        iterator1.remove();
      } 
      if (!list.isEmpty()) {
        if (list.size() == 1) {
          this.playerNetServerHandler.sendPacket((Packet)new S21PacketChunkData(list.get(0), true, 65535));
        } else {
          this.playerNetServerHandler.sendPacket((Packet)new S26PacketMapChunkBulk(list));
        } 
        for (TileEntity tileentity : list1)
          sendTileEntityUpdate(tileentity); 
        for (Chunk chunk1 : list)
          getServerForPlayer().getEntityTracker().func_85172_a(this, chunk1); 
      } 
    } 
    Entity entity = getSpectatingEntity();
    if (entity != this)
      if (!entity.isEntityAlive()) {
        setSpectatingEntity((Entity)this);
      } else {
        setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
        this.mcServer.getConfigurationManager().serverUpdateMountedMovingPlayer(this);
        if (isSneaking())
          setSpectatingEntity((Entity)this); 
      }  
  }
  
  public void onUpdateEntity() {
    try {
      super.onUpdate();
      for (int i = 0; i < this.inventory.getSizeInventory(); i++) {
        ItemStack itemstack = this.inventory.getStackInSlot(i);
        if (itemstack != null && itemstack.getItem().isMap()) {
          Packet packet = ((ItemMapBase)itemstack.getItem()).createMapDataPacket(itemstack, this.worldObj, this);
          if (packet != null)
            this.playerNetServerHandler.sendPacket(packet); 
        } 
      } 
      if (getHealth() != this.lastHealth || this.lastFoodLevel != this.foodStats.getFoodLevel() || ((this.foodStats.getSaturationLevel() == 0.0F)) != this.wasHungry) {
        this.playerNetServerHandler.sendPacket((Packet)new S06PacketUpdateHealth(getHealth(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
        this.lastHealth = getHealth();
        this.lastFoodLevel = this.foodStats.getFoodLevel();
        this.wasHungry = (this.foodStats.getSaturationLevel() == 0.0F);
      } 
      if (getHealth() + getAbsorptionAmount() != this.combinedHealth) {
        this.combinedHealth = getHealth() + getAbsorptionAmount();
        for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.health)) {
          getWorldScoreboard().getValueFromObjective(getName(), scoreobjective).func_96651_a(Arrays.asList(new EntityPlayer[] { this }));
        } 
      } 
      if (this.experienceTotal != this.lastExperience) {
        this.lastExperience = this.experienceTotal;
        this.playerNetServerHandler.sendPacket((Packet)new S1FPacketSetExperience(this.experience, this.experienceTotal, this.experienceLevel));
      } 
      if (this.ticksExisted % 20 * 5 == 0 && !getStatFile().hasAchievementUnlocked(AchievementList.exploreAllBiomes))
        updateBiomesExplored(); 
    } catch (Throwable throwable) {
      CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
      CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
      addEntityCrashInfo(crashreportcategory);
      throw new ReportedException(crashreport);
    } 
  }
  
  protected void updateBiomesExplored() {
    BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ)));
    String s = biomegenbase.biomeName;
    JsonSerializableSet jsonserializableset = (JsonSerializableSet)getStatFile().func_150870_b((StatBase)AchievementList.exploreAllBiomes);
    if (jsonserializableset == null)
      jsonserializableset = (JsonSerializableSet)getStatFile().func_150872_a((StatBase)AchievementList.exploreAllBiomes, (IJsonSerializable)new JsonSerializableSet()); 
    jsonserializableset.add(s);
    if (getStatFile().canUnlockAchievement(AchievementList.exploreAllBiomes) && jsonserializableset.size() >= BiomeGenBase.explorationBiomesList.size()) {
      Set<BiomeGenBase> set = Sets.newHashSet(BiomeGenBase.explorationBiomesList);
      for (String s1 : jsonserializableset) {
        Iterator<BiomeGenBase> iterator = set.iterator();
        while (iterator.hasNext()) {
          BiomeGenBase biomegenbase1 = iterator.next();
          if (biomegenbase1.biomeName.equals(s1))
            iterator.remove(); 
        } 
        if (set.isEmpty())
          break; 
      } 
      if (set.isEmpty())
        triggerAchievement((StatBase)AchievementList.exploreAllBiomes); 
    } 
  }
  
  public void onDeath(DamageSource cause) {
    if (this.worldObj.getGameRules().getBoolean("showDeathMessages")) {
      Team team = getTeam();
      if (team != null && team.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS) {
        if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS) {
          this.mcServer.getConfigurationManager().sendMessageToAllTeamMembers(this, getCombatTracker().getDeathMessage());
        } else if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM) {
          this.mcServer.getConfigurationManager().sendMessageToTeamOrEvryPlayer(this, getCombatTracker().getDeathMessage());
        } 
      } else {
        this.mcServer.getConfigurationManager().sendChatMsg(getCombatTracker().getDeathMessage());
      } 
    } 
    if (!this.worldObj.getGameRules().getBoolean("keepInventory"))
      this.inventory.dropAllItems(); 
    for (ScoreObjective scoreobjective : this.worldObj.getScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.deathCount)) {
      Score score = getWorldScoreboard().getValueFromObjective(getName(), scoreobjective);
      score.func_96648_a();
    } 
    EntityLivingBase entitylivingbase = func_94060_bK();
    if (entitylivingbase != null) {
      EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(EntityList.getEntityID((Entity)entitylivingbase)));
      if (entitylist$entityegginfo != null)
        triggerAchievement(entitylist$entityegginfo.field_151513_e); 
      entitylivingbase.addToPlayerScore((Entity)this, this.scoreValue);
    } 
    triggerAchievement(StatList.deathsStat);
    func_175145_a(StatList.timeSinceDeathStat);
    getCombatTracker().reset();
  }
  
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (isEntityInvulnerable(source))
      return false; 
    boolean flag = (this.mcServer.isDedicatedServer() && canPlayersAttack() && "fall".equals(source.damageType));
    if (!flag && this.respawnInvulnerabilityTicks > 0 && source != DamageSource.outOfWorld)
      return false; 
    if (source instanceof net.minecraft.util.EntityDamageSource) {
      Entity entity = source.getEntity();
      if (entity instanceof EntityPlayer && !canAttackPlayer((EntityPlayer)entity))
        return false; 
      if (entity instanceof EntityArrow) {
        EntityArrow entityarrow = (EntityArrow)entity;
        if (entityarrow.shootingEntity instanceof EntityPlayer && !canAttackPlayer((EntityPlayer)entityarrow.shootingEntity))
          return false; 
      } 
    } 
    return super.attackEntityFrom(source, amount);
  }
  
  public boolean canAttackPlayer(EntityPlayer other) {
    return !canPlayersAttack() ? false : super.canAttackPlayer(other);
  }
  
  private boolean canPlayersAttack() {
    return this.mcServer.isPVPEnabled();
  }
  
  public void travelToDimension(int dimensionId) {
    if (this.dimension == 1 && dimensionId == 1) {
      triggerAchievement((StatBase)AchievementList.theEnd2);
      this.worldObj.removeEntity((Entity)this);
      this.playerConqueredTheEnd = true;
      this.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(4, 0.0F));
    } else {
      if (this.dimension == 0 && dimensionId == 1) {
        triggerAchievement((StatBase)AchievementList.theEnd);
        BlockPos blockpos = this.mcServer.worldServerForDimension(dimensionId).getSpawnCoordinate();
        if (blockpos != null)
          this.playerNetServerHandler.setPlayerLocation(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 0.0F, 0.0F); 
        dimensionId = 1;
      } else {
        triggerAchievement((StatBase)AchievementList.portal);
      } 
      this.mcServer.getConfigurationManager().transferPlayerToDimension(this, dimensionId);
      this.lastExperience = -1;
      this.lastHealth = -1.0F;
      this.lastFoodLevel = -1;
    } 
  }
  
  public boolean isSpectatedByPlayer(EntityPlayerMP player) {
    return player.isSpectator() ? ((getSpectatingEntity() == this)) : (isSpectator() ? false : super.isSpectatedByPlayer(player));
  }
  
  private void sendTileEntityUpdate(TileEntity p_147097_1_) {
    if (p_147097_1_ != null) {
      Packet packet = p_147097_1_.getDescriptionPacket();
      if (packet != null)
        this.playerNetServerHandler.sendPacket(packet); 
    } 
  }
  
  public void onItemPickup(Entity p_71001_1_, int p_71001_2_) {
    super.onItemPickup(p_71001_1_, p_71001_2_);
    this.openContainer.detectAndSendChanges();
  }
  
  public EntityPlayer.EnumStatus trySleep(BlockPos bedLocation) {
    EntityPlayer.EnumStatus entityplayer$enumstatus = super.trySleep(bedLocation);
    if (entityplayer$enumstatus == EntityPlayer.EnumStatus.OK) {
      S0APacketUseBed s0APacketUseBed = new S0APacketUseBed(this, bedLocation);
      getServerForPlayer().getEntityTracker().sendToAllTrackingEntity((Entity)this, (Packet)s0APacketUseBed);
      this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
      this.playerNetServerHandler.sendPacket((Packet)s0APacketUseBed);
    } 
    return entityplayer$enumstatus;
  }
  
  public void wakeUpPlayer(boolean p_70999_1_, boolean updateWorldFlag, boolean setSpawn) {
    if (isPlayerSleeping())
      getServerForPlayer().getEntityTracker().func_151248_b((Entity)this, (Packet)new S0BPacketAnimation((Entity)this, 2)); 
    super.wakeUpPlayer(p_70999_1_, updateWorldFlag, setSpawn);
    if (this.playerNetServerHandler != null)
      this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch); 
  }
  
  public void mountEntity(Entity entityIn) {
    Entity entity = this.ridingEntity;
    super.mountEntity(entityIn);
    if (entityIn != entity) {
      this.playerNetServerHandler.sendPacket((Packet)new S1BPacketEntityAttach(0, (Entity)this, this.ridingEntity));
      this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    } 
  }
  
  protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {}
  
  public void handleFalling(double p_71122_1_, boolean p_71122_3_) {
    int i = MathHelper.floor_double(this.posX);
    int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
    int k = MathHelper.floor_double(this.posZ);
    BlockPos blockpos = new BlockPos(i, j, k);
    Block block = this.worldObj.getBlockState(blockpos).getBlock();
    if (block.getMaterial() == Material.air) {
      Block block1 = this.worldObj.getBlockState(blockpos.down()).getBlock();
      if (block1 instanceof net.minecraft.block.BlockFence || block1 instanceof net.minecraft.block.BlockWall || block1 instanceof net.minecraft.block.BlockFenceGate) {
        blockpos = blockpos.down();
        block = this.worldObj.getBlockState(blockpos).getBlock();
      } 
    } 
    super.updateFallState(p_71122_1_, p_71122_3_, block, blockpos);
  }
  
  public void openEditSign(TileEntitySign signTile) {
    signTile.setPlayer(this);
    this.playerNetServerHandler.sendPacket((Packet)new S36PacketSignEditorOpen(signTile.getPos()));
  }
  
  private void getNextWindowId() {
    this.currentWindowId = this.currentWindowId % 100 + 1;
  }
  
  public void displayGui(IInteractionObject guiOwner) {
    getNextWindowId();
    this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, guiOwner.getGuiID(), guiOwner.getDisplayName()));
    this.openContainer = guiOwner.createContainer(this.inventory, this);
    this.openContainer.windowId = this.currentWindowId;
    this.openContainer.onCraftGuiOpened(this);
  }
  
  public void displayGUIChest(IInventory chestInventory) {
    if (this.openContainer != this.inventoryContainer)
      closeScreen(); 
    if (chestInventory instanceof ILockableContainer) {
      ILockableContainer ilockablecontainer = (ILockableContainer)chestInventory;
      if (ilockablecontainer.isLocked() && !canOpen(ilockablecontainer.getLockCode()) && !isSpectator()) {
        this.playerNetServerHandler.sendPacket((Packet)new S02PacketChat((IChatComponent)new ChatComponentTranslation("container.isLocked", new Object[] { chestInventory.getDisplayName() }), (byte)2));
        this.playerNetServerHandler.sendPacket((Packet)new S29PacketSoundEffect("random.door_close", this.posX, this.posY, this.posZ, 1.0F, 1.0F));
        return;
      } 
    } 
    getNextWindowId();
    if (chestInventory instanceof IInteractionObject) {
      this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, ((IInteractionObject)chestInventory).getGuiID(), chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
      this.openContainer = ((IInteractionObject)chestInventory).createContainer(this.inventory, this);
    } else {
      this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, "minecraft:container", chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
      this.openContainer = (Container)new ContainerChest(this.inventory, chestInventory, this);
    } 
    this.openContainer.windowId = this.currentWindowId;
    this.openContainer.onCraftGuiOpened(this);
  }
  
  public void displayVillagerTradeGui(IMerchant villager) {
    getNextWindowId();
    this.openContainer = (Container)new ContainerMerchant(this.inventory, villager, this.worldObj);
    this.openContainer.windowId = this.currentWindowId;
    this.openContainer.onCraftGuiOpened(this);
    InventoryMerchant inventoryMerchant = ((ContainerMerchant)this.openContainer).getMerchantInventory();
    IChatComponent ichatcomponent = villager.getDisplayName();
    this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, "minecraft:villager", ichatcomponent, inventoryMerchant.getSizeInventory()));
    MerchantRecipeList merchantrecipelist = villager.getRecipes(this);
    if (merchantrecipelist != null) {
      PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
      packetbuffer.writeInt(this.currentWindowId);
      merchantrecipelist.writeToBuf(packetbuffer);
      this.playerNetServerHandler.sendPacket((Packet)new S3FPacketCustomPayload("MC|TrList", packetbuffer));
    } 
  }
  
  public void displayGUIHorse(EntityHorse horse, IInventory horseInventory) {
    if (this.openContainer != this.inventoryContainer)
      closeScreen(); 
    getNextWindowId();
    this.playerNetServerHandler.sendPacket((Packet)new S2DPacketOpenWindow(this.currentWindowId, "EntityHorse", horseInventory.getDisplayName(), horseInventory.getSizeInventory(), horse.getEntityId()));
    this.openContainer = (Container)new ContainerHorseInventory(this.inventory, horseInventory, horse, this);
    this.openContainer.windowId = this.currentWindowId;
    this.openContainer.onCraftGuiOpened(this);
  }
  
  public void displayGUIBook(ItemStack bookStack) {
    Item item = bookStack.getItem();
    if (item == Items.written_book)
      this.playerNetServerHandler.sendPacket((Packet)new S3FPacketCustomPayload("MC|BOpen", new PacketBuffer(Unpooled.buffer()))); 
  }
  
  public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack) {
    if (!(containerToSend.getSlot(slotInd) instanceof net.minecraft.inventory.SlotCrafting))
      if (!this.isChangingQuantityOnly)
        this.playerNetServerHandler.sendPacket((Packet)new S2FPacketSetSlot(containerToSend.windowId, slotInd, stack));  
  }
  
  public void sendContainerToPlayer(Container p_71120_1_) {
    updateCraftingInventory(p_71120_1_, p_71120_1_.getInventory());
  }
  
  public void updateCraftingInventory(Container containerToSend, List<ItemStack> itemsList) {
    this.playerNetServerHandler.sendPacket((Packet)new S30PacketWindowItems(containerToSend.windowId, itemsList));
    this.playerNetServerHandler.sendPacket((Packet)new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack()));
  }
  
  public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue) {
    this.playerNetServerHandler.sendPacket((Packet)new S31PacketWindowProperty(containerIn.windowId, varToUpdate, newValue));
  }
  
  public void func_175173_a(Container p_175173_1_, IInventory p_175173_2_) {
    for (int i = 0; i < p_175173_2_.getFieldCount(); i++)
      this.playerNetServerHandler.sendPacket((Packet)new S31PacketWindowProperty(p_175173_1_.windowId, i, p_175173_2_.getField(i))); 
  }
  
  public void closeScreen() {
    this.playerNetServerHandler.sendPacket((Packet)new S2EPacketCloseWindow(this.openContainer.windowId));
    closeContainer();
  }
  
  public void updateHeldItem() {
    if (!this.isChangingQuantityOnly)
      this.playerNetServerHandler.sendPacket((Packet)new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack())); 
  }
  
  public void closeContainer() {
    this.openContainer.onContainerClosed(this);
    this.openContainer = this.inventoryContainer;
  }
  
  public void setEntityActionState(float p_110430_1_, float p_110430_2_, boolean p_110430_3_, boolean sneaking) {
    if (this.ridingEntity != null) {
      if (p_110430_1_ >= -1.0F && p_110430_1_ <= 1.0F)
        this.moveStrafing = p_110430_1_; 
      if (p_110430_2_ >= -1.0F && p_110430_2_ <= 1.0F)
        this.moveForward = p_110430_2_; 
      this.isJumping = p_110430_3_;
      setSneaking(sneaking);
    } 
  }
  
  public void addStat(StatBase stat, int amount) {
    if (stat != null) {
      this.statsFile.increaseStat(this, stat, amount);
      for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(stat.func_150952_k()))
        getWorldScoreboard().getValueFromObjective(getName(), scoreobjective).increseScore(amount); 
      if (this.statsFile.func_150879_e())
        this.statsFile.func_150876_a(this); 
    } 
  }
  
  public void func_175145_a(StatBase p_175145_1_) {
    if (p_175145_1_ != null) {
      this.statsFile.unlockAchievement(this, p_175145_1_, 0);
      for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(p_175145_1_.func_150952_k()))
        getWorldScoreboard().getValueFromObjective(getName(), scoreobjective).setScorePoints(0); 
      if (this.statsFile.func_150879_e())
        this.statsFile.func_150876_a(this); 
    } 
  }
  
  public void mountEntityAndWakeUp() {
    if (this.riddenByEntity != null)
      this.riddenByEntity.mountEntity((Entity)this); 
    if (this.sleeping)
      wakeUpPlayer(true, false, false); 
  }
  
  public void setPlayerHealthUpdated() {
    this.lastHealth = -1.0E8F;
  }
  
  public void addChatComponentMessage(IChatComponent chatComponent) {
    this.playerNetServerHandler.sendPacket((Packet)new S02PacketChat(chatComponent));
  }
  
  protected void onItemUseFinish() {
    this.playerNetServerHandler.sendPacket((Packet)new S19PacketEntityStatus((Entity)this, (byte)9));
    super.onItemUseFinish();
  }
  
  public void setItemInUse(ItemStack stack, int duration) {
    super.setItemInUse(stack, duration);
    if (stack != null && stack.getItem() != null && stack.getItem().getItemUseAction(stack) == EnumAction.EAT)
      getServerForPlayer().getEntityTracker().func_151248_b((Entity)this, (Packet)new S0BPacketAnimation((Entity)this, 3)); 
  }
  
  public void clonePlayer(EntityPlayer oldPlayer, boolean respawnFromEnd) {
    super.clonePlayer(oldPlayer, respawnFromEnd);
    this.lastExperience = -1;
    this.lastHealth = -1.0F;
    this.lastFoodLevel = -1;
    this.destroyedItemsNetCache.addAll(((EntityPlayerMP)oldPlayer).destroyedItemsNetCache);
  }
  
  protected void onNewPotionEffect(PotionEffect id) {
    super.onNewPotionEffect(id);
    this.playerNetServerHandler.sendPacket((Packet)new S1DPacketEntityEffect(getEntityId(), id));
  }
  
  protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_) {
    super.onChangedPotionEffect(id, p_70695_2_);
    this.playerNetServerHandler.sendPacket((Packet)new S1DPacketEntityEffect(getEntityId(), id));
  }
  
  protected void onFinishedPotionEffect(PotionEffect p_70688_1_) {
    super.onFinishedPotionEffect(p_70688_1_);
    this.playerNetServerHandler.sendPacket((Packet)new S1EPacketRemoveEntityEffect(getEntityId(), p_70688_1_));
  }
  
  public void setPositionAndUpdate(double x, double y, double z) {
    this.playerNetServerHandler.setPlayerLocation(x, y, z, this.rotationYaw, this.rotationPitch);
  }
  
  public void onCriticalHit(Entity entityHit) {
    getServerForPlayer().getEntityTracker().func_151248_b((Entity)this, (Packet)new S0BPacketAnimation(entityHit, 4));
  }
  
  public void onEnchantmentCritical(Entity entityHit) {
    getServerForPlayer().getEntityTracker().func_151248_b((Entity)this, (Packet)new S0BPacketAnimation(entityHit, 5));
  }
  
  public void sendPlayerAbilities() {
    if (this.playerNetServerHandler != null) {
      this.playerNetServerHandler.sendPacket((Packet)new S39PacketPlayerAbilities(this.capabilities));
      updatePotionMetadata();
    } 
  }
  
  public WorldServer getServerForPlayer() {
    return (WorldServer)this.worldObj;
  }
  
  public void setGameType(WorldSettings.GameType gameType) {
    this.theItemInWorldManager.setGameType(gameType);
    this.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(3, gameType.getID()));
    if (gameType == WorldSettings.GameType.SPECTATOR) {
      mountEntity((Entity)null);
    } else {
      setSpectatingEntity((Entity)this);
    } 
    sendPlayerAbilities();
    markPotionsDirty();
  }
  
  public boolean isSpectator() {
    return (this.theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR);
  }
  
  public void addChatMessage(IChatComponent component) {
    this.playerNetServerHandler.sendPacket((Packet)new S02PacketChat(component));
  }
  
  public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
    if ("seed".equals(commandName) && !this.mcServer.isDedicatedServer())
      return true; 
    if (!"tell".equals(commandName) && !"help".equals(commandName) && !"me".equals(commandName) && !"trigger".equals(commandName)) {
      if (this.mcServer.getConfigurationManager().canSendCommands(getGameProfile())) {
        UserListOpsEntry userlistopsentry = (UserListOpsEntry)this.mcServer.getConfigurationManager().getOppedPlayers().getEntry(getGameProfile());
        return (userlistopsentry != null) ? ((userlistopsentry.getPermissionLevel() >= permLevel)) : ((this.mcServer.getOpPermissionLevel() >= permLevel));
      } 
      return false;
    } 
    return true;
  }
  
  public String getPlayerIP() {
    String s = this.playerNetServerHandler.netManager.getRemoteAddress().toString();
    s = s.substring(s.indexOf("/") + 1);
    s = s.substring(0, s.indexOf(":"));
    return s;
  }
  
  public void handleClientSettings(C15PacketClientSettings packetIn) {
    this.translator = packetIn.getLang();
    this.chatVisibility = packetIn.getChatVisibility();
    this.chatColours = packetIn.isColorsEnabled();
    getDataWatcher().updateObject(10, Byte.valueOf((byte)packetIn.getModelPartFlags()));
  }
  
  public EntityPlayer.EnumChatVisibility getChatVisibility() {
    return this.chatVisibility;
  }
  
  public void loadResourcePack(String url, String hash) {
    this.playerNetServerHandler.sendPacket((Packet)new S48PacketResourcePackSend(url, hash));
  }
  
  public BlockPos getPosition() {
    return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
  }
  
  public void markPlayerActive() {
    this.playerLastActiveTime = MinecraftServer.getCurrentTimeMillis();
  }
  
  public StatisticsFile getStatFile() {
    return this.statsFile;
  }
  
  public void removeEntity(Entity p_152339_1_) {
    if (p_152339_1_ instanceof EntityPlayer) {
      this.playerNetServerHandler.sendPacket((Packet)new S13PacketDestroyEntities(new int[] { p_152339_1_.getEntityId() }));
    } else {
      this.destroyedItemsNetCache.add(Integer.valueOf(p_152339_1_.getEntityId()));
    } 
  }
  
  protected void updatePotionMetadata() {
    if (isSpectator()) {
      resetPotionEffectMetadata();
      setInvisible(true);
    } else {
      super.updatePotionMetadata();
    } 
    getServerForPlayer().getEntityTracker().func_180245_a(this);
  }
  
  public Entity getSpectatingEntity() {
    return (this.spectatingEntity == null) ? (Entity)this : this.spectatingEntity;
  }
  
  public void setSpectatingEntity(Entity entityToSpectate) {
    Entity entity = getSpectatingEntity();
    this.spectatingEntity = (entityToSpectate == null) ? (Entity)this : entityToSpectate;
    if (entity != this.spectatingEntity) {
      this.playerNetServerHandler.sendPacket((Packet)new S43PacketCamera(this.spectatingEntity));
      setPositionAndUpdate(this.spectatingEntity.posX, this.spectatingEntity.posY, this.spectatingEntity.posZ);
    } 
  }
  
  public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
    if (this.theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR) {
      setSpectatingEntity(targetEntity);
    } else {
      super.attackTargetEntityWithCurrentItem(targetEntity);
    } 
  }
  
  public long getLastActiveTime() {
    return this.playerLastActiveTime;
  }
  
  public IChatComponent getTabListDisplayName() {
    return null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\entity\player\EntityPlayerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */