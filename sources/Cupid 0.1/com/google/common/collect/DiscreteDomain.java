package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.NoSuchElementException;

@GwtCompatible
@Beta
public abstract class DiscreteDomain<C extends Comparable> {
  public static DiscreteDomain<Integer> integers() {
    return IntegerDomain.INSTANCE;
  }
  
  private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable {
    private static final IntegerDomain INSTANCE = new IntegerDomain();
    
    private static final long serialVersionUID = 0L;
    
    public Integer next(Integer value) {
      int i = value.intValue();
      return (i == Integer.MAX_VALUE) ? null : Integer.valueOf(i + 1);
    }
    
    public Integer previous(Integer value) {
      int i = value.intValue();
      return (i == Integer.MIN_VALUE) ? null : Integer.valueOf(i - 1);
    }
    
    public long distance(Integer start, Integer end) {
      return end.intValue() - start.intValue();
    }
    
    public Integer minValue() {
      return Integer.valueOf(-2147483648);
    }
    
    public Integer maxValue() {
      return Integer.valueOf(2147483647);
    }
    
    private Object readResolve() {
      return INSTANCE;
    }
    
    public String toString() {
      return "DiscreteDomain.integers()";
    }
  }
  
  public static DiscreteDomain<Long> longs() {
    return LongDomain.INSTANCE;
  }
  
  private static final class LongDomain extends DiscreteDomain<Long> implements Serializable {
    private static final LongDomain INSTANCE = new LongDomain();
    
    private static final long serialVersionUID = 0L;
    
    public Long next(Long value) {
      long l = value.longValue();
      return (l == Long.MAX_VALUE) ? null : Long.valueOf(l + 1L);
    }
    
    public Long previous(Long value) {
      long l = value.longValue();
      return (l == Long.MIN_VALUE) ? null : Long.valueOf(l - 1L);
    }
    
    public long distance(Long start, Long end) {
      long result = end.longValue() - start.longValue();
      if (end.longValue() > start.longValue() && result < 0L)
        return Long.MAX_VALUE; 
      if (end.longValue() < start.longValue() && result > 0L)
        return Long.MIN_VALUE; 
      return result;
    }
    
    public Long minValue() {
      return Long.valueOf(Long.MIN_VALUE);
    }
    
    public Long maxValue() {
      return Long.valueOf(Long.MAX_VALUE);
    }
    
    private Object readResolve() {
      return INSTANCE;
    }
    
    public String toString() {
      return "DiscreteDomain.longs()";
    }
  }
  
  public static DiscreteDomain<BigInteger> bigIntegers() {
    return BigIntegerDomain.INSTANCE;
  }
  
  public abstract C next(C paramC);
  
  public abstract C previous(C paramC);
  
  public abstract long distance(C paramC1, C paramC2);
  
  private static final class BigIntegerDomain extends DiscreteDomain<BigInteger> implements Serializable {
    private static final BigIntegerDomain INSTANCE = new BigIntegerDomain();
    
    private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
    
    private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
    
    private static final long serialVersionUID = 0L;
    
    public BigInteger next(BigInteger value) {
      return value.add(BigInteger.ONE);
    }
    
    public BigInteger previous(BigInteger value) {
      return value.subtract(BigInteger.ONE);
    }
    
    public long distance(BigInteger start, BigInteger end) {
      return end.subtract(start).max(MIN_LONG).min(MAX_LONG).longValue();
    }
    
    private Object readResolve() {
      return INSTANCE;
    }
    
    public String toString() {
      return "DiscreteDomain.bigIntegers()";
    }
  }
  
  public C minValue() {
    throw new NoSuchElementException();
  }
  
  public C maxValue() {
    throw new NoSuchElementException();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\collect\DiscreteDomain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */