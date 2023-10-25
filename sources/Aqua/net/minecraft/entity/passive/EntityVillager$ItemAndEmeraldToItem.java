package net.minecraft.entity.passive;

import java.util.Random;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

static class EntityVillager.ItemAndEmeraldToItem
implements EntityVillager.ITradeList {
    public ItemStack buyingItemStack;
    public EntityVillager.PriceInfo buyingPriceInfo;
    public ItemStack sellingItemstack;
    public EntityVillager.PriceInfo field_179408_d;

    public EntityVillager.ItemAndEmeraldToItem(Item p_i45813_1_, EntityVillager.PriceInfo p_i45813_2_, Item p_i45813_3_, EntityVillager.PriceInfo p_i45813_4_) {
        this.buyingItemStack = new ItemStack(p_i45813_1_);
        this.buyingPriceInfo = p_i45813_2_;
        this.sellingItemstack = new ItemStack(p_i45813_3_);
        this.field_179408_d = p_i45813_4_;
    }

    public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
        int i = 1;
        if (this.buyingPriceInfo != null) {
            i = this.buyingPriceInfo.getPrice(random);
        }
        int j = 1;
        if (this.field_179408_d != null) {
            j = this.field_179408_d.getPrice(random);
        }
        recipeList.add((Object)new MerchantRecipe(new ItemStack(this.buyingItemStack.getItem(), i, this.buyingItemStack.getMetadata()), new ItemStack(Items.emerald), new ItemStack(this.sellingItemstack.getItem(), j, this.sellingItemstack.getMetadata())));
    }
}
