// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import java.util.Random;
import net.minecraft.block.state.IBlockState;

public class BlockGravel extends BlockFalling
{
    private static final String __OBFID = "CL_00000252";
    
    @Override
    public Item getItemDropped(final IBlockState state, final Random rand, int fortune) {
        if (fortune > 3) {
            fortune = 3;
        }
        return (rand.nextInt(10 - fortune * 3) == 0) ? Items.flint : Item.getItemFromBlock(this);
    }
}
