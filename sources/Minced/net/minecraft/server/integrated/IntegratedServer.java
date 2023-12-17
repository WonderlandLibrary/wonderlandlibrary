// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.integrated;

import org.apache.logging.log4j.LogManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.optifine.ClearWater;
import net.minecraft.src.Config;
import java.util.Arrays;
import java.util.concurrent.Future;
import com.google.common.util.concurrent.Futures;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.collect.Lists;
import net.minecraft.util.HttpUtil;
import net.minecraft.profiler.Snooper;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReport;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameType;
import net.minecraft.util.Util;

import java.io.IOException;
import net.minecraft.util.CryptManager;
import net.minecraft.world.ServerWorldEventHandler;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldServer;
import net.optifine.reflect.Reflector;
import net.minecraft.world.WorldType;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.world.WorldServerDemo;

import java.io.File;
import net.minecraft.server.management.PlayerProfileCache;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.world.WorldSettings;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.MinecraftServer;

public class IntegratedServer extends MinecraftServer
{
    private static final Logger LOGGER;
    private final Minecraft mc;
    private final WorldSettings worldSettings;
    private boolean isGamePaused;
    private boolean isPublic;
    private ThreadLanServerPing lanServerPing;
    private long ticksSaveLast;
    public World difficultyUpdateWorld;
    public BlockPos difficultyUpdatePos;
    public DifficultyInstance difficultyLast;
    
    public IntegratedServer(final Minecraft clientIn, final String folderNameIn, final String worldNameIn, final WorldSettings worldSettingsIn, final YggdrasilAuthenticationService authServiceIn, final MinecraftSessionService sessionServiceIn, final GameProfileRepository profileRepoIn, final PlayerProfileCache profileCacheIn) {
        super(new File(clientIn.gameDir, "saves"), clientIn.getProxy(), clientIn.getDataFixer(), authServiceIn, sessionServiceIn, profileRepoIn, profileCacheIn);
        this.ticksSaveLast = 0L;
        this.difficultyUpdateWorld = null;
        this.difficultyUpdatePos = null;
        this.difficultyLast = null;
        this.setServerOwner(clientIn.getSession().getUsername());
        this.setFolderName(folderNameIn);
        this.setWorldName(worldNameIn);
        this.setDemo(clientIn.isDemo());
        this.canCreateBonusChest(worldSettingsIn.isBonusChestEnabled());
        this.setBuildLimit(256);
        this.setPlayerList(new IntegratedPlayerList(this));
        this.mc = clientIn;
        this.worldSettings = (this.isDemo() ? WorldServerDemo.DEMO_WORLD_SETTINGS : worldSettingsIn);
        final ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(folderNameIn, false);
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
    public ServerCommandManager createCommandManager() {
        return new IntegratedServerCommandManager(this);
    }
    
    @Override
    public void loadAllWorlds(final String saveName, final String worldNameIn, final long seed, final WorldType type, final String generatorOptions) {
        this.convertMapIfNeeded(saveName);
        final boolean flag = Reflector.DimensionManager.exists();
        if (!flag) {
            this.worlds = new WorldServer[3];
            this.timeOfLastDimensionTick = new long[this.worlds.length][100];
        }
        final ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(saveName, true);
        this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (worldinfo == null) {
            worldinfo = new WorldInfo(this.worldSettings, worldNameIn);
        }
        else {
            worldinfo.setWorldName(worldNameIn);
        }
        if (flag) {
            final WorldServer worldserver = (WorldServer)(this.isDemo() ? new WorldServerDemo(this, isavehandler, worldinfo, 0, this.profiler).init() : ((WorldServer)new WorldServer(this, isavehandler, worldinfo, 0, this.profiler).init()));
            worldserver.initialize(this.worldSettings);
            final Integer[] ainteger2;
            final Integer[] ainteger = ainteger2 = (Integer[])Reflector.call(Reflector.DimensionManager_getStaticDimensionIDs, new Object[0]);
            for (int i1 = ainteger.length, j1 = 0; j1 < i1; ++j1) {
                final int k = ainteger2[j1];
                final WorldServer worldserver2 = (WorldServer)((k == 0) ? worldserver : new WorldServerMulti(this, isavehandler, k, worldserver, this.profiler).init());
                worldserver2.addEventListener(new ServerWorldEventHandler(this, worldserver2));
                if (!this.isSinglePlayer()) {
                    worldserver2.getWorldInfo().setGameType(this.getGameType());
                }
                if (Reflector.EventBus.exists()) {
                    Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, worldserver2);
                }
            }
            this.getPlayerList().setPlayerManager(new WorldServer[] { worldserver });
            if (worldserver.getWorldInfo().getDifficulty() == null) {
                this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
            }
        }
        else {
            for (int l = 0; l < this.worlds.length; ++l) {
                int i2 = 0;
                if (l == 1) {
                    i2 = -1;
                }
                if (l == 2) {
                    i2 = 1;
                }
                if (l == 0) {
                    if (this.isDemo()) {
                        this.worlds[l] = (WorldServer)new WorldServerDemo(this, isavehandler, worldinfo, i2, this.profiler).init();
                    }
                    else {
                        this.worlds[l] = (WorldServer)new WorldServer(this, isavehandler, worldinfo, i2, this.profiler).init();
                    }
                    this.worlds[l].initialize(this.worldSettings);
                }
                else {
                    this.worlds[l] = (WorldServer)new WorldServerMulti(this, isavehandler, i2, this.worlds[0], this.profiler).init();
                }
                this.worlds[l].addEventListener(new ServerWorldEventHandler(this, this.worlds[l]));
            }
            this.getPlayerList().setPlayerManager(this.worlds);
            if (this.worlds[0].getWorldInfo().getDifficulty() == null) {
                this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
            }
        }
        this.initialWorldChunkLoad();
    }
    
    @Override
    public boolean init() throws IOException {
        IntegratedServer.LOGGER.info("Starting integrated minecraft server version 1.12.2");
        this.setOnlineMode(true);
        this.setCanSpawnAnimals(true);
        this.setCanSpawnNPCs(true);
        this.setAllowPvp(true);
        this.setAllowFlight(true);
        IntegratedServer.LOGGER.info("Generating keypair");
        this.setKeyPair(CryptManager.generateKeyPair());
        if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists()) {
            final Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            if (!Reflector.callBoolean(object, Reflector.FMLCommonHandler_handleServerAboutToStart, this)) {
                return false;
            }
        }
        this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.worldSettings.getSeed(), this.worldSettings.getTerrainType(), this.worldSettings.getGeneratorOptions());
        this.setMOTD(this.getServerOwner() + " - " + this.worlds[0].getWorldInfo().getWorldName());
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
        this.isGamePaused = (Minecraft.getMinecraft().getConnection() != null && Minecraft.getMinecraft().isGamePaused());
        if (!flag && this.isGamePaused) {
            IntegratedServer.LOGGER.info("Saving and pausing game...");
            this.getPlayerList().saveAllPlayerData();
            this.saveAllWorlds(false);
        }
        if (this.isGamePaused) {
            synchronized (this.futureTaskQueue) {
                while (!this.futureTaskQueue.isEmpty()) {
                    Util.runTask(this.futureTaskQueue.poll(), IntegratedServer.LOGGER);
                }
            }
        }
        else {
            super.tick();
            if (this.mc.gameSettings.renderDistanceChunks != this.getPlayerList().getViewDistance()) {
                IntegratedServer.LOGGER.info("Changing view distance to {}, from {}", (Object)this.mc.gameSettings.renderDistanceChunks, (Object)this.getPlayerList().getViewDistance());
                this.getPlayerList().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
            }
            if (this.mc.world != null) {
                final WorldInfo worldinfo1 = this.worlds[0].getWorldInfo();
                final WorldInfo worldinfo2 = this.mc.world.getWorldInfo();
                if (!worldinfo1.isDifficultyLocked() && worldinfo2.getDifficulty() != worldinfo1.getDifficulty()) {
                    IntegratedServer.LOGGER.info("Changing difficulty to {}, from {}", (Object)worldinfo2.getDifficulty(), (Object)worldinfo1.getDifficulty());
                    this.setDifficultyForAllWorlds(worldinfo2.getDifficulty());
                }
                else if (worldinfo2.isDifficultyLocked() && !worldinfo1.isDifficultyLocked()) {
                    IntegratedServer.LOGGER.info("Locking difficulty to {}", (Object)worldinfo2.getDifficulty());
                    for (final WorldServer worldserver : this.worlds) {
                        if (worldserver != null) {
                            worldserver.getWorldInfo().setDifficultyLocked(true);
                        }
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
    public GameType getGameType() {
        return this.worldSettings.getGameType();
    }
    
    @Override
    public EnumDifficulty getDifficulty() {
        return (this.mc.world == null) ? this.mc.gameSettings.difficulty : this.mc.world.getWorldInfo().getDifficulty();
    }
    
    @Override
    public boolean isHardcore() {
        return this.worldSettings.getHardcoreEnabled();
    }
    
    @Override
    public boolean shouldBroadcastRconToOps() {
        return true;
    }
    
    @Override
    public boolean shouldBroadcastConsoleToOps() {
        return true;
    }
    
    @Override
    public void saveAllWorlds(final boolean isSilent) {
        if (isSilent) {
            final int i = this.getTickCounter();
            final int j = this.mc.gameSettings.ofAutoSaveTicks;
            if (i < this.ticksSaveLast + j) {
                return;
            }
            this.ticksSaveLast = i;
        }
        super.saveAllWorlds(isSilent);
    }
    
    @Override
    public File getDataDirectory() {
        return this.mc.gameDir;
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
    public void finalTick(final CrashReport report) {
        this.mc.crashed(report);
    }
    
    @Override
    public CrashReport addServerInfoToCrashReport(CrashReport report) {
        report = super.addServerInfoToCrashReport(report);
        report.getCategory().addDetail("Type", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return "Integrated Server (map_client.txt)";
            }
        });
        report.getCategory().addDetail("Is Modded", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                String s = ClientBrandRetriever.getClientModName();
                if (!s.equals("vanilla")) {
                    return "Definitely; Client brand changed to '" + s + "'";
                }
                s = IntegratedServer.this.getServerModName();
                if (!"vanilla".equals(s)) {
                    return "Definitely; Server brand changed to '" + s + "'";
                }
                return (Minecraft.class.getSigners() == null) ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and both client + server brands are untouched.";
            }
        });
        return report;
    }
    
    @Override
    public void setDifficultyForAllWorlds(final EnumDifficulty difficulty) {
        super.setDifficultyForAllWorlds(difficulty);
        if (this.mc.world != null) {
            this.mc.world.getWorldInfo().setDifficulty(difficulty);
        }
    }
    
    @Override
    public void addServerStatsToSnooper(final Snooper playerSnooper) {
        super.addServerStatsToSnooper(playerSnooper);
        playerSnooper.addClientStat("snooper_partner", this.mc.getPlayerUsageSnooper().getUniqueID());
    }
    
    @Override
    public boolean isSnooperEnabled() {
        return Minecraft.getMinecraft().isSnooperEnabled();
    }
    
    @Override
    public String shareToLAN(final GameType type, final boolean allowCheats) {
        try {
            int i = -1;
            try {
                i = HttpUtil.getSuitableLanPort();
            }
            catch (IOException ex) {}
            if (i <= 0) {
                i = 25564;
            }
            this.getNetworkSystem().addEndpoint(null, i);
            IntegratedServer.LOGGER.info("Started on {}", (Object)i);
            this.isPublic = true;
            (this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), i + "")).start();
            this.getPlayerList().setGameType(type);
            this.getPlayerList().setCommandsAllowedForAll(allowCheats);
            final Minecraft mc = this.mc;
            Minecraft.player.setPermissionLevel(allowCheats ? 4 : 0);
            return i + "";
        }
        catch (IOException var61) {
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
            Futures.getUnchecked((Future<Object>)this.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    for (final EntityPlayerMP entityplayermp : Lists.newArrayList((Iterable<EntityPlayerMP>)IntegratedServer.this.getPlayerList().getPlayers())) {
                        final UUID uniqueID = entityplayermp.getUniqueID();
                        Minecraft mc1 = IntegratedServer.this.mc;
                        if (!uniqueID.equals(Minecraft.player.getUniqueID())) {
                            IntegratedServer.this.getPlayerList().playerLoggedOut(entityplayermp);
                        }
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
    
    public boolean getPublic() {
        return this.isPublic;
    }
    
    @Override
    public void setGameType(final GameType gameMode) {
        super.setGameType(gameMode);
        this.getPlayerList().setGameType(gameMode);
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
        for (final WorldServer worldserver : Arrays.asList(this.worlds)) {
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
            this.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(2, 0.0f));
            this.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(7, 0.0f));
            this.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(8, 0.0f));
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
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
