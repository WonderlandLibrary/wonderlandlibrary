package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IRecipe {
   ItemStack getRecipeOutput();

   int getRecipeSize();

   ItemStack[] getRemainingItems(InventoryCrafting var1);

   boolean matches(InventoryCrafting var1, World var2);

   ItemStack getCraftingResult(InventoryCrafting var1);
}
