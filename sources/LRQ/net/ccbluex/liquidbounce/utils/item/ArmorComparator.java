/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.item;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import net.ccbluex.liquidbounce.api.enums.EnchantmentType;
import net.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemArmor;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.item.ArmorPiece;
import net.ccbluex.liquidbounce.utils.item.ItemUtils;

public class ArmorComparator
extends MinecraftInstance
implements Comparator<ArmorPiece> {
    private static final IEnchantment[] DAMAGE_REDUCTION_ENCHANTMENTS = new IEnchantment[]{classProvider.getEnchantmentEnum(EnchantmentType.PROTECTION), classProvider.getEnchantmentEnum(EnchantmentType.PROJECTILE_PROTECTION), classProvider.getEnchantmentEnum(EnchantmentType.FIRE_PROTECTION), classProvider.getEnchantmentEnum(EnchantmentType.BLAST_PROTECTION)};
    private static final float[] ENCHANTMENT_FACTORS = new float[]{1.5f, 0.4f, 0.39f, 0.38f};
    private static final float[] ENCHANTMENT_DAMAGE_REDUCTION_FACTOR = new float[]{0.04f, 0.08f, 0.15f, 0.08f};
    private static final IEnchantment[] OTHER_ENCHANTMENTS = new IEnchantment[]{classProvider.getEnchantmentEnum(EnchantmentType.FEATHER_FALLING), classProvider.getEnchantmentEnum(EnchantmentType.THORNS), classProvider.getEnchantmentEnum(EnchantmentType.RESPIRATION), classProvider.getEnchantmentEnum(EnchantmentType.AQUA_AFFINITY), classProvider.getEnchantmentEnum(EnchantmentType.UNBREAKING)};
    private static final float[] OTHER_ENCHANTMENT_FACTORS = new float[]{3.0f, 1.0f, 0.1f, 0.05f, 0.01f};

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public int compare(ArmorPiece o1, ArmorPiece o2) {
        int compare = Double.compare(ArmorComparator.round(this.getThresholdedDamageReduction(o2.getItemStack()), 3), ArmorComparator.round(this.getThresholdedDamageReduction(o1.getItemStack()), 3));
        if (compare == 0) {
            int otherEnchantmentCmp = Double.compare(ArmorComparator.round(this.getEnchantmentThreshold(o1.getItemStack()), 3), ArmorComparator.round(this.getEnchantmentThreshold(o2.getItemStack()), 3));
            if (otherEnchantmentCmp == 0) {
                int enchantmentCountCmp = Integer.compare(ItemUtils.getEnchantmentCount(o1.getItemStack()), ItemUtils.getEnchantmentCount(o2.getItemStack()));
                if (enchantmentCountCmp != 0) {
                    return enchantmentCountCmp;
                }
                IItemArmor o1a = o1.getItemStack().getItem().asItemArmor();
                IItemArmor o2a = o2.getItemStack().getItem().asItemArmor();
                int durabilityCmp = Integer.compare(o1a.getArmorMaterial().getDurability(o1a.getArmorType()), o2a.getArmorMaterial().getDurability(o2a.getArmorType()));
                if (durabilityCmp != 0) {
                    return durabilityCmp;
                }
                return Integer.compare(o1a.getArmorMaterial().getEnchantability(), o2a.getArmorMaterial().getEnchantability());
            }
            return otherEnchantmentCmp;
        }
        return compare;
    }

    private float getThresholdedDamageReduction(IItemStack itemStack) {
        IItemArmor item = itemStack.getItem().asItemArmor();
        return this.getDamageReduction(item.getArmorMaterial().getDamageReductionAmount(item.getArmorType()), 0) * (1.0f - this.getThresholdedEnchantmentDamageReduction(itemStack));
    }

    private float getDamageReduction(int defensePoints, int toughness) {
        return 1.0f - Math.min(20.0f, Math.max((float)defensePoints / 5.0f, (float)defensePoints - 1.0f / (2.0f + (float)toughness / 4.0f))) / 25.0f;
    }

    private float getThresholdedEnchantmentDamageReduction(IItemStack itemStack) {
        float sum = 0.0f;
        for (int i = 0; i < DAMAGE_REDUCTION_ENCHANTMENTS.length; ++i) {
            sum += (float)ItemUtils.getEnchantment(itemStack, DAMAGE_REDUCTION_ENCHANTMENTS[i]) * ENCHANTMENT_FACTORS[i] * ENCHANTMENT_DAMAGE_REDUCTION_FACTOR[i];
        }
        return sum;
    }

    private float getEnchantmentThreshold(IItemStack itemStack) {
        float sum = 0.0f;
        for (int i = 0; i < OTHER_ENCHANTMENTS.length; ++i) {
            sum += (float)ItemUtils.getEnchantment(itemStack, OTHER_ENCHANTMENTS[i]) * OTHER_ENCHANTMENT_FACTORS[i];
        }
        return sum;
    }
}

