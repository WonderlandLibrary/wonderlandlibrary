// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;

public interface IRecipe
{
    boolean matches(final InventoryCrafting p0, final World p1);
    
    ItemStack getCraftingResult(final InventoryCrafting p0);
    
    boolean canFit(final int p0, final int p1);
    
    ItemStack getRecipeOutput();
    
    NonNullList<ItemStack> getRemainingItems(final InventoryCrafting p0);
    
    default NonNullList<Ingredient> getIngredients() {
        return NonNullList.create();
    }
    
    default boolean isDynamic() {
        return false;
    }
    
    default String getGroup() {
        return "";
    }
}
