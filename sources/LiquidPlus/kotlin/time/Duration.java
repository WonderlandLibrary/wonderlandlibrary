/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.time;

import kotlin.Deprecated;
import kotlin.DeprecatedSinceKotlin;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.WasExperimental;
import kotlin.comparisons.ComparisonsKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmInline;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.functions.Function4;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;
import kotlin.ranges.LongRange;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;
import kotlin.time.DurationJvmKt;
import kotlin.time.DurationKt;
import kotlin.time.DurationUnit;
import kotlin.time.DurationUnitKt;
import kotlin.time.ExperimentalTime;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@JvmInline
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\t\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b-\n\u0002\u0018\u0002\n\u0002\b\u0017\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u001b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000e\n\u0002\b\u0010\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\b\b\u0087@\u0018\u0000 \u00a4\u00012\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0002\u00a4\u0001B\u0014\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0004\u0010\u0005J%\u0010D\u001a\u00020\u00002\u0006\u0010E\u001a\u00020\u00032\u0006\u0010F\u001a\u00020\u0003H\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bG\u0010HJ\u001b\u0010I\u001a\u00020\t2\u0006\u0010J\u001a\u00020\u0000H\u0096\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\bK\u0010LJ\u001e\u0010M\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\u000fH\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bO\u0010PJ\u001e\u0010M\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\tH\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bO\u0010QJ\u001b\u0010M\u001a\u00020\u000f2\u0006\u0010J\u001a\u00020\u0000H\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\bR\u0010SJ\u001a\u0010T\u001a\u00020U2\b\u0010J\u001a\u0004\u0018\u00010VH\u00d6\u0003\u00a2\u0006\u0004\bW\u0010XJ\u0010\u0010Y\u001a\u00020\tH\u00d6\u0001\u00a2\u0006\u0004\bZ\u0010\rJ\r\u0010[\u001a\u00020U\u00a2\u0006\u0004\b\\\u0010]J\u000f\u0010^\u001a\u00020UH\u0002\u00a2\u0006\u0004\b_\u0010]J\u000f\u0010`\u001a\u00020UH\u0002\u00a2\u0006\u0004\ba\u0010]J\r\u0010b\u001a\u00020U\u00a2\u0006\u0004\bc\u0010]J\r\u0010d\u001a\u00020U\u00a2\u0006\u0004\be\u0010]J\r\u0010f\u001a\u00020U\u00a2\u0006\u0004\bg\u0010]J\u001b\u0010h\u001a\u00020\u00002\u0006\u0010J\u001a\u00020\u0000H\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\bi\u0010jJ\u001b\u0010k\u001a\u00020\u00002\u0006\u0010J\u001a\u00020\u0000H\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\bl\u0010jJ\u001e\u0010m\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\u000fH\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bn\u0010PJ\u001e\u0010m\u001a\u00020\u00002\u0006\u0010N\u001a\u00020\tH\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bn\u0010QJ\u009d\u0001\u0010o\u001a\u0002Hp\"\u0004\b\u0000\u0010p2u\u0010q\u001aq\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(u\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(v\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(w\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(x\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(y\u0012\u0004\u0012\u0002Hp0rH\u0086\b\u00f8\u0001\u0002\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0004\bz\u0010{J\u0088\u0001\u0010o\u001a\u0002Hp\"\u0004\b\u0000\u0010p2`\u0010q\u001a\\\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(v\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(w\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(x\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(y\u0012\u0004\u0012\u0002Hp0|H\u0086\b\u00f8\u0001\u0002\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0004\bz\u0010}Js\u0010o\u001a\u0002Hp\"\u0004\b\u0000\u0010p2K\u0010q\u001aG\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(w\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(x\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(y\u0012\u0004\u0012\u0002Hp0~H\u0086\b\u00f8\u0001\u0002\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0004\bz\u0010\u007fJ`\u0010o\u001a\u0002Hp\"\u0004\b\u0000\u0010p27\u0010q\u001a3\u0012\u0013\u0012\u00110\u0003\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(x\u0012\u0013\u0012\u00110\t\u00a2\u0006\f\bs\u0012\b\bt\u0012\u0004\b\b(y\u0012\u0004\u0012\u0002Hp0\u0080\u0001H\u0086\b\u00f8\u0001\u0002\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0005\bz\u0010\u0081\u0001J\u0019\u0010\u0082\u0001\u001a\u00020\u000f2\u0007\u0010\u0083\u0001\u001a\u00020=\u00a2\u0006\u0006\b\u0084\u0001\u0010\u0085\u0001J\u0019\u0010\u0086\u0001\u001a\u00020\t2\u0007\u0010\u0083\u0001\u001a\u00020=\u00a2\u0006\u0006\b\u0087\u0001\u0010\u0088\u0001J\u0011\u0010\u0089\u0001\u001a\u00030\u008a\u0001\u00a2\u0006\u0006\b\u008b\u0001\u0010\u008c\u0001J\u0019\u0010\u008d\u0001\u001a\u00020\u00032\u0007\u0010\u0083\u0001\u001a\u00020=\u00a2\u0006\u0006\b\u008e\u0001\u0010\u008f\u0001J\u0011\u0010\u0090\u0001\u001a\u00020\u0003H\u0007\u00a2\u0006\u0005\b\u0091\u0001\u0010\u0005J\u0011\u0010\u0092\u0001\u001a\u00020\u0003H\u0007\u00a2\u0006\u0005\b\u0093\u0001\u0010\u0005J\u0013\u0010\u0094\u0001\u001a\u00030\u008a\u0001H\u0016\u00a2\u0006\u0006\b\u0095\u0001\u0010\u008c\u0001J%\u0010\u0094\u0001\u001a\u00030\u008a\u00012\u0007\u0010\u0083\u0001\u001a\u00020=2\t\b\u0002\u0010\u0096\u0001\u001a\u00020\t\u00a2\u0006\u0006\b\u0095\u0001\u0010\u0097\u0001J\u0018\u0010\u0098\u0001\u001a\u00020\u0000H\u0086\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0005\b\u0099\u0001\u0010\u0005JK\u0010\u009a\u0001\u001a\u00030\u009b\u0001*\b0\u009c\u0001j\u0003`\u009d\u00012\u0007\u0010\u009e\u0001\u001a\u00020\t2\u0007\u0010\u009f\u0001\u001a\u00020\t2\u0007\u0010\u00a0\u0001\u001a\u00020\t2\b\u0010\u0083\u0001\u001a\u00030\u008a\u00012\u0007\u0010\u00a1\u0001\u001a\u00020UH\u0002\u00a2\u0006\u0006\b\u00a2\u0001\u0010\u00a3\u0001R\u0017\u0010\u0006\u001a\u00020\u00008F\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\u0005R\u001a\u0010\b\u001a\u00020\t8@X\u0081\u0004\u00a2\u0006\f\u0012\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\rR\u001a\u0010\u000e\u001a\u00020\u000f8FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0010\u0010\u000b\u001a\u0004\b\u0011\u0010\u0012R\u001a\u0010\u0013\u001a\u00020\u000f8FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0014\u0010\u000b\u001a\u0004\b\u0015\u0010\u0012R\u001a\u0010\u0016\u001a\u00020\u000f8FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u0017\u0010\u000b\u001a\u0004\b\u0018\u0010\u0012R\u001a\u0010\u0019\u001a\u00020\u000f8FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u001a\u0010\u000b\u001a\u0004\b\u001b\u0010\u0012R\u001a\u0010\u001c\u001a\u00020\u000f8FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b\u001d\u0010\u000b\u001a\u0004\b\u001e\u0010\u0012R\u001a\u0010\u001f\u001a\u00020\u000f8FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b \u0010\u000b\u001a\u0004\b!\u0010\u0012R\u001a\u0010\"\u001a\u00020\u000f8FX\u0087\u0004\u00a2\u0006\f\u0012\u0004\b#\u0010\u000b\u001a\u0004\b$\u0010\u0012R\u0011\u0010%\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b&\u0010\u0005R\u0011\u0010'\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b(\u0010\u0005R\u0011\u0010)\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b*\u0010\u0005R\u0011\u0010+\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b,\u0010\u0005R\u0011\u0010-\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b.\u0010\u0005R\u0011\u0010/\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b0\u0010\u0005R\u0011\u00101\u001a\u00020\u00038F\u00a2\u0006\u0006\u001a\u0004\b2\u0010\u0005R\u001a\u00103\u001a\u00020\t8@X\u0081\u0004\u00a2\u0006\f\u0012\u0004\b4\u0010\u000b\u001a\u0004\b5\u0010\rR\u001a\u00106\u001a\u00020\t8@X\u0081\u0004\u00a2\u0006\f\u0012\u0004\b7\u0010\u000b\u001a\u0004\b8\u0010\rR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u00109\u001a\u00020\t8@X\u0081\u0004\u00a2\u0006\f\u0012\u0004\b:\u0010\u000b\u001a\u0004\b;\u0010\rR\u0014\u0010<\u001a\u00020=8BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b>\u0010?R\u0015\u0010@\u001a\u00020\t8\u00c2\u0002X\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\bA\u0010\rR\u0014\u0010B\u001a\u00020\u00038BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\bC\u0010\u0005\u0088\u0001\u0002\u0092\u0001\u00020\u0003\u00f8\u0001\u0000\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b\u009920\u0001\u00a8\u0006\u00a5\u0001"}, d2={"Lkotlin/time/Duration;", "", "rawValue", "", "constructor-impl", "(J)J", "absoluteValue", "getAbsoluteValue-UwyO8pc", "hoursComponent", "", "getHoursComponent$annotations", "()V", "getHoursComponent-impl", "(J)I", "inDays", "", "getInDays$annotations", "getInDays-impl", "(J)D", "inHours", "getInHours$annotations", "getInHours-impl", "inMicroseconds", "getInMicroseconds$annotations", "getInMicroseconds-impl", "inMilliseconds", "getInMilliseconds$annotations", "getInMilliseconds-impl", "inMinutes", "getInMinutes$annotations", "getInMinutes-impl", "inNanoseconds", "getInNanoseconds$annotations", "getInNanoseconds-impl", "inSeconds", "getInSeconds$annotations", "getInSeconds-impl", "inWholeDays", "getInWholeDays-impl", "inWholeHours", "getInWholeHours-impl", "inWholeMicroseconds", "getInWholeMicroseconds-impl", "inWholeMilliseconds", "getInWholeMilliseconds-impl", "inWholeMinutes", "getInWholeMinutes-impl", "inWholeNanoseconds", "getInWholeNanoseconds-impl", "inWholeSeconds", "getInWholeSeconds-impl", "minutesComponent", "getMinutesComponent$annotations", "getMinutesComponent-impl", "nanosecondsComponent", "getNanosecondsComponent$annotations", "getNanosecondsComponent-impl", "secondsComponent", "getSecondsComponent$annotations", "getSecondsComponent-impl", "storageUnit", "Lkotlin/time/DurationUnit;", "getStorageUnit-impl", "(J)Lkotlin/time/DurationUnit;", "unitDiscriminator", "getUnitDiscriminator-impl", "value", "getValue-impl", "addValuesMixedRanges", "thisMillis", "otherNanos", "addValuesMixedRanges-UwyO8pc", "(JJJ)J", "compareTo", "other", "compareTo-LRDsOJo", "(JJ)I", "div", "scale", "div-UwyO8pc", "(JD)J", "(JI)J", "div-LRDsOJo", "(JJ)D", "equals", "", "", "equals-impl", "(JLjava/lang/Object;)Z", "hashCode", "hashCode-impl", "isFinite", "isFinite-impl", "(J)Z", "isInMillis", "isInMillis-impl", "isInNanos", "isInNanos-impl", "isInfinite", "isInfinite-impl", "isNegative", "isNegative-impl", "isPositive", "isPositive-impl", "minus", "minus-LRDsOJo", "(JJ)J", "plus", "plus-LRDsOJo", "times", "times-UwyO8pc", "toComponents", "T", "action", "Lkotlin/Function5;", "Lkotlin/ParameterName;", "name", "days", "hours", "minutes", "seconds", "nanoseconds", "toComponents-impl", "(JLkotlin/jvm/functions/Function5;)Ljava/lang/Object;", "Lkotlin/Function4;", "(JLkotlin/jvm/functions/Function4;)Ljava/lang/Object;", "Lkotlin/Function3;", "(JLkotlin/jvm/functions/Function3;)Ljava/lang/Object;", "Lkotlin/Function2;", "(JLkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "toDouble", "unit", "toDouble-impl", "(JLkotlin/time/DurationUnit;)D", "toInt", "toInt-impl", "(JLkotlin/time/DurationUnit;)I", "toIsoString", "", "toIsoString-impl", "(J)Ljava/lang/String;", "toLong", "toLong-impl", "(JLkotlin/time/DurationUnit;)J", "toLongMilliseconds", "toLongMilliseconds-impl", "toLongNanoseconds", "toLongNanoseconds-impl", "toString", "toString-impl", "decimals", "(JLkotlin/time/DurationUnit;I)Ljava/lang/String;", "unaryMinus", "unaryMinus-UwyO8pc", "appendFractional", "", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "whole", "fractional", "fractionalSize", "isoZeroes", "appendFractional-impl", "(JLjava/lang/StringBuilder;IIILjava/lang/String;Z)V", "Companion", "kotlin-stdlib"})
@SinceKotlin(version="1.6")
@WasExperimental(markerClass={ExperimentalTime.class})
public final class Duration
implements Comparable<Duration> {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private final long rawValue;
    private static final long ZERO = Duration.constructor-impl(0L);
    private static final long INFINITE = DurationKt.access$durationOfMillis(0x3FFFFFFFFFFFFFFFL);
    private static final long NEG_INFINITE = DurationKt.access$durationOfMillis(-4611686018427387903L);

    private static final long getValue-impl(long arg0) {
        return arg0 >> 1;
    }

    private static final int getUnitDiscriminator-impl(long arg0) {
        boolean bl = false;
        return (int)arg0 & 1;
    }

    private static final boolean isInNanos-impl(long arg0) {
        boolean bl = false;
        return ((int)arg0 & 1) == 0;
    }

    private static final boolean isInMillis-impl(long arg0) {
        boolean bl = false;
        return ((int)arg0 & 1) == 1;
    }

    private static final DurationUnit getStorageUnit-impl(long arg0) {
        return Duration.isInNanos-impl(arg0) ? DurationUnit.NANOSECONDS : DurationUnit.MILLISECONDS;
    }

    public static final long unaryMinus-UwyO8pc(long arg0) {
        boolean bl = false;
        return DurationKt.access$durationOf(-Duration.getValue-impl(arg0), (int)arg0 & 1);
    }

    public static final long plus-LRDsOJo(long arg0, long other) {
        long l;
        if (Duration.isInfinite-impl(arg0)) {
            if (Duration.isFinite-impl(other) || (arg0 ^ other) >= 0L) {
                return arg0;
            }
            throw new IllegalArgumentException("Summing infinite durations of different signs yields an undefined result.");
        }
        if (Duration.isInfinite-impl(other)) {
            return other;
        }
        boolean bl = false;
        bl = false;
        if (((int)arg0 & 1) == ((int)other & 1)) {
            long result = Duration.getValue-impl(arg0) + Duration.getValue-impl(other);
            l = Duration.isInNanos-impl(arg0) ? DurationKt.access$durationOfNanosNormalized(result) : DurationKt.access$durationOfMillisNormalized(result);
        } else {
            l = Duration.isInMillis-impl(arg0) ? Duration.addValuesMixedRanges-UwyO8pc(arg0, Duration.getValue-impl(arg0), Duration.getValue-impl(other)) : Duration.addValuesMixedRanges-UwyO8pc(arg0, Duration.getValue-impl(other), Duration.getValue-impl(arg0));
        }
        return l;
    }

    private static final long addValuesMixedRanges-UwyO8pc(long arg0, long thisMillis, long otherNanos) {
        long l;
        long otherMillis = DurationKt.access$nanosToMillis(otherNanos);
        long resultMillis = thisMillis + otherMillis;
        boolean bl = -4611686018426L <= resultMillis ? resultMillis < 4611686018427L : false;
        if (bl) {
            long otherNanoRemainder = otherNanos - DurationKt.access$millisToNanos(otherMillis);
            l = DurationKt.access$durationOfNanos(DurationKt.access$millisToNanos(resultMillis) + otherNanoRemainder);
        } else {
            l = DurationKt.access$durationOfMillis(RangesKt.coerceIn(resultMillis, -4611686018427387903L, 0x3FFFFFFFFFFFFFFFL));
        }
        return l;
    }

    public static final long minus-LRDsOJo(long arg0, long other) {
        return Duration.plus-LRDsOJo(arg0, Duration.unaryMinus-UwyO8pc(other));
    }

    public static final long times-UwyO8pc(long arg0, int scale) {
        long l;
        if (Duration.isInfinite-impl(arg0)) {
            if (scale == 0) {
                throw new IllegalArgumentException("Multiplying infinite duration by zero yields an undefined result.");
            }
            return scale > 0 ? arg0 : Duration.unaryMinus-UwyO8pc(arg0);
        }
        if (scale == 0) {
            return ZERO;
        }
        long value = Duration.getValue-impl(arg0);
        long result = value * (long)scale;
        if (Duration.isInNanos-impl(arg0)) {
            boolean bl = value <= Integer.MAX_VALUE ? -2147483647L <= value : false;
            if (bl) {
                l = DurationKt.access$durationOfNanos(result);
            } else if (result / (long)scale == value) {
                l = DurationKt.access$durationOfNanosNormalized(result);
            } else {
                long millis = DurationKt.access$nanosToMillis(value);
                long remNanos = value - DurationKt.access$millisToNanos(millis);
                long resultMillis = millis * (long)scale;
                long totalMillis = resultMillis + DurationKt.access$nanosToMillis(remNanos * (long)scale);
                l = resultMillis / (long)scale == millis && (totalMillis ^ resultMillis) >= 0L ? DurationKt.access$durationOfMillis(RangesKt.coerceIn(totalMillis, new LongRange(-4611686018427387903L, 0x3FFFFFFFFFFFFFFFL))) : (MathKt.getSign(value) * MathKt.getSign(scale) > 0 ? INFINITE : NEG_INFINITE);
            }
        } else {
            l = result / (long)scale == value ? DurationKt.access$durationOfMillis(RangesKt.coerceIn(result, new LongRange(-4611686018427387903L, 0x3FFFFFFFFFFFFFFFL))) : (MathKt.getSign(value) * MathKt.getSign(scale) > 0 ? INFINITE : NEG_INFINITE);
        }
        return l;
    }

    public static final long times-UwyO8pc(long arg0, double scale) {
        int intScale = MathKt.roundToInt(scale);
        if ((double)intScale == scale) {
            return Duration.times-UwyO8pc(arg0, intScale);
        }
        DurationUnit unit = Duration.getStorageUnit-impl(arg0);
        double result = Duration.toDouble-impl(arg0, unit) * scale;
        return DurationKt.toDuration(result, unit);
    }

    public static final long div-UwyO8pc(long arg0, int scale) {
        if (scale == 0) {
            long l;
            if (Duration.isPositive-impl(arg0)) {
                l = INFINITE;
            } else if (Duration.isNegative-impl(arg0)) {
                l = NEG_INFINITE;
            } else {
                throw new IllegalArgumentException("Dividing zero duration by zero yields an undefined result.");
            }
            return l;
        }
        if (Duration.isInNanos-impl(arg0)) {
            return DurationKt.access$durationOfNanos(Duration.getValue-impl(arg0) / (long)scale);
        }
        if (Duration.isInfinite-impl(arg0)) {
            return Duration.times-UwyO8pc(arg0, MathKt.getSign(scale));
        }
        long result = Duration.getValue-impl(arg0) / (long)scale;
        boolean bl = -4611686018426L <= result ? result < 4611686018427L : false;
        if (bl) {
            long rem = DurationKt.access$millisToNanos(Duration.getValue-impl(arg0) - result * (long)scale) / (long)scale;
            return DurationKt.access$durationOfNanos(DurationKt.access$millisToNanos(result) + rem);
        }
        return DurationKt.access$durationOfMillis(result);
    }

    public static final long div-UwyO8pc(long arg0, double scale) {
        int intScale = MathKt.roundToInt(scale);
        if ((double)intScale == scale && intScale != 0) {
            return Duration.div-UwyO8pc(arg0, intScale);
        }
        DurationUnit unit = Duration.getStorageUnit-impl(arg0);
        double result = Duration.toDouble-impl(arg0, unit) / scale;
        return DurationKt.toDuration(result, unit);
    }

    public static final double div-LRDsOJo(long arg0, long other) {
        DurationUnit coarserUnit = (DurationUnit)((Object)ComparisonsKt.maxOf((Comparable)((Object)Duration.getStorageUnit-impl(arg0)), (Comparable)((Object)Duration.getStorageUnit-impl(other))));
        return Duration.toDouble-impl(arg0, coarserUnit) / Duration.toDouble-impl(other, coarserUnit);
    }

    public static final boolean isNegative-impl(long arg0) {
        return arg0 < 0L;
    }

    public static final boolean isPositive-impl(long arg0) {
        return arg0 > 0L;
    }

    public static final boolean isInfinite-impl(long arg0) {
        return arg0 == INFINITE || arg0 == NEG_INFINITE;
    }

    public static final boolean isFinite-impl(long arg0) {
        return !Duration.isInfinite-impl(arg0);
    }

    public static final long getAbsoluteValue-UwyO8pc(long arg0) {
        return Duration.isNegative-impl(arg0) ? Duration.unaryMinus-UwyO8pc(arg0) : arg0;
    }

    public static int compareTo-LRDsOJo(long arg0, long other) {
        long compareBits = arg0 ^ other;
        if (compareBits < 0L || ((int)compareBits & 1) == 0) {
            return Intrinsics.compare(arg0, other);
        }
        boolean bl = false;
        bl = false;
        int r = ((int)arg0 & 1) - ((int)other & 1);
        return Duration.isNegative-impl(arg0) ? -r : r;
    }

    public int compareTo-LRDsOJo(long other) {
        return Duration.compareTo-LRDsOJo(this.rawValue, other);
    }

    public static final <T> T toComponents-impl(long arg0, @NotNull Function5<? super Long, ? super Integer, ? super Integer, ? super Integer, ? super Integer, ? extends T> action) {
        Intrinsics.checkNotNullParameter(action, "action");
        boolean bl = false;
        return action.invoke(Duration.getInWholeDays-impl(arg0), Duration.getHoursComponent-impl(arg0), Duration.getMinutesComponent-impl(arg0), Duration.getSecondsComponent-impl(arg0), Duration.getNanosecondsComponent-impl(arg0));
    }

    public static final <T> T toComponents-impl(long arg0, @NotNull Function4<? super Long, ? super Integer, ? super Integer, ? super Integer, ? extends T> action) {
        Intrinsics.checkNotNullParameter(action, "action");
        boolean bl = false;
        return action.invoke(Duration.getInWholeHours-impl(arg0), Duration.getMinutesComponent-impl(arg0), Duration.getSecondsComponent-impl(arg0), Duration.getNanosecondsComponent-impl(arg0));
    }

    public static final <T> T toComponents-impl(long arg0, @NotNull Function3<? super Long, ? super Integer, ? super Integer, ? extends T> action) {
        Intrinsics.checkNotNullParameter(action, "action");
        boolean bl = false;
        return action.invoke(Duration.getInWholeMinutes-impl(arg0), Duration.getSecondsComponent-impl(arg0), Duration.getNanosecondsComponent-impl(arg0));
    }

    public static final <T> T toComponents-impl(long arg0, @NotNull Function2<? super Long, ? super Integer, ? extends T> action) {
        Intrinsics.checkNotNullParameter(action, "action");
        boolean bl = false;
        return action.invoke(Duration.getInWholeSeconds-impl(arg0), Duration.getNanosecondsComponent-impl(arg0));
    }

    public static final int getHoursComponent-impl(long arg0) {
        return Duration.isInfinite-impl(arg0) ? 0 : (int)(Duration.getInWholeHours-impl(arg0) % (long)24);
    }

    @PublishedApi
    public static /* synthetic */ void getHoursComponent$annotations() {
    }

    public static final int getMinutesComponent-impl(long arg0) {
        return Duration.isInfinite-impl(arg0) ? 0 : (int)(Duration.getInWholeMinutes-impl(arg0) % (long)60);
    }

    @PublishedApi
    public static /* synthetic */ void getMinutesComponent$annotations() {
    }

    public static final int getSecondsComponent-impl(long arg0) {
        return Duration.isInfinite-impl(arg0) ? 0 : (int)(Duration.getInWholeSeconds-impl(arg0) % (long)60);
    }

    @PublishedApi
    public static /* synthetic */ void getSecondsComponent$annotations() {
    }

    public static final int getNanosecondsComponent-impl(long arg0) {
        return Duration.isInfinite-impl(arg0) ? 0 : (Duration.isInMillis-impl(arg0) ? (int)DurationKt.access$millisToNanos(Duration.getValue-impl(arg0) % (long)1000) : (int)(Duration.getValue-impl(arg0) % (long)1000000000));
    }

    @PublishedApi
    public static /* synthetic */ void getNanosecondsComponent$annotations() {
    }

    public static final double toDouble-impl(long arg0, @NotNull DurationUnit unit) {
        Intrinsics.checkNotNullParameter((Object)unit, "unit");
        long l = arg0;
        return l == INFINITE ? Double.POSITIVE_INFINITY : (l == NEG_INFINITE ? Double.NEGATIVE_INFINITY : DurationUnitKt.convertDurationUnit((double)Duration.getValue-impl(arg0), Duration.getStorageUnit-impl(arg0), unit));
    }

    public static final long toLong-impl(long arg0, @NotNull DurationUnit unit) {
        Intrinsics.checkNotNullParameter((Object)unit, "unit");
        long l = arg0;
        return l == INFINITE ? Long.MAX_VALUE : (l == NEG_INFINITE ? Long.MIN_VALUE : DurationUnitKt.convertDurationUnit(Duration.getValue-impl(arg0), Duration.getStorageUnit-impl(arg0), unit));
    }

    public static final int toInt-impl(long arg0, @NotNull DurationUnit unit) {
        Intrinsics.checkNotNullParameter((Object)unit, "unit");
        return (int)RangesKt.coerceIn(Duration.toLong-impl(arg0, unit), Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static final double getInDays-impl(long arg0) {
        return Duration.toDouble-impl(arg0, DurationUnit.DAYS);
    }

    @Deprecated(message="Use inWholeDays property instead or convert toDouble(DAYS) if a double value is required.", replaceWith=@ReplaceWith(expression="toDouble(DurationUnit.DAYS)", imports={}))
    @ExperimentalTime
    public static /* synthetic */ void getInDays$annotations() {
    }

    public static final double getInHours-impl(long arg0) {
        return Duration.toDouble-impl(arg0, DurationUnit.HOURS);
    }

    @Deprecated(message="Use inWholeHours property instead or convert toDouble(HOURS) if a double value is required.", replaceWith=@ReplaceWith(expression="toDouble(DurationUnit.HOURS)", imports={}))
    @ExperimentalTime
    public static /* synthetic */ void getInHours$annotations() {
    }

    public static final double getInMinutes-impl(long arg0) {
        return Duration.toDouble-impl(arg0, DurationUnit.MINUTES);
    }

    @Deprecated(message="Use inWholeMinutes property instead or convert toDouble(MINUTES) if a double value is required.", replaceWith=@ReplaceWith(expression="toDouble(DurationUnit.MINUTES)", imports={}))
    @ExperimentalTime
    public static /* synthetic */ void getInMinutes$annotations() {
    }

    public static final double getInSeconds-impl(long arg0) {
        return Duration.toDouble-impl(arg0, DurationUnit.SECONDS);
    }

    @Deprecated(message="Use inWholeSeconds property instead or convert toDouble(SECONDS) if a double value is required.", replaceWith=@ReplaceWith(expression="toDouble(DurationUnit.SECONDS)", imports={}))
    @ExperimentalTime
    public static /* synthetic */ void getInSeconds$annotations() {
    }

    public static final double getInMilliseconds-impl(long arg0) {
        return Duration.toDouble-impl(arg0, DurationUnit.MILLISECONDS);
    }

    @Deprecated(message="Use inWholeMilliseconds property instead or convert toDouble(MILLISECONDS) if a double value is required.", replaceWith=@ReplaceWith(expression="toDouble(DurationUnit.MILLISECONDS)", imports={}))
    @ExperimentalTime
    public static /* synthetic */ void getInMilliseconds$annotations() {
    }

    public static final double getInMicroseconds-impl(long arg0) {
        return Duration.toDouble-impl(arg0, DurationUnit.MICROSECONDS);
    }

    @Deprecated(message="Use inWholeMicroseconds property instead or convert toDouble(MICROSECONDS) if a double value is required.", replaceWith=@ReplaceWith(expression="toDouble(DurationUnit.MICROSECONDS)", imports={}))
    @ExperimentalTime
    public static /* synthetic */ void getInMicroseconds$annotations() {
    }

    public static final double getInNanoseconds-impl(long arg0) {
        return Duration.toDouble-impl(arg0, DurationUnit.NANOSECONDS);
    }

    @Deprecated(message="Use inWholeNanoseconds property instead or convert toDouble(NANOSECONDS) if a double value is required.", replaceWith=@ReplaceWith(expression="toDouble(DurationUnit.NANOSECONDS)", imports={}))
    @ExperimentalTime
    public static /* synthetic */ void getInNanoseconds$annotations() {
    }

    public static final long getInWholeDays-impl(long arg0) {
        return Duration.toLong-impl(arg0, DurationUnit.DAYS);
    }

    public static final long getInWholeHours-impl(long arg0) {
        return Duration.toLong-impl(arg0, DurationUnit.HOURS);
    }

    public static final long getInWholeMinutes-impl(long arg0) {
        return Duration.toLong-impl(arg0, DurationUnit.MINUTES);
    }

    public static final long getInWholeSeconds-impl(long arg0) {
        return Duration.toLong-impl(arg0, DurationUnit.SECONDS);
    }

    public static final long getInWholeMilliseconds-impl(long arg0) {
        return Duration.isInMillis-impl(arg0) && Duration.isFinite-impl(arg0) ? Duration.getValue-impl(arg0) : Duration.toLong-impl(arg0, DurationUnit.MILLISECONDS);
    }

    public static final long getInWholeMicroseconds-impl(long arg0) {
        return Duration.toLong-impl(arg0, DurationUnit.MICROSECONDS);
    }

    public static final long getInWholeNanoseconds-impl(long arg0) {
        long value = Duration.getValue-impl(arg0);
        return Duration.isInNanos-impl(arg0) ? value : (value > 9223372036854L ? Long.MAX_VALUE : (value < -9223372036854L ? Long.MIN_VALUE : DurationKt.access$millisToNanos(value)));
    }

    @Deprecated(message="Use inWholeNanoseconds property instead.", replaceWith=@ReplaceWith(expression="this.inWholeNanoseconds", imports={}))
    @ExperimentalTime
    public static final long toLongNanoseconds-impl(long arg0) {
        return Duration.getInWholeNanoseconds-impl(arg0);
    }

    @Deprecated(message="Use inWholeMilliseconds property instead.", replaceWith=@ReplaceWith(expression="this.inWholeMilliseconds", imports={}))
    @ExperimentalTime
    public static final long toLongMilliseconds-impl(long arg0) {
        return Duration.getInWholeMilliseconds-impl(arg0);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static String toString-impl(long arg0) {
        String string;
        long l = arg0;
        if (l == 0L) {
            string = "0s";
        } else if (l == INFINITE) {
            string = "Infinity";
        } else if (l == NEG_INFINITE) {
            string = "-Infinity";
        } else {
            int n;
            void nanoseconds;
            void seconds;
            void minutes;
            void hours;
            StringBuilder stringBuilder;
            boolean isNegative = Duration.isNegative-impl(arg0);
            StringBuilder $this$toString_impl_u24lambda_u2d5 = stringBuilder = new StringBuilder();
            boolean bl = false;
            if (isNegative) {
                $this$toString_impl_u24lambda_u2d5.append('-');
            }
            long arg0$iv = Duration.getAbsoluteValue-UwyO8pc(arg0);
            boolean bl2 = false;
            int n2 = Duration.getNanosecondsComponent-impl(arg0$iv);
            int n3 = Duration.getSecondsComponent-impl(arg0$iv);
            int n4 = Duration.getMinutesComponent-impl(arg0$iv);
            int n5 = Duration.getHoursComponent-impl(arg0$iv);
            long days = Duration.getInWholeDays-impl(arg0$iv);
            boolean bl3 = false;
            boolean hasDays = days != 0L;
            boolean hasHours = hours != false;
            boolean hasMinutes = minutes != false;
            boolean hasSeconds = seconds != false || nanoseconds != false;
            int components = 0;
            if (hasDays) {
                $this$toString_impl_u24lambda_u2d5.append(days).append('d');
                n = components;
                components = n + 1;
            }
            if (hasHours || hasDays && (hasMinutes || hasSeconds)) {
                n = components;
                components = n + 1;
                if (n > 0) {
                    $this$toString_impl_u24lambda_u2d5.append(' ');
                }
                $this$toString_impl_u24lambda_u2d5.append((int)hours).append('h');
            }
            if (hasMinutes || hasSeconds && (hasHours || hasDays)) {
                n = components;
                components = n + 1;
                if (n > 0) {
                    $this$toString_impl_u24lambda_u2d5.append(' ');
                }
                $this$toString_impl_u24lambda_u2d5.append((int)minutes).append('m');
            }
            if (hasSeconds) {
                n = components;
                components = n + 1;
                if (n > 0) {
                    $this$toString_impl_u24lambda_u2d5.append(' ');
                }
                if (seconds != false || hasDays || hasHours || hasMinutes) {
                    Duration.appendFractional-impl(arg0, $this$toString_impl_u24lambda_u2d5, (int)seconds, (int)nanoseconds, 9, "s", false);
                } else if (nanoseconds >= 1000000) {
                    Duration.appendFractional-impl(arg0, $this$toString_impl_u24lambda_u2d5, (int)(nanoseconds / 1000000), (int)(nanoseconds % 1000000), 6, "ms", false);
                } else if (nanoseconds >= 1000) {
                    Duration.appendFractional-impl(arg0, $this$toString_impl_u24lambda_u2d5, (int)(nanoseconds / 1000), (int)(nanoseconds % 1000), 3, "us", false);
                } else {
                    $this$toString_impl_u24lambda_u2d5.append((int)nanoseconds).append("ns");
                }
            }
            if (isNegative && components > 1) {
                $this$toString_impl_u24lambda_u2d5.insert(1, '(').append(')');
            }
            String string2 = stringBuilder.toString();
            Intrinsics.checkNotNullExpressionValue(string2, "StringBuilder().apply(builderAction).toString()");
            string = string2;
        }
        return string;
    }

    @NotNull
    public String toString() {
        return Duration.toString-impl(this.rawValue);
    }

    private static final void appendFractional-impl(long arg0, StringBuilder $this$appendFractional, int whole, int fractional, int fractionalSize, String unit, boolean isoZeroes) {
        $this$appendFractional.append(whole);
        if (fractional != 0) {
            int n;
            StringBuilder stringBuilder;
            int n2;
            int n3;
            String fracString;
            block5: {
                $this$appendFractional.append('.');
                fracString = StringsKt.padStart(String.valueOf(fractional), fractionalSize, '0');
                CharSequence $this$indexOfLast$iv = fracString;
                boolean $i$f$indexOfLast = false;
                n3 = $this$indexOfLast$iv.length() + -1;
                if (0 <= n3) {
                    do {
                        int index$iv = n3--;
                        char it = $this$indexOfLast$iv.charAt(index$iv);
                        boolean bl = false;
                        if (!(it != '0')) continue;
                        n2 = index$iv;
                        break block5;
                    } while (0 <= n3);
                }
                n2 = -1;
            }
            int nonZeroDigits = n2 + 1;
            if (!isoZeroes && nonZeroDigits < 3) {
                stringBuilder = $this$appendFractional;
                n = 0;
                StringBuilder stringBuilder2 = stringBuilder.append(fracString, n, nonZeroDigits);
                Intrinsics.checkNotNullExpressionValue(stringBuilder2, "this.append(value, startIndex, endIndex)");
            } else {
                stringBuilder = $this$appendFractional;
                n = 0;
                n3 = (nonZeroDigits + 2) / 3 * 3;
                StringBuilder stringBuilder3 = stringBuilder.append(fracString, n, n3);
                Intrinsics.checkNotNullExpressionValue(stringBuilder3, "this.append(value, startIndex, endIndex)");
            }
        }
        $this$appendFractional.append(unit);
    }

    @NotNull
    public static final String toString-impl(long arg0, @NotNull DurationUnit unit, int decimals) {
        boolean bl;
        Intrinsics.checkNotNullParameter((Object)unit, "unit");
        boolean bl2 = bl = decimals >= 0;
        if (!bl) {
            boolean bl3 = false;
            String string = Intrinsics.stringPlus("decimals must be not negative, but was ", decimals);
            throw new IllegalArgumentException(string.toString());
        }
        double number = Duration.toDouble-impl(arg0, unit);
        if (Double.isInfinite(number)) {
            return String.valueOf(number);
        }
        return Intrinsics.stringPlus(DurationJvmKt.formatToExactDecimals(number, RangesKt.coerceAtMost(decimals, 12)), DurationUnitKt.shortName(unit));
    }

    public static /* synthetic */ String toString-impl$default(long l, DurationUnit durationUnit, int n, int n2, Object object) {
        if ((n2 & 2) != 0) {
            n = 0;
        }
        return Duration.toString-impl(l, durationUnit, n);
    }

    /*
     * WARNING - void declaration
     */
    @NotNull
    public static final String toIsoString-impl(long arg0) {
        void minutes;
        boolean hasMinutes;
        void nanoseconds;
        void seconds;
        StringBuilder stringBuilder;
        StringBuilder $this$toIsoString_impl_u24lambda_u2d9 = stringBuilder = new StringBuilder();
        boolean bl = false;
        if (Duration.isNegative-impl(arg0)) {
            $this$toIsoString_impl_u24lambda_u2d9.append('-');
        }
        $this$toIsoString_impl_u24lambda_u2d9.append("PT");
        long arg0$iv = Duration.getAbsoluteValue-UwyO8pc(arg0);
        boolean bl2 = false;
        int n = Duration.getNanosecondsComponent-impl(arg0$iv);
        int n2 = Duration.getSecondsComponent-impl(arg0$iv);
        int n3 = Duration.getMinutesComponent-impl(arg0$iv);
        long hours = Duration.getInWholeHours-impl(arg0$iv);
        boolean bl3 = false;
        long hours2 = hours;
        if (Duration.isInfinite-impl(arg0)) {
            hours2 = 9999999999999L;
        }
        boolean hasHours = hours2 != 0L;
        boolean hasSeconds = seconds != false || nanoseconds != false;
        boolean bl4 = hasMinutes = minutes != false || hasSeconds && hasHours;
        if (hasHours) {
            $this$toIsoString_impl_u24lambda_u2d9.append(hours2).append('H');
        }
        if (hasMinutes) {
            $this$toIsoString_impl_u24lambda_u2d9.append((int)minutes).append('M');
        }
        if (hasSeconds || !hasHours && !hasMinutes) {
            Duration.appendFractional-impl(arg0, $this$toIsoString_impl_u24lambda_u2d9, (int)seconds, (int)nanoseconds, 9, "S", true);
        }
        String string = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(string, "StringBuilder().apply(builderAction).toString()");
        return string;
    }

    public static int hashCode-impl(long arg0) {
        long l = arg0;
        return (int)(l ^ l >>> 32);
    }

    public int hashCode() {
        return Duration.hashCode-impl(this.rawValue);
    }

    public static boolean equals-impl(long arg0, Object other) {
        if (!(other instanceof Duration)) {
            return false;
        }
        long l = ((Duration)other).unbox-impl();
        return arg0 == l;
    }

    public boolean equals(Object other) {
        return Duration.equals-impl(this.rawValue, other);
    }

    private /* synthetic */ Duration(long rawValue) {
        this.rawValue = rawValue;
    }

    public static long constructor-impl(long rawValue) {
        long l = rawValue;
        if (DurationJvmKt.getDurationAssertionsEnabled()) {
            if (Duration.isInNanos-impl(l)) {
                long l2 = Duration.getValue-impl(l);
                if (!(-4611686018426999999L <= l2 ? l2 < 4611686018427000000L : false)) {
                    throw new AssertionError((Object)(Duration.getValue-impl(l) + " ns is out of nanoseconds range"));
                }
            } else {
                long l3 = Duration.getValue-impl(l);
                if (!(-4611686018427387903L <= l3 ? l3 < 0x4000000000000000L : false)) {
                    throw new AssertionError((Object)(Duration.getValue-impl(l) + " ms is out of milliseconds range"));
                }
                l3 = Duration.getValue-impl(l);
                boolean bl = -4611686018426L <= l3 ? l3 < 4611686018427L : false;
                if (bl) {
                    throw new AssertionError((Object)(Duration.getValue-impl(l) + " ms is denormalized"));
                }
            }
        }
        return l;
    }

    public static final /* synthetic */ Duration box-impl(long v) {
        return new Duration(v);
    }

    public final /* synthetic */ long unbox-impl() {
        return this.rawValue;
    }

    public static final boolean equals-impl0(long p1, long p2) {
        return p1 == p2;
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0017\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\n\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J \u0010*\u001a\u00020\r2\u0006\u0010+\u001a\u00020\r2\u0006\u0010,\u001a\u00020-2\u0006\u0010.\u001a\u00020-H\u0007J\u001d\u0010\f\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b/\u0010\u0011J\u001d\u0010\f\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b/\u0010\u0014J\u001d\u0010\f\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b/\u0010\u0017J\u001d\u0010\u0018\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b0\u0010\u0011J\u001d\u0010\u0018\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b0\u0010\u0014J\u001d\u0010\u0018\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b0\u0010\u0017J\u001d\u0010\u001b\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b1\u0010\u0011J\u001d\u0010\u001b\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b1\u0010\u0014J\u001d\u0010\u001b\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b1\u0010\u0017J\u001d\u0010\u001e\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b2\u0010\u0011J\u001d\u0010\u001e\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b2\u0010\u0014J\u001d\u0010\u001e\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b2\u0010\u0017J\u001d\u0010!\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b3\u0010\u0011J\u001d\u0010!\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b3\u0010\u0014J\u001d\u0010!\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b3\u0010\u0017J\u001d\u0010$\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b4\u0010\u0011J\u001d\u0010$\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b4\u0010\u0014J\u001d\u0010$\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b4\u0010\u0017J\u001b\u00105\u001a\u00020\u00042\u0006\u0010+\u001a\u000206\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b7\u00108J\u001b\u00109\u001a\u00020\u00042\u0006\u0010+\u001a\u000206\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b:\u00108J\u001b\u0010;\u001a\u0004\u0018\u00010\u00042\u0006\u0010+\u001a\u000206\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0002\b<J\u001b\u0010=\u001a\u0004\u0018\u00010\u00042\u0006\u0010+\u001a\u000206\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0002\b>J\u001d\u0010'\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\rH\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b?\u0010\u0011J\u001d\u0010'\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0012H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b?\u0010\u0014J\u001d\u0010'\u001a\u00020\u00042\u0006\u0010+\u001a\u00020\u0015H\u0007\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b?\u0010\u0017R\u0019\u0010\u0003\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0005\u0010\u0006R\u001c\u0010\b\u001a\u00020\u0004X\u0080\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\t\u0010\u0006R\u0019\u0010\n\u001a\u00020\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u000b\u0010\u0006R%\u0010\f\u001a\u00020\u0004*\u00020\r8\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\u000e\u0010\u000f\u001a\u0004\b\u0010\u0010\u0011R%\u0010\f\u001a\u00020\u0004*\u00020\u00128\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\u000e\u0010\u0013\u001a\u0004\b\u0010\u0010\u0014R%\u0010\f\u001a\u00020\u0004*\u00020\u00158\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\u000e\u0010\u0016\u001a\u0004\b\u0010\u0010\u0017R%\u0010\u0018\u001a\u00020\u0004*\u00020\r8\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\u0019\u0010\u000f\u001a\u0004\b\u001a\u0010\u0011R%\u0010\u0018\u001a\u00020\u0004*\u00020\u00128\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\u0019\u0010\u0013\u001a\u0004\b\u001a\u0010\u0014R%\u0010\u0018\u001a\u00020\u0004*\u00020\u00158\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\u0019\u0010\u0016\u001a\u0004\b\u001a\u0010\u0017R%\u0010\u001b\u001a\u00020\u0004*\u00020\r8\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\u001c\u0010\u000f\u001a\u0004\b\u001d\u0010\u0011R%\u0010\u001b\u001a\u00020\u0004*\u00020\u00128\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\u001c\u0010\u0013\u001a\u0004\b\u001d\u0010\u0014R%\u0010\u001b\u001a\u00020\u0004*\u00020\u00158\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\u001c\u0010\u0016\u001a\u0004\b\u001d\u0010\u0017R%\u0010\u001e\u001a\u00020\u0004*\u00020\r8\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\u001f\u0010\u000f\u001a\u0004\b \u0010\u0011R%\u0010\u001e\u001a\u00020\u0004*\u00020\u00128\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\u001f\u0010\u0013\u001a\u0004\b \u0010\u0014R%\u0010\u001e\u001a\u00020\u0004*\u00020\u00158\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\u001f\u0010\u0016\u001a\u0004\b \u0010\u0017R%\u0010!\u001a\u00020\u0004*\u00020\r8\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\"\u0010\u000f\u001a\u0004\b#\u0010\u0011R%\u0010!\u001a\u00020\u0004*\u00020\u00128\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\"\u0010\u0013\u001a\u0004\b#\u0010\u0014R%\u0010!\u001a\u00020\u0004*\u00020\u00158\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b\"\u0010\u0016\u001a\u0004\b#\u0010\u0017R%\u0010$\u001a\u00020\u0004*\u00020\r8\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b%\u0010\u000f\u001a\u0004\b&\u0010\u0011R%\u0010$\u001a\u00020\u0004*\u00020\u00128\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b%\u0010\u0013\u001a\u0004\b&\u0010\u0014R%\u0010$\u001a\u00020\u0004*\u00020\u00158\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b%\u0010\u0016\u001a\u0004\b&\u0010\u0017R%\u0010'\u001a\u00020\u0004*\u00020\r8\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b(\u0010\u000f\u001a\u0004\b)\u0010\u0011R%\u0010'\u001a\u00020\u0004*\u00020\u00128\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b(\u0010\u0013\u001a\u0004\b)\u0010\u0014R%\u0010'\u001a\u00020\u0004*\u00020\u00158\u00c6\u0002X\u0087\u0004\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\f\u0012\u0004\b(\u0010\u0016\u001a\u0004\b)\u0010\u0017\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006@"}, d2={"Lkotlin/time/Duration$Companion;", "", "()V", "INFINITE", "Lkotlin/time/Duration;", "getINFINITE-UwyO8pc", "()J", "J", "NEG_INFINITE", "getNEG_INFINITE-UwyO8pc$kotlin_stdlib", "ZERO", "getZERO-UwyO8pc", "days", "", "getDays-UwyO8pc$annotations", "(D)V", "getDays-UwyO8pc", "(D)J", "", "(I)V", "(I)J", "", "(J)V", "(J)J", "hours", "getHours-UwyO8pc$annotations", "getHours-UwyO8pc", "microseconds", "getMicroseconds-UwyO8pc$annotations", "getMicroseconds-UwyO8pc", "milliseconds", "getMilliseconds-UwyO8pc$annotations", "getMilliseconds-UwyO8pc", "minutes", "getMinutes-UwyO8pc$annotations", "getMinutes-UwyO8pc", "nanoseconds", "getNanoseconds-UwyO8pc$annotations", "getNanoseconds-UwyO8pc", "seconds", "getSeconds-UwyO8pc$annotations", "getSeconds-UwyO8pc", "convert", "value", "sourceUnit", "Lkotlin/time/DurationUnit;", "targetUnit", "days-UwyO8pc", "hours-UwyO8pc", "microseconds-UwyO8pc", "milliseconds-UwyO8pc", "minutes-UwyO8pc", "nanoseconds-UwyO8pc", "parse", "", "parse-UwyO8pc", "(Ljava/lang/String;)J", "parseIsoString", "parseIsoString-UwyO8pc", "parseIsoStringOrNull", "parseIsoStringOrNull-FghU774", "parseOrNull", "parseOrNull-FghU774", "seconds-UwyO8pc", "kotlin-stdlib"})
    public static final class Companion {
        private Companion() {
        }

        public final long getZERO-UwyO8pc() {
            return ZERO;
        }

        public final long getINFINITE-UwyO8pc() {
            return INFINITE;
        }

        public final long getNEG_INFINITE-UwyO8pc$kotlin_stdlib() {
            return NEG_INFINITE;
        }

        @ExperimentalTime
        public final double convert(double value, @NotNull DurationUnit sourceUnit, @NotNull DurationUnit targetUnit) {
            Intrinsics.checkNotNullParameter((Object)sourceUnit, "sourceUnit");
            Intrinsics.checkNotNullParameter((Object)targetUnit, "targetUnit");
            return DurationUnitKt.convertDurationUnit(value, sourceUnit, targetUnit);
        }

        private final long getNanoseconds-UwyO8pc(int $this$nanoseconds) {
            return DurationKt.toDuration($this$nanoseconds, DurationUnit.NANOSECONDS);
        }

        @InlineOnly
        public static /* synthetic */ void getNanoseconds-UwyO8pc$annotations(int n) {
        }

        private final long getNanoseconds-UwyO8pc(long $this$nanoseconds) {
            return DurationKt.toDuration($this$nanoseconds, DurationUnit.NANOSECONDS);
        }

        @InlineOnly
        public static /* synthetic */ void getNanoseconds-UwyO8pc$annotations(long l) {
        }

        private final long getNanoseconds-UwyO8pc(double $this$nanoseconds) {
            return DurationKt.toDuration($this$nanoseconds, DurationUnit.NANOSECONDS);
        }

        @InlineOnly
        public static /* synthetic */ void getNanoseconds-UwyO8pc$annotations(double d) {
        }

        private final long getMicroseconds-UwyO8pc(int $this$microseconds) {
            return DurationKt.toDuration($this$microseconds, DurationUnit.MICROSECONDS);
        }

        @InlineOnly
        public static /* synthetic */ void getMicroseconds-UwyO8pc$annotations(int n) {
        }

        private final long getMicroseconds-UwyO8pc(long $this$microseconds) {
            return DurationKt.toDuration($this$microseconds, DurationUnit.MICROSECONDS);
        }

        @InlineOnly
        public static /* synthetic */ void getMicroseconds-UwyO8pc$annotations(long l) {
        }

        private final long getMicroseconds-UwyO8pc(double $this$microseconds) {
            return DurationKt.toDuration($this$microseconds, DurationUnit.MICROSECONDS);
        }

        @InlineOnly
        public static /* synthetic */ void getMicroseconds-UwyO8pc$annotations(double d) {
        }

        private final long getMilliseconds-UwyO8pc(int $this$milliseconds) {
            return DurationKt.toDuration($this$milliseconds, DurationUnit.MILLISECONDS);
        }

        @InlineOnly
        public static /* synthetic */ void getMilliseconds-UwyO8pc$annotations(int n) {
        }

        private final long getMilliseconds-UwyO8pc(long $this$milliseconds) {
            return DurationKt.toDuration($this$milliseconds, DurationUnit.MILLISECONDS);
        }

        @InlineOnly
        public static /* synthetic */ void getMilliseconds-UwyO8pc$annotations(long l) {
        }

        private final long getMilliseconds-UwyO8pc(double $this$milliseconds) {
            return DurationKt.toDuration($this$milliseconds, DurationUnit.MILLISECONDS);
        }

        @InlineOnly
        public static /* synthetic */ void getMilliseconds-UwyO8pc$annotations(double d) {
        }

        private final long getSeconds-UwyO8pc(int $this$seconds) {
            return DurationKt.toDuration($this$seconds, DurationUnit.SECONDS);
        }

        @InlineOnly
        public static /* synthetic */ void getSeconds-UwyO8pc$annotations(int n) {
        }

        private final long getSeconds-UwyO8pc(long $this$seconds) {
            return DurationKt.toDuration($this$seconds, DurationUnit.SECONDS);
        }

        @InlineOnly
        public static /* synthetic */ void getSeconds-UwyO8pc$annotations(long l) {
        }

        private final long getSeconds-UwyO8pc(double $this$seconds) {
            return DurationKt.toDuration($this$seconds, DurationUnit.SECONDS);
        }

        @InlineOnly
        public static /* synthetic */ void getSeconds-UwyO8pc$annotations(double d) {
        }

        private final long getMinutes-UwyO8pc(int $this$minutes) {
            return DurationKt.toDuration($this$minutes, DurationUnit.MINUTES);
        }

        @InlineOnly
        public static /* synthetic */ void getMinutes-UwyO8pc$annotations(int n) {
        }

        private final long getMinutes-UwyO8pc(long $this$minutes) {
            return DurationKt.toDuration($this$minutes, DurationUnit.MINUTES);
        }

        @InlineOnly
        public static /* synthetic */ void getMinutes-UwyO8pc$annotations(long l) {
        }

        private final long getMinutes-UwyO8pc(double $this$minutes) {
            return DurationKt.toDuration($this$minutes, DurationUnit.MINUTES);
        }

        @InlineOnly
        public static /* synthetic */ void getMinutes-UwyO8pc$annotations(double d) {
        }

        private final long getHours-UwyO8pc(int $this$hours) {
            return DurationKt.toDuration($this$hours, DurationUnit.HOURS);
        }

        @InlineOnly
        public static /* synthetic */ void getHours-UwyO8pc$annotations(int n) {
        }

        private final long getHours-UwyO8pc(long $this$hours) {
            return DurationKt.toDuration($this$hours, DurationUnit.HOURS);
        }

        @InlineOnly
        public static /* synthetic */ void getHours-UwyO8pc$annotations(long l) {
        }

        private final long getHours-UwyO8pc(double $this$hours) {
            return DurationKt.toDuration($this$hours, DurationUnit.HOURS);
        }

        @InlineOnly
        public static /* synthetic */ void getHours-UwyO8pc$annotations(double d) {
        }

        private final long getDays-UwyO8pc(int $this$days) {
            return DurationKt.toDuration($this$days, DurationUnit.DAYS);
        }

        @InlineOnly
        public static /* synthetic */ void getDays-UwyO8pc$annotations(int n) {
        }

        private final long getDays-UwyO8pc(long $this$days) {
            return DurationKt.toDuration($this$days, DurationUnit.DAYS);
        }

        @InlineOnly
        public static /* synthetic */ void getDays-UwyO8pc$annotations(long l) {
        }

        private final long getDays-UwyO8pc(double $this$days) {
            return DurationKt.toDuration($this$days, DurationUnit.DAYS);
        }

        @InlineOnly
        public static /* synthetic */ void getDays-UwyO8pc$annotations(double d) {
        }

        @Deprecated(message="Use 'Int.nanoseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.nanoseconds", imports={"kotlin.time.Duration.Companion.nanoseconds"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long nanoseconds-UwyO8pc(int value) {
            return DurationKt.toDuration(value, DurationUnit.NANOSECONDS);
        }

        @Deprecated(message="Use 'Long.nanoseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.nanoseconds", imports={"kotlin.time.Duration.Companion.nanoseconds"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long nanoseconds-UwyO8pc(long value) {
            return DurationKt.toDuration(value, DurationUnit.NANOSECONDS);
        }

        @Deprecated(message="Use 'Double.nanoseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.nanoseconds", imports={"kotlin.time.Duration.Companion.nanoseconds"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long nanoseconds-UwyO8pc(double value) {
            return DurationKt.toDuration(value, DurationUnit.NANOSECONDS);
        }

        @Deprecated(message="Use 'Int.microseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.microseconds", imports={"kotlin.time.Duration.Companion.microseconds"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long microseconds-UwyO8pc(int value) {
            return DurationKt.toDuration(value, DurationUnit.MICROSECONDS);
        }

        @Deprecated(message="Use 'Long.microseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.microseconds", imports={"kotlin.time.Duration.Companion.microseconds"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long microseconds-UwyO8pc(long value) {
            return DurationKt.toDuration(value, DurationUnit.MICROSECONDS);
        }

        @Deprecated(message="Use 'Double.microseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.microseconds", imports={"kotlin.time.Duration.Companion.microseconds"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long microseconds-UwyO8pc(double value) {
            return DurationKt.toDuration(value, DurationUnit.MICROSECONDS);
        }

        @Deprecated(message="Use 'Int.milliseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.milliseconds", imports={"kotlin.time.Duration.Companion.milliseconds"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long milliseconds-UwyO8pc(int value) {
            return DurationKt.toDuration(value, DurationUnit.MILLISECONDS);
        }

        @Deprecated(message="Use 'Long.milliseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.milliseconds", imports={"kotlin.time.Duration.Companion.milliseconds"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long milliseconds-UwyO8pc(long value) {
            return DurationKt.toDuration(value, DurationUnit.MILLISECONDS);
        }

        @Deprecated(message="Use 'Double.milliseconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.milliseconds", imports={"kotlin.time.Duration.Companion.milliseconds"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long milliseconds-UwyO8pc(double value) {
            return DurationKt.toDuration(value, DurationUnit.MILLISECONDS);
        }

        @Deprecated(message="Use 'Int.seconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.seconds", imports={"kotlin.time.Duration.Companion.seconds"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long seconds-UwyO8pc(int value) {
            return DurationKt.toDuration(value, DurationUnit.SECONDS);
        }

        @Deprecated(message="Use 'Long.seconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.seconds", imports={"kotlin.time.Duration.Companion.seconds"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long seconds-UwyO8pc(long value) {
            return DurationKt.toDuration(value, DurationUnit.SECONDS);
        }

        @Deprecated(message="Use 'Double.seconds' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.seconds", imports={"kotlin.time.Duration.Companion.seconds"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long seconds-UwyO8pc(double value) {
            return DurationKt.toDuration(value, DurationUnit.SECONDS);
        }

        @Deprecated(message="Use 'Int.minutes' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.minutes", imports={"kotlin.time.Duration.Companion.minutes"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long minutes-UwyO8pc(int value) {
            return DurationKt.toDuration(value, DurationUnit.MINUTES);
        }

        @Deprecated(message="Use 'Long.minutes' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.minutes", imports={"kotlin.time.Duration.Companion.minutes"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long minutes-UwyO8pc(long value) {
            return DurationKt.toDuration(value, DurationUnit.MINUTES);
        }

        @Deprecated(message="Use 'Double.minutes' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.minutes", imports={"kotlin.time.Duration.Companion.minutes"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long minutes-UwyO8pc(double value) {
            return DurationKt.toDuration(value, DurationUnit.MINUTES);
        }

        @Deprecated(message="Use 'Int.hours' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.hours", imports={"kotlin.time.Duration.Companion.hours"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long hours-UwyO8pc(int value) {
            return DurationKt.toDuration(value, DurationUnit.HOURS);
        }

        @Deprecated(message="Use 'Long.hours' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.hours", imports={"kotlin.time.Duration.Companion.hours"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long hours-UwyO8pc(long value) {
            return DurationKt.toDuration(value, DurationUnit.HOURS);
        }

        @Deprecated(message="Use 'Double.hours' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.hours", imports={"kotlin.time.Duration.Companion.hours"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long hours-UwyO8pc(double value) {
            return DurationKt.toDuration(value, DurationUnit.HOURS);
        }

        @Deprecated(message="Use 'Int.days' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.days", imports={"kotlin.time.Duration.Companion.days"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long days-UwyO8pc(int value) {
            return DurationKt.toDuration(value, DurationUnit.DAYS);
        }

        @Deprecated(message="Use 'Long.days' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.days", imports={"kotlin.time.Duration.Companion.days"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long days-UwyO8pc(long value) {
            return DurationKt.toDuration(value, DurationUnit.DAYS);
        }

        @Deprecated(message="Use 'Double.days' extension property from Duration.Companion instead.", replaceWith=@ReplaceWith(expression="value.days", imports={"kotlin.time.Duration.Companion.days"}))
        @DeprecatedSinceKotlin(warningSince="1.6")
        @SinceKotlin(version="1.5")
        @ExperimentalTime
        public final long days-UwyO8pc(double value) {
            return DurationKt.toDuration(value, DurationUnit.DAYS);
        }

        public final long parse-UwyO8pc(@NotNull String value) {
            long l;
            Intrinsics.checkNotNullParameter(value, "value");
            try {
                l = DurationKt.access$parseDuration(value, false);
            }
            catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid duration string format: '" + value + "'.", e);
            }
            return l;
        }

        public final long parseIsoString-UwyO8pc(@NotNull String value) {
            long l;
            Intrinsics.checkNotNullParameter(value, "value");
            try {
                l = DurationKt.access$parseDuration(value, true);
            }
            catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid ISO duration string format: '" + value + "'.", e);
            }
            return l;
        }

        @Nullable
        public final Duration parseOrNull-FghU774(@NotNull String value) {
            Duration duration;
            Intrinsics.checkNotNullParameter(value, "value");
            try {
                duration = Duration.box-impl(DurationKt.access$parseDuration(value, false));
            }
            catch (IllegalArgumentException e) {
                duration = null;
            }
            return duration;
        }

        @Nullable
        public final Duration parseIsoStringOrNull-FghU774(@NotNull String value) {
            Duration duration;
            Intrinsics.checkNotNullParameter(value, "value");
            try {
                duration = Duration.box-impl(DurationKt.access$parseDuration(value, true));
            }
            catch (IllegalArgumentException e) {
                duration = null;
            }
            return duration;
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

