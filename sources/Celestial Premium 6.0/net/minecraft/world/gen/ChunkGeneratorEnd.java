/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.BlockChorusFlower;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.NoiseGeneratorOctaves;
import net.minecraft.world.gen.NoiseGeneratorSimplex;
import net.minecraft.world.gen.feature.WorldGenEndGateway;
import net.minecraft.world.gen.feature.WorldGenEndIsland;
import net.minecraft.world.gen.structure.MapGenEndCity;

public class ChunkGeneratorEnd
implements IChunkGenerator {
    private final Random rand;
    protected static final IBlockState END_STONE = Blocks.END_STONE.getDefaultState();
    protected static final IBlockState AIR = Blocks.AIR.getDefaultState();
    private final NoiseGeneratorOctaves lperlinNoise1;
    private final NoiseGeneratorOctaves lperlinNoise2;
    private final NoiseGeneratorOctaves perlinNoise1;
    public NoiseGeneratorOctaves noiseGen5;
    public NoiseGeneratorOctaves noiseGen6;
    private final World worldObj;
    private final boolean mapFeaturesEnabled;
    private final BlockPos field_191061_n;
    private final MapGenEndCity endCityGen = new MapGenEndCity(this);
    private final NoiseGeneratorSimplex islandNoise;
    private double[] buffer;
    private Biome[] biomesForGeneration;
    double[] pnr;
    double[] ar;
    double[] br;
    private final WorldGenEndIsland endIslands = new WorldGenEndIsland();

    public ChunkGeneratorEnd(World p_i47241_1_, boolean p_i47241_2_, long p_i47241_3_, BlockPos p_i47241_5_) {
        this.worldObj = p_i47241_1_;
        this.mapFeaturesEnabled = p_i47241_2_;
        this.field_191061_n = p_i47241_5_;
        this.rand = new Random(p_i47241_3_);
        this.lperlinNoise1 = new NoiseGeneratorOctaves(this.rand, 16);
        this.lperlinNoise2 = new NoiseGeneratorOctaves(this.rand, 16);
        this.perlinNoise1 = new NoiseGeneratorOctaves(this.rand, 8);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
        this.islandNoise = new NoiseGeneratorSimplex(this.rand);
    }

    public void setBlocksInChunk(int x, int z, ChunkPrimer primer) {
        int i = 2;
        int j = 3;
        int k = 33;
        int l = 3;
        this.buffer = this.getHeights(this.buffer, x * 2, 0, z * 2, 3, 33, 3);
        for (int i1 = 0; i1 < 2; ++i1) {
            for (int j1 = 0; j1 < 2; ++j1) {
                for (int k1 = 0; k1 < 32; ++k1) {
                    double d0 = 0.25;
                    double d1 = this.buffer[((i1 + 0) * 3 + j1 + 0) * 33 + k1 + 0];
                    double d2 = this.buffer[((i1 + 0) * 3 + j1 + 1) * 33 + k1 + 0];
                    double d3 = this.buffer[((i1 + 1) * 3 + j1 + 0) * 33 + k1 + 0];
                    double d4 = this.buffer[((i1 + 1) * 3 + j1 + 1) * 33 + k1 + 0];
                    double d5 = (this.buffer[((i1 + 0) * 3 + j1 + 0) * 33 + k1 + 1] - d1) * 0.25;
                    double d6 = (this.buffer[((i1 + 0) * 3 + j1 + 1) * 33 + k1 + 1] - d2) * 0.25;
                    double d7 = (this.buffer[((i1 + 1) * 3 + j1 + 0) * 33 + k1 + 1] - d3) * 0.25;
                    double d8 = (this.buffer[((i1 + 1) * 3 + j1 + 1) * 33 + k1 + 1] - d4) * 0.25;
                    for (int l1 = 0; l1 < 4; ++l1) {
                        double d9 = 0.125;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * 0.125;
                        double d13 = (d4 - d2) * 0.125;
                        for (int i2 = 0; i2 < 8; ++i2) {
                            double d14 = 0.125;
                            double d15 = d10;
                            double d16 = (d11 - d10) * 0.125;
                            for (int j2 = 0; j2 < 8; ++j2) {
                                IBlockState iblockstate = AIR;
                                if (d15 > 0.0) {
                                    iblockstate = END_STONE;
                                }
                                int k2 = i2 + i1 * 8;
                                int l2 = l1 + k1 * 4;
                                int i3 = j2 + j1 * 8;
                                primer.setBlockState(k2, l2, i3, iblockstate);
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

    public void buildSurfaces(ChunkPrimer primer) {
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                boolean k = true;
                int l = -1;
                IBlockState iblockstate = END_STONE;
                IBlockState iblockstate1 = END_STONE;
                for (int i1 = 127; i1 >= 0; --i1) {
                    IBlockState iblockstate2 = primer.getBlockState(i, i1, j);
                    if (iblockstate2.getMaterial() == Material.AIR) {
                        l = -1;
                        continue;
                    }
                    if (iblockstate2.getBlock() != Blocks.STONE) continue;
                    if (l == -1) {
                        l = 1;
                        if (i1 >= 0) {
                            primer.setBlockState(i, i1, j, iblockstate);
                            continue;
                        }
                        primer.setBlockState(i, i1, j, iblockstate1);
                        continue;
                    }
                    if (l <= 0) continue;
                    --l;
                    primer.setBlockState(i, i1, j, iblockstate1);
                }
            }
        }
    }

    @Override
    public Chunk provideChunk(int x, int z) {
        this.rand.setSeed((long)x * 341873128712L + (long)z * 132897987541L);
        ChunkPrimer chunkprimer = new ChunkPrimer();
        this.biomesForGeneration = this.worldObj.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        this.setBlocksInChunk(x, z, chunkprimer);
        this.buildSurfaces(chunkprimer);
        if (this.mapFeaturesEnabled) {
            this.endCityGen.generate(this.worldObj, x, z, chunkprimer);
        }
        Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
        byte[] abyte = chunk.getBiomeArray();
        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte)Biome.getIdForBiome(this.biomesForGeneration[i]);
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    private float getIslandHeightValue(int p_185960_1_, int p_185960_2_, int p_185960_3_, int p_185960_4_) {
        float f = p_185960_1_ * 2 + p_185960_3_;
        float f1 = p_185960_2_ * 2 + p_185960_4_;
        float f2 = 100.0f - MathHelper.sqrt(f * f + f1 * f1) * 8.0f;
        if (f2 > 80.0f) {
            f2 = 80.0f;
        }
        if (f2 < -100.0f) {
            f2 = -100.0f;
        }
        for (int i = -12; i <= 12; ++i) {
            for (int j = -12; j <= 12; ++j) {
                long k = p_185960_1_ + i;
                long l = p_185960_2_ + j;
                if (k * k + l * l <= 4096L || !(this.islandNoise.getValue(k, l) < (double)-0.9f)) continue;
                float f3 = (MathHelper.abs(k) * 3439.0f + MathHelper.abs(l) * 147.0f) % 13.0f + 9.0f;
                f = p_185960_3_ - i * 2;
                f1 = p_185960_4_ - j * 2;
                float f4 = 100.0f - MathHelper.sqrt(f * f + f1 * f1) * f3;
                if (f4 > 80.0f) {
                    f4 = 80.0f;
                }
                if (f4 < -100.0f) {
                    f4 = -100.0f;
                }
                if (!(f4 > f2)) continue;
                f2 = f4;
            }
        }
        return f2;
    }

    public boolean isIslandChunk(int p_185961_1_, int p_185961_2_) {
        return (long)p_185961_1_ * (long)p_185961_1_ + (long)p_185961_2_ * (long)p_185961_2_ > 4096L && this.getIslandHeightValue(p_185961_1_, p_185961_2_, 1, 1) >= 0.0f;
    }

    private double[] getHeights(double[] p_185963_1_, int p_185963_2_, int p_185963_3_, int p_185963_4_, int p_185963_5_, int p_185963_6_, int p_185963_7_) {
        if (p_185963_1_ == null) {
            p_185963_1_ = new double[p_185963_5_ * p_185963_6_ * p_185963_7_];
        }
        double d0 = 684.412;
        double d1 = 684.412;
        this.pnr = this.perlinNoise1.generateNoiseOctaves(this.pnr, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_, (d0 *= 2.0) / 80.0, 4.277575000000001, d0 / 80.0);
        this.ar = this.lperlinNoise1.generateNoiseOctaves(this.ar, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_, d0, 684.412, d0);
        this.br = this.lperlinNoise2.generateNoiseOctaves(this.br, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_, d0, 684.412, d0);
        int i = p_185963_2_ / 2;
        int j = p_185963_4_ / 2;
        int k = 0;
        for (int l = 0; l < p_185963_5_; ++l) {
            for (int i1 = 0; i1 < p_185963_7_; ++i1) {
                float f = this.getIslandHeightValue(i, j, l, i1);
                for (int j1 = 0; j1 < p_185963_6_; ++j1) {
                    double d2 = this.ar[k] / 512.0;
                    double d3 = this.br[k] / 512.0;
                    double d5 = (this.pnr[k] / 10.0 + 1.0) / 2.0;
                    double d4 = d5 < 0.0 ? d2 : (d5 > 1.0 ? d3 : d2 + (d3 - d2) * d5);
                    d4 -= 8.0;
                    d4 += (double)f;
                    int k1 = 2;
                    if (j1 > p_185963_6_ / 2 - k1) {
                        double d6 = (float)(j1 - (p_185963_6_ / 2 - k1)) / 64.0f;
                        d6 = MathHelper.clamp(d6, 0.0, 1.0);
                        d4 = d4 * (1.0 - d6) + -3000.0 * d6;
                    }
                    if (j1 < (k1 = 8)) {
                        double d7 = (float)(k1 - j1) / ((float)k1 - 1.0f);
                        d4 = d4 * (1.0 - d7) + -30.0 * d7;
                    }
                    p_185963_1_[k] = d4;
                    ++k;
                }
            }
        }
        return p_185963_1_;
    }

    @Override
    public void populate(int x, int z) {
        BlockFalling.fallInstantly = true;
        BlockPos blockpos = new BlockPos(x * 16, 0, z * 16);
        if (this.mapFeaturesEnabled) {
            this.endCityGen.generateStructure(this.worldObj, this.rand, new ChunkPos(x, z));
        }
        this.worldObj.getBiome(blockpos.add(16, 0, 16)).decorate(this.worldObj, this.worldObj.rand, blockpos);
        long i = (long)x * (long)x + (long)z * (long)z;
        if (i > 4096L) {
            float f = this.getIslandHeightValue(x, z, 1, 1);
            if (f < -20.0f && this.rand.nextInt(14) == 0) {
                this.endIslands.generate(this.worldObj, this.rand, blockpos.add(this.rand.nextInt(16) + 8, 55 + this.rand.nextInt(16), this.rand.nextInt(16) + 8));
                if (this.rand.nextInt(4) == 0) {
                    this.endIslands.generate(this.worldObj, this.rand, blockpos.add(this.rand.nextInt(16) + 8, 55 + this.rand.nextInt(16), this.rand.nextInt(16) + 8));
                }
            }
            if (this.getIslandHeightValue(x, z, 1, 1) > 40.0f) {
                int i2;
                int l1;
                int j2;
                int j = this.rand.nextInt(5);
                for (int k = 0; k < j; ++k) {
                    int k1;
                    int i1;
                    int l = this.rand.nextInt(16) + 8;
                    int j1 = this.worldObj.getHeight(blockpos.add(l, 0, i1 = this.rand.nextInt(16) + 8)).getY();
                    if (j1 <= 0 || !this.worldObj.isAirBlock(blockpos.add(l, (k1 = j1 - 1) + 1, i1)) || this.worldObj.getBlockState(blockpos.add(l, k1, i1)).getBlock() != Blocks.END_STONE) continue;
                    BlockChorusFlower.generatePlant(this.worldObj, blockpos.add(l, k1 + 1, i1), this.rand, 8);
                }
                if (this.rand.nextInt(700) == 0 && (j2 = this.worldObj.getHeight(blockpos.add(l1 = this.rand.nextInt(16) + 8, 0, i2 = this.rand.nextInt(16) + 8)).getY()) > 0) {
                    int k2 = j2 + 3 + this.rand.nextInt(7);
                    BlockPos blockpos1 = blockpos.add(l1, k2, i2);
                    new WorldGenEndGateway().generate(this.worldObj, this.rand, blockpos1);
                    TileEntity tileentity = this.worldObj.getTileEntity(blockpos1);
                    if (tileentity instanceof TileEntityEndGateway) {
                        TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)tileentity;
                        tileentityendgateway.func_190603_b(this.field_191061_n);
                    }
                }
            }
        }
        BlockFalling.fallInstantly = false;
    }

    @Override
    public boolean generateStructures(Chunk chunkIn, int x, int z) {
        return false;
    }

    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
        return this.worldObj.getBiome(pos).getSpawnableList(creatureType);
    }

    @Override
    @Nullable
    public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position, boolean p_180513_4_) {
        return "EndCity".equals(structureName) && this.endCityGen != null ? this.endCityGen.getClosestStrongholdPos(worldIn, position, p_180513_4_) : null;
    }

    @Override
    public boolean func_193414_a(World p_193414_1_, String p_193414_2_, BlockPos p_193414_3_) {
        return "EndCity".equals(p_193414_2_) && this.endCityGen != null ? this.endCityGen.isInsideStructure(p_193414_3_) : false;
    }

    @Override
    public void recreateStructures(Chunk chunkIn, int x, int z) {
    }
}

