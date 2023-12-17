// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemMultiTexture extends ItemBlock
{
    protected final Block unused;
    protected final Mapper nameFunction;
    
    public ItemMultiTexture(final Block p_i47262_1_, final Block p_i47262_2_, final Mapper p_i47262_3_) {
        super(p_i47262_1_);
        this.unused = p_i47262_2_;
        this.nameFunction = p_i47262_3_;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    public ItemMultiTexture(final Block block, final Block block2, final String[] namesByMeta) {
        this(block, block2, new Mapper() {
            @Override
            public String apply(final ItemStack p_apply_1_) {
                int i = p_apply_1_.getMetadata();
                if (i < 0 || i >= namesByMeta.length) {
                    i = 0;
                }
                return namesByMeta[i];
            }
        });
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
    
    @Override
    public String getTranslationKey(final ItemStack stack) {
        return super.getTranslationKey() + "." + this.nameFunction.apply(stack);
    }
    
    public interface Mapper
    {
        String apply(final ItemStack p0);
    }
}
