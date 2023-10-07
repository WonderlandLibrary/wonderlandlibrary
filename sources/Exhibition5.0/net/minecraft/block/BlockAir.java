// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class BlockAir extends Block
{
    private static final String __OBFID = "CL_00000190";
    
    protected BlockAir() {
        super(Material.air);
    }
    
    @Override
    public int getRenderType() {
        return -1;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(final World worldIn, final BlockPos pos, final IBlockState state) {
        return null;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean canCollideCheck(final IBlockState state, final boolean p_176209_2_) {
        return false;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
    }
}
