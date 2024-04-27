/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.Enchantment
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment;
import net.ccbluex.liquidbounce.injection.backend.EnchantmentImpl;
import net.minecraft.enchantment.Enchantment;

public final class EnchantmentImplKt {
    public static final Enchantment unwrap(IEnchantment iEnchantment) {
        boolean bl = false;
        return ((EnchantmentImpl)iEnchantment).getWrapped();
    }

    public static final IEnchantment wrap(Enchantment enchantment) {
        boolean bl = false;
        return new EnchantmentImpl(enchantment);
    }
}

