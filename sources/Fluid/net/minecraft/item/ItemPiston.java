// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemPiston extends ItemBlock
{
    public ItemPiston(final Block block) {
        super(block);
    }
    
    @Override
    public int getMetadata(final int damage) {
        return 7;
    }
}
