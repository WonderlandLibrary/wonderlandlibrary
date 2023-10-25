/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;

public class ContainerFurnace
extends Container {
    private int field_178155_i;
    private int field_178154_h;
    private final IInventory tileFurnace;
    private int field_178152_f;
    private int field_178153_g;

    public ContainerFurnace(InventoryPlayer inventoryPlayer, IInventory iInventory) {
        this.tileFurnace = iInventory;
        this.addSlotToContainer(new Slot(iInventory, 0, 56, 17));
        this.addSlotToContainer(new SlotFurnaceFuel(iInventory, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnaceOutput(inventoryPlayer.player, iInventory, 2, 116, 35));
        int n = 0;
        while (n < 3) {
            int n2 = 0;
            while (n2 < 9) {
                this.addSlotToContainer(new Slot(inventoryPlayer, n2 + n * 9 + 9, 8 + n2 * 18, 84 + n * 18));
                ++n2;
            }
            ++n;
        }
        n = 0;
        while (n < 9) {
            this.addSlotToContainer(new Slot(inventoryPlayer, n, 8 + n * 18, 142));
            ++n;
        }
    }

    @Override
    public void updateProgressBar(int n, int n2) {
        this.tileFurnace.setField(n, n2);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.tileFurnace.isUseableByPlayer(entityPlayer);
    }

    @Override
    public void onCraftGuiOpened(ICrafting iCrafting) {
        super.onCraftGuiOpened(iCrafting);
        iCrafting.func_175173_a(this, this.tileFurnace);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        int n = 0;
        while (n < this.crafters.size()) {
            ICrafting iCrafting = (ICrafting)this.crafters.get(n);
            if (this.field_178152_f != this.tileFurnace.getField(2)) {
                iCrafting.sendProgressBarUpdate(this, 2, this.tileFurnace.getField(2));
            }
            if (this.field_178154_h != this.tileFurnace.getField(0)) {
                iCrafting.sendProgressBarUpdate(this, 0, this.tileFurnace.getField(0));
            }
            if (this.field_178155_i != this.tileFurnace.getField(1)) {
                iCrafting.sendProgressBarUpdate(this, 1, this.tileFurnace.getField(1));
            }
            if (this.field_178153_g != this.tileFurnace.getField(3)) {
                iCrafting.sendProgressBarUpdate(this, 3, this.tileFurnace.getField(3));
            }
            ++n;
        }
        this.field_178152_f = this.tileFurnace.getField(2);
        this.field_178154_h = this.tileFurnace.getField(0);
        this.field_178155_i = this.tileFurnace.getField(1);
        this.field_178153_g = this.tileFurnace.getField(3);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(n);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (n == 2) {
                if (!this.mergeItemStack(itemStack2, 3, 39, true)) {
                    return null;
                }
                slot.onSlotChange(itemStack2, itemStack);
            } else if (n != 1 && n != 0 ? (FurnaceRecipes.instance().getSmeltingResult(itemStack2) != null ? !this.mergeItemStack(itemStack2, 0, 1, false) : (TileEntityFurnace.isItemFuel(itemStack2) ? !this.mergeItemStack(itemStack2, 1, 2, false) : (n >= 3 && n < 30 ? !this.mergeItemStack(itemStack2, 30, 39, false) : n >= 30 && n < 39 && !this.mergeItemStack(itemStack2, 3, 30, false)))) : !this.mergeItemStack(itemStack2, 3, 39, false)) {
                return null;
            }
            if (itemStack2.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if (itemStack2.stackSize == itemStack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(entityPlayer, itemStack2);
        }
        return itemStack;
    }
}

