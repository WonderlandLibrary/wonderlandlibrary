// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

@ElementTypesAreNonnullByDefault
final class FarmHashFingerprint64 extends AbstractNonStreamingHashFunction
{
    static final HashFunction FARMHASH_FINGERPRINT_64;
    private static final long K0 = -4348849565147123417L;
    private static final long K1 = -5435081209227447693L;
    private static final long K2 = -7286425919675154353L;
    
    @Override
    public HashCode hashBytes(final byte[] input, final int off, final int len) {
        Preconditions.checkPositionIndexes(off, off + len, input.length);
        return HashCode.fromLong(fingerprint(input, off, len));
    }
    
    @Override
    public int bits() {
        return 64;
    }
    
    @Override
    public String toString() {
        return "Hashing.farmHashFingerprint64()";
    }
    
    @VisibleForTesting
    static long fingerprint(final byte[] bytes, final int offset, final int length) {
        if (length <= 32) {
            if (length <= 16) {
                return hashLength0to16(bytes, offset, length);
            }
            return hashLength17to32(bytes, offset, length);
        }
        else {
            if (length <= 64) {
                return hashLength33To64(bytes, offset, length);
            }
            return hashLength65Plus(bytes, offset, length);
        }
    }
    
    private static long shiftMix(final long val) {
        return val ^ val >>> 47;
    }
    
    private static long hashLength16(final long u, final long v, final long mul) {
        long a = (u ^ v) * mul;
        a ^= a >>> 47;
        long b = (v ^ a) * mul;
        b ^= b >>> 47;
        b *= mul;
        return b;
    }
    
    private static void weakHashLength32WithSeeds(final byte[] bytes, final int offset, long seedA, long seedB, final long[] output) {
        final long part1 = LittleEndianByteArray.load64(bytes, offset);
        final long part2 = LittleEndianByteArray.load64(bytes, offset + 8);
        final long part3 = LittleEndianByteArray.load64(bytes, offset + 16);
        final long part4 = LittleEndianByteArray.load64(bytes, offset + 24);
        seedA += part1;
        seedB = Long.rotateRight(seedB + seedA + part4, 21);
        final long c = seedA;
        seedA += part2;
        seedA += part3;
        seedB += Long.rotateRight(seedA, 44);
        output[0] = seedA + part4;
        output[1] = seedB + c;
    }
    
    private static long hashLength0to16(final byte[] bytes, final int offset, final int length) {
        if (length >= 8) {
            final long mul = -7286425919675154353L + length * 2;
            final long a = LittleEndianByteArray.load64(bytes, offset) - 7286425919675154353L;
            final long b = LittleEndianByteArray.load64(bytes, offset + length - 8);
            final long c = Long.rotateRight(b, 37) * mul + a;
            final long d = (Long.rotateRight(a, 25) + b) * mul;
            return hashLength16(c, d, mul);
        }
        if (length >= 4) {
            final long mul = -7286425919675154353L + length * 2;
            final long a = (long)LittleEndianByteArray.load32(bytes, offset) & 0xFFFFFFFFL;
            return hashLength16(length + (a << 3), (long)LittleEndianByteArray.load32(bytes, offset + length - 4) & 0xFFFFFFFFL, mul);
        }
        if (length > 0) {
            final byte a2 = bytes[offset];
            final byte b2 = bytes[offset + (length >> 1)];
            final byte c2 = bytes[offset + (length - 1)];
            final int y = (a2 & 0xFF) + ((b2 & 0xFF) << 8);
            final int z = length + ((c2 & 0xFF) << 2);
            return shiftMix(y * -7286425919675154353L ^ z * -4348849565147123417L) * -7286425919675154353L;
        }
        return -7286425919675154353L;
    }
    
    private static long hashLength17to32(final byte[] bytes, final int offset, final int length) {
        final long mul = -7286425919675154353L + length * 2;
        final long a = LittleEndianByteArray.load64(bytes, offset) * -5435081209227447693L;
        final long b = LittleEndianByteArray.load64(bytes, offset + 8);
        final long c = LittleEndianByteArray.load64(bytes, offset + length - 8) * mul;
        final long d = LittleEndianByteArray.load64(bytes, offset + length - 16) * -7286425919675154353L;
        return hashLength16(Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30) + d, a + Long.rotateRight(b - 7286425919675154353L, 18) + c, mul);
    }
    
    private static long hashLength33To64(final byte[] bytes, final int offset, final int length) {
        final long mul = -7286425919675154353L + length * 2;
        final long a = LittleEndianByteArray.load64(bytes, offset) * -7286425919675154353L;
        final long b = LittleEndianByteArray.load64(bytes, offset + 8);
        final long c = LittleEndianByteArray.load64(bytes, offset + length - 8) * mul;
        final long d = LittleEndianByteArray.load64(bytes, offset + length - 16) * -7286425919675154353L;
        final long y = Long.rotateRight(a + b, 43) + Long.rotateRight(c, 30) + d;
        final long z = hashLength16(y, a + Long.rotateRight(b - 7286425919675154353L, 18) + c, mul);
        final long e = LittleEndianByteArray.load64(bytes, offset + 16) * mul;
        final long f = LittleEndianByteArray.load64(bytes, offset + 24);
        final long g = (y + LittleEndianByteArray.load64(bytes, offset + length - 32)) * mul;
        final long h = (z + LittleEndianByteArray.load64(bytes, offset + length - 24)) * mul;
        return hashLength16(Long.rotateRight(e + f, 43) + Long.rotateRight(g, 30) + h, e + Long.rotateRight(f + a, 18) + g, mul);
    }
    
    private static long hashLength65Plus(final byte[] bytes, int offset, final int length) {
        final int seed = 81;
        long x = 81L;
        long y = 2480279821605975764L;
        long z = shiftMix(y * -7286425919675154353L + 113L) * -7286425919675154353L;
        final long[] v = new long[2];
        final long[] w = new long[2];
        x = x * -7286425919675154353L + LittleEndianByteArray.load64(bytes, offset);
        final int end = offset + (length - 1) / 64 * 64;
        final int last64offset = end + (length - 1 & 0x3F) - 63;
        do {
            x = Long.rotateRight(x + y + v[0] + LittleEndianByteArray.load64(bytes, offset + 8), 37) * -5435081209227447693L;
            y = Long.rotateRight(y + v[1] + LittleEndianByteArray.load64(bytes, offset + 48), 42) * -5435081209227447693L;
            x ^= w[1];
            y += v[0] + LittleEndianByteArray.load64(bytes, offset + 40);
            z = Long.rotateRight(z + w[0], 33) * -5435081209227447693L;
            weakHashLength32WithSeeds(bytes, offset, v[1] * -5435081209227447693L, x + w[0], v);
            weakHashLength32WithSeeds(bytes, offset + 32, z + w[1], y + LittleEndianByteArray.load64(bytes, offset + 16), w);
            final long tmp = x;
            x = z;
            z = tmp;
            offset += 64;
        } while (offset != end);
        final long mul = -5435081209227447693L + ((z & 0xFFL) << 1);
        offset = last64offset;
        final long[] array = w;
        final int n = 0;
        array[n] += (length - 1 & 0x3F);
        final long[] array2 = v;
        final int n2 = 0;
        array2[n2] += w[0];
        final long[] array3 = w;
        final int n3 = 0;
        array3[n3] += v[0];
        x = Long.rotateRight(x + y + v[0] + LittleEndianByteArray.load64(bytes, offset + 8), 37) * mul;
        y = Long.rotateRight(y + v[1] + LittleEndianByteArray.load64(bytes, offset + 48), 42) * mul;
        x ^= w[1] * 9L;
        y += v[0] * 9L + LittleEndianByteArray.load64(bytes, offset + 40);
        z = Long.rotateRight(z + w[0], 33) * mul;
        weakHashLength32WithSeeds(bytes, offset, v[1] * mul, x + w[0], v);
        weakHashLength32WithSeeds(bytes, offset + 32, z + w[1], y + LittleEndianByteArray.load64(bytes, offset + 16), w);
        return hashLength16(hashLength16(v[0], w[0], mul) + shiftMix(y) * -4348849565147123417L + x, hashLength16(v[1], w[1], mul) + z, mul);
    }
    
    static {
        FARMHASH_FINGERPRINT_64 = new FarmHashFingerprint64();
    }
}
