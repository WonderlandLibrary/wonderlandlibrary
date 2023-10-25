package net.minecraft.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipesBanners;

public class RecipesBanners {
    void addRecipes(CraftingManager p_179534_1_) {
        for (EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
            p_179534_1_.addRecipe(new ItemStack(Items.banner, 1, enumdyecolor.getDyeDamage()), new Object[]{"###", "###", " | ", Character.valueOf((char)'#'), new ItemStack(Blocks.wool, 1, enumdyecolor.getMetadata()), Character.valueOf((char)'|'), Items.stick});
        }
        p_179534_1_.addRecipe((IRecipe)new RecipeDuplicatePattern(null));
        p_179534_1_.addRecipe((IRecipe)new RecipeAddPattern(null));
    }
}
