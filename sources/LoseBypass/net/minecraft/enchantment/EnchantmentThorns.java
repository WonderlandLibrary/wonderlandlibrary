/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import java.util.Random;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

public class EnchantmentThorns
extends Enchantment {
    public EnchantmentThorns(int p_i45764_1_, ResourceLocation p_i45764_2_, int p_i45764_3_) {
        super(p_i45764_1_, p_i45764_2_, p_i45764_3_, EnumEnchantmentType.ARMOR_TORSO);
        this.setName("thorns");
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 10 + 20 * (enchantmentLevel - 1);
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        if (stack.getItem() instanceof ItemArmor) {
            return true;
        }
        boolean bl = super.canApply(stack);
        return bl;
    }

    @Override
    public void onUserHurt(EntityLivingBase user, Entity attacker, int level) {
        Random random = user.getRNG();
        ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantment.thorns, user);
        if (!EnchantmentThorns.func_92094_a(level, random)) {
            if (itemstack == null) return;
            itemstack.damageItem(1, user);
            return;
        }
        if (attacker != null) {
            attacker.attackEntityFrom(DamageSource.causeThornsDamage(user), EnchantmentThorns.func_92095_b(level, random));
            attacker.playSound("damage.thorns", 0.5f, 1.0f);
        }
        if (itemstack == null) return;
        itemstack.damageItem(3, user);
    }

    public static boolean func_92094_a(int p_92094_0_, Random p_92094_1_) {
        if (p_92094_0_ <= 0) {
            return false;
        }
        if (!(p_92094_1_.nextFloat() < 0.15f * (float)p_92094_0_)) return false;
        return true;
    }

    public static int func_92095_b(int p_92095_0_, Random p_92095_1_) {
        int n;
        if (p_92095_0_ > 10) {
            n = p_92095_0_ - 10;
            return n;
        }
        n = 1 + p_92095_1_.nextInt(4);
        return n;
    }
}

