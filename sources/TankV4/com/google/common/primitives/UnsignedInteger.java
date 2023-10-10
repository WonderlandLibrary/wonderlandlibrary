package com.google.common.primitives;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.math.BigInteger;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
public final class UnsignedInteger extends Number implements Comparable {
   public static final UnsignedInteger ZERO = fromIntBits(0);
   public static final UnsignedInteger ONE = fromIntBits(1);
   public static final UnsignedInteger MAX_VALUE = fromIntBits(-1);
   private final int value;

   private UnsignedInteger(int var1) {
      this.value = var1 & -1;
   }

   public static UnsignedInteger fromIntBits(int var0) {
      return new UnsignedInteger(var0);
   }

   public static UnsignedInteger valueOf(long var0) {
      Preconditions.checkArgument((var0 & 4294967295L) == var0, "value (%s) is outside the range for an unsigned integer value", var0);
      return fromIntBits((int)var0);
   }

   public static UnsignedInteger valueOf(BigInteger var0) {
      Preconditions.checkNotNull(var0);
      Preconditions.checkArgument(var0.signum() >= 0 && var0.bitLength() <= 32, "value (%s) is outside the range for an unsigned integer value", var0);
      return fromIntBits(var0.intValue());
   }

   public static UnsignedInteger valueOf(String var0) {
      return valueOf(var0, 10);
   }

   public static UnsignedInteger valueOf(String var0, int var1) {
      return fromIntBits(UnsignedInts.parseUnsignedInt(var0, var1));
   }

   @CheckReturnValue
   public UnsignedInteger plus(UnsignedInteger var1) {
      return fromIntBits(this.value + ((UnsignedInteger)Preconditions.checkNotNull(var1)).value);
   }

   @CheckReturnValue
   public UnsignedInteger minus(UnsignedInteger var1) {
      return fromIntBits(this.value - ((UnsignedInteger)Preconditions.checkNotNull(var1)).value);
   }

   @CheckReturnValue
   @GwtIncompatible("Does not truncate correctly")
   public UnsignedInteger times(UnsignedInteger var1) {
      return fromIntBits(this.value * ((UnsignedInteger)Preconditions.checkNotNull(var1)).value);
   }

   @CheckReturnValue
   public UnsignedInteger dividedBy(UnsignedInteger var1) {
      return fromIntBits(UnsignedInts.divide(this.value, ((UnsignedInteger)Preconditions.checkNotNull(var1)).value));
   }

   @CheckReturnValue
   public UnsignedInteger mod(UnsignedInteger var1) {
      return fromIntBits(UnsignedInts.remainder(this.value, ((UnsignedInteger)Preconditions.checkNotNull(var1)).value));
   }

   public int intValue() {
      return this.value;
   }

   public long longValue() {
      return UnsignedInts.toLong(this.value);
   }

   public float floatValue() {
      return (float)this.longValue();
   }

   public double doubleValue() {
      return (double)this.longValue();
   }

   public BigInteger bigIntegerValue() {
      return BigInteger.valueOf(this.longValue());
   }

   public int compareTo(UnsignedInteger var1) {
      Preconditions.checkNotNull(var1);
      return UnsignedInts.compare(this.value, var1.value);
   }

   public int hashCode() {
      return this.value;
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 instanceof UnsignedInteger) {
         UnsignedInteger var2 = (UnsignedInteger)var1;
         return this.value == var2.value;
      } else {
         return false;
      }
   }

   public String toString() {
      return this.toString(10);
   }

   public String toString(int var1) {
      return UnsignedInts.toString(this.value, var1);
   }

   public int compareTo(Object var1) {
      return this.compareTo((UnsignedInteger)var1);
   }
}
