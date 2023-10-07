// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MapColor;

public class BlockCompressed extends Block
{
    private final MapColor mapColor;
    private static final String __OBFID = "CL_00000268";
    
    public BlockCompressed(final MapColor p_i45414_1_) {
        super(Material.iron);
        this.mapColor = p_i45414_1_;
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public MapColor getMapColor(final IBlockState state) {
        return this.mapColor;
    }
}
