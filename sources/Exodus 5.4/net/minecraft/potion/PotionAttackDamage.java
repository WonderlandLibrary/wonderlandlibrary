/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.potion;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class PotionAttackDamage
extends Potion {
    protected PotionAttackDamage(int n, ResourceLocation resourceLocation, boolean bl, int n2) {
        super(n, resourceLocation, bl, n2);
    }

    @Override
    public double getAttributeModifierAmount(int n, AttributeModifier attributeModifier) {
        return this.id == Potion.weakness.id ? (double)(-0.5f * (float)(n + 1)) : 1.3 * (double)(n + 1);
    }
}

