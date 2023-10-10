package com.ibm.icu.text;

import java.math.BigDecimal;
import java.math.BigInteger;

final class DigitList {
   public static final int MAX_LONG_DIGITS = 19;
   public static final int DBL_DIG = 17;
   public int decimalAt = 0;
   public int count = 0;
   public byte[] digits = new byte[19];
   private static byte[] LONG_MIN_REP;

   private final void ensureCapacity(int var1, int var2) {
      if (var1 > this.digits.length) {
         byte[] var3 = new byte[var1 * 2];
         System.arraycopy(this.digits, 0, var3, 0, var2);
         this.digits = var3;
      }

   }

   public void append(int var1) {
      this.ensureCapacity(this.count + 1, this.count);
      this.digits[this.count++] = (byte)var1;
   }

   public byte getDigitValue(int var1) {
      return (byte)(this.digits[var1] - 48);
   }

   public final double getDouble() {
      if (this.count == 0) {
         return 0.0D;
      } else {
         StringBuilder var1 = new StringBuilder(this.count);
         var1.append('.');

         for(int var2 = 0; var2 < this.count; ++var2) {
            var1.append((char)this.digits[var2]);
         }

         var1.append('E');
         var1.append(Integer.toString(this.decimalAt));
         return Double.valueOf(var1.toString());
      }
   }

   public final long getLong() {
      // $FF: Couldn't be decompiled
   }

   public BigInteger getBigInteger(boolean param1) {
      // $FF: Couldn't be decompiled
   }

   private String getStringRep(boolean param1) {
      // $FF: Couldn't be decompiled
   }

   public BigDecimal getBigDecimal(boolean param1) {
      // $FF: Couldn't be decompiled
   }

   public com.ibm.icu.math.BigDecimal getBigDecimalICU(boolean param1) {
      // $FF: Couldn't be decompiled
   }

   boolean isIntegral() {
      while(this.count > 0 && this.digits[this.count - 1] == 48) {
         --this.count;
      }

      return this.count == 0 || this.decimalAt >= this.count;
   }

   final void set(double var1, int var3, boolean var4) {
      if (var1 == 0.0D) {
         var1 = 0.0D;
      }

      String var5 = Double.toString(var1);
      this.set((String)var5, 19);
      if (var4) {
         if (-this.decimalAt > var3) {
            this.count = 0;
            return;
         }

         if (-this.decimalAt == var3) {
            if (this < 0) {
               this.count = 1;
               ++this.decimalAt;
               this.digits[0] = 49;
            } else {
               this.count = 0;
            }

            return;
         }
      }

      while(this.count > 1 && this.digits[this.count - 1] == 48) {
         --this.count;
      }

      this.round(var4 ? var3 + this.decimalAt : (var3 == 0 ? -1 : var3));
   }

   private void set(String var1, int var2) {
      this.decimalAt = -1;
      this.count = 0;
      int var3 = 0;
      int var4 = 0;
      boolean var5 = false;
      int var6 = 0;
      if (var1.charAt(var6) == '-') {
         ++var6;
      }

      for(; var6 < var1.length(); ++var6) {
         char var7 = var1.charAt(var6);
         if (var7 == '.') {
            this.decimalAt = this.count;
         } else {
            if (var7 == 'e' || var7 == 'E') {
               ++var6;
               if (var1.charAt(var6) == '+') {
                  ++var6;
               }

               var3 = Integer.valueOf(var1.substring(var6));
               break;
            }

            if (this.count < var2) {
               if (!var5) {
                  var5 = var7 != '0';
                  if (!var5 && this.decimalAt != -1) {
                     ++var4;
                  }
               }

               if (var5) {
                  this.ensureCapacity(this.count + 1, this.count);
                  this.digits[this.count++] = (byte)var7;
               }
            }
         }
      }

      if (this.decimalAt == -1) {
         this.decimalAt = this.count;
      }

      this.decimalAt += var3 - var4;
   }

   public final void round(int var1) {
      if (var1 >= 0 && var1 < this.count) {
         if (this < var1) {
            do {
               --var1;
               if (var1 < 0) {
                  this.digits[0] = 49;
                  ++this.decimalAt;
                  var1 = 0;
                  break;
               }

               ++this.digits[var1];
            } while(this.digits[var1] > 57);

            ++var1;
         }

         this.count = var1;
      }

      while(this.count > 1 && this.digits[this.count - 1] == 48) {
         --this.count;
      }

   }

   public final void set(long var1) {
      this.set(var1, 0);
   }

   public final void set(long var1, int var3) {
      if (var1 <= 0L) {
         if (var1 == Long.MIN_VALUE) {
            this.decimalAt = this.count = 19;
            System.arraycopy(LONG_MIN_REP, 0, this.digits, 0, this.count);
         } else {
            this.count = 0;
            this.decimalAt = 0;
         }
      } else {
         int var4;
         for(var4 = 19; var1 > 0L; var1 /= 10L) {
            --var4;
            this.digits[var4] = (byte)((int)(48L + var1 % 10L));
         }

         this.decimalAt = 19 - var4;

         int var5;
         for(var5 = 18; this.digits[var5] == 48; --var5) {
         }

         this.count = var5 - var4 + 1;
         System.arraycopy(this.digits, var4, this.digits, 0, this.count);
      }

      if (var3 > 0) {
         this.round(var3);
      }

   }

   public final void set(BigInteger var1, int var2) {
      String var3 = var1.toString();

      for(this.count = this.decimalAt = var3.length(); this.count > 1 && var3.charAt(this.count - 1) == '0'; --this.count) {
      }

      int var4 = 0;
      if (var3.charAt(0) == '-') {
         ++var4;
         --this.count;
         --this.decimalAt;
      }

      this.ensureCapacity(this.count, 0);

      for(int var5 = 0; var5 < this.count; ++var5) {
         this.digits[var5] = (byte)var3.charAt(var5 + var4);
      }

      if (var2 > 0) {
         this.round(var2);
      }

   }

   private void setBigDecimalDigits(String var1, int var2, boolean var3) {
      this.set(var1, var1.length());
      this.round(var3 ? var2 + this.decimalAt : (var2 == 0 ? -1 : var2));
   }

   public final void set(BigDecimal var1, int var2, boolean var3) {
      this.setBigDecimalDigits(var1.toString(), var2, var3);
   }

   public final void set(com.ibm.icu.math.BigDecimal var1, int var2, boolean var3) {
      this.setBigDecimalDigits(var1.toString(), var2, var3);
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof DigitList)) {
         return false;
      } else {
         DigitList var2 = (DigitList)var1;
         if (this.count == var2.count && this.decimalAt == var2.decimalAt) {
            for(int var3 = 0; var3 < this.count; ++var3) {
               if (this.digits[var3] != var2.digits[var3]) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public int hashCode() {
      int var1 = this.decimalAt;

      for(int var2 = 0; var2 < this.count; ++var2) {
         var1 = var1 * 37 + this.digits[var2];
      }

      return var1;
   }

   public String toString() {
      // $FF: Couldn't be decompiled
   }

   static {
      String var0 = Long.toString(Long.MIN_VALUE);
      LONG_MIN_REP = new byte[19];

      for(int var1 = 0; var1 < 19; ++var1) {
         LONG_MIN_REP[var1] = (byte)var0.charAt(var1 + 1);
      }

   }
}
