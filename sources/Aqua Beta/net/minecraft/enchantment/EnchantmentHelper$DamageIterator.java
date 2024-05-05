package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

static final class EnchantmentHelper.DamageIterator
implements EnchantmentHelper.IModifier {
    public EntityLivingBase user;
    public Entity target;

    private EnchantmentHelper.DamageIterator() {
    }

    public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
        enchantmentIn.onEntityDamaged(this.user, this.target, enchantmentLevel);
    }
}
