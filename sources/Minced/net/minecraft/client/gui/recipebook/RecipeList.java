// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.recipebook;

import net.minecraft.item.ItemStack;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.stats.RecipeBook;
import com.google.common.collect.Lists;
import java.util.BitSet;
import net.minecraft.item.crafting.IRecipe;
import java.util.List;

public class RecipeList
{
    private List<IRecipe> recipes;
    private final BitSet craftable;
    private final BitSet canFit;
    private final BitSet inBook;
    private boolean singleResultItem;
    
    public RecipeList() {
        this.recipes = (List<IRecipe>)Lists.newArrayList();
        this.craftable = new BitSet();
        this.canFit = new BitSet();
        this.inBook = new BitSet();
        this.singleResultItem = true;
    }
    
    public boolean isNotEmpty() {
        return !this.inBook.isEmpty();
    }
    
    public void updateKnownRecipes(final RecipeBook book) {
        for (int i = 0; i < this.recipes.size(); ++i) {
            this.inBook.set(i, book.isUnlocked(this.recipes.get(i)));
        }
    }
    
    public void canCraft(final RecipeItemHelper handler, final int width, final int height, final RecipeBook book) {
        for (int i = 0; i < this.recipes.size(); ++i) {
            final IRecipe irecipe = this.recipes.get(i);
            final boolean flag = irecipe.canFit(width, height) && book.isUnlocked(irecipe);
            this.canFit.set(i, flag);
            this.craftable.set(i, flag && handler.canCraft(irecipe, null));
        }
    }
    
    public boolean isCraftable(final IRecipe recipe) {
        return this.craftable.get(this.recipes.indexOf(recipe));
    }
    
    public boolean containsCraftableRecipes() {
        return !this.craftable.isEmpty();
    }
    
    public boolean containsValidRecipes() {
        return !this.canFit.isEmpty();
    }
    
    public List<IRecipe> getRecipes() {
        return this.recipes;
    }
    
    public List<IRecipe> getRecipes(final boolean onlyCraftable) {
        final List<IRecipe> list = (List<IRecipe>)Lists.newArrayList();
        for (int i = this.inBook.nextSetBit(0); i >= 0; i = this.inBook.nextSetBit(i + 1)) {
            if ((onlyCraftable ? this.craftable : this.canFit).get(i)) {
                list.add(this.recipes.get(i));
            }
        }
        return list;
    }
    
    public List<IRecipe> getDisplayRecipes(final boolean onlyCraftable) {
        final List<IRecipe> list = (List<IRecipe>)Lists.newArrayList();
        for (int i = this.inBook.nextSetBit(0); i >= 0; i = this.inBook.nextSetBit(i + 1)) {
            if (this.canFit.get(i) && this.craftable.get(i) == onlyCraftable) {
                list.add(this.recipes.get(i));
            }
        }
        return list;
    }
    
    public void add(final IRecipe recipe) {
        this.recipes.add(recipe);
        if (this.singleResultItem) {
            final ItemStack itemstack = this.recipes.get(0).getRecipeOutput();
            final ItemStack itemstack2 = recipe.getRecipeOutput();
            this.singleResultItem = (ItemStack.areItemsEqual(itemstack, itemstack2) && ItemStack.areItemStackTagsEqual(itemstack, itemstack2));
        }
    }
    
    public boolean hasSingleResultItem() {
        return this.singleResultItem;
    }
}
