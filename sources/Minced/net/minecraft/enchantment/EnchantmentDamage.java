// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentDamage extends Enchantment
{
    private static final String[] DAMAGE_NAMES;
    private static final int[] MIN_COST;
    private static final int[] LEVEL_COST;
    private static final int[] LEVEL_COST_SPAN;
    public final int damageType;
    
    public EnchantmentDamage(final Rarity rarityIn, final int damageTypeIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.WEAPON, slots);
        this.damageType = damageTypeIn;
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return EnchantmentDamage.MIN_COST[this.damageType] + (enchantmentLevel - 1) * EnchantmentDamage.LEVEL_COST[this.damageType];
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + EnchantmentDamage.LEVEL_COST_SPAN[this.damageType];
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @Override
    public float calcDamageByCreature(final int level, final EnumCreatureAttribute creatureType) {
        if (this.damageType == 0) {
            return 1.0f + Math.max(0, level - 1) * 0.5f;
        }
        if (this.damageType == 1 && creatureType == EnumCreatureAttribute.UNDEAD) {
            return level * 2.5f;
        }
        return (this.damageType == 2 && creatureType == EnumCreatureAttribute.ARTHROPOD) ? (level * 2.5f) : 0.0f;
    }
    
    @Override
    public String getName() {
        return "enchantment.damage." + EnchantmentDamage.DAMAGE_NAMES[this.damageType];
    }
    
    public boolean canApplyTogether(final Enchantment ench) {
        return !(ench instanceof EnchantmentDamage);
    }
    
    @Override
    public boolean canApply(final ItemStack stack) {
        return stack.getItem() instanceof ItemAxe || super.canApply(stack);
    }
    
    @Override
    public void onEntityDamaged(final EntityLivingBase user, final Entity target, final int level) {
        if (target instanceof EntityLivingBase) {
            final EntityLivingBase entitylivingbase = (EntityLivingBase)target;
            if (this.damageType == 2 && entitylivingbase.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
                final int i = 20 + user.getRNG().nextInt(10 * level);
                entitylivingbase.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, i, 3));
            }
        }
    }
    
    static {
        DAMAGE_NAMES = new String[] { "all", "undead", "arthropods" };
        MIN_COST = new int[] { 1, 5, 5 };
        LEVEL_COST = new int[] { 11, 8, 8 };
        LEVEL_COST_SPAN = new int[] { 20, 20, 20 };
    }
}
