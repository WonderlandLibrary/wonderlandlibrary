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
implements Comparator {
    private static final IEnchantment[] OTHER_ENCHANTMENTS;
    private static final IEnchantment[] DAMAGE_REDUCTION_ENCHANTMENTS;
    private static final float[] OTHER_ENCHANTMENT_FACTORS;
    private static final float[] ENCHANTMENT_FACTORS;
    private static final float[] ENCHANTMENT_DAMAGE_REDUCTION_FACTOR;

    public static double round(double d, int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = BigDecimal.valueOf(d);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    private float getThresholdedEnchantmentDamageReduction(IItemStack iItemStack) {
        float f = 0.0f;
        for (int i = 0; i < DAMAGE_REDUCTION_ENCHANTMENTS.length; ++i) {
            f += (float)ItemUtils.getEnchantment(iItemStack, DAMAGE_REDUCTION_ENCHANTMENTS[i]) * ENCHANTMENT_FACTORS[i] * ENCHANTMENT_DAMAGE_REDUCTION_FACTOR[i];
        }
        return f;
    }

    public int compare(ArmorPiece armorPiece, ArmorPiece armorPiece2) {
        int n = Double.compare(ArmorComparator.round(this.getThresholdedDamageReduction(armorPiece2.getItemStack()), 3), ArmorComparator.round(this.getThresholdedDamageReduction(armorPiece.getItemStack()), 3));
        if (n == 0) {
            int n2 = Double.compare(ArmorComparator.round(this.getEnchantmentThreshold(armorPiece.getItemStack()), 3), ArmorComparator.round(this.getEnchantmentThreshold(armorPiece2.getItemStack()), 3));
            if (n2 == 0) {
                int n3 = Integer.compare(ItemUtils.getEnchantmentCount(armorPiece.getItemStack()), ItemUtils.getEnchantmentCount(armorPiece2.getItemStack()));
                if (n3 != 0) {
                    return n3;
                }
                IItemArmor iItemArmor = armorPiece.getItemStack().getItem().asItemArmor();
                IItemArmor iItemArmor2 = armorPiece2.getItemStack().getItem().asItemArmor();
                int n4 = Integer.compare(iItemArmor.getArmorMaterial().getDurability(iItemArmor.getArmorType()), iItemArmor2.getArmorMaterial().getDurability(iItemArmor2.getArmorType()));
                if (n4 != 0) {
                    return n4;
                }
                return Integer.compare(iItemArmor.getArmorMaterial().getEnchantability(), iItemArmor2.getArmorMaterial().getEnchantability());
            }
            return n2;
        }
        return n;
    }

    private float getDamageReduction(int n, int n2) {
        return 1.0f - Math.min(20.0f, Math.max((float)n / 5.0f, (float)n - 1.0f / (2.0f + (float)n2 / 4.0f))) / 25.0f;
    }

    public int compare(Object object, Object object2) {
        return this.compare((ArmorPiece)object, (ArmorPiece)object2);
    }

    private float getThresholdedDamageReduction(IItemStack iItemStack) {
        IItemArmor iItemArmor = iItemStack.getItem().asItemArmor();
        return this.getDamageReduction(iItemArmor.getArmorMaterial().getDamageReductionAmount(iItemArmor.getArmorType()), 0) * (1.0f - this.getThresholdedEnchantmentDamageReduction(iItemStack));
    }

    private float getEnchantmentThreshold(IItemStack iItemStack) {
        float f = 0.0f;
        for (int i = 0; i < OTHER_ENCHANTMENTS.length; ++i) {
            f += (float)ItemUtils.getEnchantment(iItemStack, OTHER_ENCHANTMENTS[i]) * OTHER_ENCHANTMENT_FACTORS[i];
        }
        return f;
    }

    static {
        DAMAGE_REDUCTION_ENCHANTMENTS = new IEnchantment[]{classProvider.getEnchantmentEnum(EnchantmentType.PROTECTION), classProvider.getEnchantmentEnum(EnchantmentType.PROJECTILE_PROTECTION), classProvider.getEnchantmentEnum(EnchantmentType.FIRE_PROTECTION), classProvider.getEnchantmentEnum(EnchantmentType.BLAST_PROTECTION)};
        ENCHANTMENT_FACTORS = new float[]{1.5f, 0.4f, 0.39f, 0.38f};
        ENCHANTMENT_DAMAGE_REDUCTION_FACTOR = new float[]{0.04f, 0.08f, 0.15f, 0.08f};
        OTHER_ENCHANTMENTS = new IEnchantment[]{classProvider.getEnchantmentEnum(EnchantmentType.FEATHER_FALLING), classProvider.getEnchantmentEnum(EnchantmentType.THORNS), classProvider.getEnchantmentEnum(EnchantmentType.RESPIRATION), classProvider.getEnchantmentEnum(EnchantmentType.AQUA_AFFINITY), classProvider.getEnchantmentEnum(EnchantmentType.UNBREAKING)};
        OTHER_ENCHANTMENT_FACTORS = new float[]{3.0f, 1.0f, 0.1f, 0.05f, 0.01f};
    }
}

