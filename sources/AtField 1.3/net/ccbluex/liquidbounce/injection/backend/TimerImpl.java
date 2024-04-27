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

    public TimerImpl(Timer timer) {
        this.wrapped = timer;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof TimerImpl && ((TimerImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public void setTimerSpeed(float f) {
        Timer timer = this.wrapped;
        if (timer == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IMixinTimer");
        }
        ((IMixinTimer)timer).setTimerSpeed(f);
    }

    @Override
    public float getTimerSpeed() {
        Timer timer = this.wrapped;
        if (timer == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IMixinTimer");
        }
        return ((IMixinTimer)timer).getTimerSpeed();
    }

    @Override
    public float getRenderPartialTicks() {
        return this.wrapped.field_194147_b;
    }

    public final Timer getWrapped() {
        return this.wrapped;
    }
}

