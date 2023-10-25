/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class RecipesArmor {
    private String[][] recipePatterns = new String[][]{{"XXX", "X X"}, {"X X", "XXX", "XXX"}, {"XXX", "X X", "X X"}, {"X X", "X X"}};
    private Item[][] recipeItems = new Item[][]{{Items.leather, Items.iron_ingot, Items.diamond, Items.gold_ingot}, {Items.leather_helmet, Items.iron_helmet, Items.diamond_helmet, Items.golden_helmet}, {Items.leather_chestplate, Items.iron_chestplate, Items.diamond_chestplate, Items.golden_chestplate}, {Items.leather_leggings, Items.iron_leggings, Items.diamond_leggings, Items.golden_leggings}, {Items.leather_boots, Items.iron_boots, Items.diamond_boots, Items.golden_boots}};

    public void addRecipes(CraftingManager craftingManager) {
        int n = 0;
        while (n < this.recipeItems[0].length) {
            Item item = this.recipeItems[0][n];
            int n2 = 0;
            while (n2 < this.recipeItems.length - 1) {
                Item item2 = this.recipeItems[n2 + 1][n];
                craftingManager.addRecipe(new ItemStack(item2), this.recipePatterns[n2], Character.valueOf('X'), item);
                ++n2;
            }
            ++n;
        }
    }
}

