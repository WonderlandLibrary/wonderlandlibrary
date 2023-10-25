package net.minecraft.entity.passive;

import java.util.Random;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

static class EntityVillager.EmeraldForItems
implements EntityVillager.ITradeList {
    public Item sellItem;
    public EntityVillager.PriceInfo price;

    public EntityVillager.EmeraldForItems(Item itemIn, EntityVillager.PriceInfo priceIn) {
        this.sellItem = itemIn;
        this.price = priceIn;
    }

    public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
        int i = 1;
        if (this.price != null) {
            i = this.price.getPrice(random);
        }
        recipeList.add((Object)new MerchantRecipe(new ItemStack(this.sellItem, i, 0), Items.emerald));
    }
}
