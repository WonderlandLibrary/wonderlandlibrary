/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.util;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.gui.recipebook.RecipeList;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.util.ISearchTree;
import net.minecraft.client.util.SearchTree;
import net.minecraft.item.ItemStack;

public class SearchTreeManager
implements IResourceManagerReloadListener {
    public static final Key<ItemStack> field_194011_a = new Key();
    public static final Key<RecipeList> field_194012_b = new Key();
    private final Map<Key<?>, SearchTree<?>> field_194013_c = Maps.newHashMap();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        for (SearchTree<?> searchtree : this.field_194013_c.values()) {
            searchtree.func_194040_a();
        }
    }

    public <T> void func_194009_a(Key<T> p_194009_1_, SearchTree<T> p_194009_2_) {
        this.field_194013_c.put(p_194009_1_, p_194009_2_);
    }

    public <T> ISearchTree<T> func_194010_a(Key<T> p_194010_1_) {
        return this.field_194013_c.get(p_194010_1_);
    }

    public static class Key<T> {
    }
}

