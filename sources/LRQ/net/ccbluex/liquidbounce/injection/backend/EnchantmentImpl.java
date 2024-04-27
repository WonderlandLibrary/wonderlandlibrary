/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.enchantment.Enchantment
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment;
import net.minecraft.enchantment.Enchantment;

public final class EnchantmentImpl
implements IEnchantment {
    private final Enchantment wrapped;

    @Override
    public int getEffectId() {
        return Enchantment.func_185258_b((Enchantment)this.wrapped);
    }

    @Override
    public String getTranslatedName(int level) {
        return this.wrapped.func_77316_c(level);
    }

    public final Enchantment getWrapped() {
        return this.wrapped;
    }

    public EnchantmentImpl(Enchantment wrapped) {
        this.wrapped = wrapped;
    }
}

