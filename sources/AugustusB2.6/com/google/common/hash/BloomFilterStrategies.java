// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.hash;

import java.util.Arrays;
import javax.annotation.CheckForNull;
import com.google.common.primitives.Ints;
import com.google.common.math.LongMath;
import java.math.RoundingMode;
import com.google.common.base.Preconditions;
import java.util.concurrent.atomic.AtomicLongArray;
import com.google.common.primitives.Longs;

@ElementTypesAreNonnullByDefault
enum BloomFilterStrategies implements BloomFilter.Strategy
{
    MURMUR128_MITZ_32(0) {
        @Override
        public <T> boolean put(@ParametricNullness final T object, final Funnel<? super T> funnel, final int numHashFunctions, final LockFreeBitArray bits) {
            final long bitSize = bits.bitSize();
            final long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
            final int hash65 = (int)hash64;
            final int hash66 = (int)(hash64 >>> 32);
            boolean bitsChanged = false;
            for (int i = 1; i <= numHashFunctions; ++i) {
                int combinedHash = hash65 + i * hash66;
                if (combinedHash < 0) {
                    combinedHash ^= -1;
                }
                bitsChanged |= bits.set(combinedHash % bitSize);
            }
            return bitsChanged;
        }
        
        @Override
        public <T> boolean mightContain(@ParametricNullness final T object, final Funnel<? super T> funnel, final int numHashFunctions, final LockFreeBitArray bits) {
            final long bitSize = bits.bitSize();
            final long hash64 = Hashing.murmur3_128().hashObject(object, funnel).asLong();
            final int hash65 = (int)hash64;
            final int hash66 = (int)(hash64 >>> 32);
            for (int i = 1; i <= numHashFunctions; ++i) {
                int combinedHash = hash65 + i * hash66;
                if (combinedHash < 0) {
                    combinedHash ^= -1;
                }
                if (!bits.get(combinedHash % bitSize)) {
                    return false;
                }
            }
            return true;
        }
    }, 
    MURMUR128_MITZ_64(1) {
        @Override
        public <T> boolean put(@ParametricNullness final T object, final Funnel<? super T> funnel, final int numHashFunctions, final LockFreeBitArray bits) {
            final long bitSize = bits.bitSize();
            final byte[] bytes = Hashing.murmur3_128().hashObject(object, funnel).getBytesInternal();
            final long hash1 = this.lowerEight(bytes);
            final long hash2 = this.upperEight(bytes);
            boolean bitsChanged = false;
            long combinedHash = hash1;
            for (int i = 0; i < numHashFunctions; ++i) {
                bitsChanged |= bits.set((combinedHash & Long.MAX_VALUE) % bitSize);
                combinedHash += hash2;
            }
            return bitsChanged;
        }
        
        @Override
        public <T> boolean mightContain(@ParametricNullness final T object, final Funnel<? super T> funnel, final int numHashFunctions, final LockFreeBitArray bits) {
            final long bitSize = bits.bitSize();
            final byte[] bytes = Hashing.murmur3_128().hashObject(object, funnel).getBytesInternal();
            final long hash1 = this.lowerEight(bytes);
            final long hash2 = this.upperEight(bytes);
            long combinedHash = hash1;
            for (int i = 0; i < numHashFunctions; ++i) {
                if (!bits.get((combinedHash & Long.MAX_VALUE) % bitSize)) {
                    return false;
                }
                combinedHash += hash2;
            }
            return true;
        }
        
        private long lowerEight(final byte[] bytes) {
            return Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
        }
        
        private long upperEight(final byte[] bytes) {
            return Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
        }
    };
    
    private static /* synthetic */ BloomFilterStrategies[] $values() {
        return new BloomFilterStrategies[] { BloomFilterStrategies.MURMUR128_MITZ_32, BloomFilterStrategies.MURMUR128_MITZ_64 };
    }
    
    static {
        $VALUES = $values();
    }
    
    static final class LockFreeBitArray
    {
        private static final int LONG_ADDRESSABLE_BITS = 6;
        final AtomicLongArray data;
        private final LongAddable bitCount;
        
        LockFreeBitArray(final long bits) {
            Preconditions.checkArgument(bits > 0L, (Object)"data length is zero!");
            this.data = new AtomicLongArray(Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING)));
            this.bitCount = LongAddables.create();
        }
        
        LockFreeBitArray(final long[] data) {
            Preconditions.checkArgument(data.length > 0, (Object)"data length is zero!");
            this.data = new AtomicLongArray(data);
            this.bitCount = LongAddables.create();
            long bitCount = 0L;
            for (final long value : data) {
                bitCount += Long.bitCount(value);
            }
            this.bitCount.add(bitCount);
        }
        
        boolean set(final long bitIndex) {
            if (this.get(bitIndex)) {
                return false;
            }
            final int longIndex = (int)(bitIndex >>> 6);
            final long mask = 1L << (int)bitIndex;
            long oldValue;
            long newValue;
            do {
                oldValue = this.data.get(longIndex);
                newValue = (oldValue | mask);
                if (oldValue == newValue) {
                    return false;
                }
            } while (!this.data.compareAndSet(longIndex, oldValue, newValue));
            this.bitCount.increment();
            return true;
        }
        
        boolean get(final long bitIndex) {
            return (this.data.get((int)(bitIndex >>> 6)) & 1L << (int)bitIndex) != 0x0L;
        }
        
        public static long[] toPlainArray(final AtomicLongArray atomicLongArray) {
            final long[] array = new long[atomicLongArray.length()];
            for (int i = 0; i < array.length; ++i) {
                array[i] = atomicLongArray.get(i);
            }
            return array;
        }
        
        long bitSize() {
            return this.data.length() * 64L;
        }
        
        long bitCount() {
            return this.bitCount.sum();
        }
        
        LockFreeBitArray copy() {
            return new LockFreeBitArray(toPlainArray(this.data));
        }
        
        void putAll(final LockFreeBitArray other) {
            Preconditions.checkArgument(this.data.length() == other.data.length(), "BitArrays must be of equal length (%s != %s)", this.data.length(), other.data.length());
            for (int i = 0; i < this.data.length(); ++i) {
                final long otherLong = other.data.get(i);
                boolean changedAnyBits = true;
                long ourLongOld;
                long ourLongNew;
                do {
                    ourLongOld = this.data.get(i);
                    ourLongNew = (ourLongOld | otherLong);
                    if (ourLongOld == ourLongNew) {
                        changedAnyBits = false;
                        break;
                    }
                } while (!this.data.compareAndSet(i, ourLongOld, ourLongNew));
                if (changedAnyBits) {
                    final int bitsAdded = Long.bitCount(ourLongNew) - Long.bitCount(ourLongOld);
                    this.bitCount.add(bitsAdded);
                }
            }
        }
        
        @Override
        public boolean equals(@CheckForNull final Object o) {
            if (o instanceof LockFreeBitArray) {
                final LockFreeBitArray lockFreeBitArray = (LockFreeBitArray)o;
                return Arrays.equals(toPlainArray(this.data), toPlainArray(lockFreeBitArray.data));
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Arrays.hashCode(toPlainArray(this.data));
        }
    }
}
