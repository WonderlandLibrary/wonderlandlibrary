// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

import net.minecraft.entity.ai.attributes.AttributeModifier;

public class PotionAttackDamage extends Potion
{
    protected final double bonusPerLevel;
    
    protected PotionAttackDamage(final boolean isBadEffectIn, final int liquidColorIn, final double bonusPerLevelIn) {
        super(isBadEffectIn, liquidColorIn);
        this.bonusPerLevel = bonusPerLevelIn;
    }
    
    @Override
    public double getAttributeModifierAmount(final int amplifier, final AttributeModifier modifier) {
        return this.bonusPerLevel * (amplifier + 1);
    }
}
