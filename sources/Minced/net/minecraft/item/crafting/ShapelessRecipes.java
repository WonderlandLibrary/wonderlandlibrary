// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.world.World;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.util.NonNullList;
import net.minecraft.item.ItemStack;

public class ShapelessRecipes implements IRecipe
{
    private final ItemStack recipeOutput;
    private final NonNullList<Ingredient> recipeItems;
    private final String group;
    
    public ShapelessRecipes(final String group, final ItemStack output, final NonNullList<Ingredient> ingredients) {
        this.group = group;
        this.recipeOutput = output;
        this.recipeItems = ingredients;
    }
    
    @Override
    public String getGroup() {
        return this.group;
    }
    
    @Override
    public ItemStack getRecipeOutput() {
        return this.recipeOutput;
    }
    
    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.recipeItems;
    }
    
    @Override
    public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inv) {
        final NonNullList<ItemStack> nonnulllist = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
        for (int i = 0; i < nonnulllist.size(); ++i) {
            final ItemStack itemstack = inv.getStackInSlot(i);
            if (itemstack.getItem().hasContainerItem()) {
                nonnulllist.set(i, new ItemStack(itemstack.getItem().getContainerItem()));
            }
        }
        return nonnulllist;
    }
    
    @Override
    public boolean matches(final InventoryCrafting inv, final World worldIn) {
        final List<Ingredient> list = (List<Ingredient>)Lists.newArrayList((Iterable)this.recipeItems);
        for (int i = 0; i < inv.getHeight(); ++i) {
            for (int j = 0; j < inv.getWidth(); ++j) {
                final ItemStack itemstack = inv.getStackInRowAndColumn(j, i);
                if (!itemstack.isEmpty()) {
                    boolean flag = false;
                    for (final Ingredient ingredient : list) {
                        if (ingredient.apply(itemstack)) {
                            flag = true;
                            list.remove(ingredient);
                            break;
                        }
                    }
                    if (!flag) {
                        return false;
                    }
                }
            }
        }
        return list.isEmpty();
    }
    
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting inv) {
        return this.recipeOutput.copy();
    }
    
    public static ShapelessRecipes deserialize(final JsonObject json) {
        final String s = JsonUtils.getString(json, "group", "");
        final NonNullList<Ingredient> nonnulllist = deserializeIngredients(JsonUtils.getJsonArray(json, "ingredients"));
        if (nonnulllist.isEmpty()) {
            throw new JsonParseException("No ingredients for shapeless recipe");
        }
        if (nonnulllist.size() > 9) {
            throw new JsonParseException("Too many ingredients for shapeless recipe");
        }
        final ItemStack itemstack = ShapedRecipes.deserializeItem(JsonUtils.getJsonObject(json, "result"), true);
        return new ShapelessRecipes(s, itemstack, nonnulllist);
    }
    
    private static NonNullList<Ingredient> deserializeIngredients(final JsonArray array) {
        final NonNullList<Ingredient> nonnulllist = NonNullList.create();
        for (int i = 0; i < array.size(); ++i) {
            final Ingredient ingredient = ShapedRecipes.deserializeIngredient(array.get(i));
            if (ingredient != Ingredient.EMPTY) {
                nonnulllist.add(ingredient);
            }
        }
        return nonnulllist;
    }
    
    @Override
    public boolean canFit(final int width, final int height) {
        return width * height >= this.recipeItems.size();
    }
}
