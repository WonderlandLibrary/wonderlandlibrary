// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.inventory.Container;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ICrafting;

public class CreativeCrafting implements ICrafting
{
    private final Minecraft mc;
    private static final String __OBFID = "CL_00000751";
    
    public CreativeCrafting(final Minecraft mc) {
        this.mc = mc;
    }
    
    @Override
    public void updateCraftingInventory(final Container p_71110_1_, final List p_71110_2_) {
    }
    
    @Override
    public void sendSlotContents(final Container p_71111_1_, final int p_71111_2_, final ItemStack p_71111_3_) {
        this.mc.playerController.sendSlotPacket(p_71111_3_, p_71111_2_);
    }
    
    @Override
    public void sendProgressBarUpdate(final Container p_71112_1_, final int p_71112_2_, final int p_71112_3_) {
    }
    
    @Override
    public void func_175173_a(final Container p_175173_1_, final IInventory p_175173_2_) {
    }
}
