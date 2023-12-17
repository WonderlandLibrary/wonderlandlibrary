// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockCocoa;
import java.util.Iterator;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;

public class WorldGenTrees extends WorldGenAbstractTree
{
    private static final IBlockState DEFAULT_TRUNK;
    private static final IBlockState DEFAULT_LEAF;
    private final int minTreeHeight;
    private final boolean vinesGrow;
    private final IBlockState metaWood;
    private final IBlockState metaLeaves;
    
    public WorldGenTrees(final boolean p_i2027_1_) {
        this(p_i2027_1_, 4, WorldGenTrees.DEFAULT_TRUNK, WorldGenTrees.DEFAULT_LEAF, false);
    }
    
    public WorldGenTrees(final boolean notify, final int minTreeHeightIn, final IBlockState woodMeta, final IBlockState p_i46446_4_, final boolean growVines) {
        super(notify);
        this.minTreeHeight = minTreeHeightIn;
        this.metaWood = woodMeta;
        this.metaLeaves = p_i46446_4_;
        this.vinesGrow = growVines;
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        final int i = rand.nextInt(3) + this.minTreeHeight;
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
                k = 2;
            }
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                for (int i2 = position.getZ() - k; i2 <= position.getZ() + k && flag; ++i2) {
                    if (j >= 0 && j < 256) {
                        if (!this.canGrowInto(worldIn.getBlockState(blockpos$mutableblockpos.setPos(l, j, i2)).getBlock())) {
                            flag = false;
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
        final Block block = worldIn.getBlockState(position.down()).getBlock();
        if ((block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.FARMLAND) && position.getY() < 256 - i - 1) {
            this.setDirtAt(worldIn, position.down());
            final int k2 = 3;
            final int l2 = 0;
            for (int i3 = position.getY() - 3 + i; i3 <= position.getY() + i; ++i3) {
                final int i4 = i3 - (position.getY() + i);
                for (int j2 = 1 - i4 / 2, k3 = position.getX() - j2; k3 <= position.getX() + j2; ++k3) {
                    final int l3 = k3 - position.getX();
                    for (int i5 = position.getZ() - j2; i5 <= position.getZ() + j2; ++i5) {
                        final int j3 = i5 - position.getZ();
                        if (Math.abs(l3) != j2 || Math.abs(j3) != j2 || (rand.nextInt(2) != 0 && i4 != 0)) {
                            final BlockPos blockpos = new BlockPos(k3, i3, i5);
                            final Material material = worldIn.getBlockState(blockpos).getMaterial();
                            if (material == Material.AIR || material == Material.LEAVES || material == Material.VINE) {
                                this.setBlockAndNotifyAdequately(worldIn, blockpos, this.metaLeaves);
                            }
                        }
                    }
                }
            }
            for (int j4 = 0; j4 < i; ++j4) {
                final Material material2 = worldIn.getBlockState(position.up(j4)).getMaterial();
                if (material2 == Material.AIR || material2 == Material.LEAVES || material2 == Material.VINE) {
                    this.setBlockAndNotifyAdequately(worldIn, position.up(j4), this.metaWood);
                    if (this.vinesGrow && j4 > 0) {
                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(-1, j4, 0))) {
                            this.addVine(worldIn, position.add(-1, j4, 0), BlockVine.EAST);
                        }
                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(1, j4, 0))) {
                            this.addVine(worldIn, position.add(1, j4, 0), BlockVine.WEST);
                        }
                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, j4, -1))) {
                            this.addVine(worldIn, position.add(0, j4, -1), BlockVine.SOUTH);
                        }
                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, j4, 1))) {
                            this.addVine(worldIn, position.add(0, j4, 1), BlockVine.NORTH);
                        }
                    }
                }
            }
            if (this.vinesGrow) {
                for (int k4 = position.getY() - 3 + i; k4 <= position.getY() + i; ++k4) {
                    final int j5 = k4 - (position.getY() + i);
                    final int k5 = 2 - j5 / 2;
                    final BlockPos.MutableBlockPos blockpos$mutableblockpos2 = new BlockPos.MutableBlockPos();
                    for (int l4 = position.getX() - k5; l4 <= position.getX() + k5; ++l4) {
                        for (int i6 = position.getZ() - k5; i6 <= position.getZ() + k5; ++i6) {
                            blockpos$mutableblockpos2.setPos(l4, k4, i6);
                            if (worldIn.getBlockState(blockpos$mutableblockpos2).getMaterial() == Material.LEAVES) {
                                final BlockPos blockpos2 = blockpos$mutableblockpos2.west();
                                final BlockPos blockpos3 = blockpos$mutableblockpos2.east();
                                final BlockPos blockpos4 = blockpos$mutableblockpos2.north();
                                final BlockPos blockpos5 = blockpos$mutableblockpos2.south();
                                if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos2).getMaterial() == Material.AIR) {
                                    this.addHangingVine(worldIn, blockpos2, BlockVine.EAST);
                                }
                                if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos3).getMaterial() == Material.AIR) {
                                    this.addHangingVine(worldIn, blockpos3, BlockVine.WEST);
                                }
                                if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos4).getMaterial() == Material.AIR) {
                                    this.addHangingVine(worldIn, blockpos4, BlockVine.SOUTH);
                                }
                                if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos5).getMaterial() == Material.AIR) {
                                    this.addHangingVine(worldIn, blockpos5, BlockVine.NORTH);
                                }
                            }
                        }
                    }
                }
                if (rand.nextInt(5) == 0 && i > 5) {
                    for (int l5 = 0; l5 < 2; ++l5) {
                        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                            if (rand.nextInt(4 - l5) == 0) {
                                final EnumFacing enumfacing2 = enumfacing.getOpposite();
                                this.placeCocoa(worldIn, rand.nextInt(3), position.add(enumfacing2.getXOffset(), i - 5 + l5, enumfacing2.getZOffset()), enumfacing);
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
    
    private void placeCocoa(final World worldIn, final int p_181652_2_, final BlockPos pos, final EnumFacing side) {
        this.setBlockAndNotifyAdequately(worldIn, pos, Blocks.COCOA.getDefaultState().withProperty((IProperty<Comparable>)BlockCocoa.AGE, p_181652_2_).withProperty((IProperty<Comparable>)BlockCocoa.FACING, side));
    }
    
    private void addVine(final World worldIn, final BlockPos pos, final PropertyBool prop) {
        this.setBlockAndNotifyAdequately(worldIn, pos, Blocks.VINE.getDefaultState().withProperty((IProperty<Comparable>)prop, true));
    }
    
    private void addHangingVine(final World worldIn, final BlockPos pos, final PropertyBool prop) {
        this.addVine(worldIn, pos, prop);
        int i = 4;
        for (BlockPos blockpos = pos.down(); worldIn.getBlockState(blockpos).getMaterial() == Material.AIR && i > 0; blockpos = blockpos.down(), --i) {
            this.addVine(worldIn, blockpos, prop);
        }
    }
    
    static {
        DEFAULT_TRUNK = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
        DEFAULT_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty((IProperty<Comparable>)BlockLeaves.CHECK_DECAY, false);
    }
}
