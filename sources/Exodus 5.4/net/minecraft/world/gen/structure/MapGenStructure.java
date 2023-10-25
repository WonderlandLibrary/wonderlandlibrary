/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
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

    public boolean func_175795_b(BlockPos blockPos) {
        this.func_143027_a(this.worldObj);
        return this.func_175797_c(blockPos) != null;
    }

    public BlockPos getClosestStrongholdPos(World world, BlockPos blockPos) {
        double d;
        this.worldObj = world;
        this.func_143027_a(world);
        this.rand.setSeed(world.getSeed());
        long l = this.rand.nextLong();
        long l2 = this.rand.nextLong();
        long l3 = (long)(blockPos.getX() >> 4) * l;
        long l4 = (long)(blockPos.getZ() >> 4) * l2;
        this.rand.setSeed(l3 ^ l4 ^ world.getSeed());
        this.recursiveGenerate(world, blockPos.getX() >> 4, blockPos.getZ() >> 4, 0, 0, null);
        double d2 = Double.MAX_VALUE;
        Object object = null;
        for (StructureStart list2 : this.structureMap.values()) {
            StructureComponent structureComponent;
            Object object2;
            if (!list2.isSizeableStructure() || !((d = ((Vec3i)(object2 = (structureComponent = list2.getComponents().get(0)).getBoundingBoxCenter())).distanceSq(blockPos)) < d2)) continue;
            d2 = d;
            object = object2;
        }
        if (object != null) {
            return object;
        }
        List<BlockPos> list = this.getCoordList();
        if (list != null) {
            Object object4 = null;
            for (BlockPos blockPos2 : list) {
                d = blockPos2.distanceSq(blockPos);
                if (!(d < d2)) continue;
                d2 = d;
                object4 = blockPos2;
            }
            return object4;
        }
        return null;
    }

    public abstract String getStructureName();

    protected StructureStart func_175797_c(BlockPos blockPos) {
        for (StructureStart structureStart : this.structureMap.values()) {
            if (!structureStart.isSizeableStructure() || !structureStart.getBoundingBox().isVecInside(blockPos)) continue;
            for (StructureComponent structureComponent : structureStart.getComponents()) {
                if (!structureComponent.getBoundingBox().isVecInside(blockPos)) continue;
                return structureStart;
            }
        }
        return null;
    }

    protected abstract StructureStart getStructureStart(int var1, int var2);

    private void func_143026_a(int n, int n2, StructureStart structureStart) {
        this.structureData.writeInstance(structureStart.writeStructureComponentsToNBT(n, n2), n, n2);
        this.structureData.markDirty();
    }

    private void func_143027_a(World world) {
        if (this.structureData == null) {
            this.structureData = (MapGenStructureData)world.loadItemData(MapGenStructureData.class, this.getStructureName());
            if (this.structureData == null) {
                this.structureData = new MapGenStructureData(this.getStructureName());
                world.setItemData(this.getStructureName(), this.structureData);
            } else {
                NBTTagCompound nBTTagCompound = this.structureData.getTagCompound();
                for (String string : nBTTagCompound.getKeySet()) {
                    NBTTagCompound nBTTagCompound2;
                    NBTBase nBTBase = nBTTagCompound.getTag(string);
                    if (nBTBase.getId() != 10 || !(nBTTagCompound2 = (NBTTagCompound)nBTBase).hasKey("ChunkX") || !nBTTagCompound2.hasKey("ChunkZ")) continue;
                    int n = nBTTagCompound2.getInteger("ChunkX");
                    int n2 = nBTTagCompound2.getInteger("ChunkZ");
                    StructureStart structureStart = MapGenStructureIO.getStructureStart(nBTTagCompound2, world);
                    if (structureStart == null) continue;
                    this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(n, n2), structureStart);
                }
            }
        }
    }

    public boolean generateStructure(World world, Random random, ChunkCoordIntPair chunkCoordIntPair) {
        this.func_143027_a(world);
        int n = (chunkCoordIntPair.chunkXPos << 4) + 8;
        int n2 = (chunkCoordIntPair.chunkZPos << 4) + 8;
        boolean bl = false;
        for (StructureStart structureStart : this.structureMap.values()) {
            if (!structureStart.isSizeableStructure() || !structureStart.func_175788_a(chunkCoordIntPair) || !structureStart.getBoundingBox().intersectsWith(n, n2, n + 15, n2 + 15)) continue;
            structureStart.generateStructure(world, random, new StructureBoundingBox(n, n2, n + 15, n2 + 15));
            structureStart.func_175787_b(chunkCoordIntPair);
            bl = true;
            this.func_143026_a(structureStart.getChunkPosX(), structureStart.getChunkPosZ(), structureStart);
        }
        return bl;
    }

    protected List<BlockPos> getCoordList() {
        return null;
    }

    protected abstract boolean canSpawnStructureAtCoords(int var1, int var2);

    @Override
    protected final void recursiveGenerate(World world, final int n, final int n2, int n3, int n4, ChunkPrimer chunkPrimer) {
        this.func_143027_a(world);
        if (!this.structureMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(n, n2))) {
            this.rand.nextInt();
            try {
                if (this.canSpawnStructureAtCoords(n, n2)) {
                    StructureStart structureStart = this.getStructureStart(n, n2);
                    this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(n, n2), structureStart);
                    this.func_143026_a(n, n2, structureStart);
                }
            }
            catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Exception preparing structure feature");
                CrashReportCategory crashReportCategory = crashReport.makeCategory("Feature being prepared");
                crashReportCategory.addCrashSectionCallable("Is feature chunk", new Callable<String>(){

                    @Override
                    public String call() throws Exception {
                        return MapGenStructure.this.canSpawnStructureAtCoords(n, n2) ? "True" : "False";
                    }
                });
                crashReportCategory.addCrashSection("Chunk location", String.format("%d,%d", n, n2));
                crashReportCategory.addCrashSectionCallable("Chunk pos hash", new Callable<String>(){

                    @Override
                    public String call() throws Exception {
                        return String.valueOf(ChunkCoordIntPair.chunkXZ2Int(n, n2));
                    }
                });
                crashReportCategory.addCrashSectionCallable("Structure type", new Callable<String>(){

                    @Override
                    public String call() throws Exception {
                        return MapGenStructure.this.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(crashReport);
            }
        }
    }

    public boolean func_175796_a(World world, BlockPos blockPos) {
        this.func_143027_a(world);
        for (StructureStart structureStart : this.structureMap.values()) {
            if (!structureStart.isSizeableStructure() || !structureStart.getBoundingBox().isVecInside(blockPos)) continue;
            return true;
        }
        return false;
    }
}

