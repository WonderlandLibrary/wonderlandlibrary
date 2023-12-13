// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.MapColor;

public class BlockCompressedPowered extends BlockCompressed
{
    private static final String __OBFID = "CL_00000287";
    
    public BlockCompressedPowered(final MapColor p_i45416_1_) {
        super(p_i45416_1_);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }
    
    @Override
    public boolean canProvidePower() {
        return true;
    }
    
    @Override
    public int isProvidingWeakPower(final IBlockAccess worldIn, final BlockPos pos, final IBlockState state, final EnumFacing side) {
        return 15;
    }
}
