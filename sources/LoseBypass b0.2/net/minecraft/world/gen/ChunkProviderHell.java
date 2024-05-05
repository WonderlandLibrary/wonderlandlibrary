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
    private final World worldObj;
    private final boolean field_177466_i;
    private final Random hellRNG;
    private double[] slowsandNoise = new double[256];
    private double[] gravelNoise = new double[256];
    private double[] netherrackExclusivityNoise = new double[256];
    private double[] noiseField;
    private final NoiseGeneratorOctaves netherNoiseGen1;
    private final NoiseGeneratorOctaves netherNoiseGen2;
    private final NoiseGeneratorOctaves netherNoiseGen3;
    private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
    private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
    public final NoiseGeneratorOctaves netherNoiseGen6;
    public final NoiseGeneratorOctaves netherNoiseGen7;
    private final WorldGenFire field_177470_t = new WorldGenFire();
    private final WorldGenGlowStone1 field_177469_u = new WorldGenGlowStone1();
    private final WorldGenGlowStone2 field_177468_v = new WorldGenGlowStone2();
    private final WorldGenerator field_177467_w = new WorldGenMinable(Blocks.quartz_ore.getDefaultState(), 14, BlockHelper.forBlock(Blocks.netherrack));
    private final WorldGenHellLava field_177473_x = new WorldGenHellLava(Blocks.flowing_lava, true);
    private final WorldGenHellLava field_177472_y = new WorldGenHellLava(Blocks.flowing_lava, false);
    private final GeneratorBushFeature field_177471_z = new GeneratorBushFeature(Blocks.brown_mushroom);
    private final GeneratorBushFeature field_177465_A = new GeneratorBushFeature(Blocks.red_mushroom);
    private final MapGenNetherBridge genNetherBridge = new MapGenNetherBridge();
    private final MapGenBase netherCaveGenerator = new MapGenCavesHell();
    double[] noiseData1;
    double[] noiseData2;
    double[] noiseData3;
    double[] noiseData4;
    double[] noiseData5;

    public ChunkProviderHell(World worldIn, boolean p_i45637_2_, long p_i45637_3_) {
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
        worldIn.func_181544_b(63);
    }

    /*
     * Exception decompiling
     */
    public void func_180515_a(int p_180515_1_, int p_180515_2_, ChunkPrimer p_180515_3_) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[UNCONDITIONALDOLOOP]], but top level block is 2[UNCONDITIONALDOLOOP]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         *     at the.bytecode.club.bytecodeviewer.decompilers.impl.CFRDecompiler.decompileToZip(CFRDecompiler.java:306)
         *     at the.bytecode.club.bytecodeviewer.resources.ResourceDecompiling.lambda$null$5(ResourceDecompiling.java:159)
         *     at java.lang.Thread.run(Thread.java:748)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public void func_180516_b(int p_180516_1_, int p_180516_2_, ChunkPrimer p_180516_3_) {
        int i = this.worldObj.func_181545_F() + 1;
        double d0 = 0.03125;
        this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, p_180516_1_ * 16, p_180516_2_ * 16, 0, 16, 16, 1, d0, d0, 1.0);
        this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, p_180516_1_ * 16, 109, p_180516_2_ * 16, 16, 1, 16, d0, 1.0, d0);
        this.netherrackExclusivityNoise = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.netherrackExclusivityNoise, p_180516_1_ * 16, p_180516_2_ * 16, 0, 16, 16, 1, d0 * 2.0, d0 * 2.0, d0 * 2.0);
        int j = 0;
        block0: while (j < 16) {
            int k = 0;
            while (true) {
                IBlockState iblockstate1;
                IBlockState iblockstate;
                int i1;
                int l;
                boolean flag1;
                boolean flag;
                if (k < 16) {
                    flag = this.slowsandNoise[j + k * 16] + this.hellRNG.nextDouble() * 0.2 > 0.0;
                    flag1 = this.gravelNoise[j + k * 16] + this.hellRNG.nextDouble() * 0.2 > 0.0;
                    l = (int)(this.netherrackExclusivityNoise[j + k * 16] / 3.0 + 3.0 + this.hellRNG.nextDouble() * 0.25);
                    i1 = -1;
                    iblockstate = Blocks.netherrack.getDefaultState();
                    iblockstate1 = Blocks.netherrack.getDefaultState();
                } else {
                    ++j;
                    continue block0;
                }
                for (int j1 = 127; j1 >= 0; --j1) {
                    if (j1 < 127 - this.hellRNG.nextInt(5) && j1 > this.hellRNG.nextInt(5)) {
                        IBlockState iblockstate2 = p_180516_3_.getBlockState(k, j1, j);
                        if (iblockstate2.getBlock() != null && iblockstate2.getBlock().getMaterial() != Material.air) {
                            if (iblockstate2.getBlock() != Blocks.netherrack) continue;
                            if (i1 == -1) {
                                if (l <= 0) {
                                    iblockstate = null;
                                    iblockstate1 = Blocks.netherrack.getDefaultState();
                                } else if (j1 >= i - 4 && j1 <= i + 1) {
                                    iblockstate = Blocks.netherrack.getDefaultState();
                                    iblockstate1 = Blocks.netherrack.getDefaultState();
                                    if (flag1) {
                                        iblockstate = Blocks.gravel.getDefaultState();
                                        iblockstate1 = Blocks.netherrack.getDefaultState();
                                    }
                                    if (flag) {
                                        iblockstate = Blocks.soul_sand.getDefaultState();
                                        iblockstate1 = Blocks.soul_sand.getDefaultState();
                                    }
                                }
                                if (j1 < i && (iblockstate == null || iblockstate.getBlock().getMaterial() == Material.air)) {
                                    iblockstate = Blocks.lava.getDefaultState();
                                }
                                i1 = l;
                                if (j1 >= i - 1) {
                                    p_180516_3_.setBlockState(k, j1, j, iblockstate);
                                    continue;
                                }
                                p_180516_3_.setBlockState(k, j1, j, iblockstate1);
                                continue;
                            }
                            if (i1 <= 0) continue;
                            --i1;
                            p_180516_3_.setBlockState(k, j1, j, iblockstate1);
                            continue;
                        }
                        i1 = -1;
                        continue;
                    }
                    p_180516_3_.setBlockState(k, j1, j, Blocks.bedrock.getDefaultState());
                }
                ++k;
            }
            break;
        }
        return;
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        this.hellRNG.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.func_180515_a(x, z, chunkprimer);
        this.func_180516_b(x, z, chunkprimer);
        this.netherCaveGenerator.generate(this, this.worldObj, x, z, chunkprimer);
        if (this.field_177466_i) {
            this.genNetherBridge.generate(this, this.worldObj, x, z, chunkprimer);
        }
        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
        BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, x * 16, z * 16, 16, 16);
        byte[] abyte = chunk.getBiomeArray();
        int i = 0;
        while (true) {
            if (i >= abyte.length) {
                chunk.resetRelightChecks();
                return chunk;
            }
            abyte[i] = (byte)abiomegenbase[i].biomeID;
            ++i;
        }
    }

    private double[] initializeNoiseField(double[] p_73164_1_, int p_73164_2_, int p_73164_3_, int p_73164_4_, int p_73164_5_, int p_73164_6_, int p_73164_7_) {
        if (p_73164_1_ == null) {
            p_73164_1_ = new double[p_73164_5_ * p_73164_6_ * p_73164_7_];
        }
        double d0 = 684.412;
        double d1 = 2053.236;
        this.noiseData4 = this.netherNoiseGen6.generateNoiseOctaves(this.noiseData4, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 1.0, 0.0, 1.0);
        this.noiseData5 = this.netherNoiseGen7.generateNoiseOctaves(this.noiseData5, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 100.0, 0.0, 100.0);
        this.noiseData1 = this.netherNoiseGen3.generateNoiseOctaves(this.noiseData1, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0 / 80.0, d1 / 60.0, d0 / 80.0);
        this.noiseData2 = this.netherNoiseGen1.generateNoiseOctaves(this.noiseData2, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0, d1, d0);
        this.noiseData3 = this.netherNoiseGen2.generateNoiseOctaves(this.noiseData3, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0, d1, d0);
        int i = 0;
        double[] adouble = new double[p_73164_6_];
        for (int j = 0; j < p_73164_6_; ++j) {
            adouble[j] = Math.cos((double)j * Math.PI * 6.0 / (double)p_73164_6_) * 2.0;
            double d2 = j;
            if (j > p_73164_6_ / 2) {
                d2 = p_73164_6_ - 1 - j;
            }
            if (!(d2 < 4.0)) continue;
            d2 = 4.0 - d2;
            int n = j;
            adouble[n] = adouble[n] - d2 * d2 * d2 * 10.0;
        }
        int l = 0;
        block1: while (l < p_73164_5_) {
            int i1 = 0;
            while (true) {
                double d3;
                if (i1 < p_73164_7_) {
                    d3 = 0.0;
                } else {
                    ++l;
                    continue block1;
                }
                for (int k = 0; k < p_73164_6_; ++i, ++k) {
                    double d4 = 0.0;
                    double d5 = adouble[k];
                    double d6 = this.noiseData2[i] / 512.0;
                    double d7 = this.noiseData3[i] / 512.0;
                    double d8 = (this.noiseData1[i] / 10.0 + 1.0) / 2.0;
                    d4 = d8 < 0.0 ? d6 : (d8 > 1.0 ? d7 : d6 + (d7 - d6) * d8);
                    d4 -= d5;
                    if (k > p_73164_6_ - 4) {
                        double d9 = (float)(k - (p_73164_6_ - 4)) / 3.0f;
                        d4 = d4 * (1.0 - d9) + -10.0 * d9;
                    }
                    if ((double)k < d3) {
                        double d10 = (d3 - (double)k) / 4.0;
                        d10 = MathHelper.clamp_double(d10, 0.0, 1.0);
                        d4 = d4 * (1.0 - d10) + -10.0 * d10;
                    }
                    p_73164_1_[i] = d4;
                }
                ++i1;
            }
            break;
        }
        return p_73164_1_;
    }

    @Override
    public boolean chunkExists(int x, int z) {
        return true;
    }

    @Override
    public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_) {
        BlockFalling.fallInstantly = true;
        BlockPos blockpos = new BlockPos(p_73153_2_ * 16, 0, p_73153_3_ * 16);
        ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);
        this.genNetherBridge.generateStructure(this.worldObj, this.hellRNG, chunkcoordintpair);
        for (int i = 0; i < 8; ++i) {
            this.field_177472_y.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
        }
        for (int j = 0; j < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1) + 1; ++j) {
            this.field_177470_t.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
        }
        for (int k = 0; k < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1); ++k) {
            this.field_177469_u.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
        }
        for (int l = 0; l < 10; ++l) {
            this.field_177468_v.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        if (this.hellRNG.nextBoolean()) {
            this.field_177471_z.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        if (this.hellRNG.nextBoolean()) {
            this.field_177465_A.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
        }
        for (int i1 = 0; i1 < 16; ++i1) {
            this.field_177467_w.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
        }
        int j1 = 0;
        while (true) {
            if (j1 >= 16) {
                BlockFalling.fallInstantly = false;
                return;
            }
            this.field_177473_x.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
            ++j1;
        }
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
        return "HellRandomLevelSource";
    }

    @Override
    public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        if (creatureType == EnumCreatureType.MONSTER) {
            if (this.genNetherBridge.func_175795_b(pos)) {
                return this.genNetherBridge.getSpawnList();
            }
            if (this.genNetherBridge.func_175796_a(this.worldObj, pos) && this.worldObj.getBlockState(pos.down()).getBlock() == Blocks.nether_brick) {
                return this.genNetherBridge.getSpawnList();
            }
        }
        BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
        return biomegenbase.getSpawnableList(creatureType);
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
        this.genNetherBridge.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
    }

    @Override
    public Chunk provideChunk(BlockPos blockPosIn) {
        return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
    }
}

