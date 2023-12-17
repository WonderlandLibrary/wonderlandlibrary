// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.util.math.MathHelper;

public class CombatRules
{
    public static float getDamageAfterAbsorb(final float damage, final float totalArmor, final float toughnessAttribute) {
        final float f = 2.0f + toughnessAttribute / 4.0f;
        final float f2 = MathHelper.clamp(totalArmor - damage / f, totalArmor * 0.2f, 20.0f);
        return damage * (1.0f - f2 / 25.0f);
    }
    
    public static float getDamageAfterMagicAbsorb(final float damage, final float enchantModifiers) {
        final float f = MathHelper.clamp(enchantModifiers, 0.0f, 20.0f);
        return damage * (1.0f - f / 25.0f);
    }
}
