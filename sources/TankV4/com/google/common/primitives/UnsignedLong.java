package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.io.Serializable;
import java.math.BigInteger;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
public final class UnsignedLong extends Number implements Comparable, Serializable {
   private static final long UNSIGNED_MASK = Long.MAX_VALUE;
   public static final UnsignedLong ZERO = new UnsignedLong(0L);
   public static final UnsignedLong ONE = new UnsignedLong(1L);
   public static final UnsignedLong MAX_VALUE = new UnsignedLong(-1L);
   private final long value;

   private UnsignedLong(long var1) {
      this.value = var1;
   }

   public static UnsignedLong fromLongBits(long var0) {
      return new UnsignedLong(var0);
   }

   public static UnsignedLong valueOf(long var0) {
      Preconditions.checkArgument(var0 >= 0L, "value (%s) is outside the range for an unsigned long value", var0);
      return fromLongBits(var0);
   }

   public static UnsignedLong valueOf(BigInteger var0) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var0.signum() >= 0 && var0.bitLength() <= 64, "value (%s) is outside the range for an unsigned long value", var0);
      return fromLongBits(var0.longValue());
   }

   public static UnsignedLong valueOf(String var0) {
      return valueOf(var0, 10);
   }

   public static UnsignedLong valueOf(String var0, int var1) {
      return fromLongBits(UnsignedLongs.parseUnsignedLong(var0, var1));
   }

   public UnsignedLong plus(UnsignedLong var1) {
      return fromLongBits(this.value + ((UnsignedLong)Preconditions.checkNotNull(var1)).value);
   }

   public UnsignedLong minus(UnsignedLong var1) {
      return fromLongBits(this.value - ((UnsignedLong)Preconditions.checkNotNull(var1)).value);
   }

   @CheckReturnValue
   public UnsignedLong times(UnsignedLong var1) {
      return fromLongBits(this.value * ((UnsignedLong)Preconditions.checkNotNull(var1)).value);
   }

   @CheckReturnValue
   public UnsignedLong dividedBy(UnsignedLong var1) {
      return fromLongBits(UnsignedLongs.divide(this.value, ((UnsignedLong)Preconditions.checkNotNull(var1)).value));
   }

   @CheckReturnValue
   public UnsignedLong mod(UnsignedLong var1) {
      return fromLongBits(UnsignedLongs.remainder(this.value, ((UnsignedLong)Preconditions.checkNotNull(var1)).value));
   }

   public int intValue() {
      return (int)this.value;
   }

   public long longValue() {
      return this.value;
   }

   public float floatValue() {
      float var1 = (float)(this.value & Long.MAX_VALUE);
      if (this.value < 0L) {
         var1 += 9.223372E18F;
      }

      return var1;
   }

   public double doubleValue() {
      double var1 = (double)(this.value & Long.MAX_VALUE);
      if (this.value < 0L) {
         var1 += 9.223372036854776E18D;
      }

      return var1;
   }

   public BigInteger bigIntegerValue() {
      BigInteger var1 = BigInteger.valueOf(this.value & Long.MAX_VALUE);
      if (this.value < 0L) {
         var1 = var1.setBit(63);
      }

      return var1;
   }

   public int compareTo(UnsignedLong var1) {
      Preconditions.checkNotNull(var1);
      return UnsignedLongs.compare(this.value, var1.value);
   }

   public int hashCode() {
      return Longs.hashCode(this.value);
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 instanceof UnsignedLong) {
         UnsignedLong var2 = (UnsignedLong)var1;
         return this.value == var2.value;
      } else {
         return false;
      }
   }

   public String toString() {
      return UnsignedLongs.toString(this.value);
   }

   public String toString(int var1) {
      return UnsignedLongs.toString(this.value, var1);
   }

   public int compareTo(Object var1) {
      return this.compareTo((UnsignedLong)var1);
   }
}
