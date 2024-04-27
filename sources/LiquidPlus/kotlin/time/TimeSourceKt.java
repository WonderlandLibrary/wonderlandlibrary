/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import kotlin.time.ExperimentalTime;
import kotlin.time.TimeMark;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000\u0016\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\n\u001a\u001d\u0010\u0004\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0007"}, d2={"compareTo", "", "Lkotlin/time/TimeMark;", "other", "minus", "Lkotlin/time/Duration;", "(Lkotlin/time/TimeMark;Lkotlin/time/TimeMark;)J", "kotlin-stdlib"})
public final class TimeSourceKt {
    @Deprecated(message="Subtracting one TimeMark from another is not a well defined operation because these time marks could have been obtained from the different time sources.", level=DeprecationLevel.ERROR)
    @ExperimentalTime
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final long minus(TimeMark $this$minus, TimeMark other) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        throw new Error("Operation is disallowed.");
    }

    @Deprecated(message="Comparing one TimeMark to another is not a well defined operation because these time marks could have been obtained from the different time sources.", level=DeprecationLevel.ERROR)
    @ExperimentalTime
    @SinceKotlin(version="1.3")
    @InlineOnly
    private static final int compareTo(TimeMark $this$compareTo, TimeMark other) {
        Intrinsics.checkNotNullParameter($this$compareTo, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        throw new Error("Operation is disallowed.");
    }
}

