/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.NonNullList
 */
package net.ccbluex.liquidbounce.injection.backend.utils;

import java.util.List;
import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.util.WrappedCreativeTabs;
import net.ccbluex.liquidbounce.api.util.WrappedMutableList;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.CreativeTabsWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public final class CreativeTabsWrapper
extends CreativeTabs {
    private final WrappedCreativeTabs wrapped;

    public ItemStack func_78016_d() {
        Object t;
        IItem $this$unwrap$iv = this.wrapped.getTabIconItem();
        boolean $i$f$unwrap = false;
        IItem iItem = $this$unwrap$iv;
        if (iItem == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.ItemImpl<*>");
        }
        Object t2 = t = ((ItemImpl)iItem).getWrapped();
        return new ItemStack(t2);
    }

    public void func_78018_a(NonNullList<ItemStack> items) {
        this.wrapped.displayAllReleventItems(new WrappedMutableList((List)items, displayAllRelevantItems.1.INSTANCE, displayAllRelevantItems.2.INSTANCE));
    }

    public String func_78024_c() {
        return this.wrapped.getTranslatedTabLabel();
    }

    public boolean hasSearchBar() {
        return this.wrapped.hasSearchBar();
    }

    public final WrappedCreativeTabs getWrapped() {
        return this.wrapped;
    }

    public CreativeTabsWrapper(WrappedCreativeTabs wrapped, String name) {
        super(name);
        this.wrapped = wrapped;
    }
}

