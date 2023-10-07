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
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.time.Clock;
import kotlin.time.ClockMark;
import kotlin.time.Duration;
import kotlin.time.DurationKt;
import kotlin.time.ExperimentalTime;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\b'\u0018\u00002\u00020\u0001:\u0001\fB\u0011\u0012\n\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004\u00a2\u0006\u0002\u0010\u0005J\b\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u000bH$R\u0018\u0010\u0002\u001a\u00060\u0003j\u0002`\u0004X\u0084\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\r"}, d2={"Lkotlin/time/AbstractDoubleClock;", "Lkotlin/time/Clock;", "unit", "Ljava/util/concurrent/TimeUnit;", "Lkotlin/time/DurationUnit;", "(Ljava/util/concurrent/TimeUnit;)V", "getUnit", "()Ljava/util/concurrent/TimeUnit;", "markNow", "Lkotlin/time/ClockMark;", "read", "", "DoubleClockMark", "kotlin-stdlib"})
@SinceKotlin(version="1.3")
@ExperimentalTime
public abstract class AbstractDoubleClock
implements Clock {
    @NotNull
    private final TimeUnit unit;

    protected abstract double read();

    @Override
    @NotNull
    public ClockMark markNow() {
        return new DoubleClockMark(this.read(), this, Duration.Companion.getZERO(), null);
    }

    @NotNull
    protected final TimeUnit getUnit() {
        return this.unit;
    }

    public AbstractDoubleClock(@NotNull TimeUnit unit) {
        Intrinsics.checkParameterIsNotNull((Object)unit, "unit");
        this.unit = unit;
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u0002\u0018\u00002\u00020\u0001B \u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\bJ\u0010\u0010\n\u001a\u00020\u0007H\u0016\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000bJ\u001b\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u0007H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000e\u0010\u000fR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0013\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0010"}, d2={"Lkotlin/time/AbstractDoubleClock$DoubleClockMark;", "Lkotlin/time/ClockMark;", "startedAt", "", "clock", "Lkotlin/time/AbstractDoubleClock;", "offset", "Lkotlin/time/Duration;", "(DLkotlin/time/AbstractDoubleClock;DLkotlin/jvm/internal/DefaultConstructorMarker;)V", "D", "elapsedNow", "()D", "plus", "duration", "plus-LRDsOJo", "(D)Lkotlin/time/ClockMark;", "kotlin-stdlib"})
    private static final class DoubleClockMark
    extends ClockMark {
        private final double startedAt;
        private final AbstractDoubleClock clock;
        private final double offset;

        @Override
        public double elapsedNow() {
            return Duration.minus-LRDsOJo(DurationKt.toDuration(this.clock.read() - this.startedAt, this.clock.getUnit()), this.offset);
        }

        @Override
        @NotNull
        public ClockMark plus-LRDsOJo(double duration) {
            return new DoubleClockMark(this.startedAt, this.clock, Duration.plus-LRDsOJo(this.offset, duration), null);
        }

        private DoubleClockMark(double startedAt, AbstractDoubleClock clock, double offset) {
            this.startedAt = startedAt;
            this.clock = clock;
            this.offset = offset;
        }

        public /* synthetic */ DoubleClockMark(double startedAt, AbstractDoubleClock clock, double offset, DefaultConstructorMarker $constructor_marker) {
            this(startedAt, clock, offset);
        }
    }
}

