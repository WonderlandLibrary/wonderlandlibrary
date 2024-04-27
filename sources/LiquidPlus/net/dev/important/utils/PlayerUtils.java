/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemBlock
 */
package net.dev.important.utils;

import net.dev.important.utils.MinecraftInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class PlayerUtils
extends MinecraftInstance {
    public static boolean checkBlock() {
        for (int i = 0; i < 35; ++i) {
            Item item;
            Item item2 = item = PlayerUtils.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75216_d() ? PlayerUtils.mc.field_71439_g.field_71069_bz.func_75139_a(i).func_75211_c().func_77973_b() : null;
            if (item == null || !(item instanceof ItemBlock)) continue;
            return true;
        }
        return false;
    }
}

