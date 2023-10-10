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
    public IInventory craftResult = new InventoryCraftResult();
    public boolean isLocalWorld;
    private final EntityPlayer thePlayer;

    public ContainerPlayer(InventoryPlayer playerInventory, boolean localWorld, EntityPlayer player) {
        this.isLocalWorld = localWorld;
        this.thePlayer = player;
        this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 144, 36));
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 2, 88 + j * 18, 26 + i * 18));
            }
        }
        for (int k = 0; k < 4; ++k) {
            final int k_f = k;
            this.addSlotToContainer(new Slot(playerInventory, playerInventory.getSizeInventory() - 1 - k, 8, 8 + k * 18){

                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                @Override
                public boolean isItemValid(ItemStack stack) {
                    if (stack == null) {
                        return false;
                    }
                    if (stack.getItem() instanceof ItemArmor) {
                        if (((ItemArmor)stack.getItem()).armorType != k_f) return false;
                        return true;
                    }
                    if (stack.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && stack.getItem() != Items.skull) {
                        return false;
                    }
                    if (k_f != 0) return false;
                    return true;
                }

                @Override
                public String getSlotTexture() {
                    return ItemArmor.EMPTY_SLOT_NAMES[k_f];
                }
            });
        }
        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlotToContainer(new Slot(playerInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }
        int i1 = 0;
        while (true) {
            if (i1 >= 9) {
                this.onCraftMatrixChanged(this.craftMatrix);
                return;
            }
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 142));
            ++i1;
        }
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj));
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        int i = 0;
        while (true) {
            if (i >= 4) {
                this.craftResult.setInventorySlotContents(0, null);
                return;
            }
            ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
            if (itemstack != null) {
                playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
            }
            ++i;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        int i;
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot == null) return itemstack;
        if (!slot.getHasStack()) return itemstack;
        ItemStack itemstack1 = slot.getStack();
        itemstack = itemstack1.copy();
        if (index == 0) {
            if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
                return null;
            }
            slot.onSlotChange(itemstack1, itemstack);
        } else if (index >= 1 && index < 5 ? !this.mergeItemStack(itemstack1, 9, 45, false) : (index >= 5 && index < 9 ? !this.mergeItemStack(itemstack1, 9, 45, false) : (itemstack.getItem() instanceof ItemArmor && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)itemstack.getItem()).armorType)).getHasStack() ? !this.mergeItemStack(itemstack1, i = 5 + ((ItemArmor)itemstack.getItem()).armorType, i + 1, false) : (index >= 9 && index < 36 ? !this.mergeItemStack(itemstack1, 36, 45, false) : (index >= 36 && index < 45 ? !this.mergeItemStack(itemstack1, 9, 36, false) : !this.mergeItemStack(itemstack1, 9, 45, false)))))) {
            return null;
        }
        if (itemstack1.stackSize == 0) {
            slot.putStack(null);
        } else {
            slot.onSlotChanged();
        }
        if (itemstack1.stackSize == itemstack.stackSize) {
            return null;
        }
        slot.onPickupFromSlot(playerIn, itemstack1);
        return itemstack;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot p_94530_2_) {
        if (p_94530_2_.inventory == this.craftResult) return false;
        if (!super.canMergeSlot(stack, p_94530_2_)) return false;
        return true;
    }
}

