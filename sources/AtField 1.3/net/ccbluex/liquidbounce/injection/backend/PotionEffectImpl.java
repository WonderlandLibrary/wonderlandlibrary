/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.jetbrains.annotations.Nullable;

public final class PotionEffectImpl
implements IPotionEffect {
    private final PotionEffect wrapped;

    public PotionEffectImpl(PotionEffect potionEffect) {
        this.wrapped = potionEffect;
    }

    @Override
    public int getDuration() {
        return this.wrapped.func_76459_b();
    }

    @Override
    public int getAmplifier() {
        return this.wrapped.func_76458_c();
    }

    public final PotionEffect getWrapped() {
        return this.wrapped;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof PotionEffectImpl && ((PotionEffectImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public int getPotionID() {
        return Potion.func_188409_a((Potion)this.wrapped.func_188419_a());
    }

    @Override
    public String getDurationString() {
        return Potion.func_188410_a((PotionEffect)this.wrapped, (float)1.0f);
    }
}

