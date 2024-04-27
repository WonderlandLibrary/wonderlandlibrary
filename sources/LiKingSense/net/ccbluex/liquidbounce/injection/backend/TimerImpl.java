/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.TypeCastException
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.util.Timer
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.util.ITimer;
import net.ccbluex.liquidbounce.injection.implementations.IMixinTimer;
import net.minecraft.util.Timer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\n\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u0010\u001a\u00020\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0096\u0002R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR$\u0010\n\u001a\u00020\u00062\u0006\u0010\t\u001a\u00020\u00068V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u000b\u0010\b\"\u0004\b\f\u0010\rR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000f\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/TimerImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/ITimer;", "wrapped", "Lnet/minecraft/util/Timer;", "(Lnet/minecraft/util/Timer;)V", "renderPartialTicks", "", "getRenderPartialTicks", "()F", "value", "timerSpeed", "getTimerSpeed", "setTimerSpeed", "(F)V", "getWrapped", "()Lnet/minecraft/util/Timer;", "equals", "", "other", "", "LiKingSense"})
public final class TimerImpl
implements ITimer {
    @NotNull
    private final Timer wrapped;

    @Override
    public float getTimerSpeed() {
        Timer timer = this.wrapped;
        if (timer == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IMixinTimer");
        }
        return ((IMixinTimer)timer).getTimerSpeed();
    }

    @Override
    public void setTimerSpeed(float value) {
        Timer timer = this.wrapped;
        if (timer == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IMixinTimer");
        }
        ((IMixinTimer)timer).setTimerSpeed(value);
    }

    @Override
    public float getRenderPartialTicks() {
        return this.wrapped.field_194147_b;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof TimerImpl && Intrinsics.areEqual((Object)((TimerImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final Timer getWrapped() {
        return this.wrapped;
    }

    public TimerImpl(@NotNull Timer wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

