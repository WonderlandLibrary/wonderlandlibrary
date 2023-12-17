// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;

public interface IMerchant
{
    void setCustomer(final EntityPlayer p0);
    
    @Nullable
    EntityPlayer getCustomer();
    
    @Nullable
    MerchantRecipeList getRecipes(final EntityPlayer p0);
    
    void setRecipes(final MerchantRecipeList p0);
    
    void useRecipe(final MerchantRecipe p0);
    
    void verifySellingItem(final ItemStack p0);
    
    ITextComponent getDisplayName();
    
    World getWorld();
    
    BlockPos getPos();
}
