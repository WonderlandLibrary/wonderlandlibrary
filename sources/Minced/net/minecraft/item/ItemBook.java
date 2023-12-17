// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

public class ItemBook extends Item
{
    @Override
    public boolean isEnchantable(final ItemStack stack) {
        return stack.getCount() == 1;
    }
    
    @Override
    public int getItemEnchantability() {
        return 1;
    }
}
