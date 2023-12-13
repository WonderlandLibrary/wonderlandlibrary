// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;

public class BlockLeavesBase extends Block
{
    protected boolean field_150121_P;
    private static final String __OBFID = "CL_00000326";
    
    protected BlockLeavesBase(final Material p_i45433_1_, final boolean p_i45433_2_) {
        super(p_i45433_1_);
        this.field_150121_P = p_i45433_2_;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return (this.field_150121_P || worldIn.getBlockState(pos).getBlock() != this) && super.shouldSideBeRendered(worldIn, pos, side);
    }
}
