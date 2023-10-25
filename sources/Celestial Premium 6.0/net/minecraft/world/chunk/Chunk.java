/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.Entity;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.IChunkGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ExtendedBlockStorage NULL_BLOCK_STORAGE = null;
    private final ExtendedBlockStorage[] storageArrays = new ExtendedBlockStorage[16];
    private final byte[] blockBiomeArray = new byte[256];
    private final int[] precipitationHeightMap = new int[256];
    private final boolean[] updateSkylightColumns = new boolean[256];
    private boolean isChunkLoaded;
    private final World worldObj;
    private final int[] heightMap;
    public final int x;
    public final int z;
    private boolean isGapLightingUpdated;
    private final Map<BlockPos, TileEntity> chunkTileEntityMap = Maps.newHashMap();
    private final ClassInheritanceMultiMap<Entity>[] entityLists;
    private boolean isTerrainPopulated;
    private boolean isLightPopulated;
    private boolean chunkTicked;
    private boolean isModified;
    private boolean hasEntities;
    private long lastSaveTime;
    private int heightMapMinimum;
    private long inhabitedTime;
    private int queuedLightChecks = 4096;
    private final ConcurrentLinkedQueue<BlockPos> tileEntityPosQueue = Queues.newConcurrentLinkedQueue();
    public boolean unloaded;

    public Chunk(World worldIn, int x, int z) {
        this.entityLists = new ClassInheritanceMultiMap[16];
        this.worldObj = worldIn;
        this.x = x;
        this.z = z;
        this.heightMap = new int[256];
        for (int i = 0; i < this.entityLists.length; ++i) {
            this.entityLists[i] = new ClassInheritanceMultiMap<Entity>(Entity.class);
        }
        Arrays.fill(this.precipitationHeightMap, -999);
        Arrays.fill(this.blockBiomeArray, (byte)-1);
    }

    public Chunk(World worldIn, ChunkPrimer primer, int x, int z) {
        this(worldIn, x, z);
        int i = 256;
        boolean flag = worldIn.provider.func_191066_m();
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 256; ++l) {
                    IBlockState iblockstate = primer.getBlockState(j, l, k);
                    if (iblockstate.getMaterial() == Material.AIR) continue;
                    int i1 = l >> 4;
                    if (this.storageArrays[i1] == NULL_BLOCK_STORAGE) {
                        this.storageArrays[i1] = new ExtendedBlockStorage(i1 << 4, flag);
                    }
                    this.storageArrays[i1].set(j, l & 0xF, k, iblockstate);
                }
            }
        }
    }

    public boolean isAtLocation(int x, int z) {
        return x == this.x && z == this.z;
    }

    public int getHeight(BlockPos pos) {
        return this.getHeightValue(pos.getX() & 0xF, pos.getZ() & 0xF);
    }

    public int getHeightValue(int x, int z) {
        return this.heightMap[z << 4 | x];
    }

    @Nullable
    private ExtendedBlockStorage getLastExtendedBlockStorage() {
        for (int i = this.storageArrays.length - 1; i >= 0; --i) {
            if (this.storageArrays[i] == NULL_BLOCK_STORAGE) continue;
            return this.storageArrays[i];
        }
        return null;
    }

    public int getTopFilledSegment() {
        ExtendedBlockStorage extendedblockstorage = this.getLastExtendedBlockStorage();
        return extendedblockstorage == null ? 0 : extendedblockstorage.getYLocation();
    }

    public ExtendedBlockStorage[] getBlockStorageArray() {
        return this.storageArrays;
    }

    protected void generateHeightMap() {
        int i = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;
        for (int j = 0; j < 16; ++j) {
            block1: for (int k = 0; k < 16; ++k) {
                this.precipitationHeightMap[j + (k << 4)] = -999;
                for (int l = i + 16; l > 0; --l) {
                    IBlockState iblockstate = this.getBlockState(j, l - 1, k);
                    if (iblockstate.getLightOpacity() == 0) continue;
                    this.heightMap[k << 4 | j] = l;
                    if (l >= this.heightMapMinimum) continue block1;
                    this.heightMapMinimum = l;
                    continue block1;
                }
            }
        }
        this.isModified = true;
    }

    public void generateSkylightMap() {
        int i = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                this.precipitationHeightMap[j + (k << 4)] = -999;
                for (int l = i + 16; l > 0; --l) {
                    if (this.getBlockLightOpacity(j, l - 1, k) == 0) continue;
                    this.heightMap[k << 4 | j] = l;
                    if (l >= this.heightMapMinimum) break;
                    this.heightMapMinimum = l;
                    break;
                }
                if (!this.worldObj.provider.func_191066_m()) continue;
                int k1 = 15;
                int i1 = i + 16 - 1;
                do {
                    ExtendedBlockStorage extendedblockstorage;
                    int j1;
                    if ((j1 = this.getBlockLightOpacity(j, i1, k)) == 0 && k1 != 15) {
                        j1 = 1;
                    }
                    if ((k1 -= j1) <= 0 || (extendedblockstorage = this.storageArrays[i1 >> 4]) == NULL_BLOCK_STORAGE) continue;
                    extendedblockstorage.setExtSkylightValue(j, i1 & 0xF, k, k1);
                    this.worldObj.notifyLightSet(new BlockPos((this.x << 4) + j, i1, (this.z << 4) + k));
                } while (--i1 > 0 && k1 > 0);
            }
        }
        this.isModified = true;
    }

    private void propagateSkylightOcclusion(int x, int z) {
        this.updateSkylightColumns[x + z * 16] = true;
        this.isGapLightingUpdated = true;
    }

    private void recheckGaps(boolean p_150803_1_) {
        this.worldObj.theProfiler.startSection("recheckGaps");
        if (this.worldObj.isAreaLoaded(new BlockPos(this.x * 16 + 8, 0, this.z * 16 + 8), 16)) {
            for (int i = 0; i < 16; ++i) {
                for (int j = 0; j < 16; ++j) {
                    if (!this.updateSkylightColumns[i + j * 16]) continue;
                    this.updateSkylightColumns[i + j * 16] = false;
                    int k = this.getHeightValue(i, j);
                    int l = this.x * 16 + i;
                    int i1 = this.z * 16 + j;
                    int j1 = Integer.MAX_VALUE;
                    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                        j1 = Math.min(j1, this.worldObj.getChunksLowestHorizon(l + enumfacing.getXOffset(), i1 + enumfacing.getZOffset()));
                    }
                    this.checkSkylightNeighborHeight(l, i1, j1);
                    for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
                        this.checkSkylightNeighborHeight(l + enumfacing1.getXOffset(), i1 + enumfacing1.getZOffset(), k);
                    }
                    if (!p_150803_1_) continue;
                    this.worldObj.theProfiler.endSection();
                    return;
                }
            }
            this.isGapLightingUpdated = false;
        }
        this.worldObj.theProfiler.endSection();
    }

    private void checkSkylightNeighborHeight(int x, int z, int maxValue) {
        int i = this.worldObj.getHeight(new BlockPos(x, 0, z)).getY();
        if (i > maxValue) {
            this.updateSkylightNeighborHeight(x, z, maxValue, i + 1);
        } else if (i < maxValue) {
            this.updateSkylightNeighborHeight(x, z, i, maxValue + 1);
        }
    }

    private void updateSkylightNeighborHeight(int x, int z, int startY, int endY) {
        if (endY > startY && this.worldObj.isAreaLoaded(new BlockPos(x, 0, z), 16)) {
            for (int i = startY; i < endY; ++i) {
                this.worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x, i, z));
            }
            this.isModified = true;
        }
    }

    private void relightBlock(int x, int y, int z) {
        int i;
        int j = i = this.heightMap[z << 4 | x] & 0xFF;
        if (y > i) {
            j = y;
        }
        while (j > 0 && this.getBlockLightOpacity(x, j - 1, z) == 0) {
            --j;
        }
        if (j != i) {
            this.worldObj.markBlocksDirtyVertical(x + this.x * 16, z + this.z * 16, j, i);
            this.heightMap[z << 4 | x] = j;
            int k = this.x * 16 + x;
            int l = this.z * 16 + z;
            if (this.worldObj.provider.func_191066_m()) {
                if (j < i) {
                    for (int j1 = j; j1 < i; ++j1) {
                        ExtendedBlockStorage extendedblockstorage2 = this.storageArrays[j1 >> 4];
                        if (extendedblockstorage2 == NULL_BLOCK_STORAGE) continue;
                        extendedblockstorage2.setExtSkylightValue(x, j1 & 0xF, z, 15);
                        this.worldObj.notifyLightSet(new BlockPos((this.x << 4) + x, j1, (this.z << 4) + z));
                    }
                } else {
                    for (int i1 = i; i1 < j; ++i1) {
                        ExtendedBlockStorage extendedblockstorage = this.storageArrays[i1 >> 4];
                        if (extendedblockstorage == NULL_BLOCK_STORAGE) continue;
                        extendedblockstorage.setExtSkylightValue(x, i1 & 0xF, z, 0);
                        this.worldObj.notifyLightSet(new BlockPos((this.x << 4) + x, i1, (this.z << 4) + z));
                    }
                }
                int k1 = 15;
                while (j > 0 && k1 > 0) {
                    ExtendedBlockStorage extendedblockstorage1;
                    int i2;
                    if ((i2 = this.getBlockLightOpacity(x, --j, z)) == 0) {
                        i2 = 1;
                    }
                    if ((k1 -= i2) < 0) {
                        k1 = 0;
                    }
                    if ((extendedblockstorage1 = this.storageArrays[j >> 4]) == NULL_BLOCK_STORAGE) continue;
                    extendedblockstorage1.setExtSkylightValue(x, j & 0xF, z, k1);
                }
            }
            int l1 = this.heightMap[z << 4 | x];
            int j2 = i;
            int k2 = l1;
            if (l1 < i) {
                j2 = l1;
                k2 = i;
            }
            if (l1 < this.heightMapMinimum) {
                this.heightMapMinimum = l1;
            }
            if (this.worldObj.provider.func_191066_m()) {
                for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                    this.updateSkylightNeighborHeight(k + enumfacing.getXOffset(), l + enumfacing.getZOffset(), j2, k2);
                }
                this.updateSkylightNeighborHeight(k, l, j2, k2);
            }
            this.isModified = true;
        }
    }

    public int getBlockLightOpacity(BlockPos pos) {
        return this.getBlockState(pos).getLightOpacity();
    }

    private int getBlockLightOpacity(int x, int y, int z) {
        return this.getBlockState(x, y, z).getLightOpacity();
    }

    public IBlockState getBlockState(BlockPos pos) {
        return this.getBlockState(pos.getX(), pos.getY(), pos.getZ());
    }

    public IBlockState getBlockState(final int x, final int y, final int z) {
        if (this.worldObj.getWorldType() == WorldType.DEBUG_WORLD) {
            IBlockState iblockstate = null;
            if (y == 60) {
                iblockstate = Blocks.BARRIER.getDefaultState();
            }
            if (y == 70) {
                iblockstate = ChunkGeneratorDebug.getBlockStateFor(x, z);
            }
            return iblockstate == null ? Blocks.AIR.getDefaultState() : iblockstate;
        }
        try {
            ExtendedBlockStorage extendedblockstorage;
            if (y >= 0 && y >> 4 < this.storageArrays.length && (extendedblockstorage = this.storageArrays[y >> 4]) != NULL_BLOCK_STORAGE) {
                return extendedblockstorage.get(x & 0xF, y & 0xF, z & 0xF);
            }
            return Blocks.AIR.getDefaultState();
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block state");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being got");
            crashreportcategory.setDetail("Location", new ICrashReportDetail<String>(){

                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(x, y, z);
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    @Nullable
    public IBlockState setBlockState(BlockPos pos, IBlockState state) {
        TileEntity tileentity;
        int k;
        int l;
        int i = pos.getX() & 0xF;
        int j = pos.getY();
        if (j >= this.precipitationHeightMap[l = (k = pos.getZ() & 0xF) << 4 | i] - 1) {
            this.precipitationHeightMap[l] = -999;
        }
        int i1 = this.heightMap[l];
        IBlockState iblockstate = this.getBlockState(pos);
        if (iblockstate == state) {
            return null;
        }
        Block block = state.getBlock();
        Block block1 = iblockstate.getBlock();
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
        boolean flag = false;
        if (extendedblockstorage == NULL_BLOCK_STORAGE) {
            if (block == Blocks.AIR) {
                return null;
            }
            this.storageArrays[j >> 4] = extendedblockstorage = new ExtendedBlockStorage(j >> 4 << 4, this.worldObj.provider.func_191066_m());
            flag = j >= i1;
        }
        extendedblockstorage.set(i, j & 0xF, k, state);
        if (block1 != block) {
            if (!this.worldObj.isRemote) {
                block1.breakBlock(this.worldObj, pos, iblockstate);
            } else if (block1 instanceof ITileEntityProvider) {
                this.worldObj.removeTileEntity(pos);
            }
        }
        if (extendedblockstorage.get(i, j & 0xF, k).getBlock() != block) {
            return null;
        }
        if (flag) {
            this.generateSkylightMap();
        } else {
            int j1 = state.getLightOpacity();
            int k1 = iblockstate.getLightOpacity();
            if (j1 > 0) {
                if (j >= i1) {
                    this.relightBlock(i, j + 1, k);
                }
            } else if (j == i1 - 1) {
                this.relightBlock(i, j, k);
            }
            if (j1 != k1 && (j1 < k1 || this.getLightFor(EnumSkyBlock.SKY, pos) > 0 || this.getLightFor(EnumSkyBlock.BLOCK, pos) > 0)) {
                this.propagateSkylightOcclusion(i, k);
            }
        }
        if (block1 instanceof ITileEntityProvider && (tileentity = this.getTileEntity(pos, EnumCreateEntityType.CHECK)) != null) {
            tileentity.updateContainingBlockInfo();
        }
        if (!this.worldObj.isRemote && block1 != block) {
            block.onBlockAdded(this.worldObj, pos, state);
        }
        if (block instanceof ITileEntityProvider) {
            TileEntity tileentity1 = this.getTileEntity(pos, EnumCreateEntityType.CHECK);
            if (tileentity1 == null) {
                tileentity1 = ((ITileEntityProvider)((Object)block)).createNewTileEntity(this.worldObj, block.getMetaFromState(state));
                this.worldObj.setTileEntity(pos, tileentity1);
            }
            if (tileentity1 != null) {
                tileentity1.updateContainingBlockInfo();
            }
        }
        this.isModified = true;
        return iblockstate;
    }

    public int getLightFor(EnumSkyBlock p_177413_1_, BlockPos pos) {
        int i = pos.getX() & 0xF;
        int j = pos.getY();
        int k = pos.getZ() & 0xF;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
        if (extendedblockstorage == NULL_BLOCK_STORAGE) {
            return this.canSeeSky(pos) ? p_177413_1_.defaultLightValue : 0;
        }
        if (p_177413_1_ == EnumSkyBlock.SKY) {
            return !this.worldObj.provider.func_191066_m() ? 0 : extendedblockstorage.getExtSkylightValue(i, j & 0xF, k);
        }
        return p_177413_1_ == EnumSkyBlock.BLOCK ? extendedblockstorage.getExtBlocklightValue(i, j & 0xF, k) : p_177413_1_.defaultLightValue;
    }

    public void setLightFor(EnumSkyBlock p_177431_1_, BlockPos pos, int value) {
        int i = pos.getX() & 0xF;
        int j = pos.getY();
        int k = pos.getZ() & 0xF;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
        if (extendedblockstorage == NULL_BLOCK_STORAGE) {
            this.storageArrays[j >> 4] = extendedblockstorage = new ExtendedBlockStorage(j >> 4 << 4, this.worldObj.provider.func_191066_m());
            this.generateSkylightMap();
        }
        this.isModified = true;
        if (p_177431_1_ == EnumSkyBlock.SKY) {
            if (this.worldObj.provider.func_191066_m()) {
                extendedblockstorage.setExtSkylightValue(i, j & 0xF, k, value);
            }
        } else if (p_177431_1_ == EnumSkyBlock.BLOCK) {
            extendedblockstorage.setExtBlocklightValue(i, j & 0xF, k, value);
        }
    }

    public int getLightSubtracted(BlockPos pos, int amount) {
        int i = pos.getX() & 0xF;
        int j = pos.getY();
        int k = pos.getZ() & 0xF;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
        if (extendedblockstorage == NULL_BLOCK_STORAGE) {
            return this.worldObj.provider.func_191066_m() && amount < EnumSkyBlock.SKY.defaultLightValue ? EnumSkyBlock.SKY.defaultLightValue - amount : 0;
        }
        int l = !this.worldObj.provider.func_191066_m() ? 0 : extendedblockstorage.getExtSkylightValue(i, j & 0xF, k);
        int i1 = extendedblockstorage.getExtBlocklightValue(i, j & 0xF, k);
        if (i1 > (l -= amount)) {
            l = i1;
        }
        return l;
    }

    public void addEntity(Entity entityIn) {
        int k;
        this.hasEntities = true;
        int i = MathHelper.floor(entityIn.posX / 16.0);
        int j = MathHelper.floor(entityIn.posZ / 16.0);
        if (i != this.x || j != this.z) {
            LOGGER.warn("Wrong location! ({}, {}) should be ({}, {}), {}", i, j, this.x, this.z, entityIn);
            entityIn.setDead();
        }
        if ((k = MathHelper.floor(entityIn.posY / 16.0)) < 0) {
            k = 0;
        }
        if (k >= this.entityLists.length) {
            k = this.entityLists.length - 1;
        }
        entityIn.addedToChunk = true;
        entityIn.chunkCoordX = this.x;
        entityIn.chunkCoordY = k;
        entityIn.chunkCoordZ = this.z;
        this.entityLists[k].add(entityIn);
    }

    public void removeEntity(Entity entityIn) {
        this.removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
    }

    public void removeEntityAtIndex(Entity entityIn, int index) {
        if (index < 0) {
            index = 0;
        }
        if (index >= this.entityLists.length) {
            index = this.entityLists.length - 1;
        }
        this.entityLists[index].remove(entityIn);
    }

    public boolean canSeeSky(BlockPos pos) {
        int k;
        int i = pos.getX() & 0xF;
        int j = pos.getY();
        return j >= this.heightMap[(k = pos.getZ() & 0xF) << 4 | i];
    }

    @Nullable
    private TileEntity createNewTileEntity(BlockPos pos) {
        IBlockState iblockstate = this.getBlockState(pos);
        Block block = iblockstate.getBlock();
        return !block.hasTileEntity() ? null : ((ITileEntityProvider)((Object)block)).createNewTileEntity(this.worldObj, iblockstate.getBlock().getMetaFromState(iblockstate));
    }

    @Nullable
    public TileEntity getTileEntity(BlockPos pos, EnumCreateEntityType p_177424_2_) {
        TileEntity tileentity = this.chunkTileEntityMap.get(pos);
        if (tileentity == null) {
            if (p_177424_2_ == EnumCreateEntityType.IMMEDIATE) {
                tileentity = this.createNewTileEntity(pos);
                this.worldObj.setTileEntity(pos, tileentity);
            } else if (p_177424_2_ == EnumCreateEntityType.QUEUED) {
                this.tileEntityPosQueue.add(pos);
            }
        } else if (tileentity.isInvalid()) {
            this.chunkTileEntityMap.remove(pos);
            return null;
        }
        return tileentity;
    }

    public void addTileEntity(TileEntity tileEntityIn) {
        this.addTileEntity(tileEntityIn.getPos(), tileEntityIn);
        if (this.isChunkLoaded) {
            this.worldObj.addTileEntity(tileEntityIn);
        }
    }

    public void addTileEntity(BlockPos pos, TileEntity tileEntityIn) {
        tileEntityIn.setWorldObj(this.worldObj);
        tileEntityIn.setPos(pos);
        if (this.getBlockState(pos).getBlock() instanceof ITileEntityProvider) {
            if (this.chunkTileEntityMap.containsKey(pos)) {
                this.chunkTileEntityMap.get(pos).invalidate();
            }
            tileEntityIn.validate();
            this.chunkTileEntityMap.put(pos, tileEntityIn);
        }
    }

    public void removeTileEntity(BlockPos pos) {
        TileEntity tileentity;
        if (this.isChunkLoaded && (tileentity = this.chunkTileEntityMap.remove(pos)) != null) {
            tileentity.invalidate();
        }
    }

    public void onChunkLoad() {
        this.isChunkLoaded = true;
        this.worldObj.addTileEntities(this.chunkTileEntityMap.values());
        for (ClassInheritanceMultiMap<Entity> classinheritancemultimap : this.entityLists) {
            this.worldObj.loadEntities(classinheritancemultimap);
        }
    }

    public void onChunkUnload() {
        this.isChunkLoaded = false;
        for (TileEntity tileentity : this.chunkTileEntityMap.values()) {
            this.worldObj.markTileEntityForRemoval(tileentity);
        }
        for (ClassInheritanceMultiMap<Entity> classinheritancemultimap : this.entityLists) {
            this.worldObj.unloadEntities(classinheritancemultimap);
        }
    }

    public void setChunkModified() {
        this.isModified = true;
    }

    public void getEntitiesWithinAABBForEntity(@Nullable Entity entityIn, AxisAlignedBB aabb, List<Entity> listToFill, Predicate<? super Entity> p_177414_4_) {
        int i = MathHelper.floor((aabb.minY - 2.0) / 16.0);
        int j = MathHelper.floor((aabb.maxY + 2.0) / 16.0);
        i = MathHelper.clamp(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp(j, 0, this.entityLists.length - 1);
        for (int k = i; k <= j; ++k) {
            if (this.entityLists[k].isEmpty()) continue;
            for (Entity entity : this.entityLists[k]) {
                Entity[] aentity;
                if (!entity.getEntityBoundingBox().intersectsWith(aabb) || entity == entityIn) continue;
                if (p_177414_4_ == null || p_177414_4_.apply(entity)) {
                    listToFill.add(entity);
                }
                if ((aentity = entity.getParts()) == null) continue;
                for (Entity entity1 : aentity) {
                    if (entity1 == entityIn || !entity1.getEntityBoundingBox().intersectsWith(aabb) || p_177414_4_ != null && !p_177414_4_.apply(entity1)) continue;
                    listToFill.add(entity1);
                }
            }
        }
    }

    public <T extends Entity> void getEntitiesOfTypeWithinAAAB(Class<? extends T> entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate<? super T> filter) {
        int i = MathHelper.floor((aabb.minY - 2.0) / 16.0);
        int j = MathHelper.floor((aabb.maxY + 2.0) / 16.0);
        i = MathHelper.clamp(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp(j, 0, this.entityLists.length - 1);
        for (int k = i; k <= j; ++k) {
            for (Entity t : this.entityLists[k].getByClass(entityClass)) {
                if (!t.getEntityBoundingBox().intersectsWith(aabb) || filter != null && !filter.apply(t)) continue;
                listToFill.add(t);
            }
        }
    }

    public boolean needsSaving(boolean p_76601_1_) {
        if (p_76601_1_ ? this.hasEntities && this.worldObj.getTotalWorldTime() != this.lastSaveTime || this.isModified : this.hasEntities && this.worldObj.getTotalWorldTime() >= this.lastSaveTime + 600L) {
            return true;
        }
        return this.isModified;
    }

    public Random getRandomWithSeed(long seed) {
        return new Random(this.worldObj.getSeed() + (long)(this.x * this.x * 4987142) + (long)(this.x * 5947611) + (long)(this.z * this.z) * 4392871L + (long)(this.z * 389711) ^ seed);
    }

    public boolean isEmpty() {
        return false;
    }

    public void populateChunk(IChunkProvider chunkProvider, IChunkGenerator chunkGenrator) {
        Chunk chunk4;
        Chunk chunk = chunkProvider.getLoadedChunk(this.x, this.z - 1);
        Chunk chunk1 = chunkProvider.getLoadedChunk(this.x + 1, this.z);
        Chunk chunk2 = chunkProvider.getLoadedChunk(this.x, this.z + 1);
        Chunk chunk3 = chunkProvider.getLoadedChunk(this.x - 1, this.z);
        if (chunk1 != null && chunk2 != null && chunkProvider.getLoadedChunk(this.x + 1, this.z + 1) != null) {
            this.populateChunk(chunkGenrator);
        }
        if (chunk3 != null && chunk2 != null && chunkProvider.getLoadedChunk(this.x - 1, this.z + 1) != null) {
            chunk3.populateChunk(chunkGenrator);
        }
        if (chunk != null && chunk1 != null && chunkProvider.getLoadedChunk(this.x + 1, this.z - 1) != null) {
            chunk.populateChunk(chunkGenrator);
        }
        if (chunk != null && chunk3 != null && (chunk4 = chunkProvider.getLoadedChunk(this.x - 1, this.z - 1)) != null) {
            chunk4.populateChunk(chunkGenrator);
        }
    }

    protected void populateChunk(IChunkGenerator generator) {
        if (this.isTerrainPopulated()) {
            if (generator.generateStructures(this, this.x, this.z)) {
                this.setChunkModified();
            }
        } else {
            this.checkLight();
            generator.populate(this.x, this.z);
            this.setChunkModified();
        }
    }

    public BlockPos getPrecipitationHeight(BlockPos pos) {
        int i = pos.getX() & 0xF;
        int j = pos.getZ() & 0xF;
        int k = i | j << 4;
        BlockPos blockpos = new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
        if (blockpos.getY() == -999) {
            int l = this.getTopFilledSegment() + 15;
            blockpos = new BlockPos(pos.getX(), l, pos.getZ());
            int i1 = -1;
            while (blockpos.getY() > 0 && i1 == -1) {
                IBlockState iblockstate = this.getBlockState(blockpos);
                Material material = iblockstate.getMaterial();
                if (!material.blocksMovement() && !material.isLiquid()) {
                    blockpos = blockpos.down();
                    continue;
                }
                i1 = blockpos.getY() + 1;
            }
            this.precipitationHeightMap[k] = i1;
        }
        return new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
    }

    public void onTick(boolean p_150804_1_) {
        if (this.isGapLightingUpdated && this.worldObj.provider.func_191066_m() && !p_150804_1_) {
            this.recheckGaps(this.worldObj.isRemote);
        }
        this.chunkTicked = true;
        if (!this.isLightPopulated && this.isTerrainPopulated) {
            this.checkLight();
        }
        while (!this.tileEntityPosQueue.isEmpty()) {
            BlockPos blockpos = this.tileEntityPosQueue.poll();
            if (this.getTileEntity(blockpos, EnumCreateEntityType.CHECK) != null || !this.getBlockState(blockpos).getBlock().hasTileEntity()) continue;
            TileEntity tileentity = this.createNewTileEntity(blockpos);
            this.worldObj.setTileEntity(blockpos, tileentity);
            this.worldObj.markBlockRangeForRenderUpdate(blockpos, blockpos);
        }
    }

    public boolean isPopulated() {
        return this.chunkTicked && this.isTerrainPopulated && this.isLightPopulated;
    }

    public boolean isChunkTicked() {
        return this.chunkTicked;
    }

    public ChunkPos getPos() {
        return new ChunkPos(this.x, this.z);
    }

    public boolean getAreLevelsEmpty(int startY, int endY) {
        if (startY < 0) {
            startY = 0;
        }
        if (endY >= 256) {
            endY = 255;
        }
        for (int i = startY; i <= endY; i += 16) {
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[i >> 4];
            if (extendedblockstorage == NULL_BLOCK_STORAGE || extendedblockstorage.isEmpty()) continue;
            return false;
        }
        return true;
    }

    public void setStorageArrays(ExtendedBlockStorage[] newStorageArrays) {
        if (this.storageArrays.length != newStorageArrays.length) {
            LOGGER.warn("Could not set level chunk sections, array length is {} instead of {}", newStorageArrays.length, this.storageArrays.length);
        } else {
            System.arraycopy(newStorageArrays, 0, this.storageArrays, 0, this.storageArrays.length);
        }
    }

    public void fillChunk(PacketBuffer buf, int p_186033_2_, boolean p_186033_3_) {
        boolean flag = this.worldObj.provider.func_191066_m();
        for (int i = 0; i < this.storageArrays.length; ++i) {
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[i];
            if ((p_186033_2_ & 1 << i) == 0) {
                if (!p_186033_3_ || extendedblockstorage == NULL_BLOCK_STORAGE) continue;
                this.storageArrays[i] = NULL_BLOCK_STORAGE;
                continue;
            }
            if (extendedblockstorage == NULL_BLOCK_STORAGE) {
                this.storageArrays[i] = extendedblockstorage = new ExtendedBlockStorage(i << 4, flag);
            }
            extendedblockstorage.getData().read(buf);
            buf.readBytes(extendedblockstorage.getBlocklightArray().getData());
            if (!flag) continue;
            buf.readBytes(extendedblockstorage.getSkylightArray().getData());
        }
        if (p_186033_3_) {
            buf.readBytes(this.blockBiomeArray);
        }
        for (int j = 0; j < this.storageArrays.length; ++j) {
            if (this.storageArrays[j] == NULL_BLOCK_STORAGE || (p_186033_2_ & 1 << j) == 0) continue;
            this.storageArrays[j].removeInvalidBlocks();
        }
        this.isLightPopulated = true;
        this.isTerrainPopulated = true;
        this.generateHeightMap();
        for (TileEntity tileentity : this.chunkTileEntityMap.values()) {
            tileentity.updateContainingBlockInfo();
        }
    }

    public Biome getBiome(BlockPos pos, BiomeProvider provider) {
        Biome biome1;
        int i = pos.getX() & 0xF;
        int j = pos.getZ() & 0xF;
        int k = this.blockBiomeArray[j << 4 | i] & 0xFF;
        if (k == 255) {
            Biome biome = provider.getBiome(pos, Biomes.PLAINS);
            k = Biome.getIdForBiome(biome);
            this.blockBiomeArray[j << 4 | i] = (byte)(k & 0xFF);
        }
        return (biome1 = Biome.getBiome(k)) == null ? Biomes.PLAINS : biome1;
    }

    public byte[] getBiomeArray() {
        return this.blockBiomeArray;
    }

    public void setBiomeArray(byte[] biomeArray) {
        if (this.blockBiomeArray.length != biomeArray.length) {
            LOGGER.warn("Could not set level chunk biomes, array length is {} instead of {}", biomeArray.length, this.blockBiomeArray.length);
        } else {
            System.arraycopy(biomeArray, 0, this.blockBiomeArray, 0, this.blockBiomeArray.length);
        }
    }

    public void resetRelightChecks() {
        this.queuedLightChecks = 0;
    }

    public void enqueueRelightChecks() {
        if (this.queuedLightChecks < 4096) {
            BlockPos blockpos = new BlockPos(this.x << 4, 0, this.z << 4);
            for (int i = 0; i < 8; ++i) {
                if (this.queuedLightChecks >= 4096) {
                    return;
                }
                int j = this.queuedLightChecks % 16;
                int k = this.queuedLightChecks / 16 % 16;
                int l = this.queuedLightChecks / 256;
                ++this.queuedLightChecks;
                for (int i1 = 0; i1 < 16; ++i1) {
                    boolean flag;
                    BlockPos blockpos1 = blockpos.add(k, (j << 4) + i1, l);
                    boolean bl = flag = i1 == 0 || i1 == 15 || k == 0 || k == 15 || l == 0 || l == 15;
                    if ((this.storageArrays[j] != NULL_BLOCK_STORAGE || !flag) && (this.storageArrays[j] == NULL_BLOCK_STORAGE || this.storageArrays[j].get(k, i1, l).getMaterial() != Material.AIR)) continue;
                    for (EnumFacing enumfacing : EnumFacing.values()) {
                        BlockPos blockpos2 = blockpos1.offset(enumfacing);
                        if (this.worldObj.getBlockState(blockpos2).getLightValue() <= 0) continue;
                        this.worldObj.checkLight(blockpos2);
                    }
                    this.worldObj.checkLight(blockpos1);
                }
            }
        }
    }

    public void checkLight() {
        this.isTerrainPopulated = true;
        this.isLightPopulated = true;
        BlockPos blockpos = new BlockPos(this.x << 4, 0, this.z << 4);
        if (this.worldObj.provider.func_191066_m()) {
            if (this.worldObj.isAreaLoaded(blockpos.add(-1, 0, -1), blockpos.add(16, this.worldObj.getSeaLevel(), 16))) {
                block0: for (int i = 0; i < 16; ++i) {
                    for (int j = 0; j < 16; ++j) {
                        if (this.checkLight(i, j)) continue;
                        this.isLightPopulated = false;
                        break block0;
                    }
                }
                if (this.isLightPopulated) {
                    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                        int k = enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 16 : 1;
                        this.worldObj.getChunk(blockpos.offset(enumfacing, k)).checkLightSide(enumfacing.getOpposite());
                    }
                    this.setSkylightUpdated();
                }
            } else {
                this.isLightPopulated = false;
            }
        }
    }

    private void setSkylightUpdated() {
        for (int i = 0; i < this.updateSkylightColumns.length; ++i) {
            this.updateSkylightColumns[i] = true;
        }
        this.recheckGaps(false);
    }

    private void checkLightSide(EnumFacing facing) {
        block4: {
            block7: {
                block6: {
                    block5: {
                        if (!this.isTerrainPopulated) break block4;
                        if (facing != EnumFacing.EAST) break block5;
                        for (int i = 0; i < 16; ++i) {
                            this.checkLight(15, i);
                        }
                        break block4;
                    }
                    if (facing != EnumFacing.WEST) break block6;
                    for (int j = 0; j < 16; ++j) {
                        this.checkLight(0, j);
                    }
                    break block4;
                }
                if (facing != EnumFacing.SOUTH) break block7;
                for (int k = 0; k < 16; ++k) {
                    this.checkLight(k, 15);
                }
                break block4;
            }
            if (facing != EnumFacing.NORTH) break block4;
            for (int l = 0; l < 16; ++l) {
                this.checkLight(l, 0);
            }
        }
    }

    private boolean checkLight(int x, int z) {
        int i = this.getTopFilledSegment();
        boolean flag = false;
        boolean flag1 = false;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos((this.x << 4) + x, 0, (this.z << 4) + z);
        for (int j = i + 16 - 1; j > this.worldObj.getSeaLevel() || j > 0 && !flag1; --j) {
            blockpos$mutableblockpos.setPos(blockpos$mutableblockpos.getX(), j, blockpos$mutableblockpos.getZ());
            int k = this.getBlockLightOpacity(blockpos$mutableblockpos);
            if (k == 255 && blockpos$mutableblockpos.getY() < this.worldObj.getSeaLevel()) {
                flag1 = true;
            }
            if (!flag && k > 0) {
                flag = true;
                continue;
            }
            if (!flag || k != 0 || this.worldObj.checkLight(blockpos$mutableblockpos)) continue;
            return false;
        }
        for (int l = blockpos$mutableblockpos.getY(); l > 0; --l) {
            blockpos$mutableblockpos.setPos(blockpos$mutableblockpos.getX(), l, blockpos$mutableblockpos.getZ());
            if (this.getBlockState(blockpos$mutableblockpos).getLightValue() <= 0) continue;
            this.worldObj.checkLight(blockpos$mutableblockpos);
        }
        return true;
    }

    public boolean isLoaded() {
        return this.isChunkLoaded;
    }

    public void setChunkLoaded(boolean loaded) {
        this.isChunkLoaded = loaded;
    }

    public World getWorld() {
        return this.worldObj;
    }

    public int[] getHeightMap() {
        return this.heightMap;
    }

    public void setHeightMap(int[] newHeightMap) {
        if (this.heightMap.length != newHeightMap.length) {
            LOGGER.warn("Could not set level chunk heightmap, array length is {} instead of {}", newHeightMap.length, this.heightMap.length);
        } else {
            System.arraycopy(newHeightMap, 0, this.heightMap, 0, this.heightMap.length);
        }
    }

    public Map<BlockPos, TileEntity> getTileEntityMap() {
        return this.chunkTileEntityMap;
    }

    public ClassInheritanceMultiMap<Entity>[] getEntityLists() {
        return this.entityLists;
    }

    public boolean isTerrainPopulated() {
        return this.isTerrainPopulated;
    }

    public void setTerrainPopulated(boolean terrainPopulated) {
        this.isTerrainPopulated = terrainPopulated;
    }

    public boolean isLightPopulated() {
        return this.isLightPopulated;
    }

    public void setLightPopulated(boolean lightPopulated) {
        this.isLightPopulated = lightPopulated;
    }

    public void setModified(boolean modified) {
        this.isModified = modified;
    }

    public void setHasEntities(boolean hasEntitiesIn) {
        this.hasEntities = hasEntitiesIn;
    }

    public void setLastSaveTime(long saveTime) {
        this.lastSaveTime = saveTime;
    }

    public int getLowestHeight() {
        return this.heightMapMinimum;
    }

    public long getInhabitedTime() {
        return this.inhabitedTime;
    }

    public void setInhabitedTime(long newInhabitedTime) {
        this.inhabitedTime = newInhabitedTime;
    }

    public static enum EnumCreateEntityType {
        IMMEDIATE,
        QUEUED,
        CHECK;

    }
}

