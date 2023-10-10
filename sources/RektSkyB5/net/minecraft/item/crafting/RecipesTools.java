/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class RecipesTools {
    private String[][] recipePatterns = new String[][]{{"XXX", " # ", " # "}, {"X", "#", "#"}, {"XX", "X#", " #"}, {"XX", " #", " #"}};
    private Object[][] recipeItems = new Object[][]{{Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.diamond, Items.gold_ingot}, {Items.wooden_pickaxe, Items.stone_pickaxe, Items.iron_pickaxe, Items.diamond_pickaxe, Items.golden_pickaxe}, {Items.wooden_shovel, Items.stone_shovel, Items.iron_shovel, Items.diamond_shovel, Items.golden_shovel}, {Items.wooden_axe, Items.stone_axe, Items.iron_axe, Items.diamond_axe, Items.golden_axe}, {Items.wooden_hoe, Items.stone_hoe, Items.iron_hoe, Items.diamond_hoe, Items.golden_hoe}};

    public void addRecipes(CraftingManager p_77586_1_) {
        for (int i2 = 0; i2 < this.recipeItems[0].length; ++i2) {
            Object object = this.recipeItems[0][i2];
            for (int j2 = 0; j2 < this.recipeItems.length - 1; ++j2) {
                Item item = (Item)this.recipeItems[j2 + 1][i2];
                p_77586_1_.addRecipe(new ItemStack(item), this.recipePatterns[j2], Character.valueOf('#'), Items.stick, Character.valueOf('X'), object);
            }
        }
        p_77586_1_.addRecipe(new ItemStack(Items.shears), " #", "# ", Character.valueOf('#'), Items.iron_ingot);
    }
}

