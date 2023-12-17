// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;

public class BlockBarrier extends Block
{
    protected BlockBarrier() {
        super(Material.BARRIER);
        this.setBlockUnbreakable();
        this.setResistance(6000001.0f);
        this.disableStats();
        this.translucent = true;
    }
    
    @Override
    @Deprecated
    public EnumBlockRenderType getRenderType(final IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    @Override
    @Deprecated
    public boolean isOpaqueCube(final IBlockState state) {
        return false;
    }
    
    @Override
    @Deprecated
    public float getAmbientOcclusionLightValue(final IBlockState state) {
        return 1.0f;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
    }
}
