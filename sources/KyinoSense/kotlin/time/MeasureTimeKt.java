/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.time;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.time.Clock;
import kotlin.time.ClockMark;
import kotlin.time.ExperimentalTime;
import kotlin.time.MonoClock;
import kotlin.time.TimedValue;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a,\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0087\b\u00f8\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0002\u0010\u0005\u001a0\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\u0087\b\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u001a0\u0010\u0000\u001a\u00020\u0001*\u00020\t2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003H\u0087\b\u00f8\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0002\u0010\n\u001a4\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b*\u00020\t2\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u0002H\b0\u0003H\u0087\b\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u000b"}, d2={"measureTime", "Lkotlin/time/Duration;", "block", "Lkotlin/Function0;", "", "(Lkotlin/jvm/functions/Function0;)D", "measureTimedValue", "Lkotlin/time/TimedValue;", "T", "Lkotlin/time/Clock;", "(Lkotlin/time/Clock;Lkotlin/jvm/functions/Function0;)D", "kotlin-stdlib"})
public final class MeasureTimeKt {
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static final double measureTime(@NotNull Function0<Unit> block) {
        int $i$f$measureTime = 0;
        Intrinsics.checkParameterIsNotNull(block, "block");
        boolean bl = false;
        Clock $this$measureTime$iv = MonoClock.INSTANCE;
        boolean $i$f$measureTime2 = false;
        boolean bl2 = false;
        ClockMark mark$iv = $this$measureTime$iv.markNow();
        block.invoke();
        return mark$iv.elapsedNow();
    }

    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static final double measureTime(@NotNull Clock $this$measureTime, @NotNull Function0<Unit> block) {
        int $i$f$measureTime = 0;
        Intrinsics.checkParameterIsNotNull($this$measureTime, "$this$measureTime");
        Intrinsics.checkParameterIsNotNull(block, "block");
        boolean bl = false;
        ClockMark mark = $this$measureTime.markNow();
        block.invoke();
        return mark.elapsedNow();
    }

    @SinceKotlin(version="1.3")
    @ExperimentalTime
    @NotNull
    public static final <T> TimedValue<T> measureTimedValue(@NotNull Function0<? extends T> block) {
        int $i$f$measureTimedValue = 0;
        Intrinsics.checkParameterIsNotNull(block, "block");
        boolean bl = false;
        Clock $this$measureTimedValue$iv = MonoClock.INSTANCE;
        boolean $i$f$measureTimedValue2 = false;
        boolean bl2 = false;
        ClockMark mark$iv = $this$measureTimedValue$iv.markNow();
        T result$iv = block.invoke();
        return new TimedValue(result$iv, mark$iv.elapsedNow(), null);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalTime
    @NotNull
    public static final <T> TimedValue<T> measureTimedValue(@NotNull Clock $this$measureTimedValue, @NotNull Function0<? extends T> block) {
        int $i$f$measureTimedValue = 0;
        Intrinsics.checkParameterIsNotNull($this$measureTimedValue, "$this$measureTimedValue");
        Intrinsics.checkParameterIsNotNull(block, "block");
        boolean bl = false;
        ClockMark mark = $this$measureTimedValue.markNow();
        T result = block.invoke();
        return new TimedValue(result, mark.elapsedNow(), null);
    }
}

