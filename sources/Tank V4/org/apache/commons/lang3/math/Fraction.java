package org.apache.commons.lang3.math;

import java.math.BigInteger;

public final class Fraction extends Number implements Comparable {
   private static final long serialVersionUID = 65382027393090L;
   public static final Fraction ZERO = new Fraction(0, 1);
   public static final Fraction ONE = new Fraction(1, 1);
   public static final Fraction ONE_HALF = new Fraction(1, 2);
   public static final Fraction ONE_THIRD = new Fraction(1, 3);
   public static final Fraction TWO_THIRDS = new Fraction(2, 3);
   public static final Fraction ONE_QUARTER = new Fraction(1, 4);
   public static final Fraction TWO_QUARTERS = new Fraction(2, 4);
   public static final Fraction THREE_QUARTERS = new Fraction(3, 4);
   public static final Fraction ONE_FIFTH = new Fraction(1, 5);
   public static final Fraction TWO_FIFTHS = new Fraction(2, 5);
   public static final Fraction THREE_FIFTHS = new Fraction(3, 5);
   public static final Fraction FOUR_FIFTHS = new Fraction(4, 5);
   private final int numerator;
   private final int denominator;
   private transient int hashCode = 0;
   private transient String toString = null;
   private transient String toProperString = null;

   private Fraction(int var1, int var2) {
      this.numerator = var1;
      this.denominator = var2;
   }

   public static Fraction getFraction(int var0, int var1) {
      if (var1 == 0) {
         throw new ArithmeticException("The denominator must not be zero");
      } else {
         if (var1 < 0) {
            if (var0 == Integer.MIN_VALUE || var1 == Integer.MIN_VALUE) {
               throw new ArithmeticException("overflow: can't negate");
            }

            var0 = -var0;
            var1 = -var1;
         }

         return new Fraction(var0, var1);
      }
   }

   public static Fraction getFraction(int var0, int var1, int var2) {
      if (var2 == 0) {
         throw new ArithmeticException("The denominator must not be zero");
      } else if (var2 < 0) {
         throw new ArithmeticException("The denominator must not be negative");
      } else if (var1 < 0) {
         throw new ArithmeticException("The numerator must not be negative");
      } else {
         long var3;
         if (var0 < 0) {
            var3 = (long)var0 * (long)var2 - (long)var1;
         } else {
            var3 = (long)var0 * (long)var2 + (long)var1;
         }

         if (var3 >= -2147483648L && var3 <= 2147483647L) {
            return new Fraction((int)var3, var2);
         } else {
            throw new ArithmeticException("Numerator too large to represent as an Integer.");
         }
      }
   }

   public static Fraction getReducedFraction(int var0, int var1) {
      if (var1 == 0) {
         throw new ArithmeticException("The denominator must not be zero");
      } else if (var0 == 0) {
         return ZERO;
      } else {
         if (var1 == Integer.MIN_VALUE && (var0 & 1) == 0) {
            var0 /= 2;
            var1 /= 2;
         }

         if (var1 < 0) {
            if (var0 == Integer.MIN_VALUE || var1 == Integer.MIN_VALUE) {
               throw new ArithmeticException("overflow: can't negate");
            }

            var0 = -var0;
            var1 = -var1;
         }

         int var2 = greatestCommonDivisor(var0, var1);
         var0 /= var2;
         var1 /= var2;
         return new Fraction(var0, var1);
      }
   }

   public static Fraction getFraction(double var0) {
      int var2 = var0 < 0.0D ? -1 : 1;
      var0 = Math.abs(var0);
      if (!(var0 > 2.147483647E9D) && !Double.isNaN(var0)) {
         int var3 = (int)var0;
         var0 -= (double)var3;
         int var4 = 0;
         int var5 = 1;
         int var6 = 1;
         int var7 = 0;
         boolean var8 = false;
         boolean var9 = false;
         int var10 = (int)var0;
         boolean var11 = false;
         double var12 = 1.0D;
         double var14 = 0.0D;
         double var16 = var0 - (double)var10;
         double var18 = 0.0D;
         double var22 = Double.MAX_VALUE;
         int var26 = 1;

         double var20;
         int var28;
         do {
            var20 = var22;
            int var29 = (int)(var12 / var16);
            var18 = var12 - (double)var29 * var16;
            int var27 = var10 * var6 + var4;
            var28 = var10 * var7 + var5;
            double var24 = (double)var27 / (double)var28;
            var22 = Math.abs(var0 - var24);
            var10 = var29;
            var12 = var16;
            var16 = var18;
            var4 = var6;
            var5 = var7;
            var6 = var27;
            var7 = var28;
            ++var26;
         } while(var20 > var22 && var28 <= 10000 && var28 > 0 && var26 < 25);

         if (var26 == 25) {
            throw new ArithmeticException("Unable to convert double to fraction");
         } else {
            return getReducedFraction((var4 + var3 * var5) * var2, var5);
         }
      } else {
         throw new ArithmeticException("The value must not be greater than Integer.MAX_VALUE or NaN");
      }
   }

   public static Fraction getFraction(String var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("The string must not be null");
      } else {
         int var1 = var0.indexOf(46);
         if (var1 >= 0) {
            return getFraction(Double.parseDouble(var0));
         } else {
            var1 = var0.indexOf(32);
            int var2;
            int var3;
            if (var1 > 0) {
               var2 = Integer.parseInt(var0.substring(0, var1));
               var0 = var0.substring(var1 + 1);
               var1 = var0.indexOf(47);
               if (var1 < 0) {
                  throw new NumberFormatException("The fraction could not be parsed as the format X Y/Z");
               } else {
                  var3 = Integer.parseInt(var0.substring(0, var1));
                  int var4 = Integer.parseInt(var0.substring(var1 + 1));
                  return getFraction(var2, var3, var4);
               }
            } else {
               var1 = var0.indexOf(47);
               if (var1 < 0) {
                  return getFraction(Integer.parseInt(var0), 1);
               } else {
                  var2 = Integer.parseInt(var0.substring(0, var1));
                  var3 = Integer.parseInt(var0.substring(var1 + 1));
                  return getFraction(var2, var3);
               }
            }
         }
      }
   }

   public int getNumerator() {
      return this.numerator;
   }

   public int getDenominator() {
      return this.denominator;
   }

   public int getProperNumerator() {
      return Math.abs(this.numerator % this.denominator);
   }

   public int getProperWhole() {
      return this.numerator / this.denominator;
   }

   public int intValue() {
      return this.numerator / this.denominator;
   }

   public long longValue() {
      return (long)this.numerator / (long)this.denominator;
   }

   public float floatValue() {
      return (float)this.numerator / (float)this.denominator;
   }

   public double doubleValue() {
      return (double)this.numerator / (double)this.denominator;
   }

   public Fraction reduce() {
      if (this.numerator == 0) {
         return this == ZERO ? this : ZERO;
      } else {
         int var1 = greatestCommonDivisor(Math.abs(this.numerator), this.denominator);
         return var1 == 1 ? this : getFraction(this.numerator / var1, this.denominator / var1);
      }
   }

   public Fraction invert() {
      if (this.numerator == 0) {
         throw new ArithmeticException("Unable to invert zero.");
      } else if (this.numerator == Integer.MIN_VALUE) {
         throw new ArithmeticException("overflow: can't negate numerator");
      } else {
         return this.numerator < 0 ? new Fraction(-this.denominator, -this.numerator) : new Fraction(this.denominator, this.numerator);
      }
   }

   public Fraction negate() {
      if (this.numerator == Integer.MIN_VALUE) {
         throw new ArithmeticException("overflow: too large to negate");
      } else {
         return new Fraction(-this.numerator, this.denominator);
      }
   }

   public Fraction abs() {
      return this.numerator >= 0 ? this : this.negate();
   }

   public Fraction pow(int var1) {
      if (var1 == 1) {
         return this;
      } else if (var1 == 0) {
         return ONE;
      } else if (var1 < 0) {
         return var1 == Integer.MIN_VALUE ? this.invert().pow(2).pow(-(var1 / 2)) : this.invert().pow(-var1);
      } else {
         Fraction var2 = this.multiplyBy(this);
         return var1 % 2 == 0 ? var2.pow(var1 / 2) : var2.pow(var1 / 2).multiplyBy(this);
      }
   }

   private static int greatestCommonDivisor(int var0, int var1) {
      if (var0 != 0 && var1 != 0) {
         if (Math.abs(var0) != 1 && Math.abs(var1) != 1) {
            if (var0 > 0) {
               var0 = -var0;
            }

            if (var1 > 0) {
               var1 = -var1;
            }

            int var2;
            for(var2 = 0; (var0 & 1) == 0 && (var1 & 1) == 0 && var2 < 31; ++var2) {
               var0 /= 2;
               var1 /= 2;
            }

            if (var2 == 31) {
               throw new ArithmeticException("overflow: gcd is 2^31");
            } else {
               int var3 = (var0 & 1) == 1 ? var1 : -(var0 / 2);

               while(true) {
                  while((var3 & 1) != 0) {
                     if (var3 > 0) {
                        var0 = -var3;
                     } else {
                        var1 = var3;
                     }

                     var3 = (var1 - var0) / 2;
                     if (var3 == 0) {
                        return -var0 * (1 << var2);
                     }
                  }

                  var3 /= 2;
               }
            }
         } else {
            return 1;
         }
      } else if (var0 != Integer.MIN_VALUE && var1 != Integer.MIN_VALUE) {
         return Math.abs(var0) + Math.abs(var1);
      } else {
         throw new ArithmeticException("overflow: gcd is 2^31");
      }
   }

   private static int mulAndCheck(int var0, int var1) {
      long var2 = (long)var0 * (long)var1;
      if (var2 >= -2147483648L && var2 <= 2147483647L) {
         return (int)var2;
      } else {
         throw new ArithmeticException("overflow: mul");
      }
   }

   private static int mulPosAndCheck(int var0, int var1) {
      long var2 = (long)var0 * (long)var1;
      if (var2 > 2147483647L) {
         throw new ArithmeticException("overflow: mulPos");
      } else {
         return (int)var2;
      }
   }

   private static int addAndCheck(int var0, int var1) {
      long var2 = (long)var0 + (long)var1;
      if (var2 >= -2147483648L && var2 <= 2147483647L) {
         return (int)var2;
      } else {
         throw new ArithmeticException("overflow: add");
      }
   }

   private static int subAndCheck(int var0, int var1) {
      long var2 = (long)var0 - (long)var1;
      if (var2 >= -2147483648L && var2 <= 2147483647L) {
         return (int)var2;
      } else {
         throw new ArithmeticException("overflow: add");
      }
   }

   public Fraction add(Fraction var1) {
      return this.addSub(var1, true);
   }

   public Fraction subtract(Fraction var1) {
      return this.addSub(var1, false);
   }

   private Fraction addSub(Fraction var1, boolean var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("The fraction must not be null");
      } else if (this.numerator == 0) {
         return var2 ? var1 : var1.negate();
      } else if (var1.numerator == 0) {
         return this;
      } else {
         int var3 = greatestCommonDivisor(this.denominator, var1.denominator);
         if (var3 == 1) {
            int var10 = mulAndCheck(this.numerator, var1.denominator);
            int var11 = mulAndCheck(var1.numerator, this.denominator);
            return new Fraction(var2 ? addAndCheck(var10, var11) : subAndCheck(var10, var11), mulPosAndCheck(this.denominator, var1.denominator));
         } else {
            BigInteger var4 = BigInteger.valueOf((long)this.numerator).multiply(BigInteger.valueOf((long)(var1.denominator / var3)));
            BigInteger var5 = BigInteger.valueOf((long)var1.numerator).multiply(BigInteger.valueOf((long)(this.denominator / var3)));
            BigInteger var6 = var2 ? var4.add(var5) : var4.subtract(var5);
            int var7 = var6.mod(BigInteger.valueOf((long)var3)).intValue();
            int var8 = var7 == 0 ? var3 : greatestCommonDivisor(var7, var3);
            BigInteger var9 = var6.divide(BigInteger.valueOf((long)var8));
            if (var9.bitLength() > 31) {
               throw new ArithmeticException("overflow: numerator too large after multiply");
            } else {
               return new Fraction(var9.intValue(), mulPosAndCheck(this.denominator / var3, var1.denominator / var8));
            }
         }
      }
   }

   public Fraction multiplyBy(Fraction var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("The fraction must not be null");
      } else if (this.numerator != 0 && var1.numerator != 0) {
         int var2 = greatestCommonDivisor(this.numerator, var1.denominator);
         int var3 = greatestCommonDivisor(var1.numerator, this.denominator);
         return getReducedFraction(mulAndCheck(this.numerator / var2, var1.numerator / var3), mulPosAndCheck(this.denominator / var3, var1.denominator / var2));
      } else {
         return ZERO;
      }
   }

   public Fraction divideBy(Fraction var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("The fraction must not be null");
      } else if (var1.numerator == 0) {
         throw new ArithmeticException("The fraction to divide by must not be zero");
      } else {
         return this.multiplyBy(var1.invert());
      }
   }

   public int hashCode() {
      if (this.hashCode == 0) {
         this.hashCode = 37 * (629 + this.getNumerator()) + this.getDenominator();
      }

      return this.hashCode;
   }

   public int compareTo(Fraction var1) {
      if (this == var1) {
         return 0;
      } else if (this.numerator == var1.numerator && this.denominator == var1.denominator) {
         return 0;
      } else {
         long var2 = (long)this.numerator * (long)var1.denominator;
         long var4 = (long)var1.numerator * (long)this.denominator;
         if (var2 == var4) {
            return 0;
         } else {
            return var2 < var4 ? -1 : 1;
         }
      }
   }

   public String toString() {
      if (this.toString == null) {
         this.toString = (new StringBuilder(32)).append(this.getNumerator()).append('/').append(this.getDenominator()).toString();
      }

      return this.toString;
   }

   public String toProperString() {
      if (this.toProperString == null) {
         if (this.numerator == 0) {
            this.toProperString = "0";
         } else if (this.numerator == this.denominator) {
            this.toProperString = "1";
         } else if (this.numerator == -1 * this.denominator) {
            this.toProperString = "-1";
         } else if ((this.numerator > 0 ? -this.numerator : this.numerator) < -this.denominator) {
            int var1 = this.getProperNumerator();
            if (var1 == 0) {
               this.toProperString = Integer.toString(this.getProperWhole());
            } else {
               this.toProperString = (new StringBuilder(32)).append(this.getProperWhole()).append(' ').append(var1).append('/').append(this.getDenominator()).toString();
            }
         } else {
            this.toProperString = (new StringBuilder(32)).append(this.getNumerator()).append('/').append(this.getDenominator()).toString();
         }
      }

      return this.toProperString;
   }

   public int compareTo(Object var1) {
      return this.compareTo((Fraction)var1);
   }
}
