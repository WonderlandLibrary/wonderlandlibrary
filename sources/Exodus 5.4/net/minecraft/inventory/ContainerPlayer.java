/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerPlayer
extends Container {
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
    private final EntityPlayer thePlayer;
    public IInventory craftResult = new InventoryCraftResult();
    public boolean isLocalWorld;

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(n);
        if (slot != null && slot.getHasStack()) {
            int n2;
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (n == 0) {
                if (!this.mergeItemStack(itemStack2, 9, 45, true)) {
                    return null;
                }
                slot.onSlotChange(itemStack2, itemStack);
            } else if (n >= 1 && n < 5 ? !this.mergeItemStack(itemStack2, 9, 45, false) : (n >= 5 && n < 9 ? !this.mergeItemStack(itemStack2, 9, 45, false) : (itemStack.getItem() instanceof ItemArmor && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)itemStack.getItem()).armorType)).getHasStack() ? !this.mergeItemStack(itemStack2, n2 = 5 + ((ItemArmor)itemStack.getItem()).armorType, n2 + 1, false) : (n >= 9 && n < 36 ? !this.mergeItemStack(itemStack2, 36, 45, false) : (n >= 36 && n < 45 ? !this.mergeItemStack(itemStack2, 9, 36, false) : !this.mergeItemStack(itemStack2, 9, 45, false)))))) {
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

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public boolean canMergeSlot(ItemStack itemStack, Slot slot) {
        return slot.inventory != this.craftResult && super.canMergeSlot(itemStack, slot);
    }

    public ContainerPlayer(InventoryPlayer inventoryPlayer, boolean bl, EntityPlayer entityPlayer) {
        int n;
        this.isLocalWorld = bl;
        this.thePlayer = entityPlayer;
        this.addSlotToContainer(new SlotCrafting(inventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 144, 36));
        int n2 = 0;
        while (n2 < 2) {
            n = 0;
            while (n < 2) {
                this.addSlotToContainer(new Slot(this.craftMatrix, n + n2 * 2, 88 + n * 18, 26 + n2 * 18));
                ++n;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 4) {
            n = n2;
            this.addSlotToContainer(new Slot(inventoryPlayer, inventoryPlayer.getSizeInventory() - 1 - n2, 8, 8 + n2 * 18){

                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public String getSlotTexture() {
                    return ItemArmor.EMPTY_SLOT_NAMES[n];
                }

                @Override
                public boolean isItemValid(ItemStack itemStack) {
                    return itemStack == null ? false : (itemStack.getItem() instanceof ItemArmor ? ((ItemArmor)itemStack.getItem()).armorType == n : (itemStack.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && itemStack.getItem() != Items.skull ? false : n == 0));
                }
            });
            ++n2;
        }
        n2 = 0;
        while (n2 < 3) {
            n = 0;
            while (n < 9) {
                this.addSlotToContainer(new Slot(inventoryPlayer, n + (n2 + 1) * 9, 8 + n * 18, 84 + n2 * 18));
                ++n;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 9) {
            this.addSlotToContainer(new Slot(inventoryPlayer, n2, 8 + n2 * 18, 142));
            ++n2;
        }
        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        int n = 0;
        while (n < 4) {
            ItemStack itemStack = this.craftMatrix.removeStackFromSlot(n);
            if (itemStack != null) {
                entityPlayer.dropPlayerItemWithRandomChoice(itemStack, false);
            }
            ++n;
        }
        this.craftResult.setInventorySlotContents(0, null);
    }

    @Override
    public void onCraftMatrixChanged(IInventory iInventory) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj));
    }
}

