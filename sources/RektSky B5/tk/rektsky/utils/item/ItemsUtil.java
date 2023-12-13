/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.utils.item;

import java.util.Comparator;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import tk.rektsky.Client;

public class ItemsUtil {

    public static class ArmorComparator
    implements Comparator<Slot> {
        @Override
        public int compare(Slot s1, Slot s2) {
            ItemStack first = s1.getStack();
            ItemStack second = s2.getStack();
            if (first.getItem() instanceof ItemArmor) {
                float reduceFirst = ((ItemArmor)first.getItem()).damageReduceAmount;
                if (first.isItemEnchanted()) {
                    NBTTagList enchantments = first.getEnchantmentTagList();
                    for (int i2 = 0; i2 < enchantments.tagCount(); ++i2) {
                        if (enchantments.getCompoundTagAt(i2).getInteger("id") != 0) continue;
                        EnchantmentProtection protection = (EnchantmentProtection)Enchantment.protection;
                        reduceFirst *= (float)protection.calcModifierDamage(enchantments.getCompoundTagAt(i2).getInteger("lvl"), DamageSource.causePlayerDamage(Client.mc.thePlayer));
                    }
                }
                float reduceSecond = ((ItemArmor)second.getItem()).damageReduceAmount;
                if (second.isItemEnchanted()) {
                    NBTTagList enchantments = second.getEnchantmentTagList();
                    for (int i3 = 0; i3 < enchantments.tagCount(); ++i3) {
                        if (enchantments.getCompoundTagAt(i3).getInteger("id") != 0) continue;
                        EnchantmentProtection protection = (EnchantmentProtection)Enchantment.protection;
                        reduceSecond *= (float)protection.calcModifierDamage(enchantments.getCompoundTagAt(i3).getInteger("lvl"), DamageSource.causePlayerDamage(Client.mc.thePlayer));
                    }
                }
                return Float.compare(reduceSecond, reduceFirst);
            }
            return 0;
        }
    }
}

