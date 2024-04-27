/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.potion.IPotionEffect;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\bR\u0014\u0010\u000b\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u0015"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/PotionEffectImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/potion/IPotionEffect;", "wrapped", "Lnet/minecraft/potion/PotionEffect;", "(Lnet/minecraft/potion/PotionEffect;)V", "amplifier", "", "getAmplifier", "()I", "duration", "getDuration", "potionID", "getPotionID", "getWrapped", "()Lnet/minecraft/potion/PotionEffect;", "equals", "", "other", "", "getDurationString", "", "LiKingSense"})
public final class PotionEffectImpl
implements IPotionEffect {
    @NotNull
    private final PotionEffect wrapped;

    @Override
    @NotNull
    public String getDurationString() {
        String string = Potion.func_188410_a((PotionEffect)this.wrapped, (float)1.0f);
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"Potion.getPotionDurationString(wrapped, 1.0f)");
        return string;
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
        return other instanceof PotionEffectImpl && Intrinsics.areEqual((Object)((PotionEffectImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final PotionEffect getWrapped() {
        return this.wrapped;
    }

    public PotionEffectImpl(@NotNull PotionEffect wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

