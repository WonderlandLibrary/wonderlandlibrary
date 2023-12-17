// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.feature;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.block.BlockSand;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateMatcher;

public class WorldGenDesertWells extends WorldGenerator
{
    private static final BlockStateMatcher IS_SAND;
    private final IBlockState sandSlab;
    private final IBlockState sandstone;
    private final IBlockState water;
    
    public WorldGenDesertWells() {
        this.sandSlab = Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
        this.sandstone = Blocks.SANDSTONE.getDefaultState();
        this.water = Blocks.FLOWING_WATER.getDefaultState();
    }
    
    @Override
    public boolean generate(final World worldIn, final Random rand, BlockPos position) {
        while (worldIn.isAirBlock(position) && position.getY() > 2) {
            position = position.down();
        }
        if (!WorldGenDesertWells.IS_SAND.apply(worldIn.getBlockState(position))) {
            return false;
        }
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (worldIn.isAirBlock(position.add(i, -1, j)) && worldIn.isAirBlock(position.add(i, -2, j))) {
                    return false;
                }
            }
        }
        for (int l = -1; l <= 0; ++l) {
            for (int l2 = -2; l2 <= 2; ++l2) {
                for (int k = -2; k <= 2; ++k) {
                    worldIn.setBlockState(position.add(l2, l, k), this.sandstone, 2);
                }
            }
        }
        worldIn.setBlockState(position, this.water, 2);
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            worldIn.setBlockState(position.offset(enumfacing), this.water, 2);
        }
        for (int i2 = -2; i2 <= 2; ++i2) {
            for (int i3 = -2; i3 <= 2; ++i3) {
                if (i2 == -2 || i2 == 2 || i3 == -2 || i3 == 2) {
                    worldIn.setBlockState(position.add(i2, 1, i3), this.sandstone, 2);
                }
            }
        }
        worldIn.setBlockState(position.add(2, 1, 0), this.sandSlab, 2);
        worldIn.setBlockState(position.add(-2, 1, 0), this.sandSlab, 2);
        worldIn.setBlockState(position.add(0, 1, 2), this.sandSlab, 2);
        worldIn.setBlockState(position.add(0, 1, -2), this.sandSlab, 2);
        for (int j2 = -1; j2 <= 1; ++j2) {
            for (int j3 = -1; j3 <= 1; ++j3) {
                if (j2 == 0 && j3 == 0) {
                    worldIn.setBlockState(position.add(j2, 4, j3), this.sandstone, 2);
                }
                else {
                    worldIn.setBlockState(position.add(j2, 4, j3), this.sandSlab, 2);
                }
            }
        }
        for (int k2 = 1; k2 <= 3; ++k2) {
            worldIn.setBlockState(position.add(-1, k2, -1), this.sandstone, 2);
            worldIn.setBlockState(position.add(-1, k2, 1), this.sandstone, 2);
            worldIn.setBlockState(position.add(1, k2, -1), this.sandstone, 2);
            worldIn.setBlockState(position.add(1, k2, 1), this.sandstone, 2);
        }
        return true;
    }
    
    static {
        IS_SAND = BlockStateMatcher.forBlock(Blocks.SAND).where(BlockSand.VARIANT, (com.google.common.base.Predicate<? extends BlockSand.EnumType>)Predicates.equalTo((Object)BlockSand.EnumType.SAND));
    }
}
