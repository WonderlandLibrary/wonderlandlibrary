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
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.random.RandomKt;
import kotlin.ranges.UIntRange;
import kotlin.ranges.ULongRange;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=2, d1={"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000f\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\"\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006\u001a\"\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\t\u0010\n\u001a\u001c\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0010\u001a\u001e\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0012\u0010\u0013\u001a2\u0010\u000b\u001a\u00020\f*\u00020\r2\u0006\u0010\u0011\u001a\u00020\f2\b\b\u0002\u0010\u0014\u001a\u00020\u000f2\b\b\u0002\u0010\u0015\u001a\u00020\u000fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0016\u0010\u0017\u001a\u0014\u0010\u0018\u001a\u00020\u0003*\u00020\rH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019\u001a\u001e\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0004\u001a\u00020\u0003H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001a\u0010\u001b\u001a&\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0003H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001c\u0010\u001d\u001a\u001c\u0010\u0018\u001a\u00020\u0003*\u00020\r2\u0006\u0010\u001e\u001a\u00020\u001fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010 \u001a\u0014\u0010!\u001a\u00020\b*\u00020\rH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\"\u001a\u001e\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0004\u001a\u00020\bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b#\u0010$\u001a&\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b%\u0010&\u001a\u001c\u0010!\u001a\u00020\b*\u00020\r2\u0006\u0010\u001e\u001a\u00020'H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010(\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006)"}, d2={"checkUIntRangeBounds", "", "from", "Lkotlin/UInt;", "until", "checkUIntRangeBounds-J1ME1BU", "(II)V", "checkULongRangeBounds", "Lkotlin/ULong;", "checkULongRangeBounds-eb3DHEI", "(JJ)V", "nextUBytes", "Lkotlin/UByteArray;", "Lkotlin/random/Random;", "size", "", "(Lkotlin/random/Random;I)[B", "array", "nextUBytes-EVgfTAA", "(Lkotlin/random/Random;[B)[B", "fromIndex", "toIndex", "nextUBytes-Wvrt4B4", "(Lkotlin/random/Random;[BII)[B", "nextUInt", "(Lkotlin/random/Random;)I", "nextUInt-qCasIEU", "(Lkotlin/random/Random;I)I", "nextUInt-a8DCA5k", "(Lkotlin/random/Random;II)I", "range", "Lkotlin/ranges/UIntRange;", "(Lkotlin/random/Random;Lkotlin/ranges/UIntRange;)I", "nextULong", "(Lkotlin/random/Random;)J", "nextULong-V1Xi4fY", "(Lkotlin/random/Random;J)J", "nextULong-jmpaW-c", "(Lkotlin/random/Random;JJ)J", "Lkotlin/ranges/ULongRange;", "(Lkotlin/random/Random;Lkotlin/ranges/ULongRange;)J", "kotlin-stdlib"})
public final class URandomKt {
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final int nextUInt(@NotNull Random $this$nextUInt) {
        Intrinsics.checkParameterIsNotNull($this$nextUInt, "$this$nextUInt");
        int n = $this$nextUInt.nextInt();
        boolean bl = false;
        return UInt.constructor-impl(n);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final int nextUInt-qCasIEU(@NotNull Random $this$nextUInt, int until) {
        Intrinsics.checkParameterIsNotNull($this$nextUInt, "$this$nextUInt");
        return URandomKt.nextUInt-a8DCA5k($this$nextUInt, 0, until);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final int nextUInt-a8DCA5k(@NotNull Random $this$nextUInt, int from, int until) {
        int signedResult;
        Intrinsics.checkParameterIsNotNull($this$nextUInt, "$this$nextUInt");
        URandomKt.checkUIntRangeBounds-J1ME1BU(from, until);
        int n = from;
        int n2 = 0;
        int signedFrom = n ^ Integer.MIN_VALUE;
        n2 = until;
        int n3 = 0;
        int signedUntil = n2 ^ Integer.MIN_VALUE;
        n3 = signedResult = $this$nextUInt.nextInt(signedFrom, signedUntil) ^ Integer.MIN_VALUE;
        boolean bl = false;
        return UInt.constructor-impl(n3);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final int nextUInt(@NotNull Random $this$nextUInt, @NotNull UIntRange range) {
        int n;
        Intrinsics.checkParameterIsNotNull($this$nextUInt, "$this$nextUInt");
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range.isEmpty()) {
            throw (Throwable)new IllegalArgumentException("Cannot get random in empty range: " + range);
        }
        int n2 = range.getLast();
        int n3 = -1;
        boolean bl = false;
        if (UnsignedKt.uintCompare(n2, n3) < 0) {
            n2 = range.getLast();
            n3 = 1;
            int n4 = range.getFirst();
            Random random = $this$nextUInt;
            bl = false;
            int n5 = UInt.constructor-impl(n2 + n3);
            n = URandomKt.nextUInt-a8DCA5k(random, n4, n5);
        } else {
            n2 = range.getFirst();
            n3 = 0;
            bl = false;
            if (UnsignedKt.uintCompare(n2, n3) > 0) {
                n2 = range.getFirst();
                n3 = 1;
                Random random = $this$nextUInt;
                bl = false;
                int n6 = UInt.constructor-impl(n2 - n3);
                n2 = URandomKt.nextUInt-a8DCA5k(random, n6, range.getLast());
                n3 = 1;
                bl = false;
                n = UInt.constructor-impl(n2 + n3);
            } else {
                n = URandomKt.nextUInt($this$nextUInt);
            }
        }
        return n;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final long nextULong(@NotNull Random $this$nextULong) {
        Intrinsics.checkParameterIsNotNull($this$nextULong, "$this$nextULong");
        long l = $this$nextULong.nextLong();
        boolean bl = false;
        return ULong.constructor-impl(l);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final long nextULong-V1Xi4fY(@NotNull Random $this$nextULong, long until) {
        Intrinsics.checkParameterIsNotNull($this$nextULong, "$this$nextULong");
        return URandomKt.nextULong-jmpaW-c($this$nextULong, 0L, until);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final long nextULong-jmpaW-c(@NotNull Random $this$nextULong, long from, long until) {
        long signedResult;
        Intrinsics.checkParameterIsNotNull($this$nextULong, "$this$nextULong");
        URandomKt.checkULongRangeBounds-eb3DHEI(from, until);
        long l = from;
        boolean bl = false;
        long signedFrom = l ^ Long.MIN_VALUE;
        long l2 = until;
        boolean bl2 = false;
        long signedUntil = l2 ^ Long.MIN_VALUE;
        long l3 = signedResult = $this$nextULong.nextLong(signedFrom, signedUntil) ^ Long.MIN_VALUE;
        boolean bl3 = false;
        return ULong.constructor-impl(l3);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final long nextULong(@NotNull Random $this$nextULong, @NotNull ULongRange range) {
        long l;
        Intrinsics.checkParameterIsNotNull($this$nextULong, "$this$nextULong");
        Intrinsics.checkParameterIsNotNull(range, "range");
        if (range.isEmpty()) {
            throw (Throwable)new IllegalArgumentException("Cannot get random in empty range: " + range);
        }
        long l2 = range.getLast();
        long l3 = -1L;
        boolean bl = false;
        if (UnsignedKt.ulongCompare(l2, l3) < 0) {
            l2 = range.getLast();
            int n = 1;
            long l4 = range.getFirst();
            Random random = $this$nextULong;
            boolean bl2 = false;
            long l5 = l2;
            int n2 = n;
            boolean bl3 = false;
            long l6 = ULong.constructor-impl((long)n2 & 0xFFFFFFFFL);
            boolean bl4 = false;
            long l7 = ULong.constructor-impl(l5 + l6);
            l = URandomKt.nextULong-jmpaW-c(random, l4, l7);
        } else {
            l2 = range.getFirst();
            l3 = 0L;
            bl = false;
            if (UnsignedKt.ulongCompare(l2, l3) > 0) {
                l2 = range.getFirst();
                int n = 1;
                Random random = $this$nextULong;
                boolean bl5 = false;
                long l8 = l2;
                int n3 = n;
                boolean bl6 = false;
                long l9 = ULong.constructor-impl((long)n3 & 0xFFFFFFFFL);
                boolean bl7 = false;
                long l10 = ULong.constructor-impl(l8 - l9);
                l2 = URandomKt.nextULong-jmpaW-c(random, l10, range.getLast());
                n = 1;
                bl5 = false;
                l8 = l2;
                n3 = n;
                bl6 = false;
                l9 = ULong.constructor-impl((long)n3 & 0xFFFFFFFFL);
                bl7 = false;
                l = ULong.constructor-impl(l8 + l9);
            } else {
                l = URandomKt.nextULong($this$nextULong);
            }
        }
        return l;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] nextUBytes-EVgfTAA(@NotNull Random $this$nextUBytes, @NotNull byte[] array) {
        Intrinsics.checkParameterIsNotNull($this$nextUBytes, "$this$nextUBytes");
        Intrinsics.checkParameterIsNotNull(array, "array");
        byte[] byArray = array;
        Random random = $this$nextUBytes;
        boolean bl = false;
        byte[] byArray2 = byArray;
        random.nextBytes(byArray2);
        return array;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] nextUBytes(@NotNull Random $this$nextUBytes, int size) {
        Intrinsics.checkParameterIsNotNull($this$nextUBytes, "$this$nextUBytes");
        byte[] byArray = $this$nextUBytes.nextBytes(size);
        boolean bl = false;
        return UByteArray.constructor-impl(byArray);
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] nextUBytes-Wvrt4B4(@NotNull Random $this$nextUBytes, @NotNull byte[] array, int fromIndex, int toIndex) {
        Intrinsics.checkParameterIsNotNull($this$nextUBytes, "$this$nextUBytes");
        Intrinsics.checkParameterIsNotNull(array, "array");
        byte[] byArray = array;
        Random random = $this$nextUBytes;
        boolean bl = false;
        byte[] byArray2 = byArray;
        random.nextBytes(byArray2, fromIndex, toIndex);
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

    @ExperimentalUnsignedTypes
    public static final void checkUIntRangeBounds-J1ME1BU(int from, int until) {
        int n = until;
        boolean bl = false;
        n = UnsignedKt.uintCompare(n, from) > 0 ? 1 : 0;
        bl = false;
        boolean bl2 = false;
        if (n == 0) {
            boolean bl3 = false;
            String string = RandomKt.boundsErrorMessage(UInt.box-impl(from), UInt.box-impl(until));
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
    }

    @ExperimentalUnsignedTypes
    public static final void checkULongRangeBounds-eb3DHEI(long from, long until) {
        long l = until;
        boolean bl = false;
        boolean bl2 = UnsignedKt.ulongCompare(l, from) > 0;
        boolean bl3 = false;
        bl = false;
        if (!bl2) {
            boolean bl4 = false;
            String string = RandomKt.boundsErrorMessage(ULong.box-impl(from), ULong.box-impl(until));
            throw (Throwable)new IllegalArgumentException(string.toString());
        }
    }
}

