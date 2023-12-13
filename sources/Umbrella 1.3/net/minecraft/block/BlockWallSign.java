/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSign;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockWallSign
extends BlockSign {
    public static final PropertyDirection field_176412_a = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    private static final String __OBFID = "CL_00002047";

    public BlockWallSign() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(field_176412_a, (Comparable)((Object)EnumFacing.NORTH)));
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
        EnumFacing var3 = (EnumFacing)((Object)access.getBlockState(pos).getValue(field_176412_a));
        float var4 = 0.28125f;
        float var5 = 0.78125f;
        float var6 = 0.0f;
        float var7 = 1.0f;
        float var8 = 0.125f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        switch (SwitchEnumFacing.field_177331_a[var3.ordinal()]) {
            case 1: {
                this.setBlockBounds(var6, var4, 1.0f - var8, var7, var5, 1.0f);
                break;
            }
            case 2: {
                this.setBlockBounds(var6, var4, 0.0f, var7, var5, var8);
                break;
            }
            case 3: {
                this.setBlockBounds(1.0f - var8, var4, var6, 1.0f, var5, var7);
                break;
            }
            case 4: {
                this.setBlockBounds(0.0f, var4, var6, var8, var5, var7);
            }
        }
    }

    @Override
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing var5 = (EnumFacing)((Object)state.getValue(field_176412_a));
        if (!worldIn.getBlockState(pos.offset(var5.getOpposite())).getBlock().getMaterial().isSolid()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing var2 = EnumFacing.getFront(meta);
        if (var2.getAxis() == EnumFacing.Axis.Y) {
            var2 = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(field_176412_a, (Comparable)((Object)var2));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)((Object)state.getValue(field_176412_a))).getIndex();
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, field_176412_a);
    }

    static final class SwitchEnumFacing {
        static final int[] field_177331_a = new int[EnumFacing.values().length];
        private static final String __OBFID = "CL_00002046";

        static {
            try {
                SwitchEnumFacing.field_177331_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177331_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177331_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumFacing.field_177331_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumFacing() {
        }
    }
}

