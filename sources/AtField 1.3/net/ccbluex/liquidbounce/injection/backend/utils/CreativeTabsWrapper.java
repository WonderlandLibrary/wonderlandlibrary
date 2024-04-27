/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.functions.Function1
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.NonNullList
 */
package net.ccbluex.liquidbounce.injection.backend.utils;

import java.util.List;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.util.WrappedCreativeTabs;
import net.ccbluex.liquidbounce.api.util.WrappedMutableList;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.CreativeTabsWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public final class CreativeTabsWrapper
extends CreativeTabs {
    private final WrappedCreativeTabs wrapped;

    public final WrappedCreativeTabs getWrapped() {
        return this.wrapped;
    }

    public ItemStack func_78016_d() {
        Item item;
        IItem iItem = this.wrapped.getTabIconItem();
        boolean bl = false;
        IItem iItem2 = iItem;
        if (iItem2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.ItemImpl<*>");
        }
        Item item2 = item = ((ItemImpl)iItem2).getWrapped();
        return new ItemStack(item2);
    }

    public void func_78018_a(NonNullList nonNullList) {
        this.wrapped.displayAllReleventItems(new WrappedMutableList((List)nonNullList, (Function1)displayAllRelevantItems.1.INSTANCE, (Function1)displayAllRelevantItems.2.INSTANCE));
    }

    public CreativeTabsWrapper(WrappedCreativeTabs wrappedCreativeTabs, String string) {
        super(string);
        this.wrapped = wrappedCreativeTabs;
    }

    public boolean hasSearchBar() {
        return this.wrapped.hasSearchBar();
    }

    public String func_78024_c() {
        return this.wrapped.getTranslatedTabLabel();
    }
}

