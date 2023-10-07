// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockMelon extends Block
{
    private static final String __OBFID = "CL_00000267";
    
    protected BlockMelon() {
        super(Material.gourd);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.melon;
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 3 + random.nextInt(5);
    }
    
    @Override
    public int quantityDroppedWithBonus(final int fortune, final Random random) {
        return Math.min(9, this.quantityDropped(random) + random.nextInt(1 + fortune));
    }
}
