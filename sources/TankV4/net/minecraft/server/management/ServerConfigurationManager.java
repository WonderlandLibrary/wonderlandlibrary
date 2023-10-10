package net.minecraft.server.management;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import java.io.File;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.network.play.server.S05PacketSpawnPosition;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S09PacketHeldItemChange;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S41PacketServerDifficulty;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.demo.DemoWorldManager;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ServerConfigurationManager {
   private final UserListBans bannedPlayers;
   protected int maxPlayers;
   private static final Logger logger = LogManager.getLogger();
   public static final File FILE_IPBANS = new File("banned-ips.json");
   private int playerPingIndex;
   public static final File FILE_PLAYERBANS = new File("banned-players.json");
   public static final File FILE_OPS = new File("ops.json");
   private final UserListOps ops;
   private final BanList bannedIPs;
   private boolean whiteListEnforced;
   private final List playerEntityList = Lists.newArrayList();
   private final Map playerStatFiles;
   public static final File FILE_WHITELIST = new File("whitelist.json");
   private WorldSettings.GameType gameType;
   private final Map uuidToPlayerMap = Maps.newHashMap();
   private IPlayerFileData playerNBTManagerObj;
   private final MinecraftServer mcServer;
   private int viewDistance;
   private final UserListWhitelist whiteListedPlayers;
   private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
   private boolean commandsAllowedForAll;

   public String[] getWhitelistedPlayerNames() {
      return this.whiteListedPlayers.getKeys();
   }

   public void removePlayerFromWhitelist(GameProfile var1) {
      this.whiteListedPlayers.removeEntry(var1);
   }

   public void playerLoggedIn(EntityPlayerMP var1) {
      this.playerEntityList.add(var1);
      this.uuidToPlayerMap.put(var1.getUniqueID(), var1);
      this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[]{var1}));
      WorldServer var2 = this.mcServer.worldServerForDimension(var1.dimension);
      var2.spawnEntityInWorld(var1);
      this.preparePlayer(var1, (WorldServer)null);

      for(int var3 = 0; var3 < this.playerEntityList.size(); ++var3) {
         EntityPlayerMP var4 = (EntityPlayerMP)this.playerEntityList.get(var3);
         var1.playerNetServerHandler.sendPacket(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.ADD_PLAYER, new EntityPlayerMP[]{var4}));
      }

   }

   public void setGameType(WorldSettings.GameType var1) {
      this.gameType = var1;
   }

   public EntityPlayerMP getPlayerByUsername(String var1) {
      Iterator var3 = this.playerEntityList.iterator();

      while(var3.hasNext()) {
         EntityPlayerMP var2 = (EntityPlayerMP)var3.next();
         if (var2.getName().equalsIgnoreCase(var1)) {
            return var2;
         }
      }

      return null;
   }

   protected void writePlayerData(EntityPlayerMP var1) {
      this.playerNBTManagerObj.writePlayerData(var1);
      StatisticsFile var2 = (StatisticsFile)this.playerStatFiles.get(var1.getUniqueID());
      if (var2 != null) {
         var2.saveStatFile();
      }

   }

   public int getCurrentPlayerCount() {
      return this.playerEntityList.size();
   }

   public void setWhiteListEnabled(boolean var1) {
      this.whiteListEnforced = var1;
   }

   public void loadWhiteList() {
   }

   public void serverUpdateMountedMovingPlayer(EntityPlayerMP var1) {
      var1.getServerForPlayer().getPlayerManager().updateMountedMovingPlayer(var1);
   }

   public void syncPlayerInventory(EntityPlayerMP var1) {
      var1.sendContainerToPlayer(var1.inventoryContainer);
      var1.setPlayerHealthUpdated();
      var1.playerNetServerHandler.sendPacket(new S09PacketHeldItemChange(var1.inventory.currentItem));
   }

   public int getViewDistance() {
      return this.viewDistance;
   }

   public void playerLoggedOut(EntityPlayerMP var1) {
      var1.triggerAchievement(StatList.leaveGameStat);
      this.writePlayerData(var1);
      WorldServer var2 = var1.getServerForPlayer();
      if (var1.ridingEntity != null) {
         var2.removePlayerEntityDangerously(var1.ridingEntity);
         logger.debug("removing player mount");
      }

      var2.removeEntity(var1);
      var2.getPlayerManager().removePlayer(var1);
      this.playerEntityList.remove(var1);
      UUID var3 = var1.getUniqueID();
      EntityPlayerMP var4 = (EntityPlayerMP)this.uuidToPlayerMap.get(var3);
      if (var4 == var1) {
         this.uuidToPlayerMap.remove(var3);
         this.playerStatFiles.remove(var3);
      }

      this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.REMOVE_PLAYER, new EntityPlayerMP[]{var1}));
   }

   private void setPlayerGameTypeBasedOnOther(EntityPlayerMP var1, EntityPlayerMP var2, World var3) {
      if (var2 != null) {
         var1.theItemInWorldManager.setGameType(var2.theItemInWorldManager.getGameType());
      } else if (this.gameType != null) {
         var1.theItemInWorldManager.setGameType(this.gameType);
      }

      var1.theItemInWorldManager.initializeGameType(var3.getWorldInfo().getGameType());
   }

   public void updateTimeAndWeatherForPlayer(EntityPlayerMP var1, WorldServer var2) {
      WorldBorder var3 = this.mcServer.worldServers[0].getWorldBorder();
      var1.playerNetServerHandler.sendPacket(new S44PacketWorldBorder(var3, S44PacketWorldBorder.Action.INITIALIZE));
      var1.playerNetServerHandler.sendPacket(new S03PacketTimeUpdate(var2.getTotalWorldTime(), var2.getWorldTime(), var2.getGameRules().getBoolean("doDaylightCycle")));
      if (var2.isRaining()) {
         var1.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(1, 0.0F));
         var1.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(7, var2.getRainStrength(1.0F)));
         var1.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(8, var2.getThunderStrength(1.0F)));
      }

   }

   public void sendPacketToAllPlayers(Packet var1) {
      for(int var2 = 0; var2 < this.playerEntityList.size(); ++var2) {
         ((EntityPlayerMP)this.playerEntityList.get(var2)).playerNetServerHandler.sendPacket(var1);
      }

   }

   public void sendChatMsgImpl(IChatComponent var1, boolean var2) {
      this.mcServer.addChatMessage(var1);
      byte var3 = (byte)(var2 ? 1 : 0);
      this.sendPacketToAllPlayers(new S02PacketChat(var1, var3));
   }

   public EntityPlayerMP createPlayerForUser(GameProfile var1) {
      UUID var2 = EntityPlayer.getUUID(var1);
      ArrayList var3 = Lists.newArrayList();

      EntityPlayerMP var5;
      for(int var4 = 0; var4 < this.playerEntityList.size(); ++var4) {
         var5 = (EntityPlayerMP)this.playerEntityList.get(var4);
         if (var5.getUniqueID().equals(var2)) {
            var3.add(var5);
         }
      }

      EntityPlayerMP var7 = (EntityPlayerMP)this.uuidToPlayerMap.get(var1.getId());
      if (var7 != null && !var3.contains(var7)) {
         var3.add(var7);
      }

      Iterator var6 = var3.iterator();

      while(var6.hasNext()) {
         var5 = (EntityPlayerMP)var6.next();
         var5.playerNetServerHandler.kickPlayerFromServer("You logged in from another location");
      }

      Object var8;
      if (this.mcServer.isDemo()) {
         var8 = new DemoWorldManager(this.mcServer.worldServerForDimension(0));
      } else {
         var8 = new ItemInWorldManager(this.mcServer.worldServerForDimension(0));
      }

      return new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(0), var1, (ItemInWorldManager)var8);
   }

   public boolean canSendCommands(GameProfile var1) {
      return this.ops.hasEntry(var1) || this.mcServer.isSinglePlayer() && this.mcServer.worldServers[0].getWorldInfo().areCommandsAllowed() && this.mcServer.getServerOwner().equalsIgnoreCase(var1.getName()) || this.commandsAllowedForAll;
   }

   public void setPlayerManager(WorldServer[] var1) {
      this.playerNBTManagerObj = var1[0].getSaveHandler().getPlayerNBTManager();
      var1[0].getWorldBorder().addListener(new IBorderListener(this) {
         final ServerConfigurationManager this$0;

         public void onTransitionStarted(WorldBorder var1, double var2, double var4, long var6) {
            this.this$0.sendPacketToAllPlayers(new S44PacketWorldBorder(var1, S44PacketWorldBorder.Action.LERP_SIZE));
         }

         public void onWarningDistanceChanged(WorldBorder var1, int var2) {
            this.this$0.sendPacketToAllPlayers(new S44PacketWorldBorder(var1, S44PacketWorldBorder.Action.SET_WARNING_BLOCKS));
         }

         public void onSizeChanged(WorldBorder var1, double var2) {
            this.this$0.sendPacketToAllPlayers(new S44PacketWorldBorder(var1, S44PacketWorldBorder.Action.SET_SIZE));
         }

         public void onDamageBufferChanged(WorldBorder var1, double var2) {
         }

         {
            this.this$0 = var1;
         }

         public void onDamageAmountChanged(WorldBorder var1, double var2) {
         }

         public void onCenterChanged(WorldBorder var1, double var2, double var4) {
            this.this$0.sendPacketToAllPlayers(new S44PacketWorldBorder(var1, S44PacketWorldBorder.Action.SET_CENTER));
         }

         public void onWarningTimeChanged(WorldBorder var1, int var2) {
            this.this$0.sendPacketToAllPlayers(new S44PacketWorldBorder(var1, S44PacketWorldBorder.Action.SET_WARNING_TIME));
         }
      });
   }

   public List func_181057_v() {
      return this.playerEntityList;
   }

   public StatisticsFile getPlayerStatsFile(EntityPlayer var1) {
      UUID var2 = var1.getUniqueID();
      StatisticsFile var3 = var2 == null ? null : (StatisticsFile)this.playerStatFiles.get(var2);
      if (var3 == null) {
         File var4 = new File(this.mcServer.worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "stats");
         File var5 = new File(var4, var2.toString() + ".json");
         if (!var5.exists()) {
            File var6 = new File(var4, var1.getName() + ".json");
            if (var6.exists() && var6.isFile()) {
               var6.renameTo(var5);
            }
         }

         var3 = new StatisticsFile(this.mcServer, var5);
         var3.readStatFile();
         this.playerStatFiles.put(var2, var3);
      }

      return var3;
   }

   public void transferEntityToWorld(Entity var1, int var2, WorldServer var3, WorldServer var4) {
      double var5 = var1.posX;
      double var7 = var1.posZ;
      double var9 = 8.0D;
      float var11 = var1.rotationYaw;
      var3.theProfiler.startSection("moving");
      if (var1.dimension == -1) {
         var5 = MathHelper.clamp_double(var5 / var9, var4.getWorldBorder().minX() + 16.0D, var4.getWorldBorder().maxX() - 16.0D);
         var7 = MathHelper.clamp_double(var7 / var9, var4.getWorldBorder().minZ() + 16.0D, var4.getWorldBorder().maxZ() - 16.0D);
         var1.setLocationAndAngles(var5, var1.posY, var7, var1.rotationYaw, var1.rotationPitch);
         if (var1.isEntityAlive()) {
            var3.updateEntityWithOptionalForce(var1, false);
         }
      } else if (var1.dimension == 0) {
         var5 = MathHelper.clamp_double(var5 * var9, var4.getWorldBorder().minX() + 16.0D, var4.getWorldBorder().maxX() - 16.0D);
         var7 = MathHelper.clamp_double(var7 * var9, var4.getWorldBorder().minZ() + 16.0D, var4.getWorldBorder().maxZ() - 16.0D);
         var1.setLocationAndAngles(var5, var1.posY, var7, var1.rotationYaw, var1.rotationPitch);
         if (var1.isEntityAlive()) {
            var3.updateEntityWithOptionalForce(var1, false);
         }
      } else {
         BlockPos var12;
         if (var2 == 1) {
            var12 = var4.getSpawnPoint();
         } else {
            var12 = var4.getSpawnCoordinate();
         }

         var5 = (double)var12.getX();
         var1.posY = (double)var12.getY();
         var7 = (double)var12.getZ();
         var1.setLocationAndAngles(var5, var1.posY, var7, 90.0F, 0.0F);
         if (var1.isEntityAlive()) {
            var3.updateEntityWithOptionalForce(var1, false);
         }
      }

      var3.theProfiler.endSection();
      if (var2 != 1) {
         var3.theProfiler.startSection("placing");
         var5 = (double)MathHelper.clamp_int((int)var5, -29999872, 29999872);
         var7 = (double)MathHelper.clamp_int((int)var7, -29999872, 29999872);
         if (var1.isEntityAlive()) {
            var1.setLocationAndAngles(var5, var1.posY, var7, var1.rotationYaw, var1.rotationPitch);
            var4.getDefaultTeleporter().placeInPortal(var1, var11);
            var4.spawnEntityInWorld(var1);
            var4.updateEntityWithOptionalForce(var1, false);
         }

         var3.theProfiler.endSection();
      }

      var1.setWorld(var4);
   }

   public EntityPlayerMP getPlayerByUUID(UUID var1) {
      return (EntityPlayerMP)this.uuidToPlayerMap.get(var1);
   }

   public void sendToAllNearExcept(EntityPlayer var1, double var2, double var4, double var6, double var8, int var10, Packet var11) {
      for(int var12 = 0; var12 < this.playerEntityList.size(); ++var12) {
         EntityPlayerMP var13 = (EntityPlayerMP)this.playerEntityList.get(var12);
         if (var13 != var1 && var13.dimension == var10) {
            double var14 = var2 - var13.posX;
            double var16 = var4 - var13.posY;
            double var18 = var6 - var13.posZ;
            if (var14 * var14 + var16 * var16 + var18 * var18 < var8 * var8) {
               var13.playerNetServerHandler.sendPacket(var11);
            }
         }
      }

   }

   public void addWhitelistedPlayer(GameProfile var1) {
      this.whiteListedPlayers.addEntry(new UserListWhitelistEntry(var1));
   }

   public void setCommandsAllowedForAll(boolean var1) {
      this.commandsAllowedForAll = var1;
   }

   public EntityPlayerMP recreatePlayerEntity(EntityPlayerMP var1, int var2, boolean var3) {
      var1.getServerForPlayer().getEntityTracker().removePlayerFromTrackers(var1);
      var1.getServerForPlayer().getEntityTracker().untrackEntity(var1);
      var1.getServerForPlayer().getPlayerManager().removePlayer(var1);
      this.playerEntityList.remove(var1);
      this.mcServer.worldServerForDimension(var1.dimension).removePlayerEntityDangerously(var1);
      BlockPos var4 = var1.getBedLocation();
      boolean var5 = var1.isSpawnForced();
      var1.dimension = var2;
      Object var6;
      if (this.mcServer.isDemo()) {
         var6 = new DemoWorldManager(this.mcServer.worldServerForDimension(var1.dimension));
      } else {
         var6 = new ItemInWorldManager(this.mcServer.worldServerForDimension(var1.dimension));
      }

      EntityPlayerMP var7 = new EntityPlayerMP(this.mcServer, this.mcServer.worldServerForDimension(var1.dimension), var1.getGameProfile(), (ItemInWorldManager)var6);
      var7.playerNetServerHandler = var1.playerNetServerHandler;
      var7.clonePlayer(var1, var3);
      var7.setEntityId(var1.getEntityId());
      var7.func_174817_o(var1);
      WorldServer var8 = this.mcServer.worldServerForDimension(var1.dimension);
      this.setPlayerGameTypeBasedOnOther(var7, var1, var8);
      BlockPos var9;
      if (var4 != null) {
         var9 = EntityPlayer.getBedSpawnLocation(this.mcServer.worldServerForDimension(var1.dimension), var4, var5);
         if (var9 != null) {
            var7.setLocationAndAngles((double)((float)var9.getX() + 0.5F), (double)((float)var9.getY() + 0.1F), (double)((float)var9.getZ() + 0.5F), 0.0F, 0.0F);
            var7.setSpawnPoint(var4, var5);
         } else {
            var7.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(0, 0.0F));
         }
      }

      var8.theChunkProviderServer.loadChunk((int)var7.posX >> 4, (int)var7.posZ >> 4);

      while(!var8.getCollidingBoundingBoxes(var7, var7.getEntityBoundingBox()).isEmpty() && var7.posY < 256.0D) {
         var7.setPosition(var7.posX, var7.posY + 1.0D, var7.posZ);
      }

      var7.playerNetServerHandler.sendPacket(new S07PacketRespawn(var7.dimension, var7.worldObj.getDifficulty(), var7.worldObj.getWorldInfo().getTerrainType(), var7.theItemInWorldManager.getGameType()));
      var9 = var8.getSpawnPoint();
      var7.playerNetServerHandler.setPlayerLocation(var7.posX, var7.posY, var7.posZ, var7.rotationYaw, var7.rotationPitch);
      var7.playerNetServerHandler.sendPacket(new S05PacketSpawnPosition(var9));
      var7.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(var7.experience, var7.experienceTotal, var7.experienceLevel));
      this.updateTimeAndWeatherForPlayer(var7, var8);
      var8.getPlayerManager().addPlayer(var7);
      var8.spawnEntityInWorld(var7);
      this.playerEntityList.add(var7);
      this.uuidToPlayerMap.put(var7.getUniqueID(), var7);
      var7.addSelfToInternalCraftingInventory();
      var7.setHealth(var7.getHealth());
      return var7;
   }

   public void initializeConnectionToPlayer(NetworkManager var1, EntityPlayerMP var2) {
      GameProfile var3 = var2.getGameProfile();
      PlayerProfileCache var4 = this.mcServer.getPlayerProfileCache();
      GameProfile var5 = var4.getProfileByUUID(var3.getId());
      String var6 = var5 == null ? var3.getName() : var5.getName();
      var4.addEntry(var3);
      NBTTagCompound var7 = this.readPlayerDataFromFile(var2);
      var2.setWorld(this.mcServer.worldServerForDimension(var2.dimension));
      var2.theItemInWorldManager.setWorld((WorldServer)var2.worldObj);
      String var8 = "local";
      if (var1.getRemoteAddress() != null) {
         var8 = var1.getRemoteAddress().toString();
      }

      logger.info(var2.getName() + "[" + var8 + "] logged in with entity id " + var2.getEntityId() + " at (" + var2.posX + ", " + var2.posY + ", " + var2.posZ + ")");
      WorldServer var9 = this.mcServer.worldServerForDimension(var2.dimension);
      WorldInfo var10 = var9.getWorldInfo();
      BlockPos var11 = var9.getSpawnPoint();
      this.setPlayerGameTypeBasedOnOther(var2, (EntityPlayerMP)null, var9);
      NetHandlerPlayServer var12 = new NetHandlerPlayServer(this.mcServer, var1, var2);
      var12.sendPacket(new S01PacketJoinGame(var2.getEntityId(), var2.theItemInWorldManager.getGameType(), var10.isHardcoreModeEnabled(), var9.provider.getDimensionId(), var9.getDifficulty(), this.getMaxPlayers(), var10.getTerrainType(), var9.getGameRules().getBoolean("reducedDebugInfo")));
      var12.sendPacket(new S3FPacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(this.getServerInstance().getServerModName())));
      var12.sendPacket(new S41PacketServerDifficulty(var10.getDifficulty(), var10.isDifficultyLocked()));
      var12.sendPacket(new S05PacketSpawnPosition(var11));
      var12.sendPacket(new S39PacketPlayerAbilities(var2.capabilities));
      var12.sendPacket(new S09PacketHeldItemChange(var2.inventory.currentItem));
      var2.getStatFile().func_150877_d();
      var2.getStatFile().sendAchievements(var2);
      this.sendScoreboard((ServerScoreboard)var9.getScoreboard(), var2);
      this.mcServer.refreshStatusNextTick();
      ChatComponentTranslation var13;
      if (!var2.getName().equalsIgnoreCase(var6)) {
         var13 = new ChatComponentTranslation("multiplayer.player.joined.renamed", new Object[]{var2.getDisplayName(), var6});
      } else {
         var13 = new ChatComponentTranslation("multiplayer.player.joined", new Object[]{var2.getDisplayName()});
      }

      var13.getChatStyle().setColor(EnumChatFormatting.YELLOW);
      this.sendChatMsg(var13);
      this.playerLoggedIn(var2);
      var12.setPlayerLocation(var2.posX, var2.posY, var2.posZ, var2.rotationYaw, var2.rotationPitch);
      this.updateTimeAndWeatherForPlayer(var2, var9);
      if (this.mcServer.getResourcePackUrl().length() > 0) {
         var2.loadResourcePack(this.mcServer.getResourcePackUrl(), this.mcServer.getResourcePackHash());
      }

      Iterator var15 = var2.getActivePotionEffects().iterator();

      while(var15.hasNext()) {
         PotionEffect var14 = (PotionEffect)var15.next();
         var12.sendPacket(new S1DPacketEntityEffect(var2.getEntityId(), var14));
      }

      var2.addSelfToInternalCraftingInventory();
      if (var7 != null && var7.hasKey("Riding", 10)) {
         Entity var17 = EntityList.createEntityFromNBT(var7.getCompoundTag("Riding"), var9);
         if (var17 != null) {
            var17.forceSpawn = true;
            var9.spawnEntityInWorld(var17);
            var2.mountEntity(var17);
            var17.forceSpawn = false;
         }
      }

   }

   public UserListOps getOppedPlayers() {
      return this.ops;
   }

   public UserListBans getBannedPlayers() {
      return this.bannedPlayers;
   }

   public int getMaxPlayers() {
      return this.maxPlayers;
   }

   public GameProfile[] getAllProfiles() {
      GameProfile[] var1 = new GameProfile[this.playerEntityList.size()];

      for(int var2 = 0; var2 < this.playerEntityList.size(); ++var2) {
         var1[var2] = ((EntityPlayerMP)this.playerEntityList.get(var2)).getGameProfile();
      }

      return var1;
   }

   public UserListWhitelist getWhitelistedPlayers() {
      return this.whiteListedPlayers;
   }

   public void sendPacketToAllPlayersInDimension(Packet var1, int var2) {
      for(int var3 = 0; var3 < this.playerEntityList.size(); ++var3) {
         EntityPlayerMP var4 = (EntityPlayerMP)this.playerEntityList.get(var3);
         if (var4.dimension == var2) {
            var4.playerNetServerHandler.sendPacket(var1);
         }
      }

   }

   public void onTick() {
      if (++this.playerPingIndex > 600) {
         this.sendPacketToAllPlayers(new S38PacketPlayerListItem(S38PacketPlayerListItem.Action.UPDATE_LATENCY, this.playerEntityList));
         this.playerPingIndex = 0;
      }

   }

   public BanList getBannedIPs() {
      return this.bannedIPs;
   }

   public ServerConfigurationManager(MinecraftServer var1) {
      this.bannedPlayers = new UserListBans(FILE_PLAYERBANS);
      this.bannedIPs = new BanList(FILE_IPBANS);
      this.ops = new UserListOps(FILE_OPS);
      this.whiteListedPlayers = new UserListWhitelist(FILE_WHITELIST);
      this.playerStatFiles = Maps.newHashMap();
      this.mcServer = var1;
      this.bannedPlayers.setLanServer(false);
      this.bannedIPs.setLanServer(false);
      this.maxPlayers = 8;
   }

   public void removeOp(GameProfile var1) {
      this.ops.removeEntry(var1);
   }

   public String func_181058_b(boolean var1) {
      String var2 = "";
      ArrayList var3 = Lists.newArrayList((Iterable)this.playerEntityList);

      for(int var4 = 0; var4 < var3.size(); ++var4) {
         if (var4 > 0) {
            var2 = var2 + ", ";
         }

         var2 = var2 + ((EntityPlayerMP)var3.get(var4)).getName();
         if (var1) {
            var2 = var2 + " (" + ((EntityPlayerMP)var3.get(var4)).getUniqueID().toString() + ")";
         }
      }

      return var2;
   }

   public void transferPlayerToDimension(EntityPlayerMP var1, int var2) {
      int var3 = var1.dimension;
      WorldServer var4 = this.mcServer.worldServerForDimension(var1.dimension);
      var1.dimension = var2;
      WorldServer var5 = this.mcServer.worldServerForDimension(var1.dimension);
      var1.playerNetServerHandler.sendPacket(new S07PacketRespawn(var1.dimension, var1.worldObj.getDifficulty(), var1.worldObj.getWorldInfo().getTerrainType(), var1.theItemInWorldManager.getGameType()));
      var4.removePlayerEntityDangerously(var1);
      var1.isDead = false;
      this.transferEntityToWorld(var1, var3, var4, var5);
      this.preparePlayer(var1, var4);
      var1.playerNetServerHandler.setPlayerLocation(var1.posX, var1.posY, var1.posZ, var1.rotationYaw, var1.rotationPitch);
      var1.theItemInWorldManager.setWorld(var5);
      this.updateTimeAndWeatherForPlayer(var1, var5);
      this.syncPlayerInventory(var1);
      Iterator var7 = var1.getActivePotionEffects().iterator();

      while(var7.hasNext()) {
         PotionEffect var6 = (PotionEffect)var7.next();
         var1.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(var1.getEntityId(), var6));
      }

   }

   public List getPlayersMatchingAddress(String var1) {
      ArrayList var2 = Lists.newArrayList();
      Iterator var4 = this.playerEntityList.iterator();

      while(var4.hasNext()) {
         EntityPlayerMP var3 = (EntityPlayerMP)var4.next();
         if (var3.getPlayerIP().equals(var1)) {
            var2.add(var3);
         }
      }

      return var2;
   }

   public NBTTagCompound readPlayerDataFromFile(EntityPlayerMP var1) {
      NBTTagCompound var2 = this.mcServer.worldServers[0].getWorldInfo().getPlayerNBTTagCompound();
      NBTTagCompound var3;
      if (var1.getName().equals(this.mcServer.getServerOwner()) && var2 != null) {
         var1.readFromNBT(var2);
         var3 = var2;
         logger.debug("loading single player");
      } else {
         var3 = this.playerNBTManagerObj.readPlayerData(var1);
      }

      return var3;
   }

   public String[] getAvailablePlayerDat() {
      return this.mcServer.worldServers[0].getSaveHandler().getPlayerNBTManager().getAvailablePlayerDat();
   }

   public void sendMessageToAllTeamMembers(EntityPlayer var1, IChatComponent var2) {
      Team var3 = var1.getTeam();
      if (var3 != null) {
         Iterator var5 = var3.getMembershipCollection().iterator();

         while(var5.hasNext()) {
            String var4 = (String)var5.next();
            EntityPlayerMP var6 = this.getPlayerByUsername(var4);
            if (var6 != null && var6 != var1) {
               var6.addChatMessage(var2);
            }
         }
      }

   }

   public NBTTagCompound getHostPlayerData() {
      return null;
   }

   public int getEntityViewDistance() {
      return PlayerManager.getFurthestViewableBlock(this.getViewDistance());
   }

   public String allowUserToConnect(SocketAddress var1, GameProfile var2) {
      String var4;
      if (this.bannedPlayers.isBanned(var2)) {
         UserListBansEntry var6 = (UserListBansEntry)this.bannedPlayers.getEntry(var2);
         var4 = "You are banned from this server!\nReason: " + var6.getBanReason();
         if (var6.getBanEndDate() != null) {
            var4 = var4 + "\nYour ban will be removed on " + dateFormat.format(var6.getBanEndDate());
         }

         return var4;
      } else if (var2 != false) {
         return "You are not white-listed on this server!";
      } else if (this.bannedIPs.isBanned(var1)) {
         IPBanEntry var3 = this.bannedIPs.getBanEntry(var1);
         var4 = "Your IP address is banned from this server!\nReason: " + var3.getBanReason();
         if (var3.getBanEndDate() != null) {
            var4 = var4 + "\nYour ban will be removed on " + dateFormat.format(var3.getBanEndDate());
         }

         return var4;
      } else {
         return this.playerEntityList.size() >= this.maxPlayers && !this.func_183023_f(var2) ? "The server is full!" : null;
      }
   }

   public String[] getOppedPlayerNames() {
      return this.ops.getKeys();
   }

   public MinecraftServer getServerInstance() {
      return this.mcServer;
   }

   protected void sendScoreboard(ServerScoreboard var1, EntityPlayerMP var2) {
      HashSet var3 = Sets.newHashSet();
      Iterator var5 = var1.getTeams().iterator();

      while(var5.hasNext()) {
         ScorePlayerTeam var4 = (ScorePlayerTeam)var5.next();
         var2.playerNetServerHandler.sendPacket(new S3EPacketTeams(var4, 0));
      }

      for(int var8 = 0; var8 < 19; ++var8) {
         ScoreObjective var9 = var1.getObjectiveInDisplaySlot(var8);
         if (var9 != null && !var3.contains(var9)) {
            Iterator var7 = var1.func_96550_d(var9).iterator();

            while(var7.hasNext()) {
               Packet var6 = (Packet)var7.next();
               var2.playerNetServerHandler.sendPacket(var6);
            }

            var3.add(var9);
         }
      }

   }

   public void setViewDistance(int var1) {
      this.viewDistance = var1;
      if (this.mcServer.worldServers != null) {
         WorldServer[] var5;
         int var4 = (var5 = this.mcServer.worldServers).length;

         for(int var3 = 0; var3 < var4; ++var3) {
            WorldServer var2 = var5[var3];
            if (var2 != null) {
               var2.getPlayerManager().setPlayerViewRadius(var1);
            }
         }
      }

   }

   public void saveAllPlayerData() {
      for(int var1 = 0; var1 < this.playerEntityList.size(); ++var1) {
         this.writePlayerData((EntityPlayerMP)this.playerEntityList.get(var1));
      }

   }

   public void sendToAllNear(double var1, double var3, double var5, double var7, int var9, Packet var10) {
      this.sendToAllNearExcept((EntityPlayer)null, var1, var3, var5, var7, var9, var10);
   }

   public boolean func_183023_f(GameProfile var1) {
      return false;
   }

   public void preparePlayer(EntityPlayerMP var1, WorldServer var2) {
      WorldServer var3 = var1.getServerForPlayer();
      if (var2 != null) {
         var2.getPlayerManager().removePlayer(var1);
      }

      var3.getPlayerManager().addPlayer(var1);
      var3.theChunkProviderServer.loadChunk((int)var1.posX >> 4, (int)var1.posZ >> 4);
   }

   public void removeAllPlayers() {
      for(int var1 = 0; var1 < this.playerEntityList.size(); ++var1) {
         ((EntityPlayerMP)this.playerEntityList.get(var1)).playerNetServerHandler.kickPlayerFromServer("Server closed");
      }

   }

   public String[] getAllUsernames() {
      String[] var1 = new String[this.playerEntityList.size()];

      for(int var2 = 0; var2 < this.playerEntityList.size(); ++var2) {
         var1[var2] = ((EntityPlayerMP)this.playerEntityList.get(var2)).getName();
      }

      return var1;
   }

   public void sendChatMsg(IChatComponent var1) {
      this.sendChatMsgImpl(var1, true);
   }

   public void sendMessageToTeamOrEvryPlayer(EntityPlayer var1, IChatComponent var2) {
      Team var3 = var1.getTeam();
      if (var3 == null) {
         this.sendChatMsg(var2);
      } else {
         for(int var4 = 0; var4 < this.playerEntityList.size(); ++var4) {
            EntityPlayerMP var5 = (EntityPlayerMP)this.playerEntityList.get(var4);
            if (var5.getTeam() != var3) {
               var5.addChatMessage(var2);
            }
         }
      }

   }

   public void addOp(GameProfile var1) {
      this.ops.addEntry(new UserListOpsEntry(var1, this.mcServer.getOpPermissionLevel(), this.ops.func_183026_b(var1)));
   }
}
