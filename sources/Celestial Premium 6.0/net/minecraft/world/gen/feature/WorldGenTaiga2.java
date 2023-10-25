/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenTaiga2
extends WorldGenAbstractTree {
    private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, false);

    public WorldGenTaiga2(boolean p_i2025_1_) {
        super(p_i2025_1_);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int i = rand.nextInt(4) + 6;
        int j = 1 + rand.nextInt(2);
        int k = i - j;
        int l = 2 + rand.nextInt(2);
        boolean flag = true;
        if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
            for (int i1 = position.getY(); i1 <= position.getY() + 1 + i && flag; ++i1) {
                int j1 = i1 - position.getY() < j ? 0 : l;
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                for (int k1 = position.getX() - j1; k1 <= position.getX() + j1 && flag; ++k1) {
                    for (int l1 = position.getZ() - j1; l1 <= position.getZ() + j1 && flag; ++l1) {
                        if (i1 >= 0 && i1 < 256) {
                            Material material = worldIn.getBlockState(blockpos$mutableblockpos.setPos(k1, i1, l1)).getMaterial();
                            if (material == Material.AIR || material == Material.LEAVES) continue;
                            flag = false;
                            continue;
                        }
                        flag = false;
                    }
                }
            }
            if (!flag) {
                return false;
            }
            Block block = worldIn.getBlockState(position.down()).getBlock();
            if ((block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.FARMLAND) && position.getY() < 256 - i - 1) {
                this.setDirtAt(worldIn, position.down());
                int i3 = rand.nextInt(2);
                int j3 = 1;
                int k3 = 0;
                for (int l3 = 0; l3 <= k; ++l3) {
                    int j4 = position.getY() + i - l3;
                    for (int i2 = position.getX() - i3; i2 <= position.getX() + i3; ++i2) {
                        int j2 = i2 - position.getX();
                        for (int k2 = position.getZ() - i3; k2 <= position.getZ() + i3; ++k2) {
                            BlockPos blockpos;
                            int l2 = k2 - position.getZ();
                            if (Math.abs(j2) == i3 && Math.abs(l2) == i3 && i3 > 0 || worldIn.getBlockState(blockpos = new BlockPos(i2, j4, k2)).isFullBlock()) continue;
                            this.setBlockAndNotifyAdequately(worldIn, blockpos, LEAF);
                        }
                    }
                    if (i3 >= j3) {
                        i3 = k3;
                        k3 = 1;
                        if (++j3 <= l) continue;
                        j3 = l;
                        continue;
                    }
                    ++i3;
                }
                int i4 = rand.nextInt(3);
                for (int k4 = 0; k4 < i - i4; ++k4) {
                    Material material1 = worldIn.getBlockState(position.up(k4)).getMaterial();
                    if (material1 != Material.AIR && material1 != Material.LEAVES) continue;
                    this.setBlockAndNotifyAdequately(worldIn, position.up(k4), TRUNK);
                }
                return true;
            }
            return false;
        }
        return false;
    }
}

