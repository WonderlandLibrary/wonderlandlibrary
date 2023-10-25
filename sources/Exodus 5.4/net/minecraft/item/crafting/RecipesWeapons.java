/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class RecipesWeapons {
    private Object[][] recipeItems;
    private String[][] recipePatterns = new String[][]{{"X", "X", "#"}};

    public RecipesWeapons() {
        this.recipeItems = new Object[][]{{Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.diamond, Items.gold_ingot}, {Items.wooden_sword, Items.stone_sword, Items.iron_sword, Items.diamond_sword, Items.golden_sword}};
    }

    public void addRecipes(CraftingManager craftingManager) {
        int n = 0;
        while (n < this.recipeItems[0].length) {
            Object object = this.recipeItems[0][n];
            int n2 = 0;
            while (n2 < this.recipeItems.length - 1) {
                Item item = (Item)this.recipeItems[n2 + 1][n];
                craftingManager.addRecipe(new ItemStack(item), this.recipePatterns[n2], Character.valueOf('#'), Items.stick, Character.valueOf('X'), object);
                ++n2;
            }
            ++n;
        }
        craftingManager.addRecipe(new ItemStack(Items.bow, 1), " #X", "# X", " #X", Character.valueOf('X'), Items.string, Character.valueOf('#'), Items.stick);
        craftingManager.addRecipe(new ItemStack(Items.arrow, 4), "X", "#", "Y", Character.valueOf('Y'), Items.feather, Character.valueOf('X'), Items.flint, Character.valueOf('#'), Items.stick);
    }
}

