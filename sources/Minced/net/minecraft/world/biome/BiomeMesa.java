// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.biome;

import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.properties.IProperty;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.block.BlockColored;
import java.util.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import java.util.Random;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.block.state.IBlockState;

public class BiomeMesa extends Biome
{
    protected static final IBlockState COARSE_DIRT;
    protected static final IBlockState GRASS;
    protected static final IBlockState HARDENED_CLAY;
    protected static final IBlockState STAINED_HARDENED_CLAY;
    protected static final IBlockState ORANGE_STAINED_HARDENED_CLAY;
    protected static final IBlockState RED_SAND;
    private IBlockState[] clayBands;
    private long worldSeed;
    private NoiseGeneratorPerlin pillarNoise;
    private NoiseGeneratorPerlin pillarRoofNoise;
    private NoiseGeneratorPerlin clayBandsOffsetNoise;
    private final boolean brycePillars;
    private final boolean hasForest;
    
    public BiomeMesa(final boolean p_i46704_1_, final boolean p_i46704_2_, final BiomeProperties properties) {
        super(properties);
        this.brycePillars = p_i46704_1_;
        this.hasForest = p_i46704_2_;
        this.spawnableCreatureList.clear();
        this.topBlock = BiomeMesa.RED_SAND;
        this.fillerBlock = BiomeMesa.STAINED_HARDENED_CLAY;
        this.decorator.treesPerChunk = -999;
        this.decorator.deadBushPerChunk = 20;
        this.decorator.reedsPerChunk = 3;
        this.decorator.cactiPerChunk = 5;
        this.decorator.flowersPerChunk = 0;
        this.spawnableCreatureList.clear();
        if (p_i46704_2_) {
            this.decorator.treesPerChunk = 5;
        }
    }
    
    @Override
    protected BiomeDecorator createBiomeDecorator() {
        return new Decorator();
    }
    
    @Override
    public WorldGenAbstractTree getRandomTreeFeature(final Random rand) {
        return BiomeMesa.TREE_FEATURE;
    }
    
    @Override
    public int getFoliageColorAtPos(final BlockPos pos) {
        return 10387789;
    }
    
    @Override
    public int getGrassColorAtPos(final BlockPos pos) {
        return 9470285;
    }
    
    @Override
    public void genTerrainBlocks(final World worldIn, final Random rand, final ChunkPrimer chunkPrimerIn, final int x, final int z, final double noiseVal) {
        if (this.clayBands == null || this.worldSeed != worldIn.getSeed()) {
            this.generateBands(worldIn.getSeed());
        }
        if (this.pillarNoise == null || this.pillarRoofNoise == null || this.worldSeed != worldIn.getSeed()) {
            final Random random = new Random(this.worldSeed);
            this.pillarNoise = new NoiseGeneratorPerlin(random, 4);
            this.pillarRoofNoise = new NoiseGeneratorPerlin(random, 1);
        }
        this.worldSeed = worldIn.getSeed();
        double d4 = 0.0;
        if (this.brycePillars) {
            final int i = (x & 0xFFFFFFF0) + (z & 0xF);
            final int j = (z & 0xFFFFFFF0) + (x & 0xF);
            final double d5 = Math.min(Math.abs(noiseVal), this.pillarNoise.getValue(i * 0.25, j * 0.25));
            if (d5 > 0.0) {
                final double d6 = 0.001953125;
                final double d7 = Math.abs(this.pillarRoofNoise.getValue(i * 0.001953125, j * 0.001953125));
                d4 = d5 * d5 * 2.5;
                final double d8 = Math.ceil(d7 * 50.0) + 14.0;
                if (d4 > d8) {
                    d4 = d8;
                }
                d4 += 64.0;
            }
        }
        final int k1 = x & 0xF;
        final int l1 = z & 0xF;
        final int i2 = worldIn.getSeaLevel();
        IBlockState iblockstate = BiomeMesa.STAINED_HARDENED_CLAY;
        IBlockState iblockstate2 = this.fillerBlock;
        final int m = (int)(noiseVal / 3.0 + 3.0 + rand.nextDouble() * 0.25);
        final boolean flag = Math.cos(noiseVal / 3.0 * 3.141592653589793) > 0.0;
        int l2 = -1;
        boolean flag2 = false;
        int i3 = 0;
        for (int j2 = 255; j2 >= 0; --j2) {
            if (chunkPrimerIn.getBlockState(l1, j2, k1).getMaterial() == Material.AIR && j2 < (int)d4) {
                chunkPrimerIn.setBlockState(l1, j2, k1, BiomeMesa.STONE);
            }
            if (j2 <= rand.nextInt(5)) {
                chunkPrimerIn.setBlockState(l1, j2, k1, BiomeMesa.BEDROCK);
            }
            else if (i3 < 15 || this.brycePillars) {
                final IBlockState iblockstate3 = chunkPrimerIn.getBlockState(l1, j2, k1);
                if (iblockstate3.getMaterial() == Material.AIR) {
                    l2 = -1;
                }
                else if (iblockstate3.getBlock() == Blocks.STONE) {
                    if (l2 == -1) {
                        flag2 = false;
                        if (m <= 0) {
                            iblockstate = BiomeMesa.AIR;
                            iblockstate2 = BiomeMesa.STONE;
                        }
                        else if (j2 >= i2 - 4 && j2 <= i2 + 1) {
                            iblockstate = BiomeMesa.STAINED_HARDENED_CLAY;
                            iblockstate2 = this.fillerBlock;
                        }
                        if (j2 < i2 && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
                            iblockstate = BiomeMesa.WATER;
                        }
                        l2 = m + Math.max(0, j2 - i2);
                        if (j2 >= i2 - 1) {
                            if (this.hasForest && j2 > 86 + m * 2) {
                                if (flag) {
                                    chunkPrimerIn.setBlockState(l1, j2, k1, BiomeMesa.COARSE_DIRT);
                                }
                                else {
                                    chunkPrimerIn.setBlockState(l1, j2, k1, BiomeMesa.GRASS);
                                }
                            }
                            else if (j2 > i2 + 3 + m) {
                                IBlockState iblockstate4;
                                if (j2 >= 64 && j2 <= 127) {
                                    if (flag) {
                                        iblockstate4 = BiomeMesa.HARDENED_CLAY;
                                    }
                                    else {
                                        iblockstate4 = this.getBand(x, j2, z);
                                    }
                                }
                                else {
                                    iblockstate4 = BiomeMesa.ORANGE_STAINED_HARDENED_CLAY;
                                }
                                chunkPrimerIn.setBlockState(l1, j2, k1, iblockstate4);
                            }
                            else {
                                chunkPrimerIn.setBlockState(l1, j2, k1, this.topBlock);
                                flag2 = true;
                            }
                        }
                        else {
                            chunkPrimerIn.setBlockState(l1, j2, k1, iblockstate2);
                            if (iblockstate2.getBlock() == Blocks.STAINED_HARDENED_CLAY) {
                                chunkPrimerIn.setBlockState(l1, j2, k1, BiomeMesa.ORANGE_STAINED_HARDENED_CLAY);
                            }
                        }
                    }
                    else if (l2 > 0) {
                        --l2;
                        if (flag2) {
                            chunkPrimerIn.setBlockState(l1, j2, k1, BiomeMesa.ORANGE_STAINED_HARDENED_CLAY);
                        }
                        else {
                            chunkPrimerIn.setBlockState(l1, j2, k1, this.getBand(x, j2, z));
                        }
                    }
                    ++i3;
                }
            }
        }
    }
    
    private void generateBands(final long p_150619_1_) {
        Arrays.fill(this.clayBands = new IBlockState[64], BiomeMesa.HARDENED_CLAY);
        final Random random = new Random(p_150619_1_);
        this.clayBandsOffsetNoise = new NoiseGeneratorPerlin(random, 1);
        for (int l1 = 0; l1 < 64; ++l1) {
            l1 += random.nextInt(5) + 1;
            if (l1 < 64) {
                this.clayBands[l1] = BiomeMesa.ORANGE_STAINED_HARDENED_CLAY;
            }
        }
        for (int i2 = random.nextInt(4) + 2, j = 0; j < i2; ++j) {
            for (int k = random.nextInt(3) + 1, m = random.nextInt(64), l2 = 0; m + l2 < 64 && l2 < k; ++l2) {
                this.clayBands[m + l2] = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);
            }
        }
        for (int j2 = random.nextInt(4) + 2, k2 = 0; k2 < j2; ++k2) {
            for (int i3 = random.nextInt(3) + 2, l3 = random.nextInt(64), i4 = 0; l3 + i4 < 64 && i4 < i3; ++i4) {
                this.clayBands[l3 + i4] = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.BROWN);
            }
        }
        for (int l4 = random.nextInt(4) + 2, j3 = 0; j3 < l4; ++j3) {
            for (int i5 = random.nextInt(3) + 1, k3 = random.nextInt(64), j4 = 0; k3 + j4 < 64 && j4 < i5; ++j4) {
                this.clayBands[k3 + j4] = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.RED);
            }
        }
        final int k4 = random.nextInt(3) + 3;
        int j5 = 0;
        for (int l5 = 0; l5 < k4; ++l5) {
            final int i6 = 1;
            j5 += random.nextInt(16) + 4;
            for (int k5 = 0; j5 + k5 < 64 && k5 < 1; ++k5) {
                this.clayBands[j5 + k5] = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);
                if (j5 + k5 > 1 && random.nextBoolean()) {
                    this.clayBands[j5 + k5 - 1] = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }
                if (j5 + k5 < 63 && random.nextBoolean()) {
                    this.clayBands[j5 + k5 + 1] = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }
            }
        }
    }
    
    private IBlockState getBand(final int p_180629_1_, final int p_180629_2_, final int p_180629_3_) {
        final int i = (int)Math.round(this.clayBandsOffsetNoise.getValue(p_180629_1_ / 512.0, p_180629_1_ / 512.0) * 2.0);
        return this.clayBands[(p_180629_2_ + i + 64) % 64];
    }
    
    static {
        COARSE_DIRT = Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
        GRASS = Blocks.GRASS.getDefaultState();
        HARDENED_CLAY = Blocks.HARDENED_CLAY.getDefaultState();
        STAINED_HARDENED_CLAY = Blocks.STAINED_HARDENED_CLAY.getDefaultState();
        ORANGE_STAINED_HARDENED_CLAY = BiomeMesa.STAINED_HARDENED_CLAY.withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
        RED_SAND = Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
    }
    
    class Decorator extends BiomeDecorator
    {
        private Decorator() {
        }
        
        @Override
        protected void generateOres(final World worldIn, final Random random) {
            super.generateOres(worldIn, random);
            this.genStandardOre1(worldIn, random, 20, this.goldGen, 32, 80);
        }
    }
}
