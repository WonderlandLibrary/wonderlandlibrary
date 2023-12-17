// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;

public class WorldGenSwamp extends WorldGenAbstractTree
{
    private static final IBlockState TRUNK;
    private static final IBlockState LEAF;
    
    public WorldGenSwamp() {
        super(false);
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        final int i = rand.nextInt(4) + 5;
        while (worldIn.getBlockState(position.down()).getMaterial() == Material.WATER) {
            position = position.down();
        }
        boolean flag = true;
        if (position.getY() < 1 || position.getY() + i + 1 > 256) {
            return false;
        }
        for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
            int k = 1;
            if (j == position.getY()) {
                k = 0;
            }
            if (j >= position.getY() + 1 + i - 2) {
                k = 3;
            }
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                for (int i2 = position.getZ() - k; i2 <= position.getZ() + k && flag; ++i2) {
                    if (j >= 0 && j < 256) {
                        final IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos.setPos(l, j, i2));
                        final Block block = iblockstate.getBlock();
                        if (iblockstate.getMaterial() != Material.AIR && iblockstate.getMaterial() != Material.LEAVES) {
                            if (block != Blocks.WATER && block != Blocks.FLOWING_WATER) {
                                flag = false;
                            }
                            else if (j > position.getY()) {
                                flag = false;
                            }
                        }
                    }
                    else {
                        flag = false;
                    }
                }
            }
        }
        if (!flag) {
            return false;
        }
        final Block block2 = worldIn.getBlockState(position.down()).getBlock();
        if ((block2 == Blocks.GRASS || block2 == Blocks.DIRT) && position.getY() < 256 - i - 1) {
            this.setDirtAt(worldIn, position.down());
            for (int k2 = position.getY() - 3 + i; k2 <= position.getY() + i; ++k2) {
                final int j2 = k2 - (position.getY() + i);
                for (int l2 = 2 - j2 / 2, j3 = position.getX() - l2; j3 <= position.getX() + l2; ++j3) {
                    final int k3 = j3 - position.getX();
                    for (int i3 = position.getZ() - l2; i3 <= position.getZ() + l2; ++i3) {
                        final int j4 = i3 - position.getZ();
                        if (Math.abs(k3) != l2 || Math.abs(j4) != l2 || (rand.nextInt(2) != 0 && j2 != 0)) {
                            final BlockPos blockpos = new BlockPos(j3, k2, i3);
                            if (!worldIn.getBlockState(blockpos).isFullBlock()) {
                                this.setBlockAndNotifyAdequately(worldIn, blockpos, WorldGenSwamp.LEAF);
                            }
                        }
                    }
                }
            }
            for (int l3 = 0; l3 < i; ++l3) {
                final IBlockState iblockstate2 = worldIn.getBlockState(position.up(l3));
                final Block block3 = iblockstate2.getBlock();
                if (iblockstate2.getMaterial() == Material.AIR || iblockstate2.getMaterial() == Material.LEAVES || block3 == Blocks.FLOWING_WATER || block3 == Blocks.WATER) {
                    this.setBlockAndNotifyAdequately(worldIn, position.up(l3), WorldGenSwamp.TRUNK);
                }
            }
            for (int i4 = position.getY() - 3 + i; i4 <= position.getY() + i; ++i4) {
                final int k4 = i4 - (position.getY() + i);
                final int i5 = 2 - k4 / 2;
                final BlockPos.MutableBlockPos blockpos$mutableblockpos2 = new BlockPos.MutableBlockPos();
                for (int l4 = position.getX() - i5; l4 <= position.getX() + i5; ++l4) {
                    for (int j5 = position.getZ() - i5; j5 <= position.getZ() + i5; ++j5) {
                        blockpos$mutableblockpos2.setPos(l4, i4, j5);
                        if (worldIn.getBlockState(blockpos$mutableblockpos2).getMaterial() == Material.LEAVES) {
                            final BlockPos blockpos2 = blockpos$mutableblockpos2.west();
                            final BlockPos blockpos3 = blockpos$mutableblockpos2.east();
                            final BlockPos blockpos4 = blockpos$mutableblockpos2.north();
                            final BlockPos blockpos5 = blockpos$mutableblockpos2.south();
                            if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos2).getMaterial() == Material.AIR) {
                                this.addVine(worldIn, blockpos2, BlockVine.EAST);
                            }
                            if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos3).getMaterial() == Material.AIR) {
                                this.addVine(worldIn, blockpos3, BlockVine.WEST);
                            }
                            if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos4).getMaterial() == Material.AIR) {
                                this.addVine(worldIn, blockpos4, BlockVine.SOUTH);
                            }
                            if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos5).getMaterial() == Material.AIR) {
                                this.addVine(worldIn, blockpos5, BlockVine.NORTH);
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private void addVine(final World worldIn, final BlockPos pos, final PropertyBool prop) {
        final IBlockState iblockstate = Blocks.VINE.getDefaultState().withProperty((IProperty<Comparable>)prop, true);
        this.setBlockAndNotifyAdequately(worldIn, pos, iblockstate);
        int i = 4;
        for (BlockPos blockpos = pos.down(); worldIn.getBlockState(blockpos).getMaterial() == Material.AIR && i > 0; blockpos = blockpos.down(), --i) {
            this.setBlockAndNotifyAdequately(worldIn, blockpos, iblockstate);
        }
    }
    
    static {
        TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
        LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty((IProperty<Comparable>)BlockOldLeaf.CHECK_DECAY, false);
    }
}
