// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldSavedData;
import java.util.List;
import net.minecraft.util.Vec3i;
import net.minecraft.util.BlockPos;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.World;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.world.gen.MapGenBase;

public abstract class MapGenStructure extends MapGenBase
{
    private MapGenStructureData structureData;
    protected Map<Long, StructureStart> structureMap;
    
    public MapGenStructure() {
        this.structureMap = Maps.newHashMap();
    }
    
    public abstract String getStructureName();
    
    @Override
    protected final void recursiveGenerate(final World worldIn, final int chunkX, final int chunkZ, final int p_180701_4_, final int p_180701_5_, final ChunkPrimer chunkPrimerIn) {
        this.initializeStructureData(worldIn);
        if (!this.structureMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ))) {
            this.rand.nextInt();
            try {
                if (this.canSpawnStructureAtCoords(chunkX, chunkZ)) {
                    final StructureStart structurestart = this.getStructureStart(chunkX, chunkZ);
                    this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ), structurestart);
                    this.setStructureStart(chunkX, chunkZ, structurestart);
                }
            }
            catch (final Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception preparing structure feature");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Feature being prepared");
                crashreportcategory.addCrashSectionCallable("Is feature chunk", new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return MapGenStructure.this.canSpawnStructureAtCoords(chunkX, chunkZ) ? "True" : "False";
                    }
                });
                crashreportcategory.addCrashSection("Chunk location", String.format("%d,%d", chunkX, chunkZ));
                crashreportcategory.addCrashSectionCallable("Chunk pos hash", new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return String.valueOf(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ));
                    }
                });
                crashreportcategory.addCrashSectionCallable("Structure type", new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return MapGenStructure.this.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }
    
    public boolean generateStructure(final World worldIn, final Random randomIn, final ChunkCoordIntPair chunkCoord) {
        this.initializeStructureData(worldIn);
        final int i = (chunkCoord.chunkXPos << 4) + 8;
        final int j = (chunkCoord.chunkZPos << 4) + 8;
        boolean flag = false;
        for (final StructureStart structurestart : this.structureMap.values()) {
            if (structurestart.isSizeableStructure() && structurestart.func_175788_a(chunkCoord) && structurestart.getBoundingBox().intersectsWith(i, j, i + 15, j + 15)) {
                structurestart.generateStructure(worldIn, randomIn, new StructureBoundingBox(i, j, i + 15, j + 15));
                structurestart.func_175787_b(chunkCoord);
                flag = true;
                this.setStructureStart(structurestart.getChunkPosX(), structurestart.getChunkPosZ(), structurestart);
            }
        }
        return flag;
    }
    
    public boolean func_175795_b(final BlockPos pos) {
        this.initializeStructureData(this.worldObj);
        return this.func_175797_c(pos) != null;
    }
    
    protected StructureStart func_175797_c(final BlockPos pos) {
        for (final StructureStart structurestart : this.structureMap.values()) {
            if (structurestart.isSizeableStructure() && structurestart.getBoundingBox().isVecInside(pos)) {
                for (final StructureComponent structurecomponent : structurestart.getComponents()) {
                    if (structurecomponent.getBoundingBox().isVecInside(pos)) {
                        return structurestart;
                    }
                }
            }
        }
        return null;
    }
    
    public boolean isPositionInStructure(final World worldIn, final BlockPos pos) {
        this.initializeStructureData(worldIn);
        for (final StructureStart structurestart : this.structureMap.values()) {
            if (structurestart.isSizeableStructure() && structurestart.getBoundingBox().isVecInside(pos)) {
                return true;
            }
        }
        return false;
    }
    
    public BlockPos getClosestStrongholdPos(final World worldIn, final BlockPos pos) {
        this.initializeStructureData(this.worldObj = worldIn);
        this.rand.setSeed(worldIn.getSeed());
        final long i = this.rand.nextLong();
        final long j = this.rand.nextLong();
        final long k = (pos.getX() >> 4) * i;
        final long l = (pos.getZ() >> 4) * j;
        this.rand.setSeed(k ^ l ^ worldIn.getSeed());
        this.recursiveGenerate(worldIn, pos.getX() >> 4, pos.getZ() >> 4, 0, 0, null);
        double d0 = Double.MAX_VALUE;
        BlockPos blockpos = null;
        for (final StructureStart structurestart : this.structureMap.values()) {
            if (structurestart.isSizeableStructure()) {
                final StructureComponent structurecomponent = structurestart.getComponents().get(0);
                final BlockPos blockpos2 = structurecomponent.getBoundingBoxCenter();
                final double d2 = blockpos2.distanceSq(pos);
                if (d2 >= d0) {
                    continue;
                }
                d0 = d2;
                blockpos = blockpos2;
            }
        }
        if (blockpos != null) {
            return blockpos;
        }
        final List<BlockPos> list = this.getCoordList();
        if (list != null) {
            BlockPos blockpos3 = null;
            for (final BlockPos blockpos4 : list) {
                final double d3 = blockpos4.distanceSq(pos);
                if (d3 < d0) {
                    d0 = d3;
                    blockpos3 = blockpos4;
                }
            }
            return blockpos3;
        }
        return null;
    }
    
    protected List<BlockPos> getCoordList() {
        return null;
    }
    
    private void initializeStructureData(final World worldIn) {
        if (this.structureData == null) {
            this.structureData = (MapGenStructureData)worldIn.loadItemData(MapGenStructureData.class, this.getStructureName());
            if (this.structureData == null) {
                this.structureData = new MapGenStructureData(this.getStructureName());
                worldIn.setItemData(this.getStructureName(), this.structureData);
            }
            else {
                final NBTTagCompound nbttagcompound = this.structureData.getTagCompound();
                for (final String s : nbttagcompound.getKeySet()) {
                    final NBTBase nbtbase = nbttagcompound.getTag(s);
                    if (nbtbase.getId() == 10) {
                        final NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbtbase;
                        if (!nbttagcompound2.hasKey("ChunkX") || !nbttagcompound2.hasKey("ChunkZ")) {
                            continue;
                        }
                        final int i = nbttagcompound2.getInteger("ChunkX");
                        final int j = nbttagcompound2.getInteger("ChunkZ");
                        final StructureStart structurestart = MapGenStructureIO.getStructureStart(nbttagcompound2, worldIn);
                        if (structurestart == null) {
                            continue;
                        }
                        this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(i, j), structurestart);
                    }
                }
            }
        }
    }
    
    private void setStructureStart(final int chunkX, final int chunkZ, final StructureStart start) {
        this.structureData.writeInstance(start.writeStructureComponentsToNBT(chunkX, chunkZ), chunkX, chunkZ);
        this.structureData.markDirty();
    }
    
    protected abstract boolean canSpawnStructureAtCoords(final int p0, final int p1);
    
    protected abstract StructureStart getStructureStart(final int p0, final int p1);
}
