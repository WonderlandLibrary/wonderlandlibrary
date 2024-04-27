/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.Timer
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.ITimer;
import net.ccbluex.liquidbounce.injection.backend.TimerImpl;
import net.minecraft.util.Timer;

public final class TimerImplKt {
    public static final Timer unwrap(ITimer iTimer) {
        boolean bl = false;
        return ((TimerImpl)iTimer).getWrapped();
    }

    public static final ITimer wrap(Timer timer) {
        boolean bl = false;
        return new TimerImpl(timer);
    }
}

