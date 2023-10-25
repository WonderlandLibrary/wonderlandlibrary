/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.game.window.property;

import com.github.steveice10.mc.protocol.data.game.window.property.WindowProperty;

public enum EnchantmentTableProperty implements WindowProperty
{
    LEVEL_SLOT_1,
    LEVEL_SLOT_2,
    LEVEL_SLOT_3,
    XP_SEED,
    ENCHANTMENT_SLOT_1,
    ENCHANTMENT_SLOT_2,
    ENCHANTMENT_SLOT_3;


    public static int getEnchantment(int type, int level) {
        return type | level << 8;
    }

    public static int getEnchantmentType(int enchantmentInfo) {
        return enchantmentInfo & 0xFF;
    }

    public static int getEnchantmentLevel(int enchantmentInfo) {
        return enchantmentInfo >> 8;
    }
}

