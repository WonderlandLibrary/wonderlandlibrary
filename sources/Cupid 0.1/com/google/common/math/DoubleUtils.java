package com.google.common.math;

import com.google.common.base.Preconditions;
import java.math.BigInteger;

final class DoubleUtils {
  static final long SIGNIFICAND_MASK = 4503599627370495L;
  
  static final long EXPONENT_MASK = 9218868437227405312L;
  
  static final long SIGN_MASK = -9223372036854775808L;
  
  static final int SIGNIFICAND_BITS = 52;
  
  static final int EXPONENT_BIAS = 1023;
  
  static final long IMPLICIT_BIT = 4503599627370496L;
  
  static double nextDown(double d) {
    return -Math.nextUp(-d);
  }
  
  static long getSignificand(double d) {
    Preconditions.checkArgument(isFinite(d), "not a normal value");
    int exponent = Math.getExponent(d);
    long bits = Double.doubleToRawLongBits(d);
    bits &= 0xFFFFFFFFFFFFFL;
    return (exponent == -1023) ? (bits << 1L) : (bits | 0x10000000000000L);
  }
  
  static boolean isFinite(double d) {
    return (Math.getExponent(d) <= 1023);
  }
  
  static boolean isNormal(double d) {
    return (Math.getExponent(d) >= -1022);
  }
  
  static double scaleNormalize(double x) {
    long significand = Double.doubleToRawLongBits(x) & 0xFFFFFFFFFFFFFL;
    return Double.longBitsToDouble(significand | ONE_BITS);
  }
  
  static double bigToDouble(BigInteger x) {
    BigInteger absX = x.abs();
    int exponent = absX.bitLength() - 1;
    if (exponent < 63)
      return x.longValue(); 
    if (exponent > 1023)
      return x.signum() * Double.POSITIVE_INFINITY; 
    int shift = exponent - 52 - 1;
    long twiceSignifFloor = absX.shiftRight(shift).longValue();
    long signifFloor = twiceSignifFloor >> 1L;
    signifFloor &= 0xFFFFFFFFFFFFFL;
    boolean increment = ((twiceSignifFloor & 0x1L) != 0L && ((signifFloor & 0x1L) != 0L || absX.getLowestSetBit() < shift));
    long signifRounded = increment ? (signifFloor + 1L) : signifFloor;
    long bits = (exponent + 1023) << 52L;
    bits += signifRounded;
    bits |= x.signum() & Long.MIN_VALUE;
    return Double.longBitsToDouble(bits);
  }
  
  static double ensureNonNegative(double value) {
    Preconditions.checkArgument(!Double.isNaN(value));
    if (value > 0.0D)
      return value; 
    return 0.0D;
  }
  
  private static final long ONE_BITS = Double.doubleToRawLongBits(1.0D);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\math\DoubleUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */