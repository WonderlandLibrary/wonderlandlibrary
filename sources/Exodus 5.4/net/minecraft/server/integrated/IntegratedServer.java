/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.util.concurrent.Futures
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.server.integrated;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import java.io.File;
import java.io.IOException;
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
    private ThreadLanServerPing lanServerPing;
    private boolean isPublic;
    private boolean isGamePaused;
    private final WorldSettings theWorldSettings;
    private final Minecraft mc;
    private static final Logger logger = LogManager.getLogger();

    @Override
    public File getDataDirectory() {
        return this.mc.mcDataDir;
    }

    @Override
    public void initiateShutdown() {
        Futures.getUnchecked(this.addScheduledTask(new Runnable(){

            @Override
            public void run() {
                for (EntityPlayerMP entityPlayerMP : Lists.newArrayList(IntegratedServer.this.getConfigurationManager().func_181057_v())) {
                    IntegratedServer.this.getConfigurationManager().playerLoggedOut(entityPlayerMP);
                }
            }
        }));
        super.initiateShutdown();
        if (this.lanServerPing != null) {
            this.lanServerPing.interrupt();
            this.lanServerPing = null;
        }
    }

    @Override
    public EnumDifficulty getDifficulty() {
        return Minecraft.theWorld.getWorldInfo().getDifficulty();
    }

    @Override
    public int getOpPermissionLevel() {
        return 4;
    }

    @Override
    public boolean isDedicatedServer() {
        return false;
    }

    public IntegratedServer(Minecraft minecraft) {
        super(minecraft.getProxy(), new File(minecraft.mcDataDir, USER_CACHE_FILE.getName()));
        this.mc = minecraft;
        this.theWorldSettings = null;
    }

    public IntegratedServer(Minecraft minecraft, String string, String string2, WorldSettings worldSettings) {
        super(new File(minecraft.mcDataDir, "saves"), minecraft.getProxy(), new File(minecraft.mcDataDir, USER_CACHE_FILE.getName()));
        this.setServerOwner(minecraft.getSession().getUsername());
        this.setFolderName(string);
        this.setWorldName(string2);
        this.setDemo(minecraft.isDemo());
        this.canCreateBonusChest(worldSettings.isBonusChestEnabled());
        this.setBuildLimit(256);
        this.setConfigManager(new IntegratedPlayerList(this));
        this.mc = minecraft;
        this.theWorldSettings = this.isDemo() ? DemoWorldServer.demoWorldSettings : worldSettings;
    }

    @Override
    public void tick() {
        boolean bl = this.isGamePaused;
        boolean bl2 = this.isGamePaused = Minecraft.getMinecraft().getNetHandler() != null && Minecraft.getMinecraft().isGamePaused();
        if (!bl && this.isGamePaused) {
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
            }
        } else {
            super.tick();
            if (Minecraft.gameSettings.renderDistanceChunks != this.getConfigurationManager().getViewDistance()) {
                logger.info("Changing view distance to {}, from {}", new Object[]{Minecraft.gameSettings.renderDistanceChunks, this.getConfigurationManager().getViewDistance()});
                this.getConfigurationManager().setViewDistance(Minecraft.gameSettings.renderDistanceChunks);
            }
            if (Minecraft.theWorld != null) {
                WorldInfo worldInfo = this.worldServers[0].getWorldInfo();
                WorldInfo worldInfo2 = Minecraft.theWorld.getWorldInfo();
                if (!worldInfo.isDifficultyLocked() && worldInfo2.getDifficulty() != worldInfo.getDifficulty()) {
                    logger.info("Changing difficulty to {}, from {}", new Object[]{worldInfo2.getDifficulty(), worldInfo.getDifficulty()});
                    this.setDifficultyForAllWorlds(worldInfo2.getDifficulty());
                } else if (worldInfo2.isDifficultyLocked() && !worldInfo.isDifficultyLocked()) {
                    logger.info("Locking difficulty to {}", new Object[]{worldInfo2.getDifficulty()});
                    WorldServer[] worldServerArray = this.worldServers;
                    int n = this.worldServers.length;
                    int n2 = 0;
                    while (n2 < n) {
                        WorldServer worldServer = worldServerArray[n2];
                        if (worldServer != null) {
                            worldServer.getWorldInfo().setDifficultyLocked(true);
                        }
                        ++n2;
                    }
                }
            }
        }
    }

    @Override
    protected void finalTick(CrashReport crashReport) {
        this.mc.crashed(crashReport);
    }

    @Override
    protected void loadAllWorlds(String string, String string2, long l, WorldType worldType, String string3) {
        this.convertMapIfNeeded(string);
        this.worldServers = new WorldServer[3];
        this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
        ISaveHandler iSaveHandler = this.getActiveAnvilConverter().getSaveLoader(string, true);
        this.setResourcePackFromWorld(this.getFolderName(), iSaveHandler);
        WorldInfo worldInfo = iSaveHandler.loadWorldInfo();
        if (worldInfo == null) {
            worldInfo = new WorldInfo(this.theWorldSettings, string2);
        } else {
            worldInfo.setWorldName(string2);
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
                this.worldServers[n].initialize(this.theWorldSettings);
            } else {
                this.worldServers[n] = (WorldServer)new WorldServerMulti((MinecraftServer)this, iSaveHandler, n2, this.worldServers[0], this.theProfiler).init();
            }
            this.worldServers[n].addWorldAccess(new WorldManager(this, this.worldServers[n]));
            ++n;
        }
        this.getConfigurationManager().setPlayerManager(this.worldServers);
        if (this.worldServers[0].getWorldInfo().getDifficulty() == null) {
            this.setDifficultyForAllWorlds(Minecraft.gameSettings.difficulty);
        }
        this.initialWorldChunkLoad();
    }

    @Override
    public String shareToLAN(WorldSettings.GameType gameType, boolean bl) {
        try {
            int n = -1;
            try {
                n = HttpUtil.getSuitableLanPort();
            }
            catch (IOException iOException) {
                // empty catch block
            }
            if (n <= 0) {
                n = 25564;
            }
            this.getNetworkSystem().addLanEndpoint(null, n);
            logger.info("Started on " + n);
            this.isPublic = true;
            this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), String.valueOf(n));
            this.lanServerPing.start();
            this.getConfigurationManager().setGameType(gameType);
            this.getConfigurationManager().setCommandsAllowedForAll(bl);
            return String.valueOf(n);
        }
        catch (IOException iOException) {
            return null;
        }
    }

    @Override
    public boolean isCommandBlockEnabled() {
        return true;
    }

    @Override
    public boolean isHardcore() {
        return this.theWorldSettings.getHardcoreEnabled();
    }

    @Override
    public CrashReport addServerInfoToCrashReport(CrashReport crashReport) {
        crashReport = super.addServerInfoToCrashReport(crashReport);
        crashReport.getCategory().addCrashSectionCallable("Type", new Callable<String>(){

            @Override
            public String call() throws Exception {
                return "Integrated Server (map_client.txt)";
            }
        });
        crashReport.getCategory().addCrashSectionCallable("Is Modded", new Callable<String>(){

            @Override
            public String call() throws Exception {
                String string = ClientBrandRetriever.getClientModName();
                if (!string.equals("vanilla")) {
                    return "Definitely; Client brand changed to '" + string + "'";
                }
                string = IntegratedServer.this.getServerModName();
                return !string.equals("vanilla") ? "Definitely; Server brand changed to '" + string + "'" : (Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and both client + server brands are untouched.");
            }
        });
        return crashReport;
    }

    @Override
    protected ServerCommandManager createNewCommandManager() {
        return new IntegratedServerCommandManager();
    }

    @Override
    protected boolean startServer() throws IOException {
        logger.info("Starting integrated minecraft server version 1.8.8");
        this.setOnlineMode(true);
        this.setCanSpawnAnimals(true);
        this.setCanSpawnNPCs(true);
        this.setAllowPvp(true);
        this.setAllowFlight(true);
        logger.info("Generating keypair");
        this.setKeyPair(CryptManager.generateKeyPair());
        this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.theWorldSettings.getSeed(), this.theWorldSettings.getTerrainType(), this.theWorldSettings.getWorldName());
        this.setMOTD(String.valueOf(this.getServerOwner()) + " - " + this.worldServers[0].getWorldInfo().getWorldName());
        return true;
    }

    @Override
    public void setDifficultyForAllWorlds(EnumDifficulty enumDifficulty) {
        super.setDifficultyForAllWorlds(enumDifficulty);
        if (Minecraft.theWorld != null) {
            Minecraft.theWorld.getWorldInfo().setDifficulty(enumDifficulty);
        }
    }

    @Override
    public boolean func_181035_ah() {
        return false;
    }

    @Override
    public boolean func_181034_q() {
        return true;
    }

    public void setStaticInstance() {
        this.setInstance();
    }

    @Override
    public boolean canStructuresSpawn() {
        return false;
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
    public void addServerStatsToSnooper(PlayerUsageSnooper playerUsageSnooper) {
        super.addServerStatsToSnooper(playerUsageSnooper);
        playerUsageSnooper.addClientStat("snooper_partner", this.mc.getPlayerUsageSnooper().getUniqueID());
    }

    public boolean getPublic() {
        return this.isPublic;
    }

    @Override
    public WorldSettings.GameType getGameType() {
        return this.theWorldSettings.getGameType();
    }

    @Override
    public boolean isSnooperEnabled() {
        return Minecraft.getMinecraft().isSnooperEnabled();
    }

    @Override
    public boolean func_183002_r() {
        return true;
    }

    @Override
    public void setGameType(WorldSettings.GameType gameType) {
        this.getConfigurationManager().setGameType(gameType);
    }
}

