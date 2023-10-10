/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDamage
extends Enchantment {
    private static final String[] protectionName = new String[]{"all", "undead", "arthropods"};
    private static final int[] baseEnchantability = new int[]{1, 5, 5};
    private static final int[] levelEnchantability = new int[]{11, 8, 8};
    private static final int[] thresholdEnchantability = new int[]{20, 20, 20};
    public final int damageType;

    public EnchantmentDamage(int enchID, ResourceLocation enchName, int enchWeight, int classification) {
        super(enchID, enchName, enchWeight, EnumEnchantmentType.WEAPON);
        this.damageType = classification;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return baseEnchantability[this.damageType] + (enchantmentLevel - 1) * levelEnchantability[this.damageType];
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + thresholdEnchantability[this.damageType];
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public float calcDamageByCreature(int level, EnumCreatureAttribute creatureType) {
        float f;
        if (this.damageType == 0) {
            f = (float)level * 1.25f;
            return f;
        }
        if (this.damageType == 1 && creatureType == EnumCreatureAttribute.UNDEAD) {
            f = (float)level * 2.5f;
            return f;
        }
        if (this.damageType != 2) return 0.0f;
        if (creatureType != EnumCreatureAttribute.ARTHROPOD) return 0.0f;
        f = (float)level * 2.5f;
        return f;
    }

    @Override
    public String getName() {
        return "enchantment.damage." + protectionName[this.damageType];
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        if (ench instanceof EnchantmentDamage) return false;
        return true;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        if (stack.getItem() instanceof ItemAxe) {
            return true;
        }
        boolean bl = super.canApply(stack);
        return bl;
    }

    @Override
    public void onEntityDamaged(EntityLivingBase user, Entity target, int level) {
        if (!(target instanceof EntityLivingBase)) return;
        EntityLivingBase entitylivingbase = (EntityLivingBase)target;
        if (this.damageType != 2) return;
        if (entitylivingbase.getCreatureAttribute() != EnumCreatureAttribute.ARTHROPOD) return;
        int i = 20 + user.getRNG().nextInt(10 * level);
        entitylivingbase.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, i, 3));
    }
}

