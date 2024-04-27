/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.time;

import java.util.Collection;
import java.util.Iterator;
import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.WasExperimental;
import kotlin.collections.IntIterator;
import kotlin.internal.InlineOnly;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import kotlin.time.Duration;
import kotlin.time.DurationUnit;
import kotlin.time.DurationUnitKt;
import kotlin.time.ExperimentalTime;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000>\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\b*\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\f\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a \u0010#\u001a\u00020\u00072\u0006\u0010$\u001a\u00020\u00012\u0006\u0010%\u001a\u00020\u0005H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010&\u001a\u0018\u0010'\u001a\u00020\u00072\u0006\u0010(\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010\u001a\u0018\u0010)\u001a\u00020\u00072\u0006\u0010*\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010\u001a\u0018\u0010+\u001a\u00020\u00072\u0006\u0010,\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010\u001a\u0018\u0010-\u001a\u00020\u00072\u0006\u0010.\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010\u001a\u0010\u0010/\u001a\u00020\u00012\u0006\u0010*\u001a\u00020\u0001H\u0002\u001a\u0010\u00100\u001a\u00020\u00012\u0006\u0010.\u001a\u00020\u0001H\u0002\u001a \u00101\u001a\u00020\u00072\u0006\u00102\u001a\u0002032\u0006\u00104\u001a\u000205H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0002\u00106\u001a\u0010\u00107\u001a\u00020\u00012\u0006\u00102\u001a\u000203H\u0002\u001a)\u00108\u001a\u00020\u0005*\u0002032\u0006\u00109\u001a\u00020\u00052\u0012\u0010:\u001a\u000e\u0012\u0004\u0012\u00020<\u0012\u0004\u0012\u0002050;H\u0082\b\u001a)\u0010=\u001a\u000203*\u0002032\u0006\u00109\u001a\u00020\u00052\u0012\u0010:\u001a\u000e\u0012\u0004\u0012\u00020<\u0012\u0004\u0012\u0002050;H\u0082\b\u001a\u001f\u0010>\u001a\u00020\u0007*\u00020\b2\u0006\u0010?\u001a\u00020\u0007H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b@\u0010A\u001a\u001f\u0010>\u001a\u00020\u0007*\u00020\u00052\u0006\u0010?\u001a\u00020\u0007H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bB\u0010C\u001a\u001c\u0010D\u001a\u00020\u0007*\u00020\b2\u0006\u0010E\u001a\u00020FH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010G\u001a\u001c\u0010D\u001a\u00020\u0007*\u00020\u00052\u0006\u0010E\u001a\u00020FH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010H\u001a\u001c\u0010D\u001a\u00020\u0007*\u00020\u00012\u0006\u0010E\u001a\u00020FH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010I\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0001X\u0080T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0003\u001a\u00020\u0001X\u0082T\u00a2\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0080T\u00a2\u0006\u0002\n\u0000\"!\u0010\u0006\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\t\u0010\n\u001a\u0004\b\u000b\u0010\f\"!\u0010\u0006\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\t\u0010\r\u001a\u0004\b\u000b\u0010\u000e\"!\u0010\u0006\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\t\u0010\u000f\u001a\u0004\b\u000b\u0010\u0010\"!\u0010\u0011\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0012\u0010\n\u001a\u0004\b\u0013\u0010\f\"!\u0010\u0011\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0012\u0010\r\u001a\u0004\b\u0013\u0010\u000e\"!\u0010\u0011\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0012\u0010\u000f\u001a\u0004\b\u0013\u0010\u0010\"!\u0010\u0014\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0015\u0010\n\u001a\u0004\b\u0016\u0010\f\"!\u0010\u0014\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0015\u0010\r\u001a\u0004\b\u0016\u0010\u000e\"!\u0010\u0014\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0015\u0010\u000f\u001a\u0004\b\u0016\u0010\u0010\"!\u0010\u0017\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0018\u0010\n\u001a\u0004\b\u0019\u0010\f\"!\u0010\u0017\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0018\u0010\r\u001a\u0004\b\u0019\u0010\u000e\"!\u0010\u0017\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u0018\u0010\u000f\u001a\u0004\b\u0019\u0010\u0010\"!\u0010\u001a\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u001b\u0010\n\u001a\u0004\b\u001c\u0010\f\"!\u0010\u001a\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u001b\u0010\r\u001a\u0004\b\u001c\u0010\u000e\"!\u0010\u001a\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u001b\u0010\u000f\u001a\u0004\b\u001c\u0010\u0010\"!\u0010\u001d\u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u001e\u0010\n\u001a\u0004\b\u001f\u0010\f\"!\u0010\u001d\u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u001e\u0010\r\u001a\u0004\b\u001f\u0010\u000e\"!\u0010\u001d\u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b\u001e\u0010\u000f\u001a\u0004\b\u001f\u0010\u0010\"!\u0010 \u001a\u00020\u0007*\u00020\b8FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b!\u0010\n\u001a\u0004\b\"\u0010\f\"!\u0010 \u001a\u00020\u0007*\u00020\u00058FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b!\u0010\r\u001a\u0004\b\"\u0010\u000e\"!\u0010 \u001a\u00020\u0007*\u00020\u00018FX\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\f\u0012\u0004\b!\u0010\u000f\u001a\u0004\b\"\u0010\u0010\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006J"}, d2={"MAX_MILLIS", "", "MAX_NANOS", "MAX_NANOS_IN_MILLIS", "NANOS_IN_MILLIS", "", "days", "Lkotlin/time/Duration;", "", "getDays$annotations", "(D)V", "getDays", "(D)J", "(I)V", "(I)J", "(J)V", "(J)J", "hours", "getHours$annotations", "getHours", "microseconds", "getMicroseconds$annotations", "getMicroseconds", "milliseconds", "getMilliseconds$annotations", "getMilliseconds", "minutes", "getMinutes$annotations", "getMinutes", "nanoseconds", "getNanoseconds$annotations", "getNanoseconds", "seconds", "getSeconds$annotations", "getSeconds", "durationOf", "normalValue", "unitDiscriminator", "(JI)J", "durationOfMillis", "normalMillis", "durationOfMillisNormalized", "millis", "durationOfNanos", "normalNanos", "durationOfNanosNormalized", "nanos", "millisToNanos", "nanosToMillis", "parseDuration", "value", "", "strictIso", "", "(Ljava/lang/String;Z)J", "parseOverLongIsoComponent", "skipWhile", "startIndex", "predicate", "Lkotlin/Function1;", "", "substringWhile", "times", "duration", "times-kIfJnKk", "(DJ)J", "times-mvk6XK0", "(IJ)J", "toDuration", "unit", "Lkotlin/time/DurationUnit;", "(DLkotlin/time/DurationUnit;)J", "(ILkotlin/time/DurationUnit;)J", "(JLkotlin/time/DurationUnit;)J", "kotlin-stdlib"})
public final class DurationKt {
    public static final int NANOS_IN_MILLIS = 1000000;
    public static final long MAX_NANOS = 4611686018426999999L;
    public static final long MAX_MILLIS = 0x3FFFFFFFFFFFFFFFL;
    private static final long MAX_NANOS_IN_MILLIS = 4611686018426L;

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalTime.class})
    public static final long toDuration(int $this$toDuration, @NotNull DurationUnit unit) {
        Intrinsics.checkNotNullParameter((Object)unit, "unit");
        return unit.compareTo((Enum)DurationUnit.SECONDS) <= 0 ? DurationKt.durationOfNanos(DurationUnitKt.convertDurationUnitOverflow($this$toDuration, unit, DurationUnit.NANOSECONDS)) : DurationKt.toDuration((long)$this$toDuration, unit);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalTime.class})
    public static final long toDuration(long $this$toDuration, @NotNull DurationUnit unit) {
        Intrinsics.checkNotNullParameter((Object)unit, "unit");
        long maxNsInUnit = DurationUnitKt.convertDurationUnitOverflow(4611686018426999999L, DurationUnit.NANOSECONDS, unit);
        boolean bl = -maxNsInUnit <= $this$toDuration ? $this$toDuration <= maxNsInUnit : false;
        if (bl) {
            return DurationKt.durationOfNanos(DurationUnitKt.convertDurationUnitOverflow($this$toDuration, unit, DurationUnit.NANOSECONDS));
        }
        long millis = DurationUnitKt.convertDurationUnit($this$toDuration, unit, DurationUnit.MILLISECONDS);
        return DurationKt.durationOfMillis(RangesKt.coerceIn(millis, -4611686018427387903L, 0x3FFFFFFFFFFFFFFFL));
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalTime.class})
    public static final long toDuration(double $this$toDuration, @NotNull DurationUnit unit) {
        long l;
        boolean bl;
        Intrinsics.checkNotNullParameter((Object)unit, "unit");
        double valueInNs = DurationUnitKt.convertDurationUnit($this$toDuration, unit, DurationUnit.NANOSECONDS);
        boolean bl2 = bl = !Double.isNaN(valueInNs);
        if (!bl) {
            boolean bl3 = false;
            String string = "Duration value cannot be NaN.";
            throw new IllegalArgumentException(string.toString());
        }
        long nanos = MathKt.roundToLong(valueInNs);
        boolean bl4 = -4611686018426999999L <= nanos ? nanos < 4611686018427000000L : false;
        if (bl4) {
            l = DurationKt.durationOfNanos(nanos);
        } else {
            long millis = MathKt.roundToLong(DurationUnitKt.convertDurationUnit($this$toDuration, unit, DurationUnit.MILLISECONDS));
            l = DurationKt.durationOfMillisNormalized(millis);
        }
        return l;
    }

    public static final long getNanoseconds(int $this$nanoseconds) {
        return DurationKt.toDuration($this$nanoseconds, DurationUnit.NANOSECONDS);
    }

    @Deprecated(message="Use 'Int.nanoseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.nanoseconds", imports={"kotlin.time.Duration.Companion.nanoseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getNanoseconds$annotations(int n) {
    }

    public static final long getNanoseconds(long $this$nanoseconds) {
        return DurationKt.toDuration($this$nanoseconds, DurationUnit.NANOSECONDS);
    }

    @Deprecated(message="Use 'Long.nanoseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.nanoseconds", imports={"kotlin.time.Duration.Companion.nanoseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getNanoseconds$annotations(long l) {
    }

    public static final long getNanoseconds(double $this$nanoseconds) {
        return DurationKt.toDuration($this$nanoseconds, DurationUnit.NANOSECONDS);
    }

    @Deprecated(message="Use 'Double.nanoseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.nanoseconds", imports={"kotlin.time.Duration.Companion.nanoseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getNanoseconds$annotations(double d) {
    }

    public static final long getMicroseconds(int $this$microseconds) {
        return DurationKt.toDuration($this$microseconds, DurationUnit.MICROSECONDS);
    }

    @Deprecated(message="Use 'Int.microseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.microseconds", imports={"kotlin.time.Duration.Companion.microseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMicroseconds$annotations(int n) {
    }

    public static final long getMicroseconds(long $this$microseconds) {
        return DurationKt.toDuration($this$microseconds, DurationUnit.MICROSECONDS);
    }

    @Deprecated(message="Use 'Long.microseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.microseconds", imports={"kotlin.time.Duration.Companion.microseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMicroseconds$annotations(long l) {
    }

    public static final long getMicroseconds(double $this$microseconds) {
        return DurationKt.toDuration($this$microseconds, DurationUnit.MICROSECONDS);
    }

    @Deprecated(message="Use 'Double.microseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.microseconds", imports={"kotlin.time.Duration.Companion.microseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMicroseconds$annotations(double d) {
    }

    public static final long getMilliseconds(int $this$milliseconds) {
        return DurationKt.toDuration($this$milliseconds, DurationUnit.MILLISECONDS);
    }

    @Deprecated(message="Use 'Int.milliseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.milliseconds", imports={"kotlin.time.Duration.Companion.milliseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMilliseconds$annotations(int n) {
    }

    public static final long getMilliseconds(long $this$milliseconds) {
        return DurationKt.toDuration($this$milliseconds, DurationUnit.MILLISECONDS);
    }

    @Deprecated(message="Use 'Long.milliseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.milliseconds", imports={"kotlin.time.Duration.Companion.milliseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMilliseconds$annotations(long l) {
    }

    public static final long getMilliseconds(double $this$milliseconds) {
        return DurationKt.toDuration($this$milliseconds, DurationUnit.MILLISECONDS);
    }

    @Deprecated(message="Use 'Double.milliseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.milliseconds", imports={"kotlin.time.Duration.Companion.milliseconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMilliseconds$annotations(double d) {
    }

    public static final long getSeconds(int $this$seconds) {
        return DurationKt.toDuration($this$seconds, DurationUnit.SECONDS);
    }

    @Deprecated(message="Use 'Int.seconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.seconds", imports={"kotlin.time.Duration.Companion.seconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getSeconds$annotations(int n) {
    }

    public static final long getSeconds(long $this$seconds) {
        return DurationKt.toDuration($this$seconds, DurationUnit.SECONDS);
    }

    @Deprecated(message="Use 'Long.seconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.seconds", imports={"kotlin.time.Duration.Companion.seconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getSeconds$annotations(long l) {
    }

    public static final long getSeconds(double $this$seconds) {
        return DurationKt.toDuration($this$seconds, DurationUnit.SECONDS);
    }

    @Deprecated(message="Use 'Double.seconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.seconds", imports={"kotlin.time.Duration.Companion.seconds"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getSeconds$annotations(double d) {
    }

    public static final long getMinutes(int $this$minutes) {
        return DurationKt.toDuration($this$minutes, DurationUnit.MINUTES);
    }

    @Deprecated(message="Use 'Int.minutes' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.minutes", imports={"kotlin.time.Duration.Companion.minutes"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMinutes$annotations(int n) {
    }

    public static final long getMinutes(long $this$minutes) {
        return DurationKt.toDuration($this$minutes, DurationUnit.MINUTES);
    }

    @Deprecated(message="Use 'Long.minutes' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.minutes", imports={"kotlin.time.Duration.Companion.minutes"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMinutes$annotations(long l) {
    }

    public static final long getMinutes(double $this$minutes) {
        return DurationKt.toDuration($this$minutes, DurationUnit.MINUTES);
    }

    @Deprecated(message="Use 'Double.minutes' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.minutes", imports={"kotlin.time.Duration.Companion.minutes"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getMinutes$annotations(double d) {
    }

    public static final long getHours(int $this$hours) {
        return DurationKt.toDuration($this$hours, DurationUnit.HOURS);
    }

    @Deprecated(message="Use 'Int.hours' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.hours", imports={"kotlin.time.Duration.Companion.hours"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getHours$annotations(int n) {
    }

    public static final long getHours(long $this$hours) {
        return DurationKt.toDuration($this$hours, DurationUnit.HOURS);
    }

    @Deprecated(message="Use 'Long.hours' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.hours", imports={"kotlin.time.Duration.Companion.hours"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getHours$annotations(long l) {
    }

    public static final long getHours(double $this$hours) {
        return DurationKt.toDuration($this$hours, DurationUnit.HOURS);
    }

    @Deprecated(message="Use 'Double.hours' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.hours", imports={"kotlin.time.Duration.Companion.hours"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getHours$annotations(double d) {
    }

    public static final long getDays(int $this$days) {
        return DurationKt.toDuration($this$days, DurationUnit.DAYS);
    }

    @Deprecated(message="Use 'Int.days' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.days", imports={"kotlin.time.Duration.Companion.days"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getDays$annotations(int n) {
    }

    public static final long getDays(long $this$days) {
        return DurationKt.toDuration($this$days, DurationUnit.DAYS);
    }

    @Deprecated(message="Use 'Long.days' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.days", imports={"kotlin.time.Duration.Companion.days"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getDays$annotations(long l) {
    }

    public static final long getDays(double $this$days) {
        return DurationKt.toDuration($this$days, DurationUnit.DAYS);
    }

    @Deprecated(message="Use 'Double.days' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="this.days", imports={"kotlin.time.Duration.Companion.days"}))
    @DeprecatedSinceKotlin(warningSince="1.5")
    @SinceKotlin(version="1.3")
    @ExperimentalTime
    public static /* synthetic */ void getDays$annotations(double d) {
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalTime.class})
    @InlineOnly
    private static final long times-mvk6XK0(int $this$times, long duration) {
        return Duration.times-UwyO8pc(duration, $this$times);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalTime.class})
    @InlineOnly
    private static final long times-kIfJnKk(double $this$times, long duration) {
        return Duration.times-UwyO8pc(duration, $this$times);
    }

    private static final long parseDuration(String value, boolean strictIso) {
        int length = value.length();
        if (length == 0) {
            throw new IllegalArgumentException("The string is empty");
        }
        int index = 0;
        long result = Duration.Companion.getZERO-UwyO8pc();
        String infinityString = "Infinity";
        char c = value.charAt(index);
        if (c == '+' ? true : c == '-') {
            int n = index;
            index = n + 1;
        }
        boolean hasSign = index > 0;
        boolean isNegative = hasSign && StringsKt.startsWith$default((CharSequence)value, '-', false, 2, null);
        if (length <= index) {
            throw new IllegalArgumentException("No components");
        }
        if (value.charAt(index) == 'P') {
            if (++index == length) {
                throw new IllegalArgumentException();
            }
            String nonDigitSymbols = "+-.";
            boolean isTimeComponent = false;
            DurationUnit prevUnit = null;
            while (index < length) {
                if (value.charAt(index) == 'T') {
                    if (isTimeComponent || ++index == length) {
                        throw new IllegalArgumentException();
                    }
                    isTimeComponent = true;
                    continue;
                }
                String $this$substringWhile$iv = value;
                boolean $i$f$substringWhile322 = false;
                String string = $this$substringWhile$iv;
                String $this$skipWhile$iv$iv22 = $this$substringWhile$iv;
                boolean $i$f$skipWhile22 = false;
                int i$iv$iv2 = index;
                while (i$iv$iv2 < $this$skipWhile$iv$iv22.length()) {
                    int it = $this$skipWhile$iv$iv22.charAt(i$iv$iv2);
                    boolean bl = false;
                    boolean bl2 = 48 <= it ? it < 58 : false;
                    if (!(bl2 || StringsKt.contains$default((CharSequence)nonDigitSymbols, (char)it, false, 2, null))) break;
                    it = i$iv$iv2;
                    i$iv$iv2 = it + 1;
                }
                int $this$skipWhile$iv$iv22 = i$iv$iv2;
                String $i$f$skipWhile22 = string.substring(index, $this$skipWhile$iv$iv22);
                Intrinsics.checkNotNullExpressionValue($i$f$skipWhile22, "this as java.lang.String\u2026ing(startIndex, endIndex)");
                String component = $i$f$skipWhile22;
                if (((CharSequence)component).length() == 0) {
                    throw new IllegalArgumentException();
                }
                CharSequence $i$f$substringWhile322 = value;
                if ((index += component.length()) < 0 || index > StringsKt.getLastIndex($i$f$substringWhile322)) {
                    int it = index;
                    boolean bl = false;
                    throw new IllegalArgumentException(Intrinsics.stringPlus("Missing unit for value ", component));
                }
                char unitChar = $i$f$substringWhile322.charAt(index);
                int $i$f$substringWhile322 = index;
                index = $i$f$substringWhile322 + 1;
                DurationUnit unit = DurationUnitKt.durationUnitByIsoChar(unitChar, isTimeComponent);
                if (prevUnit != null && prevUnit.compareTo((Enum)unit) <= 0) {
                    throw new IllegalArgumentException("Unexpected order of duration components");
                }
                prevUnit = unit;
                int dotIndex = StringsKt.indexOf$default((CharSequence)component, '.', 0, false, 6, null);
                if (unit == DurationUnit.SECONDS && dotIndex > 0) {
                    $i$f$skipWhile22 = component;
                    i$iv$iv2 = 0;
                    String it = $i$f$skipWhile22.substring(i$iv$iv2, dotIndex);
                    Intrinsics.checkNotNullExpressionValue(it, "this as java.lang.String\u2026ing(startIndex, endIndex)");
                    String whole = it;
                    result = Duration.plus-LRDsOJo(result, DurationKt.toDuration(DurationKt.parseOverLongIsoComponent(whole), unit));
                    $i$f$skipWhile22 = component;
                    String i$iv$iv2 = $i$f$skipWhile22.substring(dotIndex);
                    Intrinsics.checkNotNullExpressionValue(i$iv$iv2, "this as java.lang.String).substring(startIndex)");
                    result = Duration.plus-LRDsOJo(result, DurationKt.toDuration(Double.parseDouble(i$iv$iv2), unit));
                    continue;
                }
                result = Duration.plus-LRDsOJo(result, DurationKt.toDuration(DurationKt.parseOverLongIsoComponent(component), unit));
            }
        } else {
            if (strictIso) {
                throw new IllegalArgumentException();
            }
            if (StringsKt.regionMatches(value, index, infinityString, 0, Math.max(length - index, infinityString.length()), true)) {
                result = Duration.Companion.getINFINITE-UwyO8pc();
            } else {
                boolean allowSpaces;
                DurationUnit prevUnit = null;
                boolean afterFirst = false;
                boolean bl = allowSpaces = !hasSign;
                if (hasSign && value.charAt(index) == '(' && StringsKt.last(value) == ')') {
                    allowSpaces = true;
                    if (++index == --length) {
                        throw new IllegalArgumentException("No components");
                    }
                }
                while (index < length) {
                    if (afterFirst && allowSpaces) {
                        String $this$skipWhile$iv = value;
                        boolean $i$f$skipWhile = false;
                        int i$iv = index;
                        while (i$iv < $this$skipWhile$iv.length()) {
                            int it = $this$skipWhile$iv.charAt(i$iv);
                            boolean bl3 = false;
                            if (!(it == 32)) break;
                            it = i$iv;
                            i$iv = it + 1;
                        }
                        index = i$iv;
                    }
                    afterFirst = true;
                    String $this$substringWhile$iv = value;
                    boolean $i$f$substringWhile = false;
                    String it = $this$substringWhile$iv;
                    String $this$skipWhile$iv$iv422 = $this$substringWhile$iv;
                    boolean $i$f$skipWhile32 = false;
                    int i$iv$iv = index;
                    while (i$iv$iv < $this$skipWhile$iv$iv422.length()) {
                        int it2 = $this$skipWhile$iv$iv422.charAt(i$iv$iv);
                        boolean bl4 = false;
                        boolean bl5 = 48 <= it2 ? it2 < 58 : false;
                        if (!(bl5 || it2 == 46)) break;
                        it2 = i$iv$iv;
                        i$iv$iv = it2 + 1;
                    }
                    int $this$skipWhile$iv$iv422 = i$iv$iv;
                    String $i$f$skipWhile32 = it.substring(index, $this$skipWhile$iv$iv422);
                    Intrinsics.checkNotNullExpressionValue($i$f$skipWhile32, "this as java.lang.String\u2026ing(startIndex, endIndex)");
                    String component = $i$f$skipWhile32;
                    if (((CharSequence)component).length() == 0) {
                        throw new IllegalArgumentException();
                    }
                    index += component.length();
                    String $this$substringWhile$iv2 = value;
                    boolean $i$f$substringWhile2 = false;
                    String $this$skipWhile$iv$iv422 = $this$substringWhile$iv2;
                    String $this$skipWhile$iv$iv = $this$substringWhile$iv2;
                    boolean $i$f$skipWhile = false;
                    int i$iv$iv3 = index;
                    while (i$iv$iv3 < $this$skipWhile$iv$iv.length()) {
                        char it3 = $this$skipWhile$iv$iv.charAt(i$iv$iv3);
                        boolean bl6 = false;
                        boolean bl7 = 'a' <= it3 ? it3 < '{' : false;
                        if (!bl7) break;
                        int n = i$iv$iv3;
                        i$iv$iv3 = n + 1;
                    }
                    int n = i$iv$iv3;
                    String string = $this$skipWhile$iv$iv422.substring(index, n);
                    Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String\u2026ing(startIndex, endIndex)");
                    String unitName = string;
                    index += unitName.length();
                    DurationUnit unit = DurationUnitKt.durationUnitByShortName(unitName);
                    if (prevUnit != null && prevUnit.compareTo((Enum)unit) <= 0) {
                        throw new IllegalArgumentException("Unexpected order of duration components");
                    }
                    prevUnit = unit;
                    int dotIndex = StringsKt.indexOf$default((CharSequence)component, '.', 0, false, 6, null);
                    if (dotIndex > 0) {
                        String string2 = component;
                        int n2 = 0;
                        String string3 = string2.substring(n2, dotIndex);
                        Intrinsics.checkNotNullExpressionValue(string3, "this as java.lang.String\u2026ing(startIndex, endIndex)");
                        String whole = string3;
                        result = Duration.plus-LRDsOJo(result, DurationKt.toDuration(Long.parseLong(whole), unit));
                        string2 = component;
                        string = string2.substring(dotIndex);
                        Intrinsics.checkNotNullExpressionValue(string, "this as java.lang.String).substring(startIndex)");
                        result = Duration.plus-LRDsOJo(result, DurationKt.toDuration(Double.parseDouble(string), unit));
                        if (index >= length) continue;
                        throw new IllegalArgumentException("Fractional component must be last");
                    }
                    result = Duration.plus-LRDsOJo(result, DurationKt.toDuration(Long.parseLong(component), unit));
                }
            }
        }
        return isNegative ? Duration.unaryMinus-UwyO8pc(result) : result;
    }

    private static final long parseOverLongIsoComponent(String value) {
        int length = value.length();
        int startIndex = 0;
        if (length > 0 && StringsKt.contains$default((CharSequence)"+-", value.charAt(0), false, 2, null)) {
            int n = startIndex;
            startIndex = n + 1;
        }
        if (length - startIndex > 16) {
            boolean bl;
            block6: {
                Iterable $this$all$iv = new IntRange(startIndex, StringsKt.getLastIndex(value));
                boolean $i$f$all = false;
                if ($this$all$iv instanceof Collection && ((Collection)$this$all$iv).isEmpty()) {
                    bl = true;
                } else {
                    Iterator iterator2 = $this$all$iv.iterator();
                    while (iterator2.hasNext()) {
                        int element$iv;
                        int it = element$iv = ((IntIterator)iterator2).nextInt();
                        boolean bl2 = false;
                        char c = value.charAt(it);
                        if ('0' <= c ? c < ':' : false) continue;
                        bl = false;
                        break block6;
                    }
                    bl = true;
                }
            }
            if (bl) {
                return value.charAt(0) == '-' ? Long.MIN_VALUE : Long.MAX_VALUE;
            }
        }
        return StringsKt.startsWith$default(value, "+", false, 2, null) ? Long.parseLong(StringsKt.drop(value, 1)) : Long.parseLong(value);
    }

    private static final String substringWhile(String $this$substringWhile, int startIndex, Function1<? super Character, Boolean> predicate) {
        boolean $i$f$substringWhile = false;
        String string = $this$substringWhile;
        String $this$skipWhile$iv = $this$substringWhile;
        boolean $i$f$skipWhile = false;
        int i$iv = startIndex;
        while (i$iv < $this$skipWhile$iv.length() && predicate.invoke(Character.valueOf($this$skipWhile$iv.charAt(i$iv))).booleanValue()) {
            int n = i$iv;
            i$iv = n + 1;
        }
        int n = i$iv;
        String string2 = string.substring(startIndex, n);
        Intrinsics.checkNotNullExpressionValue(string2, "this as java.lang.String\u2026ing(startIndex, endIndex)");
        return string2;
    }

    private static final int skipWhile(String $this$skipWhile, int startIndex, Function1<? super Character, Boolean> predicate) {
        boolean $i$f$skipWhile = false;
        int i = startIndex;
        while (i < $this$skipWhile.length() && predicate.invoke(Character.valueOf($this$skipWhile.charAt(i))).booleanValue()) {
            int n = i;
            i = n + 1;
        }
        return i;
    }

    private static final long nanosToMillis(long nanos) {
        return nanos / (long)1000000;
    }

    private static final long millisToNanos(long millis) {
        return millis * (long)1000000;
    }

    private static final long durationOfNanos(long normalNanos) {
        return Duration.constructor-impl(normalNanos << 1);
    }

    private static final long durationOfMillis(long normalMillis) {
        return Duration.constructor-impl((normalMillis << 1) + 1L);
    }

    private static final long durationOf(long normalValue, int unitDiscriminator) {
        return Duration.constructor-impl((normalValue << 1) + (long)unitDiscriminator);
    }

    private static final long durationOfNanosNormalized(long nanos) {
        return (-4611686018426999999L <= nanos ? nanos < 4611686018427000000L : false) ? DurationKt.durationOfNanos(nanos) : DurationKt.durationOfMillis(DurationKt.nanosToMillis(nanos));
    }

    private static final long durationOfMillisNormalized(long millis) {
        return (-4611686018426L <= millis ? millis < 4611686018427L : false) ? DurationKt.durationOfNanos(DurationKt.millisToNanos(millis)) : DurationKt.durationOfMillis(RangesKt.coerceIn(millis, -4611686018427387903L, 0x3FFFFFFFFFFFFFFFL));
    }

    public static final /* synthetic */ long access$parseDuration(String value, boolean strictIso) {
        return DurationKt.parseDuration(value, strictIso);
    }

    public static final /* synthetic */ long access$durationOf(long normalValue, int unitDiscriminator) {
        return DurationKt.durationOf(normalValue, unitDiscriminator);
    }

    public static final /* synthetic */ long access$durationOfNanosNormalized(long nanos) {
        return DurationKt.durationOfNanosNormalized(nanos);
    }

    public static final /* synthetic */ long access$durationOfMillisNormalized(long millis) {
        return DurationKt.durationOfMillisNormalized(millis);
    }

    public static final /* synthetic */ long access$nanosToMillis(long nanos) {
        return DurationKt.nanosToMillis(nanos);
    }

    public static final /* synthetic */ long access$millisToNanos(long millis) {
        return DurationKt.millisToNanos(millis);
    }

    public static final /* synthetic */ long access$durationOfNanos(long normalNanos) {
        return DurationKt.durationOfNanos(normalNanos);
    }

    public static final /* synthetic */ long access$durationOfMillis(long normalMillis) {
        return DurationKt.durationOfMillis(normalMillis);
    }
}

