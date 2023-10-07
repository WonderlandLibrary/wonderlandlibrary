// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server;

import org.apache.logging.log4j.LogManager;
import java.util.concurrent.Executors;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.command.CommandResultStats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.util.Vec3;
import java.awt.GraphicsEnvironment;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.command.CommandBase;
import java.util.concurrent.Callable;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import java.util.concurrent.FutureTask;
import java.util.Collections;
import java.util.Arrays;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import com.mojang.authlib.GameProfile;
import java.awt.image.BufferedImage;
import io.netty.buffer.ByteBuf;
import com.google.common.base.Charsets;
import io.netty.handler.codec.base64.Base64;
import java.io.OutputStream;
import java.awt.image.RenderedImage;
import io.netty.buffer.ByteBufOutputStream;
import org.apache.commons.lang3.Validate;
import javax.imageio.ImageIO;
import io.netty.buffer.Unpooled;
import java.util.Date;
import java.text.SimpleDateFormat;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.util.BlockPos;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.util.IProgressUpdate;
import java.io.IOException;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import java.util.UUID;
import com.google.common.collect.Queues;
import com.google.common.collect.Lists;
import java.util.Queue;
import net.minecraft.server.management.PlayerProfileCache;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.security.KeyPair;
import java.net.Proxy;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.world.WorldServer;
import java.util.Random;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.NetworkSystem;
import net.minecraft.profiler.Profiler;
import net.minecraft.command.ICommandManager;
import java.util.List;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.world.storage.ISaveFormat;
import java.io.File;
import org.apache.logging.log4j.Logger;
import net.minecraft.profiler.IPlayerUsage;
import net.minecraft.util.IThreadListener;
import net.minecraft.command.ICommandSender;

public abstract class MinecraftServer implements ICommandSender, Runnable, IThreadListener, IPlayerUsage
{
    private static final Logger logger;
    public static final File USER_CACHE_FILE;
    private static MinecraftServer mcServer;
    private final ISaveFormat anvilConverterForAnvilFile;
    private final PlayerUsageSnooper usageSnooper;
    private final File anvilFile;
    private final List playersOnline;
    private final ICommandManager commandManager;
    public final Profiler theProfiler;
    private final NetworkSystem networkSystem;
    private final ServerStatusResponse statusResponse;
    private final Random random;
    private int serverPort;
    public WorldServer[] worldServers;
    private ServerConfigurationManager serverConfigManager;
    private boolean serverRunning;
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
    private int maxPlayerIdleMinutes;
    public final long[] tickTimeArray;
    public long[][] timeOfLastDimensionTick;
    private KeyPair serverKeyPair;
    private String serverOwner;
    private String folderName;
    private String worldName;
    private boolean isDemo;
    private boolean enableBonusChest;
    private boolean worldIsBeingDeleted;
    private String resourcePackUrl;
    private String resourcePackHash;
    private boolean serverIsRunning;
    private long timeOfLastWarning;
    private String userMessage;
    private boolean startProfiling;
    private boolean isGamemodeForced;
    private final YggdrasilAuthenticationService authService;
    private final MinecraftSessionService sessionService;
    private long nanoTimeSinceStatusRefresh;
    private final GameProfileRepository profileRepo;
    private final PlayerProfileCache profileCache;
    protected final Queue futureTaskQueue;
    private Thread serverThread;
    private long currentTime;
    private static final String __OBFID = "CL_00001462";
    
    public MinecraftServer(final Proxy p_i46053_1_, final File p_i46053_2_) {
        this.usageSnooper = new PlayerUsageSnooper("server", this, getCurrentTimeMillis());
        this.playersOnline = Lists.newArrayList();
        this.theProfiler = new Profiler();
        this.statusResponse = new ServerStatusResponse();
        this.random = new Random();
        this.serverPort = -1;
        this.serverRunning = true;
        this.maxPlayerIdleMinutes = 0;
        this.tickTimeArray = new long[100];
        this.resourcePackUrl = "";
        this.resourcePackHash = "";
        this.nanoTimeSinceStatusRefresh = 0L;
        this.futureTaskQueue = Queues.newArrayDeque();
        this.currentTime = getCurrentTimeMillis();
        this.serverProxy = p_i46053_1_;
        MinecraftServer.mcServer = this;
        this.anvilFile = null;
        this.networkSystem = null;
        this.profileCache = new PlayerProfileCache(this, p_i46053_2_);
        this.commandManager = null;
        this.anvilConverterForAnvilFile = null;
        this.authService = new YggdrasilAuthenticationService(p_i46053_1_, UUID.randomUUID().toString());
        this.sessionService = this.authService.createMinecraftSessionService();
        this.profileRepo = this.authService.createProfileRepository();
    }
    
    public MinecraftServer(final File workDir, final Proxy proxy, final File profileCacheDir) {
        this.usageSnooper = new PlayerUsageSnooper("server", this, getCurrentTimeMillis());
        this.playersOnline = Lists.newArrayList();
        this.theProfiler = new Profiler();
        this.statusResponse = new ServerStatusResponse();
        this.random = new Random();
        this.serverPort = -1;
        this.serverRunning = true;
        this.maxPlayerIdleMinutes = 0;
        this.tickTimeArray = new long[100];
        this.resourcePackUrl = "";
        this.resourcePackHash = "";
        this.nanoTimeSinceStatusRefresh = 0L;
        this.futureTaskQueue = Queues.newArrayDeque();
        this.currentTime = getCurrentTimeMillis();
        this.serverProxy = proxy;
        MinecraftServer.mcServer = this;
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
    
    protected void convertMapIfNeeded(final String worldNameIn) {
        if (this.getActiveAnvilConverter().isOldMapFormat(worldNameIn)) {
            MinecraftServer.logger.info("Converting map!");
            this.setUserMessage("menu.convertingLevel");
            this.getActiveAnvilConverter().convertMapFormat(worldNameIn, new IProgressUpdate() {
                private long startTime = System.currentTimeMillis();
                private static final String __OBFID = "CL_00001417";
                
                @Override
                public void displaySavingString(final String message) {
                }
                
                @Override
                public void resetProgressAndMessage(final String p_73721_1_) {
                }
                
                @Override
                public void setLoadingProgress(final int progress) {
                    if (System.currentTimeMillis() - this.startTime >= 1000L) {
                        this.startTime = System.currentTimeMillis();
                        MinecraftServer.logger.info("Converting... " + progress + "%");
                    }
                }
                
                @Override
                public void setDoneWorking() {
                }
                
                @Override
                public void displayLoadingString(final String message) {
                }
            });
        }
    }
    
    protected synchronized void setUserMessage(final String message) {
        this.userMessage = message;
    }
    
    public synchronized String getUserMessage() {
        return this.userMessage;
    }
    
    protected void loadAllWorlds(final String p_71247_1_, final String p_71247_2_, final long seed, final WorldType type, final String p_71247_6_) {
        this.convertMapIfNeeded(p_71247_1_);
        this.setUserMessage("menu.loadingLevel");
        this.worldServers = new WorldServer[3];
        this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
        final ISaveHandler var7 = this.anvilConverterForAnvilFile.getSaveLoader(p_71247_1_, true);
        this.setResourcePackFromWorld(this.getFolderName(), var7);
        WorldInfo var8 = var7.loadWorldInfo();
        WorldSettings var9;
        if (var8 == null) {
            if (this.isDemo()) {
                var9 = DemoWorldServer.demoWorldSettings;
            }
            else {
                var9 = new WorldSettings(seed, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), type);
                var9.setWorldName(p_71247_6_);
                if (this.enableBonusChest) {
                    var9.enableBonusChest();
                }
            }
            var8 = new WorldInfo(var9, p_71247_2_);
        }
        else {
            var8.setWorldName(p_71247_2_);
            var9 = new WorldSettings(var8);
        }
        for (int var10 = 0; var10 < this.worldServers.length; ++var10) {
            byte var11 = 0;
            if (var10 == 1) {
                var11 = -1;
            }
            if (var10 == 2) {
                var11 = 1;
            }
            if (var10 == 0) {
                if (this.isDemo()) {
                    this.worldServers[var10] = (WorldServer)new DemoWorldServer(this, var7, var8, var11, this.theProfiler).init();
                }
                else {
                    this.worldServers[var10] = (WorldServer)new WorldServer(this, var7, var8, var11, this.theProfiler).init();
                }
                this.worldServers[var10].initialize(var9);
            }
            else {
                this.worldServers[var10] = (WorldServer)new WorldServerMulti(this, var7, var11, this.worldServers[0], this.theProfiler).init();
            }
            this.worldServers[var10].addWorldAccess(new WorldManager(this, this.worldServers[var10]));
            if (!this.isSinglePlayer()) {
                this.worldServers[var10].getWorldInfo().setGameType(this.getGameType());
            }
        }
        this.serverConfigManager.setPlayerManager(this.worldServers);
        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.initialWorldChunkLoad();
    }
    
    protected void initialWorldChunkLoad() {
        final boolean var1 = true;
        final boolean var2 = true;
        final boolean var3 = true;
        final boolean var4 = true;
        int var5 = 0;
        this.setUserMessage("menu.generatingTerrain");
        final byte var6 = 0;
        MinecraftServer.logger.info("Preparing start region for level " + var6);
        final WorldServer var7 = this.worldServers[var6];
        final BlockPos var8 = var7.getSpawnPoint();
        long var9 = getCurrentTimeMillis();
        for (int var10 = -192; var10 <= 192 && this.isServerRunning(); var10 += 16) {
            for (int var11 = -192; var11 <= 192 && this.isServerRunning(); var11 += 16) {
                final long var12 = getCurrentTimeMillis();
                if (var12 - var9 > 1000L) {
                    this.outputPercentRemaining("Preparing spawn area", var5 * 100 / 625);
                    var9 = var12;
                }
                ++var5;
                var7.theChunkProviderServer.loadChunk(var8.getX() + var10 >> 4, var8.getZ() + var11 >> 4);
            }
        }
        this.clearCurrentTask();
    }
    
    protected void setResourcePackFromWorld(final String worldNameIn, final ISaveHandler saveHandlerIn) {
        final File var3 = new File(saveHandlerIn.getWorldDirectory(), "resources.zip");
        if (var3.isFile()) {
            this.setResourcePack("level://" + worldNameIn + "/" + var3.getName(), "");
        }
    }
    
    public abstract boolean canStructuresSpawn();
    
    public abstract WorldSettings.GameType getGameType();
    
    public abstract EnumDifficulty getDifficulty();
    
    public abstract boolean isHardcore();
    
    public abstract int getOpPermissionLevel();
    
    protected void outputPercentRemaining(final String message, final int percent) {
        this.currentTask = message;
        this.percentDone = percent;
        MinecraftServer.logger.info(message + ": " + percent + "%");
    }
    
    protected void clearCurrentTask() {
        this.currentTask = null;
        this.percentDone = 0;
    }
    
    protected void saveAllWorlds(final boolean dontLog) {
        if (!this.worldIsBeingDeleted) {
            for (final WorldServer var5 : this.worldServers) {
                if (var5 != null) {
                    if (!dontLog) {
                        MinecraftServer.logger.info("Saving chunks for level '" + var5.getWorldInfo().getWorldName() + "'/" + var5.provider.getDimensionName());
                    }
                    try {
                        var5.saveAllChunks(true, null);
                    }
                    catch (MinecraftException var6) {
                        MinecraftServer.logger.warn(var6.getMessage());
                    }
                }
            }
        }
    }
    
    public void stopServer() {
        if (!this.worldIsBeingDeleted) {
            MinecraftServer.logger.info("Stopping server");
            if (this.getNetworkSystem() != null) {
                this.getNetworkSystem().terminateEndpoints();
            }
            if (this.serverConfigManager != null) {
                MinecraftServer.logger.info("Saving players");
                this.serverConfigManager.saveAllPlayerData();
                this.serverConfigManager.removeAllPlayers();
            }
            if (this.worldServers != null) {
                MinecraftServer.logger.info("Saving worlds");
                this.saveAllWorlds(false);
                for (final WorldServer var2 : this.worldServers) {
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
        MinecraftServer.mcServer = this;
    }
    
    @Override
    public void run() {
        try {
            if (this.startServer()) {
                this.currentTime = getCurrentTimeMillis();
                long var1 = 0L;
                this.statusResponse.setServerDescription(new ChatComponentText(this.motd));
                this.statusResponse.setProtocolVersionInfo(new ServerStatusResponse.MinecraftProtocolVersionIdentifier("1.8", 47));
                this.addFaviconToStatusResponse(this.statusResponse);
                while (this.serverRunning) {
                    final long var2 = getCurrentTimeMillis();
                    long var3 = var2 - this.currentTime;
                    if (var3 > 2000L && this.currentTime - this.timeOfLastWarning >= 15000L) {
                        MinecraftServer.logger.warn("Can't keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", new Object[] { var3, var3 / 50L });
                        var3 = 2000L;
                        this.timeOfLastWarning = this.currentTime;
                    }
                    if (var3 < 0L) {
                        MinecraftServer.logger.warn("Time ran backwards! Did the system time change?");
                        var3 = 0L;
                    }
                    var1 += var3;
                    this.currentTime = var2;
                    if (this.worldServers[0].areAllPlayersAsleep()) {
                        this.tick();
                        var1 = 0L;
                    }
                    else {
                        while (var1 > 50L) {
                            var1 -= 50L;
                            this.tick();
                        }
                    }
                    Thread.sleep(Math.max(1L, 50L - var1));
                    this.serverIsRunning = true;
                }
            }
            else {
                this.finalTick(null);
            }
        }
        catch (Throwable var4) {
            MinecraftServer.logger.error("Encountered an unexpected exception", var4);
            CrashReport var5 = null;
            if (var4 instanceof ReportedException) {
                var5 = this.addServerInfoToCrashReport(((ReportedException)var4).getCrashReport());
            }
            else {
                var5 = this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", var4));
            }
            final File var6 = new File(new File(this.getDataDirectory(), "crash-reports"), "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt");
            if (var5.saveToFile(var6)) {
                MinecraftServer.logger.error("This crash report has been saved to: " + var6.getAbsolutePath());
            }
            else {
                MinecraftServer.logger.error("We were unable to save this crash report to disk.");
            }
            this.finalTick(var5);
            try {
                this.stopServer();
                this.serverStopped = true;
            }
            catch (Throwable var7) {
                MinecraftServer.logger.error("Exception stopping the server", var7);
            }
            finally {
                this.systemExitNow();
            }
        }
        finally {
            try {
                this.stopServer();
                this.serverStopped = true;
            }
            catch (Throwable var8) {
                MinecraftServer.logger.error("Exception stopping the server", var8);
                this.systemExitNow();
            }
            finally {
                this.systemExitNow();
            }
        }
    }
    
    private void addFaviconToStatusResponse(final ServerStatusResponse response) {
        final File var2 = this.getFile("server-icon.png");
        if (var2.isFile()) {
            final ByteBuf var3 = Unpooled.buffer();
            try {
                final BufferedImage var4 = ImageIO.read(var2);
                Validate.validState(var4.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                Validate.validState(var4.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                ImageIO.write(var4, "PNG", (OutputStream)new ByteBufOutputStream(var3));
                final ByteBuf var5 = Base64.encode(var3);
                response.setFavicon("data:image/png;base64," + var5.toString(Charsets.UTF_8));
            }
            catch (Exception var6) {
                MinecraftServer.logger.error("Couldn't load server icon", (Throwable)var6);
            }
            finally {
                var3.release();
            }
        }
    }
    
    public File getDataDirectory() {
        return new File(".");
    }
    
    protected void finalTick(final CrashReport report) {
    }
    
    protected void systemExitNow() {
    }
    
    public void tick() {
        final long var1 = System.nanoTime();
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
            final GameProfile[] var2 = new GameProfile[Math.min(this.getCurrentPlayerCount(), 12)];
            final int var3 = MathHelper.getRandomIntegerInRange(this.random, 0, this.getCurrentPlayerCount() - var2.length);
            for (int var4 = 0; var4 < var2.length; ++var4) {
                var2[var4] = this.serverConfigManager.playerEntityList.get(var3 + var4).getGameProfile();
            }
            Collections.shuffle(Arrays.asList(var2));
            this.statusResponse.getPlayerCountData().setPlayers(var2);
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
    
    public void updateTimeLightAndEntities() {
        this.theProfiler.startSection("jobs");
        final Queue var1 = this.futureTaskQueue;
        synchronized (this.futureTaskQueue) {
            while (!this.futureTaskQueue.isEmpty()) {
                try {
                    this.futureTaskQueue.poll().run();
                }
                catch (Throwable var2) {
                    MinecraftServer.logger.fatal((Object)var2);
                }
            }
        }
        this.theProfiler.endStartSection("levels");
        for (int var3 = 0; var3 < this.worldServers.length; ++var3) {
            final long var4 = System.nanoTime();
            if (var3 == 0 || this.getAllowNether()) {
                final WorldServer var5 = this.worldServers[var3];
                this.theProfiler.startSection(var5.getWorldInfo().getWorldName());
                if (this.tickCounter % 20 == 0) {
                    this.theProfiler.startSection("timeSync");
                    this.serverConfigManager.sendPacketToAllPlayersInDimension(new S03PacketTimeUpdate(var5.getTotalWorldTime(), var5.getWorldTime(), var5.getGameRules().getGameRuleBooleanValue("doDaylightCycle")), var5.provider.getDimensionId());
                    this.theProfiler.endSection();
                }
                this.theProfiler.startSection("tick");
                try {
                    var5.tick();
                }
                catch (Throwable var7) {
                    final CrashReport var6 = CrashReport.makeCrashReport(var7, "Exception ticking world");
                    var5.addWorldInfoToCrashReport(var6);
                    throw new ReportedException(var6);
                }
                try {
                    var5.updateEntities();
                }
                catch (Throwable var8) {
                    final CrashReport var6 = CrashReport.makeCrashReport(var8, "Exception ticking world entities");
                    var5.addWorldInfoToCrashReport(var6);
                    throw new ReportedException(var6);
                }
                this.theProfiler.endSection();
                this.theProfiler.startSection("tracker");
                var5.getEntityTracker().updateTrackedEntities();
                this.theProfiler.endSection();
                this.theProfiler.endSection();
            }
            this.timeOfLastDimensionTick[var3][this.tickCounter % 100] = System.nanoTime() - var4;
        }
        this.theProfiler.endStartSection("connection");
        this.getNetworkSystem().networkTick();
        this.theProfiler.endStartSection("players");
        this.serverConfigManager.onTick();
        this.theProfiler.endStartSection("tickables");
        for (int var3 = 0; var3 < this.playersOnline.size(); ++var3) {
            this.playersOnline.get(var3).update();
        }
        this.theProfiler.endSection();
    }
    
    public boolean getAllowNether() {
        return true;
    }
    
    public void startServerThread() {
        (this.serverThread = new Thread(this, "Server thread")).start();
    }
    
    public File getFile(final String fileName) {
        return new File(this.getDataDirectory(), fileName);
    }
    
    public void logWarning(final String msg) {
        MinecraftServer.logger.warn(msg);
    }
    
    public WorldServer worldServerForDimension(final int dimension) {
        return (dimension == -1) ? this.worldServers[1] : ((dimension == 1) ? this.worldServers[2] : this.worldServers[0]);
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
    
    public CrashReport addServerInfoToCrashReport(final CrashReport report) {
        report.getCategory().addCrashSectionCallable("Profiler Position", new Callable() {
            private static final String __OBFID = "CL_00001418";
            
            public String func_179879_a() {
                return MinecraftServer.this.theProfiler.profilingEnabled ? MinecraftServer.this.theProfiler.getNameOfLastSection() : "N/A (disabled)";
            }
            
            @Override
            public Object call() {
                return this.func_179879_a();
            }
        });
        if (this.serverConfigManager != null) {
            report.getCategory().addCrashSectionCallable("Player Count", new Callable() {
                private static final String __OBFID = "CL_00001419";
                
                @Override
                public String call() {
                    return MinecraftServer.this.serverConfigManager.getCurrentPlayerCount() + " / " + MinecraftServer.this.serverConfigManager.getMaxPlayers() + "; " + MinecraftServer.this.serverConfigManager.playerEntityList;
                }
            });
        }
        return report;
    }
    
    public List func_180506_a(final ICommandSender p_180506_1_, String p_180506_2_, final BlockPos p_180506_3_) {
        final ArrayList var4 = Lists.newArrayList();
        if (p_180506_2_.startsWith("/")) {
            p_180506_2_ = p_180506_2_.substring(1);
            final boolean var5 = !p_180506_2_.contains(" ");
            final List var6 = this.commandManager.getTabCompletionOptions(p_180506_1_, p_180506_2_, p_180506_3_);
            if (var6 != null) {
                for (final String var8 : var6) {
                    if (var5) {
                        var4.add("/" + var8);
                    }
                    else {
                        var4.add(var8);
                    }
                }
            }
            return var4;
        }
        final String[] var9 = p_180506_2_.split(" ", -1);
        final String var10 = var9[var9.length - 1];
        for (final String var14 : this.serverConfigManager.getAllUsernames()) {
            if (CommandBase.doesStringStartWith(var10, var14)) {
                var4.add(var14);
            }
        }
        return var4;
    }
    
    public static MinecraftServer getServer() {
        return MinecraftServer.mcServer;
    }
    
    public boolean func_175578_N() {
        return this.anvilFile != null;
    }
    
    @Override
    public String getName() {
        return "Server";
    }
    
    @Override
    public void addChatMessage(final IChatComponent message) {
        MinecraftServer.logger.info(message.getUnformattedText());
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final int permissionLevel, final String command) {
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
    
    public void setServerOwner(final String owner) {
        this.serverOwner = owner;
    }
    
    public boolean isSinglePlayer() {
        return this.serverOwner != null;
    }
    
    public String getFolderName() {
        return this.folderName;
    }
    
    public void setFolderName(final String name) {
        this.folderName = name;
    }
    
    public void setWorldName(final String p_71246_1_) {
        this.worldName = p_71246_1_;
    }
    
    public String getWorldName() {
        return this.worldName;
    }
    
    public void setKeyPair(final KeyPair keyPair) {
        this.serverKeyPair = keyPair;
    }
    
    public void setDifficultyForAllWorlds(final EnumDifficulty difficulty) {
        for (final WorldServer var3 : this.worldServers) {
            if (var3 != null) {
                if (var3.getWorldInfo().isHardcoreModeEnabled()) {
                    var3.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
                    var3.setAllowedSpawnTypes(true, true);
                }
                else if (this.isSinglePlayer()) {
                    var3.getWorldInfo().setDifficulty(difficulty);
                    var3.setAllowedSpawnTypes(var3.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                }
                else {
                    var3.getWorldInfo().setDifficulty(difficulty);
                    var3.setAllowedSpawnTypes(this.allowSpawnMonsters(), this.canSpawnAnimals);
                }
            }
        }
    }
    
    protected boolean allowSpawnMonsters() {
        return true;
    }
    
    public boolean isDemo() {
        return this.isDemo;
    }
    
    public void setDemo(final boolean demo) {
        this.isDemo = demo;
    }
    
    public void canCreateBonusChest(final boolean enable) {
        this.enableBonusChest = enable;
    }
    
    public ISaveFormat getActiveAnvilConverter() {
        return this.anvilConverterForAnvilFile;
    }
    
    public void deleteWorldAndStopServer() {
        this.worldIsBeingDeleted = true;
        this.getActiveAnvilConverter().flushCache();
        for (final WorldServer var2 : this.worldServers) {
            if (var2 != null) {
                var2.flush();
            }
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
    
    public void setResourcePack(final String url, final String hash) {
        this.resourcePackUrl = url;
        this.resourcePackHash = hash;
    }
    
    @Override
    public void addServerStatsToSnooper(final PlayerUsageSnooper playerSnooper) {
        playerSnooper.addClientStat("whitelist_enabled", false);
        playerSnooper.addClientStat("whitelist_count", 0);
        if (this.serverConfigManager != null) {
            playerSnooper.addClientStat("players_current", this.getCurrentPlayerCount());
            playerSnooper.addClientStat("players_max", this.getMaxPlayers());
            playerSnooper.addClientStat("players_seen", this.serverConfigManager.getAvailablePlayerDat().length);
        }
        playerSnooper.addClientStat("uses_auth", this.onlineMode);
        playerSnooper.addClientStat("gui_state", this.getGuiEnabled() ? "enabled" : "disabled");
        playerSnooper.addClientStat("run_time", (getCurrentTimeMillis() - playerSnooper.getMinecraftStartTimeMillis()) / 60L * 1000L);
        playerSnooper.addClientStat("avg_tick_ms", (int)(MathHelper.average(this.tickTimeArray) * 1.0E-6));
        int var2 = 0;
        if (this.worldServers != null) {
            for (final WorldServer var3 : this.worldServers) {
                if (var3 != null) {
                    final WorldInfo var4 = var3.getWorldInfo();
                    playerSnooper.addClientStat("world[" + var2 + "][dimension]", var3.provider.getDimensionId());
                    playerSnooper.addClientStat("world[" + var2 + "][mode]", var4.getGameType());
                    playerSnooper.addClientStat("world[" + var2 + "][difficulty]", var3.getDifficulty());
                    playerSnooper.addClientStat("world[" + var2 + "][hardcore]", var4.isHardcoreModeEnabled());
                    playerSnooper.addClientStat("world[" + var2 + "][generator_name]", var4.getTerrainType().getWorldTypeName());
                    playerSnooper.addClientStat("world[" + var2 + "][generator_version]", var4.getTerrainType().getGeneratorVersion());
                    playerSnooper.addClientStat("world[" + var2 + "][height]", this.buildLimit);
                    playerSnooper.addClientStat("world[" + var2 + "][chunks_loaded]", var3.getChunkProvider().getLoadedChunkCount());
                    ++var2;
                }
            }
        }
        playerSnooper.addClientStat("worlds", var2);
    }
    
    @Override
    public void addServerTypeToSnooper(final PlayerUsageSnooper playerSnooper) {
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
    
    public void setOnlineMode(final boolean online) {
        this.onlineMode = online;
    }
    
    public boolean getCanSpawnAnimals() {
        return this.canSpawnAnimals;
    }
    
    public void setCanSpawnAnimals(final boolean spawnAnimals) {
        this.canSpawnAnimals = spawnAnimals;
    }
    
    public boolean getCanSpawnNPCs() {
        return this.canSpawnNPCs;
    }
    
    public void setCanSpawnNPCs(final boolean spawnNpcs) {
        this.canSpawnNPCs = spawnNpcs;
    }
    
    public boolean isPVPEnabled() {
        return this.pvpEnabled;
    }
    
    public void setAllowPvp(final boolean allowPvp) {
        this.pvpEnabled = allowPvp;
    }
    
    public boolean isFlightAllowed() {
        return this.allowFlight;
    }
    
    public void setAllowFlight(final boolean allow) {
        this.allowFlight = allow;
    }
    
    public abstract boolean isCommandBlockEnabled();
    
    public String getMOTD() {
        return this.motd;
    }
    
    public void setMOTD(final String motdIn) {
        this.motd = motdIn;
    }
    
    public int getBuildLimit() {
        return this.buildLimit;
    }
    
    public void setBuildLimit(final int maxBuildHeight) {
        this.buildLimit = maxBuildHeight;
    }
    
    public ServerConfigurationManager getConfigurationManager() {
        return this.serverConfigManager;
    }
    
    public void setConfigManager(final ServerConfigurationManager configManager) {
        this.serverConfigManager = configManager;
    }
    
    public void setGameType(final WorldSettings.GameType gameMode) {
        for (int var2 = 0; var2 < this.worldServers.length; ++var2) {
            getServer().worldServers[var2].getWorldInfo().setGameType(gameMode);
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
    
    public abstract String shareToLAN(final WorldSettings.GameType p0, final boolean p1);
    
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
    
    public boolean isBlockProtected(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
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
    
    public void setPlayerIdleTimeout(final int idleTimeout) {
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
    
    public Entity getEntityFromUuid(final UUID uuid) {
        for (final WorldServer var5 : this.worldServers) {
            if (var5 != null) {
                final Entity var6 = var5.getEntityFromUuid(uuid);
                if (var6 != null) {
                    return var6;
                }
            }
        }
        return null;
    }
    
    @Override
    public boolean sendCommandFeedback() {
        return getServer().worldServers[0].getGameRules().getGameRuleBooleanValue("sendCommandFeedback");
    }
    
    @Override
    public void func_174794_a(final CommandResultStats.Type p_174794_1_, final int p_174794_2_) {
    }
    
    public int getMaxWorldSize() {
        return 29999984;
    }
    
    public ListenableFuture callFromMainThread(final Callable callable) {
        Validate.notNull((Object)callable);
        if (!this.isCallingFromMinecraftThread()) {
            final ListenableFutureTask var2 = ListenableFutureTask.create(callable);
            final Queue var3 = this.futureTaskQueue;
            synchronized (this.futureTaskQueue) {
                this.futureTaskQueue.add(var2);
                return (ListenableFuture)var2;
            }
        }
        try {
            return Futures.immediateFuture(callable.call());
        }
        catch (Exception var4) {
            return (ListenableFuture)Futures.immediateFailedCheckedFuture(var4);
        }
    }
    
    @Override
    public ListenableFuture addScheduledTask(final Runnable runnableToSchedule) {
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
    
    static {
        logger = LogManager.getLogger();
        USER_CACHE_FILE = new File("usercache.json");
    }
}
