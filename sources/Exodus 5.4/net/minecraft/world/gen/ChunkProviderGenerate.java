/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderSettings;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCaves;
import net.minecraft.world.gen.MapGenRavine;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureOceanMonument;

public class ChunkProviderGenerate
implements IChunkProvider {
    private double[] stoneNoise;
    public NoiseGeneratorOctaves mobSpawnerNoise;
    public NoiseGeneratorOctaves noiseGen6;
    private MapGenMineshaft mineshaftGenerator;
    private StructureOceanMonument oceanMonumentGenerator;
    private WorldType field_177475_o;
    private NoiseGeneratorPerlin field_147430_m;
    private final double[] field_147434_q;
    private Block field_177476_s = Blocks.water;
    private MapGenBase ravineGenerator;
    private World worldObj;
    private ChunkProviderSettings settings;
    double[] field_147428_e;
    private MapGenStronghold strongholdGenerator;
    private MapGenVillage villageGenerator;
    private final boolean mapFeaturesEnabled;
    public NoiseGeneratorOctaves noiseGen5;
    private NoiseGeneratorOctaves field_147432_k;
    private MapGenScatteredFeature scatteredFeatureGenerator;
    private NoiseGeneratorOctaves field_147431_j;
    private final float[] parabolicField;
    private MapGenBase caveGenerator;
    double[] field_147425_f;
    private NoiseGeneratorOctaves field_147429_l;
    double[] field_147426_g;
    double[] field_147427_d;
    private BiomeGenBase[] biomesForGeneration;
    private Random rand;

    @Override
    public boolean saveChunks(boolean bl, IProgressUpdate iProgressUpdate) {
        return true;
    }

    @Override
    public Chunk provideChunk(BlockPos blockPos) {
        return this.provideChunk(blockPos.getX() >> 4, blockPos.getZ() >> 4);
    }

    @Override
    public void populate(IChunkProvider iChunkProvider, int n, int n2) {
        int n3;
        int n4;
        int n5;
        BlockFalling.fallInstantly = true;
        int n6 = n * 16;
        int n7 = n2 * 16;
        BlockPos blockPos = new BlockPos(n6, 0, n7);
        BiomeGenBase biomeGenBase = this.worldObj.getBiomeGenForCoords(blockPos.add(16, 0, 16));
        this.rand.setSeed(this.worldObj.getSeed());
        long l = this.rand.nextLong() / 2L * 2L + 1L;
        long l2 = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed((long)n * l + (long)n2 * l2 ^ this.worldObj.getSeed());
        boolean bl = false;
        ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair(n, n2);
        if (this.settings.useMineShafts && this.mapFeaturesEnabled) {
            this.mineshaftGenerator.generateStructure(this.worldObj, this.rand, chunkCoordIntPair);
        }
        if (this.settings.useVillages && this.mapFeaturesEnabled) {
            bl = this.villageGenerator.generateStructure(this.worldObj, this.rand, chunkCoordIntPair);
        }
        if (this.settings.useStrongholds && this.mapFeaturesEnabled) {
            this.strongholdGenerator.generateStructure(this.worldObj, this.rand, chunkCoordIntPair);
        }
        if (this.settings.useTemples && this.mapFeaturesEnabled) {
            this.scatteredFeatureGenerator.generateStructure(this.worldObj, this.rand, chunkCoordIntPair);
        }
        if (this.settings.useMonuments && this.mapFeaturesEnabled) {
            this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, chunkCoordIntPair);
        }
        if (biomeGenBase != BiomeGenBase.desert && biomeGenBase != BiomeGenBase.desertHills && this.settings.useWaterLakes && !bl && this.rand.nextInt(this.settings.waterLakeChance) == 0) {
            n5 = this.rand.nextInt(16) + 8;
            n4 = this.rand.nextInt(256);
            n3 = this.rand.nextInt(16) + 8;
            new WorldGenLakes(Blocks.water).generate(this.worldObj, this.rand, blockPos.add(n5, n4, n3));
        }
        if (!bl && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes) {
            n5 = this.rand.nextInt(16) + 8;
            n4 = this.rand.nextInt(this.rand.nextInt(248) + 8);
            n3 = this.rand.nextInt(16) + 8;
            if (n4 < this.worldObj.func_181545_F() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0) {
                new WorldGenLakes(Blocks.lava).generate(this.worldObj, this.rand, blockPos.add(n5, n4, n3));
            }
        }
        if (this.settings.useDungeons) {
            n5 = 0;
            while (n5 < this.settings.dungeonChance) {
                n4 = this.rand.nextInt(16) + 8;
                n3 = this.rand.nextInt(256);
                int n8 = this.rand.nextInt(16) + 8;
                new WorldGenDungeons().generate(this.worldObj, this.rand, blockPos.add(n4, n3, n8));
                ++n5;
            }
        }
        biomeGenBase.decorate(this.worldObj, this.rand, new BlockPos(n6, 0, n7));
        SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomeGenBase, n6 + 8, n7 + 8, 16, 16, this.rand);
        blockPos = blockPos.add(8, 0, 8);
        n5 = 0;
        while (n5 < 16) {
            n4 = 0;
            while (n4 < 16) {
                BlockPos blockPos2 = this.worldObj.getPrecipitationHeight(blockPos.add(n5, 0, n4));
                BlockPos blockPos3 = blockPos2.down();
                if (this.worldObj.canBlockFreezeWater(blockPos3)) {
                    this.worldObj.setBlockState(blockPos3, Blocks.ice.getDefaultState(), 2);
                }
                if (this.worldObj.canSnowAt(blockPos2, true)) {
                    this.worldObj.setBlockState(blockPos2, Blocks.snow_layer.getDefaultState(), 2);
                }
                ++n4;
            }
            ++n5;
        }
        BlockFalling.fallInstantly = false;
    }

    public void replaceBlocksForBiome(int n, int n2, ChunkPrimer chunkPrimer, BiomeGenBase[] biomeGenBaseArray) {
        double d = 0.03125;
        this.stoneNoise = this.field_147430_m.func_151599_a(this.stoneNoise, n * 16, n2 * 16, 16, 16, d * 2.0, d * 2.0, 1.0);
        int n3 = 0;
        while (n3 < 16) {
            int n4 = 0;
            while (n4 < 16) {
                BiomeGenBase biomeGenBase = biomeGenBaseArray[n4 + n3 * 16];
                biomeGenBase.genTerrainBlocks(this.worldObj, this.rand, chunkPrimer, n * 16 + n3, n2 * 16 + n4, this.stoneNoise[n4 + n3 * 16]);
                ++n4;
            }
            ++n3;
        }
    }

    @Override
    public boolean canSave() {
        return true;
    }

    public void setBlocksInChunk(int n, int n2, ChunkPrimer chunkPrimer) {
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, n * 4 - 2, n2 * 4 - 2, 10, 10);
        this.func_147423_a(n * 4, 0, n2 * 4);
        int n3 = 0;
        while (n3 < 4) {
            int n4 = n3 * 5;
            int n5 = (n3 + 1) * 5;
            int n6 = 0;
            while (n6 < 4) {
                int n7 = (n4 + n6) * 33;
                int n8 = (n4 + n6 + 1) * 33;
                int n9 = (n5 + n6) * 33;
                int n10 = (n5 + n6 + 1) * 33;
                int n11 = 0;
                while (n11 < 32) {
                    double d = 0.125;
                    double d2 = this.field_147434_q[n7 + n11];
                    double d3 = this.field_147434_q[n8 + n11];
                    double d4 = this.field_147434_q[n9 + n11];
                    double d5 = this.field_147434_q[n10 + n11];
                    double d6 = (this.field_147434_q[n7 + n11 + 1] - d2) * d;
                    double d7 = (this.field_147434_q[n8 + n11 + 1] - d3) * d;
                    double d8 = (this.field_147434_q[n9 + n11 + 1] - d4) * d;
                    double d9 = (this.field_147434_q[n10 + n11 + 1] - d5) * d;
                    int n12 = 0;
                    while (n12 < 8) {
                        double d10 = 0.25;
                        double d11 = d2;
                        double d12 = d3;
                        double d13 = (d4 - d2) * d10;
                        double d14 = (d5 - d3) * d10;
                        int n13 = 0;
                        while (n13 < 4) {
                            double d15 = 0.25;
                            double d16 = (d12 - d11) * d15;
                            double d17 = d11 - d16;
                            int n14 = 0;
                            while (n14 < 4) {
                                double d18;
                                d17 += d16;
                                if (d18 > 0.0) {
                                    chunkPrimer.setBlockState(n3 * 4 + n13, n11 * 8 + n12, n6 * 4 + n14, Blocks.stone.getDefaultState());
                                } else if (n11 * 8 + n12 < this.settings.seaLevel) {
                                    chunkPrimer.setBlockState(n3 * 4 + n13, n11 * 8 + n12, n6 * 4 + n14, this.field_177476_s.getDefaultState());
                                }
                                ++n14;
                            }
                            d11 += d13;
                            d12 += d14;
                            ++n13;
                        }
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                        d5 += d9;
                        ++n12;
                    }
                    ++n11;
                }
                ++n6;
            }
            ++n3;
        }
    }

    @Override
    public String makeString() {
        return "RandomLevelSource";
    }

    public ChunkProviderGenerate(World world, long l, boolean bl, String string) {
        this.stoneNoise = new double[256];
        this.caveGenerator = new MapGenCaves();
        this.strongholdGenerator = new MapGenStronghold();
        this.villageGenerator = new MapGenVillage();
        this.mineshaftGenerator = new MapGenMineshaft();
        this.scatteredFeatureGenerator = new MapGenScatteredFeature();
        this.ravineGenerator = new MapGenRavine();
        this.oceanMonumentGenerator = new StructureOceanMonument();
        this.worldObj = world;
        this.mapFeaturesEnabled = bl;
        this.field_177475_o = world.getWorldInfo().getTerrainType();
        this.rand = new Random(l);
        this.field_147431_j = new NoiseGeneratorOctaves(this.rand, 16);
        this.field_147432_k = new NoiseGeneratorOctaves(this.rand, 16);
        this.field_147429_l = new NoiseGeneratorOctaves(this.rand, 8);
        this.field_147430_m = new NoiseGeneratorPerlin(this.rand, 4);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
        this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.field_147434_q = new double[825];
        this.parabolicField = new float[25];
        int n = -2;
        while (n <= 2) {
            int n2 = -2;
            while (n2 <= 2) {
                float f;
                this.parabolicField[n + 2 + (n2 + 2) * 5] = f = 10.0f / MathHelper.sqrt_float((float)(n * n + n2 * n2) + 0.2f);
                ++n2;
            }
            ++n;
        }
        if (string != null) {
            this.settings = ChunkProviderSettings.Factory.jsonToFactory(string).func_177864_b();
            this.field_177476_s = this.settings.useLavaOceans ? Blocks.lava : Blocks.water;
            world.func_181544_b(this.settings.seaLevel);
        }
    }

    private void func_147423_a(int n, int n2, int n3) {
        this.field_147426_g = this.noiseGen6.generateNoiseOctaves(this.field_147426_g, n, n3, 5, 5, this.settings.depthNoiseScaleX, this.settings.depthNoiseScaleZ, this.settings.depthNoiseScaleExponent);
        float f = this.settings.coordinateScale;
        float f2 = this.settings.heightScale;
        this.field_147427_d = this.field_147429_l.generateNoiseOctaves(this.field_147427_d, n, n2, n3, 5, 33, 5, f / this.settings.mainNoiseScaleX, f2 / this.settings.mainNoiseScaleY, f / this.settings.mainNoiseScaleZ);
        this.field_147428_e = this.field_147431_j.generateNoiseOctaves(this.field_147428_e, n, n2, n3, 5, 33, 5, f, f2, f);
        this.field_147425_f = this.field_147432_k.generateNoiseOctaves(this.field_147425_f, n, n2, n3, 5, 33, 5, f, f2, f);
        n3 = 0;
        n = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        while (n6 < 5) {
            int n7 = 0;
            while (n7 < 5) {
                float f3 = 0.0f;
                float f4 = 0.0f;
                float f5 = 0.0f;
                int n8 = 2;
                BiomeGenBase biomeGenBase = this.biomesForGeneration[n6 + 2 + (n7 + 2) * 10];
                int n9 = -n8;
                while (n9 <= n8) {
                    int n10 = -n8;
                    while (n10 <= n8) {
                        BiomeGenBase biomeGenBase2 = this.biomesForGeneration[n6 + n9 + 2 + (n7 + n10 + 2) * 10];
                        float f6 = this.settings.biomeDepthOffSet + biomeGenBase2.minHeight * this.settings.biomeDepthWeight;
                        float f7 = this.settings.biomeScaleOffset + biomeGenBase2.maxHeight * this.settings.biomeScaleWeight;
                        if (this.field_177475_o == WorldType.AMPLIFIED && f6 > 0.0f) {
                            f6 = 1.0f + f6 * 2.0f;
                            f7 = 1.0f + f7 * 4.0f;
                        }
                        float f8 = this.parabolicField[n9 + 2 + (n10 + 2) * 5] / (f6 + 2.0f);
                        if (biomeGenBase2.minHeight > biomeGenBase.minHeight) {
                            f8 /= 2.0f;
                        }
                        f3 += f7 * f8;
                        f4 += f6 * f8;
                        f5 += f8;
                        ++n10;
                    }
                    ++n9;
                }
                f3 /= f5;
                f4 /= f5;
                f3 = f3 * 0.9f + 0.1f;
                f4 = (f4 * 4.0f - 1.0f) / 8.0f;
                double d = this.field_147426_g[n5] / 8000.0;
                if (d < 0.0) {
                    d = -d * 0.3;
                }
                if ((d = d * 3.0 - 2.0) < 0.0) {
                    if ((d /= 2.0) < -1.0) {
                        d = -1.0;
                    }
                    d /= 1.4;
                    d /= 2.0;
                } else {
                    if (d > 1.0) {
                        d = 1.0;
                    }
                    d /= 8.0;
                }
                ++n5;
                double d2 = f4;
                double d3 = f3;
                d2 += d * 0.2;
                d2 = d2 * (double)this.settings.baseSize / 8.0;
                double d4 = (double)this.settings.baseSize + d2 * 4.0;
                int n11 = 0;
                while (n11 < 33) {
                    double d5 = ((double)n11 - d4) * (double)this.settings.stretchY * 128.0 / 256.0 / d3;
                    if (d5 < 0.0) {
                        d5 *= 4.0;
                    }
                    double d6 = this.field_147428_e[n4] / (double)this.settings.lowerLimitScale;
                    double d7 = this.field_147425_f[n4] / (double)this.settings.upperLimitScale;
                    double d8 = (this.field_147427_d[n4] / 10.0 + 1.0) / 2.0;
                    double d9 = MathHelper.denormalizeClamp(d6, d7, d8) - d5;
                    if (n11 > 29) {
                        double d10 = (float)(n11 - 29) / 3.0f;
                        d9 = d9 * (1.0 - d10) + -10.0 * d10;
                    }
                    this.field_147434_q[n4] = d9;
                    ++n4;
                    ++n11;
                }
                ++n7;
            }
            ++n6;
        }
    }

    @Override
    public void saveExtraData() {
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumCreatureType, BlockPos blockPos) {
        BiomeGenBase biomeGenBase = this.worldObj.getBiomeGenForCoords(blockPos);
        if (this.mapFeaturesEnabled) {
            if (enumCreatureType == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.func_175798_a(blockPos)) {
                return this.scatteredFeatureGenerator.getScatteredFeatureSpawnList();
            }
            if (enumCreatureType == EnumCreatureType.MONSTER && this.settings.useMonuments && this.oceanMonumentGenerator.func_175796_a(this.worldObj, blockPos)) {
                return this.oceanMonumentGenerator.func_175799_b();
            }
        }
        return biomeGenBase.getSpawnableList(enumCreatureType);
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public void recreateStructures(Chunk chunk, int n, int n2) {
        if (this.settings.useMineShafts && this.mapFeaturesEnabled) {
            this.mineshaftGenerator.generate(this, this.worldObj, n, n2, null);
        }
        if (this.settings.useVillages && this.mapFeaturesEnabled) {
            this.villageGenerator.generate(this, this.worldObj, n, n2, null);
        }
        if (this.settings.useStrongholds && this.mapFeaturesEnabled) {
            this.strongholdGenerator.generate(this, this.worldObj, n, n2, null);
        }
        if (this.settings.useTemples && this.mapFeaturesEnabled) {
            this.scatteredFeatureGenerator.generate(this, this.worldObj, n, n2, null);
        }
        if (this.settings.useMonuments && this.mapFeaturesEnabled) {
            this.oceanMonumentGenerator.generate(this, this.worldObj, n, n2, null);
        }
    }

    @Override
    public BlockPos getStrongholdGen(World world, String string, BlockPos blockPos) {
        return "Stronghold".equals(string) && this.strongholdGenerator != null ? this.strongholdGenerator.getClosestStrongholdPos(world, blockPos) : null;
    }

    @Override
    public Chunk provideChunk(int n, int n2) {
        this.rand.setSeed((long)n * 341873128712L + (long)n2 * 132897987541L);
        ChunkPrimer chunkPrimer = new ChunkPrimer();
        this.setBlocksInChunk(n, n2, chunkPrimer);
        this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, n * 16, n2 * 16, 16, 16);
        this.replaceBlocksForBiome(n, n2, chunkPrimer, this.biomesForGeneration);
        if (this.settings.useCaves) {
            this.caveGenerator.generate(this, this.worldObj, n, n2, chunkPrimer);
        }
        if (this.settings.useRavines) {
            this.ravineGenerator.generate(this, this.worldObj, n, n2, chunkPrimer);
        }
        if (this.settings.useMineShafts && this.mapFeaturesEnabled) {
            this.mineshaftGenerator.generate(this, this.worldObj, n, n2, chunkPrimer);
        }
        if (this.settings.useVillages && this.mapFeaturesEnabled) {
            this.villageGenerator.generate(this, this.worldObj, n, n2, chunkPrimer);
        }
        if (this.settings.useStrongholds && this.mapFeaturesEnabled) {
            this.strongholdGenerator.generate(this, this.worldObj, n, n2, chunkPrimer);
        }
        if (this.settings.useTemples && this.mapFeaturesEnabled) {
            this.scatteredFeatureGenerator.generate(this, this.worldObj, n, n2, chunkPrimer);
        }
        if (this.settings.useMonuments && this.mapFeaturesEnabled) {
            this.oceanMonumentGenerator.generate(this, this.worldObj, n, n2, chunkPrimer);
        }
        Chunk chunk = new Chunk(this.worldObj, chunkPrimer, n, n2);
        byte[] byArray = chunk.getBiomeArray();
        int n3 = 0;
        while (n3 < byArray.length) {
            byArray[n3] = (byte)this.biomesForGeneration[n3].biomeID;
            ++n3;
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    @Override
    public boolean chunkExists(int n, int n2) {
        return true;
    }

    @Override
    public boolean func_177460_a(IChunkProvider iChunkProvider, Chunk chunk, int n, int n2) {
        boolean bl = false;
        if (this.settings.useMonuments && this.mapFeaturesEnabled && chunk.getInhabitedTime() < 3600L) {
            bl |= this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, new ChunkCoordIntPair(n, n2));
        }
        return bl;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }
}

