/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.time;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.time.ClockMark;
import kotlin.time.Duration;
import kotlin.time.ExperimentalTime;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\b\u0003\u0018\u00002\u00020\u0001B\u0018\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0005J\u0010\u0010\u000b\u001a\u00020\u0004H\u0016\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J\u001b\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u0004H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000e\u0010\u000fR\u0016\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000\u00a2\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0010"}, d2={"Lkotlin/time/AdjustedClockMark;", "Lkotlin/time/ClockMark;", "mark", "adjustment", "Lkotlin/time/Duration;", "(Lkotlin/time/ClockMark;DLkotlin/jvm/internal/DefaultConstructorMarker;)V", "getAdjustment", "()D", "D", "getMark", "()Lkotlin/time/ClockMark;", "elapsedNow", "plus", "duration", "plus-LRDsOJo", "(D)Lkotlin/time/ClockMark;", "kotlin-stdlib"})
@ExperimentalTime
final class AdjustedClockMark
extends ClockMark {
    @NotNull
    private final ClockMark mark;
    private final double adjustment;

    @Override
    public double elapsedNow() {
        return Duration.minus-LRDsOJo(this.mark.elapsedNow(), this.adjustment);
    }

    @Override
    @NotNull
    public ClockMark plus-LRDsOJo(double duration) {
        return new AdjustedClockMark(this.mark, Duration.plus-LRDsOJo(this.adjustment, duration), null);
    }

    @NotNull
    public final ClockMark getMark() {
        return this.mark;
    }

    public final double getAdjustment() {
        return this.adjustment;
    }

    private AdjustedClockMark(ClockMark mark, double adjustment) {
        this.mark = mark;
        this.adjustment = adjustment;
    }

    public /* synthetic */ AdjustedClockMark(ClockMark mark, double adjustment, DefaultConstructorMarker $constructor_marker) {
        this(mark, adjustment);
    }
}

