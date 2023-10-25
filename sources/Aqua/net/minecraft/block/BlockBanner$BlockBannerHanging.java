package net.minecraft.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public static class BlockBanner.BlockBannerHanging
extends BlockBanner {
    public BlockBanner.BlockBannerHanging() {
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty)FACING, (Comparable)EnumFacing.NORTH));
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue((IProperty)FACING);
        float f = 0.0f;
        float f1 = 0.78125f;
        float f2 = 0.0f;
        float f3 = 1.0f;
        float f4 = 0.125f;
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
        switch (BlockBanner.1.$SwitchMap$net$minecraft$util$EnumFacing[enumfacing.ordinal()]) {
            default: {
                this.setBlockBounds(f2, f, 1.0f - f4, f3, f1, 1.0f);
                break;
            }
            case 2: {
                this.setBlockBounds(f2, f, 0.0f, f3, f1, f4);
                break;
            }
            case 3: {
                this.setBlockBounds(1.0f - f4, f, f2, 1.0f, f1, f3);
                break;
            }
            case 4: {
                this.setBlockBounds(0.0f, f, f2, f4, f1, f3);
            }
        }
    }

    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
        if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().getMaterial().isSolid()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront((int)meta);
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty((IProperty)FACING, (Comparable)enumfacing);
    }

    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)state.getValue((IProperty)FACING)).getIndex();
    }

    protected BlockState createBlockState() {
        return new BlockState((Block)this, new IProperty[]{FACING});
    }
}
