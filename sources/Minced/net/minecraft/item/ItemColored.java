// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemColored extends ItemBlock
{
    private String[] subtypeNames;
    
    public ItemColored(final Block block, final boolean hasSubtypes) {
        super(block);
        if (hasSubtypes) {
            this.setMaxDamage(0);
            this.setHasSubtypes(true);
        }
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
    
    public ItemColored setSubtypeNames(final String[] names) {
        this.subtypeNames = names;
        return this;
    }
    
    @Override
    public String getTranslationKey(final ItemStack stack) {
        if (this.subtypeNames == null) {
            return super.getTranslationKey(stack);
        }
        final int i = stack.getMetadata();
        return (i >= 0 && i < this.subtypeNames.length) ? (super.getTranslationKey(stack) + "." + this.subtypeNames[i]) : super.getTranslationKey(stack);
    }
}
