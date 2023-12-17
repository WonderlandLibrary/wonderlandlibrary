// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import java.util.Random;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockPackedIce extends Block
{
    public BlockPackedIce() {
        super(Material.PACKED_ICE);
        this.slipperiness = 0.98f;
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
}
