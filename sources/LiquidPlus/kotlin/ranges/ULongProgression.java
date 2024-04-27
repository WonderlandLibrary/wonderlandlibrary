/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.ranges;

import java.util.Iterator;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.ULong;
import kotlin.UnsignedKt;
import kotlin.WasExperimental;
import kotlin.internal.UProgressionUtilKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.ranges.ULongProgressionIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0017\u0018\u0000 \u001a2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u001aB\"\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J\u0013\u0010\u000f\u001a\u00020\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0096\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0016J\b\u0010\u0015\u001a\u00020\u0010H\u0016J\u0012\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00020\u0017H\u0086\u0002\u00f8\u0001\u0000J\b\u0010\u0018\u001a\u00020\u0019H\u0016R\u0019\u0010\b\u001a\u00020\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\t\u0010\nR\u0019\u0010\f\u001a\u00020\u0002\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\n\n\u0002\u0010\u000b\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\n\u00f8\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006\u001b"}, d2={"Lkotlin/ranges/ULongProgression;", "", "Lkotlin/ULong;", "start", "endInclusive", "step", "", "(JJJLkotlin/jvm/internal/DefaultConstructorMarker;)V", "first", "getFirst-s-VKNKU", "()J", "J", "last", "getLast-s-VKNKU", "getStep", "equals", "", "other", "", "hashCode", "", "isEmpty", "iterator", "", "toString", "", "Companion", "kotlin-stdlib"})
@SinceKotlin(version="1.5")
@WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
public class ULongProgression
implements Iterable<ULong>,
KMappedMarker {
    @NotNull
    public static final Companion Companion = new Companion(null);
    private final long first;
    private final long last;
    private final long step;

    private ULongProgression(long start, long endInclusive, long step) {
        if (step == 0L) {
            throw new IllegalArgumentException("Step must be non-zero.");
        }
        if (step == Long.MIN_VALUE) {
            throw new IllegalArgumentException("Step must be greater than Long.MIN_VALUE to avoid overflow on negation.");
        }
        this.first = start;
        this.last = UProgressionUtilKt.getProgressionLastElement-7ftBX0g(start, endInclusive, step);
        this.step = step;
    }

    public final long getFirst-s-VKNKU() {
        return this.first;
    }

    public final long getLast-s-VKNKU() {
        return this.last;
    }

    public final long getStep() {
        return this.step;
    }

    @Override
    @NotNull
    public final Iterator<ULong> iterator() {
        return new ULongProgressionIterator(this.getFirst-s-VKNKU(), this.getLast-s-VKNKU(), this.step, null);
    }

    public boolean isEmpty() {
        return this.step > 0L ? UnsignedKt.ulongCompare(this.getFirst-s-VKNKU(), this.getLast-s-VKNKU()) > 0 : UnsignedKt.ulongCompare(this.getFirst-s-VKNKU(), this.getLast-s-VKNKU()) < 0;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ULongProgression && (this.isEmpty() && ((ULongProgression)other).isEmpty() || this.getFirst-s-VKNKU() == ((ULongProgression)other).getFirst-s-VKNKU() && this.getLast-s-VKNKU() == ((ULongProgression)other).getLast-s-VKNKU() && this.step == ((ULongProgression)other).step);
    }

    public int hashCode() {
        return this.isEmpty() ? -1 : 31 * (31 * (int)ULong.constructor-impl(this.getFirst-s-VKNKU() ^ ULong.constructor-impl(this.getFirst-s-VKNKU() >>> 32)) + (int)ULong.constructor-impl(this.getLast-s-VKNKU() ^ ULong.constructor-impl(this.getLast-s-VKNKU() >>> 32))) + (int)(this.step ^ this.step >>> 32);
    }

    @NotNull
    public String toString() {
        return this.step > 0L ? ULong.toString-impl(this.getFirst-s-VKNKU()) + ".." + ULong.toString-impl(this.getLast-s-VKNKU()) + " step " + this.step : ULong.toString-impl(this.getFirst-s-VKNKU()) + " downTo " + ULong.toString-impl(this.getLast-s-VKNKU()) + " step " + -this.step;
    }

    public /* synthetic */ ULongProgression(long start, long endInclusive, long step, DefaultConstructorMarker $constructor_marker) {
        this(start, endInclusive, step);
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J(\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t\u00f8\u0001\u0000\u00a2\u0006\u0004\b\n\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\f"}, d2={"Lkotlin/ranges/ULongProgression$Companion;", "", "()V", "fromClosedRange", "Lkotlin/ranges/ULongProgression;", "rangeStart", "Lkotlin/ULong;", "rangeEnd", "step", "", "fromClosedRange-7ftBX0g", "(JJJ)Lkotlin/ranges/ULongProgression;", "kotlin-stdlib"})
    public static final class Companion {
        private Companion() {
        }

        @NotNull
        public final ULongProgression fromClosedRange-7ftBX0g(long rangeStart, long rangeEnd, long step) {
            return new ULongProgression(rangeStart, rangeEnd, step, null);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

