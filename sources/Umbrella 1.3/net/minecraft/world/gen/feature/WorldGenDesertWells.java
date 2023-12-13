/*
 * Decompiled with CFR 0.150.
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
    private static final BlockStateHelper field_175913_a = BlockStateHelper.forBlock(Blocks.sand).func_177637_a(BlockSand.VARIANT_PROP, Predicates.equalTo((Object)BlockSand.EnumType.SAND));
    private final IBlockState field_175911_b = Blocks.stone_slab.getDefaultState().withProperty(BlockStoneSlab.field_176556_M, (Comparable)((Object)BlockStoneSlab.EnumType.SAND)).withProperty(BlockSlab.HALF_PROP, (Comparable)((Object)BlockSlab.EnumBlockHalf.BOTTOM));
    private final IBlockState field_175912_c = Blocks.sandstone.getDefaultState();
    private final IBlockState field_175910_d = Blocks.flowing_water.getDefaultState();
    private static final String __OBFID = "CL_00000407";

    @Override
    public boolean generate(World worldIn, Random p_180709_2_, BlockPos p_180709_3_) {
        int var5;
        int var4;
        while (worldIn.isAirBlock(p_180709_3_) && p_180709_3_.getY() > 2) {
            p_180709_3_ = p_180709_3_.offsetDown();
        }
        if (!field_175913_a.func_177639_a(worldIn.getBlockState(p_180709_3_))) {
            return false;
        }
        for (var4 = -2; var4 <= 2; ++var4) {
            for (var5 = -2; var5 <= 2; ++var5) {
                if (!worldIn.isAirBlock(p_180709_3_.add(var4, -1, var5)) || !worldIn.isAirBlock(p_180709_3_.add(var4, -2, var5))) continue;
                return false;
            }
        }
        for (var4 = -1; var4 <= 0; ++var4) {
            for (var5 = -2; var5 <= 2; ++var5) {
                for (int var6 = -2; var6 <= 2; ++var6) {
                    worldIn.setBlockState(p_180709_3_.add(var5, var4, var6), this.field_175912_c, 2);
                }
            }
        }
        worldIn.setBlockState(p_180709_3_, this.field_175910_d, 2);
        for (EnumFacing var8 : EnumFacing.Plane.HORIZONTAL) {
            worldIn.setBlockState(p_180709_3_.offset(var8), this.field_175910_d, 2);
        }
        for (var4 = -2; var4 <= 2; ++var4) {
            for (var5 = -2; var5 <= 2; ++var5) {
                if (var4 != -2 && var4 != 2 && var5 != -2 && var5 != 2) continue;
                worldIn.setBlockState(p_180709_3_.add(var4, 1, var5), this.field_175912_c, 2);
            }
        }
        worldIn.setBlockState(p_180709_3_.add(2, 1, 0), this.field_175911_b, 2);
        worldIn.setBlockState(p_180709_3_.add(-2, 1, 0), this.field_175911_b, 2);
        worldIn.setBlockState(p_180709_3_.add(0, 1, 2), this.field_175911_b, 2);
        worldIn.setBlockState(p_180709_3_.add(0, 1, -2), this.field_175911_b, 2);
        for (var4 = -1; var4 <= 1; ++var4) {
            for (var5 = -1; var5 <= 1; ++var5) {
                if (var4 == 0 && var5 == 0) {
                    worldIn.setBlockState(p_180709_3_.add(var4, 4, var5), this.field_175912_c, 2);
                    continue;
                }
                worldIn.setBlockState(p_180709_3_.add(var4, 4, var5), this.field_175911_b, 2);
            }
        }
        for (var4 = 1; var4 <= 3; ++var4) {
            worldIn.setBlockState(p_180709_3_.add(-1, var4, -1), this.field_175912_c, 2);
            worldIn.setBlockState(p_180709_3_.add(-1, var4, 1), this.field_175912_c, 2);
            worldIn.setBlockState(p_180709_3_.add(1, var4, -1), this.field_175912_c, 2);
            worldIn.setBlockState(p_180709_3_.add(1, var4, 1), this.field_175912_c, 2);
        }
        return true;
    }
}

