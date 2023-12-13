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
    private Random endRNG;
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    public NoiseGeneratorOctaves noiseGen4;
    public NoiseGeneratorOctaves noiseGen5;
    private World endWorld;
    private double[] densities;
    private BiomeGenBase[] biomesForGeneration;
    double[] noiseData1;
    double[] noiseData2;
    double[] noiseData3;
    double[] noiseData4;
    double[] noiseData5;

    public ChunkProviderEnd(World worldIn, long p_i2007_2_) {
        this.endWorld = worldIn;
        this.endRNG = new Random(p_i2007_2_);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.endRNG, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.endRNG, 10);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.endRNG, 16);
    }

    public void func_180520_a(int p_180520_1_, int p_180520_2_, ChunkPrimer p_180520_3_) {
        int i2 = 2;
        int j2 = i2 + 1;
        int k2 = 33;
        int l2 = i2 + 1;
        this.densities = this.initializeNoiseField(this.densities, p_180520_1_ * i2, 0, p_180520_2_ * i2, j2, k2, l2);
        for (int i1 = 0; i1 < i2; ++i1) {
            for (int j1 = 0; j1 < i2; ++j1) {
                for (int k1 = 0; k1 < 32; ++k1) {
                    double d0 = 0.25;
                    double d1 = this.densities[((i1 + 0) * l2 + j1 + 0) * k2 + k1 + 0];
                    double d2 = this.densities[((i1 + 0) * l2 + j1 + 1) * k2 + k1 + 0];
                    double d3 = this.densities[((i1 + 1) * l2 + j1 + 0) * k2 + k1 + 0];
                    double d4 = this.densities[((i1 + 1) * l2 + j1 + 1) * k2 + k1 + 0];
                    double d5 = (this.densities[((i1 + 0) * l2 + j1 + 0) * k2 + k1 + 1] - d1) * d0;
                    double d6 = (this.densities[((i1 + 0) * l2 + j1 + 1) * k2 + k1 + 1] - d2) * d0;
                    double d7 = (this.densities[((i1 + 1) * l2 + j1 + 0) * k2 + k1 + 1] - d3) * d0;
                    double d8 = (this.densities[((i1 + 1) * l2 + j1 + 1) * k2 + k1 + 1] - d4) * d0;
                    for (int l1 = 0; l1 < 4; ++l1) {
                        double d9 = 0.125;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for (int i22 = 0; i22 < 8; ++i22) {
                            double d14 = 0.125;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            for (int j22 = 0; j22 < 8; ++j22) {
                                IBlockState iblockstate = null;
                                if (d15 > 0.0) {
                                    iblockstate = Blocks.end_stone.getDefaultState();
                                }
                                int k22 = i22 + i1 * 8;
                                int l22 = l1 + k1 * 4;
                                int i3 = j22 + j1 * 8;
                                p_180520_3_.setBlockState(k22, l22, i3, iblockstate);
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

    public void func_180519_a(ChunkPrimer p_180519_1_) {
        for (int i2 = 0; i2 < 16; ++i2) {
            for (int j2 = 0; j2 < 16; ++j2) {
                int k2 = 1;
                int l2 = -1;
                IBlockState iblockstate = Blocks.end_stone.getDefaultState();
                IBlockState iblockstate1 = Blocks.end_stone.getDefaultState();
                for (int i1 = 127; i1 >= 0; --i1) {
                    IBlockState iblockstate2 = p_180519_1_.getBlockState(i2, i1, j2);
                    if (iblockstate2.getBlock().getMaterial() == Material.air) {
                        l2 = -1;
                        continue;
                    }
                    if (iblockstate2.getBlock() != Blocks.stone) continue;
                    if (l2 == -1) {
                        if (k2 <= 0) {
                            iblockstate = Blocks.air.getDefaultState();
                            iblockstate1 = Blocks.end_stone.getDefaultState();
                        }
                        l2 = k2;
                        if (i1 >= 0) {
                            p_180519_1_.setBlockState(i2, i1, j2, iblockstate);
                            continue;
                        }
                        p_180519_1_.setBlockState(i2, i1, j2, iblockstate1);
                        continue;
                    }
                    if (l2 <= 0) continue;
                    --l2;
                    p_180519_1_.setBlockState(i2, i1, j2, iblockstate1);
                }
            }
        }
    }

    @Override
    public Chunk provideChunk(int x2, int z2) {
        this.endRNG.setSeed((long)x2 * 341873128712L + (long)z2 * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.biomesForGeneration = this.endWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x2 * 16, z2 * 16, 16, 16);
        this.func_180520_a(x2, z2, chunkprimer);
        this.func_180519_a(chunkprimer);
        Chunk chunk = new Chunk(this.endWorld, chunkprimer, x2, z2);
        byte[] abyte = chunk.getBiomeArray();
        for (int i2 = 0; i2 < abyte.length; ++i2) {
            abyte[i2] = (byte)this.biomesForGeneration[i2].biomeID;
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    private double[] initializeNoiseField(double[] p_73187_1_, int p_73187_2_, int p_73187_3_, int p_73187_4_, int p_73187_5_, int p_73187_6_, int p_73187_7_) {
        if (p_73187_1_ == null) {
            p_73187_1_ = new double[p_73187_5_ * p_73187_6_ * p_73187_7_];
        }
        double d0 = 684.412;
        double d1 = 684.412;
        this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 1.121, 1.121, 0.5);
        this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 200.0, 200.0, 0.5);
        this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, (d0 *= 2.0) / 80.0, d1 / 160.0, d0 / 80.0);
        this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, d0, d1, d0);
        this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, d0, d1, d0);
        int i2 = 0;
        for (int j2 = 0; j2 < p_73187_5_; ++j2) {
            for (int k2 = 0; k2 < p_73187_7_; ++k2) {
                float f2 = (float)(j2 + p_73187_2_) / 1.0f;
                float f1 = (float)(k2 + p_73187_4_) / 1.0f;
                float f22 = 100.0f - MathHelper.sqrt_float(f2 * f2 + f1 * f1) * 8.0f;
                if (f22 > 80.0f) {
                    f22 = 80.0f;
                }
                if (f22 < -100.0f) {
                    f22 = -100.0f;
                }
                for (int l2 = 0; l2 < p_73187_6_; ++l2) {
                    double d2 = 0.0;
                    double d3 = this.noiseData2[i2] / 512.0;
                    double d4 = this.noiseData3[i2] / 512.0;
                    double d5 = (this.noiseData1[i2] / 10.0 + 1.0) / 2.0;
                    d2 = d5 < 0.0 ? d3 : (d5 > 1.0 ? d4 : d3 + (d4 - d3) * d5);
                    d2 -= 8.0;
                    d2 += (double)f22;
                    int i1 = 2;
                    if (l2 > p_73187_6_ / 2 - i1) {
                        double d6 = (float)(l2 - (p_73187_6_ / 2 - i1)) / 64.0f;
                        d6 = MathHelper.clamp_double(d6, 0.0, 1.0);
                        d2 = d2 * (1.0 - d6) + -3000.0 * d6;
                    }
                    if (l2 < (i1 = 8)) {
                        double d7 = (float)(i1 - l2) / ((float)i1 - 1.0f);
                        d2 = d2 * (1.0 - d7) + -30.0 * d7;
                    }
                    p_73187_1_[i2] = d2;
                    ++i2;
                }
            }
        }
        return p_73187_1_;
    }

    @Override
    public boolean chunkExists(int x2, int z2) {
        return true;
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        BlockFalling.fallInstantly = true;
        BlockPos blockpos = new BlockPos(p_73153_2_ * 16, 0, p_73153_3_ * 16);
        this.endWorld.getBiomeGenForCoords(blockpos.add(16, 0, 16)).decorate(this.endWorld, this.endWorld.rand, blockpos);
        BlockFalling.fallInstantly = false;
    }

    @Override
    public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_) {
        return false;
    }

    @Override
    public boolean saveChunks(boolean p_73151_1_, IProgressUpdate progressCallback) {
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
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return this.endWorld.getBiomeGenForCoords(pos).getSpawnableList(creatureType);
    }

    @Override
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position) {
        return null;
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;
    }

    @Override
    public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {
    }

    @Override
    public Chunk provideChunk(BlockPos blockPosIn) {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }
}

