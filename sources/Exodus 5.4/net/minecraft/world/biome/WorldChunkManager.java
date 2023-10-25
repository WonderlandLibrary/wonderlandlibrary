/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.world.biome;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeCache;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class WorldChunkManager {
    private GenLayer genBiomes;
    private String field_180301_f = "";
    private GenLayer biomeIndexLayer;
    private BiomeCache biomeCache = new BiomeCache(this);
    private List<BiomeGenBase> biomesToSpawnIn = Lists.newArrayList();

    protected WorldChunkManager() {
        this.biomesToSpawnIn.add(BiomeGenBase.forest);
        this.biomesToSpawnIn.add(BiomeGenBase.plains);
        this.biomesToSpawnIn.add(BiomeGenBase.taiga);
        this.biomesToSpawnIn.add(BiomeGenBase.taigaHills);
        this.biomesToSpawnIn.add(BiomeGenBase.forestHills);
        this.biomesToSpawnIn.add(BiomeGenBase.jungle);
        this.biomesToSpawnIn.add(BiomeGenBase.jungleHills);
    }

    public BiomeGenBase getBiomeGenerator(BlockPos blockPos) {
        return this.getBiomeGenerator(blockPos, null);
    }

    public List<BiomeGenBase> getBiomesToSpawnIn() {
        return this.biomesToSpawnIn;
    }

    public void cleanupCache() {
        this.biomeCache.cleanupCache();
    }

    public WorldChunkManager(long l, WorldType worldType, String string) {
        this();
        this.field_180301_f = string;
        GenLayer[] genLayerArray = GenLayer.initializeAllBiomeGenerators(l, worldType, string);
        this.genBiomes = genLayerArray[0];
        this.biomeIndexLayer = genLayerArray[1];
    }

    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] biomeGenBaseArray, int n, int n2, int n3, int n4) {
        return this.getBiomeGenAt(biomeGenBaseArray, n, n2, n3, n4, true);
    }

    public float[] getRainfall(float[] fArray, int n, int n2, int n3, int n4) {
        IntCache.resetIntCache();
        if (fArray == null || fArray.length < n3 * n4) {
            fArray = new float[n3 * n4];
        }
        int[] nArray = this.biomeIndexLayer.getInts(n, n2, n3, n4);
        int n5 = 0;
        while (n5 < n3 * n4) {
            try {
                float f = (float)BiomeGenBase.getBiomeFromBiomeList(nArray[n5], BiomeGenBase.field_180279_ad).getIntRainfall() / 65536.0f;
                if (f > 1.0f) {
                    f = 1.0f;
                }
                fArray[n5] = f;
            }
            catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
                CrashReportCategory crashReportCategory = crashReport.makeCategory("DownfallBlock");
                crashReportCategory.addCrashSection("biome id", n5);
                crashReportCategory.addCrashSection("downfalls[] size", fArray.length);
                crashReportCategory.addCrashSection("x", n);
                crashReportCategory.addCrashSection("z", n2);
                crashReportCategory.addCrashSection("w", n3);
                crashReportCategory.addCrashSection("h", n4);
                throw new ReportedException(crashReport);
            }
            ++n5;
        }
        return fArray;
    }

    public BlockPos findBiomePosition(int n, int n2, int n3, List<BiomeGenBase> list, Random random) {
        IntCache.resetIntCache();
        int n4 = n - n3 >> 2;
        int n5 = n2 - n3 >> 2;
        int n6 = n + n3 >> 2;
        int n7 = n2 + n3 >> 2;
        int n8 = n6 - n4 + 1;
        int n9 = n7 - n5 + 1;
        int[] nArray = this.genBiomes.getInts(n4, n5, n8, n9);
        BlockPos blockPos = null;
        int n10 = 0;
        int n11 = 0;
        while (n11 < n8 * n9) {
            int n12 = n4 + n11 % n8 << 2;
            int n13 = n5 + n11 / n8 << 2;
            BiomeGenBase biomeGenBase = BiomeGenBase.getBiome(nArray[n11]);
            if (list.contains(biomeGenBase) && (blockPos == null || random.nextInt(n10 + 1) == 0)) {
                blockPos = new BlockPos(n12, 0, n13);
                ++n10;
            }
            ++n11;
        }
        return blockPos;
    }

    public BiomeGenBase getBiomeGenerator(BlockPos blockPos, BiomeGenBase biomeGenBase) {
        return this.biomeCache.func_180284_a(blockPos.getX(), blockPos.getZ(), biomeGenBase);
    }

    public float getTemperatureAtHeight(float f, int n) {
        return f;
    }

    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] biomeGenBaseArray, int n, int n2, int n3, int n4, boolean bl) {
        IntCache.resetIntCache();
        if (biomeGenBaseArray == null || biomeGenBaseArray.length < n3 * n4) {
            biomeGenBaseArray = new BiomeGenBase[n3 * n4];
        }
        if (bl && n3 == 16 && n4 == 16 && (n & 0xF) == 0 && (n2 & 0xF) == 0) {
            BiomeGenBase[] biomeGenBaseArray2 = this.biomeCache.getCachedBiomes(n, n2);
            System.arraycopy(biomeGenBaseArray2, 0, biomeGenBaseArray, 0, n3 * n4);
            return biomeGenBaseArray;
        }
        int[] nArray = this.biomeIndexLayer.getInts(n, n2, n3, n4);
        int n5 = 0;
        while (n5 < n3 * n4) {
            biomeGenBaseArray[n5] = BiomeGenBase.getBiomeFromBiomeList(nArray[n5], BiomeGenBase.field_180279_ad);
            ++n5;
        }
        return biomeGenBaseArray;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean areBiomesViable(int n, int n2, int n3, List<BiomeGenBase> list) {
        IntCache.resetIntCache();
        int n4 = n - n3 >> 2;
        int n5 = n2 - n3 >> 2;
        int n6 = n + n3 >> 2;
        int n7 = n2 + n3 >> 2;
        int n8 = n6 - n4 + 1;
        int n9 = n7 - n5 + 1;
        int[] nArray = this.genBiomes.getInts(n4, n5, n8, n9);
        try {
            int n10 = 0;
            while (true) {
                if (n10 >= n8 * n9) {
                    return true;
                }
                BiomeGenBase biomeGenBase = BiomeGenBase.getBiome(nArray[n10]);
                if (!list.contains(biomeGenBase)) {
                    return false;
                }
                ++n10;
            }
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Layer");
            crashReportCategory.addCrashSection("Layer", this.genBiomes.toString());
            crashReportCategory.addCrashSection("x", n);
            crashReportCategory.addCrashSection("z", n2);
            crashReportCategory.addCrashSection("radius", n3);
            crashReportCategory.addCrashSection("allowed", list);
            throw new ReportedException(crashReport);
        }
    }

    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomeGenBaseArray, int n, int n2, int n3, int n4) {
        IntCache.resetIntCache();
        if (biomeGenBaseArray == null || biomeGenBaseArray.length < n3 * n4) {
            biomeGenBaseArray = new BiomeGenBase[n3 * n4];
        }
        int[] nArray = this.genBiomes.getInts(n, n2, n3, n4);
        try {
            int n5 = 0;
            while (n5 < n3 * n4) {
                biomeGenBaseArray[n5] = BiomeGenBase.getBiomeFromBiomeList(nArray[n5], BiomeGenBase.field_180279_ad);
                ++n5;
            }
            return biomeGenBaseArray;
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("RawBiomeBlock");
            crashReportCategory.addCrashSection("biomes[] size", biomeGenBaseArray.length);
            crashReportCategory.addCrashSection("x", n);
            crashReportCategory.addCrashSection("z", n2);
            crashReportCategory.addCrashSection("w", n3);
            crashReportCategory.addCrashSection("h", n4);
            throw new ReportedException(crashReport);
        }
    }

    public WorldChunkManager(World world) {
        this(world.getSeed(), world.getWorldInfo().getTerrainType(), world.getWorldInfo().getGeneratorOptions());
    }
}

