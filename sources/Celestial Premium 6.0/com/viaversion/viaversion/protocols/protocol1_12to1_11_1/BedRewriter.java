/*
 * Decompiled with CFR 0.150.
 */
package com.viaversion.viaversion.protocols.protocol1_12to1_11_1;

import com.viaversion.viaversion.api.minecraft.item.Item;

public class BedRewriter {
    public static void toClientItem(Item item) {
        if (item == null) {
            return;
        }
        if (item.identifier() == 355 && item.data() == 0) {
            item.setData((short)14);
        }
    }

    public static void toServerItem(Item item) {
        if (item == null) {
            return;
        }
        if (item.identifier() == 355 && item.data() == 14) {
            item.setData((short)0);
        }
    }
}

