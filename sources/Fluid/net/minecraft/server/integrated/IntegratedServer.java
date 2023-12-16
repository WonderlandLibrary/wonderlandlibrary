// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.server.integrated;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.optifine.ClearWater;
import net.minecraft.src.Config;
import java.util.Arrays;
import java.util.concurrent.Future;
import com.google.common.util.concurrent.Futures;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.collect.Lists;
import java.net.InetAddress;
import net.minecraft.util.HttpUtil;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.client.ClientBrandRetriever;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.util.Util;
import java.util.concurrent.FutureTask;
import java.io.IOException;
import net.minecraft.util.CryptManager;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldServer;
import net.optifine.reflect.Reflector;
import net.minecraft.world.WorldType;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.server.management.ServerConfigurationManager;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.MinecraftServer;

public class IntegratedServer extends MinecraftServer
{
    private static final Logger logger;
    private final Minecraft mc;
    private final WorldSettings theWorldSettings;
    private boolean isGamePaused;
    private boolean isPublic;
    private ThreadLanServerPing lanServerPing;
    private long ticksSaveLast;
    public World difficultyUpdateWorld;
    public BlockPos difficultyUpdatePos;
    public DifficultyInstance difficultyLast;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public IntegratedServer(final Minecraft mcIn) {
        super(mcIn.getProxy(), new File(mcIn.mcDataDir, IntegratedServer.USER_CACHE_FILE.getName()));
        this.ticksSaveLast = 0L;
        this.difficultyUpdateWorld = null;
        this.difficultyUpdatePos = null;
        this.difficultyLast = null;
        this.mc = mcIn;
        this.theWorldSettings = null;
    }
    
    public IntegratedServer(final Minecraft mcIn, final String folderName, final String worldName, final WorldSettings settings) {
        super(new File(mcIn.mcDataDir, "saves"), mcIn.getProxy(), new File(mcIn.mcDataDir, IntegratedServer.USER_CACHE_FILE.getName()));
        this.ticksSaveLast = 0L;
        this.difficultyUpdateWorld = null;
        this.difficultyUpdatePos = null;
        this.difficultyLast = null;
        this.setServerOwner(mcIn.getSession().getUsername());
        this.setFolderName(folderName);
        this.setWorldName(worldName);
        this.setDemo(mcIn.isDemo());
        this.canCreateBonusChest(settings.isBonusChestEnabled());
        this.setBuildLimit(256);
        this.setConfigManager(new IntegratedPlayerList(this));
        this.mc = mcIn;
        this.theWorldSettings = (this.isDemo() ? DemoWorldServer.demoWorldSettings : settings);
        final ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(folderName, false);
        final WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (worldinfo != null) {
            final NBTTagCompound nbttagcompound = worldinfo.getPlayerNBTTagCompound();
            if (nbttagcompound != null && nbttagcompound.hasKey("Dimension")) {
                final int i = PacketThreadUtil.lastDimensionId = nbttagcompound.getInteger("Dimension");
                this.mc.loadingScreen.setLoadingProgress(-1);
            }
        }
    }
    
    @Override
    protected ServerCommandManager createNewCommandManager() {
        return new IntegratedServerCommandManager();
    }
    
    @Override
    protected void loadAllWorlds(final String saveName, final String worldNameIn, final long seed, final WorldType type, final String worldNameIn2) {
        this.convertMapIfNeeded(saveName);
        final boolean flag = Reflector.DimensionManager.exists();
        if (!flag) {
            this.worldServers = new WorldServer[3];
            this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
        }
        final ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(saveName, true);
        this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (worldinfo == null) {
            worldinfo = new WorldInfo(this.theWorldSettings, worldNameIn);
        }
        else {
            worldinfo.setWorldName(worldNameIn);
        }
        if (flag) {
            final WorldServer worldserver = (WorldServer)(this.isDemo() ? new DemoWorldServer(this, isavehandler, worldinfo, 0, this.theProfiler).init() : ((WorldServer)new WorldServer(this, isavehandler, worldinfo, 0, this.theProfiler).init()));
            worldserver.initialize(this.theWorldSettings);
            final Integer[] ainteger2;
            final Integer[] ainteger = ainteger2 = (Integer[])Reflector.call(Reflector.DimensionManager_getStaticDimensionIDs, new Object[0]);
            for (int i = ainteger.length, j = 0; j < i; ++j) {
                final int k = ainteger2[j];
                final WorldServer worldserver2 = (WorldServer)((k == 0) ? worldserver : new WorldServerMulti(this, isavehandler, k, worldserver, this.theProfiler).init());
                worldserver2.addWorldAccess(new WorldManager(this, worldserver2));
                if (!this.isSinglePlayer()) {
                    worldserver2.getWorldInfo().setGameType(this.getGameType());
                }
                if (Reflector.EventBus.exists()) {
                    Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, worldserver2);
                }
            }
            this.getConfigurationManager().setPlayerManager(new WorldServer[] { worldserver });
            if (worldserver.getWorldInfo().getDifficulty() == null) {
                this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
            }
        }
        else {
            for (int l = 0; l < this.worldServers.length; ++l) {
                int i2 = 0;
                if (l == 1) {
                    i2 = -1;
                }
                if (l == 2) {
                    i2 = 1;
                }
                if (l == 0) {
                    if (this.isDemo()) {
                        this.worldServers[l] = (WorldServer)new DemoWorldServer(this, isavehandler, worldinfo, i2, this.theProfiler).init();
                    }
                    else {
                        this.worldServers[l] = (WorldServer)new WorldServer(this, isavehandler, worldinfo, i2, this.theProfiler).init();
                    }
                    this.worldServers[l].initialize(this.theWorldSettings);
                }
                else {
                    this.worldServers[l] = (WorldServer)new WorldServerMulti(this, isavehandler, i2, this.worldServers[0], this.theProfiler).init();
                }
                this.worldServers[l].addWorldAccess(new WorldManager(this, this.worldServers[l]));
            }
            this.getConfigurationManager().setPlayerManager(this.worldServers);
            if (this.worldServers[0].getWorldInfo().getDifficulty() == null) {
                this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
            }
        }
        this.initialWorldChunkLoad();
    }
    
    @Override
    protected boolean startServer() throws IOException {
        IntegratedServer.logger.info("Starting integrated minecraft server version 1.9");
        this.setOnlineMode(true);
        this.setCanSpawnAnimals(true);
        this.setCanSpawnNPCs(true);
        this.setAllowPvp(true);
        this.setAllowFlight(true);
        IntegratedServer.logger.info("Generating keypair");
        this.setKeyPair(CryptManager.generateKeyPair());
        if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists()) {
            final Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            if (!Reflector.callBoolean(object, Reflector.FMLCommonHandler_handleServerAboutToStart, this)) {
                return false;
            }
        }
        this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.theWorldSettings.getSeed(), this.theWorldSettings.getTerrainType(), this.theWorldSettings.getWorldName());
        this.setMOTD(String.valueOf(this.getServerOwner()) + " - " + this.worldServers[0].getWorldInfo().getWorldName());
        if (Reflector.FMLCommonHandler_handleServerStarting.exists()) {
            final Object object2 = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == Boolean.TYPE) {
                return Reflector.callBoolean(object2, Reflector.FMLCommonHandler_handleServerStarting, this);
            }
            Reflector.callVoid(object2, Reflector.FMLCommonHandler_handleServerStarting, this);
        }
        return true;
    }
    
    @Override
    public void tick() {
        this.onTick();
        final boolean flag = this.isGamePaused;
        this.isGamePaused = (Minecraft.getMinecraft().getNetHandler() != null && Minecraft.getMinecraft().isGamePaused());
        if (!flag && this.isGamePaused) {
            IntegratedServer.logger.info("Saving and pausing game...");
            this.getConfigurationManager().saveAllPlayerData();
            this.saveAllWorlds(false);
        }
        if (this.isGamePaused) {
            synchronized (this.futureTaskQueue) {
                while (!this.futureTaskQueue.isEmpty()) {
                    Util.runTask(this.futureTaskQueue.poll(), IntegratedServer.logger);
                }
                monitorexit(this.futureTaskQueue);
                return;
            }
        }
        super.tick();
        if (this.mc.gameSettings.renderDistanceChunks != this.getConfigurationManager().getViewDistance()) {
            IntegratedServer.logger.info("Changing view distance to {}, from {}", new Object[] { this.mc.gameSettings.renderDistanceChunks, this.getConfigurationManager().getViewDistance() });
            this.getConfigurationManager().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
        }
        if (this.mc.theWorld != null) {
            final WorldInfo worldinfo1 = this.worldServers[0].getWorldInfo();
            final WorldInfo worldinfo2 = this.mc.theWorld.getWorldInfo();
            if (!worldinfo1.isDifficultyLocked() && worldinfo2.getDifficulty() != worldinfo1.getDifficulty()) {
                IntegratedServer.logger.info("Changing difficulty to {}, from {}", new Object[] { worldinfo2.getDifficulty(), worldinfo1.getDifficulty() });
                this.setDifficultyForAllWorlds(worldinfo2.getDifficulty());
            }
            else if (worldinfo2.isDifficultyLocked() && !worldinfo1.isDifficultyLocked()) {
                IntegratedServer.logger.info("Locking difficulty to {}", new Object[] { worldinfo2.getDifficulty() });
                WorldServer[] worldServers;
                for (int length = (worldServers = this.worldServers).length, i = 0; i < length; ++i) {
                    final WorldServer worldserver = worldServers[i];
                    if (worldserver != null) {
                        worldserver.getWorldInfo().setDifficultyLocked(true);
                    }
                }
            }
        }
    }
    
    @Override
    public boolean canStructuresSpawn() {
        return false;
    }
    
    @Override
    public WorldSettings.GameType getGameType() {
        return this.theWorldSettings.getGameType();
    }
    
    @Override
    public EnumDifficulty getDifficulty() {
        return (this.mc.theWorld == null) ? this.mc.gameSettings.difficulty : this.mc.theWorld.getWorldInfo().getDifficulty();
    }
    
    @Override
    public boolean isHardcore() {
        return this.theWorldSettings.getHardcoreEnabled();
    }
    
    @Override
    public boolean shouldBroadcastRconToOps() {
        return true;
    }
    
    @Override
    public boolean shouldBroadcastConsoleToOps() {
        return true;
    }
    
    public void saveAllWorlds(final boolean dontLog) {
        if (dontLog) {
            final int i = this.getTickCounter();
            final int j = this.mc.gameSettings.ofAutoSaveTicks;
            if (i < this.ticksSaveLast + j) {
                return;
            }
            this.ticksSaveLast = i;
        }
        super.saveAllWorlds(dontLog);
    }
    
    @Override
    public File getDataDirectory() {
        return this.mc.mcDataDir;
    }
    
    @Override
    public boolean isDedicatedServer() {
        return false;
    }
    
    @Override
    public boolean shouldUseNativeTransport() {
        return false;
    }
    
    @Override
    protected void finalTick(final CrashReport report) {
        this.mc.crashed(report);
    }
    
    @Override
    public CrashReport addServerInfoToCrashReport(CrashReport report) {
        report = super.addServerInfoToCrashReport(report);
        report.getCategory().addCrashSectionCallable("Type", new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "Integrated Server (map_client.txt)";
            }
        });
        report.getCategory().addCrashSectionCallable("Is Modded", new Callable<String>() {
            @Override
            public String call() throws Exception {
                String s = ClientBrandRetriever.getClientModName();
                if (!s.equals("vanilla")) {
                    return "Definitely; Client brand changed to '" + s + "'";
                }
                s = IntegratedServer.this.getServerModName();
                return s.equals("vanilla") ? ((Minecraft.class.getSigners() == null) ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and both client + server brands are untouched.") : ("Definitely; Server brand changed to '" + s + "'");
            }
        });
        return report;
    }
    
    @Override
    public void setDifficultyForAllWorlds(final EnumDifficulty difficulty) {
        super.setDifficultyForAllWorlds(difficulty);
        if (this.mc.theWorld != null) {
            this.mc.theWorld.getWorldInfo().setDifficulty(difficulty);
        }
    }
    
    @Override
    public void addServerStatsToSnooper(final PlayerUsageSnooper playerSnooper) {
        super.addServerStatsToSnooper(playerSnooper);
        playerSnooper.addClientStat("snooper_partner", this.mc.getPlayerUsageSnooper().getUniqueID());
    }
    
    @Override
    public boolean isSnooperEnabled() {
        return Minecraft.getMinecraft().isSnooperEnabled();
    }
    
    @Override
    public String shareToLAN(final WorldSettings.GameType type, final boolean allowCheats) {
        try {
            int i = -1;
            try {
                i = HttpUtil.getSuitableLanPort();
            }
            catch (final IOException ex) {}
            if (i <= 0) {
                i = 25564;
            }
            this.getNetworkSystem().addLanEndpoint(null, i);
            IntegratedServer.logger.info("Started on " + i);
            this.isPublic = true;
            (this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), new StringBuilder(String.valueOf(i)).toString())).start();
            this.getConfigurationManager().setGameType(type);
            this.getConfigurationManager().setCommandsAllowedForAll(allowCheats);
            return new StringBuilder(String.valueOf(i)).toString();
        }
        catch (final IOException var6) {
            return null;
        }
    }
    
    @Override
    public void stopServer() {
        super.stopServer();
        if (this.lanServerPing != null) {
            this.lanServerPing.interrupt();
            this.lanServerPing = null;
        }
    }
    
    @Override
    public void initiateShutdown() {
        if (!Reflector.MinecraftForge.exists() || this.isServerRunning()) {
            Futures.getUnchecked((Future)this.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    for (final EntityPlayerMP entityplayermp : Lists.newArrayList((Iterable)IntegratedServer.this.getConfigurationManager().getPlayerList())) {
                        IntegratedServer.this.getConfigurationManager().playerLoggedOut(entityplayermp);
                    }
                }
            }));
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
    
    @Override
    public void setGameType(final WorldSettings.GameType gameMode) {
        this.getConfigurationManager().setGameType(gameMode);
    }
    
    @Override
    public boolean isCommandBlockEnabled() {
        return true;
    }
    
    @Override
    public int getOpPermissionLevel() {
        return 4;
    }
    
    private void onTick() {
        for (final WorldServer worldserver : Arrays.asList(this.worldServers)) {
            this.onTick(worldserver);
        }
    }
    
    public DifficultyInstance getDifficultyAsync(final World p_getDifficultyAsync_1_, final BlockPos p_getDifficultyAsync_2_) {
        this.difficultyUpdateWorld = p_getDifficultyAsync_1_;
        this.difficultyUpdatePos = p_getDifficultyAsync_2_;
        return this.difficultyLast;
    }
    
    private void onTick(final WorldServer p_onTick_1_) {
        if (!Config.isTimeDefault()) {
            this.fixWorldTime(p_onTick_1_);
        }
        if (!Config.isWeatherEnabled()) {
            this.fixWorldWeather(p_onTick_1_);
        }
        if (Config.waterOpacityChanged) {
            Config.waterOpacityChanged = false;
            ClearWater.updateWaterOpacity(Config.getGameSettings(), p_onTick_1_);
        }
        if (this.difficultyUpdateWorld == p_onTick_1_ && this.difficultyUpdatePos != null) {
            this.difficultyLast = p_onTick_1_.getDifficultyForLocation(this.difficultyUpdatePos);
            this.difficultyUpdateWorld = null;
            this.difficultyUpdatePos = null;
        }
    }
    
    private void fixWorldWeather(final WorldServer p_fixWorldWeather_1_) {
        final WorldInfo worldinfo = p_fixWorldWeather_1_.getWorldInfo();
        if (worldinfo.isRaining() || worldinfo.isThundering()) {
            worldinfo.setRainTime(0);
            worldinfo.setRaining(false);
            p_fixWorldWeather_1_.setRainStrength(0.0f);
            worldinfo.setThunderTime(0);
            worldinfo.setThundering(false);
            p_fixWorldWeather_1_.setThunderStrength(0.0f);
            this.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0f));
            this.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, 0.0f));
            this.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, 0.0f));
        }
    }
    
    private void fixWorldTime(final WorldServer p_fixWorldTime_1_) {
        final WorldInfo worldinfo = p_fixWorldTime_1_.getWorldInfo();
        if (worldinfo.getGameType().getID() == 1) {
            final long i = p_fixWorldTime_1_.getWorldTime();
            final long j = i % 24000L;
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
