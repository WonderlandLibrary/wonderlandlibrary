package net.minecraft.server.integrated;

import com.google.common.util.concurrent.Futures;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedPlayerList;
import net.minecraft.server.integrated.IntegratedServerCommandManager;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.src.Config;
import net.minecraft.util.BlockPos;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.Util;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.optifine.ClearWater;
import net.optifine.reflect.Reflector;
import net.optifine.reflect.ReflectorConstructor;
import net.optifine.reflect.ReflectorMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegratedServer
extends MinecraftServer {
    private static final Logger logger = LogManager.getLogger();
    private final Minecraft mc;
    private final WorldSettings theWorldSettings;
    private boolean isGamePaused;
    private boolean isPublic;
    private ThreadLanServerPing lanServerPing;
    private long ticksSaveLast = 0L;
    public World difficultyUpdateWorld = null;
    public BlockPos difficultyUpdatePos = null;
    public DifficultyInstance difficultyLast = null;

    public IntegratedServer(Minecraft mcIn) {
        super(mcIn.getProxy(), new File(mcIn.mcDataDir, USER_CACHE_FILE.getName()));
        this.mc = mcIn;
        this.theWorldSettings = null;
    }

    public IntegratedServer(Minecraft mcIn, String folderName, String worldName, WorldSettings settings) {
        super(new File(mcIn.mcDataDir, "saves"), mcIn.getProxy(), new File(mcIn.mcDataDir, USER_CACHE_FILE.getName()));
        NBTTagCompound nbttagcompound;
        this.setServerOwner(mcIn.getSession().getUsername());
        this.setFolderName(folderName);
        this.setWorldName(worldName);
        this.setDemo(mcIn.isDemo());
        this.canCreateBonusChest(settings.isBonusChestEnabled());
        this.setBuildLimit(256);
        this.setConfigManager((ServerConfigurationManager)new IntegratedPlayerList(this));
        this.mc = mcIn;
        this.theWorldSettings = this.isDemo() ? DemoWorldServer.demoWorldSettings : settings;
        ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(folderName, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (worldinfo != null && (nbttagcompound = worldinfo.getPlayerNBTTagCompound()) != null && nbttagcompound.hasKey("Dimension")) {
            int i;
            PacketThreadUtil.lastDimensionId = i = nbttagcompound.getInteger("Dimension");
            this.mc.loadingScreen.setLoadingProgress(-1);
        }
    }

    protected ServerCommandManager createNewCommandManager() {
        return new IntegratedServerCommandManager();
    }

    protected void loadAllWorlds(String saveName, String worldNameIn, long seed, WorldType type, String worldNameIn2) {
        this.convertMapIfNeeded(saveName);
        boolean flag = Reflector.DimensionManager.exists();
        if (!flag) {
            this.worldServers = new WorldServer[3];
            this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
        }
        ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(saveName, true);
        this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (worldinfo == null) {
            worldinfo = new WorldInfo(this.theWorldSettings, worldNameIn);
        } else {
            worldinfo.setWorldName(worldNameIn);
        }
        if (flag) {
            Integer[] ainteger;
            WorldServer worldserver = this.isDemo() ? (WorldServer)new DemoWorldServer((MinecraftServer)this, isavehandler, worldinfo, 0, this.theProfiler).init() : (WorldServer)new WorldServer((MinecraftServer)this, isavehandler, worldinfo, 0, this.theProfiler).init();
            worldserver.initialize(this.theWorldSettings);
            Integer[] ainteger1 = ainteger = (Integer[])Reflector.call((ReflectorMethod)Reflector.DimensionManager_getStaticDimensionIDs, (Object[])new Object[0]);
            int i = ainteger.length;
            for (int j = 0; j < i; ++j) {
                int k = ainteger1[j];
                WorldServer worldserver1 = k == 0 ? worldserver : (WorldServer)new WorldServerMulti((MinecraftServer)this, isavehandler, k, worldserver, this.theProfiler).init();
                worldserver1.addWorldAccess((IWorldAccess)new WorldManager((MinecraftServer)this, worldserver1));
                if (!this.isSinglePlayer()) {
                    worldserver1.getWorldInfo().setGameType(this.getGameType());
                }
                if (!Reflector.EventBus.exists()) continue;
                Reflector.postForgeBusEvent((ReflectorConstructor)Reflector.WorldEvent_Load_Constructor, (Object[])new Object[]{worldserver1});
            }
            this.getConfigurationManager().setPlayerManager(new WorldServer[]{worldserver});
            if (worldserver.getWorldInfo().getDifficulty() == null) {
                this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
            }
        } else {
            for (int l = 0; l < this.worldServers.length; ++l) {
                int i1 = 0;
                if (l == 1) {
                    i1 = -1;
                }
                if (l == 2) {
                    i1 = 1;
                }
                if (l == 0) {
                    this.worldServers[l] = this.isDemo() ? (WorldServer)new DemoWorldServer((MinecraftServer)this, isavehandler, worldinfo, i1, this.theProfiler).init() : (WorldServer)new WorldServer((MinecraftServer)this, isavehandler, worldinfo, i1, this.theProfiler).init();
                    this.worldServers[l].initialize(this.theWorldSettings);
                } else {
                    this.worldServers[l] = (WorldServer)new WorldServerMulti((MinecraftServer)this, isavehandler, i1, this.worldServers[0], this.theProfiler).init();
                }
                this.worldServers[l].addWorldAccess((IWorldAccess)new WorldManager((MinecraftServer)this, this.worldServers[l]));
            }
            this.getConfigurationManager().setPlayerManager(this.worldServers);
            if (this.worldServers[0].getWorldInfo().getDifficulty() == null) {
                this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
            }
        }
        this.initialWorldChunkLoad();
    }

    protected boolean startServer() throws IOException {
        logger.info("Starting integrated minecraft server version 1.9");
        this.setOnlineMode(true);
        this.setCanSpawnAnimals(true);
        this.setCanSpawnNPCs(true);
        this.setAllowPvp(true);
        this.setAllowFlight(true);
        logger.info("Generating keypair");
        this.setKeyPair(CryptManager.generateKeyPair());
        if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists()) {
            Object object = Reflector.call((ReflectorMethod)Reflector.FMLCommonHandler_instance, (Object[])new Object[0]);
            if (!Reflector.callBoolean((Object)object, (ReflectorMethod)Reflector.FMLCommonHandler_handleServerAboutToStart, (Object[])new Object[]{this})) {
                return false;
            }
        }
        this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.theWorldSettings.getSeed(), this.theWorldSettings.getTerrainType(), this.theWorldSettings.getWorldName());
        this.setMOTD(this.getServerOwner() + " - " + this.worldServers[0].getWorldInfo().getWorldName());
        if (Reflector.FMLCommonHandler_handleServerStarting.exists()) {
            Object object1 = Reflector.call((ReflectorMethod)Reflector.FMLCommonHandler_instance, (Object[])new Object[0]);
            if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == Boolean.TYPE) {
                return Reflector.callBoolean((Object)object1, (ReflectorMethod)Reflector.FMLCommonHandler_handleServerStarting, (Object[])new Object[]{this});
            }
            Reflector.callVoid((Object)object1, (ReflectorMethod)Reflector.FMLCommonHandler_handleServerStarting, (Object[])new Object[]{this});
        }
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void tick() {
        this.onTick();
        boolean flag = this.isGamePaused;
        boolean bl = this.isGamePaused = Minecraft.getMinecraft().getNetHandler() != null && Minecraft.getMinecraft().isGamePaused();
        if (!flag && this.isGamePaused) {
            logger.info("Saving and pausing game...");
            this.getConfigurationManager().saveAllPlayerData();
            this.saveAllWorlds(false);
        }
        if (this.isGamePaused) {
            Queue queue = this.futureTaskQueue;
            synchronized (queue) {
                while (!this.futureTaskQueue.isEmpty()) {
                    Util.runTask((FutureTask)((FutureTask)this.futureTaskQueue.poll()), (Logger)logger);
                }
            }
        } else {
            super.tick();
            if (this.mc.gameSettings.renderDistanceChunks != this.getConfigurationManager().getViewDistance()) {
                logger.info("Changing view distance to {}, from {}", new Object[]{this.mc.gameSettings.renderDistanceChunks, this.getConfigurationManager().getViewDistance()});
                this.getConfigurationManager().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
            }
            if (this.mc.theWorld != null) {
                WorldInfo worldinfo1 = this.worldServers[0].getWorldInfo();
                WorldInfo worldinfo = this.mc.theWorld.getWorldInfo();
                if (!worldinfo1.isDifficultyLocked() && worldinfo.getDifficulty() != worldinfo1.getDifficulty()) {
                    logger.info("Changing difficulty to {}, from {}", new Object[]{worldinfo.getDifficulty(), worldinfo1.getDifficulty()});
                    this.setDifficultyForAllWorlds(worldinfo.getDifficulty());
                } else if (worldinfo.isDifficultyLocked() && !worldinfo1.isDifficultyLocked()) {
                    logger.info("Locking difficulty to {}", new Object[]{worldinfo.getDifficulty()});
                    for (WorldServer worldserver : this.worldServers) {
                        if (worldserver == null) continue;
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
        return this.mc.theWorld == null ? this.mc.gameSettings.difficulty : this.mc.theWorld.getWorldInfo().getDifficulty();
    }

    public boolean isHardcore() {
        return this.theWorldSettings.getHardcoreEnabled();
    }

    public boolean shouldBroadcastRconToOps() {
        return true;
    }

    public boolean shouldBroadcastConsoleToOps() {
        return true;
    }

    public void saveAllWorlds(boolean dontLog) {
        if (dontLog) {
            int j;
            int i = this.getTickCounter();
            if ((long)i < this.ticksSaveLast + (long)(j = this.mc.gameSettings.ofAutoSaveTicks)) {
                return;
            }
            this.ticksSaveLast = i;
        }
        super.saveAllWorlds(dontLog);
    }

    public File getDataDirectory() {
        return this.mc.mcDataDir;
    }

    public boolean isDedicatedServer() {
        return false;
    }

    public boolean shouldUseNativeTransport() {
        return false;
    }

    protected void finalTick(CrashReport report) {
        this.mc.crashed(report);
    }

    public CrashReport addServerInfoToCrashReport(CrashReport report) {
        report = super.addServerInfoToCrashReport(report);
        report.getCategory().addCrashSectionCallable("Type", (Callable)new /* Unavailable Anonymous Inner Class!! */);
        report.getCategory().addCrashSectionCallable("Is Modded", (Callable)new /* Unavailable Anonymous Inner Class!! */);
        return report;
    }

    public void setDifficultyForAllWorlds(EnumDifficulty difficulty) {
        super.setDifficultyForAllWorlds(difficulty);
        if (this.mc.theWorld != null) {
            this.mc.theWorld.getWorldInfo().setDifficulty(difficulty);
        }
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper) {
        super.addServerStatsToSnooper(playerSnooper);
        playerSnooper.addClientStat("snooper_partner", (Object)this.mc.getPlayerUsageSnooper().getUniqueID());
    }

    public boolean isSnooperEnabled() {
        return Minecraft.getMinecraft().isSnooperEnabled();
    }

    public String shareToLAN(WorldSettings.GameType type, boolean allowCheats) {
        try {
            int i = -1;
            try {
                i = HttpUtil.getSuitableLanPort();
            }
            catch (IOException iOException) {
                // empty catch block
            }
            if (i <= 0) {
                i = 25564;
            }
            this.getNetworkSystem().addLanEndpoint((InetAddress)null, i);
            logger.info("Started on " + i);
            this.isPublic = true;
            this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), i + "");
            this.lanServerPing.start();
            this.getConfigurationManager().setGameType(type);
            this.getConfigurationManager().setCommandsAllowedForAll(allowCheats);
            return i + "";
        }
        catch (IOException var6) {
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
        if (!Reflector.MinecraftForge.exists() || this.isServerRunning()) {
            Futures.getUnchecked((Future)this.addScheduledTask((Runnable)new /* Unavailable Anonymous Inner Class!! */));
        }
        super.initiateShutdown();
        if (this.lanServerPing != null) {
            this.lanServerPing.interrupt();
            this.lanServerPing = null;
        }
    }

    public void setStaticInstance() {
        this.setInstance();
    }

    public boolean getPublic() {
        return this.isPublic;
    }

    public void setGameType(WorldSettings.GameType gameMode) {
        this.getConfigurationManager().setGameType(gameMode);
    }

    public boolean isCommandBlockEnabled() {
        return true;
    }

    public int getOpPermissionLevel() {
        return 4;
    }

    private void onTick() {
        for (WorldServer worldserver : Arrays.asList((Object[])this.worldServers)) {
            this.onTick(worldserver);
        }
    }

    public DifficultyInstance getDifficultyAsync(World p_getDifficultyAsync_1_, BlockPos p_getDifficultyAsync_2_) {
        this.difficultyUpdateWorld = p_getDifficultyAsync_1_;
        this.difficultyUpdatePos = p_getDifficultyAsync_2_;
        return this.difficultyLast;
    }

    private void onTick(WorldServer p_onTick_1_) {
        if (!Config.isTimeDefault()) {
            this.fixWorldTime(p_onTick_1_);
        }
        if (!Config.isWeatherEnabled()) {
            this.fixWorldWeather(p_onTick_1_);
        }
        if (Config.waterOpacityChanged) {
            Config.waterOpacityChanged = false;
            ClearWater.updateWaterOpacity((GameSettings)Config.getGameSettings(), (World)p_onTick_1_);
        }
        if (this.difficultyUpdateWorld == p_onTick_1_ && this.difficultyUpdatePos != null) {
            this.difficultyLast = p_onTick_1_.getDifficultyForLocation(this.difficultyUpdatePos);
            this.difficultyUpdateWorld = null;
            this.difficultyUpdatePos = null;
        }
    }

    private void fixWorldWeather(WorldServer p_fixWorldWeather_1_) {
        WorldInfo worldinfo = p_fixWorldWeather_1_.getWorldInfo();
        if (worldinfo.isRaining() || worldinfo.isThundering()) {
            worldinfo.setRainTime(0);
            worldinfo.setRaining(false);
            p_fixWorldWeather_1_.setRainStrength(0.0f);
            worldinfo.setThunderTime(0);
            worldinfo.setThundering(false);
            p_fixWorldWeather_1_.setThunderStrength(0.0f);
            this.getConfigurationManager().sendPacketToAllPlayers((Packet)new S2BPacketChangeGameState(2, 0.0f));
            this.getConfigurationManager().sendPacketToAllPlayers((Packet)new S2BPacketChangeGameState(7, 0.0f));
            this.getConfigurationManager().sendPacketToAllPlayers((Packet)new S2BPacketChangeGameState(8, 0.0f));
        }
    }

    private void fixWorldTime(WorldServer p_fixWorldTime_1_) {
        WorldInfo worldinfo = p_fixWorldTime_1_.getWorldInfo();
        if (worldinfo.getGameType().getID() == 1) {
            long i = p_fixWorldTime_1_.getWorldTime();
            long j = i % 24000L;
            if (Config.isTimeDayOnly()) {
                if (j <= 1000L) {
                    p_fixWorldTime_1_.setWorldTime(i - j + 1001L);
                }
                if (j >= 11000L) {
                    p_fixWorldTime_1_.setWorldTime(i - j + 24001L);
                }
            }
            if (Config.isTimeNightOnly()) {
                if (j <= 14000L) {
                    p_fixWorldTime_1_.setWorldTime(i - j + 14001L);
                }
                if (j >= 22000L) {
                    p_fixWorldTime_1_.setWorldTime(i - j + 24000L + 14001L);
                }
            }
        }
    }
}
