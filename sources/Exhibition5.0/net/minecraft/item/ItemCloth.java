// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemCloth extends ItemBlock
{
    private static final String __OBFID = "CL_00000075";
    
    public ItemCloth(final Block p_i45358_1_) {
        super(p_i45358_1_);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return super.getUnlocalizedName() + "." + EnumDyeColor.func_176764_b(stack.getMetadata()).func_176762_d();
    }
}
