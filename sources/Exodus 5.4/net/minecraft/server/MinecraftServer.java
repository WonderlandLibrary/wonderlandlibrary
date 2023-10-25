/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Charsets
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Queues
 *  com.google.common.util.concurrent.Futures
 *  com.google.common.util.concurrent.ListenableFuture
 *  com.google.common.util.concurrent.ListenableFutureTask
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.GameProfileRepository
 *  com.mojang.authlib.minecraft.MinecraftSessionService
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  org.apache.commons.lang3.Validate
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.server;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.imageio.ImageIO;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ITickable;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Util;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MinecraftServer
implements IThreadListener,
IPlayerUsage,
Runnable,
ICommandSender {
    private final PlayerProfileCache profileCache;
    private boolean isGamemodeForced;
    private boolean enableBonusChest;
    private KeyPair serverKeyPair;
    private String folderName;
    protected final ICommandManager commandManager;
    private boolean worldIsBeingDeleted;
    private boolean serverRunning = true;
    private static MinecraftServer mcServer;
    public String currentTask;
    public final long[] tickTimeArray;
    private boolean canSpawnNPCs;
    private String userMessage;
    private final MinecraftSessionService sessionService;
    private ServerConfigurationManager serverConfigManager;
    private final Random random;
    private String motd;
    public int percentDone;
    public long[][] timeOfLastDimensionTick;
    private boolean isDemo;
    private String worldName;
    public WorldServer[] worldServers;
    private final NetworkSystem networkSystem;
    private int maxPlayerIdleMinutes = 0;
    private boolean serverIsRunning;
    private int buildLimit;
    private long timeOfLastWarning;
    private final GameProfileRepository profileRepo;
    protected final Queue<FutureTask<?>> futureTaskQueue;
    private boolean serverStopped;
    private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("server", this, MinecraftServer.getCurrentTimeMillis());
    private int tickCounter;
    private final YggdrasilAuthenticationService authService;
    public static final File USER_CACHE_FILE;
    public final Profiler theProfiler;
    private final List<ITickable> playersOnline = Lists.newArrayList();
    private Thread serverThread;
    private int serverPort = -1;
    private final ISaveFormat anvilConverterForAnvilFile;
    private long currentTime;
    private boolean onlineMode;
    private String resourcePackHash = "";
    private final ServerStatusResponse statusResponse;
    private long nanoTimeSinceStatusRefresh = 0L;
    private String resourcePackUrl = "";
    private boolean allowFlight;
    private String serverOwner;
    protected final Proxy serverProxy;
    private boolean canSpawnAnimals;
    private static final Logger logger;
    private boolean startProfiling;
    private boolean pvpEnabled;
    private final File anvilFile;

    @Override
    public boolean sendCommandFeedback() {
        return MinecraftServer.getServer().worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
    }

    public void setAllowPvp(boolean bl) {
        this.pvpEnabled = bl;
    }

    public boolean isAnvilFileSet() {
        return this.anvilFile != null;
    }

    @Override
    public boolean isSnooperEnabled() {
        return true;
    }

    public int getTickCounter() {
        return this.tickCounter;
    }

    public boolean isAnnouncingPlayerAchievements() {
        return true;
    }

    public boolean isDemo() {
        return this.isDemo;
    }

    public boolean serverIsInRunLoop() {
        return this.serverIsRunning;
    }

    protected ServerCommandManager createNewCommandManager() {
        return new ServerCommandManager();
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public void run() {
        block14: {
            block13: {
                try {
                    block15: {
                        if (!this.startServer()) break block15;
                        this.currentTime = MinecraftServer.getCurrentTimeMillis();
                        var1_1 = 0L;
                        this.statusResponse.setServerDescription(new ChatComponentText(this.motd));
                        this.statusResponse.setProtocolVersionInfo(new ServerStatusResponse.MinecraftProtocolVersionIdentifier("1.8.8", 47));
                        this.addFaviconToStatusResponse(this.statusResponse);
                        while (this.serverRunning) {
                            block16: {
                                var3_3 = MinecraftServer.getCurrentTimeMillis();
                                var5_5 = var3_3 - this.currentTime;
                                if (var5_5 > 2000L && this.currentTime - this.timeOfLastWarning >= 15000L) {
                                    MinecraftServer.logger.warn("Can't keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", new Object[]{var5_5, var5_5 / 50L});
                                    var5_5 = 2000L;
                                    this.timeOfLastWarning = this.currentTime;
                                }
                                if (var5_5 < 0L) {
                                    MinecraftServer.logger.warn("Time ran backwards! Did the system time change?");
                                    var5_5 = 0L;
                                }
                                var1_1 += var5_5;
                                this.currentTime = var3_3;
                                if (!this.worldServers[0].areAllPlayersAsleep()) ** GOTO lbl26
                                this.tick();
                                var1_1 = 0L;
                                break block16;
lbl-1000:
                                // 1 sources

                                {
                                    var1_1 -= 50L;
                                    this.tick();
lbl26:
                                    // 2 sources

                                    ** while (var1_1 > 50L)
                                }
                            }
                            Thread.sleep(Math.max(1L, 50L - var1_1));
                            this.serverIsRunning = true;
                        }
                        break block13;
                    }
                    this.finalTick(null);
                }
                catch (Throwable var1_2) {
                    MinecraftServer.logger.error("Encountered an unexpected exception", var1_2);
                    var2_6 = null;
                    var2_6 = var1_2 instanceof ReportedException != false ? this.addServerInfoToCrashReport(((ReportedException)var1_2).getCrashReport()) : this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", var1_2));
                    var3_4 = new File(new File(this.getDataDirectory(), "crash-reports"), "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt");
                    if (var2_6.saveToFile(var3_4)) {
                        MinecraftServer.logger.error("This crash report has been saved to: " + var3_4.getAbsolutePath());
                    } else {
                        MinecraftServer.logger.error("We were unable to save this crash report to disk.");
                    }
                    this.finalTick(var2_6);
                    try {
                        this.serverStopped = true;
                        this.stopServer();
                    }
                    catch (Throwable var8_7) {
                        MinecraftServer.logger.error("Exception stopping the server", var8_7);
                        this.systemExitNow();
                        break block14;
                    }
                    this.systemExitNow();
                    break block14;
                }
            }
            try {
                this.serverStopped = true;
                this.stopServer();
            }
            catch (Throwable var8_8) {
                MinecraftServer.logger.error("Exception stopping the server", var8_8);
                this.systemExitNow();
                break block14;
            }
            this.systemExitNow();
        }
    }

    public void deleteWorldAndStopServer() {
        this.worldIsBeingDeleted = true;
        this.getActiveAnvilConverter().flushCache();
        int n = 0;
        while (n < this.worldServers.length) {
            WorldServer worldServer = this.worldServers[n];
            if (worldServer != null) {
                worldServer.flush();
            }
            ++n;
        }
        this.getActiveAnvilConverter().deleteWorldDirectory(this.worldServers[0].getSaveHandler().getWorldDirectoryName());
        this.initiateShutdown();
    }

    public ServerConfigurationManager getConfigurationManager() {
        return this.serverConfigManager;
    }

    public PlayerProfileCache getPlayerProfileCache() {
        return this.profileCache;
    }

    public List<String> getTabCompletions(ICommandSender iCommandSender, String string, BlockPos blockPos) {
        ArrayList arrayList = Lists.newArrayList();
        if (string.startsWith("/")) {
            boolean bl = !(string = string.substring(1)).contains(" ");
            List<String> list = this.commandManager.getTabCompletionOptions(iCommandSender, string, blockPos);
            if (list != null) {
                for (String string2 : list) {
                    if (bl) {
                        arrayList.add("/" + string2);
                        continue;
                    }
                    arrayList.add(string2);
                }
            }
            return arrayList;
        }
        String[] stringArray = string.split(" ", -1);
        String string3 = stringArray[stringArray.length - 1];
        String[] stringArray2 = this.serverConfigManager.getAllUsernames();
        int n = stringArray2.length;
        int n2 = 0;
        while (n2 < n) {
            String string4 = stringArray2[n2];
            if (CommandBase.doesStringStartWith(string3, string4)) {
                arrayList.add(string4);
            }
            ++n2;
        }
        return arrayList;
    }

    public CrashReport addServerInfoToCrashReport(CrashReport crashReport) {
        crashReport.getCategory().addCrashSectionCallable("Profiler Position", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return MinecraftServer.this.theProfiler.profilingEnabled ? MinecraftServer.this.theProfiler.getNameOfLastSection() : "N/A (disabled)";
            }
        });
        if (this.serverConfigManager != null) {
            crashReport.getCategory().addCrashSectionCallable("Player Count", new Callable<String>(){

                @Override
                public String call() {
                    return String.valueOf(MinecraftServer.this.serverConfigManager.getCurrentPlayerCount()) + " / " + MinecraftServer.this.serverConfigManager.getMaxPlayers() + "; " + MinecraftServer.this.serverConfigManager.func_181057_v();
                }
            });
        }
        return crashReport;
    }

    protected void convertMapIfNeeded(String string) {
        if (this.getActiveAnvilConverter().isOldMapFormat(string)) {
            logger.info("Converting map!");
            this.setUserMessage("menu.convertingLevel");
            this.getActiveAnvilConverter().convertMapFormat(string, new IProgressUpdate(){
                private long startTime = System.currentTimeMillis();

                @Override
                public void displayLoadingString(String string) {
                }

                @Override
                public void resetProgressAndMessage(String string) {
                }

                @Override
                public void setDoneWorking() {
                }

                @Override
                public void setLoadingProgress(int n) {
                    if (System.currentTimeMillis() - this.startTime >= 1000L) {
                        this.startTime = System.currentTimeMillis();
                        logger.info("Converting... " + n + "%");
                    }
                }

                @Override
                public void displaySavingString(String string) {
                }
            });
        }
    }

    public Proxy getServerProxy() {
        return this.serverProxy;
    }

    public String getServerModName() {
        return "vanilla";
    }

    public void setDifficultyForAllWorlds(EnumDifficulty enumDifficulty) {
        int n = 0;
        while (n < this.worldServers.length) {
            WorldServer worldServer = this.worldServers[n];
            if (worldServer != null) {
                if (worldServer.getWorldInfo().isHardcoreModeEnabled()) {
                    worldServer.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
                    worldServer.setAllowedSpawnTypes(true, true);
                } else if (this.isSinglePlayer()) {
                    worldServer.getWorldInfo().setDifficulty(enumDifficulty);
                    worldServer.setAllowedSpawnTypes(worldServer.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                } else {
                    worldServer.getWorldInfo().setDifficulty(enumDifficulty);
                    worldServer.setAllowedSpawnTypes(this.allowSpawnMonsters(), this.canSpawnAnimals);
                }
            }
            ++n;
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(int n, String string) {
        return true;
    }

    public abstract String shareToLAN(WorldSettings.GameType var1, boolean var2);

    public String getServerOwner() {
        return this.serverOwner;
    }

    protected void setResourcePackFromWorld(String string, ISaveHandler iSaveHandler) {
        File file = new File(iSaveHandler.getWorldDirectory(), "resources.zip");
        if (file.isFile()) {
            this.setResourcePack("level://" + string + "/" + file.getName(), "");
        }
    }

    public abstract boolean isHardcore();

    public void updateTimeLightAndEntities() {
        this.theProfiler.startSection("jobs");
        Queue<FutureTask<?>> queue = this.futureTaskQueue;
        synchronized (queue) {
            while (!this.futureTaskQueue.isEmpty()) {
                Util.func_181617_a(this.futureTaskQueue.poll(), logger);
            }
        }
        this.theProfiler.endStartSection("levels");
        int n = 0;
        while (n < this.worldServers.length) {
            long l = System.nanoTime();
            if (n == 0 || this.getAllowNether()) {
                WorldServer worldServer = this.worldServers[n];
                this.theProfiler.startSection(worldServer.getWorldInfo().getWorldName());
                if (this.tickCounter % 20 == 0) {
                    this.theProfiler.startSection("timeSync");
                    this.serverConfigManager.sendPacketToAllPlayersInDimension(new S03PacketTimeUpdate(worldServer.getTotalWorldTime(), worldServer.getWorldTime(), worldServer.getGameRules().getBoolean("doDaylightCycle")), worldServer.provider.getDimensionId());
                    this.theProfiler.endSection();
                }
                this.theProfiler.startSection("tick");
                try {
                    worldServer.tick();
                }
                catch (Throwable throwable) {
                    CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Exception ticking world");
                    worldServer.addWorldInfoToCrashReport(crashReport);
                    throw new ReportedException(crashReport);
                }
                try {
                    worldServer.updateEntities();
                }
                catch (Throwable throwable) {
                    CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Exception ticking world entities");
                    worldServer.addWorldInfoToCrashReport(crashReport);
                    throw new ReportedException(crashReport);
                }
                this.theProfiler.endSection();
                this.theProfiler.startSection("tracker");
                worldServer.getEntityTracker().updateTrackedEntities();
                this.theProfiler.endSection();
                this.theProfiler.endSection();
            }
            this.timeOfLastDimensionTick[n][this.tickCounter % 100] = System.nanoTime() - l;
            ++n;
        }
        this.theProfiler.endStartSection("connection");
        this.getNetworkSystem().networkTick();
        this.theProfiler.endStartSection("players");
        this.serverConfigManager.onTick();
        this.theProfiler.endStartSection("tickables");
        n = 0;
        while (n < this.playersOnline.size()) {
            this.playersOnline.get(n).update();
            ++n;
        }
        this.theProfiler.endSection();
    }

    public KeyPair getKeyPair() {
        return this.serverKeyPair;
    }

    public NetworkSystem getNetworkSystem() {
        return this.networkSystem;
    }

    public void setWorldName(String string) {
        this.worldName = string;
    }

    @Override
    public World getEntityWorld() {
        return this.worldServers[0];
    }

    public void tick() {
        long l = System.nanoTime();
        ++this.tickCounter;
        if (this.startProfiling) {
            this.startProfiling = false;
            this.theProfiler.profilingEnabled = true;
            this.theProfiler.clearProfiling();
        }
        this.theProfiler.startSection("root");
        this.updateTimeLightAndEntities();
        if (l - this.nanoTimeSinceStatusRefresh >= 5000000000L) {
            this.nanoTimeSinceStatusRefresh = l;
            this.statusResponse.setPlayerCountData(new ServerStatusResponse.PlayerCountData(this.getMaxPlayers(), this.getCurrentPlayerCount()));
            GameProfile[] gameProfileArray = new GameProfile[Math.min(this.getCurrentPlayerCount(), 12)];
            int n = MathHelper.getRandomIntegerInRange(this.random, 0, this.getCurrentPlayerCount() - gameProfileArray.length);
            int n2 = 0;
            while (n2 < gameProfileArray.length) {
                gameProfileArray[n2] = this.serverConfigManager.func_181057_v().get(n + n2).getGameProfile();
                ++n2;
            }
            Collections.shuffle(Arrays.asList(gameProfileArray));
            this.statusResponse.getPlayerCountData().setPlayers(gameProfileArray);
        }
        if (this.tickCounter % 900 == 0) {
            this.theProfiler.startSection("save");
            this.serverConfigManager.saveAllPlayerData();
            this.saveAllWorlds(true);
            this.theProfiler.endSection();
        }
        this.theProfiler.startSection("tallying");
        this.tickTimeArray[this.tickCounter % 100] = System.nanoTime() - l;
        this.theProfiler.endSection();
        this.theProfiler.startSection("snooper");
        if (!this.usageSnooper.isSnooperRunning() && this.tickCounter > 100) {
            this.usageSnooper.startSnooper();
        }
        if (this.tickCounter % 6000 == 0) {
            this.usageSnooper.addMemoryStatsToSnooper();
        }
        this.theProfiler.endSection();
        this.theProfiler.endSection();
    }

    public String getMOTD() {
        return this.motd;
    }

    @Override
    public void setCommandStat(CommandResultStats.Type type, int n) {
    }

    public abstract EnumDifficulty getDifficulty();

    static {
        logger = LogManager.getLogger();
        USER_CACHE_FILE = new File("usercache.json");
    }

    public static MinecraftServer getServer() {
        return mcServer;
    }

    public GameProfile[] getGameProfiles() {
        return this.serverConfigManager.getAllProfiles();
    }

    public void enableProfiling() {
        this.startProfiling = true;
    }

    public MinecraftServer(Proxy proxy, File file) {
        this.theProfiler = new Profiler();
        this.statusResponse = new ServerStatusResponse();
        this.random = new Random();
        this.tickTimeArray = new long[100];
        this.futureTaskQueue = Queues.newArrayDeque();
        this.currentTime = MinecraftServer.getCurrentTimeMillis();
        this.serverProxy = proxy;
        mcServer = this;
        this.anvilFile = null;
        this.networkSystem = null;
        this.profileCache = new PlayerProfileCache(this, file);
        this.commandManager = null;
        this.anvilConverterForAnvilFile = null;
        this.authService = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
        this.sessionService = this.authService.createMinecraftSessionService();
        this.profileRepo = this.authService.createProfileRepository();
    }

    public void setMOTD(String string) {
        this.motd = string;
    }

    public int getMaxPlayerIdleMinutes() {
        return this.maxPlayerIdleMinutes;
    }

    protected abstract boolean startServer() throws IOException;

    public int getMaxPlayers() {
        return this.serverConfigManager.getMaxPlayers();
    }

    protected void saveAllWorlds(boolean bl) {
        if (!this.worldIsBeingDeleted) {
            WorldServer[] worldServerArray = this.worldServers;
            int n = this.worldServers.length;
            int n2 = 0;
            while (n2 < n) {
                WorldServer worldServer = worldServerArray[n2];
                if (worldServer != null) {
                    if (!bl) {
                        logger.info("Saving chunks for level '" + worldServer.getWorldInfo().getWorldName() + "'/" + worldServer.provider.getDimensionName());
                    }
                    try {
                        worldServer.saveAllChunks(true, null);
                    }
                    catch (MinecraftException minecraftException) {
                        logger.warn(minecraftException.getMessage());
                    }
                }
                ++n2;
            }
        }
    }

    @Override
    public Vec3 getPositionVector() {
        return new Vec3(0.0, 0.0, 0.0);
    }

    public boolean isServerStopped() {
        return this.serverStopped;
    }

    public void stopServer() {
        if (!this.worldIsBeingDeleted) {
            logger.info("Stopping server");
            if (this.getNetworkSystem() != null) {
                this.getNetworkSystem().terminateEndpoints();
            }
            if (this.serverConfigManager != null) {
                logger.info("Saving players");
                this.serverConfigManager.saveAllPlayerData();
                this.serverConfigManager.removeAllPlayers();
            }
            if (this.worldServers != null) {
                logger.info("Saving worlds");
                this.saveAllWorlds(false);
                int n = 0;
                while (n < this.worldServers.length) {
                    WorldServer worldServer = this.worldServers[n];
                    worldServer.flush();
                    ++n;
                }
            }
            if (this.usageSnooper.isSnooperRunning()) {
                this.usageSnooper.stopSnooper();
            }
        }
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(this.getName());
    }

    public boolean isSinglePlayer() {
        return this.serverOwner != null;
    }

    public boolean getAllowNether() {
        return true;
    }

    public abstract boolean func_183002_r();

    public String getMinecraftVersion() {
        return "1.8.8";
    }

    public abstract boolean canStructuresSpawn();

    public void setKeyPair(KeyPair keyPair) {
        this.serverKeyPair = keyPair;
    }

    protected void setInstance() {
        mcServer = this;
    }

    @Override
    public BlockPos getPosition() {
        return BlockPos.ORIGIN;
    }

    public void setOnlineMode(boolean bl) {
        this.onlineMode = bl;
    }

    @Override
    public void addServerStatsToSnooper(PlayerUsageSnooper playerUsageSnooper) {
        playerUsageSnooper.addClientStat("whitelist_enabled", false);
        playerUsageSnooper.addClientStat("whitelist_count", 0);
        if (this.serverConfigManager != null) {
            playerUsageSnooper.addClientStat("players_current", this.getCurrentPlayerCount());
            playerUsageSnooper.addClientStat("players_max", this.getMaxPlayers());
            playerUsageSnooper.addClientStat("players_seen", this.serverConfigManager.getAvailablePlayerDat().length);
        }
        playerUsageSnooper.addClientStat("uses_auth", this.onlineMode);
        playerUsageSnooper.addClientStat("gui_state", this.getGuiEnabled() ? "enabled" : "disabled");
        playerUsageSnooper.addClientStat("run_time", (MinecraftServer.getCurrentTimeMillis() - playerUsageSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L);
        playerUsageSnooper.addClientStat("avg_tick_ms", (int)(MathHelper.average(this.tickTimeArray) * 1.0E-6));
        int n = 0;
        if (this.worldServers != null) {
            int n2 = 0;
            while (n2 < this.worldServers.length) {
                if (this.worldServers[n2] != null) {
                    WorldServer worldServer = this.worldServers[n2];
                    WorldInfo worldInfo = worldServer.getWorldInfo();
                    playerUsageSnooper.addClientStat("world[" + n + "][dimension]", worldServer.provider.getDimensionId());
                    playerUsageSnooper.addClientStat("world[" + n + "][mode]", (Object)worldInfo.getGameType());
                    playerUsageSnooper.addClientStat("world[" + n + "][difficulty]", (Object)worldServer.getDifficulty());
                    playerUsageSnooper.addClientStat("world[" + n + "][hardcore]", worldInfo.isHardcoreModeEnabled());
                    playerUsageSnooper.addClientStat("world[" + n + "][generator_name]", worldInfo.getTerrainType().getWorldTypeName());
                    playerUsageSnooper.addClientStat("world[" + n + "][generator_version]", worldInfo.getTerrainType().getGeneratorVersion());
                    playerUsageSnooper.addClientStat("world[" + n + "][height]", this.buildLimit);
                    playerUsageSnooper.addClientStat("world[" + n + "][chunks_loaded]", worldServer.getChunkProvider().getLoadedChunkCount());
                    ++n;
                }
                ++n2;
            }
        }
        playerUsageSnooper.addClientStat("worlds", n);
    }

    public synchronized String getUserMessage() {
        return this.userMessage;
    }

    public void setAllowFlight(boolean bl) {
        this.allowFlight = bl;
    }

    public Entity getEntityFromUuid(UUID uUID) {
        WorldServer[] worldServerArray = this.worldServers;
        int n = this.worldServers.length;
        int n2 = 0;
        while (n2 < n) {
            Entity entity;
            WorldServer worldServer = worldServerArray[n2];
            if (worldServer != null && (entity = worldServer.getEntityFromUuid(uUID)) != null) {
                return entity;
            }
            ++n2;
        }
        return null;
    }

    public GameProfileRepository getGameProfileRepository() {
        return this.profileRepo;
    }

    protected void clearCurrentTask() {
        this.currentTask = null;
        this.percentDone = 0;
    }

    protected void systemExitNow() {
    }

    @Override
    public String getName() {
        return "Server";
    }

    public boolean isFlightAllowed() {
        return this.allowFlight;
    }

    public void setCanSpawnNPCs(boolean bl) {
        this.canSpawnNPCs = bl;
    }

    public void canCreateBonusChest(boolean bl) {
        this.enableBonusChest = bl;
    }

    public String getResourcePackUrl() {
        return this.resourcePackUrl;
    }

    @Override
    public ListenableFuture<Object> addScheduledTask(Runnable runnable) {
        Validate.notNull((Object)runnable);
        return this.callFromMainThread(Executors.callable(runnable));
    }

    public int getBuildLimit() {
        return this.buildLimit;
    }

    public void initiateShutdown() {
        this.serverRunning = false;
    }

    public void setConfigManager(ServerConfigurationManager serverConfigurationManager) {
        this.serverConfigManager = serverConfigurationManager;
    }

    protected boolean allowSpawnMonsters() {
        return true;
    }

    public void setDemo(boolean bl) {
        this.isDemo = bl;
    }

    public int getNetworkCompressionTreshold() {
        return 256;
    }

    public abstract boolean func_181035_ah();

    @Override
    public void addServerTypeToSnooper(PlayerUsageSnooper playerUsageSnooper) {
        playerUsageSnooper.addStatToSnooper("singleplayer", this.isSinglePlayer());
        playerUsageSnooper.addStatToSnooper("server_brand", this.getServerModName());
        playerUsageSnooper.addStatToSnooper("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        playerUsageSnooper.addStatToSnooper("dedicated", this.isDedicatedServer());
    }

    public void setGameType(WorldSettings.GameType gameType) {
        int n = 0;
        while (n < this.worldServers.length) {
            MinecraftServer.getServer().worldServers[n].getWorldInfo().setGameType(gameType);
            ++n;
        }
    }

    public ServerStatusResponse getServerStatusResponse() {
        return this.statusResponse;
    }

    public File getFile(String string) {
        return new File(this.getDataDirectory(), string);
    }

    public boolean getCanSpawnNPCs() {
        return this.canSpawnNPCs;
    }

    public ICommandManager getCommandManager() {
        return this.commandManager;
    }

    public abstract boolean isDedicatedServer();

    public MinecraftSessionService getMinecraftSessionService() {
        return this.sessionService;
    }

    @Override
    public void addChatMessage(IChatComponent iChatComponent) {
        logger.info(iChatComponent.getUnformattedText());
    }

    public void startServerThread() {
        this.serverThread = new Thread((Runnable)this, "Server thread");
        this.serverThread.start();
    }

    protected void loadAllWorlds(String string, String string2, long l, WorldType worldType, String string3) {
        WorldSettings worldSettings;
        this.convertMapIfNeeded(string);
        this.setUserMessage("menu.loadingLevel");
        this.worldServers = new WorldServer[3];
        this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
        ISaveHandler iSaveHandler = this.anvilConverterForAnvilFile.getSaveLoader(string, true);
        this.setResourcePackFromWorld(this.getFolderName(), iSaveHandler);
        WorldInfo worldInfo = iSaveHandler.loadWorldInfo();
        if (worldInfo == null) {
            if (this.isDemo()) {
                worldSettings = DemoWorldServer.demoWorldSettings;
            } else {
                worldSettings = new WorldSettings(l, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), worldType);
                worldSettings.setWorldName(string3);
                if (this.enableBonusChest) {
                    worldSettings.enableBonusChest();
                }
            }
            worldInfo = new WorldInfo(worldSettings, string2);
        } else {
            worldInfo.setWorldName(string2);
            worldSettings = new WorldSettings(worldInfo);
        }
        int n = 0;
        while (n < this.worldServers.length) {
            int n2 = 0;
            if (n == 1) {
                n2 = -1;
            }
            if (n == 2) {
                n2 = 1;
            }
            if (n == 0) {
                this.worldServers[n] = this.isDemo() ? (WorldServer)new DemoWorldServer(this, iSaveHandler, worldInfo, n2, this.theProfiler).init() : (WorldServer)new WorldServer(this, iSaveHandler, worldInfo, n2, this.theProfiler).init();
                this.worldServers[n].initialize(worldSettings);
            } else {
                this.worldServers[n] = (WorldServer)new WorldServerMulti(this, iSaveHandler, n2, this.worldServers[0], this.theProfiler).init();
            }
            this.worldServers[n].addWorldAccess(new WorldManager(this, this.worldServers[n]));
            if (!this.isSinglePlayer()) {
                this.worldServers[n].getWorldInfo().setGameType(this.getGameType());
            }
            ++n;
        }
        this.serverConfigManager.setPlayerManager(this.worldServers);
        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.initialWorldChunkLoad();
    }

    public abstract WorldSettings.GameType getGameType();

    public boolean getForceGamemode() {
        return this.isGamemodeForced;
    }

    public boolean isPVPEnabled() {
        return this.pvpEnabled;
    }

    public boolean isServerInOnlineMode() {
        return this.onlineMode;
    }

    public abstract boolean isCommandBlockEnabled();

    public boolean getCanSpawnAnimals() {
        return this.canSpawnAnimals;
    }

    public void setCanSpawnAnimals(boolean bl) {
        this.canSpawnAnimals = bl;
    }

    public String getResourcePackHash() {
        return this.resourcePackHash;
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public int getCurrentPlayerCount() {
        return this.serverConfigManager.getCurrentPlayerCount();
    }

    public String getFolderName() {
        return this.folderName;
    }

    public abstract int getOpPermissionLevel();

    protected void initialWorldChunkLoad() {
        int n = 16;
        int n2 = 4;
        int n3 = 192;
        int n4 = 625;
        int n5 = 0;
        this.setUserMessage("menu.generatingTerrain");
        int n6 = 0;
        logger.info("Preparing start region for level " + n6);
        WorldServer worldServer = this.worldServers[n6];
        BlockPos blockPos = worldServer.getSpawnPoint();
        long l = MinecraftServer.getCurrentTimeMillis();
        int n7 = -192;
        while (n7 <= 192 && this.isServerRunning()) {
            int n8 = -192;
            while (n8 <= 192 && this.isServerRunning()) {
                long l2 = MinecraftServer.getCurrentTimeMillis();
                if (l2 - l > 1000L) {
                    this.outputPercentRemaining("Preparing spawn area", n5 * 100 / 625);
                    l = l2;
                }
                ++n5;
                worldServer.theChunkProviderServer.loadChunk(blockPos.getX() + n7 >> 4, blockPos.getZ() + n8 >> 4);
                n8 += 16;
            }
            n7 += 16;
        }
        this.clearCurrentTask();
    }

    public boolean isBlockProtected(World world, BlockPos blockPos, EntityPlayer entityPlayer) {
        return false;
    }

    @Override
    public Entity getCommandSenderEntity() {
        return null;
    }

    @Override
    public boolean isCallingFromMinecraftThread() {
        return Thread.currentThread() == this.serverThread;
    }

    public int getSpawnProtectionSize() {
        return 16;
    }

    public void setFolderName(String string) {
        this.folderName = string;
    }

    public void logWarning(String string) {
        logger.warn(string);
    }

    public String[] getAllUsernames() {
        return this.serverConfigManager.getAllUsernames();
    }

    public WorldServer worldServerForDimension(int n) {
        return n == -1 ? this.worldServers[1] : (n == 1 ? this.worldServers[2] : this.worldServers[0]);
    }

    protected synchronized void setUserMessage(String string) {
        this.userMessage = string;
    }

    public abstract boolean func_181034_q();

    protected void finalTick(CrashReport crashReport) {
    }

    public boolean getGuiEnabled() {
        return false;
    }

    public ISaveFormat getActiveAnvilConverter() {
        return this.anvilConverterForAnvilFile;
    }

    public MinecraftServer(File file, Proxy proxy, File file2) {
        this.theProfiler = new Profiler();
        this.statusResponse = new ServerStatusResponse();
        this.random = new Random();
        this.tickTimeArray = new long[100];
        this.futureTaskQueue = Queues.newArrayDeque();
        this.currentTime = MinecraftServer.getCurrentTimeMillis();
        this.serverProxy = proxy;
        mcServer = this;
        this.anvilFile = file;
        this.networkSystem = new NetworkSystem(this);
        this.profileCache = new PlayerProfileCache(this, file2);
        this.commandManager = this.createNewCommandManager();
        this.anvilConverterForAnvilFile = new AnvilSaveConverter(file);
        this.authService = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
        this.sessionService = this.authService.createMinecraftSessionService();
        this.profileRepo = this.authService.createProfileRepository();
    }

    public PlayerUsageSnooper getPlayerUsageSnooper() {
        return this.usageSnooper;
    }

    public void refreshStatusNextTick() {
        this.nanoTimeSinceStatusRefresh = 0L;
    }

    public void setBuildLimit(int n) {
        this.buildLimit = n;
    }

    public File getDataDirectory() {
        return new File(".");
    }

    public boolean isServerRunning() {
        return this.serverRunning;
    }

    protected void outputPercentRemaining(String string, int n) {
        this.currentTask = string;
        this.percentDone = n;
        logger.info(String.valueOf(string) + ": " + n + "%");
    }

    public void setServerOwner(String string) {
        this.serverOwner = string;
    }

    private void addFaviconToStatusResponse(ServerStatusResponse serverStatusResponse) {
        block3: {
            File file = this.getFile("server-icon.png");
            if (file.isFile()) {
                ByteBuf byteBuf = Unpooled.buffer();
                try {
                    BufferedImage bufferedImage = ImageIO.read(file);
                    Validate.validState((bufferedImage.getWidth() == 64 ? 1 : 0) != 0, (String)"Must be 64 pixels wide", (Object[])new Object[0]);
                    Validate.validState((bufferedImage.getHeight() == 64 ? 1 : 0) != 0, (String)"Must be 64 pixels high", (Object[])new Object[0]);
                    ImageIO.write((RenderedImage)bufferedImage, "PNG", new ByteBufOutputStream(byteBuf));
                    ByteBuf byteBuf2 = Base64.encode(byteBuf);
                    serverStatusResponse.setFavicon("data:image/png;base64," + byteBuf2.toString(Charsets.UTF_8));
                }
                catch (Exception exception) {
                    logger.error("Couldn't load server icon", (Throwable)exception);
                    byteBuf.release();
                    break block3;
                }
                byteBuf.release();
            }
        }
    }

    public String getWorldName() {
        return this.worldName;
    }

    public <V> ListenableFuture<V> callFromMainThread(Callable<V> callable) {
        Validate.notNull(callable);
        if (!this.isCallingFromMinecraftThread() && !this.isServerStopped()) {
            ListenableFutureTask listenableFutureTask = ListenableFutureTask.create(callable);
            Queue<FutureTask<?>> queue = this.futureTaskQueue;
            synchronized (queue) {
                this.futureTaskQueue.add((FutureTask<?>)listenableFutureTask);
                return listenableFutureTask;
            }
        }
        try {
            return Futures.immediateFuture(callable.call());
        }
        catch (Exception exception) {
            return Futures.immediateFailedCheckedFuture((Exception)exception);
        }
    }

    public int getMaxWorldSize() {
        return 29999984;
    }

    public void setResourcePack(String string, String string2) {
        this.resourcePackUrl = string;
        this.resourcePackHash = string2;
    }

    public void setPlayerIdleTimeout(int n) {
        this.maxPlayerIdleMinutes = n;
    }
}

