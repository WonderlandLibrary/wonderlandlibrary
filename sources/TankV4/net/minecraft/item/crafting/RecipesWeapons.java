package net.minecraft.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipesWeapons {
   private Object[][] recipeItems;
   private String[][] recipePatterns = new String[][]{{"X", "X", "#"}};

   public RecipesWeapons() {
      this.recipeItems = new Object[][]{{Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.diamond, Items.gold_ingot}, {Items.wooden_sword, Items.stone_sword, Items.iron_sword, Items.diamond_sword, Items.golden_sword}};
   }

   public void addRecipes(CraftingManager var1) {
      for(int var2 = 0; var2 < this.recipeItems[0].length; ++var2) {
         Object var3 = this.recipeItems[0][var2];

         for(int var4 = 0; var4 < this.recipeItems.length - 1; ++var4) {
            Item var5 = (Item)this.recipeItems[var4 + 1][var2];
            var1.addRecipe(new ItemStack(var5), this.recipePatterns[var4], '#', Items.stick, 'X', var3);
         }
      }

      var1.addRecipe(new ItemStack(Items.bow, 1), " #X", "# X", " #X", 'X', Items.string, '#', Items.stick);
      var1.addRecipe(new ItemStack(Items.arrow, 4), "X", "#", "Y", 'Y', Items.feather, 'X', Items.flint, '#', Items.stick);
   }
}
