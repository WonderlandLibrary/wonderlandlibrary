// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.util.EnumWorldBlockLayer;
import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockGlass extends BlockBreakable
{
    private static final String __OBFID = "CL_00000249";
    
    public BlockGlass(final Material p_i45408_1_, final boolean p_i45408_2_) {
        super(p_i45408_1_, p_i45408_2_);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    protected boolean canSilkHarvest() {
        return true;
    }
}
