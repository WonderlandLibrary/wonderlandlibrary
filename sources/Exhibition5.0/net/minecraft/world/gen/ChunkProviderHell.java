// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen;

import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.util.BlockPos;
import net.minecraft.block.BlockFalling;
import net.minecraft.util.MathHelper;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.block.Block;
import com.google.common.base.Predicate;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.block.state.pattern.BlockHelper;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.MapGenNetherBridge;
import net.minecraft.world.gen.feature.WorldGenHellLava;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.feature.WorldGenGlowStone2;
import net.minecraft.world.gen.feature.WorldGenGlowStone1;
import net.minecraft.world.gen.feature.WorldGenFire;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class ChunkProviderHell implements IChunkProvider
{
    private final World worldObj;
    private final boolean field_177466_i;
    private final Random hellRNG;
    private double[] slowsandNoise;
    private double[] gravelNoise;
    private double[] netherrackExclusivityNoise;
    private double[] noiseField;
    private final NoiseGeneratorOctaves netherNoiseGen1;
    private final NoiseGeneratorOctaves netherNoiseGen2;
    private final NoiseGeneratorOctaves netherNoiseGen3;
    private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
    private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
    public final NoiseGeneratorOctaves netherNoiseGen6;
    public final NoiseGeneratorOctaves netherNoiseGen7;
    private final WorldGenFire field_177470_t;
    private final WorldGenGlowStone1 field_177469_u;
    private final WorldGenGlowStone2 field_177468_v;
    private final WorldGenerator field_177467_w;
    private final WorldGenHellLava field_177473_x;
    private final WorldGenHellLava field_177472_y;
    private final GeneratorBushFeature field_177471_z;
    private final GeneratorBushFeature field_177465_A;
    private final MapGenNetherBridge genNetherBridge;
    private final MapGenBase netherCaveGenerator;
    double[] noiseData1;
    double[] noiseData2;
    double[] noiseData3;
    double[] noiseData4;
    double[] noiseData5;
    private static final String __OBFID = "CL_00000392";
    
    public ChunkProviderHell(final World worldIn, final boolean p_i45637_2_, final long p_i45637_3_) {
        this.slowsandNoise = new double[256];
        this.gravelNoise = new double[256];
        this.netherrackExclusivityNoise = new double[256];
        this.field_177470_t = new WorldGenFire();
        this.field_177469_u = new WorldGenGlowStone1();
        this.field_177468_v = new WorldGenGlowStone2();
        this.field_177467_w = new WorldGenMinable(Blocks.quartz_ore.getDefaultState(), 14, (Predicate)BlockHelper.forBlock(Blocks.netherrack));
        this.field_177473_x = new WorldGenHellLava(Blocks.flowing_lava, true);
        this.field_177472_y = new WorldGenHellLava(Blocks.flowing_lava, false);
        this.field_177471_z = new GeneratorBushFeature(Blocks.brown_mushroom);
        this.field_177465_A = new GeneratorBushFeature(Blocks.red_mushroom);
        this.genNetherBridge = new MapGenNetherBridge();
        this.netherCaveGenerator = new MapGenCavesHell();
        this.worldObj = worldIn;
        this.field_177466_i = p_i45637_2_;
        this.hellRNG = new Random(p_i45637_3_);
        this.netherNoiseGen1 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.netherNoiseGen2 = new NoiseGeneratorOctaves(this.hellRNG, 16);
        this.netherNoiseGen3 = new NoiseGeneratorOctaves(this.hellRNG, 8);
        this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
        this.netherNoiseGen6 = new NoiseGeneratorOctaves(this.hellRNG, 10);
        this.netherNoiseGen7 = new NoiseGeneratorOctaves(this.hellRNG, 16);
    }
    
    public void func_180515_a(final int p_180515_1_, final int p_180515_2_, final ChunkPrimer p_180515_3_) {
        final byte var4 = 4;
        final byte var5 = 32;
        final int var6 = var4 + 1;
        final byte var7 = 17;
        final int var8 = var4 + 1;
        this.noiseField = this.initializeNoiseField(this.noiseField, p_180515_1_ * var4, 0, p_180515_2_ * var4, var6, var7, var8);
        for (int var9 = 0; var9 < var4; ++var9) {
            for (int var10 = 0; var10 < var4; ++var10) {
                for (int var11 = 0; var11 < 16; ++var11) {
                    final double var12 = 0.125;
                    double var13 = this.noiseField[((var9 + 0) * var8 + var10 + 0) * var7 + var11 + 0];
                    double var14 = this.noiseField[((var9 + 0) * var8 + var10 + 1) * var7 + var11 + 0];
                    double var15 = this.noiseField[((var9 + 1) * var8 + var10 + 0) * var7 + var11 + 0];
                    double var16 = this.noiseField[((var9 + 1) * var8 + var10 + 1) * var7 + var11 + 0];
                    final double var17 = (this.noiseField[((var9 + 0) * var8 + var10 + 0) * var7 + var11 + 1] - var13) * var12;
                    final double var18 = (this.noiseField[((var9 + 0) * var8 + var10 + 1) * var7 + var11 + 1] - var14) * var12;
                    final double var19 = (this.noiseField[((var9 + 1) * var8 + var10 + 0) * var7 + var11 + 1] - var15) * var12;
                    final double var20 = (this.noiseField[((var9 + 1) * var8 + var10 + 1) * var7 + var11 + 1] - var16) * var12;
                    for (int var21 = 0; var21 < 8; ++var21) {
                        final double var22 = 0.25;
                        double var23 = var13;
                        double var24 = var14;
                        final double var25 = (var15 - var13) * var22;
                        final double var26 = (var16 - var14) * var22;
                        for (int var27 = 0; var27 < 4; ++var27) {
                            final double var28 = 0.25;
                            double var29 = var23;
                            final double var30 = (var24 - var23) * var28;
                            for (int var31 = 0; var31 < 4; ++var31) {
                                IBlockState var32 = null;
                                if (var11 * 8 + var21 < var5) {
                                    var32 = Blocks.lava.getDefaultState();
                                }
                                if (var29 > 0.0) {
                                    var32 = Blocks.netherrack.getDefaultState();
                                }
                                final int var33 = var27 + var9 * 4;
                                final int var34 = var21 + var11 * 8;
                                final int var35 = var31 + var10 * 4;
                                p_180515_3_.setBlockState(var33, var34, var35, var32);
                                var29 += var30;
                            }
                            var23 += var25;
                            var24 += var26;
                        }
                        var13 += var17;
                        var14 += var18;
                        var15 += var19;
                        var16 += var20;
                    }
                }
            }
        }
    }
    
    public void func_180516_b(final int p_180516_1_, final int p_180516_2_, final ChunkPrimer p_180516_3_) {
        final byte var4 = 64;
        final double var5 = 0.03125;
        this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, p_180516_1_ * 16, p_180516_2_ * 16, 0, 16, 16, 1, var5, var5, 1.0);
        this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, p_180516_1_ * 16, 109, p_180516_2_ * 16, 16, 1, 16, var5, 1.0, var5);
        this.netherrackExclusivityNoise = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.netherrackExclusivityNoise, p_180516_1_ * 16, p_180516_2_ * 16, 0, 16, 16, 1, var5 * 2.0, var5 * 2.0, var5 * 2.0);
        for (int var6 = 0; var6 < 16; ++var6) {
            for (int var7 = 0; var7 < 16; ++var7) {
                final boolean var8 = this.slowsandNoise[var6 + var7 * 16] + this.hellRNG.nextDouble() * 0.2 > 0.0;
                final boolean var9 = this.gravelNoise[var6 + var7 * 16] + this.hellRNG.nextDouble() * 0.2 > 0.0;
                final int var10 = (int)(this.netherrackExclusivityNoise[var6 + var7 * 16] / 3.0 + 3.0 + this.hellRNG.nextDouble() * 0.25);
                int var11 = -1;
                IBlockState var12 = Blocks.netherrack.getDefaultState();
                IBlockState var13 = Blocks.netherrack.getDefaultState();
                for (int var14 = 127; var14 >= 0; --var14) {
                    if (var14 < 127 - this.hellRNG.nextInt(5) && var14 > this.hellRNG.nextInt(5)) {
                        final IBlockState var15 = p_180516_3_.getBlockState(var7, var14, var6);
                        if (var15.getBlock() != null && var15.getBlock().getMaterial() != Material.air) {
                            if (var15.getBlock() == Blocks.netherrack) {
                                if (var11 == -1) {
                                    if (var10 <= 0) {
                                        var12 = null;
                                        var13 = Blocks.netherrack.getDefaultState();
                                    }
                                    else if (var14 >= var4 - 4 && var14 <= var4 + 1) {
                                        var12 = Blocks.netherrack.getDefaultState();
                                        var13 = Blocks.netherrack.getDefaultState();
                                        if (var9) {
                                            var12 = Blocks.gravel.getDefaultState();
                                            var13 = Blocks.netherrack.getDefaultState();
                                        }
                                        if (var8) {
                                            var12 = Blocks.soul_sand.getDefaultState();
                                            var13 = Blocks.soul_sand.getDefaultState();
                                        }
                                    }
                                    if (var14 < var4 && (var12 == null || var12.getBlock().getMaterial() == Material.air)) {
                                        var12 = Blocks.lava.getDefaultState();
                                    }
                                    var11 = var10;
                                    if (var14 >= var4 - 1) {
                                        p_180516_3_.setBlockState(var7, var14, var6, var12);
                                    }
                                    else {
                                        p_180516_3_.setBlockState(var7, var14, var6, var13);
                                    }
                                }
                                else if (var11 > 0) {
                                    --var11;
                                    p_180516_3_.setBlockState(var7, var14, var6, var13);
                                }
                            }
                        }
                        else {
                            var11 = -1;
                        }
                    }
                    else {
                        p_180516_3_.setBlockState(var7, var14, var6, Blocks.bedrock.getDefaultState());
                    }
                }
            }
        }
    }
    
    @Override
    public Chunk provideChunk(final int p_73154_1_, final int p_73154_2_) {
        this.hellRNG.setSeed(p_73154_1_ * 341873128712L + p_73154_2_ * 132897987541L);
        final ChunkPrimer var3 = new ChunkPrimer();
        this.func_180515_a(p_73154_1_, p_73154_2_, var3);
        this.func_180516_b(p_73154_1_, p_73154_2_, var3);
        this.netherCaveGenerator.func_175792_a(this, this.worldObj, p_73154_1_, p_73154_2_, var3);
        if (this.field_177466_i) {
            this.genNetherBridge.func_175792_a(this, this.worldObj, p_73154_1_, p_73154_2_, var3);
        }
        final Chunk var4 = new Chunk(this.worldObj, var3, p_73154_1_, p_73154_2_);
        final BiomeGenBase[] var5 = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, p_73154_1_ * 16, p_73154_2_ * 16, 16, 16);
        final byte[] var6 = var4.getBiomeArray();
        for (int var7 = 0; var7 < var6.length; ++var7) {
            var6[var7] = (byte)var5[var7].biomeID;
        }
        var4.resetRelightChecks();
        return var4;
    }
    
    private double[] initializeNoiseField(double[] p_73164_1_, final int p_73164_2_, final int p_73164_3_, final int p_73164_4_, final int p_73164_5_, final int p_73164_6_, final int p_73164_7_) {
        if (p_73164_1_ == null) {
            p_73164_1_ = new double[p_73164_5_ * p_73164_6_ * p_73164_7_];
        }
        final double var8 = 684.412;
        final double var9 = 2053.236;
        this.noiseData4 = this.netherNoiseGen6.generateNoiseOctaves(this.noiseData4, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 1.0, 0.0, 1.0);
        this.noiseData5 = this.netherNoiseGen7.generateNoiseOctaves(this.noiseData5, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 100.0, 0.0, 100.0);
        this.noiseData1 = this.netherNoiseGen3.generateNoiseOctaves(this.noiseData1, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, var8 / 80.0, var9 / 60.0, var8 / 80.0);
        this.noiseData2 = this.netherNoiseGen1.generateNoiseOctaves(this.noiseData2, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, var8, var9, var8);
        this.noiseData3 = this.netherNoiseGen2.generateNoiseOctaves(this.noiseData3, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, var8, var9, var8);
        int var10 = 0;
        final double[] var11 = new double[p_73164_6_];
        for (int var12 = 0; var12 < p_73164_6_; ++var12) {
            var11[var12] = Math.cos(var12 * 3.141592653589793 * 6.0 / p_73164_6_) * 2.0;
            double var13 = var12;
            if (var12 > p_73164_6_ / 2) {
                var13 = p_73164_6_ - 1 - var12;
            }
            if (var13 < 4.0) {
                var13 = 4.0 - var13;
                final double[] array = var11;
                final int n = var12;
                array[n] -= var13 * var13 * var13 * 10.0;
            }
        }
        for (int var12 = 0; var12 < p_73164_5_; ++var12) {
            for (int var14 = 0; var14 < p_73164_7_; ++var14) {
                final double var15 = 0.0;
                for (int var16 = 0; var16 < p_73164_6_; ++var16) {
                    double var17 = 0.0;
                    final double var18 = var11[var16];
                    final double var19 = this.noiseData2[var10] / 512.0;
                    final double var20 = this.noiseData3[var10] / 512.0;
                    final double var21 = (this.noiseData1[var10] / 10.0 + 1.0) / 2.0;
                    if (var21 < 0.0) {
                        var17 = var19;
                    }
                    else if (var21 > 1.0) {
                        var17 = var20;
                    }
                    else {
                        var17 = var19 + (var20 - var19) * var21;
                    }
                    var17 -= var18;
                    if (var16 > p_73164_6_ - 4) {
                        final double var22 = (var16 - (p_73164_6_ - 4)) / 3.0f;
                        var17 = var17 * (1.0 - var22) + -10.0 * var22;
                    }
                    if (var16 < var15) {
                        double var22 = (var15 - var16) / 4.0;
                        var22 = MathHelper.clamp_double(var22, 0.0, 1.0);
                        var17 = var17 * (1.0 - var22) + -10.0 * var22;
                    }
                    p_73164_1_[var10] = var17;
                    ++var10;
                }
            }
        }
        return p_73164_1_;
    }
    
    @Override
    public boolean chunkExists(final int p_73149_1_, final int p_73149_2_) {
        return true;
    }
    
    @Override
    public void populate(final IChunkProvider p_73153_1_, final int p_73153_2_, final int p_73153_3_) {
        BlockFalling.fallInstantly = true;
        final BlockPos var4 = new BlockPos(p_73153_2_ * 16, 0, p_73153_3_ * 16);
        final ChunkCoordIntPair var5 = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);
        this.genNetherBridge.func_175794_a(this.worldObj, this.hellRNG, var5);
        for (int var6 = 0; var6 < 8; ++var6) {
            this.field_177472_y.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
        }
        for (int var6 = 0; var6 < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1) + 1; ++var6) {
            this.field_177470_t.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
        }
        for (int var6 = 0; var6 < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1); ++var6) {
            this.field_177469_u.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
        }
        for (int var6 = 0; var6 < 10; ++var6) {
            this.field_177468_v.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        if (this.hellRNG.nextBoolean()) {
            this.field_177471_z.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        if (this.hellRNG.nextBoolean()) {
            this.field_177465_A.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        for (int var6 = 0; var6 < 16; ++var6) {
            this.field_177467_w.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
        }
        for (int var6 = 0; var6 < 16; ++var6) {
            this.field_177473_x.generate(this.worldObj, this.hellRNG, var4.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
        }
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
        return "HellRandomLevelSource";
    }
    
    @Override
    public List func_177458_a(final EnumCreatureType p_177458_1_, final BlockPos p_177458_2_) {
        if (p_177458_1_ == EnumCreatureType.MONSTER) {
            if (this.genNetherBridge.func_175795_b(p_177458_2_)) {
                return this.genNetherBridge.getSpawnList();
            }
            if (this.genNetherBridge.func_175796_a(this.worldObj, p_177458_2_) && this.worldObj.getBlockState(p_177458_2_.offsetDown()).getBlock() == Blocks.nether_brick) {
                return this.genNetherBridge.getSpawnList();
            }
        }
        final BiomeGenBase var3 = this.worldObj.getBiomeGenForCoords(p_177458_2_);
        return var3.getSpawnableList(p_177458_1_);
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
        this.genNetherBridge.func_175792_a(this, this.worldObj, p_180514_2_, p_180514_3_, null);
    }
    
    @Override
    public Chunk func_177459_a(final BlockPos p_177459_1_) {
        return this.provideChunk(p_177459_1_.getX() >> 4, p_177459_1_.getZ() >> 4);
    }
}
