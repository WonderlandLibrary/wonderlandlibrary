// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.util;

import java.util.Iterator;
import com.google.common.collect.Table;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.CraftingManager;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import net.minecraft.client.gui.recipebook.RecipeList;
import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import java.util.Map;
import net.minecraft.stats.RecipeBook;

public class RecipeBookClient extends RecipeBook
{
    public static final Map<CreativeTabs, List<RecipeList>> RECIPES_BY_TAB;
    public static final List<RecipeList> ALL_RECIPES;
    
    private static RecipeList newRecipeList(final CreativeTabs srcTab) {
        final RecipeList recipelist = new RecipeList();
        RecipeBookClient.ALL_RECIPES.add(recipelist);
        RecipeBookClient.RECIPES_BY_TAB.computeIfAbsent(srcTab, p_194085_0_ -> new ArrayList()).add(recipelist);
        RecipeBookClient.RECIPES_BY_TAB.computeIfAbsent(CreativeTabs.SEARCH, p_194083_0_ -> new ArrayList()).add(recipelist);
        return recipelist;
    }
    
    private static CreativeTabs getItemStackTab(final ItemStack stackIn) {
        final CreativeTabs creativetabs = stackIn.getItem().getCreativeTab();
        if (creativetabs != CreativeTabs.BUILDING_BLOCKS && creativetabs != CreativeTabs.TOOLS && creativetabs != CreativeTabs.REDSTONE) {
            return (creativetabs == CreativeTabs.COMBAT) ? CreativeTabs.TOOLS : CreativeTabs.MISC;
        }
        return creativetabs;
    }
    
    static {
        RECIPES_BY_TAB = Maps.newHashMap();
        ALL_RECIPES = Lists.newArrayList();
        final Table<CreativeTabs, String, RecipeList> table = (Table<CreativeTabs, String, RecipeList>)HashBasedTable.create();
        for (final IRecipe irecipe : CraftingManager.REGISTRY) {
            if (!irecipe.isDynamic()) {
                final CreativeTabs creativetabs = getItemStackTab(irecipe.getRecipeOutput());
                final String s = irecipe.getGroup();
                RecipeList recipelist1;
                if (s.isEmpty()) {
                    recipelist1 = newRecipeList(creativetabs);
                }
                else {
                    recipelist1 = (RecipeList)table.get((Object)creativetabs, (Object)s);
                    if (recipelist1 == null) {
                        recipelist1 = newRecipeList(creativetabs);
                        table.put((Object)creativetabs, (Object)s, (Object)recipelist1);
                    }
                }
                recipelist1.add(irecipe);
            }
        }
    }
}
