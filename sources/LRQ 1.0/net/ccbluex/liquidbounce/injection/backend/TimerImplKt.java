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
    public static final Timer unwrap(ITimer $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((TimerImpl)$this$unwrap).getWrapped();
    }

    public static final ITimer wrap(Timer $this$wrap) {
        int $i$f$wrap = 0;
        return new TimerImpl($this$wrap);
    }
}

