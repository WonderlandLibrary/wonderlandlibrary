// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import net.minecraft.item.Item;
import java.util.Comparator;
import it.unimi.dsi.fastutil.ints.IntComparators;
import net.minecraft.client.util.RecipeItemHelper;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import com.google.common.base.Predicate;

public class Ingredient implements Predicate<ItemStack>
{
    public static final Ingredient EMPTY;
    private final ItemStack[] matchingStacks;
    private IntList matchingStacksPacked;
    
    private Ingredient(final ItemStack... p_i47503_1_) {
        this.matchingStacks = p_i47503_1_;
    }
    
    public ItemStack[] getMatchingStacks() {
        return this.matchingStacks;
    }
    
    public boolean apply(@Nullable final ItemStack p_apply_1_) {
        if (p_apply_1_ == null) {
            return false;
        }
        for (final ItemStack itemstack : this.matchingStacks) {
            if (itemstack.getItem() == p_apply_1_.getItem()) {
                final int i = itemstack.getMetadata();
                if (i == 32767 || i == p_apply_1_.getMetadata()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public IntList getValidItemStacksPacked() {
        if (this.matchingStacksPacked == null) {
            this.matchingStacksPacked = (IntList)new IntArrayList(this.matchingStacks.length);
            for (final ItemStack itemstack : this.matchingStacks) {
                this.matchingStacksPacked.add(RecipeItemHelper.pack(itemstack));
            }
            this.matchingStacksPacked.sort((Comparator)IntComparators.NATURAL_COMPARATOR);
        }
        return this.matchingStacksPacked;
    }
    
    public static Ingredient fromItem(final Item p_193367_0_) {
        return fromStacks(new ItemStack(p_193367_0_, 1, 32767));
    }
    
    public static Ingredient fromItems(final Item... items) {
        final ItemStack[] aitemstack = new ItemStack[items.length];
        for (int i = 0; i < items.length; ++i) {
            aitemstack[i] = new ItemStack(items[i]);
        }
        return fromStacks(aitemstack);
    }
    
    public static Ingredient fromStacks(final ItemStack... stacks) {
        if (stacks.length > 0) {
            for (final ItemStack itemstack : stacks) {
                if (!itemstack.isEmpty()) {
                    return new Ingredient(stacks);
                }
            }
        }
        return Ingredient.EMPTY;
    }
    
    static {
        EMPTY = new Ingredient(new ItemStack[0]) {
            @Override
            public boolean apply(@Nullable final ItemStack p_apply_1_) {
                return p_apply_1_.isEmpty();
            }
        };
    }
}
