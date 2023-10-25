/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public interface IRecipe {
    public boolean matches(InventoryCrafting var1, World var2);

    public ItemStack getCraftingResult(InventoryCrafting var1);

    public boolean func_194133_a(int var1, int var2);

    public ItemStack getRecipeOutput();

    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting var1);

    default public NonNullList<Ingredient> func_192400_c() {
        return NonNullList.func_191196_a();
    }

    default public boolean func_192399_d() {
        return false;
    }

    default public String func_193358_e() {
        return "";
    }
}

