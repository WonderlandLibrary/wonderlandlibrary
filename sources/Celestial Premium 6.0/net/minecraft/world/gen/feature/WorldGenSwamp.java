/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class WorldGenSwamp
extends WorldGenAbstractTree {
    private static final IBlockState TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
    private static final IBlockState LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockOldLeaf.CHECK_DECAY, false);

    public WorldGenSwamp() {
        super(false);
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {
        int i = rand.nextInt(4) + 5;
        while (worldIn.getBlockState(position.down()).getMaterial() == Material.WATER) {
            position = position.down();
        }
        boolean flag = true;
        if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
            for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
                int k = 1;
                if (j == position.getY()) {
                    k = 0;
                }
                if (j >= position.getY() + 1 + i - 2) {
                    k = 3;
                }
                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
                        if (j >= 0 && j < 256) {
                            IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos.setPos(l, j, i1));
                            Block block = iblockstate.getBlock();
                            if (iblockstate.getMaterial() == Material.AIR || iblockstate.getMaterial() == Material.LEAVES) continue;
                            if (block != Blocks.WATER && block != Blocks.FLOWING_WATER) {
                                flag = false;
                                continue;
                            }
                            if (j <= position.getY()) continue;
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
            Block block1 = worldIn.getBlockState(position.down()).getBlock();
            if ((block1 == Blocks.GRASS || block1 == Blocks.DIRT) && position.getY() < 256 - i - 1) {
                this.setDirtAt(worldIn, position.down());
                for (int k1 = position.getY() - 3 + i; k1 <= position.getY() + i; ++k1) {
                    int j2 = k1 - (position.getY() + i);
                    int l2 = 2 - j2 / 2;
                    for (int j3 = position.getX() - l2; j3 <= position.getX() + l2; ++j3) {
                        int k3 = j3 - position.getX();
                        for (int i4 = position.getZ() - l2; i4 <= position.getZ() + l2; ++i4) {
                            BlockPos blockpos;
                            int j1 = i4 - position.getZ();
                            if (Math.abs(k3) == l2 && Math.abs(j1) == l2 && (rand.nextInt(2) == 0 || j2 == 0) || worldIn.getBlockState(blockpos = new BlockPos(j3, k1, i4)).isFullBlock()) continue;
                            this.setBlockAndNotifyAdequately(worldIn, blockpos, LEAF);
                        }
                    }
                }
                for (int l1 = 0; l1 < i; ++l1) {
                    IBlockState iblockstate1 = worldIn.getBlockState(position.up(l1));
                    Block block2 = iblockstate1.getBlock();
                    if (iblockstate1.getMaterial() != Material.AIR && iblockstate1.getMaterial() != Material.LEAVES && block2 != Blocks.FLOWING_WATER && block2 != Blocks.WATER) continue;
                    this.setBlockAndNotifyAdequately(worldIn, position.up(l1), TRUNK);
                }
                for (int i2 = position.getY() - 3 + i; i2 <= position.getY() + i; ++i2) {
                    int k2 = i2 - (position.getY() + i);
                    int i3 = 2 - k2 / 2;
                    BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
                    for (int l3 = position.getX() - i3; l3 <= position.getX() + i3; ++l3) {
                        for (int j4 = position.getZ() - i3; j4 <= position.getZ() + i3; ++j4) {
                            blockpos$mutableblockpos1.setPos(l3, i2, j4);
                            if (worldIn.getBlockState(blockpos$mutableblockpos1).getMaterial() != Material.LEAVES) continue;
                            BlockPos blockpos3 = blockpos$mutableblockpos1.west();
                            BlockPos blockpos4 = blockpos$mutableblockpos1.east();
                            BlockPos blockpos1 = blockpos$mutableblockpos1.north();
                            BlockPos blockpos2 = blockpos$mutableblockpos1.south();
                            if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos3).getMaterial() == Material.AIR) {
                                this.addVine(worldIn, blockpos3, BlockVine.EAST);
                            }
                            if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos4).getMaterial() == Material.AIR) {
                                this.addVine(worldIn, blockpos4, BlockVine.WEST);
                            }
                            if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos1).getMaterial() == Material.AIR) {
                                this.addVine(worldIn, blockpos1, BlockVine.SOUTH);
                            }
                            if (rand.nextInt(4) != 0 || worldIn.getBlockState(blockpos2).getMaterial() != Material.AIR) continue;
                            this.addVine(worldIn, blockpos2, BlockVine.NORTH);
                        }
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private void addVine(World worldIn, BlockPos pos, PropertyBool prop) {
        IBlockState iblockstate = Blocks.VINE.getDefaultState().withProperty(prop, true);
        this.setBlockAndNotifyAdequately(worldIn, pos, iblockstate);
        BlockPos blockpos = pos.down();
        for (int i = 4; worldIn.getBlockState(blockpos).getMaterial() == Material.AIR && i > 0; --i) {
            this.setBlockAndNotifyAdequately(worldIn, blockpos, iblockstate);
            blockpos = blockpos.down();
        }
    }
}

