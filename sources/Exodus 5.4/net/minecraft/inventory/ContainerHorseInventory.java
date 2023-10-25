/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerHorseInventory
extends Container {
    private IInventory horseInventory;
    private EntityHorse theHorse;

    public ContainerHorseInventory(IInventory iInventory, IInventory iInventory2, final EntityHorse entityHorse, EntityPlayer entityPlayer) {
        int n;
        int n2;
        this.horseInventory = iInventory2;
        this.theHorse = entityHorse;
        int n3 = 3;
        iInventory2.openInventory(entityPlayer);
        int n4 = (n3 - 4) * 18;
        this.addSlotToContainer(new Slot(iInventory2, 0, 8, 18){

            @Override
            public boolean isItemValid(ItemStack itemStack) {
                return super.isItemValid(itemStack) && itemStack.getItem() == Items.saddle && !this.getHasStack();
            }
        });
        this.addSlotToContainer(new Slot(iInventory2, 1, 8, 36){

            @Override
            public boolean canBeHovered() {
                return entityHorse.canWearArmor();
            }

            @Override
            public boolean isItemValid(ItemStack itemStack) {
                return super.isItemValid(itemStack) && entityHorse.canWearArmor() && EntityHorse.isArmorItem(itemStack.getItem());
            }
        });
        if (entityHorse.isChested()) {
            n2 = 0;
            while (n2 < n3) {
                n = 0;
                while (n < 5) {
                    this.addSlotToContainer(new Slot(iInventory2, 2 + n + n2 * 5, 80 + n * 18, 18 + n2 * 18));
                    ++n;
                }
                ++n2;
            }
        }
        n2 = 0;
        while (n2 < 3) {
            n = 0;
            while (n < 9) {
                this.addSlotToContainer(new Slot(iInventory, n + n2 * 9 + 9, 8 + n * 18, 102 + n2 * 18 + n4));
                ++n;
            }
            ++n2;
        }
        n2 = 0;
        while (n2 < 9) {
            this.addSlotToContainer(new Slot(iInventory, n2, 8 + n2 * 18, 160 + n4));
            ++n2;
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int n) {
        ItemStack itemStack = null;
        Slot slot = (Slot)this.inventorySlots.get(n);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (n < this.horseInventory.getSizeInventory() ? !this.mergeItemStack(itemStack2, this.horseInventory.getSizeInventory(), this.inventorySlots.size(), true) : (this.getSlot(1).isItemValid(itemStack2) && !this.getSlot(1).getHasStack() ? !this.mergeItemStack(itemStack2, 1, 2, false) : (this.getSlot(0).isItemValid(itemStack2) ? !this.mergeItemStack(itemStack2, 0, 1, false) : this.horseInventory.getSizeInventory() <= 2 || !this.mergeItemStack(itemStack2, 2, this.horseInventory.getSizeInventory(), false)))) {
                return null;
            }
            if (itemStack2.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemStack;
    }

    @Override
    public void onContainerClosed(EntityPlayer entityPlayer) {
        super.onContainerClosed(entityPlayer);
        this.horseInventory.closeInventory(entityPlayer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return this.horseInventory.isUseableByPlayer(entityPlayer) && this.theHorse.isEntityAlive() && this.theHorse.getDistanceToEntity(entityPlayer) < 8.0f;
    }
}

