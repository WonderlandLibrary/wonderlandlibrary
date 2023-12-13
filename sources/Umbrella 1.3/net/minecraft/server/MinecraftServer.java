/*
 * Decompiled with CFR 0.150.
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
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
implements ICommandSender,
Runnable,
IThreadListener,
IPlayerUsage {
    private static final Logger logger = LogManager.getLogger();
    public static final File USER_CACHE_FILE = new File("usercache.json");
    private static MinecraftServer mcServer;
    private final ISaveFormat anvilConverterForAnvilFile;
    private final PlayerUsageSnooper usageSnooper = new PlayerUsageSnooper("server", this, MinecraftServer.getCurrentTimeMillis());
    private final File anvilFile;
    private final List playersOnline = Lists.newArrayList();
    private final ICommandManager commandManager;
    public final Profiler theProfiler = new Profiler();
    private final NetworkSystem networkSystem;
    private final ServerStatusResponse statusResponse = new ServerStatusResponse();
    private final Random random = new Random();
    private int serverPort = -1;
    public WorldServer[] worldServers;
    private ServerConfigurationManager serverConfigManager;
    private boolean serverRunning = true;
    private boolean serverStopped;
    private int tickCounter;
    protected final Proxy serverProxy;
    public String currentTask;
    public int percentDone;
    private boolean onlineMode;
    private boolean canSpawnAnimals;
    private boolean canSpawnNPCs;
    private boolean pvpEnabled;
    private boolean allowFlight;
    private String motd;
    private int buildLimit;
    private int maxPlayerIdleMinutes = 0;
    public final long[] tickTimeArray = new long[100];
    public long[][] timeOfLastDimensionTick;
    private KeyPair serverKeyPair;
    private String serverOwner;
    private String folderName;
    private String worldName;
    private boolean isDemo;
    private boolean enableBonusChest;
    private boolean worldIsBeingDeleted;
    private String resourcePackUrl = "";
    private String resourcePackHash = "";
    private boolean serverIsRunning;
    private long timeOfLastWarning;
    private String userMessage;
    private boolean startProfiling;
    private boolean isGamemodeForced;
    private final YggdrasilAuthenticationService authService;
    private final MinecraftSessionService sessionService;
    private long nanoTimeSinceStatusRefresh = 0L;
    private final GameProfileRepository profileRepo;
    private final PlayerProfileCache profileCache;
    protected final Queue futureTaskQueue = Queues.newArrayDeque();
    private Thread serverThread;
    private long currentTime = MinecraftServer.getCurrentTimeMillis();
    private static final String __OBFID = "CL_00001462";

    public MinecraftServer(Proxy p_i46053_1_, File p_i46053_2_) {
        this.serverProxy = p_i46053_1_;
        mcServer = this;
        this.anvilFile = null;
        this.networkSystem = null;
        this.profileCache = new PlayerProfileCache(this, p_i46053_2_);
        this.commandManager = null;
        this.anvilConverterForAnvilFile = null;
        this.authService = new YggdrasilAuthenticationService(p_i46053_1_, UUID.randomUUID().toString());
        this.sessionService = this.authService.createMinecraftSessionService();
        this.profileRepo = this.authService.createProfileRepository();
    }

    public MinecraftServer(File workDir, Proxy proxy, File profileCacheDir) {
        this.serverProxy = proxy;
        mcServer = this;
        this.anvilFile = workDir;
        this.networkSystem = new NetworkSystem(this);
        this.profileCache = new PlayerProfileCache(this, profileCacheDir);
        this.commandManager = this.createNewCommandManager();
        this.anvilConverterForAnvilFile = new AnvilSaveConverter(workDir);
        this.authService = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
        this.sessionService = this.authService.createMinecraftSessionService();
        this.profileRepo = this.authService.createProfileRepository();
    }

    protected ServerCommandManager createNewCommandManager() {
        return new ServerCommandManager();
    }

    protected abstract boolean startServer() throws IOException;

    protected void convertMapIfNeeded(String worldNameIn) {
        if (this.getActiveAnvilConverter().isOldMapFormat(worldNameIn)) {
            logger.info("Converting map!");
            this.setUserMessage("menu.convertingLevel");
            this.getActiveAnvilConverter().convertMapFormat(worldNameIn, new IProgressUpdate(){
                private long startTime = System.currentTimeMillis();
                private static final String __OBFID = "CL_00001417";

                @Override
                public void displaySavingString(String message) {
                }

                @Override
                public void resetProgressAndMessage(String p_73721_1_) {
                }

                @Override
                public void setLoadingProgress(int progress) {
                    if (System.currentTimeMillis() - this.startTime >= 1000L) {
                        this.startTime = System.currentTimeMillis();
                        logger.info("Converting... " + progress + "%");
                    }
                }

                @Override
                public void setDoneWorking() {
                }

                @Override
                public void displayLoadingString(String message) {
                }
            });
        }
    }

    protected synchronized void setUserMessage(String message) {
        this.userMessage = message;
    }

    public synchronized String getUserMessage() {
        return this.userMessage;
    }

    protected void loadAllWorlds(String p_71247_1_, String p_71247_2_, long seed, WorldType type, String p_71247_6_) {
        WorldSettings var8;
        this.convertMapIfNeeded(p_71247_1_);
        this.setUserMessage("menu.loadingLevel");
        this.worldServers = new WorldServer[3];
        this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
        ISaveHandler var7 = this.anvilConverterForAnvilFile.getSaveLoader(p_71247_1_, true);
        this.setResourcePackFromWorld(this.getFolderName(), var7);
        WorldInfo var9 = var7.loadWorldInfo();
        if (var9 == null) {
            if (this.isDemo()) {
                var8 = DemoWorldServer.demoWorldSettings;
            } else {
                var8 = new WorldSettings(seed, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), type);
                var8.setWorldName(p_71247_6_);
                if (this.enableBonusChest) {
                    var8.enableBonusChest();
                }
            }
            var9 = new WorldInfo(var8, p_71247_2_);
        } else {
            var9.setWorldName(p_71247_2_);
            var8 = new WorldSettings(var9);
        }
        for (int var10 = 0; var10 < this.worldServers.length; ++var10) {
            int var11 = 0;
            if (var10 == 1) {
                var11 = -1;
            }
            if (var10 == 2) {
                var11 = 1;
            }
            if (var10 == 0) {
                this.worldServers[var10] = this.isDemo() ? (WorldServer)new DemoWorldServer(this, var7, var9, var11, this.theProfiler).init() : (WorldServer)new WorldServer(this, var7, var9, var11, this.theProfiler).init();
                this.worldServers[var10].initialize(var8);
            } else {
                this.worldServers[var10] = (WorldServer)new WorldServerMulti(this, var7, var11, this.worldServers[0], this.theProfiler).init();
            }
            this.worldServers[var10].addWorldAccess(new WorldManager(this, this.worldServers[var10]));
            if (this.isSinglePlayer()) continue;
            this.worldServers[var10].getWorldInfo().setGameType(this.getGameType());
        }
        this.serverConfigManager.setPlayerManager(this.worldServers);
        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.initialWorldChunkLoad();
    }

    protected void initialWorldChunkLoad() {
        boolean var1 = true;
        boolean var2 = true;
        boolean var3 = true;
        boolean var4 = true;
        int var5 = 0;
        this.setUserMessage("menu.generatingTerrain");
        int var6 = 0;
        logger.info("Preparing start region for level " + var6);
        WorldServer var7 = this.worldServers[var6];
        BlockPos var8 = var7.getSpawnPoint();
        long var9 = MinecraftServer.getCurrentTimeMillis();
        for (int var11 = -192; var11 <= 192 && this.isServerRunning(); var11 += 16) {
            for (int var12 = -192; var12 <= 192 && this.isServerRunning(); var12 += 16) {
                long var13 = MinecraftServer.getCurrentTimeMillis();
                if (var13 - var9 > 1000L) {
                    this.outputPercentRemaining("Preparing spawn area", var5 * 100 / 625);
                    var9 = var13;
                }
                ++var5;
                var7.theChunkProviderServer.loadChunk(var8.getX() + var11 >> 4, var8.getZ() + var12 >> 4);
            }
        }
        this.clearCurrentTask();
    }

    protected void setResourcePackFromWorld(String worldNameIn, ISaveHandler saveHandlerIn) {
        File var3 = new File(saveHandlerIn.getWorldDirectory(), "resources.zip");
        if (var3.isFile()) {
            this.setResourcePack("level://" + worldNameIn + "/" + var3.getName(), "");
        }
    }

    public abstract boolean canStructuresSpawn();

    public abstract WorldSettings.GameType getGameType();

    public abstract EnumDifficulty getDifficulty();

    public abstract boolean isHardcore();

    public abstract int getOpPermissionLevel();

    protected void outputPercentRemaining(String message, int percent) {
        this.currentTask = message;
        this.percentDone = percent;
        logger.info(String.valueOf(message) + ": " + percent + "%");
    }

    protected void clearCurrentTask() {
        this.currentTask = null;
        this.percentDone = 0;
    }

    protected void saveAllWorlds(boolean dontLog) {
        if (!this.worldIsBeingDeleted) {
            for (WorldServer var5 : this.worldServers) {
                if (var5 == null) continue;
                if (!dontLog) {
                    logger.info("Saving chunks for level '" + var5.getWorldInfo().getWorldName() + "'/" + var5.provider.getDimensionName());
                }
                try {
                    var5.saveAllChunks(true, null);
                }
                catch (MinecraftException var7) {
                    logger.warn(var7.getMessage());
                }
            }
        }
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
                for (int var1 = 0; var1 < this.worldServers.length; ++var1) {
                    WorldServer var2 = this.worldServers[var1];
                    var2.flush();
                }
            }
            if (this.usageSnooper.isSnooperRunning()) {
                this.usageSnooper.stopSnooper();
            }
        }
    }

    public boolean isServerRunning() {
        return this.serverRunning;
    }

    public void initiateShutdown() {
        this.serverRunning = false;
    }

    protected void func_175585_v() {
        mcServer = this;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void run() {
        try {
            if (!this.startServer()) {
                this.finalTick(null);
                return;
            }
            this.currentTime = MinecraftServer.getCurrentTimeMillis();
            var1 = 0L;
            this.statusResponse.setServerDescription(new ChatComponentText(this.motd));
            this.statusResponse.setProtocolVersionInfo(new ServerStatusResponse.MinecraftProtocolVersionIdentifier("1.8", 47));
            this.addFaviconToStatusResponse(this.statusResponse);
            ** GOTO lbl72
        }
        catch (Throwable var46) {
            MinecraftServer.logger.error("Encountered an unexpected exception", var46);
            var2 = null;
            var2 = var46 instanceof ReportedException != false ? this.addServerInfoToCrashReport(((ReportedException)var46).getCrashReport()) : this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", var46));
            var3 = new File(new File(this.getDataDirectory(), "crash-reports"), "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt");
            if (var2.saveToFile(var3)) {
                MinecraftServer.logger.error("This crash report has been saved to: " + var3.getAbsolutePath());
            } else {
                MinecraftServer.logger.error("We were unable to save this crash report to disk.");
            }
            this.finalTick(var2);
            try {
                try {
                    this.stopServer();
                    this.serverStopped = true;
                    return;
                }
                catch (Throwable var44) {
                    MinecraftServer.logger.error("Exception stopping the server", var44);
                    this.systemExitNow();
                    return;
                }
            }
            finally {
                this.systemExitNow();
            }
        }
        finally {
            block26: {
                try {
                    try {
                        this.stopServer();
                        this.serverStopped = true;
                    }
                    catch (Throwable var44) {
                        MinecraftServer.logger.error("Exception stopping the server", var44);
                        this.systemExitNow();
                        break block26;
                    }
                }
                catch (Throwable var9_12) {
                    this.systemExitNow();
                    throw var9_12;
                }
                this.systemExitNow();
            }
        }
lbl-1000:
        // 1 sources

        {
            block27: {
                var48 = MinecraftServer.getCurrentTimeMillis();
                var5 = var48 - this.currentTime;
                if (var5 > 2000L && this.currentTime - this.timeOfLastWarning >= 15000L) {
                    MinecraftServer.logger.warn("Can't keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", new Object[]{var5, var5 / 50L});
                    var5 = 2000L;
                    this.timeOfLastWarning = this.currentTime;
                }
                if (var5 < 0L) {
                    MinecraftServer.logger.warn("Time ran backwards! Did the system time change?");
                    var5 = 0L;
                }
                var1 += var5;
                this.currentTime = var48;
                if (!this.worldServers[0].areAllPlayersAsleep()) ** GOTO lbl68
                this.tick();
                var1 = 0L;
                break block27;
lbl-1000:
                // 1 sources

                {
                    var1 -= 50L;
                    this.tick();
lbl68:
                    // 2 sources

                    ** while (var1 > 50L)
                }
            }
            Thread.sleep(Math.max(1L, 50L - var1));
            this.serverIsRunning = true;
lbl72:
            // 2 sources

            ** while (this.serverRunning)
        }
lbl73:
        // 1 sources

    }

    private void addFaviconToStatusResponse(ServerStatusResponse response) {
        File var2 = this.getFile("server-icon.png");
        if (var2.isFile()) {
            ByteBuf var3 = Unpooled.buffer();
            try {
                try {
                    BufferedImage var4 = ImageIO.read(var2);
                    Validate.validState((var4.getWidth() == 64 ? 1 : 0) != 0, (String)"Must be 64 pixels wide", (Object[])new Object[0]);
                    Validate.validState((var4.getHeight() == 64 ? 1 : 0) != 0, (String)"Must be 64 pixels high", (Object[])new Object[0]);
                    ImageIO.write((RenderedImage)var4, "PNG", new ByteBufOutputStream(var3));
                    ByteBuf var5 = Base64.encode(var3);
                    response.setFavicon("data:image/png;base64," + var5.toString(Charsets.UTF_8));
                }
                catch (Exception var9) {
                    logger.error("Couldn't load server icon", (Throwable)var9);
                    var3.release();
                }
            }
            finally {
                var3.release();
            }
        }
    }

    public File getDataDirectory() {
        return new File(".");
    }

    protected void finalTick(CrashReport report) {
    }

    protected void systemExitNow() {
    }

    public void tick() {
        long var1 = System.nanoTime();
        ++this.tickCounter;
        if (this.startProfiling) {
            this.startProfiling = false;
            this.theProfiler.profilingEnabled = true;
            this.theProfiler.clearProfiling();
        }
        this.theProfiler.startSection("root");
        this.updateTimeLightAndEntities();
        if (var1 - this.nanoTimeSinceStatusRefresh >= 5000000000L) {
            this.nanoTimeSinceStatusRefresh = var1;
            this.statusResponse.setPlayerCountData(new ServerStatusResponse.PlayerCountData(this.getMaxPlayers(), this.getCurrentPlayerCount()));
            GameProfile[] var3 = new GameProfile[Math.min(this.getCurrentPlayerCount(), 12)];
            int var4 = MathHelper.getRandomIntegerInRange(this.random, 0, this.getCurrentPlayerCount() - var3.length);
            for (int var5 = 0; var5 < var3.length; ++var5) {
                var3[var5] = ((EntityPlayerMP)this.serverConfigManager.playerEntityList.get(var4 + var5)).getGameProfile();
            }
            Collections.shuffle(Arrays.asList(var3));
            this.statusResponse.getPlayerCountData().setPlayers(var3);
        }
        if (this.tickCounter % 900 == 0) {
            this.theProfiler.startSection("save");
            this.serverConfigManager.saveAllPlayerData();
            this.saveAllWorlds(true);
            this.theProfiler.endSection();
        }
        this.theProfiler.startSection("tallying");
        this.tickTimeArray[this.tickCounter % 100] = System.nanoTime() - var1;
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

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void updateTimeLightAndEntities() {
        int var11;
        block16: {
            this.theProfiler.startSection("jobs");
            Queue var1 = this.futureTaskQueue;
            Queue queue = this.futureTaskQueue;
            // MONITORENTER : queue
            while (true) {
                while (true) {
                    if (this.futureTaskQueue.isEmpty()) {
                        // MONITOREXIT : queue
                        break block16;
                    }
                    try {
                        ((FutureTask)this.futureTaskQueue.poll()).run();
                    }
                    catch (Throwable var9) {
                        logger.fatal((Object)var9);
                    }
                }
                break;
            }
            catch (Throwable throwable) {
                // MONITOREXIT : queue
                throw throwable;
            }
        }
        this.theProfiler.endStartSection("levels");
        for (var11 = 0; var11 < this.worldServers.length; ++var11) {
            long var2 = System.nanoTime();
            if (var11 == 0 || this.getAllowNether()) {
                WorldServer var4 = this.worldServers[var11];
                this.theProfiler.startSection(var4.getWorldInfo().getWorldName());
                if (this.tickCounter % 20 == 0) {
                    this.theProfiler.startSection("timeSync");
                    this.serverConfigManager.sendPacketToAllPlayersInDimension(new S03PacketTimeUpdate(var4.getTotalWorldTime(), var4.getWorldTime(), var4.getGameRules().getGameRuleBooleanValue("doDaylightCycle")), var4.provider.getDimensionId());
                    this.theProfiler.endSection();
                }
                this.theProfiler.startSection("tick");
                try {
                    var4.tick();
                }
                catch (Throwable var8) {
                    CrashReport var6 = CrashReport.makeCrashReport(var8, "Exception ticking world");
                    var4.addWorldInfoToCrashReport(var6);
                    throw new ReportedException(var6);
                }
                try {
                    var4.updateEntities();
                }
                catch (Throwable var7) {
                    CrashReport var6 = CrashReport.makeCrashReport(var7, "Exception ticking world entities");
                    var4.addWorldInfoToCrashReport(var6);
                    throw new ReportedException(var6);
                }
                this.theProfiler.endSection();
                this.theProfiler.startSection("tracker");
                var4.getEntityTracker().updateTrackedEntities();
                this.theProfiler.endSection();
                this.theProfiler.endSection();
            }
            this.timeOfLastDimensionTick[var11][this.tickCounter % 100] = System.nanoTime() - var2;
        }
        this.theProfiler.endStartSection("connection");
        this.getNetworkSystem().networkTick();
        this.theProfiler.endStartSection("players");
        this.serverConfigManager.onTick();
        this.theProfiler.endStartSection("tickables");
        for (var11 = 0; var11 < this.playersOnline.size(); ++var11) {
            ((IUpdatePlayerListBox)this.playersOnline.get(var11)).update();
        }
        this.theProfiler.endSection();
    }

    public boolean getAllowNether() {
        return true;
    }

    public void startServerThread() {
        this.serverThread = new Thread((Runnable)this, "Server thread");
        this.serverThread.start();
    }

    public File getFile(String fileName) {
        return new File(this.getDataDirectory(), fileName);
    }

    public void logWarning(String msg) {
        logger.warn(msg);
    }

    public WorldServer worldServerForDimension(int dimension) {
        return dimension == -1 ? this.worldServers[1] : (dimension == 1 ? this.worldServers[2] : this.worldServers[0]);
    }

    public String getMinecraftVersion() {
        return "1.8";
    }

    public int getCurrentPlayerCount() {
        return this.serverConfigManager.getCurrentPlayerCount();
    }

    public int getMaxPlayers() {
        return this.serverConfigManager.getMaxPlayers();
    }

    public String[] getAllUsernames() {
        return this.serverConfigManager.getAllUsernames();
    }

    public GameProfile[] getGameProfiles() {
        return this.serverConfigManager.getAllProfiles();
    }

    public String getServerModName() {
        return "vanilla";
    }

    public CrashReport addServerInfoToCrashReport(CrashReport report) {
        report.getCategory().addCrashSectionCallable("Profiler Position", new Callable(){
            private static final String __OBFID = "CL_00001418";

            public String func_179879_a() {
                return MinecraftServer.this.theProfiler.profilingEnabled ? MinecraftServer.this.theProfiler.getNameOfLastSection() : "N/A (disabled)";
            }

            public Object call() {
                return this.func_179879_a();
            }
        });
        if (this.serverConfigManager != null) {
            report.getCategory().addCrashSectionCallable("Player Count", new Callable(){
                private static final String __OBFID = "CL_00001419";

                public String call() {
                    return String.valueOf(MinecraftServer.this.serverConfigManager.getCurrentPlayerCount()) + " / " + MinecraftServer.this.serverConfigManager.getMaxPlayers() + "; " + ((MinecraftServer)MinecraftServer.this).serverConfigManager.playerEntityList;
                }
            });
        }
        return report;
    }

    public List func_180506_a(ICommandSender p_180506_1_, String p_180506_2_, BlockPos p_180506_3_) {
        ArrayList var4 = Lists.newArrayList();
        if (p_180506_2_.startsWith("/")) {
            boolean var11 = !(p_180506_2_ = p_180506_2_.substring(1)).contains(" ");
            List var12 = this.commandManager.getTabCompletionOptions(p_180506_1_, p_180506_2_, p_180506_3_);
            if (var12 != null) {
                for (String var14 : var12) {
                    if (var11) {
                        var4.add("/" + var14);
                        continue;
                    }
                    var4.add(var14);
                }
            }
            return var4;
        }
        String[] var5 = p_180506_2_.split(" ", -1);
        String var6 = var5[var5.length - 1];
        for (String var10 : this.serverConfigManager.getAllUsernames()) {
            if (!CommandBase.doesStringStartWith(var6, var10)) continue;
            var4.add(var10);
        }
        return var4;
    }

    public static MinecraftServer getServer() {
        return mcServer;
    }

    public boolean func_175578_N() {
        return this.anvilFile != null;
    }

    @Override
    public String getName() {
        return "Server";
    }

    @Override
    public void addChatMessage(IChatComponent message) {
        logger.info(message.getUnformattedText());
    }

    @Override
    public boolean canCommandSenderUseCommand(int permissionLevel, String command) {
        return true;
    }

    public ICommandManager getCommandManager() {
        return this.commandManager;
    }

    public KeyPair getKeyPair() {
        return this.serverKeyPair;
    }

    public String getServerOwner() {
        return this.serverOwner;
    }

    public void setServerOwner(String owner) {
        this.serverOwner = owner;
    }

    public boolean isSinglePlayer() {
        return this.serverOwner != null;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public void setFolderName(String name) {
        this.folderName = name;
    }

    public void setWorldName(String p_71246_1_) {
        this.worldName = p_71246_1_;
    }

    public String getWorldName() {
        return this.worldName;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.serverKeyPair = keyPair;
    }

    public void setDifficultyForAllWorlds(EnumDifficulty difficulty) {
        for (int var2 = 0; var2 < this.worldServers.length; ++var2) {
            WorldServer var3 = this.worldServers[var2];
            if (var3 == null) continue;
            if (var3.getWorldInfo().isHardcoreModeEnabled()) {
                var3.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
                var3.setAllowedSpawnTypes(true, true);
                continue;
            }
            if (this.isSinglePlayer()) {
                var3.getWorldInfo().setDifficulty(difficulty);
                var3.setAllowedSpawnTypes(var3.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                continue;
            }
            var3.getWorldInfo().setDifficulty(difficulty);
            var3.setAllowedSpawnTypes(this.allowSpawnMonsters(), this.canSpawnAnimals);
        }
    }

    protected boolean allowSpawnMonsters() {
        return true;
    }

    public boolean isDemo() {
        return this.isDemo;
    }

    public void setDemo(boolean demo) {
        this.isDemo = demo;
    }

    public void canCreateBonusChest(boolean enable) {
        this.enableBonusChest = enable;
    }

    public ISaveFormat getActiveAnvilConverter() {
        return this.anvilConverterForAnvilFile;
    }

    public void deleteWorldAndStopServer() {
        this.worldIsBeingDeleted = true;
        this.getActiveAnvilConverter().flushCache();
        for (int var1 = 0; var1 < this.worldServers.length; ++var1) {
            WorldServer var2 = this.worldServers[var1];
            if (var2 == null) continue;
            var2.flush();
        }
        this.getActiveAnvilConverter().deleteWorldDirectory(this.worldServers[0].getSaveHandler().getWorldDirectoryName());
        this.initiateShutdown();
    }

    public String getResourcePackUrl() {
        return this.resourcePackUrl;
    }

    public String getResourcePackHash() {
        return this.resourcePackHash;
    }

    public void setResourcePack(String url, String hash) {
        this.resourcePackUrl = url;
        this.resourcePackHash = hash;
    }

    @Override
    public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper) {
        playerSnooper.addClientStat("whitelist_enabled", false);
        playerSnooper.addClientStat("whitelist_count", 0);
        if (this.serverConfigManager != null) {
            playerSnooper.addClientStat("players_current", this.getCurrentPlayerCount());
            playerSnooper.addClientStat("players_max", this.getMaxPlayers());
            playerSnooper.addClientStat("players_seen", this.serverConfigManager.getAvailablePlayerDat().length);
        }
        playerSnooper.addClientStat("uses_auth", this.onlineMode);
        playerSnooper.addClientStat("gui_state", this.getGuiEnabled() ? "enabled" : "disabled");
        playerSnooper.addClientStat("run_time", (MinecraftServer.getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L);
        playerSnooper.addClientStat("avg_tick_ms", (int)(MathHelper.average(this.tickTimeArray) * 1.0E-6));
        int var2 = 0;
        if (this.worldServers != null) {
            for (int var3 = 0; var3 < this.worldServers.length; ++var3) {
                if (this.worldServers[var3] == null) continue;
                WorldServer var4 = this.worldServers[var3];
                WorldInfo var5 = var4.getWorldInfo();
                playerSnooper.addClientStat("world[" + var2 + "][dimension]", var4.provider.getDimensionId());
                playerSnooper.addClientStat("world[" + var2 + "][mode]", (Object)var5.getGameType());
                playerSnooper.addClientStat("world[" + var2 + "][difficulty]", (Object)var4.getDifficulty());
                playerSnooper.addClientStat("world[" + var2 + "][hardcore]", var5.isHardcoreModeEnabled());
                playerSnooper.addClientStat("world[" + var2 + "][generator_name]", var5.getTerrainType().getWorldTypeName());
                playerSnooper.addClientStat("world[" + var2 + "][generator_version]", var5.getTerrainType().getGeneratorVersion());
                playerSnooper.addClientStat("world[" + var2 + "][height]", this.buildLimit);
                playerSnooper.addClientStat("world[" + var2 + "][chunks_loaded]", var4.getChunkProvider().getLoadedChunkCount());
                ++var2;
            }
        }
        playerSnooper.addClientStat("worlds", var2);
    }

    @Override
    public void addServerTypeToSnooper(PlayerUsageSnooper playerSnooper) {
        playerSnooper.addStatToSnooper("singleplayer", this.isSinglePlayer());
        playerSnooper.addStatToSnooper("server_brand", this.getServerModName());
        playerSnooper.addStatToSnooper("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        playerSnooper.addStatToSnooper("dedicated", this.isDedicatedServer());
    }

    @Override
    public boolean isSnooperEnabled() {
        return true;
    }

    public abstract boolean isDedicatedServer();

    public boolean isServerInOnlineMode() {
        return this.onlineMode;
    }

    public void setOnlineMode(boolean online) {
        this.onlineMode = online;
    }

    public boolean getCanSpawnAnimals() {
        return this.canSpawnAnimals;
    }

    public void setCanSpawnAnimals(boolean spawnAnimals) {
        this.canSpawnAnimals = spawnAnimals;
    }

    public boolean getCanSpawnNPCs() {
        return this.canSpawnNPCs;
    }

    public void setCanSpawnNPCs(boolean spawnNpcs) {
        this.canSpawnNPCs = spawnNpcs;
    }

    public boolean isPVPEnabled() {
        return this.pvpEnabled;
    }

    public void setAllowPvp(boolean allowPvp) {
        this.pvpEnabled = allowPvp;
    }

    public boolean isFlightAllowed() {
        return this.allowFlight;
    }

    public void setAllowFlight(boolean allow) {
        this.allowFlight = allow;
    }

    public abstract boolean isCommandBlockEnabled();

    public String getMOTD() {
        return this.motd;
    }

    public void setMOTD(String motdIn) {
        this.motd = motdIn;
    }

    public int getBuildLimit() {
        return this.buildLimit;
    }

    public void setBuildLimit(int maxBuildHeight) {
        this.buildLimit = maxBuildHeight;
    }

    public ServerConfigurationManager getConfigurationManager() {
        return this.serverConfigManager;
    }

    public void setConfigManager(ServerConfigurationManager configManager) {
        this.serverConfigManager = configManager;
    }

    public void setGameType(WorldSettings.GameType gameMode) {
        for (int var2 = 0; var2 < this.worldServers.length; ++var2) {
            MinecraftServer.getServer().worldServers[var2].getWorldInfo().setGameType(gameMode);
        }
    }

    public NetworkSystem getNetworkSystem() {
        return this.networkSystem;
    }

    public boolean serverIsInRunLoop() {
        return this.serverIsRunning;
    }

    public boolean getGuiEnabled() {
        return false;
    }

    public abstract String shareToLAN(WorldSettings.GameType var1, boolean var2);

    public int getTickCounter() {
        return this.tickCounter;
    }

    public void enableProfiling() {
        this.startProfiling = true;
    }

    public PlayerUsageSnooper getPlayerUsageSnooper() {
        return this.usageSnooper;
    }

    @Override
    public BlockPos getPosition() {
        return BlockPos.ORIGIN;
    }

    @Override
    public Vec3 getPositionVector() {
        return new Vec3(0.0, 0.0, 0.0);
    }

    @Override
    public World getEntityWorld() {
        return this.worldServers[0];
    }

    @Override
    public Entity getCommandSenderEntity() {
        return null;
    }

    public int getSpawnProtectionSize() {
        return 16;
    }

    public boolean isBlockProtected(World worldIn, BlockPos pos, EntityPlayer playerIn) {
        return false;
    }

    public boolean getForceGamemode() {
        return this.isGamemodeForced;
    }

    public Proxy getServerProxy() {
        return this.serverProxy;
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public int getMaxPlayerIdleMinutes() {
        return this.maxPlayerIdleMinutes;
    }

    public void setPlayerIdleTimeout(int idleTimeout) {
        this.maxPlayerIdleMinutes = idleTimeout;
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(this.getName());
    }

    public boolean isAnnouncingPlayerAchievements() {
        return true;
    }

    public MinecraftSessionService getMinecraftSessionService() {
        return this.sessionService;
    }

    public GameProfileRepository getGameProfileRepository() {
        return this.profileRepo;
    }

    public PlayerProfileCache getPlayerProfileCache() {
        return this.profileCache;
    }

    public ServerStatusResponse getServerStatusResponse() {
        return this.statusResponse;
    }

    public void refreshStatusNextTick() {
        this.nanoTimeSinceStatusRefresh = 0L;
    }

    public Entity getEntityFromUuid(UUID uuid) {
        for (WorldServer var5 : this.worldServers) {
            Entity var6;
            if (var5 == null || (var6 = var5.getEntityFromUuid(uuid)) == null) continue;
            return var6;
        }
        return null;
    }

    @Override
    public boolean sendCommandFeedback() {
        return MinecraftServer.getServer().worldServers[0].getGameRules().getGameRuleBooleanValue("sendCommandFeedback");
    }

    @Override
    public void func_174794_a(CommandResultStats.Type p_174794_1_, int p_174794_2_) {
    }

    public int getMaxWorldSize() {
        return 29999984;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public ListenableFuture callFromMainThread(Callable callable) {
        Validate.notNull((Object)callable);
        if (!this.isCallingFromMinecraftThread()) {
            ListenableFutureTask var2 = ListenableFutureTask.create((Callable)callable);
            Queue var3 = this.futureTaskQueue;
            Queue queue = this.futureTaskQueue;
            synchronized (queue) {
                this.futureTaskQueue.add(var2);
                return var2;
            }
        }
        try {
            return Futures.immediateFuture(callable.call());
        }
        catch (Exception var6) {
            return Futures.immediateFailedCheckedFuture((Exception)var6);
        }
    }

    @Override
    public ListenableFuture addScheduledTask(Runnable runnableToSchedule) {
        Validate.notNull((Object)runnableToSchedule);
        return this.callFromMainThread(Executors.callable(runnableToSchedule));
    }

    @Override
    public boolean isCallingFromMinecraftThread() {
        return Thread.currentThread() == this.serverThread;
    }

    public int getNetworkCompressionTreshold() {
        return 256;
    }
}

