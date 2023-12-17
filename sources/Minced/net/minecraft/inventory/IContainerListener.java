// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface IContainerListener
{
    void sendAllContents(final Container p0, final NonNullList<ItemStack> p1);
    
    void sendSlotContents(final Container p0, final int p1, final ItemStack p2);
    
    void sendWindowProperty(final Container p0, final int p1, final int p2);
    
    void sendAllWindowProperties(final Container p0, final IInventory p1);
}
