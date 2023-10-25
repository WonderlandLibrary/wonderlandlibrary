package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import java.io.Serializable;
import javax.annotation.Nullable;

@Beta
public final class BloomFilter<T> implements Predicate<T>, Serializable {
  private final BloomFilterStrategies.BitArray bits;
  
  private final int numHashFunctions;
  
  private final Funnel<T> funnel;
  
  private final Strategy strategy;
  
  private BloomFilter(BloomFilterStrategies.BitArray bits, int numHashFunctions, Funnel<T> funnel, Strategy strategy) {
    Preconditions.checkArgument((numHashFunctions > 0), "numHashFunctions (%s) must be > 0", new Object[] { Integer.valueOf(numHashFunctions) });
    Preconditions.checkArgument((numHashFunctions <= 255), "numHashFunctions (%s) must be <= 255", new Object[] { Integer.valueOf(numHashFunctions) });
    this.bits = (BloomFilterStrategies.BitArray)Preconditions.checkNotNull(bits);
    this.numHashFunctions = numHashFunctions;
    this.funnel = (Funnel<T>)Preconditions.checkNotNull(funnel);
    this.strategy = (Strategy)Preconditions.checkNotNull(strategy);
  }
  
  public BloomFilter<T> copy() {
    return new BloomFilter(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy);
  }
  
  public boolean mightContain(T object) {
    return this.strategy.mightContain(object, this.funnel, this.numHashFunctions, this.bits);
  }
  
  @Deprecated
  public boolean apply(T input) {
    return mightContain(input);
  }
  
  public boolean put(T object) {
    return this.strategy.put(object, this.funnel, this.numHashFunctions, this.bits);
  }
  
  public double expectedFpp() {
    return Math.pow(this.bits.bitCount() / bitSize(), this.numHashFunctions);
  }
  
  @VisibleForTesting
  long bitSize() {
    return this.bits.bitSize();
  }
  
  public boolean isCompatible(BloomFilter<T> that) {
    Preconditions.checkNotNull(that);
    return (this != that && this.numHashFunctions == that.numHashFunctions && bitSize() == that.bitSize() && this.strategy.equals(that.strategy) && this.funnel.equals(that.funnel));
  }
  
  public void putAll(BloomFilter<T> that) {
    Preconditions.checkNotNull(that);
    Preconditions.checkArgument((this != that), "Cannot combine a BloomFilter with itself.");
    Preconditions.checkArgument((this.numHashFunctions == that.numHashFunctions), "BloomFilters must have the same number of hash functions (%s != %s)", new Object[] { Integer.valueOf(this.numHashFunctions), Integer.valueOf(that.numHashFunctions) });
    Preconditions.checkArgument((bitSize() == that.bitSize()), "BloomFilters must have the same size underlying bit arrays (%s != %s)", new Object[] { Long.valueOf(bitSize()), Long.valueOf(that.bitSize()) });
    Preconditions.checkArgument(this.strategy.equals(that.strategy), "BloomFilters must have equal strategies (%s != %s)", new Object[] { this.strategy, that.strategy });
    Preconditions.checkArgument(this.funnel.equals(that.funnel), "BloomFilters must have equal funnels (%s != %s)", new Object[] { this.funnel, that.funnel });
    this.bits.putAll(that.bits);
  }
  
  public boolean equals(@Nullable Object object) {
    if (object == this)
      return true; 
    if (object instanceof BloomFilter) {
      BloomFilter<?> that = (BloomFilter)object;
      return (this.numHashFunctions == that.numHashFunctions && this.funnel.equals(that.funnel) && this.bits.equals(that.bits) && this.strategy.equals(that.strategy));
    } 
    return false;
  }
  
  public int hashCode() {
    return Objects.hashCode(new Object[] { Integer.valueOf(this.numHashFunctions), this.funnel, this.strategy, this.bits });
  }
  
  private static final Strategy DEFAULT_STRATEGY = getDefaultStrategyFromSystemProperty();
  
  @VisibleForTesting
  static final String USE_MITZ32_PROPERTY = "com.google.common.hash.BloomFilter.useMitz32";
  
  @VisibleForTesting
  static Strategy getDefaultStrategyFromSystemProperty() {
    return Boolean.parseBoolean(System.getProperty("com.google.common.hash.BloomFilter.useMitz32")) ? BloomFilterStrategies.MURMUR128_MITZ_32 : BloomFilterStrategies.MURMUR128_MITZ_64;
  }
  
  public static <T> BloomFilter<T> create(Funnel<T> funnel, int expectedInsertions, double fpp) {
    return create(funnel, expectedInsertions, fpp, DEFAULT_STRATEGY);
  }
  
  @VisibleForTesting
  static <T> BloomFilter<T> create(Funnel<T> funnel, int expectedInsertions, double fpp, Strategy strategy) {
    Preconditions.checkNotNull(funnel);
    Preconditions.checkArgument((expectedInsertions >= 0), "Expected insertions (%s) must be >= 0", new Object[] { Integer.valueOf(expectedInsertions) });
    Preconditions.checkArgument((fpp > 0.0D), "False positive probability (%s) must be > 0.0", new Object[] { Double.valueOf(fpp) });
    Preconditions.checkArgument((fpp < 1.0D), "False positive probability (%s) must be < 1.0", new Object[] { Double.valueOf(fpp) });
    Preconditions.checkNotNull(strategy);
    if (expectedInsertions == 0)
      expectedInsertions = 1; 
    long numBits = optimalNumOfBits(expectedInsertions, fpp);
    int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
    try {
      return new BloomFilter<T>(new BloomFilterStrategies.BitArray(numBits), numHashFunctions, funnel, strategy);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Could not create BloomFilter of " + numBits + " bits", e);
    } 
  }
  
  public static <T> BloomFilter<T> create(Funnel<T> funnel, int expectedInsertions) {
    return create(funnel, expectedInsertions, 0.03D);
  }
  
  @VisibleForTesting
  static int optimalNumOfHashFunctions(long n, long m) {
    return Math.max(1, (int)Math.round((m / n) * Math.log(2.0D)));
  }
  
  @VisibleForTesting
  static long optimalNumOfBits(long n, double p) {
    if (p == 0.0D)
      p = Double.MIN_VALUE; 
    return (long)(-n * Math.log(p) / Math.log(2.0D) * Math.log(2.0D));
  }
  
  private Object writeReplace() {
    return new SerialForm<T>(this);
  }
  
  private static class SerialForm<T> implements Serializable {
    final long[] data;
    
    final int numHashFunctions;
    
    final Funnel<T> funnel;
    
    final BloomFilter.Strategy strategy;
    
    private static final long serialVersionUID = 1L;
    
    SerialForm(BloomFilter<T> bf) {
      this.data = bf.bits.data;
      this.numHashFunctions = bf.numHashFunctions;
      this.funnel = bf.funnel;
      this.strategy = bf.strategy;
    }
    
    Object readResolve() {
      return new BloomFilter(new BloomFilterStrategies.BitArray(this.data), this.numHashFunctions, this.funnel, this.strategy);
    }
  }
  
  static interface Strategy extends Serializable {
    <T> boolean put(T param1T, Funnel<? super T> param1Funnel, int param1Int, BloomFilterStrategies.BitArray param1BitArray);
    
    <T> boolean mightContain(T param1T, Funnel<? super T> param1Funnel, int param1Int, BloomFilterStrategies.BitArray param1BitArray);
    
    int ordinal();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\hash\BloomFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */