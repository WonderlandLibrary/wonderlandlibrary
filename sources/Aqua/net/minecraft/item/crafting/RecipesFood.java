package net.minecraft.item.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class RecipesFood {
    public void addRecipes(CraftingManager p_77608_1_) {
        p_77608_1_.addShapelessRecipe(new ItemStack(Items.mushroom_stew), new Object[]{Blocks.brown_mushroom, Blocks.red_mushroom, Items.bowl});
        p_77608_1_.addRecipe(new ItemStack(Items.cookie, 8), new Object[]{"#X#", Character.valueOf((char)'X'), new ItemStack(Items.dye, 1, EnumDyeColor.BROWN.getDyeDamage()), Character.valueOf((char)'#'), Items.wheat});
        p_77608_1_.addRecipe(new ItemStack(Items.rabbit_stew), new Object[]{" R ", "CPM", " B ", Character.valueOf((char)'R'), new ItemStack(Items.cooked_rabbit), Character.valueOf((char)'C'), Items.carrot, Character.valueOf((char)'P'), Items.baked_potato, Character.valueOf((char)'M'), Blocks.brown_mushroom, Character.valueOf((char)'B'), Items.bowl});
        p_77608_1_.addRecipe(new ItemStack(Items.rabbit_stew), new Object[]{" R ", "CPD", " B ", Character.valueOf((char)'R'), new ItemStack(Items.cooked_rabbit), Character.valueOf((char)'C'), Items.carrot, Character.valueOf((char)'P'), Items.baked_potato, Character.valueOf((char)'D'), Blocks.red_mushroom, Character.valueOf((char)'B'), Items.bowl});
        p_77608_1_.addRecipe(new ItemStack(Blocks.melon_block), new Object[]{"MMM", "MMM", "MMM", Character.valueOf((char)'M'), Items.melon});
        p_77608_1_.addRecipe(new ItemStack(Items.melon_seeds), new Object[]{"M", Character.valueOf((char)'M'), Items.melon});
        p_77608_1_.addRecipe(new ItemStack(Items.pumpkin_seeds, 4), new Object[]{"M", Character.valueOf((char)'M'), Blocks.pumpkin});
        p_77608_1_.addShapelessRecipe(new ItemStack(Items.pumpkin_pie), new Object[]{Blocks.pumpkin, Items.sugar, Items.egg});
        p_77608_1_.addShapelessRecipe(new ItemStack(Items.fermented_spider_eye), new Object[]{Items.spider_eye, Blocks.brown_mushroom, Items.sugar});
        p_77608_1_.addShapelessRecipe(new ItemStack(Items.blaze_powder, 2), new Object[]{Items.blaze_rod});
        p_77608_1_.addShapelessRecipe(new ItemStack(Items.magma_cream), new Object[]{Items.blaze_powder, Items.slime_ball});
    }
}
