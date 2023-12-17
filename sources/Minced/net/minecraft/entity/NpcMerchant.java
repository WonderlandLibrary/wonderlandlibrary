// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;

public class NpcMerchant implements IMerchant
{
    private final InventoryMerchant merchantInventory;
    private final EntityPlayer customer;
    private MerchantRecipeList recipeList;
    private final ITextComponent name;
    
    public NpcMerchant(final EntityPlayer customerIn, final ITextComponent nameIn) {
        this.customer = customerIn;
        this.name = nameIn;
        this.merchantInventory = new InventoryMerchant(customerIn, this);
    }
    
    @Nullable
    @Override
    public EntityPlayer getCustomer() {
        return this.customer;
    }
    
    @Override
    public void setCustomer(@Nullable final EntityPlayer player) {
    }
    
    @Nullable
    @Override
    public MerchantRecipeList getRecipes(final EntityPlayer player) {
        return this.recipeList;
    }
    
    @Override
    public void setRecipes(@Nullable final MerchantRecipeList recipeList) {
        this.recipeList = recipeList;
    }
    
    @Override
    public void useRecipe(final MerchantRecipe recipe) {
        recipe.incrementToolUses();
    }
    
    @Override
    public void verifySellingItem(final ItemStack stack) {
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return (this.name != null) ? this.name : new TextComponentTranslation("entity.Villager.name", new Object[0]);
    }
    
    @Override
    public World getWorld() {
        return this.customer.world;
    }
    
    @Override
    public BlockPos getPos() {
        return new BlockPos(this.customer);
    }
}
