/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.biome;

import java.util.Arrays;
import java.util.Random;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeGenMesa
extends BiomeGenBase {
    private long field_150622_aD;
    private NoiseGeneratorPerlin field_150624_aF;
    private IBlockState[] field_150621_aC;
    private NoiseGeneratorPerlin field_150623_aE;
    private boolean field_150620_aI;
    private boolean field_150626_aH;
    private NoiseGeneratorPerlin field_150625_aG;

    @Override
    public int getGrassColorAtPos(BlockPos blockPos) {
        return 9470285;
    }

    @Override
    public void decorate(World world, Random random, BlockPos blockPos) {
        super.decorate(world, random, blockPos);
    }

    @Override
    public int getFoliageColorAtPos(BlockPos blockPos) {
        return 10387789;
    }

    private void func_150619_a(long l) {
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        this.field_150621_aC = new IBlockState[64];
        Arrays.fill(this.field_150621_aC, Blocks.hardened_clay.getDefaultState());
        Random random = new Random(l);
        this.field_150625_aG = new NoiseGeneratorPerlin(random, 1);
        int n6 = 0;
        while (n6 < 64) {
            if ((n6 += random.nextInt(5) + 1) < 64) {
                this.field_150621_aC[n6] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
            }
            ++n6;
        }
        n6 = random.nextInt(4) + 2;
        int n7 = 0;
        while (n7 < n6) {
            n5 = random.nextInt(3) + 1;
            n4 = random.nextInt(64);
            n3 = 0;
            while (n4 + n3 < 64 && n3 < n5) {
                this.field_150621_aC[n4 + n3] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);
                ++n3;
            }
            ++n7;
        }
        n7 = random.nextInt(4) + 2;
        n5 = 0;
        while (n5 < n7) {
            n4 = random.nextInt(3) + 2;
            n3 = random.nextInt(64);
            n2 = 0;
            while (n3 + n2 < 64 && n2 < n4) {
                this.field_150621_aC[n3 + n2] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN);
                ++n2;
            }
            ++n5;
        }
        n5 = random.nextInt(4) + 2;
        n4 = 0;
        while (n4 < n5) {
            n3 = random.nextInt(3) + 1;
            n2 = random.nextInt(64);
            n = 0;
            while (n2 + n < 64 && n < n3) {
                this.field_150621_aC[n2 + n] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED);
                ++n;
            }
            ++n4;
        }
        n4 = random.nextInt(3) + 3;
        n3 = 0;
        n2 = 0;
        while (n2 < n4) {
            n = 1;
            n3 += random.nextInt(16) + 4;
            int n8 = 0;
            while (n3 + n8 < 64 && n8 < n) {
                this.field_150621_aC[n3 + n8] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);
                if (n3 + n8 > 1 && random.nextBoolean()) {
                    this.field_150621_aC[n3 + n8 - 1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }
                if (n3 + n8 < 63 && random.nextBoolean()) {
                    this.field_150621_aC[n3 + n8 + 1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
                }
                ++n8;
            }
            ++n2;
        }
    }

    @Override
    public WorldGenAbstractTree genBigTreeChance(Random random) {
        return this.worldGeneratorTrees;
    }

    @Override
    protected BiomeGenBase createMutatedBiome(int n) {
        boolean bl = this.biomeID == BiomeGenBase.mesa.biomeID;
        BiomeGenMesa biomeGenMesa = new BiomeGenMesa(n, bl, this.field_150620_aI);
        if (!bl) {
            biomeGenMesa.setHeight(height_LowHills);
            biomeGenMesa.setBiomeName(String.valueOf(this.biomeName) + " M");
        } else {
            biomeGenMesa.setBiomeName(String.valueOf(this.biomeName) + " (Bryce)");
        }
        biomeGenMesa.func_150557_a(this.color, true);
        return biomeGenMesa;
    }

    private IBlockState func_180629_a(int n, int n2, int n3) {
        int n4 = (int)Math.round(this.field_150625_aG.func_151601_a((double)n * 1.0 / 512.0, (double)n * 1.0 / 512.0) * 2.0);
        return this.field_150621_aC[(n2 + n4 + 64) % 64];
    }

    public BiomeGenMesa(int n, boolean bl, boolean bl2) {
        super(n);
        this.field_150626_aH = bl;
        this.field_150620_aI = bl2;
        this.setDisableRain();
        this.setTemperatureRainfall(2.0f, 0.0f);
        this.spawnableCreatureList.clear();
        this.topBlock = Blocks.sand.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
        this.fillerBlock = Blocks.stained_hardened_clay.getDefaultState();
        this.theBiomeDecorator.treesPerChunk = -999;
        this.theBiomeDecorator.deadBushPerChunk = 20;
        this.theBiomeDecorator.reedsPerChunk = 3;
        this.theBiomeDecorator.cactiPerChunk = 5;
        this.theBiomeDecorator.flowersPerChunk = 0;
        this.spawnableCreatureList.clear();
        if (bl2) {
            this.theBiomeDecorator.treesPerChunk = 5;
        }
    }

    @Override
    public void genTerrainBlocks(World world, Random random, ChunkPrimer chunkPrimer, int n, int n2, double d) {
        int n3;
        int n4;
        if (this.field_150621_aC == null || this.field_150622_aD != world.getSeed()) {
            this.func_150619_a(world.getSeed());
        }
        if (this.field_150623_aE == null || this.field_150624_aF == null || this.field_150622_aD != world.getSeed()) {
            Random random2 = new Random(this.field_150622_aD);
            this.field_150623_aE = new NoiseGeneratorPerlin(random2, 4);
            this.field_150624_aF = new NoiseGeneratorPerlin(random2, 1);
        }
        this.field_150622_aD = world.getSeed();
        double d2 = 0.0;
        if (this.field_150626_aH) {
            n4 = (n & 0xFFFFFFF0) + (n2 & 0xF);
            n3 = (n2 & 0xFFFFFFF0) + (n & 0xF);
            double d3 = Math.min(Math.abs(d), this.field_150623_aE.func_151601_a((double)n4 * 0.25, (double)n3 * 0.25));
            if (d3 > 0.0) {
                d2 = d3 * d3 * 2.5;
                double d4 = 0.001953125;
                double d5 = Math.abs(this.field_150624_aF.func_151601_a((double)n4 * d4, (double)n3 * d4));
                double d6 = Math.ceil(d5 * 50.0) + 14.0;
                if (d2 > d6) {
                    d2 = d6;
                }
                d2 += 64.0;
            }
        }
        n4 = n & 0xF;
        n3 = n2 & 0xF;
        int n5 = world.func_181545_F();
        IBlockState iBlockState = Blocks.stained_hardened_clay.getDefaultState();
        IBlockState iBlockState2 = this.fillerBlock;
        int n6 = (int)(d / 3.0 + 3.0 + random.nextDouble() * 0.25);
        boolean bl = Math.cos(d / 3.0 * Math.PI) > 0.0;
        int n7 = -1;
        boolean bl2 = false;
        int n8 = 255;
        while (n8 >= 0) {
            if (chunkPrimer.getBlockState(n3, n8, n4).getBlock().getMaterial() == Material.air && n8 < (int)d2) {
                chunkPrimer.setBlockState(n3, n8, n4, Blocks.stone.getDefaultState());
            }
            if (n8 <= random.nextInt(5)) {
                chunkPrimer.setBlockState(n3, n8, n4, Blocks.bedrock.getDefaultState());
            } else {
                IBlockState iBlockState3 = chunkPrimer.getBlockState(n3, n8, n4);
                if (iBlockState3.getBlock().getMaterial() == Material.air) {
                    n7 = -1;
                } else if (iBlockState3.getBlock() == Blocks.stone) {
                    IBlockState iBlockState4;
                    if (n7 == -1) {
                        bl2 = false;
                        if (n6 <= 0) {
                            iBlockState = null;
                            iBlockState2 = Blocks.stone.getDefaultState();
                        } else if (n8 >= n5 - 4 && n8 <= n5 + 1) {
                            iBlockState = Blocks.stained_hardened_clay.getDefaultState();
                            iBlockState2 = this.fillerBlock;
                        }
                        if (n8 < n5 && (iBlockState == null || iBlockState.getBlock().getMaterial() == Material.air)) {
                            iBlockState = Blocks.water.getDefaultState();
                        }
                        n7 = n6 + Math.max(0, n8 - n5);
                        if (n8 < n5 - 1) {
                            chunkPrimer.setBlockState(n3, n8, n4, iBlockState2);
                            if (iBlockState2.getBlock() == Blocks.stained_hardened_clay) {
                                chunkPrimer.setBlockState(n3, n8, n4, iBlockState2.getBlock().getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
                            }
                        } else if (this.field_150620_aI && n8 > 86 + n6 * 2) {
                            if (bl) {
                                chunkPrimer.setBlockState(n3, n8, n4, Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT));
                            } else {
                                chunkPrimer.setBlockState(n3, n8, n4, Blocks.grass.getDefaultState());
                            }
                        } else if (n8 <= n5 + 3 + n6) {
                            chunkPrimer.setBlockState(n3, n8, n4, this.topBlock);
                            bl2 = true;
                        } else {
                            iBlockState4 = n8 >= 64 && n8 <= 127 ? (bl ? Blocks.hardened_clay.getDefaultState() : this.func_180629_a(n, n8, n2)) : Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
                            chunkPrimer.setBlockState(n3, n8, n4, iBlockState4);
                        }
                    } else if (n7 > 0) {
                        --n7;
                        if (bl2) {
                            chunkPrimer.setBlockState(n3, n8, n4, Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
                        } else {
                            iBlockState4 = this.func_180629_a(n, n8, n2);
                            chunkPrimer.setBlockState(n3, n8, n4, iBlockState4);
                        }
                    }
                }
            }
            --n8;
        }
    }
}

