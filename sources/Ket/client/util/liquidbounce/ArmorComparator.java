package client.util.liquidbounce;

import client.util.MinecraftInstance;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

import static client.util.liquidbounce.ItemUtils.getEnchantmentCount;

public final class ArmorComparator implements MinecraftInstance, Comparator<ArmorPiece> {

    private static final Enchantment[] DAMAGE_REDUCTION_ENCHANTMENTS = {Enchantment.protection, Enchantment.projectileProtection, Enchantment.fireProtection, Enchantment.blastProtection};
    private static final float[] ENCHANTMENT_FACTORS = {1.5f, 0.4f, 0.39f, 0.38f};
    private static final float[] ENCHANTMENT_DAMAGE_REDUCTION_FACTOR = {0.04f, 0.08f, 0.15f, 0.08f};
    private static final Enchantment[] OTHER_ENCHANTMENTS = {Enchantment.featherFalling, Enchantment.thorns, Enchantment.respiration, Enchantment.aquaAffinity, Enchantment.unbreaking};
    private static final float[] OTHER_ENCHANTMENT_FACTORS = {3f, 1f, 0.1f, 0.05f, 0.01f};

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public int compare(ArmorPiece o1, ArmorPiece o2) {
        final int compare = Double.compare(round(getThresholdedDamageReduction(o2.getItemStack()), 3), round(getThresholdedDamageReduction(o1.getItemStack()), 3));
        if (compare == 0) {
            int otherEnchantmentCmp = Double.compare(round(getEnchantmentThreshold(o1.getItemStack()), 3), round(getEnchantmentThreshold(o2.getItemStack()), 3));
            if (otherEnchantmentCmp == 0) {
                final int enchantmentCountCmp = Integer.compare(getEnchantmentCount(o1.getItemStack()), getEnchantmentCount(o2.getItemStack()));
                if (enchantmentCountCmp != 0) return enchantmentCountCmp;
                final ItemArmor o1a = (ItemArmor) o1.getItemStack().getItem(), o2a = (ItemArmor) o2.getItemStack().getItem();
                final int durabilityCmp = Integer.compare(o1a.getArmorMaterial().getDurability(o1a.armorType), o2a.getArmorMaterial().getDurability(o2a.armorType));
                if (durabilityCmp != 0) return durabilityCmp;
                return Integer.compare(o1a.getArmorMaterial().getEnchantability(), o2a.getArmorMaterial().getEnchantability());
            }
            return otherEnchantmentCmp;
        }
        return compare;
    }

    private float getThresholdedDamageReduction(ItemStack itemStack) {
        final ItemArmor item = (ItemArmor) itemStack.getItem();
        return getDamageReduction(item.getArmorMaterial().getDamageReductionAmount(item.armorType), 0) * (1 - getThresholdedEnchantmentDamageReduction(itemStack));
    }

    private float getDamageReduction(int defensePoints, int toughness) {
        return 1 - Math.min(20, Math.max(defensePoints / 5f, defensePoints - 1 / (2 + toughness / 4f))) / 25f;
    }

    private float getThresholdedEnchantmentDamageReduction(ItemStack itemStack) {
        float sum = 0f;
        for (int i = 0; i < DAMAGE_REDUCTION_ENCHANTMENTS.length; i++) sum += ItemUtils.getEnchantment(itemStack, DAMAGE_REDUCTION_ENCHANTMENTS[i]) * ENCHANTMENT_FACTORS[i] * ENCHANTMENT_DAMAGE_REDUCTION_FACTOR[i];
        return sum;
    }

    private float getEnchantmentThreshold(ItemStack itemStack) {
        float sum = 0f;
        for (int i = 0; i < OTHER_ENCHANTMENTS.length; i++) sum += ItemUtils.getEnchantment(itemStack, OTHER_ENCHANTMENTS[i]) * OTHER_ENCHANTMENT_FACTORS[i];
        return sum;
    }
}
