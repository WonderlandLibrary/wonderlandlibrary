// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.item.Item;
import net.minecraft.potion.PotionType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.PotionHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerBrewingStand extends Container
{
    private final IInventory tileBrewingStand;
    private final Slot slot;
    private int prevBrewTime;
    private int prevFuel;
    
    public ContainerBrewingStand(final InventoryPlayer playerInventory, final IInventory tileBrewingStandIn) {
        this.tileBrewingStand = tileBrewingStandIn;
        this.addSlotToContainer(new Potion(tileBrewingStandIn, 0, 56, 51));
        this.addSlotToContainer(new Potion(tileBrewingStandIn, 1, 79, 58));
        this.addSlotToContainer(new Potion(tileBrewingStandIn, 2, 102, 51));
        this.slot = this.addSlotToContainer(new Ingredient(tileBrewingStandIn, 3, 79, 17));
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
    public void addListener(final IContainerListener listener) {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileBrewingStand);
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.listeners.size(); ++i) {
            final IContainerListener icontainerlistener = this.listeners.get(i);
            if (this.prevBrewTime != this.tileBrewingStand.getField(0)) {
                icontainerlistener.sendWindowProperty(this, 0, this.tileBrewingStand.getField(0));
            }
            if (this.prevFuel != this.tileBrewingStand.getField(1)) {
                icontainerlistener.sendWindowProperty(this, 1, this.tileBrewingStand.getField(1));
            }
        }
        this.prevBrewTime = this.tileBrewingStand.getField(0);
        this.prevFuel = this.tileBrewingStand.getField(1);
    }
    
    @Override
    public void updateProgressBar(final int id, final int data) {
        this.tileBrewingStand.setField(id, data);
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.tileBrewingStand.isUsableByPlayer(playerIn);
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if ((index < 0 || index > 2) && index != 3 && index != 4) {
                if (this.slot.isItemValid(itemstack2)) {
                    if (!this.mergeItemStack(itemstack2, 3, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (Potion.canHoldPotion(itemstack) && itemstack.getCount() == 1) {
                    if (!this.mergeItemStack(itemstack2, 0, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (Fuel.isValidBrewingFuel(itemstack)) {
                    if (!this.mergeItemStack(itemstack2, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 5 && index < 32) {
                    if (!this.mergeItemStack(itemstack2, 32, 41, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 32 && index < 41) {
                    if (!this.mergeItemStack(itemstack2, 5, 32, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (!this.mergeItemStack(itemstack2, 5, 41, false)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                if (!this.mergeItemStack(itemstack2, 5, 41, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(itemstack2, itemstack);
            }
            if (itemstack2.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }
            if (itemstack2.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack2);
        }
        return itemstack;
    }
    
    static class Fuel extends Slot
    {
        public Fuel(final IInventory iInventoryIn, final int index, final int xPosition, final int yPosition) {
            super(iInventoryIn, index, xPosition, yPosition);
        }
        
        @Override
        public boolean isItemValid(final ItemStack stack) {
            return isValidBrewingFuel(stack);
        }
        
        public static boolean isValidBrewingFuel(final ItemStack itemStackIn) {
            return itemStackIn.getItem() == Items.BLAZE_POWDER;
        }
        
        @Override
        public int getSlotStackLimit() {
            return 64;
        }
    }
    
    static class Ingredient extends Slot
    {
        public Ingredient(final IInventory iInventoryIn, final int index, final int xPosition, final int yPosition) {
            super(iInventoryIn, index, xPosition, yPosition);
        }
        
        @Override
        public boolean isItemValid(final ItemStack stack) {
            return PotionHelper.isReagent(stack);
        }
        
        @Override
        public int getSlotStackLimit() {
            return 64;
        }
    }
    
    static class Potion extends Slot
    {
        public Potion(final IInventory p_i47598_1_, final int p_i47598_2_, final int p_i47598_3_, final int p_i47598_4_) {
            super(p_i47598_1_, p_i47598_2_, p_i47598_3_, p_i47598_4_);
        }
        
        @Override
        public boolean isItemValid(final ItemStack stack) {
            return canHoldPotion(stack);
        }
        
        @Override
        public int getSlotStackLimit() {
            return 1;
        }
        
        @Override
        public ItemStack onTake(final EntityPlayer thePlayer, final ItemStack stack) {
            final PotionType potiontype = PotionUtils.getPotionFromItem(stack);
            if (thePlayer instanceof EntityPlayerMP) {
                CriteriaTriggers.BREWED_POTION.trigger((EntityPlayerMP)thePlayer, potiontype);
            }
            super.onTake(thePlayer, stack);
            return stack;
        }
        
        public static boolean canHoldPotion(final ItemStack stack) {
            final Item item = stack.getItem();
            return item == Items.POTIONITEM || item == Items.SPLASH_POTION || item == Items.LINGERING_POTION || item == Items.GLASS_BOTTLE;
        }
    }
}
