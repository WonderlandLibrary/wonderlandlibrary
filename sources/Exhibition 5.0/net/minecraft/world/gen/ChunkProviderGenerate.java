// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockFalling;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.util.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.block.Block;
import net.minecraft.world.WorldType;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderGenerate implements IChunkProvider
{
    private Random rand;
    private NoiseGeneratorOctaves field_147431_j;
    private NoiseGeneratorOctaves field_147432_k;
    private NoiseGeneratorOctaves field_147429_l;
    private NoiseGeneratorPerlin field_147430_m;
    public NoiseGeneratorOctaves noiseGen5;
    public NoiseGeneratorOctaves noiseGen6;
    public NoiseGeneratorOctaves mobSpawnerNoise;
    private World worldObj;
    private final boolean mapFeaturesEnabled;
    private WorldType field_177475_o;
    private final double[] field_147434_q;
    private final float[] parabolicField;
    private ChunkProviderSettings field_177477_r;
    private Block field_177476_s;
    private double[] stoneNoise;
    private MapGenBase caveGenerator;
    private MapGenStronghold strongholdGenerator;
    private MapGenVillage villageGenerator;
    private MapGenMineshaft mineshaftGenerator;
    private MapGenScatteredFeature scatteredFeatureGenerator;
    private MapGenBase ravineGenerator;
    private StructureOceanMonument field_177474_A;
    private BiomeGenBase[] biomesForGeneration;
    double[] field_147427_d;
    double[] field_147428_e;
    double[] field_147425_f;
    double[] field_147426_g;
    private static final String __OBFID = "CL_00000396";
    
    public ChunkProviderGenerate(final World worldIn, final long p_i45636_2_, final boolean p_i45636_4_, final String p_i45636_5_) {
        this.field_177476_s = Blocks.water;
        this.stoneNoise = new double[256];
        this.caveGenerator = new MapGenCaves();
        this.strongholdGenerator = new MapGenStronghold();
        this.villageGenerator = new MapGenVillage();
        this.mineshaftGenerator = new MapGenMineshaft();
        this.scatteredFeatureGenerator = new MapGenScatteredFeature();
        this.ravineGenerator = new MapGenRavine();
        this.field_177474_A = new StructureOceanMonument();
        this.worldObj = worldIn;
        this.mapFeaturesEnabled = p_i45636_4_;
        this.field_177475_o = worldIn.getWorldInfo().getTerrainType();
        this.rand = new Random(p_i45636_2_);
        this.field_147431_j = new NoiseGeneratorOctaves(this.rand, 16);
        this.field_147432_k = new NoiseGeneratorOctaves(this.rand, 16);
        this.field_147429_l = new NoiseGeneratorOctaves(this.rand, 8);
        this.field_147430_m = new NoiseGeneratorPerlin(this.rand, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.field_147434_q = new double[825];
        this.parabolicField = new float[25];
        for (int var6 = -2; var6 <= 2; ++var6) {
            for (int var7 = -2; var7 <= 2; ++var7) {
                final float var8 = 10.0f / MathHelper.sqrt_float(var6 * var6 + var7 * var7 + 0.2f);
                this.parabolicField[var6 + 2 + (var7 + 2) * 5] = var8;
            }
        }
        if (p_i45636_5_ != null) {
            this.field_177477_r = ChunkProviderSettings.Factory.createFactory(p_i45636_5_).func_177864_b();
            this.field_177476_s = (this.field_177477_r.field_177778_E ? Blocks.lava : Blocks.water);
        }
    }
    
    public void func_180518_a(final int p_180518_1_, final int p_180518_2_, final ChunkPrimer p_180518_3_) {
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, p_180518_1_ * 4 - 2, p_180518_2_ * 4 - 2, 10, 10);
        this.func_147423_a(p_180518_1_ * 4, 0, p_180518_2_ * 4);
        for (int var4 = 0; var4 < 4; ++var4) {
            final int var5 = var4 * 5;
            final int var6 = (var4 + 1) * 5;
            for (int var7 = 0; var7 < 4; ++var7) {
                final int var8 = (var5 + var7) * 33;
                final int var9 = (var5 + var7 + 1) * 33;
                final int var10 = (var6 + var7) * 33;
                final int var11 = (var6 + var7 + 1) * 33;
                for (int var12 = 0; var12 < 32; ++var12) {
                    final double var13 = 0.125;
                    double var14 = this.field_147434_q[var8 + var12];
                    double var15 = this.field_147434_q[var9 + var12];
                    double var16 = this.field_147434_q[var10 + var12];
                    double var17 = this.field_147434_q[var11 + var12];
                    final double var18 = (this.field_147434_q[var8 + var12 + 1] - var14) * var13;
                    final double var19 = (this.field_147434_q[var9 + var12 + 1] - var15) * var13;
                    final double var20 = (this.field_147434_q[var10 + var12 + 1] - var16) * var13;
                    final double var21 = (this.field_147434_q[var11 + var12 + 1] - var17) * var13;
                    for (int var22 = 0; var22 < 8; ++var22) {
                        final double var23 = 0.25;
                        double var24 = var14;
                        double var25 = var15;
                        final double var26 = (var16 - var14) * var23;
                        final double var27 = (var17 - var15) * var23;
                        for (int var28 = 0; var28 < 4; ++var28) {
                            final double var29 = 0.25;
                            final double var30 = (var25 - var24) * var29;
                            double var31 = var24 - var30;
                            for (int var32 = 0; var32 < 4; ++var32) {
                                if ((var31 += var30) > 0.0) {
                                    p_180518_3_.setBlockState(var4 * 4 + var28, var12 * 8 + var22, var7 * 4 + var32, Blocks.stone.getDefaultState());
                                }
                                else if (var12 * 8 + var22 < this.field_177477_r.field_177841_q) {
                                    p_180518_3_.setBlockState(var4 * 4 + var28, var12 * 8 + var22, var7 * 4 + var32, this.field_177476_s.getDefaultState());
                                }
                            }
                            var24 += var26;
                            var25 += var27;
                        }
                        var14 += var18;
                        var15 += var19;
                        var16 += var20;
                        var17 += var21;
                    }
                }
            }
        }
    }
    
    public void func_180517_a(final int p_180517_1_, final int p_180517_2_, final ChunkPrimer p_180517_3_, final BiomeGenBase[] p_180517_4_) {
        final double var5 = 0.03125;
        this.stoneNoise = this.field_147430_m.func_151599_a(this.stoneNoise, p_180517_1_ * 16, p_180517_2_ * 16, 16, 16, var5 * 2.0, var5 * 2.0, 1.0);
        for (int var6 = 0; var6 < 16; ++var6) {
            for (int var7 = 0; var7 < 16; ++var7) {
                final BiomeGenBase var8 = p_180517_4_[var7 + var6 * 16];
                var8.genTerrainBlocks(this.worldObj, this.rand, p_180517_3_, p_180517_1_ * 16 + var6, p_180517_2_ * 16 + var7, this.stoneNoise[var7 + var6 * 16]);
            }
        }
    }
    
    @Override
    public Chunk provideChunk(final int p_73154_1_, final int p_73154_2_) {
        this.rand.setSeed(p_73154_1_ * 341873128712L + p_73154_2_ * 132897987541L);
        final ChunkPrimer var3 = new ChunkPrimer();
        this.func_180518_a(p_73154_1_, p_73154_2_, var3);
        this.func_180517_a(p_73154_1_, p_73154_2_, var3, this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16));
        if (this.field_177477_r.field_177839_r) {
            this.caveGenerator.func_175792_a(this, this.worldObj, p_73154_1_, p_73154_2_, var3);
        }
        if (this.field_177477_r.field_177850_z) {
            this.ravineGenerator.func_175792_a(this, this.worldObj, p_73154_1_, p_73154_2_, var3);
        }
        if (this.field_177477_r.field_177829_w && this.mapFeaturesEnabled) {
            this.mineshaftGenerator.func_175792_a(this, this.worldObj, p_73154_1_, p_73154_2_, var3);
        }
        if (this.field_177477_r.field_177831_v && this.mapFeaturesEnabled) {
            this.villageGenerator.func_175792_a(this, this.worldObj, p_73154_1_, p_73154_2_, var3);
        }
        if (this.field_177477_r.field_177833_u && this.mapFeaturesEnabled) {
            this.strongholdGenerator.func_175792_a(this, this.worldObj, p_73154_1_, p_73154_2_, var3);
        }
        if (this.field_177477_r.field_177854_x && this.mapFeaturesEnabled) {
            this.scatteredFeatureGenerator.func_175792_a(this, this.worldObj, p_73154_1_, p_73154_2_, var3);
        }
        if (this.field_177477_r.field_177852_y && this.mapFeaturesEnabled) {
            this.field_177474_A.func_175792_a(this, this.worldObj, p_73154_1_, p_73154_2_, var3);
        }
        final Chunk var4 = new Chunk(this.worldObj, var3, p_73154_1_, p_73154_2_);
        final byte[] var5 = var4.getBiomeArray();
        for (int var6 = 0; var6 < var5.length; ++var6) {
            var5[var6] = (byte)this.biomesForGeneration[var6].biomeID;
        }
        var4.generateSkylightMap();
        return var4;
    }
    
    private void func_147423_a(final int p_147423_1_, final int p_147423_2_, final int p_147423_3_) {
        this.field_147426_g = this.noiseGen6.generateNoiseOctaves(this.field_147426_g, p_147423_1_, p_147423_3_, 5, 5, this.field_177477_r.field_177808_e, this.field_177477_r.field_177803_f, this.field_177477_r.field_177804_g);
        final float var4 = this.field_177477_r.field_177811_a;
        final float var5 = this.field_177477_r.field_177809_b;
        this.field_147427_d = this.field_147429_l.generateNoiseOctaves(this.field_147427_d, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, var4 / this.field_177477_r.field_177825_h, var5 / this.field_177477_r.field_177827_i, var4 / this.field_177477_r.field_177821_j);
        this.field_147428_e = this.field_147431_j.generateNoiseOctaves(this.field_147428_e, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, var4, var5, var4);
        this.field_147425_f = this.field_147432_k.generateNoiseOctaves(this.field_147425_f, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, var4, var5, var4);
        final boolean var6 = false;
        final boolean var7 = false;
        int var8 = 0;
        int var9 = 0;
        for (int var10 = 0; var10 < 5; ++var10) {
            for (int var11 = 0; var11 < 5; ++var11) {
                float var12 = 0.0f;
                float var13 = 0.0f;
                float var14 = 0.0f;
                final byte var15 = 2;
                final BiomeGenBase var16 = this.biomesForGeneration[var10 + 2 + (var11 + 2) * 10];
                for (int var17 = -var15; var17 <= var15; ++var17) {
                    for (int var18 = -var15; var18 <= var15; ++var18) {
                        final BiomeGenBase var19 = this.biomesForGeneration[var10 + var17 + 2 + (var11 + var18 + 2) * 10];
                        float var20 = this.field_177477_r.field_177813_n + var19.minHeight * this.field_177477_r.field_177819_m;
                        float var21 = this.field_177477_r.field_177843_p + var19.maxHeight * this.field_177477_r.field_177815_o;
                        if (this.field_177475_o == WorldType.AMPLIFIED && var20 > 0.0f) {
                            var20 = 1.0f + var20 * 2.0f;
                            var21 = 1.0f + var21 * 4.0f;
                        }
                        float var22 = this.parabolicField[var17 + 2 + (var18 + 2) * 5] / (var20 + 2.0f);
                        if (var19.minHeight > var16.minHeight) {
                            var22 /= 2.0f;
                        }
                        var12 += var21 * var22;
                        var13 += var20 * var22;
                        var14 += var22;
                    }
                }
                var12 /= var14;
                var13 /= var14;
                var12 = var12 * 0.9f + 0.1f;
                var13 = (var13 * 4.0f - 1.0f) / 8.0f;
                double var23 = this.field_147426_g[var9] / 8000.0;
                if (var23 < 0.0) {
                    var23 = -var23 * 0.3;
                }
                var23 = var23 * 3.0 - 2.0;
                if (var23 < 0.0) {
                    var23 /= 2.0;
                    if (var23 < -1.0) {
                        var23 = -1.0;
                    }
                    var23 /= 1.4;
                    var23 /= 2.0;
                }
                else {
                    if (var23 > 1.0) {
                        var23 = 1.0;
                    }
                    var23 /= 8.0;
                }
                ++var9;
                double var24 = var13;
                final double var25 = var12;
                var24 += var23 * 0.2;
                var24 = var24 * this.field_177477_r.field_177823_k / 8.0;
                final double var26 = this.field_177477_r.field_177823_k + var24 * 4.0;
                for (int var27 = 0; var27 < 33; ++var27) {
                    double var28 = (var27 - var26) * this.field_177477_r.field_177817_l * 128.0 / 256.0 / var25;
                    if (var28 < 0.0) {
                        var28 *= 4.0;
                    }
                    final double var29 = this.field_147428_e[var8] / this.field_177477_r.field_177806_d;
                    final double var30 = this.field_147425_f[var8] / this.field_177477_r.field_177810_c;
                    final double var31 = (this.field_147427_d[var8] / 10.0 + 1.0) / 2.0;
                    double var32 = MathHelper.denormalizeClamp(var29, var30, var31) - var28;
                    if (var27 > 29) {
                        final double var33 = (var27 - 29) / 3.0f;
                        var32 = var32 * (1.0 - var33) + -10.0 * var33;
                    }
                    this.field_147434_q[var8] = var32;
                    ++var8;
                }
            }
        }
    }
    
    @Override
    public boolean chunkExists(final int p_73149_1_, final int p_73149_2_) {
        return true;
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
        BlockFalling.fallInstantly = true;
        final int var4 = p_73153_2_ * 16;
        final int var5 = p_73153_3_ * 16;
        BlockPos var6 = new BlockPos(var4, 0, var5);
        final BiomeGenBase var7 = this.worldObj.getBiomeGenForCoords(var6.add(16, 0, 16));
        this.rand.setSeed(this.worldObj.getSeed());
        final long var8 = this.rand.nextLong() / 2L * 2L + 1L;
        final long var9 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(p_73153_2_ * var8 + p_73153_3_ * var9 ^ this.worldObj.getSeed());
        boolean var10 = false;
        final ChunkCoordIntPair var11 = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);
        if (this.field_177477_r.field_177829_w && this.mapFeaturesEnabled) {
            this.mineshaftGenerator.func_175794_a(this.worldObj, this.rand, var11);
        }
        if (this.field_177477_r.field_177831_v && this.mapFeaturesEnabled) {
            var10 = this.villageGenerator.func_175794_a(this.worldObj, this.rand, var11);
        }
        if (this.field_177477_r.field_177833_u && this.mapFeaturesEnabled) {
            this.strongholdGenerator.func_175794_a(this.worldObj, this.rand, var11);
        }
        if (this.field_177477_r.field_177854_x && this.mapFeaturesEnabled) {
            this.scatteredFeatureGenerator.func_175794_a(this.worldObj, this.rand, var11);
        }
        if (this.field_177477_r.field_177852_y && this.mapFeaturesEnabled) {
            this.field_177474_A.func_175794_a(this.worldObj, this.rand, var11);
        }
        if (var7 != BiomeGenBase.desert && var7 != BiomeGenBase.desertHills && this.field_177477_r.field_177781_A && !var10 && this.rand.nextInt(this.field_177477_r.field_177782_B) == 0) {
            final int var12 = this.rand.nextInt(16) + 8;
            final int var13 = this.rand.nextInt(256);
            final int var14 = this.rand.nextInt(16) + 8;
            new WorldGenLakes(Blocks.water).generate(this.worldObj, this.rand, var6.add(var12, var13, var14));
        }
        if (!var10 && this.rand.nextInt(this.field_177477_r.field_177777_D / 10) == 0 && this.field_177477_r.field_177783_C) {
            final int var12 = this.rand.nextInt(16) + 8;
            final int var13 = this.rand.nextInt(this.rand.nextInt(248) + 8);
            final int var14 = this.rand.nextInt(16) + 8;
            if (var13 < 63 || this.rand.nextInt(this.field_177477_r.field_177777_D / 8) == 0) {
                new WorldGenLakes(Blocks.lava).generate(this.worldObj, this.rand, var6.add(var12, var13, var14));
            }
        }
        if (this.field_177477_r.field_177837_s) {
            for (int var12 = 0; var12 < this.field_177477_r.field_177835_t; ++var12) {
                final int var13 = this.rand.nextInt(16) + 8;
                final int var14 = this.rand.nextInt(256);
                final int var15 = this.rand.nextInt(16) + 8;
                new WorldGenDungeons().generate(this.worldObj, this.rand, var6.add(var13, var14, var15));
            }
        }
        var7.func_180624_a(this.worldObj, this.rand, new BlockPos(var4, 0, var5));
        SpawnerAnimals.performWorldGenSpawning(this.worldObj, var7, var4 + 8, var5 + 8, 16, 16, this.rand);
        var6 = var6.add(8, 0, 8);
        for (int var12 = 0; var12 < 16; ++var12) {
            for (int var13 = 0; var13 < 16; ++var13) {
                final BlockPos var16 = this.worldObj.func_175725_q(var6.add(var12, 0, var13));
                final BlockPos var17 = var16.offsetDown();
                if (this.worldObj.func_175675_v(var17)) {
                    this.worldObj.setBlockState(var17, Blocks.ice.getDefaultState(), 2);
                }
                if (this.worldObj.func_175708_f(var16, true)) {
                    this.worldObj.setBlockState(var16, Blocks.snow_layer.getDefaultState(), 2);
                }
            }
        }
        BlockFalling.fallInstantly = false;
    }
    
    @Override
    public boolean func_177460_a(final IChunkProvider p_177460_1_, final Chunk p_177460_2_, final int p_177460_3_, final int p_177460_4_) {
        boolean var5 = false;
        if (this.field_177477_r.field_177852_y && this.mapFeaturesEnabled && p_177460_2_.getInhabitedTime() < 3600L) {
            var5 |= this.field_177474_A.func_175794_a(this.worldObj, this.rand, new ChunkCoordIntPair(p_177460_3_, p_177460_4_));
        }
        return var5;
    }
    
    @Override
    public boolean saveChunks(final boolean p_73151_1_, final IProgressUpdate p_73151_2_) {
        return true;
    }
    
    @Override
    public void saveExtraData() {
    }
    
    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }
    
    @Override
    public boolean canSave() {
        return true;
    }
    
    @Override
    public String makeString() {
        return "RandomLevelSource";
    }
    
    @Override
    public List func_177458_a(final EnumCreatureType p_177458_1_, final BlockPos p_177458_2_) {
        final BiomeGenBase var3 = this.worldObj.getBiomeGenForCoords(p_177458_2_);
        if (this.mapFeaturesEnabled) {
            if (p_177458_1_ == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.func_175798_a(p_177458_2_)) {
                return this.scatteredFeatureGenerator.getScatteredFeatureSpawnList();
            }
            if (p_177458_1_ == EnumCreatureType.MONSTER && this.field_177477_r.field_177852_y && this.field_177474_A.func_175796_a(this.worldObj, p_177458_2_)) {
                return this.field_177474_A.func_175799_b();
            }
        }
        return var3.getSpawnableList(p_177458_1_);
    }
    
    @Override
    public BlockPos func_180513_a(final World worldIn, final String p_180513_2_, final BlockPos p_180513_3_) {
        return ("Stronghold".equals(p_180513_2_) && this.strongholdGenerator != null) ? this.strongholdGenerator.func_180706_b(worldIn, p_180513_3_) : null;
    }
    
    @Override
    public int getLoadedChunkCount() {
        return 0;
    }
    
    @Override
    public void func_180514_a(final Chunk p_180514_1_, final int p_180514_2_, final int p_180514_3_) {
        if (this.field_177477_r.field_177829_w && this.mapFeaturesEnabled) {
            this.mineshaftGenerator.func_175792_a(this, this.worldObj, p_180514_2_, p_180514_3_, null);
        }
        if (this.field_177477_r.field_177831_v && this.mapFeaturesEnabled) {
            this.villageGenerator.func_175792_a(this, this.worldObj, p_180514_2_, p_180514_3_, null);
        }
        if (this.field_177477_r.field_177833_u && this.mapFeaturesEnabled) {
            this.strongholdGenerator.func_175792_a(this, this.worldObj, p_180514_2_, p_180514_3_, null);
        }
        if (this.field_177477_r.field_177854_x && this.mapFeaturesEnabled) {
            this.scatteredFeatureGenerator.func_175792_a(this, this.worldObj, p_180514_2_, p_180514_3_, null);
        }
        if (this.field_177477_r.field_177852_y && this.mapFeaturesEnabled) {
            this.field_177474_A.func_175792_a(this, this.worldObj, p_180514_2_, p_180514_3_, null);
        }
    }
    
    @Override
    public Chunk func_177459_a(final BlockPos p_177459_1_) {
        return this.provideChunk(p_177459_1_.getX() >> 4, p_177459_1_.getZ() >> 4);
    }
}
