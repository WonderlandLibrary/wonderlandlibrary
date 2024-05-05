/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.util.Timer
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.util.ITimer;
import net.ccbluex.liquidbounce.injection.implementations.IMixinTimer;
import net.minecraft.util.Timer;
import org.jetbrains.annotations.Nullable;

public final class TimerImpl
implements ITimer {
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
        return other instanceof TimerImpl && ((TimerImpl)other).wrapped.equals(this.wrapped);
    }

    public final Timer getWrapped() {
        return this.wrapped;
    }

    public TimerImpl(Timer wrapped) {
        this.wrapped = wrapped;
    }
}

