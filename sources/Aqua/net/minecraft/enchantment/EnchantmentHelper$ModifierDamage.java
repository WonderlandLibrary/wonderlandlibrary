package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.DamageSource;

static final class EnchantmentHelper.ModifierDamage
implements EnchantmentHelper.IModifier {
    public int damageModifier;
    public DamageSource source;

    private EnchantmentHelper.ModifierDamage() {
    }

    public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
        this.damageModifier += enchantmentIn.calcModifierDamage(enchantmentLevel, this.source);
    }
}
