// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.network.Packet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;
import java.util.UUID;
import com.google.common.base.MoreObjects;
import com.google.common.base.Function;
import net.minecraft.entity.EntityLiving;
import com.google.common.base.Predicate;
import net.minecraft.util.EntitySelectors;
import ru.tuskevich.event.events.Event;
import ru.tuskevich.event.EventManager;
import ru.tuskevich.event.events.impl.EventOverlay;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.HUD.NoOverlay;
import ru.tuskevich.Minced;
import net.minecraft.util.math.Vec3i;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import java.util.Iterator;
import net.minecraft.util.ITickable;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.Collection;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.BlockObserver;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.init.Biomes;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReport;
import net.minecraft.world.biome.Biome;
import net.minecraft.util.math.BlockPos;
import com.google.common.collect.Lists;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.scoreboard.Scoreboard;
import java.util.Calendar;
import net.minecraft.profiler.Profiler;
import net.minecraft.advancements.FunctionManager;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.pathfinding.PathWorldListener;
import java.util.Random;
import net.minecraft.util.IntHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import java.util.List;

public abstract class World implements IBlockAccess
{
    private int seaLevel;
    protected boolean scheduledUpdatesAreImmediate;
    public final List<Entity> loadedEntityList;
    protected final List<Entity> unloadedEntityList;
    public final List<TileEntity> loadedTileEntityList;
    public final List<TileEntity> tickableTileEntities;
    private final List<TileEntity> addedTileEntityList;
    private final List<TileEntity> tileEntitiesToBeRemoved;
    public final List<EntityPlayer> playerEntities;
    public final List<Entity> weatherEffects;
    protected final IntHashMap<Entity> entitiesById;
    private final long cloudColour = 16777215L;
    private int skylightSubtracted;
    protected int updateLCG;
    protected final int DIST_HASH_MAGIC = 1013904223;
    protected float prevRainingStrength;
    protected float rainingStrength;
    protected float prevThunderingStrength;
    protected float thunderingStrength;
    private int lastLightningBolt;
    public final Random rand;
    public final WorldProvider provider;
    protected PathWorldListener pathListener;
    protected List<IWorldEventListener> eventListeners;
    protected IChunkProvider chunkProvider;
    protected final ISaveHandler saveHandler;
    protected WorldInfo worldInfo;
    protected boolean findingSpawnPoint;
    protected MapStorage mapStorage;
    protected VillageCollection villageCollection;
    protected LootTableManager lootTable;
    protected AdvancementManager advancementManager;
    protected FunctionManager functionManager;
    public final Profiler profiler;
    private final Calendar calendar;
    protected Scoreboard worldScoreboard;
    public final boolean isRemote;
    protected boolean spawnHostileMobs;
    protected boolean spawnPeacefulMobs;
    private boolean processingLoadedTiles;
    private final WorldBorder worldBorder;
    int[] lightUpdateBlockList;
    
    protected World(final ISaveHandler saveHandlerIn, final WorldInfo info, final WorldProvider providerIn, final Profiler profilerIn, final boolean client) {
        this.seaLevel = 63;
        this.loadedEntityList = (List<Entity>)Lists.newArrayList();
        this.unloadedEntityList = (List<Entity>)Lists.newArrayList();
        this.loadedTileEntityList = (List<TileEntity>)Lists.newArrayList();
        this.tickableTileEntities = (List<TileEntity>)Lists.newArrayList();
        this.addedTileEntityList = (List<TileEntity>)Lists.newArrayList();
        this.tileEntitiesToBeRemoved = (List<TileEntity>)Lists.newArrayList();
        this.playerEntities = (List<EntityPlayer>)Lists.newArrayList();
        this.weatherEffects = (List<Entity>)Lists.newArrayList();
        this.entitiesById = new IntHashMap<Entity>();
        this.updateLCG = new Random().nextInt();
        this.rand = new Random();
        this.pathListener = new PathWorldListener();
        this.eventListeners = (List<IWorldEventListener>)Lists.newArrayList((Object[])new IWorldEventListener[] { this.pathListener });
        this.calendar = Calendar.getInstance();
        this.worldScoreboard = new Scoreboard();
        this.spawnHostileMobs = true;
        this.spawnPeacefulMobs = true;
        this.lightUpdateBlockList = new int[32768];
        this.saveHandler = saveHandlerIn;
        this.profiler = profilerIn;
        this.worldInfo = info;
        this.provider = providerIn;
        this.isRemote = client;
        this.worldBorder = providerIn.createWorldBorder();
    }
    
    public World init() {
        return this;
    }
    
    @Override
    public Biome getBiome(final BlockPos pos) {
        if (this.isBlockLoaded(pos)) {
            final Chunk chunk = this.getChunk(pos);
            try {
                return chunk.getBiome(pos, this.provider.getBiomeProvider());
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting biome");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Coordinates of biome request");
                crashreportcategory.addDetail("Location", new ICrashReportDetail<String>() {
                    @Override
                    public String call() throws Exception {
                        return CrashReportCategory.getCoordinateInfo(pos);
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
        return this.provider.getBiomeProvider().getBiome(pos, Biomes.PLAINS);
    }
    
    public BiomeProvider getBiomeProvider() {
        return this.provider.getBiomeProvider();
    }
    
    protected abstract IChunkProvider createChunkProvider();
    
    public void initialize(final WorldSettings settings) {
        this.worldInfo.setServerInitialized(true);
    }
    
    @Nullable
    public MinecraftServer getMinecraftServer() {
        return null;
    }
    
    public void setInitialSpawnLocation() {
        this.setSpawnPoint(new BlockPos(8, 64, 8));
    }
    
    public IBlockState getGroundAboveSeaLevel(final BlockPos pos) {
        BlockPos blockpos;
        for (blockpos = new BlockPos(pos.getX(), this.getSeaLevel(), pos.getZ()); !this.isAirBlock(blockpos.up()); blockpos = blockpos.up()) {}
        return this.getBlockState(blockpos);
    }
    
    private boolean isValid(final BlockPos pos) {
        return !this.isOutsideBuildHeight(pos) && pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000;
    }
    
    private boolean isOutsideBuildHeight(final BlockPos pos) {
        return pos.getY() < 0 || pos.getY() >= 256;
    }
    
    @Override
    public boolean isAirBlock(final BlockPos pos) {
        return this.getBlockState(pos).getMaterial() == Material.AIR;
    }
    
    public boolean isBlockLoaded(final BlockPos pos) {
        return this.isBlockLoaded(pos, true);
    }
    
    public boolean isBlockLoaded(final BlockPos pos, final boolean allowEmpty) {
        return this.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, allowEmpty);
    }
    
    public boolean isAreaLoaded(final BlockPos center, final int radius) {
        return this.isAreaLoaded(center, radius, true);
    }
    
    public boolean isAreaLoaded(final BlockPos center, final int radius, final boolean allowEmpty) {
        return this.isAreaLoaded(center.getX() - radius, center.getY() - radius, center.getZ() - radius, center.getX() + radius, center.getY() + radius, center.getZ() + radius, allowEmpty);
    }
    
    public boolean isAreaLoaded(final BlockPos from, final BlockPos to) {
        return this.isAreaLoaded(from, to, true);
    }
    
    public boolean isAreaLoaded(final BlockPos from, final BlockPos to, final boolean allowEmpty) {
        return this.isAreaLoaded(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), allowEmpty);
    }
    
    public boolean isAreaLoaded(final StructureBoundingBox box) {
        return this.isAreaLoaded(box, true);
    }
    
    public boolean isAreaLoaded(final StructureBoundingBox box, final boolean allowEmpty) {
        return this.isAreaLoaded(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, allowEmpty);
    }
    
    private boolean isAreaLoaded(int xStart, final int yStart, int zStart, int xEnd, final int yEnd, int zEnd, final boolean allowEmpty) {
        if (yEnd >= 0 && yStart < 256) {
            xStart >>= 4;
            zStart >>= 4;
            xEnd >>= 4;
            zEnd >>= 4;
            for (int i = xStart; i <= xEnd; ++i) {
                for (int j = zStart; j <= zEnd; ++j) {
                    if (!this.isChunkLoaded(i, j, allowEmpty)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    protected abstract boolean isChunkLoaded(final int p0, final int p1, final boolean p2);
    
    public Chunk getChunk(final BlockPos pos) {
        return this.getChunk(pos.getX() >> 4, pos.getZ() >> 4);
    }
    
    public Chunk getChunk(final int chunkX, final int chunkZ) {
        return this.chunkProvider.provideChunk(chunkX, chunkZ);
    }
    
    public boolean isChunkGeneratedAt(final int x, final int z) {
        return this.isChunkLoaded(x, z, false) || this.chunkProvider.isChunkGeneratedAt(x, z);
    }
    
    public boolean setBlockState(final BlockPos pos, final IBlockState newState, final int flags) {
        if (this.isOutsideBuildHeight(pos)) {
            return false;
        }
        if (!this.isRemote && this.worldInfo.getTerrainType() == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return false;
        }
        final Chunk chunk = this.getChunk(pos);
        final Block block = newState.getBlock();
        final IBlockState iblockstate = chunk.setBlockState(pos, newState);
        if (iblockstate == null) {
            return false;
        }
        if (newState.getLightOpacity() != iblockstate.getLightOpacity() || newState.getLightValue() != iblockstate.getLightValue()) {
            this.profiler.startSection("checkLight");
            this.checkLight(pos);
            this.profiler.endSection();
        }
        if ((flags & 0x2) != 0x0 && (!this.isRemote || (flags & 0x4) == 0x0) && chunk.isPopulated()) {
            this.notifyBlockUpdate(pos, iblockstate, newState, flags);
        }
        if (!this.isRemote && (flags & 0x1) != 0x0) {
            this.notifyNeighborsRespectDebug(pos, iblockstate.getBlock(), true);
            if (newState.hasComparatorInputOverride()) {
                this.updateComparatorOutputLevel(pos, block);
            }
        }
        else if (!this.isRemote && (flags & 0x10) == 0x0) {
            this.updateObservingBlocksAt(pos, block);
        }
        return true;
    }
    
    public boolean setBlockToAir(final BlockPos pos) {
        return this.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }
    
    public boolean destroyBlock(final BlockPos pos, final boolean dropBlock) {
        final IBlockState iblockstate = this.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (iblockstate.getMaterial() == Material.AIR) {
            return false;
        }
        this.playEvent(2001, pos, Block.getStateId(iblockstate));
        if (dropBlock) {
            block.dropBlockAsItem(this, pos, iblockstate, 0);
        }
        return this.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }
    
    public boolean setBlockState(final BlockPos pos, final IBlockState state) {
        return this.setBlockState(pos, state, 3);
    }
    
    public void notifyBlockUpdate(final BlockPos pos, final IBlockState oldState, final IBlockState newState, final int flags) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).notifyBlockUpdate(this, pos, oldState, newState, flags);
        }
    }
    
    public void notifyNeighborsRespectDebug(final BlockPos pos, final Block blockType, final boolean updateObservers) {
        if (this.worldInfo.getTerrainType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
            this.notifyNeighborsOfStateChange(pos, blockType, updateObservers);
        }
    }
    
    public void markBlocksDirtyVertical(final int x, final int z, int y1, int y2) {
        if (y1 > y2) {
            final int i = y2;
            y2 = y1;
            y1 = i;
        }
        if (this.provider.hasSkyLight()) {
            for (int j = y1; j <= y2; ++j) {
                this.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x, j, z));
            }
        }
        this.markBlockRangeForRenderUpdate(x, y1, z, x, y2, z);
    }
    
    public void markBlockRangeForRenderUpdate(final BlockPos rangeMin, final BlockPos rangeMax) {
        this.markBlockRangeForRenderUpdate(rangeMin.getX(), rangeMin.getY(), rangeMin.getZ(), rangeMax.getX(), rangeMax.getY(), rangeMax.getZ());
    }
    
    public void markBlockRangeForRenderUpdate(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).markBlockRangeForRenderUpdate(x1, y1, z1, x2, y2, z2);
        }
    }
    
    public void updateObservingBlocksAt(final BlockPos pos, final Block blockType) {
        this.observedNeighborChanged(pos.west(), blockType, pos);
        this.observedNeighborChanged(pos.east(), blockType, pos);
        this.observedNeighborChanged(pos.down(), blockType, pos);
        this.observedNeighborChanged(pos.up(), blockType, pos);
        this.observedNeighborChanged(pos.north(), blockType, pos);
        this.observedNeighborChanged(pos.south(), blockType, pos);
    }
    
    public void notifyNeighborsOfStateChange(final BlockPos pos, final Block blockType, final boolean updateObservers) {
        this.neighborChanged(pos.west(), blockType, pos);
        this.neighborChanged(pos.east(), blockType, pos);
        this.neighborChanged(pos.down(), blockType, pos);
        this.neighborChanged(pos.up(), blockType, pos);
        this.neighborChanged(pos.north(), blockType, pos);
        this.neighborChanged(pos.south(), blockType, pos);
        if (updateObservers) {
            this.updateObservingBlocksAt(pos, blockType);
        }
    }
    
    public void notifyNeighborsOfStateExcept(final BlockPos pos, final Block blockType, final EnumFacing skipSide) {
        if (skipSide != EnumFacing.WEST) {
            this.neighborChanged(pos.west(), blockType, pos);
        }
        if (skipSide != EnumFacing.EAST) {
            this.neighborChanged(pos.east(), blockType, pos);
        }
        if (skipSide != EnumFacing.DOWN) {
            this.neighborChanged(pos.down(), blockType, pos);
        }
        if (skipSide != EnumFacing.UP) {
            this.neighborChanged(pos.up(), blockType, pos);
        }
        if (skipSide != EnumFacing.NORTH) {
            this.neighborChanged(pos.north(), blockType, pos);
        }
        if (skipSide != EnumFacing.SOUTH) {
            this.neighborChanged(pos.south(), blockType, pos);
        }
    }
    
    public void neighborChanged(final BlockPos pos, final Block blockIn, final BlockPos fromPos) {
        if (!this.isRemote) {
            final IBlockState iblockstate = this.getBlockState(pos);
            try {
                iblockstate.neighborChanged(this, pos, blockIn, fromPos);
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
                crashreportcategory.addDetail("Source block type", new ICrashReportDetail<String>() {
                    @Override
                    public String call() throws Exception {
                        try {
                            return String.format("ID #%d (%s // %s)", Block.getIdFromBlock(blockIn), blockIn.getTranslationKey(), blockIn.getClass().getCanonicalName());
                        }
                        catch (Throwable var2) {
                            return "ID #" + Block.getIdFromBlock(blockIn);
                        }
                    }
                });
                CrashReportCategory.addBlockInfo(crashreportcategory, pos, iblockstate);
                throw new ReportedException(crashreport);
            }
        }
    }
    
    public void observedNeighborChanged(final BlockPos pos, final Block changedBlock, final BlockPos changedBlockPos) {
        if (!this.isRemote) {
            final IBlockState iblockstate = this.getBlockState(pos);
            if (iblockstate.getBlock() == Blocks.OBSERVER) {
                try {
                    ((BlockObserver)iblockstate.getBlock()).observedNeighborChanged(iblockstate, this, pos, changedBlock, changedBlockPos);
                }
                catch (Throwable throwable) {
                    final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while updating neighbours");
                    final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
                    crashreportcategory.addDetail("Source block type", new ICrashReportDetail<String>() {
                        @Override
                        public String call() throws Exception {
                            try {
                                return String.format("ID #%d (%s // %s)", Block.getIdFromBlock(changedBlock), changedBlock.getTranslationKey(), changedBlock.getClass().getCanonicalName());
                            }
                            catch (Throwable var2) {
                                return "ID #" + Block.getIdFromBlock(changedBlock);
                            }
                        }
                    });
                    CrashReportCategory.addBlockInfo(crashreportcategory, pos, iblockstate);
                    throw new ReportedException(crashreport);
                }
            }
        }
    }
    
    public boolean isBlockTickPending(final BlockPos pos, final Block blockType) {
        return false;
    }
    
    public boolean canSeeSky(final BlockPos pos) {
        return this.getChunk(pos).canSeeSky(pos);
    }
    
    public boolean canBlockSeeSky(final BlockPos pos) {
        if (pos.getY() >= this.getSeaLevel()) {
            return this.canSeeSky(pos);
        }
        final BlockPos blockpos = new BlockPos(pos.getX(), this.getSeaLevel(), pos.getZ());
        if (!this.canSeeSky(blockpos)) {
            return false;
        }
        for (BlockPos blockpos2 = blockpos.down(); blockpos2.getY() > pos.getY(); blockpos2 = blockpos2.down()) {
            final IBlockState iblockstate = this.getBlockState(blockpos2);
            if (iblockstate.getLightOpacity() > 0 && !iblockstate.getMaterial().isLiquid()) {
                return false;
            }
        }
        return true;
    }
    
    public int getLight(BlockPos pos) {
        if (pos.getY() < 0) {
            return 0;
        }
        if (pos.getY() >= 256) {
            pos = new BlockPos(pos.getX(), 255, pos.getZ());
        }
        return this.getChunk(pos).getLightSubtracted(pos, 0);
    }
    
    public int getLightFromNeighbors(final BlockPos pos) {
        return this.getLight(pos, true);
    }
    
    public int getLight(BlockPos pos, final boolean checkNeighbors) {
        if (pos.getX() < -30000000 || pos.getZ() < -30000000 || pos.getX() >= 30000000 || pos.getZ() >= 30000000) {
            return 15;
        }
        if (checkNeighbors && this.getBlockState(pos).useNeighborBrightness()) {
            int i1 = this.getLight(pos.up(), false);
            final int j = this.getLight(pos.east(), false);
            final int k = this.getLight(pos.west(), false);
            final int l = this.getLight(pos.south(), false);
            final int m = this.getLight(pos.north(), false);
            if (j > i1) {
                i1 = j;
            }
            if (k > i1) {
                i1 = k;
            }
            if (l > i1) {
                i1 = l;
            }
            if (m > i1) {
                i1 = m;
            }
            return i1;
        }
        if (pos.getY() < 0) {
            return 0;
        }
        if (pos.getY() >= 256) {
            pos = new BlockPos(pos.getX(), 255, pos.getZ());
        }
        final Chunk chunk = this.getChunk(pos);
        return chunk.getLightSubtracted(pos, this.skylightSubtracted);
    }
    
    public BlockPos getHeight(final BlockPos pos) {
        return new BlockPos(pos.getX(), this.getHeight(pos.getX(), pos.getZ()), pos.getZ());
    }
    
    public int getHeight(final int x, final int z) {
        int i;
        if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000) {
            if (this.isChunkLoaded(x >> 4, z >> 4, true)) {
                i = this.getChunk(x >> 4, z >> 4).getHeightValue(x & 0xF, z & 0xF);
            }
            else {
                i = 0;
            }
        }
        else {
            i = this.getSeaLevel() + 1;
        }
        return i;
    }
    
    @Deprecated
    public int getChunksLowestHorizon(final int x, final int z) {
        if (x < -30000000 || z < -30000000 || x >= 30000000 || z >= 30000000) {
            return this.getSeaLevel() + 1;
        }
        if (!this.isChunkLoaded(x >> 4, z >> 4, true)) {
            return 0;
        }
        final Chunk chunk = this.getChunk(x >> 4, z >> 4);
        return chunk.getLowestHeight();
    }
    
    public int getLightFromNeighborsFor(final EnumSkyBlock type, BlockPos pos) {
        if (!this.provider.hasSkyLight() && type == EnumSkyBlock.SKY) {
            return 0;
        }
        if (pos.getY() < 0) {
            pos = new BlockPos(pos.getX(), 0, pos.getZ());
        }
        if (!this.isValid(pos)) {
            return type.defaultLightValue;
        }
        if (!this.isBlockLoaded(pos)) {
            return type.defaultLightValue;
        }
        if (this.getBlockState(pos).useNeighborBrightness()) {
            int i1 = this.getLightFor(type, pos.up());
            final int j = this.getLightFor(type, pos.east());
            final int k = this.getLightFor(type, pos.west());
            final int l = this.getLightFor(type, pos.south());
            final int m = this.getLightFor(type, pos.north());
            if (j > i1) {
                i1 = j;
            }
            if (k > i1) {
                i1 = k;
            }
            if (l > i1) {
                i1 = l;
            }
            if (m > i1) {
                i1 = m;
            }
            return i1;
        }
        final Chunk chunk = this.getChunk(pos);
        return chunk.getLightFor(type, pos);
    }
    
    public int getLightFor(final EnumSkyBlock type, BlockPos pos) {
        if (pos.getY() < 0) {
            pos = new BlockPos(pos.getX(), 0, pos.getZ());
        }
        if (!this.isValid(pos)) {
            return type.defaultLightValue;
        }
        if (!this.isBlockLoaded(pos)) {
            return type.defaultLightValue;
        }
        final Chunk chunk = this.getChunk(pos);
        return chunk.getLightFor(type, pos);
    }
    
    public void setLightFor(final EnumSkyBlock type, final BlockPos pos, final int lightValue) {
        if (this.isValid(pos) && this.isBlockLoaded(pos)) {
            final Chunk chunk = this.getChunk(pos);
            chunk.setLightFor(type, pos, lightValue);
            this.notifyLightSet(pos);
        }
    }
    
    public void notifyLightSet(final BlockPos pos) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).notifyLightSet(pos);
        }
    }
    
    @Override
    public int getCombinedLight(final BlockPos pos, final int lightValue) {
        final int i = this.getLightFromNeighborsFor(EnumSkyBlock.SKY, pos);
        int j = this.getLightFromNeighborsFor(EnumSkyBlock.BLOCK, pos);
        if (j < lightValue) {
            j = lightValue;
        }
        return i << 20 | j << 4;
    }
    
    public float getLightBrightness(final BlockPos pos) {
        return this.provider.getLightBrightnessTable()[this.getLightFromNeighbors(pos)];
    }
    
    @Override
    public IBlockState getBlockState(final BlockPos pos) {
        if (this.isOutsideBuildHeight(pos)) {
            return Blocks.AIR.getDefaultState();
        }
        final Chunk chunk = this.getChunk(pos);
        return chunk.getBlockState(pos);
    }
    
    public boolean isDaytime() {
        return this.skylightSubtracted < 4;
    }
    
    @Nullable
    public RayTraceResult rayTraceBlocks(final Vec3d start, final Vec3d end) {
        return this.rayTraceBlocks(start, end, false, false, false);
    }
    
    @Nullable
    public RayTraceResult rayTraceBlocks(final Vec3d start, final Vec3d end, final boolean stopOnLiquid) {
        return this.rayTraceBlocks(start, end, stopOnLiquid, false, false);
    }
    
    @Nullable
    public RayTraceResult rayTraceBlocks(Vec3d vec31, final Vec3d vec32, final boolean stopOnLiquid, final boolean ignoreBlockWithoutBoundingBox, final boolean returnLastUncollidableBlock) {
        if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
            return null;
        }
        if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z)) {
            final int i = MathHelper.floor(vec32.x);
            final int j = MathHelper.floor(vec32.y);
            final int k = MathHelper.floor(vec32.z);
            int l = MathHelper.floor(vec31.x);
            int i2 = MathHelper.floor(vec31.y);
            int j2 = MathHelper.floor(vec31.z);
            BlockPos blockpos = new BlockPos(l, i2, j2);
            final IBlockState iblockstate = this.getBlockState(blockpos);
            final Block block = iblockstate.getBlock();
            if ((!ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox(this, blockpos) != Block.NULL_AABB) && block.canCollideCheck(iblockstate, stopOnLiquid)) {
                final RayTraceResult raytraceresult = iblockstate.collisionRayTrace(this, blockpos, vec31, vec32);
                if (raytraceresult != null) {
                    return raytraceresult;
                }
            }
            RayTraceResult raytraceresult2 = null;
            int k2 = 200;
            while (k2-- >= 0) {
                if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
                    return null;
                }
                if (l == i && i2 == j && j2 == k) {
                    return returnLastUncollidableBlock ? raytraceresult2 : null;
                }
                boolean flag2 = true;
                boolean flag3 = true;
                boolean flag4 = true;
                double d0 = 999.0;
                double d2 = 999.0;
                double d3 = 999.0;
                if (i > l) {
                    d0 = l + 1.0;
                }
                else if (i < l) {
                    d0 = l + 0.0;
                }
                else {
                    flag2 = false;
                }
                if (j > i2) {
                    d2 = i2 + 1.0;
                }
                else if (j < i2) {
                    d2 = i2 + 0.0;
                }
                else {
                    flag3 = false;
                }
                if (k > j2) {
                    d3 = j2 + 1.0;
                }
                else if (k < j2) {
                    d3 = j2 + 0.0;
                }
                else {
                    flag4 = false;
                }
                double d4 = 999.0;
                double d5 = 999.0;
                double d6 = 999.0;
                final double d7 = vec32.x - vec31.x;
                final double d8 = vec32.y - vec31.y;
                final double d9 = vec32.z - vec31.z;
                if (flag2) {
                    d4 = (d0 - vec31.x) / d7;
                }
                if (flag3) {
                    d5 = (d2 - vec31.y) / d8;
                }
                if (flag4) {
                    d6 = (d3 - vec31.z) / d9;
                }
                if (d4 == -0.0) {
                    d4 = -1.0E-4;
                }
                if (d5 == -0.0) {
                    d5 = -1.0E-4;
                }
                if (d6 == -0.0) {
                    d6 = -1.0E-4;
                }
                EnumFacing enumfacing;
                if (d4 < d5 && d4 < d6) {
                    enumfacing = ((i > l) ? EnumFacing.WEST : EnumFacing.EAST);
                    vec31 = new Vec3d(d0, vec31.y + d8 * d4, vec31.z + d9 * d4);
                }
                else if (d5 < d6) {
                    enumfacing = ((j > i2) ? EnumFacing.DOWN : EnumFacing.UP);
                    vec31 = new Vec3d(vec31.x + d7 * d5, d2, vec31.z + d9 * d5);
                }
                else {
                    enumfacing = ((k > j2) ? EnumFacing.NORTH : EnumFacing.SOUTH);
                    vec31 = new Vec3d(vec31.x + d7 * d6, vec31.y + d8 * d6, d3);
                }
                l = MathHelper.floor(vec31.x) - ((enumfacing == EnumFacing.EAST) ? 1 : 0);
                i2 = MathHelper.floor(vec31.y) - ((enumfacing == EnumFacing.UP) ? 1 : 0);
                j2 = MathHelper.floor(vec31.z) - ((enumfacing == EnumFacing.SOUTH) ? 1 : 0);
                blockpos = new BlockPos(l, i2, j2);
                final IBlockState iblockstate2 = this.getBlockState(blockpos);
                final Block block2 = iblockstate2.getBlock();
                if (ignoreBlockWithoutBoundingBox && iblockstate2.getMaterial() != Material.PORTAL && iblockstate2.getCollisionBoundingBox(this, blockpos) == Block.NULL_AABB) {
                    continue;
                }
                if (block2.canCollideCheck(iblockstate2, stopOnLiquid)) {
                    final RayTraceResult raytraceresult3 = iblockstate2.collisionRayTrace(this, blockpos, vec31, vec32);
                    if (raytraceresult3 != null) {
                        return raytraceresult3;
                    }
                    continue;
                }
                else {
                    raytraceresult2 = new RayTraceResult(RayTraceResult.Type.MISS, vec31, enumfacing, blockpos);
                }
            }
            return returnLastUncollidableBlock ? raytraceresult2 : null;
        }
        return null;
    }
    
    public void playSound(@Nullable final EntityPlayer player, final BlockPos pos, final SoundEvent soundIn, final SoundCategory category, final float volume, final float pitch) {
        this.playSound(player, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, soundIn, category, volume, pitch);
    }
    
    public void playSound(@Nullable final EntityPlayer player, final double x, final double y, final double z, final SoundEvent soundIn, final SoundCategory category, final float volume, final float pitch) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).playSoundToAllNearExcept(player, soundIn, category, x, y, z, volume, pitch);
        }
    }
    
    public void playSound(final double x, final double y, final double z, final SoundEvent soundIn, final SoundCategory category, final float volume, final float pitch, final boolean distanceDelay) {
    }
    
    public void playRecord(final BlockPos blockPositionIn, @Nullable final SoundEvent soundEventIn) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).playRecord(soundEventIn, blockPositionIn);
        }
    }
    
    public void spawnParticle(final EnumParticleTypes particleType, final double xCoord, final double yCoord, final double zCoord, final double xSpeed, final double ySpeed, final double zSpeed, final int... parameters) {
        this.spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange(), xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
    }
    
    public void spawnAlwaysVisibleParticle(final int id, final double x, final double y, final double z, final double xSpeed, final double ySpeed, final double zSpeed, final int... parameters) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).spawnParticle(id, false, true, x, y, z, xSpeed, ySpeed, zSpeed, parameters);
        }
    }
    
    public void spawnParticle(final EnumParticleTypes particleType, final boolean ignoreRange, final double xCoord, final double yCoord, final double zCoord, final double xSpeed, final double ySpeed, final double zSpeed, final int... parameters) {
        this.spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange() || ignoreRange, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
    }
    
    private void spawnParticle(final int particleID, final boolean ignoreRange, final double xCood, final double yCoord, final double zCoord, final double xSpeed, final double ySpeed, final double zSpeed, final int... parameters) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).spawnParticle(particleID, ignoreRange, xCood, yCoord, zCoord, xSpeed, ySpeed, zSpeed, parameters);
        }
    }
    
    public boolean addWeatherEffect(final Entity entityIn) {
        this.weatherEffects.add(entityIn);
        return true;
    }
    
    public boolean spawnEntity(final Entity entityIn) {
        final int i = MathHelper.floor(entityIn.posX / 16.0);
        final int j = MathHelper.floor(entityIn.posZ / 16.0);
        boolean flag = entityIn.forceSpawn;
        if (entityIn instanceof EntityPlayer) {
            flag = true;
        }
        if (!flag && !this.isChunkLoaded(i, j, false)) {
            return false;
        }
        if (entityIn instanceof EntityPlayer) {
            final EntityPlayer entityplayer = (EntityPlayer)entityIn;
            this.playerEntities.add(entityplayer);
            this.updateAllPlayersSleepingFlag();
        }
        this.getChunk(i, j).addEntity(entityIn);
        this.loadedEntityList.add(entityIn);
        this.onEntityAdded(entityIn);
        return true;
    }
    
    protected void onEntityAdded(final Entity entityIn) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).onEntityAdded(entityIn);
        }
    }
    
    protected void onEntityRemoved(final Entity entityIn) {
        for (int i = 0; i < this.eventListeners.size(); ++i) {
            this.eventListeners.get(i).onEntityRemoved(entityIn);
        }
    }
    
    public void removeEntity(final Entity entityIn) {
        if (entityIn.isBeingRidden()) {
            entityIn.removePassengers();
        }
        if (entityIn.isRiding()) {
            entityIn.dismountRidingEntity();
        }
        entityIn.setDead();
        if (entityIn instanceof EntityPlayer) {
            this.playerEntities.remove(entityIn);
            this.updateAllPlayersSleepingFlag();
            this.onEntityRemoved(entityIn);
        }
    }
    
    public void removeEntityDangerously(final Entity entityIn) {
        entityIn.setDropItemsWhenDead(false);
        entityIn.setDead();
        if (entityIn instanceof EntityPlayer) {
            this.playerEntities.remove(entityIn);
            this.updateAllPlayersSleepingFlag();
        }
        final int i = entityIn.chunkCoordX;
        final int j = entityIn.chunkCoordZ;
        if (entityIn.addedToChunk && this.isChunkLoaded(i, j, true)) {
            this.getChunk(i, j).removeEntity(entityIn);
        }
        this.loadedEntityList.remove(entityIn);
        this.onEntityRemoved(entityIn);
    }
    
    public void addEventListener(final IWorldEventListener listener) {
        this.eventListeners.add(listener);
    }
    
    public void removeEventListener(final IWorldEventListener listener) {
        this.eventListeners.remove(listener);
    }
    
    private boolean getCollisionBoxes(@Nullable final Entity entityIn, final AxisAlignedBB aabb, final boolean p_191504_3_, @Nullable final List<AxisAlignedBB> outList) {
        final int i = MathHelper.floor(aabb.minX) - 1;
        final int j = MathHelper.ceil(aabb.maxX) + 1;
        final int k = MathHelper.floor(aabb.minY) - 1;
        final int l = MathHelper.ceil(aabb.maxY) + 1;
        final int i2 = MathHelper.floor(aabb.minZ) - 1;
        final int j2 = MathHelper.ceil(aabb.maxZ) + 1;
        final WorldBorder worldborder = this.getWorldBorder();
        final boolean flag = entityIn != null && entityIn.isOutsideBorder();
        final boolean flag2 = entityIn != null && this.isInsideWorldBorder(entityIn);
        final IBlockState iblockstate = Blocks.STONE.getDefaultState();
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        try {
            for (int k2 = i; k2 < j; ++k2) {
                for (int l2 = i2; l2 < j2; ++l2) {
                    final boolean flag3 = k2 == i || k2 == j - 1;
                    final boolean flag4 = l2 == i2 || l2 == j2 - 1;
                    if ((!flag3 || !flag4) && this.isBlockLoaded(blockpos$pooledmutableblockpos.setPos(k2, 64, l2))) {
                        for (int i3 = k; i3 < l; ++i3) {
                            if ((!flag3 && !flag4) || i3 != l - 1) {
                                if (p_191504_3_) {
                                    if (k2 < -30000000 || k2 >= 30000000 || l2 < -30000000 || l2 >= 30000000) {
                                        final boolean lvt_21_2_ = true;
                                        return lvt_21_2_;
                                    }
                                }
                                else if (entityIn != null && flag == flag2) {
                                    entityIn.setOutsideBorder(!flag2);
                                }
                                blockpos$pooledmutableblockpos.setPos(k2, i3, l2);
                                IBlockState iblockstate2;
                                if (!p_191504_3_ && !worldborder.contains(blockpos$pooledmutableblockpos) && flag2) {
                                    iblockstate2 = iblockstate;
                                }
                                else {
                                    iblockstate2 = this.getBlockState(blockpos$pooledmutableblockpos);
                                }
                                iblockstate2.addCollisionBoxToList(this, blockpos$pooledmutableblockpos, aabb, outList, entityIn, false);
                                if (p_191504_3_ && !outList.isEmpty()) {
                                    final boolean flag5 = true;
                                    return flag5;
                                }
                            }
                        }
                    }
                }
            }
        }
        finally {
            blockpos$pooledmutableblockpos.release();
        }
        return !outList.isEmpty();
    }
    
    public List<AxisAlignedBB> getCollisionBoxes2(@Nullable final Entity entityIn, final AxisAlignedBB aabb) {
        final List<AxisAlignedBB> list = (List<AxisAlignedBB>)Lists.newArrayList();
        this.getCollisionBoxes(entityIn, aabb, false, list);
        return list;
    }
    
    public List<AxisAlignedBB> getCollisionBoxes(@Nullable final Entity entityIn, final AxisAlignedBB aabb) {
        final List<AxisAlignedBB> list = (List<AxisAlignedBB>)Lists.newArrayList();
        this.getCollisionBoxes(entityIn, aabb, false, list);
        if (entityIn != null) {
            final List<Entity> list2 = this.getEntitiesWithinAABBExcludingEntity(entityIn, aabb.grow(0.25));
            for (int i = 0; i < list2.size(); ++i) {
                final Entity entity = list2.get(i);
                if (!entityIn.isRidingSameEntity(entity)) {
                    AxisAlignedBB axisalignedbb = entity.getCollisionBoundingBox();
                    if (axisalignedbb != null && axisalignedbb.intersects(aabb)) {
                        list.add(axisalignedbb);
                    }
                    axisalignedbb = entityIn.getCollisionBox(entity);
                    if (axisalignedbb != null && axisalignedbb.intersects(aabb)) {
                        list.add(axisalignedbb);
                    }
                }
            }
        }
        return list;
    }
    
    public boolean isInsideWorldBorder(final Entity entityToCheck) {
        double d0 = this.worldBorder.minX();
        double d2 = this.worldBorder.minZ();
        double d3 = this.worldBorder.maxX();
        double d4 = this.worldBorder.maxZ();
        if (entityToCheck.isOutsideBorder()) {
            ++d0;
            ++d2;
            --d3;
            --d4;
        }
        else {
            --d0;
            --d2;
            ++d3;
            ++d4;
        }
        return entityToCheck.posX > d0 && entityToCheck.posX < d3 && entityToCheck.posZ > d2 && entityToCheck.posZ < d4;
    }
    
    public boolean collidesWithAnyBlock(final AxisAlignedBB bbox) {
        return this.getCollisionBoxes(null, bbox, true, Lists.newArrayList());
    }
    
    public int calculateSkylightSubtracted(final float partialTicks) {
        final float f = this.getCelestialAngle(partialTicks);
        float f2 = 1.0f - (MathHelper.cos(f * 6.2831855f) * 2.0f + 0.5f);
        f2 = MathHelper.clamp(f2, 0.0f, 1.0f);
        f2 = 1.0f - f2;
        f2 *= (float)(1.0 - this.getRainStrength(partialTicks) * 5.0f / 16.0);
        f2 *= (float)(1.0 - this.getThunderStrength(partialTicks) * 5.0f / 16.0);
        f2 = 1.0f - f2;
        return (int)(f2 * 11.0f);
    }
    
    public float getSunBrightness(final float partialTicks) {
        final float f = this.getCelestialAngle(partialTicks);
        float f2 = 1.0f - (MathHelper.cos(f * 6.2831855f) * 2.0f + 0.2f);
        f2 = MathHelper.clamp(f2, 0.0f, 1.0f);
        f2 = 1.0f - f2;
        f2 *= (float)(1.0 - this.getRainStrength(partialTicks) * 5.0f / 16.0);
        f2 *= (float)(1.0 - this.getThunderStrength(partialTicks) * 5.0f / 16.0);
        return f2 * 0.8f + 0.2f;
    }
    
    public Vec3d getSkyColor(final Entity entityIn, final float partialTicks) {
        final float f = this.getCelestialAngle(partialTicks);
        float f2 = MathHelper.cos(f * 6.2831855f) * 2.0f + 0.5f;
        f2 = MathHelper.clamp(f2, 0.0f, 1.0f);
        final int i = MathHelper.floor(entityIn.posX);
        final int j = MathHelper.floor(entityIn.posY);
        final int k = MathHelper.floor(entityIn.posZ);
        final BlockPos blockpos = new BlockPos(i, j, k);
        final Biome biome = this.getBiome(blockpos);
        final float f3 = biome.getTemperature(blockpos);
        final int l = biome.getSkyColorByTemp(f3);
        float f4 = (l >> 16 & 0xFF) / 255.0f;
        float f5 = (l >> 8 & 0xFF) / 255.0f;
        float f6 = (l & 0xFF) / 255.0f;
        f4 *= f2;
        f5 *= f2;
        f6 *= f2;
        final float f7 = this.getRainStrength(partialTicks);
        if (f7 > 0.0f) {
            final float f8 = (f4 * 0.3f + f5 * 0.59f + f6 * 0.11f) * 0.6f;
            final float f9 = 1.0f - f7 * 0.75f;
            f4 = f4 * f9 + f8 * (1.0f - f9);
            f5 = f5 * f9 + f8 * (1.0f - f9);
            f6 = f6 * f9 + f8 * (1.0f - f9);
        }
        final float f10 = this.getThunderStrength(partialTicks);
        if (f10 > 0.0f) {
            final float f11 = (f4 * 0.3f + f5 * 0.59f + f6 * 0.11f) * 0.2f;
            final float f12 = 1.0f - f10 * 0.75f;
            f4 = f4 * f12 + f11 * (1.0f - f12);
            f5 = f5 * f12 + f11 * (1.0f - f12);
            f6 = f6 * f12 + f11 * (1.0f - f12);
        }
        if (this.lastLightningBolt > 0) {
            float f13 = this.lastLightningBolt - partialTicks;
            if (f13 > 1.0f) {
                f13 = 1.0f;
            }
            f13 *= 0.45f;
            f4 = f4 * (1.0f - f13) + 0.8f * f13;
            f5 = f5 * (1.0f - f13) + 0.8f * f13;
            f6 = f6 * (1.0f - f13) + 1.0f * f13;
        }
        return new Vec3d(f4, f5, f6);
    }
    
    public float getCelestialAngle(final float partialTicks) {
        return this.provider.calculateCelestialAngle(this.worldInfo.getWorldTime(), partialTicks);
    }
    
    public int getMoonPhase() {
        return this.provider.getMoonPhase(this.worldInfo.getWorldTime());
    }
    
    public float getCurrentMoonPhaseFactor() {
        return WorldProvider.MOON_PHASE_FACTORS[this.provider.getMoonPhase(this.worldInfo.getWorldTime())];
    }
    
    public float getCelestialAngleRadians(final float partialTicks) {
        final float f = this.getCelestialAngle(partialTicks);
        return f * 6.2831855f;
    }
    
    public Vec3d getCloudColour(final float partialTicks) {
        final float f = this.getCelestialAngle(partialTicks);
        float f2 = MathHelper.cos(f * 6.2831855f) * 2.0f + 0.5f;
        f2 = MathHelper.clamp(f2, 0.0f, 1.0f);
        float f3 = 1.0f;
        float f4 = 1.0f;
        float f5 = 1.0f;
        final float f6 = this.getRainStrength(partialTicks);
        if (f6 > 0.0f) {
            final float f7 = (f3 * 0.3f + f4 * 0.59f + f5 * 0.11f) * 0.6f;
            final float f8 = 1.0f - f6 * 0.95f;
            f3 = f3 * f8 + f7 * (1.0f - f8);
            f4 = f4 * f8 + f7 * (1.0f - f8);
            f5 = f5 * f8 + f7 * (1.0f - f8);
        }
        f3 *= f2 * 0.9f + 0.1f;
        f4 *= f2 * 0.9f + 0.1f;
        f5 *= f2 * 0.85f + 0.15f;
        final float f9 = this.getThunderStrength(partialTicks);
        if (f9 > 0.0f) {
            final float f10 = (f3 * 0.3f + f4 * 0.59f + f5 * 0.11f) * 0.2f;
            final float f11 = 1.0f - f9 * 0.95f;
            f3 = f3 * f11 + f10 * (1.0f - f11);
            f4 = f4 * f11 + f10 * (1.0f - f11);
            f5 = f5 * f11 + f10 * (1.0f - f11);
        }
        return new Vec3d(f3, f4, f5);
    }
    
    public Vec3d getFogColor(final float partialTicks) {
        final float f = this.getCelestialAngle(partialTicks);
        return this.provider.getFogColor(f, partialTicks);
    }
    
    public BlockPos getPrecipitationHeight(final BlockPos pos) {
        return this.getChunk(pos).getPrecipitationHeight(pos);
    }
    
    public BlockPos getTopSolidOrLiquidBlock(final BlockPos pos) {
        final Chunk chunk = this.getChunk(pos);
        BlockPos blockpos;
        BlockPos blockpos2;
        for (blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos.getY() >= 0; blockpos = blockpos2) {
            blockpos2 = blockpos.down();
            final Material material = chunk.getBlockState(blockpos2).getMaterial();
            if (material.blocksMovement() && material != Material.LEAVES) {
                break;
            }
        }
        return blockpos;
    }
    
    public float getStarBrightness(final float partialTicks) {
        final float f = this.getCelestialAngle(partialTicks);
        float f2 = 1.0f - (MathHelper.cos(f * 6.2831855f) * 2.0f + 0.25f);
        f2 = MathHelper.clamp(f2, 0.0f, 1.0f);
        return f2 * f2 * 0.5f;
    }
    
    public boolean isUpdateScheduled(final BlockPos pos, final Block blk) {
        return true;
    }
    
    public void scheduleUpdate(final BlockPos pos, final Block blockIn, final int delay) {
    }
    
    public void updateBlockTick(final BlockPos pos, final Block blockIn, final int delay, final int priority) {
    }
    
    public void scheduleBlockUpdate(final BlockPos pos, final Block blockIn, final int delay, final int priority) {
    }
    
    public void updateEntities() {
        this.profiler.startSection("entities");
        this.profiler.startSection("global");
        for (int i = 0; i < this.weatherEffects.size(); ++i) {
            final Entity entity = this.weatherEffects.get(i);
            try {
                final Entity entity5 = entity;
                ++entity5.ticksExisted;
                entity.onUpdate();
            }
            catch (Throwable throwable2) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable2, "Ticking entity");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being ticked");
                if (entity == null) {
                    crashreportcategory.addCrashSection("Entity", "~~NULL~~");
                }
                else {
                    entity.addEntityCrashInfo(crashreportcategory);
                }
                throw new ReportedException(crashreport);
            }
            if (entity.isDead) {
                this.weatherEffects.remove(i--);
            }
        }
        this.profiler.endStartSection("remove");
        this.loadedEntityList.removeAll(this.unloadedEntityList);
        for (int k = 0; k < this.unloadedEntityList.size(); ++k) {
            final Entity entity2 = this.unloadedEntityList.get(k);
            final int j = entity2.chunkCoordX;
            final int k2 = entity2.chunkCoordZ;
            if (entity2.addedToChunk && this.isChunkLoaded(j, k2, true)) {
                this.getChunk(j, k2).removeEntity(entity2);
            }
        }
        for (int l = 0; l < this.unloadedEntityList.size(); ++l) {
            this.onEntityRemoved(this.unloadedEntityList.get(l));
        }
        this.unloadedEntityList.clear();
        this.tickPlayers();
        this.profiler.endStartSection("regular");
        for (int i2 = 0; i2 < this.loadedEntityList.size(); ++i2) {
            final Entity entity3 = this.loadedEntityList.get(i2);
            final Entity entity4 = entity3.getRidingEntity();
            if (entity4 != null) {
                if (!entity4.isDead && entity4.isPassenger(entity3)) {
                    continue;
                }
                entity3.dismountRidingEntity();
            }
            this.profiler.startSection("tick");
            if (!entity3.isDead && !(entity3 instanceof EntityPlayerMP)) {
                try {
                    this.updateEntity(entity3);
                }
                catch (Throwable throwable3) {
                    final CrashReport crashreport2 = CrashReport.makeCrashReport(throwable3, "Ticking entity");
                    final CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Entity being ticked");
                    entity3.addEntityCrashInfo(crashreportcategory2);
                    throw new ReportedException(crashreport2);
                }
            }
            this.profiler.endSection();
            this.profiler.startSection("remove");
            if (entity3.isDead) {
                final int l2 = entity3.chunkCoordX;
                final int i3 = entity3.chunkCoordZ;
                if (entity3.addedToChunk && this.isChunkLoaded(l2, i3, true)) {
                    this.getChunk(l2, i3).removeEntity(entity3);
                }
                this.loadedEntityList.remove(i2--);
                this.onEntityRemoved(entity3);
            }
            this.profiler.endSection();
        }
        this.profiler.endStartSection("blockEntities");
        if (!this.tileEntitiesToBeRemoved.isEmpty()) {
            this.tickableTileEntities.removeAll(this.tileEntitiesToBeRemoved);
            this.loadedTileEntityList.removeAll(this.tileEntitiesToBeRemoved);
            this.tileEntitiesToBeRemoved.clear();
        }
        this.processingLoadedTiles = true;
        final Iterator<TileEntity> iterator = this.tickableTileEntities.iterator();
        while (iterator.hasNext()) {
            final TileEntity tileentity = iterator.next();
            if (!tileentity.isInvalid() && tileentity.hasWorld()) {
                final BlockPos blockpos = tileentity.getPos();
                if (this.isBlockLoaded(blockpos) && this.worldBorder.contains(blockpos)) {
                    try {
                        this.profiler.func_194340_a(() -> String.valueOf(TileEntity.getKey(tileentity.getClass())));
                        ((ITickable)tileentity).update();
                        this.profiler.endSection();
                    }
                    catch (Throwable throwable4) {
                        final CrashReport crashreport3 = CrashReport.makeCrashReport(throwable4, "Ticking block entity");
                        final CrashReportCategory crashreportcategory3 = crashreport3.makeCategory("Block entity being ticked");
                        tileentity.addInfoToCrashReport(crashreportcategory3);
                        throw new ReportedException(crashreport3);
                    }
                }
            }
            if (tileentity.isInvalid()) {
                iterator.remove();
                this.loadedTileEntityList.remove(tileentity);
                if (!this.isBlockLoaded(tileentity.getPos())) {
                    continue;
                }
                this.getChunk(tileentity.getPos()).removeTileEntity(tileentity.getPos());
            }
        }
        this.processingLoadedTiles = false;
        this.profiler.endStartSection("pendingBlockEntities");
        if (!this.addedTileEntityList.isEmpty()) {
            for (int j2 = 0; j2 < this.addedTileEntityList.size(); ++j2) {
                final TileEntity tileentity2 = this.addedTileEntityList.get(j2);
                if (!tileentity2.isInvalid()) {
                    if (!this.loadedTileEntityList.contains(tileentity2)) {
                        this.addTileEntity(tileentity2);
                    }
                    if (this.isBlockLoaded(tileentity2.getPos())) {
                        final Chunk chunk = this.getChunk(tileentity2.getPos());
                        final IBlockState iblockstate = chunk.getBlockState(tileentity2.getPos());
                        chunk.addTileEntity(tileentity2.getPos(), tileentity2);
                        this.notifyBlockUpdate(tileentity2.getPos(), iblockstate, iblockstate, 3);
                    }
                }
            }
            this.addedTileEntityList.clear();
        }
        this.profiler.endSection();
        this.profiler.endSection();
    }
    
    protected void tickPlayers() {
    }
    
    public boolean addTileEntity(final TileEntity tile) {
        final boolean flag = this.loadedTileEntityList.add(tile);
        if (flag && tile instanceof ITickable) {
            this.tickableTileEntities.add(tile);
        }
        if (this.isRemote) {
            final BlockPos blockpos1 = tile.getPos();
            final IBlockState iblockstate1 = this.getBlockState(blockpos1);
            this.notifyBlockUpdate(blockpos1, iblockstate1, iblockstate1, 2);
        }
        return flag;
    }
    
    public void addTileEntities(final Collection<TileEntity> tileEntityCollection) {
        if (this.processingLoadedTiles) {
            this.addedTileEntityList.addAll(tileEntityCollection);
        }
        else {
            for (final TileEntity tileentity2 : tileEntityCollection) {
                this.addTileEntity(tileentity2);
            }
        }
    }
    
    public void updateEntity(final Entity ent) {
        this.updateEntityWithOptionalForce(ent, true);
    }
    
    public void updateEntityWithOptionalForce(final Entity entityIn, final boolean forceUpdate) {
        if (!(entityIn instanceof EntityPlayer)) {
            final int j2 = MathHelper.floor(entityIn.posX);
            final int k2 = MathHelper.floor(entityIn.posZ);
            final int l2 = 32;
            if (forceUpdate && !this.isAreaLoaded(j2 - 32, 0, k2 - 32, j2 + 32, 0, k2 + 32, true)) {
                return;
            }
        }
        entityIn.lastTickPosX = entityIn.posX;
        entityIn.lastTickPosY = entityIn.posY;
        entityIn.lastTickPosZ = entityIn.posZ;
        entityIn.prevRotationYaw = entityIn.rotationYaw;
        entityIn.prevRotationPitch = entityIn.rotationPitch;
        if (forceUpdate && entityIn.addedToChunk) {
            ++entityIn.ticksExisted;
            if (entityIn.isRiding()) {
                entityIn.updateRidden();
            }
            else {
                entityIn.onUpdate();
            }
        }
        this.profiler.startSection("chunkCheck");
        if (Double.isNaN(entityIn.posX) || Double.isInfinite(entityIn.posX)) {
            entityIn.posX = entityIn.lastTickPosX;
        }
        if (Double.isNaN(entityIn.posY) || Double.isInfinite(entityIn.posY)) {
            entityIn.posY = entityIn.lastTickPosY;
        }
        if (Double.isNaN(entityIn.posZ) || Double.isInfinite(entityIn.posZ)) {
            entityIn.posZ = entityIn.lastTickPosZ;
        }
        if (Double.isNaN(entityIn.rotationPitch) || Double.isInfinite(entityIn.rotationPitch)) {
            entityIn.rotationPitch = entityIn.prevRotationPitch;
        }
        if (Double.isNaN(entityIn.rotationYaw) || Double.isInfinite(entityIn.rotationYaw)) {
            entityIn.rotationYaw = entityIn.prevRotationYaw;
        }
        final int i3 = MathHelper.floor(entityIn.posX / 16.0);
        final int j3 = MathHelper.floor(entityIn.posY / 16.0);
        final int k3 = MathHelper.floor(entityIn.posZ / 16.0);
        if (!entityIn.addedToChunk || entityIn.chunkCoordX != i3 || entityIn.chunkCoordY != j3 || entityIn.chunkCoordZ != k3) {
            if (entityIn.addedToChunk && this.isChunkLoaded(entityIn.chunkCoordX, entityIn.chunkCoordZ, true)) {
                this.getChunk(entityIn.chunkCoordX, entityIn.chunkCoordZ).removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
            }
            if (!entityIn.setPositionNonDirty() && !this.isChunkLoaded(i3, k3, true)) {
                entityIn.addedToChunk = false;
            }
            else {
                this.getChunk(i3, k3).addEntity(entityIn);
            }
        }
        this.profiler.endSection();
        if (forceUpdate && entityIn.addedToChunk) {
            for (final Entity entity4 : entityIn.getPassengers()) {
                if (!entity4.isDead && entity4.getRidingEntity() == entityIn) {
                    this.updateEntity(entity4);
                }
                else {
                    entity4.dismountRidingEntity();
                }
            }
        }
    }
    
    public boolean checkNoEntityCollision(final AxisAlignedBB bb) {
        return this.checkNoEntityCollision(bb, null);
    }
    
    public boolean checkNoEntityCollision(final AxisAlignedBB bb, @Nullable final Entity entityIn) {
        final List<Entity> list = this.getEntitiesWithinAABBExcludingEntity(null, bb);
        for (int j2 = 0; j2 < list.size(); ++j2) {
            final Entity entity4 = list.get(j2);
            if (!entity4.isDead && entity4.preventEntitySpawning && entity4 != entityIn && (entityIn == null || entity4.isRidingSameEntity(entityIn))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean checkBlockCollision(final AxisAlignedBB bb) {
        final int j2 = MathHelper.floor(bb.minX);
        final int k2 = MathHelper.ceil(bb.maxX);
        final int l2 = MathHelper.floor(bb.minY);
        final int i3 = MathHelper.ceil(bb.maxY);
        final int j3 = MathHelper.floor(bb.minZ);
        final int k3 = MathHelper.ceil(bb.maxZ);
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        for (int l3 = j2; l3 < k2; ++l3) {
            for (int i4 = l2; i4 < i3; ++i4) {
                for (int j4 = j3; j4 < k3; ++j4) {
                    final IBlockState iblockstate1 = this.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));
                    if (iblockstate1.getMaterial() != Material.AIR) {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }
        }
        blockpos$pooledmutableblockpos.release();
        return false;
    }
    
    public boolean containsAnyLiquid(final AxisAlignedBB bb) {
        final int j2 = MathHelper.floor(bb.minX);
        final int k2 = MathHelper.ceil(bb.maxX);
        final int l2 = MathHelper.floor(bb.minY);
        final int i3 = MathHelper.ceil(bb.maxY);
        final int j3 = MathHelper.floor(bb.minZ);
        final int k3 = MathHelper.ceil(bb.maxZ);
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        for (int l3 = j2; l3 < k2; ++l3) {
            for (int i4 = l2; i4 < i3; ++i4) {
                for (int j4 = j3; j4 < k3; ++j4) {
                    final IBlockState iblockstate1 = this.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4));
                    if (iblockstate1.getMaterial().isLiquid()) {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }
        }
        blockpos$pooledmutableblockpos.release();
        return false;
    }
    
    public boolean isFlammableWithin(final AxisAlignedBB bb) {
        final int j2 = MathHelper.floor(bb.minX);
        final int k2 = MathHelper.ceil(bb.maxX);
        final int l2 = MathHelper.floor(bb.minY);
        final int i3 = MathHelper.ceil(bb.maxY);
        final int j3 = MathHelper.floor(bb.minZ);
        final int k3 = MathHelper.ceil(bb.maxZ);
        if (this.isAreaLoaded(j2, l2, j3, k2, i3, k3, true)) {
            final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
            for (int l3 = j2; l3 < k2; ++l3) {
                for (int i4 = l2; i4 < i3; ++i4) {
                    for (int j4 = j3; j4 < k3; ++j4) {
                        final Block block = this.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4)).getBlock();
                        if (block == Blocks.FIRE || block == Blocks.FLOWING_LAVA || block == Blocks.LAVA) {
                            blockpos$pooledmutableblockpos.release();
                            return true;
                        }
                    }
                }
            }
            blockpos$pooledmutableblockpos.release();
        }
        return false;
    }
    
    public boolean handleMaterialAcceleration(final AxisAlignedBB bb, final Material materialIn, final Entity entityIn) {
        final int j2 = MathHelper.floor(bb.minX);
        final int k2 = MathHelper.ceil(bb.maxX);
        final int l2 = MathHelper.floor(bb.minY);
        final int i3 = MathHelper.ceil(bb.maxY);
        final int j3 = MathHelper.floor(bb.minZ);
        final int k3 = MathHelper.ceil(bb.maxZ);
        if (!this.isAreaLoaded(j2, l2, j3, k2, i3, k3, true)) {
            return false;
        }
        boolean flag = false;
        Vec3d vec3d = Vec3d.ZERO;
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        for (int l3 = j2; l3 < k2; ++l3) {
            for (int i4 = l2; i4 < i3; ++i4) {
                for (int j4 = j3; j4 < k3; ++j4) {
                    blockpos$pooledmutableblockpos.setPos(l3, i4, j4);
                    final IBlockState iblockstate1 = this.getBlockState(blockpos$pooledmutableblockpos);
                    final Block block = iblockstate1.getBlock();
                    if (iblockstate1.getMaterial() == materialIn) {
                        final double d0 = i4 + 1 - BlockLiquid.getLiquidHeightPercent(iblockstate1.getValue((IProperty<Integer>)BlockLiquid.LEVEL));
                        if (i3 >= d0) {
                            flag = true;
                            vec3d = block.modifyAcceleration(this, blockpos$pooledmutableblockpos, entityIn, vec3d);
                        }
                    }
                }
            }
        }
        blockpos$pooledmutableblockpos.release();
        if (vec3d.length() > 0.0 && entityIn.isPushedByWater()) {
            vec3d = vec3d.normalize();
            final double d2 = 0.014;
            entityIn.motionX += vec3d.x * 0.014;
            entityIn.motionY += vec3d.y * 0.014;
            entityIn.motionZ += vec3d.z * 0.014;
        }
        return flag;
    }
    
    public boolean isMaterialInBB(final AxisAlignedBB bb, final Material materialIn) {
        final int j2 = MathHelper.floor(bb.minX);
        final int k2 = MathHelper.ceil(bb.maxX);
        final int l2 = MathHelper.floor(bb.minY);
        final int i3 = MathHelper.ceil(bb.maxY);
        final int j3 = MathHelper.floor(bb.minZ);
        final int k3 = MathHelper.ceil(bb.maxZ);
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        for (int l3 = j2; l3 < k2; ++l3) {
            for (int i4 = l2; i4 < i3; ++i4) {
                for (int j4 = j3; j4 < k3; ++j4) {
                    if (this.getBlockState(blockpos$pooledmutableblockpos.setPos(l3, i4, j4)).getMaterial() == materialIn) {
                        blockpos$pooledmutableblockpos.release();
                        return true;
                    }
                }
            }
        }
        blockpos$pooledmutableblockpos.release();
        return false;
    }
    
    public Explosion createExplosion(@Nullable final Entity entityIn, final double x, final double y, final double z, final float strength, final boolean damagesTerrain) {
        return this.newExplosion(entityIn, x, y, z, strength, false, damagesTerrain);
    }
    
    public Explosion newExplosion(@Nullable final Entity entityIn, final double x, final double y, final double z, final float strength, final boolean causesFire, final boolean damagesTerrain) {
        final Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, causesFire, damagesTerrain);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }
    
    public float getBlockDensity(final Vec3d vec, final AxisAlignedBB bb) {
        final double d0 = 1.0 / ((bb.maxX - bb.minX) * 2.0 + 1.0);
        final double d2 = 1.0 / ((bb.maxY - bb.minY) * 2.0 + 1.0);
        final double d3 = 1.0 / ((bb.maxZ - bb.minZ) * 2.0 + 1.0);
        final double d4 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
        final double d5 = (1.0 - Math.floor(1.0 / d3) * d3) / 2.0;
        if (d0 >= 0.0 && d2 >= 0.0 && d3 >= 0.0) {
            int j2 = 0;
            int k2 = 0;
            for (float f = 0.0f; f <= 1.0f; f += (float)d0) {
                for (float f2 = 0.0f; f2 <= 1.0f; f2 += (float)d2) {
                    for (float f3 = 0.0f; f3 <= 1.0f; f3 += (float)d3) {
                        final double d6 = bb.minX + (bb.maxX - bb.minX) * f;
                        final double d7 = bb.minY + (bb.maxY - bb.minY) * f2;
                        final double d8 = bb.minZ + (bb.maxZ - bb.minZ) * f3;
                        if (this.rayTraceBlocks(new Vec3d(d6 + d4, d7, d8 + d5), vec) == null) {
                            ++j2;
                        }
                        ++k2;
                    }
                }
            }
            return j2 / (float)k2;
        }
        return 0.0f;
    }
    
    public boolean extinguishFire(@Nullable final EntityPlayer player, BlockPos pos, final EnumFacing side) {
        pos = pos.offset(side);
        if (this.getBlockState(pos).getBlock() == Blocks.FIRE) {
            this.playEvent(player, 1009, pos, 0);
            this.setBlockToAir(pos);
            return true;
        }
        return false;
    }
    
    public String getDebugLoadedEntities() {
        return "All: " + this.loadedEntityList.size();
    }
    
    public String getProviderName() {
        return this.chunkProvider.makeString();
    }
    
    @Nullable
    @Override
    public TileEntity getTileEntity(final BlockPos pos) {
        if (this.isOutsideBuildHeight(pos)) {
            return null;
        }
        TileEntity tileentity2 = null;
        if (this.processingLoadedTiles) {
            tileentity2 = this.getPendingTileEntityAt(pos);
        }
        if (tileentity2 == null) {
            tileentity2 = this.getChunk(pos).getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
        }
        if (tileentity2 == null) {
            tileentity2 = this.getPendingTileEntityAt(pos);
        }
        return tileentity2;
    }
    
    @Nullable
    private TileEntity getPendingTileEntityAt(final BlockPos pos) {
        for (int j2 = 0; j2 < this.addedTileEntityList.size(); ++j2) {
            final TileEntity tileentity2 = this.addedTileEntityList.get(j2);
            if (!tileentity2.isInvalid() && tileentity2.getPos().equals(pos)) {
                return tileentity2;
            }
        }
        return null;
    }
    
    public void setTileEntity(final BlockPos pos, @Nullable final TileEntity tileEntityIn) {
        if (!this.isOutsideBuildHeight(pos) && tileEntityIn != null && !tileEntityIn.isInvalid()) {
            if (this.processingLoadedTiles) {
                tileEntityIn.setPos(pos);
                final Iterator<TileEntity> iterator1 = this.addedTileEntityList.iterator();
                while (iterator1.hasNext()) {
                    final TileEntity tileentity2 = iterator1.next();
                    if (tileentity2.getPos().equals(pos)) {
                        tileentity2.invalidate();
                        iterator1.remove();
                    }
                }
                this.addedTileEntityList.add(tileEntityIn);
            }
            else {
                this.getChunk(pos).addTileEntity(pos, tileEntityIn);
                this.addTileEntity(tileEntityIn);
            }
        }
    }
    
    public void removeTileEntity(final BlockPos pos) {
        final TileEntity tileentity2 = this.getTileEntity(pos);
        if (tileentity2 != null && this.processingLoadedTiles) {
            tileentity2.invalidate();
            this.addedTileEntityList.remove(tileentity2);
        }
        else {
            if (tileentity2 != null) {
                this.addedTileEntityList.remove(tileentity2);
                this.loadedTileEntityList.remove(tileentity2);
                this.tickableTileEntities.remove(tileentity2);
            }
            this.getChunk(pos).removeTileEntity(pos);
        }
    }
    
    public void markTileEntityForRemoval(final TileEntity tileEntityIn) {
        this.tileEntitiesToBeRemoved.add(tileEntityIn);
    }
    
    public boolean isBlockFullCube(final BlockPos pos) {
        final AxisAlignedBB axisalignedbb = this.getBlockState(pos).getCollisionBoundingBox(this, pos);
        return axisalignedbb != Block.NULL_AABB && axisalignedbb.getAverageEdgeLength() >= 1.0;
    }
    
    public boolean isBlockNormalCube(final BlockPos pos, final boolean _default) {
        if (this.isOutsideBuildHeight(pos)) {
            return false;
        }
        final Chunk chunk1 = this.chunkProvider.getLoadedChunk(pos.getX() >> 4, pos.getZ() >> 4);
        if (chunk1 != null && !chunk1.isEmpty()) {
            final IBlockState iblockstate1 = this.getBlockState(pos);
            return iblockstate1.getMaterial().isOpaque() && iblockstate1.isFullCube();
        }
        return _default;
    }
    
    public void calculateInitialSkylight() {
        final int j2 = this.calculateSkylightSubtracted(1.0f);
        if (j2 != this.skylightSubtracted) {
            this.skylightSubtracted = j2;
        }
    }
    
    public void setAllowedSpawnTypes(final boolean hostile, final boolean peaceful) {
        this.spawnHostileMobs = hostile;
        this.spawnPeacefulMobs = peaceful;
    }
    
    public void tick() {
        this.updateWeather();
    }
    
    protected void calculateInitialWeather() {
        if (this.worldInfo.isRaining()) {
            this.rainingStrength = 1.0f;
            if (this.worldInfo.isThundering()) {
                this.thunderingStrength = 1.0f;
            }
        }
    }
    
    protected void updateWeather() {
        if (this.provider.hasSkyLight() && !this.isRemote) {
            final boolean flag = this.getGameRules().getBoolean("doWeatherCycle");
            if (flag) {
                int j2 = this.worldInfo.getCleanWeatherTime();
                if (j2 > 0) {
                    --j2;
                    this.worldInfo.setCleanWeatherTime(j2);
                    this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
                    this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
                }
                int k2 = this.worldInfo.getThunderTime();
                if (k2 <= 0) {
                    if (this.worldInfo.isThundering()) {
                        this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
                    }
                    else {
                        this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
                    }
                }
                else {
                    --k2;
                    this.worldInfo.setThunderTime(k2);
                    if (k2 <= 0) {
                        this.worldInfo.setThundering(!this.worldInfo.isThundering());
                    }
                }
                int l2 = this.worldInfo.getRainTime();
                if (l2 <= 0) {
                    if (this.worldInfo.isRaining()) {
                        this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
                    }
                    else {
                        this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
                    }
                }
                else {
                    --l2;
                    this.worldInfo.setRainTime(l2);
                    if (l2 <= 0) {
                        this.worldInfo.setRaining(!this.worldInfo.isRaining());
                    }
                }
            }
            this.prevThunderingStrength = this.thunderingStrength;
            if (this.worldInfo.isThundering()) {
                this.thunderingStrength += (float)0.01;
            }
            else {
                this.thunderingStrength -= (float)0.01;
            }
            this.thunderingStrength = MathHelper.clamp(this.thunderingStrength, 0.0f, 1.0f);
            this.prevRainingStrength = this.rainingStrength;
            if (this.worldInfo.isRaining()) {
                this.rainingStrength += (float)0.01;
            }
            else {
                this.rainingStrength -= (float)0.01;
            }
            this.rainingStrength = MathHelper.clamp(this.rainingStrength, 0.0f, 1.0f);
        }
    }
    
    protected void playMoodSoundAndCheckLight(final int x, final int z, final Chunk chunkIn) {
        chunkIn.enqueueRelightChecks();
    }
    
    protected void updateBlocks() {
    }
    
    public void immediateBlockTick(final BlockPos pos, final IBlockState state, final Random random) {
        this.scheduledUpdatesAreImmediate = true;
        state.getBlock().updateTick(this, pos, state, random);
        this.scheduledUpdatesAreImmediate = false;
    }
    
    public boolean canBlockFreezeWater(final BlockPos pos) {
        return this.canBlockFreeze(pos, false);
    }
    
    public boolean canBlockFreezeNoWater(final BlockPos pos) {
        return this.canBlockFreeze(pos, true);
    }
    
    public boolean canBlockFreeze(final BlockPos pos, final boolean noWaterAdj) {
        final Biome biome = this.getBiome(pos);
        final float f = biome.getTemperature(pos);
        if (f >= 0.15f) {
            return false;
        }
        if (pos.getY() >= 0 && pos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
            final IBlockState iblockstate1 = this.getBlockState(pos);
            final Block block = iblockstate1.getBlock();
            if ((block == Blocks.WATER || block == Blocks.FLOWING_WATER) && iblockstate1.getValue((IProperty<Integer>)BlockLiquid.LEVEL) == 0) {
                if (!noWaterAdj) {
                    return true;
                }
                final boolean flag = this.isWater(pos.west()) && this.isWater(pos.east()) && this.isWater(pos.north()) && this.isWater(pos.south());
                if (!flag) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean isWater(final BlockPos pos) {
        return this.getBlockState(pos).getMaterial() == Material.WATER;
    }
    
    public boolean canSnowAt(final BlockPos pos, final boolean checkLight) {
        final Biome biome = this.getBiome(pos);
        final float f = biome.getTemperature(pos);
        if (f >= 0.15f) {
            return false;
        }
        if (!checkLight) {
            return true;
        }
        if (pos.getY() >= 0 && pos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
            final IBlockState iblockstate1 = this.getBlockState(pos);
            if (iblockstate1.getMaterial() == Material.AIR && Blocks.SNOW_LAYER.canPlaceBlockAt(this, pos)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkLight(final BlockPos pos) {
        boolean flag = false;
        if (this.provider.hasSkyLight()) {
            flag |= this.checkLightFor(EnumSkyBlock.SKY, pos);
        }
        flag |= this.checkLightFor(EnumSkyBlock.BLOCK, pos);
        return flag;
    }
    
    private int getRawLight(final BlockPos pos, final EnumSkyBlock lightType) {
        if (lightType == EnumSkyBlock.SKY && this.canSeeSky(pos)) {
            return 15;
        }
        final IBlockState iblockstate1 = this.getBlockState(pos);
        int j2 = (lightType == EnumSkyBlock.SKY) ? 0 : iblockstate1.getLightValue();
        int k2 = iblockstate1.getLightOpacity();
        if (k2 >= 15 && iblockstate1.getLightValue() > 0) {
            k2 = 1;
        }
        if (k2 < 1) {
            k2 = 1;
        }
        if (k2 >= 15) {
            return 0;
        }
        if (j2 >= 14) {
            return j2;
        }
        final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
        try {
            for (final EnumFacing enumfacing : EnumFacing.values()) {
                blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing);
                final int l2 = this.getLightFor(lightType, blockpos$pooledmutableblockpos) - k2;
                if (l2 > j2) {
                    j2 = l2;
                }
                if (j2 >= 14) {
                    final int i3 = j2;
                    return i3;
                }
            }
            return j2;
        }
        finally {
            blockpos$pooledmutableblockpos.release();
        }
    }
    
    public boolean checkLightFor(final EnumSkyBlock lightType, final BlockPos pos) {
        if (Minced.getInstance().manager.getModule(NoOverlay.class).state) {
            final EventOverlay event = new EventOverlay(EventOverlay.OverlayType.Light);
            EventManager.call(event);
            if (event.isCanceled() && lightType == EnumSkyBlock.SKY) {
                return false;
            }
        }
        if (!this.isAreaLoaded(pos, 17, false)) {
            return false;
        }
        int j2 = 0;
        int k2 = 0;
        this.profiler.startSection("getBrightness");
        final int l2 = this.getLightFor(lightType, pos);
        final int i3 = this.getRawLight(pos, lightType);
        final int j3 = pos.getX();
        final int k3 = pos.getY();
        final int l3 = pos.getZ();
        if (i3 > l2) {
            this.lightUpdateBlockList[k2++] = 133152;
        }
        else if (i3 < l2) {
            this.lightUpdateBlockList[k2++] = (0x20820 | l2 << 18);
            while (j2 < k2) {
                final int i4 = this.lightUpdateBlockList[j2++];
                final int j4 = (i4 & 0x3F) - 32 + j3;
                final int k4 = (i4 >> 6 & 0x3F) - 32 + k3;
                final int l4 = (i4 >> 12 & 0x3F) - 32 + l3;
                final int i5 = i4 >> 18 & 0xF;
                final BlockPos blockpos1 = new BlockPos(j4, k4, l4);
                int j5 = this.getLightFor(lightType, blockpos1);
                if (j5 == i5) {
                    this.setLightFor(lightType, blockpos1, 0);
                    if (i5 <= 0) {
                        continue;
                    }
                    final int k5 = MathHelper.abs(j4 - j3);
                    final int l5 = MathHelper.abs(k4 - k3);
                    final int i6 = MathHelper.abs(l4 - l3);
                    if (k5 + l5 + i6 >= 17) {
                        continue;
                    }
                    final BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain();
                    for (final EnumFacing enumfacing : EnumFacing.values()) {
                        final int j6 = j4 + enumfacing.getXOffset();
                        final int k6 = k4 + enumfacing.getYOffset();
                        final int l6 = l4 + enumfacing.getZOffset();
                        blockpos$pooledmutableblockpos.setPos(j6, k6, l6);
                        final int i7 = Math.max(1, this.getBlockState(blockpos$pooledmutableblockpos).getLightOpacity());
                        j5 = this.getLightFor(lightType, blockpos$pooledmutableblockpos);
                        if (j5 == i5 - i7 && k2 < this.lightUpdateBlockList.length) {
                            this.lightUpdateBlockList[k2++] = (j6 - j3 + 32 | k6 - k3 + 32 << 6 | l6 - l3 + 32 << 12 | i5 - i7 << 18);
                        }
                    }
                    blockpos$pooledmutableblockpos.release();
                }
            }
            j2 = 0;
        }
        this.profiler.endSection();
        this.profiler.startSection("checkedPosition < toCheckCount");
        while (j2 < k2) {
            final int j7 = this.lightUpdateBlockList[j2++];
            final int k7 = (j7 & 0x3F) - 32 + j3;
            final int l7 = (j7 >> 6 & 0x3F) - 32 + k3;
            final int i8 = (j7 >> 12 & 0x3F) - 32 + l3;
            final BlockPos blockpos2 = new BlockPos(k7, l7, i8);
            final int j8 = this.getLightFor(lightType, blockpos2);
            final int k8 = this.getRawLight(blockpos2, lightType);
            if (k8 != j8) {
                this.setLightFor(lightType, blockpos2, k8);
                if (k8 <= j8) {
                    continue;
                }
                final int l8 = Math.abs(k7 - j3);
                final int i9 = Math.abs(l7 - k3);
                final int j9 = Math.abs(i8 - l3);
                final boolean flag = k2 < this.lightUpdateBlockList.length - 6;
                if (l8 + i9 + j9 >= 17 || !flag) {
                    continue;
                }
                if (this.getLightFor(lightType, blockpos2.west()) < k8) {
                    this.lightUpdateBlockList[k2++] = k7 - 1 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                }
                if (this.getLightFor(lightType, blockpos2.east()) < k8) {
                    this.lightUpdateBlockList[k2++] = k7 + 1 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                }
                if (this.getLightFor(lightType, blockpos2.down()) < k8) {
                    this.lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 - 1 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                }
                if (this.getLightFor(lightType, blockpos2.up()) < k8) {
                    this.lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 + 1 - k3 + 32 << 6) + (i8 - l3 + 32 << 12);
                }
                if (this.getLightFor(lightType, blockpos2.north()) < k8) {
                    this.lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 - 1 - l3 + 32 << 12);
                }
                if (this.getLightFor(lightType, blockpos2.south()) >= k8) {
                    continue;
                }
                this.lightUpdateBlockList[k2++] = k7 - j3 + 32 + (l7 - k3 + 32 << 6) + (i8 + 1 - l3 + 32 << 12);
            }
        }
        this.profiler.endSection();
        return true;
    }
    
    public boolean tickUpdates(final boolean runAllPending) {
        return false;
    }
    
    @Nullable
    public List<NextTickListEntry> getPendingBlockUpdates(final Chunk chunkIn, final boolean remove) {
        return null;
    }
    
    @Nullable
    public List<NextTickListEntry> getPendingBlockUpdates(final StructureBoundingBox structureBB, final boolean remove) {
        return null;
    }
    
    public List<Entity> getEntitiesWithinAABBExcludingEntity(@Nullable final Entity entityIn, final AxisAlignedBB bb) {
        return this.getEntitiesInAABBexcluding(entityIn, bb, EntitySelectors.NOT_SPECTATING);
    }
    
    public List<Entity> getEntitiesInAABBexcluding(@Nullable final Entity entityIn, final AxisAlignedBB boundingBox, @Nullable final Predicate<? super Entity> predicate) {
        final List<Entity> list = (List<Entity>)Lists.newArrayList();
        final int j2 = MathHelper.floor((boundingBox.minX - 2.0) / 16.0);
        final int k2 = MathHelper.floor((boundingBox.maxX + 2.0) / 16.0);
        final int l2 = MathHelper.floor((boundingBox.minZ - 2.0) / 16.0);
        final int i3 = MathHelper.floor((boundingBox.maxZ + 2.0) / 16.0);
        for (int j3 = j2; j3 <= k2; ++j3) {
            for (int k3 = l2; k3 <= i3; ++k3) {
                if (this.isChunkLoaded(j3, k3, true)) {
                    this.getChunk(j3, k3).getEntitiesWithinAABBForEntity(entityIn, boundingBox, list, predicate);
                }
            }
        }
        return list;
    }
    
    public <T extends Entity> List<T> getEntities(final Class<? extends T> entityType, final Predicate<? super T> filter) {
        final List<T> list = (List<T>)Lists.newArrayList();
        for (final Entity entity4 : this.loadedEntityList) {
            if (entityType.isAssignableFrom(entity4.getClass()) && filter.apply((Object)entity4)) {
                list.add((T)entity4);
            }
        }
        return list;
    }
    
    public <T extends Entity> List<T> getPlayers(final Class<? extends T> playerType, final Predicate<? super T> filter) {
        final List<T> list = (List<T>)Lists.newArrayList();
        for (final Entity entity4 : this.playerEntities) {
            if (playerType.isAssignableFrom(entity4.getClass()) && filter.apply((Object)entity4)) {
                list.add((T)entity4);
            }
        }
        return list;
    }
    
    public <T extends Entity> List<T> getEntitiesWithinAABB(final Class<? extends T> classEntity, final AxisAlignedBB bb) {
        return this.getEntitiesWithinAABB(classEntity, bb, (com.google.common.base.Predicate<? super T>)EntitySelectors.NOT_SPECTATING);
    }
    
    public <T extends Entity> List<T> getEntitiesWithinAABB(final Class<? extends T> clazz, final AxisAlignedBB aabb, @Nullable final Predicate<? super T> filter) {
        final int j2 = MathHelper.floor((aabb.minX - 2.0) / 16.0);
        final int k2 = MathHelper.ceil((aabb.maxX + 2.0) / 16.0);
        final int l2 = MathHelper.floor((aabb.minZ - 2.0) / 16.0);
        final int i3 = MathHelper.ceil((aabb.maxZ + 2.0) / 16.0);
        final List<T> list = (List<T>)Lists.newArrayList();
        for (int j3 = j2; j3 < k2; ++j3) {
            for (int k3 = l2; k3 < i3; ++k3) {
                if (this.isChunkLoaded(j3, k3, true)) {
                    this.getChunk(j3, k3).getEntitiesOfTypeWithinAABB(clazz, aabb, list, filter);
                }
            }
        }
        return list;
    }
    
    @Nullable
    public <T extends Entity> T findNearestEntityWithinAABB(final Class<? extends T> entityType, final AxisAlignedBB aabb, final T closestTo) {
        final List<T> list = this.getEntitiesWithinAABB(entityType, aabb);
        T t = null;
        double d0 = Double.MAX_VALUE;
        for (int j2 = 0; j2 < list.size(); ++j2) {
            final T t2 = list.get(j2);
            if (t2 != closestTo && EntitySelectors.NOT_SPECTATING.apply((Object)t2)) {
                final double d2 = closestTo.getDistanceSq(t2);
                if (d2 <= d0) {
                    t = t2;
                    d0 = d2;
                }
            }
        }
        return t;
    }
    
    @Nullable
    public Entity getEntityByID(final int id) {
        return this.entitiesById.lookup(id);
    }
    
    public List<Entity> getLoadedEntityList() {
        return this.loadedEntityList;
    }
    
    public void markChunkDirty(final BlockPos pos, final TileEntity unusedTileEntity) {
        if (this.isBlockLoaded(pos)) {
            this.getChunk(pos).markDirty();
        }
    }
    
    public int countEntities(final Class<?> entityType) {
        int j2 = 0;
        for (final Entity entity4 : this.loadedEntityList) {
            if ((!(entity4 instanceof EntityLiving) || !((EntityLiving)entity4).isNoDespawnRequired()) && entityType.isAssignableFrom(entity4.getClass())) {
                ++j2;
            }
        }
        return j2;
    }
    
    public void loadEntities(final Collection<Entity> entityCollection) {
        this.loadedEntityList.addAll(entityCollection);
        for (final Entity entity4 : entityCollection) {
            this.onEntityAdded(entity4);
        }
    }
    
    public void unloadEntities(final Collection<Entity> entityCollection) {
        this.unloadedEntityList.addAll(entityCollection);
    }
    
    public boolean mayPlace(final Block blockIn, final BlockPos pos, final boolean skipCollisionCheck, final EnumFacing sidePlacedOn, @Nullable final Entity placer) {
        final IBlockState iblockstate1 = this.getBlockState(pos);
        final AxisAlignedBB axisalignedbb = skipCollisionCheck ? null : blockIn.getDefaultState().getCollisionBoundingBox(this, pos);
        return (axisalignedbb == Block.NULL_AABB || this.checkNoEntityCollision(axisalignedbb.offset(pos), placer)) && ((iblockstate1.getMaterial() == Material.CIRCUITS && blockIn == Blocks.ANVIL) || (iblockstate1.getMaterial().isReplaceable() && blockIn.canPlaceBlockOnSide(this, pos, sidePlacedOn)));
    }
    
    public int getSeaLevel() {
        return this.seaLevel;
    }
    
    public void setSeaLevel(final int seaLevelIn) {
        this.seaLevel = seaLevelIn;
    }
    
    @Override
    public int getStrongPower(final BlockPos pos, final EnumFacing direction) {
        return this.getBlockState(pos).getStrongPower(this, pos, direction);
    }
    
    @Override
    public WorldType getWorldType() {
        return this.worldInfo.getTerrainType();
    }
    
    public int getStrongPower(final BlockPos pos) {
        int j2 = 0;
        j2 = Math.max(j2, this.getStrongPower(pos.down(), EnumFacing.DOWN));
        if (j2 >= 15) {
            return j2;
        }
        j2 = Math.max(j2, this.getStrongPower(pos.up(), EnumFacing.UP));
        if (j2 >= 15) {
            return j2;
        }
        j2 = Math.max(j2, this.getStrongPower(pos.north(), EnumFacing.NORTH));
        if (j2 >= 15) {
            return j2;
        }
        j2 = Math.max(j2, this.getStrongPower(pos.south(), EnumFacing.SOUTH));
        if (j2 >= 15) {
            return j2;
        }
        j2 = Math.max(j2, this.getStrongPower(pos.west(), EnumFacing.WEST));
        if (j2 >= 15) {
            return j2;
        }
        j2 = Math.max(j2, this.getStrongPower(pos.east(), EnumFacing.EAST));
        return (j2 >= 15) ? j2 : j2;
    }
    
    public boolean isSidePowered(final BlockPos pos, final EnumFacing side) {
        return this.getRedstonePower(pos, side) > 0;
    }
    
    public int getRedstonePower(final BlockPos pos, final EnumFacing facing) {
        final IBlockState iblockstate1 = this.getBlockState(pos);
        return iblockstate1.isNormalCube() ? this.getStrongPower(pos) : iblockstate1.getWeakPower(this, pos, facing);
    }
    
    public boolean isBlockPowered(final BlockPos pos) {
        return this.getRedstonePower(pos.down(), EnumFacing.DOWN) > 0 || this.getRedstonePower(pos.up(), EnumFacing.UP) > 0 || this.getRedstonePower(pos.north(), EnumFacing.NORTH) > 0 || this.getRedstonePower(pos.south(), EnumFacing.SOUTH) > 0 || this.getRedstonePower(pos.west(), EnumFacing.WEST) > 0 || this.getRedstonePower(pos.east(), EnumFacing.EAST) > 0;
    }
    
    public int getRedstonePowerFromNeighbors(final BlockPos pos) {
        int j2 = 0;
        for (final EnumFacing enumfacing : EnumFacing.values()) {
            final int k2 = this.getRedstonePower(pos.offset(enumfacing), enumfacing);
            if (k2 >= 15) {
                return 15;
            }
            if (k2 > j2) {
                j2 = k2;
            }
        }
        return j2;
    }
    
    @Nullable
    public EntityPlayer getClosestPlayerToEntity(final Entity entityIn, final double distance) {
        return this.getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance, false);
    }
    
    @Nullable
    public EntityPlayer getNearestPlayerNotCreative(final Entity entityIn, final double distance) {
        return this.getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance, true);
    }
    
    @Nullable
    public EntityPlayer getClosestPlayer(final double posX, final double posY, final double posZ, final double distance, final boolean spectator) {
        final Predicate<Entity> predicate = spectator ? EntitySelectors.CAN_AI_TARGET : EntitySelectors.NOT_SPECTATING;
        return this.getClosestPlayer(posX, posY, posZ, distance, predicate);
    }
    
    @Nullable
    public EntityPlayer getClosestPlayer(final double x, final double y, final double z, final double distance, final Predicate<Entity> predicate) {
        double d0 = -1.0;
        EntityPlayer entityplayer = null;
        for (int j2 = 0; j2 < this.playerEntities.size(); ++j2) {
            final EntityPlayer entityplayer2 = this.playerEntities.get(j2);
            if (predicate.apply((Object)entityplayer2)) {
                final double d2 = entityplayer2.getDistanceSq(x, y, z);
                if ((distance < 0.0 || d2 < distance * distance) && (d0 == -1.0 || d2 < d0)) {
                    d0 = d2;
                    entityplayer = entityplayer2;
                }
            }
        }
        return entityplayer;
    }
    
    public boolean isAnyPlayerWithinRangeAt(final double x, final double y, final double z, final double range) {
        for (int j2 = 0; j2 < this.playerEntities.size(); ++j2) {
            final EntityPlayer entityplayer = this.playerEntities.get(j2);
            if (EntitySelectors.NOT_SPECTATING.apply((Object)entityplayer)) {
                final double d0 = entityplayer.getDistanceSq(x, y, z);
                if (range < 0.0 || d0 < range * range) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Nullable
    public EntityPlayer getNearestAttackablePlayer(final Entity entityIn, final double maxXZDistance, final double maxYDistance) {
        return this.getNearestAttackablePlayer(entityIn.posX, entityIn.posY, entityIn.posZ, maxXZDistance, maxYDistance, null, null);
    }
    
    @Nullable
    public EntityPlayer getNearestAttackablePlayer(final BlockPos pos, final double maxXZDistance, final double maxYDistance) {
        return this.getNearestAttackablePlayer(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, maxXZDistance, maxYDistance, null, null);
    }
    
    @Nullable
    public EntityPlayer getNearestAttackablePlayer(final double posX, final double posY, final double posZ, final double maxXZDistance, final double maxYDistance, @Nullable final Function<EntityPlayer, Double> playerToDouble, @Nullable final Predicate<EntityPlayer> predicate) {
        double d0 = -1.0;
        EntityPlayer entityplayer = null;
        for (int j2 = 0; j2 < this.playerEntities.size(); ++j2) {
            final EntityPlayer entityplayer2 = this.playerEntities.get(j2);
            if (!entityplayer2.capabilities.disableDamage && entityplayer2.isEntityAlive() && !entityplayer2.isSpectator() && (predicate == null || predicate.apply((Object)entityplayer2))) {
                final double d2 = entityplayer2.getDistanceSq(posX, entityplayer2.posY, posZ);
                double d3 = maxXZDistance;
                if (entityplayer2.isSneaking()) {
                    d3 = maxXZDistance * 0.800000011920929;
                }
                if (entityplayer2.isInvisible()) {
                    float f = entityplayer2.getArmorVisibility();
                    if (f < 0.1f) {
                        f = 0.1f;
                    }
                    d3 *= 0.7f * f;
                }
                if (playerToDouble != null) {
                    d3 *= (double)MoreObjects.firstNonNull(playerToDouble.apply((Object)entityplayer2), (Object)1.0);
                }
                if ((maxYDistance < 0.0 || Math.abs(entityplayer2.posY - posY) < maxYDistance * maxYDistance) && (maxXZDistance < 0.0 || d2 < d3 * d3) && (d0 == -1.0 || d2 < d0)) {
                    d0 = d2;
                    entityplayer = entityplayer2;
                }
            }
        }
        return entityplayer;
    }
    
    @Nullable
    public EntityPlayer getPlayerEntityByName(final String name) {
        for (int j2 = 0; j2 < this.playerEntities.size(); ++j2) {
            final EntityPlayer entityplayer = this.playerEntities.get(j2);
            if (name.equals(entityplayer.getName())) {
                return entityplayer;
            }
        }
        return null;
    }
    
    @Nullable
    public EntityPlayer getPlayerEntityByUUID(final UUID uuid) {
        for (int j2 = 0; j2 < this.playerEntities.size(); ++j2) {
            final EntityPlayer entityplayer = this.playerEntities.get(j2);
            if (uuid.equals(entityplayer.getUniqueID())) {
                return entityplayer;
            }
        }
        return null;
    }
    
    public void sendQuittingDisconnectingPacket() {
    }
    
    public void checkSessionLock() throws MinecraftException {
        this.saveHandler.checkSessionLock();
    }
    
    public void setTotalWorldTime(final long worldTime) {
        this.worldInfo.setWorldTotalTime(worldTime);
    }
    
    public long getSeed() {
        return this.worldInfo.getSeed();
    }
    
    public long getTotalWorldTime() {
        return this.worldInfo.getWorldTotalTime();
    }
    
    public long getWorldTime() {
        return this.worldInfo.getWorldTime();
    }
    
    public void setWorldTime(final long time) {
        this.worldInfo.setWorldTime(time);
    }
    
    public BlockPos getSpawnPoint() {
        BlockPos blockpos1 = new BlockPos(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
        if (!this.getWorldBorder().contains(blockpos1)) {
            blockpos1 = this.getHeight(new BlockPos(this.getWorldBorder().getCenterX(), 0.0, this.getWorldBorder().getCenterZ()));
        }
        return blockpos1;
    }
    
    public void setSpawnPoint(final BlockPos pos) {
        this.worldInfo.setSpawn(pos);
    }
    
    public void joinEntityInSurroundings(final Entity entityIn) {
        final int j2 = MathHelper.floor(entityIn.posX / 16.0);
        final int k2 = MathHelper.floor(entityIn.posZ / 16.0);
        final int l2 = 2;
        for (int i3 = -2; i3 <= 2; ++i3) {
            for (int j3 = -2; j3 <= 2; ++j3) {
                this.getChunk(j2 + i3, k2 + j3);
            }
        }
        if (!this.loadedEntityList.contains(entityIn)) {
            this.loadedEntityList.add(entityIn);
        }
    }
    
    public boolean isBlockModifiable(final EntityPlayer player, final BlockPos pos) {
        return true;
    }
    
    public void setEntityState(final Entity entityIn, final byte state) {
    }
    
    public IChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
    public void addBlockEvent(final BlockPos pos, final Block blockIn, final int eventID, final int eventParam) {
        this.getBlockState(pos).onBlockEventReceived(this, pos, eventID, eventParam);
    }
    
    public ISaveHandler getSaveHandler() {
        return this.saveHandler;
    }
    
    public WorldInfo getWorldInfo() {
        return this.worldInfo;
    }
    
    public GameRules getGameRules() {
        return this.worldInfo.getGameRulesInstance();
    }
    
    public void updateAllPlayersSleepingFlag() {
    }
    
    public float getThunderStrength(final float delta) {
        return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * delta) * this.getRainStrength(delta);
    }
    
    public void setThunderStrength(final float strength) {
        this.prevThunderingStrength = strength;
        this.thunderingStrength = strength;
    }
    
    public float getRainStrength(final float delta) {
        return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * delta;
    }
    
    public void setRainStrength(final float strength) {
        this.prevRainingStrength = strength;
        this.rainingStrength = strength;
    }
    
    public boolean isThundering() {
        return this.getThunderStrength(1.0f) > 0.9;
    }
    
    public boolean isRaining() {
        return this.getRainStrength(1.0f) > 0.2;
    }
    
    public boolean isRainingAt(final BlockPos position) {
        if (!this.isRaining()) {
            return false;
        }
        if (!this.canSeeSky(position)) {
            return false;
        }
        if (this.getPrecipitationHeight(position).getY() > position.getY()) {
            return false;
        }
        final Biome biome = this.getBiome(position);
        return !biome.getEnableSnow() && !this.canSnowAt(position, false) && biome.canRain();
    }
    
    public boolean isBlockinHighHumidity(final BlockPos pos) {
        final Biome biome = this.getBiome(pos);
        return biome.isHighHumidity();
    }
    
    @Nullable
    public MapStorage getMapStorage() {
        return this.mapStorage;
    }
    
    public void setData(final String dataID, final WorldSavedData worldSavedDataIn) {
        this.mapStorage.setData(dataID, worldSavedDataIn);
    }
    
    @Nullable
    public WorldSavedData loadData(final Class<? extends WorldSavedData> clazz, final String dataID) {
        return this.mapStorage.getOrLoadData(clazz, dataID);
    }
    
    public int getUniqueDataId(final String key) {
        return this.mapStorage.getUniqueDataId(key);
    }
    
    public void playBroadcastSound(final int id, final BlockPos pos, final int data) {
        for (int j2 = 0; j2 < this.eventListeners.size(); ++j2) {
            this.eventListeners.get(j2).broadcastSound(id, pos, data);
        }
    }
    
    public void playEvent(final int type, final BlockPos pos, final int data) {
        this.playEvent(null, type, pos, data);
    }
    
    public void playEvent(@Nullable final EntityPlayer player, final int type, final BlockPos pos, final int data) {
        try {
            for (int j2 = 0; j2 < this.eventListeners.size(); ++j2) {
                this.eventListeners.get(j2).playEvent(player, type, pos, data);
            }
        }
        catch (Throwable throwable3) {
            final CrashReport crashreport3 = CrashReport.makeCrashReport(throwable3, "Playing level event");
            final CrashReportCategory crashreportcategory3 = crashreport3.makeCategory("Level event being played");
            crashreportcategory3.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(pos));
            crashreportcategory3.addCrashSection("Event source", player);
            crashreportcategory3.addCrashSection("Event type", type);
            crashreportcategory3.addCrashSection("Event data", data);
            throw new ReportedException(crashreport3);
        }
    }
    
    public int getHeight() {
        return 256;
    }
    
    public int getActualHeight() {
        return this.provider.isNether() ? 128 : 256;
    }
    
    public Random setRandomSeed(final int seedX, final int seedY, final int seedZ) {
        final long j2 = seedX * 341873128712L + seedY * 132897987541L + this.getWorldInfo().getSeed() + seedZ;
        this.rand.setSeed(j2);
        return this.rand;
    }
    
    public double getHorizon() {
        return (this.worldInfo.getTerrainType() == WorldType.FLAT) ? 0.0 : 63.0;
    }
    
    public CrashReportCategory addWorldInfoToCrashReport(final CrashReport report) {
        final CrashReportCategory crashreportcategory3 = report.makeCategoryDepth("Affected level", 1);
        crashreportcategory3.addCrashSection("Level name", (this.worldInfo == null) ? "????" : this.worldInfo.getWorldName());
        crashreportcategory3.addDetail("All players", new ICrashReportDetail<String>() {
            @Override
            public String call() {
                return World.this.playerEntities.size() + " total; " + World.this.playerEntities;
            }
        });
        crashreportcategory3.addDetail("Chunk stats", new ICrashReportDetail<String>() {
            @Override
            public String call() {
                return World.this.chunkProvider.makeString();
            }
        });
        try {
            this.worldInfo.addToCrashReport(crashreportcategory3);
        }
        catch (Throwable throwable3) {
            crashreportcategory3.addCrashSectionThrowable("Level Data Unobtainable", throwable3);
        }
        return crashreportcategory3;
    }
    
    public void sendBlockBreakProgress(final int breakerId, final BlockPos pos, final int progress) {
        for (int j2 = 0; j2 < this.eventListeners.size(); ++j2) {
            final IWorldEventListener iworldeventlistener = this.eventListeners.get(j2);
            iworldeventlistener.sendBlockBreakProgress(breakerId, pos, progress);
        }
    }
    
    public Calendar getCurrentDate() {
        if (this.getTotalWorldTime() % 600L == 0L) {
            this.calendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis());
        }
        return this.calendar;
    }
    
    public void makeFireworks(final double x, final double y, final double z, final double motionX, final double motionY, final double motionZ, @Nullable final NBTTagCompound compound) {
    }
    
    public Scoreboard getScoreboard() {
        return this.worldScoreboard;
    }
    
    public void updateComparatorOutputLevel(final BlockPos pos, final Block blockIn) {
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            BlockPos blockpos1 = pos.offset(enumfacing);
            if (this.isBlockLoaded(blockpos1)) {
                IBlockState iblockstate1 = this.getBlockState(blockpos1);
                if (Blocks.UNPOWERED_COMPARATOR.isSameDiode(iblockstate1)) {
                    iblockstate1.neighborChanged(this, blockpos1, blockIn, pos);
                }
                else {
                    if (!iblockstate1.isNormalCube()) {
                        continue;
                    }
                    blockpos1 = blockpos1.offset(enumfacing);
                    iblockstate1 = this.getBlockState(blockpos1);
                    if (!Blocks.UNPOWERED_COMPARATOR.isSameDiode(iblockstate1)) {
                        continue;
                    }
                    iblockstate1.neighborChanged(this, blockpos1, blockIn, pos);
                }
            }
        }
    }
    
    public DifficultyInstance getDifficultyForLocation(final BlockPos pos) {
        long j2 = 0L;
        float f = 0.0f;
        if (this.isBlockLoaded(pos)) {
            f = this.getCurrentMoonPhaseFactor();
            j2 = this.getChunk(pos).getInhabitedTime();
        }
        return new DifficultyInstance(this.getDifficulty(), this.getWorldTime(), j2, f);
    }
    
    public EnumDifficulty getDifficulty() {
        return this.getWorldInfo().getDifficulty();
    }
    
    public int getSkylightSubtracted() {
        return this.skylightSubtracted;
    }
    
    public void setSkylightSubtracted(final int newSkylightSubtracted) {
        this.skylightSubtracted = newSkylightSubtracted;
    }
    
    public int getLastLightningBolt() {
        return this.lastLightningBolt;
    }
    
    public void setLastLightningBolt(final int lastLightningBoltIn) {
        this.lastLightningBolt = lastLightningBoltIn;
    }
    
    public VillageCollection getVillageCollection() {
        return this.villageCollection;
    }
    
    public WorldBorder getWorldBorder() {
        return this.worldBorder;
    }
    
    public boolean isSpawnChunk(final int x, final int z) {
        final BlockPos blockpos1 = this.getSpawnPoint();
        final int j2 = x * 16 + 8 - blockpos1.getX();
        final int k2 = z * 16 + 8 - blockpos1.getZ();
        final int l2 = 128;
        return j2 >= -128 && j2 <= 128 && k2 >= -128 && k2 <= 128;
    }
    
    public void sendPacketToServer(final Packet<?> packetIn) {
        throw new UnsupportedOperationException("Can't send packets to server unless you're on the client.");
    }
    
    public LootTableManager getLootTableManager() {
        return this.lootTable;
    }
    
    @Nullable
    public BlockPos findNearestStructure(final String structureName, final BlockPos position, final boolean findUnexplored) {
        return null;
    }
}
