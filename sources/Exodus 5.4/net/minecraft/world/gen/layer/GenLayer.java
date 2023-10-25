/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.layer;

import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.layer.GenLayerAddIsland;
import net.minecraft.world.gen.layer.GenLayerAddMushroomIsland;
import net.minecraft.world.gen.layer.GenLayerAddSnow;
import net.minecraft.world.gen.layer.GenLayerBiome;
import net.minecraft.world.gen.layer.GenLayerBiomeEdge;
import net.minecraft.world.gen.layer.GenLayerDeepOcean;
import net.minecraft.world.gen.layer.GenLayerEdge;
import net.minecraft.world.gen.layer.GenLayerFuzzyZoom;
import net.minecraft.world.gen.layer.GenLayerHills;
import net.minecraft.world.gen.layer.GenLayerIsland;
import net.minecraft.world.gen.layer.GenLayerRareBiome;
import net.minecraft.world.gen.layer.GenLayerRemoveTooMuchOcean;
import net.minecraft.world.gen.layer.GenLayerRiver;
import net.minecraft.world.gen.layer.GenLayerRiverInit;
import net.minecraft.world.gen.layer.GenLayerRiverMix;
import net.minecraft.world.gen.layer.GenLayerShore;
import net.minecraft.world.gen.layer.GenLayerSmooth;
import net.minecraft.world.gen.layer.GenLayerVoronoiZoom;
import net.minecraft.world.gen.layer.GenLayerZoom;

public abstract class GenLayer {
    protected GenLayer parent;
    protected long baseSeed;
    private long chunkSeed;
    private long worldGenSeed;

    public abstract int[] getInts(int var1, int var2, int var3, int var4);

    protected int selectRandom(int ... nArray) {
        return nArray[this.nextInt(nArray.length)];
    }

    public void initChunkSeed(long l, long l2) {
        this.chunkSeed = this.worldGenSeed;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += l;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += l2;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += l;
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += l2;
    }

    public GenLayer(long l) {
        this.baseSeed = l;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += l;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += l;
        this.baseSeed *= this.baseSeed * 6364136223846793005L + 1442695040888963407L;
        this.baseSeed += l;
    }

    protected int selectModeOrRandom(int n, int n2, int n3, int n4) {
        return n2 == n3 && n3 == n4 ? n2 : (n == n2 && n == n3 ? n : (n == n2 && n == n4 ? n : (n == n3 && n == n4 ? n : (n == n2 && n3 != n4 ? n : (n == n3 && n2 != n4 ? n : (n == n4 && n2 != n3 ? n : (n2 == n3 && n != n4 ? n2 : (n2 == n4 && n != n3 ? n2 : (n3 == n4 && n != n2 ? n3 : this.selectRandom(n, n2, n3, n4))))))))));
    }

    public void initWorldGenSeed(long l) {
        this.worldGenSeed = l;
        if (this.parent != null) {
            this.parent.initWorldGenSeed(l);
        }
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
        this.worldGenSeed *= this.worldGenSeed * 6364136223846793005L + 1442695040888963407L;
        this.worldGenSeed += this.baseSeed;
    }

    protected static boolean biomesEqualOrMesaPlateau(int n, int n2) {
        if (n == n2) {
            return true;
        }
        if (n != BiomeGenBase.mesaPlateau_F.biomeID && n != BiomeGenBase.mesaPlateau.biomeID) {
            final BiomeGenBase biomeGenBase = BiomeGenBase.getBiome(n);
            final BiomeGenBase biomeGenBase2 = BiomeGenBase.getBiome(n2);
            try {
                return biomeGenBase != null && biomeGenBase2 != null ? biomeGenBase.isEqualTo(biomeGenBase2) : false;
            }
            catch (Throwable throwable) {
                CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Comparing biomes");
                CrashReportCategory crashReportCategory = crashReport.makeCategory("Biomes being compared");
                crashReportCategory.addCrashSection("Biome A ID", n);
                crashReportCategory.addCrashSection("Biome B ID", n2);
                crashReportCategory.addCrashSectionCallable("Biome A", new Callable<String>(){

                    @Override
                    public String call() throws Exception {
                        return String.valueOf(biomeGenBase);
                    }
                });
                crashReportCategory.addCrashSectionCallable("Biome B", new Callable<String>(){

                    @Override
                    public String call() throws Exception {
                        return String.valueOf(biomeGenBase2);
                    }
                });
                throw new ReportedException(crashReport);
            }
        }
        return n2 == BiomeGenBase.mesaPlateau_F.biomeID || n2 == BiomeGenBase.mesaPlateau.biomeID;
    }

    public static GenLayer[] initializeAllBiomeGenerators(long l, WorldType worldType, String string) {
        int n;
        GenLayer genLayer = new GenLayerIsland(1L);
        genLayer = new GenLayerFuzzyZoom(2000L, genLayer);
        GenLayerAddIsland genLayerAddIsland = new GenLayerAddIsland(1L, genLayer);
        GenLayerZoom genLayerZoom = new GenLayerZoom(2001L, genLayerAddIsland);
        GenLayerAddIsland genLayerAddIsland2 = new GenLayerAddIsland(2L, genLayerZoom);
        genLayerAddIsland2 = new GenLayerAddIsland(50L, genLayerAddIsland2);
        genLayerAddIsland2 = new GenLayerAddIsland(70L, genLayerAddIsland2);
        GenLayerRemoveTooMuchOcean genLayerRemoveTooMuchOcean = new GenLayerRemoveTooMuchOcean(2L, genLayerAddIsland2);
        GenLayerAddSnow genLayerAddSnow = new GenLayerAddSnow(2L, genLayerRemoveTooMuchOcean);
        GenLayerAddIsland genLayerAddIsland3 = new GenLayerAddIsland(3L, genLayerAddSnow);
        GenLayerEdge genLayerEdge = new GenLayerEdge(2L, genLayerAddIsland3, GenLayerEdge.Mode.COOL_WARM);
        genLayerEdge = new GenLayerEdge(2L, genLayerEdge, GenLayerEdge.Mode.HEAT_ICE);
        genLayerEdge = new GenLayerEdge(3L, genLayerEdge, GenLayerEdge.Mode.SPECIAL);
        GenLayerZoom genLayerZoom2 = new GenLayerZoom(2002L, genLayerEdge);
        genLayerZoom2 = new GenLayerZoom(2003L, genLayerZoom2);
        GenLayerAddIsland genLayerAddIsland4 = new GenLayerAddIsland(4L, genLayerZoom2);
        GenLayerAddMushroomIsland genLayerAddMushroomIsland = new GenLayerAddMushroomIsland(5L, genLayerAddIsland4);
        GenLayerDeepOcean genLayerDeepOcean = new GenLayerDeepOcean(4L, genLayerAddMushroomIsland);
        GenLayer genLayer2 = GenLayerZoom.magnify(1000L, genLayerDeepOcean, 0);
        ChunkProviderSettings chunkProviderSettings = null;
        int n2 = n = 4;
        if (worldType == WorldType.CUSTOMIZED && string.length() > 0) {
            chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory(string).func_177864_b();
            n = chunkProviderSettings.biomeSize;
            n2 = chunkProviderSettings.riverSize;
        }
        if (worldType == WorldType.LARGE_BIOMES) {
            n = 6;
        }
        GenLayer genLayer3 = GenLayerZoom.magnify(1000L, genLayer2, 0);
        GenLayerRiverInit genLayerRiverInit = new GenLayerRiverInit(100L, genLayer3);
        GenLayerBiome genLayerBiome = new GenLayerBiome(200L, genLayer2, worldType, string);
        GenLayer genLayer4 = GenLayerZoom.magnify(1000L, genLayerBiome, 2);
        GenLayerBiomeEdge genLayerBiomeEdge = new GenLayerBiomeEdge(1000L, genLayer4);
        GenLayer genLayer5 = GenLayerZoom.magnify(1000L, genLayerRiverInit, 2);
        GenLayer genLayer6 = new GenLayerHills(1000L, genLayerBiomeEdge, genLayer5);
        GenLayer genLayer7 = GenLayerZoom.magnify(1000L, genLayerRiverInit, 2);
        genLayer7 = GenLayerZoom.magnify(1000L, genLayer7, n2);
        GenLayerRiver genLayerRiver = new GenLayerRiver(1L, genLayer7);
        GenLayerSmooth genLayerSmooth = new GenLayerSmooth(1000L, genLayerRiver);
        genLayer6 = new GenLayerRareBiome(1001L, genLayer6);
        int n3 = 0;
        while (n3 < n) {
            genLayer6 = new GenLayerZoom(1000 + n3, genLayer6);
            if (n3 == 0) {
                genLayer6 = new GenLayerAddIsland(3L, genLayer6);
            }
            if (n3 == 1 || n == 1) {
                genLayer6 = new GenLayerShore(1000L, genLayer6);
            }
            ++n3;
        }
        GenLayerSmooth genLayerSmooth2 = new GenLayerSmooth(1000L, genLayer6);
        GenLayerRiverMix genLayerRiverMix = new GenLayerRiverMix(100L, genLayerSmooth2, genLayerSmooth);
        GenLayerVoronoiZoom genLayerVoronoiZoom = new GenLayerVoronoiZoom(10L, genLayerRiverMix);
        genLayerRiverMix.initWorldGenSeed(l);
        genLayerVoronoiZoom.initWorldGenSeed(l);
        return new GenLayer[]{genLayerRiverMix, genLayerVoronoiZoom, genLayerRiverMix};
    }

    protected static boolean isBiomeOceanic(int n) {
        return n == BiomeGenBase.ocean.biomeID || n == BiomeGenBase.deepOcean.biomeID || n == BiomeGenBase.frozenOcean.biomeID;
    }

    protected int nextInt(int n) {
        int n2 = (int)((this.chunkSeed >> 24) % (long)n);
        if (n2 < 0) {
            n2 += n;
        }
        this.chunkSeed *= this.chunkSeed * 6364136223846793005L + 1442695040888963407L;
        this.chunkSeed += this.worldGenSeed;
        return n2;
    }
}

