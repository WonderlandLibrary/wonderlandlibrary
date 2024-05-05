package net.minecraft.client.gui.inventory;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/*
 * Exception performing whole class analysis ignored.
 */
static class GuiContainerCreative.ContainerCreative
extends Container {
    public List<ItemStack> itemList = Lists.newArrayList();

    public GuiContainerCreative.ContainerCreative(EntityPlayer p_i1086_1_) {
        InventoryPlayer inventoryplayer = p_i1086_1_.inventory;
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot((IInventory)GuiContainerCreative.access$100(), i * 9 + j, 9 + j * 18, 18 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot((IInventory)inventoryplayer, k, 9 + k * 18, 112));
        }
        this.scrollTo(0.0f);
    }

    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    public void scrollTo(float p_148329_1_) {
        int i = (this.itemList.size() + 9 - 1) / 9 - 5;
        int j = (int)((double)(p_148329_1_ * (float)i) + 0.5);
        if (j < 0) {
            j = 0;
        }
        for (int k = 0; k < 5; ++k) {
            for (int l = 0; l < 9; ++l) {
                int i1 = l + (k + j) * 9;
                if (i1 >= 0 && i1 < this.itemList.size()) {
                    GuiContainerCreative.access$100().setInventorySlotContents(l + k * 9, (ItemStack)this.itemList.get(i1));
                    continue;
                }
                GuiContainerCreative.access$100().setInventorySlotContents(l + k * 9, (ItemStack)null);
            }
        }
    }

    public boolean func_148328_e() {
        return this.itemList.size() > 45;
    }

    protected void retrySlotClick(int slotId, int clickedButton, boolean mode, EntityPlayer playerIn) {
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        Slot slot;
        if (index >= this.inventorySlots.size() - 9 && index < this.inventorySlots.size() && (slot = (Slot)this.inventorySlots.get(index)) != null && slot.getHasStack()) {
            slot.putStack((ItemStack)null);
        }
        return null;
    }

    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.yDisplayPosition > 90;
    }

    public boolean canDragIntoSlot(Slot p_94531_1_) {
        return p_94531_1_.inventory instanceof InventoryPlayer || p_94531_1_.yDisplayPosition > 90 && p_94531_1_.xDisplayPosition <= 162;
    }
}
