/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.jvm.JvmStatic
 */
package net.ccbluex.liquidbounce.script.api.global;

import kotlin.jvm.JvmStatic;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;

public final class Item {
    public static final Item INSTANCE;

    static {
        Item item;
        INSTANCE = item = new Item();
    }

    private Item() {
    }

    @JvmStatic
    public static final IItemStack create(String string) {
        return ItemUtils.createItem(string);
    }
}

