/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenLakes
extends WorldGenerator {
    private Block block;

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        Object object;
        int n;
        blockPos = blockPos.add(-8, 0, -8);
        while (blockPos.getY() > 5 && world.isAirBlock(blockPos)) {
            blockPos = blockPos.down();
        }
        if (blockPos.getY() <= 4) {
            return false;
        }
        blockPos = blockPos.down(4);
        boolean[] blArray = new boolean[2048];
        int n2 = random.nextInt(4) + 4;
        int n3 = 0;
        while (n3 < n2) {
            double d = random.nextDouble() * 6.0 + 3.0;
            double d2 = random.nextDouble() * 4.0 + 2.0;
            double d3 = random.nextDouble() * 6.0 + 3.0;
            double d4 = random.nextDouble() * (16.0 - d - 2.0) + 1.0 + d / 2.0;
            double d5 = random.nextDouble() * (8.0 - d2 - 4.0) + 2.0 + d2 / 2.0;
            double d6 = random.nextDouble() * (16.0 - d3 - 2.0) + 1.0 + d3 / 2.0;
            int n4 = 1;
            while (n4 < 15) {
                int n5 = 1;
                while (n5 < 15) {
                    int n6 = 1;
                    while (n6 < 7) {
                        double d7 = ((double)n4 - d4) / (d / 2.0);
                        double d8 = ((double)n6 - d5) / (d2 / 2.0);
                        double d9 = ((double)n5 - d6) / (d3 / 2.0);
                        double d10 = d7 * d7 + d8 * d8 + d9 * d9;
                        if (d10 < 1.0) {
                            blArray[(n4 * 16 + n5) * 8 + n6] = true;
                        }
                        ++n6;
                    }
                    ++n5;
                }
                ++n4;
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < 16) {
            int n7 = 0;
            while (n7 < 16) {
                n = 0;
                while (n < 8) {
                    boolean bl;
                    boolean bl2 = bl = !blArray[(n3 * 16 + n7) * 8 + n] && (n3 < 15 && blArray[((n3 + 1) * 16 + n7) * 8 + n] || n3 > 0 && blArray[((n3 - 1) * 16 + n7) * 8 + n] || n7 < 15 && blArray[(n3 * 16 + n7 + 1) * 8 + n] || n7 > 0 && blArray[(n3 * 16 + (n7 - 1)) * 8 + n] || n < 7 && blArray[(n3 * 16 + n7) * 8 + n + 1] || n > 0 && blArray[(n3 * 16 + n7) * 8 + (n - 1)]);
                    if (bl) {
                        object = world.getBlockState(blockPos.add(n3, n, n7)).getBlock().getMaterial();
                        if (n >= 4 && ((Material)object).isLiquid()) {
                            return false;
                        }
                        if (n < 4 && !((Material)object).isSolid() && world.getBlockState(blockPos.add(n3, n, n7)).getBlock() != this.block) {
                            return false;
                        }
                    }
                    ++n;
                }
                ++n7;
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < 16) {
            int n8 = 0;
            while (n8 < 16) {
                n = 0;
                while (n < 8) {
                    if (blArray[(n3 * 16 + n8) * 8 + n]) {
                        world.setBlockState(blockPos.add(n3, n, n8), n >= 4 ? Blocks.air.getDefaultState() : this.block.getDefaultState(), 2);
                    }
                    ++n;
                }
                ++n8;
            }
            ++n3;
        }
        n3 = 0;
        while (n3 < 16) {
            int n9 = 0;
            while (n9 < 16) {
                n = 4;
                while (n < 8) {
                    BlockPos blockPos2;
                    if (blArray[(n3 * 16 + n9) * 8 + n] && world.getBlockState(blockPos2 = blockPos.add(n3, n - 1, n9)).getBlock() == Blocks.dirt && world.getLightFor(EnumSkyBlock.SKY, blockPos.add(n3, n, n9)) > 0) {
                        object = world.getBiomeGenForCoords(blockPos2);
                        if (((BiomeGenBase)object).topBlock.getBlock() == Blocks.mycelium) {
                            world.setBlockState(blockPos2, Blocks.mycelium.getDefaultState(), 2);
                        } else {
                            world.setBlockState(blockPos2, Blocks.grass.getDefaultState(), 2);
                        }
                    }
                    ++n;
                }
                ++n9;
            }
            ++n3;
        }
        if (this.block.getMaterial() == Material.lava) {
            n3 = 0;
            while (n3 < 16) {
                int n10 = 0;
                while (n10 < 16) {
                    n = 0;
                    while (n < 8) {
                        boolean bl;
                        boolean bl3 = bl = !blArray[(n3 * 16 + n10) * 8 + n] && (n3 < 15 && blArray[((n3 + 1) * 16 + n10) * 8 + n] || n3 > 0 && blArray[((n3 - 1) * 16 + n10) * 8 + n] || n10 < 15 && blArray[(n3 * 16 + n10 + 1) * 8 + n] || n10 > 0 && blArray[(n3 * 16 + (n10 - 1)) * 8 + n] || n < 7 && blArray[(n3 * 16 + n10) * 8 + n + 1] || n > 0 && blArray[(n3 * 16 + n10) * 8 + (n - 1)]);
                        if (bl && (n < 4 || random.nextInt(2) != 0) && world.getBlockState(blockPos.add(n3, n, n10)).getBlock().getMaterial().isSolid()) {
                            world.setBlockState(blockPos.add(n3, n, n10), Blocks.stone.getDefaultState(), 2);
                        }
                        ++n;
                    }
                    ++n10;
                }
                ++n3;
            }
        }
        if (this.block.getMaterial() == Material.water) {
            n3 = 0;
            while (n3 < 16) {
                int n11 = 0;
                while (n11 < 16) {
                    n = 4;
                    if (world.canBlockFreezeWater(blockPos.add(n3, n, n11))) {
                        world.setBlockState(blockPos.add(n3, n, n11), Blocks.ice.getDefaultState(), 2);
                    }
                    ++n11;
                }
                ++n3;
            }
        }
        return true;
    }

    public WorldGenLakes(Block block) {
        this.block = block;
    }
}

