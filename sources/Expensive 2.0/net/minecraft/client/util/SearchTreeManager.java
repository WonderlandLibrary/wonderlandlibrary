package net.minecraft.client.util;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;

public class SearchTreeManager implements IResourceManagerReloadListener
{
    public static final Key<ItemStack> ITEMS = new Key<>();
    public static final Key<ItemStack> TAGS = new Key<>();
    public static final Key<RecipeList> RECIPES = new Key<>();
    private final Map < Key<?>, IMutableSearchTree<? >> trees = Maps.newHashMap();

    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        for (IMutableSearchTree<?> imutablesearchtree : this.trees.values())
        {
            imutablesearchtree.recalculate();
        }
    }

    public <T> void add(Key<T> key, IMutableSearchTree<T> value)
    {
        this.trees.put(key, value);
    }

    public <T> IMutableSearchTree<T> get(Key<T> key)
    {
        return (IMutableSearchTree<T>)this.trees.get(key);
    }

    public static class Key<T>
    {
    }
}
