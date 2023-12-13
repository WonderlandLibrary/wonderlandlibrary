package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ShapedRecipes implements IRecipe {
   private final int recipeHeight;
   private final int recipeWidth;
   private final ItemStack recipeOutput;
   private final ItemStack[] recipeItems;
   private boolean copyIngredientNBT;

   public boolean matches(InventoryCrafting var1, World var2) {
      for(int var3 = 0; var3 <= 3 - this.recipeWidth; ++var3) {
         byte var4 = 0;
         if (var4 <= 3 - this.recipeHeight) {
            return true;
         }
      }

      return false;
   }

   public ItemStack[] getRemainingItems(InventoryCrafting var1) {
      ItemStack[] var2 = new ItemStack[var1.getSizeInventory()];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         ItemStack var4 = var1.getStackInSlot(var3);
         if (var4 != null && var4.getItem().hasContainerItem()) {
            var2[var3] = new ItemStack(var4.getItem().getContainerItem());
         }
      }

      return var2;
   }

   public ItemStack getCraftingResult(InventoryCrafting var1) {
      ItemStack var2 = this.getRecipeOutput().copy();
      if (this.copyIngredientNBT) {
         for(int var3 = 0; var3 < var1.getSizeInventory(); ++var3) {
            ItemStack var4 = var1.getStackInSlot(var3);
            if (var4 != null && var4.hasTagCompound()) {
               var2.setTagCompound((NBTTagCompound)var4.getTagCompound().copy());
            }
         }
      }

      return var2;
   }

   public ItemStack getRecipeOutput() {
      return this.recipeOutput;
   }

   public ShapedRecipes(int var1, int var2, ItemStack[] var3, ItemStack var4) {
      this.recipeWidth = var1;
      this.recipeHeight = var2;
      this.recipeItems = var3;
      this.recipeOutput = var4;
   }

   public int getRecipeSize() {
      return this.recipeWidth * this.recipeHeight;
   }
}
