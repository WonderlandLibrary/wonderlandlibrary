/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class RecipeBookCloning
implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        int n = 0;
        ItemStack itemStack = null;
        int n2 = 0;
        while (n2 < inventoryCrafting.getSizeInventory()) {
            ItemStack itemStack2 = inventoryCrafting.getStackInSlot(n2);
            if (itemStack2 != null) {
                if (itemStack2.getItem() == Items.written_book) {
                    if (itemStack != null) {
                        return false;
                    }
                    itemStack = itemStack2;
                } else {
                    if (itemStack2.getItem() != Items.writable_book) {
                        return false;
                    }
                    ++n;
                }
            }
            ++n2;
        }
        return itemStack != null && n > 0;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inventoryCrafting) {
        ItemStack[] itemStackArray = new ItemStack[inventoryCrafting.getSizeInventory()];
        int n = 0;
        while (n < itemStackArray.length) {
            ItemStack itemStack = inventoryCrafting.getStackInSlot(n);
            if (itemStack != null && itemStack.getItem() instanceof ItemEditableBook) {
                itemStackArray[n] = itemStack;
                break;
            }
            ++n;
        }
        return itemStackArray;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        int n = 0;
        ItemStack itemStack = null;
        int n2 = 0;
        while (n2 < inventoryCrafting.getSizeInventory()) {
            ItemStack itemStack2 = inventoryCrafting.getStackInSlot(n2);
            if (itemStack2 != null) {
                if (itemStack2.getItem() == Items.written_book) {
                    if (itemStack != null) {
                        return null;
                    }
                    itemStack = itemStack2;
                } else {
                    if (itemStack2.getItem() != Items.writable_book) {
                        return null;
                    }
                    ++n;
                }
            }
            ++n2;
        }
        if (itemStack != null && n >= 1 && ItemEditableBook.getGeneration(itemStack) < 2) {
            ItemStack itemStack3 = new ItemStack(Items.written_book, n);
            itemStack3.setTagCompound((NBTTagCompound)itemStack.getTagCompound().copy());
            itemStack3.getTagCompound().setInteger("generation", ItemEditableBook.getGeneration(itemStack) + 1);
            if (itemStack.hasDisplayName()) {
                itemStack3.setStackDisplayName(itemStack.getDisplayName());
            }
            return itemStack3;
        }
        return null;
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}

