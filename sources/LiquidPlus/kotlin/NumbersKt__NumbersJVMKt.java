/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.ExperimentalStdlibApi;
import kotlin.Metadata;
import kotlin.NumbersKt__FloorDivModKt;
import kotlin.SinceKotlin;
import kotlin.WasExperimental;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.DoubleCompanionObject;
import kotlin.jvm.internal.FloatCompanionObject;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000*\n\u0000\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0005\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0005\u001a\u00020\t*\u00020\n2\u0006\u0010\b\u001a\u00020\u0001H\u0087\b\u001a\r\u0010\u000b\u001a\u00020\f*\u00020\u0006H\u0087\b\u001a\r\u0010\u000b\u001a\u00020\f*\u00020\tH\u0087\b\u001a\r\u0010\r\u001a\u00020\f*\u00020\u0006H\u0087\b\u001a\r\u0010\r\u001a\u00020\f*\u00020\tH\u0087\b\u001a\r\u0010\u000e\u001a\u00020\f*\u00020\u0006H\u0087\b\u001a\r\u0010\u000e\u001a\u00020\f*\u00020\tH\u0087\b\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u000f\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0011\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0010\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0011\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0010\u001a\u00020\u0001H\u0087\b\u001a\r\u0010\u0012\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0012\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010\u0013\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0013\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010\u0014\u001a\u00020\u0002*\u00020\u0006H\u0087\b\u001a\r\u0010\u0014\u001a\u00020\u0001*\u00020\tH\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0002*\u00020\u0006H\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\tH\u0087\b\u00a8\u0006\u0016"}, d2={"countLeadingZeroBits", "", "", "countOneBits", "countTrailingZeroBits", "fromBits", "", "Lkotlin/Double$Companion;", "bits", "", "Lkotlin/Float$Companion;", "isFinite", "", "isInfinite", "isNaN", "rotateLeft", "bitCount", "rotateRight", "takeHighestOneBit", "takeLowestOneBit", "toBits", "toRawBits", "kotlin-stdlib"}, xs="kotlin/NumbersKt")
class NumbersKt__NumbersJVMKt
extends NumbersKt__FloorDivModKt {
    @InlineOnly
    private static final boolean isNaN(double $this$isNaN) {
        return Double.isNaN($this$isNaN);
    }

    @InlineOnly
    private static final boolean isNaN(float $this$isNaN) {
        return Float.isNaN($this$isNaN);
    }

    @InlineOnly
    private static final boolean isInfinite(double $this$isInfinite) {
        return Double.isInfinite($this$isInfinite);
    }

    @InlineOnly
    private static final boolean isInfinite(float $this$isInfinite) {
        return Float.isInfinite($this$isInfinite);
    }

    @InlineOnly
    private static final boolean isFinite(double $this$isFinite) {
        return !Double.isInfinite($this$isFinite) && !Double.isNaN($this$isFinite);
    }

    @InlineOnly
    private static final boolean isFinite(float $this$isFinite) {
        return !Float.isInfinite($this$isFinite) && !Float.isNaN($this$isFinite);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final long toBits(double $this$toBits) {
        return Double.doubleToLongBits($this$toBits);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final long toRawBits(double $this$toRawBits) {
        return Double.doubleToRawLongBits($this$toRawBits);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final double fromBits(DoubleCompanionObject $this$fromBits, long bits) {
        Intrinsics.checkNotNullParameter($this$fromBits, "<this>");
        return Double.longBitsToDouble(bits);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final int toBits(float $this$toBits) {
        return Float.floatToIntBits($this$toBits);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final int toRawBits(float $this$toRawBits) {
        return Float.floatToRawIntBits($this$toRawBits);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final float fromBits(FloatCompanionObject $this$fromBits, int bits) {
        Intrinsics.checkNotNullParameter($this$fromBits, "<this>");
        return Float.intBitsToFloat(bits);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countOneBits(int $this$countOneBits) {
        return Integer.bitCount($this$countOneBits);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countLeadingZeroBits(int $this$countLeadingZeroBits) {
        return Integer.numberOfLeadingZeros($this$countLeadingZeroBits);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countTrailingZeroBits(int $this$countTrailingZeroBits) {
        return Integer.numberOfTrailingZeros($this$countTrailingZeroBits);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int takeHighestOneBit(int $this$takeHighestOneBit) {
        return Integer.highestOneBit($this$takeHighestOneBit);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int takeLowestOneBit(int $this$takeLowestOneBit) {
        return Integer.lowestOneBit($this$takeLowestOneBit);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int rotateLeft(int $this$rotateLeft, int bitCount) {
        return Integer.rotateLeft($this$rotateLeft, bitCount);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int rotateRight(int $this$rotateRight, int bitCount) {
        return Integer.rotateRight($this$rotateRight, bitCount);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countOneBits(long $this$countOneBits) {
        return Long.bitCount($this$countOneBits);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countLeadingZeroBits(long $this$countLeadingZeroBits) {
        return Long.numberOfLeadingZeros($this$countLeadingZeroBits);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final int countTrailingZeroBits(long $this$countTrailingZeroBits) {
        return Long.numberOfTrailingZeros($this$countTrailingZeroBits);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final long takeHighestOneBit(long $this$takeHighestOneBit) {
        return Long.highestOneBit($this$takeHighestOneBit);
    }

    @SinceKotlin(version="1.4")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final long takeLowestOneBit(long $this$takeLowestOneBit) {
        return Long.lowestOneBit($this$takeLowestOneBit);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final long rotateLeft(long $this$rotateLeft, int bitCount) {
        return Long.rotateLeft($this$rotateLeft, bitCount);
    }

    @SinceKotlin(version="1.6")
    @WasExperimental(markerClass={ExperimentalStdlibApi.class})
    @InlineOnly
    private static final long rotateRight(long $this$rotateRight, int bitCount) {
        return Long.rotateRight($this$rotateRight, bitCount);
    }
}

