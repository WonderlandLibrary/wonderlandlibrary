// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockFalling;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.World;
import java.util.Random;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderEnd implements IChunkProvider
{
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
    private static final String __OBFID = "CL_00000397";
    
    public ChunkProviderEnd(final World worldIn, final long p_i2007_2_) {
        this.endWorld = worldIn;
        this.endRNG = new Random(p_i2007_2_);
        this.noiseGen1 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen2 = new NoiseGeneratorOctaves(this.endRNG, 16);
        this.noiseGen3 = new NoiseGeneratorOctaves(this.endRNG, 8);
        this.noiseGen4 = new NoiseGeneratorOctaves(this.endRNG, 10);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.endRNG, 16);
    }
    
    public void func_180520_a(final int p_180520_1_, final int p_180520_2_, final ChunkPrimer p_180520_3_) {
        final byte var4 = 2;
        final int var5 = var4 + 1;
        final byte var6 = 33;
        final int var7 = var4 + 1;
        this.densities = this.initializeNoiseField(this.densities, p_180520_1_ * var4, 0, p_180520_2_ * var4, var5, var6, var7);
        for (int var8 = 0; var8 < var4; ++var8) {
            for (int var9 = 0; var9 < var4; ++var9) {
                for (int var10 = 0; var10 < 32; ++var10) {
                    final double var11 = 0.25;
                    double var12 = this.densities[((var8 + 0) * var7 + var9 + 0) * var6 + var10 + 0];
                    double var13 = this.densities[((var8 + 0) * var7 + var9 + 1) * var6 + var10 + 0];
                    double var14 = this.densities[((var8 + 1) * var7 + var9 + 0) * var6 + var10 + 0];
                    double var15 = this.densities[((var8 + 1) * var7 + var9 + 1) * var6 + var10 + 0];
                    final double var16 = (this.densities[((var8 + 0) * var7 + var9 + 0) * var6 + var10 + 1] - var12) * var11;
                    final double var17 = (this.densities[((var8 + 0) * var7 + var9 + 1) * var6 + var10 + 1] - var13) * var11;
                    final double var18 = (this.densities[((var8 + 1) * var7 + var9 + 0) * var6 + var10 + 1] - var14) * var11;
                    final double var19 = (this.densities[((var8 + 1) * var7 + var9 + 1) * var6 + var10 + 1] - var15) * var11;
                    for (int var20 = 0; var20 < 4; ++var20) {
                        final double var21 = 0.125;
                        double var22 = var12;
                        double var23 = var13;
                        final double var24 = (var14 - var12) * var21;
                        final double var25 = (var15 - var13) * var21;
                        for (int var26 = 0; var26 < 8; ++var26) {
                            final double var27 = 0.125;
                            double var28 = var22;
                            final double var29 = (var23 - var22) * var27;
                            for (int var30 = 0; var30 < 8; ++var30) {
                                IBlockState var31 = null;
                                if (var28 > 0.0) {
                                    var31 = Blocks.end_stone.getDefaultState();
                                }
                                final int var32 = var26 + var8 * 8;
                                final int var33 = var20 + var10 * 4;
                                final int var34 = var30 + var9 * 8;
                                p_180520_3_.setBlockState(var32, var33, var34, var31);
                                var28 += var29;
                            }
                            var22 += var24;
                            var23 += var25;
                        }
                        var12 += var16;
                        var13 += var17;
                        var14 += var18;
                        var15 += var19;
                    }
                }
            }
        }
    }
    
    public void func_180519_a(final ChunkPrimer p_180519_1_) {
        for (int var2 = 0; var2 < 16; ++var2) {
            for (int var3 = 0; var3 < 16; ++var3) {
                final byte var4 = 1;
                int var5 = -1;
                IBlockState var6 = Blocks.end_stone.getDefaultState();
                IBlockState var7 = Blocks.end_stone.getDefaultState();
                for (int var8 = 127; var8 >= 0; --var8) {
                    final IBlockState var9 = p_180519_1_.getBlockState(var2, var8, var3);
                    if (var9.getBlock().getMaterial() == Material.air) {
                        var5 = -1;
                    }
                    else if (var9.getBlock() == Blocks.stone) {
                        if (var5 == -1) {
                            if (var4 <= 0) {
                                var6 = Blocks.air.getDefaultState();
                                var7 = Blocks.end_stone.getDefaultState();
                            }
                            var5 = var4;
                            if (var8 >= 0) {
                                p_180519_1_.setBlockState(var2, var8, var3, var6);
                            }
                            else {
                                p_180519_1_.setBlockState(var2, var8, var3, var7);
                            }
                        }
                        else if (var5 > 0) {
                            --var5;
                            p_180519_1_.setBlockState(var2, var8, var3, var7);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public Chunk provideChunk(final int p_73154_1_, final int p_73154_2_) {
        this.endRNG.setSeed(p_73154_1_ * 341873128712L + p_73154_2_ * 132897987541L);
        final ChunkPrimer var3 = new ChunkPrimer();
        this.biomesForGeneration = this.endWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
        this.func_180520_a(p_73154_1_, p_73154_2_, var3);
        this.func_180519_a(var3);
        final Chunk var4 = new Chunk(this.endWorld, var3, p_73154_1_, p_73154_2_);
        final byte[] var5 = var4.getBiomeArray();
        for (int var6 = 0; var6 < var5.length; ++var6) {
            var5[var6] = (byte)this.biomesForGeneration[var6].biomeID;
        }
        var4.generateSkylightMap();
        return var4;
    }
    
    private double[] initializeNoiseField(double[] p_73187_1_, final int p_73187_2_, final int p_73187_3_, final int p_73187_4_, final int p_73187_5_, final int p_73187_6_, final int p_73187_7_) {
        if (p_73187_1_ == null) {
            p_73187_1_ = new double[p_73187_5_ * p_73187_6_ * p_73187_7_];
        }
        double var8 = 684.412;
        final double var9 = 684.412;
        this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 1.121, 1.121, 0.5);
        this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 200.0, 200.0, 0.5);
        var8 *= 2.0;
        this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, var8 / 80.0, var9 / 160.0, var8 / 80.0);
        this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, var8, var9, var8);
        this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, var8, var9, var8);
        int var10 = 0;
        for (int var11 = 0; var11 < p_73187_5_; ++var11) {
            for (int var12 = 0; var12 < p_73187_7_; ++var12) {
                final float var13 = (var11 + p_73187_2_) / 1.0f;
                final float var14 = (var12 + p_73187_4_) / 1.0f;
                float var15 = 100.0f - MathHelper.sqrt_float(var13 * var13 + var14 * var14) * 8.0f;
                if (var15 > 80.0f) {
                    var15 = 80.0f;
                }
                if (var15 < -100.0f) {
                    var15 = -100.0f;
                }
                for (int var16 = 0; var16 < p_73187_6_; ++var16) {
                    double var17 = 0.0;
                    final double var18 = this.noiseData2[var10] / 512.0;
                    final double var19 = this.noiseData3[var10] / 512.0;
                    final double var20 = (this.noiseData1[var10] / 10.0 + 1.0) / 2.0;
                    if (var20 < 0.0) {
                        var17 = var18;
                    }
                    else if (var20 > 1.0) {
                        var17 = var19;
                    }
                    else {
                        var17 = var18 + (var19 - var18) * var20;
                    }
                    var17 -= 8.0;
                    var17 += var15;
                    byte var21 = 2;
                    if (var16 > p_73187_6_ / 2 - var21) {
                        double var22 = (var16 - (p_73187_6_ / 2 - var21)) / 64.0f;
                        var22 = MathHelper.clamp_double(var22, 0.0, 1.0);
                        var17 = var17 * (1.0 - var22) + -3000.0 * var22;
                    }
                    var21 = 8;
                    if (var16 < var21) {
                        final double var22 = (var21 - var16) / (var21 - 1.0f);
                        var17 = var17 * (1.0 - var22) + -30.0 * var22;
                    }
                    p_73187_1_[var10] = var17;
                    ++var10;
                }
            }
        }
        return p_73187_1_;
    }
    
    @Override
    public boolean chunkExists(final int p_73149_1_, final int p_73149_2_) {
        return true;
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
        BlockFalling.fallInstantly = true;
        final BlockPos var4 = new BlockPos(p_73153_2_ * 16, 0, p_73153_3_ * 16);
        this.endWorld.getBiomeGenForCoords(var4.add(16, 0, 16)).func_180624_a(this.endWorld, this.endWorld.rand, var4);
        BlockFalling.fallInstantly = false;
    }
    
    @Override
    public boolean func_177460_a(final IChunkProvider p_177460_1_, final Chunk p_177460_2_, final int p_177460_3_, final int p_177460_4_) {
        return false;
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
        return this.endWorld.getBiomeGenForCoords(p_177458_2_).getSpawnableList(p_177458_1_);
    }
    
    @Override
    public BlockPos func_180513_a(final World worldIn, final String p_180513_2_, final BlockPos p_180513_3_) {
        return null;
    }
    
    @Override
    public int getLoadedChunkCount() {
        return 0;
    }
    
    @Override
    public void func_180514_a(final Chunk p_180514_1_, final int p_180514_2_, final int p_180514_3_) {
    }
    
    @Override
    public Chunk func_177459_a(final BlockPos p_177459_1_) {
        return this.provideChunk(p_177459_1_.getX() >> 4, p_177459_1_.getZ() >> 4);
    }
}
