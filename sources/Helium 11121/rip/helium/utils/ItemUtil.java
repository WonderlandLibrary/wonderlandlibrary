package rip.helium.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.List;

/**
 * @author antja03
 */
public class ItemUtil {

    public static float getSharpnessLevel(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSword)) {
            return 0.0f;
        }
        return (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1f;
    }

    public static float getItemDamage(ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemSword)) {
            if (itemStack.getItem() instanceof ItemTool) {
                double damage = 1.0 + ((ItemTool) itemStack.getItem()).getToolMaterial().getDamageVsEntity();
                damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
                return (float) damage;
            } else {
                return 1;
            }
        }

        double damage = 4.0 + ((ItemSword) itemStack.getItem()).getMaterial().getDamageVsEntity();
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25;
        return (float) damage;
    }

    public static double getArmorProtection(ItemStack armorStack) {
        if (!(armorStack.getItem() instanceof ItemArmor))
            return 0.0;

        double protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, armorStack);

        return ((ItemArmor) armorStack.getItem()).damageReduceAmount + ((6 + protectionLevel * protectionLevel) * 0.75 / 3);
    }

    public static ItemStack compareDamage(ItemStack item1, ItemStack item2) {
        if (getItemDamage(item1) > getItemDamage(item2)) {
            return item1;
        } else if (getItemDamage(item2) > getItemDamage(item1)) {
            return item2;
        }
        return null;
    }

    public static ItemStack compareProtection(ItemStack item1, ItemStack item2) {
        if (!(item1.getItem() instanceof ItemArmor) && !(item2.getItem() instanceof ItemArmor))
            return null;

        if (!(item1.getItem() instanceof ItemArmor))
            return item2;

        if (!(item2.getItem() instanceof ItemArmor))
            return item1;

        if (getArmorProtection(item1) > getArmorProtection(item2)) {
            return item1;
        } else if (getArmorProtection(item2) > getArmorProtection(item1)) {
            return item2;
        }

        return null;
    }

    public static boolean isPotionNegative(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ItemPotion) {
            ItemPotion potion = (ItemPotion) itemStack.getItem();

            List<PotionEffect> potionEffectList = potion.getEffects(itemStack);

            return potionEffectList.stream()
                    .map(potionEffect -> Potion.potionTypes[potionEffect.getPotionID()])
                    .anyMatch(Potion::isBadEffect);
        }
        return false;
    }

}
