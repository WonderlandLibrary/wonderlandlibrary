package net.minecraft.server.integrated;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.Util;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import optifine.Reflector;
import optifine.WorldServerOF;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedServer extends MinecraftServer {
  private static final Logger logger = LogManager.getLogger();
  
  private final Minecraft mc;
  
  private final WorldSettings theWorldSettings;
  
  private boolean isGamePaused;
  
  private boolean isPublic;
  
  private ThreadLanServerPing lanServerPing;
  
  private static final String __OBFID = "CL_00001129";
  
  public IntegratedServer(Minecraft mcIn) {
    super(mcIn.getProxy(), new File(mcIn.mcDataDir, USER_CACHE_FILE.getName()));
    this.mc = mcIn;
    this.theWorldSettings = null;
  }
  
  public IntegratedServer(Minecraft mcIn, String folderName, String worldName, WorldSettings settings) {
    super(new File(mcIn.mcDataDir, "saves"), mcIn.getProxy(), new File(mcIn.mcDataDir, USER_CACHE_FILE.getName()));
    setServerOwner(mcIn.getSession().getUsername());
    setFolderName(folderName);
    setWorldName(worldName);
    setDemo(mcIn.isDemo());
    canCreateBonusChest(settings.isBonusChestEnabled());
    setBuildLimit(256);
    setConfigManager(new IntegratedPlayerList(this));
    this.mc = mcIn;
    this.theWorldSettings = isDemo() ? DemoWorldServer.demoWorldSettings : settings;
  }
  
  protected ServerCommandManager createNewCommandManager() {
    return new IntegratedServerCommandManager();
  }
  
  protected void loadAllWorlds(String p_71247_1_, String p_71247_2_, long seed, WorldType type, String p_71247_6_) {
    convertMapIfNeeded(p_71247_1_);
    ISaveHandler isavehandler = getActiveAnvilConverter().getSaveLoader(p_71247_1_, true);
    setResourcePackFromWorld(getFolderName(), isavehandler);
    WorldInfo worldinfo = isavehandler.loadWorldInfo();
    if (Reflector.DimensionManager.exists()) {
      WorldServer worldserver = isDemo() ? (WorldServer)(new DemoWorldServer(this, isavehandler, worldinfo, 0, this.theProfiler)).init() : (WorldServer)(new WorldServerOF(this, isavehandler, worldinfo, 0, this.theProfiler)).init();
      worldserver.initialize(this.theWorldSettings);
      Integer[] ainteger = (Integer[])Reflector.call(Reflector.DimensionManager_getStaticDimensionIDs, new Object[0]);
      Integer[] ainteger1 = ainteger;
      int i = ainteger.length;
      for (int j = 0; j < i; j++) {
        int k = ainteger1[j].intValue();
        WorldServer worldserver1 = (k == 0) ? worldserver : (WorldServer)(new WorldServerMulti(this, isavehandler, k, worldserver, this.theProfiler)).init();
        worldserver1.addWorldAccess((IWorldAccess)new WorldManager(this, worldserver1));
        if (!isSinglePlayer())
          worldserver1.getWorldInfo().setGameType(getGameType()); 
        if (Reflector.EventBus.exists())
          Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, new Object[] { worldserver1 }); 
      } 
      getConfigurationManager().setPlayerManager(new WorldServer[] { worldserver });
      if (worldserver.getWorldInfo().getDifficulty() == null)
        setDifficultyForAllWorlds(this.mc.gameSettings.difficulty); 
    } else {
      this.worldServers = new WorldServer[3];
      this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
      setResourcePackFromWorld(getFolderName(), isavehandler);
      if (worldinfo == null) {
        worldinfo = new WorldInfo(this.theWorldSettings, p_71247_2_);
      } else {
        worldinfo.setWorldName(p_71247_2_);
      } 
      for (int l = 0; l < this.worldServers.length; l++) {
        byte b0 = 0;
        if (l == 1)
          b0 = -1; 
        if (l == 2)
          b0 = 1; 
        if (l == 0) {
          if (isDemo()) {
            this.worldServers[l] = (WorldServer)(new DemoWorldServer(this, isavehandler, worldinfo, b0, this.theProfiler)).init();
          } else {
            this.worldServers[l] = (WorldServer)(new WorldServerOF(this, isavehandler, worldinfo, b0, this.theProfiler)).init();
          } 
          this.worldServers[l].initialize(this.theWorldSettings);
        } else {
          this.worldServers[l] = (WorldServer)(new WorldServerMulti(this, isavehandler, b0, this.worldServers[0], this.theProfiler)).init();
        } 
        this.worldServers[l].addWorldAccess((IWorldAccess)new WorldManager(this, this.worldServers[l]));
      } 
      getConfigurationManager().setPlayerManager(this.worldServers);
      if (this.worldServers[0].getWorldInfo().getDifficulty() == null)
        setDifficultyForAllWorlds(this.mc.gameSettings.difficulty); 
    } 
    initialWorldChunkLoad();
  }
  
  protected boolean startServer() throws IOException {
    logger.info("Starting integrated minecraft server version 1.8.8");
    setOnlineMode(true);
    setCanSpawnAnimals(true);
    setCanSpawnNPCs(true);
    setAllowPvp(true);
    setAllowFlight(true);
    logger.info("Generating keypair");
    setKeyPair(CryptManager.generateKeyPair());
    if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists()) {
      Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
      if (!Reflector.callBoolean(object, Reflector.FMLCommonHandler_handleServerAboutToStart, new Object[] { this }))
        return false; 
    } 
    loadAllWorlds(getFolderName(), getWorldName(), this.theWorldSettings.getSeed(), this.theWorldSettings.getTerrainType(), this.theWorldSettings.getWorldName());
    setMOTD(getServerOwner() + " - " + this.worldServers[0].getWorldInfo().getWorldName());
    if (Reflector.FMLCommonHandler_handleServerStarting.exists()) {
      Object object1 = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
      if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == boolean.class)
        return Reflector.callBoolean(object1, Reflector.FMLCommonHandler_handleServerStarting, new Object[] { this }); 
      Reflector.callVoid(object1, Reflector.FMLCommonHandler_handleServerStarting, new Object[] { this });
    } 
    return true;
  }
  
  public void tick() {
    boolean flag = this.isGamePaused;
    this.isGamePaused = (Minecraft.getMinecraft().getNetHandler() != null && Minecraft.getMinecraft().isGamePaused());
    if (!flag && this.isGamePaused) {
      logger.info("Saving and pausing game...");
      getConfigurationManager().saveAllPlayerData();
      saveAllWorlds(false);
    } 
    if (this.isGamePaused) {
      Queue var3 = this.futureTaskQueue;
      synchronized (this.futureTaskQueue) {
        while (!this.futureTaskQueue.isEmpty())
          Util.func_181617_a(this.futureTaskQueue.poll(), logger); 
      } 
    } else {
      super.tick();
      if (this.mc.gameSettings.renderDistanceChunks != getConfigurationManager().getViewDistance()) {
        logger.info("Changing view distance to {}, from {}", new Object[] { Integer.valueOf(this.mc.gameSettings.renderDistanceChunks), Integer.valueOf(getConfigurationManager().getViewDistance()) });
        getConfigurationManager().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
      } 
      if (this.mc.theWorld != null) {
        WorldInfo worldinfo = this.worldServers[0].getWorldInfo();
        WorldInfo worldinfo1 = this.mc.theWorld.getWorldInfo();
        if (!worldinfo.isDifficultyLocked() && worldinfo1.getDifficulty() != worldinfo.getDifficulty()) {
          logger.info("Changing difficulty to {}, from {}", new Object[] { worldinfo1.getDifficulty(), worldinfo.getDifficulty() });
          setDifficultyForAllWorlds(worldinfo1.getDifficulty());
        } else if (worldinfo1.isDifficultyLocked() && !worldinfo.isDifficultyLocked()) {
          logger.info("Locking difficulty to {}", new Object[] { worldinfo1.getDifficulty() });
          for (WorldServer worldserver : this.worldServers) {
            if (worldserver != null)
              worldserver.getWorldInfo().setDifficultyLocked(true); 
          } 
        } 
      } 
    } 
  }
  
  public boolean canStructuresSpawn() {
    return false;
  }
  
  public WorldSettings.GameType getGameType() {
    return this.theWorldSettings.getGameType();
  }
  
  public EnumDifficulty getDifficulty() {
    return (this.mc.theWorld == null) ? this.mc.gameSettings.difficulty : this.mc.theWorld.getWorldInfo().getDifficulty();
  }
  
  public boolean isHardcore() {
    return this.theWorldSettings.getHardcoreEnabled();
  }
  
  public boolean func_181034_q() {
    return true;
  }
  
  public boolean func_183002_r() {
    return true;
  }
  
  public File getDataDirectory() {
    return this.mc.mcDataDir;
  }
  
  public boolean func_181035_ah() {
    return false;
  }
  
  public boolean isDedicatedServer() {
    return false;
  }
  
  protected void finalTick(CrashReport report) {
    this.mc.crashed(report);
  }
  
  public CrashReport addServerInfoToCrashReport(CrashReport report) {
    report = super.addServerInfoToCrashReport(report);
    report.getCategory().addCrashSectionCallable("Type", new Callable() {
          private static final String __OBFID = "CL_00001130";
          
          public String call() throws Exception {
            return "Integrated Server (map_client.txt)";
          }
        });
    report.getCategory().addCrashSectionCallable("Is Modded", new Callable() {
          private static final String __OBFID = "CL_00001131";
          
          public String call() throws Exception {
            String s = ClientBrandRetriever.getClientModName();
            if (!s.equals("vanilla"))
              return "Definitely; Client brand changed to '" + s + "'"; 
            s = IntegratedServer.this.getServerModName();
            return !s.equals("vanilla") ? ("Definitely; Server brand changed to '" + s + "'") : ((Minecraft.class.getSigners() == null) ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and both client + server brands are untouched.");
          }
        });
    return report;
  }
  
  public void setDifficultyForAllWorlds(EnumDifficulty difficulty) {
    super.setDifficultyForAllWorlds(difficulty);
    if (this.mc.theWorld != null)
      this.mc.theWorld.getWorldInfo().setDifficulty(difficulty); 
  }
  
  public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper) {
    super.addServerStatsToSnooper(playerSnooper);
    playerSnooper.addClientStat("snooper_partner", this.mc.getPlayerUsageSnooper().getUniqueID());
  }
  
  public boolean isSnooperEnabled() {
    return Minecraft.getMinecraft().isSnooperEnabled();
  }
  
  public String shareToLAN(WorldSettings.GameType type, boolean allowCheats) {
    try {
      int i = -1;
      try {
        i = HttpUtil.getSuitableLanPort();
      } catch (IOException iOException) {}
      if (i <= 0)
        i = 25564; 
      getNetworkSystem().addLanEndpoint((InetAddress)null, i);
      logger.info("Started on " + i);
      this.isPublic = true;
      this.lanServerPing = new ThreadLanServerPing(getMOTD(), i + "");
      this.lanServerPing.start();
      getConfigurationManager().setGameType(type);
      getConfigurationManager().setCommandsAllowedForAll(allowCheats);
      return i + "";
    } catch (IOException var6) {
      return null;
    } 
  }
  
  public void stopServer() {
    super.stopServer();
    if (this.lanServerPing != null) {
      this.lanServerPing.interrupt();
      this.lanServerPing = null;
    } 
  }
  
  public void initiateShutdown() {
    Futures.getUnchecked((Future)addScheduledTask(new Runnable() {
            private static final String __OBFID = "CL_00002380";
            
            public void run() {
              for (EntityPlayerMP entityplayermp : Lists.newArrayList(IntegratedServer.this.getConfigurationManager().func_181057_v()))
                IntegratedServer.this.getConfigurationManager().playerLoggedOut(entityplayermp); 
            }
          }));
    super.initiateShutdown();
    if (this.lanServerPing != null) {
      this.lanServerPing.interrupt();
      this.lanServerPing = null;
    } 
  }
  
  public void setStaticInstance() {
    setInstance();
  }
  
  public boolean getPublic() {
    return this.isPublic;
  }
  
  public void setGameType(WorldSettings.GameType gameMode) {
    getConfigurationManager().setGameType(gameMode);
  }
  
  public boolean isCommandBlockEnabled() {
    return true;
  }
  
  public int getOpPermissionLevel() {
    return 4;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\server\integrated\IntegratedServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */