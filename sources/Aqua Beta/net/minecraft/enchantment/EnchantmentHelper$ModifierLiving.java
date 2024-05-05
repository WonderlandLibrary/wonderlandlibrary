package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;

static final class EnchantmentHelper.ModifierLiving
implements EnchantmentHelper.IModifier {
    public float livingModifier;
    public EnumCreatureAttribute entityLiving;

    private EnchantmentHelper.ModifierLiving() {
    }

    public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
        this.livingModifier += enchantmentIn.calcDamageByCreature(enchantmentLevel, this.entityLiving);
    }
}
