/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.GeneratorBushFeature;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.MapGenCavesHell;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.feature.WorldGenFire;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.feature.WorldGenGlowStone2;
import net.minecraft.world.gen.feature.WorldGenHellLava;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.MapGenNetherBridge;

public class ChunkProviderHell
implements IChunkProvider {
    private final MapGenBase netherCaveGenerator;
    private final WorldGenHellLava field_177472_y;
    private double[] gravelNoise;
    double[] noiseData2;
    private final NoiseGeneratorOctaves netherNoiseGen1;
    private final NoiseGeneratorOctaves netherNoiseGen2;
    private final WorldGenGlowStone1 field_177469_u;
    public final NoiseGeneratorOctaves netherNoiseGen6;
    private final WorldGenHellLava field_177473_x;
    private double[] noiseField;
    double[] noiseData4;
    public final NoiseGeneratorOctaves netherNoiseGen7;
    double[] noiseData5;
    private final MapGenNetherBridge genNetherBridge;
    private final WorldGenGlowStone2 field_177468_v;
    double[] noiseData1;
    private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
    private final World worldObj;
    private final boolean field_177466_i;
    private final NoiseGeneratorOctaves netherNoiseGen3;
    private final WorldGenerator field_177467_w;
    double[] noiseData3;
    private final WorldGenFire field_177470_t;
    private double[] netherrackExclusivityNoise;
    private double[] slowsandNoise = new double[256];
    private final Random hellRNG;
    private final GeneratorBushFeature field_177471_z;
    private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
    private final GeneratorBushFeature field_177465_A;

    @Override
    public Chunk provideChunk(BlockPos blockPos) {
        return this.provideChunk(blockPos.getX() >> 4, blockPos.getZ() >> 4);
    }

    @Override
    public Chunk provideChunk(int n, int n2) {
        this.hellRNG.setSeed((long)n * 341873128712L + (long)n2 * 132897987541L);
        ChunkPrimer chunkPrimer = new ChunkPrimer();
        this.func_180515_a(n, n2, chunkPrimer);
        this.func_180516_b(n, n2, chunkPrimer);
        this.netherCaveGenerator.generate(this, this.worldObj, n, n2, chunkPrimer);
        if (this.field_177466_i) {
            this.genNetherBridge.generate(this, this.worldObj, n, n2, chunkPrimer);
        }
        Chunk chunk = new Chunk(this.worldObj, chunkPrimer, n, n2);
        BiomeGenBase[] biomeGenBaseArray = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, n * 16, n2 * 16, 16, 16);
        byte[] byArray = chunk.getBiomeArray();
        int n3 = 0;
        while (n3 < byArray.length) {
            byArray[n3] = (byte)biomeGenBaseArray[n3].biomeID;
            ++n3;
        }
        chunk.resetRelightChecks();
        return chunk;
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumCreatureType, BlockPos blockPos) {
        if (enumCreatureType == EnumCreatureType.MONSTER) {
            if (this.genNetherBridge.func_175795_b(blockPos)) {
                return this.genNetherBridge.getSpawnList();
            }
            if (this.genNetherBridge.func_175796_a(this.worldObj, blockPos) && this.worldObj.getBlockState(blockPos.down()).getBlock() == Blocks.nether_brick) {
                return this.genNetherBridge.getSpawnList();
            }
        }
        BiomeGenBase biomeGenBase = this.worldObj.getBiomeGenForCoords(blockPos);
        return biomeGenBase.getSpawnableList(enumCreatureType);
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    private double[] initializeNoiseField(double[] dArray, int n, int n2, int n3, int n4, int n5, int n6) {
        if (dArray == null) {
            dArray = new double[n4 * n5 * n6];
        }
        double d = 684.412;
        double d2 = 2053.236;
        this.noiseData4 = this.netherNoiseGen6.generateNoiseOctaves(this.noiseData4, n, n2, n3, n4, 1, n6, 1.0, 0.0, 1.0);
        this.noiseData5 = this.netherNoiseGen7.generateNoiseOctaves(this.noiseData5, n, n2, n3, n4, 1, n6, 100.0, 0.0, 100.0);
        this.noiseData1 = this.netherNoiseGen3.generateNoiseOctaves(this.noiseData1, n, n2, n3, n4, n5, n6, d / 80.0, d2 / 60.0, d / 80.0);
        this.noiseData2 = this.netherNoiseGen1.generateNoiseOctaves(this.noiseData2, n, n2, n3, n4, n5, n6, d, d2, d);
        this.noiseData3 = this.netherNoiseGen2.generateNoiseOctaves(this.noiseData3, n, n2, n3, n4, n5, n6, d, d2, d);
        int n7 = 0;
        double[] dArray2 = new double[n5];
        int n8 = 0;
        while (n8 < n5) {
            dArray2[n8] = Math.cos((double)n8 * Math.PI * 6.0 / (double)n5) * 2.0;
            double d3 = n8;
            if (n8 > n5 / 2) {
                d3 = n5 - 1 - n8;
            }
            if (d3 < 4.0) {
                d3 = 4.0 - d3;
                int n9 = n8;
                dArray2[n9] = dArray2[n9] - d3 * d3 * d3 * 10.0;
            }
            ++n8;
        }
        n8 = 0;
        while (n8 < n4) {
            int n10 = 0;
            while (n10 < n6) {
                double d4 = 0.0;
                int n11 = 0;
                while (n11 < n5) {
                    double d5;
                    double d6 = 0.0;
                    double d7 = dArray2[n11];
                    double d8 = this.noiseData2[n7] / 512.0;
                    double d9 = this.noiseData3[n7] / 512.0;
                    double d10 = (this.noiseData1[n7] / 10.0 + 1.0) / 2.0;
                    d6 = d10 < 0.0 ? d8 : (d10 > 1.0 ? d9 : d8 + (d9 - d8) * d10);
                    d6 -= d7;
                    if (n11 > n5 - 4) {
                        d5 = (float)(n11 - (n5 - 4)) / 3.0f;
                        d6 = d6 * (1.0 - d5) + -10.0 * d5;
                    }
                    if ((double)n11 < d4) {
                        d5 = (d4 - (double)n11) / 4.0;
                        d5 = MathHelper.clamp_double(d5, 0.0, 1.0);
                        d6 = d6 * (1.0 - d5) + -10.0 * d5;
                    }
                    dArray[n7] = d6;
                    ++n7;
                    ++n11;
                }
                ++n10;
            }
            ++n8;
        }
        return dArray;
    }

    @Override
    public BlockPos getStrongholdGen(World world, String string, BlockPos blockPos) {
        return null;
    }

    @Override
    public void saveExtraData() {
    }

    public void func_180516_b(int n, int n2, ChunkPrimer chunkPrimer) {
        int n3 = this.worldObj.func_181545_F() + 1;
        double d = 0.03125;
        this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, n * 16, n2 * 16, 0, 16, 16, 1, d, d, 1.0);
        this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, n * 16, 109, n2 * 16, 16, 1, 16, d, 1.0, d);
        this.netherrackExclusivityNoise = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.netherrackExclusivityNoise, n * 16, n2 * 16, 0, 16, 16, 1, d * 2.0, d * 2.0, d * 2.0);
        int n4 = 0;
        while (n4 < 16) {
            int n5 = 0;
            while (n5 < 16) {
                boolean bl = this.slowsandNoise[n4 + n5 * 16] + this.hellRNG.nextDouble() * 0.2 > 0.0;
                boolean bl2 = this.gravelNoise[n4 + n5 * 16] + this.hellRNG.nextDouble() * 0.2 > 0.0;
                int n6 = (int)(this.netherrackExclusivityNoise[n4 + n5 * 16] / 3.0 + 3.0 + this.hellRNG.nextDouble() * 0.25);
                int n7 = -1;
                IBlockState iBlockState = Blocks.netherrack.getDefaultState();
                IBlockState iBlockState2 = Blocks.netherrack.getDefaultState();
                int n8 = 127;
                while (n8 >= 0) {
                    if (n8 < 127 - this.hellRNG.nextInt(5) && n8 > this.hellRNG.nextInt(5)) {
                        IBlockState iBlockState3 = chunkPrimer.getBlockState(n5, n8, n4);
                        if (iBlockState3.getBlock() != null && iBlockState3.getBlock().getMaterial() != Material.air) {
                            if (iBlockState3.getBlock() == Blocks.netherrack) {
                                if (n7 == -1) {
                                    if (n6 <= 0) {
                                        iBlockState = null;
                                        iBlockState2 = Blocks.netherrack.getDefaultState();
                                    } else if (n8 >= n3 - 4 && n8 <= n3 + 1) {
                                        iBlockState = Blocks.netherrack.getDefaultState();
                                        iBlockState2 = Blocks.netherrack.getDefaultState();
                                        if (bl2) {
                                            iBlockState = Blocks.gravel.getDefaultState();
                                            iBlockState2 = Blocks.netherrack.getDefaultState();
                                        }
                                        if (bl) {
                                            iBlockState = Blocks.soul_sand.getDefaultState();
                                            iBlockState2 = Blocks.soul_sand.getDefaultState();
                                        }
                                    }
                                    if (n8 < n3 && (iBlockState == null || iBlockState.getBlock().getMaterial() == Material.air)) {
                                        iBlockState = Blocks.lava.getDefaultState();
                                    }
                                    n7 = n6;
                                    if (n8 >= n3 - 1) {
                                        chunkPrimer.setBlockState(n5, n8, n4, iBlockState);
                                    } else {
                                        chunkPrimer.setBlockState(n5, n8, n4, iBlockState2);
                                    }
                                } else if (n7 > 0) {
                                    --n7;
                                    chunkPrimer.setBlockState(n5, n8, n4, iBlockState2);
                                }
                            }
                        } else {
                            n7 = -1;
                        }
                    } else {
                        chunkPrimer.setBlockState(n5, n8, n4, Blocks.bedrock.getDefaultState());
                    }
                    --n8;
                }
                ++n5;
            }
            ++n4;
        }
    }

    @Override
    public String makeString() {
        return "HellRandomLevelSource";
    }

    @Override
    public boolean chunkExists(int n, int n2) {
        return true;
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    public void func_180515_a(int n, int n2, ChunkPrimer chunkPrimer) {
        int n3 = 4;
        int n4 = this.worldObj.func_181545_F() / 2 + 1;
        int n5 = n3 + 1;
        int n6 = 17;
        int n7 = n3 + 1;
        this.noiseField = this.initializeNoiseField(this.noiseField, n * n3, 0, n2 * n3, n5, n6, n7);
        int n8 = 0;
        while (n8 < n3) {
            int n9 = 0;
            while (n9 < n3) {
                int n10 = 0;
                while (n10 < 16) {
                    double d = 0.125;
                    double d2 = this.noiseField[((n8 + 0) * n7 + n9 + 0) * n6 + n10 + 0];
                    double d3 = this.noiseField[((n8 + 0) * n7 + n9 + 1) * n6 + n10 + 0];
                    double d4 = this.noiseField[((n8 + 1) * n7 + n9 + 0) * n6 + n10 + 0];
                    double d5 = this.noiseField[((n8 + 1) * n7 + n9 + 1) * n6 + n10 + 0];
                    double d6 = (this.noiseField[((n8 + 0) * n7 + n9 + 0) * n6 + n10 + 1] - d2) * d;
                    double d7 = (this.noiseField[((n8 + 0) * n7 + n9 + 1) * n6 + n10 + 1] - d3) * d;
                    double d8 = (this.noiseField[((n8 + 1) * n7 + n9 + 0) * n6 + n10 + 1] - d4) * d;
                    double d9 = (this.noiseField[((n8 + 1) * n7 + n9 + 1) * n6 + n10 + 1] - d5) * d;
                    int n11 = 0;
                    while (n11 < 8) {
                        double d10 = 0.25;
                        double d11 = d2;
                        double d12 = d3;
                        double d13 = (d4 - d2) * d10;
                        double d14 = (d5 - d3) * d10;
                        int n12 = 0;
                        while (n12 < 4) {
                            double d15 = 0.25;
                            double d16 = d11;
                            double d17 = (d12 - d11) * d15;
                            int n13 = 0;
                            while (n13 < 4) {
                                IBlockState iBlockState = null;
                                if (n10 * 8 + n11 < n4) {
                                    iBlockState = Blocks.lava.getDefaultState();
                                }
                                if (d16 > 0.0) {
                                    iBlockState = Blocks.netherrack.getDefaultState();
                                }
                                int n14 = n12 + n8 * 4;
                                int n15 = n11 + n10 * 8;
                                int n16 = n13 + n9 * 4;
                                chunkPrimer.setBlockState(n14, n15, n16, iBlockState);
                                d16 += d17;
                                ++n13;
                            }
                            d11 += d13;
                            d12 += d14;
                            ++n12;
                        }
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                        d5 += d9;
                        ++n11;
                    }
                    ++n10;
                }
                ++n9;
            }
            ++n8;
        }
    }

    @Override
    public void populate(IChunkProvider iChunkProvider, int n, int n2) {
        BlockFalling.fallInstantly = true;
        BlockPos blockPos = new BlockPos(n * 16, 0, n2 * 16);
        ChunkCoordIntPair chunkCoordIntPair = new ChunkCoordIntPair(n, n2);
        this.genNetherBridge.generateStructure(this.worldObj, this.hellRNG, chunkCoordIntPair);
        int n3 = 0;
        while (n3 < 8) {
            this.field_177472_y.generate(this.worldObj, this.hellRNG, blockPos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
            ++n3;
        }
        n3 = 0;
        while (n3 < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1) + 1) {
            this.field_177470_t.generate(this.worldObj, this.hellRNG, blockPos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
            ++n3;
        }
        n3 = 0;
        while (n3 < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1)) {
            this.field_177469_u.generate(this.worldObj, this.hellRNG, blockPos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
            ++n3;
        }
        n3 = 0;
        while (n3 < 10) {
            this.field_177468_v.generate(this.worldObj, this.hellRNG, blockPos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
            ++n3;
        }
        if (this.hellRNG.nextBoolean()) {
            this.field_177471_z.generate(this.worldObj, this.hellRNG, blockPos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        if (this.hellRNG.nextBoolean()) {
            this.field_177465_A.generate(this.worldObj, this.hellRNG, blockPos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        n3 = 0;
        while (n3 < 16) {
            this.field_177467_w.generate(this.worldObj, this.hellRNG, blockPos.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
            ++n3;
        }
        n3 = 0;
        while (n3 < 16) {
            this.field_177473_x.generate(this.worldObj, this.hellRNG, blockPos.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
            ++n3;
        }
        BlockFalling.fallInstantly = false;
    }

    @Override
    public boolean saveChunks(boolean bl, IProgressUpdate iProgressUpdate) {
        return true;
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public boolean func_177460_a(IChunkProvider iChunkProvider, Chunk chunk, int n, int n2) {
        return false;
    }

    @Override
    public void recreateStructures(Chunk chunk, int n, int n2) {
        this.genNetherBridge.generate(this, this.worldObj, n, n2, null);
    }

    public ChunkProviderHell(World world, boolean bl, long l) {
        this.gravelNoise = new double[256];
        this.netherrackExclusivityNoise = new double[256];
        this.field_177470_t = new WorldGenFire();
        this.field_177469_u = new WorldGenGlowStone1();
        this.field_177468_v = new WorldGenGlowStone2();
        this.field_177467_w = new WorldGenMinable(Blocks.quartz_ore.getDefaultState(), 14, BlockHelper.forBlock(Blocks.netherrack));
        this.field_177473_x = new WorldGenHellLava(Blocks.flowing_lava, true);
        this.field_177472_y = new WorldGenHellLava(Blocks.flowing_lava, false);
        this.field_177471_z = new GeneratorBushFeature(Blocks.brown_mushroom);
        this.field_177465_A = new GeneratorBushFeature(Blocks.red_mushroom);
        this.genNetherBridge = new MapGenNetherBridge();
        this.netherCaveGenerator = new MapGenCavesHell();
        this.worldObj = world;
        this.field_177466_i = bl;
        this.hellRNG = new Random(l);
        this.netherNoiseGen1 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.netherNoiseGen2 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.netherNoiseGen3 = new NoiseGeneratorOctaves(this.hellRNG, 8);
        this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.netherNoiseGen6 = new NoiseGeneratorOctaves(this.hellRNG, 10);
        this.netherNoiseGen7 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        world.func_181544_b(63);
    }
}

