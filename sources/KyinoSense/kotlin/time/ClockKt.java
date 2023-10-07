/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.time;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import kotlin.time.ClockMark;
import kotlin.time.ExperimentalTime;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000\u0016\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\n\u001a\u001d\u0010\u0004\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0007"}, d2={"compareTo", "", "Lkotlin/time/ClockMark;", "other", "minus", "Lkotlin/time/Duration;", "(Lkotlin/time/ClockMark;Lkotlin/time/ClockMark;)D", "kotlin-stdlib"})
public final class ClockKt {
    @Deprecated(message="Subtracting one ClockMark from another is not a well defined operation because these clock marks could have been obtained from the different clocks.", level=DeprecationLevel.ERROR)
    @ExperimentalTime
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final double minus(@NotNull ClockMark $this$minus, ClockMark other) {
        int $i$f$minus = 0;
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        throw (Throwable)new Error("Operation is disallowed.");
    }

    @Deprecated(message="Comparing one ClockMark to another is not a well defined operation because these clock marks could have been obtained from the different clocks.", level=DeprecationLevel.ERROR)
    @ExperimentalTime
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final int compareTo(@NotNull ClockMark $this$compareTo, ClockMark other) {
        int $i$f$compareTo = 0;
        Intrinsics.checkParameterIsNotNull($this$compareTo, "$this$compareTo");
        throw (Throwable)new Error("Operation is disallowed.");
    }
}

