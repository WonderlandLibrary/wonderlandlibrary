package com.google.common.hash;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import java.math.RoundingMode;
import java.util.Arrays;

enum BloomFilterStrategies implements BloomFilter.Strategy {
  MURMUR128_MITZ_32 {
    public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
      long bitSize = bits.bitSize();
      long hash64 = Hashing.murmur3_128().<T>hashObject(object, funnel).asLong();
      int hash1 = (int)hash64;
      int hash2 = (int)(hash64 >>> 32L);
      boolean bitsChanged = false;
      for (int i = 1; i <= numHashFunctions; i++) {
        int combinedHash = hash1 + i * hash2;
        if (combinedHash < 0)
          combinedHash ^= 0xFFFFFFFF; 
        bitsChanged |= bits.set(combinedHash % bitSize);
      } 
      return bitsChanged;
    }
    
    public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
      long bitSize = bits.bitSize();
      long hash64 = Hashing.murmur3_128().<T>hashObject(object, funnel).asLong();
      int hash1 = (int)hash64;
      int hash2 = (int)(hash64 >>> 32L);
      for (int i = 1; i <= numHashFunctions; i++) {
        int combinedHash = hash1 + i * hash2;
        if (combinedHash < 0)
          combinedHash ^= 0xFFFFFFFF; 
        if (!bits.get(combinedHash % bitSize))
          return false; 
      } 
      return true;
    }
  },
  MURMUR128_MITZ_64 {
    public <T> boolean put(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
      long bitSize = bits.bitSize();
      byte[] bytes = Hashing.murmur3_128().<T>hashObject(object, funnel).getBytesInternal();
      long hash1 = lowerEight(bytes);
      long hash2 = upperEight(bytes);
      boolean bitsChanged = false;
      long combinedHash = hash1;
      for (int i = 0; i < numHashFunctions; i++) {
        bitsChanged |= bits.set((combinedHash & Long.MAX_VALUE) % bitSize);
        combinedHash += hash2;
      } 
      return bitsChanged;
    }
    
    public <T> boolean mightContain(T object, Funnel<? super T> funnel, int numHashFunctions, BitArray bits) {
      long bitSize = bits.bitSize();
      byte[] bytes = Hashing.murmur3_128().<T>hashObject(object, funnel).getBytesInternal();
      long hash1 = lowerEight(bytes);
      long hash2 = upperEight(bytes);
      long combinedHash = hash1;
      for (int i = 0; i < numHashFunctions; i++) {
        if (!bits.get((combinedHash & Long.MAX_VALUE) % bitSize))
          return false; 
        combinedHash += hash2;
      } 
      return true;
    }
    
    private long lowerEight(byte[] bytes) {
      return Longs.fromBytes(bytes[7], bytes[6], bytes[5], bytes[4], bytes[3], bytes[2], bytes[1], bytes[0]);
    }
    
    private long upperEight(byte[] bytes) {
      return Longs.fromBytes(bytes[15], bytes[14], bytes[13], bytes[12], bytes[11], bytes[10], bytes[9], bytes[8]);
    }
  };
  
  static final class BitArray {
    final long[] data;
    
    long bitCount;
    
    BitArray(long bits) {
      this(new long[Ints.checkedCast(LongMath.divide(bits, 64L, RoundingMode.CEILING))]);
    }
    
    BitArray(long[] data) {
      Preconditions.checkArgument((data.length > 0), "data length is zero!");
      this.data = data;
      long bitCount = 0L;
      for (long value : data)
        bitCount += Long.bitCount(value); 
      this.bitCount = bitCount;
    }
    
    boolean set(long index) {
      if (!get(index)) {
        this.data[(int)(index >>> 6L)] = this.data[(int)(index >>> 6L)] | 1L << (int)index;
        this.bitCount++;
        return true;
      } 
      return false;
    }
    
    boolean get(long index) {
      return ((this.data[(int)(index >>> 6L)] & 1L << (int)index) != 0L);
    }
    
    long bitSize() {
      return this.data.length * 64L;
    }
    
    long bitCount() {
      return this.bitCount;
    }
    
    BitArray copy() {
      return new BitArray((long[])this.data.clone());
    }
    
    void putAll(BitArray array) {
      Preconditions.checkArgument((this.data.length == array.data.length), "BitArrays must be of equal length (%s != %s)", new Object[] { Integer.valueOf(this.data.length), Integer.valueOf(array.data.length) });
      this.bitCount = 0L;
      for (int i = 0; i < this.data.length; i++) {
        this.data[i] = this.data[i] | array.data[i];
        this.bitCount += Long.bitCount(this.data[i]);
      } 
    }
    
    public boolean equals(Object o) {
      if (o instanceof BitArray) {
        BitArray bitArray = (BitArray)o;
        return Arrays.equals(this.data, bitArray.data);
      } 
      return false;
    }
    
    public int hashCode() {
      return Arrays.hashCode(this.data);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\hash\BloomFilterStrategies.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */