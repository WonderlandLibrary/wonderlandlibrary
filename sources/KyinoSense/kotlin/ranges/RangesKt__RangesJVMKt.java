/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.ranges;

import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.ranges.ClosedFloatRange;
import kotlin.ranges.ClosedFloatingPointRange;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\u001a\u001b\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\u0002\u00a8\u0006\u0004"}, d2={"rangeTo", "Lkotlin/ranges/ClosedFloatingPointRange;", "", "that", "kotlin-stdlib"}, xs="kotlin/ranges/RangesKt")
class RangesKt__RangesJVMKt {
    @SinceKotlin(version="1.1")
    @NotNull
    public static final ClosedFloatingPointRange<Float> rangeTo(float $this$rangeTo, float that) {
        return new ClosedFloatRange($this$rangeTo, that);
    }
}

