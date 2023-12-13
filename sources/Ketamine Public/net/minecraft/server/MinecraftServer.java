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
import net.minecraft.command.*;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkSystem;
import net.minecraft.network.ServerStatusResponse;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.storage.AnvilSaveConverter;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Proxy;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public abstract class MinecraftServer implements Runnable, ICommandSender, IThreadListener {
    private static final Logger logger = LogManager.getLogger();
    /**
     * Instance of Minecraft Server.
     */
    private static MinecraftServer mcServer;
    private final ISaveFormat anvilConverterForAnvilFile;
    /**
     * The PlayerUsageSnooper instance.
     */
    private final File anvilFile;
    private final List<ITickable> playersOnline = Lists.newArrayList();
    protected final ICommandManager commandManager;
    private final NetworkSystem networkSystem;
    private final ServerStatusResponse statusResponse = new ServerStatusResponse();
    private final Random random = new Random();
    /**
     * The server's port.
     */
    private final int serverPort = -1;
    /**
     * The server world instances.
     */
    public WorldServer[] worldServers;
    /**
     * The ServerConfigurationManager instance.
     */
    private ServerConfigurationManager serverConfigManager;
    /**
     * Indicates whether the server is running or not. Set to false to initiate a shutdown.
     */
    private boolean serverRunning = true;
    /**
     * Indicates to other classes that the server is safely stopped.
     */
    private boolean serverStopped;
    /**
     * Incremented every tick.
     */
    private int tickCounter;
    protected final Proxy serverProxy;
    /**
     * The task the server is currently working on(and will output on outputPercentRemaining).
     */
    public String currentTask;
    /**
     * The percentage of the current task finished so far.
     */
    public int percentDone;
    /**
     * True if the server is in online mode.
     */
    private boolean onlineMode;
    /**
     * True if the server has animals turned on.
     */
    private boolean canSpawnAnimals;
    private boolean canSpawnNPCs;
    /**
     * Indicates whether PvP is active on the server or not.
     */
    private boolean pvpEnabled;
    /**
     * Determines if flight is allowed or not.
     */
    private boolean allowFlight;
    /**
     * The server MOTD string.
     */
    private String motd;
    /**
     * Maximum build height.
     */
    private int buildLimit;
    private int maxPlayerIdleMinutes = 0;
    /**
     * Stats are [dimension][tick%100] system.nanoTime is stored.
     */
    public long[][] timeOfLastDimensionTick;
    private KeyPair serverKeyPair;
    /**
     * Username of the server owner (for integrated servers)
     */
    private String serverOwner;
    private String folderName;
    private String worldName;
    private boolean enableBonusChest;
    /**
     * If true, there is no need to save chunks or stop the server, because that is already being done.
     */
    private boolean worldIsBeingDeleted;
    /**
     * The texture pack for the server
     */
    private String resourcePackUrl = "";
    private String resourcePackHash = "";
    private boolean serverIsRunning;
    /**
     * Set when warned for "Can't keep up", which triggers again after 15 seconds.
     */
    private long timeOfLastWarning;
    private String userMessage;
    private boolean isGamemodeForced;
    private final YggdrasilAuthenticationService authService;
    private final MinecraftSessionService sessionService;
    private long nanoTimeSinceStatusRefresh = 0L;
    private final GameProfileRepository profileRepo;
    private final PlayerProfileCache profileCache;
    protected final Queue<FutureTask<?>> futureTaskQueue = Queues.newArrayDeque();
    private Thread serverThread;
    private long currentTime = getCurrentTimeMillis();
    public MinecraftServer(Proxy proxy, File workDir) {
        this.serverProxy = proxy;
        mcServer = this;
        this.anvilFile = null;
        this.networkSystem = null;
        this.profileCache = new PlayerProfileCache(this, workDir);
        this.commandManager = null;
        this.anvilConverterForAnvilFile = null;
        this.authService = new YggdrasilAuthenticationService(proxy, UUID.randomUUID().toString());
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

    public void run() {
        try {
            if (this.startServer()) {
                this.currentTime = getCurrentTimeMillis();
                long i = 0L;
                this.statusResponse.setServerDescription(new ChatComponentText(this.motd));
                this.statusResponse.setProtocolVersionInfo(new ServerStatusResponse.MinecraftProtocolVersionIdentifier("1.8.8", 47));
                this.addFaviconToStatusResponse(this.statusResponse);

                while (this.serverRunning) {
                    long k = getCurrentTimeMillis();
                    long j = k - this.currentTime;

                    if (j > 2000L && this.currentTime - this.timeOfLastWarning >= 15000L) {
                        logger.warn("Can't keep up! Did the system time change, or is the server overloaded? Running {}ms behind, skipping {} tick(s)", Long.valueOf(j), Long.valueOf(j / 50L));
                        j = 2000L;
                        this.timeOfLastWarning = this.currentTime;
                    }

                    if (j < 0L) {
                        logger.warn("Time ran backwards! Did the system time change?");
                        j = 0L;
                    }

                    i += j;
                    this.currentTime = k;

                    if (this.worldServers[0].areAllPlayersAsleep()) {
                        this.tick();
                        i = 0L;
                    } else {
                        while (i > 50L) {
                            i -= 50L;
                            this.tick();
                        }
                    }

                    Thread.sleep(Math.max(1L, 50L - i));
                    this.serverIsRunning = true;
                }
            } else {
                this.finalTick(null);
            }
        } catch (Throwable throwable1) {
            logger.error("Encountered an unexpected exception", throwable1);
            CrashReport crashreport = null;

            if (throwable1 instanceof ReportedException) {
                crashreport = this.addServerInfoToCrashReport(((ReportedException) throwable1).getCrashReport());
            } else {
                crashreport = this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", throwable1));
            }

            File file1 = new File(new File(this.getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (crashreport.saveToFile(file1)) {
                logger.error("This crash report has been saved to: " + file1.getAbsolutePath());
            } else {
                logger.error("We were unable to save this crash report to disk.");
            }

            this.finalTick(crashreport);
        } finally {
            try {
                this.serverStopped = true;
                this.stopServer();
            } catch (Throwable throwable) {
                logger.error("Exception stopping the server", throwable);
            } finally {
                this.systemExitNow();
            }
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getName() {
        return "Server";
    }

    /**
     * Send a chat message to the CommandSender
     */
    public void addChatMessage(IChatComponent component) {
        logger.info(component.getUnformattedText());
    }

    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     */
    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return true;
    }

    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    public BlockPos getPosition() {
        return BlockPos.ORIGIN;
    }

    /**
     * Get the position vector. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return 0.0D,
     * 0.0D, 0.0D
     */
    public Vec3 getPositionVector() {
        return new Vec3(0.0D, 0.0D, 0.0D);
    }

    /**
     * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the overworld
     */
    public World getEntityWorld() {
        return this.worldServers[0];
    }

    /**
     * Returns the entity associated with the command sender. MAY BE NULL!
     */
    public Entity getCommandSenderEntity() {
        return null;
    }

    /**
     * Get the formatted ChatComponent that will be used for the sender's username in chat
     */
    public IChatComponent getDisplayName() {
        return new ChatComponentText(this.getName());
    }

    /**
     * Returns true if the command sender should be sent feedback about executed commands
     */
    public boolean sendCommandFeedback() {
        return getServer().worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
    }

    public void setCommandStat(CommandResultStats.Type type, int amount) {
    }

    public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule) {
        Validate.notNull(runnableToSchedule);
        return this.callFromMainThread(Executors.callable(runnableToSchedule));
    }

    public boolean isCallingFromMinecraftThread() {
        return Thread.currentThread() == this.serverThread;
    }
    public static final File USER_CACHE_FILE = new File("usercache.json");
    public final long[] tickTimeArray = new long[100];

    /**
     * Gets mcServer.
     */
    public static MinecraftServer getServer() {
        return mcServer;
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    protected ServerCommandManager createNewCommandManager() {
        return new ServerCommandManager();
    }

    /**
     * Initialises the server and starts it.
     */
    protected abstract boolean startServer() throws IOException;

    protected void convertMapIfNeeded(String worldNameIn) {
        if (this.getActiveAnvilConverter().isOldMapFormat(worldNameIn)) {
            logger.info("Converting map!");
            this.setUserMessage("menu.convertingLevel");
            this.getActiveAnvilConverter().convertMapFormat(worldNameIn, new IProgressUpdate() {
                private long startTime = System.currentTimeMillis();

                public void displaySavingString(String message) {
                }

                public void resetProgressAndMessage(String message) {
                }

                public void setLoadingProgress(int progress) {
                    if (System.currentTimeMillis() - this.startTime >= 1000L) {
                        this.startTime = System.currentTimeMillis();
                        MinecraftServer.logger.info("Converting... " + progress + "%");
                    }
                }

                public void setDoneWorking() {
                }

                public void displayLoadingString(String message) {
                }
            });
        }
    }

    public synchronized String getUserMessage() {
        return this.userMessage;
    }

    /**
     * Typically "menu.convertingLevel", "menu.loadingLevel" or others.
     */
    protected synchronized void setUserMessage(String message) {
        this.userMessage = message;
    }

    protected void loadAllWorlds(String p_71247_1_, String p_71247_2_, long seed, WorldType type, String p_71247_6_) {
        this.convertMapIfNeeded(p_71247_1_);
        this.setUserMessage("menu.loadingLevel");
        this.worldServers = new WorldServer[3];
        this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
        ISaveHandler isavehandler = this.anvilConverterForAnvilFile.getSaveLoader(p_71247_1_, true);
        this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        WorldSettings worldsettings;

        if (worldinfo == null) {
            worldsettings = new WorldSettings(seed, this.getGameType(), this.canStructuresSpawn(), this.isHardcore(), type);
            worldsettings.setWorldName(p_71247_6_);

            if (this.enableBonusChest) {
                worldsettings.enableBonusChest();
            }

            worldinfo = new WorldInfo(worldsettings, p_71247_2_);
        } else {
            worldinfo.setWorldName(p_71247_2_);
            worldsettings = new WorldSettings(worldinfo);
        }

        for (int i = 0; i < this.worldServers.length; ++i) {
            int j = 0;

            if (i == 1) {
                j = -1;
            }

            if (i == 2) {
                j = 1;
            }

            if (i == 0) {
                this.worldServers[i] = (WorldServer) (new WorldServer(this, isavehandler, worldinfo, j)).init();

                this.worldServers[i].initialize(worldsettings);
            } else {
                this.worldServers[i] = (WorldServer) (new WorldServerMulti(this, isavehandler, j, this.worldServers[0])).init();
            }

            this.worldServers[i].addWorldAccess(new WorldManager(this, this.worldServers[i]));

            if (!this.isSinglePlayer()) {
                this.worldServers[i].getWorldInfo().setGameType(this.getGameType());
            }
        }

        this.serverConfigManager.setPlayerManager(this.worldServers);
        this.setDifficultyForAllWorlds(this.getDifficulty());
        this.initialWorldChunkLoad();
    }

    protected void initialWorldChunkLoad() {
        int i = 16;
        int j = 4;
        int k = 192;
        int l = 625;
        int i1 = 0;
        this.setUserMessage("menu.generatingTerrain");
        int j1 = 0;
        logger.info("Preparing start region for level " + j1);
        WorldServer worldserver = this.worldServers[j1];
        BlockPos blockpos = worldserver.getSpawnPoint();
        long k1 = getCurrentTimeMillis();

        for (int l1 = -192; l1 <= 192 && this.isServerRunning(); l1 += 16) {
            for (int i2 = -192; i2 <= 192 && this.isServerRunning(); i2 += 16) {
                long j2 = getCurrentTimeMillis();

                if (j2 - k1 > 1000L) {
                    this.outputPercentRemaining("Preparing spawn area", i1 * 100 / 625);
                    k1 = j2;
                }

                ++i1;
                worldserver.theChunkProviderServer.loadChunk(blockpos.getX() + l1 >> 4, blockpos.getZ() + i2 >> 4);
            }
        }

        this.clearCurrentTask();
    }

    protected void setResourcePackFromWorld(String worldNameIn, ISaveHandler saveHandlerIn) {
        File file1 = new File(saveHandlerIn.getWorldDirectory(), "resources.zip");

        if (file1.isFile()) {
            this.setResourcePack("level://" + worldNameIn + "/" + file1.getName(), "");
        }
    }

    public abstract boolean canStructuresSpawn();

    public abstract WorldSettings.GameType getGameType();

    /**
     * Sets the game type for all worlds.
     */
    public void setGameType(WorldSettings.GameType gameMode) {
        for (int i = 0; i < this.worldServers.length; ++i) {
            getServer().worldServers[i].getWorldInfo().setGameType(gameMode);
        }
    }

    /**
     * Get the server's difficulty
     */
    public abstract EnumDifficulty getDifficulty();

    /**
     * Defaults to false.
     */
    public abstract boolean isHardcore();

    public abstract int getOpPermissionLevel();

    public abstract boolean func_181034_q();

    public abstract boolean func_183002_r();

    /**
     * Used to display a percent remaining given text and the percentage.
     */
    protected void outputPercentRemaining(String message, int percent) {
        this.currentTask = message;
        this.percentDone = percent;
        logger.info(message + ": " + percent + "%");
    }

    /**
     * Set current task to null and set its percentage to 0.
     */
    protected void clearCurrentTask() {
        this.currentTask = null;
        this.percentDone = 0;
    }

    /**
     * par1 indicates if a log message should be output.
     */
    protected void saveAllWorlds(boolean dontLog) {
        if (!this.worldIsBeingDeleted) {
            for (WorldServer worldserver : this.worldServers) {
                if (worldserver != null) {
                    if (!dontLog) {
                        logger.info("Saving chunks for level '" + worldserver.getWorldInfo().getWorldName() + "'/" + worldserver.provider.getDimensionName());
                    }

                    try {
                        worldserver.saveAllChunks(true, null);
                    } catch (MinecraftException minecraftexception) {
                        logger.warn(minecraftexception.getMessage());
                    }
                }
            }
        }
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
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

                for (int i = 0; i < this.worldServers.length; ++i) {
                    WorldServer worldserver = this.worldServers[i];
                    worldserver.flush();
                }
            }
        }
    }

    public boolean isServerRunning() {
        return this.serverRunning;
    }

    /**
     * Sets the serverRunning variable to false, in order to get the server to shut down.
     */
    public void initiateShutdown() {
        this.serverRunning = false;
    }

    protected void setInstance() {
        mcServer = this;
    }

    private void addFaviconToStatusResponse(ServerStatusResponse response) {
        File file1 = this.getFile("server-icon.png");

        if (file1.isFile()) {
            ByteBuf bytebuf = Unpooled.buffer();

            try {
                BufferedImage bufferedimage = ImageIO.read(file1);
                Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide");
                Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high");
                ImageIO.write(bufferedimage, "PNG", new ByteBufOutputStream(bytebuf));
                ByteBuf bytebuf1 = Base64.encode(bytebuf);
                response.setFavicon("data:image/png;base64," + bytebuf1.toString(Charsets.UTF_8));
            } catch (Exception exception) {
                logger.error("Couldn't load server icon", exception);
            } finally {
                bytebuf.release();
            }
        }
    }

    public File getDataDirectory() {
        return new File(".");
    }

    /**
     * Called on exit from the main run() loop.
     */
    protected void finalTick(CrashReport report) {
    }

    /**
     * Directly calls System.exit(0), instantly killing the program.
     */
    protected void systemExitNow() {
    }

    /**
     * Main function called by run() every loop.
     */
    public void tick() {
        long i = System.nanoTime();
        ++this.tickCounter;

        this.updateTimeLightAndEntities();

        if (i - this.nanoTimeSinceStatusRefresh >= 5000000000L) {
            this.nanoTimeSinceStatusRefresh = i;
            this.statusResponse.setPlayerCountData(new ServerStatusResponse.PlayerCountData(this.getMaxPlayers(), this.getCurrentPlayerCount()));
            GameProfile[] agameprofile = new GameProfile[Math.min(this.getCurrentPlayerCount(), 12)];
            int j = MathHelper.getRandomIntegerInRange(this.random, 0, this.getCurrentPlayerCount() - agameprofile.length);

            for (int k = 0; k < agameprofile.length; ++k) {
                agameprofile[k] = this.serverConfigManager.func_181057_v().get(j + k).getGameProfile();
            }

            Collections.shuffle(Arrays.asList(agameprofile));
            this.statusResponse.getPlayerCountData().setPlayers(agameprofile);
        }

        if (this.tickCounter % 900 == 0) {
            this.serverConfigManager.saveAllPlayerData();
            this.saveAllWorlds(true);
        }

        this.tickTimeArray[this.tickCounter % 100] = System.nanoTime() - i;
    }

    public void updateTimeLightAndEntities() {
        synchronized (this.futureTaskQueue) {
            while (!this.futureTaskQueue.isEmpty()) {
                Util.func_181617_a((FutureTask) this.futureTaskQueue.poll(), logger);
            }
        }

        for (int j = 0; j < this.worldServers.length; ++j) {
            long i = System.nanoTime();

            if (j == 0 || this.getAllowNether()) {
                WorldServer worldserver = this.worldServers[j];
                if (this.tickCounter % 20 == 0) {
                    this.serverConfigManager.sendPacketToAllPlayersInDimension(new S03PacketTimeUpdate(worldserver.getTotalWorldTime(), worldserver.getWorldTime(), worldserver.getGameRules().getBoolean("doDaylightCycle")), worldserver.provider.getDimensionId());
                }

                try {
                    worldserver.tick();
                } catch (Throwable throwable1) {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Exception ticking world");
                    worldserver.addWorldInfoToCrashReport(crashreport);
                    throw new ReportedException(crashreport);
                }

                try {
                    worldserver.updateEntities();
                } catch (Throwable throwable) {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Exception ticking world entities");
                    worldserver.addWorldInfoToCrashReport(crashreport1);
                    throw new ReportedException(crashreport1);
                }

                worldserver.getEntityTracker().updateTrackedEntities();
            }

            this.timeOfLastDimensionTick[j][this.tickCounter % 100] = System.nanoTime() - i;
        }

        this.getNetworkSystem().networkTick();
        this.serverConfigManager.onTick();

        for (int k = 0; k < this.playersOnline.size(); ++k) {
            this.playersOnline.get(k).update();
        }
    }

    public boolean getAllowNether() {
        return true;
    }

    public void startServerThread() {
        this.serverThread = new Thread(this, "Server thread");
        this.serverThread.start();
    }

    /**
     * Returns a File object from the specified string.
     */
    public File getFile(String fileName) {
        return new File(this.getDataDirectory(), fileName);
    }

    /**
     * Logs the message with a level of WARN.
     */
    public void logWarning(String msg) {
        logger.warn(msg);
    }

    /**
     * Gets the worldServer by the given dimension.
     */
    public WorldServer worldServerForDimension(int dimension) {
        return dimension == -1 ? this.worldServers[1] : (dimension == 1 ? this.worldServers[2] : this.worldServers[0]);
    }

    /**
     * Returns the server's Minecraft version as string.
     */
    public String getMinecraftVersion() {
        return "1.8.8";
    }

    /**
     * Returns the number of players currently on the server.
     */
    public int getCurrentPlayerCount() {
        return this.serverConfigManager.getCurrentPlayerCount();
    }

    /**
     * Returns the maximum number of players allowed on the server.
     */
    public int getMaxPlayers() {
        return this.serverConfigManager.getMaxPlayers();
    }

    /**
     * Returns an array of the usernames of all the connected players.
     */
    public String[] getAllUsernames() {
        return this.serverConfigManager.getAllUsernames();
    }

    /**
     * Returns an array of the GameProfiles of all the connected players
     */
    public GameProfile[] getGameProfiles() {
        return this.serverConfigManager.getAllProfiles();
    }

    public String getServerModName() {
        return "vanilla";
    }

    /**
     * Adds the server info, including from theWorldServer, to the crash report.
     */
    public CrashReport addServerInfoToCrashReport(CrashReport report) {
        if (this.serverConfigManager != null) {
            report.getCategory().addCrashSectionCallable("Player Count", new Callable<String>() {
                public String call() {
                    return MinecraftServer.this.serverConfigManager.getCurrentPlayerCount() + " / " + MinecraftServer.this.serverConfigManager.getMaxPlayers() + "; " + MinecraftServer.this.serverConfigManager.func_181057_v();
                }
            });
        }

        return report;
    }

    public List<String> getTabCompletions(ICommandSender sender, String input, BlockPos pos) {
        List<String> list = Lists.newArrayList();

        if (input.startsWith("/")) {
            input = input.substring(1);
            boolean flag = !input.contains(" ");
            List<String> list1 = this.commandManager.getTabCompletionOptions(sender, input, pos);

            if (list1 != null) {
                for (String s2 : list1) {
                    if (flag) {
                        list.add("/" + s2);
                    } else {
                        list.add(s2);
                    }
                }
            }

            return list;
        } else {
            String[] astring = input.split(" ", -1);
            String s = astring[astring.length - 1];

            for (String s1 : this.serverConfigManager.getAllUsernames()) {
                if (CommandBase.doesStringStartWith(s, s1)) {
                    list.add(s1);
                }
            }

            return list;
        }
    }

    public boolean isAnvilFileSet() {
        return this.anvilFile != null;
    }

    public ICommandManager getCommandManager() {
        return this.commandManager;
    }

    /**
     * Gets KeyPair instanced in MinecraftServer.
     */
    public KeyPair getKeyPair() {
        return this.serverKeyPair;
    }

    public void setKeyPair(KeyPair keyPair) {
        this.serverKeyPair = keyPair;
    }

    /**
     * Returns the username of the server owner (for integrated servers)
     */
    public String getServerOwner() {
        return this.serverOwner;
    }

    /**
     * Sets the username of the owner of this server (in the case of an integrated server)
     */
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

    public String getWorldName() {
        return this.worldName;
    }

    public void setWorldName(String p_71246_1_) {
        this.worldName = p_71246_1_;
    }

    public void setDifficultyForAllWorlds(EnumDifficulty difficulty) {
        for (int i = 0; i < this.worldServers.length; ++i) {
            World world = this.worldServers[i];

            if (world != null) {
                if (world.getWorldInfo().isHardcoreModeEnabled()) {
                    world.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
                    world.setAllowedSpawnTypes(true, true);
                } else if (this.isSinglePlayer()) {
                    world.getWorldInfo().setDifficulty(difficulty);
                    world.setAllowedSpawnTypes(world.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                } else {
                    world.getWorldInfo().setDifficulty(difficulty);
                    world.setAllowedSpawnTypes(this.allowSpawnMonsters(), this.canSpawnAnimals);
                }
            }
        }
    }

    protected boolean allowSpawnMonsters() {
        return true;
    }

    public void canCreateBonusChest(boolean enable) {
        this.enableBonusChest = enable;
    }

    public ISaveFormat getActiveAnvilConverter() {
        return this.anvilConverterForAnvilFile;
    }

    /**
     * WARNING : directly calls
     * getActiveAnvilConverter().deleteWorldDirectory(theWorldServer[0].getSaveHandler().getWorldDirectoryName());
     */
    public void deleteWorldAndStopServer() {
        this.worldIsBeingDeleted = true;
        this.getActiveAnvilConverter().flushCache();

        for (int i = 0; i < this.worldServers.length; ++i) {
            WorldServer worldserver = this.worldServers[i];

            if (worldserver != null) {
                worldserver.flush();
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

    public void setResourcePack(String url, String hash) {
        this.resourcePackUrl = url;
        this.resourcePackHash = hash;
    }

    /**
     * Returns whether snooping is enabled or not.
     */
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

    public abstract boolean func_181035_ah();

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

    /**
     * Return whether command blocks are enabled.
     */
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

    public boolean isServerStopped() {
        return this.serverStopped;
    }

    public ServerConfigurationManager getConfigurationManager() {
        return this.serverConfigManager;
    }

    public void setConfigManager(ServerConfigurationManager configManager) {
        this.serverConfigManager = configManager;
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

    /**
     * On dedicated does nothing. On integrated, sets commandsAllowedForAll, gameType and allows external connections.
     */
    public abstract String shareToLAN(WorldSettings.GameType type, boolean allowCheats);

    public int getTickCounter() {
        return this.tickCounter;
    }

    /**
     * Return the spawn protection area's size.
     */
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

    public int getMaxPlayerIdleMinutes() {
        return this.maxPlayerIdleMinutes;
    }

    public void setPlayerIdleTimeout(int idleTimeout) {
        this.maxPlayerIdleMinutes = idleTimeout;
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
        for (WorldServer worldserver : this.worldServers) {
            if (worldserver != null) {
                Entity entity = worldserver.getEntityFromUuid(uuid);

                if (entity != null) {
                    return entity;
                }
            }
        }

        return null;
    }

    public int getMaxWorldSize() {
        return 29999984;
    }

    public <V> ListenableFuture<V> callFromMainThread(Callable<V> callable) {
        Validate.notNull(callable);

        if (!this.isCallingFromMinecraftThread() && !this.isServerStopped()) {
            ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.create(callable);

            synchronized (this.futureTaskQueue) {
                this.futureTaskQueue.add(listenablefuturetask);
                return listenablefuturetask;
            }
        } else {
            try {
                return Futures.immediateFuture(callable.call());
            } catch (Exception exception) {
                return Futures.immediateFailedCheckedFuture(exception);
            }
        }
    }

    /**
     * The compression treshold. If the packet is larger than the specified amount of bytes, it will be compressed
     */
    public int getNetworkCompressionTreshold() {
        return 256;
    }
}
