// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import com.google.common.base.Predicate;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.properties.PropertyDirection;

public class BlockWallSign extends BlockSign
{
    public static final PropertyDirection field_176412_a;
    private static final String __OBFID = "CL_00002047";
    
    public BlockWallSign() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockWallSign.field_176412_a, EnumFacing.NORTH));
    }
    
    @Override
    public void setBlockBoundsBasedOnState(final IBlockAccess access, final BlockPos pos) {
        final EnumFacing var3 = (EnumFacing)access.getBlockState(pos).getValue(BlockWallSign.field_176412_a);
        final float var4 = 0.28125f;
        final float var5 = 0.78125f;
        final float var6 = 0.0f;
        final float var7 = 1.0f;
        final float var8 = 0.125f;
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
                break;
            }
        }
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        final EnumFacing var5 = (EnumFacing)state.getValue(BlockWallSign.field_176412_a);
        if (!worldIn.getBlockState(pos.offset(var5.getOpposite())).getBlock().getMaterial().isSolid()) {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing var2 = EnumFacing.getFront(meta);
        if (var2.getAxis() == EnumFacing.Axis.Y) {
            var2 = EnumFacing.NORTH;
        }
        return this.getDefaultState().withProperty(BlockWallSign.field_176412_a, var2);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumFacing)state.getValue(BlockWallSign.field_176412_a)).getIndex();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockWallSign.field_176412_a });
    }
    
    static {
        field_176412_a = PropertyDirection.create("facing", (Predicate)EnumFacing.Plane.HORIZONTAL);
    }
    
    static final class SwitchEnumFacing
    {
        static final int[] field_177331_a;
        private static final String __OBFID = "CL_00002046";
        
        static {
            field_177331_a = new int[EnumFacing.values().length];
            try {
                SwitchEnumFacing.field_177331_a[EnumFacing.NORTH.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumFacing.field_177331_a[EnumFacing.SOUTH.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumFacing.field_177331_a[EnumFacing.WEST.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumFacing.field_177331_a[EnumFacing.EAST.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
