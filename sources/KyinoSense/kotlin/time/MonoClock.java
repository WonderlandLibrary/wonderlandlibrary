/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.time;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.time.AbstractLongClock;
import kotlin.time.Clock;
import kotlin.time.ExperimentalTime;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0014J\b\u0010\u0006\u001a\u00020\u0007H\u0016\u00a8\u0006\b"}, d2={"Lkotlin/time/MonoClock;", "Lkotlin/time/AbstractLongClock;", "Lkotlin/time/Clock;", "()V", "read", "", "toString", "", "kotlin-stdlib"})
@SinceKotlin(version="1.3")
@ExperimentalTime
public final class MonoClock
extends AbstractLongClock
implements Clock {
    public static final MonoClock INSTANCE;

    @Override
    protected long read() {
        return System.nanoTime();
    }

    @NotNull
    public String toString() {
        return "Clock(System.nanoTime())";
    }

    private MonoClock() {
        super(TimeUnit.NANOSECONDS);
    }

    static {
        MonoClock monoClock;
        INSTANCE = monoClock = new MonoClock();
    }
}

