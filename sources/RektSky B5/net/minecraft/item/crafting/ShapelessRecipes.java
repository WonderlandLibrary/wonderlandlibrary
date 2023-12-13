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
        for (int i2 = 0; i2 < aitemstack.length; ++i2) {
            ItemStack itemstack = inv.getStackInSlot(i2);
            if (itemstack == null || !itemstack.getItem().hasContainerItem()) continue;
            aitemstack[i2] = new ItemStack(itemstack.getItem().getContainerItem());
        }
        return aitemstack;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ArrayList<ItemStack> list = Lists.newArrayList(this.recipeItems);
        for (int i2 = 0; i2 < inv.getHeight(); ++i2) {
            for (int j2 = 0; j2 < inv.getWidth(); ++j2) {
                ItemStack itemstack = inv.getStackInRowAndColumn(j2, i2);
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

