/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.inventory;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;

public class ContainerBrewingStand
extends Container {
    private final IInventory tileBrewingStand;
    private final Slot theSlot;
    private int prevBrewTime;
    private int prevFuel;

    public ContainerBrewingStand(InventoryPlayer playerInventory, IInventory tileBrewingStandIn) {
        this.tileBrewingStand = tileBrewingStandIn;
        this.addSlotToContainer(new Potion(tileBrewingStandIn, 0, 56, 51));
        this.addSlotToContainer(new Potion(tileBrewingStandIn, 1, 79, 58));
        this.addSlotToContainer(new Potion(tileBrewingStandIn, 2, 102, 51));
        this.theSlot = this.addSlotToContainer(new Ingredient(tileBrewingStandIn, 3, 79, 17));
        this.addSlotToContainer(new Fuel(tileBrewingStandIn, 4, 17, 17));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileBrewingStand);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.listeners.size(); ++i) {
            IContainerListener icontainerlistener = (IContainerListener)this.listeners.get(i);
            if (this.prevBrewTime != this.tileBrewingStand.getField(0)) {
                icontainerlistener.sendProgressBarUpdate(this, 0, this.tileBrewingStand.getField(0));
            }
            if (this.prevFuel == this.tileBrewingStand.getField(1)) continue;
            icontainerlistener.sendProgressBarUpdate(this, 1, this.tileBrewingStand.getField(1));
        }
        this.prevBrewTime = this.tileBrewingStand.getField(0);
        this.prevFuel = this.tileBrewingStand.getField(1);
    }

    @Override
    public void updateProgressBar(int id, int data) {
        this.tileBrewingStand.setField(id, data);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.tileBrewingStand.isUsableByPlayer(playerIn);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if ((index < 0 || index > 2) && index != 3 && index != 4) {
                if (this.theSlot.isItemValid(itemstack1) ? !this.mergeItemStack(itemstack1, 3, 4, false) : (Potion.canHoldPotion(itemstack) && itemstack.getCount() == 1 ? !this.mergeItemStack(itemstack1, 0, 3, false) : (Fuel.isValidBrewingFuel(itemstack) ? !this.mergeItemStack(itemstack1, 4, 5, false) : (index >= 5 && index < 32 ? !this.mergeItemStack(itemstack1, 32, 41, false) : (index >= 32 && index < 41 ? !this.mergeItemStack(itemstack1, 5, 32, false) : !this.mergeItemStack(itemstack1, 5, 41, false)))))) {
                    return ItemStack.field_190927_a;
                }
            } else {
                if (!this.mergeItemStack(itemstack1, 5, 41, true)) {
                    return ItemStack.field_190927_a;
                }
                slot.onSlotChange(itemstack1, itemstack);
            }
            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.field_190927_a);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.field_190927_a;
            }
            slot.func_190901_a(playerIn, itemstack1);
        }
        return itemstack;
    }

    static class Potion
    extends Slot {
        public Potion(IInventory p_i47598_1_, int p_i47598_2_, int p_i47598_3_, int p_i47598_4_) {
            super(p_i47598_1_, p_i47598_2_, p_i47598_3_, p_i47598_4_);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return Potion.canHoldPotion(stack);
        }

        @Override
        public int getSlotStackLimit() {
            return 1;
        }

        @Override
        public ItemStack func_190901_a(EntityPlayer p_190901_1_, ItemStack p_190901_2_) {
            PotionType potiontype = PotionUtils.getPotionFromItem(p_190901_2_);
            if (p_190901_1_ instanceof EntityPlayerMP) {
                CriteriaTriggers.field_192130_j.func_192173_a((EntityPlayerMP)p_190901_1_, potiontype);
            }
            super.func_190901_a(p_190901_1_, p_190901_2_);
            return p_190901_2_;
        }

        public static boolean canHoldPotion(ItemStack stack) {
            Item item = stack.getItem();
            return item == Items.POTIONITEM || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION || item == Items.GLASS_BOTTLE;
        }
    }

    static class Ingredient
    extends Slot {
        public Ingredient(IInventory iInventoryIn, int index, int xPosition, int yPosition) {
            super(iInventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return PotionHelper.isReagent(stack);
        }

        @Override
        public int getSlotStackLimit() {
            return 64;
        }
    }

    static class Fuel
    extends Slot {
        public Fuel(IInventory iInventoryIn, int index, int xPosition, int yPosition) {
            super(iInventoryIn, index, xPosition, yPosition);
        }

        @Override
        public boolean isItemValid(ItemStack stack) {
            return Fuel.isValidBrewingFuel(stack);
        }

        public static boolean isValidBrewingFuel(ItemStack itemStackIn) {
            return itemStackIn.getItem() == Items.BLAZE_POWDER;
        }

        @Override
        public int getSlotStackLimit() {
            return 64;
        }
    }
}

