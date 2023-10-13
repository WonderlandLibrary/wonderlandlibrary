/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IRecipe {
    public boolean matches(InventoryCrafting var1, World var2);

    public ItemStack getCraftingResult(InventoryCrafting var1);

    public int getRecipeSize();

    public ItemStack getRecipeOutput();

    public ItemStack[] func_179532_b(InventoryCrafting var1);
}

