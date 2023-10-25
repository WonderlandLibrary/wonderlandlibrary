package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@GwtCompatible(emulated = true)
public final class BigIntegerMath {
  @VisibleForTesting
  static final int SQRT2_PRECOMPUTE_THRESHOLD = 256;
  
  public static boolean isPowerOfTwo(BigInteger x) {
    Preconditions.checkNotNull(x);
    return (x.signum() > 0 && x.getLowestSetBit() == x.bitLength() - 1);
  }
  
  public static int log2(BigInteger x, RoundingMode mode) {
    BigInteger x2;
    int logX2Floor;
    MathPreconditions.checkPositive("x", (BigInteger)Preconditions.checkNotNull(x));
    int logFloor = x.bitLength() - 1;
    switch (mode) {
      case UNNECESSARY:
        MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
      case DOWN:
      case FLOOR:
        return logFloor;
      case UP:
      case CEILING:
        return isPowerOfTwo(x) ? logFloor : (logFloor + 1);
      case HALF_DOWN:
      case HALF_UP:
      case HALF_EVEN:
        if (logFloor < 256) {
          BigInteger halfPower = SQRT2_PRECOMPUTED_BITS.shiftRight(256 - logFloor);
          if (x.compareTo(halfPower) <= 0)
            return logFloor; 
          return logFloor + 1;
        } 
        x2 = x.pow(2);
        logX2Floor = x2.bitLength() - 1;
        return (logX2Floor < 2 * logFloor + 1) ? logFloor : (logFloor + 1);
    } 
    throw new AssertionError();
  }
  
  @VisibleForTesting
  static final BigInteger SQRT2_PRECOMPUTED_BITS = new BigInteger("16a09e667f3bcc908b2fb1366ea957d3e3adec17512775099da2f590b0667322a", 16);
  
  @GwtIncompatible("TODO")
  public static int log10(BigInteger x, RoundingMode mode) {
    BigInteger x2, halfPowerSquared;
    MathPreconditions.checkPositive("x", x);
    if (fitsInLong(x))
      return LongMath.log10(x.longValue(), mode); 
    int approxLog10 = (int)(log2(x, RoundingMode.FLOOR) * LN_2 / LN_10);
    BigInteger approxPow = BigInteger.TEN.pow(approxLog10);
    int approxCmp = approxPow.compareTo(x);
    if (approxCmp > 0) {
      do {
        approxLog10--;
        approxPow = approxPow.divide(BigInteger.TEN);
        approxCmp = approxPow.compareTo(x);
      } while (approxCmp > 0);
    } else {
      BigInteger nextPow = BigInteger.TEN.multiply(approxPow);
      int nextCmp = nextPow.compareTo(x);
      while (nextCmp <= 0) {
        approxLog10++;
        approxPow = nextPow;
        approxCmp = nextCmp;
        nextPow = BigInteger.TEN.multiply(approxPow);
        nextCmp = nextPow.compareTo(x);
      } 
    } 
    int floorLog = approxLog10;
    BigInteger floorPow = approxPow;
    int floorCmp = approxCmp;
    switch (mode) {
      case UNNECESSARY:
        MathPreconditions.checkRoundingUnnecessary((floorCmp == 0));
      case DOWN:
      case FLOOR:
        return floorLog;
      case UP:
      case CEILING:
        return floorPow.equals(x) ? floorLog : (floorLog + 1);
      case HALF_DOWN:
      case HALF_UP:
      case HALF_EVEN:
        x2 = x.pow(2);
        halfPowerSquared = floorPow.pow(2).multiply(BigInteger.TEN);
        return (x2.compareTo(halfPowerSquared) <= 0) ? floorLog : (floorLog + 1);
    } 
    throw new AssertionError();
  }
  
  private static final double LN_10 = Math.log(10.0D);
  
  private static final double LN_2 = Math.log(2.0D);
  
  @GwtIncompatible("TODO")
  public static BigInteger sqrt(BigInteger x, RoundingMode mode) {
    int sqrtFloorInt;
    boolean sqrtFloorIsExact;
    BigInteger halfSquare;
    MathPreconditions.checkNonNegative("x", x);
    if (fitsInLong(x))
      return BigInteger.valueOf(LongMath.sqrt(x.longValue(), mode)); 
    BigInteger sqrtFloor = sqrtFloor(x);
    switch (mode) {
      case UNNECESSARY:
        MathPreconditions.checkRoundingUnnecessary(sqrtFloor.pow(2).equals(x));
      case DOWN:
      case FLOOR:
        return sqrtFloor;
      case UP:
      case CEILING:
        sqrtFloorInt = sqrtFloor.intValue();
        sqrtFloorIsExact = (sqrtFloorInt * sqrtFloorInt == x.intValue() && sqrtFloor.pow(2).equals(x));
        return sqrtFloorIsExact ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
      case HALF_DOWN:
      case HALF_UP:
      case HALF_EVEN:
        halfSquare = sqrtFloor.pow(2).add(sqrtFloor);
        return (halfSquare.compareTo(x) >= 0) ? sqrtFloor : sqrtFloor.add(BigInteger.ONE);
    } 
    throw new AssertionError();
  }
  
  @GwtIncompatible("TODO")
  private static BigInteger sqrtFloor(BigInteger x) {
    BigInteger sqrt0;
    int log2 = log2(x, RoundingMode.FLOOR);
    if (log2 < 1023) {
      sqrt0 = sqrtApproxWithDoubles(x);
    } else {
      int shift = log2 - 52 & 0xFFFFFFFE;
      sqrt0 = sqrtApproxWithDoubles(x.shiftRight(shift)).shiftLeft(shift >> 1);
    } 
    BigInteger sqrt1 = sqrt0.add(x.divide(sqrt0)).shiftRight(1);
    if (sqrt0.equals(sqrt1))
      return sqrt0; 
    while (true) {
      sqrt0 = sqrt1;
      sqrt1 = sqrt0.add(x.divide(sqrt0)).shiftRight(1);
      if (sqrt1.compareTo(sqrt0) >= 0)
        return sqrt0; 
    } 
  }
  
  @GwtIncompatible("TODO")
  private static BigInteger sqrtApproxWithDoubles(BigInteger x) {
    return DoubleMath.roundToBigInteger(Math.sqrt(DoubleUtils.bigToDouble(x)), RoundingMode.HALF_EVEN);
  }
  
  @GwtIncompatible("TODO")
  public static BigInteger divide(BigInteger p, BigInteger q, RoundingMode mode) {
    BigDecimal pDec = new BigDecimal(p);
    BigDecimal qDec = new BigDecimal(q);
    return pDec.divide(qDec, 0, mode).toBigIntegerExact();
  }
  
  public static BigInteger factorial(int n) {
    MathPreconditions.checkNonNegative("n", n);
    if (n < LongMath.factorials.length)
      return BigInteger.valueOf(LongMath.factorials[n]); 
    int approxSize = IntMath.divide(n * IntMath.log2(n, RoundingMode.CEILING), 64, RoundingMode.CEILING);
    ArrayList<BigInteger> bignums = new ArrayList<BigInteger>(approxSize);
    int startingNumber = LongMath.factorials.length;
    long product = LongMath.factorials[startingNumber - 1];
    int shift = Long.numberOfTrailingZeros(product);
    product >>= shift;
    int productBits = LongMath.log2(product, RoundingMode.FLOOR) + 1;
    int bits = LongMath.log2(startingNumber, RoundingMode.FLOOR) + 1;
    int nextPowerOfTwo = 1 << bits - 1;
    long num;
    for (num = startingNumber; num <= n; num++) {
      if ((num & nextPowerOfTwo) != 0L) {
        nextPowerOfTwo <<= 1;
        bits++;
      } 
      int tz = Long.numberOfTrailingZeros(num);
      long normalizedNum = num >> tz;
      shift += tz;
      int normalizedBits = bits - tz;
      if (normalizedBits + productBits >= 64) {
        bignums.add(BigInteger.valueOf(product));
        product = 1L;
        productBits = 0;
      } 
      product *= normalizedNum;
      productBits = LongMath.log2(product, RoundingMode.FLOOR) + 1;
    } 
    if (product > 1L)
      bignums.add(BigInteger.valueOf(product)); 
    return listProduct(bignums).shiftLeft(shift);
  }
  
  static BigInteger listProduct(List<BigInteger> nums) {
    return listProduct(nums, 0, nums.size());
  }
  
  static BigInteger listProduct(List<BigInteger> nums, int start, int end) {
    switch (end - start) {
      case 0:
        return BigInteger.ONE;
      case 1:
        return nums.get(start);
      case 2:
        return ((BigInteger)nums.get(start)).multiply(nums.get(start + 1));
      case 3:
        return ((BigInteger)nums.get(start)).multiply(nums.get(start + 1)).multiply(nums.get(start + 2));
    } 
    int m = end + start >>> 1;
    return listProduct(nums, start, m).multiply(listProduct(nums, m, end));
  }
  
  public static BigInteger binomial(int n, int k) {
    MathPreconditions.checkNonNegative("n", n);
    MathPreconditions.checkNonNegative("k", k);
    Preconditions.checkArgument((k <= n), "k (%s) > n (%s)", new Object[] { Integer.valueOf(k), Integer.valueOf(n) });
    if (k > n >> 1)
      k = n - k; 
    if (k < LongMath.biggestBinomials.length && n <= LongMath.biggestBinomials[k])
      return BigInteger.valueOf(LongMath.binomial(n, k)); 
    BigInteger accum = BigInteger.ONE;
    long numeratorAccum = n;
    long denominatorAccum = 1L;
    int bits = LongMath.log2(n, RoundingMode.CEILING);
    int numeratorBits = bits;
    for (int i = 1; i < k; i++) {
      int p = n - i;
      int q = i + 1;
      if (numeratorBits + bits >= 63) {
        accum = accum.multiply(BigInteger.valueOf(numeratorAccum)).divide(BigInteger.valueOf(denominatorAccum));
        numeratorAccum = p;
        denominatorAccum = q;
        numeratorBits = bits;
      } else {
        numeratorAccum *= p;
        denominatorAccum *= q;
        numeratorBits += bits;
      } 
    } 
    return accum.multiply(BigInteger.valueOf(numeratorAccum)).divide(BigInteger.valueOf(denominatorAccum));
  }
  
  @GwtIncompatible("TODO")
  static boolean fitsInLong(BigInteger x) {
    return (x.bitLength() <= 63);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\math\BigIntegerMath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */