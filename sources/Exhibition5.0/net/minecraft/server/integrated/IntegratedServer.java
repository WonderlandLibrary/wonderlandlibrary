// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.integrated;

import org.apache.logging.log4j.LogManager;
import java.util.concurrent.Future;
import com.google.common.util.concurrent.Futures;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayerMP;
import com.google.common.collect.Lists;
import java.net.InetAddress;
import net.minecraft.util.HttpUtil;
import net.minecraft.profiler.PlayerUsageSnooper;
import net.minecraft.client.ClientBrandRetriever;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.world.EnumDifficulty;
import java.util.Queue;
import java.util.concurrent.FutureTask;
import java.io.IOException;
import net.minecraft.util.CryptManager;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.WorldManager;
import net.minecraft.world.WorldServerMulti;
import net.minecraft.optifine.WorldServerOF;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldType;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.optifine.Reflector;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.server.management.ServerConfigurationManager;
import java.io.File;
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
    private static final String __OBFID = "CL_00001129";
    
    public IntegratedServer(final Minecraft mcIn) {
        super(mcIn.getProxy(), new File(mcIn.mcDataDir, IntegratedServer.USER_CACHE_FILE.getName()));
        this.mc = mcIn;
        this.theWorldSettings = null;
    }
    
    public IntegratedServer(final Minecraft mcIn, final String folderName, final String worldName, final WorldSettings settings) {
        super(new File(mcIn.mcDataDir, "saves"), mcIn.getProxy(), new File(mcIn.mcDataDir, IntegratedServer.USER_CACHE_FILE.getName()));
        this.setServerOwner(mcIn.getSession().getUsername());
        this.setFolderName(folderName);
        this.setWorldName(worldName);
        this.setDemo(mcIn.isDemo());
        this.canCreateBonusChest(settings.isBonusChestEnabled());
        this.setBuildLimit(256);
        this.setConfigManager(new IntegratedPlayerList(this));
        this.mc = mcIn;
        this.theWorldSettings = (this.isDemo() ? DemoWorldServer.demoWorldSettings : settings);
        Reflector.callVoid(Reflector.ModLoader_registerServer, this);
    }
    
    @Override
    protected ServerCommandManager createNewCommandManager() {
        return new IntegratedServerCommandManager();
    }
    
    @Override
    protected void loadAllWorlds(final String p_71247_1_, final String p_71247_2_, final long seed, final WorldType type, final String p_71247_6_) {
        this.convertMapIfNeeded(p_71247_1_);
        final ISaveHandler var7 = this.getActiveAnvilConverter().getSaveLoader(p_71247_1_, true);
        this.worldServers = new WorldServer[3];
        this.timeOfLastDimensionTick = new long[this.worldServers.length][100];
        this.setResourcePackFromWorld(this.getFolderName(), var7);
        WorldInfo var8 = var7.loadWorldInfo();
        if (var8 == null) {
            var8 = new WorldInfo(this.theWorldSettings, p_71247_2_);
        }
        else {
            var8.setWorldName(p_71247_2_);
        }
        for (int var9 = 0; var9 < this.worldServers.length; ++var9) {
            byte var10 = 0;
            if (var9 == 1) {
                var10 = -1;
            }
            if (var9 == 2) {
                var10 = 1;
            }
            if (var9 == 0) {
                if (this.isDemo()) {
                    this.worldServers[var9] = (WorldServer)new DemoWorldServer(this, var7, var8, var10, this.theProfiler).init();
                }
                else {
                    this.worldServers[var9] = (WorldServer)new WorldServerOF(this, var7, var8, var10, this.theProfiler).init();
                }
                this.worldServers[var9].initialize(this.theWorldSettings);
            }
            else {
                this.worldServers[var9] = (WorldServer)new WorldServerMulti(this, var7, var10, this.worldServers[0], this.theProfiler).init();
            }
            this.worldServers[var9].addWorldAccess(new WorldManager(this, this.worldServers[var9]));
        }
        this.getConfigurationManager().setPlayerManager(this.worldServers);
        if (this.worldServers[0].getWorldInfo().getDifficulty() == null) {
            this.setDifficultyForAllWorlds(this.mc.gameSettings.difficulty);
        }
        this.initialWorldChunkLoad();
    }
    
    @Override
    protected boolean startServer() throws IOException {
        IntegratedServer.logger.info("Starting integrated minecraft server version 1.8");
        this.setOnlineMode(true);
        this.setCanSpawnAnimals(true);
        this.setCanSpawnNPCs(true);
        this.setAllowPvp(true);
        this.setAllowFlight(true);
        IntegratedServer.logger.info("Generating keypair");
        this.setKeyPair(CryptManager.generateKeyPair());
        if (Reflector.FMLCommonHandler_handleServerAboutToStart.exists()) {
            final Object inst = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            if (!Reflector.callBoolean(inst, Reflector.FMLCommonHandler_handleServerAboutToStart, this)) {
                return false;
            }
        }
        this.loadAllWorlds(this.getFolderName(), this.getWorldName(), this.theWorldSettings.getSeed(), this.theWorldSettings.getTerrainType(), this.theWorldSettings.getWorldName());
        this.setMOTD(this.getServerOwner() + " - " + this.worldServers[0].getWorldInfo().getWorldName());
        if (Reflector.FMLCommonHandler_handleServerStarting.exists()) {
            final Object inst = Reflector.call(Reflector.FMLCommonHandler_instance, new Object[0]);
            if (Reflector.FMLCommonHandler_handleServerStarting.getReturnType() == Boolean.TYPE) {
                return Reflector.callBoolean(inst, Reflector.FMLCommonHandler_handleServerStarting, this);
            }
            Reflector.callVoid(inst, Reflector.FMLCommonHandler_handleServerStarting, this);
        }
        return true;
    }
    
    @Override
    public void tick() {
        final boolean var1 = this.isGamePaused;
        this.isGamePaused = (Minecraft.getMinecraft().getNetHandler() != null && Minecraft.getMinecraft().isGamePaused());
        if (!var1 && this.isGamePaused) {
            IntegratedServer.logger.info("Saving and pausing game...");
            this.getConfigurationManager().saveAllPlayerData();
            this.saveAllWorlds(false);
        }
        if (this.isGamePaused) {
            final Queue var2 = this.futureTaskQueue;
            final Queue var3 = this.futureTaskQueue;
            synchronized (this.futureTaskQueue) {
                while (!this.futureTaskQueue.isEmpty()) {
                    try {
                        this.futureTaskQueue.poll().run();
                    }
                    catch (Throwable var4) {
                        IntegratedServer.logger.fatal((Object)var4);
                    }
                }
            }
        }
        else {
            super.tick();
            if (this.mc.gameSettings.renderDistanceChunks != this.getConfigurationManager().getViewDistance()) {
                IntegratedServer.logger.info("Changing view distance to {}, from {}", new Object[] { this.mc.gameSettings.renderDistanceChunks, this.getConfigurationManager().getViewDistance() });
                this.getConfigurationManager().setViewDistance(this.mc.gameSettings.renderDistanceChunks);
            }
            if (this.mc.theWorld != null) {
                final WorldInfo var5 = this.worldServers[0].getWorldInfo();
                final WorldInfo var6 = this.mc.theWorld.getWorldInfo();
                if (!var5.isDifficultyLocked() && var6.getDifficulty() != var5.getDifficulty()) {
                    IntegratedServer.logger.info("Changing difficulty to {}, from {}", new Object[] { var6.getDifficulty(), var5.getDifficulty() });
                    this.setDifficultyForAllWorlds(var6.getDifficulty());
                }
                else if (var6.isDifficultyLocked() && !var5.isDifficultyLocked()) {
                    IntegratedServer.logger.info("Locking difficulty to {}", new Object[] { var6.getDifficulty() });
                    for (final WorldServer var10 : this.worldServers) {
                        if (var10 != null) {
                            var10.getWorldInfo().setDifficultyLocked(true);
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
    public File getDataDirectory() {
        return this.mc.mcDataDir;
    }
    
    @Override
    public boolean isDedicatedServer() {
        return false;
    }
    
    @Override
    protected void finalTick(final CrashReport report) {
        this.mc.crashed(report);
    }
    
    @Override
    public CrashReport addServerInfoToCrashReport(CrashReport report) {
        report = super.addServerInfoToCrashReport(report);
        report.getCategory().addCrashSectionCallable("Type", new Callable() {
            private static final String __OBFID = "CL_00001130";
            
            @Override
            public String call() {
                return "Integrated Server (map_client.txt)";
            }
        });
        report.getCategory().addCrashSectionCallable("Is Modded", new Callable() {
            private static final String __OBFID = "CL_00001131";
            
            @Override
            public String call() {
                String var1 = ClientBrandRetriever.getClientModName();
                if (!var1.equals("vanilla")) {
                    return "Definitely; Client brand changed to '" + var1 + "'";
                }
                var1 = IntegratedServer.this.getServerModName();
                return var1.equals("vanilla") ? ((Minecraft.class.getSigners() == null) ? "Very likely; Jar signature invalidated" : "Probably not. Jar signature remains and both client + server brands are untouched.") : ("Definitely; Server brand changed to '" + var1 + "'");
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
            int var6 = -1;
            try {
                var6 = HttpUtil.getSuitableLanPort();
            }
            catch (IOException ex) {}
            if (var6 <= 0) {
                var6 = 25564;
            }
            this.getNetworkSystem().addLanEndpoint(null, var6);
            IntegratedServer.logger.info("Started on " + var6);
            this.isPublic = true;
            (this.lanServerPing = new ThreadLanServerPing(this.getMOTD(), var6 + "")).start();
            this.getConfigurationManager().func_152604_a(type);
            this.getConfigurationManager().setCommandsAllowedForAll(allowCheats);
            return var6 + "";
        }
        catch (IOException var7) {
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
        Futures.getUnchecked((Future)this.addScheduledTask(new Runnable() {
            private static final String __OBFID = "CL_00002380";
            
            @Override
            public void run() {
                final ArrayList var1 = Lists.newArrayList((Iterable)IntegratedServer.this.getConfigurationManager().playerEntityList);
                for (final EntityPlayerMP var3 : var1) {
                    IntegratedServer.this.getConfigurationManager().playerLoggedOut(var3);
                }
            }
        }));
        super.initiateShutdown();
        if (this.lanServerPing != null) {
            this.lanServerPing.interrupt();
            this.lanServerPing = null;
        }
    }
    
    public void func_175592_a() {
        this.func_175585_v();
    }
    
    public boolean getPublic() {
        return this.isPublic;
    }
    
    @Override
    public void setGameType(final WorldSettings.GameType gameMode) {
        this.getConfigurationManager().func_152604_a(gameMode);
    }
    
    @Override
    public boolean isCommandBlockEnabled() {
        return true;
    }
    
    @Override
    public int getOpPermissionLevel() {
        return 4;
    }
    
    static {
        logger = LogManager.getLogger();
    }
}
