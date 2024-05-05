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

    @Override
    public String getDurationString() {
        return Potion.func_188410_a((PotionEffect)this.wrapped, (float)1.0f);
    }

    @Override
    public int getAmplifier() {
        return this.wrapped.func_76458_c();
    }

    @Override
    public int getDuration() {
        return this.wrapped.func_76459_b();
    }

    @Override
    public int getPotionID() {
        return Potion.func_188409_a((Potion)this.wrapped.func_188419_a());
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof PotionEffectImpl && ((PotionEffectImpl)other).wrapped.equals(this.wrapped);
    }

    public final PotionEffect getWrapped() {
        return this.wrapped;
    }

    public PotionEffectImpl(PotionEffect wrapped) {
        this.wrapped = wrapped;
    }
}

