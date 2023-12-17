// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import java.util.Random;
import net.minecraft.util.DamageSource;
import net.minecraft.init.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentThorns extends Enchantment
{
    public EnchantmentThorns(final Rarity rarityIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.ARMOR_CHEST, slots);
        this.setName("thorns");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 10 + 20 * (enchantmentLevel - 1);
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
        return stack.getItem() instanceof ItemArmor || super.canApply(stack);
    }
    
    @Override
    public void onUserHurt(final EntityLivingBase user, final Entity attacker, final int level) {
        final Random random = user.getRNG();
        final ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.THORNS, user);
        if (shouldHit(level, random)) {
            if (attacker != null) {
                attacker.attackEntityFrom(DamageSource.causeThornsDamage(user), (float)getDamage(level, random));
            }
            if (!itemstack.isEmpty()) {
                itemstack.damageItem(3, user);
            }
        }
        else if (!itemstack.isEmpty()) {
            itemstack.damageItem(1, user);
        }
    }
    
    public static boolean shouldHit(final int level, final Random rnd) {
        return level > 0 && rnd.nextFloat() < 0.15f * level;
    }
    
    public static int getDamage(final int level, final Random rnd) {
        return (level > 10) ? (level - 10) : (1 + rnd.nextInt(4));
    }
}
