/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.NonNullList
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend.utils;

import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.util.WrappedCreativeTabs;
import net.ccbluex.liquidbounce.api.util.WrappedMutableList;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.CreativeTabsWrapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\t\u001a\u00020\n2\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fH\u0016J\b\u0010\u000e\u001a\u00020\rH\u0016J\b\u0010\u000f\u001a\u00020\u0005H\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/utils/CreativeTabsWrapper;", "Lnet/minecraft/creativetab/CreativeTabs;", "wrapped", "Lnet/ccbluex/liquidbounce/api/util/WrappedCreativeTabs;", "name", "", "(Lnet/ccbluex/liquidbounce/api/util/WrappedCreativeTabs;Ljava/lang/String;)V", "getWrapped", "()Lnet/ccbluex/liquidbounce/api/util/WrappedCreativeTabs;", "displayAllRelevantItems", "", "items", "Lnet/minecraft/util/NonNullList;", "Lnet/minecraft/item/ItemStack;", "getTabIconItem", "getTranslatedTabLabel", "hasSearchBar", "", "LiKingSense"})
public final class CreativeTabsWrapper
extends CreativeTabs {
    @NotNull
    private final WrappedCreativeTabs wrapped;

    @NotNull
    public ItemStack func_78016_d() {
        Object t2;
        IItem $this$unwrap$iv = this.wrapped.getTabIconItem();
        boolean $i$f$unwrap = false;
        IItem iItem = $this$unwrap$iv;
        if (iItem == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.ItemImpl<*>");
        }
        Object t3 = t2 = ((ItemImpl)iItem).getWrapped();
        return new ItemStack(t3);
    }

    public void func_78018_a(@NotNull NonNullList<ItemStack> items) {
        Intrinsics.checkParameterIsNotNull(items, (String)"items");
        this.wrapped.displayAllReleventItems(new WrappedMutableList((List)items, displayAllRelevantItems.1.INSTANCE, displayAllRelevantItems.2.INSTANCE));
    }

    @NotNull
    public String func_78024_c() {
        return this.wrapped.getTranslatedTabLabel();
    }

    public boolean hasSearchBar() {
        return this.wrapped.hasSearchBar();
    }

    @NotNull
    public final WrappedCreativeTabs getWrapped() {
        return this.wrapped;
    }

    public CreativeTabsWrapper(@NotNull WrappedCreativeTabs wrapped, @NotNull String name) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        Intrinsics.checkParameterIsNotNull((Object)name, (String)"name");
        super(name);
        this.wrapped = wrapped;
    }
}

