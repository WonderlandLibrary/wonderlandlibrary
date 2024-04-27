/*
 * Decompiled with CFR 0.152.
 */
package kotlin.internal;

import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UnsignedKt;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006\u001a*\u0010\u0000\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0007H\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\b\u0010\t\u001a*\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000f\u0010\u0006\u001a*\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0010H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0012"}, d2={"differenceModulo", "Lkotlin/UInt;", "a", "b", "c", "differenceModulo-WZ9TVnA", "(III)I", "Lkotlin/ULong;", "differenceModulo-sambcqE", "(JJJ)J", "getProgressionLastElement", "start", "end", "step", "", "getProgressionLastElement-Nkh28Cs", "", "getProgressionLastElement-7ftBX0g", "kotlin-stdlib"})
public final class UProgressionUtilKt {
    private static final int differenceModulo-WZ9TVnA(int a, int b, int c) {
        int n;
        int bc;
        int ac = UnsignedKt.uintRemainder-J1ME1BU(a, c);
        if (UnsignedKt.uintCompare(ac, bc = UnsignedKt.uintRemainder-J1ME1BU(b, c)) >= 0) {
            n = UInt.constructor-impl(ac - bc);
        } else {
            int n2 = UInt.constructor-impl(ac - bc);
            n = UInt.constructor-impl(n2 + c);
        }
        return n;
    }

    private static final long differenceModulo-sambcqE(long a, long b, long c) {
        long l;
        long bc;
        long ac = UnsignedKt.ulongRemainder-eb3DHEI(a, c);
        if (UnsignedKt.ulongCompare(ac, bc = UnsignedKt.ulongRemainder-eb3DHEI(b, c)) >= 0) {
            l = ULong.constructor-impl(ac - bc);
        } else {
            long l2 = ULong.constructor-impl(ac - bc);
            l = ULong.constructor-impl(l2 + c);
        }
        return l;
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    public static final int getProgressionLastElement-Nkh28Cs(int start, int end, int step) {
        int n;
        if (step > 0) {
            if (UnsignedKt.uintCompare(start, end) >= 0) {
                n = end;
            } else {
                int n2 = step;
                n2 = UProgressionUtilKt.differenceModulo-WZ9TVnA(end, start, UInt.constructor-impl(n2));
                n = UInt.constructor-impl(end - n2);
            }
        } else if (step < 0) {
            if (UnsignedKt.uintCompare(start, end) <= 0) {
                n = end;
            } else {
                int n3 = -step;
                n3 = UProgressionUtilKt.differenceModulo-WZ9TVnA(start, end, UInt.constructor-impl(n3));
                n = UInt.constructor-impl(end + n3);
            }
        } else {
            throw new IllegalArgumentException("Step is zero.");
        }
        return n;
    }

    @PublishedApi
    @SinceKotlin(version="1.3")
    public static final long getProgressionLastElement-7ftBX0g(long start, long end, long step) {
        long l;
        if (step > 0L) {
            if (UnsignedKt.ulongCompare(start, end) >= 0) {
                l = end;
            } else {
                long l2 = step;
                l2 = UProgressionUtilKt.differenceModulo-sambcqE(end, start, ULong.constructor-impl(l2));
                l = ULong.constructor-impl(end - l2);
            }
        } else if (step < 0L) {
            if (UnsignedKt.ulongCompare(start, end) <= 0) {
                l = end;
            } else {
                long l3 = -step;
                l3 = UProgressionUtilKt.differenceModulo-sambcqE(start, end, ULong.constructor-impl(l3));
                l = ULong.constructor-impl(end + l3);
            }
        } else {
            throw new IllegalArgumentException("Step is zero.");
        }
        return l;
    }
}

