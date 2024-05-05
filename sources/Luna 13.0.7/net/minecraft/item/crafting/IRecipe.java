package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract interface IRecipe
{
  public abstract boolean matches(InventoryCrafting paramInventoryCrafting, World paramWorld);
  
  public abstract ItemStack getCraftingResult(InventoryCrafting paramInventoryCrafting);
  
  public abstract int getRecipeSize();
  
  public abstract ItemStack getRecipeOutput();
  
  public abstract ItemStack[] func_179532_b(InventoryCrafting paramInventoryCrafting);
}
