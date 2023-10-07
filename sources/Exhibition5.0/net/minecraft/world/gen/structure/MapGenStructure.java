// 
// Decompiled by Procyon v0.5.30
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
import net.minecraft.util.LongHashMap;
import java.util.Map;
import net.minecraft.world.gen.MapGenBase;

public abstract class MapGenStructure extends MapGenBase
{
    private MapGenStructureData field_143029_e;
    protected Map structureMap;
    private static final String __OBFID = "CL_00000505";
    private LongHashMap structureLongMap;
    
    public MapGenStructure() {
        this.structureMap = Maps.newHashMap();
        this.structureLongMap = new LongHashMap();
    }
    
    public abstract String getStructureName();
    
    @Override
    protected final void func_180701_a(final World worldIn, final int p_180701_2_, final int p_180701_3_, final int p_180701_4_, final int p_180701_5_, final ChunkPrimer p_180701_6_) {
        this.func_143027_a(worldIn);
        if (!this.structureLongMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(p_180701_2_, p_180701_3_))) {
            this.rand.nextInt();
            try {
                if (this.canSpawnStructureAtCoords(p_180701_2_, p_180701_3_)) {
                    final StructureStart var10 = this.getStructureStart(p_180701_2_, p_180701_3_);
                    this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(p_180701_2_, p_180701_3_), var10);
                    this.structureLongMap.add(ChunkCoordIntPair.chunkXZ2Int(p_180701_2_, p_180701_3_), var10);
                    this.func_143026_a(p_180701_2_, p_180701_3_, var10);
                }
            }
            catch (Throwable var12) {
                final CrashReport var11 = CrashReport.makeCrashReport(var12, "Exception preparing structure feature");
                final CrashReportCategory var13 = var11.makeCategory("Feature being prepared");
                var13.addCrashSectionCallable("Is feature chunk", new Callable() {
                    private static final String __OBFID = "CL_00000506";
                    
                    @Override
                    public String call() {
                        return MapGenStructure.this.canSpawnStructureAtCoords(p_180701_2_, p_180701_3_) ? "True" : "False";
                    }
                });
                var13.addCrashSection("Chunk location", String.format("%d,%d", p_180701_2_, p_180701_3_));
                var13.addCrashSectionCallable("Chunk pos hash", new Callable() {
                    private static final String __OBFID = "CL_00000507";
                    
                    @Override
                    public String call() {
                        return String.valueOf(ChunkCoordIntPair.chunkXZ2Int(p_180701_2_, p_180701_3_));
                    }
                });
                var13.addCrashSectionCallable("Structure type", new Callable() {
                    private static final String __OBFID = "CL_00000508";
                    
                    @Override
                    public String call() {
                        return MapGenStructure.this.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(var11);
            }
        }
    }
    
    public boolean func_175794_a(final World worldIn, final Random p_175794_2_, final ChunkCoordIntPair p_175794_3_) {
        this.func_143027_a(worldIn);
        final int var4 = (p_175794_3_.chunkXPos << 4) + 8;
        final int var5 = (p_175794_3_.chunkZPos << 4) + 8;
        boolean var6 = false;
        for (final StructureStart var8 : this.structureMap.values()) {
            if (var8.isSizeableStructure() && var8.func_175788_a(p_175794_3_) && var8.getBoundingBox().intersectsWith(var4, var5, var4 + 15, var5 + 15)) {
                var8.generateStructure(worldIn, p_175794_2_, new StructureBoundingBox(var4, var5, var4 + 15, var5 + 15));
                var8.func_175787_b(p_175794_3_);
                var6 = true;
                this.func_143026_a(var8.func_143019_e(), var8.func_143018_f(), var8);
            }
        }
        return var6;
    }
    
    public boolean func_175795_b(final BlockPos p_175795_1_) {
        this.func_143027_a(this.worldObj);
        return this.func_175797_c(p_175795_1_) != null;
    }
    
    protected StructureStart func_175797_c(final BlockPos p_175797_1_) {
        for (final StructureStart var3 : this.structureMap.values()) {
            if (var3.isSizeableStructure() && var3.getBoundingBox().func_175898_b(p_175797_1_)) {
                for (final StructureComponent var5 : var3.getComponents()) {
                    if (var5.getBoundingBox().func_175898_b(p_175797_1_)) {
                        return var3;
                    }
                }
            }
        }
        return null;
    }
    
    public boolean func_175796_a(final World worldIn, final BlockPos p_175796_2_) {
        this.func_143027_a(worldIn);
        for (final StructureStart var4 : this.structureMap.values()) {
            if (var4.isSizeableStructure() && var4.getBoundingBox().func_175898_b(p_175796_2_)) {
                return true;
            }
        }
        return false;
    }
    
    public BlockPos func_180706_b(final World worldIn, final BlockPos p_180706_2_) {
        this.func_143027_a(this.worldObj = worldIn);
        this.rand.setSeed(worldIn.getSeed());
        final long var3 = this.rand.nextLong();
        final long var4 = this.rand.nextLong();
        final long var5 = (p_180706_2_.getX() >> 4) * var3;
        final long var6 = (p_180706_2_.getZ() >> 4) * var4;
        this.rand.setSeed(var5 ^ var6 ^ worldIn.getSeed());
        this.func_180701_a(worldIn, p_180706_2_.getX() >> 4, p_180706_2_.getZ() >> 4, 0, 0, null);
        double var7 = Double.MAX_VALUE;
        BlockPos var8 = null;
        for (final StructureStart var10 : this.structureMap.values()) {
            if (var10.isSizeableStructure()) {
                final StructureComponent var11 = var10.getComponents().get(0);
                final BlockPos var12 = var11.func_180776_a();
                final double var13 = var12.distanceSq(p_180706_2_);
                if (var13 >= var7) {
                    continue;
                }
                var7 = var13;
                var8 = var12;
            }
        }
        if (var8 != null) {
            return var8;
        }
        final List var14 = this.getCoordList();
        if (var14 != null) {
            BlockPos var15 = null;
            for (final BlockPos var12 : var14) {
                final double var13 = var12.distanceSq(p_180706_2_);
                if (var13 < var7) {
                    var7 = var13;
                    var15 = var12;
                }
            }
            return var15;
        }
        return null;
    }
    
    protected List getCoordList() {
        return null;
    }
    
    private void func_143027_a(final World worldIn) {
        if (this.field_143029_e == null) {
            this.field_143029_e = (MapGenStructureData)worldIn.loadItemData(MapGenStructureData.class, this.getStructureName());
            if (this.field_143029_e == null) {
                this.field_143029_e = new MapGenStructureData(this.getStructureName());
                worldIn.setItemData(this.getStructureName(), this.field_143029_e);
            }
            else {
                final NBTTagCompound var2 = this.field_143029_e.func_143041_a();
                for (final String var4 : var2.getKeySet()) {
                    final NBTBase var5 = var2.getTag(var4);
                    if (var5.getId() == 10) {
                        final NBTTagCompound var6 = (NBTTagCompound)var5;
                        if (!var6.hasKey("ChunkX") || !var6.hasKey("ChunkZ")) {
                            continue;
                        }
                        final int var7 = var6.getInteger("ChunkX");
                        final int var8 = var6.getInteger("ChunkZ");
                        final StructureStart var9 = MapGenStructureIO.func_143035_a(var6, worldIn);
                        if (var9 == null) {
                            continue;
                        }
                        this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(var7, var8), var9);
                        this.structureLongMap.add(ChunkCoordIntPair.chunkXZ2Int(var7, var8), var9);
                    }
                }
            }
        }
    }
    
    private void func_143026_a(final int p_143026_1_, final int p_143026_2_, final StructureStart p_143026_3_) {
        this.field_143029_e.func_143043_a(p_143026_3_.func_143021_a(p_143026_1_, p_143026_2_), p_143026_1_, p_143026_2_);
        this.field_143029_e.markDirty();
    }
    
    protected abstract boolean canSpawnStructureAtCoords(final int p0, final int p1);
    
    protected abstract StructureStart getStructureStart(final int p0, final int p1);
}
