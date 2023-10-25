/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerWorkbench
extends Container {
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
    private BlockPos pos;
    private World worldObj;
    public IInventory craftResult = new InventoryCraftResult();

    @Override
    public void onCraftMatrixChanged(IInventory iInventory) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(n);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (n == 0) {
                if (!this.mergeItemStack(itemStack2, 10, 46, true)) {
                    return null;
                }
                slot.onSlotChange(itemStack2, itemStack);
            } else if (n >= 10 && n < 37 ? !this.mergeItemStack(itemStack2, 37, 46, false) : (n >= 37 && n < 46 ? !this.mergeItemStack(itemStack2, 10, 37, false) : !this.mergeItemStack(itemStack2, 10, 46, false))) {
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

    public ContainerWorkbench(InventoryPlayer inventoryPlayer, World world, BlockPos blockPos) {
        int n;
        this.worldObj = world;
        this.pos = blockPos;
        this.addSlotToContainer(new SlotCrafting(inventoryPlayer.player, this.craftMatrix, this.craftResult, 0, 124, 35));
        int n2 = 0;
        while (n2 < 3) {
            n = 0;
            while (n < 3) {
                this.addSlotToContainer(new Slot(this.craftMatrix, n + n2 * 3, 30 + n * 18, 17 + n2 * 18));
                ++n;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 3) {
            n = 0;
            while (n < 9) {
                this.addSlotToContainer(new Slot(inventoryPlayer, n + n2 * 9 + 9, 8 + n * 18, 84 + n2 * 18));
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
    public boolean canMergeSlot(ItemStack itemStack, Slot slot) {
        return slot.inventory != this.craftResult && super.canMergeSlot(itemStack, slot);
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        if (!this.worldObj.isRemote) {
            int n = 0;
            while (n < 9) {
                ItemStack itemStack = this.craftMatrix.removeStackFromSlot(n);
                if (itemStack != null) {
                    entityPlayer.dropPlayerItemWithRandomChoice(itemStack, false);
                }
                ++n;
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.worldObj.getBlockState(this.pos).getBlock() != Blocks.crafting_table ? false : entityPlayer.getDistanceSq((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) <= 64.0;
    }
}

