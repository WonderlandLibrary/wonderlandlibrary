// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.item.ItemArmor;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentDurability extends Enchantment
{
    protected EnchantmentDurability(final Rarity rarityIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.BREAKABLE, slots);
        this.setName("durability");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 5 + (enchantmentLevel - 1) * 8;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    @Override
    public boolean canApply(final ItemStack stack) {
        return stack.isItemStackDamageable() || super.canApply(stack);
    }
    
    public static boolean negateDamage(final ItemStack stack, final int level, final Random rand) {
        return (!(stack.getItem() instanceof ItemArmor) || rand.nextFloat() >= 0.6f) && rand.nextInt(level + 1) > 0;
    }
}
