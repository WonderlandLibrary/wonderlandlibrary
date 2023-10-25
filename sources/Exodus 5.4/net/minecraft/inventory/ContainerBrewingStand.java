/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;

public class ContainerBrewingStand
extends Container {
    private IInventory tileBrewingStand;
    private int brewTime;
    private final Slot theSlot;

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        int n = 0;
        while (n < this.crafters.size()) {
            ICrafting iCrafting = (ICrafting)this.crafters.get(n);
            if (this.brewTime != this.tileBrewingStand.getField(0)) {
                iCrafting.sendProgressBarUpdate(this, 0, this.tileBrewingStand.getField(0));
            }
            ++n;
        }
        this.brewTime = this.tileBrewingStand.getField(0);
    }

    @Override
    public void updateProgressBar(int n, int n2) {
        this.tileBrewingStand.setField(n, n2);
    }

    public ContainerBrewingStand(InventoryPlayer inventoryPlayer, IInventory iInventory) {
        this.tileBrewingStand = iInventory;
        this.addSlotToContainer(new Potion(inventoryPlayer.player, iInventory, 0, 56, 46));
        this.addSlotToContainer(new Potion(inventoryPlayer.player, iInventory, 1, 79, 53));
        this.addSlotToContainer(new Potion(inventoryPlayer.player, iInventory, 2, 102, 46));
        this.theSlot = this.addSlotToContainer(new Ingredient(iInventory, 3, 79, 17));
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
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.tileBrewingStand.isUseableByPlayer(entityPlayer);
    }

    @Override
    public void onCraftGuiOpened(ICrafting iCrafting) {
        super.onCraftGuiOpened(iCrafting);
        iCrafting.func_175173_a(this, this.tileBrewingStand);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(n);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if ((n < 0 || n > 2) && n != 3) {
                if (!this.theSlot.getHasStack() && this.theSlot.isItemValid(itemStack2) ? !this.mergeItemStack(itemStack2, 3, 4, false) : (Potion.canHoldPotion(itemStack) ? !this.mergeItemStack(itemStack2, 0, 3, false) : (n >= 4 && n < 31 ? !this.mergeItemStack(itemStack2, 31, 40, false) : (n >= 31 && n < 40 ? !this.mergeItemStack(itemStack2, 4, 31, false) : !this.mergeItemStack(itemStack2, 4, 40, false))))) {
                    return null;
                }
            } else {
                if (!this.mergeItemStack(itemStack2, 4, 40, true)) {
                    return null;
                }
                slot.onSlotChange(itemStack2, itemStack);
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

    class Ingredient
    extends Slot {
        @Override
        public int getSlotStackLimit() {
            return 64;
        }

        @Override
        public boolean isItemValid(ItemStack itemStack) {
            return itemStack != null ? itemStack.getItem().isPotionIngredient(itemStack) : false;
        }

        public Ingredient(IInventory iInventory, int n, int n2, int n3) {
            super(iInventory, n, n2, n3);
        }
    }

    static class Potion
    extends Slot {
        private EntityPlayer player;

        @Override
        public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {
            if (itemStack.getItem() == Items.potionitem && itemStack.getMetadata() > 0) {
                this.player.triggerAchievement(AchievementList.potion);
            }
            super.onPickupFromSlot(entityPlayer, itemStack);
        }

        @Override
        public boolean isItemValid(ItemStack itemStack) {
            return Potion.canHoldPotion(itemStack);
        }

        public Potion(EntityPlayer entityPlayer, IInventory iInventory, int n, int n2, int n3) {
            super(iInventory, n, n2, n3);
            this.player = entityPlayer;
        }

        public static boolean canHoldPotion(ItemStack itemStack) {
            return itemStack != null && (itemStack.getItem() == Items.potionitem || itemStack.getItem() == Items.glass_bottle);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }
}

