package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

static final class EnchantmentHelper.HurtIterator
implements EnchantmentHelper.IModifier {
    public EntityLivingBase user;
    public Entity attacker;

    private EnchantmentHelper.HurtIterator() {
    }

    public void calculateModifier(Enchantment enchantmentIn, int enchantmentLevel) {
        enchantmentIn.onUserHurt(this.user, this.attacker, enchantmentLevel);
    }
}
