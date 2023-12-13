// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;

public class BlockRail extends BlockRailBase
{
    public static final PropertyEnum field_176565_b;
    private static final String __OBFID = "CL_00000293";
    
    protected BlockRail() {
        super(false);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockRail.field_176565_b, EnumRailDirection.NORTH_SOUTH));
    }
    
    @Override
    protected void func_176561_b(final World worldIn, final BlockPos p_176561_2_, final IBlockState p_176561_3_, final Block p_176561_4_) {
        if (p_176561_4_.canProvidePower() && new Rail(this, worldIn, p_176561_2_, p_176561_3_).countAdjacentRails() == 3) {
            this.func_176564_a(worldIn, p_176561_2_, p_176561_3_, false);
        }
    }
    
    @Override
    public IProperty func_176560_l() {
        return BlockRail.field_176565_b;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty(BlockRail.field_176565_b, EnumRailDirection.func_177016_a(meta));
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((EnumRailDirection)state.getValue(BlockRail.field_176565_b)).func_177015_a();
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockRail.field_176565_b });
    }
    
    static {
        field_176565_b = PropertyEnum.create("shape", EnumRailDirection.class);
    }
}
