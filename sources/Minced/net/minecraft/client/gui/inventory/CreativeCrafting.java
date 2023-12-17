// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.inventory.Container;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.IContainerListener;

public class CreativeCrafting implements IContainerListener
{
    private final Minecraft mc;
    
    public CreativeCrafting(final Minecraft mc) {
        this.mc = mc;
    }
    
    @Override
    public void sendAllContents(final Container containerToSend, final NonNullList<ItemStack> itemsList) {
    }
    
    @Override
    public void sendSlotContents(final Container containerToSend, final int slotInd, final ItemStack stack) {
        this.mc.playerController.sendSlotPacket(stack, slotInd);
    }
    
    @Override
    public void sendWindowProperty(final Container containerIn, final int varToUpdate, final int newValue) {
    }
    
    @Override
    public void sendAllWindowProperties(final Container containerIn, final IInventory inventory) {
    }
}
