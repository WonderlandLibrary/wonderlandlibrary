/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util;

import net.minecraft.util.math.MathHelper;

public class CombatRules {
    public static float getDamageAfterAbsorb(float damage, float totalArmor, float toughnessAttribute) {
        float f = 2.0f + toughnessAttribute / 4.0f;
        float f1 = MathHelper.clamp(totalArmor - damage / f, totalArmor * 0.2f, 20.0f);
        return damage * (1.0f - f1 / 25.0f);
    }

    public static float getDamageAfterMagicAbsorb(float p_188401_0_, float p_188401_1_) {
        float f = MathHelper.clamp(p_188401_1_, 0.0f, 20.0f);
        return p_188401_0_ * (1.0f - f / 25.0f);
    }
}

