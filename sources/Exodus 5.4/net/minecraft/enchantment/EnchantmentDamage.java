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
    private static final int[] levelEnchantability;
    public final int damageType;
    private static final int[] thresholdEnchantability;
    private static final String[] protectionName;
    private static final int[] baseEnchantability;

    public EnchantmentDamage(int n, ResourceLocation resourceLocation, int n2, int n3) {
        super(n, resourceLocation, n2, EnumEnchantmentType.WEAPON);
        this.damageType = n3;
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return !(enchantment instanceof EnchantmentDamage);
    }

    @Override
    public int getMinEnchantability(int n) {
        return baseEnchantability[this.damageType] + (n - 1) * levelEnchantability[this.damageType];
    }

    @Override
    public String getName() {
        return "enchantment.damage." + protectionName[this.damageType];
    }

    @Override
    public void onEntityDamaged(EntityLivingBase entityLivingBase, Entity entity, int n) {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase2 = (EntityLivingBase)entity;
            if (this.damageType == 2 && entityLivingBase2.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD) {
                int n2 = 20 + entityLivingBase.getRNG().nextInt(10 * n);
                entityLivingBase2.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, n2, 3));
            }
        }
    }

    @Override
    public float calcDamageByCreature(int n, EnumCreatureAttribute enumCreatureAttribute) {
        return this.damageType == 0 ? (float)n * 1.25f : (this.damageType == 1 && enumCreatureAttribute == EnumCreatureAttribute.UNDEAD ? (float)n * 2.5f : (this.damageType == 2 && enumCreatureAttribute == EnumCreatureAttribute.ARTHROPOD ? (float)n * 2.5f : 0.0f));
    }

    @Override
    public int getMaxEnchantability(int n) {
        return this.getMinEnchantability(n) + thresholdEnchantability[this.damageType];
    }

    static {
        protectionName = new String[]{"all", "undead", "arthropods"};
        baseEnchantability = new int[]{1, 5, 5};
        levelEnchantability = new int[]{11, 8, 8};
        thresholdEnchantability = new int[]{20, 20, 20};
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public boolean canApply(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemAxe ? true : super.canApply(itemStack);
    }
}

