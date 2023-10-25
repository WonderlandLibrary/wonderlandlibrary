package net.minecraft.entity.passive;

import java.util.Random;
import net.minecraft.village.MerchantRecipeList;

static interface EntityVillager.ITradeList {
    public void modifyMerchantRecipeList(MerchantRecipeList var1, Random var2);
}
