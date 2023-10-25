/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.stats.AchievementList;

public class SlotCrafting
extends Slot {
    private final EntityPlayer thePlayer;
    private int amountCrafted;
    private final InventoryCrafting craftMatrix;

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return false;
    }

    public SlotCrafting(EntityPlayer entityPlayer, InventoryCrafting inventoryCrafting, IInventory iInventory, int n, int n2, int n3) {
        super(iInventory, n, n2, n3);
        this.thePlayer = entityPlayer;
        this.craftMatrix = inventoryCrafting;
    }

    @Override
    public ItemStack decrStackSize(int n) {
        if (this.getHasStack()) {
            this.amountCrafted += Math.min(n, this.getStack().stackSize);
        }
        return super.decrStackSize(n);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer entityPlayer, ItemStack itemStack) {
        this.onCrafting(itemStack);
        ItemStack[] itemStackArray = CraftingManager.getInstance().func_180303_b(this.craftMatrix, entityPlayer.worldObj);
        int n = 0;
        while (n < itemStackArray.length) {
            ItemStack itemStack2 = this.craftMatrix.getStackInSlot(n);
            ItemStack itemStack3 = itemStackArray[n];
            if (itemStack2 != null) {
                this.craftMatrix.decrStackSize(n, 1);
            }
            if (itemStack3 != null) {
                if (this.craftMatrix.getStackInSlot(n) == null) {
                    this.craftMatrix.setInventorySlotContents(n, itemStack3);
                } else if (!this.thePlayer.inventory.addItemStackToInventory(itemStack3)) {
                    this.thePlayer.dropPlayerItemWithRandomChoice(itemStack3, false);
                }
            }
            ++n;
        }
    }

    @Override
    protected void onCrafting(ItemStack itemStack) {
        if (this.amountCrafted > 0) {
            itemStack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
        }
        this.amountCrafted = 0;
        if (itemStack.getItem() == Item.getItemFromBlock(Blocks.crafting_table)) {
            this.thePlayer.triggerAchievement(AchievementList.buildWorkBench);
        }
        if (itemStack.getItem() instanceof ItemPickaxe) {
            this.thePlayer.triggerAchievement(AchievementList.buildPickaxe);
        }
        if (itemStack.getItem() == Item.getItemFromBlock(Blocks.furnace)) {
            this.thePlayer.triggerAchievement(AchievementList.buildFurnace);
        }
        if (itemStack.getItem() instanceof ItemHoe) {
            this.thePlayer.triggerAchievement(AchievementList.buildHoe);
        }
        if (itemStack.getItem() == Items.bread) {
            this.thePlayer.triggerAchievement(AchievementList.makeBread);
        }
        if (itemStack.getItem() == Items.cake) {
            this.thePlayer.triggerAchievement(AchievementList.bakeCake);
        }
        if (itemStack.getItem() instanceof ItemPickaxe && ((ItemPickaxe)itemStack.getItem()).getToolMaterial() != Item.ToolMaterial.WOOD) {
            this.thePlayer.triggerAchievement(AchievementList.buildBetterPickaxe);
        }
        if (itemStack.getItem() instanceof ItemSword) {
            this.thePlayer.triggerAchievement(AchievementList.buildSword);
        }
        if (itemStack.getItem() == Item.getItemFromBlock(Blocks.enchanting_table)) {
            this.thePlayer.triggerAchievement(AchievementList.enchantments);
        }
        if (itemStack.getItem() == Item.getItemFromBlock(Blocks.bookshelf)) {
            this.thePlayer.triggerAchievement(AchievementList.bookcase);
        }
        if (itemStack.getItem() == Items.golden_apple && itemStack.getMetadata() == 1) {
            this.thePlayer.triggerAchievement(AchievementList.overpowered);
        }
    }

    @Override
    protected void onCrafting(ItemStack itemStack, int n) {
        this.amountCrafted += n;
        this.onCrafting(itemStack);
    }
}

