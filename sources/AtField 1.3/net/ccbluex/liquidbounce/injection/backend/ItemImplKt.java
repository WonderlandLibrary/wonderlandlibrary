/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.minecraft.item.Item;

public final class ItemImplKt {
    public static final Item unwrap(IItem iItem) {
        boolean bl = false;
        return ((ItemImpl)iItem).getWrapped();
    }

    public static final IItem wrap(Item item) {
        boolean bl = false;
        return new ItemImpl(item);
    }
}

