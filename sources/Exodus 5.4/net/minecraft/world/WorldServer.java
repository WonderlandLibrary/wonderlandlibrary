/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  com.google.common.util.concurrent.ListenableFuture
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEventData;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.INpc;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S24PacketBlockAction;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S2APacketParticles;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2CPacketSpawnGlobalEntity;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.ScoreboardSaveData;
import net.minecraft.scoreboard.ServerScoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3i;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.village.VillageCollection;
import net.minecraft.village.VillageSiege;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.feature.WorldGeneratorBonusChest;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorldServer
extends World
implements IThreadListener {
    private int blockEventCacheIndex;
    private final Map<UUID, Entity> entitiesByUuid;
    private List<NextTickListEntry> pendingTickListEntriesThisTick;
    protected final VillageSiege villageSiege;
    private static final List<WeightedRandomChestContent> bonusChestContent;
    private final TreeSet<NextTickListEntry> pendingTickListEntriesTreeSet;
    private final PlayerManager thePlayerManager;
    private final EntityTracker theEntityTracker;
    private static final Logger logger;
    public boolean disableLevelSaving;
    private boolean allPlayersSleeping;
    public ChunkProviderServer theChunkProviderServer;
    private final Teleporter worldTeleporter;
    private final Set<NextTickListEntry> pendingTickListEntriesHashSet = Sets.newHashSet();
    private int updateEntityTick;
    private final SpawnerAnimals mobSpawner;
    private ServerBlockEventList[] field_147490_S;
    private final MinecraftServer mcServer;

    static {
        logger = LogManager.getLogger();
        bonusChestContent = Lists.newArrayList((Object[])new WeightedRandomChestContent[]{new WeightedRandomChestContent(Items.stick, 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.planks), 0, 1, 3, 10), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log), 0, 1, 3, 10), new WeightedRandomChestContent(Items.stone_axe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_axe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.stone_pickaxe, 0, 1, 1, 3), new WeightedRandomChestContent(Items.wooden_pickaxe, 0, 1, 1, 5), new WeightedRandomChestContent(Items.apple, 0, 2, 3, 5), new WeightedRandomChestContent(Items.bread, 0, 2, 3, 3), new WeightedRandomChestContent(Item.getItemFromBlock(Blocks.log2), 0, 1, 3, 10)});
    }

    @Override
    protected void onEntityRemoved(Entity entity) {
        super.onEntityRemoved(entity);
        this.entitiesById.removeObject(entity.getEntityId());
        this.entitiesByUuid.remove(entity.getUniqueID());
        Entity[] entityArray = entity.getParts();
        if (entityArray != null) {
            int n = 0;
            while (n < entityArray.length) {
                this.entitiesById.removeObject(entityArray[n].getEntityId());
                ++n;
            }
        }
    }

    @Override
    public void initialize(WorldSettings worldSettings) {
        if (!this.worldInfo.isInitialized()) {
            try {
                this.createSpawnPosition(worldSettings);
                if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
                    this.setDebugWorldSettings();
                }
                super.initialize(worldSettings);
            }
            catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Exception initializing level");
                try {
                    this.addWorldInfoToCrashReport(crashReport);
                }
                catch (Throwable throwable2) {
                    // empty catch block
                }
                throw new ReportedException(crashReport);
            }
            this.worldInfo.setServerInitialized(true);
        }
    }

    @Override
    protected int getRenderDistanceChunks() {
        return this.mcServer.getConfigurationManager().getViewDistance();
    }

    public void resetUpdateEntityTick() {
        this.updateEntityTick = 0;
    }

    @Override
    public boolean isCallingFromMinecraftThread() {
        return this.mcServer.isCallingFromMinecraftThread();
    }

    private boolean canSpawnNPCs() {
        return this.mcServer.getCanSpawnNPCs();
    }

    @Override
    public void setEntityState(Entity entity, byte by) {
        this.getEntityTracker().func_151248_b(entity, new S19PacketEntityStatus(entity, by));
    }

    @Override
    public Explosion newExplosion(Entity entity, double d, double d2, double d3, float f, boolean bl, boolean bl2) {
        Explosion explosion = new Explosion(this, entity, d, d2, d3, f, bl, bl2);
        explosion.doExplosionA();
        explosion.doExplosionB(false);
        if (!bl2) {
            explosion.func_180342_d();
        }
        for (EntityPlayer entityPlayer : this.playerEntities) {
            if (!(entityPlayer.getDistanceSq(d, d2, d3) < 4096.0)) continue;
            ((EntityPlayerMP)entityPlayer).playerNetServerHandler.sendPacket(new S27PacketExplosion(d, d2, d3, f, explosion.getAffectedBlockPositions(), explosion.getPlayerKnockbackMap().get(entityPlayer)));
        }
        return explosion;
    }

    @Override
    public List<NextTickListEntry> func_175712_a(StructureBoundingBox structureBoundingBox, boolean bl) {
        ArrayList arrayList = null;
        int n = 0;
        while (n < 2) {
            Iterator<NextTickListEntry> iterator = n == 0 ? this.pendingTickListEntriesTreeSet.iterator() : this.pendingTickListEntriesThisTick.iterator();
            while (iterator.hasNext()) {
                NextTickListEntry nextTickListEntry = iterator.next();
                BlockPos blockPos = nextTickListEntry.position;
                if (blockPos.getX() < structureBoundingBox.minX || blockPos.getX() >= structureBoundingBox.maxX || blockPos.getZ() < structureBoundingBox.minZ || blockPos.getZ() >= structureBoundingBox.maxZ) continue;
                if (bl) {
                    this.pendingTickListEntriesHashSet.remove(nextTickListEntry);
                    iterator.remove();
                }
                if (arrayList == null) {
                    arrayList = Lists.newArrayList();
                }
                arrayList.add(nextTickListEntry);
            }
            ++n;
        }
        return arrayList;
    }

    public List<TileEntity> getTileEntitiesIn(int n, int n2, int n3, int n4, int n5, int n6) {
        ArrayList arrayList = Lists.newArrayList();
        int n7 = 0;
        while (n7 < this.loadedTileEntityList.size()) {
            TileEntity tileEntity = (TileEntity)this.loadedTileEntityList.get(n7);
            BlockPos blockPos = tileEntity.getPos();
            if (blockPos.getX() >= n && blockPos.getY() >= n2 && blockPos.getZ() >= n3 && blockPos.getX() < n4 && blockPos.getY() < n5 && blockPos.getZ() < n6) {
                arrayList.add(tileEntity);
            }
            ++n7;
        }
        return arrayList;
    }

    private void setDebugWorldSettings() {
        this.worldInfo.setMapFeaturesEnabled(false);
        this.worldInfo.setAllowCommands(true);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThundering(false);
        this.worldInfo.setCleanWeatherTime(1000000000);
        this.worldInfo.setWorldTime(6000L);
        this.worldInfo.setGameType(WorldSettings.GameType.SPECTATOR);
        this.worldInfo.setHardcore(false);
        this.worldInfo.setDifficulty(EnumDifficulty.PEACEFUL);
        this.worldInfo.setDifficultyLocked(true);
        this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
    }

    public void spawnParticle(EnumParticleTypes enumParticleTypes, double d, double d2, double d3, int n, double d4, double d5, double d6, double d7, int ... nArray) {
        this.spawnParticle(enumParticleTypes, false, d, d2, d3, n, d4, d5, d6, d7, nArray);
    }

    public boolean canCreatureTypeSpawnHere(EnumCreatureType enumCreatureType, BiomeGenBase.SpawnListEntry spawnListEntry, BlockPos blockPos) {
        List<BiomeGenBase.SpawnListEntry> list = this.getChunkProvider().getPossibleCreatures(enumCreatureType, blockPos);
        return list != null && !list.isEmpty() ? list.contains(spawnListEntry) : false;
    }

    @Override
    public void updateAllPlayersSleepingFlag() {
        this.allPlayersSleeping = false;
        if (!this.playerEntities.isEmpty()) {
            int n = 0;
            int n2 = 0;
            for (EntityPlayer entityPlayer : this.playerEntities) {
                if (entityPlayer.isSpectator()) {
                    ++n;
                    continue;
                }
                if (!entityPlayer.isPlayerSleeping()) continue;
                ++n2;
            }
            this.allPlayersSleeping = n2 > 0 && n2 >= this.playerEntities.size() - n;
        }
    }

    @Override
    public void scheduleBlockUpdate(BlockPos blockPos, Block block, int n, int n2) {
        NextTickListEntry nextTickListEntry = new NextTickListEntry(blockPos, block);
        nextTickListEntry.setPriority(n2);
        if (block.getMaterial() != Material.air) {
            nextTickListEntry.setScheduledTime((long)n + this.worldInfo.getWorldTotalTime());
        }
        if (!this.pendingTickListEntriesHashSet.contains(nextTickListEntry)) {
            this.pendingTickListEntriesHashSet.add(nextTickListEntry);
            this.pendingTickListEntriesTreeSet.add(nextTickListEntry);
        }
    }

    public EntityTracker getEntityTracker() {
        return this.theEntityTracker;
    }

    private boolean fireBlockEvent(BlockEventData blockEventData) {
        IBlockState iBlockState = this.getBlockState(blockEventData.getPosition());
        return iBlockState.getBlock() == blockEventData.getBlock() ? iBlockState.getBlock().onBlockEventReceived(this, blockEventData.getPosition(), iBlockState, blockEventData.getEventID(), blockEventData.getEventParameter()) : false;
    }

    @Override
    public ListenableFuture<Object> addScheduledTask(Runnable runnable) {
        return this.mcServer.addScheduledTask(runnable);
    }

    @Override
    public void scheduleUpdate(BlockPos blockPos, Block block, int n) {
        this.updateBlockTick(blockPos, block, n, 0);
    }

    @Override
    public void updateEntities() {
        if (this.playerEntities.isEmpty()) {
            if (this.updateEntityTick++ >= 1200) {
                return;
            }
        } else {
            this.resetUpdateEntityTick();
        }
        super.updateEntities();
    }

    private void createSpawnPosition(WorldSettings worldSettings) {
        if (!this.provider.canRespawnHere()) {
            this.worldInfo.setSpawn(BlockPos.ORIGIN.up(this.provider.getAverageGroundLevel()));
        } else if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            this.worldInfo.setSpawn(BlockPos.ORIGIN.up());
        } else {
            this.findingSpawnPoint = true;
            WorldChunkManager worldChunkManager = this.provider.getWorldChunkManager();
            List<BiomeGenBase> list = worldChunkManager.getBiomesToSpawnIn();
            Random random = new Random(this.getSeed());
            BlockPos blockPos = worldChunkManager.findBiomePosition(0, 0, 256, list, random);
            int n = 0;
            int n2 = this.provider.getAverageGroundLevel();
            int n3 = 0;
            if (blockPos != null) {
                n = blockPos.getX();
                n3 = blockPos.getZ();
            } else {
                logger.warn("Unable to find spawn biome");
            }
            int n4 = 0;
            while (!this.provider.canCoordinateBeSpawn(n, n3)) {
                n += random.nextInt(64) - random.nextInt(64);
                n3 += random.nextInt(64) - random.nextInt(64);
                if (++n4 == 1000) break;
            }
            this.worldInfo.setSpawn(new BlockPos(n, n2, n3));
            this.findingSpawnPoint = false;
            if (worldSettings.isBonusChestEnabled()) {
                this.createBonusChest();
            }
        }
    }

    @Override
    public World init() {
        this.mapStorage = new MapStorage(this.saveHandler);
        String string = VillageCollection.fileNameForProvider(this.provider);
        VillageCollection villageCollection = (VillageCollection)this.mapStorage.loadData(VillageCollection.class, string);
        if (villageCollection == null) {
            this.villageCollectionObj = new VillageCollection(this);
            this.mapStorage.setData(string, this.villageCollectionObj);
        } else {
            this.villageCollectionObj = villageCollection;
            this.villageCollectionObj.setWorldsForAll(this);
        }
        this.worldScoreboard = new ServerScoreboard(this.mcServer);
        ScoreboardSaveData scoreboardSaveData = (ScoreboardSaveData)this.mapStorage.loadData(ScoreboardSaveData.class, "scoreboard");
        if (scoreboardSaveData == null) {
            scoreboardSaveData = new ScoreboardSaveData();
            this.mapStorage.setData("scoreboard", scoreboardSaveData);
        }
        scoreboardSaveData.setScoreboard(this.worldScoreboard);
        ((ServerScoreboard)this.worldScoreboard).func_96547_a(scoreboardSaveData);
        this.getWorldBorder().setCenter(this.worldInfo.getBorderCenterX(), this.worldInfo.getBorderCenterZ());
        this.getWorldBorder().setDamageAmount(this.worldInfo.getBorderDamagePerBlock());
        this.getWorldBorder().setDamageBuffer(this.worldInfo.getBorderSafeZone());
        this.getWorldBorder().setWarningDistance(this.worldInfo.getBorderWarningDistance());
        this.getWorldBorder().setWarningTime(this.worldInfo.getBorderWarningTime());
        if (this.worldInfo.getBorderLerpTime() > 0L) {
            this.getWorldBorder().setTransition(this.worldInfo.getBorderSize(), this.worldInfo.getBorderLerpTarget(), this.worldInfo.getBorderLerpTime());
        } else {
            this.getWorldBorder().setTransition(this.worldInfo.getBorderSize());
        }
        return this;
    }

    @Override
    public List<NextTickListEntry> getPendingBlockUpdates(Chunk chunk, boolean bl) {
        ChunkCoordIntPair chunkCoordIntPair = chunk.getChunkCoordIntPair();
        int n = (chunkCoordIntPair.chunkXPos << 4) - 2;
        int n2 = n + 16 + 2;
        int n3 = (chunkCoordIntPair.chunkZPos << 4) - 2;
        int n4 = n3 + 16 + 2;
        return this.func_175712_a(new StructureBoundingBox(n, 0, n3, n2, 256, n4), bl);
    }

    @Override
    public void setInitialSpawnLocation() {
        if (this.worldInfo.getSpawnY() <= 0) {
            this.worldInfo.setSpawnY(this.func_181545_F() + 1);
        }
        int n = this.worldInfo.getSpawnX();
        int n2 = this.worldInfo.getSpawnZ();
        int n3 = 0;
        while (this.getGroundAboveSeaLevel(new BlockPos(n, 0, n2)).getMaterial() == Material.air) {
            n += this.rand.nextInt(8) - this.rand.nextInt(8);
            n2 += this.rand.nextInt(8) - this.rand.nextInt(8);
            if (++n3 == 10000) break;
        }
        this.worldInfo.setSpawnX(n);
        this.worldInfo.setSpawnZ(n2);
    }

    public BlockPos getSpawnCoordinate() {
        return this.provider.getSpawnCoordinate();
    }

    @Override
    public boolean tickUpdates(boolean bl) {
        NextTickListEntry nextTickListEntry;
        if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            return false;
        }
        int n = this.pendingTickListEntriesTreeSet.size();
        if (n != this.pendingTickListEntriesHashSet.size()) {
            throw new IllegalStateException("TickNextTick list out of synch");
        }
        if (n > 1000) {
            n = 1000;
        }
        this.theProfiler.startSection("cleaning");
        int n2 = 0;
        while (n2 < n) {
            nextTickListEntry = this.pendingTickListEntriesTreeSet.first();
            if (!bl && nextTickListEntry.scheduledTime > this.worldInfo.getWorldTotalTime()) break;
            this.pendingTickListEntriesTreeSet.remove(nextTickListEntry);
            this.pendingTickListEntriesHashSet.remove(nextTickListEntry);
            this.pendingTickListEntriesThisTick.add(nextTickListEntry);
            ++n2;
        }
        this.theProfiler.endSection();
        this.theProfiler.startSection("ticking");
        Iterator<NextTickListEntry> iterator = this.pendingTickListEntriesThisTick.iterator();
        while (iterator.hasNext()) {
            nextTickListEntry = iterator.next();
            iterator.remove();
            int n3 = 0;
            if (this.isAreaLoaded(nextTickListEntry.position.add(-n3, -n3, -n3), nextTickListEntry.position.add(n3, n3, n3))) {
                IBlockState iBlockState = this.getBlockState(nextTickListEntry.position);
                if (iBlockState.getBlock().getMaterial() == Material.air || !Block.isEqualTo(iBlockState.getBlock(), nextTickListEntry.getBlock())) continue;
                try {
                    iBlockState.getBlock().updateTick(this, nextTickListEntry.position, iBlockState, this.rand);
                    continue;
                }
                catch (Throwable throwable) {
                    CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Exception while ticking a block");
                    CrashReportCategory crashReportCategory = crashReport.makeCategory("Block being ticked");
                    CrashReportCategory.addBlockInfo(crashReportCategory, nextTickListEntry.position, iBlockState);
                    throw new ReportedException(crashReport);
                }
            }
            this.scheduleUpdate(nextTickListEntry.position, nextTickListEntry.getBlock(), 0);
        }
        this.theProfiler.endSection();
        this.pendingTickListEntriesThisTick.clear();
        return !this.pendingTickListEntriesTreeSet.isEmpty();
    }

    @Override
    public void updateBlockTick(BlockPos blockPos, Block block, int n, int n2) {
        NextTickListEntry nextTickListEntry = new NextTickListEntry(blockPos, block);
        int n3 = 0;
        if (this.scheduledUpdatesAreImmediate && block.getMaterial() != Material.air) {
            if (block.requiresUpdates()) {
                IBlockState iBlockState;
                n3 = 8;
                if (this.isAreaLoaded(nextTickListEntry.position.add(-n3, -n3, -n3), nextTickListEntry.position.add(n3, n3, n3)) && (iBlockState = this.getBlockState(nextTickListEntry.position)).getBlock().getMaterial() != Material.air && iBlockState.getBlock() == nextTickListEntry.getBlock()) {
                    iBlockState.getBlock().updateTick(this, nextTickListEntry.position, iBlockState, this.rand);
                }
                return;
            }
            n = 1;
        }
        if (this.isAreaLoaded(blockPos.add(-n3, -n3, -n3), blockPos.add(n3, n3, n3))) {
            if (block.getMaterial() != Material.air) {
                nextTickListEntry.setScheduledTime((long)n + this.worldInfo.getWorldTotalTime());
                nextTickListEntry.setPriority(n2);
            }
            if (!this.pendingTickListEntriesHashSet.contains(nextTickListEntry)) {
                this.pendingTickListEntriesHashSet.add(nextTickListEntry);
                this.pendingTickListEntriesTreeSet.add(nextTickListEntry);
            }
        }
    }

    @Override
    protected void updateWeather() {
        boolean bl = this.isRaining();
        super.updateWeather();
        if (this.prevRainingStrength != this.rainingStrength) {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(7, this.rainingStrength), this.provider.getDimensionId());
        }
        if (this.prevThunderingStrength != this.thunderingStrength) {
            this.mcServer.getConfigurationManager().sendPacketToAllPlayersInDimension(new S2BPacketChangeGameState(8, this.thunderingStrength), this.provider.getDimensionId());
        }
        if (bl != this.isRaining()) {
            if (bl) {
                this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0f));
            } else {
                this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(1, 0.0f));
            }
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, this.rainingStrength));
            this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, this.thunderingStrength));
        }
    }

    protected BlockPos adjustPosToNearbyEntity(BlockPos blockPos) {
        BlockPos blockPos2 = this.getPrecipitationHeight(blockPos);
        AxisAlignedBB axisAlignedBB = new AxisAlignedBB(blockPos2, new BlockPos(blockPos2.getX(), this.getHeight(), blockPos2.getZ())).expand(3.0, 3.0, 3.0);
        List<EntityLivingBase> list = this.getEntitiesWithinAABB(EntityLivingBase.class, axisAlignedBB, new Predicate<EntityLivingBase>(){

            public boolean apply(EntityLivingBase entityLivingBase) {
                return entityLivingBase != null && entityLivingBase.isEntityAlive() && WorldServer.this.canSeeSky(entityLivingBase.getPosition());
            }
        });
        return !list.isEmpty() ? list.get(this.rand.nextInt(list.size())).getPosition() : blockPos2;
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        IChunkLoader iChunkLoader = this.saveHandler.getChunkLoader(this.provider);
        this.theChunkProviderServer = new ChunkProviderServer(this, iChunkLoader, this.provider.createChunkGenerator());
        return this.theChunkProviderServer;
    }

    @Override
    public void updateEntityWithOptionalForce(Entity entity, boolean bl) {
        if (!this.canSpawnAnimals() && (entity instanceof EntityAnimal || entity instanceof EntityWaterMob)) {
            entity.setDead();
        }
        if (!this.canSpawnNPCs() && entity instanceof INpc) {
            entity.setDead();
        }
        super.updateEntityWithOptionalForce(entity, bl);
    }

    public BiomeGenBase.SpawnListEntry getSpawnListEntryForTypeAt(EnumCreatureType enumCreatureType, BlockPos blockPos) {
        List<BiomeGenBase.SpawnListEntry> list = this.getChunkProvider().getPossibleCreatures(enumCreatureType, blockPos);
        return list != null && !list.isEmpty() ? WeightedRandom.getRandomItem(this.rand, list) : null;
    }

    @Override
    protected void updateBlocks() {
        super.updateBlocks();
        if (this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            for (ChunkCoordIntPair chunkCoordIntPair : this.activeChunkSet) {
                this.getChunkFromChunkCoords(chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos).func_150804_b(false);
            }
        } else {
            int n = 0;
            int n2 = 0;
            for (ChunkCoordIntPair chunkCoordIntPair : this.activeChunkSet) {
                Object object;
                int n3;
                int n4 = chunkCoordIntPair.chunkXPos * 16;
                int n5 = chunkCoordIntPair.chunkZPos * 16;
                this.theProfiler.startSection("getChunk");
                Chunk chunk = this.getChunkFromChunkCoords(chunkCoordIntPair.chunkXPos, chunkCoordIntPair.chunkZPos);
                this.playMoodSoundAndCheckLight(n4, n5, chunk);
                this.theProfiler.endStartSection("tickChunk");
                chunk.func_150804_b(false);
                this.theProfiler.endStartSection("thunder");
                if (this.rand.nextInt(100000) == 0 && this.isRaining() && this.isThundering()) {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    n3 = this.updateLCG >> 2;
                    object = this.adjustPosToNearbyEntity(new BlockPos(n4 + (n3 & 0xF), 0, n5 + (n3 >> 8 & 0xF)));
                    if (this.canLightningStrike((BlockPos)object)) {
                        this.addWeatherEffect(new EntityLightningBolt(this, ((Vec3i)object).getX(), ((Vec3i)object).getY(), ((Vec3i)object).getZ()));
                    }
                }
                this.theProfiler.endStartSection("iceandsnow");
                if (this.rand.nextInt(16) == 0) {
                    this.updateLCG = this.updateLCG * 3 + 1013904223;
                    n3 = this.updateLCG >> 2;
                    object = this.getPrecipitationHeight(new BlockPos(n4 + (n3 & 0xF), 0, n5 + (n3 >> 8 & 0xF)));
                    BlockPos blockPos = ((BlockPos)object).down();
                    if (this.canBlockFreezeNoWater(blockPos)) {
                        this.setBlockState(blockPos, Blocks.ice.getDefaultState());
                    }
                    if (this.isRaining() && this.canSnowAt((BlockPos)object, true)) {
                        this.setBlockState((BlockPos)object, Blocks.snow_layer.getDefaultState());
                    }
                    if (this.isRaining() && this.getBiomeGenForCoords(blockPos).canSpawnLightningBolt()) {
                        this.getBlockState(blockPos).getBlock().fillWithRain(this, blockPos);
                    }
                }
                this.theProfiler.endStartSection("tickBlocks");
                n3 = this.getGameRules().getInt("randomTickSpeed");
                if (n3 > 0) {
                    ExtendedBlockStorage[] extendedBlockStorageArray = chunk.getBlockStorageArray();
                    int n6 = extendedBlockStorageArray.length;
                    int n7 = 0;
                    while (n7 < n6) {
                        object = extendedBlockStorageArray[n7];
                        if (object != null && ((ExtendedBlockStorage)object).getNeedsRandomTick()) {
                            int n8 = 0;
                            while (n8 < n3) {
                                this.updateLCG = this.updateLCG * 3 + 1013904223;
                                int n9 = this.updateLCG >> 2;
                                int n10 = n9 & 0xF;
                                int n11 = n9 >> 8 & 0xF;
                                int n12 = n9 >> 16 & 0xF;
                                ++n2;
                                IBlockState iBlockState = ((ExtendedBlockStorage)object).get(n10, n12, n11);
                                Block block = iBlockState.getBlock();
                                if (block.getTickRandomly()) {
                                    ++n;
                                    block.randomTick(this, new BlockPos(n10 + n4, n12 + ((ExtendedBlockStorage)object).getYLocation(), n11 + n5), iBlockState, this.rand);
                                }
                                ++n8;
                            }
                        }
                        ++n7;
                    }
                }
                this.theProfiler.endSection();
            }
        }
    }

    @Override
    public boolean isBlockModifiable(EntityPlayer entityPlayer, BlockPos blockPos) {
        return !this.mcServer.isBlockProtected(this, blockPos, entityPlayer) && this.getWorldBorder().contains(blockPos);
    }

    public void flush() {
        this.saveHandler.flush();
    }

    public boolean areAllPlayersAsleep() {
        if (this.allPlayersSleeping && !this.isRemote) {
            for (EntityPlayer entityPlayer : this.playerEntities) {
                if (!entityPlayer.isSpectator() && entityPlayer.isPlayerFullyAsleep()) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    public Teleporter getDefaultTeleporter() {
        return this.worldTeleporter;
    }

    @Override
    public boolean addWeatherEffect(Entity entity) {
        if (super.addWeatherEffect(entity)) {
            this.mcServer.getConfigurationManager().sendToAllNear(entity.posX, entity.posY, entity.posZ, 512.0, this.provider.getDimensionId(), new S2CPacketSpawnGlobalEntity(entity));
            return true;
        }
        return false;
    }

    public void saveChunkData() {
        if (this.chunkProvider.canSave()) {
            this.chunkProvider.saveExtraData();
        }
    }

    @Override
    public void addBlockEvent(BlockPos blockPos, Block block, int n, int n2) {
        BlockEventData blockEventData = new BlockEventData(blockPos, block, n, n2);
        for (BlockEventData blockEventData2 : this.field_147490_S[this.blockEventCacheIndex]) {
            if (!blockEventData2.equals(blockEventData)) continue;
            return;
        }
        this.field_147490_S[this.blockEventCacheIndex].add(blockEventData);
    }

    public WorldServer(MinecraftServer minecraftServer, ISaveHandler iSaveHandler, WorldInfo worldInfo, int n, Profiler profiler) {
        super(iSaveHandler, worldInfo, WorldProvider.getProviderForDimension(n), profiler, false);
        this.pendingTickListEntriesTreeSet = new TreeSet();
        this.entitiesByUuid = Maps.newHashMap();
        this.mobSpawner = new SpawnerAnimals();
        this.villageSiege = new VillageSiege(this);
        this.field_147490_S = new ServerBlockEventList[]{new ServerBlockEventList(), new ServerBlockEventList()};
        this.pendingTickListEntriesThisTick = Lists.newArrayList();
        this.mcServer = minecraftServer;
        this.theEntityTracker = new EntityTracker(this);
        this.thePlayerManager = new PlayerManager(this);
        this.provider.registerWorld(this);
        this.chunkProvider = this.createChunkProvider();
        this.worldTeleporter = new Teleporter(this);
        this.calculateInitialSkylight();
        this.calculateInitialWeather();
        this.getWorldBorder().setSize(minecraftServer.getMaxWorldSize());
    }

    private boolean canSpawnAnimals() {
        return this.mcServer.getCanSpawnAnimals();
    }

    public PlayerManager getPlayerManager() {
        return this.thePlayerManager;
    }

    public MinecraftServer getMinecraftServer() {
        return this.mcServer;
    }

    protected void createBonusChest() {
        WorldGeneratorBonusChest worldGeneratorBonusChest = new WorldGeneratorBonusChest(bonusChestContent, 10);
        int n = 0;
        while (n < 10) {
            int n2;
            int n3 = this.worldInfo.getSpawnX() + this.rand.nextInt(6) - this.rand.nextInt(6);
            BlockPos blockPos = this.getTopSolidOrLiquidBlock(new BlockPos(n3, 0, n2 = this.worldInfo.getSpawnZ() + this.rand.nextInt(6) - this.rand.nextInt(6))).up();
            if (worldGeneratorBonusChest.generate(this, this.rand, blockPos)) break;
            ++n;
        }
    }

    public void saveAllChunks(boolean bl, IProgressUpdate iProgressUpdate) throws MinecraftException {
        if (this.chunkProvider.canSave()) {
            if (iProgressUpdate != null) {
                iProgressUpdate.displaySavingString("Saving level");
            }
            this.saveLevel();
            if (iProgressUpdate != null) {
                iProgressUpdate.displayLoadingString("Saving chunks");
            }
            this.chunkProvider.saveChunks(bl, iProgressUpdate);
            for (Chunk chunk : Lists.newArrayList(this.theChunkProviderServer.func_152380_a())) {
                if (chunk == null || this.thePlayerManager.hasPlayerInstance(chunk.xPosition, chunk.zPosition)) continue;
                this.theChunkProviderServer.dropChunk(chunk.xPosition, chunk.zPosition);
            }
        }
    }

    private void resetRainAndThunder() {
        this.worldInfo.setRainTime(0);
        this.worldInfo.setRaining(false);
        this.worldInfo.setThunderTime(0);
        this.worldInfo.setThundering(false);
    }

    protected void wakeAllPlayers() {
        this.allPlayersSleeping = false;
        for (EntityPlayer entityPlayer : this.playerEntities) {
            if (!entityPlayer.isPlayerSleeping()) continue;
            entityPlayer.wakeUpPlayer(false, false, true);
        }
        this.resetRainAndThunder();
    }

    public Entity getEntityFromUuid(UUID uUID) {
        return this.entitiesByUuid.get(uUID);
    }

    @Override
    public boolean isBlockTickPending(BlockPos blockPos, Block block) {
        NextTickListEntry nextTickListEntry = new NextTickListEntry(blockPos, block);
        return this.pendingTickListEntriesThisTick.contains(nextTickListEntry);
    }

    protected void saveLevel() throws MinecraftException {
        this.checkSessionLock();
        this.worldInfo.setBorderSize(this.getWorldBorder().getDiameter());
        this.worldInfo.getBorderCenterX(this.getWorldBorder().getCenterX());
        this.worldInfo.getBorderCenterZ(this.getWorldBorder().getCenterZ());
        this.worldInfo.setBorderSafeZone(this.getWorldBorder().getDamageBuffer());
        this.worldInfo.setBorderDamagePerBlock(this.getWorldBorder().getDamageAmount());
        this.worldInfo.setBorderWarningDistance(this.getWorldBorder().getWarningDistance());
        this.worldInfo.setBorderWarningTime(this.getWorldBorder().getWarningTime());
        this.worldInfo.setBorderLerpTarget(this.getWorldBorder().getTargetSize());
        this.worldInfo.setBorderLerpTime(this.getWorldBorder().getTimeUntilTarget());
        this.saveHandler.saveWorldInfoWithPlayer(this.worldInfo, this.mcServer.getConfigurationManager().getHostPlayerData());
        this.mapStorage.saveAllData();
    }

    private void sendQueuedBlockEvents() {
        while (!this.field_147490_S[this.blockEventCacheIndex].isEmpty()) {
            int n = this.blockEventCacheIndex;
            this.blockEventCacheIndex ^= 1;
            for (BlockEventData blockEventData : this.field_147490_S[n]) {
                if (!this.fireBlockEvent(blockEventData)) continue;
                this.mcServer.getConfigurationManager().sendToAllNear(blockEventData.getPosition().getX(), blockEventData.getPosition().getY(), blockEventData.getPosition().getZ(), 64.0, this.provider.getDimensionId(), new S24PacketBlockAction(blockEventData.getPosition(), blockEventData.getBlock(), blockEventData.getEventID(), blockEventData.getEventParameter()));
            }
            this.field_147490_S[n].clear();
        }
    }

    public void spawnParticle(EnumParticleTypes enumParticleTypes, boolean bl, double d, double d2, double d3, int n, double d4, double d5, double d6, double d7, int ... nArray) {
        S2APacketParticles s2APacketParticles = new S2APacketParticles(enumParticleTypes, bl, (float)d, (float)d2, (float)d3, (float)d4, (float)d5, (float)d6, (float)d7, n, nArray);
        int n2 = 0;
        while (n2 < this.playerEntities.size()) {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP)this.playerEntities.get(n2);
            BlockPos blockPos = entityPlayerMP.getPosition();
            double d8 = blockPos.distanceSq(d, d2, d3);
            if (d8 <= 256.0 || bl && d8 <= 65536.0) {
                entityPlayerMP.playerNetServerHandler.sendPacket(s2APacketParticles);
            }
            ++n2;
        }
    }

    @Override
    protected void onEntityAdded(Entity entity) {
        super.onEntityAdded(entity);
        this.entitiesById.addKey(entity.getEntityId(), entity);
        this.entitiesByUuid.put(entity.getUniqueID(), entity);
        Entity[] entityArray = entity.getParts();
        if (entityArray != null) {
            int n = 0;
            while (n < entityArray.length) {
                this.entitiesById.addKey(entityArray[n].getEntityId(), entityArray[n]);
                ++n;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getWorldInfo().isHardcoreModeEnabled() && this.getDifficulty() != EnumDifficulty.HARD) {
            this.getWorldInfo().setDifficulty(EnumDifficulty.HARD);
        }
        this.provider.getWorldChunkManager().cleanupCache();
        if (this.areAllPlayersAsleep()) {
            if (this.getGameRules().getBoolean("doDaylightCycle")) {
                long l = this.worldInfo.getWorldTime() + 24000L;
                this.worldInfo.setWorldTime(l - l % 24000L);
            }
            this.wakeAllPlayers();
        }
        this.theProfiler.startSection("mobSpawner");
        if (this.getGameRules().getBoolean("doMobSpawning") && this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD) {
            this.mobSpawner.findChunksForSpawning(this, this.spawnHostileMobs, this.spawnPeacefulMobs, this.worldInfo.getWorldTotalTime() % 400L == 0L);
        }
        this.theProfiler.endStartSection("chunkSource");
        this.chunkProvider.unloadQueuedChunks();
        int n = this.calculateSkylightSubtracted(1.0f);
        if (n != this.getSkylightSubtracted()) {
            this.setSkylightSubtracted(n);
        }
        this.worldInfo.setWorldTotalTime(this.worldInfo.getWorldTotalTime() + 1L);
        if (this.getGameRules().getBoolean("doDaylightCycle")) {
            this.worldInfo.setWorldTime(this.worldInfo.getWorldTime() + 1L);
        }
        this.theProfiler.endStartSection("tickPending");
        this.tickUpdates(false);
        this.theProfiler.endStartSection("tickBlocks");
        this.updateBlocks();
        this.theProfiler.endStartSection("chunkMap");
        this.thePlayerManager.updatePlayerInstances();
        this.theProfiler.endStartSection("village");
        this.villageCollectionObj.tick();
        this.villageSiege.tick();
        this.theProfiler.endStartSection("portalForcer");
        this.worldTeleporter.removeStalePortalLocations(this.getTotalWorldTime());
        this.theProfiler.endSection();
        this.sendQueuedBlockEvents();
    }

    static class ServerBlockEventList
    extends ArrayList<BlockEventData> {
        private ServerBlockEventList() {
        }
    }
}

