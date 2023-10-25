/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicates
 */
package net.minecraft.world.gen.feature;

import com.google.common.base.Predicates;
import java.util.Random;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenDesertWells
extends WorldGenerator {
    private static final BlockStateHelper field_175913_a = BlockStateHelper.forBlock(Blocks.sand).where(BlockSand.VARIANT, Predicates.equalTo((Object)BlockSand.EnumType.SAND));
    private final IBlockState field_175912_c;
    private final IBlockState field_175911_b = Blocks.stone_slab.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM);
    private final IBlockState field_175910_d;

    @Override
    public boolean generate(World world, Random random, BlockPos blockPos) {
        int n;
        while (world.isAirBlock(blockPos) && blockPos.getY() > 2) {
            blockPos = blockPos.down();
        }
        if (!field_175913_a.apply(world.getBlockState(blockPos))) {
            return false;
        }
        int n2 = -2;
        while (n2 <= 2) {
            n = -2;
            while (n <= 2) {
                if (world.isAirBlock(blockPos.add(n2, -1, n)) && world.isAirBlock(blockPos.add(n2, -2, n))) {
                    return false;
                }
                ++n;
            }
            ++n2;
        }
        n2 = -1;
        while (n2 <= 0) {
            n = -2;
            while (n <= 2) {
                int n3 = -2;
                while (n3 <= 2) {
                    world.setBlockState(blockPos.add(n, n2, n3), this.field_175912_c, 2);
                    ++n3;
                }
                ++n;
            }
            ++n2;
        }
        world.setBlockState(blockPos, this.field_175910_d, 2);
        for (EnumFacing enumFacing : EnumFacing.Plane.HORIZONTAL) {
            world.setBlockState(blockPos.offset(enumFacing), this.field_175910_d, 2);
        }
        int n4 = -2;
        while (n4 <= 2) {
            int n5 = -2;
            while (n5 <= 2) {
                if (n4 == -2 || n4 == 2 || n5 == -2 || n5 == 2) {
                    world.setBlockState(blockPos.add(n4, 1, n5), this.field_175912_c, 2);
                }
                ++n5;
            }
            ++n4;
        }
        world.setBlockState(blockPos.add(2, 1, 0), this.field_175911_b, 2);
        world.setBlockState(blockPos.add(-2, 1, 0), this.field_175911_b, 2);
        world.setBlockState(blockPos.add(0, 1, 2), this.field_175911_b, 2);
        world.setBlockState(blockPos.add(0, 1, -2), this.field_175911_b, 2);
        n4 = -1;
        while (n4 <= 1) {
            int n6 = -1;
            while (n6 <= 1) {
                if (n4 == 0 && n6 == 0) {
                    world.setBlockState(blockPos.add(n4, 4, n6), this.field_175912_c, 2);
                } else {
                    world.setBlockState(blockPos.add(n4, 4, n6), this.field_175911_b, 2);
                }
                ++n6;
            }
            ++n4;
        }
        n4 = 1;
        while (n4 <= 3) {
            world.setBlockState(blockPos.add(-1, n4, -1), this.field_175912_c, 2);
            world.setBlockState(blockPos.add(-1, n4, 1), this.field_175912_c, 2);
            world.setBlockState(blockPos.add(1, n4, -1), this.field_175912_c, 2);
            world.setBlockState(blockPos.add(1, n4, 1), this.field_175912_c, 2);
            ++n4;
        }
        return true;
    }

    public WorldGenDesertWells() {
        this.field_175912_c = Blocks.sandstone.getDefaultState();
        this.field_175910_d = Blocks.flowing_water.getDefaultState();
    }
}

