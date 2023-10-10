package com.ibm.icu.impl;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.MissingResourceException;

public final class DateNumberFormat extends NumberFormat {
   private static final long serialVersionUID = -6315692826916346953L;
   private char[] digits;
   private char zeroDigit;
   private char minusSign;
   private boolean positiveOnly = false;
   private transient char[] decimalBuf = new char[20];
   private static SimpleCache CACHE = new SimpleCache();
   private int maxIntDigits;
   private int minIntDigits;
   private static final long PARSE_THRESHOLD = 922337203685477579L;

   public DateNumberFormat(ULocale var1, String var2, String var3) {
      this.initialize(var1, var2, var3);
   }

   public DateNumberFormat(ULocale var1, char var2, String var3) {
      StringBuffer var4 = new StringBuffer();

      for(int var5 = 0; var5 < 10; ++var5) {
         var4.append((char)(var2 + var5));
      }

      this.initialize(var1, var4.toString(), var3);
   }

   private void initialize(ULocale var1, String var2, String var3) {
      char[] var4 = (char[])CACHE.get(var1);
      if (var4 == null) {
         ICUResourceBundle var6 = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", var1);

         String var5;
         try {
            var5 = var6.getStringWithFallback("NumberElements/" + var3 + "/symbols/minusSign");
         } catch (MissingResourceException var10) {
            if (!var3.equals("latn")) {
               try {
                  var5 = var6.getStringWithFallback("NumberElements/latn/symbols/minusSign");
               } catch (MissingResourceException var9) {
                  var5 = "-";
               }
            } else {
               var5 = "-";
            }
         }

         var4 = new char[11];

         for(int var7 = 0; var7 < 10; ++var7) {
            var4[var7] = var2.charAt(var7);
         }

         var4[10] = var5.charAt(0);
         CACHE.put(var1, var4);
      }

      this.digits = new char[10];
      System.arraycopy(var4, 0, this.digits, 0, 10);
      this.zeroDigit = this.digits[0];
      this.minusSign = var4[10];
   }

   public void setMaximumIntegerDigits(int var1) {
      this.maxIntDigits = var1;
   }

   public int getMaximumIntegerDigits() {
      return this.maxIntDigits;
   }

   public void setMinimumIntegerDigits(int var1) {
      this.minIntDigits = var1;
   }

   public int getMinimumIntegerDigits() {
      return this.minIntDigits;
   }

   public void setParsePositiveOnly(boolean var1) {
      this.positiveOnly = var1;
   }

   public char getZeroDigit() {
      return this.zeroDigit;
   }

   public void setZeroDigit(char var1) {
      this.zeroDigit = var1;
      if (this.digits == null) {
         this.digits = new char[10];
      }

      this.digits[0] = var1;

      for(int var2 = 1; var2 < 10; ++var2) {
         this.digits[var2] = (char)(var1 + var2);
      }

   }

   public char[] getDigits() {
      return this.digits;
   }

   public StringBuffer format(double var1, StringBuffer var3, FieldPosition var4) {
      throw new UnsupportedOperationException("StringBuffer format(double, StringBuffer, FieldPostion) is not implemented");
   }

   public StringBuffer format(long var1, StringBuffer var3, FieldPosition var4) {
      if (var1 < 0L) {
         var3.append(this.minusSign);
         var1 = -var1;
      }

      int var5 = (int)var1;
      int var6 = this.decimalBuf.length < this.maxIntDigits ? this.decimalBuf.length : this.maxIntDigits;
      int var7 = var6 - 1;

      while(true) {
         this.decimalBuf[var7] = this.digits[var5 % 10];
         var5 /= 10;
         if (var7 == 0 || var5 == 0) {
            for(int var8 = this.minIntDigits - (var6 - var7); var8 > 0; --var8) {
               --var7;
               this.decimalBuf[var7] = this.digits[0];
            }

            int var9 = var6 - var7;
            var3.append(this.decimalBuf, var7, var9);
            var4.setBeginIndex(0);
            if (var4.getField() == 0) {
               var4.setEndIndex(var9);
            } else {
               var4.setEndIndex(0);
            }

            return var3;
         }

         --var7;
      }
   }

   public StringBuffer format(BigInteger var1, StringBuffer var2, FieldPosition var3) {
      throw new UnsupportedOperationException("StringBuffer format(BigInteger, StringBuffer, FieldPostion) is not implemented");
   }

   public StringBuffer format(BigDecimal var1, StringBuffer var2, FieldPosition var3) {
      throw new UnsupportedOperationException("StringBuffer format(BigDecimal, StringBuffer, FieldPostion) is not implemented");
   }

   public StringBuffer format(com.ibm.icu.math.BigDecimal var1, StringBuffer var2, FieldPosition var3) {
      throw new UnsupportedOperationException("StringBuffer format(BigDecimal, StringBuffer, FieldPostion) is not implemented");
   }

   public Number parse(String var1, ParsePosition var2) {
      long var3 = 0L;
      boolean var5 = false;
      boolean var6 = false;
      int var7 = var2.getIndex();

      int var8;
      for(var8 = 0; var7 + var8 < var1.length(); ++var8) {
         char var9 = var1.charAt(var7 + var8);
         if (var8 == 0 && var9 == this.minusSign) {
            if (this.positiveOnly) {
               break;
            }

            var6 = true;
         } else {
            int var10 = var9 - this.digits[0];
            if (var10 < 0 || 9 < var10) {
               var10 = UCharacter.digit(var9);
            }

            if (var10 < 0 || 9 < var10) {
               for(var10 = 0; var10 < 10 && var9 != this.digits[var10]; ++var10) {
               }
            }

            if (0 > var10 || var10 > 9 || var3 >= 922337203685477579L) {
               break;
            }

            var5 = true;
            var3 = var3 * 10L + (long)var10;
         }
      }

      Long var11 = null;
      if (var5) {
         var3 = var6 ? var3 * -1L : var3;
         var11 = var3;
         var2.setIndex(var7 + var8);
      }

      return var11;
   }

   public boolean equals(Object var1) {
      if (var1 != null && super.equals(var1) && var1 instanceof DateNumberFormat) {
         DateNumberFormat var2 = (DateNumberFormat)var1;
         return this.maxIntDigits == var2.maxIntDigits && this.minIntDigits == var2.minIntDigits && this.minusSign == var2.minusSign && this.positiveOnly == var2.positiveOnly && Arrays.equals(this.digits, var2.digits);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return super.hashCode();
   }

   private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
      var1.defaultReadObject();
      if (this.digits == null) {
         this.setZeroDigit(this.zeroDigit);
      }

      this.decimalBuf = new char[20];
   }
}
