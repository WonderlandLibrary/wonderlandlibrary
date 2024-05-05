package net.minecraft.item;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

/*
 * Exception performing whole class analysis ignored.
 */
public static enum ItemArmor.ArmorMaterial {
    LEATHER("leather", 5, new int[]{1, 3, 2, 1}, 15),
    CHAIN("chainmail", 15, new int[]{2, 5, 4, 1}, 12),
    IRON("iron", 15, new int[]{2, 6, 5, 2}, 9),
    GOLD("gold", 7, new int[]{2, 5, 3, 1}, 25),
    DIAMOND("diamond", 33, new int[]{3, 8, 6, 3}, 10);

    private final String name;
    private final int maxDamageFactor;
    private final int[] damageReductionAmountArray;
    private final int enchantability;

    private ItemArmor.ArmorMaterial(String name, int maxDamage, int[] reductionAmounts, int enchantability) {
        this.name = name;
        this.maxDamageFactor = maxDamage;
        this.damageReductionAmountArray = reductionAmounts;
        this.enchantability = enchantability;
    }

    public int getDurability(int armorType) {
        return ItemArmor.access$000()[armorType] * this.maxDamageFactor;
    }

    public int getDamageReductionAmount(int armorType) {
        return this.damageReductionAmountArray[armorType];
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public Item getRepairItem() {
        return this == LEATHER ? Items.leather : (this == CHAIN ? Items.iron_ingot : (this == GOLD ? Items.gold_ingot : (this == IRON ? Items.iron_ingot : (this == DIAMOND ? Items.diamond : null))));
    }

    public String getName() {
        return this.name;
    }
}
