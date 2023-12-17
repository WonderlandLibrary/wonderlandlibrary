// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen;

import javax.annotation.Nullable;
import java.util.List;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.world.gen.feature.WorldGenEndGateway;
import net.minecraft.block.BlockChorusFlower;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.block.BlockFalling;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenEndIsland;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenEndCity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import java.util.Random;

public class ChunkGeneratorEnd implements IChunkGenerator
{
    private final Random rand;
    protected static final IBlockState END_STONE;
    protected static final IBlockState AIR;
    private final NoiseGeneratorOctaves lperlinNoise1;
    private final NoiseGeneratorOctaves lperlinNoise2;
    private final NoiseGeneratorOctaves perlinNoise1;
    public NoiseGeneratorOctaves noiseGen5;
    public NoiseGeneratorOctaves noiseGen6;
    private final World world;
    private final boolean mapFeaturesEnabled;
    private final BlockPos spawnPoint;
    private final MapGenEndCity endCityGen;
    private final NoiseGeneratorSimplex islandNoise;
    private double[] buffer;
    private Biome[] biomesForGeneration;
    double[] pnr;
    double[] ar;
    double[] br;
    private final WorldGenEndIsland endIslands;
    
    public ChunkGeneratorEnd(final World p_i47241_1_, final boolean p_i47241_2_, final long p_i47241_3_, final BlockPos p_i47241_5_) {
        this.endCityGen = new MapGenEndCity(this);
        this.endIslands = new WorldGenEndIsland();
        this.world = p_i47241_1_;
        this.mapFeaturesEnabled = p_i47241_2_;
        this.spawnPoint = p_i47241_5_;
        this.rand = new Random(p_i47241_3_);
        this.lperlinNoise1 = new NoiseGeneratorOctaves(this.rand, 16);
        this.lperlinNoise2 = new NoiseGeneratorOctaves(this.rand, 16);
        this.perlinNoise1 = new NoiseGeneratorOctaves(this.rand, 8);
        this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
        this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
        this.islandNoise = new NoiseGeneratorSimplex(this.rand);
    }
    
    public void setBlocksInChunk(final int x, final int z, final ChunkPrimer primer) {
        final int i = 2;
        final int j = 3;
        final int k = 33;
        final int l = 3;
        this.buffer = this.getHeights(this.buffer, x * 2, 0, z * 2, 3, 33, 3);
        for (int i2 = 0; i2 < 2; ++i2) {
            for (int j2 = 0; j2 < 2; ++j2) {
                for (int k2 = 0; k2 < 32; ++k2) {
                    final double d0 = 0.25;
                    double d2 = this.buffer[((i2 + 0) * 3 + j2 + 0) * 33 + k2 + 0];
                    double d3 = this.buffer[((i2 + 0) * 3 + j2 + 1) * 33 + k2 + 0];
                    double d4 = this.buffer[((i2 + 1) * 3 + j2 + 0) * 33 + k2 + 0];
                    double d5 = this.buffer[((i2 + 1) * 3 + j2 + 1) * 33 + k2 + 0];
                    final double d6 = (this.buffer[((i2 + 0) * 3 + j2 + 0) * 33 + k2 + 1] - d2) * 0.25;
                    final double d7 = (this.buffer[((i2 + 0) * 3 + j2 + 1) * 33 + k2 + 1] - d3) * 0.25;
                    final double d8 = (this.buffer[((i2 + 1) * 3 + j2 + 0) * 33 + k2 + 1] - d4) * 0.25;
                    final double d9 = (this.buffer[((i2 + 1) * 3 + j2 + 1) * 33 + k2 + 1] - d5) * 0.25;
                    for (int l2 = 0; l2 < 4; ++l2) {
                        final double d10 = 0.125;
                        double d11 = d2;
                        double d12 = d3;
                        final double d13 = (d4 - d2) * 0.125;
                        final double d14 = (d5 - d3) * 0.125;
                        for (int i3 = 0; i3 < 8; ++i3) {
                            final double d15 = 0.125;
                            double d16 = d11;
                            final double d17 = (d12 - d11) * 0.125;
                            for (int j3 = 0; j3 < 8; ++j3) {
                                IBlockState iblockstate = ChunkGeneratorEnd.AIR;
                                if (d16 > 0.0) {
                                    iblockstate = ChunkGeneratorEnd.END_STONE;
                                }
                                final int k3 = i3 + i2 * 8;
                                final int l3 = l2 + k2 * 4;
                                final int i4 = j3 + j2 * 8;
                                primer.setBlockState(k3, l3, i4, iblockstate);
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
    
    public void buildSurfaces(final ChunkPrimer primer) {
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                final int k = 1;
                int l = -1;
                final IBlockState iblockstate = ChunkGeneratorEnd.END_STONE;
                final IBlockState iblockstate2 = ChunkGeneratorEnd.END_STONE;
                for (int i2 = 127; i2 >= 0; --i2) {
                    final IBlockState iblockstate3 = primer.getBlockState(i, i2, j);
                    if (iblockstate3.getMaterial() == Material.AIR) {
                        l = -1;
                    }
                    else if (iblockstate3.getBlock() == Blocks.STONE) {
                        if (l == -1) {
                            l = 1;
                            if (i2 >= 0) {
                                primer.setBlockState(i, i2, j, iblockstate);
                            }
                            else {
                                primer.setBlockState(i, i2, j, iblockstate2);
                            }
                        }
                        else if (l > 0) {
                            --l;
                            primer.setBlockState(i, i2, j, iblockstate2);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public Chunk generateChunk(final int x, final int z) {
        this.rand.setSeed(x * 341873128712L + z * 132897987541L);
        final ChunkPrimer chunkprimer = new ChunkPrimer();
        this.biomesForGeneration = this.world.getBiomeProvider().getBiomes(this.biomesForGeneration, x * 16, z * 16, 16, 16);
        this.setBlocksInChunk(x, z, chunkprimer);
        this.buildSurfaces(chunkprimer);
        if (this.mapFeaturesEnabled) {
            this.endCityGen.generate(this.world, x, z, chunkprimer);
        }
        final Chunk chunk = new Chunk(this.world, chunkprimer, x, z);
        final byte[] abyte = chunk.getBiomeArray();
        for (int i = 0; i < abyte.length; ++i) {
            abyte[i] = (byte)Biome.getIdForBiome(this.biomesForGeneration[i]);
        }
        chunk.generateSkylightMap();
        return chunk;
    }
    
    private float getIslandHeightValue(final int p_185960_1_, final int p_185960_2_, final int p_185960_3_, final int p_185960_4_) {
        float f = (float)(p_185960_1_ * 2 + p_185960_3_);
        float f2 = (float)(p_185960_2_ * 2 + p_185960_4_);
        float f3 = 100.0f - MathHelper.sqrt(f * f + f2 * f2) * 8.0f;
        if (f3 > 80.0f) {
            f3 = 80.0f;
        }
        if (f3 < -100.0f) {
            f3 = -100.0f;
        }
        for (int i = -12; i <= 12; ++i) {
            for (int j = -12; j <= 12; ++j) {
                final long k = p_185960_1_ + i;
                final long l = p_185960_2_ + j;
                if (k * k + l * l > 4096L && this.islandNoise.getValue((double)k, (double)l) < -0.8999999761581421) {
                    final float f4 = (MathHelper.abs((float)k) * 3439.0f + MathHelper.abs((float)l) * 147.0f) % 13.0f + 9.0f;
                    f = (float)(p_185960_3_ - i * 2);
                    f2 = (float)(p_185960_4_ - j * 2);
                    float f5 = 100.0f - MathHelper.sqrt(f * f + f2 * f2) * f4;
                    if (f5 > 80.0f) {
                        f5 = 80.0f;
                    }
                    if (f5 < -100.0f) {
                        f5 = -100.0f;
                    }
                    if (f5 > f3) {
                        f3 = f5;
                    }
                }
            }
        }
        return f3;
    }
    
    public boolean isIslandChunk(final int p_185961_1_, final int p_185961_2_) {
        return p_185961_1_ * (long)p_185961_1_ + p_185961_2_ * (long)p_185961_2_ > 4096L && this.getIslandHeightValue(p_185961_1_, p_185961_2_, 1, 1) >= 0.0f;
    }
    
    private double[] getHeights(double[] p_185963_1_, final int p_185963_2_, final int p_185963_3_, final int p_185963_4_, final int p_185963_5_, final int p_185963_6_, final int p_185963_7_) {
        if (p_185963_1_ == null) {
            p_185963_1_ = new double[p_185963_5_ * p_185963_6_ * p_185963_7_];
        }
        double d0 = 684.412;
        final double d2 = 684.412;
        d0 *= 2.0;
        this.pnr = this.perlinNoise1.generateNoiseOctaves(this.pnr, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_, d0 / 80.0, 4.277575000000001, d0 / 80.0);
        this.ar = this.lperlinNoise1.generateNoiseOctaves(this.ar, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_, d0, 684.412, d0);
        this.br = this.lperlinNoise2.generateNoiseOctaves(this.br, p_185963_2_, p_185963_3_, p_185963_4_, p_185963_5_, p_185963_6_, p_185963_7_, d0, 684.412, d0);
        final int i = p_185963_2_ / 2;
        final int j = p_185963_4_ / 2;
        int k = 0;
        for (int l = 0; l < p_185963_5_; ++l) {
            for (int i2 = 0; i2 < p_185963_7_; ++i2) {
                final float f = this.getIslandHeightValue(i, j, l, i2);
                for (int j2 = 0; j2 < p_185963_6_; ++j2) {
                    final double d3 = this.ar[k] / 512.0;
                    final double d4 = this.br[k] / 512.0;
                    final double d5 = (this.pnr[k] / 10.0 + 1.0) / 2.0;
                    double d6;
                    if (d5 < 0.0) {
                        d6 = d3;
                    }
                    else if (d5 > 1.0) {
                        d6 = d4;
                    }
                    else {
                        d6 = d3 + (d4 - d3) * d5;
                    }
                    d6 -= 8.0;
                    d6 += f;
                    int k2 = 2;
                    if (j2 > p_185963_6_ / 2 - k2) {
                        double d7 = (j2 - (p_185963_6_ / 2 - k2)) / 64.0f;
                        d7 = MathHelper.clamp(d7, 0.0, 1.0);
                        d6 = d6 * (1.0 - d7) + -3000.0 * d7;
                    }
                    k2 = 8;
                    if (j2 < k2) {
                        final double d8 = (k2 - j2) / (k2 - 1.0f);
                        d6 = d6 * (1.0 - d8) + -30.0 * d8;
                    }
                    p_185963_1_[k] = d6;
                    ++k;
                }
            }
        }
        return p_185963_1_;
    }
    
    @Override
    public void populate(final int x, final int z) {
        BlockFalling.fallInstantly = true;
        final BlockPos blockpos = new BlockPos(x * 16, 0, z * 16);
        if (this.mapFeaturesEnabled) {
            this.endCityGen.generateStructure(this.world, this.rand, new ChunkPos(x, z));
        }
        this.world.getBiome(blockpos.add(16, 0, 16)).decorate(this.world, this.world.rand, blockpos);
        final long i = x * (long)x + z * (long)z;
        if (i > 4096L) {
            final float f = this.getIslandHeightValue(x, z, 1, 1);
            if (f < -20.0f && this.rand.nextInt(14) == 0) {
                this.endIslands.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, 55 + this.rand.nextInt(16), this.rand.nextInt(16) + 8));
                if (this.rand.nextInt(4) == 0) {
                    this.endIslands.generate(this.world, this.rand, blockpos.add(this.rand.nextInt(16) + 8, 55 + this.rand.nextInt(16), this.rand.nextInt(16) + 8));
                }
            }
            if (this.getIslandHeightValue(x, z, 1, 1) > 40.0f) {
                for (int j = this.rand.nextInt(5), k = 0; k < j; ++k) {
                    final int l = this.rand.nextInt(16) + 8;
                    final int i2 = this.rand.nextInt(16) + 8;
                    final int j2 = this.world.getHeight(blockpos.add(l, 0, i2)).getY();
                    if (j2 > 0) {
                        final int k2 = j2 - 1;
                        if (this.world.isAirBlock(blockpos.add(l, k2 + 1, i2)) && this.world.getBlockState(blockpos.add(l, k2, i2)).getBlock() == Blocks.END_STONE) {
                            BlockChorusFlower.generatePlant(this.world, blockpos.add(l, k2 + 1, i2), this.rand, 8);
                        }
                    }
                }
                if (this.rand.nextInt(700) == 0) {
                    final int l2 = this.rand.nextInt(16) + 8;
                    final int i3 = this.rand.nextInt(16) + 8;
                    final int j3 = this.world.getHeight(blockpos.add(l2, 0, i3)).getY();
                    if (j3 > 0) {
                        final int k3 = j3 + 3 + this.rand.nextInt(7);
                        final BlockPos blockpos2 = blockpos.add(l2, k3, i3);
                        new WorldGenEndGateway().generate(this.world, this.rand, blockpos2);
                        final TileEntity tileentity = this.world.getTileEntity(blockpos2);
                        if (tileentity instanceof TileEntityEndGateway) {
                            final TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)tileentity;
                            tileentityendgateway.setExactPosition(this.spawnPoint);
                        }
                    }
                }
            }
        }
        BlockFalling.fallInstantly = false;
    }
    
    @Override
    public boolean generateStructures(final Chunk chunkIn, final int x, final int z) {
        return false;
    }
    
    @Override
    public List<Biome.SpawnListEntry> getPossibleCreatures(final EnumCreatureType creatureType, final BlockPos pos) {
        return this.world.getBiome(pos).getSpawnableList(creatureType);
    }
    
    @Nullable
    @Override
    public BlockPos getNearestStructurePos(final World worldIn, final String structureName, final BlockPos position, final boolean findUnexplored) {
        return ("EndCity".equals(structureName) && this.endCityGen != null) ? this.endCityGen.getNearestStructurePos(worldIn, position, findUnexplored) : null;
    }
    
    @Override
    public boolean isInsideStructure(final World worldIn, final String structureName, final BlockPos pos) {
        return "EndCity".equals(structureName) && this.endCityGen != null && this.endCityGen.isInsideStructure(pos);
    }
    
    @Override
    public void recreateStructures(final Chunk chunkIn, final int x, final int z) {
    }
    
    static {
        END_STONE = Blocks.END_STONE.getDefaultState();
        AIR = Blocks.AIR.getDefaultState();
    }
}
