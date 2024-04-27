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
    public static final Enchantment unwrap(IEnchantment $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((EnchantmentImpl)$this$unwrap).getWrapped();
    }

    public static final IEnchantment wrap(Enchantment $this$wrap) {
        int $i$f$wrap = 0;
        return new EnchantmentImpl($this$wrap);
    }
}

