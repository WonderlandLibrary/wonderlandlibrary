/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorOctaves;

public class ChunkProviderEnd
implements IChunkProvider {
    double[] noiseData2;
    private NoiseGeneratorOctaves noiseGen3;
    private BiomeGenBase[] biomesForGeneration;
    public NoiseGeneratorOctaves noiseGen5;
    private double[] densities;
    private Random endRNG;
    double[] noiseData1;
    private NoiseGeneratorOctaves noiseGen2;
    public NoiseGeneratorOctaves noiseGen4;
    double[] noiseData5;
    double[] noiseData3;
    private World endWorld;
    double[] noiseData4;
    private NoiseGeneratorOctaves noiseGen1;

    @Override
    public Chunk provideChunk(int n, int n2) {
        this.endRNG.setSeed((long)n * 341873128712L + (long)n2 * 132897987541L);
        ChunkPrimer chunkPrimer = new ChunkPrimer();
        this.biomesForGeneration = this.endWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, n * 16, n2 * 16, 16, 16);
        this.func_180520_a(n, n2, chunkPrimer);
        this.func_180519_a(chunkPrimer);
        Chunk chunk = new Chunk(this.endWorld, chunkPrimer, n, n2);
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
    public void saveExtraData() {
    }

    @Override
    public void recreateStructures(Chunk chunk, int n, int n2) {
    }

    @Override
    public boolean func_177460_a(IChunkProvider iChunkProvider, Chunk chunk, int n, int n2) {
        return false;
    }

    @Override
    public void populate(IChunkProvider iChunkProvider, int n, int n2) {
        BlockFalling.fallInstantly = true;
        BlockPos blockPos = new BlockPos(n * 16, 0, n2 * 16);
        this.endWorld.getBiomeGenForCoords(blockPos.add(16, 0, 16)).decorate(this.endWorld, this.endWorld.rand, blockPos);
        BlockFalling.fallInstantly = false;
    }

    @Override
    public Chunk provideChunk(BlockPos blockPos) {
        return this.provideChunk(blockPos.getX() >> 4, blockPos.getZ() >> 4);
    }

    public void func_180519_a(ChunkPrimer chunkPrimer) {
        int n = 0;
        while (n < 16) {
            int n2 = 0;
            while (n2 < 16) {
                int n3 = 1;
                int n4 = -1;
                IBlockState iBlockState = Blocks.end_stone.getDefaultState();
                IBlockState iBlockState2 = Blocks.end_stone.getDefaultState();
                int n5 = 127;
                while (n5 >= 0) {
                    IBlockState iBlockState3 = chunkPrimer.getBlockState(n, n5, n2);
                    if (iBlockState3.getBlock().getMaterial() == Material.air) {
                        n4 = -1;
                    } else if (iBlockState3.getBlock() == Blocks.stone) {
                        if (n4 == -1) {
                            if (n3 <= 0) {
                                iBlockState = Blocks.air.getDefaultState();
                                iBlockState2 = Blocks.end_stone.getDefaultState();
                            }
                            n4 = n3;
                            if (n5 >= 0) {
                                chunkPrimer.setBlockState(n, n5, n2, iBlockState);
                            } else {
                                chunkPrimer.setBlockState(n, n5, n2, iBlockState2);
                            }
                        } else if (n4 > 0) {
                            --n4;
                            chunkPrimer.setBlockState(n, n5, n2, iBlockState2);
                        }
                    }
                    --n5;
                }
                ++n2;
            }
            ++n;
        }
    }

    public ChunkProviderEnd(World world, long l) {
        this.endWorld = world;
        this.endRNG = new Random(l);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.endRNG, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.endRNG, 10);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.endRNG, 16);
    }

    @Override
    public boolean canSave() {
        return true;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;
    }

    @Override
    public boolean saveChunks(boolean bl, IProgressUpdate iProgressUpdate) {
        return true;
    }

    @Override
    public String makeString() {
        return "RandomLevelSource";
    }

    @Override
    public boolean chunkExists(int n, int n2) {
        return true;
    }

    private double[] initializeNoiseField(double[] dArray, int n, int n2, int n3, int n4, int n5, int n6) {
        if (dArray == null) {
            dArray = new double[n4 * n5 * n6];
        }
        double d = 684.412;
        double d2 = 684.412;
        this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, n, n3, n4, n6, 1.121, 1.121, 0.5);
        this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, n, n3, n4, n6, 200.0, 200.0, 0.5);
        this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, n, n2, n3, n4, n5, n6, (d *= 2.0) / 80.0, d2 / 160.0, d / 80.0);
        this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, n, n2, n3, n4, n5, n6, d, d2, d);
        this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, n, n2, n3, n4, n5, n6, d, d2, d);
        int n7 = 0;
        int n8 = 0;
        while (n8 < n4) {
            int n9 = 0;
            while (n9 < n6) {
                float f = (float)(n8 + n) / 1.0f;
                float f2 = (float)(n9 + n3) / 1.0f;
                float f3 = 100.0f - MathHelper.sqrt_float(f * f + f2 * f2) * 8.0f;
                if (f3 > 80.0f) {
                    f3 = 80.0f;
                }
                if (f3 < -100.0f) {
                    f3 = -100.0f;
                }
                int n10 = 0;
                while (n10 < n5) {
                    double d3;
                    double d4 = 0.0;
                    double d5 = this.noiseData2[n7] / 512.0;
                    double d6 = this.noiseData3[n7] / 512.0;
                    double d7 = (this.noiseData1[n7] / 10.0 + 1.0) / 2.0;
                    d4 = d7 < 0.0 ? d5 : (d7 > 1.0 ? d6 : d5 + (d6 - d5) * d7);
                    d4 -= 8.0;
                    d4 += (double)f3;
                    int n11 = 2;
                    if (n10 > n5 / 2 - n11) {
                        d3 = (float)(n10 - (n5 / 2 - n11)) / 64.0f;
                        d3 = MathHelper.clamp_double(d3, 0.0, 1.0);
                        d4 = d4 * (1.0 - d3) + -3000.0 * d3;
                    }
                    if (n10 < (n11 = 8)) {
                        d3 = (float)(n11 - n10) / ((float)n11 - 1.0f);
                        d4 = d4 * (1.0 - d3) + -30.0 * d3;
                    }
                    dArray[n7] = d4;
                    ++n7;
                    ++n10;
                }
                ++n9;
            }
            ++n8;
        }
        return dArray;
    }

    public void func_180520_a(int n, int n2, ChunkPrimer chunkPrimer) {
        int n3 = 2;
        int n4 = n3 + 1;
        int n5 = 33;
        int n6 = n3 + 1;
        this.densities = this.initializeNoiseField(this.densities, n * n3, 0, n2 * n3, n4, n5, n6);
        int n7 = 0;
        while (n7 < n3) {
            int n8 = 0;
            while (n8 < n3) {
                int n9 = 0;
                while (n9 < 32) {
                    double d = 0.25;
                    double d2 = this.densities[((n7 + 0) * n6 + n8 + 0) * n5 + n9 + 0];
                    double d3 = this.densities[((n7 + 0) * n6 + n8 + 1) * n5 + n9 + 0];
                    double d4 = this.densities[((n7 + 1) * n6 + n8 + 0) * n5 + n9 + 0];
                    double d5 = this.densities[((n7 + 1) * n6 + n8 + 1) * n5 + n9 + 0];
                    double d6 = (this.densities[((n7 + 0) * n6 + n8 + 0) * n5 + n9 + 1] - d2) * d;
                    double d7 = (this.densities[((n7 + 0) * n6 + n8 + 1) * n5 + n9 + 1] - d3) * d;
                    double d8 = (this.densities[((n7 + 1) * n6 + n8 + 0) * n5 + n9 + 1] - d4) * d;
                    double d9 = (this.densities[((n7 + 1) * n6 + n8 + 1) * n5 + n9 + 1] - d5) * d;
                    int n10 = 0;
                    while (n10 < 4) {
                        double d10 = 0.125;
                        double d11 = d2;
                        double d12 = d3;
                        double d13 = (d4 - d2) * d10;
                        double d14 = (d5 - d3) * d10;
                        int n11 = 0;
                        while (n11 < 8) {
                            double d15 = 0.125;
                            double d16 = d11;
                            double d17 = (d12 - d11) * d15;
                            int n12 = 0;
                            while (n12 < 8) {
                                IBlockState iBlockState = null;
                                if (d16 > 0.0) {
                                    iBlockState = Blocks.end_stone.getDefaultState();
                                }
                                int n13 = n11 + n7 * 8;
                                int n14 = n10 + n9 * 4;
                                int n15 = n12 + n8 * 8;
                                chunkPrimer.setBlockState(n13, n14, n15, iBlockState);
                                d16 += d17;
                                ++n12;
                            }
                            d11 += d13;
                            d12 += d14;
                            ++n11;
                        }
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                        d5 += d9;
                        ++n10;
                    }
                    ++n9;
                }
                ++n8;
            }
            ++n7;
        }
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType enumCreatureType, BlockPos blockPos) {
        return this.endWorld.getBiomeGenForCoords(blockPos).getSpawnableList(enumCreatureType);
    }

    @Override
    public BlockPos getStrongholdGen(World world, String string, BlockPos blockPos) {
        return null;
    }
}

