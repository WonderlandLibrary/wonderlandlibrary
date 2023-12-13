// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world;

import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.util.WeightedRandomChestContent;
import org.apache.logging.log4j.LogManager;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.UUID;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.block.BlockEventData;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.util.Vec3;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.biome.WorldChunkManager;
import java.util.Random;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.entity.INpc;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityAnimal;
import java.util.ArrayList;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.block.material.Material;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Collection;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.storage.MapStorage;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import java.util.List;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.gen.ChunkProviderServer;
import java.util.Map;
import java.util.TreeSet;
import java.util.Set;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.entity.EntityTracker;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.IThreadListener;

public class WorldServer extends World implements IThreadListener
{
    private static final Logger logger;
    private final MinecraftServer mcServer;
    private final EntityTracker theEntityTracker;
    private final PlayerManager thePlayerManager;
    private final Set pendingTickListEntriesHashSet;
    private final TreeSet pendingTickListEntriesTreeSet;
    private final Map entitiesByUuid;
    public ChunkProviderServer theChunkProviderServer;
    public boolean disableLevelSaving;
    private boolean allPlayersSleeping;
    private int updateEntityTick;
    private final Teleporter worldTeleporter;
    private final SpawnerAnimals field_175742_R;
    protected final VillageSiege villageSiege;
    private ServerBlockEventList[] field_147490_S;
    private int blockEventCacheIndex;
    private static final List bonusChestContent;
    private List pendingTickListEntriesThisTick;
    private static final String __OBFID = "CL_00001437";
    
    public WorldServer(final MinecraftServer server, final ISaveHandler saveHandlerIn, final WorldInfo info, final int dimensionId, final Profiler profilerIn) {
        super(saveHandlerIn, info, WorldProvider.getProviderForDimension(dimensionId), profilerIn, false);
        this.pendingTickListEntriesHashSet = Sets.newHashSet();
        this.pendingTickListEntriesTreeSet = new TreeSet();
        this.entitiesByUuid = Maps.newHashMap();
        this.field_175742_R = new SpawnerAnimals();
        this.villageSiege = new VillageSiege(this);
        this.field_147490_S = new ServerBlockEventList[] { new ServerBlockEventList((Object)null), new ServerBlockEventList((Object)null) };
        this.pendingTickListEntriesThisTick = Lists.newArrayList();
        this.mcServer = server;
        this.theEntityTracker = new EntityTracker(this);
        this.thePlayerManager = new PlayerManager(this);
        this.provider.registerWorld(this);
        this.chunkProvider = this.createChunkProvider();
        this.worldTeleporter = new Teleporter(this);
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
        this.getWorldBorder().setSize(server.getMaxWorldSize());
    }
    
    @Override
    public World init() {
        this.mapStorage = new MapStorage(this.saveHandler);
        final String var1 = VillageCollection.func_176062_a(this.provider);
        final VillageCollection var2 = (VillageCollection)this.mapStorage.loadData(VillageCollection.class, var1);
        if (var2 == null) {
            this.villageCollectionObj = new VillageCollection(this);
            this.mapStorage.setData(var1, this.villageCollectionObj);
        }
        else {
            (this.villageCollectionObj = var2).func_82566_a(this);
        }
        this.worldScoreboard = new ServerScoreboard(this.mcServer);
        ScoreboardSaveData var3 = (ScoreboardSaveData)this.mapStorage.loadData(ScoreboardSaveData.class, "scoreboard");
        if (var3 == null) {
            var3 = new ScoreboardSaveData();
            this.mapStorage.setData("scoreboard", var3);
        }
        var3.func_96499_a(this.worldScoreboard);
        ((ServerScoreboard)this.worldScoreboard).func_96547_a(var3);
        this.getWorldBorder().setCenter(this.worldInfo.func_176120_C(), this.worldInfo.func_176126_D());
        this.getWorldBorder().func_177744_c(this.worldInfo.func_176140_I());
        this.getWorldBorder().setDamageBuffer(this.worldInfo.func_176138_H());
        this.getWorldBorder().setWarningDistance(this.worldInfo.func_176131_J());
        this.getWorldBorder().setWarningTime(this.worldInfo.func_176139_K());
        if (this.worldInfo.func_176134_F() > 0L) {
            this.getWorldBorder().setTransition(this.worldInfo.func_176137_E(), this.worldInfo.func_176132_G(), this.worldInfo.func_176134_F());
        }
        else {
            this.getWorldBorder().setTransition(this.worldInfo.func_176137_E());
        }
        return this;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.getWorldInfo().isHardcoreModeEnabled() && this.getDifficulty() != EnumDifficulty.HARD) {
            this.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
        }
        this.provider.getWorldChunkManager().cleanupCache();
        if (this.areAllPlayersAsleep()) {
            if (this.getGameRules().getGameRuleBooleanValue("doDaylightCycle")) {
                final long var1 = this.worldInfo.getWorldTime() + 24000L;
                this.worldInfo.setWorldTime(var1 - var1 % 24000L);
            }
            this.wakeAllPlayers();
        }
        this.theProfiler.startSection("mobSpawner");
        if (this.getGameRules().getGameRuleBooleanValue("doMobSpawning") && this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD) {
            this.field_175742_R.findChunksForSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs, this.worldInfo.getWorldTotalTime() % 400L == 0L);
        }
        this.theProfiler.endStartSection("chunkSource");
        this.chunkProvider.unloadQueuedChunks();
        final int var2 = this.calculateSkylightSubtracted(1.0f);
        if (var2 != this.getSkylightSubtracted()) {
            this.setSkylightSubtracted(var2);
        }
        this.worldInfo.incrementTotalWorldTime(this.worldInfo.getWorldTotalTime() + 1L);
        if (this.getGameRules().getGameRuleBooleanValue("doDaylightCycle")) {
            this.worldInfo.setWorldTime(this.worldInfo.getWorldTime() + 1L);
        }
        this.theProfiler.endStartSection("tickPending");
        this.tickUpdates(false);
        this.theProfiler.endStartSection("tickBlocks");
        this.func_147456_g();
        this.theProfiler.endStartSection("chunkMap");
        this.thePlayerManager.updatePlayerInstances();
        this.theProfiler.endStartSection("village");
        this.villageCollectionObj.tick();
        this.villageSiege.tick();
        this.theProfiler.endStartSection("portalForcer");
        this.worldTeleporter.removeStalePortalLocations(this.getTotalWorldTime());
        this.theProfiler.endSection();
        this.func_147488_Z();
    }
    
    public BiomeGenBase.SpawnListEntry func_175734_a(final EnumCreatureType p_175734_1_, final BlockPos p_175734_2_) {
        final List var3 = this.getChunkProvider().func_177458_a(p_175734_1_, p_175734_2_);
        return (var3 != null && !var3.isEmpty()) ? ((BiomeGenBase.SpawnListEntry)WeightedRandom.getRandomItem(this.rand, var3)) : null;
    }
    
    public boolean func_175732_a(final EnumCreatureType p_175732_1_, final BiomeGenBase.SpawnListEntry p_175732_2_, final BlockPos p_175732_3_) {
        final List var4 = this.getChunkProvider().func_177458_a(p_175732_1_, p_175732_3_);
        return var4 != null && !var4.isEmpty() && var4.contains(p_175732_2_);
    }
    
    @Override
    public void updateAllPlayersSleepingFlag() {
        this.allPlayersSleeping = false;
        if (!this.playerEntities.isEmpty()) {
            int var1 = 0;
            int var2 = 0;
            for (final EntityPlayer var4 : this.playerEntities) {
                if (var4.func_175149_v()) {
                    ++var1;
                }
                else {
                    if (!var4.isPlayerSleeping()) {
                        continue;
                    }
                    ++var2;
                }
            }
            this.allPlayersSleeping = (var2 > 0 && var2 >= this.playerEntities.size() - var1);
        }
    }
    
    protected void wakeAllPlayers() {
        this.allPlayersSleeping = false;
        for (final EntityPlayer var2 : this.playerEntities) {
            if (var2.isPlayerSleeping()) {
                var2.wakeUpPlayer(false, false, true);
            }
        }
        this.resetRainAndThunder();
    }
    
    private void resetRainAndThunder() {
        this.worldInfo.setRainTime(0);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThunderTime(0);
        this.worldInfo.setThundering(false);
    }
    
    public boolean areAllPlayersAsleep() {
        if (this.allPlayersSleeping && !this.isRemote) {
            for (final EntityPlayer var2 : this.playerEntities) {
                if (var2.func_175149_v() || !var2.isPlayerFullyAsleep()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void setInitialSpawnLocation() {
        if (this.worldInfo.getSpawnY() <= 0) {
            this.worldInfo.setSpawnY(64);
        }
        int var1 = this.worldInfo.getSpawnX();
        int var2 = this.worldInfo.getSpawnZ();
        int var3 = 0;
        while (this.getGroundAboveSeaLevel(new BlockPos(var1, 0, var2)).getMaterial() == Material.air) {
            var1 += this.rand.nextInt(8) - this.rand.nextInt(8);
            var2 += this.rand.nextInt(8) - this.rand.nextInt(8);
            if (++var3 == 10000) {
                break;
            }
        }
        this.worldInfo.setSpawnX(var1);
        this.worldInfo.setSpawnZ(var2);
    }
    
    @Override
    protected void func_147456_g() {
        super.func_147456_g();
        if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            for (final ChunkCoordIntPair var22 : this.activeChunkSet) {
                this.getChunkFromChunkCoords(var22.chunkXPos, var22.chunkZPos).func_150804_b(false);
            }
        }
        else {
            int var23 = 0;
            int var24 = 0;
            for (final ChunkCoordIntPair var26 : this.activeChunkSet) {
                final int var27 = var26.chunkXPos * 16;
                final int var28 = var26.chunkZPos * 16;
                this.theProfiler.startSection("getChunk");
                final Chunk var29 = this.getChunkFromChunkCoords(var26.chunkXPos, var26.chunkZPos);
                this.func_147467_a(var27, var28, var29);
                this.theProfiler.endStartSection("tickChunk");
                var29.func_150804_b(false);
                this.theProfiler.endStartSection("thunder");
                if (this.rand.nextInt(100000) == 0 && this.isRaining() && this.isThundering()) {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    final int var30 = this.updateLCG >> 2;
                    final BlockPos var31 = this.func_175736_a(new BlockPos(var27 + (var30 & 0xF), 0, var28 + (var30 >> 8 & 0xF)));
                    if (this.func_175727_C(var31)) {
                        this.addWeatherEffect(new EntityLightningBolt(this, var31.getX(), var31.getY(), var31.getZ()));
                    }
                }
                this.theProfiler.endStartSection("iceandsnow");
                if (this.rand.nextInt(16) == 0) {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    final int var30 = this.updateLCG >> 2;
                    final BlockPos var31 = this.func_175725_q(new BlockPos(var27 + (var30 & 0xF), 0, var28 + (var30 >> 8 & 0xF)));
                    final BlockPos var32 = var31.offsetDown();
                    if (this.func_175662_w(var32)) {
                        this.setBlockState(var32, Blocks.ice.getDefaultState());
                    }
                    if (this.isRaining() && this.func_175708_f(var31, true)) {
                        this.setBlockState(var31, Blocks.snow_layer.getDefaultState());
                    }
                    if (this.isRaining() && this.getBiomeGenForCoords(var32).canSpawnLightningBolt()) {
                        this.getBlockState(var32).getBlock().fillWithRain(this, var32);
                    }
                }
                this.theProfiler.endStartSection("tickBlocks");
                final int var30 = this.getGameRules().getInt("randomTickSpeed");
                if (var30 > 0) {
                    for (final ExtendedBlockStorage var36 : var29.getBlockStorageArray()) {
                        if (var36 != null && var36.getNeedsRandomTick()) {
                            for (int var37 = 0; var37 < var30; ++var37) {
                                this.updateLCG = this.updateLCG * 3 + 1013904223;
                                final int var38 = this.updateLCG >> 2;
                                final int var39 = var38 & 0xF;
                                final int var40 = var38 >> 8 & 0xF;
                                final int var41 = var38 >> 16 & 0xF;
                                ++var24;
                                final BlockPos var42 = new BlockPos(var39 + var27, var41 + var36.getYLocation(), var40 + var28);
                                final IBlockState var43 = var36.get(var39, var41, var40);
                                final Block var44 = var43.getBlock();
                                if (var44.getTickRandomly()) {
                                    ++var23;
                                    var44.randomTick(this, var42, var43, this.rand);
                                }
                            }
                        }
                    }
                }
                this.theProfiler.endSection();
            }
        }
    }
    
    protected BlockPos func_175736_a(final BlockPos p_175736_1_) {
        final BlockPos var2 = this.func_175725_q(p_175736_1_);
        final AxisAlignedBB var3 = new AxisAlignedBB(var2, new BlockPos(var2.getX(), this.getHeight(), var2.getZ())).expand(3.0, 3.0, 3.0);
        final List var4 = this.func_175647_a(EntityLivingBase.class, var3, (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00001889";
            
            public boolean func_180242_a(final EntityLivingBase p_180242_1_) {
                return p_180242_1_ != null && p_180242_1_.isEntityAlive() && WorldServer.this.isAgainstSky(p_180242_1_.getPosition());
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_180242_a((EntityLivingBase)p_apply_1_);
            }
        });
        return var4.isEmpty() ? var2 : var4.get(this.rand.nextInt(var4.size())).getPosition();
    }
    
    @Override
    public boolean isBlockTickPending(final BlockPos pos, final Block blockType) {
        final NextTickListEntry var3 = new NextTickListEntry(pos, blockType);
        return this.pendingTickListEntriesThisTick.contains(var3);
    }
    
    @Override
    public void scheduleUpdate(final BlockPos pos, final Block blockIn, final int delay) {
        this.func_175654_a(pos, blockIn, delay, 0);
    }
    
    @Override
    public void func_175654_a(final BlockPos p_175654_1_, final Block p_175654_2_, int p_175654_3_, final int p_175654_4_) {
        final NextTickListEntry var5 = new NextTickListEntry(p_175654_1_, p_175654_2_);
        byte var6 = 0;
        if (this.scheduledUpdatesAreImmediate && p_175654_2_.getMaterial() != Material.air) {
            if (p_175654_2_.requiresUpdates()) {
                var6 = 8;
                if (this.isAreaLoaded(var5.field_180282_a.add(-var6, -var6, -var6), var5.field_180282_a.add(var6, var6, var6))) {
                    final IBlockState var7 = this.getBlockState(var5.field_180282_a);
                    if (var7.getBlock().getMaterial() != Material.air && var7.getBlock() == var5.func_151351_a()) {
                        var7.getBlock().updateTick(this, var5.field_180282_a, var7, this.rand);
                    }
                }
                return;
            }
            p_175654_3_ = 1;
        }
        if (this.isAreaLoaded(p_175654_1_.add(-var6, -var6, -var6), p_175654_1_.add(var6, var6, var6))) {
            if (p_175654_2_.getMaterial() != Material.air) {
                var5.setScheduledTime(p_175654_3_ + this.worldInfo.getWorldTotalTime());
                var5.setPriority(p_175654_4_);
            }
            if (!this.pendingTickListEntriesHashSet.contains(var5)) {
                this.pendingTickListEntriesHashSet.add(var5);
                this.pendingTickListEntriesTreeSet.add(var5);
            }
        }
    }
    
    @Override
    public void func_180497_b(final BlockPos p_180497_1_, final Block p_180497_2_, final int p_180497_3_, final int p_180497_4_) {
        final NextTickListEntry var5 = new NextTickListEntry(p_180497_1_, p_180497_2_);
        var5.setPriority(p_180497_4_);
        if (p_180497_2_.getMaterial() != Material.air) {
            var5.setScheduledTime(p_180497_3_ + this.worldInfo.getWorldTotalTime());
        }
        if (!this.pendingTickListEntriesHashSet.contains(var5)) {
            this.pendingTickListEntriesHashSet.add(var5);
            this.pendingTickListEntriesTreeSet.add(var5);
        }
    }
    
    @Override
    public void updateEntities() {
        if (this.playerEntities.isEmpty()) {
            if (this.updateEntityTick++ >= 1200) {
                return;
            }
        }
        else {
            this.resetUpdateEntityTick();
        }
        super.updateEntities();
    }
    
    public void resetUpdateEntityTick() {
        this.updateEntityTick = 0;
    }
    
    @Override
    public boolean tickUpdates(final boolean p_72955_1_) {
        if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            return false;
        }
        int var2 = this.pendingTickListEntriesTreeSet.size();
        if (var2 != this.pendingTickListEntriesHashSet.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        }
        if (var2 > 1000) {
            var2 = 1000;
        }
        this.theProfiler.startSection("cleaning");
        for (int var3 = 0; var3 < var2; ++var3) {
            final NextTickListEntry var4 = this.pendingTickListEntriesTreeSet.first();
            if (!p_72955_1_ && var4.scheduledTime > this.worldInfo.getWorldTotalTime()) {
                break;
            }
            this.pendingTickListEntriesTreeSet.remove(var4);
            this.pendingTickListEntriesHashSet.remove(var4);
            this.pendingTickListEntriesThisTick.add(var4);
        }
        this.theProfiler.endSection();
        this.theProfiler.startSection("ticking");
        final Iterator var5 = this.pendingTickListEntriesThisTick.iterator();
        while (var5.hasNext()) {
            final NextTickListEntry var4 = var5.next();
            var5.remove();
            final byte var6 = 0;
            if (this.isAreaLoaded(var4.field_180282_a.add(-var6, -var6, -var6), var4.field_180282_a.add(var6, var6, var6))) {
                final IBlockState var7 = this.getBlockState(var4.field_180282_a);
                if (var7.getBlock().getMaterial() == Material.air || !Block.isEqualTo(var7.getBlock(), var4.func_151351_a())) {
                    continue;
                }
                try {
                    var7.getBlock().updateTick(this, var4.field_180282_a, var7, this.rand);
                }
                catch (Throwable var9) {
                    final CrashReport var8 = CrashReport.makeCrashReport(var9, "Exception while ticking a block");
                    final CrashReportCategory var10 = var8.makeCategory("Block being ticked");
                    CrashReportCategory.addBlockInfo(var10, var4.field_180282_a, var7);
                    throw new ReportedException(var8);
                }
            }
            else {
                this.scheduleUpdate(var4.field_180282_a, var4.func_151351_a(), 0);
            }
        }
        this.theProfiler.endSection();
        this.pendingTickListEntriesThisTick.clear();
        return !this.pendingTickListEntriesTreeSet.isEmpty();
    }
    
    @Override
    public List getPendingBlockUpdates(final Chunk p_72920_1_, final boolean p_72920_2_) {
        final ChunkCoordIntPair var3 = p_72920_1_.getChunkCoordIntPair();
        final int var4 = (var3.chunkXPos << 4) - 2;
        final int var5 = var4 + 16 + 2;
        final int var6 = (var3.chunkZPos << 4) - 2;
        final int var7 = var6 + 16 + 2;
        return this.func_175712_a(new StructureBoundingBox(var4, 0, var6, var5, 256, var7), p_72920_2_);
    }
    
    @Override
    public List func_175712_a(final StructureBoundingBox p_175712_1_, final boolean p_175712_2_) {
        ArrayList var3 = null;
        for (int var4 = 0; var4 < 2; ++var4) {
            Iterator var5;
            if (var4 == 0) {
                var5 = this.pendingTickListEntriesTreeSet.iterator();
            }
            else {
                var5 = this.pendingTickListEntriesThisTick.iterator();
                if (!this.pendingTickListEntriesThisTick.isEmpty()) {
                    WorldServer.logger.debug("toBeTicked = " + this.pendingTickListEntriesThisTick.size());
                }
            }
            while (var5.hasNext()) {
                final NextTickListEntry var6 = var5.next();
                final BlockPos var7 = var6.field_180282_a;
                if (var7.getX() >= p_175712_1_.minX && var7.getX() < p_175712_1_.maxX && var7.getZ() >= p_175712_1_.minZ && var7.getZ() < p_175712_1_.maxZ) {
                    if (p_175712_2_) {
                        this.pendingTickListEntriesHashSet.remove(var6);
                        var5.remove();
                    }
                    if (var3 == null) {
                        var3 = Lists.newArrayList();
                    }
                    var3.add(var6);
                }
            }
        }
        return var3;
    }
    
    @Override
    public void updateEntityWithOptionalForce(final Entity p_72866_1_, final boolean p_72866_2_) {
        if (!this.func_175735_ai() && (p_72866_1_ instanceof EntityAnimal || p_72866_1_ instanceof EntityWaterMob)) {
            p_72866_1_.setDead();
        }
        if (!this.func_175738_ah() && p_72866_1_ instanceof INpc) {
            p_72866_1_.setDead();
        }
        super.updateEntityWithOptionalForce(p_72866_1_, p_72866_2_);
    }
    
    private boolean func_175738_ah() {
        return this.mcServer.getCanSpawnNPCs();
    }
    
    private boolean func_175735_ai() {
        return this.mcServer.getCanSpawnAnimals();
    }
    
    @Override
    protected IChunkProvider createChunkProvider() {
        final IChunkLoader var1 = this.saveHandler.getChunkLoader(this.provider);
        return this.theChunkProviderServer = new ChunkProviderServer(this, var1, this.provider.createChunkGenerator());
    }
    
    public List func_147486_a(final int p_147486_1_, final int p_147486_2_, final int p_147486_3_, final int p_147486_4_, final int p_147486_5_, final int p_147486_6_) {
        final ArrayList var7 = Lists.newArrayList();
        for (int var8 = 0; var8 < this.loadedTileEntityList.size(); ++var8) {
            final TileEntity var9 = this.loadedTileEntityList.get(var8);
            final BlockPos var10 = var9.getPos();
            if (var10.getX() >= p_147486_1_ && var10.getY() >= p_147486_2_ && var10.getZ() >= p_147486_3_ && var10.getX() < p_147486_4_ && var10.getY() < p_147486_5_ && var10.getZ() < p_147486_6_) {
                var7.add(var9);
            }
        }
        return var7;
    }
    
    @Override
    public boolean isBlockModifiable(final EntityPlayer p_175660_1_, final BlockPos p_175660_2_) {
        return !this.mcServer.isBlockProtected(this, p_175660_2_, p_175660_1_) && this.getWorldBorder().contains(p_175660_2_);
    }
    
    @Override
    public void initialize(final WorldSettings settings) {
        if (!this.worldInfo.isInitialized()) {
            try {
                this.createSpawnPosition(settings);
                if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
                    this.setDebugWorldSettings();
                }
                super.initialize(settings);
            }
            catch (Throwable var4) {
                final CrashReport var3 = CrashReport.makeCrashReport(var4, "Exception initializing level");
                try {
                    this.addWorldInfoToCrashReport(var3);
                }
                catch (Throwable t) {}
                throw new ReportedException(var3);
            }
            this.worldInfo.setServerInitialized(true);
        }
    }
    
    private void setDebugWorldSettings() {
        this.worldInfo.setMapFeaturesEnabled(false);
        this.worldInfo.setAllowCommands(true);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThundering(false);
        this.worldInfo.func_176142_i(1000000000);
        this.worldInfo.setWorldTime(6000L);
        this.worldInfo.setGameType(WorldSettings.GameType.SPECTATOR);
        this.worldInfo.setHardcore(false);
        this.worldInfo.setDifficulty(EnumDifficulty.PEACEFUL);
        this.worldInfo.setDifficultyLocked(true);
        this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
    }
    
    private void createSpawnPosition(final WorldSettings p_73052_1_) {
        if (!this.provider.canRespawnHere()) {
            this.worldInfo.setSpawn(BlockPos.ORIGIN.offsetUp(this.provider.getAverageGroundLevel()));
        }
        else if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            this.worldInfo.setSpawn(BlockPos.ORIGIN.offsetUp());
        }
        else {
            this.findingSpawnPoint = true;
            final WorldChunkManager var2 = this.provider.getWorldChunkManager();
            final List var3 = var2.getBiomesToSpawnIn();
            final Random var4 = new Random(this.getSeed());
            final BlockPos var5 = var2.findBiomePosition(0, 0, 256, var3, var4);
            int var6 = 0;
            final int var7 = this.provider.getAverageGroundLevel();
            int var8 = 0;
            if (var5 != null) {
                var6 = var5.getX();
                var8 = var5.getZ();
            }
            else {
                WorldServer.logger.warn("Unable to find spawn biome");
            }
            int var9 = 0;
            while (!this.provider.canCoordinateBeSpawn(var6, var8)) {
                var6 += var4.nextInt(64) - var4.nextInt(64);
                var8 += var4.nextInt(64) - var4.nextInt(64);
                if (++var9 == 1000) {
                    break;
                }
            }
            this.worldInfo.setSpawn(new BlockPos(var6, var7, var8));
            this.findingSpawnPoint = false;
            if (p_73052_1_.isBonusChestEnabled()) {
                this.createBonusChest();
            }
        }
    }
    
    protected void createBonusChest() {
        final WorldGeneratorBonusChest var1 = new WorldGeneratorBonusChest(WorldServer.bonusChestContent, 10);
        for (int var2 = 0; var2 < 10; ++var2) {
            final int var3 = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
            final int var4 = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6);
            final BlockPos var5 = this.func_175672_r(new BlockPos(var3, 0, var4)).offsetUp();
            if (var1.generate(this, this.rand, var5)) {
                break;
            }
        }
    }
    
    public BlockPos func_180504_m() {
        return this.provider.func_177496_h();
    }
    
    public void saveAllChunks(final boolean p_73044_1_, final IProgressUpdate p_73044_2_) throws MinecraftException {
        if (this.chunkProvider.canSave()) {
            if (p_73044_2_ != null) {
                p_73044_2_.displaySavingString("Saving level");
            }
            this.saveLevel();
            if (p_73044_2_ != null) {
                p_73044_2_.displayLoadingString("Saving chunks");
            }
            this.chunkProvider.saveChunks(p_73044_1_, p_73044_2_);
            final List var3 = this.theChunkProviderServer.func_152380_a();
            for (final Chunk var5 : var3) {
                if (!this.thePlayerManager.func_152621_a(var5.xPosition, var5.zPosition)) {
                    this.theChunkProviderServer.dropChunk(var5.xPosition, var5.zPosition);
                }
            }
        }
    }
    
    public void saveChunkData() {
        if (this.chunkProvider.canSave()) {
            this.chunkProvider.saveExtraData();
        }
    }
    
    protected void saveLevel() throws MinecraftException {
        this.checkSessionLock();
        this.worldInfo.func_176145_a(this.getWorldBorder().getDiameter());
        this.worldInfo.func_176124_d(this.getWorldBorder().getCenterX());
        this.worldInfo.func_176141_c(this.getWorldBorder().getCenterZ());
        this.worldInfo.func_176129_e(this.getWorldBorder().getDamageBuffer());
        this.worldInfo.func_176125_f(this.getWorldBorder().func_177727_n());
        this.worldInfo.func_176122_j(this.getWorldBorder().getWarningDistance());
        this.worldInfo.func_176136_k(this.getWorldBorder().getWarningTime());
        this.worldInfo.func_176118_b(this.getWorldBorder().getTargetSize());
        this.worldInfo.func_176135_e(this.getWorldBorder().getTimeUntilTarget());
        this.saveHandler.saveWorldInfoWithPlayer(this.worldInfo, this.mcServer.getConfigurationManager().getHostPlayerData());
        this.mapStorage.saveAllData();
    }
    
    @Override
    protected void onEntityAdded(final Entity p_72923_1_) {
        super.onEntityAdded(p_72923_1_);
        this.entitiesById.addKey(p_72923_1_.getEntityId(), p_72923_1_);
        this.entitiesByUuid.put(p_72923_1_.getUniqueID(), p_72923_1_);
        final Entity[] var2 = p_72923_1_.getParts();
        if (var2 != null) {
            for (final Entity element : var2) {
                this.entitiesById.addKey(element.getEntityId(), element);
            }
        }
    }
    
    @Override
    protected void onEntityRemoved(final Entity p_72847_1_) {
        super.onEntityRemoved(p_72847_1_);
        this.entitiesById.removeObject(p_72847_1_.getEntityId());
        this.entitiesByUuid.remove(p_72847_1_.getUniqueID());
        final Entity[] var2 = p_72847_1_.getParts();
        if (var2 != null) {
            for (final Entity element : var2) {
                this.entitiesById.removeObject(element.getEntityId());
            }
        }
    }
    
    @Override
    public boolean addWeatherEffect(final Entity p_72942_1_) {
        if (super.addWeatherEffect(p_72942_1_)) {
            this.mcServer.getConfigurationManager().sendToAllNear(p_72942_1_.posX, p_72942_1_.posY, p_72942_1_.posZ, 512.0, this.provider.getDimensionId(), new S2CPacketSpawnGlobalEntity(p_72942_1_));
            return true;
        }
        return false;
    }
    
    @Override
    public void setEntityState(final Entity entityIn, final byte p_72960_2_) {
        this.getEntityTracker().func_151248_b(entityIn, new S19PacketEntityStatus(entityIn, p_72960_2_));
    }
    
    @Override
    public Explosion newExplosion(final Entity p_72885_1_, final double p_72885_2_, final double p_72885_4_, final double p_72885_6_, final float p_72885_8_, final boolean p_72885_9_, final boolean p_72885_10_) {
        final Explosion var11 = new Explosion(this, p_72885_1_, p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_, p_72885_9_, p_72885_10_);
        var11.doExplosionA();
        var11.doExplosionB(false);
        if (!p_72885_10_) {
            var11.func_180342_d();
        }
        for (final EntityPlayer var13 : this.playerEntities) {
            if (var13.getDistanceSq(p_72885_2_, p_72885_4_, p_72885_6_) < 4096.0) {
                ((EntityPlayerMP)var13).playerNetServerHandler.sendPacket(new S27PacketExplosion(p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_, var11.func_180343_e(), var11.func_77277_b().get(var13)));
            }
        }
        return var11;
    }
    
    @Override
    public void addBlockEvent(final BlockPos pos, final Block blockIn, final int eventID, final int eventParam) {
        final BlockEventData var5 = new BlockEventData(pos, blockIn, eventID, eventParam);
        for (final BlockEventData var7 : this.field_147490_S[this.blockEventCacheIndex]) {
            if (var7.equals(var5)) {
                return;
            }
        }
        this.field_147490_S[this.blockEventCacheIndex].add(var5);
    }
    
    private void func_147488_Z() {
        while (!this.field_147490_S[this.blockEventCacheIndex].isEmpty()) {
            final int var1 = this.blockEventCacheIndex;
            this.blockEventCacheIndex ^= 0x1;
            for (final BlockEventData var3 : this.field_147490_S[var1]) {
                if (this.func_147485_a(var3)) {
                    this.mcServer.getConfigurationManager().sendToAllNear(var3.func_180328_a().getX(), var3.func_180328_a().getY(), var3.func_180328_a().getZ(), 64.0, this.provider.getDimensionId(), new S24PacketBlockAction(var3.func_180328_a(), var3.getBlock(), var3.getEventID(), var3.getEventParameter()));
                }
            }
            this.field_147490_S[var1].clear();
        }
    }
    
    private boolean func_147485_a(final BlockEventData p_147485_1_) {
        final IBlockState var2 = this.getBlockState(p_147485_1_.func_180328_a());
        return var2.getBlock() == p_147485_1_.getBlock() && var2.getBlock().onBlockEventReceived(this, p_147485_1_.func_180328_a(), var2, p_147485_1_.getEventID(), p_147485_1_.getEventParameter());
    }
    
    public void flush() {
        this.saveHandler.flush();
    }
    
    @Override
    protected void updateWeather() {
        final boolean var1 = this.isRaining();
        super.updateWeather();
        if (this.prevRainingStrength != this.rainingStrength) {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(7, this.rainingStrength), this.provider.getDimensionId());
        }
        if (this.prevThunderingStrength != this.thunderingStrength) {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(8, this.thunderingStrength), this.provider.getDimensionId());
        }
        if (var1 != this.isRaining()) {
            if (var1) {
                this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0f));
            }
            else {
                this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(1, 0.0f));
            }
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, this.rainingStrength));
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, this.thunderingStrength));
        }
    }
    
    @Override
    protected int getRenderDistanceChunks() {
        return this.mcServer.getConfigurationManager().getViewDistance();
    }
    
    public MinecraftServer func_73046_m() {
        return this.mcServer;
    }
    
    public EntityTracker getEntityTracker() {
        return this.theEntityTracker;
    }
    
    public PlayerManager getPlayerManager() {
        return this.thePlayerManager;
    }
    
    public Teleporter getDefaultTeleporter() {
        return this.worldTeleporter;
    }
    
    public void func_175739_a(final EnumParticleTypes p_175739_1_, final double p_175739_2_, final double p_175739_4_, final double p_175739_6_, final int p_175739_8_, final double p_175739_9_, final double p_175739_11_, final double p_175739_13_, final double p_175739_15_, final int... p_175739_17_) {
        this.func_180505_a(p_175739_1_, false, p_175739_2_, p_175739_4_, p_175739_6_, p_175739_8_, p_175739_9_, p_175739_11_, p_175739_13_, p_175739_15_, p_175739_17_);
    }
    
    public void func_180505_a(final EnumParticleTypes p_180505_1_, final boolean p_180505_2_, final double p_180505_3_, final double p_180505_5_, final double p_180505_7_, final int p_180505_9_, final double p_180505_10_, final double p_180505_12_, final double p_180505_14_, final double p_180505_16_, final int... p_180505_18_) {
        final S2APacketParticles var19 = new S2APacketParticles(p_180505_1_, p_180505_2_, (float)p_180505_3_, (float)p_180505_5_, (float)p_180505_7_, (float)p_180505_10_, (float)p_180505_12_, (float)p_180505_14_, (float)p_180505_16_, p_180505_9_, p_180505_18_);
        for (int var20 = 0; var20 < this.playerEntities.size(); ++var20) {
            final EntityPlayerMP var21 = this.playerEntities.get(var20);
            final BlockPos var22 = var21.getPosition();
            final double var23 = var22.distanceSq(p_180505_3_, p_180505_5_, p_180505_7_);
            if (var23 <= 256.0 || (p_180505_2_ && var23 <= 65536.0)) {
                var21.playerNetServerHandler.sendPacket(var19);
            }
        }
    }
    
    public Entity getEntityFromUuid(final UUID uuid) {
        return this.entitiesByUuid.get(uuid);
    }
    
    @Override
    public ListenableFuture addScheduledTask(final Runnable runnableToSchedule) {
        return this.mcServer.addScheduledTask(runnableToSchedule);
    }
    
    @Override
    public boolean isCallingFromMinecraftThread() {
        return this.mcServer.isCallingFromMinecraftThread();
    }
    
    static {
        logger = LogManager.getLogger();
        bonusChestContent = Lists.newArrayList((Object[])new WeightedRandomChestContent[] { new WeightedRandomChestContent(Items.stick, 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.planks), 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log), 0, 1, 3, 10), new WeightedRandomChestContent(Items.stone_axe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_axe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.stone_pickaxe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.apple, 0, 2, 3, 5), new WeightedRandomChestContent(Items.bread, 0, 2, 3, 3), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log2), 0, 1, 3, 10) });
    }
    
    static class ServerBlockEventList extends ArrayList
    {
        private static final long serialVersionUID = 1L;
        private static final String __OBFID = "CL_00001439";
        
        private ServerBlockEventList() {
        }
        
        ServerBlockEventList(final Object p_i1521_1_) {
            this();
        }
    }
}
