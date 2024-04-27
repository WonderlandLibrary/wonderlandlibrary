/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.random;

import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UnsignedKt;
import kotlin.WasExperimental;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.random.RandomKt;
import kotlin.ranges.UIntRange;
import kotlin.ranges.ULongRange;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\"\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006\u001a\"\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0000\u00f8\u0001\u0000\u00a2\u0006\u0004\b\t\u0010\n\u001a\u001c\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010\u001a\u001e\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0012\u0010\u0013\u001a2\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0014\u001a\u00020\u000f2\b\b\u0002\u0010\u0015\u001a\u00020\u000fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0016\u0010\u0017\u001a\u0014\u0010\u0018\u001a\u00020\u0003*\u00020\rH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019\u001a\u001e\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0003H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001a\u0010\u001b\u001a&\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001c\u0010\u001d\u001a\u001c\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u001e\u001a\u00020\u001fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010 \u001a\u0014\u0010!\u001a\u00020\b*\u00020\rH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\"\u001a\u001e\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0004\u001a\u00020\bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b#\u0010$\u001a&\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b%\u0010&\u001a\u001c\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u001e\u001a\u00020'H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010(\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006)"}, d2={"checkUIntRangeBounds", "", "from", "Lkotlin/UInt;", "until", "checkUIntRangeBounds-J1ME1BU", "(II)V", "checkULongRangeBounds", "Lkotlin/ULong;", "checkULongRangeBounds-eb3DHEI", "(JJ)V", "nextUBytes", "Lkotlin/UByteArray;", "Lkotlin/random/Random;", "size", "", "(Lkotlin/random/Random;I)[B", "array", "nextUBytes-EVgfTAA", "(Lkotlin/random/Random;[B)[B", "fromIndex", "toIndex", "nextUBytes-Wvrt4B4", "(Lkotlin/random/Random;[BII)[B", "nextUInt", "(Lkotlin/random/Random;)I", "nextUInt-qCasIEU", "(Lkotlin/random/Random;I)I", "nextUInt-a8DCA5k", "(Lkotlin/random/Random;II)I", "range", "Lkotlin/ranges/UIntRange;", "(Lkotlin/random/Random;Lkotlin/ranges/UIntRange;)I", "nextULong", "(Lkotlin/random/Random;)J", "nextULong-V1Xi4fY", "(Lkotlin/random/Random;J)J", "nextULong-jmpaW-c", "(Lkotlin/random/Random;JJ)J", "Lkotlin/ranges/ULongRange;", "(Lkotlin/random/Random;Lkotlin/ranges/ULongRange;)J", "kotlin-stdlib"})
public final class URandomKt {
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int nextUInt(@NotNull Random $this$nextUInt) {
        Intrinsics.checkNotNullParameter($this$nextUInt, "<this>");
        return UInt.constructor-impl($this$nextUInt.nextInt());
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int nextUInt-qCasIEU(@NotNull Random $this$nextUInt, int until) {
        Intrinsics.checkNotNullParameter($this$nextUInt, "$this$nextUInt");
        return URandomKt.nextUInt-a8DCA5k($this$nextUInt, 0, until);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int nextUInt-a8DCA5k(@NotNull Random $this$nextUInt, int from, int until) {
        int signedResult;
        Intrinsics.checkNotNullParameter($this$nextUInt, "$this$nextUInt");
        URandomKt.checkUIntRangeBounds-J1ME1BU(from, until);
        int signedFrom = from ^ Integer.MIN_VALUE;
        int signedUntil = until ^ Integer.MIN_VALUE;
        int n = signedResult = $this$nextUInt.nextInt(signedFrom, signedUntil) ^ Integer.MIN_VALUE;
        return UInt.constructor-impl(n);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int nextUInt(@NotNull Random $this$nextUInt, @NotNull UIntRange range) {
        Intrinsics.checkNotNullParameter($this$nextUInt, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        if (range.isEmpty()) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("Cannot get random in empty range: ", range));
        }
        return UnsignedKt.uintCompare(range.getLast-pVg5ArA(), -1) < 0 ? URandomKt.nextUInt-a8DCA5k($this$nextUInt, range.getFirst-pVg5ArA(), UInt.constructor-impl(range.getLast-pVg5ArA() + 1)) : (UnsignedKt.uintCompare(range.getFirst-pVg5ArA(), 0) > 0 ? UInt.constructor-impl(URandomKt.nextUInt-a8DCA5k($this$nextUInt, UInt.constructor-impl(range.getFirst-pVg5ArA() - 1), range.getLast-pVg5ArA()) + 1) : URandomKt.nextUInt($this$nextUInt));
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long nextULong(@NotNull Random $this$nextULong) {
        Intrinsics.checkNotNullParameter($this$nextULong, "<this>");
        return ULong.constructor-impl($this$nextULong.nextLong());
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long nextULong-V1Xi4fY(@NotNull Random $this$nextULong, long until) {
        Intrinsics.checkNotNullParameter($this$nextULong, "$this$nextULong");
        return URandomKt.nextULong-jmpaW-c($this$nextULong, 0L, until);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long nextULong-jmpaW-c(@NotNull Random $this$nextULong, long from, long until) {
        long signedResult;
        Intrinsics.checkNotNullParameter($this$nextULong, "$this$nextULong");
        URandomKt.checkULongRangeBounds-eb3DHEI(from, until);
        long signedFrom = from ^ Long.MIN_VALUE;
        long signedUntil = until ^ Long.MIN_VALUE;
        long l = signedResult = $this$nextULong.nextLong(signedFrom, signedUntil) ^ Long.MIN_VALUE;
        return ULong.constructor-impl(l);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long nextULong(@NotNull Random $this$nextULong, @NotNull ULongRange range) {
        long l;
        Intrinsics.checkNotNullParameter($this$nextULong, "<this>");
        Intrinsics.checkNotNullParameter(range, "range");
        if (range.isEmpty()) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("Cannot get random in empty range: ", range));
        }
        if (UnsignedKt.ulongCompare(range.getLast-s-VKNKU(), -1L) < 0) {
            long l2 = range.getLast-s-VKNKU();
            int n = 1;
            long l3 = ULong.constructor-impl((long)n & 0xFFFFFFFFL);
            l = URandomKt.nextULong-jmpaW-c($this$nextULong, range.getFirst-s-VKNKU(), ULong.constructor-impl(l2 + l3));
        } else if (UnsignedKt.ulongCompare(range.getFirst-s-VKNKU(), 0L) > 0) {
            long l4 = range.getFirst-s-VKNKU();
            int n = 1;
            long l5 = ULong.constructor-impl((long)n & 0xFFFFFFFFL);
            l4 = URandomKt.nextULong-jmpaW-c($this$nextULong, ULong.constructor-impl(l4 - l5), range.getLast-s-VKNKU());
            n = 1;
            l5 = ULong.constructor-impl((long)n & 0xFFFFFFFFL);
            l = ULong.constructor-impl(l4 + l5);
        } else {
            l = URandomKt.nextULong($this$nextULong);
        }
        return l;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] nextUBytes-EVgfTAA(@NotNull Random $this$nextUBytes, @NotNull byte[] array) {
        Intrinsics.checkNotNullParameter($this$nextUBytes, "$this$nextUBytes");
        Intrinsics.checkNotNullParameter(array, "array");
        $this$nextUBytes.nextBytes(array);
        return array;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] nextUBytes(@NotNull Random $this$nextUBytes, int size) {
        Intrinsics.checkNotNullParameter($this$nextUBytes, "<this>");
        return UByteArray.constructor-impl($this$nextUBytes.nextBytes(size));
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] nextUBytes-Wvrt4B4(@NotNull Random $this$nextUBytes, @NotNull byte[] array, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$nextUBytes, "$this$nextUBytes");
        Intrinsics.checkNotNullParameter(array, "array");
        $this$nextUBytes.nextBytes(array, fromIndex, toIndex);
        return array;
    }

    public static /* synthetic */ byte[] nextUBytes-Wvrt4B4$default(Random random, byte[] byArray, int n, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            n2 = UByteArray.getSize-impl(byArray);
        }
        return URandomKt.nextUBytes-Wvrt4B4(random, byArray, n, n2);
    }

    public static final void checkUIntRangeBounds-J1ME1BU(int from, int until) {
        boolean bl;
        boolean bl2 = bl = UnsignedKt.uintCompare(until, from) > 0;
        if (!bl) {
            boolean bl3 = false;
            String string = RandomKt.boundsErrorMessage(UInt.box-impl(from), UInt.box-impl(until));
            throw new IllegalArgumentException(string.toString());
        }
    }

    public static final void checkULongRangeBounds-eb3DHEI(long from, long until) {
        boolean bl;
        boolean bl2 = bl = UnsignedKt.ulongCompare(until, from) > 0;
        if (!bl) {
            boolean bl3 = false;
            String string = RandomKt.boundsErrorMessage(ULong.box-impl(from), ULong.box-impl(until));
            throw new IllegalArgumentException(string.toString());
        }
    }
}

