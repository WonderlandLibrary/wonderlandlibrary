package net.minecraft.world.gen.structure;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3i;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.MapGenStructureData;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;

public abstract class MapGenStructure
extends MapGenBase {
    private MapGenStructureData structureData;
    protected Map<Long, StructureStart> structureMap = Maps.newHashMap();

    public abstract String getStructureName();

    protected final void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int p_180701_4_, int p_180701_5_, ChunkPrimer chunkPrimerIn) {
        this.initializeStructureData(worldIn);
        if (!this.structureMap.containsKey((Object)ChunkCoordIntPair.chunkXZ2Int((int)chunkX, (int)chunkZ))) {
            this.rand.nextInt();
            try {
                if (this.canSpawnStructureAtCoords(chunkX, chunkZ)) {
                    StructureStart structurestart = this.getStructureStart(chunkX, chunkZ);
                    this.structureMap.put((Object)ChunkCoordIntPair.chunkXZ2Int((int)chunkX, (int)chunkZ), (Object)structurestart);
                    this.setStructureStart(chunkX, chunkZ, structurestart);
                }
            }
            catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport((Throwable)throwable, (String)"Exception preparing structure feature");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Feature being prepared");
                crashreportcategory.addCrashSectionCallable("Is feature chunk", (Callable)new /* Unavailable Anonymous Inner Class!! */);
                crashreportcategory.addCrashSection("Chunk location", (Object)String.format((String)"%d,%d", (Object[])new Object[]{chunkX, chunkZ}));
                crashreportcategory.addCrashSectionCallable("Chunk pos hash", (Callable)new /* Unavailable Anonymous Inner Class!! */);
                crashreportcategory.addCrashSectionCallable("Structure type", (Callable)new /* Unavailable Anonymous Inner Class!! */);
                throw new ReportedException(crashreport);
            }
        }
    }

    public boolean generateStructure(World worldIn, Random randomIn, ChunkCoordIntPair chunkCoord) {
        this.initializeStructureData(worldIn);
        int i = (chunkCoord.chunkXPos << 4) + 8;
        int j = (chunkCoord.chunkZPos << 4) + 8;
        boolean flag = false;
        for (StructureStart structurestart : this.structureMap.values()) {
            if (!structurestart.isSizeableStructure() || !structurestart.func_175788_a(chunkCoord) || !structurestart.getBoundingBox().intersectsWith(i, j, i + 15, j + 15)) continue;
            structurestart.generateStructure(worldIn, randomIn, new StructureBoundingBox(i, j, i + 15, j + 15));
            structurestart.func_175787_b(chunkCoord);
            flag = true;
            this.setStructureStart(structurestart.getChunkPosX(), structurestart.getChunkPosZ(), structurestart);
        }
        return flag;
    }

    public boolean func_175795_b(BlockPos pos) {
        this.initializeStructureData(this.worldObj);
        return this.func_175797_c(pos) != null;
    }

    protected StructureStart func_175797_c(BlockPos pos) {
        for (StructureStart structurestart : this.structureMap.values()) {
            if (!structurestart.isSizeableStructure() || !structurestart.getBoundingBox().isVecInside((Vec3i)pos)) continue;
            for (StructureComponent structurecomponent : structurestart.getComponents()) {
                if (!structurecomponent.getBoundingBox().isVecInside((Vec3i)pos)) continue;
                return structurestart;
            }
        }
        return null;
    }

    public boolean isPositionInStructure(World worldIn, BlockPos pos) {
        this.initializeStructureData(worldIn);
        for (StructureStart structurestart : this.structureMap.values()) {
            if (!structurestart.isSizeableStructure() || !structurestart.getBoundingBox().isVecInside((Vec3i)pos)) continue;
            return true;
        }
        return false;
    }

    public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos) {
        this.worldObj = worldIn;
        this.initializeStructureData(worldIn);
        this.rand.setSeed(worldIn.getSeed());
        long i = this.rand.nextLong();
        long j = this.rand.nextLong();
        long k = (long)(pos.getX() >> 4) * i;
        long l = (long)(pos.getZ() >> 4) * j;
        this.rand.setSeed(k ^ l ^ worldIn.getSeed());
        this.recursiveGenerate(worldIn, pos.getX() >> 4, pos.getZ() >> 4, 0, 0, null);
        double d0 = Double.MAX_VALUE;
        BlockPos blockpos = null;
        for (StructureStart structurestart : this.structureMap.values()) {
            StructureComponent structurecomponent;
            BlockPos blockpos1;
            double d1;
            if (!structurestart.isSizeableStructure() || !((d1 = (blockpos1 = (structurecomponent = (StructureComponent)structurestart.getComponents().get(0)).getBoundingBoxCenter()).distanceSq((Vec3i)pos)) < d0)) continue;
            d0 = d1;
            blockpos = blockpos1;
        }
        if (blockpos != null) {
            return blockpos;
        }
        List<BlockPos> list = this.getCoordList();
        if (list != null) {
            BlockPos blockpos2 = null;
            for (BlockPos blockpos3 : list) {
                double d2 = blockpos3.distanceSq((Vec3i)pos);
                if (!(d2 < d0)) continue;
                d0 = d2;
                blockpos2 = blockpos3;
            }
            return blockpos2;
        }
        return null;
    }

    protected List<BlockPos> getCoordList() {
        return null;
    }

    private void initializeStructureData(World worldIn) {
        if (this.structureData == null) {
            this.structureData = (MapGenStructureData)worldIn.loadItemData(MapGenStructureData.class, this.getStructureName());
            if (this.structureData == null) {
                this.structureData = new MapGenStructureData(this.getStructureName());
                worldIn.setItemData(this.getStructureName(), (WorldSavedData)this.structureData);
            } else {
                NBTTagCompound nbttagcompound = this.structureData.getTagCompound();
                for (String s : nbttagcompound.getKeySet()) {
                    NBTTagCompound nbttagcompound1;
                    NBTBase nbtbase = nbttagcompound.getTag(s);
                    if (nbtbase.getId() != 10 || !(nbttagcompound1 = (NBTTagCompound)nbtbase).hasKey("ChunkX") || !nbttagcompound1.hasKey("ChunkZ")) continue;
                    int i = nbttagcompound1.getInteger("ChunkX");
                    int j = nbttagcompound1.getInteger("ChunkZ");
                    StructureStart structurestart = MapGenStructureIO.getStructureStart((NBTTagCompound)nbttagcompound1, (World)worldIn);
                    if (structurestart == null) continue;
                    this.structureMap.put((Object)ChunkCoordIntPair.chunkXZ2Int((int)i, (int)j), (Object)structurestart);
                }
            }
        }
    }

    private void setStructureStart(int chunkX, int chunkZ, StructureStart start) {
        this.structureData.writeInstance(start.writeStructureComponentsToNBT(chunkX, chunkZ), chunkX, chunkZ);
        this.structureData.markDirty();
    }

    protected abstract boolean canSpawnStructureAtCoords(int var1, int var2);

    protected abstract StructureStart getStructureStart(int var1, int var2);
}
