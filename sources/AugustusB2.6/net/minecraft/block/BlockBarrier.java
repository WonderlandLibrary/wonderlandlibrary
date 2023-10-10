// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import net.augustus.utils.interfaces.MM;

public class BlockBarrier extends Block implements MM
{
    protected BlockBarrier() {
        super(Material.barrier);
        this.setBlockUnbreakable();
        this.setResistance(6000001.0f);
        this.disableStats();
        this.translucent = true;
    }
    
    @Override
    public int getRenderType() {
        if (BlockBarrier.mm.barriers.isToggled()) {
            return 3;
        }
        return -1;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public float getAmbientOcclusionLightValue() {
        return 1.0f;
    }
    
    @Override
    public void dropBlockAsItemWithChance(final World worldIn, final BlockPos pos, final IBlockState state, final float chance, final int fortune) {
    }
}
