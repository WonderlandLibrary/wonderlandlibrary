package com.ibm.icu.math;

import com.ibm.icu.lang.UCharacter;
import java.io.Serializable;
import java.math.BigInteger;

public class BigDecimal extends Number implements Serializable, Comparable {
   public static final BigDecimal ZERO = new BigDecimal(0L);
   public static final BigDecimal ONE = new BigDecimal(1L);
   public static final BigDecimal TEN = new BigDecimal(10);
   public static final int ROUND_CEILING = 2;
   public static final int ROUND_DOWN = 1;
   public static final int ROUND_FLOOR = 3;
   public static final int ROUND_HALF_DOWN = 5;
   public static final int ROUND_HALF_EVEN = 6;
   public static final int ROUND_HALF_UP = 4;
   public static final int ROUND_UNNECESSARY = 7;
   public static final int ROUND_UP = 0;
   private static final byte ispos = 1;
   private static final byte iszero = 0;
   private static final byte isneg = -1;
   private static final int MinExp = -999999999;
   private static final int MaxExp = 999999999;
   private static final int MinArg = -999999999;
   private static final int MaxArg = 999999999;
   private static final MathContext plainMC = new MathContext(0, 0);
   private static final long serialVersionUID = 8245355804974198832L;
   private static byte[] bytecar = new byte[190];
   private static byte[] bytedig = diginit();
   private byte ind;
   private byte form;
   private byte[] mant;
   private int exp;

   public BigDecimal(java.math.BigDecimal var1) {
      this(var1.toString());
   }

   public BigDecimal(BigInteger var1) {
      this(var1.toString(10));
   }

   public BigDecimal(BigInteger var1, int var2) {
      this(var1.toString(10));
      if (var2 < 0) {
         throw new NumberFormatException("Negative scale: " + var2);
      } else {
         this.exp = -var2;
      }
   }

   public BigDecimal(char[] var1) {
      this(var1, 0, var1.length);
   }

   public BigDecimal(char[] var1, int var2, int var3) {
      this.form = 0;
      boolean var9 = false;
      boolean var10 = false;
      boolean var11 = false;
      boolean var12 = false;
      boolean var13 = false;
      boolean var14 = false;
      boolean var15 = false;
      boolean var16 = false;
      boolean var17 = false;
      if (var3 <= 0) {
         this.bad(var1);
      }

      this.ind = 1;
      if (var1[var2] == '-') {
         --var3;
         if (var3 == 0) {
            this.bad(var1);
         }

         this.ind = -1;
         ++var2;
      } else if (var1[var2] == '+') {
         --var3;
         if (var3 == 0) {
            this.bad(var1);
         }

         ++var2;
      }

      boolean var4 = false;
      boolean var5 = false;
      int var6 = 0;
      int var7 = -1;
      int var8 = -1;
      int var18 = var3;

      int var20;
      char var21;
      int var24;
      char var25;
      int var26;
      for(var20 = var2; var18 > 0; ++var20) {
         var21 = var1[var20];
         if (var21 >= '0' && var21 <= '9') {
            var8 = var20;
            ++var6;
         } else if (var21 == '.') {
            if (var7 >= 0) {
               this.bad(var1);
            }

            var7 = var20 - var2;
         } else {
            if (var21 == 'e' || var21 == 'E') {
               if (var20 - var2 > var3 - 2) {
                  this.bad(var1);
               }

               var11 = false;
               int var22;
               if (var1[var20 + 1] == '-') {
                  var11 = true;
                  var22 = var20 + 2;
               } else if (var1[var20 + 1] == '+') {
                  var22 = var20 + 2;
               } else {
                  var22 = var20 + 1;
               }

               int var23 = var3 - (var22 - var2);
               if (var23 == 0 | var23 > 9) {
                  this.bad(var1);
               }

               int var19 = var23;

               for(var24 = var22; var19 > 0; ++var24) {
                  var25 = var1[var24];
                  if (var25 < '0') {
                     this.bad(var1);
                  }

                  if (var25 > '9') {
                     if (!UCharacter.isDigit(var25)) {
                        this.bad(var1);
                     }

                     var26 = UCharacter.digit(var25, 10);
                     if (var26 < 0) {
                        this.bad(var1);
                     }
                  } else {
                     var26 = var25 - 48;
                  }

                  this.exp = this.exp * 10 + var26;
                  --var19;
               }

               if (var11) {
                  this.exp = -this.exp;
               }

               var5 = true;
               break;
            }

            if (!UCharacter.isDigit(var21)) {
               this.bad(var1);
            }

            var4 = true;
            var8 = var20;
            ++var6;
         }

         --var18;
      }

      if (var6 == 0) {
         this.bad(var1);
      }

      if (var7 >= 0) {
         this.exp = this.exp + var7 - var6;
      }

      var18 = var8 - 1;

      for(var20 = var2; var20 <= var18; ++var20) {
         var21 = var1[var20];
         if (var21 == '0') {
            ++var2;
            --var7;
            --var6;
         } else if (var21 == '.') {
            ++var2;
            --var7;
         } else {
            if (var21 <= '9' || UCharacter.digit(var21, 10) != 0) {
               break;
            }

            ++var2;
            --var7;
            --var6;
         }
      }

      this.mant = new byte[var6];
      var24 = var2;
      if (var4) {
         var18 = var6;

         for(var20 = 0; var18 > 0; ++var20) {
            if (var20 == var7) {
               ++var24;
            }

            var25 = var1[var24];
            if (var25 <= '9') {
               this.mant[var20] = (byte)(var25 - 48);
            } else {
               var26 = UCharacter.digit(var25, 10);
               if (var26 < 0) {
                  this.bad(var1);
               }

               this.mant[var20] = (byte)var26;
            }

            ++var24;
            --var18;
         }
      } else {
         var18 = var6;

         for(var20 = 0; var18 > 0; ++var20) {
            if (var20 == var7) {
               ++var24;
            }

            this.mant[var20] = (byte)(var1[var24] - 48);
            ++var24;
            --var18;
         }
      }

      if (this.mant[0] == 0) {
         this.ind = 0;
         if (this.exp > 0) {
            this.exp = 0;
         }

         if (var5) {
            this.mant = ZERO.mant;
            this.exp = 0;
         }
      } else if (var5) {
         this.form = 1;
         int var27 = this.exp + this.mant.length - 1;
         if (var27 < -999999999 | var27 > 999999999) {
            this.bad(var1);
         }
      }

   }

   public BigDecimal(double var1) {
      this((new java.math.BigDecimal(var1)).toString());
   }

   public BigDecimal(int var1) {
      this.form = 0;
      boolean var3 = false;
      if (var1 <= 9 && var1 >= -9) {
         if (var1 == 0) {
            this.mant = ZERO.mant;
            this.ind = 0;
         } else if (var1 == 1) {
            this.mant = ONE.mant;
            this.ind = 1;
         } else if (var1 == -1) {
            this.mant = ONE.mant;
            this.ind = -1;
         } else {
            this.mant = new byte[1];
            if (var1 > 0) {
               this.mant[0] = (byte)var1;
               this.ind = 1;
            } else {
               this.mant[0] = (byte)(-var1);
               this.ind = -1;
            }
         }

      } else {
         if (var1 > 0) {
            this.ind = 1;
            var1 = -var1;
         } else {
            this.ind = -1;
         }

         int var2 = var1;
         int var4 = 9;

         while(true) {
            var2 /= 10;
            if (var2 == 0) {
               this.mant = new byte[10 - var4];
               var4 = 10 - var4 - 1;

               while(true) {
                  this.mant[var4] = (byte)(-((byte)(var1 % 10)));
                  var1 /= 10;
                  if (var1 == 0) {
                     return;
                  }

                  --var4;
               }
            }

            --var4;
         }
      }
   }

   public BigDecimal(long var1) {
      this.form = 0;
      boolean var5 = false;
      if (var1 > 0L) {
         this.ind = 1;
         var1 = -var1;
      } else if (var1 == 0L) {
         this.ind = 0;
      } else {
         this.ind = -1;
      }

      long var3 = var1;
      int var6 = 18;

      while(true) {
         var3 /= 10L;
         if (var3 == 0L) {
            this.mant = new byte[19 - var6];
            var6 = 19 - var6 - 1;

            while(true) {
               this.mant[var6] = (byte)(-((byte)((int)(var1 % 10L))));
               var1 /= 10L;
               if (var1 == 0L) {
                  return;
               }

               --var6;
            }
         }

         --var6;
      }
   }

   public BigDecimal(String var1) {
      this(var1.toCharArray(), 0, var1.length());
   }

   private BigDecimal() {
      this.form = 0;
   }

   public BigDecimal abs() {
      return this.abs(plainMC);
   }

   public BigDecimal abs(MathContext var1) {
      return this.ind == -1 ? this.negate(var1) : this.plus(var1);
   }

   public BigDecimal add(BigDecimal var1) {
      return this.add(var1, plainMC);
   }

   public BigDecimal add(BigDecimal var1, MathContext var2) {
      boolean var10 = false;
      boolean var11 = false;
      boolean var12 = false;
      Object var13 = null;
      boolean var14 = false;
      boolean var15 = false;
      boolean var16 = false;
      boolean var17 = false;
      boolean var18 = false;
      boolean var19 = false;
      if (var2.lostDigits) {
         this.checkdigits(var1, var2.digits);
      }

      BigDecimal var3 = this;
      if (this.ind == 0 && var2.form != 0) {
         return var1.plus(var2);
      } else if (var1.ind == 0 && var2.form != 0) {
         return this.plus(var2);
      } else {
         int var4 = var2.digits;
         if (var4 > 0) {
            if (this.mant.length > var4) {
               var3 = clone(this).round(var2);
            }

            if (var1.mant.length > var4) {
               var1 = clone(var1).round(var2);
            }
         }

         BigDecimal var5 = new BigDecimal();
         byte[] var6 = var3.mant;
         int var7 = var3.mant.length;
         byte[] var8 = var1.mant;
         int var9 = var1.mant.length;
         int var21;
         if (var3.exp == var1.exp) {
            var5.exp = var3.exp;
         } else {
            int var20;
            if (var3.exp > var1.exp) {
               var20 = var7 + var3.exp - var1.exp;
               if (var20 >= var9 + var4 + 1 && var4 > 0) {
                  var5.mant = var6;
                  var5.exp = var3.exp;
                  var5.ind = var3.ind;
                  if (var7 < var4) {
                     var5.mant = extend(var3.mant, var4);
                     var5.exp -= var4 - var7;
                  }

                  return var5.finish(var2, false);
               }

               var5.exp = var1.exp;
               if (var20 > var4 + 1 && var4 > 0) {
                  var21 = var20 - var4 - 1;
                  var9 -= var21;
                  var5.exp += var21;
                  var20 = var4 + 1;
               }

               if (var20 > var7) {
                  var7 = var20;
               }
            } else {
               var20 = var9 + var1.exp - var3.exp;
               if (var20 >= var7 + var4 + 1 && var4 > 0) {
                  var5.mant = var8;
                  var5.exp = var1.exp;
                  var5.ind = var1.ind;
                  if (var9 < var4) {
                     var5.mant = extend(var1.mant, var4);
                     var5.exp -= var4 - var9;
                  }

                  return var5.finish(var2, false);
               }

               var5.exp = var3.exp;
               if (var20 > var4 + 1 && var4 > 0) {
                  var21 = var20 - var4 - 1;
                  var7 -= var21;
                  var5.exp += var21;
                  var20 = var4 + 1;
               }

               if (var20 > var9) {
                  var9 = var20;
               }
            }
         }

         if (var3.ind == 0) {
            var5.ind = 1;
         } else {
            var5.ind = var3.ind;
         }

         byte var22;
         if (var3.ind == -1 == (var1.ind == -1)) {
            var22 = 1;
         } else {
            var22 = -1;
            if (var1.ind != 0) {
               byte[] var23;
               if (var7 < var9 | var3.ind == 0) {
                  var23 = var6;
                  var6 = var8;
                  var8 = var23;
                  var21 = var7;
                  var7 = var9;
                  var9 = var21;
                  var5.ind = (byte)(-var5.ind);
               } else if (var7 <= var9) {
                  int var24 = 0;
                  int var25 = 0;
                  int var26 = var6.length - 1;
                  int var27 = var8.length - 1;

                  while(true) {
                     byte var28;
                     if (var24 <= var26) {
                        var28 = var6[var24];
                     } else {
                        if (var25 > var27) {
                           if (var2.form != 0) {
                              return ZERO;
                           }
                           break;
                        }

                        var28 = 0;
                     }

                     byte var29;
                     if (var25 <= var27) {
                        var29 = var8[var25];
                     } else {
                        var29 = 0;
                     }

                     if (var28 != var29) {
                        if (var28 < var29) {
                           var23 = var6;
                           var6 = var8;
                           var8 = var23;
                           var21 = var7;
                           var7 = var9;
                           var9 = var21;
                           var5.ind = (byte)(-var5.ind);
                        }
                        break;
                     }

                     ++var24;
                     ++var25;
                  }
               }
            }
         }

         var5.mant = byteaddsub(var6, var7, var8, var9, var22, false);
         return var5.finish(var2, false);
      }
   }

   public int compareTo(BigDecimal var1) {
      return this.compareTo(var1, plainMC);
   }

   public int compareTo(BigDecimal var1, MathContext var2) {
      boolean var3 = false;
      boolean var4 = false;
      if (var2.lostDigits) {
         this.checkdigits(var1, var2.digits);
      }

      if (this.ind == var1.ind & this.exp == var1.exp) {
         int var7 = this.mant.length;
         if (var7 < var1.mant.length) {
            return (byte)(-this.ind);
         }

         if (var7 > var1.mant.length) {
            return this.ind;
         }

         if (var7 <= var2.digits | var2.digits == 0) {
            int var6 = var7;

            for(int var8 = 0; var6 > 0; ++var8) {
               if (this.mant[var8] < var1.mant[var8]) {
                  return (byte)(-this.ind);
               }

               if (this.mant[var8] > var1.mant[var8]) {
                  return this.ind;
               }

               --var6;
            }

            return 0;
         }
      } else {
         if (this.ind < var1.ind) {
            return -1;
         }

         if (this.ind > var1.ind) {
            return 1;
         }
      }

      BigDecimal var5 = clone(var1);
      var5.ind = (byte)(-var5.ind);
      return this.add(var5, var2).ind;
   }

   public BigDecimal divide(BigDecimal var1) {
      return this.dodivide('D', var1, plainMC, -1);
   }

   public BigDecimal divide(BigDecimal var1, int var2) {
      MathContext var3 = new MathContext(0, 0, false, var2);
      return this.dodivide('D', var1, var3, -1);
   }

   public BigDecimal divide(BigDecimal var1, int var2, int var3) {
      if (var2 < 0) {
         throw new ArithmeticException("Negative scale: " + var2);
      } else {
         MathContext var4 = new MathContext(0, 0, false, var3);
         return this.dodivide('D', var1, var4, var2);
      }
   }

   public BigDecimal divide(BigDecimal var1, MathContext var2) {
      return this.dodivide('D', var1, var2, -1);
   }

   public BigDecimal divideInteger(BigDecimal var1) {
      return this.dodivide('I', var1, plainMC, 0);
   }

   public BigDecimal divideInteger(BigDecimal var1, MathContext var2) {
      return this.dodivide('I', var1, var2, 0);
   }

   public BigDecimal max(BigDecimal var1) {
      return this.max(var1, plainMC);
   }

   public BigDecimal max(BigDecimal var1, MathContext var2) {
      return this.compareTo(var1, var2) >= 0 ? this.plus(var2) : var1.plus(var2);
   }

   public BigDecimal min(BigDecimal var1) {
      return this.min(var1, plainMC);
   }

   public BigDecimal min(BigDecimal var1, MathContext var2) {
      return this.compareTo(var1, var2) <= 0 ? this.plus(var2) : var1.plus(var2);
   }

   public BigDecimal multiply(BigDecimal var1) {
      return this.multiply(var1, plainMC);
   }

   public BigDecimal multiply(BigDecimal var1, MathContext var2) {
      Object var6 = null;
      Object var7 = null;
      boolean var9 = false;
      boolean var12 = false;
      boolean var13 = false;
      if (var2.lostDigits) {
         this.checkdigits(var1, var2.digits);
      }

      BigDecimal var3 = this;
      int var4 = 0;
      int var5 = var2.digits;
      if (var5 > 0) {
         if (this.mant.length > var5) {
            var3 = clone(this).round(var2);
         }

         if (var1.mant.length > var5) {
            var1 = clone(var1).round(var2);
         }
      } else {
         if (this.exp > 0) {
            var4 += this.exp;
         }

         if (var1.exp > 0) {
            var4 += var1.exp;
         }
      }

      byte[] var15;
      byte[] var16;
      if (var3.mant.length < var1.mant.length) {
         var15 = var3.mant;
         var16 = var1.mant;
      } else {
         var15 = var1.mant;
         var16 = var3.mant;
      }

      int var8 = var15.length + var16.length - 1;
      int var17;
      if (var15[0] * var16[0] > 9) {
         var17 = var8 + 1;
      } else {
         var17 = var8;
      }

      BigDecimal var10 = new BigDecimal();
      byte[] var11 = new byte[var17];
      int var14 = var15.length;

      for(int var18 = 0; var14 > 0; ++var18) {
         byte var19 = var15[var18];
         if (var19 != 0) {
            var11 = byteaddsub(var11, var11.length, var16, var8, var19, true);
         }

         --var8;
         --var14;
      }

      var10.ind = (byte)(var3.ind * var1.ind);
      var10.exp = var3.exp + var1.exp - var4;
      if (var4 == 0) {
         var10.mant = var11;
      } else {
         var10.mant = extend(var11, var11.length + var4);
      }

      return var10.finish(var2, false);
   }

   public BigDecimal negate() {
      return this.negate(plainMC);
   }

   public BigDecimal negate(MathContext var1) {
      if (var1.lostDigits) {
         this.checkdigits((BigDecimal)null, var1.digits);
      }

      BigDecimal var2 = clone(this);
      var2.ind = (byte)(-var2.ind);
      return var2.finish(var1, false);
   }

   public BigDecimal plus() {
      return this.plus(plainMC);
   }

   public BigDecimal plus(MathContext var1) {
      if (var1.lostDigits) {
         this.checkdigits((BigDecimal)null, var1.digits);
      }

      if (var1.form == 0 && this.form == 0) {
         if (this.mant.length <= var1.digits) {
            return this;
         }

         if (var1.digits == 0) {
            return this;
         }
      }

      return clone(this).finish(var1, false);
   }

   public BigDecimal pow(BigDecimal var1) {
      return this.pow(var1, plainMC);
   }

   public BigDecimal pow(BigDecimal var1, MathContext var2) {
      boolean var6 = false;
      boolean var7 = false;
      boolean var11 = false;
      if (var2.lostDigits) {
         this.checkdigits(var1, var2.digits);
      }

      int var3 = var1.intcheck(-999999999, 999999999);
      BigDecimal var4 = this;
      int var5 = var2.digits;
      int var12;
      if (var5 == 0) {
         if (var1.ind == -1) {
            throw new ArithmeticException("Negative power: " + var1.toString());
         }

         var12 = 0;
      } else {
         if (var1.mant.length + var1.exp > var5) {
            throw new ArithmeticException("Too many digits: " + var1.toString());
         }

         if (this.mant.length > var5) {
            var4 = clone(this).round(var2);
         }

         int var13 = var1.mant.length + var1.exp;
         var12 = var5 + var13 + 1;
      }

      MathContext var8 = new MathContext(var12, var2.form, false, var2.roundingMode);
      BigDecimal var9 = ONE;
      if (var3 == 0) {
         return var9;
      } else {
         if (var3 < 0) {
            var3 = -var3;
         }

         boolean var10 = false;
         int var14 = 1;

         while(true) {
            var3 += var3;
            if (var3 < 0) {
               var10 = true;
               var9 = var9.multiply(var4, var8);
            }

            if (var14 == 31) {
               if (var1.ind < 0) {
                  var9 = ONE.divide(var9, var8);
               }

               return var9.finish(var2, true);
            }

            if (var10) {
               var9 = var9.multiply(var9, var8);
            }

            ++var14;
         }
      }
   }

   public BigDecimal remainder(BigDecimal var1) {
      return this.dodivide('R', var1, plainMC, -1);
   }

   public BigDecimal remainder(BigDecimal var1, MathContext var2) {
      return this.dodivide('R', var1, var2, -1);
   }

   public BigDecimal subtract(BigDecimal var1) {
      return this.subtract(var1, plainMC);
   }

   public BigDecimal subtract(BigDecimal var1, MathContext var2) {
      if (var2.lostDigits) {
         this.checkdigits(var1, var2.digits);
      }

      BigDecimal var3 = clone(var1);
      var3.ind = (byte)(-var3.ind);
      return this.add(var3, var2);
   }

   public byte byteValueExact() {
      int var1 = this.intValueExact();
      if (var1 > 127 | var1 < -128) {
         throw new ArithmeticException("Conversion overflow: " + this.toString());
      } else {
         return (byte)var1;
      }
   }

   public double doubleValue() {
      return Double.valueOf(this.toString());
   }

   public boolean equals(Object var1) {
      boolean var3 = false;
      Object var4 = null;
      Object var5 = null;
      if (var1 == null) {
         return false;
      } else if (!(var1 instanceof BigDecimal)) {
         return false;
      } else {
         BigDecimal var2 = (BigDecimal)var1;
         if (this.ind != var2.ind) {
            return false;
         } else {
            int var6;
            int var7;
            if (this.mant.length == var2.mant.length & this.exp == var2.exp & this.form == var2.form) {
               var6 = this.mant.length;

               for(var7 = 0; var6 > 0; ++var7) {
                  if (this.mant[var7] != var2.mant[var7]) {
                     return false;
                  }

                  --var6;
               }
            } else {
               char[] var8 = this.layout();
               char[] var9 = var2.layout();
               if (var8.length != var9.length) {
                  return false;
               }

               var6 = var8.length;

               for(var7 = 0; var6 > 0; ++var7) {
                  if (var8[var7] != var9[var7]) {
                     return false;
                  }

                  --var6;
               }
            }

            return true;
         }
      }
   }

   public float floatValue() {
      return Float.valueOf(this.toString());
   }

   public String format(int var1, int var2) {
      return this.format(var1, var2, -1, -1, 1, 4);
   }

   public String format(int var1, int var2, int var3, int var4, int var5, int var6) {
      boolean var8 = false;
      boolean var9 = false;
      boolean var10 = false;
      Object var11 = null;
      boolean var12 = false;
      boolean var13 = false;
      boolean var14 = false;
      boolean var16 = false;
      Object var17 = null;
      boolean var18 = false;
      boolean var19 = false;
      if (var1 < -1 | var1 == 0) {
         this.badarg("format", 1, String.valueOf(var1));
      }

      if (var2 < -1) {
         this.badarg("format", 2, String.valueOf(var2));
      }

      if (var3 < -1 | var3 == 0) {
         this.badarg("format", 3, String.valueOf(var3));
      }

      if (var4 < -1) {
         this.badarg("format", 4, String.valueOf(var3));
      }

      if (var5 != 1 && var5 != 2) {
         if (var5 == -1) {
            var5 = 1;
         } else {
            this.badarg("format", 5, String.valueOf(var5));
         }
      }

      if (var6 != 4) {
         try {
            if (var6 == -1) {
               var6 = 4;
            } else {
               new MathContext(9, 1, false, var6);
            }
         } catch (IllegalArgumentException var21) {
            this.badarg("format", 6, String.valueOf(var6));
         }
      }

      BigDecimal var7 = clone(this);
      if (var4 == -1) {
         var7.form = 0;
      } else if (var7.ind == 0) {
         var7.form = 0;
      } else {
         int var22 = var7.exp + var7.mant.length;
         if (var22 > var4) {
            var7.form = (byte)var5;
         } else if (var22 < -5) {
            var7.form = (byte)var5;
         } else {
            var7.form = 0;
         }
      }

      if (var2 >= 0) {
         while(true) {
            int var23;
            if (var7.form == 0) {
               var23 = -var7.exp;
            } else if (var7.form == 1) {
               var23 = var7.mant.length - 1;
            } else {
               int var24 = (var7.exp + var7.mant.length - 1) % 3;
               if (var24 < 0) {
                  var24 += 3;
               }

               ++var24;
               if (var24 >= var7.mant.length) {
                  var23 = 0;
               } else {
                  var23 = var7.mant.length - var24;
               }
            }

            if (var23 == var2) {
               break;
            }

            if (var23 < var2) {
               byte[] var25 = extend(var7.mant, var7.mant.length + var2 - var23);
               var7.mant = var25;
               var7.exp -= var2 - var23;
               if (var7.exp < -999999999) {
                  throw new ArithmeticException("Exponent Overflow: " + var7.exp);
               }
               break;
            }

            int var26 = var23 - var2;
            if (var26 > var7.mant.length) {
               var7.mant = ZERO.mant;
               var7.ind = 0;
               var7.exp = 0;
            } else {
               int var27 = var7.mant.length - var26;
               int var28 = var7.exp;
               var7.round(var27, var6);
               if (var7.exp - var28 == var26) {
                  break;
               }
            }
         }
      }

      char[] var15 = var7.layout();
      int var20;
      int var29;
      char[] var30;
      int var31;
      if (var1 > 0) {
         var20 = var15.length;

         for(var29 = 0; var20 > 0 && var15[var29] != '.' && var15[var29] != 'E'; ++var29) {
            --var20;
         }

         if (var29 > var1) {
            this.badarg("format", 1, String.valueOf(var1));
         }

         if (var29 < var1) {
            var30 = new char[var15.length + var1 - var29];
            var20 = var1 - var29;

            for(var31 = 0; var20 > 0; ++var31) {
               var30[var31] = ' ';
               --var20;
            }

            System.arraycopy(var15, 0, var30, var31, var15.length);
            var15 = var30;
         }
      }

      if (var3 > 0) {
         var20 = var15.length - 1;

         for(var29 = var15.length - 1; var20 > 0 && var15[var29] != 'E'; --var29) {
            --var20;
         }

         if (var29 == 0) {
            var30 = new char[var15.length + var3 + 2];
            System.arraycopy(var15, 0, var30, 0, var15.length);
            var20 = var3 + 2;

            for(var31 = var15.length; var20 > 0; ++var31) {
               var30[var31] = ' ';
               --var20;
            }

            var15 = var30;
         } else {
            int var32 = var15.length - var29 - 2;
            if (var32 > var3) {
               this.badarg("format", 3, String.valueOf(var3));
            }

            if (var32 < var3) {
               var30 = new char[var15.length + var3 - var32];
               System.arraycopy(var15, 0, var30, 0, var29 + 2);
               var20 = var3 - var32;

               for(var31 = var29 + 2; var20 > 0; ++var31) {
                  var30[var31] = '0';
                  --var20;
               }

               System.arraycopy(var15, var29 + 2, var30, var31, var32);
               var15 = var30;
            }
         }
      }

      return new String(var15);
   }

   public int hashCode() {
      return this.toString().hashCode();
   }

   public int intValue() {
      return this.toBigInteger().intValue();
   }

   public int intValueExact() {
      boolean var2 = false;
      boolean var4 = false;
      boolean var5 = false;
      if (this.ind == 0) {
         return 0;
      } else {
         int var1 = this.mant.length - 1;
         int var7;
         if (this.exp < 0) {
            var1 += this.exp;
            byte[] var10000 = this.mant;
            if (var1 + 1 < 0) {
               throw new ArithmeticException("Decimal part non-zero: " + this.toString());
            }

            if (var1 < 0) {
               return 0;
            }

            var7 = 0;
         } else {
            if (this.exp + var1 > 9) {
               throw new ArithmeticException("Conversion overflow: " + this.toString());
            }

            var7 = this.exp;
         }

         int var3 = 0;
         int var6 = var1 + var7;

         for(int var8 = 0; var8 <= var6; ++var8) {
            var3 *= 10;
            if (var8 <= var1) {
               var3 += this.mant[var8];
            }
         }

         if (var1 + var7 == 9) {
            int var9 = var3 / 1000000000;
            if (var9 != this.mant[0]) {
               if (var3 == Integer.MIN_VALUE && this.ind == -1 && this.mant[0] == 2) {
                  return var3;
               }

               throw new ArithmeticException("Conversion overflow: " + this.toString());
            }
         }

         if (this.ind == 1) {
            return var3;
         } else {
            return -var3;
         }
      }
   }

   public long longValue() {
      return this.toBigInteger().longValue();
   }

   public long longValueExact() {
      boolean var2 = false;
      boolean var3 = false;
      boolean var6 = false;
      long var7 = 0L;
      if (this.ind == 0) {
         return 0L;
      } else {
         int var1 = this.mant.length - 1;
         int var11;
         if (this.exp < 0) {
            var1 += this.exp;
            int var10;
            if (var1 < 0) {
               var10 = 0;
            } else {
               var10 = var1 + 1;
            }

            byte[] var10000 = this.mant;
            if (var10 < 0) {
               throw new ArithmeticException("Decimal part non-zero: " + this.toString());
            }

            if (var1 < 0) {
               return 0L;
            }

            var11 = 0;
         } else {
            if (this.exp + this.mant.length > 18) {
               throw new ArithmeticException("Conversion overflow: " + this.toString());
            }

            var11 = this.exp;
         }

         long var4 = 0L;
         int var9 = var1 + var11;

         for(int var12 = 0; var12 <= var9; ++var12) {
            var4 *= 10L;
            if (var12 <= var1) {
               var4 += (long)this.mant[var12];
            }
         }

         if (var1 + var11 == 18) {
            var7 = var4 / 1000000000000000000L;
            if (var7 != (long)this.mant[0]) {
               if (var4 == Long.MIN_VALUE && this.ind == -1 && this.mant[0] == 9) {
                  return var4;
               }

               throw new ArithmeticException("Conversion overflow: " + this.toString());
            }
         }

         if (this.ind == 1) {
            return var4;
         } else {
            return -var4;
         }
      }
   }

   public BigDecimal movePointLeft(int var1) {
      BigDecimal var2 = clone(this);
      var2.exp -= var1;
      return var2.finish(plainMC, false);
   }

   public BigDecimal movePointRight(int var1) {
      BigDecimal var2 = clone(this);
      var2.exp += var1;
      return var2.finish(plainMC, false);
   }

   public int scale() {
      return this.exp >= 0 ? 0 : -this.exp;
   }

   public BigDecimal setScale(int var1) {
      return this.setScale(var1, 7);
   }

   public BigDecimal setScale(int var1, int var2) {
      boolean var5 = false;
      boolean var6 = false;
      int var3 = this.scale();
      if (var3 == var1 && this.form == 0) {
         return this;
      } else {
         BigDecimal var4 = clone(this);
         if (var3 <= var1) {
            int var7;
            if (var3 == 0) {
               var7 = var4.exp + var1;
            } else {
               var7 = var1 - var3;
            }

            var4.mant = extend(var4.mant, var4.mant.length + var7);
            var4.exp = -var1;
         } else {
            if (var1 < 0) {
               throw new ArithmeticException("Negative scale: " + var1);
            }

            int var8 = var4.mant.length - (var3 - var1);
            var4 = var4.round(var8, var2);
            if (var4.exp != -var1) {
               var4.mant = extend(var4.mant, var4.mant.length + 1);
               --var4.exp;
            }
         }

         var4.form = 0;
         return var4;
      }
   }

   public short shortValueExact() {
      int var1 = this.intValueExact();
      if (var1 > 32767 | var1 < -32768) {
         throw new ArithmeticException("Conversion overflow: " + this.toString());
      } else {
         return (short)var1;
      }
   }

   public int signum() {
      return this.ind;
   }

   public java.math.BigDecimal toBigDecimal() {
      return new java.math.BigDecimal(this.unscaledValue(), this.scale());
   }

   public BigInteger toBigInteger() {
      BigDecimal var1 = null;
      boolean var2 = false;
      Object var3 = null;
      if (this.exp >= 0 & this.form == 0) {
         var1 = this;
      } else if (this.exp >= 0) {
         var1 = clone(this);
         var1.form = 0;
      } else if (-this.exp >= this.mant.length) {
         var1 = ZERO;
      } else {
         var1 = clone(this);
         int var4 = var1.mant.length + var1.exp;
         byte[] var5 = new byte[var4];
         System.arraycopy(var1.mant, 0, var5, 0, var4);
         var1.mant = var5;
         var1.form = 0;
         var1.exp = 0;
      }

      return new BigInteger(new String(var1.layout()));
   }

   public BigInteger toBigIntegerExact() {
      if (this.exp < 0) {
         byte[] var10000 = this.mant;
         if (this.mant.length + this.exp < 0) {
            throw new ArithmeticException("Decimal part non-zero: " + this.toString());
         }
      }

      return this.toBigInteger();
   }

   public char[] toCharArray() {
      return this.layout();
   }

   public String toString() {
      return new String(this.layout());
   }

   public BigInteger unscaledValue() {
      BigDecimal var1 = null;
      if (this.exp >= 0) {
         var1 = this;
      } else {
         var1 = clone(this);
         var1.exp = 0;
      }

      return var1.toBigInteger();
   }

   public static BigDecimal valueOf(double var0) {
      return new BigDecimal((new Double(var0)).toString());
   }

   public static BigDecimal valueOf(long var0) {
      return valueOf(var0, 0);
   }

   public static BigDecimal valueOf(long var0, int var2) {
      BigDecimal var3 = null;
      if (var0 == 0L) {
         var3 = ZERO;
      } else if (var0 == 1L) {
         var3 = ONE;
      } else if (var0 == 10L) {
         var3 = TEN;
      } else {
         var3 = new BigDecimal(var0);
      }

      if (var2 == 0) {
         return var3;
      } else if (var2 < 0) {
         throw new NumberFormatException("Negative scale: " + var2);
      } else {
         var3 = clone(var3);
         var3.exp = -var2;
         return var3;
      }
   }

   private char[] layout() {
      boolean var2 = false;
      StringBuilder var3 = null;
      boolean var4 = false;
      boolean var5 = false;
      boolean var6 = false;
      Object var7 = null;
      boolean var10 = false;
      char[] var1 = new char[this.mant.length];
      int var11 = this.mant.length;

      int var12;
      for(var12 = 0; var11 > 0; ++var12) {
         var1[var12] = (char)(this.mant[var12] + 48);
         --var11;
      }

      char[] var16;
      if (this.form != 0) {
         var3 = new StringBuilder(var1.length + 15);
         if (this.ind == -1) {
            var3.append('-');
         }

         int var13 = this.exp + var1.length - 1;
         if (this.form == 1) {
            var3.append(var1[0]);
            if (var1.length > 1) {
               var3.append('.').append(var1, 1, var1.length - 1);
            }
         } else {
            int var14 = var13 % 3;
            if (var14 < 0) {
               var14 += 3;
            }

            var13 -= var14;
            ++var14;
            if (var14 >= var1.length) {
               var3.append(var1, 0, var1.length);

               for(var11 = var14 - var1.length; var11 > 0; --var11) {
                  var3.append('0');
               }
            } else {
               var3.append(var1, 0, var14).append('.').append(var1, var14, var1.length - var14);
            }
         }

         if (var13 != 0) {
            char var15;
            if (var13 < 0) {
               var15 = '-';
               var13 = -var13;
            } else {
               var15 = '+';
            }

            var3.append('E').append(var15).append(var13);
         }

         var16 = new char[var3.length()];
         var11 = var3.length();
         if (0 != var11) {
            var3.getChars(0, var11, var16, 0);
         }

         return var16;
      } else if (this.exp == 0) {
         if (this.ind >= 0) {
            return var1;
         } else {
            var16 = new char[var1.length + 1];
            var16[0] = '-';
            System.arraycopy(var1, 0, var16, 1, var1.length);
            return var16;
         }
      } else {
         int var8 = this.ind == -1 ? 1 : 0;
         int var9 = this.exp + var1.length;
         int var17;
         if (var9 < 1) {
            var17 = var8 + 2 - this.exp;
            var16 = new char[var17];
            if (var8 != 0) {
               var16[0] = '-';
            }

            var16[var8] = '0';
            var16[var8 + 1] = '.';
            var11 = -var9;

            for(var12 = var8 + 2; var11 > 0; ++var12) {
               var16[var12] = '0';
               --var11;
            }

            System.arraycopy(var1, 0, var16, var8 + 2 - var9, var1.length);
            return var16;
         } else if (var9 <= var1.length) {
            var17 = var8 + 1 + var1.length;
            var16 = new char[var17];
            if (var8 != 0) {
               var16[0] = '-';
            }

            System.arraycopy(var1, 0, var16, var8, var9);
            var16[var8 + var9] = '.';
            System.arraycopy(var1, var9, var16, var8 + var9 + 1, var1.length - var9);
            return var16;
         } else {
            var17 = var8 + var9;
            var16 = new char[var17];
            if (var8 != 0) {
               var16[0] = '-';
            }

            System.arraycopy(var1, 0, var16, var8, var1.length);
            var11 = var9 - var1.length;

            for(var12 = var8 + var1.length; var11 > 0; ++var12) {
               var16[var12] = '0';
               --var11;
            }

            return var16;
         }
      }
   }

   private int intcheck(int var1, int var2) {
      int var3 = this.intValueExact();
      if (var3 < var1 | var3 > var2) {
         throw new ArithmeticException("Conversion overflow: " + var3);
      } else {
         return var3;
      }
   }

   private BigDecimal dodivide(char var1, BigDecimal var2, MathContext var3, int var4) {
      boolean var16 = false;
      boolean var17 = false;
      boolean var18 = false;
      boolean var19 = false;
      boolean var20 = false;
      boolean var21 = false;
      boolean var22 = false;
      boolean var23 = false;
      Object var24 = null;
      boolean var25 = false;
      boolean var26 = false;
      Object var27 = null;
      if (var3.lostDigits) {
         this.checkdigits(var2, var3.digits);
      }

      BigDecimal var5 = this;
      if (var2.ind == 0) {
         throw new ArithmeticException("Divide by 0");
      } else if (this.ind == 0) {
         if (var3.form != 0) {
            return ZERO;
         } else {
            return var4 == -1 ? this : this.setScale(var4);
         }
      } else {
         int var6 = var3.digits;
         if (var6 > 0) {
            if (this.mant.length > var6) {
               var5 = clone(this).round(var3);
            }

            if (var2.mant.length > var6) {
               var2 = clone(var2).round(var3);
            }
         } else {
            if (var4 == -1) {
               var4 = this.scale();
            }

            var6 = this.mant.length;
            if (var4 != -this.exp) {
               var6 = var6 + var4 + this.exp;
            }

            var6 = var6 - (var2.mant.length - 1) - var2.exp;
            if (var6 < this.mant.length) {
               var6 = this.mant.length;
            }

            if (var6 < var2.mant.length) {
               var6 = var2.mant.length;
            }
         }

         int var7 = var5.exp - var2.exp + var5.mant.length - var2.mant.length;
         if (var7 < 0 && var1 != 'D') {
            return var1 == 'I' ? ZERO : clone(var5).finish(var3, false);
         } else {
            BigDecimal var8 = new BigDecimal();
            var8.ind = (byte)(var5.ind * var2.ind);
            var8.exp = var7;
            var8.mant = new byte[var6 + 1];
            int var9 = var6 + var6 + 1;
            byte[] var10 = extend(var5.mant, var9);
            int var11 = var9;
            byte[] var12 = var2.mant;
            int var13 = var9;
            int var14 = var12[0] * 10 + 1;
            if (var12.length > 1) {
               var14 += var12[1];
            }

            int var15 = 0;

            int var30;
            label255:
            while(true) {
               int var29 = 0;

               while(true) {
                  int var28;
                  int var32;
                  label287: {
                     if (var11 >= var13) {
                        label272: {
                           if (var11 != var13) {
                              var32 = var10[0] * 10;
                              if (var11 > 1) {
                                 var32 += var10[1];
                              }
                              break label287;
                           }

                           var28 = var11;

                           for(var30 = 0; var28 > 0; ++var30) {
                              byte var31;
                              if (var30 < var12.length) {
                                 var31 = var12[var30];
                              } else {
                                 var31 = 0;
                              }

                              if (var10[var30] < var31) {
                                 break label272;
                              }

                              if (var10[var30] > var31) {
                                 var32 = var10[0];
                                 break label287;
                              }

                              --var28;
                           }

                           ++var29;
                           var8.mant[var15] = (byte)var29;
                           ++var15;
                           var10[0] = 0;
                           break label255;
                        }
                     }

                     if (var15 != 0 | var29 != 0) {
                        var8.mant[var15] = (byte)var29;
                        ++var15;
                        if (var15 == var6 + 1 || var10[0] == 0) {
                           break label255;
                        }
                     }

                     if (var4 >= 0 && -var8.exp > var4 || var1 != 'D' && var8.exp <= 0) {
                        break label255;
                     }

                     --var8.exp;
                     --var13;
                     break;
                  }

                  int var33 = var32 * 10 / var14;
                  if (var33 == 0) {
                     var33 = 1;
                  }

                  var29 += var33;
                  var10 = byteaddsub(var10, var11, var12, var13, -var33, true);
                  if (var10[0] == 0) {
                     var28 = var11 - 2;

                     int var34;
                     for(var34 = 0; var34 <= var28 && var10[var34] == 0; ++var34) {
                        --var11;
                     }

                     if (var34 != 0) {
                        System.arraycopy(var10, var34, var10, 0, var11);
                     }
                  }
               }
            }

            if (var15 == 0) {
               var15 = 1;
            }

            if (var1 == 'I' | var1 == 'R') {
               if (var15 + var8.exp > var6) {
                  throw new ArithmeticException("Integer overflow");
               }

               if (var1 == 'R') {
                  if (var8.mant[0] == 0) {
                     return clone(var5).finish(var3, false);
                  }

                  if (var10[0] == 0) {
                     return ZERO;
                  }

                  var8.ind = var5.ind;
                  int var35 = var6 + var6 + 1 - var5.mant.length;
                  var8.exp = var8.exp - var35 + var5.exp;
                  int var36 = var11;

                  for(var30 = var11 - 1; var30 >= 1 && var8.exp < var5.exp & var8.exp < var2.exp && var10[var30] == 0; --var30) {
                     --var36;
                     ++var8.exp;
                  }

                  if (var36 < var10.length) {
                     byte[] var37 = new byte[var36];
                     System.arraycopy(var10, 0, var37, 0, var36);
                     var10 = var37;
                  }

                  var8.mant = var10;
                  return var8.finish(var3, false);
               }
            } else if (var10[0] != 0) {
               byte var38 = var8.mant[var15 - 1];
               if (var38 % 5 == 0) {
                  var8.mant[var15 - 1] = (byte)(var38 + 1);
               }
            }

            if (var4 >= 0) {
               if (var15 != var8.mant.length) {
                  var8.exp -= var8.mant.length - var15;
               }

               int var39 = var8.mant.length - (-var8.exp - var4);
               var8.round(var39, var3.roundingMode);
               if (var8.exp != -var4) {
                  var8.mant = extend(var8.mant, var8.mant.length + 1);
                  --var8.exp;
               }

               return var8.finish(var3, true);
            } else {
               if (var15 == var8.mant.length) {
                  var8.round(var3);
               } else {
                  if (var8.mant[0] == 0) {
                     return ZERO;
                  }

                  byte[] var40 = new byte[var15];
                  System.arraycopy(var8.mant, 0, var40, 0, var15);
                  var8.mant = var40;
               }

               return var8.finish(var3, true);
            }
         }
      }
   }

   private void bad(char[] var1) {
      throw new NumberFormatException("Not a number: " + String.valueOf(var1));
   }

   private void badarg(String var1, int var2, String var3) {
      throw new IllegalArgumentException("Bad argument " + var2 + " " + "to" + " " + var1 + ":" + " " + var3);
   }

   private static final byte[] extend(byte[] var0, int var1) {
      if (var0.length == var1) {
         return var0;
      } else {
         byte[] var2 = new byte[var1];
         System.arraycopy(var0, 0, var2, 0, var0.length);
         return var2;
      }
   }

   private static final byte[] byteaddsub(byte[] var0, int var1, byte[] var2, int var3, int var4, boolean var5) {
      boolean var14 = false;
      boolean var15 = false;
      boolean var17 = false;
      int var6 = var0.length;
      int var7 = var2.length;
      int var8 = var1 - 1;
      int var9 = var3 - 1;
      int var10 = var9;
      if (var9 < var8) {
         var10 = var8;
      }

      byte[] var11 = (byte[])null;
      if (var5 && var10 + 1 == var6) {
         var11 = var0;
      }

      if (var11 == null) {
         var11 = new byte[var10 + 1];
      }

      boolean var12 = false;
      if (var4 == 1) {
         var12 = true;
      } else if (var4 == -1) {
         var12 = true;
      }

      int var13 = 0;

      for(int var19 = var10; var19 >= 0; --var19) {
         if (var8 >= 0) {
            if (var8 < var6) {
               var13 += var0[var8];
            }

            --var8;
         }

         if (var9 >= 0) {
            if (var9 < var7) {
               if (var12) {
                  if (var4 > 0) {
                     var13 += var2[var9];
                  } else {
                     var13 -= var2[var9];
                  }
               } else {
                  var13 += var2[var9] * var4;
               }
            }

            --var9;
         }

         if (var13 < 10 && var13 >= 0) {
            var11[var19] = (byte)var13;
            var13 = 0;
         } else {
            int var20 = var13 + 90;
            var11[var19] = bytedig[var20];
            var13 = bytecar[var20];
         }
      }

      if (var13 == 0) {
         return var11;
      } else {
         byte[] var16 = (byte[])null;
         if (var5 && var10 + 2 == var0.length) {
            var16 = var0;
         }

         if (var16 == null) {
            var16 = new byte[var10 + 2];
         }

         var16[0] = (byte)var13;
         if (var10 < 10) {
            int var18 = var10 + 1;

            for(int var21 = 0; var18 > 0; ++var21) {
               var16[var21 + 1] = var11[var21];
               --var18;
            }
         } else {
            System.arraycopy(var11, 0, var16, 1, var10 + 1);
         }

         return var16;
      }
   }

   private static final byte[] diginit() {
      boolean var1 = false;
      boolean var2 = false;
      byte[] var0 = new byte[190];

      for(int var3 = 0; var3 <= 189; ++var3) {
         int var4 = var3 - 90;
         if (var4 >= 0) {
            var0[var3] = (byte)(var4 % 10);
            bytecar[var3] = (byte)(var4 / 10);
         } else {
            var4 += 100;
            var0[var3] = (byte)(var4 % 10);
            bytecar[var3] = (byte)(var4 / 10 - 10);
         }
      }

      return var0;
   }

   private static final BigDecimal clone(BigDecimal var0) {
      BigDecimal var1 = new BigDecimal();
      var1.ind = var0.ind;
      var1.exp = var0.exp;
      var1.form = var0.form;
      var1.mant = var0.mant;
      return var1;
   }

   private void checkdigits(BigDecimal var1, int var2) {
      if (var2 != 0) {
         byte[] var10000;
         if (this.mant.length > var2) {
            var10000 = this.mant;
            if (var2 < 0) {
               throw new ArithmeticException("Too many digits: " + this.toString());
            }
         }

         if (var1 != null) {
            if (var1.mant.length > var2) {
               var10000 = var1.mant;
               if (var2 < 0) {
                  throw new ArithmeticException("Too many digits: " + var1.toString());
               }
            }

         }
      }
   }

   private BigDecimal round(MathContext var1) {
      return this.round(var1.digits, var1.roundingMode);
   }

   private BigDecimal round(int var1, int var2) {
      boolean var6 = false;
      boolean var7 = false;
      Object var9 = null;
      int var3 = this.mant.length - var1;
      if (var3 <= 0) {
         return this;
      } else {
         this.exp += var3;
         byte var4 = this.ind;
         byte[] var5 = this.mant;
         byte var10;
         if (var1 > 0) {
            this.mant = new byte[var1];
            System.arraycopy(var5, 0, this.mant, 0, var1);
            var6 = true;
            var10 = var5[var1];
         } else {
            this.mant = ZERO.mant;
            this.ind = 0;
            var6 = false;
            if (var1 == 0) {
               var10 = var5[0];
            } else {
               var10 = 0;
            }
         }

         int var8 = 0;
         if (var2 == 4) {
            if (var10 >= 5) {
               var8 = var4;
            }
         } else if (var2 == 7) {
            if (var1 < 0) {
               throw new ArithmeticException("Rounding necessary");
            }
         } else if (var2 == 5) {
            if (var10 > 5) {
               var8 = var4;
            } else if (var10 == 5 && var1 + 1 < 0) {
               var8 = var4;
            }
         } else if (var2 == 6) {
            if (var10 > 5) {
               var8 = var4;
            } else if (var10 == 5) {
               if (var1 + 1 < 0) {
                  var8 = var4;
               } else if (this.mant[this.mant.length - 1] % 2 != 0) {
                  var8 = var4;
               }
            }
         } else if (var2 != 1) {
            if (var2 == 0) {
               if (var1 < 0) {
                  var8 = var4;
               }
            } else if (var2 == 2) {
               if (var4 > 0 && var1 < 0) {
                  var8 = var4;
               }
            } else {
               if (var2 != 3) {
                  throw new IllegalArgumentException("Bad round value: " + var2);
               }

               if (var4 < 0 && var1 < 0) {
                  var8 = var4;
               }
            }
         }

         if (var8 != 0) {
            if (this.ind == 0) {
               this.mant = ONE.mant;
               this.ind = (byte)var8;
            } else {
               if (this.ind == -1) {
                  var8 = -var8;
               }

               byte[] var11 = byteaddsub(this.mant, this.mant.length, ONE.mant, 1, var8, var6);
               if (var11.length > this.mant.length) {
                  ++this.exp;
                  System.arraycopy(var11, 0, this.mant, 0, this.mant.length);
               } else {
                  this.mant = var11;
               }
            }
         }

         if (this.exp > 999999999) {
            throw new ArithmeticException("Exponent Overflow: " + this.exp);
         } else {
            return this;
         }
      }
   }

   private BigDecimal finish(MathContext var1, boolean var2) {
      boolean var3 = false;
      boolean var4 = false;
      Object var5 = null;
      boolean var6 = false;
      boolean var7 = false;
      if (var1.digits != 0 && this.mant.length > var1.digits) {
         this.round(var1);
      }

      int var10;
      byte[] var11;
      if (var2 && var1.form != 0) {
         int var9 = this.mant.length;

         for(var10 = var9 - 1; var10 >= 1 && this.mant[var10] == 0; --var10) {
            --var9;
            ++this.exp;
         }

         if (var9 < this.mant.length) {
            var11 = new byte[var9];
            System.arraycopy(this.mant, 0, var11, 0, var9);
            this.mant = var11;
         }
      }

      this.form = 0;
      int var8 = this.mant.length;

      for(var10 = 0; var8 > 0; ++var10) {
         if (this.mant[var10] != 0) {
            if (var10 > 0) {
               var11 = new byte[this.mant.length - var10];
               System.arraycopy(this.mant, var10, var11, 0, this.mant.length - var10);
               this.mant = var11;
            }

            int var12 = this.exp + this.mant.length;
            if (var12 > 0) {
               if (var12 > var1.digits && var1.digits != 0) {
                  this.form = (byte)var1.form;
               }

               if (var12 - 1 <= 999999999) {
                  return this;
               }
            } else if (var12 < -5) {
               this.form = (byte)var1.form;
            }

            --var12;
            if (var12 < -999999999 | var12 > 999999999) {
               if (this.form != 2) {
                  throw new ArithmeticException("Exponent Overflow: " + var12);
               }

               int var13 = var12 % 3;
               if (var13 < 0) {
                  var13 += 3;
               }

               var12 -= var13;
               if (var12 < -999999999 || var12 > 999999999) {
                  throw new ArithmeticException("Exponent Overflow: " + var12);
               }
            }

            return this;
         }

         --var8;
      }

      this.ind = 0;
      if (var1.form != 0) {
         this.exp = 0;
      } else if (this.exp > 0) {
         this.exp = 0;
      } else if (this.exp < -999999999) {
         throw new ArithmeticException("Exponent Overflow: " + this.exp);
      }

      this.mant = ZERO.mant;
      return this;
   }

   public int compareTo(Object var1) {
      return this.compareTo((BigDecimal)var1);
   }
}
