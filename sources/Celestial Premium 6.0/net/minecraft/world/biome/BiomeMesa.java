/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeMesa
extends Biome {
    protected static final IBlockState COARSE_DIRT = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
    protected static final IBlockState GRASS = Blocks.GRASS.getDefaultState();
    protected static final IBlockState HARDENED_CLAY = Blocks.HARDENED_CLAY.getDefaultState();
    protected static final IBlockState STAINED_HARDENED_CLAY = Blocks.STAINED_HARDENED_CLAY.getDefaultState();
    protected static final IBlockState ORANGE_STAINED_HARDENED_CLAY = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
    protected static final IBlockState RED_SAND = Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
    private IBlockState[] clayBands;
    private long worldSeed;
    private NoiseGeneratorPerlin pillarNoise;
    private NoiseGeneratorPerlin pillarRoofNoise;
    private NoiseGeneratorPerlin clayBandsOffsetNoise;
    private final boolean brycePillars;
    private final boolean hasForest;

    public BiomeMesa(boolean p_i46704_1_, boolean p_i46704_2_, Biome.BiomeProperties properties) {
        super(properties);
        this.brycePillars = p_i46704_1_;
        this.hasForest = p_i46704_2_;
        this.spawnableCreatureList.clear();
        this.topBlock = RED_SAND;
        this.fillerBlock = STAINED_HARDENED_CLAY;
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 20;
        this.theBiomeDecorator.reedsPerChunk = 3;
        this.theBiomeDecorator.cactiPerChunk = 5;
        this.theBiomeDecorator.flowersPerChunk = 0;
        this.spawnableCreatureList.clear();
        if (p_i46704_2_) {
            this.theBiomeDecorator.treesPerChunk = 5;
        }
    }

    @Override
    protected BiomeDecorator createBiomeDecorator() {
        return new Decorator();
    }

    @Override
    public WorldGenAbstractTree genBigTreeChance(Random rand) {
        return TREE_FEATURE;
    }

    @Override
    public int getFoliageColorAtPos(BlockPos pos) {
        return 10387789;
    }

    @Override
    public int getGrassColorAtPos(BlockPos pos) {
        return 9470285;
    }

    @Override
    public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int x, int z, double noiseVal) {
        if (this.clayBands == null || this.worldSeed != worldIn.getSeed()) {
            this.generateBands(worldIn.getSeed());
        }
        if (this.pillarNoise == null || this.pillarRoofNoise == null || this.worldSeed != worldIn.getSeed()) {
            Random random = new Random(this.worldSeed);
            this.pillarNoise = new NoiseGeneratorPerlin(random, 4);
            this.pillarRoofNoise = new NoiseGeneratorPerlin(random, 1);
        }
        this.worldSeed = worldIn.getSeed();
        double d4 = 0.0;
        if (this.brycePillars) {
            int i = (x & 0xFFFFFFF0) + (z & 0xF);
            int j = (z & 0xFFFFFFF0) + (x & 0xF);
            double d0 = Math.min(Math.abs(noiseVal), this.pillarNoise.getValue((double)i * 0.25, (double)j * 0.25));
            if (d0 > 0.0) {
                double d1 = 0.001953125;
                d4 = d0 * d0 * 2.5;
                double d2 = Math.abs(this.pillarRoofNoise.getValue((double)i * 0.001953125, (double)j * 0.001953125));
                double d3 = Math.ceil(d2 * 50.0) + 14.0;
                if (d4 > d3) {
                    d4 = d3;
                }
                d4 += 64.0;
            }
        }
        int k1 = x & 0xF;
        int l1 = z & 0xF;
        int i2 = worldIn.getSeaLevel();
        IBlockState iblockstate = STAINED_HARDENED_CLAY;
        IBlockState iblockstate3 = this.fillerBlock;
        int k = (int)(noiseVal / 3.0 + 3.0 + rand.nextDouble() * 0.25);
        boolean flag = Math.cos(noiseVal / 3.0 * Math.PI) > 0.0;
        int l = -1;
        boolean flag1 = false;
        int i1 = 0;
        for (int j1 = 255; j1 >= 0; --j1) {
            if (chunkPrimerIn.getBlockState(l1, j1, k1).getMaterial() == Material.AIR && j1 < (int)d4) {
                chunkPrimerIn.setBlockState(l1, j1, k1, STONE);
            }
            if (j1 <= rand.nextInt(5)) {
                chunkPrimerIn.setBlockState(l1, j1, k1, BEDROCK);
                continue;
            }
            if (i1 >= 15 && !this.brycePillars) continue;
            IBlockState iblockstate1 = chunkPrimerIn.getBlockState(l1, j1, k1);
            if (iblockstate1.getMaterial() == Material.AIR) {
                l = -1;
                continue;
            }
            if (iblockstate1.getBlock() != Blocks.STONE) continue;
            if (l == -1) {
                flag1 = false;
                if (k <= 0) {
                    iblockstate = AIR;
                    iblockstate3 = STONE;
                } else if (j1 >= i2 - 4 && j1 <= i2 + 1) {
                    iblockstate = STAINED_HARDENED_CLAY;
                    iblockstate3 = this.fillerBlock;
                }
                if (j1 < i2 && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
                    iblockstate = WATER;
                }
                l = k + Math.max(0, j1 - i2);
                if (j1 >= i2 - 1) {
                    if (this.hasForest && j1 > 86 + k * 2) {
                        if (flag) {
                            chunkPrimerIn.setBlockState(l1, j1, k1, COARSE_DIRT);
                        } else {
                            chunkPrimerIn.setBlockState(l1, j1, k1, GRASS);
                        }
                    } else if (j1 > i2 + 3 + k) {
                        IBlockState iblockstate2 = j1 >= 64 && j1 <= 127 ? (flag ? HARDENED_CLAY : this.getBand(x, j1, z)) : ORANGE_STAINED_HARDENED_CLAY;
                        chunkPrimerIn.setBlockState(l1, j1, k1, iblockstate2);
                    } else {
                        chunkPrimerIn.setBlockState(l1, j1, k1, this.topBlock);
                        flag1 = true;
                    }
                } else {
                    chunkPrimerIn.setBlockState(l1, j1, k1, iblockstate3);
                    if (iblockstate3.getBlock() == Blocks.STAINED_HARDENED_CLAY) {
                        chunkPrimerIn.setBlockState(l1, j1, k1, ORANGE_STAINED_HARDENED_CLAY);
                    }
                }
            } else if (l > 0) {
                --l;
                if (flag1) {
                    chunkPrimerIn.setBlockState(l1, j1, k1, ORANGE_STAINED_HARDENED_CLAY);
                } else {
                    chunkPrimerIn.setBlockState(l1, j1, k1, this.getBand(x, j1, z));
                }
            }
            ++i1;
        }
    }

    private void generateBands(long p_150619_1_) {
        this.clayBands = new IBlockState[64];
        Arrays.fill(this.clayBands, HARDENED_CLAY);
        Random random = new Random(p_150619_1_);
        this.clayBandsOffsetNoise = new NoiseGeneratorPerlin(random, 1);
        for (int l1 = 0; l1 < 64; ++l1) {
            if ((l1 += random.nextInt(5) + 1) >= 64) continue;
            this.clayBands[l1] = ORANGE_STAINED_HARDENED_CLAY;
        }
        int i2 = random.nextInt(4) + 2;
        for (int i = 0; i < i2; ++i) {
            int j = random.nextInt(3) + 1;
            int k = random.nextInt(64);
            for (int l = 0; k + l < 64 && l < j; ++l) {
                this.clayBands[k + l] = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);
            }
        }
        int j2 = random.nextInt(4) + 2;
        for (int k2 = 0; k2 < j2; ++k2) {
            int i3 = random.nextInt(3) + 2;
            int l3 = random.nextInt(64);
            for (int i1 = 0; l3 + i1 < 64 && i1 < i3; ++i1) {
                this.clayBands[l3 + i1] = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.BROWN);
            }
        }
        int l2 = random.nextInt(4) + 2;
        for (int j3 = 0; j3 < l2; ++j3) {
            int i4 = random.nextInt(3) + 1;
            int k4 = random.nextInt(64);
            for (int j1 = 0; k4 + j1 < 64 && j1 < i4; ++j1) {
                this.clayBands[k4 + j1] = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.RED);
            }
        }
        int k3 = random.nextInt(3) + 3;
        int j4 = 0;
        for (int l4 = 0; l4 < k3; ++l4) {
            boolean i5 = true;
            for (int k1 = 0; (j4 += random.nextInt(16) + 4) + k1 < 64 && k1 < 1; ++k1) {
                this.clayBands[j4 + k1] = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);
                if (j4 + k1 > 1 && random.nextBoolean()) {
                    this.clayBands[j4 + k1 - 1] = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }
                if (j4 + k1 >= 63 || !random.nextBoolean()) continue;
                this.clayBands[j4 + k1 + 1] = STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
            }
        }
    }

    private IBlockState getBand(int p_180629_1_, int p_180629_2_, int p_180629_3_) {
        int i = (int)Math.round(this.clayBandsOffsetNoise.getValue((double)p_180629_1_ / 512.0, (double)p_180629_1_ / 512.0) * 2.0);
        return this.clayBands[(p_180629_2_ + i + 64) % 64];
    }

    class Decorator
    extends BiomeDecorator {
        private Decorator() {
        }

        @Override
        protected void generateOres(World worldIn, Random random) {
            super.generateOres(worldIn, random);
            this.genStandardOre1(worldIn, random, 20, this.goldGen, 32, 80);
        }
    }
}

