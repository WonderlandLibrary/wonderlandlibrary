/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.ExperimentalStdlibApi;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.NumbersKt;
import kotlin.SinceKotlin;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.WasExperimental;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000&\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b)\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0003\u0010\u0004\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u0005H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0006\u0010\u0007\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\t\u0010\n\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u000bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\f\u0010\r\u001a\u0017\u0010\u000e\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000f\u0010\u0004\u001a\u0017\u0010\u000e\u001a\u00020\u0001*\u00020\u0005H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0010\u0010\u0007\u001a\u0017\u0010\u000e\u001a\u00020\u0001*\u00020\bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\n\u001a\u0017\u0010\u000e\u001a\u00020\u0001*\u00020\u000bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0012\u0010\r\u001a\u0017\u0010\u0013\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0014\u0010\u0004\u001a\u0017\u0010\u0013\u001a\u00020\u0001*\u00020\u0005H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0007\u001a\u0017\u0010\u0013\u001a\u00020\u0001*\u00020\bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0016\u0010\n\u001a\u0017\u0010\u0013\u001a\u00020\u0001*\u00020\u000bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0017\u0010\r\u001a\u001f\u0010\u0018\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001a\u0010\u001b\u001a\u001f\u0010\u0018\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001c\u0010\u001d\u001a\u001f\u0010\u0018\u001a\u00020\b*\u00020\b2\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u001f\u001a\u001f\u0010\u0018\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b \u0010!\u001a\u001f\u0010\"\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b#\u0010\u001b\u001a\u001f\u0010\"\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b$\u0010\u001d\u001a\u001f\u0010\"\u001a\u00020\b*\u00020\b2\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b%\u0010\u001f\u001a\u001f\u0010\"\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b&\u0010!\u001a\u0017\u0010'\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b(\u0010)\u001a\u0017\u0010'\u001a\u00020\u0005*\u00020\u0005H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b*\u0010\u0007\u001a\u0017\u0010'\u001a\u00020\b*\u00020\bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b+\u0010,\u001a\u0017\u0010'\u001a\u00020\u000b*\u00020\u000bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b-\u0010.\u001a\u0017\u0010/\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b0\u0010)\u001a\u0017\u0010/\u001a\u00020\u0005*\u00020\u0005H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b1\u0010\u0007\u001a\u0017\u0010/\u001a\u00020\b*\u00020\bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b2\u0010,\u001a\u0017\u0010/\u001a\u00020\u000b*\u00020\u000bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b3\u0010.\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u00064"}, d2={"countLeadingZeroBits", "", "Lkotlin/UByte;", "countLeadingZeroBits-7apg3OU", "(B)I", "Lkotlin/UInt;", "countLeadingZeroBits-WZ4Q5Ns", "(I)I", "Lkotlin/ULong;", "countLeadingZeroBits-VKZWuLQ", "(J)I", "Lkotlin/UShort;", "countLeadingZeroBits-xj2QHRw", "(S)I", "countOneBits", "countOneBits-7apg3OU", "countOneBits-WZ4Q5Ns", "countOneBits-VKZWuLQ", "countOneBits-xj2QHRw", "countTrailingZeroBits", "countTrailingZeroBits-7apg3OU", "countTrailingZeroBits-WZ4Q5Ns", "countTrailingZeroBits-VKZWuLQ", "countTrailingZeroBits-xj2QHRw", "rotateLeft", "bitCount", "rotateLeft-LxnNnR4", "(BI)B", "rotateLeft-V7xB4Y4", "(II)I", "rotateLeft-JSWoG40", "(JI)J", "rotateLeft-olVBNx4", "(SI)S", "rotateRight", "rotateRight-LxnNnR4", "rotateRight-V7xB4Y4", "rotateRight-JSWoG40", "rotateRight-olVBNx4", "takeHighestOneBit", "takeHighestOneBit-7apg3OU", "(B)B", "takeHighestOneBit-WZ4Q5Ns", "takeHighestOneBit-VKZWuLQ", "(J)J", "takeHighestOneBit-xj2QHRw", "(S)S", "takeLowestOneBit", "takeLowestOneBit-7apg3OU", "takeLowestOneBit-WZ4Q5Ns", "takeLowestOneBit-VKZWuLQ", "takeLowestOneBit-xj2QHRw", "kotlin-stdlib"})
@JvmName(name="UNumbersKt")
public final class UNumbersKt {
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countOneBits-WZ4Q5Ns(int $this$countOneBits) {
        int n = $this$countOneBits;
        return Integer.bitCount(n);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countLeadingZeroBits-WZ4Q5Ns(int $this$countLeadingZeroBits) {
        int n = $this$countLeadingZeroBits;
        return Integer.numberOfLeadingZeros(n);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countTrailingZeroBits-WZ4Q5Ns(int $this$countTrailingZeroBits) {
        int n = $this$countTrailingZeroBits;
        return Integer.numberOfTrailingZeros(n);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int takeHighestOneBit-WZ4Q5Ns(int $this$takeHighestOneBit) {
        int n = $this$takeHighestOneBit;
        n = Integer.highestOneBit(n);
        return UInt.constructor-impl(n);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int takeLowestOneBit-WZ4Q5Ns(int $this$takeLowestOneBit) {
        int n = $this$takeLowestOneBit;
        n = Integer.lowestOneBit(n);
        return UInt.constructor-impl(n);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class, ExperimentalUnsignedTypes.class})
    @InlineOnly
    private static final int rotateLeft-V7xB4Y4(int $this$rotateLeft, int bitCount) {
        int n = $this$rotateLeft;
        n = Integer.rotateLeft(n, bitCount);
        return UInt.constructor-impl(n);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class, ExperimentalUnsignedTypes.class})
    @InlineOnly
    private static final int rotateRight-V7xB4Y4(int $this$rotateRight, int bitCount) {
        int n = $this$rotateRight;
        n = Integer.rotateRight(n, bitCount);
        return UInt.constructor-impl(n);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countOneBits-VKZWuLQ(long $this$countOneBits) {
        long l = $this$countOneBits;
        return Long.bitCount(l);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countLeadingZeroBits-VKZWuLQ(long $this$countLeadingZeroBits) {
        long l = $this$countLeadingZeroBits;
        return Long.numberOfLeadingZeros(l);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countTrailingZeroBits-VKZWuLQ(long $this$countTrailingZeroBits) {
        long l = $this$countTrailingZeroBits;
        return Long.numberOfTrailingZeros(l);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final long takeHighestOneBit-VKZWuLQ(long $this$takeHighestOneBit) {
        long l = $this$takeHighestOneBit;
        l = Long.highestOneBit(l);
        return ULong.constructor-impl(l);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final long takeLowestOneBit-VKZWuLQ(long $this$takeLowestOneBit) {
        long l = $this$takeLowestOneBit;
        l = Long.lowestOneBit(l);
        return ULong.constructor-impl(l);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class, ExperimentalUnsignedTypes.class})
    @InlineOnly
    private static final long rotateLeft-JSWoG40(long $this$rotateLeft, int bitCount) {
        long l = $this$rotateLeft;
        l = Long.rotateLeft(l, bitCount);
        return ULong.constructor-impl(l);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class, ExperimentalUnsignedTypes.class})
    @InlineOnly
    private static final long rotateRight-JSWoG40(long $this$rotateRight, int bitCount) {
        long l = $this$rotateRight;
        l = Long.rotateRight(l, bitCount);
        return ULong.constructor-impl(l);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countOneBits-7apg3OU(byte $this$countOneBits) {
        int n;
        int n2 = n = UInt.constructor-impl($this$countOneBits & 0xFF);
        return Integer.bitCount(n2);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countLeadingZeroBits-7apg3OU(byte $this$countLeadingZeroBits) {
        byte by = $this$countLeadingZeroBits;
        return Integer.numberOfLeadingZeros(by & 0xFF) - 24;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countTrailingZeroBits-7apg3OU(byte $this$countTrailingZeroBits) {
        byte by = $this$countTrailingZeroBits;
        return Integer.numberOfTrailingZeros(by | 0x100);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final byte takeHighestOneBit-7apg3OU(byte $this$takeHighestOneBit) {
        int n = $this$takeHighestOneBit & 0xFF;
        n = Integer.highestOneBit(n);
        return UByte.constructor-impl((byte)n);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final byte takeLowestOneBit-7apg3OU(byte $this$takeLowestOneBit) {
        int n = $this$takeLowestOneBit & 0xFF;
        n = Integer.lowestOneBit(n);
        return UByte.constructor-impl((byte)n);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class, ExperimentalUnsignedTypes.class})
    @InlineOnly
    private static final byte rotateLeft-LxnNnR4(byte $this$rotateLeft, int bitCount) {
        byte by = NumbersKt.rotateLeft($this$rotateLeft, bitCount);
        return UByte.constructor-impl(by);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class, ExperimentalUnsignedTypes.class})
    @InlineOnly
    private static final byte rotateRight-LxnNnR4(byte $this$rotateRight, int bitCount) {
        byte by = NumbersKt.rotateRight($this$rotateRight, bitCount);
        return UByte.constructor-impl(by);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countOneBits-xj2QHRw(short $this$countOneBits) {
        int n;
        int n2 = n = UInt.constructor-impl($this$countOneBits & 0xFFFF);
        return Integer.bitCount(n2);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countLeadingZeroBits-xj2QHRw(short $this$countLeadingZeroBits) {
        short s = $this$countLeadingZeroBits;
        return Integer.numberOfLeadingZeros(s & 0xFFFF) - 16;
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countTrailingZeroBits-xj2QHRw(short $this$countTrailingZeroBits) {
        short s = $this$countTrailingZeroBits;
        return Integer.numberOfTrailingZeros(s | 0x10000);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final short takeHighestOneBit-xj2QHRw(short $this$takeHighestOneBit) {
        int n = $this$takeHighestOneBit & 0xFFFF;
        n = Integer.highestOneBit(n);
        return UShort.constructor-impl((short)n);
    }

    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class, ExperimentalStdlibApi.class})
    @InlineOnly
    private static final short takeLowestOneBit-xj2QHRw(short $this$takeLowestOneBit) {
        int n = $this$takeLowestOneBit & 0xFFFF;
        n = Integer.lowestOneBit(n);
        return UShort.constructor-impl((short)n);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class, ExperimentalUnsignedTypes.class})
    @InlineOnly
    private static final short rotateLeft-olVBNx4(short $this$rotateLeft, int bitCount) {
        short s = NumbersKt.rotateLeft($this$rotateLeft, bitCount);
        return UShort.constructor-impl(s);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class, ExperimentalUnsignedTypes.class})
    @InlineOnly
    private static final short rotateRight-olVBNx4(short $this$rotateRight, int bitCount) {
        short s = NumbersKt.rotateRight($this$rotateRight, bitCount);
        return UShort.constructor-impl(s);
    }
}

