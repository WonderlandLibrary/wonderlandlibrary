// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import java.util.Random;
import net.minecraft.block.material.Material;

public class BlockEmptyDrops extends Block
{
    public BlockEmptyDrops(final Material materialIn) {
        super(materialIn);
    }
    
    @Override
    public int quantityDropped(final Random random) {
        return 0;
    }
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
        return Items.AIR;
    }
}
