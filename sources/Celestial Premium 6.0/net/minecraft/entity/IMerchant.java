/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.World;

public interface IMerchant {
    public void setCustomer(EntityPlayer var1);

    @Nullable
    public EntityPlayer getCustomer();

    @Nullable
    public MerchantRecipeList getRecipes(EntityPlayer var1);

    public void setRecipes(MerchantRecipeList var1);

    public void useRecipe(MerchantRecipe var1);

    public void verifySellingItem(ItemStack var1);

    public ITextComponent getDisplayName();

    public World func_190670_t_();

    public BlockPos func_190671_u_();
}

