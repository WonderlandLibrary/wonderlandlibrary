// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.WorldType;
import com.google.common.collect.Lists;
import net.minecraft.init.Biomes;
import java.util.List;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.ChunkGeneratorSettings;

public class BiomeProvider
{
    private ChunkGeneratorSettings settings;
    private GenLayer genBiomes;
    private GenLayer biomeIndexLayer;
    private final BiomeCache biomeCache;
    private final List<Biome> biomesToSpawnIn;
    
    protected BiomeProvider() {
        this.biomeCache = new BiomeCache(this);
        this.biomesToSpawnIn = (List<Biome>)Lists.newArrayList((Object[])new Biome[] { Biomes.FOREST, Biomes.PLAINS, Biomes.TAIGA, Biomes.TAIGA_HILLS, Biomes.FOREST_HILLS, Biomes.JUNGLE, Biomes.JUNGLE_HILLS });
    }
    
    private BiomeProvider(final long seed, final WorldType worldTypeIn, final String options) {
        this();
        if (worldTypeIn == WorldType.CUSTOMIZED && !options.isEmpty()) {
            this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(options).build();
        }
        final GenLayer[] agenlayer = GenLayer.initializeAllBiomeGenerators(seed, worldTypeIn, this.settings);
        this.genBiomes = agenlayer[0];
        this.biomeIndexLayer = agenlayer[1];
    }
    
    public BiomeProvider(final WorldInfo info) {
        this(info.getSeed(), info.getTerrainType(), info.getGeneratorOptions());
    }
    
    public List<Biome> getBiomesToSpawnIn() {
        return this.biomesToSpawnIn;
    }
    
    public Biome getBiome(final BlockPos pos) {
        return this.getBiome(pos, null);
    }
    
    public Biome getBiome(final BlockPos pos, final Biome defaultBiome) {
        return this.biomeCache.getBiome(pos.getX(), pos.getZ(), defaultBiome);
    }
    
    public float getTemperatureAtHeight(final float p_76939_1_, final int p_76939_2_) {
        return p_76939_1_;
    }
    
    public Biome[] getBiomesForGeneration(Biome[] biomes, final int x, final int z, final int width, final int height) {
        IntCache.resetIntCache();
        if (biomes == null || biomes.length < width * height) {
            biomes = new Biome[width * height];
        }
        final int[] aint = this.genBiomes.getInts(x, z, width, height);
        try {
            for (int i = 0; i < width * height; ++i) {
                biomes[i] = Biome.getBiome(aint[i], Biomes.DEFAULT);
            }
            return biomes;
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("RawBiomeBlock");
            crashreportcategory.addCrashSection("biomes[] size", biomes.length);
            crashreportcategory.addCrashSection("x", x);
            crashreportcategory.addCrashSection("z", z);
            crashreportcategory.addCrashSection("w", width);
            crashreportcategory.addCrashSection("h", height);
            throw new ReportedException(crashreport);
        }
    }
    
    public Biome[] getBiomes(@Nullable final Biome[] oldBiomeList, final int x, final int z, final int width, final int depth) {
        return this.getBiomes(oldBiomeList, x, z, width, depth, true);
    }
    
    public Biome[] getBiomes(@Nullable Biome[] listToReuse, final int x, final int z, final int width, final int length, final boolean cacheFlag) {
        IntCache.resetIntCache();
        if (listToReuse == null || listToReuse.length < width * length) {
            listToReuse = new Biome[width * length];
        }
        if (cacheFlag && width == 16 && length == 16 && (x & 0xF) == 0x0 && (z & 0xF) == 0x0) {
            final Biome[] abiome = this.biomeCache.getCachedBiomes(x, z);
            System.arraycopy(abiome, 0, listToReuse, 0, width * length);
            return listToReuse;
        }
        final int[] aint = this.biomeIndexLayer.getInts(x, z, width, length);
        for (int i = 0; i < width * length; ++i) {
            listToReuse[i] = Biome.getBiome(aint[i], Biomes.DEFAULT);
        }
        return listToReuse;
    }
    
    public boolean areBiomesViable(final int x, final int z, final int radius, final List<Biome> allowed) {
        IntCache.resetIntCache();
        final int i = x - radius >> 2;
        final int j = z - radius >> 2;
        final int k = x + radius >> 2;
        final int l = z + radius >> 2;
        final int i2 = k - i + 1;
        final int j2 = l - j + 1;
        final int[] aint = this.genBiomes.getInts(i, j, i2, j2);
        try {
            for (int k2 = 0; k2 < i2 * j2; ++k2) {
                final Biome biome = Biome.getBiome(aint[k2]);
                if (!allowed.contains(biome)) {
                    return false;
                }
            }
            return true;
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Invalid Biome id");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Layer");
            crashreportcategory.addCrashSection("Layer", this.genBiomes.toString());
            crashreportcategory.addCrashSection("x", x);
            crashreportcategory.addCrashSection("z", z);
            crashreportcategory.addCrashSection("radius", radius);
            crashreportcategory.addCrashSection("allowed", allowed);
            throw new ReportedException(crashreport);
        }
    }
    
    @Nullable
    public BlockPos findBiomePosition(final int x, final int z, final int range, final List<Biome> biomes, final Random random) {
        IntCache.resetIntCache();
        final int i = x - range >> 2;
        final int j = z - range >> 2;
        final int k = x + range >> 2;
        final int l = z + range >> 2;
        final int i2 = k - i + 1;
        final int j2 = l - j + 1;
        final int[] aint = this.genBiomes.getInts(i, j, i2, j2);
        BlockPos blockpos = null;
        int k2 = 0;
        for (int l2 = 0; l2 < i2 * j2; ++l2) {
            final int i3 = i + l2 % i2 << 2;
            final int j3 = j + l2 / i2 << 2;
            final Biome biome = Biome.getBiome(aint[l2]);
            if (biomes.contains(biome) && (blockpos == null || random.nextInt(k2 + 1) == 0)) {
                blockpos = new BlockPos(i3, 0, j3);
                ++k2;
            }
        }
        return blockpos;
    }
    
    public void cleanupCache() {
        this.biomeCache.cleanupCache();
    }
    
    public boolean isFixedBiome() {
        return this.settings != null && this.settings.fixedBiome >= 0;
    }
    
    public Biome getFixedBiome() {
        return (this.settings != null && this.settings.fixedBiome >= 0) ? Biome.getBiomeForId(this.settings.fixedBiome) : null;
    }
}
