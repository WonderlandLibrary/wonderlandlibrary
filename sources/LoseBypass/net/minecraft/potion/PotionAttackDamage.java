/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.potion;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionAttackDamage
extends Potion {
    protected PotionAttackDamage(int potionID, ResourceLocation location, boolean badEffect, int potionColor) {
        super(potionID, location, badEffect, potionColor);
    }

    @Override
    public double getAttributeModifierAmount(int p_111183_1_, AttributeModifier modifier) {
        double d;
        if (this.id == Potion.weakness.id) {
            d = -0.5f * (float)(p_111183_1_ + 1);
            return d;
        }
        d = 1.3 * (double)(p_111183_1_ + 1);
        return d;
    }
}

