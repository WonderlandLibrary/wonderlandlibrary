/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;

public class EnchantmentProtection
extends Enchantment {
    public final Type protectionType;

    public EnchantmentProtection(Enchantment.Rarity rarityIn, Type protectionTypeIn, EntityEquipmentSlot ... slots) {
        super(rarityIn, EnumEnchantmentType.ARMOR, slots);
        this.protectionType = protectionTypeIn;
        if (protectionTypeIn == Type.FALL) {
            this.type = EnumEnchantmentType.ARMOR_FEET;
        }
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return this.protectionType.getMinimalEnchantability() + (enchantmentLevel - 1) * this.protectionType.getEnchantIncreasePerLevel();
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + this.protectionType.getEnchantIncreasePerLevel();
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    public int calcModifierDamage(int level, DamageSource source) {
        if (source.canHarmInCreative()) {
            return 0;
        }
        if (this.protectionType == Type.ALL) {
            return level;
        }
        if (this.protectionType == Type.FIRE && source.isFireDamage()) {
            return level * 2;
        }
        if (this.protectionType == Type.FALL && source == DamageSource.fall) {
            return level * 3;
        }
        if (this.protectionType == Type.EXPLOSION && source.isExplosion()) {
            return level * 2;
        }
        return this.protectionType == Type.PROJECTILE && source.isProjectile() ? level * 2 : 0;
    }

    @Override
    public String getName() {
        return "enchantment.protect." + this.protectionType.getTypeName();
    }

    @Override
    public boolean canApplyTogether(Enchantment ench) {
        if (ench instanceof EnchantmentProtection) {
            EnchantmentProtection enchantmentprotection = (EnchantmentProtection)ench;
            if (this.protectionType == enchantmentprotection.protectionType) {
                return false;
            }
            return this.protectionType == Type.FALL || enchantmentprotection.protectionType == Type.FALL;
        }
        return super.canApplyTogether(ench);
    }

    public static int getFireTimeForEntity(EntityLivingBase p_92093_0_, int p_92093_1_) {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FIRE_PROTECTION, p_92093_0_);
        if (i > 0) {
            p_92093_1_ -= MathHelper.floor((float)p_92093_1_ * (float)i * 0.15f);
        }
        return p_92093_1_;
    }

    public static double getBlastDamageReduction(EntityLivingBase entityLivingBaseIn, double damage) {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.BLAST_PROTECTION, entityLivingBaseIn);
        if (i > 0) {
            damage -= (double)MathHelper.floor(damage * (double)((float)i * 0.15f));
        }
        return damage;
    }

    public static enum Type {
        ALL("all", 1, 11, 20),
        FIRE("fire", 10, 8, 12),
        FALL("fall", 5, 6, 10),
        EXPLOSION("explosion", 5, 8, 12),
        PROJECTILE("projectile", 3, 6, 15);

        private final String typeName;
        private final int minEnchantability;
        private final int levelCost;
        private final int levelCostSpan;

        private Type(String name, int minimal, int perLevelEnchantability, int p_i47051_6_) {
            this.typeName = name;
            this.minEnchantability = minimal;
            this.levelCost = perLevelEnchantability;
            this.levelCostSpan = p_i47051_6_;
        }

        public String getTypeName() {
            return this.typeName;
        }

        public int getMinimalEnchantability() {
            return this.minEnchantability;
        }

        public int getEnchantIncreasePerLevel() {
            return this.levelCost;
        }
    }
}

