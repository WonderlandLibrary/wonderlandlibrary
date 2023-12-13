/*
 * Decompiled with CFR 0.152.
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
import optfine.Reflector;
import optfine.WorldServerOF;
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
    private static final String __OBFID = "CL_00001129";

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
        Reflector.callVoid(Reflector.ModLoader_registerServer, this);
    }

    @Override
    protected ServerCommandManager createNewCommandManager() {
        return new IntegratedServerCommandManager();
    }

    @Override
    protected void loadAllWorlds(String p_71247_1_, String p_71247_2_, long seed, WorldType type, String p_71247_6_) {
        this.convertMapIfNeeded(p_71247_1_);
        ISaveHandler isavehandler = this.getActiveAnvilConverter().getSaveLoader(p_71247_1_, true);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        if (Reflector.DimensionManager.exists()) {
            Integer[] ainteger;
            WorldServer worldserver = this.isDemo() ? (WorldServer)new DemoWorldServer(this, isavehandler, worldinfo, 0, this.theProfiler).init() : (WorldServer)new WorldServerOF(this, isavehandler, worldinfo, 0, this.theProfiler).init();
            worldserver.initialize(this.theWorldSettings);
            Integer[] ainteger1 = ainteger = (Integer[])Reflector.call(Reflector.DimensionManager_getStaticDimensionIDs, new Object[0]);
            int i2 = ainteger.length;
            for (int j2 = 0; j2 < i2; ++j2) {
                int k2 = ainteger1[j2];
                WorldServer worldserver1 = k2 == 0 ? worldserver : (WorldServer)new WorldServerMulti((MinecraftServer)this, isavehandler, k2, worldserver, this.theProfiler).init();
                worldserver1.addWorldAccess(new WorldManager(this, worldserver1));
                if (!this.isSinglePlayer()) {
                    worldserver1.getWorldInfo().setGameType(this.getGameType());
                }
                if (!Reflector.EventBus.exists()) continue;
                Reflector.postForgeBusEvent(Reflector.WorldEvent_Load_Constructor, worldserver1);
            }
            this.getConfigurationManager().setPlayerManager(new WorldServer[]{worldserver});
            if (worldserver.getWorldInfo().getDifficulty() == null) {
                this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
            }
        } else {
            this.worldServers = new WorldServer[3];
            this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
            this.setResourcePackFromWorld(this.getFolderName(), isavehandler);
            if (worldinfo == null) {
                worldinfo = new WorldInfo(this.theWorldSettings, p_71247_2_);
            } else {
                worldinfo.setWorldName(p_71247_2_);
            }
            for (int l2 = 0; l2 < this.worldServers.length; ++l2) {
                int b0 = 0;
                if (l2 == 1) {
                    b0 = -1;
                }
                if (l2 == 2) {
                    b0 = 1;
                }
                if (l2 == 0) {
                    this.worldServers[l2] = this.isDemo() ? (WorldServer)new DemoWorldServer(this, isavehandler, worldinfo, b0, this.theProfiler).init() : (WorldServer)new WorldServerOF(this, isavehandler, worldinfo, b0, this.theProfiler).init();
                    this.worldServers[l2].initialize(this.theWorldSettings);
                } else {
                    this.worldServers[l2] = (WorldServer)new WorldServerMulti((MinecraftServer)this, isavehandler, b0, this.worldServers[0], this.theProfiler).init();
                }
                this.worldServers[l2].addWorldAccess(new WorldManager(this, this.worldServers[l2]));
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
        logger.info("Starting integrated minecraft server version 1.8.8");
        this.setOnlineMode(true);
        this.setCanSpawnAnimals(true);
        this.setCanSpawnNPCs(true);
        this.setAllowPvp(true);
        this.setAllowFlight(true);
        logger.info("Generating keypair");
        this.setKeyPair(CryptManager.generateKeyPair());
        if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists()) {
            Object object = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            if (!Reflector.callBoolean(object, Reflector.FMLCommonHandler_handleServerAboutToStart, this)) {
                return false;
            }
        }
        this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.theWorldSettings.getSeed(), this.theWorldSettings.getTerrainType(), this.theWorldSettings.getWorldName());
        this.setMOTD(this.getServerOwner() + " - " + this.worldServers[0].getWorldInfo().getWorldName());
        if (Reflector.FMLCommonHandler_handleServerStarting.exists()) {
            Object object1 = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == Boolean.TYPE) {
                return Reflector.callBoolean(object1, Reflector.FMLCommonHandler_handleServerStarting, this);
            }
            Reflector.callVoid(object1, Reflector.FMLCommonHandler_handleServerStarting, this);
        }
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
            Queue var3 = this.futureTaskQueue;
            Queue queue = this.futureTaskQueue;
            synchronized (queue) {
                while (!this.futureTaskQueue.isEmpty()) {
                    Util.func_181617_a((FutureTask)this.futureTaskQueue.poll(), logger);
                }
            }
        } else {
            super.tick();
            if (this.mc.gameSettings.renderDistanceChunks != this.getConfigurationManager().getViewDistance()) {
                logger.info("Changing view distance to {}, from {}", this.mc.gameSettings.renderDistanceChunks, this.getConfigurationManager().getViewDistance());
                this.getConfigurationManager().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
            }
            if (this.mc.theWorld != null) {
                WorldInfo worldinfo = this.worldServers[0].getWorldInfo();
                WorldInfo worldinfo1 = this.mc.theWorld.getWorldInfo();
                if (!worldinfo.isDifficultyLocked() && worldinfo1.getDifficulty() != worldinfo.getDifficulty()) {
                    logger.info("Changing difficulty to {}, from {}", new Object[]{worldinfo1.getDifficulty(), worldinfo.getDifficulty()});
                    this.setDifficultyForAllWorlds(worldinfo1.getDifficulty());
                } else if (worldinfo1.isDifficultyLocked() && !worldinfo.isDifficultyLocked()) {
                    logger.info("Locking difficulty to {}", new Object[]{worldinfo1.getDifficulty()});
                    for (WorldServer worldserver : this.worldServers) {
                        if (worldserver == null) continue;
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
        return this.mc.theWorld == null ? this.mc.gameSettings.difficulty : this.mc.theWorld.getWorldInfo().getDifficulty();
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
    public boolean func_181035_ah() {
        return false;
    }

    @Override
    public boolean isDedicatedServer() {
        return false;
    }

    @Override
    protected void finalTick(CrashReport report) {
        this.mc.crashed(report);
    }

    @Override
    public CrashReport addServerInfoToCrashReport(CrashReport report) {
        report = super.addServerInfoToCrashReport(report);
        report.getCategory().addCrashSectionCallable("Type", new Callable(){
            private static final String __OBFID = "CL_00001130";

            public String call() throws Exception {
                return "Integrated Server (map_client.txt)";
            }
        });
        report.getCategory().addCrashSectionCallable("Is Modded", new Callable(){
            private static final String __OBFID = "CL_00001131";

            public String call() throws Exception {
                String s2 = ClientBrandRetriever.getClientModName();
                if (!s2.equals("vanilla")) {
                    return "Definitely; Client brand changed to '" + s2 + "'";
                }
                s2 = IntegratedServer.this.getServerModName();
                return !s2.equals("vanilla") ? "Definitely; Server brand changed to '" + s2 + "'" : (Minecraft.class.getSigners() == null ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and both client + server brands are untouched.");
            }
        });
        return report;
    }

    @Override
    public void setDifficultyForAllWorlds(EnumDifficulty difficulty) {
        super.setDifficultyForAllWorlds(difficulty);
        if (this.mc.theWorld != null) {
            this.mc.theWorld.getWorldInfo().setDifficulty(difficulty);
        }
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
            int i2 = -1;
            try {
                i2 = HttpUtil.getSuitableLanPort();
            }
            catch (IOException iOException) {
                // empty catch block
            }
            if (i2 <= 0) {
                i2 = 25564;
            }
            this.getNetworkSystem().addLanEndpoint(null, i2);
            logger.info("Started on " + i2);
            this.isPublic = true;
            this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), i2 + "");
            this.lanServerPing.start();
            this.getConfigurationManager().setGameType(type);
            this.getConfigurationManager().setCommandsAllowedForAll(allowCheats);
            return i2 + "";
        }
        catch (IOException var6) {
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
        Futures.getUnchecked(this.addScheduledTask(new Runnable(){
            private static final String __OBFID = "CL_00002380";

            @Override
            public void run() {
                for (EntityPlayerMP entityplayermp : Lists.newArrayList(IntegratedServer.this.getConfigurationManager().func_181057_v())) {
                    IntegratedServer.this.getConfigurationManager().playerLoggedOut(entityplayermp);
                }
            }
        }));
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

