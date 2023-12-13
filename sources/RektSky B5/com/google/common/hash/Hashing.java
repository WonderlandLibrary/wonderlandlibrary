/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.hash.AbstractCompositeHashFunction;
import com.google.common.hash.ChecksumHashFunction;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.MessageDigestHashFunction;
import com.google.common.hash.Murmur3_128HashFunction;
import com.google.common.hash.Murmur3_32HashFunction;
import com.google.common.hash.SipHashFunction;
import java.util.Iterator;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.annotation.Nullable;

@Beta
public final class Hashing {
    private static final int GOOD_FAST_HASH_SEED = (int)System.currentTimeMillis();

    public static HashFunction goodFastHash(int minimumBits) {
        int bits = Hashing.checkPositiveAndMakeMultipleOf32(minimumBits);
        if (bits == 32) {
            return Murmur3_32Holder.GOOD_FAST_HASH_FUNCTION_32;
        }
        if (bits <= 128) {
            return Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
        }
        int hashFunctionsNeeded = (bits + 127) / 128;
        HashFunction[] hashFunctions = new HashFunction[hashFunctionsNeeded];
        hashFunctions[0] = Murmur3_128Holder.GOOD_FAST_HASH_FUNCTION_128;
        int seed = GOOD_FAST_HASH_SEED;
        for (int i2 = 1; i2 < hashFunctionsNeeded; ++i2) {
            hashFunctions[i2] = Hashing.murmur3_128(seed += 1500450271);
        }
        return new ConcatenatedHashFunction(hashFunctions);
    }

    public static HashFunction murmur3_32(int seed) {
        return new Murmur3_32HashFunction(seed);
    }

    public static HashFunction murmur3_32() {
        return Murmur3_32Holder.MURMUR3_32;
    }

    public static HashFunction murmur3_128(int seed) {
        return new Murmur3_128HashFunction(seed);
    }

    public static HashFunction murmur3_128() {
        return Murmur3_128Holder.MURMUR3_128;
    }

    public static HashFunction sipHash24() {
        return SipHash24Holder.SIP_HASH_24;
    }

    public static HashFunction sipHash24(long k0, long k1) {
        return new SipHashFunction(2, 4, k0, k1);
    }

    public static HashFunction md5() {
        return Md5Holder.MD5;
    }

    public static HashFunction sha1() {
        return Sha1Holder.SHA_1;
    }

    public static HashFunction sha256() {
        return Sha256Holder.SHA_256;
    }

    public static HashFunction sha512() {
        return Sha512Holder.SHA_512;
    }

    public static HashFunction crc32() {
        return Crc32Holder.CRC_32;
    }

    public static HashFunction adler32() {
        return Adler32Holder.ADLER_32;
    }

    private static HashFunction checksumHashFunction(ChecksumType type, String toString) {
        return new ChecksumHashFunction(type, type.bits, toString);
    }

    public static int consistentHash(HashCode hashCode, int buckets) {
        return Hashing.consistentHash(hashCode.padToLong(), buckets);
    }

    public static int consistentHash(long input, int buckets) {
        int next;
        Preconditions.checkArgument(buckets > 0, "buckets must be positive: %s", buckets);
        LinearCongruentialGenerator generator = new LinearCongruentialGenerator(input);
        int candidate = 0;
        while ((next = (int)((double)(candidate + 1) / generator.nextDouble())) >= 0 && next < buckets) {
            candidate = next;
        }
        return candidate;
    }

    public static HashCode combineOrdered(Iterable<HashCode> hashCodes) {
        Iterator<HashCode> iterator = hashCodes.iterator();
        Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
        int bits = iterator.next().bits();
        byte[] resultBytes = new byte[bits / 8];
        for (HashCode hashCode : hashCodes) {
            byte[] nextBytes = hashCode.asBytes();
            Preconditions.checkArgument(nextBytes.length == resultBytes.length, "All hashcodes must have the same bit length.");
            for (int i2 = 0; i2 < nextBytes.length; ++i2) {
                resultBytes[i2] = (byte)(resultBytes[i2] * 37 ^ nextBytes[i2]);
            }
        }
        return HashCode.fromBytesNoCopy(resultBytes);
    }

    public static HashCode combineUnordered(Iterable<HashCode> hashCodes) {
        Iterator<HashCode> iterator = hashCodes.iterator();
        Preconditions.checkArgument(iterator.hasNext(), "Must be at least 1 hash code to combine.");
        byte[] resultBytes = new byte[iterator.next().bits() / 8];
        for (HashCode hashCode : hashCodes) {
            byte[] nextBytes = hashCode.asBytes();
            Preconditions.checkArgument(nextBytes.length == resultBytes.length, "All hashcodes must have the same bit length.");
            for (int i2 = 0; i2 < nextBytes.length; ++i2) {
                int n2 = i2;
                resultBytes[n2] = (byte)(resultBytes[n2] + nextBytes[i2]);
            }
        }
        return HashCode.fromBytesNoCopy(resultBytes);
    }

    static int checkPositiveAndMakeMultipleOf32(int bits) {
        Preconditions.checkArgument(bits > 0, "Number of bits must be positive");
        return bits + 31 & 0xFFFFFFE0;
    }

    private Hashing() {
    }

    static /* synthetic */ int access$000() {
        return GOOD_FAST_HASH_SEED;
    }

    static /* synthetic */ HashFunction access$100(ChecksumType x0, String x1) {
        return Hashing.checksumHashFunction(x0, x1);
    }

    private static final class LinearCongruentialGenerator {
        private long state;

        public LinearCongruentialGenerator(long seed) {
            this.state = seed;
        }

        public double nextDouble() {
            this.state = 2862933555777941757L * this.state + 1L;
            return (double)((int)(this.state >>> 33) + 1) / 2.147483648E9;
        }
    }

    @VisibleForTesting
    static final class ConcatenatedHashFunction
    extends AbstractCompositeHashFunction {
        private final int bits;

        ConcatenatedHashFunction(HashFunction ... functions) {
            super(functions);
            int bitSum = 0;
            for (HashFunction function : functions) {
                bitSum += function.bits();
            }
            this.bits = bitSum;
        }

        @Override
        HashCode makeHash(Hasher[] hashers) {
            byte[] bytes = new byte[this.bits / 8];
            int i2 = 0;
            for (Hasher hasher : hashers) {
                HashCode newHash = hasher.hash();
                i2 += newHash.writeBytesTo(bytes, i2, newHash.bits() / 8);
            }
            return HashCode.fromBytesNoCopy(bytes);
        }

        @Override
        public int bits() {
            return this.bits;
        }

        public boolean equals(@Nullable Object object) {
            if (object instanceof ConcatenatedHashFunction) {
                ConcatenatedHashFunction other = (ConcatenatedHashFunction)object;
                if (this.bits != other.bits || this.functions.length != other.functions.length) {
                    return false;
                }
                for (int i2 = 0; i2 < this.functions.length; ++i2) {
                    if (this.functions[i2].equals(other.functions[i2])) continue;
                    return false;
                }
                return true;
            }
            return false;
        }

        public int hashCode() {
            int hash = this.bits;
            for (HashFunction function : this.functions) {
                hash ^= function.hashCode();
            }
            return hash;
        }
    }

    static enum ChecksumType implements Supplier<Checksum>
    {
        CRC_32(32){

            @Override
            public Checksum get() {
                return new CRC32();
            }
        }
        ,
        ADLER_32(32){

            @Override
            public Checksum get() {
                return new Adler32();
            }
        };

        private final int bits;

        private ChecksumType(int bits) {
            this.bits = bits;
        }

        @Override
        public abstract Checksum get();
    }

    private static class Adler32Holder {
        static final HashFunction ADLER_32 = Hashing.access$100(ChecksumType.ADLER_32, "Hashing.adler32()");

        private Adler32Holder() {
        }
    }

    private static class Crc32Holder {
        static final HashFunction CRC_32 = Hashing.access$100(ChecksumType.CRC_32, "Hashing.crc32()");

        private Crc32Holder() {
        }
    }

    private static class Sha512Holder {
        static final HashFunction SHA_512 = new MessageDigestHashFunction("SHA-512", "Hashing.sha512()");

        private Sha512Holder() {
        }
    }

    private static class Sha256Holder {
        static final HashFunction SHA_256 = new MessageDigestHashFunction("SHA-256", "Hashing.sha256()");

        private Sha256Holder() {
        }
    }

    private static class Sha1Holder {
        static final HashFunction SHA_1 = new MessageDigestHashFunction("SHA-1", "Hashing.sha1()");

        private Sha1Holder() {
        }
    }

    private static class Md5Holder {
        static final HashFunction MD5 = new MessageDigestHashFunction("MD5", "Hashing.md5()");

        private Md5Holder() {
        }
    }

    private static class SipHash24Holder {
        static final HashFunction SIP_HASH_24 = new SipHashFunction(2, 4, 506097522914230528L, 1084818905618843912L);

        private SipHash24Holder() {
        }
    }

    private static class Murmur3_128Holder {
        static final HashFunction MURMUR3_128 = new Murmur3_128HashFunction(0);
        static final HashFunction GOOD_FAST_HASH_FUNCTION_128 = Hashing.murmur3_128(Hashing.access$000());

        private Murmur3_128Holder() {
        }
    }

    private static class Murmur3_32Holder {
        static final HashFunction MURMUR3_32 = new Murmur3_32HashFunction(0);
        static final HashFunction GOOD_FAST_HASH_FUNCTION_32 = Hashing.murmur3_32(Hashing.access$000());

        private Murmur3_32Holder() {
        }
    }
}

