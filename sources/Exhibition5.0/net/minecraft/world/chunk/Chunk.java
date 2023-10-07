// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.chunk;

import org.apache.logging.log4j.LogManager;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.ChunkCoordIntPair;
import java.util.Random;
import com.google.common.base.Predicate;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import java.util.Collection;
import net.minecraft.util.MathHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.world.gen.ChunkProviderDebug;
import net.minecraft.world.WorldType;
import net.minecraft.crash.CrashReportCategory;
import java.util.concurrent.Callable;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.init.Blocks;
import net.minecraft.world.EnumSkyBlock;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import java.util.Arrays;
import net.minecraft.entity.Entity;
import com.google.common.collect.Queues;
import com.google.common.collect.Maps;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.util.ClassInheratanceMultiMap;
import java.util.Map;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.apache.logging.log4j.Logger;

public class Chunk
{
    private static final Logger logger;
    private final ExtendedBlockStorage[] storageArrays;
    private final byte[] blockBiomeArray;
    private final int[] precipitationHeightMap;
    private final boolean[] updateSkylightColumns;
    private boolean isChunkLoaded;
    private final World worldObj;
    private final int[] heightMap;
    public final int xPosition;
    public final int zPosition;
    private boolean isGapLightingUpdated;
    private final Map chunkTileEntityMap;
    private final ClassInheratanceMultiMap[] entityLists;
    private boolean isTerrainPopulated;
    private boolean isLightPopulated;
    private boolean field_150815_m;
    private boolean isModified;
    private boolean hasEntities;
    private long lastSaveTime;
    private int heightMapMinimum;
    private long inhabitedTime;
    private int queuedLightChecks;
    private ConcurrentLinkedQueue field_177447_w;
    private static final String __OBFID = "CL_00000373";
    
    public Chunk(final World worldIn, final int x, final int z) {
        this.storageArrays = new ExtendedBlockStorage[16];
        this.blockBiomeArray = new byte[256];
        this.precipitationHeightMap = new int[256];
        this.updateSkylightColumns = new boolean[256];
        this.chunkTileEntityMap = Maps.newHashMap();
        this.queuedLightChecks = 4096;
        this.field_177447_w = Queues.newConcurrentLinkedQueue();
        this.entityLists = new ClassInheratanceMultiMap[16];
        this.worldObj = worldIn;
        this.xPosition = x;
        this.zPosition = z;
        this.heightMap = new int[256];
        for (int var4 = 0; var4 < this.entityLists.length; ++var4) {
            this.entityLists[var4] = new ClassInheratanceMultiMap(Entity.class);
        }
        Arrays.fill(this.precipitationHeightMap, -999);
        Arrays.fill(this.blockBiomeArray, (byte)(-1));
    }
    
    public Chunk(final World worldIn, final ChunkPrimer primer, final int x, final int z) {
        this(worldIn, x, z);
        final short var5 = 256;
        final boolean var6 = !worldIn.provider.getHasNoSky();
        for (int var7 = 0; var7 < 16; ++var7) {
            for (int var8 = 0; var8 < 16; ++var8) {
                for (int var9 = 0; var9 < var5; ++var9) {
                    final int var10 = var7 * var5 * 16 | var8 * var5 | var9;
                    final IBlockState var11 = primer.getBlockState(var10);
                    if (var11.getBlock().getMaterial() != Material.air) {
                        final int var12 = var9 >> 4;
                        if (this.storageArrays[var12] == null) {
                            this.storageArrays[var12] = new ExtendedBlockStorage(var12 << 4, var6);
                        }
                        this.storageArrays[var12].set(var7, var9 & 0xF, var8, var11);
                    }
                }
            }
        }
    }
    
    public boolean isAtLocation(final int x, final int z) {
        return x == this.xPosition && z == this.zPosition;
    }
    
    public int getHeight(final BlockPos pos) {
        return this.getHeight(pos.getX() & 0xF, pos.getZ() & 0xF);
    }
    
    public int getHeight(final int x, final int z) {
        return this.heightMap[z << 4 | x];
    }
    
    public int getTopFilledSegment() {
        for (int var1 = this.storageArrays.length - 1; var1 >= 0; --var1) {
            if (this.storageArrays[var1] != null) {
                return this.storageArrays[var1].getYLocation();
            }
        }
        return 0;
    }
    
    public ExtendedBlockStorage[] getBlockStorageArray() {
        return this.storageArrays;
    }
    
    protected void generateHeightMap() {
        final int var1 = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;
        for (int var2 = 0; var2 < 16; ++var2) {
            for (int var3 = 0; var3 < 16; ++var3) {
                this.precipitationHeightMap[var2 + (var3 << 4)] = -999;
                int var4 = var1 + 16;
                while (var4 > 0) {
                    final Block var5 = this.getBlock0(var2, var4 - 1, var3);
                    if (var5.getLightOpacity() == 0) {
                        --var4;
                    }
                    else {
                        if ((this.heightMap[var3 << 4 | var2] = var4) < this.heightMapMinimum) {
                            this.heightMapMinimum = var4;
                            break;
                        }
                        break;
                    }
                }
            }
        }
        this.isModified = true;
    }
    
    public void generateSkylightMap() {
        final int var1 = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;
        for (int var2 = 0; var2 < 16; ++var2) {
            for (int var3 = 0; var3 < 16; ++var3) {
                this.precipitationHeightMap[var2 + (var3 << 4)] = -999;
                int var4 = var1 + 16;
                while (var4 > 0) {
                    if (this.getBlockLightOpacity(var2, var4 - 1, var3) == 0) {
                        --var4;
                    }
                    else {
                        if ((this.heightMap[var3 << 4 | var2] = var4) < this.heightMapMinimum) {
                            this.heightMapMinimum = var4;
                            break;
                        }
                        break;
                    }
                }
                if (!this.worldObj.provider.getHasNoSky()) {
                    var4 = 15;
                    int var5 = var1 + 16 - 1;
                    do {
                        int var6 = this.getBlockLightOpacity(var2, var5, var3);
                        if (var6 == 0 && var4 != 15) {
                            var6 = 1;
                        }
                        var4 -= var6;
                        if (var4 > 0) {
                            final ExtendedBlockStorage var7 = this.storageArrays[var5 >> 4];
                            if (var7 == null) {
                                continue;
                            }
                            var7.setExtSkylightValue(var2, var5 & 0xF, var3, var4);
                            this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + var2, var5, (this.zPosition << 4) + var3));
                        }
                    } while (--var5 > 0 && var4 > 0);
                }
            }
        }
        this.isModified = true;
    }
    
    private void propagateSkylightOcclusion(final int x, final int z) {
        this.updateSkylightColumns[x + z * 16] = true;
        this.isGapLightingUpdated = true;
    }
    
    private void recheckGaps(final boolean p_150803_1_) {
        this.worldObj.theProfiler.startSection("recheckGaps");
        if (this.worldObj.isAreaLoaded(new BlockPos(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8), 16)) {
            for (int var2 = 0; var2 < 16; ++var2) {
                for (int var3 = 0; var3 < 16; ++var3) {
                    if (this.updateSkylightColumns[var2 + var3 * 16]) {
                        this.updateSkylightColumns[var2 + var3 * 16] = false;
                        final int var4 = this.getHeight(var2, var3);
                        final int var5 = this.xPosition * 16 + var2;
                        final int var6 = this.zPosition * 16 + var3;
                        int var7 = Integer.MAX_VALUE;
                        for (final EnumFacing var9 : EnumFacing.Plane.HORIZONTAL) {
                            var7 = Math.min(var7, this.worldObj.getChunksLowestHorizon(var5 + var9.getFrontOffsetX(), var6 + var9.getFrontOffsetZ()));
                        }
                        this.checkSkylightNeighborHeight(var5, var6, var7);
                        for (final EnumFacing var9 : EnumFacing.Plane.HORIZONTAL) {
                            this.checkSkylightNeighborHeight(var5 + var9.getFrontOffsetX(), var6 + var9.getFrontOffsetZ(), var4);
                        }
                        if (p_150803_1_) {
                            this.worldObj.theProfiler.endSection();
                            return;
                        }
                    }
                }
            }
            this.isGapLightingUpdated = false;
        }
        this.worldObj.theProfiler.endSection();
    }
    
    private void checkSkylightNeighborHeight(final int x, final int p_76599_2_, final int z) {
        final int var4 = this.worldObj.getHorizon(new BlockPos(x, 0, p_76599_2_)).getY();
        if (var4 > z) {
            this.updateSkylightNeighborHeight(x, p_76599_2_, z, var4 + 1);
        }
        else if (var4 < z) {
            this.updateSkylightNeighborHeight(x, p_76599_2_, var4, z + 1);
        }
    }
    
    private void updateSkylightNeighborHeight(final int x, final int z, final int startY, final int endY) {
        if (endY > startY && this.worldObj.isAreaLoaded(new BlockPos(x, 0, z), 16)) {
            for (int var5 = startY; var5 < endY; ++var5) {
                this.worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x, var5, z));
            }
            this.isModified = true;
        }
    }
    
    private void relightBlock(final int x, final int y, final int z) {
        int var5;
        final int var4 = var5 = (this.heightMap[z << 4 | x] & 0xFF);
        if (y > var4) {
            var5 = y;
        }
        while (var5 > 0 && this.getBlockLightOpacity(x, var5 - 1, z) == 0) {
            --var5;
        }
        if (var5 != var4) {
            this.worldObj.markBlocksDirtyVertical(x + this.xPosition * 16, z + this.zPosition * 16, var5, var4);
            this.heightMap[z << 4 | x] = var5;
            final int var6 = this.xPosition * 16 + x;
            final int var7 = this.zPosition * 16 + z;
            if (!this.worldObj.provider.getHasNoSky()) {
                if (var5 < var4) {
                    for (int var8 = var5; var8 < var4; ++var8) {
                        final ExtendedBlockStorage var9 = this.storageArrays[var8 >> 4];
                        if (var9 != null) {
                            var9.setExtSkylightValue(x, var8 & 0xF, z, 15);
                            this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, var8, (this.zPosition << 4) + z));
                        }
                    }
                }
                else {
                    for (int var8 = var4; var8 < var5; ++var8) {
                        final ExtendedBlockStorage var9 = this.storageArrays[var8 >> 4];
                        if (var9 != null) {
                            var9.setExtSkylightValue(x, var8 & 0xF, z, 0);
                            this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + x, var8, (this.zPosition << 4) + z));
                        }
                    }
                }
                int var8 = 15;
                while (var5 > 0 && var8 > 0) {
                    --var5;
                    int var10 = this.getBlockLightOpacity(x, var5, z);
                    if (var10 == 0) {
                        var10 = 1;
                    }
                    var8 -= var10;
                    if (var8 < 0) {
                        var8 = 0;
                    }
                    final ExtendedBlockStorage var11 = this.storageArrays[var5 >> 4];
                    if (var11 != null) {
                        var11.setExtSkylightValue(x, var5 & 0xF, z, var8);
                    }
                }
            }
            int var8 = this.heightMap[z << 4 | x];
            int var10;
            int var12;
            if ((var12 = var8) < (var10 = var4)) {
                var10 = var8;
                var12 = var4;
            }
            if (var8 < this.heightMapMinimum) {
                this.heightMapMinimum = var8;
            }
            if (!this.worldObj.provider.getHasNoSky()) {
                for (final EnumFacing var14 : EnumFacing.Plane.HORIZONTAL) {
                    this.updateSkylightNeighborHeight(var6 + var14.getFrontOffsetX(), var7 + var14.getFrontOffsetZ(), var10, var12);
                }
                this.updateSkylightNeighborHeight(var6, var7, var10, var12);
            }
            this.isModified = true;
        }
    }
    
    public int getBlockLightOpacity(final BlockPos pos) {
        return this.getBlock(pos).getLightOpacity();
    }
    
    private int getBlockLightOpacity(final int p_150808_1_, final int p_150808_2_, final int p_150808_3_) {
        return this.getBlock0(p_150808_1_, p_150808_2_, p_150808_3_).getLightOpacity();
    }
    
    public Block getBlock0(final int x, final int y, final int z) {
        Block var4 = Blocks.air;
        if (y >= 0 && y >> 4 < this.storageArrays.length) {
            final ExtendedBlockStorage var5 = this.storageArrays[y >> 4];
            if (var5 != null) {
                try {
                    var4 = var5.getBlockByExtId(x, y & 0xF, z);
                }
                catch (Throwable var7) {
                    final CrashReport var6 = CrashReport.makeCrashReport(var7, "Getting block");
                    throw new ReportedException(var6);
                }
            }
        }
        return var4;
    }
    
    public Block getBlock(final int x, final int y, final int z) {
        try {
            return this.getBlock0(x & 0xF, y, z & 0xF);
        }
        catch (ReportedException var6) {
            final CrashReportCategory var5 = var6.getCrashReport().makeCategory("Block being got");
            var5.addCrashSectionCallable("Location", new Callable() {
                private static final String __OBFID = "CL_00000374";
                
                @Override
                public String call() {
                    return CrashReportCategory.getCoordinateInfo(new BlockPos(Chunk.this.xPosition * 16 + x, y, Chunk.this.zPosition * 16 + z));
                }
            });
            throw var6;
        }
    }
    
    public Block getBlock(final BlockPos pos) {
        try {
            return this.getBlock0(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
        }
        catch (ReportedException var4) {
            final CrashReportCategory var3 = var4.getCrashReport().makeCategory("Block being got");
            var3.addCrashSectionCallable("Location", new Callable() {
                private static final String __OBFID = "CL_00002011";
                
                public String func_177455_a() {
                    return CrashReportCategory.getCoordinateInfo(pos);
                }
                
                @Override
                public Object call() {
                    return this.func_177455_a();
                }
            });
            throw var4;
        }
    }
    
    public IBlockState getBlockState(final BlockPos pos) {
        if (this.worldObj.getWorldType() == WorldType.DEBUG_WORLD) {
            IBlockState var7 = null;
            if (pos.getY() == 60) {
                var7 = Blocks.barrier.getDefaultState();
            }
            if (pos.getY() == 70) {
                var7 = ChunkProviderDebug.func_177461_b(pos.getX(), pos.getZ());
            }
            return (var7 == null) ? Blocks.air.getDefaultState() : var7;
        }
        try {
            if (pos.getY() >= 0 && pos.getY() >> 4 < this.storageArrays.length) {
                final ExtendedBlockStorage var8 = this.storageArrays[pos.getY() >> 4];
                if (var8 != null) {
                    final int var9 = pos.getX() & 0xF;
                    final int var10 = pos.getY() & 0xF;
                    final int var11 = pos.getZ() & 0xF;
                    return var8.get(var9, var10, var11);
                }
            }
            return Blocks.air.getDefaultState();
        }
        catch (Throwable var13) {
            final CrashReport var12 = CrashReport.makeCrashReport(var13, "Getting block state");
            final CrashReportCategory var14 = var12.makeCategory("Block being got");
            var14.addCrashSectionCallable("Location", new Callable() {
                private static final String __OBFID = "CL_00002010";
                
                public String func_177448_a() {
                    return CrashReportCategory.getCoordinateInfo(pos);
                }
                
                @Override
                public Object call() {
                    return this.func_177448_a();
                }
            });
            throw new ReportedException(var12);
        }
    }
    
    private int getBlockMetadata(final int p_76628_1_, final int p_76628_2_, final int p_76628_3_) {
        if (p_76628_2_ >> 4 >= this.storageArrays.length) {
            return 0;
        }
        final ExtendedBlockStorage var4 = this.storageArrays[p_76628_2_ >> 4];
        return (var4 != null) ? var4.getExtBlockMetadata(p_76628_1_, p_76628_2_ & 0xF, p_76628_3_) : 0;
    }
    
    public int getBlockMetadata(final BlockPos pos) {
        return this.getBlockMetadata(pos.getX() & 0xF, pos.getY(), pos.getZ() & 0xF);
    }
    
    public IBlockState setBlockState(final BlockPos p_177436_1_, final IBlockState p_177436_2_) {
        final int var3 = p_177436_1_.getX() & 0xF;
        final int var4 = p_177436_1_.getY();
        final int var5 = p_177436_1_.getZ() & 0xF;
        final int var6 = var5 << 4 | var3;
        if (var4 >= this.precipitationHeightMap[var6] - 1) {
            this.precipitationHeightMap[var6] = -999;
        }
        final int var7 = this.heightMap[var6];
        final IBlockState var8 = this.getBlockState(p_177436_1_);
        if (var8 == p_177436_2_) {
            return null;
        }
        final Block var9 = p_177436_2_.getBlock();
        final Block var10 = var8.getBlock();
        ExtendedBlockStorage var11 = this.storageArrays[var4 >> 4];
        boolean var12 = false;
        if (var11 == null) {
            if (var9 == Blocks.air) {
                return null;
            }
            final ExtendedBlockStorage[] storageArrays = this.storageArrays;
            final int n = var4 >> 4;
            final ExtendedBlockStorage extendedBlockStorage = new ExtendedBlockStorage(var4 >> 4 << 4, !this.worldObj.provider.getHasNoSky());
            storageArrays[n] = extendedBlockStorage;
            var11 = extendedBlockStorage;
            var12 = (var4 >= var7);
        }
        var11.set(var3, var4 & 0xF, var5, p_177436_2_);
        if (var10 != var9) {
            if (!this.worldObj.isRemote) {
                var10.breakBlock(this.worldObj, p_177436_1_, var8);
            }
            else if (var10 instanceof ITileEntityProvider) {
                this.worldObj.removeTileEntity(p_177436_1_);
            }
        }
        if (var11.getBlockByExtId(var3, var4 & 0xF, var5) != var9) {
            return null;
        }
        if (var12) {
            this.generateSkylightMap();
        }
        else {
            final int var13 = var9.getLightOpacity();
            final int var14 = var10.getLightOpacity();
            if (var13 > 0) {
                if (var4 >= var7) {
                    this.relightBlock(var3, var4 + 1, var5);
                }
            }
            else if (var4 == var7 - 1) {
                this.relightBlock(var3, var4, var5);
            }
            if (var13 != var14 && (var13 < var14 || this.getLightFor(EnumSkyBlock.SKY, p_177436_1_) > 0 || this.getLightFor(EnumSkyBlock.BLOCK, p_177436_1_) > 0)) {
                this.propagateSkylightOcclusion(var3, var5);
            }
        }
        if (var10 instanceof ITileEntityProvider) {
            final TileEntity var15 = this.func_177424_a(p_177436_1_, EnumCreateEntityType.CHECK);
            if (var15 != null) {
                var15.updateContainingBlockInfo();
            }
        }
        if (!this.worldObj.isRemote && var10 != var9) {
            var9.onBlockAdded(this.worldObj, p_177436_1_, p_177436_2_);
        }
        if (var9 instanceof ITileEntityProvider) {
            TileEntity var15 = this.func_177424_a(p_177436_1_, EnumCreateEntityType.CHECK);
            if (var15 == null) {
                var15 = ((ITileEntityProvider)var9).createNewTileEntity(this.worldObj, var9.getMetaFromState(p_177436_2_));
                this.worldObj.setTileEntity(p_177436_1_, var15);
            }
            if (var15 != null) {
                var15.updateContainingBlockInfo();
            }
        }
        this.isModified = true;
        return var8;
    }
    
    public int getLightFor(final EnumSkyBlock p_177413_1_, final BlockPos p_177413_2_) {
        final int var3 = p_177413_2_.getX() & 0xF;
        final int var4 = p_177413_2_.getY();
        final int var5 = p_177413_2_.getZ() & 0xF;
        final ExtendedBlockStorage var6 = this.storageArrays[var4 >> 4];
        return (var6 == null) ? (this.canSeeSky(p_177413_2_) ? p_177413_1_.defaultLightValue : 0) : ((p_177413_1_ == EnumSkyBlock.SKY) ? (this.worldObj.provider.getHasNoSky() ? 0 : var6.getExtSkylightValue(var3, var4 & 0xF, var5)) : ((p_177413_1_ == EnumSkyBlock.BLOCK) ? var6.getExtBlocklightValue(var3, var4 & 0xF, var5) : p_177413_1_.defaultLightValue));
    }
    
    public void setLightFor(final EnumSkyBlock p_177431_1_, final BlockPos p_177431_2_, final int p_177431_3_) {
        final int var4 = p_177431_2_.getX() & 0xF;
        final int var5 = p_177431_2_.getY();
        final int var6 = p_177431_2_.getZ() & 0xF;
        ExtendedBlockStorage var7 = this.storageArrays[var5 >> 4];
        if (var7 == null) {
            final ExtendedBlockStorage[] storageArrays = this.storageArrays;
            final int n = var5 >> 4;
            final ExtendedBlockStorage extendedBlockStorage = new ExtendedBlockStorage(var5 >> 4 << 4, !this.worldObj.provider.getHasNoSky());
            storageArrays[n] = extendedBlockStorage;
            var7 = extendedBlockStorage;
            this.generateSkylightMap();
        }
        this.isModified = true;
        if (p_177431_1_ == EnumSkyBlock.SKY) {
            if (!this.worldObj.provider.getHasNoSky()) {
                var7.setExtSkylightValue(var4, var5 & 0xF, var6, p_177431_3_);
            }
        }
        else if (p_177431_1_ == EnumSkyBlock.BLOCK) {
            var7.setExtBlocklightValue(var4, var5 & 0xF, var6, p_177431_3_);
        }
    }
    
    public int setLight(final BlockPos p_177443_1_, final int p_177443_2_) {
        final int var3 = p_177443_1_.getX() & 0xF;
        final int var4 = p_177443_1_.getY();
        final int var5 = p_177443_1_.getZ() & 0xF;
        final ExtendedBlockStorage var6 = this.storageArrays[var4 >> 4];
        if (var6 == null) {
            return (!this.worldObj.provider.getHasNoSky() && p_177443_2_ < EnumSkyBlock.SKY.defaultLightValue) ? (EnumSkyBlock.SKY.defaultLightValue - p_177443_2_) : 0;
        }
        int var7 = this.worldObj.provider.getHasNoSky() ? 0 : var6.getExtSkylightValue(var3, var4 & 0xF, var5);
        var7 -= p_177443_2_;
        final int var8 = var6.getExtBlocklightValue(var3, var4 & 0xF, var5);
        if (var8 > var7) {
            var7 = var8;
        }
        return var7;
    }
    
    public void addEntity(final Entity entityIn) {
        this.hasEntities = true;
        final int var2 = MathHelper.floor_double(entityIn.posX / 16.0);
        final int var3 = MathHelper.floor_double(entityIn.posZ / 16.0);
        if (var2 != this.xPosition || var3 != this.zPosition) {
            Chunk.logger.warn("Wrong location! (" + var2 + ", " + var3 + ") should be (" + this.xPosition + ", " + this.zPosition + "), " + entityIn, new Object[] { entityIn });
            entityIn.setDead();
        }
        int var4 = MathHelper.floor_double(entityIn.posY / 16.0);
        if (var4 < 0) {
            var4 = 0;
        }
        if (var4 >= this.entityLists.length) {
            var4 = this.entityLists.length - 1;
        }
        entityIn.addedToChunk = true;
        entityIn.chunkCoordX = this.xPosition;
        entityIn.chunkCoordY = var4;
        entityIn.chunkCoordZ = this.zPosition;
        this.entityLists[var4].add(entityIn);
    }
    
    public void removeEntity(final Entity p_76622_1_) {
        this.removeEntityAtIndex(p_76622_1_, p_76622_1_.chunkCoordY);
    }
    
    public void removeEntityAtIndex(final Entity p_76608_1_, int p_76608_2_) {
        if (p_76608_2_ < 0) {
            p_76608_2_ = 0;
        }
        if (p_76608_2_ >= this.entityLists.length) {
            p_76608_2_ = this.entityLists.length - 1;
        }
        this.entityLists[p_76608_2_].remove(p_76608_1_);
    }
    
    public boolean canSeeSky(final BlockPos pos) {
        final int var2 = pos.getX() & 0xF;
        final int var3 = pos.getY();
        final int var4 = pos.getZ() & 0xF;
        return var3 >= this.heightMap[var4 << 4 | var2];
    }
    
    private TileEntity createNewTileEntity(final BlockPos pos) {
        final Block var2 = this.getBlock(pos);
        return var2.hasTileEntity() ? ((ITileEntityProvider)var2).createNewTileEntity(this.worldObj, this.getBlockMetadata(pos)) : null;
    }
    
    public TileEntity func_177424_a(final BlockPos p_177424_1_, final EnumCreateEntityType p_177424_2_) {
        TileEntity var3 = this.chunkTileEntityMap.get(p_177424_1_);
        if (var3 == null) {
            if (p_177424_2_ == EnumCreateEntityType.IMMEDIATE) {
                var3 = this.createNewTileEntity(p_177424_1_);
                this.worldObj.setTileEntity(p_177424_1_, var3);
            }
            else if (p_177424_2_ == EnumCreateEntityType.QUEUED) {
                this.field_177447_w.add(p_177424_1_);
            }
        }
        else if (var3.isInvalid()) {
            this.chunkTileEntityMap.remove(p_177424_1_);
            return null;
        }
        return var3;
    }
    
    public void addTileEntity(final TileEntity tileEntityIn) {
        this.addTileEntity(tileEntityIn.getPos(), tileEntityIn);
        if (this.isChunkLoaded) {
            this.worldObj.addTileEntity(tileEntityIn);
        }
    }
    
    public void addTileEntity(final BlockPos pos, final TileEntity tileEntityIn) {
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
    
    public void removeTileEntity(final BlockPos pos) {
        if (this.isChunkLoaded) {
            final TileEntity var2 = this.chunkTileEntityMap.remove(pos);
            if (var2 != null) {
                var2.invalidate();
            }
        }
    }
    
    public void onChunkLoad() {
        this.isChunkLoaded = true;
        this.worldObj.addTileEntities(this.chunkTileEntityMap.values());
        for (int var1 = 0; var1 < this.entityLists.length; ++var1) {
            for (final Entity var3 : this.entityLists[var1]) {
                var3.onChunkLoad();
            }
            this.worldObj.loadEntities(this.entityLists[var1]);
        }
    }
    
    public void onChunkUnload() {
        this.isChunkLoaded = false;
        for (final TileEntity var2 : this.chunkTileEntityMap.values()) {
            this.worldObj.markTileEntityForRemoval(var2);
        }
        for (int var3 = 0; var3 < this.entityLists.length; ++var3) {
            this.worldObj.unloadEntities(this.entityLists[var3]);
        }
    }
    
    public void setChunkModified() {
        this.isModified = true;
    }
    
    public void func_177414_a(final Entity p_177414_1_, final AxisAlignedBB p_177414_2_, final List p_177414_3_, final Predicate p_177414_4_) {
        int var5 = MathHelper.floor_double((p_177414_2_.minY - 2.0) / 16.0);
        int var6 = MathHelper.floor_double((p_177414_2_.maxY + 2.0) / 16.0);
        var5 = MathHelper.clamp_int(var5, 0, this.entityLists.length - 1);
        var6 = MathHelper.clamp_int(var6, 0, this.entityLists.length - 1);
        for (int var7 = var5; var7 <= var6; ++var7) {
            for (Entity var9 : this.entityLists[var7]) {
                if (var9 != p_177414_1_ && var9.getEntityBoundingBox().intersectsWith(p_177414_2_) && (p_177414_4_ == null || p_177414_4_.apply((Object)var9))) {
                    p_177414_3_.add(var9);
                    final Entity[] var10 = var9.getParts();
                    if (var10 == null) {
                        continue;
                    }
                    for (int var11 = 0; var11 < var10.length; ++var11) {
                        var9 = var10[var11];
                        if (var9 != p_177414_1_ && var9.getEntityBoundingBox().intersectsWith(p_177414_2_) && (p_177414_4_ == null || p_177414_4_.apply((Object)var9))) {
                            p_177414_3_.add(var9);
                        }
                    }
                }
            }
        }
    }
    
    public void func_177430_a(final Class p_177430_1_, final AxisAlignedBB p_177430_2_, final List p_177430_3_, final Predicate p_177430_4_) {
        int var5 = MathHelper.floor_double((p_177430_2_.minY - 2.0) / 16.0);
        int var6 = MathHelper.floor_double((p_177430_2_.maxY + 2.0) / 16.0);
        var5 = MathHelper.clamp_int(var5, 0, this.entityLists.length - 1);
        var6 = MathHelper.clamp_int(var6, 0, this.entityLists.length - 1);
        for (int var7 = var5; var7 <= var6; ++var7) {
            for (final Entity var9 : this.entityLists[var7].func_180215_b(p_177430_1_)) {
                if (var9.getEntityBoundingBox().intersectsWith(p_177430_2_) && (p_177430_4_ == null || p_177430_4_.apply((Object)var9))) {
                    p_177430_3_.add(var9);
                }
            }
        }
    }
    
    public boolean needsSaving(final boolean p_76601_1_) {
        if (p_76601_1_) {
            if ((this.hasEntities && this.worldObj.getTotalWorldTime() != this.lastSaveTime) || this.isModified) {
                return true;
            }
        }
        else if (this.hasEntities && this.worldObj.getTotalWorldTime() >= this.lastSaveTime + 600L) {
            return true;
        }
        return this.isModified;
    }
    
    public Random getRandomWithSeed(final long seed) {
        return new Random(this.worldObj.getSeed() + this.xPosition * this.xPosition * 4987142 + this.xPosition * 5947611 + this.zPosition * this.zPosition * 4392871L + this.zPosition * 389711 ^ seed);
    }
    
    public boolean isEmpty() {
        return false;
    }
    
    public void populateChunk(final IChunkProvider p_76624_1_, final IChunkProvider p_76624_2_, final int p_76624_3_, final int p_76624_4_) {
        final boolean var5 = p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ - 1);
        final boolean var6 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_);
        final boolean var7 = p_76624_1_.chunkExists(p_76624_3_, p_76624_4_ + 1);
        final boolean var8 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_);
        final boolean var9 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ - 1);
        final boolean var10 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ + 1);
        final boolean var11 = p_76624_1_.chunkExists(p_76624_3_ - 1, p_76624_4_ + 1);
        final boolean var12 = p_76624_1_.chunkExists(p_76624_3_ + 1, p_76624_4_ - 1);
        if (var6 && var7 && var10) {
            if (!this.isTerrainPopulated) {
                p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_);
            }
            else {
                p_76624_1_.func_177460_a(p_76624_2_, this, p_76624_3_, p_76624_4_);
            }
        }
        if (var8 && var7 && var11) {
            final Chunk var13 = p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_);
            if (!var13.isTerrainPopulated) {
                p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_);
            }
            else {
                p_76624_1_.func_177460_a(p_76624_2_, var13, p_76624_3_ - 1, p_76624_4_);
            }
        }
        if (var5 && var6 && var12) {
            final Chunk var13 = p_76624_1_.provideChunk(p_76624_3_, p_76624_4_ - 1);
            if (!var13.isTerrainPopulated) {
                p_76624_1_.populate(p_76624_2_, p_76624_3_, p_76624_4_ - 1);
            }
            else {
                p_76624_1_.func_177460_a(p_76624_2_, var13, p_76624_3_, p_76624_4_ - 1);
            }
        }
        if (var9 && var5 && var8) {
            final Chunk var13 = p_76624_1_.provideChunk(p_76624_3_ - 1, p_76624_4_ - 1);
            if (!var13.isTerrainPopulated) {
                p_76624_1_.populate(p_76624_2_, p_76624_3_ - 1, p_76624_4_ - 1);
            }
            else {
                p_76624_1_.func_177460_a(p_76624_2_, var13, p_76624_3_ - 1, p_76624_4_ - 1);
            }
        }
    }
    
    public BlockPos func_177440_h(final BlockPos p_177440_1_) {
        final int var2 = p_177440_1_.getX() & 0xF;
        final int var3 = p_177440_1_.getZ() & 0xF;
        final int var4 = var2 | var3 << 4;
        BlockPos var5 = new BlockPos(p_177440_1_.getX(), this.precipitationHeightMap[var4], p_177440_1_.getZ());
        if (var5.getY() == -999) {
            final int var6 = this.getTopFilledSegment() + 15;
            var5 = new BlockPos(p_177440_1_.getX(), var6, p_177440_1_.getZ());
            int var7 = -1;
            while (var5.getY() > 0 && var7 == -1) {
                final Block var8 = this.getBlock(var5);
                final Material var9 = var8.getMaterial();
                if (!var9.blocksMovement() && !var9.isLiquid()) {
                    var5 = var5.offsetDown();
                }
                else {
                    var7 = var5.getY() + 1;
                }
            }
            this.precipitationHeightMap[var4] = var7;
        }
        return new BlockPos(p_177440_1_.getX(), this.precipitationHeightMap[var4], p_177440_1_.getZ());
    }
    
    public void func_150804_b(final boolean p_150804_1_) {
        if (this.isGapLightingUpdated && !this.worldObj.provider.getHasNoSky() && !p_150804_1_) {
            this.recheckGaps(this.worldObj.isRemote);
        }
        this.field_150815_m = true;
        if (!this.isLightPopulated && this.isTerrainPopulated) {
            this.func_150809_p();
        }
        while (!this.field_177447_w.isEmpty()) {
            final BlockPos var2 = this.field_177447_w.poll();
            if (this.func_177424_a(var2, EnumCreateEntityType.CHECK) == null && this.getBlock(var2).hasTileEntity()) {
                final TileEntity var3 = this.createNewTileEntity(var2);
                this.worldObj.setTileEntity(var2, var3);
                this.worldObj.markBlockRangeForRenderUpdate(var2, var2);
            }
        }
    }
    
    public boolean isPopulated() {
        return this.field_150815_m && this.isTerrainPopulated && this.isLightPopulated;
    }
    
    public ChunkCoordIntPair getChunkCoordIntPair() {
        return new ChunkCoordIntPair(this.xPosition, this.zPosition);
    }
    
    public boolean getAreLevelsEmpty(int p_76606_1_, int p_76606_2_) {
        if (p_76606_1_ < 0) {
            p_76606_1_ = 0;
        }
        if (p_76606_2_ >= 256) {
            p_76606_2_ = 255;
        }
        for (int var3 = p_76606_1_; var3 <= p_76606_2_; var3 += 16) {
            final ExtendedBlockStorage var4 = this.storageArrays[var3 >> 4];
            if (var4 != null && !var4.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    public void setStorageArrays(final ExtendedBlockStorage[] newStorageArrays) {
        if (this.storageArrays.length != newStorageArrays.length) {
            Chunk.logger.warn("Could not set level chunk sections, array length is " + newStorageArrays.length + " instead of " + this.storageArrays.length);
        }
        else {
            for (int var2 = 0; var2 < this.storageArrays.length; ++var2) {
                this.storageArrays[var2] = newStorageArrays[var2];
            }
        }
    }
    
    public void func_177439_a(final byte[] p_177439_1_, final int p_177439_2_, final boolean p_177439_3_) {
        int var4 = 0;
        final boolean var5 = !this.worldObj.provider.getHasNoSky();
        for (int var6 = 0; var6 < this.storageArrays.length; ++var6) {
            if ((p_177439_2_ & 1 << var6) != 0x0) {
                if (this.storageArrays[var6] == null) {
                    this.storageArrays[var6] = new ExtendedBlockStorage(var6 << 4, var5);
                }
                final char[] var7 = this.storageArrays[var6].getData();
                for (int var8 = 0; var8 < var7.length; ++var8) {
                    var7[var8] = (char)((p_177439_1_[var4 + 1] & 0xFF) << 8 | (p_177439_1_[var4] & 0xFF));
                    var4 += 2;
                }
            }
            else if (p_177439_3_ && this.storageArrays[var6] != null) {
                this.storageArrays[var6] = null;
            }
        }
        for (int var6 = 0; var6 < this.storageArrays.length; ++var6) {
            if ((p_177439_2_ & 1 << var6) != 0x0 && this.storageArrays[var6] != null) {
                final NibbleArray var9 = this.storageArrays[var6].getBlocklightArray();
                System.arraycopy(p_177439_1_, var4, var9.getData(), 0, var9.getData().length);
                var4 += var9.getData().length;
            }
        }
        if (var5) {
            for (int var6 = 0; var6 < this.storageArrays.length; ++var6) {
                if ((p_177439_2_ & 1 << var6) != 0x0 && this.storageArrays[var6] != null) {
                    final NibbleArray var9 = this.storageArrays[var6].getSkylightArray();
                    System.arraycopy(p_177439_1_, var4, var9.getData(), 0, var9.getData().length);
                    var4 += var9.getData().length;
                }
            }
        }
        if (p_177439_3_) {
            System.arraycopy(p_177439_1_, var4, this.blockBiomeArray, 0, this.blockBiomeArray.length);
            final int n = var4 + this.blockBiomeArray.length;
        }
        for (int var6 = 0; var6 < this.storageArrays.length; ++var6) {
            if (this.storageArrays[var6] != null && (p_177439_2_ & 1 << var6) != 0x0) {
                this.storageArrays[var6].removeInvalidBlocks();
            }
        }
        this.isLightPopulated = true;
        this.isTerrainPopulated = true;
        this.generateHeightMap();
        for (final TileEntity var11 : this.chunkTileEntityMap.values()) {
            var11.updateContainingBlockInfo();
        }
    }
    
    public BiomeGenBase getBiome(final BlockPos pos, final WorldChunkManager chunkManager) {
        final int var3 = pos.getX() & 0xF;
        final int var4 = pos.getZ() & 0xF;
        int var5 = this.blockBiomeArray[var4 << 4 | var3] & 0xFF;
        if (var5 == 255) {
            final BiomeGenBase var6 = chunkManager.func_180300_a(pos, BiomeGenBase.plains);
            var5 = var6.biomeID;
            this.blockBiomeArray[var4 << 4 | var3] = (byte)(var5 & 0xFF);
        }
        final BiomeGenBase var6 = BiomeGenBase.getBiome(var5);
        return (var6 == null) ? BiomeGenBase.plains : var6;
    }
    
    public byte[] getBiomeArray() {
        return this.blockBiomeArray;
    }
    
    public void setBiomeArray(final byte[] biomeArray) {
        if (this.blockBiomeArray.length != biomeArray.length) {
            Chunk.logger.warn("Could not set level chunk biomes, array length is " + biomeArray.length + " instead of " + this.blockBiomeArray.length);
        }
        else {
            for (int var2 = 0; var2 < this.blockBiomeArray.length; ++var2) {
                this.blockBiomeArray[var2] = biomeArray[var2];
            }
        }
    }
    
    public void resetRelightChecks() {
        this.queuedLightChecks = 0;
    }
    
    public void enqueueRelightChecks() {
        final BlockPos var1 = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
        for (int var2 = 0; var2 < 8; ++var2) {
            if (this.queuedLightChecks >= 4096) {
                return;
            }
            final int var3 = this.queuedLightChecks % 16;
            final int var4 = this.queuedLightChecks / 16 % 16;
            final int var5 = this.queuedLightChecks / 256;
            ++this.queuedLightChecks;
            for (int var6 = 0; var6 < 16; ++var6) {
                final BlockPos var7 = var1.add(var4, (var3 << 4) + var6, var5);
                final boolean var8 = var6 == 0 || var6 == 15 || var4 == 0 || var4 == 15 || var5 == 0 || var5 == 15;
                if ((this.storageArrays[var3] == null && var8) || (this.storageArrays[var3] != null && this.storageArrays[var3].getBlockByExtId(var4, var6, var5).getMaterial() == Material.air)) {
                    for (final EnumFacing var12 : EnumFacing.values()) {
                        final BlockPos var13 = var7.offset(var12);
                        if (this.worldObj.getBlockState(var13).getBlock().getLightValue() > 0) {
                            this.worldObj.checkLight(var13);
                        }
                    }
                    this.worldObj.checkLight(var7);
                }
            }
        }
    }
    
    public void func_150809_p() {
        this.isTerrainPopulated = true;
        this.isLightPopulated = true;
        final BlockPos var1 = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
        if (!this.worldObj.provider.getHasNoSky()) {
            if (this.worldObj.isAreaLoaded(var1.add(-1, 0, -1), var1.add(16, 63, 16))) {
            Label_0116:
                for (int var2 = 0; var2 < 16; ++var2) {
                    for (int var3 = 0; var3 < 16; ++var3) {
                        if (!this.func_150811_f(var2, var3)) {
                            this.isLightPopulated = false;
                            break Label_0116;
                        }
                    }
                }
                if (this.isLightPopulated) {
                    for (final EnumFacing var5 : EnumFacing.Plane.HORIZONTAL) {
                        final int var6 = (var5.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE) ? 16 : 1;
                        this.worldObj.getChunkFromBlockCoords(var1.offset(var5, var6)).func_180700_a(var5.getOpposite());
                    }
                    this.func_177441_y();
                }
            }
            else {
                this.isLightPopulated = false;
            }
        }
    }
    
    private void func_177441_y() {
        for (int var1 = 0; var1 < this.updateSkylightColumns.length; ++var1) {
            this.updateSkylightColumns[var1] = true;
        }
        this.recheckGaps(false);
    }
    
    private void func_180700_a(final EnumFacing p_180700_1_) {
        if (this.isTerrainPopulated) {
            if (p_180700_1_ == EnumFacing.EAST) {
                for (int var2 = 0; var2 < 16; ++var2) {
                    this.func_150811_f(15, var2);
                }
            }
            else if (p_180700_1_ == EnumFacing.WEST) {
                for (int var2 = 0; var2 < 16; ++var2) {
                    this.func_150811_f(0, var2);
                }
            }
            else if (p_180700_1_ == EnumFacing.SOUTH) {
                for (int var2 = 0; var2 < 16; ++var2) {
                    this.func_150811_f(var2, 15);
                }
            }
            else if (p_180700_1_ == EnumFacing.NORTH) {
                for (int var2 = 0; var2 < 16; ++var2) {
                    this.func_150811_f(var2, 0);
                }
            }
        }
    }
    
    private boolean func_150811_f(final int p_150811_1_, final int p_150811_2_) {
        final BlockPos var3 = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
        final int var4 = this.getTopFilledSegment();
        boolean var5 = false;
        boolean var6;
        int var7;
        BlockPos var8;
        int var9;
        for (var6 = false, var7 = var4 + 16 - 1; var7 > 63 || (var7 > 0 && !var6); --var7) {
            var8 = var3.add(p_150811_1_, var7, p_150811_2_);
            var9 = this.getBlockLightOpacity(var8);
            if (var9 == 255 && var7 < 63) {
                var6 = true;
            }
            if (!var5 && var9 > 0) {
                var5 = true;
            }
            else if (var5 && var9 == 0 && !this.worldObj.checkLight(var8)) {
                return false;
            }
        }
        while (var7 > 0) {
            var8 = var3.add(p_150811_1_, var7, p_150811_2_);
            if (this.getBlock(var8).getLightValue() > 0) {
                this.worldObj.checkLight(var8);
            }
            --var7;
        }
        return true;
    }
    
    public boolean isLoaded() {
        return this.isChunkLoaded;
    }
    
    public void func_177417_c(final boolean p_177417_1_) {
        this.isChunkLoaded = p_177417_1_;
    }
    
    public World getWorld() {
        return this.worldObj;
    }
    
    public int[] getHeightMap() {
        return this.heightMap;
    }
    
    public void setHeightMap(final int[] newHeightMap) {
        if (this.heightMap.length != newHeightMap.length) {
            Chunk.logger.warn("Could not set level chunk heightmap, array length is " + newHeightMap.length + " instead of " + this.heightMap.length);
        }
        else {
            for (int var2 = 0; var2 < this.heightMap.length; ++var2) {
                this.heightMap[var2] = newHeightMap[var2];
            }
        }
    }
    
    public Map getTileEntityMap() {
        return this.chunkTileEntityMap;
    }
    
    public ClassInheratanceMultiMap[] getEntityLists() {
        return this.entityLists;
    }
    
    public boolean isTerrainPopulated() {
        return this.isTerrainPopulated;
    }
    
    public void setTerrainPopulated(final boolean terrainPopulated) {
        this.isTerrainPopulated = terrainPopulated;
    }
    
    public boolean isLightPopulated() {
        return this.isLightPopulated;
    }
    
    public void setLightPopulated(final boolean lightPopulated) {
        this.isLightPopulated = lightPopulated;
    }
    
    public void setModified(final boolean modified) {
        this.isModified = modified;
    }
    
    public void setHasEntities(final boolean hasEntitiesIn) {
        this.hasEntities = hasEntitiesIn;
    }
    
    public void setLastSaveTime(final long saveTime) {
        this.lastSaveTime = saveTime;
    }
    
    public int getLowestHeight() {
        return this.heightMapMinimum;
    }
    
    public long getInhabitedTime() {
        return this.inhabitedTime;
    }
    
    public void setInhabitedTime(final long newInhabitedTime) {
        this.inhabitedTime = newInhabitedTime;
    }
    
    static {
        logger = LogManager.getLogger();
    }
    
    public enum EnumCreateEntityType
    {
        IMMEDIATE("IMMEDIATE", 0), 
        QUEUED("QUEUED", 1), 
        CHECK("CHECK", 2);
        
        private static final EnumCreateEntityType[] $VALUES;
        private static final String __OBFID = "CL_00002009";
        
        private EnumCreateEntityType(final String p_i45642_1_, final int p_i45642_2_) {
        }
        
        static {
            $VALUES = new EnumCreateEntityType[] { EnumCreateEntityType.IMMEDIATE, EnumCreateEntityType.QUEUED, EnumCreateEntityType.CHECK };
        }
    }
}
