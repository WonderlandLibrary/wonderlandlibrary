// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.item.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;

public class RecipesTools
{
    private String[][] recipePatterns;
    private Object[][] recipeItems;
    private static final String __OBFID = "CL_00000096";
    
    public RecipesTools() {
        this.recipePatterns = new String[][] { { "XXX", " # ", " # " }, { "X", "#", "#" }, { "XX", "X#", " #" }, { "XX", " #", " #" } };
        this.recipeItems = new Object[][] { { Blocks.planks, Blocks.cobblestone, Items.iron_ingot, Items.diamond, Items.gold_ingot }, { Items.wooden_pickaxe, Items.stone_pickaxe, Items.iron_pickaxe, Items.diamond_pickaxe, Items.golden_pickaxe }, { Items.wooden_shovel, Items.stone_shovel, Items.iron_shovel, Items.diamond_shovel, Items.golden_shovel }, { Items.wooden_axe, Items.stone_axe, Items.iron_axe, Items.diamond_axe, Items.golden_axe }, { Items.wooden_hoe, Items.stone_hoe, Items.iron_hoe, Items.diamond_hoe, Items.golden_hoe } };
    }
    
    public void addRecipes(final CraftingManager p_77586_1_) {
        for (int var2 = 0; var2 < this.recipeItems[0].length; ++var2) {
            final Object var3 = this.recipeItems[0][var2];
            for (int var4 = 0; var4 < this.recipeItems.length - 1; ++var4) {
                final Item var5 = (Item)this.recipeItems[var4 + 1][var2];
                p_77586_1_.addRecipe(new ItemStack(var5), this.recipePatterns[var4], '#', Items.stick, 'X', var3);
            }
        }
        p_77586_1_.addRecipe(new ItemStack(Items.shears), " #", "# ", '#', Items.iron_ingot);
    }
}
