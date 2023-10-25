/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCavesHell;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenBush;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.feature.WorldGenGlowStone2;
import net.minecraft.world.gen.feature.WorldGenHellLava;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.MapGenNetherBridge;

public class ChunkGeneratorHell
implements IChunkGenerator {
    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    protected static final IBlockState NETHERRACK = Blocks.NETHERRACK.getDefaultState();
    protected static final IBlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
    protected static final IBlockState LAVA = Blocks.LAVA.getDefaultState();
    protected static final IBlockState GRAVEL = Blocks.GRAVEL.getDefaultState();
    protected static final IBlockState SOUL_SAND = Blocks.SOUL_SAND.getDefaultState();
    private final World world;
    private final boolean generateStructures;
    private final Random rand;
    private double[] slowsandNoise = new double[256];
    private double[] gravelNoise = new double[256];
    private double[] depthBuffer = new double[256];
    private double[] buffer;
    private final NoiseGeneratorOctaves lperlinNoise1;
    private final NoiseGeneratorOctaves lperlinNoise2;
    private final NoiseGeneratorOctaves perlinNoise1;
    private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
    private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
    public final NoiseGeneratorOctaves scaleNoise;
    public final NoiseGeneratorOctaves depthNoise;
    private final WorldGenFire fireFeature = new WorldGenFire();
    private final WorldGenGlowStone1 lightGemGen = new WorldGenGlowStone1();
    private final WorldGenGlowStone2 hellPortalGen = new WorldGenGlowStone2();
    private final WorldGenerator quartzGen = new WorldGenMinable(Blocks.QUARTZ_ORE.getDefaultState(), 14, BlockMatcher.forBlock(Blocks.NETHERRACK));
    private final WorldGenerator magmaGen = new WorldGenMinable(Blocks.MAGMA.getDefaultState(), 33, BlockMatcher.forBlock(Blocks.NETHERRACK));
    private final WorldGenHellLava lavaTrapGen = new WorldGenHellLava(Blocks.FLOWING_LAVA, true);
    private final WorldGenHellLava hellSpringGen = new WorldGenHellLava(Blocks.FLOWING_LAVA, false);
    private final WorldGenBush brownMushroomFeature = new WorldGenBush(Blocks.BROWN_MUSHROOM);
    private final WorldGenBush redMushroomFeature = new WorldGenBush(Blocks.RED_MUSHROOM);
    private final MapGenNetherBridge genNetherBridge = new MapGenNetherBridge();
    private final MapGenBase genNetherCaves = new MapGenCavesHell();
    double[] pnr;
    double[] ar;
    double[] br;
    double[] noiseData4;
    double[] dr;

    public ChunkGeneratorHell(World worldIn, boolean p_i45637_2_, long seed) {
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

    public void prepareHeights(int p_185936_1_, int p_185936_2_, ChunkPrimer primer) {
        int i = 4;
        int j = this.world.getSeaLevel() / 2 + 1;
        int k = 5;
        int l = 17;
        int i1 = 5;
        this.buffer = this.getHeights(this.buffer, p_185936_1_ * 4, 0, p_185936_2_ * 4, 5, 17, 5);
        for (int j1 = 0; j1 < 4; ++j1) {
            for (int k1 = 0; k1 < 4; ++k1) {
                for (int l1 = 0; l1 < 16; ++l1) {
                    double d0 = 0.125;
                    double d1 = this.buffer[((j1 + 0) * 5 + k1 + 0) * 17 + l1 + 0];
                    double d2 = this.buffer[((j1 + 0) * 5 + k1 + 1) * 17 + l1 + 0];
                    double d3 = this.buffer[((j1 + 1) * 5 + k1 + 0) * 17 + l1 + 0];
                    double d4 = this.buffer[((j1 + 1) * 5 + k1 + 1) * 17 + l1 + 0];
                    double d5 = (this.buffer[((j1 + 0) * 5 + k1 + 0) * 17 + l1 + 1] - d1) * 0.125;
                    double d6 = (this.buffer[((j1 + 0) * 5 + k1 + 1) * 17 + l1 + 1] - d2) * 0.125;
                    double d7 = (this.buffer[((j1 + 1) * 5 + k1 + 0) * 17 + l1 + 1] - d3) * 0.125;
                    double d8 = (this.buffer[((j1 + 1) * 5 + k1 + 1) * 17 + l1 + 1] - d4) * 0.125;
                    for (int i2 = 0; i2 < 8; ++i2) {
                        double d9 = 0.25;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * 0.25;
                        double d13 = (d4 - d2) * 0.25;
                        for (int j2 = 0; j2 < 4; ++j2) {
                            double d14 = 0.25;
                            double d15 = d10;
                            double d16 = (d11 - d10) * 0.25;
                            for (int k2 = 0; k2 < 4; ++k2) {
                                IBlockState iblockstate = null;
                                if (l1 * 8 + i2 < j) {
                                    iblockstate = LAVA;
                                }
                                if (d15 > 0.0) {
                                    iblockstate = NETHERRACK;
                                }
                                int l2 = j2 + j1 * 4;
                                int i3 = i2 + l1 * 8;
                                int j3 = k2 + k1 * 4;
                                primer.setBlockState(l2, i3, j3, iblockstate);
                                d15 += d16;
                            }
                            d10 += d12;
                            d11 += d13;
                        }
                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    public void buildSurfaces(int p_185937_1_, int p_185937_2_, ChunkPrimer primer) {
        int i = this.world.getSeaLevel() + 1;
        double d0 = 0.03125;
        this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, p_185937_1_ * 16, p_185937_2_ * 16, 0, 16, 16, 1, 0.03125, 0.03125, 1.0);
        this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, p_185937_1_ * 16, 109, p_185937_2_ * 16, 16, 1, 16, 0.03125, 1.0, 0.03125);
        this.depthBuffer = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.depthBuffer, p_185937_1_ * 16, p_185937_2_ * 16, 0, 16, 16, 1, 0.0625, 0.0625, 0.0625);
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                boolean flag = this.slowsandNoise[j + k * 16] + this.rand.nextDouble() * 0.2 > 0.0;
                boolean flag1 = this.gravelNoise[j + k * 16] + this.rand.nextDouble() * 0.2 > 0.0;
                int l = (int)(this.depthBuffer[j + k * 16] / 3.0 + 3.0 + this.rand.nextDouble() * 0.25);
                int i1 = -1;
                IBlockState iblockstate = NETHERRACK;
                IBlockState iblockstate1 = NETHERRACK;
                for (int j1 = 127; j1 >= 0; --j1) {
                    if (j1 < 127 - this.rand.nextInt(5) && j1 > this.rand.nextInt(5)) {
                        IBlockState iblockstate2 = primer.getBlockState(k, j1, j);
                        if (iblockstate2.getBlock() != null && iblockstate2.getMaterial() != Material.AIR) {
                            if (iblockstate2.getBlock() != Blocks.NETHERRACK) continue;
                            if (i1 == -1) {
                                if (l <= 0) {
                                    iblockstate = AIR;
                                    iblockstate1 = NETHERRACK;
                                } else if (j1 >= i - 4 && j1 <= i + 1) {
                                    iblockstate = NETHERRACK;
                                    iblockstate1 = NETHERRACK;
                                    if (flag1) {
                                        iblockstate = GRAVEL;
                                        iblockstate1 = NETHERRACK;
                                    }
                                    if (flag) {
                                        iblockstate = SOUL_SAND;
                                        iblockstate1 = SOUL_SAND;
                                    }
                                }
                                if (j1 < i && (iblockstate == null || iblockstate.getMaterial() == Material.AIR)) {
                                    iblockstate = LAVA;
                                }
                                i1 = l;
                                if (j1 >= i - 1) {
                                    primer.setBlockState(k, j1, j, iblockstate);
                                    continue;
                                }
                                primer.setBlockState(k, j1, j, iblockstate1);
                                continue;
                            }
                            if (i1 <= 0) continue;
                            --i1;
                            primer.setBlockState(k, j1, j, iblockstate1);
                            continue;
                        }
                        i1 = -1;
                        continue;
                    }
                    primer.setBlockState(k, j1, j, BEDROCK);
                }
            }
        }
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.prepareHeights(x, z, chunkprimer);
        this.buildSurfaces(x, z, chunkprimer);
        this.genNetherCaves.generate(this.world, x, z, chunkprimer);
        if (this.generateStructures) {
            this.genNetherBridge.generate(this.world, x, z, chunkprimer);
        }
        Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        Biome[] abiome = this.world.getBiomeProvider().getBiomes(null, x * 16, z * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();
        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte)Biome.getIdForBiome(abiome[i]);
        }
        chunk.resetRelightChecks();
        return chunk;
    }

    private double[] getHeights(double[] p_185938_1_, int p_185938_2_, int p_185938_3_, int p_185938_4_, int p_185938_5_, int p_185938_6_, int p_185938_7_) {
        if (p_185938_1_ == null) {
            p_185938_1_ = new double[p_185938_5_ * p_185938_6_ * p_185938_7_];
        }
        double d0 = 684.412;
        double d1 = 2053.236;
        this.noiseData4 = this.scaleNoise.generateNoiseOctaves(this.noiseData4, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, 1, p_185938_7_, 1.0, 0.0, 1.0);
        this.dr = this.depthNoise.generateNoiseOctaves(this.dr, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, 1, p_185938_7_, 100.0, 0.0, 100.0);
        this.pnr = this.perlinNoise1.generateNoiseOctaves(this.pnr, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, p_185938_6_, p_185938_7_, 8.555150000000001, 34.2206, 8.555150000000001);
        this.ar = this.lperlinNoise1.generateNoiseOctaves(this.ar, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, p_185938_6_, p_185938_7_, 684.412, 2053.236, 684.412);
        this.br = this.lperlinNoise2.generateNoiseOctaves(this.br, p_185938_2_, p_185938_3_, p_185938_4_, p_185938_5_, p_185938_6_, p_185938_7_, 684.412, 2053.236, 684.412);
        int i = 0;
        double[] adouble = new double[p_185938_6_];
        for (int j = 0; j < p_185938_6_; ++j) {
            adouble[j] = Math.cos((double)j * Math.PI * 6.0 / (double)p_185938_6_) * 2.0;
            double d2 = j;
            if (j > p_185938_6_ / 2) {
                d2 = p_185938_6_ - 1 - j;
            }
            if (!(d2 < 4.0)) continue;
            d2 = 4.0 - d2;
            int n = j;
            adouble[n] = adouble[n] - d2 * d2 * d2 * 10.0;
        }
        for (int l = 0; l < p_185938_5_; ++l) {
            for (int i1 = 0; i1 < p_185938_7_; ++i1) {
                double d3 = 0.0;
                for (int k = 0; k < p_185938_6_; ++k) {
                    double d4 = adouble[k];
                    double d5 = this.ar[i] / 512.0;
                    double d6 = this.br[i] / 512.0;
                    double d7 = (this.pnr[i] / 10.0 + 1.0) / 2.0;
                    double d8 = d7 < 0.0 ? d5 : (d7 > 1.0 ? d6 : d5 + (d6 - d5) * d7);
                    d8 -= d4;
                    if (k > p_185938_6_ - 4) {
                        double d9 = (float)(k - (p_185938_6_ - 4)) / 3.0f;
                        d8 = d8 * (1.0 - d9) + -10.0 * d9;
                    }
                    if ((double)k < 0.0) {
                        double d10 = (0.0 - (double)k) / 4.0;
                        d10 = MathHelper.clamp(d10, 0.0, 1.0);
                        d8 = d8 * (1.0 - d10) + -10.0 * d10;
                    }
                    p_185938_1_[i] = d8;
                    ++i;
                }
            }
        }
        return p_185938_1_;
    }

    @Override
    public void populate(int x, int z) {
        BlockFalling.fallInstantly = true;
        int i = x * 16;
        int j = z * 16;
        BlockPos blockpos = new BlockPos(i, 0, j);
        Biome biome = this.world.getBiome(blockpos.add(16, 0, 16));
        ChunkPos chunkpos = new ChunkPos(x, z);
        this.genNetherBridge.generateStructure(this.world, this.rand, chunkpos);
        for (int k = 0; k < 8; ++k) {
            this.hellSpringGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
        }
        for (int i1 = 0; i1 < this.rand.nextInt(this.rand.nextInt(10) + 1) + 1; ++i1) {
            this.fireFeature.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
        }
        for (int j1 = 0; j1 < this.rand.nextInt(this.rand.nextInt(10) + 1); ++j1) {
            this.lightGemGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, this.rand.nextInt(120) + 4, this.rand.nextInt(16) + 8));
        }
        for (int k1 = 0; k1 < 10; ++k1) {
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
        int i2 = this.world.getSeaLevel() / 2 + 1;
        for (int l = 0; l < 4; ++l) {
            this.magmaGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16), i2 - 5 + this.rand.nextInt(10), this.rand.nextInt(16)));
        }
        for (int j2 = 0; j2 < 16; ++j2) {
            this.lavaTrapGen.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16), this.rand.nextInt(108) + 10, this.rand.nextInt(16)));
        }
        biome.decorate(this.world, this.rand, new BlockPos(i, 0, j));
        BlockFalling.fallInstantly = false;
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        if (creatureType == EnumCreatureType.MONSTER) {
            if (this.genNetherBridge.isInsideStructure(pos)) {
                return this.genNetherBridge.getSpawnList();
            }
            if (this.genNetherBridge.isPositionInStructure(this.world, pos) && this.world.getBlockState(pos.down()).getBlock() == Blocks.NETHER_BRICK) {
                return this.genNetherBridge.getSpawnList();
            }
        }
        Biome biome = this.world.getBiome(pos);
        return biome.getSpawnableList(creatureType);
    }

    @Override
    @Nullable
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_) {
        return "Fortress".equals(structureName) && this.genNetherBridge != null ? this.genNetherBridge.getClosestStrongholdPos(worldIn, position, p_180513_4_) : null;
    }

    @Override
    public boolean func_193414_a(World p_193414_1_, String p_193414_2_, BlockPos p_193414_3_) {
        return "Fortress".equals(p_193414_2_) && this.genNetherBridge != null ? this.genNetherBridge.isInsideStructure(p_193414_3_) : false;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {
        this.genNetherBridge.generate(this.world, x, z, null);
    }
}

