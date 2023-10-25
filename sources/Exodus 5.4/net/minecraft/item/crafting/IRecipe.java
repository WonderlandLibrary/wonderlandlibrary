/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IRecipe {
    public int getRecipeSize();

    public boolean matches(InventoryCrafting var1, World var2);

    public ItemStack getRecipeOutput();

    public ItemStack getCraftingResult(InventoryCrafting var1);

    public ItemStack[] getRemainingItems(InventoryCrafting var1);
}

