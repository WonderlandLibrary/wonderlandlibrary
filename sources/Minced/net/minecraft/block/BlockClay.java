// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockClay extends Block
{
    public BlockClay() {
        super(Material.CLAY);
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.CLAY_BALL;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 4;
    }
}
