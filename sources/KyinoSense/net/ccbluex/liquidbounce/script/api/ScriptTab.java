/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.init.Items
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.script.api;

import java.util.List;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptUtils;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0018\u0010\u000b\u001a\u00020\f2\u000e\u0010\r\u001a\n\u0012\u0004\u0012\u00020\u0007\u0018\u00010\u000eH\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016R\u0019\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\n\n\u0002\u0010\n\u001a\u0004\b\b\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0013"}, d2={"Lnet/ccbluex/liquidbounce/script/api/ScriptTab;", "Lnet/minecraft/creativetab/CreativeTabs;", "tabObject", "Ljdk/nashorn/api/scripting/JSObject;", "(Ljdk/nashorn/api/scripting/JSObject;)V", "items", "", "Lnet/minecraft/item/ItemStack;", "getItems", "()[Lnet/minecraft/item/ItemStack;", "[Lnet/minecraft/item/ItemStack;", "displayAllReleventItems", "", "p_78018_1_", "", "getTabIconItem", "Lnet/minecraft/item/Item;", "getTranslatedTabLabel", "", "KyinoClient"})
public final class ScriptTab
extends CreativeTabs {
    @NotNull
    private final ItemStack[] items;
    private final JSObject tabObject;

    @NotNull
    public final ItemStack[] getItems() {
        return this.items;
    }

    @NotNull
    public Item func_78016_d() {
        Object object = this.tabObject.getMember("icon");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        Object object2 = Items.class.getField((String)object).get(null);
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.item.Item");
        }
        return (Item)object2;
    }

    @NotNull
    public String func_78024_c() {
        Object object = this.tabObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        return (String)object;
    }

    public void func_78018_a(@Nullable List<ItemStack> p_78018_1_) {
        ItemStack[] $this$forEach$iv = this.items;
        boolean $i$f$forEach = false;
        ItemStack[] itemStackArray = $this$forEach$iv;
        int n = itemStackArray.length;
        for (int i = 0; i < n; ++i) {
            ItemStack element$iv;
            ItemStack it = element$iv = itemStackArray[i];
            boolean bl = false;
            List<ItemStack> list = p_78018_1_;
            if (list == null) continue;
            list.add(it);
        }
    }

    public ScriptTab(@NotNull JSObject tabObject) {
        Intrinsics.checkParameterIsNotNull(tabObject, "tabObject");
        Object object = tabObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        super((String)object);
        this.tabObject = tabObject;
        Object object2 = ScriptUtils.convert(this.tabObject.getMember("items"), ItemStack[].class);
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<net.minecraft.item.ItemStack>");
        }
        this.items = (ItemStack[])object2;
    }
}

