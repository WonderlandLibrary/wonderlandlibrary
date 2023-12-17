// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockFalling;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.block.material.Material;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.block.Block;
import com.google.common.base.Predicate;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenHellLava;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenGlowStone2;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.feature.WorldGenFire;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;

public class ChunkGeneratorHell implements IChunkGenerator
{
    protected static final IBlockState AIR;
    protected static final IBlockState NETHERRACK;
    protected static final IBlockState BEDROCK;
    protected static final IBlockState LAVA;
    protected static final IBlockState GRAVEL;
    protected static final IBlockState SOUL_SAND;
    private final World world;
    private final boolean generateStructures;
    private final Random rand;
    private double[] slowsandNoise;
    private double[] gravelNoise;
    private double[] depthBuffer;
    private double[] buffer;
    private final NoiseGeneratorOctaves lperlinNoise1;
    private final NoiseGeneratorOctaves lperlinNoise2;
    private final NoiseGeneratorOctaves perlinNoise1;
    private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
    private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
    public final NoiseGeneratorOctaves scaleNoise;
    public final NoiseGeneratorOctaves depthNoise;
    private final WorldGenFire fireFeature;
    private final WorldGenGlowStone1 lightGemGen;
    private final WorldGenGlowStone2 hellPortalGen;
    private final WorldGenerator quartzGen;
    private final WorldGenerator magmaGen;
    private final WorldGenHellLava lavaTrapGen;
    private final WorldGenHellLava hellSpringGen;
    private final WorldGenBush brownMushroomFeature;
    private final WorldGenBush redMushroomFeature;
    private final MapGenNetherBridge genNetherBridge;
    private final MapGenBase genNetherCaves;
    double[] pnr;
    double[] ar;
    double[] br;
    double[] noiseData4;
    double[] dr;
    
    public ChunkGeneratorHell(final World worldIn, final boolean p_i45637_2_, final long seed) {
        this.slowsandNoise = new double[256];
        this.gravelNoise = new double[256];
        this.depthBuffer = new double[256];
        this.fireFeature = new WorldGenFire();
        this.lightGemGen = new WorldGenGlowStone1();
        this.hellPortalGen = new WorldGenGlowStone2();
        this.quartzGen = new WorldGenMinable(Blocks.QUARTZ_ORE.getDefaultState(), 14, (Predicate<IBlockState>)BlockMatcher.forBlock(Blocks.NETHERRACK));
        this.magmaGen = new WorldGenMinable(Blocks.MAGMA.getDefaultState(), 33, (Predicate<IBlockState>)BlockMatcher.forBlock(Blocks.NETHERRACK));
        this.lavaTrapGen = new WorldGenHellLava(Blocks.FLOWING_LAVA, true);
        this.hellSpringGen = new WorldGenHellLava(Blocks.FLOWING_LAVA, false);
        this.brownMushroomFeature = new WorldGenBush(Blocks.BROWN_MUSHROOM);
        this.redMushroomFeature = new WorldGenBush(Blocks.RED_MUSHROOM);
        this.genNetherBridge = new MapGenNetherBridge();
        this.genNetherCaves = new MapGenCavesHell();
        this.world = worldIn;
        this.generateStructures = p_i45637_2_;
        this.rand = new Random(seed);
        this.lperlinNoise1 = new NoiseGeneratorOctaves(this.rand, 16);
        this.lperlinNoise2 = new NoiseGeneratorOctaves(this.rand, 16);
        this.perlinNoise1 = new NoiseGeneratorOctaves(this.rand, 8);
        this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.rand, 4);
        this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(this.rand, 4);
        this.scaleNoise = new NoiseGeneratorOctaves(this.rand, 10);
        this.depthNoise = new NoiseGeneratorOctaves(this.rand, 16);
        worldIn.setSeaLevel(63);
    }
    
    public void prepareHeights(final int p_185936_1_, final int p_185936_2_, final ChunkPrimer primer) {
        final int i = 4;
        final int j = this.world.getSeaLevel() / 2 + 1;
        final int k = 5;
        final int l = 17;
        final int i2 = 5;
        this.buffer = this.getHeights(this.buffer, p_185936_1_ * 4, 0, p_185936_2_ * 4, 5, 17, 5);
        for (int j2 = 0; j2 < 4; ++j2) {
            for (int k2 = 0; k2 < 4; ++k2) {
                for (int l2 = 0; l2 < 16; ++l2) {
                    final double d0 = 0.125;
                    double d2 = this.buffer[((j2 + 0) * 5 + k2 + 0) * 17 + l2 + 0];
                    double d3 = this.buffer[((j2 + 0) * 5 + k2 + 1) * 17 + l2 + 0];
                    double d4 = this.buffer[((j2 + 1) * 5 + k2 + 0) * 17 + l2 + 0];
                    double d5 = this.buffer[((j2 + 1) * 5 + k2 + 1) * 17 + l2 + 0];
                    final double d6 = (this.buffer[((j2 + 0) * 5 + k2 + 0) * 17 + l2 + 1] - d2) * 0.125;
                    final double d7 = (this.buffer[((j2 + 0) * 5 + k2 + 1) * 17 + l2 + 1] - d3) * 0.125;
                    final double d8 = (this.buffer[((j2 + 1) * 5 + k2 + 0) * 17 + l2 + 1] - d4) * 0.125;
                    final double d9 = (this.buffer[((j2 + 1) * 5 + k2 + 1) * 17 + l2 + 1] - d5) * 0.125;
                    for (int i3 = 0; i3 < 8; ++i3) {
                        final double d10 = 0.25;
                        double d11 = d2;
                        double d12 = d3;
                        final double d13 = (d4 - d2) * 0.25;
                        final double d14 = (d5 - d3) * 0.25;
                        for (int j3 = 0; j3 < 4; ++j3) {
                            final double d15 = 0.25;
                            double d16 = d11;
                            final double d17 = (d12 - d11) * 0.25;
                            for (int k3 = 0; k3 < 4; ++k3) {
                                IBlockState iblockstate = null;
                                if (l2 * 8 + i3 < j) {
                                    iblockstate = ChunkGeneratorHell.LAVA;
                                }
                                if (d16 > 0.0) {
                                    iblockstate = ChunkGeneratorHell.NETHERRACK;
                                }
                                final int l3 = j3 + j2 * 4;
                                final int i4 = i3 + l2 * 8;
                                final int j4 = k3 + k2 * 4;
                                primer.setBlockState(l3, i4, j4, iblockstate);
                                d16 += d17;
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
    
    public void buildSurfaces(final int p_185937_1_, final int p_185937_2_, final ChunkPrimer primer) {
        final int i = this.world.getSeaLevel() + 1;
        final double d0 = 0.03125;
        this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, p_185937_1_ * 16, p_185937_2_ * 16, 0, 16, 16, 1, 0.03125, 0.03125, 1.0);
        this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, p_185937_1_ * 16, 109, p_185937_2_ * 16, 16, 1, 16, 0.03125, 1.0, 0.03125);
        this.depthBuffer = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.depthBuffer, p_185937_1_ * 16, p_185937_2_ * 16, 0, 16, 16, 1, 0.0625, 0.0625, 0.0625);
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                final boolean flag = this.slowsandNoise[j + k * 16] + this.rand.nextDouble() * 0.2 > 0.0;
                final boolean flag2 = this.gravelNoise[j + k * 16] + this.rand.nextDouble() * 0.2 > 0.0;
                final int l = (int)(this.depthBuffer[j + k * 16] / 3.0 + 3.0 + this.rand.nextDouble() * 0.25);
                int i2 = -1;
                IBlockState iblockstate = ChunkGeneratorHell.NETHERRACK;
                IBlockState iblockstate2 = ChunkGeneratorHell.NETHERRACK;
                for (int j2 = 127; j2 >= 0; --j2) {
                    if (j2 < 127 - this.rand.nextInt(5) && j2 > this.rand.nextInt(5)) {
                        final IBlockState iblockstate3 = primer.getBlockState(k, j2, j);
                        if (iblockstate3.getBlock() != null && iblockstate3.getMaterial() != Material.AIR) {
                            if (iblockstate3.getBlock() == Blocks.NETHERRACK) {
                                if (i2 == -1) {
                                    if (l <= 0) {
                                        iblockstate = ChunkGeneratorHell.AIR;
                                        iblockstate2 = ChunkGeneratorHell.NETHERRACK;
                                    }
                                    else if (j2 >= i - 4 && j2 <= i + 1) {
                                        iblockstate = ChunkGeneratorHell.NETHERRACK;
                                        iblockstate2 = ChunkGeneratorHell.NETHERRACK;
                                        if (flag2) {
                                            iblockstate = ChunkGeneratorHell.GRAVEL;
                                            iblockstate2 = ChunkGeneratorHell.NETHERRACK;
                                        }
                                        if (flag) {
                                            iblockstate = ChunkGeneratorHell.SOUL_SAND;
                                            iblockstate2 = ChunkGeneratorHell.SOUL_SAND;
                                        }
                                    }
                                    if (j2 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
                                        iblockstate = ChunkGeneratorHell.LAVA;
                                    }
                                    i2 = l;
                                    if (j2 >= i - 1) {
                                        primer.setBlockState(k, j2, j, iblockstate);
                                    }
                                    else {
                                        primer.setBlockState(k, j2, j, iblockstate2);
                                    }
                                }
                                else if (i2 > 0) {
                                    --i2;
                                    primer.setBlockState(k, j2, j, iblockstate2);
                                }
                            }
                        }
                        else {
                            i2 = -1;
                        }
                    }
                    else {
                        primer.setBlockState(k, j2, j, ChunkGeneratorHell.BEDROCK);
                    }
                }
            }
        }
    }
    
    @Override
    public Chunk generateChunk(final int x, final int z) {
        this.rand.setSeed(x * 341873128712L + z * 132897987541L);
        final ChunkPrimer chunkprimer = new ChunkPrimer();
        this.prepareHeights(x, z, chunkprimer);
        this.buildSurfaces(x, z, chunkprimer);
        this.genNetherCaves.generate(this.world, x, z, chunkprimer);
        if (this.generateStructures) {
            this.genNetherBridge.generate(this.world, x, z, chunkprimer);
        }
        final Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        final Biome[] abiome = this.world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16);
        final byte[] abyte = chunk.getBiomeArray();
        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte)Biome.getIdForBiome(abiome[i]);
        }
        chunk.resetRelightChecks();
        return chunk;
    }
    
    private double[] getHeights(double[] p_185938_1_, final int p_185938_2_, final int p_185938_3_, final int p_185938_4_, final int p_185938_5_, final int p_185938_6_, final int p_185938_7_) {
        if (p_185938_1_ == null) {
            p_185938_1_ = new double[p_185938_5_ * p_185938_6_ * p_185938_7_];
        }
        final double d0 = 684.412;
        final double d2 = 2053.236;
        this.noiseData4 = this.scaleNoise.generateNoiseOctaves(this.noiseData4, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, 1, p_185938_7_, 1.0, 0.0, 1.0);
        this.dr = this.depthNoise.generateNoiseOctaves(this.dr, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, 1, p_185938_7_, 100.0, 0.0, 100.0);
        this.pnr = this.perlinNoise1.generateNoiseOctaves(this.pnr, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, p_185938_6_, p_185938_7_, 8.555150000000001, 34.2206, 8.555150000000001);
        this.ar = this.lperlinNoise1.generateNoiseOctaves(this.ar, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, p_185938_6_, p_185938_7_, 684.412, 2053.236, 684.412);
        this.br = this.lperlinNoise2.generateNoiseOctaves(this.br, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, p_185938_6_, p_185938_7_, 684.412, 2053.236, 684.412);
        int i = 0;
        final double[] adouble = new double[p_185938_6_];
        for (int j = 0; j < p_185938_6_; ++j) {
            adouble[j] = Math.cos(j * 3.141592653589793 * 6.0 / p_185938_6_) * 2.0;
            double d3 = j;
            if (j > p_185938_6_ / 2) {
                d3 = p_185938_6_ - 1 - j;
            }
            if (d3 < 4.0) {
                d3 = 4.0 - d3;
                final double[] array = adouble;
                final int n = j;
                array[n] -= d3 * d3 * d3 * 10.0;
            }
        }
        for (int l = 0; l < p_185938_5_; ++l) {
            for (int i2 = 0; i2 < p_185938_7_; ++i2) {
                final double d4 = 0.0;
                for (int k = 0; k < p_185938_6_; ++k) {
                    final double d5 = adouble[k];
                    final double d6 = this.ar[i] / 512.0;
                    final double d7 = this.br[i] / 512.0;
                    final double d8 = (this.pnr[i] / 10.0 + 1.0) / 2.0;
                    double d9;
                    if (d8 < 0.0) {
                        d9 = d6;
                    }
                    else if (d8 > 1.0) {
                        d9 = d7;
                    }
                    else {
                        d9 = d6 + (d7 - d6) * d8;
                    }
                    d9 -= d5;
                    if (k > p_185938_6_ - 4) {
                        final double d10 = (k - (p_185938_6_ - 4)) / 3.0f;
                        d9 = d9 * (1.0 - d10) + -10.0 * d10;
                    }
                    if (k < 0.0) {
                        double d11 = (0.0 - k) / 4.0;
                        d11 = MathHelper.clamp(d11, 0.0, 1.0);
                        d9 = d9 * (1.0 - d11) + -10.0 * d11;
                    }
                    p_185938_1_[i] = d9;
                    ++i;
                }
            }
        }
        return p_185938_1_;
    }
    
    @Override
    public void populate(final int x, final int z) {
        BlockFalling.fallInstantly = true;
        final int i = x * 16;
        final int j = z * 16;
        final BlockPos blockpos = new BlockPos(i, 0, j);
        final Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
        final ChunkPos chunkpos = new ChunkPos(x, z);
        this.genNetherBridge.generateStructure(this.world, this.rand, chunkpos);
        for (int k = 0; k < 8; ++k) {
            this.hellSpringGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
        }
        for (int i2 = 0; i2 < this.rand.nextInt(this.rand.nextInt(10) + 1) + 1; ++i2) {
            this.fireFeature.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
        }
        for (int j2 = 0; j2 < this.rand.nextInt(this.rand.nextInt(10) + 1); ++j2) {
            this.lightGemGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
        }
        for (int k2 = 0; k2 < 10; ++k2) {
            this.hellPortalGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
        }
        if (this.rand.nextBoolean()) {
            this.brownMushroomFeature.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
        }
        if (this.rand.nextBoolean()) {
            this.redMushroomFeature.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(128), this.rand.nextInt(16) + 8));
        }
        for (int l1 = 0; l1 < 16; ++l1) {
            this.quartzGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16), this.rand.nextInt(108) + 10, this.rand.nextInt(16)));
        }
        final int i3 = this.world.getSeaLevel() / 2 + 1;
        for (int m = 0; m < 4; ++m) {
            this.magmaGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16), i3 - 5 + this.rand.nextInt(10), this.rand.nextInt(16)));
        }
        for (int j3 = 0; j3 < 16; ++j3) {
            this.lavaTrapGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16), this.rand.nextInt(108) + 10, this.rand.nextInt(16)));
        }
        biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
        BlockFalling.fallInstantly = false;
    }
    
    @Override
    public boolean generateStructures(final Chunk chunkIn, final int x, final int z) {
        return false;
    }
    
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        if (creatureType == EnumCreatureType.MONSTER) {
            if (this.genNetherBridge.isInsideStructure(pos)) {
                return this.genNetherBridge.getSpawnList();
            }
            if (this.genNetherBridge.isPositionInStructure(this.world, pos) && this.world.getBlockState(pos.down()).getBlock() == Blocks.NETHER_BRICK) {
                return this.genNetherBridge.getSpawnList();
            }
        }
        final Biome biome = this.world.getBiome(pos);
        return biome.getSpawnableList(creatureType);
    }
    
    @Nullable
    @Override
    public BlockPos getNearestStructurePos(final World worldIn, final String structureName, final BlockPos position, final boolean findUnexplored) {
        return ("Fortress".equals(structureName) && this.genNetherBridge != null) ? this.genNetherBridge.getNearestStructurePos(worldIn, position, findUnexplored) : null;
    }
    
    @Override
    public boolean isInsideStructure(final World worldIn, final String structureName, final BlockPos pos) {
        return "Fortress".equals(structureName) && this.genNetherBridge != null && this.genNetherBridge.isInsideStructure(pos);
    }
    
    @Override
    public void recreateStructures(final Chunk chunkIn, final int x, final int z) {
        this.genNetherBridge.generate(this.world, x, z, null);
    }
    
    static {
        AIR = Blocks.AIR.getDefaultState();
        NETHERRACK = Blocks.NETHERRACK.getDefaultState();
        BEDROCK = Blocks.BEDROCK.getDefaultState();
        LAVA = Blocks.LAVA.getDefaultState();
        GRAVEL = Blocks.GRAVEL.getDefaultState();
        SOUL_SAND = Blocks.SOUL_SAND.getDefaultState();
    }
}
