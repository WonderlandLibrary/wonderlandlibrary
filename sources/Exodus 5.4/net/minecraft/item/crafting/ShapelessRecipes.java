/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.item.crafting;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ShapelessRecipes
implements IRecipe {
    private final ItemStack recipeOutput;
    private final List<ItemStack> recipeItems;

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

    @Override
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }

    @Override
    public int getRecipeSize() {
        return this.recipeItems.size();
    }

    public ShapelessRecipes(ItemStack itemStack, List<ItemStack> list) {
        this.recipeOutput = itemStack;
        this.recipeItems = list;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting) {
        return this.recipeOutput.copy();
    }

    @Override
    public boolean matches(InventoryCrafting inventoryCrafting, World world) {
        ArrayList arrayList = Lists.newArrayList(this.recipeItems);
        int n = 0;
        while (n < inventoryCrafting.getHeight()) {
            int n2 = 0;
            while (n2 < inventoryCrafting.getWidth()) {
                ItemStack itemStack = inventoryCrafting.getStackInRowAndColumn(n2, n);
                if (itemStack != null) {
                    boolean bl = false;
                    for (ItemStack itemStack2 : arrayList) {
                        if (itemStack.getItem() != itemStack2.getItem() || itemStack2.getMetadata() != Short.MAX_VALUE && itemStack.getMetadata() != itemStack2.getMetadata()) continue;
                        bl = true;
                        arrayList.remove(itemStack2);
                        break;
                    }
                    if (!bl) {
                        return false;
                    }
                }
                ++n2;
            }
            ++n;
        }
        return arrayList.isEmpty();
    }
}

