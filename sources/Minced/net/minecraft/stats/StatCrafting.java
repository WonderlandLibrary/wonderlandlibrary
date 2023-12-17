// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.stats;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.item.Item;

public class StatCrafting extends StatBase
{
    private final Item item;
    
    public StatCrafting(final String p_i45910_1_, final String p_i45910_2_, final ITextComponent statNameIn, final Item p_i45910_4_) {
        super(p_i45910_1_ + p_i45910_2_, statNameIn);
        this.item = p_i45910_4_;
    }
    
    public Item getItem() {
        return this.item;
    }
}
