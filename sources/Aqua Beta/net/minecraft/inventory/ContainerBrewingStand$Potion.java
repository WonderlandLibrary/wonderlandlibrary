package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;

static class ContainerBrewingStand.Potion
extends Slot {
    private EntityPlayer player;

    public ContainerBrewingStand.Potion(EntityPlayer playerIn, IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.player = playerIn;
    }

    public boolean isItemValid(ItemStack stack) {
        return ContainerBrewingStand.Potion.canHoldPotion(stack);
    }

    public int getSlotStackLimit() {
        return 1;
    }

    public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
        if (stack.getItem() == Items.potionitem && stack.getMetadata() > 0) {
            this.player.triggerAchievement((StatBase)AchievementList.potion);
        }
        super.onPickupFromSlot(playerIn, stack);
    }

    public static boolean canHoldPotion(ItemStack stack) {
        return stack != null && (stack.getItem() == Items.potionitem || stack.getItem() == Items.glass_bottle);
    }
}
