/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.time;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.SinceKotlin;
import kotlin.jvm.internal.Intrinsics;
import kotlin.time.DurationUnit;
import kotlin.time.ExperimentalTime;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000 \n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0001\u001a \u0010\u0000\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0001\u001a \u0010\u0007\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H\u0001\u001a\f\u0010\b\u001a\u00020\u0004*\u00020\tH\u0007\u001a\f\u0010\n\u001a\u00020\t*\u00020\u0004H\u0007\u00a8\u0006\u000b"}, d2={"convertDurationUnit", "", "value", "sourceUnit", "Lkotlin/time/DurationUnit;", "targetUnit", "", "convertDurationUnitOverflow", "toDurationUnit", "Ljava/util/concurrent/TimeUnit;", "toTimeUnit", "kotlin-stdlib"}, xs="kotlin/time/DurationUnitKt")
class DurationUnitKt__DurationUnitJvmKt {
    @SinceKotlin(version="1.6")
    @ExperimentalTime
    @NotNull
    public static final TimeUnit toTimeUnit(@NotNull DurationUnit $this$toTimeUnit) {
        Intrinsics.checkNotNullParameter((Object)$this$toTimeUnit, "<this>");
        return $this$toTimeUnit.getTimeUnit$kotlin_stdlib();
    }

    @SinceKotlin(version="1.6")
    @ExperimentalTime
    @NotNull
    public static final DurationUnit toDurationUnit(@NotNull TimeUnit $this$toDurationUnit) {
        DurationUnit durationUnit;
        Intrinsics.checkNotNullParameter((Object)$this$toDurationUnit, "<this>");
        TimeUnit timeUnit = $this$toDurationUnit;
        int n = WhenMappings.$EnumSwitchMapping$0[timeUnit.ordinal()];
        switch (n) {
            case 1: {
                durationUnit = DurationUnit.NANOSECONDS;
                break;
            }
            case 2: {
                durationUnit = DurationUnit.MICROSECONDS;
                break;
            }
            case 3: {
                durationUnit = DurationUnit.MILLISECONDS;
                break;
            }
            case 4: {
                durationUnit = DurationUnit.SECONDS;
                break;
            }
            case 5: {
                durationUnit = DurationUnit.MINUTES;
                break;
            }
            case 6: {
                durationUnit = DurationUnit.HOURS;
                break;
            }
            case 7: {
                durationUnit = DurationUnit.DAYS;
                break;
            }
            default: {
                throw new NoWhenBranchMatchedException();
            }
        }
        return durationUnit;
    }

    @SinceKotlin(version="1.3")
    public static final double convertDurationUnit(double value, @NotNull DurationUnit sourceUnit, @NotNull DurationUnit targetUnit) {
        Intrinsics.checkNotNullParameter((Object)sourceUnit, "sourceUnit");
        Intrinsics.checkNotNullParameter((Object)targetUnit, "targetUnit");
        long sourceInTargets = targetUnit.getTimeUnit$kotlin_stdlib().convert(1L, sourceUnit.getTimeUnit$kotlin_stdlib());
        if (sourceInTargets > 0L) {
            return value * (double)sourceInTargets;
        }
        long otherInThis = sourceUnit.getTimeUnit$kotlin_stdlib().convert(1L, targetUnit.getTimeUnit$kotlin_stdlib());
        return value / (double)otherInThis;
    }

    @SinceKotlin(version="1.5")
    public static final long convertDurationUnitOverflow(long value, @NotNull DurationUnit sourceUnit, @NotNull DurationUnit targetUnit) {
        Intrinsics.checkNotNullParameter((Object)sourceUnit, "sourceUnit");
        Intrinsics.checkNotNullParameter((Object)targetUnit, "targetUnit");
        return targetUnit.getTimeUnit$kotlin_stdlib().convert(value, sourceUnit.getTimeUnit$kotlin_stdlib());
    }

    @SinceKotlin(version="1.5")
    public static final long convertDurationUnit(long value, @NotNull DurationUnit sourceUnit, @NotNull DurationUnit targetUnit) {
        Intrinsics.checkNotNullParameter((Object)sourceUnit, "sourceUnit");
        Intrinsics.checkNotNullParameter((Object)targetUnit, "targetUnit");
        return targetUnit.getTimeUnit$kotlin_stdlib().convert(value, sourceUnit.getTimeUnit$kotlin_stdlib());
    }

    @Metadata(mv={1, 6, 0}, k=3, xi=48)
    public final class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] nArray = new int[TimeUnit.values().length];
            nArray[TimeUnit.NANOSECONDS.ordinal()] = 1;
            nArray[TimeUnit.MICROSECONDS.ordinal()] = 2;
            nArray[TimeUnit.MILLISECONDS.ordinal()] = 3;
            nArray[TimeUnit.SECONDS.ordinal()] = 4;
            nArray[TimeUnit.MINUTES.ordinal()] = 5;
            nArray[TimeUnit.HOURS.ordinal()] = 6;
            nArray[TimeUnit.DAYS.ordinal()] = 7;
            $EnumSwitchMapping$0 = nArray;
        }
    }
}

