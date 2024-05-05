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
    public static final PotionEffect unwrap(IPotionEffect $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((PotionEffectImpl)$this$unwrap).getWrapped();
    }

    public static final IPotionEffect wrap(PotionEffect $this$wrap) {
        int $i$f$wrap = 0;
        return new PotionEffectImpl($this$wrap);
    }
}

