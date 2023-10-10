/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil;

public class HashCommon {
    private static final int INT_PHI = -1640531527;
    private static final int INV_INT_PHI = 340573321;
    private static final long LONG_PHI = -7046029254386353131L;
    private static final long INV_LONG_PHI = -1018231460777725123L;

    protected HashCommon() {
    }

    public static int murmurHash3(int x2) {
        x2 ^= x2 >>> 16;
        x2 *= -2048144789;
        x2 ^= x2 >>> 13;
        x2 *= -1028477387;
        x2 ^= x2 >>> 16;
        return x2;
    }

    public static long murmurHash3(long x2) {
        x2 ^= x2 >>> 33;
        x2 *= -49064778989728563L;
        x2 ^= x2 >>> 33;
        x2 *= -4265267296055464877L;
        x2 ^= x2 >>> 33;
        return x2;
    }

    public static int mix(int x2) {
        int h2 = x2 * -1640531527;
        return h2 ^ h2 >>> 16;
    }

    public static int invMix(int x2) {
        return (x2 ^ x2 >>> 16) * 340573321;
    }

    public static long mix(long x2) {
        long h2 = x2 * -7046029254386353131L;
        h2 ^= h2 >>> 32;
        return h2 ^ h2 >>> 16;
    }

    public static long invMix(long x2) {
        x2 ^= x2 >>> 32;
        x2 ^= x2 >>> 16;
        return (x2 ^ x2 >>> 32) * -1018231460777725123L;
    }

    public static int float2int(float f2) {
        return Float.floatToRawIntBits(f2);
    }

    public static int double2int(double d2) {
        long l2 = Double.doubleToRawLongBits(d2);
        return (int)(l2 ^ l2 >>> 32);
    }

    public static int long2int(long l2) {
        return (int)(l2 ^ l2 >>> 32);
    }

    public static int nextPowerOfTwo(int x2) {
        if (x2 == 0) {
            return 1;
        }
        --x2;
        x2 |= x2 >> 1;
        x2 |= x2 >> 2;
        x2 |= x2 >> 4;
        x2 |= x2 >> 8;
        return (x2 | x2 >> 16) + 1;
    }

    public static long nextPowerOfTwo(long x2) {
        if (x2 == 0L) {
            return 1L;
        }
        --x2;
        x2 |= x2 >> 1;
        x2 |= x2 >> 2;
        x2 |= x2 >> 4;
        x2 |= x2 >> 8;
        x2 |= x2 >> 16;
        return (x2 | x2 >> 32) + 1L;
    }

    public static int maxFill(int n2, float f2) {
        return Math.min((int)Math.ceil((float)n2 * f2), n2 - 1);
    }

    public static long maxFill(long n2, float f2) {
        return Math.min((long)Math.ceil((float)n2 * f2), n2 - 1L);
    }

    public static int arraySize(int expected, float f2) {
        long s2 = Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil((float)expected / f2)));
        if (s2 > 0x40000000L) {
            throw new IllegalArgumentException("Too large (" + expected + " expected elements with load factor " + f2 + ")");
        }
        return (int)s2;
    }

    public static long bigArraySize(long expected, float f2) {
        return HashCommon.nextPowerOfTwo((long)Math.ceil((float)expected / f2));
    }
}

