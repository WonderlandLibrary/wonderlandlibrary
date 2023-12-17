// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import com.google.common.util.concurrent.ListenableFuture;
import net.minecraft.network.play.server.SPacketParticles;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.block.BlockEventData;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSpawnGlobalEntity;
import net.minecraft.entity.EntityList;
import java.util.Collection;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.biome.BiomeProvider;
import java.util.Random;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.entity.INpc;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.init.Blocks;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.material.Material;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import javax.annotation.Nullable;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.biome.Biome;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.world.storage.loot.LootTableManager;
import java.io.File;
import net.minecraft.world.storage.WorldSavedDataCallableSave;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.world.storage.WorldSavedData;
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
import net.minecraft.entity.Entity;
import java.util.UUID;
import java.util.Map;
import java.util.TreeSet;
import java.util.Set;
import net.minecraft.server.management.PlayerChunkMap;
import net.minecraft.entity.EntityTracker;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.IThreadListener;

public class WorldServer extends World implements IThreadListener
{
    private static final Logger LOGGER;
    private final MinecraftServer server;
    private final EntityTracker entityTracker;
    private final PlayerChunkMap playerChunkMap;
    private final Set<NextTickListEntry> pendingTickListEntriesHashSet;
    private final TreeSet<NextTickListEntry> pendingTickListEntriesTreeSet;
    private final Map<UUID, Entity> entitiesByUuid;
    public boolean disableLevelSaving;
    private boolean allPlayersSleeping;
    private int updateEntityTick;
    private final Teleporter worldTeleporter;
    private final WorldEntitySpawner entitySpawner;
    protected final VillageSiege villageSiege;
    private final ServerBlockEventList[] blockEventQueue;
    private int blockEventCacheIndex;
    private final List<NextTickListEntry> pendingTickListEntriesThisTick;
    
    public WorldServer(final MinecraftServer server, final ISaveHandler saveHandlerIn, final WorldInfo info, final int dimensionId, final Profiler profilerIn) {
        super(saveHandlerIn, info, DimensionType.getById(dimensionId).createDimension(), profilerIn, false);
        this.pendingTickListEntriesHashSet = (Set<NextTickListEntry>)Sets.newHashSet();
        this.pendingTickListEntriesTreeSet = new TreeSet<NextTickListEntry>();
        this.entitiesByUuid = (Map<UUID, Entity>)Maps.newHashMap();
        this.entitySpawner = new WorldEntitySpawner();
        this.villageSiege = new VillageSiege(this);
        this.blockEventQueue = new ServerBlockEventList[] { new ServerBlockEventList(), new ServerBlockEventList() };
        this.pendingTickListEntriesThisTick = (List<NextTickListEntry>)Lists.newArrayList();
        this.server = server;
        this.entityTracker = new EntityTracker(this);
        this.playerChunkMap = new PlayerChunkMap(this);
        this.provider.setWorld(this);
        this.chunkProvider = this.createChunkProvider();
        this.worldTeleporter = new Teleporter(this);
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
        this.getWorldBorder().setSize(server.getMaxWorldSize());
    }
    
    @Override
    public World init() {
        this.mapStorage = new MapStorage(this.saveHandler);
        final String s = VillageCollection.fileNameForProvider(this.provider);
        final VillageCollection villagecollection = (VillageCollection)this.mapStorage.getOrLoadData(VillageCollection.class, s);
        if (villagecollection == null) {
            this.villageCollection = new VillageCollection(this);
            this.mapStorage.setData(s, this.villageCollection);
        }
        else {
            (this.villageCollection = villagecollection).setWorldsForAll(this);
        }
        this.worldScoreboard = new ServerScoreboard(this.server);
        ScoreboardSaveData scoreboardsavedata = (ScoreboardSaveData)this.mapStorage.getOrLoadData(ScoreboardSaveData.class, "scoreboard");
        if (scoreboardsavedata == null) {
            scoreboardsavedata = new ScoreboardSaveData();
            this.mapStorage.setData("scoreboard", scoreboardsavedata);
        }
        scoreboardsavedata.setScoreboard(this.worldScoreboard);
        ((ServerScoreboard)this.worldScoreboard).addDirtyRunnable(new WorldSavedDataCallableSave(scoreboardsavedata));
        this.lootTable = new LootTableManager(new File(new File(this.saveHandler.getWorldDirectory(), "data"), "loot_tables"));
        this.advancementManager = new AdvancementManager(new File(new File(this.saveHandler.getWorldDirectory(), "data"), "advancements"));
        this.functionManager = new FunctionManager(new File(new File(this.saveHandler.getWorldDirectory(), "data"), "functions"), this.server);
        this.getWorldBorder().setCenter(this.worldInfo.getBorderCenterX(), this.worldInfo.getBorderCenterZ());
        this.getWorldBorder().setDamageAmount(this.worldInfo.getBorderDamagePerBlock());
        this.getWorldBorder().setDamageBuffer(this.worldInfo.getBorderSafeZone());
        this.getWorldBorder().setWarningDistance(this.worldInfo.getBorderWarningDistance());
        this.getWorldBorder().setWarningTime(this.worldInfo.getBorderWarningTime());
        if (this.worldInfo.getBorderLerpTime() > 0L) {
            this.getWorldBorder().setTransition(this.worldInfo.getBorderSize(), this.worldInfo.getBorderLerpTarget(), this.worldInfo.getBorderLerpTime());
        }
        else {
            this.getWorldBorder().setTransition(this.worldInfo.getBorderSize());
        }
        return this;
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.getWorldInfo().isHardcoreModeEnabled() && this.getDifficulty() != EnumDifficulty.HARD) {
            this.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
        }
        this.provider.getBiomeProvider().cleanupCache();
        if (this.areAllPlayersAsleep()) {
            if (this.getGameRules().getBoolean("doDaylightCycle")) {
                final long i = this.worldInfo.getWorldTime() + 24000L;
                this.worldInfo.setWorldTime(i - i % 24000L);
            }
            this.wakeAllPlayers();
        }
        this.profiler.startSection("mobSpawner");
        if (this.getGameRules().getBoolean("doMobSpawning") && this.worldInfo.getTerrainType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
            this.entitySpawner.findChunksForSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs, this.worldInfo.getWorldTotalTime() % 400L == 0L);
        }
        this.profiler.endStartSection("chunkSource");
        this.chunkProvider.tick();
        final int j = this.calculateSkylightSubtracted(1.0f);
        if (j != this.getSkylightSubtracted()) {
            this.setSkylightSubtracted(j);
        }
        this.worldInfo.setWorldTotalTime(this.worldInfo.getWorldTotalTime() + 1L);
        if (this.getGameRules().getBoolean("doDaylightCycle")) {
            this.worldInfo.setWorldTime(this.worldInfo.getWorldTime() + 1L);
        }
        this.profiler.endStartSection("tickPending");
        this.tickUpdates(false);
        this.profiler.endStartSection("tickBlocks");
        this.updateBlocks();
        this.profiler.endStartSection("chunkMap");
        this.playerChunkMap.tick();
        this.profiler.endStartSection("village");
        this.villageCollection.tick();
        this.villageSiege.tick();
        this.profiler.endStartSection("portalForcer");
        this.worldTeleporter.removeStalePortalLocations(this.getTotalWorldTime());
        this.profiler.endSection();
        this.sendQueuedBlockEvents();
    }
    
    @Nullable
    public Biome.SpawnListEntry getSpawnListEntryForTypeAt(final EnumCreatureType creatureType, final BlockPos pos) {
        final List<Biome.SpawnListEntry> list = this.getChunkProvider().getPossibleCreatures(creatureType, pos);
        return (list != null && !list.isEmpty()) ? WeightedRandom.getRandomItem(this.rand, list) : null;
    }
    
    public boolean canCreatureTypeSpawnHere(final EnumCreatureType creatureType, final Biome.SpawnListEntry spawnListEntry, final BlockPos pos) {
        final List<Biome.SpawnListEntry> list = this.getChunkProvider().getPossibleCreatures(creatureType, pos);
        return list != null && !list.isEmpty() && list.contains(spawnListEntry);
    }
    
    @Override
    public void updateAllPlayersSleepingFlag() {
        this.allPlayersSleeping = false;
        if (!this.playerEntities.isEmpty()) {
            int i = 0;
            int j = 0;
            for (final EntityPlayer entityplayer : this.playerEntities) {
                if (entityplayer.isSpectator()) {
                    ++i;
                }
                else {
                    if (!entityplayer.isPlayerSleeping()) {
                        continue;
                    }
                    ++j;
                }
            }
            this.allPlayersSleeping = (j > 0 && j >= this.playerEntities.size() - i);
        }
    }
    
    protected void wakeAllPlayers() {
        this.allPlayersSleeping = false;
        for (final EntityPlayer entityplayer : this.playerEntities.stream().filter(EntityPlayer::isPlayerSleeping).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList())) {
            entityplayer.wakeUpPlayer(false, false, true);
        }
        if (this.getGameRules().getBoolean("doWeatherCycle")) {
            this.resetRainAndThunder();
        }
    }
    
    private void resetRainAndThunder() {
        this.worldInfo.setRainTime(0);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThunderTime(0);
        this.worldInfo.setThundering(false);
    }
    
    public boolean areAllPlayersAsleep() {
        if (this.allPlayersSleeping && !this.isRemote) {
            for (final EntityPlayer entityplayer : this.playerEntities) {
                if (!entityplayer.isSpectator() && !entityplayer.isPlayerFullyAsleep()) {
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
            this.worldInfo.setSpawnY(this.getSeaLevel() + 1);
        }
        int i = this.worldInfo.getSpawnX();
        int j = this.worldInfo.getSpawnZ();
        int k = 0;
        while (this.getGroundAboveSeaLevel(new BlockPos(i, 0, j)).getMaterial() == Material.AIR) {
            i += this.rand.nextInt(8) - this.rand.nextInt(8);
            j += this.rand.nextInt(8) - this.rand.nextInt(8);
            if (++k == 10000) {
                break;
            }
        }
        this.worldInfo.setSpawnX(i);
        this.worldInfo.setSpawnZ(j);
    }
    
    @Override
    protected boolean isChunkLoaded(final int x, final int z, final boolean allowEmpty) {
        return this.getChunkProvider().chunkExists(x, z);
    }
    
    protected void playerCheckLight() {
        this.profiler.startSection("playerCheckLight");
        if (!this.playerEntities.isEmpty()) {
            final int i = this.rand.nextInt(this.playerEntities.size());
            final EntityPlayer entityplayer = this.playerEntities.get(i);
            final int j = MathHelper.floor(entityplayer.posX) + this.rand.nextInt(11) - 5;
            final int k = MathHelper.floor(entityplayer.posY) + this.rand.nextInt(11) - 5;
            final int l = MathHelper.floor(entityplayer.posZ) + this.rand.nextInt(11) - 5;
            this.checkLight(new BlockPos(j, k, l));
        }
        this.profiler.endSection();
    }
    
    @Override
    protected void updateBlocks() {
        this.playerCheckLight();
        if (this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            final Iterator<Chunk> iterator1 = this.playerChunkMap.getChunkIterator();
            while (iterator1.hasNext()) {
                iterator1.next().onTick(false);
            }
        }
        else {
            final int i = this.getGameRules().getInt("randomTickSpeed");
            final boolean flag = this.isRaining();
            final boolean flag2 = this.isThundering();
            this.profiler.startSection("pollingChunks");
            final Iterator<Chunk> iterator2 = this.playerChunkMap.getChunkIterator();
            while (iterator2.hasNext()) {
                this.profiler.startSection("getChunk");
                final Chunk chunk = iterator2.next();
                final int j = chunk.x * 16;
                final int k = chunk.z * 16;
                this.profiler.endStartSection("checkNextLight");
                chunk.enqueueRelightChecks();
                this.profiler.endStartSection("tickChunk");
                chunk.onTick(false);
                this.profiler.endStartSection("thunder");
                if (flag && flag2 && this.rand.nextInt(100000) == 0) {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    final int l = this.updateLCG >> 2;
                    final BlockPos blockpos = this.adjustPosToNearbyEntity(new BlockPos(j + (l & 0xF), 0, k + (l >> 8 & 0xF)));
                    if (this.isRainingAt(blockpos)) {
                        final DifficultyInstance difficultyinstance = this.getDifficultyForLocation(blockpos);
                        if (this.getGameRules().getBoolean("doMobSpawning") && this.rand.nextDouble() < difficultyinstance.getAdditionalDifficulty() * 0.01) {
                            final EntitySkeletonHorse entityskeletonhorse = new EntitySkeletonHorse(this);
                            entityskeletonhorse.setTrap(true);
                            entityskeletonhorse.setGrowingAge(0);
                            entityskeletonhorse.setPosition(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                            this.spawnEntity(entityskeletonhorse);
                            this.addWeatherEffect(new EntityLightningBolt(this, blockpos.getX(), blockpos.getY(), blockpos.getZ(), true));
                        }
                        else {
                            this.addWeatherEffect(new EntityLightningBolt(this, blockpos.getX(), blockpos.getY(), blockpos.getZ(), false));
                        }
                    }
                }
                this.profiler.endStartSection("iceandsnow");
                if (this.rand.nextInt(16) == 0) {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    final int j2 = this.updateLCG >> 2;
                    final BlockPos blockpos2 = this.getPrecipitationHeight(new BlockPos(j + (j2 & 0xF), 0, k + (j2 >> 8 & 0xF)));
                    final BlockPos blockpos3 = blockpos2.down();
                    if (this.canBlockFreezeNoWater(blockpos3)) {
                        this.setBlockState(blockpos3, Blocks.ICE.getDefaultState());
                    }
                    if (flag && this.canSnowAt(blockpos2, true)) {
                        this.setBlockState(blockpos2, Blocks.SNOW_LAYER.getDefaultState());
                    }
                    if (flag && this.getBiome(blockpos3).canRain()) {
                        this.getBlockState(blockpos3).getBlock().fillWithRain(this, blockpos3);
                    }
                }
                this.profiler.endStartSection("tickBlocks");
                if (i > 0) {
                    for (final ExtendedBlockStorage extendedblockstorage : chunk.getBlockStorageArray()) {
                        if (extendedblockstorage != Chunk.NULL_BLOCK_STORAGE && extendedblockstorage.needsRandomTick()) {
                            for (int i2 = 0; i2 < i; ++i2) {
                                this.updateLCG = this.updateLCG * 3 + 1013904223;
                                final int j3 = this.updateLCG >> 2;
                                final int k2 = j3 & 0xF;
                                final int l2 = j3 >> 8 & 0xF;
                                final int i3 = j3 >> 16 & 0xF;
                                final IBlockState iblockstate = extendedblockstorage.get(k2, i3, l2);
                                final Block block = iblockstate.getBlock();
                                this.profiler.startSection("randomTick");
                                if (block.getTickRandomly()) {
                                    block.randomTick(this, new BlockPos(k2 + j, i3 + extendedblockstorage.getYLocation(), l2 + k), iblockstate, this.rand);
                                }
                                this.profiler.endSection();
                            }
                        }
                    }
                }
                this.profiler.endSection();
            }
            this.profiler.endSection();
        }
    }
    
    protected BlockPos adjustPosToNearbyEntity(final BlockPos pos) {
        BlockPos blockpos = this.getPrecipitationHeight(pos);
        final AxisAlignedBB axisalignedbb = new AxisAlignedBB(blockpos, new BlockPos(blockpos.getX(), this.getHeight(), blockpos.getZ())).grow(3.0);
        final List<EntityLivingBase> list = this.getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, axisalignedbb, (com.google.common.base.Predicate<? super EntityLivingBase>)new Predicate<EntityLivingBase>() {
            public boolean apply(@Nullable final EntityLivingBase p_apply_1_) {
                return p_apply_1_ != null && p_apply_1_.isEntityAlive() && WorldServer.this.canSeeSky(p_apply_1_.getPosition());
            }
        });
        if (!list.isEmpty()) {
            return list.get(this.rand.nextInt(list.size())).getPosition();
        }
        if (blockpos.getY() == -1) {
            blockpos = blockpos.up(2);
        }
        return blockpos;
    }
    
    @Override
    public boolean isBlockTickPending(final BlockPos pos, final Block blockType) {
        final NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockType);
        return this.pendingTickListEntriesThisTick.contains(nextticklistentry);
    }
    
    @Override
    public boolean isUpdateScheduled(final BlockPos pos, final Block blk) {
        final NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blk);
        return this.pendingTickListEntriesHashSet.contains(nextticklistentry);
    }
    
    @Override
    public void scheduleUpdate(final BlockPos pos, final Block blockIn, final int delay) {
        this.updateBlockTick(pos, blockIn, delay, 0);
    }
    
    @Override
    public void updateBlockTick(final BlockPos pos, final Block blockIn, int delay, final int priority) {
        final Material material = blockIn.getDefaultState().getMaterial();
        if (this.scheduledUpdatesAreImmediate && material != Material.AIR) {
            if (blockIn.requiresUpdates()) {
                if (this.isAreaLoaded(pos.add(-8, -8, -8), pos.add(8, 8, 8))) {
                    final IBlockState iblockstate = this.getBlockState(pos);
                    if (iblockstate.getMaterial() != Material.AIR && iblockstate.getBlock() == blockIn) {
                        iblockstate.getBlock().updateTick(this, pos, iblockstate, this.rand);
                    }
                }
                return;
            }
            delay = 1;
        }
        final NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockIn);
        if (this.isBlockLoaded(pos)) {
            if (material != Material.AIR) {
                nextticklistentry.setScheduledTime(delay + this.worldInfo.getWorldTotalTime());
                nextticklistentry.setPriority(priority);
            }
            if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry)) {
                this.pendingTickListEntriesHashSet.add(nextticklistentry);
                this.pendingTickListEntriesTreeSet.add(nextticklistentry);
            }
        }
    }
    
    @Override
    public void scheduleBlockUpdate(final BlockPos pos, final Block blockIn, final int delay, final int priority) {
        final NextTickListEntry nextticklistentry = new NextTickListEntry(pos, blockIn);
        nextticklistentry.setPriority(priority);
        final Material material = blockIn.getDefaultState().getMaterial();
        if (material != Material.AIR) {
            nextticklistentry.setScheduledTime(delay + this.worldInfo.getWorldTotalTime());
        }
        if (!this.pendingTickListEntriesHashSet.contains(nextticklistentry)) {
            this.pendingTickListEntriesHashSet.add(nextticklistentry);
            this.pendingTickListEntriesTreeSet.add(nextticklistentry);
        }
    }
    
    @Override
    public void updateEntities() {
        if (this.playerEntities.isEmpty()) {
            if (this.updateEntityTick++ >= 300) {
                return;
            }
        }
        else {
            this.resetUpdateEntityTick();
        }
        this.provider.onWorldUpdateEntities();
        super.updateEntities();
    }
    
    @Override
    protected void tickPlayers() {
        super.tickPlayers();
        this.profiler.endStartSection("players");
        for (int i = 0; i < this.playerEntities.size(); ++i) {
            final Entity entity = this.playerEntities.get(i);
            final Entity entity2 = entity.getRidingEntity();
            if (entity2 != null) {
                if (!entity2.isDead && entity2.isPassenger(entity)) {
                    continue;
                }
                entity.dismountRidingEntity();
            }
            this.profiler.startSection("tick");
            if (!entity.isDead) {
                try {
                    this.updateEntity(entity);
                }
                catch (Throwable throwable) {
                    final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
                    final CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
                    entity.addEntityCrashInfo(crashreportcategory);
                    throw new ReportedException(crashreport);
                }
            }
            this.profiler.endSection();
            this.profiler.startSection("remove");
            if (entity.isDead) {
                final int j = entity.chunkCoordX;
                final int k = entity.chunkCoordZ;
                if (entity.addedToChunk && this.isChunkLoaded(j, k, true)) {
                    this.getChunk(j, k).removeEntity(entity);
                }
                this.loadedEntityList.remove(entity);
                this.onEntityRemoved(entity);
            }
            this.profiler.endSection();
        }
    }
    
    public void resetUpdateEntityTick() {
        this.updateEntityTick = 0;
    }
    
    @Override
    public boolean tickUpdates(final boolean runAllPending) {
        if (this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return false;
        }
        int i = this.pendingTickListEntriesTreeSet.size();
        if (i != this.pendingTickListEntriesHashSet.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        }
        if (i > 65536) {
            i = 65536;
        }
        this.profiler.startSection("cleaning");
        for (int j = 0; j < i; ++j) {
            final NextTickListEntry nextticklistentry = this.pendingTickListEntriesTreeSet.first();
            if (!runAllPending && nextticklistentry.scheduledTime > this.worldInfo.getWorldTotalTime()) {
                break;
            }
            this.pendingTickListEntriesTreeSet.remove(nextticklistentry);
            this.pendingTickListEntriesHashSet.remove(nextticklistentry);
            this.pendingTickListEntriesThisTick.add(nextticklistentry);
        }
        this.profiler.endSection();
        this.profiler.startSection("ticking");
        final Iterator<NextTickListEntry> iterator = this.pendingTickListEntriesThisTick.iterator();
        while (iterator.hasNext()) {
            final NextTickListEntry nextticklistentry2 = iterator.next();
            iterator.remove();
            final int k = 0;
            if (this.isAreaLoaded(nextticklistentry2.position.add(0, 0, 0), nextticklistentry2.position.add(0, 0, 0))) {
                final IBlockState iblockstate = this.getBlockState(nextticklistentry2.position);
                if (iblockstate.getMaterial() == Material.AIR || !Block.isEqualTo(iblockstate.getBlock(), nextticklistentry2.getBlock())) {
                    continue;
                }
                try {
                    iblockstate.getBlock().updateTick(this, nextticklistentry2.position, iblockstate, this.rand);
                }
                catch (Throwable throwable) {
                    final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while ticking a block");
                    final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being ticked");
                    CrashReportCategory.addBlockInfo(crashreportcategory, nextticklistentry2.position, iblockstate);
                    throw new ReportedException(crashreport);
                }
            }
            else {
                this.scheduleUpdate(nextticklistentry2.position, nextticklistentry2.getBlock(), 0);
            }
        }
        this.profiler.endSection();
        this.pendingTickListEntriesThisTick.clear();
        return !this.pendingTickListEntriesTreeSet.isEmpty();
    }
    
    @Nullable
    @Override
    public List<NextTickListEntry> getPendingBlockUpdates(final Chunk chunkIn, final boolean remove) {
        final ChunkPos chunkpos = chunkIn.getPos();
        final int i = (chunkpos.x << 4) - 2;
        final int j = i + 16 + 2;
        final int k = (chunkpos.z << 4) - 2;
        final int l = k + 16 + 2;
        return this.getPendingBlockUpdates(new StructureBoundingBox(i, 0, k, j, 256, l), remove);
    }
    
    @Nullable
    @Override
    public List<NextTickListEntry> getPendingBlockUpdates(final StructureBoundingBox structureBB, final boolean remove) {
        List<NextTickListEntry> list = null;
        for (int i = 0; i < 2; ++i) {
            Iterator<NextTickListEntry> iterator;
            if (i == 0) {
                iterator = this.pendingTickListEntriesTreeSet.iterator();
            }
            else {
                iterator = this.pendingTickListEntriesThisTick.iterator();
            }
            while (iterator.hasNext()) {
                final NextTickListEntry nextticklistentry = iterator.next();
                final BlockPos blockpos = nextticklistentry.position;
                if (blockpos.getX() >= structureBB.minX && blockpos.getX() < structureBB.maxX && blockpos.getZ() >= structureBB.minZ && blockpos.getZ() < structureBB.maxZ) {
                    if (remove) {
                        if (i == 0) {
                            this.pendingTickListEntriesHashSet.remove(nextticklistentry);
                        }
                        iterator.remove();
                    }
                    if (list == null) {
                        list = (List<NextTickListEntry>)Lists.newArrayList();
                    }
                    list.add(nextticklistentry);
                }
            }
        }
        return list;
    }
    
    @Override
    public void updateEntityWithOptionalForce(final Entity entityIn, final boolean forceUpdate) {
        if (!this.canSpawnAnimals() && (entityIn instanceof EntityAnimal || entityIn instanceof EntityWaterMob)) {
            entityIn.setDead();
        }
        if (!this.canSpawnNPCs() && entityIn instanceof INpc) {
            entityIn.setDead();
        }
        super.updateEntityWithOptionalForce(entityIn, forceUpdate);
    }
    
    private boolean canSpawnNPCs() {
        return this.server.getCanSpawnNPCs();
    }
    
    private boolean canSpawnAnimals() {
        return this.server.getCanSpawnAnimals();
    }
    
    @Override
    protected IChunkProvider createChunkProvider() {
        final IChunkLoader ichunkloader = this.saveHandler.getChunkLoader(this.provider);
        return new ChunkProviderServer(this, ichunkloader, this.provider.createChunkGenerator());
    }
    
    @Override
    public boolean isBlockModifiable(final EntityPlayer player, final BlockPos pos) {
        return !this.server.isBlockProtected(this, pos, player) && this.getWorldBorder().contains(pos);
    }
    
    @Override
    public void initialize(final WorldSettings settings) {
        if (!this.worldInfo.isInitialized()) {
            try {
                this.createSpawnPosition(settings);
                if (this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
                    this.setDebugWorldSettings();
                }
                super.initialize(settings);
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception initializing level");
                try {
                    this.addWorldInfoToCrashReport(crashreport);
                }
                catch (Throwable t) {}
                throw new ReportedException(crashreport);
            }
            this.worldInfo.setServerInitialized(true);
        }
    }
    
    private void setDebugWorldSettings() {
        this.worldInfo.setMapFeaturesEnabled(false);
        this.worldInfo.setAllowCommands(true);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThundering(false);
        this.worldInfo.setCleanWeatherTime(1000000000);
        this.worldInfo.setWorldTime(6000L);
        this.worldInfo.setGameType(GameType.SPECTATOR);
        this.worldInfo.setHardcore(false);
        this.worldInfo.setDifficulty(EnumDifficulty.PEACEFUL);
        this.worldInfo.setDifficultyLocked(true);
        this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
    }
    
    private void createSpawnPosition(final WorldSettings settings) {
        if (!this.provider.canRespawnHere()) {
            this.worldInfo.setSpawn(BlockPos.ORIGIN.up(this.provider.getAverageGroundLevel()));
        }
        else if (this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            this.worldInfo.setSpawn(BlockPos.ORIGIN.up());
        }
        else {
            this.findingSpawnPoint = true;
            final BiomeProvider biomeprovider = this.provider.getBiomeProvider();
            final List<Biome> list = biomeprovider.getBiomesToSpawnIn();
            final Random random = new Random(this.getSeed());
            final BlockPos blockpos = biomeprovider.findBiomePosition(0, 0, 256, list, random);
            int i = 8;
            final int j = this.provider.getAverageGroundLevel();
            int k = 8;
            if (blockpos != null) {
                i = blockpos.getX();
                k = blockpos.getZ();
            }
            else {
                WorldServer.LOGGER.warn("Unable to find spawn biome");
            }
            int l = 0;
            while (!this.provider.canCoordinateBeSpawn(i, k)) {
                i += random.nextInt(64) - random.nextInt(64);
                k += random.nextInt(64) - random.nextInt(64);
                if (++l == 1000) {
                    break;
                }
            }
            this.worldInfo.setSpawn(new BlockPos(i, j, k));
            this.findingSpawnPoint = false;
            if (settings.isBonusChestEnabled()) {
                this.createBonusChest();
            }
        }
    }
    
    protected void createBonusChest() {
        final WorldGeneratorBonusChest worldgeneratorbonuschest = new WorldGeneratorBonusChest();
        for (int i = 0; i < 10; ++i) {
            final int j = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
            final int k = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6);
            final BlockPos blockpos = this.getTopSolidOrLiquidBlock(new BlockPos(j, 0, k)).up();
            if (worldgeneratorbonuschest.generate(this, this.rand, blockpos)) {
                break;
            }
        }
    }
    
    @Nullable
    public BlockPos getSpawnCoordinate() {
        return this.provider.getSpawnCoordinate();
    }
    
    public void saveAllChunks(final boolean all, @Nullable final IProgressUpdate progressCallback) throws MinecraftException {
        final ChunkProviderServer chunkproviderserver = this.getChunkProvider();
        if (chunkproviderserver.canSave()) {
            if (progressCallback != null) {
                progressCallback.displaySavingString("Saving level");
            }
            this.saveLevel();
            if (progressCallback != null) {
                progressCallback.displayLoadingString("Saving chunks");
            }
            chunkproviderserver.saveChunks(all);
            for (final Chunk chunk : Lists.newArrayList((Iterable)chunkproviderserver.getLoadedChunks())) {
                if (chunk != null && !this.playerChunkMap.contains(chunk.x, chunk.z)) {
                    chunkproviderserver.queueUnload(chunk);
                }
            }
        }
    }
    
    public void flushToDisk() {
        final ChunkProviderServer chunkproviderserver = this.getChunkProvider();
        if (chunkproviderserver.canSave()) {
            chunkproviderserver.flushToDisk();
        }
    }
    
    protected void saveLevel() throws MinecraftException {
        this.checkSessionLock();
        for (final WorldServer worldserver : this.server.worlds) {
            if (worldserver instanceof WorldServerMulti) {
                ((WorldServerMulti)worldserver).saveAdditionalData();
            }
        }
        this.worldInfo.setBorderSize(this.getWorldBorder().getDiameter());
        this.worldInfo.getBorderCenterX(this.getWorldBorder().getCenterX());
        this.worldInfo.getBorderCenterZ(this.getWorldBorder().getCenterZ());
        this.worldInfo.setBorderSafeZone(this.getWorldBorder().getDamageBuffer());
        this.worldInfo.setBorderDamagePerBlock(this.getWorldBorder().getDamageAmount());
        this.worldInfo.setBorderWarningDistance(this.getWorldBorder().getWarningDistance());
        this.worldInfo.setBorderWarningTime(this.getWorldBorder().getWarningTime());
        this.worldInfo.setBorderLerpTarget(this.getWorldBorder().getTargetSize());
        this.worldInfo.setBorderLerpTime(this.getWorldBorder().getTimeUntilTarget());
        this.saveHandler.saveWorldInfoWithPlayer(this.worldInfo, this.server.getPlayerList().getHostPlayerData());
        this.mapStorage.saveAllData();
    }
    
    @Override
    public boolean spawnEntity(final Entity entityIn) {
        return this.canAddEntity(entityIn) && super.spawnEntity(entityIn);
    }
    
    @Override
    public void loadEntities(final Collection<Entity> entityCollection) {
        for (final Entity entity : Lists.newArrayList((Iterable)entityCollection)) {
            if (this.canAddEntity(entity)) {
                this.loadedEntityList.add(entity);
                this.onEntityAdded(entity);
            }
        }
    }
    
    private boolean canAddEntity(final Entity entityIn) {
        if (entityIn.isDead) {
            WorldServer.LOGGER.warn("Tried to add entity {} but it was marked as removed already", (Object)EntityList.getKey(entityIn));
            return false;
        }
        final UUID uuid = entityIn.getUniqueID();
        if (this.entitiesByUuid.containsKey(uuid)) {
            final Entity entity = this.entitiesByUuid.get(uuid);
            if (this.unloadedEntityList.contains(entity)) {
                this.unloadedEntityList.remove(entity);
            }
            else {
                if (!(entityIn instanceof EntityPlayer)) {
                    WorldServer.LOGGER.warn("Keeping entity {} that already exists with UUID {}", (Object)EntityList.getKey(entity), (Object)uuid.toString());
                    return false;
                }
                WorldServer.LOGGER.warn("Force-added player with duplicate UUID {}", (Object)uuid.toString());
            }
            this.removeEntityDangerously(entity);
        }
        return true;
    }
    
    @Override
    protected void onEntityAdded(final Entity entityIn) {
        super.onEntityAdded(entityIn);
        this.entitiesById.addKey(entityIn.getEntityId(), entityIn);
        this.entitiesByUuid.put(entityIn.getUniqueID(), entityIn);
        final Entity[] aentity = entityIn.getParts();
        if (aentity != null) {
            for (final Entity entity : aentity) {
                this.entitiesById.addKey(entity.getEntityId(), entity);
            }
        }
    }
    
    @Override
    protected void onEntityRemoved(final Entity entityIn) {
        super.onEntityRemoved(entityIn);
        this.entitiesById.removeObject(entityIn.getEntityId());
        this.entitiesByUuid.remove(entityIn.getUniqueID());
        final Entity[] aentity = entityIn.getParts();
        if (aentity != null) {
            for (final Entity entity : aentity) {
                this.entitiesById.removeObject(entity.getEntityId());
            }
        }
    }
    
    @Override
    public boolean addWeatherEffect(final Entity entityIn) {
        if (super.addWeatherEffect(entityIn)) {
            this.server.getPlayerList().sendToAllNearExcept(null, entityIn.posX, entityIn.posY, entityIn.posZ, 512.0, this.provider.getDimensionType().getId(), new SPacketSpawnGlobalEntity(entityIn));
            return true;
        }
        return false;
    }
    
    @Override
    public void setEntityState(final Entity entityIn, final byte state) {
        this.getEntityTracker().sendToTrackingAndSelf(entityIn, new SPacketEntityStatus(entityIn, state));
    }
    
    @Override
    public ChunkProviderServer getChunkProvider() {
        return (ChunkProviderServer)super.getChunkProvider();
    }
    
    @Override
    public Explosion newExplosion(@Nullable final Entity entityIn, final double x, final double y, final double z, final float strength, final boolean causesFire, final boolean damagesTerrain) {
        final Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, causesFire, damagesTerrain);
        explosion.doExplosionA();
        explosion.doExplosionB(false);
        if (!damagesTerrain) {
            explosion.clearAffectedBlockPositions();
        }
        for (final EntityPlayer entityplayer : this.playerEntities) {
            if (entityplayer.getDistanceSq(x, y, z) < 4096.0) {
                ((EntityPlayerMP)entityplayer).connection.sendPacket(new SPacketExplosion(x, y, z, strength, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(entityplayer)));
            }
        }
        return explosion;
    }
    
    @Override
    public void addBlockEvent(final BlockPos pos, final Block blockIn, final int eventID, final int eventParam) {
        final BlockEventData blockeventdata = new BlockEventData(pos, blockIn, eventID, eventParam);
        for (final BlockEventData blockeventdata2 : this.blockEventQueue[this.blockEventCacheIndex]) {
            if (blockeventdata2.equals(blockeventdata)) {
                return;
            }
        }
        this.blockEventQueue[this.blockEventCacheIndex].add(blockeventdata);
    }
    
    private void sendQueuedBlockEvents() {
        while (!this.blockEventQueue[this.blockEventCacheIndex].isEmpty()) {
            final int i = this.blockEventCacheIndex;
            this.blockEventCacheIndex ^= 0x1;
            for (final BlockEventData blockeventdata : this.blockEventQueue[i]) {
                if (this.fireBlockEvent(blockeventdata)) {
                    this.server.getPlayerList().sendToAllNearExcept(null, blockeventdata.getPosition().getX(), blockeventdata.getPosition().getY(), blockeventdata.getPosition().getZ(), 64.0, this.provider.getDimensionType().getId(), new SPacketBlockAction(blockeventdata.getPosition(), blockeventdata.getBlock(), blockeventdata.getEventID(), blockeventdata.getEventParameter()));
                }
            }
            this.blockEventQueue[i].clear();
        }
    }
    
    private boolean fireBlockEvent(final BlockEventData event) {
        final IBlockState iblockstate = this.getBlockState(event.getPosition());
        return iblockstate.getBlock() == event.getBlock() && iblockstate.onBlockEventReceived(this, event.getPosition(), event.getEventID(), event.getEventParameter());
    }
    
    public void flush() {
        this.saveHandler.flush();
    }
    
    @Override
    protected void updateWeather() {
        final boolean flag = this.isRaining();
        super.updateWeather();
        if (this.prevRainingStrength != this.rainingStrength) {
            this.server.getPlayerList().sendPacketToAllPlayersInDimension(new SPacketChangeGameState(7, this.rainingStrength), this.provider.getDimensionType().getId());
        }
        if (this.prevThunderingStrength != this.thunderingStrength) {
            this.server.getPlayerList().sendPacketToAllPlayersInDimension(new SPacketChangeGameState(8, this.thunderingStrength), this.provider.getDimensionType().getId());
        }
        if (flag != this.isRaining()) {
            if (flag) {
                this.server.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(2, 0.0f));
            }
            else {
                this.server.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(1, 0.0f));
            }
            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(7, this.rainingStrength));
            this.server.getPlayerList().sendPacketToAllPlayers(new SPacketChangeGameState(8, this.thunderingStrength));
        }
    }
    
    @Nullable
    @Override
    public MinecraftServer getMinecraftServer() {
        return this.server;
    }
    
    public EntityTracker getEntityTracker() {
        return this.entityTracker;
    }
    
    public PlayerChunkMap getPlayerChunkMap() {
        return this.playerChunkMap;
    }
    
    public Teleporter getDefaultTeleporter() {
        return this.worldTeleporter;
    }
    
    public TemplateManager getStructureTemplateManager() {
        return this.saveHandler.getStructureTemplateManager();
    }
    
    public void spawnParticle(final EnumParticleTypes particleType, final double xCoord, final double yCoord, final double zCoord, final int numberOfParticles, final double xOffset, final double yOffset, final double zOffset, final double particleSpeed, final int... particleArguments) {
        this.spawnParticle(particleType, false, xCoord, yCoord, zCoord, numberOfParticles, xOffset, yOffset, zOffset, particleSpeed, particleArguments);
    }
    
    public void spawnParticle(final EnumParticleTypes particleType, final boolean longDistance, final double xCoord, final double yCoord, final double zCoord, final int numberOfParticles, final double xOffset, final double yOffset, final double zOffset, final double particleSpeed, final int... particleArguments) {
        final SPacketParticles spacketparticles = new SPacketParticles(particleType, longDistance, (float)xCoord, (float)yCoord, (float)zCoord, (float)xOffset, (float)yOffset, (float)zOffset, (float)particleSpeed, numberOfParticles, particleArguments);
        for (int i = 0; i < this.playerEntities.size(); ++i) {
            final EntityPlayerMP entityplayermp = this.playerEntities.get(i);
            this.sendPacketWithinDistance(entityplayermp, longDistance, xCoord, yCoord, zCoord, spacketparticles);
        }
    }
    
    public void spawnParticle(final EntityPlayerMP player, final EnumParticleTypes particle, final boolean longDistance, final double x, final double y, final double z, final int count, final double xOffset, final double yOffset, final double zOffset, final double speed, final int... arguments) {
        final Packet<?> packet = new SPacketParticles(particle, longDistance, (float)x, (float)y, (float)z, (float)xOffset, (float)yOffset, (float)zOffset, (float)speed, count, arguments);
        this.sendPacketWithinDistance(player, longDistance, x, y, z, packet);
    }
    
    private void sendPacketWithinDistance(final EntityPlayerMP player, final boolean longDistance, final double x, final double y, final double z, final Packet<?> packetIn) {
        final BlockPos blockpos = player.getPosition();
        final double d0 = blockpos.distanceSq(x, y, z);
        if (d0 <= 1024.0 || (longDistance && d0 <= 262144.0)) {
            player.connection.sendPacket(packetIn);
        }
    }
    
    @Nullable
    public Entity getEntityFromUuid(final UUID uuid) {
        return this.entitiesByUuid.get(uuid);
    }
    
    @Override
    public ListenableFuture<Object> addScheduledTask(final Runnable runnableToSchedule) {
        return this.server.addScheduledTask(runnableToSchedule);
    }
    
    @Override
    public boolean isCallingFromMinecraftThread() {
        return this.server.isCallingFromMinecraftThread();
    }
    
    @Nullable
    @Override
    public BlockPos findNearestStructure(final String structureName, final BlockPos position, final boolean findUnexplored) {
        return this.getChunkProvider().getNearestStructurePos(this, structureName, position, findUnexplored);
    }
    
    public AdvancementManager getAdvancementManager() {
        return this.advancementManager;
    }
    
    public FunctionManager getFunctionManager() {
        return this.functionManager;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    static class ServerBlockEventList extends ArrayList<BlockEventData>
    {
        private ServerBlockEventList() {
        }
    }
}
