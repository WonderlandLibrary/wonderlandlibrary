/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin;

import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import org.jetbrains.annotations.NotNull;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0010\t\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a\u0018\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0004\u001a\u0018\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u0003H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007\u001a\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0001\u001a\"\u0010\f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\r\u0010\u000e\u001a\"\u0010\u000f\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0010\u0010\u000e\u001a\u0010\u0010\u0011\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\tH\u0001\u001a\u0018\u0010\u0012\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00132\u0006\u0010\u000b\u001a\u00020\u0013H\u0001\u001a\"\u0010\u0014\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016\u001a\"\u0010\u0017\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0018\u0010\u0016\u001a\u0010\u0010\u0019\u001a\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0013H\u0001\u001a\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u0013H\u0000\u001a\u0018\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0002\u001a\u00020\u00132\u0006\u0010\u001c\u001a\u00020\tH\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u001d"}, d2={"doubleToUInt", "Lkotlin/UInt;", "v", "", "(D)I", "doubleToULong", "Lkotlin/ULong;", "(D)J", "uintCompare", "", "v1", "v2", "uintDivide", "uintDivide-J1ME1BU", "(II)I", "uintRemainder", "uintRemainder-J1ME1BU", "uintToDouble", "ulongCompare", "", "ulongDivide", "ulongDivide-eb3DHEI", "(JJ)J", "ulongRemainder", "ulongRemainder-eb3DHEI", "ulongToDouble", "ulongToString", "", "base", "kotlin-stdlib"})
@JvmName(name="UnsignedKt")
public final class UnsignedKt {
    @PublishedApi
    public static final int uintCompare(int v1, int v2) {
        return Intrinsics.compare(v1 ^ Integer.MIN_VALUE, v2 ^ Integer.MIN_VALUE);
    }

    @PublishedApi
    public static final int ulongCompare(long v1, long v2) {
        return Intrinsics.compare(v1 ^ Long.MIN_VALUE, v2 ^ Long.MIN_VALUE);
    }

    @PublishedApi
    public static final int uintDivide-J1ME1BU(int v1, int v2) {
        long l = ((long)v1 & 0xFFFFFFFFL) / ((long)v2 & 0xFFFFFFFFL);
        return UInt.constructor-impl((int)l);
    }

    @PublishedApi
    public static final int uintRemainder-J1ME1BU(int v1, int v2) {
        long l = ((long)v1 & 0xFFFFFFFFL) % ((long)v2 & 0xFFFFFFFFL);
        return UInt.constructor-impl((int)l);
    }

    @PublishedApi
    public static final long ulongDivide-eb3DHEI(long v1, long v2) {
        long l;
        long quotient;
        long dividend = v1;
        long divisor = v2;
        if (divisor < 0L) {
            return UnsignedKt.ulongCompare(v1, v2) < 0 ? ULong.constructor-impl(0L) : ULong.constructor-impl(1L);
        }
        if (dividend >= 0L) {
            return ULong.constructor-impl(dividend / divisor);
        }
        long rem = dividend - (quotient = (dividend >>> 1) / divisor << 1) * divisor;
        long l2 = ULong.constructor-impl(rem);
        return ULong.constructor-impl(quotient + (long)(UnsignedKt.ulongCompare(l2, l = ULong.constructor-impl(divisor)) >= 0 ? 1 : 0));
    }

    @PublishedApi
    public static final long ulongRemainder-eb3DHEI(long v1, long v2) {
        long l;
        long rem;
        long dividend = v1;
        long divisor = v2;
        if (divisor < 0L) {
            return UnsignedKt.ulongCompare(v1, v2) < 0 ? v1 : ULong.constructor-impl(v1 - v2);
        }
        if (dividend >= 0L) {
            return ULong.constructor-impl(dividend % divisor);
        }
        long quotient = (dividend >>> 1) / divisor << 1;
        long l2 = ULong.constructor-impl(rem = dividend - quotient * divisor);
        return ULong.constructor-impl(rem - (UnsignedKt.ulongCompare(l2, l = ULong.constructor-impl(divisor)) >= 0 ? divisor : 0L));
    }

    @PublishedApi
    public static final int doubleToUInt(double v) {
        return Double.isNaN(v) ? 0 : (v <= UnsignedKt.uintToDouble(0) ? 0 : (v >= UnsignedKt.uintToDouble(-1) ? -1 : (v <= 2.147483647E9 ? UInt.constructor-impl((int)v) : UInt.constructor-impl(UInt.constructor-impl((int)(v - (double)Integer.MAX_VALUE)) + UInt.constructor-impl(Integer.MAX_VALUE)))));
    }

    @PublishedApi
    public static final long doubleToULong(double v) {
        return Double.isNaN(v) ? 0L : (v <= UnsignedKt.ulongToDouble(0L) ? 0L : (v >= UnsignedKt.ulongToDouble(-1L) ? -1L : (v < 9.223372036854776E18 ? ULong.constructor-impl((long)v) : ULong.constructor-impl(ULong.constructor-impl((long)(v - 9.223372036854776E18)) + Long.MIN_VALUE))));
    }

    @PublishedApi
    public static final double uintToDouble(int v) {
        return (double)(v & Integer.MAX_VALUE) + (double)(v >>> 31 << 30) * (double)2;
    }

    @PublishedApi
    public static final double ulongToDouble(long v) {
        return (double)(v >>> 11) * (double)2048 + (double)(v & 0x7FFL);
    }

    @NotNull
    public static final String ulongToString(long v) {
        return UnsignedKt.ulongToString(v, 10);
    }

    @NotNull
    public static final String ulongToString(long v, int base) {
        if (v >= 0L) {
            long l = v;
            String string = Long.toString(l, CharsKt.checkRadix(base));
            Intrinsics.checkNotNullExpressionValue(string, "toString(this, checkRadix(radix))");
            return string;
        }
        long quotient = (v >>> 1) / (long)base << 1;
        long rem = v - quotient * (long)base;
        if (rem >= (long)base) {
            rem -= (long)base;
            ++quotient;
        }
        long l = quotient;
        String string = Long.toString(l, CharsKt.checkRadix(base));
        Intrinsics.checkNotNullExpressionValue(string, "toString(this, checkRadix(radix))");
        String string2 = string;
        l = rem;
        string = Long.toString(l, CharsKt.checkRadix(base));
        Intrinsics.checkNotNullExpressionValue(string, "toString(this, checkRadix(radix))");
        return Intrinsics.stringPlus(string2, string);
    }
}

