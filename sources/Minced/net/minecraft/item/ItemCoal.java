// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.util.NonNullList;
import net.minecraft.creativetab.CreativeTabs;

public class ItemCoal extends Item
{
    public ItemCoal() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }
    
    @Override
    public String getTranslationKey(final ItemStack stack) {
        return (stack.getMetadata() == 1) ? "item.charcoal" : "item.coal";
    }
    
    @Override
    public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 0));
            items.add(new ItemStack(this, 1, 1));
        }
    }
}
