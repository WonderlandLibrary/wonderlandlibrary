/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.time;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.time.AdjustedClockMark;
import kotlin.time.Duration;
import kotlin.time.ExperimentalTime;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\b'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u0004H&\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0005J\u0006\u0010\u0006\u001a\u00020\u0007J\u0006\u0010\b\u001a\u00020\u0007J\u001b\u0010\t\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0004H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\fJ\u001b\u0010\r\u001a\u00020\u00002\u0006\u0010\n\u001a\u00020\u0004H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000e\u0010\f\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u000f"}, d2={"Lkotlin/time/ClockMark;", "", "()V", "elapsedNow", "Lkotlin/time/Duration;", "()D", "hasNotPassedNow", "", "hasPassedNow", "minus", "duration", "minus-LRDsOJo", "(D)Lkotlin/time/ClockMark;", "plus", "plus-LRDsOJo", "kotlin-stdlib"})
@SinceKotlin(version="1.3")
@ExperimentalTime
public abstract class ClockMark {
    public abstract double elapsedNow();

    @NotNull
    public ClockMark plus-LRDsOJo(double duration) {
        return new AdjustedClockMark(this, duration, null);
    }

    @NotNull
    public ClockMark minus-LRDsOJo(double duration) {
        return this.plus-LRDsOJo(Duration.unaryMinus-impl(duration));
    }

    public final boolean hasPassedNow() {
        return !Duration.isNegative-impl(this.elapsedNow());
    }

    public final boolean hasNotPassedNow() {
        return Duration.isNegative-impl(this.elapsedNow());
    }
}

