package net.minecraft.entity.passive;

import java.util.Random;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

static class EntityVillager.ListEnchantedItemForEmeralds
implements EntityVillager.ITradeList {
    public ItemStack enchantedItemStack;
    public EntityVillager.PriceInfo priceInfo;

    public EntityVillager.ListEnchantedItemForEmeralds(Item p_i45814_1_, EntityVillager.PriceInfo p_i45814_2_) {
        this.enchantedItemStack = new ItemStack(p_i45814_1_);
        this.priceInfo = p_i45814_2_;
    }

    public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
        int i = 1;
        if (this.priceInfo != null) {
            i = this.priceInfo.getPrice(random);
        }
        ItemStack itemstack = new ItemStack(Items.emerald, i, 0);
        ItemStack itemstack1 = new ItemStack(this.enchantedItemStack.getItem(), 1, this.enchantedItemStack.getMetadata());
        itemstack1 = EnchantmentHelper.addRandomEnchantment((Random)random, (ItemStack)itemstack1, (int)(5 + random.nextInt(15)));
        recipeList.add((Object)new MerchantRecipe(itemstack, itemstack1));
    }
}
