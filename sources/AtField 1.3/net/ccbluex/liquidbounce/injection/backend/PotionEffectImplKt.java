/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.potion.PotionEffect
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.ccbluex.liquidbounce.injection.backend.PotionEffectImpl;
import net.minecraft.potion.PotionEffect;

public final class PotionEffectImplKt {
    public static final PotionEffect unwrap(IPotionEffect iPotionEffect) {
        boolean bl = false;
        return ((PotionEffectImpl)iPotionEffect).getWrapped();
    }

    public static final IPotionEffect wrap(PotionEffect potionEffect) {
        boolean bl = false;
        return new PotionEffectImpl(potionEffect);
    }
}

