// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.util.math.MathHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.inventory.EntityEquipmentSlot;

public class EnchantmentProtection extends Enchantment
{
    public final Type protectionType;
    
    public EnchantmentProtection(final Rarity rarityIn, final Type protectionTypeIn, final EntityEquipmentSlot... slots) {
        super(rarityIn, EnumEnchantmentType.ARMOR, slots);
        this.protectionType = protectionTypeIn;
        if (protectionTypeIn == Type.FALL) {
            this.type = EnumEnchantmentType.ARMOR_FEET;
        }
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return this.protectionType.getMinimalEnchantability() + (enchantmentLevel - 1) * this.protectionType.getEnchantIncreasePerLevel();
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + this.protectionType.getEnchantIncreasePerLevel();
    }
    
    @Override
    public int getMaxLevel() {
        return 4;
    }
    
    @Override
    public int calcModifierDamage(final int level, final DamageSource source) {
        if (source.canHarmInCreative()) {
            return 0;
        }
        if (this.protectionType == Type.ALL) {
            return level;
        }
        if (this.protectionType == Type.FIRE && source.isFireDamage()) {
            return level * 2;
        }
        if (this.protectionType == Type.FALL && source == DamageSource.FALL) {
            return level * 3;
        }
        if (this.protectionType == Type.EXPLOSION && source.isExplosion()) {
            return level * 2;
        }
        return (this.protectionType == Type.PROJECTILE && source.isProjectile()) ? (level * 2) : 0;
    }
    
    @Override
    public String getName() {
        return "enchantment.protect." + this.protectionType.getTypeName();
    }
    
    public boolean canApplyTogether(final Enchantment ench) {
        if (ench instanceof EnchantmentProtection) {
            final EnchantmentProtection enchantmentprotection = (EnchantmentProtection)ench;
            return this.protectionType != enchantmentprotection.protectionType && (this.protectionType == Type.FALL || enchantmentprotection.protectionType == Type.FALL);
        }
        return super.canApplyTogether(ench);
    }
    
    public static int getFireTimeForEntity(final EntityLivingBase p_92093_0_, int p_92093_1_) {
        final int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FIRE_PROTECTION, p_92093_0_);
        if (i > 0) {
            p_92093_1_ -= MathHelper.floor(p_92093_1_ * (float)i * 0.15f);
        }
        return p_92093_1_;
    }
    
    public static double getBlastDamageReduction(final EntityLivingBase entityLivingBaseIn, double damage) {
        final int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.BLAST_PROTECTION, entityLivingBaseIn);
        if (i > 0) {
            damage -= MathHelper.floor(damage * (i * 0.15f));
        }
        return damage;
    }
    
    public enum Type
    {
        ALL("all", 1, 11, 20), 
        FIRE("fire", 10, 8, 12), 
        FALL("fall", 5, 6, 10), 
        EXPLOSION("explosion", 5, 8, 12), 
        PROJECTILE("projectile", 3, 6, 15);
        
        private final String typeName;
        private final int minEnchantability;
        private final int levelCost;
        private final int levelCostSpan;
        
        private Type(final String name, final int minimal, final int perLevelEnchantability, final int p_i47051_6_) {
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
