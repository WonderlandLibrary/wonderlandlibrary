package net.minecraft.entity.passive;

import java.util.Random;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

static class EntityVillager.ListItemForEmeralds
implements EntityVillager.ITradeList {
    public ItemStack itemToBuy;
    public EntityVillager.PriceInfo priceInfo;

    public EntityVillager.ListItemForEmeralds(Item par1Item, EntityVillager.PriceInfo priceInfo) {
        this.itemToBuy = new ItemStack(par1Item);
        this.priceInfo = priceInfo;
    }

    public EntityVillager.ListItemForEmeralds(ItemStack stack, EntityVillager.PriceInfo priceInfo) {
        this.itemToBuy = stack;
        this.priceInfo = priceInfo;
    }

    public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
        ItemStack itemstack1;
        ItemStack itemstack;
        int i = 1;
        if (this.priceInfo != null) {
            i = this.priceInfo.getPrice(random);
        }
        if (i < 0) {
            itemstack = new ItemStack(Items.emerald, 1, 0);
            itemstack1 = new ItemStack(this.itemToBuy.getItem(), -i, this.itemToBuy.getMetadata());
        } else {
            itemstack = new ItemStack(Items.emerald, i, 0);
            itemstack1 = new ItemStack(this.itemToBuy.getItem(), 1, this.itemToBuy.getMetadata());
        }
        recipeList.add((Object)new MerchantRecipe(itemstack, itemstack1));
    }
}
