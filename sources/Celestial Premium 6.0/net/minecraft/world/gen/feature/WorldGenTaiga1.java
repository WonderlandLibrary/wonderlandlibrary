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

public class WorldGenTaiga1
extends WorldGenAbstractTree {
    private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
    private static final IBlockState LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, false);

    public WorldGenTaiga1() {
        super(false);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int i = rand.nextInt(5) + 7;
        int j = i - rand.nextInt(2) - 3;
        int k = i - j;
        int l = 1 + rand.nextInt(k + 1);
        if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
            boolean flag = true;
            for (int i1 = position.getY(); i1 <= position.getY() + 1 + i && flag; ++i1) {
                int j1 = 1;
                j1 = i1 - position.getY() < j ? 0 : l;
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                for (int k1 = position.getX() - j1; k1 <= position.getX() + j1 && flag; ++k1) {
                    for (int l1 = position.getZ() - j1; l1 <= position.getZ() + j1 && flag; ++l1) {
                        if (i1 >= 0 && i1 < 256) {
                            if (this.canGrowInto(worldIn.getBlockState(blockpos$mutableblockpos.setPos(k1, i1, l1)).getBlock())) continue;
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
            if ((block == Blocks.GRASS || block == Blocks.DIRT) && position.getY() < 256 - i - 1) {
                this.setDirtAt(worldIn, position.down());
                int k2 = 0;
                for (int l2 = position.getY() + i; l2 >= position.getY() + j; --l2) {
                    for (int j3 = position.getX() - k2; j3 <= position.getX() + k2; ++j3) {
                        int k3 = j3 - position.getX();
                        for (int i2 = position.getZ() - k2; i2 <= position.getZ() + k2; ++i2) {
                            BlockPos blockpos;
                            int j2 = i2 - position.getZ();
                            if (Math.abs(k3) == k2 && Math.abs(j2) == k2 && k2 > 0 || worldIn.getBlockState(blockpos = new BlockPos(j3, l2, i2)).isFullBlock()) continue;
                            this.setBlockAndNotifyAdequately(worldIn, blockpos, LEAF);
                        }
                    }
                    if (k2 >= 1 && l2 == position.getY() + j + 1) {
                        --k2;
                        continue;
                    }
                    if (k2 >= l) continue;
                    ++k2;
                }
                for (int i3 = 0; i3 < i - 1; ++i3) {
                    Material material = worldIn.getBlockState(position.up(i3)).getMaterial();
                    if (material != Material.AIR && material != Material.LEAVES) continue;
                    this.setBlockAndNotifyAdequately(worldIn, position.up(i3), TRUNK);
                }
                return true;
            }
            return false;
        }
        return false;
    }
}

