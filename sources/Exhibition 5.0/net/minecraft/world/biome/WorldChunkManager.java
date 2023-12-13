// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.biome;

import java.util.Random;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.world.gen.layer.GenLayer;

public class WorldChunkManager
{
    private GenLayer genBiomes;
    private GenLayer biomeIndexLayer;
    private BiomeCache biomeCache;
    private List biomesToSpawnIn;
    private String field_180301_f;
    private static final String __OBFID = "CL_00000166";
    
    protected WorldChunkManager() {
        this.biomeCache = new BiomeCache(this);
        this.field_180301_f = "";
        (this.biomesToSpawnIn = Lists.newArrayList()).add(BiomeGenBase.forest);
        this.biomesToSpawnIn.add(BiomeGenBase.plains);
        this.biomesToSpawnIn.add(BiomeGenBase.taiga);
        this.biomesToSpawnIn.add(BiomeGenBase.taigaHills);
        this.biomesToSpawnIn.add(BiomeGenBase.forestHills);
        this.biomesToSpawnIn.add(BiomeGenBase.jungle);
        this.biomesToSpawnIn.add(BiomeGenBase.jungleHills);
    }
    
    public WorldChunkManager(final long p_i45744_1_, final WorldType p_i45744_3_, final String p_i45744_4_) {
        this();
        this.field_180301_f = p_i45744_4_;
        final GenLayer[] var5 = GenLayer.func_180781_a(p_i45744_1_, p_i45744_3_, p_i45744_4_);
        this.genBiomes = var5[0];
        this.biomeIndexLayer = var5[1];
    }
    
    public WorldChunkManager(final World worldIn) {
        this(worldIn.getSeed(), worldIn.getWorldInfo().getTerrainType(), worldIn.getWorldInfo().getGeneratorOptions());
    }
    
    public List getBiomesToSpawnIn() {
        return this.biomesToSpawnIn;
    }
    
    public BiomeGenBase func_180631_a(final BlockPos p_180631_1_) {
        return this.func_180300_a(p_180631_1_, null);
    }
    
    public BiomeGenBase func_180300_a(final BlockPos p_180300_1_, final BiomeGenBase p_180300_2_) {
        return this.biomeCache.func_180284_a(p_180300_1_.getX(), p_180300_1_.getZ(), p_180300_2_);
    }
    
    public float[] getRainfall(float[] p_76936_1_, final int p_76936_2_, final int p_76936_3_, final int p_76936_4_, final int p_76936_5_) {
        IntCache.resetIntCache();
        if (p_76936_1_ == null || p_76936_1_.length < p_76936_4_ * p_76936_5_) {
            p_76936_1_ = new float[p_76936_4_ * p_76936_5_];
        }
        final int[] var6 = this.biomeIndexLayer.getInts(p_76936_2_, p_76936_3_, p_76936_4_, p_76936_5_);
        for (int var7 = 0; var7 < p_76936_4_ * p_76936_5_; ++var7) {
            try {
                float var8 = BiomeGenBase.getBiomeFromBiomeList(var6[var7], BiomeGenBase.field_180279_ad).getIntRainfall() / 65536.0f;
                if (var8 > 1.0f) {
                    var8 = 1.0f;
                }
                p_76936_1_[var7] = var8;
            }
            catch (Throwable var10) {
                final CrashReport var9 = CrashReport.makeCrashReport(var10, "Invalid Biome id");
                final CrashReportCategory var11 = var9.makeCategory("DownfallBlock");
                var11.addCrashSection("biome id", var7);
                var11.addCrashSection("downfalls[] size", p_76936_1_.length);
                var11.addCrashSection("x", p_76936_2_);
                var11.addCrashSection("z", p_76936_3_);
                var11.addCrashSection("w", p_76936_4_);
                var11.addCrashSection("h", p_76936_5_);
                throw new ReportedException(var9);
            }
        }
        return p_76936_1_;
    }
    
    public float getTemperatureAtHeight(final float p_76939_1_, final int p_76939_2_) {
        return p_76939_1_;
    }
    
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] p_76937_1_, final int p_76937_2_, final int p_76937_3_, final int p_76937_4_, final int p_76937_5_) {
        IntCache.resetIntCache();
        if (p_76937_1_ == null || p_76937_1_.length < p_76937_4_ * p_76937_5_) {
            p_76937_1_ = new BiomeGenBase[p_76937_4_ * p_76937_5_];
        }
        final int[] var6 = this.genBiomes.getInts(p_76937_2_, p_76937_3_, p_76937_4_, p_76937_5_);
        try {
            for (int var7 = 0; var7 < p_76937_4_ * p_76937_5_; ++var7) {
                p_76937_1_[var7] = BiomeGenBase.getBiomeFromBiomeList(var6[var7], BiomeGenBase.field_180279_ad);
            }
            return p_76937_1_;
        }
        catch (Throwable var9) {
            final CrashReport var8 = CrashReport.makeCrashReport(var9, "Invalid Biome id");
            final CrashReportCategory var10 = var8.makeCategory("RawBiomeBlock");
            var10.addCrashSection("biomes[] size", p_76937_1_.length);
            var10.addCrashSection("x", p_76937_2_);
            var10.addCrashSection("z", p_76937_3_);
            var10.addCrashSection("w", p_76937_4_);
            var10.addCrashSection("h", p_76937_5_);
            throw new ReportedException(var8);
        }
    }
    
    public BiomeGenBase[] loadBlockGeneratorData(final BiomeGenBase[] p_76933_1_, final int p_76933_2_, final int p_76933_3_, final int p_76933_4_, final int p_76933_5_) {
        return this.getBiomeGenAt(p_76933_1_, p_76933_2_, p_76933_3_, p_76933_4_, p_76933_5_, true);
    }
    
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] p_76931_1_, final int p_76931_2_, final int p_76931_3_, final int p_76931_4_, final int p_76931_5_, final boolean p_76931_6_) {
        IntCache.resetIntCache();
        if (p_76931_1_ == null || p_76931_1_.length < p_76931_4_ * p_76931_5_) {
            p_76931_1_ = new BiomeGenBase[p_76931_4_ * p_76931_5_];
        }
        if (p_76931_6_ && p_76931_4_ == 16 && p_76931_5_ == 16 && (p_76931_2_ & 0xF) == 0x0 && (p_76931_3_ & 0xF) == 0x0) {
            final BiomeGenBase[] var9 = this.biomeCache.getCachedBiomes(p_76931_2_, p_76931_3_);
            System.arraycopy(var9, 0, p_76931_1_, 0, p_76931_4_ * p_76931_5_);
            return p_76931_1_;
        }
        final int[] var10 = this.biomeIndexLayer.getInts(p_76931_2_, p_76931_3_, p_76931_4_, p_76931_5_);
        for (int var11 = 0; var11 < p_76931_4_ * p_76931_5_; ++var11) {
            p_76931_1_[var11] = BiomeGenBase.getBiomeFromBiomeList(var10[var11], BiomeGenBase.field_180279_ad);
        }
        return p_76931_1_;
    }
    
    public boolean areBiomesViable(final int p_76940_1_, final int p_76940_2_, final int p_76940_3_, final List p_76940_4_) {
        IntCache.resetIntCache();
        final int var5 = p_76940_1_ - p_76940_3_ >> 2;
        final int var6 = p_76940_2_ - p_76940_3_ >> 2;
        final int var7 = p_76940_1_ + p_76940_3_ >> 2;
        final int var8 = p_76940_2_ + p_76940_3_ >> 2;
        final int var9 = var7 - var5 + 1;
        final int var10 = var8 - var6 + 1;
        final int[] var11 = this.genBiomes.getInts(var5, var6, var9, var10);
        try {
            for (int var12 = 0; var12 < var9 * var10; ++var12) {
                final BiomeGenBase var13 = BiomeGenBase.getBiome(var11[var12]);
                if (!p_76940_4_.contains(var13)) {
                    return false;
                }
            }
            return true;
        }
        catch (Throwable var15) {
            final CrashReport var14 = CrashReport.makeCrashReport(var15, "Invalid Biome id");
            final CrashReportCategory var16 = var14.makeCategory("Layer");
            var16.addCrashSection("Layer", this.genBiomes.toString());
            var16.addCrashSection("x", p_76940_1_);
            var16.addCrashSection("z", p_76940_2_);
            var16.addCrashSection("radius", p_76940_3_);
            var16.addCrashSection("allowed", p_76940_4_);
            throw new ReportedException(var14);
        }
    }
    
    public BlockPos findBiomePosition(final int x, final int z, final int range, final List biomes, final Random random) {
        IntCache.resetIntCache();
        final int var6 = x - range >> 2;
        final int var7 = z - range >> 2;
        final int var8 = x + range >> 2;
        final int var9 = z + range >> 2;
        final int var10 = var8 - var6 + 1;
        final int var11 = var9 - var7 + 1;
        final int[] var12 = this.genBiomes.getInts(var6, var7, var10, var11);
        BlockPos var13 = null;
        int var14 = 0;
        for (int var15 = 0; var15 < var10 * var11; ++var15) {
            final int var16 = var6 + var15 % var10 << 2;
            final int var17 = var7 + var15 / var10 << 2;
            final BiomeGenBase var18 = BiomeGenBase.getBiome(var12[var15]);
            if (biomes.contains(var18) && (var13 == null || random.nextInt(var14 + 1) == 0)) {
                var13 = new BlockPos(var16, 0, var17);
                ++var14;
            }
        }
        return var13;
    }
    
    public void cleanupCache() {
        this.biomeCache.cleanupCache();
    }
}
