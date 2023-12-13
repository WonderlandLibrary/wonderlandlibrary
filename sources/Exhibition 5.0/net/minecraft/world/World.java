// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import java.util.UUID;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLiving;
import com.google.common.base.Predicate;
import net.minecraft.command.IEntitySelector;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import java.util.Iterator;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReportCategory;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Sets;
import com.google.common.collect.Lists;
import net.minecraft.world.border.WorldBorder;
import java.util.Set;
import net.minecraft.scoreboard.Scoreboard;
import java.util.Calendar;
import net.minecraft.profiler.Profiler;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.chunk.IChunkProvider;
import java.util.Random;
import net.minecraft.util.IntHashMap;
import java.util.List;

public abstract class World implements IBlockAccess
{
    protected boolean scheduledUpdatesAreImmediate;
    public final List loadedEntityList;
    protected final List unloadedEntityList;
    public final List loadedTileEntityList;
    public final List tickableTileEntities;
    private final List addedTileEntityList;
    private final List tileEntitiesToBeRemoved;
    public final List playerEntities;
    public final List weatherEffects;
    protected final IntHashMap entitiesById;
    private long cloudColour;
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
    protected List worldAccesses;
    protected IChunkProvider chunkProvider;
    protected final ISaveHandler saveHandler;
    protected WorldInfo worldInfo;
    protected boolean findingSpawnPoint;
    protected MapStorage mapStorage;
    protected VillageCollection villageCollectionObj;
    public final Profiler theProfiler;
    private final Calendar theCalendar;
    protected Scoreboard worldScoreboard;
    public final boolean isRemote;
    protected Set activeChunkSet;
    private int ambientTickCountdown;
    protected boolean spawnHostileMobs;
    protected boolean spawnPeacefulMobs;
    private boolean processingLoadedTiles;
    private final WorldBorder worldBorder;
    int[] lightUpdateBlockList;
    private static final String __OBFID = "CL_00000140";
    
    protected World(final ISaveHandler saveHandlerIn, final WorldInfo info, final WorldProvider providerIn, final Profiler profilerIn, final boolean client) {
        this.loadedEntityList = Lists.newArrayList();
        this.unloadedEntityList = Lists.newArrayList();
        this.loadedTileEntityList = Lists.newArrayList();
        this.tickableTileEntities = Lists.newArrayList();
        this.addedTileEntityList = Lists.newArrayList();
        this.tileEntitiesToBeRemoved = Lists.newArrayList();
        this.playerEntities = Lists.newArrayList();
        this.weatherEffects = Lists.newArrayList();
        this.entitiesById = new IntHashMap();
        this.cloudColour = 16777215L;
        this.updateLCG = new Random().nextInt();
        this.rand = new Random();
        this.worldAccesses = Lists.newArrayList();
        this.theCalendar = Calendar.getInstance();
        this.worldScoreboard = new Scoreboard();
        this.activeChunkSet = Sets.newHashSet();
        this.ambientTickCountdown = this.rand.nextInt(12000);
        this.spawnHostileMobs = true;
        this.spawnPeacefulMobs = true;
        this.lightUpdateBlockList = new int[32768];
        this.saveHandler = saveHandlerIn;
        this.theProfiler = profilerIn;
        this.worldInfo = info;
        this.provider = providerIn;
        this.isRemote = client;
        this.worldBorder = providerIn.getWorldBorder();
    }
    
    public World init() {
        return this;
    }
    
    @Override
    public BiomeGenBase getBiomeGenForCoords(final BlockPos pos) {
        if (this.isBlockLoaded(pos)) {
            final Chunk var2 = this.getChunkFromBlockCoords(pos);
            try {
                return var2.getBiome(pos, this.provider.getWorldChunkManager());
            }
            catch (Throwable var4) {
                final CrashReport var3 = CrashReport.makeCrashReport(var4, "Getting biome");
                final CrashReportCategory var5 = var3.makeCategory("Coordinates of biome request");
                var5.addCrashSectionCallable("Location", new Callable() {
                    private static final String __OBFID = "CL_00000141";
                    
                    @Override
                    public String call() {
                        return CrashReportCategory.getCoordinateInfo(pos);
                    }
                });
                throw new ReportedException(var3);
            }
        }
        return this.provider.getWorldChunkManager().func_180300_a(pos, BiomeGenBase.plains);
    }
    
    public WorldChunkManager getWorldChunkManager() {
        return this.provider.getWorldChunkManager();
    }
    
    protected abstract IChunkProvider createChunkProvider();
    
    public void initialize(final WorldSettings settings) {
        this.worldInfo.setServerInitialized(true);
    }
    
    public void setInitialSpawnLocation() {
        this.setSpawnLocation(new BlockPos(8, 64, 8));
    }
    
    public Block getGroundAboveSeaLevel(final BlockPos pos) {
        BlockPos var2;
        for (var2 = new BlockPos(pos.getX(), 63, pos.getZ()); !this.isAirBlock(var2.offsetUp()); var2 = var2.offsetUp()) {}
        return this.getBlockState(var2).getBlock();
    }
    
    private boolean isValid(final BlockPos pos) {
        return pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000 && pos.getY() >= 0 && pos.getY() < 256;
    }
    
    @Override
    public boolean isAirBlock(final BlockPos pos) {
        return this.getBlockState(pos).getBlock().getMaterial() == Material.air;
    }
    
    public boolean isBlockLoaded(final BlockPos pos) {
        return this.isBlockLoaded(pos, true);
    }
    
    public boolean isBlockLoaded(final BlockPos pos, final boolean p_175668_2_) {
        return this.isValid(pos) && this.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, p_175668_2_);
    }
    
    public boolean isAreaLoaded(final BlockPos p_175697_1_, final int radius) {
        return this.isAreaLoaded(p_175697_1_, radius, true);
    }
    
    public boolean isAreaLoaded(final BlockPos p_175648_1_, final int radius, final boolean p_175648_3_) {
        return this.isAreaLoaded(p_175648_1_.getX() - radius, p_175648_1_.getY() - radius, p_175648_1_.getZ() - radius, p_175648_1_.getX() + radius, p_175648_1_.getY() + radius, p_175648_1_.getZ() + radius, p_175648_3_);
    }
    
    public boolean isAreaLoaded(final BlockPos p_175707_1_, final BlockPos p_175707_2_) {
        return this.isAreaLoaded(p_175707_1_, p_175707_2_, true);
    }
    
    public boolean isAreaLoaded(final BlockPos p_175706_1_, final BlockPos p_175706_2_, final boolean p_175706_3_) {
        return this.isAreaLoaded(p_175706_1_.getX(), p_175706_1_.getY(), p_175706_1_.getZ(), p_175706_2_.getX(), p_175706_2_.getY(), p_175706_2_.getZ(), p_175706_3_);
    }
    
    public boolean isAreaLoaded(final StructureBoundingBox p_175711_1_) {
        return this.isAreaLoaded(p_175711_1_, true);
    }
    
    public boolean isAreaLoaded(final StructureBoundingBox p_175639_1_, final boolean p_175639_2_) {
        return this.isAreaLoaded(p_175639_1_.minX, p_175639_1_.minY, p_175639_1_.minZ, p_175639_1_.maxX, p_175639_1_.maxY, p_175639_1_.maxZ, p_175639_2_);
    }
    
    private boolean isAreaLoaded(int p_175663_1_, final int p_175663_2_, int p_175663_3_, int p_175663_4_, final int p_175663_5_, int p_175663_6_, final boolean p_175663_7_) {
        if (p_175663_5_ >= 0 && p_175663_2_ < 256) {
            p_175663_1_ >>= 4;
            p_175663_3_ >>= 4;
            p_175663_4_ >>= 4;
            p_175663_6_ >>= 4;
            for (int var8 = p_175663_1_; var8 <= p_175663_4_; ++var8) {
                for (int var9 = p_175663_3_; var9 <= p_175663_6_; ++var9) {
                    if (!this.isChunkLoaded(var8, var9, p_175663_7_)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    protected boolean isChunkLoaded(final int x, final int z, final boolean allowEmpty) {
        return this.chunkProvider.chunkExists(x, z) && (allowEmpty || !this.chunkProvider.provideChunk(x, z).isEmpty());
    }
    
    public Chunk getChunkFromBlockCoords(final BlockPos pos) {
        return this.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4);
    }
    
    public Chunk getChunkFromChunkCoords(final int chunkX, final int chunkZ) {
        return this.chunkProvider.provideChunk(chunkX, chunkZ);
    }
    
    public boolean setBlockState(final BlockPos pos, final IBlockState newState, final int flags) {
        if (!this.isValid(pos)) {
            return false;
        }
        if (!this.isRemote && this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
            return false;
        }
        final Chunk var4 = this.getChunkFromBlockCoords(pos);
        final Block var5 = newState.getBlock();
        final IBlockState var6 = var4.setBlockState(pos, newState);
        if (var6 == null) {
            return false;
        }
        final Block var7 = var6.getBlock();
        if (var5.getLightOpacity() != var7.getLightOpacity() || var5.getLightValue() != var7.getLightValue()) {
            this.theProfiler.startSection("checkLight");
            this.checkLight(pos);
            this.theProfiler.endSection();
        }
        if ((flags & 0x2) != 0x0 && (!this.isRemote || (flags & 0x4) == 0x0) && var4.isPopulated()) {
            this.markBlockForUpdate(pos);
        }
        if (!this.isRemote && (flags & 0x1) != 0x0) {
            this.func_175722_b(pos, var6.getBlock());
            if (var5.hasComparatorInputOverride()) {
                this.updateComparatorOutputLevel(pos, var5);
            }
        }
        return true;
    }
    
    public boolean setBlockToAir(final BlockPos pos) {
        return this.setBlockState(pos, Blocks.air.getDefaultState(), 3);
    }
    
    public boolean destroyBlock(final BlockPos pos, final boolean dropBlock) {
        final IBlockState var3 = this.getBlockState(pos);
        final Block var4 = var3.getBlock();
        if (var4.getMaterial() == Material.air) {
            return false;
        }
        this.playAuxSFX(2001, pos, Block.getStateId(var3));
        if (dropBlock) {
            var4.dropBlockAsItem(this, pos, var3, 0);
        }
        return this.setBlockState(pos, Blocks.air.getDefaultState(), 3);
    }
    
    public boolean setBlockState(final BlockPos pos, final IBlockState state) {
        return this.setBlockState(pos, state, 3);
    }
    
    public void markBlockForUpdate(final BlockPos pos) {
        for (int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
            this.worldAccesses.get(var2).markBlockForUpdate(pos);
        }
    }
    
    public void func_175722_b(final BlockPos pos, final Block blockType) {
        if (this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD) {
            this.notifyNeighborsOfStateChange(pos, blockType);
        }
    }
    
    public void markBlocksDirtyVertical(final int x1, final int z1, int x2, int z2) {
        if (x2 > z2) {
            final int var5 = z2;
            z2 = x2;
            x2 = var5;
        }
        if (!this.provider.getHasNoSky()) {
            for (int var5 = x2; var5 <= z2; ++var5) {
                this.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x1, var5, z1));
            }
        }
        this.markBlockRangeForRenderUpdate(x1, x2, z1, x1, z2, z1);
    }
    
    public void markBlockRangeForRenderUpdate(final BlockPos rangeMin, final BlockPos rangeMax) {
        this.markBlockRangeForRenderUpdate(rangeMin.getX(), rangeMin.getY(), rangeMin.getZ(), rangeMax.getX(), rangeMax.getY(), rangeMax.getZ());
    }
    
    public void markBlockRangeForRenderUpdate(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        for (int var7 = 0; var7 < this.worldAccesses.size(); ++var7) {
            this.worldAccesses.get(var7).markBlockRangeForRenderUpdate(x1, y1, z1, x2, y2, z2);
        }
    }
    
    public void notifyNeighborsOfStateChange(final BlockPos pos, final Block blockType) {
        this.notifyBlockOfStateChange(pos.offsetWest(), blockType);
        this.notifyBlockOfStateChange(pos.offsetEast(), blockType);
        this.notifyBlockOfStateChange(pos.offsetDown(), blockType);
        this.notifyBlockOfStateChange(pos.offsetUp(), blockType);
        this.notifyBlockOfStateChange(pos.offsetNorth(), blockType);
        this.notifyBlockOfStateChange(pos.offsetSouth(), blockType);
    }
    
    public void notifyNeighborsOfStateExcept(final BlockPos pos, final Block blockType, final EnumFacing skipSide) {
        if (skipSide != EnumFacing.WEST) {
            this.notifyBlockOfStateChange(pos.offsetWest(), blockType);
        }
        if (skipSide != EnumFacing.EAST) {
            this.notifyBlockOfStateChange(pos.offsetEast(), blockType);
        }
        if (skipSide != EnumFacing.DOWN) {
            this.notifyBlockOfStateChange(pos.offsetDown(), blockType);
        }
        if (skipSide != EnumFacing.UP) {
            this.notifyBlockOfStateChange(pos.offsetUp(), blockType);
        }
        if (skipSide != EnumFacing.NORTH) {
            this.notifyBlockOfStateChange(pos.offsetNorth(), blockType);
        }
        if (skipSide != EnumFacing.SOUTH) {
            this.notifyBlockOfStateChange(pos.offsetSouth(), blockType);
        }
    }
    
    public void notifyBlockOfStateChange(final BlockPos pos, final Block blockIn) {
        if (!this.isRemote) {
            final IBlockState var3 = this.getBlockState(pos);
            try {
                var3.getBlock().onNeighborBlockChange(this, pos, var3, blockIn);
            }
            catch (Throwable var5) {
                final CrashReport var4 = CrashReport.makeCrashReport(var5, "Exception while updating neighbours");
                final CrashReportCategory var6 = var4.makeCategory("Block being updated");
                var6.addCrashSectionCallable("Source block type", new Callable() {
                    private static final String __OBFID = "CL_00000142";
                    
                    @Override
                    public String call() {
                        try {
                            return String.format("ID #%d (%s // %s)", Block.getIdFromBlock(blockIn), blockIn.getUnlocalizedName(), blockIn.getClass().getCanonicalName());
                        }
                        catch (Throwable var2) {
                            return "ID #" + Block.getIdFromBlock(blockIn);
                        }
                    }
                });
                CrashReportCategory.addBlockInfo(var6, pos, var3);
                throw new ReportedException(var4);
            }
        }
    }
    
    public boolean isBlockTickPending(final BlockPos pos, final Block blockType) {
        return false;
    }
    
    public boolean isAgainstSky(final BlockPos pos) {
        return this.getChunkFromBlockCoords(pos).canSeeSky(pos);
    }
    
    public boolean canBlockSeeSky(final BlockPos pos) {
        if (pos.getY() >= 63) {
            return this.isAgainstSky(pos);
        }
        BlockPos var2 = new BlockPos(pos.getX(), 63, pos.getZ());
        if (!this.isAgainstSky(var2)) {
            return false;
        }
        for (var2 = var2.offsetDown(); var2.getY() > pos.getY(); var2 = var2.offsetDown()) {
            final Block var3 = this.getBlockState(var2).getBlock();
            if (var3.getLightOpacity() > 0 && !var3.getMaterial().isLiquid()) {
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
        return this.getChunkFromBlockCoords(pos).setLight(pos, 0);
    }
    
    public int getLightFromNeighbors(final BlockPos pos) {
        return this.getLight(pos, true);
    }
    
    public int getLight(BlockPos pos, final boolean checkNeighbors) {
        if (pos.getX() < -30000000 || pos.getZ() < -30000000 || pos.getX() >= 30000000 || pos.getZ() >= 30000000) {
            return 15;
        }
        if (checkNeighbors && this.getBlockState(pos).getBlock().getUseNeighborBrightness()) {
            int var8 = this.getLight(pos.offsetUp(), false);
            final int var9 = this.getLight(pos.offsetEast(), false);
            final int var10 = this.getLight(pos.offsetWest(), false);
            final int var11 = this.getLight(pos.offsetSouth(), false);
            final int var12 = this.getLight(pos.offsetNorth(), false);
            if (var9 > var8) {
                var8 = var9;
            }
            if (var10 > var8) {
                var8 = var10;
            }
            if (var11 > var8) {
                var8 = var11;
            }
            if (var12 > var8) {
                var8 = var12;
            }
            return var8;
        }
        if (pos.getY() < 0) {
            return 0;
        }
        if (pos.getY() >= 256) {
            pos = new BlockPos(pos.getX(), 255, pos.getZ());
        }
        final Chunk var13 = this.getChunkFromBlockCoords(pos);
        return var13.setLight(pos, this.skylightSubtracted);
    }
    
    public BlockPos getHorizon(final BlockPos pos) {
        int var2;
        if (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000) {
            if (this.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, true)) {
                var2 = this.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4).getHeight(pos.getX() & 0xF, pos.getZ() & 0xF);
            }
            else {
                var2 = 0;
            }
        }
        else {
            var2 = 64;
        }
        return new BlockPos(pos.getX(), var2, pos.getZ());
    }
    
    public int getChunksLowestHorizon(final int x, final int z) {
        if (x < -30000000 || z < -30000000 || x >= 30000000 || z >= 30000000) {
            return 64;
        }
        if (!this.isChunkLoaded(x >> 4, z >> 4, true)) {
            return 0;
        }
        final Chunk var3 = this.getChunkFromChunkCoords(x >> 4, z >> 4);
        return var3.getLowestHeight();
    }
    
    public int getLightFromNeighborsFor(final EnumSkyBlock type, BlockPos p_175705_2_) {
        if (this.provider.getHasNoSky() && type == EnumSkyBlock.SKY) {
            return 0;
        }
        if (p_175705_2_.getY() < 0) {
            p_175705_2_ = new BlockPos(p_175705_2_.getX(), 0, p_175705_2_.getZ());
        }
        if (!this.isValid(p_175705_2_)) {
            return type.defaultLightValue;
        }
        if (!this.isBlockLoaded(p_175705_2_)) {
            return type.defaultLightValue;
        }
        if (this.getBlockState(p_175705_2_).getBlock().getUseNeighborBrightness()) {
            int var8 = this.getLightFor(type, p_175705_2_.offsetUp());
            final int var9 = this.getLightFor(type, p_175705_2_.offsetEast());
            final int var10 = this.getLightFor(type, p_175705_2_.offsetWest());
            final int var11 = this.getLightFor(type, p_175705_2_.offsetSouth());
            final int var12 = this.getLightFor(type, p_175705_2_.offsetNorth());
            if (var9 > var8) {
                var8 = var9;
            }
            if (var10 > var8) {
                var8 = var10;
            }
            if (var11 > var8) {
                var8 = var11;
            }
            if (var12 > var8) {
                var8 = var12;
            }
            return var8;
        }
        final Chunk var13 = this.getChunkFromBlockCoords(p_175705_2_);
        return var13.getLightFor(type, p_175705_2_);
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
        final Chunk var3 = this.getChunkFromBlockCoords(pos);
        return var3.getLightFor(type, pos);
    }
    
    public void setLightFor(final EnumSkyBlock type, final BlockPos pos, final int lightValue) {
        if (this.isValid(pos) && this.isBlockLoaded(pos)) {
            final Chunk var4 = this.getChunkFromBlockCoords(pos);
            var4.setLightFor(type, pos, lightValue);
            this.notifyLightSet(pos);
        }
    }
    
    public void notifyLightSet(final BlockPos pos) {
        for (int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
            this.worldAccesses.get(var2).notifyLightSet(pos);
        }
    }
    
    @Override
    public int getCombinedLight(final BlockPos p_175626_1_, final int p_175626_2_) {
        final int var3 = this.getLightFromNeighborsFor(EnumSkyBlock.SKY, p_175626_1_);
        int var4 = this.getLightFromNeighborsFor(EnumSkyBlock.BLOCK, p_175626_1_);
        if (var4 < p_175626_2_) {
            var4 = p_175626_2_;
        }
        return var3 << 20 | var4 << 4;
    }
    
    public float getLightBrightness(final BlockPos pos) {
        return this.provider.getLightBrightnessTable()[this.getLightFromNeighbors(pos)];
    }
    
    @Override
    public IBlockState getBlockState(final BlockPos pos) {
        if (!this.isValid(pos)) {
            return Blocks.air.getDefaultState();
        }
        final Chunk var2 = this.getChunkFromBlockCoords(pos);
        return var2.getBlockState(pos);
    }
    
    public boolean isDaytime() {
        return this.skylightSubtracted < 4;
    }
    
    public MovingObjectPosition rayTraceBlocks(final Vec3 vec1, final Vec3 vec2) {
        return this.rayTraceBlocks(vec1, vec2, false, false, false);
    }
    
    public MovingObjectPosition rayTraceBlocks(final Vec3 vec1, final Vec3 vec2, final boolean bool) {
        return this.rayTraceBlocks(vec1, vec2, bool, false, false);
    }
    
    public MovingObjectPosition rayTraceBlocks(Vec3 vec1, final Vec3 vec2, final boolean stopOnLiquid, final boolean ignoreBlockWithoutBoundingBox, final boolean returnLastUncollidableBlock) {
        if (Double.isNaN(vec1.xCoord) || Double.isNaN(vec1.yCoord) || Double.isNaN(vec1.zCoord)) {
            return null;
        }
        if (!Double.isNaN(vec2.xCoord) && !Double.isNaN(vec2.yCoord) && !Double.isNaN(vec2.zCoord)) {
            final int var6 = MathHelper.floor_double(vec2.xCoord);
            final int var7 = MathHelper.floor_double(vec2.yCoord);
            final int var8 = MathHelper.floor_double(vec2.zCoord);
            int var9 = MathHelper.floor_double(vec1.xCoord);
            int var10 = MathHelper.floor_double(vec1.yCoord);
            int var11 = MathHelper.floor_double(vec1.zCoord);
            BlockPos var12 = new BlockPos(var9, var10, var11);
            new BlockPos(var6, var7, var8);
            final IBlockState var13 = this.getBlockState(var12);
            final Block var14 = var13.getBlock();
            if ((!ignoreBlockWithoutBoundingBox || var14.getCollisionBoundingBox(this, var12, var13) != null) && var14.canCollideCheck(var13, stopOnLiquid)) {
                final MovingObjectPosition var15 = var14.collisionRayTrace(this, var12, vec1, vec2);
                if (var15 != null) {
                    return var15;
                }
            }
            MovingObjectPosition var16 = null;
            int var17 = 200;
            while (var17-- >= 0) {
                if (Double.isNaN(vec1.xCoord) || Double.isNaN(vec1.yCoord) || Double.isNaN(vec1.zCoord)) {
                    return null;
                }
                if (var9 == var6 && var10 == var7 && var11 == var8) {
                    return returnLastUncollidableBlock ? var16 : null;
                }
                boolean var18 = true;
                boolean var19 = true;
                boolean var20 = true;
                double var21 = 999.0;
                double var22 = 999.0;
                double var23 = 999.0;
                if (var6 > var9) {
                    var21 = var9 + 1.0;
                }
                else if (var6 < var9) {
                    var21 = var9 + 0.0;
                }
                else {
                    var18 = false;
                }
                if (var7 > var10) {
                    var22 = var10 + 1.0;
                }
                else if (var7 < var10) {
                    var22 = var10 + 0.0;
                }
                else {
                    var19 = false;
                }
                if (var8 > var11) {
                    var23 = var11 + 1.0;
                }
                else if (var8 < var11) {
                    var23 = var11 + 0.0;
                }
                else {
                    var20 = false;
                }
                double var24 = 999.0;
                double var25 = 999.0;
                double var26 = 999.0;
                final double var27 = vec2.xCoord - vec1.xCoord;
                final double var28 = vec2.yCoord - vec1.yCoord;
                final double var29 = vec2.zCoord - vec1.zCoord;
                if (var18) {
                    var24 = (var21 - vec1.xCoord) / var27;
                }
                if (var19) {
                    var25 = (var22 - vec1.yCoord) / var28;
                }
                if (var20) {
                    var26 = (var23 - vec1.zCoord) / var29;
                }
                if (var24 == -0.0) {
                    var24 = -1.0E-4;
                }
                if (var25 == -0.0) {
                    var25 = -1.0E-4;
                }
                if (var26 == -0.0) {
                    var26 = -1.0E-4;
                }
                EnumFacing var30;
                if (var24 < var25 && var24 < var26) {
                    var30 = ((var6 > var9) ? EnumFacing.WEST : EnumFacing.EAST);
                    vec1 = new Vec3(var21, vec1.yCoord + var28 * var24, vec1.zCoord + var29 * var24);
                }
                else if (var25 < var26) {
                    var30 = ((var7 > var10) ? EnumFacing.DOWN : EnumFacing.UP);
                    vec1 = new Vec3(vec1.xCoord + var27 * var25, var22, vec1.zCoord + var29 * var25);
                }
                else {
                    var30 = ((var8 > var11) ? EnumFacing.NORTH : EnumFacing.SOUTH);
                    vec1 = new Vec3(vec1.xCoord + var27 * var26, vec1.yCoord + var28 * var26, var23);
                }
                var9 = MathHelper.floor_double(vec1.xCoord) - ((var30 == EnumFacing.EAST) ? 1 : 0);
                var10 = MathHelper.floor_double(vec1.yCoord) - ((var30 == EnumFacing.UP) ? 1 : 0);
                var11 = MathHelper.floor_double(vec1.zCoord) - ((var30 == EnumFacing.SOUTH) ? 1 : 0);
                var12 = new BlockPos(var9, var10, var11);
                final IBlockState var31 = this.getBlockState(var12);
                final Block var32 = var31.getBlock();
                if (ignoreBlockWithoutBoundingBox && var32.getCollisionBoundingBox(this, var12, var31) == null) {
                    continue;
                }
                if (var32.canCollideCheck(var31, stopOnLiquid)) {
                    final MovingObjectPosition var33 = var32.collisionRayTrace(this, var12, vec1, vec2);
                    if (var33 != null) {
                        return var33;
                    }
                    continue;
                }
                else {
                    var16 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec1, var30, var12);
                }
            }
            return returnLastUncollidableBlock ? var16 : null;
        }
        return null;
    }
    
    public void playSoundAtEntity(final Entity p_72956_1_, final String p_72956_2_, final float p_72956_3_, final float p_72956_4_) {
        for (int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
            this.worldAccesses.get(var5).playSound(p_72956_2_, p_72956_1_.posX, p_72956_1_.posY, p_72956_1_.posZ, p_72956_3_, p_72956_4_);
        }
    }
    
    public void playSoundToNearExcept(final EntityPlayer p_85173_1_, final String p_85173_2_, final float p_85173_3_, final float p_85173_4_) {
        for (int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
            this.worldAccesses.get(var5).playSoundToNearExcept(p_85173_1_, p_85173_2_, p_85173_1_.posX, p_85173_1_.posY, p_85173_1_.posZ, p_85173_3_, p_85173_4_);
        }
    }
    
    public void playSoundEffect(final double x, final double y, final double z, final String soundName, final float volume, final float pitch) {
        for (int var10 = 0; var10 < this.worldAccesses.size(); ++var10) {
            this.worldAccesses.get(var10).playSound(soundName, x, y, z, volume, pitch);
        }
    }
    
    public void playSound(final double x, final double y, final double z, final String soundName, final float volume, final float pitch, final boolean distanceDelay) {
    }
    
    public void func_175717_a(final BlockPos p_175717_1_, final String p_175717_2_) {
        for (int var3 = 0; var3 < this.worldAccesses.size(); ++var3) {
            this.worldAccesses.get(var3).func_174961_a(p_175717_2_, p_175717_1_);
        }
    }
    
    public void spawnParticle(final EnumParticleTypes p_175688_1_, final double p_175688_2_, final double p_175688_4_, final double p_175688_6_, final double p_175688_8_, final double p_175688_10_, final double p_175688_12_, final int... p_175688_14_) {
        this.spawnParticle(p_175688_1_.func_179348_c(), p_175688_1_.func_179344_e(), p_175688_2_, p_175688_4_, p_175688_6_, p_175688_8_, p_175688_10_, p_175688_12_, p_175688_14_);
    }
    
    public void spawnParticle(final EnumParticleTypes p_175682_1_, final boolean p_175682_2_, final double p_175682_3_, final double p_175682_5_, final double p_175682_7_, final double p_175682_9_, final double p_175682_11_, final double p_175682_13_, final int... p_175682_15_) {
        this.spawnParticle(p_175682_1_.func_179348_c(), p_175682_1_.func_179344_e() | p_175682_2_, p_175682_3_, p_175682_5_, p_175682_7_, p_175682_9_, p_175682_11_, p_175682_13_, p_175682_15_);
    }
    
    private void spawnParticle(final int p_175720_1_, final boolean p_175720_2_, final double p_175720_3_, final double p_175720_5_, final double p_175720_7_, final double p_175720_9_, final double p_175720_11_, final double p_175720_13_, final int... p_175720_15_) {
        for (int var16 = 0; var16 < this.worldAccesses.size(); ++var16) {
            this.worldAccesses.get(var16).func_180442_a(p_175720_1_, p_175720_2_, p_175720_3_, p_175720_5_, p_175720_7_, p_175720_9_, p_175720_11_, p_175720_13_, p_175720_15_);
        }
    }
    
    public boolean addWeatherEffect(final Entity p_72942_1_) {
        this.weatherEffects.add(p_72942_1_);
        return true;
    }
    
    public boolean spawnEntityInWorld(final Entity p_72838_1_) {
        final int var2 = MathHelper.floor_double(p_72838_1_.posX / 16.0);
        final int var3 = MathHelper.floor_double(p_72838_1_.posZ / 16.0);
        boolean var4 = p_72838_1_.forceSpawn;
        if (p_72838_1_ instanceof EntityPlayer) {
            var4 = true;
        }
        if (!var4 && !this.isChunkLoaded(var2, var3, true)) {
            return false;
        }
        if (p_72838_1_ instanceof EntityPlayer) {
            final EntityPlayer var5 = (EntityPlayer)p_72838_1_;
            this.playerEntities.add(var5);
            this.updateAllPlayersSleepingFlag();
        }
        this.getChunkFromChunkCoords(var2, var3).addEntity(p_72838_1_);
        this.loadedEntityList.add(p_72838_1_);
        this.onEntityAdded(p_72838_1_);
        return true;
    }
    
    protected void onEntityAdded(final Entity p_72923_1_) {
        for (int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
            this.worldAccesses.get(var2).onEntityAdded(p_72923_1_);
        }
    }
    
    protected void onEntityRemoved(final Entity p_72847_1_) {
        for (int var2 = 0; var2 < this.worldAccesses.size(); ++var2) {
            this.worldAccesses.get(var2).onEntityRemoved(p_72847_1_);
        }
    }
    
    public void removeEntity(final Entity p_72900_1_) {
        if (p_72900_1_.riddenByEntity != null) {
            p_72900_1_.riddenByEntity.mountEntity(null);
        }
        if (p_72900_1_.ridingEntity != null) {
            p_72900_1_.mountEntity(null);
        }
        p_72900_1_.setDead();
        if (p_72900_1_ instanceof EntityPlayer) {
            this.playerEntities.remove(p_72900_1_);
            this.updateAllPlayersSleepingFlag();
            this.onEntityRemoved(p_72900_1_);
        }
    }
    
    public void removePlayerEntityDangerously(final Entity p_72973_1_) {
        p_72973_1_.setDead();
        if (p_72973_1_ instanceof EntityPlayer) {
            this.playerEntities.remove(p_72973_1_);
            this.updateAllPlayersSleepingFlag();
        }
        final int var2 = p_72973_1_.chunkCoordX;
        final int var3 = p_72973_1_.chunkCoordZ;
        if (p_72973_1_.addedToChunk && this.isChunkLoaded(var2, var3, true)) {
            this.getChunkFromChunkCoords(var2, var3).removeEntity(p_72973_1_);
        }
        this.loadedEntityList.remove(p_72973_1_);
        this.onEntityRemoved(p_72973_1_);
    }
    
    public void addWorldAccess(final IWorldAccess p_72954_1_) {
        this.worldAccesses.add(p_72954_1_);
    }
    
    public void removeWorldAccess(final IWorldAccess p_72848_1_) {
        this.worldAccesses.remove(p_72848_1_);
    }
    
    public List getCollidingBoundingBoxes(final Entity p_72945_1_, final AxisAlignedBB p_72945_2_) {
        final ArrayList var3 = Lists.newArrayList();
        final int var4 = MathHelper.floor_double(p_72945_2_.minX);
        final int var5 = MathHelper.floor_double(p_72945_2_.maxX + 1.0);
        final int var6 = MathHelper.floor_double(p_72945_2_.minY);
        final int var7 = MathHelper.floor_double(p_72945_2_.maxY + 1.0);
        final int var8 = MathHelper.floor_double(p_72945_2_.minZ);
        final int var9 = MathHelper.floor_double(p_72945_2_.maxZ + 1.0);
        for (int var10 = var4; var10 < var5; ++var10) {
            for (int var11 = var8; var11 < var9; ++var11) {
                if (this.isBlockLoaded(new BlockPos(var10, 64, var11))) {
                    for (int var12 = var6 - 1; var12 < var7; ++var12) {
                        final BlockPos var13 = new BlockPos(var10, var12, var11);
                        final boolean var14 = p_72945_1_.isOutsideBorder();
                        final boolean var15 = this.isInsideBorder(this.getWorldBorder(), p_72945_1_);
                        if (var14 && var15) {
                            p_72945_1_.setOutsideBorder(false);
                        }
                        else if (!var14 && !var15) {
                            p_72945_1_.setOutsideBorder(true);
                        }
                        IBlockState var16;
                        if (!this.getWorldBorder().contains(var13) && var15) {
                            var16 = Blocks.stone.getDefaultState();
                        }
                        else {
                            var16 = this.getBlockState(var13);
                        }
                        var16.getBlock().addCollisionBoxesToList(this, var13, var16, p_72945_2_, var3, p_72945_1_);
                    }
                }
            }
        }
        final double var17 = 0.25;
        final List var18 = this.getEntitiesWithinAABBExcludingEntity(p_72945_1_, p_72945_2_.expand(var17, var17, var17));
        for (int var19 = 0; var19 < var18.size(); ++var19) {
            if (p_72945_1_.riddenByEntity != var18 && p_72945_1_.ridingEntity != var18) {
                AxisAlignedBB var20 = var18.get(var19).getBoundingBox();
                if (var20 != null && var20.intersectsWith(p_72945_2_)) {
                    var3.add(var20);
                }
                var20 = p_72945_1_.getCollisionBox(var18.get(var19));
                if (var20 != null && var20.intersectsWith(p_72945_2_)) {
                    var3.add(var20);
                }
            }
        }
        return var3;
    }
    
    public boolean isInsideBorder(final WorldBorder p_175673_1_, final Entity p_175673_2_) {
        double var3 = p_175673_1_.minX();
        double var4 = p_175673_1_.minZ();
        double var5 = p_175673_1_.maxX();
        double var6 = p_175673_1_.maxZ();
        if (p_175673_2_.isOutsideBorder()) {
            ++var3;
            ++var4;
            --var5;
            --var6;
        }
        else {
            --var3;
            --var4;
            ++var5;
            ++var6;
        }
        return p_175673_2_.posX > var3 && p_175673_2_.posX < var5 && p_175673_2_.posZ > var4 && p_175673_2_.posZ < var6;
    }
    
    public List func_147461_a(final AxisAlignedBB p_147461_1_) {
        final ArrayList var2 = Lists.newArrayList();
        final int var3 = MathHelper.floor_double(p_147461_1_.minX);
        final int var4 = MathHelper.floor_double(p_147461_1_.maxX + 1.0);
        final int var5 = MathHelper.floor_double(p_147461_1_.minY);
        final int var6 = MathHelper.floor_double(p_147461_1_.maxY + 1.0);
        final int var7 = MathHelper.floor_double(p_147461_1_.minZ);
        final int var8 = MathHelper.floor_double(p_147461_1_.maxZ + 1.0);
        for (int var9 = var3; var9 < var4; ++var9) {
            for (int var10 = var7; var10 < var8; ++var10) {
                if (this.isBlockLoaded(new BlockPos(var9, 64, var10))) {
                    for (int var11 = var5 - 1; var11 < var6; ++var11) {
                        final BlockPos var12 = new BlockPos(var9, var11, var10);
                        IBlockState var13;
                        if (var9 >= -30000000 && var9 < 30000000 && var10 >= -30000000 && var10 < 30000000) {
                            var13 = this.getBlockState(var12);
                        }
                        else {
                            var13 = Blocks.bedrock.getDefaultState();
                        }
                        var13.getBlock().addCollisionBoxesToList(this, var12, var13, p_147461_1_, var2, null);
                    }
                }
            }
        }
        return var2;
    }
    
    public int calculateSkylightSubtracted(final float p_72967_1_) {
        final float var2 = this.getCelestialAngle(p_72967_1_);
        float var3 = 1.0f - (MathHelper.cos(var2 * 3.1415927f * 2.0f) * 2.0f + 0.5f);
        var3 = MathHelper.clamp_float(var3, 0.0f, 1.0f);
        var3 = 1.0f - var3;
        var3 *= (float)(1.0 - this.getRainStrength(p_72967_1_) * 5.0f / 16.0);
        var3 *= (float)(1.0 - this.getWeightedThunderStrength(p_72967_1_) * 5.0f / 16.0);
        var3 = 1.0f - var3;
        return (int)(var3 * 11.0f);
    }
    
    public float getSunBrightness(final float p_72971_1_) {
        final float var2 = this.getCelestialAngle(p_72971_1_);
        float var3 = 1.0f - (MathHelper.cos(var2 * 3.1415927f * 2.0f) * 2.0f + 0.2f);
        var3 = MathHelper.clamp_float(var3, 0.0f, 1.0f);
        var3 = 1.0f - var3;
        var3 *= (float)(1.0 - this.getRainStrength(p_72971_1_) * 5.0f / 16.0);
        var3 *= (float)(1.0 - this.getWeightedThunderStrength(p_72971_1_) * 5.0f / 16.0);
        return var3 * 0.8f + 0.2f;
    }
    
    public Vec3 getSkyColor(final Entity p_72833_1_, final float p_72833_2_) {
        final float var3 = this.getCelestialAngle(p_72833_2_);
        float var4 = MathHelper.cos(var3 * 3.1415927f * 2.0f) * 2.0f + 0.5f;
        var4 = MathHelper.clamp_float(var4, 0.0f, 1.0f);
        final int var5 = MathHelper.floor_double(p_72833_1_.posX);
        final int var6 = MathHelper.floor_double(p_72833_1_.posY);
        final int var7 = MathHelper.floor_double(p_72833_1_.posZ);
        final BlockPos var8 = new BlockPos(var5, var6, var7);
        final BiomeGenBase var9 = this.getBiomeGenForCoords(var8);
        final float var10 = var9.func_180626_a(var8);
        final int var11 = var9.getSkyColorByTemp(var10);
        float var12 = (var11 >> 16 & 0xFF) / 255.0f;
        float var13 = (var11 >> 8 & 0xFF) / 255.0f;
        float var14 = (var11 & 0xFF) / 255.0f;
        var12 *= var4;
        var13 *= var4;
        var14 *= var4;
        final float var15 = this.getRainStrength(p_72833_2_);
        if (var15 > 0.0f) {
            final float var16 = (var12 * 0.3f + var13 * 0.59f + var14 * 0.11f) * 0.6f;
            final float var17 = 1.0f - var15 * 0.75f;
            var12 = var12 * var17 + var16 * (1.0f - var17);
            var13 = var13 * var17 + var16 * (1.0f - var17);
            var14 = var14 * var17 + var16 * (1.0f - var17);
        }
        final float var16 = this.getWeightedThunderStrength(p_72833_2_);
        if (var16 > 0.0f) {
            final float var17 = (var12 * 0.3f + var13 * 0.59f + var14 * 0.11f) * 0.2f;
            final float var18 = 1.0f - var16 * 0.75f;
            var12 = var12 * var18 + var17 * (1.0f - var18);
            var13 = var13 * var18 + var17 * (1.0f - var18);
            var14 = var14 * var18 + var17 * (1.0f - var18);
        }
        if (this.lastLightningBolt > 0) {
            float var17 = this.lastLightningBolt - p_72833_2_;
            if (var17 > 1.0f) {
                var17 = 1.0f;
            }
            var17 *= 0.45f;
            var12 = var12 * (1.0f - var17) + 0.8f * var17;
            var13 = var13 * (1.0f - var17) + 0.8f * var17;
            var14 = var14 * (1.0f - var17) + 1.0f * var17;
        }
        return new Vec3(var12, var13, var14);
    }
    
    public float getCelestialAngle(final float p_72826_1_) {
        return this.provider.calculateCelestialAngle(this.worldInfo.getWorldTime(), p_72826_1_);
    }
    
    public int getMoonPhase() {
        return this.provider.getMoonPhase(this.worldInfo.getWorldTime());
    }
    
    public float getCurrentMoonPhaseFactor() {
        return WorldProvider.moonPhaseFactors[this.provider.getMoonPhase(this.worldInfo.getWorldTime())];
    }
    
    public float getCelestialAngleRadians(final float p_72929_1_) {
        final float var2 = this.getCelestialAngle(p_72929_1_);
        return var2 * 3.1415927f * 2.0f;
    }
    
    public Vec3 getCloudColour(final float p_72824_1_) {
        final float var2 = this.getCelestialAngle(p_72824_1_);
        float var3 = MathHelper.cos(var2 * 3.1415927f * 2.0f) * 2.0f + 0.5f;
        var3 = MathHelper.clamp_float(var3, 0.0f, 1.0f);
        float var4 = (this.cloudColour >> 16 & 0xFFL) / 255.0f;
        float var5 = (this.cloudColour >> 8 & 0xFFL) / 255.0f;
        float var6 = (this.cloudColour & 0xFFL) / 255.0f;
        final float var7 = this.getRainStrength(p_72824_1_);
        if (var7 > 0.0f) {
            final float var8 = (var4 * 0.3f + var5 * 0.59f + var6 * 0.11f) * 0.6f;
            final float var9 = 1.0f - var7 * 0.95f;
            var4 = var4 * var9 + var8 * (1.0f - var9);
            var5 = var5 * var9 + var8 * (1.0f - var9);
            var6 = var6 * var9 + var8 * (1.0f - var9);
        }
        var4 *= var3 * 0.9f + 0.1f;
        var5 *= var3 * 0.9f + 0.1f;
        var6 *= var3 * 0.85f + 0.15f;
        final float var8 = this.getWeightedThunderStrength(p_72824_1_);
        if (var8 > 0.0f) {
            final float var9 = (var4 * 0.3f + var5 * 0.59f + var6 * 0.11f) * 0.2f;
            final float var10 = 1.0f - var8 * 0.95f;
            var4 = var4 * var10 + var9 * (1.0f - var10);
            var5 = var5 * var10 + var9 * (1.0f - var10);
            var6 = var6 * var10 + var9 * (1.0f - var10);
        }
        return new Vec3(var4, var5, var6);
    }
    
    public Vec3 getFogColor(final float p_72948_1_) {
        final float var2 = this.getCelestialAngle(p_72948_1_);
        return this.provider.getFogColor(var2, p_72948_1_);
    }
    
    public BlockPos func_175725_q(final BlockPos p_175725_1_) {
        return this.getChunkFromBlockCoords(p_175725_1_).func_177440_h(p_175725_1_);
    }
    
    public BlockPos func_175672_r(final BlockPos p_175672_1_) {
        final Chunk var2 = this.getChunkFromBlockCoords(p_175672_1_);
        BlockPos var3;
        BlockPos var4;
        for (var3 = new BlockPos(p_175672_1_.getX(), var2.getTopFilledSegment() + 16, p_175672_1_.getZ()); var3.getY() >= 0; var3 = var4) {
            var4 = var3.offsetDown();
            final Material var5 = var2.getBlock(var4).getMaterial();
            if (var5.blocksMovement() && var5 != Material.leaves) {
                break;
            }
        }
        return var3;
    }
    
    public float getStarBrightness(final float p_72880_1_) {
        final float var2 = this.getCelestialAngle(p_72880_1_);
        float var3 = 1.0f - (MathHelper.cos(var2 * 3.1415927f * 2.0f) * 2.0f + 0.25f);
        var3 = MathHelper.clamp_float(var3, 0.0f, 1.0f);
        return var3 * var3 * 0.5f;
    }
    
    public void scheduleUpdate(final BlockPos pos, final Block blockIn, final int delay) {
    }
    
    public void func_175654_a(final BlockPos p_175654_1_, final Block p_175654_2_, final int p_175654_3_, final int p_175654_4_) {
    }
    
    public void func_180497_b(final BlockPos p_180497_1_, final Block p_180497_2_, final int p_180497_3_, final int p_180497_4_) {
    }
    
    public void updateEntities() {
        this.theProfiler.startSection("entities");
        this.theProfiler.startSection("global");
        for (int var1 = 0; var1 < this.weatherEffects.size(); ++var1) {
            final Entity var2 = this.weatherEffects.get(var1);
            try {
                final Entity entity = var2;
                ++entity.ticksExisted;
                var2.onUpdate();
            }
            catch (Throwable var4) {
                final CrashReport var3 = CrashReport.makeCrashReport(var4, "Ticking entity");
                final CrashReportCategory var5 = var3.makeCategory("Entity being ticked");
                if (var2 == null) {
                    var5.addCrashSection("Entity", "~~NULL~~");
                }
                else {
                    var2.addEntityCrashInfo(var5);
                }
                throw new ReportedException(var3);
            }
            if (var2.isDead) {
                this.weatherEffects.remove(var1--);
            }
        }
        this.theProfiler.endStartSection("remove");
        this.loadedEntityList.removeAll(this.unloadedEntityList);
        for (int var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
            final Entity var2 = this.unloadedEntityList.get(var1);
            final int var6 = var2.chunkCoordX;
            final int var7 = var2.chunkCoordZ;
            if (var2.addedToChunk && this.isChunkLoaded(var6, var7, true)) {
                this.getChunkFromChunkCoords(var6, var7).removeEntity(var2);
            }
        }
        for (int var1 = 0; var1 < this.unloadedEntityList.size(); ++var1) {
            this.onEntityRemoved(this.unloadedEntityList.get(var1));
        }
        this.unloadedEntityList.clear();
        this.theProfiler.endStartSection("regular");
        for (int var1 = 0; var1 < this.loadedEntityList.size(); ++var1) {
            final Entity var2 = this.loadedEntityList.get(var1);
            if (var2.ridingEntity != null) {
                if (!var2.ridingEntity.isDead && var2.ridingEntity.riddenByEntity == var2) {
                    continue;
                }
                var2.ridingEntity.riddenByEntity = null;
                var2.ridingEntity = null;
            }
            this.theProfiler.startSection("tick");
            if (!var2.isDead) {
                try {
                    this.updateEntity(var2);
                }
                catch (Throwable var8) {
                    final CrashReport var3 = CrashReport.makeCrashReport(var8, "Ticking entity");
                    final CrashReportCategory var5 = var3.makeCategory("Entity being ticked");
                    var2.addEntityCrashInfo(var5);
                    throw new ReportedException(var3);
                }
            }
            this.theProfiler.endSection();
            this.theProfiler.startSection("remove");
            if (var2.isDead) {
                final int var6 = var2.chunkCoordX;
                final int var7 = var2.chunkCoordZ;
                if (var2.addedToChunk && this.isChunkLoaded(var6, var7, true)) {
                    this.getChunkFromChunkCoords(var6, var7).removeEntity(var2);
                }
                this.loadedEntityList.remove(var1--);
                this.onEntityRemoved(var2);
            }
            this.theProfiler.endSection();
        }
        this.theProfiler.endStartSection("blockEntities");
        this.processingLoadedTiles = true;
        final Iterator var9 = this.tickableTileEntities.iterator();
        while (var9.hasNext()) {
            final TileEntity var10 = var9.next();
            if (!var10.isInvalid() && var10.hasWorldObj()) {
                final BlockPos var11 = var10.getPos();
                if (this.isBlockLoaded(var11) && this.worldBorder.contains(var11)) {
                    try {
                        ((IUpdatePlayerListBox)var10).update();
                    }
                    catch (Throwable var13) {
                        final CrashReport var12 = CrashReport.makeCrashReport(var13, "Ticking block entity");
                        final CrashReportCategory var14 = var12.makeCategory("Block entity being ticked");
                        var10.addInfoToCrashReport(var14);
                        throw new ReportedException(var12);
                    }
                }
            }
            if (var10.isInvalid()) {
                var9.remove();
                this.loadedTileEntityList.remove(var10);
                if (!this.isBlockLoaded(var10.getPos())) {
                    continue;
                }
                this.getChunkFromBlockCoords(var10.getPos()).removeTileEntity(var10.getPos());
            }
        }
        this.processingLoadedTiles = false;
        if (!this.tileEntitiesToBeRemoved.isEmpty()) {
            this.tickableTileEntities.removeAll(this.tileEntitiesToBeRemoved);
            this.loadedTileEntityList.removeAll(this.tileEntitiesToBeRemoved);
            this.tileEntitiesToBeRemoved.clear();
        }
        this.theProfiler.endStartSection("pendingBlockEntities");
        if (!this.addedTileEntityList.isEmpty()) {
            for (int var15 = 0; var15 < this.addedTileEntityList.size(); ++var15) {
                final TileEntity var16 = this.addedTileEntityList.get(var15);
                if (!var16.isInvalid()) {
                    if (!this.loadedTileEntityList.contains(var16)) {
                        this.addTileEntity(var16);
                    }
                    if (this.isBlockLoaded(var16.getPos())) {
                        this.getChunkFromBlockCoords(var16.getPos()).addTileEntity(var16.getPos(), var16);
                    }
                    this.markBlockForUpdate(var16.getPos());
                }
            }
            this.addedTileEntityList.clear();
        }
        this.theProfiler.endSection();
        this.theProfiler.endSection();
    }
    
    public boolean addTileEntity(final TileEntity tile) {
        final boolean var2 = this.loadedTileEntityList.add(tile);
        if (var2 && tile instanceof IUpdatePlayerListBox) {
            this.tickableTileEntities.add(tile);
        }
        return var2;
    }
    
    public void addTileEntities(final Collection tileEntityCollection) {
        if (this.processingLoadedTiles) {
            this.addedTileEntityList.addAll(tileEntityCollection);
        }
        else {
            for (final TileEntity var3 : tileEntityCollection) {
                this.loadedTileEntityList.add(var3);
                if (var3 instanceof IUpdatePlayerListBox) {
                    this.tickableTileEntities.add(var3);
                }
            }
        }
    }
    
    public void updateEntity(final Entity ent) {
        this.updateEntityWithOptionalForce(ent, true);
    }
    
    public void updateEntityWithOptionalForce(final Entity p_72866_1_, final boolean p_72866_2_) {
        final int var3 = MathHelper.floor_double(p_72866_1_.posX);
        final int var4 = MathHelper.floor_double(p_72866_1_.posZ);
        final byte var5 = 32;
        if (!p_72866_2_ || this.isAreaLoaded(var3 - var5, 0, var4 - var5, var3 + var5, 0, var4 + var5, true)) {
            p_72866_1_.lastTickPosX = p_72866_1_.posX;
            p_72866_1_.lastTickPosY = p_72866_1_.posY;
            p_72866_1_.lastTickPosZ = p_72866_1_.posZ;
            p_72866_1_.prevRotationYaw = p_72866_1_.rotationYaw;
            p_72866_1_.prevRotationPitch = p_72866_1_.rotationPitch;
            if (p_72866_2_ && p_72866_1_.addedToChunk) {
                ++p_72866_1_.ticksExisted;
                if (p_72866_1_.ridingEntity != null) {
                    p_72866_1_.updateRidden();
                }
                else {
                    p_72866_1_.onUpdate();
                }
            }
            this.theProfiler.startSection("chunkCheck");
            if (Double.isNaN(p_72866_1_.posX) || Double.isInfinite(p_72866_1_.posX)) {
                p_72866_1_.posX = p_72866_1_.lastTickPosX;
            }
            if (Double.isNaN(p_72866_1_.posY) || Double.isInfinite(p_72866_1_.posY)) {
                p_72866_1_.posY = p_72866_1_.lastTickPosY;
            }
            if (Double.isNaN(p_72866_1_.posZ) || Double.isInfinite(p_72866_1_.posZ)) {
                p_72866_1_.posZ = p_72866_1_.lastTickPosZ;
            }
            if (Double.isNaN(p_72866_1_.rotationPitch) || Double.isInfinite(p_72866_1_.rotationPitch)) {
                p_72866_1_.rotationPitch = p_72866_1_.prevRotationPitch;
            }
            if (Double.isNaN(p_72866_1_.rotationYaw) || Double.isInfinite(p_72866_1_.rotationYaw)) {
                p_72866_1_.rotationYaw = p_72866_1_.prevRotationYaw;
            }
            final int var6 = MathHelper.floor_double(p_72866_1_.posX / 16.0);
            final int var7 = MathHelper.floor_double(p_72866_1_.posY / 16.0);
            final int var8 = MathHelper.floor_double(p_72866_1_.posZ / 16.0);
            if (!p_72866_1_.addedToChunk || p_72866_1_.chunkCoordX != var6 || p_72866_1_.chunkCoordY != var7 || p_72866_1_.chunkCoordZ != var8) {
                if (p_72866_1_.addedToChunk && this.isChunkLoaded(p_72866_1_.chunkCoordX, p_72866_1_.chunkCoordZ, true)) {
                    this.getChunkFromChunkCoords(p_72866_1_.chunkCoordX, p_72866_1_.chunkCoordZ).removeEntityAtIndex(p_72866_1_, p_72866_1_.chunkCoordY);
                }
                if (this.isChunkLoaded(var6, var8, true)) {
                    p_72866_1_.addedToChunk = true;
                    this.getChunkFromChunkCoords(var6, var8).addEntity(p_72866_1_);
                }
                else {
                    p_72866_1_.addedToChunk = false;
                }
            }
            this.theProfiler.endSection();
            if (p_72866_2_ && p_72866_1_.addedToChunk && p_72866_1_.riddenByEntity != null) {
                if (!p_72866_1_.riddenByEntity.isDead && p_72866_1_.riddenByEntity.ridingEntity == p_72866_1_) {
                    this.updateEntity(p_72866_1_.riddenByEntity);
                }
                else {
                    p_72866_1_.riddenByEntity.ridingEntity = null;
                    p_72866_1_.riddenByEntity = null;
                }
            }
        }
    }
    
    public boolean checkNoEntityCollision(final AxisAlignedBB p_72855_1_) {
        return this.checkNoEntityCollision(p_72855_1_, null);
    }
    
    public boolean checkNoEntityCollision(final AxisAlignedBB p_72917_1_, final Entity p_72917_2_) {
        final List var3 = this.getEntitiesWithinAABBExcludingEntity(null, p_72917_1_);
        for (int var4 = 0; var4 < var3.size(); ++var4) {
            final Entity var5 = var3.get(var4);
            if (!var5.isDead && var5.preventEntitySpawning && var5 != p_72917_2_ && (p_72917_2_ == null || (p_72917_2_.ridingEntity != var5 && p_72917_2_.riddenByEntity != var5))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean checkBlockCollision(final AxisAlignedBB p_72829_1_) {
        final int var2 = MathHelper.floor_double(p_72829_1_.minX);
        final int var3 = MathHelper.floor_double(p_72829_1_.maxX);
        final int var4 = MathHelper.floor_double(p_72829_1_.minY);
        final int var5 = MathHelper.floor_double(p_72829_1_.maxY);
        final int var6 = MathHelper.floor_double(p_72829_1_.minZ);
        final int var7 = MathHelper.floor_double(p_72829_1_.maxZ);
        for (int var8 = var2; var8 <= var3; ++var8) {
            for (int var9 = var4; var9 <= var5; ++var9) {
                for (int var10 = var6; var10 <= var7; ++var10) {
                    final Block var11 = this.getBlockState(new BlockPos(var8, var9, var10)).getBlock();
                    if (var11.getMaterial() != Material.air) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isAnyLiquid(final AxisAlignedBB p_72953_1_) {
        final int var2 = MathHelper.floor_double(p_72953_1_.minX);
        final int var3 = MathHelper.floor_double(p_72953_1_.maxX);
        final int var4 = MathHelper.floor_double(p_72953_1_.minY);
        final int var5 = MathHelper.floor_double(p_72953_1_.maxY);
        final int var6 = MathHelper.floor_double(p_72953_1_.minZ);
        final int var7 = MathHelper.floor_double(p_72953_1_.maxZ);
        for (int var8 = var2; var8 <= var3; ++var8) {
            for (int var9 = var4; var9 <= var5; ++var9) {
                for (int var10 = var6; var10 <= var7; ++var10) {
                    final Block var11 = this.getBlockState(new BlockPos(var8, var9, var10)).getBlock();
                    if (var11.getMaterial().isLiquid()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean func_147470_e(final AxisAlignedBB p_147470_1_) {
        final int var2 = MathHelper.floor_double(p_147470_1_.minX);
        final int var3 = MathHelper.floor_double(p_147470_1_.maxX + 1.0);
        final int var4 = MathHelper.floor_double(p_147470_1_.minY);
        final int var5 = MathHelper.floor_double(p_147470_1_.maxY + 1.0);
        final int var6 = MathHelper.floor_double(p_147470_1_.minZ);
        final int var7 = MathHelper.floor_double(p_147470_1_.maxZ + 1.0);
        if (this.isAreaLoaded(var2, var4, var6, var3, var5, var7, true)) {
            for (int var8 = var2; var8 < var3; ++var8) {
                for (int var9 = var4; var9 < var5; ++var9) {
                    for (int var10 = var6; var10 < var7; ++var10) {
                        final Block var11 = this.getBlockState(new BlockPos(var8, var9, var10)).getBlock();
                        if (var11 == Blocks.fire || var11 == Blocks.flowing_lava || var11 == Blocks.lava) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean handleMaterialAcceleration(final AxisAlignedBB p_72918_1_, final Material p_72918_2_, final Entity p_72918_3_) {
        final int var4 = MathHelper.floor_double(p_72918_1_.minX);
        final int var5 = MathHelper.floor_double(p_72918_1_.maxX + 1.0);
        final int var6 = MathHelper.floor_double(p_72918_1_.minY);
        final int var7 = MathHelper.floor_double(p_72918_1_.maxY + 1.0);
        final int var8 = MathHelper.floor_double(p_72918_1_.minZ);
        final int var9 = MathHelper.floor_double(p_72918_1_.maxZ + 1.0);
        if (!this.isAreaLoaded(var4, var6, var8, var5, var7, var9, true)) {
            return false;
        }
        boolean var10 = false;
        Vec3 var11 = new Vec3(0.0, 0.0, 0.0);
        for (int var12 = var4; var12 < var5; ++var12) {
            for (int var13 = var6; var13 < var7; ++var13) {
                for (int var14 = var8; var14 < var9; ++var14) {
                    final BlockPos var15 = new BlockPos(var12, var13, var14);
                    final IBlockState var16 = this.getBlockState(var15);
                    final Block var17 = var16.getBlock();
                    if (var17.getMaterial() == p_72918_2_) {
                        final double var18 = var13 + 1 - BlockLiquid.getLiquidHeightPercent((int)var16.getValue(BlockLiquid.LEVEL));
                        if (var7 >= var18) {
                            var10 = true;
                            var11 = var17.modifyAcceleration(this, var15, p_72918_3_, var11);
                        }
                    }
                }
            }
        }
        if (var11.lengthVector() > 0.0 && p_72918_3_.isPushedByWater()) {
            var11 = var11.normalize();
            final double var19 = 0.014;
            p_72918_3_.motionX += var11.xCoord * var19;
            p_72918_3_.motionY += var11.yCoord * var19;
            p_72918_3_.motionZ += var11.zCoord * var19;
        }
        return var10;
    }
    
    public boolean isMaterialInBB(final AxisAlignedBB p_72875_1_, final Material p_72875_2_) {
        final int var3 = MathHelper.floor_double(p_72875_1_.minX);
        final int var4 = MathHelper.floor_double(p_72875_1_.maxX + 1.0);
        final int var5 = MathHelper.floor_double(p_72875_1_.minY);
        final int var6 = MathHelper.floor_double(p_72875_1_.maxY + 1.0);
        final int var7 = MathHelper.floor_double(p_72875_1_.minZ);
        final int var8 = MathHelper.floor_double(p_72875_1_.maxZ + 1.0);
        for (int var9 = var3; var9 < var4; ++var9) {
            for (int var10 = var5; var10 < var6; ++var10) {
                for (int var11 = var7; var11 < var8; ++var11) {
                    if (this.getBlockState(new BlockPos(var9, var10, var11)).getBlock().getMaterial() == p_72875_2_) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isAABBInMaterial(final AxisAlignedBB p_72830_1_, final Material p_72830_2_) {
        final int var3 = MathHelper.floor_double(p_72830_1_.minX);
        final int var4 = MathHelper.floor_double(p_72830_1_.maxX + 1.0);
        final int var5 = MathHelper.floor_double(p_72830_1_.minY);
        final int var6 = MathHelper.floor_double(p_72830_1_.maxY + 1.0);
        final int var7 = MathHelper.floor_double(p_72830_1_.minZ);
        final int var8 = MathHelper.floor_double(p_72830_1_.maxZ + 1.0);
        for (int var9 = var3; var9 < var4; ++var9) {
            for (int var10 = var5; var10 < var6; ++var10) {
                for (int var11 = var7; var11 < var8; ++var11) {
                    final BlockPos var12 = new BlockPos(var9, var10, var11);
                    final IBlockState var13 = this.getBlockState(var12);
                    final Block var14 = var13.getBlock();
                    if (var14.getMaterial() == p_72830_2_) {
                        final int var15 = (int)var13.getValue(BlockLiquid.LEVEL);
                        double var16 = var10 + 1;
                        if (var15 < 8) {
                            var16 = var10 + 1 - var15 / 8.0;
                        }
                        if (var16 >= p_72830_1_.minY) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public Explosion createExplosion(final Entity p_72876_1_, final double p_72876_2_, final double p_72876_4_, final double p_72876_6_, final float p_72876_8_, final boolean p_72876_9_) {
        return this.newExplosion(p_72876_1_, p_72876_2_, p_72876_4_, p_72876_6_, p_72876_8_, false, p_72876_9_);
    }
    
    public Explosion newExplosion(final Entity p_72885_1_, final double p_72885_2_, final double p_72885_4_, final double p_72885_6_, final float p_72885_8_, final boolean p_72885_9_, final boolean p_72885_10_) {
        final Explosion var11 = new Explosion(this, p_72885_1_, p_72885_2_, p_72885_4_, p_72885_6_, p_72885_8_, p_72885_9_, p_72885_10_);
        var11.doExplosionA();
        var11.doExplosionB(true);
        return var11;
    }
    
    public float getBlockDensity(final Vec3 p_72842_1_, final AxisAlignedBB p_72842_2_) {
        final double var3 = 1.0 / ((p_72842_2_.maxX - p_72842_2_.minX) * 2.0 + 1.0);
        final double var4 = 1.0 / ((p_72842_2_.maxY - p_72842_2_.minY) * 2.0 + 1.0);
        final double var5 = 1.0 / ((p_72842_2_.maxZ - p_72842_2_.minZ) * 2.0 + 1.0);
        if (var3 >= 0.0 && var4 >= 0.0 && var5 >= 0.0) {
            int var6 = 0;
            int var7 = 0;
            for (float var8 = 0.0f; var8 <= 1.0f; var8 += (float)var3) {
                for (float var9 = 0.0f; var9 <= 1.0f; var9 += (float)var4) {
                    for (float var10 = 0.0f; var10 <= 1.0f; var10 += (float)var5) {
                        final double var11 = p_72842_2_.minX + (p_72842_2_.maxX - p_72842_2_.minX) * var8;
                        final double var12 = p_72842_2_.minY + (p_72842_2_.maxY - p_72842_2_.minY) * var9;
                        final double var13 = p_72842_2_.minZ + (p_72842_2_.maxZ - p_72842_2_.minZ) * var10;
                        if (this.rayTraceBlocks(new Vec3(var11, var12, var13), p_72842_1_) == null) {
                            ++var6;
                        }
                        ++var7;
                    }
                }
            }
            return var6 / var7;
        }
        return 0.0f;
    }
    
    public boolean func_175719_a(final EntityPlayer p_175719_1_, BlockPos p_175719_2_, final EnumFacing p_175719_3_) {
        p_175719_2_ = p_175719_2_.offset(p_175719_3_);
        if (this.getBlockState(p_175719_2_).getBlock() == Blocks.fire) {
            this.playAuxSFXAtEntity(p_175719_1_, 1004, p_175719_2_, 0);
            this.setBlockToAir(p_175719_2_);
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
    
    @Override
    public TileEntity getTileEntity(final BlockPos pos) {
        if (!this.isValid(pos)) {
            return null;
        }
        TileEntity var2 = null;
        if (this.processingLoadedTiles) {
            for (int var3 = 0; var3 < this.addedTileEntityList.size(); ++var3) {
                final TileEntity var4 = this.addedTileEntityList.get(var3);
                if (!var4.isInvalid() && var4.getPos().equals(pos)) {
                    var2 = var4;
                    break;
                }
            }
        }
        if (var2 == null) {
            var2 = this.getChunkFromBlockCoords(pos).func_177424_a(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
        }
        if (var2 == null) {
            for (int var3 = 0; var3 < this.addedTileEntityList.size(); ++var3) {
                final TileEntity var4 = this.addedTileEntityList.get(var3);
                if (!var4.isInvalid() && var4.getPos().equals(pos)) {
                    var2 = var4;
                    break;
                }
            }
        }
        return var2;
    }
    
    public void setTileEntity(final BlockPos p_175690_1_, final TileEntity p_175690_2_) {
        if (p_175690_2_ != null && !p_175690_2_.isInvalid()) {
            if (this.processingLoadedTiles) {
                p_175690_2_.setPos(p_175690_1_);
                final Iterator var3 = this.addedTileEntityList.iterator();
                while (var3.hasNext()) {
                    final TileEntity var4 = var3.next();
                    if (var4.getPos().equals(p_175690_1_)) {
                        var4.invalidate();
                        var3.remove();
                    }
                }
                this.addedTileEntityList.add(p_175690_2_);
            }
            else {
                this.addTileEntity(p_175690_2_);
                this.getChunkFromBlockCoords(p_175690_1_).addTileEntity(p_175690_1_, p_175690_2_);
            }
        }
    }
    
    public void removeTileEntity(final BlockPos pos) {
        final TileEntity var2 = this.getTileEntity(pos);
        if (var2 != null && this.processingLoadedTiles) {
            var2.invalidate();
            this.addedTileEntityList.remove(var2);
        }
        else {
            if (var2 != null) {
                this.addedTileEntityList.remove(var2);
                this.loadedTileEntityList.remove(var2);
                this.tickableTileEntities.remove(var2);
            }
            this.getChunkFromBlockCoords(pos).removeTileEntity(pos);
        }
    }
    
    public void markTileEntityForRemoval(final TileEntity tileEntityIn) {
        this.tileEntitiesToBeRemoved.add(tileEntityIn);
    }
    
    public boolean func_175665_u(final BlockPos p_175665_1_) {
        final IBlockState var2 = this.getBlockState(p_175665_1_);
        final AxisAlignedBB var3 = var2.getBlock().getCollisionBoundingBox(this, p_175665_1_, var2);
        return var3 != null && var3.getAverageEdgeLength() >= 1.0;
    }
    
    public static boolean doesBlockHaveSolidTopSurface(final IBlockAccess p_175683_0_, final BlockPos p_175683_1_) {
        final IBlockState var2 = p_175683_0_.getBlockState(p_175683_1_);
        final Block var3 = var2.getBlock();
        return (var3.getMaterial().isOpaque() && var3.isFullCube()) || ((var3 instanceof BlockStairs) ? (var2.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP) : ((var3 instanceof BlockSlab) ? (var2.getValue(BlockSlab.HALF_PROP) == BlockSlab.EnumBlockHalf.TOP) : (var3 instanceof BlockHopper || (var3 instanceof BlockSnow && (int)var2.getValue(BlockSnow.LAYERS_PROP) == 7))));
    }
    
    public boolean func_175677_d(final BlockPos p_175677_1_, final boolean p_175677_2_) {
        if (!this.isValid(p_175677_1_)) {
            return p_175677_2_;
        }
        final Chunk var3 = this.chunkProvider.func_177459_a(p_175677_1_);
        if (var3.isEmpty()) {
            return p_175677_2_;
        }
        final Block var4 = this.getBlockState(p_175677_1_).getBlock();
        return var4.getMaterial().isOpaque() && var4.isFullCube();
    }
    
    public void calculateInitialSkylight() {
        final int var1 = this.calculateSkylightSubtracted(1.0f);
        if (var1 != this.skylightSubtracted) {
            this.skylightSubtracted = var1;
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
        if (!this.provider.getHasNoSky() && !this.isRemote) {
            int var1 = this.worldInfo.func_176133_A();
            if (var1 > 0) {
                --var1;
                this.worldInfo.func_176142_i(var1);
                this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
                this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
            }
            int var2 = this.worldInfo.getThunderTime();
            if (var2 <= 0) {
                if (this.worldInfo.isThundering()) {
                    this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
                }
                else {
                    this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
                }
            }
            else {
                --var2;
                this.worldInfo.setThunderTime(var2);
                if (var2 <= 0) {
                    this.worldInfo.setThundering(!this.worldInfo.isThundering());
                }
            }
            this.prevThunderingStrength = this.thunderingStrength;
            if (this.worldInfo.isThundering()) {
                this.thunderingStrength += 0.01;
            }
            else {
                this.thunderingStrength -= 0.01;
            }
            this.thunderingStrength = MathHelper.clamp_float(this.thunderingStrength, 0.0f, 1.0f);
            int var3 = this.worldInfo.getRainTime();
            if (var3 <= 0) {
                if (this.worldInfo.isRaining()) {
                    this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
                }
                else {
                    this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
                }
            }
            else {
                --var3;
                this.worldInfo.setRainTime(var3);
                if (var3 <= 0) {
                    this.worldInfo.setRaining(!this.worldInfo.isRaining());
                }
            }
            this.prevRainingStrength = this.rainingStrength;
            if (this.worldInfo.isRaining()) {
                this.rainingStrength += 0.01;
            }
            else {
                this.rainingStrength -= 0.01;
            }
            this.rainingStrength = MathHelper.clamp_float(this.rainingStrength, 0.0f, 1.0f);
        }
    }
    
    protected void setActivePlayerChunksAndCheckLight() {
        this.activeChunkSet.clear();
        this.theProfiler.startSection("buildList");
        for (int var1 = 0; var1 < this.playerEntities.size(); ++var1) {
            final EntityPlayer var2 = this.playerEntities.get(var1);
            final int var3 = MathHelper.floor_double(var2.posX / 16.0);
            final int var4 = MathHelper.floor_double(var2.posZ / 16.0);
            for (int var5 = this.getRenderDistanceChunks(), var6 = -var5; var6 <= var5; ++var6) {
                for (int var7 = -var5; var7 <= var5; ++var7) {
                    this.activeChunkSet.add(new ChunkCoordIntPair(var6 + var3, var7 + var4));
                }
            }
        }
        this.theProfiler.endSection();
        if (this.ambientTickCountdown > 0) {
            --this.ambientTickCountdown;
        }
        this.theProfiler.startSection("playerCheckLight");
        if (!this.playerEntities.isEmpty()) {
            final int var1 = this.rand.nextInt(this.playerEntities.size());
            final EntityPlayer var2 = this.playerEntities.get(var1);
            final int var3 = MathHelper.floor_double(var2.posX) + this.rand.nextInt(11) - 5;
            final int var4 = MathHelper.floor_double(var2.posY) + this.rand.nextInt(11) - 5;
            final int var5 = MathHelper.floor_double(var2.posZ) + this.rand.nextInt(11) - 5;
            this.checkLight(new BlockPos(var3, var4, var5));
        }
        this.theProfiler.endSection();
    }
    
    protected abstract int getRenderDistanceChunks();
    
    protected void func_147467_a(final int p_147467_1_, final int p_147467_2_, final Chunk p_147467_3_) {
        this.theProfiler.endStartSection("moodSound");
        if (this.ambientTickCountdown == 0 && !this.isRemote) {
            this.updateLCG = this.updateLCG * 3 + 1013904223;
            final int var4 = this.updateLCG >> 2;
            int var5 = var4 & 0xF;
            int var6 = var4 >> 8 & 0xF;
            final int var7 = var4 >> 16 & 0xFF;
            final BlockPos var8 = new BlockPos(var5, var7, var6);
            final Block var9 = p_147467_3_.getBlock(var8);
            var5 += p_147467_1_;
            var6 += p_147467_2_;
            if (var9.getMaterial() == Material.air && this.getLight(var8) <= this.rand.nextInt(8) && this.getLightFor(EnumSkyBlock.SKY, var8) <= 0) {
                final EntityPlayer var10 = this.getClosestPlayer(var5 + 0.5, var7 + 0.5, var6 + 0.5, 8.0);
                if (var10 != null && var10.getDistanceSq(var5 + 0.5, var7 + 0.5, var6 + 0.5) > 4.0) {
                    this.playSoundEffect(var5 + 0.5, var7 + 0.5, var6 + 0.5, "ambient.cave.cave", 0.7f, 0.8f + this.rand.nextFloat() * 0.2f);
                    this.ambientTickCountdown = this.rand.nextInt(12000) + 6000;
                }
            }
        }
        this.theProfiler.endStartSection("checkLight");
        p_147467_3_.enqueueRelightChecks();
    }
    
    protected void func_147456_g() {
        this.setActivePlayerChunksAndCheckLight();
    }
    
    public void func_175637_a(final Block p_175637_1_, final BlockPos p_175637_2_, final Random p_175637_3_) {
        this.scheduledUpdatesAreImmediate = true;
        p_175637_1_.updateTick(this, p_175637_2_, this.getBlockState(p_175637_2_), p_175637_3_);
        this.scheduledUpdatesAreImmediate = false;
    }
    
    public boolean func_175675_v(final BlockPos p_175675_1_) {
        return this.func_175670_e(p_175675_1_, false);
    }
    
    public boolean func_175662_w(final BlockPos p_175662_1_) {
        return this.func_175670_e(p_175662_1_, true);
    }
    
    public boolean func_175670_e(final BlockPos p_175670_1_, final boolean p_175670_2_) {
        final BiomeGenBase var3 = this.getBiomeGenForCoords(p_175670_1_);
        final float var4 = var3.func_180626_a(p_175670_1_);
        if (var4 > 0.15f) {
            return false;
        }
        if (p_175670_1_.getY() >= 0 && p_175670_1_.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, p_175670_1_) < 10) {
            final IBlockState var5 = this.getBlockState(p_175670_1_);
            final Block var6 = var5.getBlock();
            if ((var6 == Blocks.water || var6 == Blocks.flowing_water) && (int)var5.getValue(BlockLiquid.LEVEL) == 0) {
                if (!p_175670_2_) {
                    return true;
                }
                final boolean var7 = this.func_175696_F(p_175670_1_.offsetWest()) && this.func_175696_F(p_175670_1_.offsetEast()) && this.func_175696_F(p_175670_1_.offsetNorth()) && this.func_175696_F(p_175670_1_.offsetSouth());
                if (!var7) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean func_175696_F(final BlockPos p_175696_1_) {
        return this.getBlockState(p_175696_1_).getBlock().getMaterial() == Material.water;
    }
    
    public boolean func_175708_f(final BlockPos p_175708_1_, final boolean p_175708_2_) {
        final BiomeGenBase var3 = this.getBiomeGenForCoords(p_175708_1_);
        final float var4 = var3.func_180626_a(p_175708_1_);
        if (var4 > 0.15f) {
            return false;
        }
        if (!p_175708_2_) {
            return true;
        }
        if (p_175708_1_.getY() >= 0 && p_175708_1_.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, p_175708_1_) < 10) {
            final Block var5 = this.getBlockState(p_175708_1_).getBlock();
            if (var5.getMaterial() == Material.air && Blocks.snow_layer.canPlaceBlockAt(this, p_175708_1_)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean checkLight(final BlockPos p_175664_1_) {
        boolean var2 = false;
        if (!this.provider.getHasNoSky()) {
            var2 |= this.checkLightFor(EnumSkyBlock.SKY, p_175664_1_);
        }
        var2 |= this.checkLightFor(EnumSkyBlock.BLOCK, p_175664_1_);
        return var2;
    }
    
    private int func_175638_a(final BlockPos p_175638_1_, final EnumSkyBlock p_175638_2_) {
        if (p_175638_2_ == EnumSkyBlock.SKY && this.isAgainstSky(p_175638_1_)) {
            return 15;
        }
        final Block var3 = this.getBlockState(p_175638_1_).getBlock();
        int var4 = (p_175638_2_ == EnumSkyBlock.SKY) ? 0 : var3.getLightValue();
        int var5 = var3.getLightOpacity();
        if (var5 >= 15 && var3.getLightValue() > 0) {
            var5 = 1;
        }
        if (var5 < 1) {
            var5 = 1;
        }
        if (var5 >= 15) {
            return 0;
        }
        if (var4 >= 14) {
            return var4;
        }
        for (final EnumFacing var9 : EnumFacing.values()) {
            final BlockPos var10 = p_175638_1_.offset(var9);
            final int var11 = this.getLightFor(p_175638_2_, var10) - var5;
            if (var11 > var4) {
                var4 = var11;
            }
            if (var4 >= 14) {
                return var4;
            }
        }
        return var4;
    }
    
    public boolean checkLightFor(final EnumSkyBlock p_180500_1_, final BlockPos p_180500_2_) {
        if (!this.isAreaLoaded(p_180500_2_, 17, false)) {
            return false;
        }
        int var3 = 0;
        int var4 = 0;
        this.theProfiler.startSection("getBrightness");
        final int var5 = this.getLightFor(p_180500_1_, p_180500_2_);
        final int var6 = this.func_175638_a(p_180500_2_, p_180500_1_);
        final int var7 = p_180500_2_.getX();
        final int var8 = p_180500_2_.getY();
        final int var9 = p_180500_2_.getZ();
        if (var6 > var5) {
            this.lightUpdateBlockList[var4++] = 133152;
        }
        else if (var6 < var5) {
            this.lightUpdateBlockList[var4++] = (0x20820 | var5 << 18);
            while (var3 < var4) {
                final int var10 = this.lightUpdateBlockList[var3++];
                final int var11 = (var10 & 0x3F) - 32 + var7;
                final int var12 = (var10 >> 6 & 0x3F) - 32 + var8;
                final int var13 = (var10 >> 12 & 0x3F) - 32 + var9;
                final int var14 = var10 >> 18 & 0xF;
                final BlockPos var15 = new BlockPos(var11, var12, var13);
                int var16 = this.getLightFor(p_180500_1_, var15);
                if (var16 == var14) {
                    this.setLightFor(p_180500_1_, var15, 0);
                    if (var14 <= 0) {
                        continue;
                    }
                    final int var17 = MathHelper.abs_int(var11 - var7);
                    final int var18 = MathHelper.abs_int(var12 - var8);
                    final int var19 = MathHelper.abs_int(var13 - var9);
                    if (var17 + var18 + var19 >= 17) {
                        continue;
                    }
                    for (final EnumFacing var23 : EnumFacing.values()) {
                        final int var24 = var11 + var23.getFrontOffsetX();
                        final int var25 = var12 + var23.getFrontOffsetY();
                        final int var26 = var13 + var23.getFrontOffsetZ();
                        final BlockPos var27 = new BlockPos(var24, var25, var26);
                        final int var28 = Math.max(1, this.getBlockState(var27).getBlock().getLightOpacity());
                        var16 = this.getLightFor(p_180500_1_, var27);
                        if (var16 == var14 - var28 && var4 < this.lightUpdateBlockList.length) {
                            this.lightUpdateBlockList[var4++] = (var24 - var7 + 32 | var25 - var8 + 32 << 6 | var26 - var9 + 32 << 12 | var14 - var28 << 18);
                        }
                    }
                }
            }
            var3 = 0;
        }
        this.theProfiler.endSection();
        this.theProfiler.startSection("checkedPosition < toCheckCount");
        while (var3 < var4) {
            final int var10 = this.lightUpdateBlockList[var3++];
            final int var11 = (var10 & 0x3F) - 32 + var7;
            final int var12 = (var10 >> 6 & 0x3F) - 32 + var8;
            final int var13 = (var10 >> 12 & 0x3F) - 32 + var9;
            final BlockPos var29 = new BlockPos(var11, var12, var13);
            final int var30 = this.getLightFor(p_180500_1_, var29);
            final int var16 = this.func_175638_a(var29, p_180500_1_);
            if (var16 != var30) {
                this.setLightFor(p_180500_1_, var29, var16);
                if (var16 <= var30) {
                    continue;
                }
                final int var17 = Math.abs(var11 - var7);
                final int var18 = Math.abs(var12 - var8);
                final int var19 = Math.abs(var13 - var9);
                final boolean var31 = var4 < this.lightUpdateBlockList.length - 6;
                if (var17 + var18 + var19 >= 17 || !var31) {
                    continue;
                }
                if (this.getLightFor(p_180500_1_, var29.offsetWest()) < var16) {
                    this.lightUpdateBlockList[var4++] = var11 - 1 - var7 + 32 + (var12 - var8 + 32 << 6) + (var13 - var9 + 32 << 12);
                }
                if (this.getLightFor(p_180500_1_, var29.offsetEast()) < var16) {
                    this.lightUpdateBlockList[var4++] = var11 + 1 - var7 + 32 + (var12 - var8 + 32 << 6) + (var13 - var9 + 32 << 12);
                }
                if (this.getLightFor(p_180500_1_, var29.offsetDown()) < var16) {
                    this.lightUpdateBlockList[var4++] = var11 - var7 + 32 + (var12 - 1 - var8 + 32 << 6) + (var13 - var9 + 32 << 12);
                }
                if (this.getLightFor(p_180500_1_, var29.offsetUp()) < var16) {
                    this.lightUpdateBlockList[var4++] = var11 - var7 + 32 + (var12 + 1 - var8 + 32 << 6) + (var13 - var9 + 32 << 12);
                }
                if (this.getLightFor(p_180500_1_, var29.offsetNorth()) < var16) {
                    this.lightUpdateBlockList[var4++] = var11 - var7 + 32 + (var12 - var8 + 32 << 6) + (var13 - 1 - var9 + 32 << 12);
                }
                if (this.getLightFor(p_180500_1_, var29.offsetSouth()) >= var16) {
                    continue;
                }
                this.lightUpdateBlockList[var4++] = var11 - var7 + 32 + (var12 - var8 + 32 << 6) + (var13 + 1 - var9 + 32 << 12);
            }
        }
        this.theProfiler.endSection();
        return true;
    }
    
    public boolean tickUpdates(final boolean p_72955_1_) {
        return false;
    }
    
    public List getPendingBlockUpdates(final Chunk p_72920_1_, final boolean p_72920_2_) {
        return null;
    }
    
    public List func_175712_a(final StructureBoundingBox p_175712_1_, final boolean p_175712_2_) {
        return null;
    }
    
    public List getEntitiesWithinAABBExcludingEntity(final Entity p_72839_1_, final AxisAlignedBB p_72839_2_) {
        return this.func_175674_a(p_72839_1_, p_72839_2_, IEntitySelector.field_180132_d);
    }
    
    public List func_175674_a(final Entity p_175674_1_, final AxisAlignedBB p_175674_2_, final Predicate p_175674_3_) {
        final ArrayList var4 = Lists.newArrayList();
        final int var5 = MathHelper.floor_double((p_175674_2_.minX - 2.0) / 16.0);
        final int var6 = MathHelper.floor_double((p_175674_2_.maxX + 2.0) / 16.0);
        final int var7 = MathHelper.floor_double((p_175674_2_.minZ - 2.0) / 16.0);
        final int var8 = MathHelper.floor_double((p_175674_2_.maxZ + 2.0) / 16.0);
        for (int var9 = var5; var9 <= var6; ++var9) {
            for (int var10 = var7; var10 <= var8; ++var10) {
                if (this.isChunkLoaded(var9, var10, true)) {
                    this.getChunkFromChunkCoords(var9, var10).func_177414_a(p_175674_1_, p_175674_2_, var4, p_175674_3_);
                }
            }
        }
        return var4;
    }
    
    public List func_175644_a(final Class p_175644_1_, final Predicate p_175644_2_) {
        final ArrayList var3 = Lists.newArrayList();
        for (final Entity var5 : this.loadedEntityList) {
            if (p_175644_1_.isAssignableFrom(var5.getClass()) && p_175644_2_.apply((Object)var5)) {
                var3.add(var5);
            }
        }
        return var3;
    }
    
    public List func_175661_b(final Class p_175661_1_, final Predicate p_175661_2_) {
        final ArrayList var3 = Lists.newArrayList();
        for (final Entity var5 : this.playerEntities) {
            if (p_175661_1_.isAssignableFrom(var5.getClass()) && p_175661_2_.apply((Object)var5)) {
                var3.add(var5);
            }
        }
        return var3;
    }
    
    public List getEntitiesWithinAABB(final Class p_72872_1_, final AxisAlignedBB p_72872_2_) {
        return this.func_175647_a(p_72872_1_, p_72872_2_, IEntitySelector.field_180132_d);
    }
    
    public List func_175647_a(final Class p_175647_1_, final AxisAlignedBB p_175647_2_, final Predicate p_175647_3_) {
        final int var4 = MathHelper.floor_double((p_175647_2_.minX - 2.0) / 16.0);
        final int var5 = MathHelper.floor_double((p_175647_2_.maxX + 2.0) / 16.0);
        final int var6 = MathHelper.floor_double((p_175647_2_.minZ - 2.0) / 16.0);
        final int var7 = MathHelper.floor_double((p_175647_2_.maxZ + 2.0) / 16.0);
        final ArrayList var8 = Lists.newArrayList();
        for (int var9 = var4; var9 <= var5; ++var9) {
            for (int var10 = var6; var10 <= var7; ++var10) {
                if (this.isChunkLoaded(var9, var10, true)) {
                    this.getChunkFromChunkCoords(var9, var10).func_177430_a(p_175647_1_, p_175647_2_, var8, p_175647_3_);
                }
            }
        }
        return var8;
    }
    
    public Entity findNearestEntityWithinAABB(final Class p_72857_1_, final AxisAlignedBB p_72857_2_, final Entity p_72857_3_) {
        final List var4 = this.getEntitiesWithinAABB(p_72857_1_, p_72857_2_);
        Entity var5 = null;
        double var6 = Double.MAX_VALUE;
        for (int var7 = 0; var7 < var4.size(); ++var7) {
            final Entity var8 = var4.get(var7);
            if (var8 != p_72857_3_ && IEntitySelector.field_180132_d.apply((Object)var8)) {
                final double var9 = p_72857_3_.getDistanceSqToEntity(var8);
                if (var9 <= var6) {
                    var5 = var8;
                    var6 = var9;
                }
            }
        }
        return var5;
    }
    
    public Entity getEntityByID(final int p_73045_1_) {
        return (Entity)this.entitiesById.lookup(p_73045_1_);
    }
    
    public List getLoadedEntityList() {
        return this.loadedEntityList;
    }
    
    public void func_175646_b(final BlockPos p_175646_1_, final TileEntity p_175646_2_) {
        if (this.isBlockLoaded(p_175646_1_)) {
            this.getChunkFromBlockCoords(p_175646_1_).setChunkModified();
        }
    }
    
    public int countEntities(final Class entityType) {
        int var2 = 0;
        for (final Entity var4 : this.loadedEntityList) {
            if ((!(var4 instanceof EntityLiving) || !((EntityLiving)var4).isNoDespawnRequired()) && entityType.isAssignableFrom(var4.getClass())) {
                ++var2;
            }
        }
        return var2;
    }
    
    public void loadEntities(final Collection entityCollection) {
        this.loadedEntityList.addAll(entityCollection);
        for (final Entity var3 : entityCollection) {
            this.onEntityAdded(var3);
        }
    }
    
    public void unloadEntities(final Collection entityCollection) {
        this.unloadedEntityList.addAll(entityCollection);
    }
    
    public boolean canBlockBePlaced(final Block p_175716_1_, final BlockPos p_175716_2_, final boolean p_175716_3_, final EnumFacing p_175716_4_, final Entity p_175716_5_, final ItemStack p_175716_6_) {
        final Block var7 = this.getBlockState(p_175716_2_).getBlock();
        final AxisAlignedBB var8 = p_175716_3_ ? null : p_175716_1_.getCollisionBoundingBox(this, p_175716_2_, p_175716_1_.getDefaultState());
        return (var8 == null || this.checkNoEntityCollision(var8, p_175716_5_)) && ((var7.getMaterial() == Material.circuits && p_175716_1_ == Blocks.anvil) || (var7.getMaterial().isReplaceable() && p_175716_1_.canReplace(this, p_175716_2_, p_175716_4_, p_175716_6_)));
    }
    
    @Override
    public int getStrongPower(final BlockPos pos, final EnumFacing direction) {
        final IBlockState var3 = this.getBlockState(pos);
        return var3.getBlock().isProvidingStrongPower(this, pos, var3, direction);
    }
    
    @Override
    public WorldType getWorldType() {
        return this.worldInfo.getTerrainType();
    }
    
    public int getStrongPower(final BlockPos pos) {
        final byte var2 = 0;
        int var3 = Math.max(var2, this.getStrongPower(pos.offsetDown(), EnumFacing.DOWN));
        if (var3 >= 15) {
            return var3;
        }
        var3 = Math.max(var3, this.getStrongPower(pos.offsetUp(), EnumFacing.UP));
        if (var3 >= 15) {
            return var3;
        }
        var3 = Math.max(var3, this.getStrongPower(pos.offsetNorth(), EnumFacing.NORTH));
        if (var3 >= 15) {
            return var3;
        }
        var3 = Math.max(var3, this.getStrongPower(pos.offsetSouth(), EnumFacing.SOUTH));
        if (var3 >= 15) {
            return var3;
        }
        var3 = Math.max(var3, this.getStrongPower(pos.offsetWest(), EnumFacing.WEST));
        if (var3 >= 15) {
            return var3;
        }
        var3 = Math.max(var3, this.getStrongPower(pos.offsetEast(), EnumFacing.EAST));
        return (var3 >= 15) ? var3 : var3;
    }
    
    public boolean func_175709_b(final BlockPos p_175709_1_, final EnumFacing p_175709_2_) {
        return this.getRedstonePower(p_175709_1_, p_175709_2_) > 0;
    }
    
    public int getRedstonePower(final BlockPos pos, final EnumFacing facing) {
        final IBlockState var3 = this.getBlockState(pos);
        final Block var4 = var3.getBlock();
        return var4.isNormalCube() ? this.getStrongPower(pos) : var4.isProvidingWeakPower(this, pos, var3, facing);
    }
    
    public boolean isBlockPowered(final BlockPos pos) {
        return this.getRedstonePower(pos.offsetDown(), EnumFacing.DOWN) > 0 || this.getRedstonePower(pos.offsetUp(), EnumFacing.UP) > 0 || this.getRedstonePower(pos.offsetNorth(), EnumFacing.NORTH) > 0 || this.getRedstonePower(pos.offsetSouth(), EnumFacing.SOUTH) > 0 || this.getRedstonePower(pos.offsetWest(), EnumFacing.WEST) > 0 || this.getRedstonePower(pos.offsetEast(), EnumFacing.EAST) > 0;
    }
    
    public int func_175687_A(final BlockPos p_175687_1_) {
        int var2 = 0;
        for (final EnumFacing var6 : EnumFacing.values()) {
            final int var7 = this.getRedstonePower(p_175687_1_.offset(var6), var6);
            if (var7 >= 15) {
                return 15;
            }
            if (var7 > var2) {
                var2 = var7;
            }
        }
        return var2;
    }
    
    public EntityPlayer getClosestPlayerToEntity(final Entity entityIn, final double distance) {
        return this.getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance);
    }
    
    public EntityPlayer getClosestPlayer(final double x, final double y, final double z, final double distance) {
        double var9 = -1.0;
        EntityPlayer var10 = null;
        for (int var11 = 0; var11 < this.playerEntities.size(); ++var11) {
            final EntityPlayer var12 = this.playerEntities.get(var11);
            if (IEntitySelector.field_180132_d.apply((Object)var12)) {
                final double var13 = var12.getDistanceSq(x, y, z);
                if ((distance < 0.0 || var13 < distance * distance) && (var9 == -1.0 || var13 < var9)) {
                    var9 = var13;
                    var10 = var12;
                }
            }
        }
        return var10;
    }
    
    public boolean func_175636_b(final double p_175636_1_, final double p_175636_3_, final double p_175636_5_, final double p_175636_7_) {
        for (int var9 = 0; var9 < this.playerEntities.size(); ++var9) {
            final EntityPlayer var10 = this.playerEntities.get(var9);
            if (IEntitySelector.field_180132_d.apply((Object)var10)) {
                final double var11 = var10.getDistanceSq(p_175636_1_, p_175636_3_, p_175636_5_);
                if (p_175636_7_ < 0.0 || var11 < p_175636_7_ * p_175636_7_) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public EntityPlayer getPlayerEntityByName(final String name) {
        for (int var2 = 0; var2 < this.playerEntities.size(); ++var2) {
            final EntityPlayer var3 = this.playerEntities.get(var2);
            if (name.equals(var3.getName())) {
                return var3;
            }
        }
        return null;
    }
    
    public EntityPlayer getPlayerEntityByUUID(final UUID uuid) {
        for (int var2 = 0; var2 < this.playerEntities.size(); ++var2) {
            final EntityPlayer var3 = this.playerEntities.get(var2);
            if (uuid.equals(var3.getUniqueID())) {
                return var3;
            }
        }
        return null;
    }
    
    public void sendQuittingDisconnectingPacket() {
    }
    
    public void checkSessionLock() throws MinecraftException {
        this.saveHandler.checkSessionLock();
    }
    
    public void func_82738_a(final long p_82738_1_) {
        this.worldInfo.incrementTotalWorldTime(p_82738_1_);
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
        BlockPos var1 = new BlockPos(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
        if (!this.getWorldBorder().contains(var1)) {
            var1 = this.getHorizon(new BlockPos(this.getWorldBorder().getCenterX(), 0.0, this.getWorldBorder().getCenterZ()));
        }
        return var1;
    }
    
    public void setSpawnLocation(final BlockPos p_175652_1_) {
        this.worldInfo.setSpawn(p_175652_1_);
    }
    
    public void joinEntityInSurroundings(final Entity entityIn) {
        final int var2 = MathHelper.floor_double(entityIn.posX / 16.0);
        final int var3 = MathHelper.floor_double(entityIn.posZ / 16.0);
        final byte var4 = 2;
        for (int var5 = var2 - var4; var5 <= var2 + var4; ++var5) {
            for (int var6 = var3 - var4; var6 <= var3 + var4; ++var6) {
                this.getChunkFromChunkCoords(var5, var6);
            }
        }
        if (!this.loadedEntityList.contains(entityIn)) {
            this.loadedEntityList.add(entityIn);
        }
    }
    
    public boolean isBlockModifiable(final EntityPlayer p_175660_1_, final BlockPos p_175660_2_) {
        return true;
    }
    
    public void setEntityState(final Entity entityIn, final byte p_72960_2_) {
    }
    
    public IChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }
    
    public void addBlockEvent(final BlockPos pos, final Block blockIn, final int eventID, final int eventParam) {
        blockIn.onBlockEventReceived(this, pos, this.getBlockState(pos), eventID, eventParam);
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
    
    public float getWeightedThunderStrength(final float p_72819_1_) {
        return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * p_72819_1_) * this.getRainStrength(p_72819_1_);
    }
    
    public void setThunderStrength(final float p_147442_1_) {
        this.prevThunderingStrength = p_147442_1_;
        this.thunderingStrength = p_147442_1_;
    }
    
    public float getRainStrength(final float p_72867_1_) {
        return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * p_72867_1_;
    }
    
    public void setRainStrength(final float strength) {
        this.prevRainingStrength = strength;
        this.rainingStrength = strength;
    }
    
    public boolean isThundering() {
        return this.getWeightedThunderStrength(1.0f) > 0.9;
    }
    
    public boolean isRaining() {
        return this.getRainStrength(1.0f) > 0.2;
    }
    
    public boolean func_175727_C(final BlockPos pos) {
        if (!this.isRaining()) {
            return false;
        }
        if (!this.isAgainstSky(pos)) {
            return false;
        }
        if (this.func_175725_q(pos).getY() > pos.getY()) {
            return false;
        }
        final BiomeGenBase var2 = this.getBiomeGenForCoords(pos);
        return !var2.getEnableSnow() && !this.func_175708_f(pos, false) && var2.canSpawnLightningBolt();
    }
    
    public boolean func_180502_D(final BlockPos p_180502_1_) {
        final BiomeGenBase var2 = this.getBiomeGenForCoords(p_180502_1_);
        return var2.isHighHumidity();
    }
    
    public MapStorage func_175693_T() {
        return this.mapStorage;
    }
    
    public void setItemData(final String p_72823_1_, final WorldSavedData p_72823_2_) {
        this.mapStorage.setData(p_72823_1_, p_72823_2_);
    }
    
    public WorldSavedData loadItemData(final Class p_72943_1_, final String p_72943_2_) {
        return this.mapStorage.loadData(p_72943_1_, p_72943_2_);
    }
    
    public int getUniqueDataId(final String p_72841_1_) {
        return this.mapStorage.getUniqueDataId(p_72841_1_);
    }
    
    public void func_175669_a(final int p_175669_1_, final BlockPos p_175669_2_, final int p_175669_3_) {
        for (int var4 = 0; var4 < this.worldAccesses.size(); ++var4) {
            this.worldAccesses.get(var4).func_180440_a(p_175669_1_, p_175669_2_, p_175669_3_);
        }
    }
    
    public void playAuxSFX(final int p_175718_1_, final BlockPos p_175718_2_, final int p_175718_3_) {
        this.playAuxSFXAtEntity(null, p_175718_1_, p_175718_2_, p_175718_3_);
    }
    
    public void playAuxSFXAtEntity(final EntityPlayer p_180498_1_, final int p_180498_2_, final BlockPos p_180498_3_, final int p_180498_4_) {
        try {
            for (int var5 = 0; var5 < this.worldAccesses.size(); ++var5) {
                this.worldAccesses.get(var5).func_180439_a(p_180498_1_, p_180498_2_, p_180498_3_, p_180498_4_);
            }
        }
        catch (Throwable var7) {
            final CrashReport var6 = CrashReport.makeCrashReport(var7, "Playing level event");
            final CrashReportCategory var8 = var6.makeCategory("Level event being played");
            var8.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(p_180498_3_));
            var8.addCrashSection("Event source", p_180498_1_);
            var8.addCrashSection("Event type", p_180498_2_);
            var8.addCrashSection("Event data", p_180498_4_);
            throw new ReportedException(var6);
        }
    }
    
    public int getHeight() {
        return 256;
    }
    
    public int getActualHeight() {
        return this.provider.getHasNoSky() ? 128 : 256;
    }
    
    public Random setRandomSeed(final int p_72843_1_, final int p_72843_2_, final int p_72843_3_) {
        final long var4 = p_72843_1_ * 341873128712L + p_72843_2_ * 132897987541L + this.getWorldInfo().getSeed() + p_72843_3_;
        this.rand.setSeed(var4);
        return this.rand;
    }
    
    public BlockPos func_180499_a(final String p_180499_1_, final BlockPos p_180499_2_) {
        return this.getChunkProvider().func_180513_a(this, p_180499_1_, p_180499_2_);
    }
    
    @Override
    public boolean extendedLevelsInChunkCache() {
        return false;
    }
    
    public double getHorizon() {
        return (this.worldInfo.getTerrainType() == WorldType.FLAT) ? 0.0 : 63.0;
    }
    
    public CrashReportCategory addWorldInfoToCrashReport(final CrashReport report) {
        final CrashReportCategory var2 = report.makeCategoryDepth("Affected level", 1);
        var2.addCrashSection("Level name", (this.worldInfo == null) ? "????" : this.worldInfo.getWorldName());
        var2.addCrashSectionCallable("All players", new Callable() {
            private static final String __OBFID = "CL_00000143";
            
            @Override
            public String call() {
                return World.this.playerEntities.size() + " total; " + World.this.playerEntities.toString();
            }
        });
        var2.addCrashSectionCallable("Chunk stats", new Callable() {
            private static final String __OBFID = "CL_00000144";
            
            @Override
            public String call() {
                return World.this.chunkProvider.makeString();
            }
        });
        try {
            this.worldInfo.addToCrashReport(var2);
        }
        catch (Throwable var3) {
            var2.addCrashSectionThrowable("Level Data Unobtainable", var3);
        }
        return var2;
    }
    
    public void sendBlockBreakProgress(final int breakerId, final BlockPos pos, final int progress) {
        for (int var4 = 0; var4 < this.worldAccesses.size(); ++var4) {
            final IWorldAccess var5 = this.worldAccesses.get(var4);
            var5.sendBlockBreakProgress(breakerId, pos, progress);
        }
    }
    
    public Calendar getCurrentDate() {
        if (this.getTotalWorldTime() % 600L == 0L) {
            this.theCalendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis());
        }
        return this.theCalendar;
    }
    
    public void makeFireworks(final double x, final double y, final double z, final double motionX, final double motionY, final double motionZ, final NBTTagCompound compund) {
    }
    
    public Scoreboard getScoreboard() {
        return this.worldScoreboard;
    }
    
    public void updateComparatorOutputLevel(final BlockPos pos, final Block blockIn) {
        for (final EnumFacing var4 : EnumFacing.Plane.HORIZONTAL) {
            BlockPos var5 = pos.offset(var4);
            if (this.isBlockLoaded(var5)) {
                IBlockState var6 = this.getBlockState(var5);
                if (Blocks.unpowered_comparator.func_149907_e(var6.getBlock())) {
                    var6.getBlock().onNeighborBlockChange(this, var5, var6, blockIn);
                }
                else {
                    if (!var6.getBlock().isNormalCube()) {
                        continue;
                    }
                    var5 = var5.offset(var4);
                    var6 = this.getBlockState(var5);
                    if (!Blocks.unpowered_comparator.func_149907_e(var6.getBlock())) {
                        continue;
                    }
                    var6.getBlock().onNeighborBlockChange(this, var5, var6, blockIn);
                }
            }
        }
    }
    
    public DifficultyInstance getDifficultyForLocation(final BlockPos pos) {
        long var2 = 0L;
        float var3 = 0.0f;
        if (this.isBlockLoaded(pos)) {
            var3 = this.getCurrentMoonPhaseFactor();
            var2 = this.getChunkFromBlockCoords(pos).getInhabitedTime();
        }
        return new DifficultyInstance(this.getDifficulty(), this.getWorldTime(), var2, var3);
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
    
    public int func_175658_ac() {
        return this.lastLightningBolt;
    }
    
    public void setLastLightningBolt(final int lastLightningBoltIn) {
        this.lastLightningBolt = lastLightningBoltIn;
    }
    
    public boolean isFindingSpawnPoint() {
        return this.findingSpawnPoint;
    }
    
    public VillageCollection getVillageCollection() {
        return this.villageCollectionObj;
    }
    
    public WorldBorder getWorldBorder() {
        return this.worldBorder;
    }
    
    public boolean chunkExists(final int x, final int z) {
        final BlockPos var3 = this.getSpawnPoint();
        final int var4 = x * 16 + 8 - var3.getX();
        final int var5 = z * 16 + 8 - var3.getZ();
        final short var6 = 128;
        return var4 >= -var6 && var4 <= var6 && var5 >= -var6 && var5 <= var6;
    }
}
