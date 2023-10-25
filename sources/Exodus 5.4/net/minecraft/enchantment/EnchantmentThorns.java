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
    @Override
    public int getMaxEnchantability(int n) {
        return super.getMinEnchantability(n) + 50;
    }

    public static boolean func_92094_a(int n, Random random) {
        return n <= 0 ? false : random.nextFloat() < 0.15f * (float)n;
    }

    @Override
    public boolean canApply(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemArmor ? true : super.canApply(itemStack);
    }

    @Override
    public int getMinEnchantability(int n) {
        return 10 + 20 * (n - 1);
    }

    @Override
    public void onUserHurt(EntityLivingBase entityLivingBase, Entity entity, int n) {
        Random random = entityLivingBase.getRNG();
        ItemStack itemStack = EnchantmentHelper.getEnchantedItem(Enchantment.thorns, entityLivingBase);
        if (EnchantmentThorns.func_92094_a(n, random)) {
            if (entity != null) {
                entity.attackEntityFrom(DamageSource.causeThornsDamage(entityLivingBase), EnchantmentThorns.func_92095_b(n, random));
                entity.playSound("damage.thorns", 0.5f, 1.0f);
            }
            if (itemStack != null) {
                itemStack.damageItem(3, entityLivingBase);
            }
        } else if (itemStack != null) {
            itemStack.damageItem(1, entityLivingBase);
        }
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    public EnchantmentThorns(int n, ResourceLocation resourceLocation, int n2) {
        super(n, resourceLocation, n2, EnumEnchantmentType.ARMOR_TORSO);
        this.setName("thorns");
    }

    public static int func_92095_b(int n, Random random) {
        return n > 10 ? n - 10 : 1 + random.nextInt(4);
    }
}

