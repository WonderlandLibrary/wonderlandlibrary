/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jdk.nashorn.api.scripting.JSObject
 *  jdk.nashorn.api.scripting.ScriptUtils
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.script.api;

import java.util.List;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptUtils;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.util.WrappedCreativeTabs;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u000b\u001a\u00020\f2\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\rH\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016R\u0019\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006\u00a2\u0006\n\n\u0002\u0010\n\u001a\u0004\b\b\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/script/api/ScriptTab;", "Lnet/ccbluex/liquidbounce/api/util/WrappedCreativeTabs;", "tabObject", "Ljdk/nashorn/api/scripting/JSObject;", "(Ljdk/nashorn/api/scripting/JSObject;)V", "items", "", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "getItems", "()[Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "[Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "displayAllReleventItems", "", "", "getTabIconItem", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "getTranslatedTabLabel", "", "LiKingSense"})
public final class ScriptTab
extends WrappedCreativeTabs {
    @NotNull
    private final IItemStack[] items;
    private final JSObject tabObject;

    @NotNull
    public final IItemStack[] getItems() {
        return this.items;
    }

    @Override
    @NotNull
    public IItem getTabIconItem() {
        Object object = this.tabObject.getMember("icon");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        return WrapperImpl.INSTANCE.getFunctions().getItemByName((String)object);
    }

    @Override
    @NotNull
    public String getTranslatedTabLabel() {
        Object object = this.tabObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        return (String)object;
    }

    @Override
    public void displayAllReleventItems(@NotNull List<IItemStack> items) {
        Intrinsics.checkParameterIsNotNull(items, (String)"items");
        Iterable $this$forEach$iv = items;
        boolean $i$f$forEach = false;
        for (Object element$iv : $this$forEach$iv) {
            IItemStack it = (IItemStack)element$iv;
            boolean bl = false;
            items.add(it);
        }
    }

    public ScriptTab(@NotNull JSObject tabObject) {
        Intrinsics.checkParameterIsNotNull((Object)tabObject, (String)"tabObject");
        Object object = tabObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        super((String)object);
        this.tabObject = tabObject;
        Object object2 = ScriptUtils.convert((Object)this.tabObject.getMember("items"), IItemStack[].class);
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<net.ccbluex.liquidbounce.api.minecraft.item.IItemStack>");
        }
        this.items = (IItemStack[])object2;
    }
}

