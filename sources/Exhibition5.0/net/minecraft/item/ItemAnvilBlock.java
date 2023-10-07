// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemAnvilBlock extends ItemMultiTexture
{
    private static final String __OBFID = "CL_00001764";
    
    public ItemAnvilBlock(final Block p_i1826_1_) {
        super(p_i1826_1_, p_i1826_1_, new String[] { "intact", "slightlyDamaged", "veryDamaged" });
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage << 2;
    }
}
