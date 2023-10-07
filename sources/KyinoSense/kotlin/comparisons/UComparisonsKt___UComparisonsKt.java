/*
 * Decompiled with CFR 0.152.
 */
package kotlin.comparisons;

import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UnsignedKt;
import kotlin.comparisons.UComparisonsKt;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000e\u001a\"\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0004\u0010\u0005\u001a+\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0007\u0010\b\u001a\"\u0010\u0000\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\tH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\n\u0010\u000b\u001a+\u0010\u0000\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\tH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\f\u0010\r\u001a\"\u0010\u0000\u001a\u00020\u000e2\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0003\u001a\u00020\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000f\u0010\u0010\u001a+\u0010\u0000\u001a\u00020\u000e2\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0003\u001a\u00020\u000e2\u0006\u0010\u0006\u001a\u00020\u000eH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\u0012\u001a\"\u0010\u0000\u001a\u00020\u00132\u0006\u0010\u0002\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u0013H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0014\u0010\u0015\u001a+\u0010\u0000\u001a\u00020\u00132\u0006\u0010\u0002\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0013H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0016\u0010\u0017\u001a\"\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0019\u0010\u0005\u001a+\u0010\u0018\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0006\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001a\u0010\b\u001a\"\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\tH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001b\u0010\u000b\u001a+\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0002\u001a\u00020\t2\u0006\u0010\u0003\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\tH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001c\u0010\r\u001a\"\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0003\u001a\u00020\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001d\u0010\u0010\u001a+\u0010\u0018\u001a\u00020\u000e2\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0003\u001a\u00020\u000e2\u0006\u0010\u0006\u001a\u00020\u000eH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u0012\u001a\"\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0002\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u0013H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001f\u0010\u0015\u001a+\u0010\u0018\u001a\u00020\u00132\u0006\u0010\u0002\u001a\u00020\u00132\u0006\u0010\u0003\u001a\u00020\u00132\u0006\u0010\u0006\u001a\u00020\u0013H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b \u0010\u0017\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006!"}, d2={"maxOf", "Lkotlin/UByte;", "a", "b", "maxOf-Kr8caGY", "(BB)B", "c", "maxOf-b33U2AM", "(BBB)B", "Lkotlin/UInt;", "maxOf-J1ME1BU", "(II)I", "maxOf-WZ9TVnA", "(III)I", "Lkotlin/ULong;", "maxOf-eb3DHEI", "(JJ)J", "maxOf-sambcqE", "(JJJ)J", "Lkotlin/UShort;", "maxOf-5PvTz6A", "(SS)S", "maxOf-VKSA0NQ", "(SSS)S", "minOf", "minOf-Kr8caGY", "minOf-b33U2AM", "minOf-J1ME1BU", "minOf-WZ9TVnA", "minOf-eb3DHEI", "minOf-sambcqE", "minOf-5PvTz6A", "minOf-VKSA0NQ", "kotlin-stdlib"}, xs="kotlin/comparisons/UComparisonsKt")
class UComparisonsKt___UComparisonsKt {
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final int maxOf-J1ME1BU(int a, int b) {
        int n = a;
        boolean bl = false;
        return UnsignedKt.uintCompare(n, b) >= 0 ? a : b;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final long maxOf-eb3DHEI(long a, long b) {
        long l = a;
        boolean bl = false;
        return UnsignedKt.ulongCompare(l, b) >= 0 ? a : b;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final byte maxOf-Kr8caGY(byte a, byte b) {
        byte by = a;
        boolean bl = false;
        byte by2 = by;
        boolean bl2 = false;
        int n = by2 & 0xFF;
        by2 = b;
        int n2 = n;
        bl2 = false;
        int n3 = by2 & 0xFF;
        return Intrinsics.compare(n2, n3) >= 0 ? a : b;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final short maxOf-5PvTz6A(short a, short b) {
        short s = a;
        boolean bl = false;
        short s2 = s;
        boolean bl2 = false;
        int n = s2 & 0xFFFF;
        s2 = b;
        int n2 = n;
        bl2 = false;
        int n3 = s2 & 0xFFFF;
        return Intrinsics.compare(n2, n3) >= 0 ? a : b;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int maxOf-WZ9TVnA(int a, int b, int c) {
        int n = 0;
        return UComparisonsKt.maxOf-J1ME1BU(a, UComparisonsKt.maxOf-J1ME1BU(b, c));
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long maxOf-sambcqE(long a, long b, long c) {
        int n = 0;
        return UComparisonsKt.maxOf-eb3DHEI(a, UComparisonsKt.maxOf-eb3DHEI(b, c));
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final byte maxOf-b33U2AM(byte a, byte b, byte c) {
        int n = 0;
        return UComparisonsKt.maxOf-Kr8caGY(a, UComparisonsKt.maxOf-Kr8caGY(b, c));
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final short maxOf-VKSA0NQ(short a, short b, short c) {
        int n = 0;
        return UComparisonsKt.maxOf-5PvTz6A(a, UComparisonsKt.maxOf-5PvTz6A(b, c));
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final int minOf-J1ME1BU(int a, int b) {
        int n = a;
        boolean bl = false;
        return UnsignedKt.uintCompare(n, b) <= 0 ? a : b;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final long minOf-eb3DHEI(long a, long b) {
        long l = a;
        boolean bl = false;
        return UnsignedKt.ulongCompare(l, b) <= 0 ? a : b;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final byte minOf-Kr8caGY(byte a, byte b) {
        byte by = a;
        boolean bl = false;
        byte by2 = by;
        boolean bl2 = false;
        int n = by2 & 0xFF;
        by2 = b;
        int n2 = n;
        bl2 = false;
        int n3 = by2 & 0xFF;
        return Intrinsics.compare(n2, n3) <= 0 ? a : b;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    public static final short minOf-5PvTz6A(short a, short b) {
        short s = a;
        boolean bl = false;
        short s2 = s;
        boolean bl2 = false;
        int n = s2 & 0xFFFF;
        s2 = b;
        int n2 = n;
        bl2 = false;
        int n3 = s2 & 0xFFFF;
        return Intrinsics.compare(n2, n3) <= 0 ? a : b;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final int minOf-WZ9TVnA(int a, int b, int c) {
        int n = 0;
        return UComparisonsKt.minOf-J1ME1BU(a, UComparisonsKt.minOf-J1ME1BU(b, c));
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final long minOf-sambcqE(long a, long b, long c) {
        int n = 0;
        return UComparisonsKt.minOf-eb3DHEI(a, UComparisonsKt.minOf-eb3DHEI(b, c));
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final byte minOf-b33U2AM(byte a, byte b, byte c) {
        int n = 0;
        return UComparisonsKt.minOf-Kr8caGY(a, UComparisonsKt.minOf-Kr8caGY(b, c));
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @InlineOnly
    private static final short minOf-VKSA0NQ(short a, short b, short c) {
        int n = 0;
        return UComparisonsKt.minOf-5PvTz6A(a, UComparisonsKt.minOf-5PvTz6A(b, c));
    }
}

