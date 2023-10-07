// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemPiston extends ItemBlock
{
    private static final String __OBFID = "CL_00000054";
    
    public ItemPiston(final Block p_i45348_1_) {
        super(p_i45348_1_);
    }
    
    @Override
    public int getMetadata(final int damage) {
        return 7;
    }
}
