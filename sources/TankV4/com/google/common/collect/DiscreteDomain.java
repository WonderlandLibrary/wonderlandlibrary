package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.NoSuchElementException;

@GwtCompatible
@Beta
public abstract class DiscreteDomain {
   public static DiscreteDomain integers() {
      return DiscreteDomain.IntegerDomain.access$000();
   }

   public static DiscreteDomain longs() {
      return DiscreteDomain.LongDomain.access$100();
   }

   public static DiscreteDomain bigIntegers() {
      return DiscreteDomain.BigIntegerDomain.access$200();
   }

   protected DiscreteDomain() {
   }

   public abstract Comparable next(Comparable var1);

   public abstract Comparable previous(Comparable var1);

   public abstract long distance(Comparable var1, Comparable var2);

   public Comparable minValue() {
      throw new NoSuchElementException();
   }

   public Comparable maxValue() {
      throw new NoSuchElementException();
   }

   private static final class BigIntegerDomain extends DiscreteDomain implements Serializable {
      private static final DiscreteDomain.BigIntegerDomain INSTANCE = new DiscreteDomain.BigIntegerDomain();
      private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
      private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
      private static final long serialVersionUID = 0L;

      public BigInteger next(BigInteger var1) {
         return var1.add(BigInteger.ONE);
      }

      public BigInteger previous(BigInteger var1) {
         return var1.subtract(BigInteger.ONE);
      }

      public long distance(BigInteger var1, BigInteger var2) {
         return var2.subtract(var1).max(MIN_LONG).min(MAX_LONG).longValue();
      }

      private Object readResolve() {
         return INSTANCE;
      }

      public String toString() {
         return "DiscreteDomain.bigIntegers()";
      }

      public long distance(Comparable var1, Comparable var2) {
         return this.distance((BigInteger)var1, (BigInteger)var2);
      }

      public Comparable previous(Comparable var1) {
         return this.previous((BigInteger)var1);
      }

      public Comparable next(Comparable var1) {
         return this.next((BigInteger)var1);
      }

      static DiscreteDomain.BigIntegerDomain access$200() {
         return INSTANCE;
      }
   }

   private static final class LongDomain extends DiscreteDomain implements Serializable {
      private static final DiscreteDomain.LongDomain INSTANCE = new DiscreteDomain.LongDomain();
      private static final long serialVersionUID = 0L;

      public Long next(Long var1) {
         long var2 = var1;
         return var2 == Long.MAX_VALUE ? null : var2 + 1L;
      }

      public Long previous(Long var1) {
         long var2 = var1;
         return var2 == Long.MIN_VALUE ? null : var2 - 1L;
      }

      public long distance(Long var1, Long var2) {
         long var3 = var2 - var1;
         if (var2 > var1 && var3 < 0L) {
            return Long.MAX_VALUE;
         } else {
            return var2 < var1 && var3 > 0L ? Long.MIN_VALUE : var3;
         }
      }

      public Long minValue() {
         return Long.MIN_VALUE;
      }

      public Long maxValue() {
         return Long.MAX_VALUE;
      }

      private Object readResolve() {
         return INSTANCE;
      }

      public String toString() {
         return "DiscreteDomain.longs()";
      }

      public Comparable maxValue() {
         return this.maxValue();
      }

      public Comparable minValue() {
         return this.minValue();
      }

      public long distance(Comparable var1, Comparable var2) {
         return this.distance((Long)var1, (Long)var2);
      }

      public Comparable previous(Comparable var1) {
         return this.previous((Long)var1);
      }

      public Comparable next(Comparable var1) {
         return this.next((Long)var1);
      }

      static DiscreteDomain.LongDomain access$100() {
         return INSTANCE;
      }
   }

   private static final class IntegerDomain extends DiscreteDomain implements Serializable {
      private static final DiscreteDomain.IntegerDomain INSTANCE = new DiscreteDomain.IntegerDomain();
      private static final long serialVersionUID = 0L;

      public Integer next(Integer var1) {
         int var2 = var1;
         return var2 == Integer.MAX_VALUE ? null : var2 + 1;
      }

      public Integer previous(Integer var1) {
         int var2 = var1;
         return var2 == Integer.MIN_VALUE ? null : var2 - 1;
      }

      public long distance(Integer var1, Integer var2) {
         return (long)var2 - (long)var1;
      }

      public Integer minValue() {
         return Integer.MIN_VALUE;
      }

      public Integer maxValue() {
         return Integer.MAX_VALUE;
      }

      private Object readResolve() {
         return INSTANCE;
      }

      public String toString() {
         return "DiscreteDomain.integers()";
      }

      public Comparable maxValue() {
         return this.maxValue();
      }

      public Comparable minValue() {
         return this.minValue();
      }

      public long distance(Comparable var1, Comparable var2) {
         return this.distance((Integer)var1, (Integer)var2);
      }

      public Comparable previous(Comparable var1) {
         return this.previous((Integer)var1);
      }

      public Comparable next(Comparable var1) {
         return this.next((Integer)var1);
      }

      static DiscreteDomain.IntegerDomain access$000() {
         return INSTANCE;
      }
   }
}
