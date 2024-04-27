/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jdk.nashorn.api.scripting.JSObject
 *  jdk.nashorn.api.scripting.ScriptUtils
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 */
package net.ccbluex.liquidbounce.script.api;

import java.util.List;
import jdk.nashorn.api.scripting.JSObject;
import jdk.nashorn.api.scripting.ScriptUtils;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.util.WrappedCreativeTabs;
import net.ccbluex.liquidbounce.injection.backend.WrapperImpl;

public final class ScriptTab
extends WrappedCreativeTabs {
    private final IItemStack[] items;
    private final JSObject tabObject;

    public final IItemStack[] getItems() {
        return this.items;
    }

    @Override
    public IItem getTabIconItem() {
        Object object = this.tabObject.getMember("icon");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        IItem iItem = WrapperImpl.INSTANCE.getFunctions().getItemByName((String)object);
        if (iItem == null) {
            Intrinsics.throwNpe();
        }
        return iItem;
    }

    public ScriptTab(JSObject jSObject) {
        Object object = jSObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        super((String)object);
        this.tabObject = jSObject;
        Object object2 = ScriptUtils.convert((Object)this.tabObject.getMember("items"), IItemStack[].class);
        if (object2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<net.ccbluex.liquidbounce.api.minecraft.item.IItemStack>");
        }
        this.items = (IItemStack[])object2;
    }

    @Override
    public void displayAllReleventItems(List list) {
        Iterable iterable = list;
        boolean bl = false;
        for (Object t : iterable) {
            IItemStack iItemStack = (IItemStack)t;
            boolean bl2 = false;
            list.add(iItemStack);
        }
    }

    @Override
    public String getTranslatedTabLabel() {
        Object object = this.tabObject.getMember("name");
        if (object == null) {
            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
        }
        return (String)object;
    }
}

