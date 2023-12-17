// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;

public class ItemLeaves extends ItemBlock
{
    private final BlockLeaves leaves;
    
    public ItemLeaves(final BlockLeaves block) {
        super(block);
        this.leaves = block;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage | 0x4;
    }
    
    @Override
    public String getTranslationKey(final ItemStack stack) {
        return super.getTranslationKey() + "." + this.leaves.getWoodType(stack.getMetadata()).getTranslationKey();
    }
}
