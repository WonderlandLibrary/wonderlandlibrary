package com.google.common.math;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.math.RoundingMode;

@GwtCompatible(emulated = true)
public final class IntMath {
  @VisibleForTesting
  static final int MAX_POWER_OF_SQRT2_UNSIGNED = -1257966797;
  
  public static boolean isPowerOfTwo(int x) {
    return ((x > 0)) & (((x & x - 1) == 0));
  }
  
  @VisibleForTesting
  static int lessThanBranchFree(int x, int y) {
    return (x - y ^ 0xFFFFFFFF ^ 0xFFFFFFFF) >>> 31;
  }
  
  public static int log2(int x, RoundingMode mode) {
    int leadingZeros, cmp, logFloor;
    MathPreconditions.checkPositive("x", x);
    switch (mode) {
      case UNNECESSARY:
        MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
      case DOWN:
      case FLOOR:
        return 31 - Integer.numberOfLeadingZeros(x);
      case UP:
      case CEILING:
        return 32 - Integer.numberOfLeadingZeros(x - 1);
      case HALF_DOWN:
      case HALF_UP:
      case HALF_EVEN:
        leadingZeros = Integer.numberOfLeadingZeros(x);
        cmp = -1257966797 >>> leadingZeros;
        logFloor = 31 - leadingZeros;
        return logFloor + lessThanBranchFree(cmp, x);
    } 
    throw new AssertionError();
  }
  
  @GwtIncompatible("need BigIntegerMath to adequately test")
  public static int log10(int x, RoundingMode mode) {
    MathPreconditions.checkPositive("x", x);
    int logFloor = log10Floor(x);
    int floorPow = powersOf10[logFloor];
    switch (mode) {
      case UNNECESSARY:
        MathPreconditions.checkRoundingUnnecessary((x == floorPow));
      case DOWN:
      case FLOOR:
        return logFloor;
      case UP:
      case CEILING:
        return logFloor + lessThanBranchFree(floorPow, x);
      case HALF_DOWN:
      case HALF_UP:
      case HALF_EVEN:
        return logFloor + lessThanBranchFree(halfPowersOf10[logFloor], x);
    } 
    throw new AssertionError();
  }
  
  private static int log10Floor(int x) {
    int y = maxLog10ForLeadingZeros[Integer.numberOfLeadingZeros(x)];
    return y - lessThanBranchFree(x, powersOf10[y]);
  }
  
  @VisibleForTesting
  static final byte[] maxLog10ForLeadingZeros = new byte[] { 
      9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 
      6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 
      3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 
      0, 0, 0 };
  
  @VisibleForTesting
  static final int[] powersOf10 = new int[] { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000 };
  
  @VisibleForTesting
  static final int[] halfPowersOf10 = new int[] { 3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, Integer.MAX_VALUE };
  
  @VisibleForTesting
  static final int FLOOR_SQRT_MAX_INT = 46340;
  
  @GwtIncompatible("failing tests")
  public static int pow(int b, int k) {
    MathPreconditions.checkNonNegative("exponent", k);
    switch (b) {
      case 0:
        return (k == 0) ? 1 : 0;
      case 1:
        return 1;
      case -1:
        return ((k & 0x1) == 0) ? 1 : -1;
      case 2:
        return (k < 32) ? (1 << k) : 0;
      case -2:
        if (k < 32)
          return ((k & 0x1) == 0) ? (1 << k) : -(1 << k); 
        return 0;
    } 
    for (int accum = 1;; k >>= 1) {
      switch (k) {
        case 0:
          return accum;
        case 1:
          return b * accum;
      } 
      accum *= ((k & 0x1) == 0) ? 1 : b;
      b *= b;
    } 
  }
  
  @GwtIncompatible("need BigIntegerMath to adequately test")
  public static int sqrt(int x, RoundingMode mode) {
    int halfSquare;
    MathPreconditions.checkNonNegative("x", x);
    int sqrtFloor = sqrtFloor(x);
    switch (mode) {
      case UNNECESSARY:
        MathPreconditions.checkRoundingUnnecessary((sqrtFloor * sqrtFloor == x));
      case DOWN:
      case FLOOR:
        return sqrtFloor;
      case UP:
      case CEILING:
        return sqrtFloor + lessThanBranchFree(sqrtFloor * sqrtFloor, x);
      case HALF_DOWN:
      case HALF_UP:
      case HALF_EVEN:
        halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
        return sqrtFloor + lessThanBranchFree(halfSquare, x);
    } 
    throw new AssertionError();
  }
  
  private static int sqrtFloor(int x) {
    return (int)Math.sqrt(x);
  }
  
  public static int divide(int p, int q, RoundingMode mode) {
    // Byte code:
    //   0: aload_2
    //   1: invokestatic checkNotNull : (Ljava/lang/Object;)Ljava/lang/Object;
    //   4: pop
    //   5: iload_1
    //   6: ifne -> 19
    //   9: new java/lang/ArithmeticException
    //   12: dup
    //   13: ldc '/ by zero'
    //   15: invokespecial <init> : (Ljava/lang/String;)V
    //   18: athrow
    //   19: iload_0
    //   20: iload_1
    //   21: idiv
    //   22: istore_3
    //   23: iload_0
    //   24: iload_1
    //   25: iload_3
    //   26: imul
    //   27: isub
    //   28: istore #4
    //   30: iload #4
    //   32: ifne -> 37
    //   35: iload_3
    //   36: ireturn
    //   37: iconst_1
    //   38: iload_0
    //   39: iload_1
    //   40: ixor
    //   41: bipush #31
    //   43: ishr
    //   44: ior
    //   45: istore #5
    //   47: getstatic com/google/common/math/IntMath$1.$SwitchMap$java$math$RoundingMode : [I
    //   50: aload_2
    //   51: invokevirtual ordinal : ()I
    //   54: iaload
    //   55: tableswitch default -> 238, 1 -> 100, 2 -> 113, 3 -> 140, 4 -> 119, 5 -> 125, 6 -> 155, 7 -> 155, 8 -> 155
    //   100: iload #4
    //   102: ifne -> 109
    //   105: iconst_1
    //   106: goto -> 110
    //   109: iconst_0
    //   110: invokestatic checkRoundingUnnecessary : (Z)V
    //   113: iconst_0
    //   114: istore #6
    //   116: goto -> 246
    //   119: iconst_1
    //   120: istore #6
    //   122: goto -> 246
    //   125: iload #5
    //   127: ifle -> 134
    //   130: iconst_1
    //   131: goto -> 135
    //   134: iconst_0
    //   135: istore #6
    //   137: goto -> 246
    //   140: iload #5
    //   142: ifge -> 149
    //   145: iconst_1
    //   146: goto -> 150
    //   149: iconst_0
    //   150: istore #6
    //   152: goto -> 246
    //   155: iload #4
    //   157: invokestatic abs : (I)I
    //   160: istore #7
    //   162: iload #7
    //   164: iload_1
    //   165: invokestatic abs : (I)I
    //   168: iload #7
    //   170: isub
    //   171: isub
    //   172: istore #8
    //   174: iload #8
    //   176: ifne -> 223
    //   179: aload_2
    //   180: getstatic java/math/RoundingMode.HALF_UP : Ljava/math/RoundingMode;
    //   183: if_acmpeq -> 213
    //   186: aload_2
    //   187: getstatic java/math/RoundingMode.HALF_EVEN : Ljava/math/RoundingMode;
    //   190: if_acmpne -> 197
    //   193: iconst_1
    //   194: goto -> 198
    //   197: iconst_0
    //   198: iload_3
    //   199: iconst_1
    //   200: iand
    //   201: ifeq -> 208
    //   204: iconst_1
    //   205: goto -> 209
    //   208: iconst_0
    //   209: iand
    //   210: ifeq -> 217
    //   213: iconst_1
    //   214: goto -> 218
    //   217: iconst_0
    //   218: istore #6
    //   220: goto -> 246
    //   223: iload #8
    //   225: ifle -> 232
    //   228: iconst_1
    //   229: goto -> 233
    //   232: iconst_0
    //   233: istore #6
    //   235: goto -> 246
    //   238: new java/lang/AssertionError
    //   241: dup
    //   242: invokespecial <init> : ()V
    //   245: athrow
    //   246: iload #6
    //   248: ifeq -> 258
    //   251: iload_3
    //   252: iload #5
    //   254: iadd
    //   255: goto -> 259
    //   258: iload_3
    //   259: ireturn
    // Line number table:
    //   Java source line number -> byte code offset
    //   #279	-> 0
    //   #280	-> 5
    //   #281	-> 9
    //   #283	-> 19
    //   #284	-> 23
    //   #286	-> 30
    //   #287	-> 35
    //   #297	-> 37
    //   #299	-> 47
    //   #301	-> 100
    //   #304	-> 113
    //   #305	-> 116
    //   #307	-> 119
    //   #308	-> 122
    //   #310	-> 125
    //   #311	-> 137
    //   #313	-> 140
    //   #314	-> 152
    //   #318	-> 155
    //   #319	-> 162
    //   #322	-> 174
    //   #323	-> 179
    //   #325	-> 223
    //   #327	-> 235
    //   #329	-> 238
    //   #331	-> 246
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   116	107	6	increment	Z
    //   162	84	7	absRem	I
    //   174	72	8	cmpRemToHalfDivisor	I
    //   0	260	0	p	I
    //   0	260	1	q	I
    //   0	260	2	mode	Ljava/math/RoundingMode;
    //   23	237	3	div	I
    //   30	230	4	rem	I
    //   47	213	5	signum	I
    //   235	25	6	increment	Z
  }
  
  public static int mod(int x, int m) {
    if (m <= 0)
      throw new ArithmeticException("Modulus " + m + " must be > 0"); 
    int result = x % m;
    return (result >= 0) ? result : (result + m);
  }
  
  public static int gcd(int a, int b) {
    MathPreconditions.checkNonNegative("a", a);
    MathPreconditions.checkNonNegative("b", b);
    if (a == 0)
      return b; 
    if (b == 0)
      return a; 
    int aTwos = Integer.numberOfTrailingZeros(a);
    a >>= aTwos;
    int bTwos = Integer.numberOfTrailingZeros(b);
    b >>= bTwos;
    while (a != b) {
      int delta = a - b;
      int minDeltaOrZero = delta & delta >> 31;
      a = delta - minDeltaOrZero - minDeltaOrZero;
      b += minDeltaOrZero;
      a >>= Integer.numberOfTrailingZeros(a);
    } 
    return a << Math.min(aTwos, bTwos);
  }
  
  public static int checkedAdd(int a, int b) {
    long result = a + b;
    MathPreconditions.checkNoOverflow((result == (int)result));
    return (int)result;
  }
  
  public static int checkedSubtract(int a, int b) {
    long result = a - b;
    MathPreconditions.checkNoOverflow((result == (int)result));
    return (int)result;
  }
  
  public static int checkedMultiply(int a, int b) {
    long result = a * b;
    MathPreconditions.checkNoOverflow((result == (int)result));
    return (int)result;
  }
  
  public static int checkedPow(int b, int k) {
    MathPreconditions.checkNonNegative("exponent", k);
    switch (b) {
      case 0:
        return (k == 0) ? 1 : 0;
      case 1:
        return 1;
      case -1:
        return ((k & 0x1) == 0) ? 1 : -1;
      case 2:
        MathPreconditions.checkNoOverflow((k < 31));
        return 1 << k;
      case -2:
        MathPreconditions.checkNoOverflow((k < 32));
        return ((k & 0x1) == 0) ? (1 << k) : (-1 << k);
    } 
    int accum = 1;
    while (true) {
      switch (k) {
        case 0:
          return accum;
        case 1:
          return checkedMultiply(accum, b);
      } 
      if ((k & 0x1) != 0)
        accum = checkedMultiply(accum, b); 
      k >>= 1;
      if (k > 0) {
        MathPreconditions.checkNoOverflow(((-46340 <= b)) & ((b <= 46340)));
        b *= b;
      } 
    } 
  }
  
  public static int factorial(int n) {
    MathPreconditions.checkNonNegative("n", n);
    return (n < factorials.length) ? factorials[n] : Integer.MAX_VALUE;
  }
  
  private static final int[] factorials = new int[] { 
      1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 
      3628800, 39916800, 479001600 };
  
  @GwtIncompatible("need BigIntegerMath to adequately test")
  public static int binomial(int n, int k) {
    MathPreconditions.checkNonNegative("n", n);
    MathPreconditions.checkNonNegative("k", k);
    Preconditions.checkArgument((k <= n), "k (%s) > n (%s)", new Object[] { Integer.valueOf(k), Integer.valueOf(n) });
    if (k > n >> 1)
      k = n - k; 
    if (k >= biggestBinomials.length || n > biggestBinomials[k])
      return Integer.MAX_VALUE; 
    switch (k) {
      case 0:
        return 1;
      case 1:
        return n;
    } 
    long result = 1L;
    for (int i = 0; i < k; i++) {
      result *= (n - i);
      result /= (i + 1);
    } 
    return (int)result;
  }
  
  @VisibleForTesting
  static int[] biggestBinomials = new int[] { 
      Integer.MAX_VALUE, Integer.MAX_VALUE, 65536, 2345, 477, 193, 110, 75, 58, 49, 
      43, 39, 37, 35, 34, 34, 33 };
  
  public static int mean(int x, int y) {
    return (x & y) + ((x ^ y) >> 1);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\math\IntMath.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */