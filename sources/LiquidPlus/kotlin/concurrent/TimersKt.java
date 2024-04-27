/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.concurrent;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.Unit;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u00004\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u001aM\u0010\u0000\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001aO\u0010\u0000\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a\u001a\u0010\u0010\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0001\u001aM\u0010\u0010\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001aO\u0010\u0010\u001a\u00020\u00012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a'\u0010\u0011\u001a\u00020\f2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a3\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a;\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a3\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a;\u0010\u0012\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a;\u0010\u0015\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0013\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u001a;\u0010\u0015\u001a\u00020\f*\u00020\u00012\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\b\u001a\u00020\t2\u0019\b\u0004\u0010\n\u001a\u0013\u0012\u0004\u0012\u00020\f\u0012\u0004\u0012\u00020\r0\u000b\u00a2\u0006\u0002\b\u000eH\u0087\b\u00f8\u0001\u0000\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\u0016"}, d2={"fixedRateTimer", "Ljava/util/Timer;", "name", "", "daemon", "", "startAt", "Ljava/util/Date;", "period", "", "action", "Lkotlin/Function1;", "Ljava/util/TimerTask;", "", "Lkotlin/ExtensionFunctionType;", "initialDelay", "timer", "timerTask", "schedule", "time", "delay", "scheduleAtFixedRate", "kotlin-stdlib"})
@JvmName(name="TimersKt")
public final class TimersKt {
    @InlineOnly
    private static final TimerTask schedule(Timer $this$schedule, long delay, Function1<? super TimerTask, Unit> action) {
        Intrinsics.checkNotNullParameter($this$schedule, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        TimerTask task = new TimerTask(action){
            final /* synthetic */ Function1<TimerTask, Unit> $action;
            {
                this.$action = $action;
            }

            public void run() {
                this.$action.invoke(this);
            }
        };
        $this$schedule.schedule(task, delay);
        return task;
    }

    @InlineOnly
    private static final TimerTask schedule(Timer $this$schedule, Date time, Function1<? super TimerTask, Unit> action) {
        Intrinsics.checkNotNullParameter($this$schedule, "<this>");
        Intrinsics.checkNotNullParameter(time, "time");
        Intrinsics.checkNotNullParameter(action, "action");
        TimerTask task = new /* invalid duplicate definition of identical inner class */;
        $this$schedule.schedule(task, time);
        return task;
    }

    @InlineOnly
    private static final TimerTask schedule(Timer $this$schedule, long delay, long period, Function1<? super TimerTask, Unit> action) {
        Intrinsics.checkNotNullParameter($this$schedule, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        TimerTask task = new /* invalid duplicate definition of identical inner class */;
        $this$schedule.schedule(task, delay, period);
        return task;
    }

    @InlineOnly
    private static final TimerTask schedule(Timer $this$schedule, Date time, long period, Function1<? super TimerTask, Unit> action) {
        Intrinsics.checkNotNullParameter($this$schedule, "<this>");
        Intrinsics.checkNotNullParameter(time, "time");
        Intrinsics.checkNotNullParameter(action, "action");
        TimerTask task = new /* invalid duplicate definition of identical inner class */;
        $this$schedule.schedule(task, time, period);
        return task;
    }

    @InlineOnly
    private static final TimerTask scheduleAtFixedRate(Timer $this$scheduleAtFixedRate, long delay, long period, Function1<? super TimerTask, Unit> action) {
        Intrinsics.checkNotNullParameter($this$scheduleAtFixedRate, "<this>");
        Intrinsics.checkNotNullParameter(action, "action");
        TimerTask task = new /* invalid duplicate definition of identical inner class */;
        $this$scheduleAtFixedRate.scheduleAtFixedRate(task, delay, period);
        return task;
    }

    @InlineOnly
    private static final TimerTask scheduleAtFixedRate(Timer $this$scheduleAtFixedRate, Date time, long period, Function1<? super TimerTask, Unit> action) {
        Intrinsics.checkNotNullParameter($this$scheduleAtFixedRate, "<this>");
        Intrinsics.checkNotNullParameter(time, "time");
        Intrinsics.checkNotNullParameter(action, "action");
        TimerTask task = new /* invalid duplicate definition of identical inner class */;
        $this$scheduleAtFixedRate.scheduleAtFixedRate(task, time, period);
        return task;
    }

    @PublishedApi
    @NotNull
    public static final Timer timer(@Nullable String name, boolean daemon) {
        return name == null ? new Timer(daemon) : new Timer(name, daemon);
    }

    @InlineOnly
    private static final Timer timer(String name, boolean daemon, long initialDelay, long period, Function1<? super TimerTask, Unit> action) {
        Timer timer;
        Intrinsics.checkNotNullParameter(action, "action");
        Timer timer2 = timer = TimersKt.timer(name, daemon);
        TimerTask timerTask2 = new /* invalid duplicate definition of identical inner class */;
        timer2.schedule(timerTask2, initialDelay, period);
        return timer;
    }

    static /* synthetic */ Timer timer$default(String name, boolean daemon, long initialDelay, long period, Function1 action, int n, Object object) {
        if ((n & 1) != 0) {
            name = null;
        }
        if ((n & 2) != 0) {
            daemon = false;
        }
        if ((n & 4) != 0) {
            initialDelay = 0L;
        }
        Intrinsics.checkNotNullParameter(action, "action");
        Timer timer = TimersKt.timer(name, daemon);
        object = timer;
        TimerTask timerTask2 = new /* invalid duplicate definition of identical inner class */;
        ((Timer)object).schedule(timerTask2, initialDelay, period);
        return timer;
    }

    @InlineOnly
    private static final Timer timer(String name, boolean daemon, Date startAt, long period, Function1<? super TimerTask, Unit> action) {
        Timer timer;
        Intrinsics.checkNotNullParameter(startAt, "startAt");
        Intrinsics.checkNotNullParameter(action, "action");
        Timer timer2 = timer = TimersKt.timer(name, daemon);
        TimerTask timerTask2 = new /* invalid duplicate definition of identical inner class */;
        timer2.schedule(timerTask2, startAt, period);
        return timer;
    }

    static /* synthetic */ Timer timer$default(String name, boolean daemon, Date startAt, long period, Function1 action, int n, Object object) {
        if ((n & 1) != 0) {
            name = null;
        }
        if ((n & 2) != 0) {
            daemon = false;
        }
        Intrinsics.checkNotNullParameter(startAt, "startAt");
        Intrinsics.checkNotNullParameter(action, "action");
        Timer timer = TimersKt.timer(name, daemon);
        object = timer;
        TimerTask timerTask2 = new /* invalid duplicate definition of identical inner class */;
        ((Timer)object).schedule(timerTask2, startAt, period);
        return timer;
    }

    @InlineOnly
    private static final Timer fixedRateTimer(String name, boolean daemon, long initialDelay, long period, Function1<? super TimerTask, Unit> action) {
        Timer timer;
        Intrinsics.checkNotNullParameter(action, "action");
        Timer timer2 = timer = TimersKt.timer(name, daemon);
        TimerTask timerTask2 = new /* invalid duplicate definition of identical inner class */;
        timer2.scheduleAtFixedRate(timerTask2, initialDelay, period);
        return timer;
    }

    static /* synthetic */ Timer fixedRateTimer$default(String name, boolean daemon, long initialDelay, long period, Function1 action, int n, Object object) {
        if ((n & 1) != 0) {
            name = null;
        }
        if ((n & 2) != 0) {
            daemon = false;
        }
        if ((n & 4) != 0) {
            initialDelay = 0L;
        }
        Intrinsics.checkNotNullParameter(action, "action");
        Timer timer = TimersKt.timer(name, daemon);
        object = timer;
        TimerTask timerTask2 = new /* invalid duplicate definition of identical inner class */;
        ((Timer)object).scheduleAtFixedRate(timerTask2, initialDelay, period);
        return timer;
    }

    @InlineOnly
    private static final Timer fixedRateTimer(String name, boolean daemon, Date startAt, long period, Function1<? super TimerTask, Unit> action) {
        Timer timer;
        Intrinsics.checkNotNullParameter(startAt, "startAt");
        Intrinsics.checkNotNullParameter(action, "action");
        Timer timer2 = timer = TimersKt.timer(name, daemon);
        TimerTask timerTask2 = new /* invalid duplicate definition of identical inner class */;
        timer2.scheduleAtFixedRate(timerTask2, startAt, period);
        return timer;
    }

    static /* synthetic */ Timer fixedRateTimer$default(String name, boolean daemon, Date startAt, long period, Function1 action, int n, Object object) {
        if ((n & 1) != 0) {
            name = null;
        }
        if ((n & 2) != 0) {
            daemon = false;
        }
        Intrinsics.checkNotNullParameter(startAt, "startAt");
        Intrinsics.checkNotNullParameter(action, "action");
        Timer timer = TimersKt.timer(name, daemon);
        object = timer;
        TimerTask timerTask2 = new /* invalid duplicate definition of identical inner class */;
        ((Timer)object).scheduleAtFixedRate(timerTask2, startAt, period);
        return timer;
    }

    @InlineOnly
    private static final TimerTask timerTask(Function1<? super TimerTask, Unit> action) {
        Intrinsics.checkNotNullParameter(action, "action");
        return new /* invalid duplicate definition of identical inner class */;
    }
}

