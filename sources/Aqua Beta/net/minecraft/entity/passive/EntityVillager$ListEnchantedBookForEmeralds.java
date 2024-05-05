package net.minecraft.entity.passive;

import java.util.Random;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

static class EntityVillager.ListEnchantedBookForEmeralds
implements EntityVillager.ITradeList {
    EntityVillager.ListEnchantedBookForEmeralds() {
    }

    public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
        Enchantment enchantment = Enchantment.enchantmentsBookList[random.nextInt(Enchantment.enchantmentsBookList.length)];
        int i = MathHelper.getRandomIntegerInRange((Random)random, (int)enchantment.getMinLevel(), (int)enchantment.getMaxLevel());
        ItemStack itemstack = Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, i));
        int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
        if (j > 64) {
            j = 64;
        }
        recipeList.add((Object)new MerchantRecipe(new ItemStack(Items.book), new ItemStack(Items.emerald, j), itemstack));
    }
}
