// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import java.io.Serializable;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;
import java.math.BigInteger;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public abstract class DiscreteDomain<C extends Comparable>
{
    final boolean supportsFastOffset;
    
    public static DiscreteDomain<Integer> integers() {
        return IntegerDomain.INSTANCE;
    }
    
    public static DiscreteDomain<Long> longs() {
        return LongDomain.INSTANCE;
    }
    
    public static DiscreteDomain<BigInteger> bigIntegers() {
        return BigIntegerDomain.INSTANCE;
    }
    
    protected DiscreteDomain() {
        this(false);
    }
    
    private DiscreteDomain(final boolean supportsFastOffset) {
        this.supportsFastOffset = supportsFastOffset;
    }
    
    C offset(final C origin, final long distance) {
        C current = origin;
        CollectPreconditions.checkNonnegative(distance, "distance");
        for (long i = 0L; i < distance; ++i) {
            current = this.next(current);
            if (current == null) {
                final String value = String.valueOf(origin);
                throw new IllegalArgumentException(new StringBuilder(51 + String.valueOf(value).length()).append("overflowed computing offset(").append(value).append(", ").append(distance).append(")").toString());
            }
        }
        return current;
    }
    
    @CheckForNull
    public abstract C next(final C p0);
    
    @CheckForNull
    public abstract C previous(final C p0);
    
    public abstract long distance(final C p0, final C p1);
    
    @CanIgnoreReturnValue
    public C minValue() {
        throw new NoSuchElementException();
    }
    
    @CanIgnoreReturnValue
    public C maxValue() {
        throw new NoSuchElementException();
    }
    
    private static final class IntegerDomain extends DiscreteDomain<Integer> implements Serializable
    {
        private static final IntegerDomain INSTANCE;
        private static final long serialVersionUID = 0L;
        
        IntegerDomain() {
            super(true, null);
        }
        
        @CheckForNull
        @Override
        public Integer next(final Integer value) {
            final int i = value;
            return (i == Integer.MAX_VALUE) ? null : Integer.valueOf(i + 1);
        }
        
        @CheckForNull
        @Override
        public Integer previous(final Integer value) {
            final int i = value;
            return (i == Integer.MIN_VALUE) ? null : Integer.valueOf(i - 1);
        }
        
        @Override
        Integer offset(final Integer origin, final long distance) {
            CollectPreconditions.checkNonnegative(distance, "distance");
            return Ints.checkedCast(origin + distance);
        }
        
        @Override
        public long distance(final Integer start, final Integer end) {
            return end - (long)start;
        }
        
        @Override
        public Integer minValue() {
            return Integer.MIN_VALUE;
        }
        
        @Override
        public Integer maxValue() {
            return Integer.MAX_VALUE;
        }
        
        private Object readResolve() {
            return IntegerDomain.INSTANCE;
        }
        
        @Override
        public String toString() {
            return "DiscreteDomain.integers()";
        }
        
        static {
            INSTANCE = new IntegerDomain();
        }
    }
    
    private static final class LongDomain extends DiscreteDomain<Long> implements Serializable
    {
        private static final LongDomain INSTANCE;
        private static final long serialVersionUID = 0L;
        
        LongDomain() {
            super(true, null);
        }
        
        @CheckForNull
        @Override
        public Long next(final Long value) {
            final long l = value;
            return (l == Long.MAX_VALUE) ? null : Long.valueOf(l + 1L);
        }
        
        @CheckForNull
        @Override
        public Long previous(final Long value) {
            final long l = value;
            return (l == Long.MIN_VALUE) ? null : Long.valueOf(l - 1L);
        }
        
        @Override
        Long offset(final Long origin, final long distance) {
            CollectPreconditions.checkNonnegative(distance, "distance");
            final long result = origin + distance;
            if (result < 0L) {
                Preconditions.checkArgument(origin < 0L, (Object)"overflow");
            }
            return result;
        }
        
        @Override
        public long distance(final Long start, final Long end) {
            final long result = end - start;
            if (end > start && result < 0L) {
                return Long.MAX_VALUE;
            }
            if (end < start && result > 0L) {
                return Long.MIN_VALUE;
            }
            return result;
        }
        
        @Override
        public Long minValue() {
            return Long.MIN_VALUE;
        }
        
        @Override
        public Long maxValue() {
            return Long.MAX_VALUE;
        }
        
        private Object readResolve() {
            return LongDomain.INSTANCE;
        }
        
        @Override
        public String toString() {
            return "DiscreteDomain.longs()";
        }
        
        static {
            INSTANCE = new LongDomain();
        }
    }
    
    private static final class BigIntegerDomain extends DiscreteDomain<BigInteger> implements Serializable
    {
        private static final BigIntegerDomain INSTANCE;
        private static final BigInteger MIN_LONG;
        private static final BigInteger MAX_LONG;
        private static final long serialVersionUID = 0L;
        
        BigIntegerDomain() {
            super(true, null);
        }
        
        @Override
        public BigInteger next(final BigInteger value) {
            return value.add(BigInteger.ONE);
        }
        
        @Override
        public BigInteger previous(final BigInteger value) {
            return value.subtract(BigInteger.ONE);
        }
        
        @Override
        BigInteger offset(final BigInteger origin, final long distance) {
            CollectPreconditions.checkNonnegative(distance, "distance");
            return origin.add(BigInteger.valueOf(distance));
        }
        
        @Override
        public long distance(final BigInteger start, final BigInteger end) {
            return end.subtract(start).max(BigIntegerDomain.MIN_LONG).min(BigIntegerDomain.MAX_LONG).longValue();
        }
        
        private Object readResolve() {
            return BigIntegerDomain.INSTANCE;
        }
        
        @Override
        public String toString() {
            return "DiscreteDomain.bigIntegers()";
        }
        
        static {
            INSTANCE = new BigIntegerDomain();
            MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
            MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
        }
    }
}
