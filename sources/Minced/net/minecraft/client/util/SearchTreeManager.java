// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.util;

import java.util.Iterator;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.item.ItemStack;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class SearchTreeManager implements IResourceManagerReloadListener
{
    public static final Key<ItemStack> ITEMS;
    public static final Key<RecipeList> RECIPES;
    private final Map<Key<?>, SearchTree<?>> trees;
    
    public SearchTreeManager() {
        this.trees = (Map<Key<?>, SearchTree<?>>)Maps.newHashMap();
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        for (final SearchTree<?> searchtree : this.trees.values()) {
            searchtree.recalculate();
        }
    }
    
    public <T> void register(final Key<T> key, final SearchTree<T> searchTreeIn) {
        this.trees.put(key, searchTreeIn);
    }
    
    public <T> ISearchTree<T> get(final Key<T> key) {
        return (ISearchTree<T>)this.trees.get(key);
    }
    
    static {
        ITEMS = new Key<ItemStack>();
        RECIPES = new Key<RecipeList>();
    }
    
    public static class Key<T>
    {
    }
}
