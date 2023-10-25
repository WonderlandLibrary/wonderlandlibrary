/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ShapedRecipes
implements IRecipe {
    private final int recipeWidth;
    private final ItemStack recipeOutput;
    private final ItemStack[] recipeItems;
    private boolean copyIngredientNBT;
    private final int recipeHeight;

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        ItemStack itemStack = this.getRecipeOutput().copy();
        if (this.copyIngredientNBT) {
            int n = 0;
            while (n < inventoryCrafting.getSizeInventory()) {
                ItemStack itemStack2 = inventoryCrafting.getStackInSlot(n);
                if (itemStack2 != null && itemStack2.hasTagCompound()) {
                    itemStack.setTagCompound((NBTTagCompound)itemStack2.getTagCompound().copy());
                }
                ++n;
            }
        }
        return itemStack;
    }

    private boolean checkMatch(InventoryCrafting inventoryCrafting, int n, int n2, boolean bl) {
        int n3 = 0;
        while (n3 < 3) {
            int n4 = 0;
            while (n4 < 3) {
                ItemStack itemStack;
                int n5 = n3 - n;
                int n6 = n4 - n2;
                ItemStack itemStack2 = null;
                if (n5 >= 0 && n6 >= 0 && n5 < this.recipeWidth && n6 < this.recipeHeight) {
                    itemStack2 = bl ? this.recipeItems[this.recipeWidth - n5 - 1 + n6 * this.recipeWidth] : this.recipeItems[n5 + n6 * this.recipeWidth];
                }
                if ((itemStack = inventoryCrafting.getStackInRowAndColumn(n3, n4)) != null || itemStack2 != null) {
                    if (itemStack == null && itemStack2 != null || itemStack != null && itemStack2 == null) {
                        return false;
                    }
                    if (itemStack2.getItem() != itemStack.getItem()) {
                        return false;
                    }
                    if (itemStack2.getMetadata() != Short.MAX_VALUE && itemStack2.getMetadata() != itemStack.getMetadata()) {
                        return false;
                    }
                }
                ++n4;
            }
            ++n3;
        }
        return true;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inventoryCrafting) {
        ItemStack[] itemStackArray = new ItemStack[inventoryCrafting.getSizeInventory()];
        int n = 0;
        while (n < itemStackArray.length) {
            ItemStack itemStack = inventoryCrafting.getStackInSlot(n);
            if (itemStack != null && itemStack.getItem().hasContainerItem()) {
                itemStackArray[n] = new ItemStack(itemStack.getItem().getContainerItem());
            }
            ++n;
        }
        return itemStackArray;
    }

    public ShapedRecipes(int n, int n2, ItemStack[] itemStackArray, ItemStack itemStack) {
        this.recipeWidth = n;
        this.recipeHeight = n2;
        this.recipeItems = itemStackArray;
        this.recipeOutput = itemStack;
    }

    @Override
    public int getRecipeSize() {
        return this.recipeWidth * this.recipeHeight;
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        int n = 0;
        while (n <= 3 - this.recipeWidth) {
            int n2 = 0;
            while (n2 <= 3 - this.recipeHeight) {
                if (this.checkMatch(inventoryCrafting, n, n2, true)) {
                    return true;
                }
                if (this.checkMatch(inventoryCrafting, n, n2, false)) {
                    return true;
                }
                ++n2;
            }
            ++n;
        }
        return false;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }
}

