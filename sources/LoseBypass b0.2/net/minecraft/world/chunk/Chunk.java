/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ClassInheritanceMultiMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.NibbleArray;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.world.gen.ChunkProviderDebug;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Chunk {
    private static final Logger logger = LogManager.getLogger();
    private final ExtendedBlockStorage[] storageArrays = new ExtendedBlockStorage[16];
    private final byte[] blockBiomeArray = new byte[256];
    private final int[] precipitationHeightMap = new int[256];
    private final boolean[] updateSkylightColumns = new boolean[256];
    private boolean isChunkLoaded;
    private final World worldObj;
    private final int[] heightMap;
    public final int xPosition;
    public final int zPosition;
    private boolean isGapLightingUpdated;
    private final Map<BlockPos, TileEntity> chunkTileEntityMap = Maps.newHashMap();
    private final ClassInheritanceMultiMap<Entity>[] entityLists;
    private boolean isTerrainPopulated;
    private boolean isLightPopulated;
    private boolean field_150815_m;
    private boolean isModified;
    private boolean hasEntities;
    private long lastSaveTime;
    private int heightMapMinimum;
    private long inhabitedTime;
    private int queuedLightChecks = 4096;
    private ConcurrentLinkedQueue<BlockPos> tileEntityPosQueue = Queues.newConcurrentLinkedQueue();

    public Chunk(World worldIn, int x, int z) {
        this.entityLists = new ClassInheritanceMultiMap[16];
        this.worldObj = worldIn;
        this.xPosition = x;
        this.zPosition = z;
        this.heightMap = new int[256];
        int i = 0;
        while (true) {
            if (i >= this.entityLists.length) {
                Arrays.fill(this.precipitationHeightMap, -999);
                Arrays.fill(this.blockBiomeArray, (byte)-1);
                return;
            }
            this.entityLists[i] = new ClassInheritanceMultiMap<Entity>(Entity.class);
            ++i;
        }
    }

    public Chunk(World worldIn, ChunkPrimer primer, int x, int z) {
        this(worldIn, x, z);
        int i = 256;
        boolean flag = !worldIn.provider.getHasNoSky();
        int j = 0;
        while (j < 16) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < i; ++l) {
                    int i1 = j * i * 16 | k * i | l;
                    IBlockState iblockstate = primer.getBlockState(i1);
                    if (iblockstate.getBlock().getMaterial() == Material.air) continue;
                    int j1 = l >> 4;
                    if (this.storageArrays[j1] == null) {
                        this.storageArrays[j1] = new ExtendedBlockStorage(j1 << 4, flag);
                    }
                    this.storageArrays[j1].set(j, l & 0xF, k, iblockstate);
                }
            }
            ++j;
        }
    }

    public boolean isAtLocation(int x, int z) {
        if (x != this.xPosition) return false;
        if (z != this.zPosition) return false;
        return true;
    }

    public int getHeight(BlockPos pos) {
        return this.getHeightValue(pos.getX() & 0xF, pos.getZ() & 0xF);
    }

    public int getHeightValue(int x, int z) {
        return this.heightMap[z << 4 | x];
    }

    public int getTopFilledSegment() {
        int i = this.storageArrays.length - 1;
        while (i >= 0) {
            if (this.storageArrays[i] != null) {
                return this.storageArrays[i].getYLocation();
            }
            --i;
        }
        return 0;
    }

    public ExtendedBlockStorage[] getBlockStorageArray() {
        return this.storageArrays;
    }

    protected void generateHeightMap() {
        int i = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;
        int j = 0;
        block0: while (true) {
            if (j >= 16) {
                this.isModified = true;
                return;
            }
            int k = 0;
            while (true) {
                if (k < 16) {
                    this.precipitationHeightMap[j + (k << 4)] = -999;
                } else {
                    ++j;
                    continue block0;
                }
                for (int l = i + 16; l > 0; --l) {
                    Block block = this.getBlock0(j, l - 1, k);
                    if (block.getLightOpacity() == 0) continue;
                    this.heightMap[k << 4 | j] = l;
                    if (l >= this.heightMapMinimum) break;
                    this.heightMapMinimum = l;
                    break;
                }
                ++k;
            }
            break;
        }
    }

    public void generateSkylightMap() {
        int i = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;
        int j = 0;
        block0: while (true) {
            if (j >= 16) {
                this.isModified = true;
                return;
            }
            int k = 0;
            while (true) {
                if (k < 16) {
                    this.precipitationHeightMap[j + (k << 4)] = -999;
                } else {
                    ++j;
                    continue block0;
                }
                for (int l = i + 16; l > 0; --l) {
                    if (this.getBlockLightOpacity(j, l - 1, k) == 0) continue;
                    this.heightMap[k << 4 | j] = l;
                    if (l >= this.heightMapMinimum) break;
                    this.heightMapMinimum = l;
                    break;
                }
                if (!this.worldObj.provider.getHasNoSky()) {
                    int k1 = 15;
                    int i1 = i + 16 - 1;
                    do {
                        ExtendedBlockStorage extendedblockstorage;
                        int j1;
                        if ((j1 = this.getBlockLightOpacity(j, i1, k)) == 0 && k1 != 15) {
                            j1 = 1;
                        }
                        if ((k1 -= j1) <= 0 || (extendedblockstorage = this.storageArrays[i1 >> 4]) == null) continue;
                        extendedblockstorage.setExtSkylightValue(j, i1 & 0xF, k, k1);
                        this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + j, i1, (this.zPosition << 4) + k));
                    } while (--i1 > 0 && k1 > 0);
                }
                ++k;
            }
            break;
        }
    }

    private void propagateSkylightOcclusion(int x, int z) {
        this.updateSkylightColumns[x + z * 16] = true;
        this.isGapLightingUpdated = true;
    }

    private void recheckGaps(boolean p_150803_1_) {
        this.worldObj.theProfiler.startSection("recheckGaps");
        if (this.worldObj.isAreaLoaded(new BlockPos(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8), 16)) {
            int i = 0;
            while (true) {
                if (i < 16) {
                } else {
                    this.isGapLightingUpdated = false;
                    break;
                }
                for (int j = 0; j < 16; ++j) {
                    if (!this.updateSkylightColumns[i + j * 16]) continue;
                    this.updateSkylightColumns[i + j * 16] = false;
                    int k = this.getHeightValue(i, j);
                    int l = this.xPosition * 16 + i;
                    int i1 = this.zPosition * 16 + j;
                    int j1 = Integer.MAX_VALUE;
                    for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                        j1 = Math.min(j1, this.worldObj.getChunksLowestHorizon(l + enumfacing.getFrontOffsetX(), i1 + enumfacing.getFrontOffsetZ()));
                    }
                    this.checkSkylightNeighborHeight(l, i1, j1);
                    for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL) {
                        this.checkSkylightNeighborHeight(l + enumfacing1.getFrontOffsetX(), i1 + enumfacing1.getFrontOffsetZ(), k);
                    }
                    if (!p_150803_1_) continue;
                    this.worldObj.theProfiler.endSection();
                    return;
                }
                ++i;
            }
        }
        this.worldObj.theProfiler.endSection();
    }

    private void checkSkylightNeighborHeight(int x, int z, int maxValue) {
        int i = this.worldObj.getHeight(new BlockPos(x, 0, z)).getY();
        if (i > maxValue) {
            this.updateSkylightNeighborHeight(x, z, maxValue, i + 1);
            return;
        }
        if (i >= maxValue) return;
        this.updateSkylightNeighborHeight(x, z, i, maxValue + 1);
    }

    private void updateSkylightNeighborHeight(int x, int z, int startY, int endY) {
        if (endY <= startY) return;
        if (!this.worldObj.isAreaLoaded(new BlockPos(x, 0, z), 16)) return;
        int i = startY;
        while (true) {
            if (i >= endY) {
                this.isModified = true;
                return;
            }
            this.worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x, i, z));
            ++i;
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
        if (j == i) return;
        this.worldObj.markBlocksDirtyVertical(x + this.xPosition * 16, z + this.zPosition * 16, j, i);
        this.heightMap[z << 4 | x] = j;
        int k = this.xPosition * 16 + x;
        int l = this.zPosition * 16 + z;
        if (!this.worldObj.provider.getHasNoSky()) {
            if (j < i) {
                for (int j1 = j; j1 < i; ++j1) {
                    ExtendedBlockStorage extendedblockstorage2 = this.storageArrays[j1 >> 4];
                    if (extendedblockstorage2 == null) continue;
                    extendedblockstorage2.setExtSkylightValue(x, j1 & 0xF, z, 15);
                    this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, j1, (this.zPosition << 4) + z));
                }
            } else {
                for (int i1 = i; i1 < j; ++i1) {
                    ExtendedBlockStorage extendedblockstorage = this.storageArrays[i1 >> 4];
                    if (extendedblockstorage == null) continue;
                    extendedblockstorage.setExtSkylightValue(x, i1 & 0xF, z, 0);
                    this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, i1, (this.zPosition << 4) + z));
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
                if ((extendedblockstorage1 = this.storageArrays[j >> 4]) == null) continue;
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
        if (!this.worldObj.provider.getHasNoSky()) {
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                this.updateSkylightNeighborHeight(k + enumfacing.getFrontOffsetX(), l + enumfacing.getFrontOffsetZ(), j2, k2);
            }
            this.updateSkylightNeighborHeight(k, l, j2, k2);
        }
        this.isModified = true;
    }

    public int getBlockLightOpacity(BlockPos pos) {
        return this.getBlock(pos).getLightOpacity();
    }

    private int getBlockLightOpacity(int x, int y, int z) {
        return this.getBlock0(x, y, z).getLightOpacity();
    }

    private Block getBlock0(int x, int y, int z) {
        Block block = Blocks.air;
        if (y < 0) return block;
        if (y >> 4 >= this.storageArrays.length) return block;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[y >> 4];
        if (extendedblockstorage == null) return block;
        try {
            return extendedblockstorage.getBlockByExtId(x, y & 0xF, z);
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block");
            throw new ReportedException(crashreport);
        }
    }

    public Block getBlock(final int x, final int y, final int z) {
        try {
            return this.getBlock0(x & 0xF, y, z & 0xF);
        }
        catch (ReportedException reportedexception) {
            CrashReportCategory crashreportcategory = reportedexception.getCrashReport().makeCategory("Block being got");
            crashreportcategory.addCrashSectionCallable("Location", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(new BlockPos(Chunk.this.xPosition * 16 + x, y, Chunk.this.zPosition * 16 + z));
                }
            });
            throw reportedexception;
        }
    }

    public Block getBlock(final BlockPos pos) {
        try {
            return this.getBlock0(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
        }
        catch (ReportedException reportedexception) {
            CrashReportCategory crashreportcategory = reportedexception.getCrashReport().makeCategory("Block being got");
            crashreportcategory.addCrashSectionCallable("Location", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(pos);
                }
            });
            throw reportedexception;
        }
    }

    public IBlockState getBlockState(final BlockPos pos) {
        if (this.worldObj.getWorldType() == WorldType.DEBUG_WORLD) {
            IBlockState iBlockState;
            IBlockState iblockstate = null;
            if (pos.getY() == 60) {
                iblockstate = Blocks.barrier.getDefaultState();
            }
            if (pos.getY() == 70) {
                iblockstate = ChunkProviderDebug.func_177461_b(pos.getX(), pos.getZ());
            }
            if (iblockstate == null) {
                iBlockState = Blocks.air.getDefaultState();
                return iBlockState;
            }
            iBlockState = iblockstate;
            return iBlockState;
        }
        try {
            if (pos.getY() < 0) return Blocks.air.getDefaultState();
            if (pos.getY() >> 4 >= this.storageArrays.length) return Blocks.air.getDefaultState();
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[pos.getY() >> 4];
            if (extendedblockstorage == null) return Blocks.air.getDefaultState();
            int j = pos.getX() & 0xF;
            int k = pos.getY() & 0xF;
            int i = pos.getZ() & 0xF;
            return extendedblockstorage.get(j, k, i);
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block state");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being got");
            crashreportcategory.addCrashSectionCallable("Location", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(pos);
                }
            });
            throw new ReportedException(crashreport);
        }
    }

    private int getBlockMetadata(int x, int y, int z) {
        if (y >> 4 >= this.storageArrays.length) {
            return 0;
        }
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[y >> 4];
        if (extendedblockstorage == null) return 0;
        int n = extendedblockstorage.getExtBlockMetadata(x, y & 0xF, z);
        return n;
    }

    public int getBlockMetadata(BlockPos pos) {
        return this.getBlockMetadata(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
    }

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
        if (extendedblockstorage == null) {
            if (block == Blocks.air) {
                return null;
            }
            ExtendedBlockStorage extendedBlockStorage = new ExtendedBlockStorage(j >> 4 << 4, !this.worldObj.provider.getHasNoSky());
            this.storageArrays[j >> 4] = extendedBlockStorage;
            extendedblockstorage = extendedBlockStorage;
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
        if (extendedblockstorage.getBlockByExtId(i, j & 0xF, k) != block) {
            return null;
        }
        if (flag) {
            this.generateSkylightMap();
        } else {
            int j1 = block.getLightOpacity();
            int k1 = block1.getLightOpacity();
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
        int n;
        int i = pos.getX() & 0xF;
        int j = pos.getY();
        int k = pos.getZ() & 0xF;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
        if (extendedblockstorage == null) {
            if (!this.canSeeSky(pos)) return 0;
            n = p_177413_1_.defaultLightValue;
            return n;
        }
        if (p_177413_1_ == EnumSkyBlock.SKY) {
            if (this.worldObj.provider.getHasNoSky()) {
                return 0;
            }
            n = extendedblockstorage.getExtSkylightValue(i, j & 0xF, k);
            return n;
        }
        if (p_177413_1_ == EnumSkyBlock.BLOCK) {
            n = extendedblockstorage.getExtBlocklightValue(i, j & 0xF, k);
            return n;
        }
        n = p_177413_1_.defaultLightValue;
        return n;
    }

    public void setLightFor(EnumSkyBlock p_177431_1_, BlockPos pos, int value) {
        int i = pos.getX() & 0xF;
        int j = pos.getY();
        int k = pos.getZ() & 0xF;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
        if (extendedblockstorage == null) {
            ExtendedBlockStorage extendedBlockStorage = new ExtendedBlockStorage(j >> 4 << 4, !this.worldObj.provider.getHasNoSky());
            this.storageArrays[j >> 4] = extendedBlockStorage;
            extendedblockstorage = extendedBlockStorage;
            this.generateSkylightMap();
        }
        this.isModified = true;
        if (p_177431_1_ == EnumSkyBlock.SKY) {
            if (this.worldObj.provider.getHasNoSky()) return;
            extendedblockstorage.setExtSkylightValue(i, j & 0xF, k, value);
            return;
        }
        if (p_177431_1_ != EnumSkyBlock.BLOCK) return;
        extendedblockstorage.setExtBlocklightValue(i, j & 0xF, k, value);
    }

    public int getLightSubtracted(BlockPos pos, int amount) {
        int i = pos.getX() & 0xF;
        int j = pos.getY();
        int k = pos.getZ() & 0xF;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j >> 4];
        if (extendedblockstorage == null) {
            if (this.worldObj.provider.getHasNoSky()) return 0;
            if (amount >= EnumSkyBlock.SKY.defaultLightValue) return 0;
            int n = EnumSkyBlock.SKY.defaultLightValue - amount;
            return n;
        }
        int l = this.worldObj.provider.getHasNoSky() ? 0 : extendedblockstorage.getExtSkylightValue(i, j & 0xF, k);
        int i1 = extendedblockstorage.getExtBlocklightValue(i, j & 0xF, k);
        if (i1 <= (l -= amount)) return l;
        return i1;
    }

    public void addEntity(Entity entityIn) {
        int k;
        this.hasEntities = true;
        int i = MathHelper.floor_double(entityIn.posX / 16.0);
        int j = MathHelper.floor_double(entityIn.posZ / 16.0);
        if (i != this.xPosition || j != this.zPosition) {
            logger.warn("Wrong location! (" + i + ", " + j + ") should be (" + this.xPosition + ", " + this.zPosition + "), " + entityIn, new Object[]{entityIn});
            entityIn.setDead();
        }
        if ((k = MathHelper.floor_double(entityIn.posY / 16.0)) < 0) {
            k = 0;
        }
        if (k >= this.entityLists.length) {
            k = this.entityLists.length - 1;
        }
        entityIn.addedToChunk = true;
        entityIn.chunkCoordX = this.xPosition;
        entityIn.chunkCoordY = k;
        entityIn.chunkCoordZ = this.zPosition;
        this.entityLists[k].add(entityIn);
    }

    public void removeEntity(Entity entityIn) {
        this.removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
    }

    public void removeEntityAtIndex(Entity entityIn, int p_76608_2_) {
        if (p_76608_2_ < 0) {
            p_76608_2_ = 0;
        }
        if (p_76608_2_ >= this.entityLists.length) {
            p_76608_2_ = this.entityLists.length - 1;
        }
        this.entityLists[p_76608_2_].remove(entityIn);
    }

    public boolean canSeeSky(BlockPos pos) {
        int k;
        int i = pos.getX() & 0xF;
        int j = pos.getY();
        if (j < this.heightMap[(k = pos.getZ() & 0xF) << 4 | i]) return false;
        return true;
    }

    private TileEntity createNewTileEntity(BlockPos pos) {
        Block block = this.getBlock(pos);
        if (!block.hasTileEntity()) {
            return null;
        }
        TileEntity tileEntity = ((ITileEntityProvider)((Object)block)).createNewTileEntity(this.worldObj, this.getBlockMetadata(pos));
        return tileEntity;
    }

    public TileEntity getTileEntity(BlockPos pos, EnumCreateEntityType p_177424_2_) {
        TileEntity tileentity = this.chunkTileEntityMap.get(pos);
        if (tileentity != null) {
            if (!tileentity.isInvalid()) return tileentity;
            this.chunkTileEntityMap.remove(pos);
            return null;
        }
        if (p_177424_2_ == EnumCreateEntityType.IMMEDIATE) {
            tileentity = this.createNewTileEntity(pos);
            this.worldObj.setTileEntity(pos, tileentity);
            return tileentity;
        }
        if (p_177424_2_ != EnumCreateEntityType.QUEUED) return tileentity;
        this.tileEntityPosQueue.add(pos);
        return tileentity;
    }

    public void addTileEntity(TileEntity tileEntityIn) {
        this.addTileEntity(tileEntityIn.getPos(), tileEntityIn);
        if (!this.isChunkLoaded) return;
        this.worldObj.addTileEntity(tileEntityIn);
    }

    public void addTileEntity(BlockPos pos, TileEntity tileEntityIn) {
        tileEntityIn.setWorldObj(this.worldObj);
        tileEntityIn.setPos(pos);
        if (!(this.getBlock(pos) instanceof ITileEntityProvider)) return;
        if (this.chunkTileEntityMap.containsKey(pos)) {
            this.chunkTileEntityMap.get(pos).invalidate();
        }
        tileEntityIn.validate();
        this.chunkTileEntityMap.put(pos, tileEntityIn);
    }

    public void removeTileEntity(BlockPos pos) {
        if (!this.isChunkLoaded) return;
        TileEntity tileentity = this.chunkTileEntityMap.remove(pos);
        if (tileentity == null) return;
        tileentity.invalidate();
    }

    public void onChunkLoad() {
        this.isChunkLoaded = true;
        this.worldObj.addTileEntities(this.chunkTileEntityMap.values());
        int i = 0;
        while (i < this.entityLists.length) {
            for (Entity entity : this.entityLists[i]) {
                entity.onChunkLoad();
            }
            this.worldObj.loadEntities(this.entityLists[i]);
            ++i;
        }
    }

    public void onChunkUnload() {
        this.isChunkLoaded = false;
        for (TileEntity tileentity : this.chunkTileEntityMap.values()) {
            this.worldObj.markTileEntityForRemoval(tileentity);
        }
        int i = 0;
        while (i < this.entityLists.length) {
            this.worldObj.unloadEntities(this.entityLists[i]);
            ++i;
        }
    }

    public void setChunkModified() {
        this.isModified = true;
    }

    public void getEntitiesWithinAABBForEntity(Entity entityIn, AxisAlignedBB aabb, List<Entity> listToFill, Predicate<? super Entity> p_177414_4_) {
        int i = MathHelper.floor_double((aabb.minY - 2.0) / 16.0);
        int j = MathHelper.floor_double((aabb.maxY + 2.0) / 16.0);
        i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);
        int k = i;
        while (k <= j) {
            if (!this.entityLists[k].isEmpty()) {
                for (Entity entity : this.entityLists[k]) {
                    Entity[] aentity;
                    if (!entity.getEntityBoundingBox().intersectsWith(aabb) || entity == entityIn) continue;
                    if (p_177414_4_ == null || p_177414_4_.apply(entity)) {
                        listToFill.add(entity);
                    }
                    if ((aentity = entity.getParts()) == null) continue;
                    for (int l = 0; l < aentity.length; ++l) {
                        entity = aentity[l];
                        if (entity == entityIn || !entity.getEntityBoundingBox().intersectsWith(aabb) || p_177414_4_ != null && !p_177414_4_.apply(entity)) continue;
                        listToFill.add(entity);
                    }
                }
            }
            ++k;
        }
    }

    public <T extends Entity> void getEntitiesOfTypeWithinAAAB(Class<? extends T> entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate<? super T> p_177430_4_) {
        int i = MathHelper.floor_double((aabb.minY - 2.0) / 16.0);
        int j = MathHelper.floor_double((aabb.maxY + 2.0) / 16.0);
        i = MathHelper.clamp_int(i, 0, this.entityLists.length - 1);
        j = MathHelper.clamp_int(j, 0, this.entityLists.length - 1);
        int k = i;
        while (k <= j) {
            for (Entity t : this.entityLists[k].getByClass(entityClass)) {
                if (!t.getEntityBoundingBox().intersectsWith(aabb) || p_177430_4_ != null && !p_177430_4_.apply(t)) continue;
                listToFill.add(t);
            }
            ++k;
        }
    }

    public boolean needsSaving(boolean p_76601_1_) {
        if (!p_76601_1_) {
            if (!this.hasEntities) return this.isModified;
            if (this.worldObj.getTotalWorldTime() < this.lastSaveTime + 600L) return this.isModified;
            return true;
        }
        if (this.hasEntities) {
            if (this.worldObj.getTotalWorldTime() != this.lastSaveTime) return true;
        }
        if (!this.isModified) return this.isModified;
        return true;
    }

    public Random getRandomWithSeed(long seed) {
        return new Random(this.worldObj.getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ seed);
    }

    public boolean isEmpty() {
        return false;
    }

    public void populateChunk(IChunkProvider p_76624_1_, IChunkProvider p_76624_2_, int p_76624_3_, int p_76624_4_) {
        boolean flag = p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ - 1);
        boolean flag1 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_);
        boolean flag2 = p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ + 1);
        boolean flag3 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_);
        boolean flag4 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ - 1);
        boolean flag5 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ + 1);
        boolean flag6 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ + 1);
        boolean flag7 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ - 1);
        if (flag1 && flag2 && flag5) {
            if (!this.isTerrainPopulated) {
                p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_);
            } else {
                p_76624_1_.func_177460_a(p_76624_2_, this, p_76624_3_, p_76624_4_);
            }
        }
        if (flag3 && flag2 && flag6) {
            Chunk chunk = p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_);
            if (!chunk.isTerrainPopulated) {
                p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_);
            } else {
                p_76624_1_.func_177460_a(p_76624_2_, chunk, p_76624_3_ - 1, p_76624_4_);
            }
        }
        if (flag && flag1 && flag7) {
            Chunk chunk1 = p_76624_1_.provideChunk(p_76624_3_, p_76624_4_ - 1);
            if (!chunk1.isTerrainPopulated) {
                p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_ - 1);
            } else {
                p_76624_1_.func_177460_a(p_76624_2_, chunk1, p_76624_3_, p_76624_4_ - 1);
            }
        }
        if (!flag4) return;
        if (!flag) return;
        if (!flag3) return;
        Chunk chunk2 = p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_ - 1);
        if (!chunk2.isTerrainPopulated) {
            p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_ - 1);
            return;
        }
        p_76624_1_.func_177460_a(p_76624_2_, chunk2, p_76624_3_ - 1, p_76624_4_ - 1);
    }

    public BlockPos getPrecipitationHeight(BlockPos pos) {
        int i = pos.getX() & 0xF;
        int j = pos.getZ() & 0xF;
        int k = i | j << 4;
        BlockPos blockpos = new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
        if (blockpos.getY() != -999) return new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
        int l = this.getTopFilledSegment() + 15;
        blockpos = new BlockPos(pos.getX(), l, pos.getZ());
        int i1 = -1;
        while (blockpos.getY() > 0 && i1 == -1) {
            Block block = this.getBlock(blockpos);
            Material material = block.getMaterial();
            if (!material.blocksMovement() && !material.isLiquid()) {
                blockpos = blockpos.down();
                continue;
            }
            i1 = blockpos.getY() + 1;
        }
        this.precipitationHeightMap[k] = i1;
        return new BlockPos(pos.getX(), this.precipitationHeightMap[k], pos.getZ());
    }

    public void func_150804_b(boolean p_150804_1_) {
        if (this.isGapLightingUpdated && !this.worldObj.provider.getHasNoSky() && !p_150804_1_) {
            this.recheckGaps(this.worldObj.isRemote);
        }
        this.field_150815_m = true;
        if (!this.isLightPopulated && this.isTerrainPopulated) {
            this.func_150809_p();
        }
        while (!this.tileEntityPosQueue.isEmpty()) {
            BlockPos blockpos = this.tileEntityPosQueue.poll();
            if (this.getTileEntity(blockpos, EnumCreateEntityType.CHECK) != null || !this.getBlock(blockpos).hasTileEntity()) continue;
            TileEntity tileentity = this.createNewTileEntity(blockpos);
            this.worldObj.setTileEntity(blockpos, tileentity);
            this.worldObj.markBlockRangeForRenderUpdate(blockpos, blockpos);
        }
    }

    public boolean isPopulated() {
        if (!this.field_150815_m) return false;
        if (!this.isTerrainPopulated) return false;
        if (!this.isLightPopulated) return false;
        return true;
    }

    public ChunkCoordIntPair getChunkCoordIntPair() {
        return new ChunkCoordIntPair(this.xPosition, this.zPosition);
    }

    public boolean getAreLevelsEmpty(int startY, int endY) {
        if (startY < 0) {
            startY = 0;
        }
        if (endY >= 256) {
            endY = 255;
        }
        int i = startY;
        while (i <= endY) {
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[i >> 4];
            if (extendedblockstorage != null && !extendedblockstorage.isEmpty()) {
                return false;
            }
            i += 16;
        }
        return true;
    }

    public void setStorageArrays(ExtendedBlockStorage[] newStorageArrays) {
        if (this.storageArrays.length != newStorageArrays.length) {
            logger.warn("Could not set level chunk sections, array length is " + newStorageArrays.length + " instead of " + this.storageArrays.length);
            return;
        }
        int i = 0;
        while (i < this.storageArrays.length) {
            this.storageArrays[i] = newStorageArrays[i];
            ++i;
        }
    }

    public void fillChunk(byte[] p_177439_1_, int p_177439_2_, boolean p_177439_3_) {
        int i1;
        int i = 0;
        boolean flag = !this.worldObj.provider.getHasNoSky();
        for (int j = 0; j < this.storageArrays.length; ++j) {
            if ((p_177439_2_ & 1 << j) != 0) {
                if (this.storageArrays[j] == null) {
                    this.storageArrays[j] = new ExtendedBlockStorage(j << 4, flag);
                }
                char[] achar = this.storageArrays[j].getData();
                for (int k = 0; k < achar.length; i += 2, ++k) {
                    achar[k] = (char)((p_177439_1_[i + 1] & 0xFF) << 8 | p_177439_1_[i] & 0xFF);
                }
                continue;
            }
            if (!p_177439_3_ || this.storageArrays[j] == null) continue;
            this.storageArrays[j] = null;
        }
        for (int l = 0; l < this.storageArrays.length; ++l) {
            if ((p_177439_2_ & 1 << l) == 0 || this.storageArrays[l] == null) continue;
            NibbleArray nibblearray = this.storageArrays[l].getBlocklightArray();
            System.arraycopy(p_177439_1_, i, nibblearray.getData(), 0, nibblearray.getData().length);
            i += nibblearray.getData().length;
        }
        if (flag) {
            for (i1 = 0; i1 < this.storageArrays.length; ++i1) {
                if ((p_177439_2_ & 1 << i1) == 0 || this.storageArrays[i1] == null) continue;
                NibbleArray nibblearray1 = this.storageArrays[i1].getSkylightArray();
                System.arraycopy(p_177439_1_, i, nibblearray1.getData(), 0, nibblearray1.getData().length);
                i += nibblearray1.getData().length;
            }
        }
        if (p_177439_3_) {
            System.arraycopy(p_177439_1_, i, this.blockBiomeArray, 0, this.blockBiomeArray.length);
            i1 = i + this.blockBiomeArray.length;
        }
        for (int j1 = 0; j1 < this.storageArrays.length; ++j1) {
            if (this.storageArrays[j1] == null || (p_177439_2_ & 1 << j1) == 0) continue;
            this.storageArrays[j1].removeInvalidBlocks();
        }
        this.isLightPopulated = true;
        this.isTerrainPopulated = true;
        this.generateHeightMap();
        Iterator<TileEntity> iterator = this.chunkTileEntityMap.values().iterator();
        while (iterator.hasNext()) {
            TileEntity tileentity = iterator.next();
            tileentity.updateContainingBlockInfo();
        }
    }

    public BiomeGenBase getBiome(BlockPos pos, WorldChunkManager chunkManager) {
        BiomeGenBase biomeGenBase;
        BiomeGenBase biomegenbase1;
        int i = pos.getX() & 0xF;
        int j = pos.getZ() & 0xF;
        int k = this.blockBiomeArray[j << 4 | i] & 0xFF;
        if (k == 255) {
            BiomeGenBase biomegenbase = chunkManager.getBiomeGenerator(pos, BiomeGenBase.plains);
            k = biomegenbase.biomeID;
            this.blockBiomeArray[j << 4 | i] = (byte)(k & 0xFF);
        }
        if ((biomegenbase1 = BiomeGenBase.getBiome(k)) == null) {
            biomeGenBase = BiomeGenBase.plains;
            return biomeGenBase;
        }
        biomeGenBase = biomegenbase1;
        return biomeGenBase;
    }

    public byte[] getBiomeArray() {
        return this.blockBiomeArray;
    }

    public void setBiomeArray(byte[] biomeArray) {
        if (this.blockBiomeArray.length != biomeArray.length) {
            logger.warn("Could not set level chunk biomes, array length is " + biomeArray.length + " instead of " + this.blockBiomeArray.length);
            return;
        }
        int i = 0;
        while (i < this.blockBiomeArray.length) {
            this.blockBiomeArray[i] = biomeArray[i];
            ++i;
        }
    }

    public void resetRelightChecks() {
        this.queuedLightChecks = 0;
    }

    public void enqueueRelightChecks() {
        BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
        int i = 0;
        while (i < 8) {
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
                if ((this.storageArrays[j] != null || !flag) && (this.storageArrays[j] == null || this.storageArrays[j].getBlockByExtId(k, i1, l).getMaterial() != Material.air)) continue;
                for (EnumFacing enumfacing : EnumFacing.values()) {
                    BlockPos blockpos2 = blockpos1.offset(enumfacing);
                    if (this.worldObj.getBlockState(blockpos2).getBlock().getLightValue() <= 0) continue;
                    this.worldObj.checkLight(blockpos2);
                }
                this.worldObj.checkLight(blockpos1);
            }
            ++i;
        }
    }

    public void func_150809_p() {
        this.isTerrainPopulated = true;
        this.isLightPopulated = true;
        BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
        if (this.worldObj.provider.getHasNoSky()) return;
        if (!this.worldObj.isAreaLoaded(blockpos.add(-1, 0, -1), blockpos.add(16, this.worldObj.func_181545_F(), 16))) {
            this.isLightPopulated = false;
            return;
        }
        block0: for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                if (this.func_150811_f(i, j)) continue;
                this.isLightPopulated = false;
                break block0;
            }
        }
        if (!this.isLightPopulated) return;
        Iterator<EnumFacing> iterator = EnumFacing.Plane.HORIZONTAL.iterator();
        while (true) {
            if (!iterator.hasNext()) {
                this.func_177441_y();
                return;
            }
            EnumFacing enumfacing = iterator.next();
            int k = enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 16 : 1;
            this.worldObj.getChunkFromBlockCoords(blockpos.offset(enumfacing, k)).func_180700_a(enumfacing.getOpposite());
        }
    }

    private void func_177441_y() {
        int i = 0;
        while (true) {
            if (i >= this.updateSkylightColumns.length) {
                this.recheckGaps(false);
                return;
            }
            this.updateSkylightColumns[i] = true;
            ++i;
        }
    }

    private void func_180700_a(EnumFacing p_180700_1_) {
        if (!this.isTerrainPopulated) return;
        if (p_180700_1_ == EnumFacing.EAST) {
            int i = 0;
            while (i < 16) {
                this.func_150811_f(15, i);
                ++i;
            }
            return;
        }
        if (p_180700_1_ == EnumFacing.WEST) {
            int j = 0;
            while (j < 16) {
                this.func_150811_f(0, j);
                ++j;
            }
            return;
        }
        if (p_180700_1_ == EnumFacing.SOUTH) {
            int k = 0;
            while (k < 16) {
                this.func_150811_f(k, 15);
                ++k;
            }
            return;
        }
        if (p_180700_1_ != EnumFacing.NORTH) return;
        int l = 0;
        while (l < 16) {
            this.func_150811_f(l, 0);
            ++l;
        }
    }

    private boolean func_150811_f(int x, int z) {
        int i = this.getTopFilledSegment();
        boolean flag = false;
        boolean flag1 = false;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos((this.xPosition << 4) + x, 0, (this.zPosition << 4) + z);
        for (int j = i + 16 - 1; j > this.worldObj.func_181545_F() || j > 0 && !flag1; --j) {
            blockpos$mutableblockpos.func_181079_c(blockpos$mutableblockpos.getX(), j, blockpos$mutableblockpos.getZ());
            int k = this.getBlockLightOpacity(blockpos$mutableblockpos);
            if (k == 255 && blockpos$mutableblockpos.getY() < this.worldObj.func_181545_F()) {
                flag1 = true;
            }
            if (!flag && k > 0) {
                flag = true;
                continue;
            }
            if (!flag || k != 0 || this.worldObj.checkLight(blockpos$mutableblockpos)) continue;
            return false;
        }
        int l = blockpos$mutableblockpos.getY();
        while (l > 0) {
            blockpos$mutableblockpos.func_181079_c(blockpos$mutableblockpos.getX(), l, blockpos$mutableblockpos.getZ());
            if (this.getBlock(blockpos$mutableblockpos).getLightValue() > 0) {
                this.worldObj.checkLight(blockpos$mutableblockpos);
            }
            --l;
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
            logger.warn("Could not set level chunk heightmap, array length is " + newHeightMap.length + " instead of " + this.heightMap.length);
            return;
        }
        int i = 0;
        while (i < this.heightMap.length) {
            this.heightMap[i] = newHeightMap[i];
            ++i;
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

