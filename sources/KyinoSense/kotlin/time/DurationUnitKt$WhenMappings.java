/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=3)
public final class DurationUnitKt$WhenMappings {
    public static final /* synthetic */ int[] $EnumSwitchMapping$0;

    static {
        $EnumSwitchMapping$0 = new int[TimeUnit.values().length];
        DurationUnitKt$WhenMappings.$EnumSwitchMapping$0[TimeUnit.NANOSECONDS.ordinal()] = 1;
        DurationUnitKt$WhenMappings.$EnumSwitchMapping$0[TimeUnit.MICROSECONDS.ordinal()] = 2;
        DurationUnitKt$WhenMappings.$EnumSwitchMapping$0[TimeUnit.MILLISECONDS.ordinal()] = 3;
        DurationUnitKt$WhenMappings.$EnumSwitchMapping$0[TimeUnit.SECONDS.ordinal()] = 4;
        DurationUnitKt$WhenMappings.$EnumSwitchMapping$0[TimeUnit.MINUTES.ordinal()] = 5;
        DurationUnitKt$WhenMappings.$EnumSwitchMapping$0[TimeUnit.HOURS.ordinal()] = 6;
        DurationUnitKt$WhenMappings.$EnumSwitchMapping$0[TimeUnit.DAYS.ordinal()] = 7;
    }
}

