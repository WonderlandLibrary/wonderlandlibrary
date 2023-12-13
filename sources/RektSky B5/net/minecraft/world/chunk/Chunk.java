/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.chunk;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import java.util.Arrays;
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

    public Chunk(World worldIn, int x2, int z2) {
        this.entityLists = new ClassInheritanceMultiMap[16];
        this.worldObj = worldIn;
        this.xPosition = x2;
        this.zPosition = z2;
        this.heightMap = new int[256];
        for (int i2 = 0; i2 < this.entityLists.length; ++i2) {
            this.entityLists[i2] = new ClassInheritanceMultiMap<Entity>(Entity.class);
        }
        Arrays.fill(this.precipitationHeightMap, -999);
        Arrays.fill(this.blockBiomeArray, (byte)-1);
    }

    public Chunk(World worldIn, ChunkPrimer primer, int x2, int z2) {
        this(worldIn, x2, z2);
        int i2 = 256;
        boolean flag = !worldIn.provider.getHasNoSky();
        for (int j2 = 0; j2 < 16; ++j2) {
            for (int k2 = 0; k2 < 16; ++k2) {
                for (int l2 = 0; l2 < i2; ++l2) {
                    int i1 = j2 * i2 * 16 | k2 * i2 | l2;
                    IBlockState iblockstate = primer.getBlockState(i1);
                    if (iblockstate.getBlock().getMaterial() == Material.air) continue;
                    int j1 = l2 >> 4;
                    if (this.storageArrays[j1] == null) {
                        this.storageArrays[j1] = new ExtendedBlockStorage(j1 << 4, flag);
                    }
                    this.storageArrays[j1].set(j2, l2 & 0xF, k2, iblockstate);
                }
            }
        }
    }

    public boolean isAtLocation(int x2, int z2) {
        return x2 == this.xPosition && z2 == this.zPosition;
    }

    public int getHeight(BlockPos pos) {
        return this.getHeightValue(pos.getX() & 0xF, pos.getZ() & 0xF);
    }

    public int getHeightValue(int x2, int z2) {
        return this.heightMap[z2 << 4 | x2];
    }

    public int getTopFilledSegment() {
        for (int i2 = this.storageArrays.length - 1; i2 >= 0; --i2) {
            if (this.storageArrays[i2] == null) continue;
            return this.storageArrays[i2].getYLocation();
        }
        return 0;
    }

    public ExtendedBlockStorage[] getBlockStorageArray() {
        return this.storageArrays;
    }

    protected void generateHeightMap() {
        int i2 = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;
        for (int j2 = 0; j2 < 16; ++j2) {
            block1: for (int k2 = 0; k2 < 16; ++k2) {
                this.precipitationHeightMap[j2 + (k2 << 4)] = -999;
                for (int l2 = i2 + 16; l2 > 0; --l2) {
                    Block block = this.getBlock0(j2, l2 - 1, k2);
                    if (block.getLightOpacity() == 0) continue;
                    this.heightMap[k2 << 4 | j2] = l2;
                    if (l2 >= this.heightMapMinimum) continue block1;
                    this.heightMapMinimum = l2;
                    continue block1;
                }
            }
        }
        this.isModified = true;
    }

    public void generateSkylightMap() {
        int i2 = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;
        for (int j2 = 0; j2 < 16; ++j2) {
            for (int k2 = 0; k2 < 16; ++k2) {
                this.precipitationHeightMap[j2 + (k2 << 4)] = -999;
                for (int l2 = i2 + 16; l2 > 0; --l2) {
                    if (this.getBlockLightOpacity(j2, l2 - 1, k2) == 0) continue;
                    this.heightMap[k2 << 4 | j2] = l2;
                    if (l2 >= this.heightMapMinimum) break;
                    this.heightMapMinimum = l2;
                    break;
                }
                if (this.worldObj.provider.getHasNoSky()) continue;
                int k1 = 15;
                int i1 = i2 + 16 - 1;
                do {
                    ExtendedBlockStorage extendedblockstorage;
                    int j1;
                    if ((j1 = this.getBlockLightOpacity(j2, i1, k2)) == 0 && k1 != 15) {
                        j1 = 1;
                    }
                    if ((k1 -= j1) <= 0 || (extendedblockstorage = this.storageArrays[i1 >> 4]) == null) continue;
                    extendedblockstorage.setExtSkylightValue(j2, i1 & 0xF, k2, k1);
                    this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + j2, i1, (this.zPosition << 4) + k2));
                } while (--i1 > 0 && k1 > 0);
            }
        }
        this.isModified = true;
    }

    private void propagateSkylightOcclusion(int x2, int z2) {
        this.updateSkylightColumns[x2 + z2 * 16] = true;
        this.isGapLightingUpdated = true;
    }

    private void recheckGaps(boolean p_150803_1_) {
        this.worldObj.theProfiler.startSection("recheckGaps");
        if (this.worldObj.isAreaLoaded(new BlockPos(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8), 16)) {
            for (int i2 = 0; i2 < 16; ++i2) {
                for (int j2 = 0; j2 < 16; ++j2) {
                    if (!this.updateSkylightColumns[i2 + j2 * 16]) continue;
                    this.updateSkylightColumns[i2 + j2 * 16] = false;
                    int k2 = this.getHeightValue(i2, j2);
                    int l2 = this.xPosition * 16 + i2;
                    int i1 = this.zPosition * 16 + j2;
                    int j1 = Integer.MAX_VALUE;
                    for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL) {
                        EnumFacing enumfacing = (EnumFacing)enumfacing0;
                        j1 = Math.min(j1, this.worldObj.getChunksLowestHorizon(l2 + enumfacing.getFrontOffsetX(), i1 + enumfacing.getFrontOffsetZ()));
                    }
                    this.checkSkylightNeighborHeight(l2, i1, j1);
                    for (Object enumfacing10 : EnumFacing.Plane.HORIZONTAL) {
                        EnumFacing enumfacing1 = (EnumFacing)enumfacing10;
                        this.checkSkylightNeighborHeight(l2 + enumfacing1.getFrontOffsetX(), i1 + enumfacing1.getFrontOffsetZ(), k2);
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

    private void checkSkylightNeighborHeight(int x2, int z2, int maxValue) {
        int i2 = this.worldObj.getHeight(new BlockPos(x2, 0, z2)).getY();
        if (i2 > maxValue) {
            this.updateSkylightNeighborHeight(x2, z2, maxValue, i2 + 1);
        } else if (i2 < maxValue) {
            this.updateSkylightNeighborHeight(x2, z2, i2, maxValue + 1);
        }
    }

    private void updateSkylightNeighborHeight(int x2, int z2, int startY, int endY) {
        if (endY > startY && this.worldObj.isAreaLoaded(new BlockPos(x2, 0, z2), 16)) {
            for (int i2 = startY; i2 < endY; ++i2) {
                this.worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x2, i2, z2));
            }
            this.isModified = true;
        }
    }

    private void relightBlock(int x2, int y2, int z2) {
        int i2;
        int j2 = i2 = this.heightMap[z2 << 4 | x2] & 0xFF;
        if (y2 > i2) {
            j2 = y2;
        }
        while (j2 > 0 && this.getBlockLightOpacity(x2, j2 - 1, z2) == 0) {
            --j2;
        }
        if (j2 != i2) {
            this.worldObj.markBlocksDirtyVertical(x2 + this.xPosition * 16, z2 + this.zPosition * 16, j2, i2);
            this.heightMap[z2 << 4 | x2] = j2;
            int k2 = this.xPosition * 16 + x2;
            int l2 = this.zPosition * 16 + z2;
            if (!this.worldObj.provider.getHasNoSky()) {
                if (j2 < i2) {
                    for (int j1 = j2; j1 < i2; ++j1) {
                        ExtendedBlockStorage extendedblockstorage2 = this.storageArrays[j1 >> 4];
                        if (extendedblockstorage2 == null) continue;
                        extendedblockstorage2.setExtSkylightValue(x2, j1 & 0xF, z2, 15);
                        this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x2, j1, (this.zPosition << 4) + z2));
                    }
                } else {
                    for (int i1 = i2; i1 < j2; ++i1) {
                        ExtendedBlockStorage extendedblockstorage = this.storageArrays[i1 >> 4];
                        if (extendedblockstorage == null) continue;
                        extendedblockstorage.setExtSkylightValue(x2, i1 & 0xF, z2, 0);
                        this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x2, i1, (this.zPosition << 4) + z2));
                    }
                }
                int k1 = 15;
                while (j2 > 0 && k1 > 0) {
                    ExtendedBlockStorage extendedblockstorage1;
                    int i22;
                    if ((i22 = this.getBlockLightOpacity(x2, --j2, z2)) == 0) {
                        i22 = 1;
                    }
                    if ((k1 -= i22) < 0) {
                        k1 = 0;
                    }
                    if ((extendedblockstorage1 = this.storageArrays[j2 >> 4]) == null) continue;
                    extendedblockstorage1.setExtSkylightValue(x2, j2 & 0xF, z2, k1);
                }
            }
            int l1 = this.heightMap[z2 << 4 | x2];
            int j22 = i2;
            int k22 = l1;
            if (l1 < i2) {
                j22 = l1;
                k22 = i2;
            }
            if (l1 < this.heightMapMinimum) {
                this.heightMapMinimum = l1;
            }
            if (!this.worldObj.provider.getHasNoSky()) {
                for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL) {
                    EnumFacing enumfacing = (EnumFacing)enumfacing0;
                    this.updateSkylightNeighborHeight(k2 + enumfacing.getFrontOffsetX(), l2 + enumfacing.getFrontOffsetZ(), j22, k22);
                }
                this.updateSkylightNeighborHeight(k2, l2, j22, k22);
            }
            this.isModified = true;
        }
    }

    public int getBlockLightOpacity(BlockPos pos) {
        return this.getBlock(pos).getLightOpacity();
    }

    private int getBlockLightOpacity(int x2, int y2, int z2) {
        return this.getBlock0(x2, y2, z2).getLightOpacity();
    }

    private Block getBlock0(int x2, int y2, int z2) {
        ExtendedBlockStorage extendedblockstorage;
        Block block = Blocks.air;
        if (y2 >= 0 && y2 >> 4 < this.storageArrays.length && (extendedblockstorage = this.storageArrays[y2 >> 4]) != null) {
            try {
                block = extendedblockstorage.getBlockByExtId(x2, y2 & 0xF, z2);
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Getting block");
                throw new ReportedException(crashreport);
            }
        }
        return block;
    }

    public Block getBlock(final int x2, final int y2, final int z2) {
        try {
            return this.getBlock0(x2 & 0xF, y2, z2 & 0xF);
        }
        catch (ReportedException reportedexception) {
            CrashReportCategory crashreportcategory = reportedexception.getCrashReport().makeCategory("Block being got");
            crashreportcategory.addCrashSectionCallable("Location", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(new BlockPos(Chunk.this.xPosition * 16 + x2, y2, Chunk.this.zPosition * 16 + z2));
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
            IBlockState iblockstate = null;
            if (pos.getY() == 60) {
                iblockstate = Blocks.barrier.getDefaultState();
            }
            if (pos.getY() == 70) {
                iblockstate = ChunkProviderDebug.func_177461_b(pos.getX(), pos.getZ());
            }
            return iblockstate == null ? Blocks.air.getDefaultState() : iblockstate;
        }
        try {
            ExtendedBlockStorage extendedblockstorage;
            if (pos.getY() >= 0 && pos.getY() >> 4 < this.storageArrays.length && (extendedblockstorage = this.storageArrays[pos.getY() >> 4]) != null) {
                int j2 = pos.getX() & 0xF;
                int k2 = pos.getY() & 0xF;
                int i2 = pos.getZ() & 0xF;
                return extendedblockstorage.get(j2, k2, i2);
            }
            return Blocks.air.getDefaultState();
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

    private int getBlockMetadata(int x2, int y2, int z2) {
        if (y2 >> 4 >= this.storageArrays.length) {
            return 0;
        }
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[y2 >> 4];
        return extendedblockstorage != null ? extendedblockstorage.getExtBlockMetadata(x2, y2 & 0xF, z2) : 0;
    }

    public int getBlockMetadata(BlockPos pos) {
        return this.getBlockMetadata(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
    }

    public IBlockState setBlockState(BlockPos pos, IBlockState state) {
        TileEntity tileentity;
        int k2;
        int l2;
        int i2 = pos.getX() & 0xF;
        int j2 = pos.getY();
        if (j2 >= this.precipitationHeightMap[l2 = (k2 = pos.getZ() & 0xF) << 4 | i2] - 1) {
            this.precipitationHeightMap[l2] = -999;
        }
        int i1 = this.heightMap[l2];
        IBlockState iblockstate = this.getBlockState(pos);
        if (iblockstate == state) {
            return null;
        }
        Block block = state.getBlock();
        Block block1 = iblockstate.getBlock();
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j2 >> 4];
        boolean flag = false;
        if (extendedblockstorage == null) {
            if (block == Blocks.air) {
                return null;
            }
            ExtendedBlockStorage extendedBlockStorage = new ExtendedBlockStorage(j2 >> 4 << 4, !this.worldObj.provider.getHasNoSky());
            this.storageArrays[j2 >> 4] = extendedBlockStorage;
            extendedblockstorage = extendedBlockStorage;
            flag = j2 >= i1;
        }
        extendedblockstorage.set(i2, j2 & 0xF, k2, state);
        if (block1 != block) {
            if (!this.worldObj.isRemote) {
                block1.breakBlock(this.worldObj, pos, iblockstate);
            } else if (block1 instanceof ITileEntityProvider) {
                this.worldObj.removeTileEntity(pos);
            }
        }
        if (extendedblockstorage.getBlockByExtId(i2, j2 & 0xF, k2) != block) {
            return null;
        }
        if (flag) {
            this.generateSkylightMap();
        } else {
            int j1 = block.getLightOpacity();
            int k1 = block1.getLightOpacity();
            if (j1 > 0) {
                if (j2 >= i1) {
                    this.relightBlock(i2, j2 + 1, k2);
                }
            } else if (j2 == i1 - 1) {
                this.relightBlock(i2, j2, k2);
            }
            if (j1 != k1 && (j1 < k1 || this.getLightFor(EnumSkyBlock.SKY, pos) > 0 || this.getLightFor(EnumSkyBlock.BLOCK, pos) > 0)) {
                this.propagateSkylightOcclusion(i2, k2);
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
        int i2 = pos.getX() & 0xF;
        int j2 = pos.getY();
        int k2 = pos.getZ() & 0xF;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j2 >> 4];
        return extendedblockstorage == null ? (this.canSeeSky(pos) ? p_177413_1_.defaultLightValue : 0) : (p_177413_1_ == EnumSkyBlock.SKY ? (this.worldObj.provider.getHasNoSky() ? 0 : extendedblockstorage.getExtSkylightValue(i2, j2 & 0xF, k2)) : (p_177413_1_ == EnumSkyBlock.BLOCK ? extendedblockstorage.getExtBlocklightValue(i2, j2 & 0xF, k2) : p_177413_1_.defaultLightValue));
    }

    public void setLightFor(EnumSkyBlock p_177431_1_, BlockPos pos, int value) {
        int i2 = pos.getX() & 0xF;
        int j2 = pos.getY();
        int k2 = pos.getZ() & 0xF;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j2 >> 4];
        if (extendedblockstorage == null) {
            ExtendedBlockStorage extendedBlockStorage = new ExtendedBlockStorage(j2 >> 4 << 4, !this.worldObj.provider.getHasNoSky());
            this.storageArrays[j2 >> 4] = extendedBlockStorage;
            extendedblockstorage = extendedBlockStorage;
            this.generateSkylightMap();
        }
        this.isModified = true;
        if (p_177431_1_ == EnumSkyBlock.SKY) {
            if (!this.worldObj.provider.getHasNoSky()) {
                extendedblockstorage.setExtSkylightValue(i2, j2 & 0xF, k2, value);
            }
        } else if (p_177431_1_ == EnumSkyBlock.BLOCK) {
            extendedblockstorage.setExtBlocklightValue(i2, j2 & 0xF, k2, value);
        }
    }

    public int getLightSubtracted(BlockPos pos, int amount) {
        int i2 = pos.getX() & 0xF;
        int j2 = pos.getY();
        int k2 = pos.getZ() & 0xF;
        ExtendedBlockStorage extendedblockstorage = this.storageArrays[j2 >> 4];
        if (extendedblockstorage == null) {
            return !this.worldObj.provider.getHasNoSky() && amount < EnumSkyBlock.SKY.defaultLightValue ? EnumSkyBlock.SKY.defaultLightValue - amount : 0;
        }
        int l2 = this.worldObj.provider.getHasNoSky() ? 0 : extendedblockstorage.getExtSkylightValue(i2, j2 & 0xF, k2);
        int i1 = extendedblockstorage.getExtBlocklightValue(i2, j2 & 0xF, k2);
        if (i1 > (l2 -= amount)) {
            l2 = i1;
        }
        return l2;
    }

    public void addEntity(Entity entityIn) {
        int k2;
        this.hasEntities = true;
        int i2 = MathHelper.floor_double(entityIn.posX / 16.0);
        int j2 = MathHelper.floor_double(entityIn.posZ / 16.0);
        if (i2 != this.xPosition || j2 != this.zPosition) {
            logger.warn("Wrong location! (" + i2 + ", " + j2 + ") should be (" + this.xPosition + ", " + this.zPosition + "), " + entityIn, entityIn);
            entityIn.setDead();
        }
        if ((k2 = MathHelper.floor_double(entityIn.posY / 16.0)) < 0) {
            k2 = 0;
        }
        if (k2 >= this.entityLists.length) {
            k2 = this.entityLists.length - 1;
        }
        entityIn.addedToChunk = true;
        entityIn.chunkCoordX = this.xPosition;
        entityIn.chunkCoordY = k2;
        entityIn.chunkCoordZ = this.zPosition;
        this.entityLists[k2].add(entityIn);
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
        int k2;
        int i2 = pos.getX() & 0xF;
        int j2 = pos.getY();
        return j2 >= this.heightMap[(k2 = pos.getZ() & 0xF) << 4 | i2];
    }

    private TileEntity createNewTileEntity(BlockPos pos) {
        Block block = this.getBlock(pos);
        return !block.hasTileEntity() ? null : ((ITileEntityProvider)((Object)block)).createNewTileEntity(this.worldObj, this.getBlockMetadata(pos));
    }

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
        if (this.getBlock(pos) instanceof ITileEntityProvider) {
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
        for (int i2 = 0; i2 < this.entityLists.length; ++i2) {
            for (Entity entity : this.entityLists[i2]) {
                entity.onChunkLoad();
            }
            this.worldObj.loadEntities(this.entityLists[i2]);
        }
    }

    public void onChunkUnload() {
        this.isChunkLoaded = false;
        for (TileEntity tileentity : this.chunkTileEntityMap.values()) {
            this.worldObj.markTileEntityForRemoval(tileentity);
        }
        for (int i2 = 0; i2 < this.entityLists.length; ++i2) {
            this.worldObj.unloadEntities(this.entityLists[i2]);
        }
    }

    public void setChunkModified() {
        this.isModified = true;
    }

    public void getEntitiesWithinAABBForEntity(Entity entityIn, AxisAlignedBB aabb, List<Entity> listToFill, Predicate<? super Entity> p_177414_4_) {
        int i2 = MathHelper.floor_double((aabb.minY - 2.0) / 16.0);
        int j2 = MathHelper.floor_double((aabb.maxY + 2.0) / 16.0);
        i2 = MathHelper.clamp_int(i2, 0, this.entityLists.length - 1);
        j2 = MathHelper.clamp_int(j2, 0, this.entityLists.length - 1);
        for (int k2 = i2; k2 <= j2; ++k2) {
            if (this.entityLists[k2].isEmpty()) continue;
            for (Entity entity : this.entityLists[k2]) {
                Entity[] aentity;
                if (!entity.getEntityBoundingBox().intersectsWith(aabb) || entity == entityIn) continue;
                if (p_177414_4_ == null || p_177414_4_.apply(entity)) {
                    listToFill.add(entity);
                }
                if ((aentity = entity.getParts()) == null) continue;
                for (int l2 = 0; l2 < aentity.length; ++l2) {
                    entity = aentity[l2];
                    if (entity == entityIn || !entity.getEntityBoundingBox().intersectsWith(aabb) || p_177414_4_ != null && !p_177414_4_.apply(entity)) continue;
                    listToFill.add(entity);
                }
            }
        }
    }

    public <T extends Entity> void getEntitiesOfTypeWithinAAAB(Class<? extends T> entityClass, AxisAlignedBB aabb, List<T> listToFill, Predicate<? super T> p_177430_4_) {
        int i2 = MathHelper.floor_double((aabb.minY - 2.0) / 16.0);
        int j2 = MathHelper.floor_double((aabb.maxY + 2.0) / 16.0);
        i2 = MathHelper.clamp_int(i2, 0, this.entityLists.length - 1);
        j2 = MathHelper.clamp_int(j2, 0, this.entityLists.length - 1);
        for (int k2 = i2; k2 <= j2; ++k2) {
            for (Entity t2 : this.entityLists[k2].getByClass(entityClass)) {
                if (!t2.getEntityBoundingBox().intersectsWith(aabb) || p_177430_4_ != null && !p_177430_4_.apply(t2)) continue;
                listToFill.add(t2);
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
        if (flag4 && flag && flag3) {
            Chunk chunk2 = p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_ - 1);
            if (!chunk2.isTerrainPopulated) {
                p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_ - 1);
            } else {
                p_76624_1_.func_177460_a(p_76624_2_, chunk2, p_76624_3_ - 1, p_76624_4_ - 1);
            }
        }
    }

    public BlockPos getPrecipitationHeight(BlockPos pos) {
        int i2 = pos.getX() & 0xF;
        int j2 = pos.getZ() & 0xF;
        int k2 = i2 | j2 << 4;
        BlockPos blockpos = new BlockPos(pos.getX(), this.precipitationHeightMap[k2], pos.getZ());
        if (blockpos.getY() == -999) {
            int l2 = this.getTopFilledSegment() + 15;
            blockpos = new BlockPos(pos.getX(), l2, pos.getZ());
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
            this.precipitationHeightMap[k2] = i1;
        }
        return new BlockPos(pos.getX(), this.precipitationHeightMap[k2], pos.getZ());
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
        return this.field_150815_m && this.isTerrainPopulated && this.isLightPopulated;
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
        for (int i2 = startY; i2 <= endY; i2 += 16) {
            ExtendedBlockStorage extendedblockstorage = this.storageArrays[i2 >> 4];
            if (extendedblockstorage == null || extendedblockstorage.isEmpty()) continue;
            return false;
        }
        return true;
    }

    public void setStorageArrays(ExtendedBlockStorage[] newStorageArrays) {
        if (this.storageArrays.length != newStorageArrays.length) {
            logger.warn("Could not set level chunk sections, array length is " + newStorageArrays.length + " instead of " + this.storageArrays.length);
        } else {
            for (int i2 = 0; i2 < this.storageArrays.length; ++i2) {
                this.storageArrays[i2] = newStorageArrays[i2];
            }
        }
    }

    public void fillChunk(byte[] p_177439_1_, int p_177439_2_, boolean p_177439_3_) {
        int i1;
        int i2 = 0;
        boolean flag = !this.worldObj.provider.getHasNoSky();
        for (int j2 = 0; j2 < this.storageArrays.length; ++j2) {
            if ((p_177439_2_ & 1 << j2) != 0) {
                if (this.storageArrays[j2] == null) {
                    this.storageArrays[j2] = new ExtendedBlockStorage(j2 << 4, flag);
                }
                char[] achar = this.storageArrays[j2].getData();
                for (int k2 = 0; k2 < achar.length; ++k2) {
                    achar[k2] = (char)((p_177439_1_[i2 + 1] & 0xFF) << 8 | p_177439_1_[i2] & 0xFF);
                    i2 += 2;
                }
                continue;
            }
            if (!p_177439_3_ || this.storageArrays[j2] == null) continue;
            this.storageArrays[j2] = null;
        }
        for (int l2 = 0; l2 < this.storageArrays.length; ++l2) {
            if ((p_177439_2_ & 1 << l2) == 0 || this.storageArrays[l2] == null) continue;
            NibbleArray nibblearray = this.storageArrays[l2].getBlocklightArray();
            System.arraycopy(p_177439_1_, i2, nibblearray.getData(), 0, nibblearray.getData().length);
            i2 += nibblearray.getData().length;
        }
        if (flag) {
            for (i1 = 0; i1 < this.storageArrays.length; ++i1) {
                if ((p_177439_2_ & 1 << i1) == 0 || this.storageArrays[i1] == null) continue;
                NibbleArray nibblearray1 = this.storageArrays[i1].getSkylightArray();
                System.arraycopy(p_177439_1_, i2, nibblearray1.getData(), 0, nibblearray1.getData().length);
                i2 += nibblearray1.getData().length;
            }
        }
        if (p_177439_3_) {
            System.arraycopy(p_177439_1_, i2, this.blockBiomeArray, 0, this.blockBiomeArray.length);
            i1 = i2 + this.blockBiomeArray.length;
        }
        for (int j1 = 0; j1 < this.storageArrays.length; ++j1) {
            if (this.storageArrays[j1] == null || (p_177439_2_ & 1 << j1) == 0) continue;
            this.storageArrays[j1].removeInvalidBlocks();
        }
        this.isLightPopulated = true;
        this.isTerrainPopulated = true;
        this.generateHeightMap();
        for (TileEntity tileentity : this.chunkTileEntityMap.values()) {
            tileentity.updateContainingBlockInfo();
        }
    }

    public BiomeGenBase getBiome(BlockPos pos, WorldChunkManager chunkManager) {
        BiomeGenBase biomegenbase1;
        int i2 = pos.getX() & 0xF;
        int j2 = pos.getZ() & 0xF;
        int k2 = this.blockBiomeArray[j2 << 4 | i2] & 0xFF;
        if (k2 == 255) {
            BiomeGenBase biomegenbase = chunkManager.getBiomeGenerator(pos, BiomeGenBase.plains);
            k2 = biomegenbase.biomeID;
            this.blockBiomeArray[j2 << 4 | i2] = (byte)(k2 & 0xFF);
        }
        return (biomegenbase1 = BiomeGenBase.getBiome(k2)) == null ? BiomeGenBase.plains : biomegenbase1;
    }

    public byte[] getBiomeArray() {
        return this.blockBiomeArray;
    }

    public void setBiomeArray(byte[] biomeArray) {
        if (this.blockBiomeArray.length != biomeArray.length) {
            logger.warn("Could not set level chunk biomes, array length is " + biomeArray.length + " instead of " + this.blockBiomeArray.length);
        } else {
            for (int i2 = 0; i2 < this.blockBiomeArray.length; ++i2) {
                this.blockBiomeArray[i2] = biomeArray[i2];
            }
        }
    }

    public void resetRelightChecks() {
        this.queuedLightChecks = 0;
    }

    public void enqueueRelightChecks() {
        BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
        for (int i2 = 0; i2 < 8; ++i2) {
            if (this.queuedLightChecks >= 4096) {
                return;
            }
            int j2 = this.queuedLightChecks % 16;
            int k2 = this.queuedLightChecks / 16 % 16;
            int l2 = this.queuedLightChecks / 256;
            ++this.queuedLightChecks;
            for (int i1 = 0; i1 < 16; ++i1) {
                boolean flag;
                BlockPos blockpos1 = blockpos.add(k2, (j2 << 4) + i1, l2);
                boolean bl = flag = i1 == 0 || i1 == 15 || k2 == 0 || k2 == 15 || l2 == 0 || l2 == 15;
                if ((this.storageArrays[j2] != null || !flag) && (this.storageArrays[j2] == null || this.storageArrays[j2].getBlockByExtId(k2, i1, l2).getMaterial() != Material.air)) continue;
                for (EnumFacing enumfacing : EnumFacing.values()) {
                    BlockPos blockpos2 = blockpos1.offset(enumfacing);
                    if (this.worldObj.getBlockState(blockpos2).getBlock().getLightValue() <= 0) continue;
                    this.worldObj.checkLight(blockpos2);
                }
                this.worldObj.checkLight(blockpos1);
            }
        }
    }

    public void func_150809_p() {
        this.isTerrainPopulated = true;
        this.isLightPopulated = true;
        BlockPos blockpos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
        if (!this.worldObj.provider.getHasNoSky()) {
            if (this.worldObj.isAreaLoaded(blockpos.add(-1, 0, -1), blockpos.add(16, this.worldObj.func_181545_F(), 16))) {
                block0: for (int i2 = 0; i2 < 16; ++i2) {
                    for (int j2 = 0; j2 < 16; ++j2) {
                        if (this.func_150811_f(i2, j2)) continue;
                        this.isLightPopulated = false;
                        break block0;
                    }
                }
                if (this.isLightPopulated) {
                    for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL) {
                        EnumFacing enumfacing = (EnumFacing)enumfacing0;
                        int k2 = enumfacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 16 : 1;
                        this.worldObj.getChunkFromBlockCoords(blockpos.offset(enumfacing, k2)).func_180700_a(enumfacing.getOpposite());
                    }
                    this.func_177441_y();
                }
            } else {
                this.isLightPopulated = false;
            }
        }
    }

    private void func_177441_y() {
        for (int i2 = 0; i2 < this.updateSkylightColumns.length; ++i2) {
            this.updateSkylightColumns[i2] = true;
        }
        this.recheckGaps(false);
    }

    private void func_180700_a(EnumFacing p_180700_1_) {
        block4: {
            block7: {
                block6: {
                    block5: {
                        if (!this.isTerrainPopulated) break block4;
                        if (p_180700_1_ != EnumFacing.EAST) break block5;
                        for (int i2 = 0; i2 < 16; ++i2) {
                            this.func_150811_f(15, i2);
                        }
                        break block4;
                    }
                    if (p_180700_1_ != EnumFacing.WEST) break block6;
                    for (int j2 = 0; j2 < 16; ++j2) {
                        this.func_150811_f(0, j2);
                    }
                    break block4;
                }
                if (p_180700_1_ != EnumFacing.SOUTH) break block7;
                for (int k2 = 0; k2 < 16; ++k2) {
                    this.func_150811_f(k2, 15);
                }
                break block4;
            }
            if (p_180700_1_ != EnumFacing.NORTH) break block4;
            for (int l2 = 0; l2 < 16; ++l2) {
                this.func_150811_f(l2, 0);
            }
        }
    }

    private boolean func_150811_f(int x2, int z2) {
        int i2 = this.getTopFilledSegment();
        boolean flag = false;
        boolean flag1 = false;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos((this.xPosition << 4) + x2, 0, (this.zPosition << 4) + z2);
        for (int j2 = i2 + 16 - 1; j2 > this.worldObj.func_181545_F() || j2 > 0 && !flag1; --j2) {
            blockpos$mutableblockpos.func_181079_c(blockpos$mutableblockpos.getX(), j2, blockpos$mutableblockpos.getZ());
            int k2 = this.getBlockLightOpacity(blockpos$mutableblockpos);
            if (k2 == 255 && blockpos$mutableblockpos.getY() < this.worldObj.func_181545_F()) {
                flag1 = true;
            }
            if (!flag && k2 > 0) {
                flag = true;
                continue;
            }
            if (!flag || k2 != 0 || this.worldObj.checkLight(blockpos$mutableblockpos)) continue;
            return false;
        }
        for (int l2 = blockpos$mutableblockpos.getY(); l2 > 0; --l2) {
            blockpos$mutableblockpos.func_181079_c(blockpos$mutableblockpos.getX(), l2, blockpos$mutableblockpos.getZ());
            if (this.getBlock(blockpos$mutableblockpos).getLightValue() <= 0) continue;
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
            logger.warn("Could not set level chunk heightmap, array length is " + newHeightMap.length + " instead of " + this.heightMap.length);
        } else {
            for (int i2 = 0; i2 < this.heightMap.length; ++i2) {
                this.heightMap[i2] = newHeightMap[i2];
            }
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

