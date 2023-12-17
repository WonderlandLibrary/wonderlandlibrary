// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.util;

import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.ints.IntIterator;
import java.util.Iterator;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntAVLTreeSet;
import java.util.Collection;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import com.google.common.collect.Lists;
import java.util.BitSet;
import net.minecraft.item.crafting.Ingredient;
import java.util.List;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;

public class RecipeItemHelper
{
    public final Int2IntMap itemToCount;
    
    public RecipeItemHelper() {
        this.itemToCount = (Int2IntMap)new Int2IntOpenHashMap();
    }
    
    public void accountStack(final ItemStack stack) {
        if (!stack.isEmpty() && !stack.isItemDamaged() && !stack.isItemEnchanted() && !stack.hasDisplayName()) {
            final int i = pack(stack);
            final int j = stack.getCount();
            this.increment(i, j);
        }
    }
    
    public static int pack(final ItemStack stack) {
        final Item item = stack.getItem();
        final int i = item.getHasSubtypes() ? stack.getMetadata() : 0;
        return Item.REGISTRY.getIDForObject(item) << 16 | (i & 0xFFFF);
    }
    
    public boolean containsItem(final int packedItem) {
        return this.itemToCount.get(packedItem) > 0;
    }
    
    public int tryTake(final int packedItem, final int maximum) {
        final int i = this.itemToCount.get(packedItem);
        if (i >= maximum) {
            this.itemToCount.put(packedItem, i - maximum);
            return packedItem;
        }
        return 0;
    }
    
    private void increment(final int packedItem, final int amount) {
        this.itemToCount.put(packedItem, this.itemToCount.get(packedItem) + amount);
    }
    
    public boolean canCraft(final IRecipe recipe, @Nullable final IntList packedItemList) {
        return this.canCraft(recipe, packedItemList, 1);
    }
    
    public boolean canCraft(final IRecipe recipe, @Nullable final IntList packedItemList, final int maxAmount) {
        return new RecipePicker(recipe).tryPick(maxAmount, packedItemList);
    }
    
    public int getBiggestCraftableStack(final IRecipe recipe, @Nullable final IntList packedItemList) {
        return this.getBiggestCraftableStack(recipe, Integer.MAX_VALUE, packedItemList);
    }
    
    public int getBiggestCraftableStack(final IRecipe recipe, final int maxAmount, @Nullable final IntList packedItemList) {
        return new RecipePicker(recipe).tryPickAll(maxAmount, packedItemList);
    }
    
    public static ItemStack unpack(final int packedItem) {
        return (packedItem == 0) ? ItemStack.EMPTY : new ItemStack(Item.getItemById(packedItem >> 16 & 0xFFFF), 1, packedItem & 0xFFFF);
    }
    
    public void clear() {
        this.itemToCount.clear();
    }
    
    class RecipePicker
    {
        private final IRecipe recipe;
        private final List<Ingredient> ingredients;
        private final int ingredientCount;
        private final int[] possessedIngredientStacks;
        private final int possessedIngredientStackCount;
        private final BitSet data;
        private IntList path;
        
        public RecipePicker(final IRecipe recipeIn) {
            this.ingredients = (List<Ingredient>)Lists.newArrayList();
            this.path = (IntList)new IntArrayList();
            this.recipe = recipeIn;
            this.ingredients.addAll(recipeIn.getIngredients());
            this.ingredients.removeIf(p_194103_0_ -> p_194103_0_ == Ingredient.EMPTY);
            this.ingredientCount = this.ingredients.size();
            this.possessedIngredientStacks = this.getUniqueAvailIngredientItems();
            this.possessedIngredientStackCount = this.possessedIngredientStacks.length;
            this.data = new BitSet(this.ingredientCount + this.possessedIngredientStackCount + this.ingredientCount + this.ingredientCount * this.possessedIngredientStackCount);
            for (int i = 0; i < this.ingredients.size(); ++i) {
                final IntList intlist = this.ingredients.get(i).getValidItemStacksPacked();
                for (int j = 0; j < this.possessedIngredientStackCount; ++j) {
                    if (intlist.contains(this.possessedIngredientStacks[j])) {
                        this.data.set(this.getIndex(true, j, i));
                    }
                }
            }
        }
        
        public boolean tryPick(final int maxAmount, @Nullable final IntList listIn) {
            if (maxAmount <= 0) {
                return true;
            }
            int k = 0;
            while (this.dfs(maxAmount)) {
                RecipeItemHelper.this.tryTake(this.possessedIngredientStacks[this.path.getInt(0)], maxAmount);
                final int l = this.path.size() - 1;
                this.setSatisfied(this.path.getInt(l));
                for (int i1 = 0; i1 < l; ++i1) {
                    this.toggleResidual((i1 & 0x1) == 0x0, (int)this.path.get(i1), (int)this.path.get(i1 + 1));
                }
                this.path.clear();
                this.data.clear(0, this.ingredientCount + this.possessedIngredientStackCount);
                ++k;
            }
            final boolean flag = k == this.ingredientCount;
            final boolean flag2 = flag && listIn != null;
            if (flag2) {
                listIn.clear();
            }
            this.data.clear(0, this.ingredientCount + this.possessedIngredientStackCount + this.ingredientCount);
            int j1 = 0;
            final List<Ingredient> list = this.recipe.getIngredients();
            for (int k2 = 0; k2 < list.size(); ++k2) {
                if (flag2 && list.get(k2) == Ingredient.EMPTY) {
                    listIn.add(0);
                }
                else {
                    for (int l2 = 0; l2 < this.possessedIngredientStackCount; ++l2) {
                        if (this.hasResidual(false, j1, l2)) {
                            this.toggleResidual(true, l2, j1);
                            RecipeItemHelper.this.increment(this.possessedIngredientStacks[l2], maxAmount);
                            if (flag2) {
                                listIn.add(this.possessedIngredientStacks[l2]);
                            }
                        }
                    }
                    ++j1;
                }
            }
            return flag;
        }
        
        private int[] getUniqueAvailIngredientItems() {
            final IntCollection intcollection = (IntCollection)new IntAVLTreeSet();
            for (final Ingredient ingredient : this.ingredients) {
                intcollection.addAll((IntCollection)ingredient.getValidItemStacksPacked());
            }
            final IntIterator intiterator = intcollection.iterator();
            while (intiterator.hasNext()) {
                if (!RecipeItemHelper.this.containsItem(intiterator.nextInt())) {
                    intiterator.remove();
                }
            }
            return intcollection.toIntArray();
        }
        
        private boolean dfs(final int amount) {
            for (int k = this.possessedIngredientStackCount, l = 0; l < k; ++l) {
                if (RecipeItemHelper.this.itemToCount.get(this.possessedIngredientStacks[l]) >= amount) {
                    this.visit(false, l);
                    while (!this.path.isEmpty()) {
                        final int i1 = this.path.size();
                        final boolean flag = (i1 & 0x1) == 0x1;
                        final int j1 = this.path.getInt(i1 - 1);
                        if (!flag && !this.isSatisfied(j1)) {
                            break;
                        }
                        for (int k2 = flag ? this.ingredientCount : k, l2 = 0; l2 < k2; ++l2) {
                            if (!this.hasVisited(flag, l2) && this.hasConnection(flag, j1, l2) && this.hasResidual(flag, j1, l2)) {
                                this.visit(flag, l2);
                                break;
                            }
                        }
                        final int i2 = this.path.size();
                        if (i2 != i1) {
                            continue;
                        }
                        this.path.removeInt(i2 - 1);
                    }
                    if (!this.path.isEmpty()) {
                        return true;
                    }
                }
            }
            return false;
        }
        
        private boolean isSatisfied(final int p_194091_1_) {
            return this.data.get(this.getSatisfiedIndex(p_194091_1_));
        }
        
        private void setSatisfied(final int p_194096_1_) {
            this.data.set(this.getSatisfiedIndex(p_194096_1_));
        }
        
        private int getSatisfiedIndex(final int p_194094_1_) {
            return this.ingredientCount + this.possessedIngredientStackCount + p_194094_1_;
        }
        
        private boolean hasConnection(final boolean p_194093_1_, final int p_194093_2_, final int p_194093_3_) {
            return this.data.get(this.getIndex(p_194093_1_, p_194093_2_, p_194093_3_));
        }
        
        private boolean hasResidual(final boolean p_194100_1_, final int p_194100_2_, final int p_194100_3_) {
            return p_194100_1_ != this.data.get(1 + this.getIndex(p_194100_1_, p_194100_2_, p_194100_3_));
        }
        
        private void toggleResidual(final boolean p_194089_1_, final int p_194089_2_, final int p_194089_3_) {
            this.data.flip(1 + this.getIndex(p_194089_1_, p_194089_2_, p_194089_3_));
        }
        
        private int getIndex(final boolean p_194095_1_, final int p_194095_2_, final int p_194095_3_) {
            final int k = p_194095_1_ ? (p_194095_2_ * this.ingredientCount + p_194095_3_) : (p_194095_3_ * this.ingredientCount + p_194095_2_);
            return this.ingredientCount + this.possessedIngredientStackCount + this.ingredientCount + 2 * k;
        }
        
        private void visit(final boolean p_194088_1_, final int p_194088_2_) {
            this.data.set(this.getVisitedIndex(p_194088_1_, p_194088_2_));
            this.path.add(p_194088_2_);
        }
        
        private boolean hasVisited(final boolean p_194101_1_, final int p_194101_2_) {
            return this.data.get(this.getVisitedIndex(p_194101_1_, p_194101_2_));
        }
        
        private int getVisitedIndex(final boolean p_194099_1_, final int p_194099_2_) {
            return (p_194099_1_ ? 0 : this.ingredientCount) + p_194099_2_;
        }
        
        public int tryPickAll(final int p_194102_1_, @Nullable final IntList list) {
            int k = 0;
            int l = Math.min(p_194102_1_, this.getMinIngredientCount()) + 1;
            int i1;
            while (true) {
                i1 = (k + l) / 2;
                if (this.tryPick(i1, null)) {
                    if (l - k <= 1) {
                        break;
                    }
                    k = i1;
                }
                else {
                    l = i1;
                }
            }
            if (i1 > 0) {
                this.tryPick(i1, list);
            }
            return i1;
        }
        
        private int getMinIngredientCount() {
            int k = Integer.MAX_VALUE;
            for (final Ingredient ingredient : this.ingredients) {
                int l = 0;
                for (final int i1 : ingredient.getValidItemStacksPacked()) {
                    l = Math.max(l, RecipeItemHelper.this.itemToCount.get(i1));
                }
                if (k > 0) {
                    k = Math.min(k, l);
                }
            }
            return k;
        }
    }
}
