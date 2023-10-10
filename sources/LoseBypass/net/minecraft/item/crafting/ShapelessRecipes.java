/*
 * Decompiled with CFR 0.152.
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

    public ShapelessRecipes(ItemStack output, List<ItemStack> inputList) {
        this.recipeOutput = output;
        this.recipeItems = inputList;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
        int i = 0;
        while (i < aitemstack.length) {
            ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack != null && itemstack.getItem().hasContainerItem()) {
                aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
            }
            ++i;
        }
        return aitemstack;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ArrayList<ItemStack> list = Lists.newArrayList(this.recipeItems);
        int i = 0;
        while (i < inv.getHeight()) {
            for (int j = 0; j < inv.getWidth(); ++j) {
                ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
                if (itemstack == null) continue;
                boolean flag = false;
                for (ItemStack itemstack1 : list) {
                    if (itemstack.getItem() != itemstack1.getItem() || itemstack1.getMetadata() != Short.MAX_VALUE && itemstack.getMetadata() != itemstack1.getMetadata()) continue;
                    flag = true;
                    list.remove(itemstack1);
                    break;
                }
                if (flag) continue;
                return false;
            }
            ++i;
        }
        return list.isEmpty();
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return this.recipeOutput.copy();
    }

    @Override
    public int getRecipeSize() {
        return this.recipeItems.size();
    }
}

