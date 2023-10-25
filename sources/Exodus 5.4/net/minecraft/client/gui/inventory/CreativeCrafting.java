/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.gui.inventory;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class CreativeCrafting
implements ICrafting {
    private final Minecraft mc;

    @Override
    public void sendSlotContents(Container container, int n, ItemStack itemStack) {
        Minecraft.playerController.sendSlotPacket(itemStack, n);
    }

    @Override
    public void sendProgressBarUpdate(Container container, int n, int n2) {
    }

    public CreativeCrafting(Minecraft minecraft) {
        this.mc = minecraft;
    }

    @Override
    public void updateCraftingInventory(Container container, List<ItemStack> list) {
    }

    @Override
    public void func_175173_a(Container container, IInventory iInventory) {
    }
}

