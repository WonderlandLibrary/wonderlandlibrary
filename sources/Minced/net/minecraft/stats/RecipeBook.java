// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.stats;

import net.minecraft.item.crafting.CraftingManager;
import javax.annotation.Nullable;
import net.minecraft.item.crafting.IRecipe;
import java.util.BitSet;

public class RecipeBook
{
    protected final BitSet recipes;
    protected final BitSet newRecipes;
    protected boolean isGuiOpen;
    protected boolean isFilteringCraftable;
    
    public RecipeBook() {
        this.recipes = new BitSet();
        this.newRecipes = new BitSet();
    }
    
    public void copyFrom(final RecipeBook that) {
        this.recipes.clear();
        this.newRecipes.clear();
        this.recipes.or(that.recipes);
        this.newRecipes.or(that.newRecipes);
    }
    
    public void unlock(final IRecipe recipe) {
        if (!recipe.isDynamic()) {
            this.recipes.set(getRecipeId(recipe));
        }
    }
    
    public boolean isUnlocked(@Nullable final IRecipe recipe) {
        return this.recipes.get(getRecipeId(recipe));
    }
    
    public void lock(final IRecipe recipe) {
        final int i = getRecipeId(recipe);
        this.recipes.clear(i);
        this.newRecipes.clear(i);
    }
    
    protected static int getRecipeId(@Nullable final IRecipe recipe) {
        return CraftingManager.REGISTRY.getIDForObject(recipe);
    }
    
    public boolean isNew(final IRecipe recipe) {
        return this.newRecipes.get(getRecipeId(recipe));
    }
    
    public void markSeen(final IRecipe recipe) {
        this.newRecipes.clear(getRecipeId(recipe));
    }
    
    public void markNew(final IRecipe recipe) {
        this.newRecipes.set(getRecipeId(recipe));
    }
    
    public boolean isGuiOpen() {
        return this.isGuiOpen;
    }
    
    public void setGuiOpen(final boolean open) {
        this.isGuiOpen = open;
    }
    
    public boolean isFilteringCraftable() {
        return this.isFilteringCraftable;
    }
    
    public void setFilteringCraftable(final boolean shouldFilter) {
        this.isFilteringCraftable = shouldFilter;
    }
}
