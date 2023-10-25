/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Queues
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
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
    private int heightMapMinimum;
    private long inhabitedTime;
    private ConcurrentLinkedQueue<BlockPos> tileEntityPosQueue;
    private final boolean[] updateSkylightColumns;
    private final int[] precipitationHeightMap;
    private final ExtendedBlockStorage[] storageArrays = new ExtendedBlockStorage[16];
    public final int zPosition;
    private long lastSaveTime;
    private boolean hasEntities;
    public final int xPosition;
    private boolean isLightPopulated;
    private final byte[] blockBiomeArray = new byte[256];
    private int queuedLightChecks = 4096;
    private boolean isModified;
    private static final Logger logger = LogManager.getLogger();
    private boolean isChunkLoaded;
    private final int[] heightMap;
    private boolean field_150815_m;
    private boolean isTerrainPopulated;
    private final ClassInheritanceMultiMap<Entity>[] entityLists;
    private final World worldObj;
    private boolean isGapLightingUpdated;
    private final Map<BlockPos, TileEntity> chunkTileEntityMap;

    public Random getRandomWithSeed(long l) {
        return new Random(this.worldObj.getSeed() + (long)(this.xPosition * this.xPosition * 4987142) + (long)(this.xPosition * 5947611) + (long)(this.zPosition * this.zPosition) * 4392871L + (long)(this.zPosition * 389711) ^ l);
    }

    public TileEntity getTileEntity(BlockPos blockPos, EnumCreateEntityType enumCreateEntityType) {
        TileEntity tileEntity = this.chunkTileEntityMap.get(blockPos);
        if (tileEntity == null) {
            if (enumCreateEntityType == EnumCreateEntityType.IMMEDIATE) {
                tileEntity = this.createNewTileEntity(blockPos);
                this.worldObj.setTileEntity(blockPos, tileEntity);
            } else if (enumCreateEntityType == EnumCreateEntityType.QUEUED) {
                this.tileEntityPosQueue.add(blockPos);
            }
        } else if (tileEntity.isInvalid()) {
            this.chunkTileEntityMap.remove(blockPos);
            return null;
        }
        return tileEntity;
    }

    public ClassInheritanceMultiMap<Entity>[] getEntityLists() {
        return this.entityLists;
    }

    public int getBlockMetadata(BlockPos blockPos) {
        return this.getBlockMetadata(blockPos.getX() & 0xF, blockPos.getY(), blockPos.getZ() & 0xF);
    }

    public void setLightFor(EnumSkyBlock enumSkyBlock, BlockPos blockPos, int n) {
        int n2 = blockPos.getX() & 0xF;
        int n3 = blockPos.getY();
        int n4 = blockPos.getZ() & 0xF;
        ExtendedBlockStorage extendedBlockStorage = this.storageArrays[n3 >> 4];
        if (extendedBlockStorage == null) {
            ExtendedBlockStorage extendedBlockStorage2 = new ExtendedBlockStorage(n3 >> 4 << 4, !this.worldObj.provider.getHasNoSky());
            this.storageArrays[n3 >> 4] = extendedBlockStorage2;
            extendedBlockStorage = extendedBlockStorage2;
            this.generateSkylightMap();
        }
        this.isModified = true;
        if (enumSkyBlock == EnumSkyBlock.SKY) {
            if (!this.worldObj.provider.getHasNoSky()) {
                extendedBlockStorage.setExtSkylightValue(n2, n3 & 0xF, n4, n);
            }
        } else if (enumSkyBlock == EnumSkyBlock.BLOCK) {
            extendedBlockStorage.setExtBlocklightValue(n2, n3 & 0xF, n4, n);
        }
    }

    public boolean isLoaded() {
        return this.isChunkLoaded;
    }

    private void propagateSkylightOcclusion(int n, int n2) {
        this.updateSkylightColumns[n + n2 * 16] = true;
        this.isGapLightingUpdated = true;
    }

    public void setTerrainPopulated(boolean bl) {
        this.isTerrainPopulated = bl;
    }

    public void removeEntity(Entity entity) {
        this.removeEntityAtIndex(entity, entity.chunkCoordY);
    }

    public boolean isAtLocation(int n, int n2) {
        return n == this.xPosition && n2 == this.zPosition;
    }

    public void removeEntityAtIndex(Entity entity, int n) {
        if (n < 0) {
            n = 0;
        }
        if (n >= this.entityLists.length) {
            n = this.entityLists.length - 1;
        }
        this.entityLists[n].remove(entity);
    }

    public void setChunkModified() {
        this.isModified = true;
    }

    public boolean isTerrainPopulated() {
        return this.isTerrainPopulated;
    }

    public Map<BlockPos, TileEntity> getTileEntityMap() {
        return this.chunkTileEntityMap;
    }

    private void updateSkylightNeighborHeight(int n, int n2, int n3, int n4) {
        if (n4 > n3 && this.worldObj.isAreaLoaded(new BlockPos(n, 0, n2), 16)) {
            int n5 = n3;
            while (n5 < n4) {
                this.worldObj.checkLightFor(EnumSkyBlock.SKY, new BlockPos(n, n5, n2));
                ++n5;
            }
            this.isModified = true;
        }
    }

    public void setLightPopulated(boolean bl) {
        this.isLightPopulated = bl;
    }

    public byte[] getBiomeArray() {
        return this.blockBiomeArray;
    }

    public boolean getAreLevelsEmpty(int n, int n2) {
        if (n < 0) {
            n = 0;
        }
        if (n2 >= 256) {
            n2 = 255;
        }
        int n3 = n;
        while (n3 <= n2) {
            ExtendedBlockStorage extendedBlockStorage = this.storageArrays[n3 >> 4];
            if (extendedBlockStorage != null && !extendedBlockStorage.isEmpty()) {
                return false;
            }
            n3 += 16;
        }
        return true;
    }

    public void setLastSaveTime(long l) {
        this.lastSaveTime = l;
    }

    public int getTopFilledSegment() {
        int n = this.storageArrays.length - 1;
        while (n >= 0) {
            if (this.storageArrays[n] != null) {
                return this.storageArrays[n].getYLocation();
            }
            --n;
        }
        return 0;
    }

    public void addTileEntity(BlockPos blockPos, TileEntity tileEntity) {
        tileEntity.setWorldObj(this.worldObj);
        tileEntity.setPos(blockPos);
        if (this.getBlock(blockPos) instanceof ITileEntityProvider) {
            if (this.chunkTileEntityMap.containsKey(blockPos)) {
                this.chunkTileEntityMap.get(blockPos).invalidate();
            }
            tileEntity.validate();
            this.chunkTileEntityMap.put(blockPos, tileEntity);
        }
    }

    public void onChunkUnload() {
        this.isChunkLoaded = false;
        for (TileEntity tileEntity : this.chunkTileEntityMap.values()) {
            this.worldObj.markTileEntityForRemoval(tileEntity);
        }
        int n = 0;
        while (n < this.entityLists.length) {
            this.worldObj.unloadEntities(this.entityLists[n]);
            ++n;
        }
    }

    public ChunkCoordIntPair getChunkCoordIntPair() {
        return new ChunkCoordIntPair(this.xPosition, this.zPosition);
    }

    public <T extends Entity> void getEntitiesOfTypeWithinAAAB(Class<? extends T> clazz, AxisAlignedBB axisAlignedBB, List<T> list, Predicate<? super T> predicate) {
        int n = MathHelper.floor_double((axisAlignedBB.minY - 2.0) / 16.0);
        int n2 = MathHelper.floor_double((axisAlignedBB.maxY + 2.0) / 16.0);
        n = MathHelper.clamp_int(n, 0, this.entityLists.length - 1);
        n2 = MathHelper.clamp_int(n2, 0, this.entityLists.length - 1);
        int n3 = n;
        while (n3 <= n2) {
            for (Entity entity : this.entityLists[n3].getByClass(clazz)) {
                if (!entity.getEntityBoundingBox().intersectsWith(axisAlignedBB) || predicate != null && !predicate.apply((Object)entity)) continue;
                list.add(entity);
            }
            ++n3;
        }
    }

    private boolean func_150811_f(int n, int n2) {
        int n3 = this.getTopFilledSegment();
        boolean bl = false;
        boolean bl2 = false;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos((this.xPosition << 4) + n, 0, (this.zPosition << 4) + n2);
        int n4 = n3 + 16 - 1;
        while (n4 > this.worldObj.func_181545_F() || n4 > 0 && !bl2) {
            mutableBlockPos.func_181079_c(mutableBlockPos.getX(), n4, mutableBlockPos.getZ());
            int n5 = this.getBlockLightOpacity(mutableBlockPos);
            if (n5 == 255 && mutableBlockPos.getY() < this.worldObj.func_181545_F()) {
                bl2 = true;
            }
            if (!bl && n5 > 0) {
                bl = true;
            } else if (bl && n5 == 0 && !this.worldObj.checkLight(mutableBlockPos)) {
                return false;
            }
            --n4;
        }
        n4 = mutableBlockPos.getY();
        while (n4 > 0) {
            mutableBlockPos.func_181079_c(mutableBlockPos.getX(), n4, mutableBlockPos.getZ());
            if (this.getBlock(mutableBlockPos).getLightValue() > 0) {
                this.worldObj.checkLight(mutableBlockPos);
            }
            --n4;
        }
        return true;
    }

    private void checkSkylightNeighborHeight(int n, int n2, int n3) {
        int n4 = this.worldObj.getHeight(new BlockPos(n, 0, n2)).getY();
        if (n4 > n3) {
            this.updateSkylightNeighborHeight(n, n2, n3, n4 + 1);
        } else if (n4 < n3) {
            this.updateSkylightNeighborHeight(n, n2, n4, n3 + 1);
        }
    }

    private void relightBlock(int n, int n2, int n3) {
        int n4;
        int n5 = n4 = this.heightMap[n3 << 4 | n] & 0xFF;
        if (n2 > n4) {
            n5 = n2;
        }
        while (n5 > 0 && this.getBlockLightOpacity(n, n5 - 1, n3) == 0) {
            --n5;
        }
        if (n5 != n4) {
            int n6;
            this.worldObj.markBlocksDirtyVertical(n + this.xPosition * 16, n3 + this.zPosition * 16, n5, n4);
            this.heightMap[n3 << 4 | n] = n5;
            int n7 = this.xPosition * 16 + n;
            int n8 = this.zPosition * 16 + n3;
            if (!this.worldObj.provider.getHasNoSky()) {
                ExtendedBlockStorage extendedBlockStorage;
                if (n5 < n4) {
                    n6 = n5;
                    while (n6 < n4) {
                        extendedBlockStorage = this.storageArrays[n6 >> 4];
                        if (extendedBlockStorage != null) {
                            extendedBlockStorage.setExtSkylightValue(n, n6 & 0xF, n3, 15);
                            this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + n, n6, (this.zPosition << 4) + n3));
                        }
                        ++n6;
                    }
                } else {
                    n6 = n4;
                    while (n6 < n5) {
                        extendedBlockStorage = this.storageArrays[n6 >> 4];
                        if (extendedBlockStorage != null) {
                            extendedBlockStorage.setExtSkylightValue(n, n6 & 0xF, n3, 0);
                            this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + n, n6, (this.zPosition << 4) + n3));
                        }
                        ++n6;
                    }
                }
                n6 = 15;
                while (n5 > 0 && n6 > 0) {
                    ExtendedBlockStorage extendedBlockStorage2;
                    int n9;
                    if ((n9 = this.getBlockLightOpacity(n, --n5, n3)) == 0) {
                        n9 = 1;
                    }
                    if ((n6 -= n9) < 0) {
                        n6 = 0;
                    }
                    if ((extendedBlockStorage2 = this.storageArrays[n5 >> 4]) == null) continue;
                    extendedBlockStorage2.setExtSkylightValue(n, n5 & 0xF, n3, n6);
                }
            }
            n6 = this.heightMap[n3 << 4 | n];
            int n10 = n4;
            int n11 = n6;
            if (n6 < n4) {
                n10 = n6;
                n11 = n4;
            }
            if (n6 < this.heightMapMinimum) {
                this.heightMapMinimum = n6;
            }
            if (!this.worldObj.provider.getHasNoSky()) {
                for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
                    this.updateSkylightNeighborHeight(n7 + enumFacing.getFrontOffsetX(), n8 + enumFacing.getFrontOffsetZ(), n10, n11);
                }
                this.updateSkylightNeighborHeight(n7, n8, n10, n11);
            }
            this.isModified = true;
        }
    }

    public Chunk(World world, ChunkPrimer chunkPrimer, int n, int n2) {
        this(world, n, n2);
        int n3 = 256;
        boolean bl = !world.provider.getHasNoSky();
        int n4 = 0;
        while (n4 < 16) {
            int n5 = 0;
            while (n5 < 16) {
                int n6 = 0;
                while (n6 < n3) {
                    int n7 = n4 * n3 * 16 | n5 * n3 | n6;
                    IBlockState iBlockState = chunkPrimer.getBlockState(n7);
                    if (iBlockState.getBlock().getMaterial() != Material.air) {
                        int n8 = n6 >> 4;
                        if (this.storageArrays[n8] == null) {
                            this.storageArrays[n8] = new ExtendedBlockStorage(n8 << 4, bl);
                        }
                        this.storageArrays[n8].set(n4, n6 & 0xF, n5, iBlockState);
                    }
                    ++n6;
                }
                ++n5;
            }
            ++n4;
        }
    }

    public void setHasEntities(boolean bl) {
        this.hasEntities = bl;
    }

    public void addEntity(Entity entity) {
        int n;
        this.hasEntities = true;
        int n2 = MathHelper.floor_double(entity.posX / 16.0);
        int n3 = MathHelper.floor_double(entity.posZ / 16.0);
        if (n2 != this.xPosition || n3 != this.zPosition) {
            logger.warn("Wrong location! (" + n2 + ", " + n3 + ") should be (" + this.xPosition + ", " + this.zPosition + "), " + entity, new Object[]{entity});
            entity.setDead();
        }
        if ((n = MathHelper.floor_double(entity.posY / 16.0)) < 0) {
            n = 0;
        }
        if (n >= this.entityLists.length) {
            n = this.entityLists.length - 1;
        }
        entity.addedToChunk = true;
        entity.chunkCoordX = this.xPosition;
        entity.chunkCoordY = n;
        entity.chunkCoordZ = this.zPosition;
        this.entityLists[n].add(entity);
    }

    public long getInhabitedTime() {
        return this.inhabitedTime;
    }

    public int getLightFor(EnumSkyBlock enumSkyBlock, BlockPos blockPos) {
        int n = blockPos.getX() & 0xF;
        int n2 = blockPos.getY();
        int n3 = blockPos.getZ() & 0xF;
        ExtendedBlockStorage extendedBlockStorage = this.storageArrays[n2 >> 4];
        return extendedBlockStorage == null ? (this.canSeeSky(blockPos) ? enumSkyBlock.defaultLightValue : 0) : (enumSkyBlock == EnumSkyBlock.SKY ? (this.worldObj.provider.getHasNoSky() ? 0 : extendedBlockStorage.getExtSkylightValue(n, n2 & 0xF, n3)) : (enumSkyBlock == EnumSkyBlock.BLOCK ? extendedBlockStorage.getExtBlocklightValue(n, n2 & 0xF, n3) : enumSkyBlock.defaultLightValue));
    }

    protected void generateHeightMap() {
        int n = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;
        int n2 = 0;
        while (n2 < 16) {
            int n3 = 0;
            while (n3 < 16) {
                this.precipitationHeightMap[n2 + (n3 << 4)] = -999;
                int n4 = n + 16;
                while (n4 > 0) {
                    Block block = this.getBlock0(n2, n4 - 1, n3);
                    if (block.getLightOpacity() != 0) {
                        this.heightMap[n3 << 4 | n2] = n4;
                        if (n4 >= this.heightMapMinimum) break;
                        this.heightMapMinimum = n4;
                        break;
                    }
                    --n4;
                }
                ++n3;
            }
            ++n2;
        }
        this.isModified = true;
    }

    public void setChunkLoaded(boolean bl) {
        this.isChunkLoaded = bl;
    }

    public boolean isEmpty() {
        return false;
    }

    public void addTileEntity(TileEntity tileEntity) {
        this.addTileEntity(tileEntity.getPos(), tileEntity);
        if (this.isChunkLoaded) {
            this.worldObj.addTileEntity(tileEntity);
        }
    }

    public void setBiomeArray(byte[] byArray) {
        if (this.blockBiomeArray.length != byArray.length) {
            logger.warn("Could not set level chunk biomes, array length is " + byArray.length + " instead of " + this.blockBiomeArray.length);
        } else {
            int n = 0;
            while (n < this.blockBiomeArray.length) {
                this.blockBiomeArray[n] = byArray[n];
                ++n;
            }
        }
    }

    public void getEntitiesWithinAABBForEntity(Entity entity, AxisAlignedBB axisAlignedBB, List<Entity> list, Predicate<? super Entity> predicate) {
        int n = MathHelper.floor_double((axisAlignedBB.minY - 2.0) / 16.0);
        int n2 = MathHelper.floor_double((axisAlignedBB.maxY + 2.0) / 16.0);
        n = MathHelper.clamp_int(n, 0, this.entityLists.length - 1);
        n2 = MathHelper.clamp_int(n2, 0, this.entityLists.length - 1);
        int n3 = n;
        while (n3 <= n2) {
            if (!this.entityLists[n3].isEmpty()) {
                for (Entity entity2 : this.entityLists[n3]) {
                    Entity[] entityArray;
                    if (!entity2.getEntityBoundingBox().intersectsWith(axisAlignedBB) || entity2 == entity) continue;
                    if (predicate == null || predicate.apply((Object)entity2)) {
                        list.add(entity2);
                    }
                    if ((entityArray = entity2.getParts()) == null) continue;
                    int n4 = 0;
                    while (n4 < entityArray.length) {
                        entity2 = entityArray[n4];
                        if (entity2 != entity && entity2.getEntityBoundingBox().intersectsWith(axisAlignedBB) && (predicate == null || predicate.apply((Object)entity2))) {
                            list.add(entity2);
                        }
                        ++n4;
                    }
                }
            }
            ++n3;
        }
    }

    public void onChunkLoad() {
        this.isChunkLoaded = true;
        this.worldObj.addTileEntities(this.chunkTileEntityMap.values());
        int n = 0;
        while (n < this.entityLists.length) {
            for (Entity entity : this.entityLists[n]) {
                entity.onChunkLoad();
            }
            this.worldObj.loadEntities(this.entityLists[n]);
            ++n;
        }
    }

    private void func_180700_a(EnumFacing enumFacing) {
        block4: {
            block7: {
                block6: {
                    block5: {
                        if (!this.isTerrainPopulated) break block4;
                        if (enumFacing != EnumFacing.EAST) break block5;
                        int n = 0;
                        while (n < 16) {
                            this.func_150811_f(15, n);
                            ++n;
                        }
                        break block4;
                    }
                    if (enumFacing != EnumFacing.WEST) break block6;
                    int n = 0;
                    while (n < 16) {
                        this.func_150811_f(0, n);
                        ++n;
                    }
                    break block4;
                }
                if (enumFacing != EnumFacing.SOUTH) break block7;
                int n = 0;
                while (n < 16) {
                    this.func_150811_f(n, 15);
                    ++n;
                }
                break block4;
            }
            if (enumFacing != EnumFacing.NORTH) break block4;
            int n = 0;
            while (n < 16) {
                this.func_150811_f(n, 0);
                ++n;
            }
        }
    }

    public Block getBlock(final int n, final int n2, final int n3) {
        try {
            return this.getBlock0(n & 0xF, n2, n3 & 0xF);
        }
        catch (ReportedException reportedException) {
            CrashReportCategory crashReportCategory = reportedException.getCrashReport().makeCategory("Block being got");
            crashReportCategory.addCrashSectionCallable("Location", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(new BlockPos(Chunk.this.xPosition * 16 + n, n2, Chunk.this.zPosition * 16 + n3));
                }
            });
            throw reportedException;
        }
    }

    public boolean canSeeSky(BlockPos blockPos) {
        int n;
        int n2 = blockPos.getX() & 0xF;
        int n3 = blockPos.getY();
        return n3 >= this.heightMap[(n = blockPos.getZ() & 0xF) << 4 | n2];
    }

    private int getBlockLightOpacity(int n, int n2, int n3) {
        return this.getBlock0(n, n2, n3).getLightOpacity();
    }

    public void populateChunk(IChunkProvider iChunkProvider, IChunkProvider iChunkProvider2, int n, int n2) {
        Chunk chunk;
        boolean bl = iChunkProvider.chunkExists(n, n2 - 1);
        boolean bl2 = iChunkProvider.chunkExists(n + 1, n2);
        boolean bl3 = iChunkProvider.chunkExists(n, n2 + 1);
        boolean bl4 = iChunkProvider.chunkExists(n - 1, n2);
        boolean bl5 = iChunkProvider.chunkExists(n - 1, n2 - 1);
        boolean bl6 = iChunkProvider.chunkExists(n + 1, n2 + 1);
        boolean bl7 = iChunkProvider.chunkExists(n - 1, n2 + 1);
        boolean bl8 = iChunkProvider.chunkExists(n + 1, n2 - 1);
        if (bl2 && bl3 && bl6) {
            if (!this.isTerrainPopulated) {
                iChunkProvider.populate(iChunkProvider2, n, n2);
            } else {
                iChunkProvider.func_177460_a(iChunkProvider2, this, n, n2);
            }
        }
        if (bl4 && bl3 && bl7) {
            chunk = iChunkProvider.provideChunk(n - 1, n2);
            if (!chunk.isTerrainPopulated) {
                iChunkProvider.populate(iChunkProvider2, n - 1, n2);
            } else {
                iChunkProvider.func_177460_a(iChunkProvider2, chunk, n - 1, n2);
            }
        }
        if (bl && bl2 && bl8) {
            chunk = iChunkProvider.provideChunk(n, n2 - 1);
            if (!chunk.isTerrainPopulated) {
                iChunkProvider.populate(iChunkProvider2, n, n2 - 1);
            } else {
                iChunkProvider.func_177460_a(iChunkProvider2, chunk, n, n2 - 1);
            }
        }
        if (bl5 && bl && bl4) {
            chunk = iChunkProvider.provideChunk(n - 1, n2 - 1);
            if (!chunk.isTerrainPopulated) {
                iChunkProvider.populate(iChunkProvider2, n - 1, n2 - 1);
            } else {
                iChunkProvider.func_177460_a(iChunkProvider2, chunk, n - 1, n2 - 1);
            }
        }
    }

    public void setStorageArrays(ExtendedBlockStorage[] extendedBlockStorageArray) {
        if (this.storageArrays.length != extendedBlockStorageArray.length) {
            logger.warn("Could not set level chunk sections, array length is " + extendedBlockStorageArray.length + " instead of " + this.storageArrays.length);
        } else {
            int n = 0;
            while (n < this.storageArrays.length) {
                this.storageArrays[n] = extendedBlockStorageArray[n];
                ++n;
            }
        }
    }

    private TileEntity createNewTileEntity(BlockPos blockPos) {
        Block block = this.getBlock(blockPos);
        return !block.hasTileEntity() ? null : ((ITileEntityProvider)((Object)block)).createNewTileEntity(this.worldObj, this.getBlockMetadata(blockPos));
    }

    public int[] getHeightMap() {
        return this.heightMap;
    }

    public void setHeightMap(int[] nArray) {
        if (this.heightMap.length != nArray.length) {
            logger.warn("Could not set level chunk heightmap, array length is " + nArray.length + " instead of " + this.heightMap.length);
        } else {
            int n = 0;
            while (n < this.heightMap.length) {
                this.heightMap[n] = nArray[n];
                ++n;
            }
        }
    }

    public int getLowestHeight() {
        return this.heightMapMinimum;
    }

    public Block getBlock(final BlockPos blockPos) {
        try {
            return this.getBlock0(blockPos.getX() & 0xF, blockPos.getY(), blockPos.getZ() & 0xF);
        }
        catch (ReportedException reportedException) {
            CrashReportCategory crashReportCategory = reportedException.getCrashReport().makeCategory("Block being got");
            crashReportCategory.addCrashSectionCallable("Location", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(blockPos);
                }
            });
            throw reportedException;
        }
    }

    public void removeTileEntity(BlockPos blockPos) {
        TileEntity tileEntity;
        if (this.isChunkLoaded && (tileEntity = this.chunkTileEntityMap.remove(blockPos)) != null) {
            tileEntity.invalidate();
        }
    }

    public Chunk(World world, int n, int n2) {
        this.precipitationHeightMap = new int[256];
        this.updateSkylightColumns = new boolean[256];
        this.chunkTileEntityMap = Maps.newHashMap();
        this.tileEntityPosQueue = Queues.newConcurrentLinkedQueue();
        this.entityLists = new ClassInheritanceMultiMap[16];
        this.worldObj = world;
        this.xPosition = n;
        this.zPosition = n2;
        this.heightMap = new int[256];
        int n3 = 0;
        while (n3 < this.entityLists.length) {
            this.entityLists[n3] = new ClassInheritanceMultiMap<Entity>(Entity.class);
            ++n3;
        }
        Arrays.fill(this.precipitationHeightMap, -999);
        Arrays.fill(this.blockBiomeArray, (byte)-1);
    }

    public void fillChunk(byte[] byArray, int n, boolean bl) {
        Object object;
        int n2 = 0;
        boolean bl2 = !this.worldObj.provider.getHasNoSky();
        int n3 = 0;
        while (n3 < this.storageArrays.length) {
            if ((n & 1 << n3) != 0) {
                if (this.storageArrays[n3] == null) {
                    this.storageArrays[n3] = new ExtendedBlockStorage(n3 << 4, bl2);
                }
                object = this.storageArrays[n3].getData();
                int n4 = 0;
                while (n4 < ((char[])object).length) {
                    object[n4] = (char)((byArray[n2 + 1] & 0xFF) << 8 | byArray[n2] & 0xFF);
                    n2 += 2;
                    ++n4;
                }
            } else if (bl && this.storageArrays[n3] != null) {
                this.storageArrays[n3] = null;
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < this.storageArrays.length) {
            if ((n & 1 << n3) != 0 && this.storageArrays[n3] != null) {
                object = this.storageArrays[n3].getBlocklightArray();
                System.arraycopy(byArray, n2, ((NibbleArray)object).getData(), 0, ((NibbleArray)object).getData().length);
                n2 += ((NibbleArray)object).getData().length;
            }
            ++n3;
        }
        if (bl2) {
            n3 = 0;
            while (n3 < this.storageArrays.length) {
                if ((n & 1 << n3) != 0 && this.storageArrays[n3] != null) {
                    object = this.storageArrays[n3].getSkylightArray();
                    System.arraycopy(byArray, n2, ((NibbleArray)object).getData(), 0, ((NibbleArray)object).getData().length);
                    n2 += ((NibbleArray)object).getData().length;
                }
                ++n3;
            }
        }
        if (bl) {
            System.arraycopy(byArray, n2, this.blockBiomeArray, 0, this.blockBiomeArray.length);
            n3 = n2 + this.blockBiomeArray.length;
        }
        n3 = 0;
        while (n3 < this.storageArrays.length) {
            if (this.storageArrays[n3] != null && (n & 1 << n3) != 0) {
                this.storageArrays[n3].removeInvalidBlocks();
            }
            ++n3;
        }
        this.isLightPopulated = true;
        this.isTerrainPopulated = true;
        this.generateHeightMap();
        for (TileEntity tileEntity : this.chunkTileEntityMap.values()) {
            tileEntity.updateContainingBlockInfo();
        }
    }

    public void setModified(boolean bl) {
        this.isModified = bl;
    }

    public BiomeGenBase getBiome(BlockPos blockPos, WorldChunkManager worldChunkManager) {
        BiomeGenBase biomeGenBase;
        int n = blockPos.getX() & 0xF;
        int n2 = blockPos.getZ() & 0xF;
        int n3 = this.blockBiomeArray[n2 << 4 | n] & 0xFF;
        if (n3 == 255) {
            biomeGenBase = worldChunkManager.getBiomeGenerator(blockPos, BiomeGenBase.plains);
            n3 = biomeGenBase.biomeID;
            this.blockBiomeArray[n2 << 4 | n] = (byte)(n3 & 0xFF);
        }
        return (biomeGenBase = BiomeGenBase.getBiome(n3)) == null ? BiomeGenBase.plains : biomeGenBase;
    }

    public boolean isLightPopulated() {
        return this.isLightPopulated;
    }

    public void func_150804_b(boolean bl) {
        if (this.isGapLightingUpdated && !this.worldObj.provider.getHasNoSky() && !bl) {
            this.recheckGaps(this.worldObj.isRemote);
        }
        this.field_150815_m = true;
        if (!this.isLightPopulated && this.isTerrainPopulated) {
            this.func_150809_p();
        }
        while (!this.tileEntityPosQueue.isEmpty()) {
            BlockPos blockPos = this.tileEntityPosQueue.poll();
            if (this.getTileEntity(blockPos, EnumCreateEntityType.CHECK) != null || !this.getBlock(blockPos).hasTileEntity()) continue;
            TileEntity tileEntity = this.createNewTileEntity(blockPos);
            this.worldObj.setTileEntity(blockPos, tileEntity);
            this.worldObj.markBlockRangeForRenderUpdate(blockPos, blockPos);
        }
    }

    public boolean needsSaving(boolean bl) {
        if (bl ? this.hasEntities && this.worldObj.getTotalWorldTime() != this.lastSaveTime || this.isModified : this.hasEntities && this.worldObj.getTotalWorldTime() >= this.lastSaveTime + 600L) {
            return true;
        }
        return this.isModified;
    }

    public int getHeight(BlockPos blockPos) {
        return this.getHeightValue(blockPos.getX() & 0xF, blockPos.getZ() & 0xF);
    }

    public ExtendedBlockStorage[] getBlockStorageArray() {
        return this.storageArrays;
    }

    public boolean isPopulated() {
        return this.field_150815_m && this.isTerrainPopulated && this.isLightPopulated;
    }

    public void func_150809_p() {
        this.isTerrainPopulated = true;
        this.isLightPopulated = true;
        BlockPos blockPos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
        if (!this.worldObj.provider.getHasNoSky()) {
            if (this.worldObj.isAreaLoaded(blockPos.add(-1, 0, -1), blockPos.add(16, this.worldObj.func_181545_F(), 16))) {
                int n = 0;
                block0: while (n < 16) {
                    int n2 = 0;
                    while (n2 < 16) {
                        if (!this.func_150811_f(n, n2)) {
                            this.isLightPopulated = false;
                            break block0;
                        }
                        ++n2;
                    }
                    ++n;
                }
                if (this.isLightPopulated) {
                    for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
                        int n3 = enumFacing.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? 16 : 1;
                        this.worldObj.getChunkFromBlockCoords(blockPos.offset(enumFacing, n3)).func_180700_a(enumFacing.getOpposite());
                    }
                    this.func_177441_y();
                }
            } else {
                this.isLightPopulated = false;
            }
        }
    }

    public World getWorld() {
        return this.worldObj;
    }

    public int getHeightValue(int n, int n2) {
        return this.heightMap[n2 << 4 | n];
    }

    public void generateSkylightMap() {
        int n = this.getTopFilledSegment();
        this.heightMapMinimum = Integer.MAX_VALUE;
        int n2 = 0;
        while (n2 < 16) {
            int n3 = 0;
            while (n3 < 16) {
                this.precipitationHeightMap[n2 + (n3 << 4)] = -999;
                int n4 = n + 16;
                while (n4 > 0) {
                    if (this.getBlockLightOpacity(n2, n4 - 1, n3) != 0) {
                        this.heightMap[n3 << 4 | n2] = n4;
                        if (n4 >= this.heightMapMinimum) break;
                        this.heightMapMinimum = n4;
                        break;
                    }
                    --n4;
                }
                if (!this.worldObj.provider.getHasNoSky()) {
                    n4 = 15;
                    int n5 = n + 16 - 1;
                    do {
                        ExtendedBlockStorage extendedBlockStorage;
                        int n6;
                        if ((n6 = this.getBlockLightOpacity(n2, n5, n3)) == 0 && n4 != 15) {
                            n6 = 1;
                        }
                        if ((n4 -= n6) <= 0 || (extendedBlockStorage = this.storageArrays[n5 >> 4]) == null) continue;
                        extendedBlockStorage.setExtSkylightValue(n2, n5 & 0xF, n3, n4);
                        this.worldObj.notifyLightSet(new BlockPos((this.xPosition << 4) + n2, n5, (this.zPosition << 4) + n3));
                    } while (--n5 > 0 && n4 > 0);
                }
                ++n3;
            }
            ++n2;
        }
        this.isModified = true;
    }

    public void setInhabitedTime(long l) {
        this.inhabitedTime = l;
    }

    public void resetRelightChecks() {
        this.queuedLightChecks = 0;
    }

    private Block getBlock0(int n, int n2, int n3) {
        ExtendedBlockStorage extendedBlockStorage;
        Block block = Blocks.air;
        if (n2 >= 0 && n2 >> 4 < this.storageArrays.length && (extendedBlockStorage = this.storageArrays[n2 >> 4]) != null) {
            try {
                block = extendedBlockStorage.getBlockByExtId(n, n2 & 0xF, n3);
            }
            catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Getting block");
                throw new ReportedException(crashReport);
            }
        }
        return block;
    }

    public int getBlockLightOpacity(BlockPos blockPos) {
        return this.getBlock(blockPos).getLightOpacity();
    }

    private void func_177441_y() {
        int n = 0;
        while (n < this.updateSkylightColumns.length) {
            this.updateSkylightColumns[n] = true;
            ++n;
        }
        this.recheckGaps(false);
    }

    public void enqueueRelightChecks() {
        BlockPos blockPos = new BlockPos(this.xPosition << 4, 0, this.zPosition << 4);
        int n = 0;
        while (n < 8) {
            if (this.queuedLightChecks >= 4096) {
                return;
            }
            int n2 = this.queuedLightChecks % 16;
            int n3 = this.queuedLightChecks / 16 % 16;
            int n4 = this.queuedLightChecks / 256;
            ++this.queuedLightChecks;
            int n5 = 0;
            while (n5 < 16) {
                boolean bl;
                BlockPos blockPos2 = blockPos.add(n3, (n2 << 4) + n5, n4);
                boolean bl2 = bl = n5 == 0 || n5 == 15 || n3 == 0 || n3 == 15 || n4 == 0 || n4 == 15;
                if (this.storageArrays[n2] == null && bl || this.storageArrays[n2] != null && this.storageArrays[n2].getBlockByExtId(n3, n5, n4).getMaterial() == Material.air) {
                    EnumFacing[] enumFacingArray = EnumFacing.values();
                    int n6 = enumFacingArray.length;
                    int n7 = 0;
                    while (n7 < n6) {
                        EnumFacing enumFacing = enumFacingArray[n7];
                        BlockPos blockPos3 = blockPos2.offset(enumFacing);
                        if (this.worldObj.getBlockState(blockPos3).getBlock().getLightValue() > 0) {
                            this.worldObj.checkLight(blockPos3);
                        }
                        ++n7;
                    }
                    this.worldObj.checkLight(blockPos2);
                }
                ++n5;
            }
            ++n;
        }
    }

    public BlockPos getPrecipitationHeight(BlockPos blockPos) {
        int n = blockPos.getX() & 0xF;
        int n2 = blockPos.getZ() & 0xF;
        int n3 = n | n2 << 4;
        BlockPos blockPos2 = new BlockPos(blockPos.getX(), this.precipitationHeightMap[n3], blockPos.getZ());
        if (blockPos2.getY() == -999) {
            int n4 = this.getTopFilledSegment() + 15;
            blockPos2 = new BlockPos(blockPos.getX(), n4, blockPos.getZ());
            int n5 = -1;
            while (blockPos2.getY() > 0 && n5 == -1) {
                Block block = this.getBlock(blockPos2);
                Material material = block.getMaterial();
                if (!material.blocksMovement() && !material.isLiquid()) {
                    blockPos2 = blockPos2.down();
                    continue;
                }
                n5 = blockPos2.getY() + 1;
            }
            this.precipitationHeightMap[n3] = n5;
        }
        return new BlockPos(blockPos.getX(), this.precipitationHeightMap[n3], blockPos.getZ());
    }

    private int getBlockMetadata(int n, int n2, int n3) {
        if (n2 >> 4 >= this.storageArrays.length) {
            return 0;
        }
        ExtendedBlockStorage extendedBlockStorage = this.storageArrays[n2 >> 4];
        return extendedBlockStorage != null ? extendedBlockStorage.getExtBlockMetadata(n, n2 & 0xF, n3) : 0;
    }

    public int getLightSubtracted(BlockPos blockPos, int n) {
        int n2 = blockPos.getX() & 0xF;
        int n3 = blockPos.getY();
        int n4 = blockPos.getZ() & 0xF;
        ExtendedBlockStorage extendedBlockStorage = this.storageArrays[n3 >> 4];
        if (extendedBlockStorage == null) {
            return !this.worldObj.provider.getHasNoSky() && n < EnumSkyBlock.SKY.defaultLightValue ? EnumSkyBlock.SKY.defaultLightValue - n : 0;
        }
        int n5 = this.worldObj.provider.getHasNoSky() ? 0 : extendedBlockStorage.getExtSkylightValue(n2, n3 & 0xF, n4);
        int n6 = extendedBlockStorage.getExtBlocklightValue(n2, n3 & 0xF, n4);
        if (n6 > (n5 -= n)) {
            n5 = n6;
        }
        return n5;
    }

    public IBlockState setBlockState(BlockPos blockPos, IBlockState iBlockState) {
        TileEntity tileEntity;
        int n;
        int n2;
        int n3 = blockPos.getX() & 0xF;
        int n4 = blockPos.getY();
        if (n4 >= this.precipitationHeightMap[n2 = (n = blockPos.getZ() & 0xF) << 4 | n3] - 1) {
            this.precipitationHeightMap[n2] = -999;
        }
        int n5 = this.heightMap[n2];
        IBlockState iBlockState2 = this.getBlockState(blockPos);
        if (iBlockState2 == iBlockState) {
            return null;
        }
        Block block = iBlockState.getBlock();
        Block block2 = iBlockState2.getBlock();
        ExtendedBlockStorage extendedBlockStorage = this.storageArrays[n4 >> 4];
        boolean bl = false;
        if (extendedBlockStorage == null) {
            if (block == Blocks.air) {
                return null;
            }
            ExtendedBlockStorage extendedBlockStorage2 = new ExtendedBlockStorage(n4 >> 4 << 4, !this.worldObj.provider.getHasNoSky());
            this.storageArrays[n4 >> 4] = extendedBlockStorage2;
            extendedBlockStorage = extendedBlockStorage2;
            bl = n4 >= n5;
        }
        extendedBlockStorage.set(n3, n4 & 0xF, n, iBlockState);
        if (block2 != block) {
            if (!this.worldObj.isRemote) {
                block2.breakBlock(this.worldObj, blockPos, iBlockState2);
            } else if (block2 instanceof ITileEntityProvider) {
                this.worldObj.removeTileEntity(blockPos);
            }
        }
        if (extendedBlockStorage.getBlockByExtId(n3, n4 & 0xF, n) != block) {
            return null;
        }
        if (bl) {
            this.generateSkylightMap();
        } else {
            int n6 = block.getLightOpacity();
            int n7 = block2.getLightOpacity();
            if (n6 > 0) {
                if (n4 >= n5) {
                    this.relightBlock(n3, n4 + 1, n);
                }
            } else if (n4 == n5 - 1) {
                this.relightBlock(n3, n4, n);
            }
            if (n6 != n7 && (n6 < n7 || this.getLightFor(EnumSkyBlock.SKY, blockPos) > 0 || this.getLightFor(EnumSkyBlock.BLOCK, blockPos) > 0)) {
                this.propagateSkylightOcclusion(n3, n);
            }
        }
        if (block2 instanceof ITileEntityProvider && (tileEntity = this.getTileEntity(blockPos, EnumCreateEntityType.CHECK)) != null) {
            tileEntity.updateContainingBlockInfo();
        }
        if (!this.worldObj.isRemote && block2 != block) {
            block.onBlockAdded(this.worldObj, blockPos, iBlockState);
        }
        if (block instanceof ITileEntityProvider) {
            TileEntity tileEntity2 = this.getTileEntity(blockPos, EnumCreateEntityType.CHECK);
            if (tileEntity2 == null) {
                tileEntity2 = ((ITileEntityProvider)((Object)block)).createNewTileEntity(this.worldObj, block.getMetaFromState(iBlockState));
                this.worldObj.setTileEntity(blockPos, tileEntity2);
            }
            if (tileEntity2 != null) {
                tileEntity2.updateContainingBlockInfo();
            }
        }
        this.isModified = true;
        return iBlockState2;
    }

    private void recheckGaps(boolean bl) {
        this.worldObj.theProfiler.startSection("recheckGaps");
        if (this.worldObj.isAreaLoaded(new BlockPos(this.xPosition * 16 + 8, 0, this.zPosition * 16 + 8), 16)) {
            int n = 0;
            while (n < 16) {
                int n2 = 0;
                while (n2 < 16) {
                    if (this.updateSkylightColumns[n + n2 * 16]) {
                        this.updateSkylightColumns[n + n2 * 16] = false;
                        int n3 = this.getHeightValue(n, n2);
                        int n4 = this.xPosition * 16 + n;
                        int n5 = this.zPosition * 16 + n2;
                        int n6 = Integer.MAX_VALUE;
                        for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
                            n6 = Math.min(n6, this.worldObj.getChunksLowestHorizon(n4 + enumFacing.getFrontOffsetX(), n5 + enumFacing.getFrontOffsetZ()));
                        }
                        this.checkSkylightNeighborHeight(n4, n5, n6);
                        for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
                            this.checkSkylightNeighborHeight(n4 + enumFacing.getFrontOffsetX(), n5 + enumFacing.getFrontOffsetZ(), n3);
                        }
                        if (bl) {
                            this.worldObj.theProfiler.endSection();
                            return;
                        }
                    }
                    ++n2;
                }
                ++n;
            }
            this.isGapLightingUpdated = false;
        }
        this.worldObj.theProfiler.endSection();
    }

    public IBlockState getBlockState(final BlockPos blockPos) {
        if (this.worldObj.getWorldType() == WorldType.DEBUG_WORLD) {
            IBlockState iBlockState = null;
            if (blockPos.getY() == 60) {
                iBlockState = Blocks.barrier.getDefaultState();
            }
            if (blockPos.getY() == 70) {
                iBlockState = ChunkProviderDebug.func_177461_b(blockPos.getX(), blockPos.getZ());
            }
            return iBlockState == null ? Blocks.air.getDefaultState() : iBlockState;
        }
        try {
            ExtendedBlockStorage extendedBlockStorage;
            if (blockPos.getY() >= 0 && blockPos.getY() >> 4 < this.storageArrays.length && (extendedBlockStorage = this.storageArrays[blockPos.getY() >> 4]) != null) {
                int n = blockPos.getX() & 0xF;
                int n2 = blockPos.getY() & 0xF;
                int n3 = blockPos.getZ() & 0xF;
                return extendedBlockStorage.get(n, n2, n3);
            }
            return Blocks.air.getDefaultState();
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Getting block state");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Block being got");
            crashReportCategory.addCrashSectionCallable("Location", new Callable<String>(){

                @Override
                public String call() throws Exception {
                    return CrashReportCategory.getCoordinateInfo(blockPos);
                }
            });
            throw new ReportedException(crashReport);
        }
    }

    public static enum EnumCreateEntityType {
        IMMEDIATE,
        QUEUED,
        CHECK;

    }
}

