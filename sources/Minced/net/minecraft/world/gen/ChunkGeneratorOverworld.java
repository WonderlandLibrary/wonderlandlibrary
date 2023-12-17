// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.gen.feature.WorldGenDungeons;
import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.WorldGenLakes;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockFalling;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.WoodlandMansion;
import net.minecraft.world.gen.structure.StructureOceanMonument;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.WorldType;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.block.state.IBlockState;

public class ChunkGeneratorOverworld implements IChunkGenerator
{
    protected static final IBlockState STONE;
    private final Random rand;
    private final NoiseGeneratorOctaves minLimitPerlinNoise;
    private final NoiseGeneratorOctaves maxLimitPerlinNoise;
    private final NoiseGeneratorOctaves mainPerlinNoise;
    private final NoiseGeneratorPerlin surfaceNoise;
    public NoiseGeneratorOctaves scaleNoise;
    public NoiseGeneratorOctaves depthNoise;
    public NoiseGeneratorOctaves forestNoise;
    private final World world;
    private final boolean mapFeaturesEnabled;
    private final WorldType terrainType;
    private final double[] heightMap;
    private final float[] biomeWeights;
    private ChunkGeneratorSettings settings;
    private IBlockState oceanBlock;
    private double[] depthBuffer;
    private final MapGenBase caveGenerator;
    private final MapGenStronghold strongholdGenerator;
    private final MapGenVillage villageGenerator;
    private final MapGenMineshaft mineshaftGenerator;
    private final MapGenScatteredFeature scatteredFeatureGenerator;
    private final MapGenBase ravineGenerator;
    private final StructureOceanMonument oceanMonumentGenerator;
    private final WoodlandMansion woodlandMansionGenerator;
    private Biome[] biomesForGeneration;
    double[] mainNoiseRegion;
    double[] minLimitRegion;
    double[] maxLimitRegion;
    double[] depthRegion;
    
    public ChunkGeneratorOverworld(final World worldIn, final long seed, final boolean mapFeaturesEnabledIn, final String generatorOptions) {
        this.oceanBlock = Blocks.WATER.getDefaultState();
        this.depthBuffer = new double[256];
        this.caveGenerator = new MapGenCaves();
        this.strongholdGenerator = new MapGenStronghold();
        this.villageGenerator = new MapGenVillage();
        this.mineshaftGenerator = new MapGenMineshaft();
        this.scatteredFeatureGenerator = new MapGenScatteredFeature();
        this.ravineGenerator = new MapGenRavine();
        this.oceanMonumentGenerator = new StructureOceanMonument();
        this.woodlandMansionGenerator = new WoodlandMansion(this);
        this.world = worldIn;
        this.mapFeaturesEnabled = mapFeaturesEnabledIn;
        this.terrainType = worldIn.getWorldInfo().getTerrainType();
        this.rand = new Random(seed);
        this.minLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.maxLimitPerlinNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.mainPerlinNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.surfaceNoise = new NoiseGeneratorPerlin(this.rand, 4);
        this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
        this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
        this.forestNoise = new NoiseGeneratorOctaves(this.rand, 8);
        this.heightMap = new double[825];
        this.biomeWeights = new float[25];
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                final float f = 10.0f / MathHelper.sqrt(i * i + j * j + 0.2f);
                this.biomeWeights[i + 2 + (j + 2) * 5] = f;
            }
        }
        if (generatorOptions != null) {
            this.settings = ChunkGeneratorSettings.Factory.jsonToFactory(generatorOptions).build();
            this.oceanBlock = (this.settings.useLavaOceans ? Blocks.LAVA.getDefaultState() : Blocks.WATER.getDefaultState());
            worldIn.setSeaLevel(this.settings.seaLevel);
        }
    }
    
    public void setBlocksInChunk(final int x, final int z, final ChunkPrimer primer) {
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomesForGeneration(this.biomesForGeneration, x * 4 - 2, z * 4 - 2, 10, 10);
        this.generateHeightmap(x * 4, 0, z * 4);
        for (int i = 0; i < 4; ++i) {
            final int j = i * 5;
            final int k = (i + 1) * 5;
            for (int l = 0; l < 4; ++l) {
                final int i2 = (j + l) * 33;
                final int j2 = (j + l + 1) * 33;
                final int k2 = (k + l) * 33;
                final int l2 = (k + l + 1) * 33;
                for (int i3 = 0; i3 < 32; ++i3) {
                    final double d0 = 0.125;
                    double d2 = this.heightMap[i2 + i3];
                    double d3 = this.heightMap[j2 + i3];
                    double d4 = this.heightMap[k2 + i3];
                    double d5 = this.heightMap[l2 + i3];
                    final double d6 = (this.heightMap[i2 + i3 + 1] - d2) * 0.125;
                    final double d7 = (this.heightMap[j2 + i3 + 1] - d3) * 0.125;
                    final double d8 = (this.heightMap[k2 + i3 + 1] - d4) * 0.125;
                    final double d9 = (this.heightMap[l2 + i3 + 1] - d5) * 0.125;
                    for (int j3 = 0; j3 < 8; ++j3) {
                        final double d10 = 0.25;
                        double d11 = d2;
                        double d12 = d3;
                        final double d13 = (d4 - d2) * 0.25;
                        final double d14 = (d5 - d3) * 0.25;
                        for (int k3 = 0; k3 < 4; ++k3) {
                            final double d15 = 0.25;
                            final double d16 = (d12 - d11) * 0.25;
                            double lvt_45_1_ = d11 - d16;
                            for (int l3 = 0; l3 < 4; ++l3) {
                                if ((lvt_45_1_ += d16) > 0.0) {
                                    primer.setBlockState(i * 4 + k3, i3 * 8 + j3, l * 4 + l3, ChunkGeneratorOverworld.STONE);
                                }
                                else if (i3 * 8 + j3 < this.settings.seaLevel) {
                                    primer.setBlockState(i * 4 + k3, i3 * 8 + j3, l * 4 + l3, this.oceanBlock);
                                }
                            }
                            d11 += d13;
                            d12 += d14;
                        }
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                        d5 += d9;
                    }
                }
            }
        }
    }
    
    public void replaceBiomeBlocks(final int x, final int z, final ChunkPrimer primer, final Biome[] biomesIn) {
        final double d0 = 0.03125;
        this.depthBuffer = this.surfaceNoise.getRegion(this.depthBuffer, x * 16, z * 16, 16, 16, 0.0625, 0.0625, 1.0);
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                final Biome biome = biomesIn[j + i * 16];
                biome.genTerrainBlocks(this.world, this.rand, primer, x * 16 + i, z * 16 + j, this.depthBuffer[j + i * 16]);
            }
        }
    }
    
    @Override
    public Chunk generateChunk(final int x, final int z) {
        this.rand.setSeed(x * 341873128712L + z * 132897987541L);
        final ChunkPrimer chunkprimer = new ChunkPrimer();
        this.setBlocksInChunk(x, z, chunkprimer);
        this.replaceBiomeBlocks(x, z, chunkprimer, this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16, 16));
        if (this.settings.useCaves) {
            this.caveGenerator.generate(this.world, x, z, chunkprimer);
        }
        if (this.settings.useRavines) {
            this.ravineGenerator.generate(this.world, x, z, chunkprimer);
        }
        if (this.mapFeaturesEnabled) {
            if (this.settings.useMineShafts) {
                this.mineshaftGenerator.generate(this.world, x, z, chunkprimer);
            }
            if (this.settings.useVillages) {
                this.villageGenerator.generate(this.world, x, z, chunkprimer);
            }
            if (this.settings.useStrongholds) {
                this.strongholdGenerator.generate(this.world, x, z, chunkprimer);
            }
            if (this.settings.useTemples) {
                this.scatteredFeatureGenerator.generate(this.world, x, z, chunkprimer);
            }
            if (this.settings.useMonuments) {
                this.oceanMonumentGenerator.generate(this.world, x, z, chunkprimer);
            }
            if (this.settings.useMansions) {
                this.woodlandMansionGenerator.generate(this.world, x, z, chunkprimer);
            }
        }
        final Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        final byte[] abyte = chunk.getBiomeArray();
        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte)Biome.getIdForBiome(this.biomesForGeneration[i]);
        }
        chunk.generateSkylightMap();
        return chunk;
    }
    
    private void generateHeightmap(final int x, final int y, final int z) {
        this.depthRegion = this.depthNoise.generateNoiseOctaves(this.depthRegion, x, z, 5, 5, this.settings.depthNoiseScaleX, this.settings.depthNoiseScaleZ, this.settings.depthNoiseScaleExponent);
        final float f = this.settings.coordinateScale;
        final float f2 = this.settings.heightScale;
        this.mainNoiseRegion = this.mainPerlinNoise.generateNoiseOctaves(this.mainNoiseRegion, x, y, z, 5, 33, 5, f / this.settings.mainNoiseScaleX, f2 / this.settings.mainNoiseScaleY, f / this.settings.mainNoiseScaleZ);
        this.minLimitRegion = this.minLimitPerlinNoise.generateNoiseOctaves(this.minLimitRegion, x, y, z, 5, 33, 5, f, f2, f);
        this.maxLimitRegion = this.maxLimitPerlinNoise.generateNoiseOctaves(this.maxLimitRegion, x, y, z, 5, 33, 5, f, f2, f);
        int i = 0;
        int j = 0;
        for (int k = 0; k < 5; ++k) {
            for (int l = 0; l < 5; ++l) {
                float f3 = 0.0f;
                float f4 = 0.0f;
                float f5 = 0.0f;
                final int i2 = 2;
                final Biome biome = this.biomesForGeneration[k + 2 + (l + 2) * 10];
                for (int j2 = -2; j2 <= 2; ++j2) {
                    for (int k2 = -2; k2 <= 2; ++k2) {
                        final Biome biome2 = this.biomesForGeneration[k + j2 + 2 + (l + k2 + 2) * 10];
                        float f6 = this.settings.biomeDepthOffSet + biome2.getBaseHeight() * this.settings.biomeDepthWeight;
                        float f7 = this.settings.biomeScaleOffset + biome2.getHeightVariation() * this.settings.biomeScaleWeight;
                        if (this.terrainType == WorldType.AMPLIFIED && f6 > 0.0f) {
                            f6 = 1.0f + f6 * 2.0f;
                            f7 = 1.0f + f7 * 4.0f;
                        }
                        float f8 = this.biomeWeights[j2 + 2 + (k2 + 2) * 5] / (f6 + 2.0f);
                        if (biome2.getBaseHeight() > biome.getBaseHeight()) {
                            f8 /= 2.0f;
                        }
                        f3 += f7 * f8;
                        f4 += f6 * f8;
                        f5 += f8;
                    }
                }
                f3 /= f5;
                f4 /= f5;
                f3 = f3 * 0.9f + 0.1f;
                f4 = (f4 * 4.0f - 1.0f) / 8.0f;
                double d7 = this.depthRegion[j] / 8000.0;
                if (d7 < 0.0) {
                    d7 = -d7 * 0.3;
                }
                d7 = d7 * 3.0 - 2.0;
                if (d7 < 0.0) {
                    d7 /= 2.0;
                    if (d7 < -1.0) {
                        d7 = -1.0;
                    }
                    d7 /= 1.4;
                    d7 /= 2.0;
                }
                else {
                    if (d7 > 1.0) {
                        d7 = 1.0;
                    }
                    d7 /= 8.0;
                }
                ++j;
                double d8 = f4;
                final double d9 = f3;
                d8 += d7 * 0.2;
                d8 = d8 * this.settings.baseSize / 8.0;
                final double d10 = this.settings.baseSize + d8 * 4.0;
                for (int l2 = 0; l2 < 33; ++l2) {
                    double d11 = (l2 - d10) * this.settings.stretchY * 128.0 / 256.0 / d9;
                    if (d11 < 0.0) {
                        d11 *= 4.0;
                    }
                    final double d12 = this.minLimitRegion[i] / this.settings.lowerLimitScale;
                    final double d13 = this.maxLimitRegion[i] / this.settings.upperLimitScale;
                    final double d14 = (this.mainNoiseRegion[i] / 10.0 + 1.0) / 2.0;
                    double d15 = MathHelper.clampedLerp(d12, d13, d14) - d11;
                    if (l2 > 29) {
                        final double d16 = (l2 - 29) / 3.0f;
                        d15 = d15 * (1.0 - d16) + -10.0 * d16;
                    }
                    this.heightMap[i] = d15;
                    ++i;
                }
            }
        }
    }
    
    @Override
    public void populate(final int x, final int z) {
        BlockFalling.fallInstantly = true;
        final int i = x * 16;
        final int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        final Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
        this.rand.setSeed(this.world.getSeed());
        final long k = this.rand.nextLong() / 2L * 2L + 1L;
        final long l = this.rand.nextLong() / 2L * 2L + 1L;
        this.rand.setSeed(x * k + z * l ^ this.world.getSeed());
        boolean flag = false;
        final ChunkPos chunkpos = new ChunkPos(x, z);
        if (this.mapFeaturesEnabled) {
            if (this.settings.useMineShafts) {
                this.mineshaftGenerator.generateStructure(this.world, this.rand, chunkpos);
            }
            if (this.settings.useVillages) {
                flag = this.villageGenerator.generateStructure(this.world, this.rand, chunkpos);
            }
            if (this.settings.useStrongholds) {
                this.strongholdGenerator.generateStructure(this.world, this.rand, chunkpos);
            }
            if (this.settings.useTemples) {
                this.scatteredFeatureGenerator.generateStructure(this.world, this.rand, chunkpos);
            }
            if (this.settings.useMonuments) {
                this.oceanMonumentGenerator.generateStructure(this.world, this.rand, chunkpos);
            }
            if (this.settings.useMansions) {
                this.woodlandMansionGenerator.generateStructure(this.world, this.rand, chunkpos);
            }
        }
        if (biome != Biomes.DESERT && biome != Biomes.DESERT_HILLS && this.settings.useWaterLakes && !flag && this.rand.nextInt(this.settings.waterLakeChance) == 0) {
            final int i2 = this.rand.nextInt(16) + 8;
            final int j2 = this.rand.nextInt(256);
            final int k2 = this.rand.nextInt(16) + 8;
            new WorldGenLakes(Blocks.WATER).generate(this.world, this.rand, blockpos.add(i2, j2, k2));
        }
        if (!flag && this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0 && this.settings.useLavaLakes) {
            final int i3 = this.rand.nextInt(16) + 8;
            final int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
            final int k3 = this.rand.nextInt(16) + 8;
            if (l2 < this.world.getSeaLevel() || this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0) {
                new WorldGenLakes(Blocks.LAVA).generate(this.world, this.rand, blockpos.add(i3, l2, k3));
            }
        }
        if (this.settings.useDungeons) {
            for (int j3 = 0; j3 < this.settings.dungeonChance; ++j3) {
                final int i4 = this.rand.nextInt(16) + 8;
                final int l3 = this.rand.nextInt(256);
                final int l4 = this.rand.nextInt(16) + 8;
                new WorldGenDungeons().generate(this.world, this.rand, blockpos.add(i4, l3, l4));
            }
        }
        biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
        WorldEntitySpawner.performWorldGenSpawning(this.world, biome, i + 8, j + 8, 16, 16, this.rand);
        blockpos = blockpos.add(8, 0, 8);
        for (int k4 = 0; k4 < 16; ++k4) {
            for (int j4 = 0; j4 < 16; ++j4) {
                final BlockPos blockpos2 = this.world.getPrecipitationHeight(blockpos.add(k4, 0, j4));
                final BlockPos blockpos3 = blockpos2.down();
                if (this.world.canBlockFreezeWater(blockpos3)) {
                    this.world.setBlockState(blockpos3, Blocks.ICE.getDefaultState(), 2);
                }
                if (this.world.canSnowAt(blockpos2, true)) {
                    this.world.setBlockState(blockpos2, Blocks.SNOW_LAYER.getDefaultState(), 2);
                }
            }
        }
        BlockFalling.fallInstantly = false;
    }
    
    @Override
    public boolean generateStructures(final Chunk chunkIn, final int x, final int z) {
        boolean flag = false;
        if (this.settings.useMonuments && this.mapFeaturesEnabled && chunkIn.getInhabitedTime() < 3600L) {
            flag |= this.oceanMonumentGenerator.generateStructure(this.world, this.rand, new ChunkPos(x, z));
        }
        return flag;
    }
    
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        final Biome biome = this.world.getBiome(pos);
        if (this.mapFeaturesEnabled) {
            if (creatureType == EnumCreatureType.MONSTER && this.scatteredFeatureGenerator.isSwampHut(pos)) {
                return this.scatteredFeatureGenerator.getMonsters();
            }
            if (creatureType == EnumCreatureType.MONSTER && this.settings.useMonuments && this.oceanMonumentGenerator.isPositionInStructure(this.world, pos)) {
                return this.oceanMonumentGenerator.getMonsters();
            }
        }
        return biome.getSpawnableList(creatureType);
    }
    
    @Override
    public boolean isInsideStructure(final World worldIn, final String structureName, final BlockPos pos) {
        if (!this.mapFeaturesEnabled) {
            return false;
        }
        if ("Stronghold".equals(structureName) && this.strongholdGenerator != null) {
            return this.strongholdGenerator.isInsideStructure(pos);
        }
        if ("Mansion".equals(structureName) && this.woodlandMansionGenerator != null) {
            return this.woodlandMansionGenerator.isInsideStructure(pos);
        }
        if ("Monument".equals(structureName) && this.oceanMonumentGenerator != null) {
            return this.oceanMonumentGenerator.isInsideStructure(pos);
        }
        if ("Village".equals(structureName) && this.villageGenerator != null) {
            return this.villageGenerator.isInsideStructure(pos);
        }
        if ("Mineshaft".equals(structureName) && this.mineshaftGenerator != null) {
            return this.mineshaftGenerator.isInsideStructure(pos);
        }
        return "Temple".equals(structureName) && this.scatteredFeatureGenerator != null && this.scatteredFeatureGenerator.isInsideStructure(pos);
    }
    
    @Nullable
    @Override
    public BlockPos getNearestStructurePos(final World worldIn, final String structureName, final BlockPos position, final boolean findUnexplored) {
        if (!this.mapFeaturesEnabled) {
            return null;
        }
        if ("Stronghold".equals(structureName) && this.strongholdGenerator != null) {
            return this.strongholdGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
        }
        if ("Mansion".equals(structureName) && this.woodlandMansionGenerator != null) {
            return this.woodlandMansionGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
        }
        if ("Monument".equals(structureName) && this.oceanMonumentGenerator != null) {
            return this.oceanMonumentGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
        }
        if ("Village".equals(structureName) && this.villageGenerator != null) {
            return this.villageGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
        }
        if ("Mineshaft".equals(structureName) && this.mineshaftGenerator != null) {
            return this.mineshaftGenerator.getNearestStructurePos(worldIn, position, findUnexplored);
        }
        return ("Temple".equals(structureName) && this.scatteredFeatureGenerator != null) ? this.scatteredFeatureGenerator.getNearestStructurePos(worldIn, position, findUnexplored) : null;
    }
    
    @Override
    public void recreateStructures(final Chunk chunkIn, final int x, final int z) {
        if (this.mapFeaturesEnabled) {
            if (this.settings.useMineShafts) {
                this.mineshaftGenerator.generate(this.world, x, z, null);
            }
            if (this.settings.useVillages) {
                this.villageGenerator.generate(this.world, x, z, null);
            }
            if (this.settings.useStrongholds) {
                this.strongholdGenerator.generate(this.world, x, z, null);
            }
            if (this.settings.useTemples) {
                this.scatteredFeatureGenerator.generate(this.world, x, z, null);
            }
            if (this.settings.useMonuments) {
                this.oceanMonumentGenerator.generate(this.world, x, z, null);
            }
            if (this.settings.useMansions) {
                this.woodlandMansionGenerator.generate(this.world, x, z, null);
            }
        }
    }
    
    static {
        STONE = Blocks.STONE.getDefaultState();
    }
}
