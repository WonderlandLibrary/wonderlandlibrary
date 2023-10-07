// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemColored extends ItemBlock
{
    private final Block field_150944_b;
    private String[] field_150945_c;
    private static final String __OBFID = "CL_00000003";
    
    public ItemColored(final Block p_i45332_1_, final boolean p_i45332_2_) {
        super(p_i45332_1_);
        this.field_150944_b = p_i45332_1_;
        if (p_i45332_2_) {
            this.setMaxDamage(0);
            this.setHasSubtypes(true);
        }
    }
    
    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        return this.field_150944_b.getRenderColor(this.field_150944_b.getStateFromMeta(stack.getMetadata()));
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
    
    public ItemColored func_150943_a(final String[] p_150943_1_) {
        this.field_150945_c = p_150943_1_;
        return this;
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        if (this.field_150945_c == null) {
            return super.getUnlocalizedName(stack);
        }
        final int var2 = stack.getMetadata();
        return (var2 >= 0 && var2 < this.field_150945_c.length) ? (super.getUnlocalizedName(stack) + "." + this.field_150945_c[var2]) : super.getUnlocalizedName(stack);
    }
}
