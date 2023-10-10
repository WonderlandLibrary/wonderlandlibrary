/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.server.integrated;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ThreadLanServerPing;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedPlayerList;
import net.minecraft.server.integrated.IntegratedServerCommandManager;
import net.minecraft.util.CryptManager;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.Util;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
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

    public IntegratedServer(Minecraft mcIn) {
        super(mcIn.getProxy(), new File(mcIn.mcDataDir, USER_CACHE_FILE.getName()));
        this.mc = mcIn;
        this.theWorldSettings = null;
    }

    public IntegratedServer(Minecraft mcIn, String folderName, String worldName, WorldSettings settings) {
        super(new File(mcIn.mcDataDir, "saves"), mcIn.getProxy(), new File(mcIn.mcDataDir, USER_CACHE_FILE.getName()));
        this.setServerOwner(mcIn.getSession().getUsername());
        this.setFolderName(folderName);
        this.setWorldName(worldName);
        this.setDemo(mcIn.isDemo());
        this.canCreateBonusChest(settings.isBonusChestEnabled());
        this.setBuildLimit(256);
        this.setConfigManager(new IntegratedPlayerList(this));
        this.mc = mcIn;
        this.theWorldSettings = this.isDemo() ? DemoWorldServer.demoWorldSettings : settings;
    }

    @Override
    protected ServerCommandManager createNewCommandManager() {
        return new IntegratedServerCommandManager();
    }

    @Override
    protected void loadAllWorlds(String p_71247_1_, String p_71247_2_, long seed, WorldType type, String p_71247_6_) {
        this.convertMapIfNeeded(p_71247_1_);
        this.worldServers = new WorldServer[3];
        this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
        ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(p_71247_1_, true);
        this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (worldinfo == null) {
            worldinfo = new WorldInfo(this.theWorldSettings, p_71247_2_);
        } else {
            worldinfo.setWorldName(p_71247_2_);
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
                this.worldServers[i] = this.isDemo() ? (WorldServer)new DemoWorldServer(this, isavehandler, worldinfo, j, this.theProfiler).init() : (WorldServer)new WorldServer(this, isavehandler, worldinfo, j, this.theProfiler).init();
                this.worldServers[i].initialize(this.theWorldSettings);
            } else {
                this.worldServers[i] = (WorldServer)new WorldServerMulti((MinecraftServer)this, isavehandler, j, this.worldServers[0], this.theProfiler).init();
            }
            this.worldServers[i].addWorldAccess(new WorldManager(this, this.worldServers[i]));
        }
        this.getConfigurationManager().setPlayerManager(this.worldServers);
        if (this.worldServers[0].getWorldInfo().getDifficulty() == null) {
            this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
        }
        this.initialWorldChunkLoad();
    }

    @Override
    protected boolean startServer() throws IOException {
        logger.info("Starting integrated minecraft server version 1.8.9");
        this.setOnlineMode(true);
        this.setCanSpawnAnimals(true);
        this.setCanSpawnNPCs(true);
        this.setAllowPvp(true);
        this.setAllowFlight(true);
        logger.info("Generating keypair");
        this.setKeyPair(CryptManager.generateKeyPair());
        this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.theWorldSettings.getSeed(), this.theWorldSettings.getTerrainType(), this.theWorldSettings.getWorldName());
        this.setMOTD(this.getServerOwner() + " - " + this.worldServers[0].getWorldInfo().getWorldName());
        return true;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void tick() {
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
                    Util.func_181617_a((FutureTask)this.futureTaskQueue.poll(), logger);
                }
                return;
            }
        }
        super.tick();
        if (this.mc.gameSettings.renderDistanceChunks != this.getConfigurationManager().getViewDistance()) {
            logger.info("Changing view distance to {}, from {}", new Object[]{this.mc.gameSettings.renderDistanceChunks, this.getConfigurationManager().getViewDistance()});
            this.getConfigurationManager().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
        }
        if (this.mc.theWorld == null) return;
        WorldInfo worldinfo1 = this.worldServers[0].getWorldInfo();
        WorldInfo worldinfo = this.mc.theWorld.getWorldInfo();
        if (!worldinfo1.isDifficultyLocked() && worldinfo.getDifficulty() != worldinfo1.getDifficulty()) {
            logger.info("Changing difficulty to {}, from {}", new Object[]{worldinfo.getDifficulty(), worldinfo1.getDifficulty()});
            this.setDifficultyForAllWorlds(worldinfo.getDifficulty());
            return;
        }
        if (!worldinfo.isDifficultyLocked()) return;
        if (worldinfo1.isDifficultyLocked()) return;
        logger.info("Locking difficulty to {}", new Object[]{worldinfo.getDifficulty()});
        WorldServer[] worldServerArray = this.worldServers;
        int n = worldServerArray.length;
        int n2 = 0;
        while (n2 < n) {
            WorldServer worldserver = worldServerArray[n2];
            if (worldserver != null) {
                worldserver.getWorldInfo().setDifficultyLocked(true);
            }
            ++n2;
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
        return this.mc.theWorld.getWorldInfo().getDifficulty();
    }

    @Override
    public boolean isHardcore() {
        return this.theWorldSettings.getHardcoreEnabled();
    }

    @Override
    public boolean func_181034_q() {
        return true;
    }

    @Override
    public boolean func_183002_r() {
        return true;
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
    public boolean func_181035_ah() {
        return false;
    }

    @Override
    protected void finalTick(CrashReport report) {
        this.mc.crashed(report);
    }

    @Override
    public CrashReport addServerInfoToCrashReport(CrashReport report) {
        report = super.addServerInfoToCrashReport(report);
        report.getCategory().addCrashSectionCallable("Type", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return "Integrated Server (map_client.txt)";
            }
        });
        report.getCategory().addCrashSectionCallable("Is Modded", new Callable<String>(){

            @Override
            public String call() throws Exception {
                String s = ClientBrandRetriever.getClientModName();
                if (!s.equals("vanilla")) {
                    return "Definitely; Client brand changed to '" + s + "'";
                }
                s = IntegratedServer.this.getServerModName();
                if (!s.equals("vanilla")) {
                    String string = "Definitely; Server brand changed to '" + s + "'";
                    return string;
                }
                if (Minecraft.class.getSigners() != null) return "Probably not. Jar signature remains and both client + server brands are untouched.";
                return "Very likely; Jar signature invalidated";
            }
        });
        return report;
    }

    @Override
    public void setDifficultyForAllWorlds(EnumDifficulty difficulty) {
        super.setDifficultyForAllWorlds(difficulty);
        if (this.mc.theWorld == null) return;
        this.mc.theWorld.getWorldInfo().setDifficulty(difficulty);
    }

    @Override
    public void addServerStatsToSnooper(PlayerUsageSnooper playerSnooper) {
        super.addServerStatsToSnooper(playerSnooper);
        playerSnooper.addClientStat("snooper_partner", this.mc.getPlayerUsageSnooper().getUniqueID());
    }

    @Override
    public boolean isSnooperEnabled() {
        return Minecraft.getMinecraft().isSnooperEnabled();
    }

    @Override
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
            this.getNetworkSystem().addLanEndpoint(null, i);
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

    @Override
    public void stopServer() {
        super.stopServer();
        if (this.lanServerPing == null) return;
        this.lanServerPing.interrupt();
        this.lanServerPing = null;
    }

    @Override
    public void initiateShutdown() {
        Futures.getUnchecked(this.addScheduledTask(new Runnable(){

            @Override
            public void run() {
                Iterator<EntityPlayerMP> iterator = Lists.newArrayList(IntegratedServer.this.getConfigurationManager().func_181057_v()).iterator();
                while (iterator.hasNext()) {
                    EntityPlayerMP entityplayermp = iterator.next();
                    IntegratedServer.this.getConfigurationManager().playerLoggedOut(entityplayermp);
                }
            }
        }));
        super.initiateShutdown();
        if (this.lanServerPing == null) return;
        this.lanServerPing.interrupt();
        this.lanServerPing = null;
    }

    public void setStaticInstance() {
        this.setInstance();
    }

    public boolean getPublic() {
        return this.isPublic;
    }

    @Override
    public void setGameType(WorldSettings.GameType gameMode) {
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
}

