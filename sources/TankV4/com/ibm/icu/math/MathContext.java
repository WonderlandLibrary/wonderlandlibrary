package com.ibm.icu.math;

import java.io.Serializable;

public final class MathContext implements Serializable {
   public static final int PLAIN = 0;
   public static final int SCIENTIFIC = 1;
   public static final int ENGINEERING = 2;
   public static final int ROUND_CEILING = 2;
   public static final int ROUND_DOWN = 1;
   public static final int ROUND_FLOOR = 3;
   public static final int ROUND_HALF_DOWN = 5;
   public static final int ROUND_HALF_EVEN = 6;
   public static final int ROUND_HALF_UP = 4;
   public static final int ROUND_UNNECESSARY = 7;
   public static final int ROUND_UP = 0;
   int digits;
   int form;
   boolean lostDigits;
   int roundingMode;
   private static final int DEFAULT_FORM = 1;
   private static final int DEFAULT_DIGITS = 9;
   private static final boolean DEFAULT_LOSTDIGITS = false;
   private static final int DEFAULT_ROUNDINGMODE = 4;
   private static final int MIN_DIGITS = 0;
   private static final int MAX_DIGITS = 999999999;
   private static final int[] ROUNDS = new int[]{4, 7, 2, 1, 3, 5, 6, 0};
   private static final String[] ROUNDWORDS = new String[]{"ROUND_HALF_UP", "ROUND_UNNECESSARY", "ROUND_CEILING", "ROUND_DOWN", "ROUND_FLOOR", "ROUND_HALF_DOWN", "ROUND_HALF_EVEN", "ROUND_UP"};
   private static final long serialVersionUID = 7163376998892515376L;
   public static final MathContext DEFAULT = new MathContext(9, 1, false, 4);

   public MathContext(int var1) {
      this(var1, 1, false, 4);
   }

   public MathContext(int var1, int var2) {
      this(var1, var2, false, 4);
   }

   public MathContext(int var1, int var2, boolean var3) {
      this(var1, var2, var3, 4);
   }

   public MathContext(int var1, int var2, boolean var3, int var4) {
      if (var1 != 9) {
         if (var1 < 0) {
            throw new IllegalArgumentException("Digits too small: " + var1);
         }

         if (var1 > 999999999) {
            throw new IllegalArgumentException("Digits too large: " + var1);
         }
      }

      if (var2 != 1 && var2 != 2 && var2 != 0) {
         throw new IllegalArgumentException("Bad form value: " + var2);
      } else if (var4 > 0) {
         throw new IllegalArgumentException("Bad roundingMode value: " + var4);
      } else {
         this.digits = var1;
         this.form = var2;
         this.lostDigits = var3;
         this.roundingMode = var4;
      }
   }

   public int getDigits() {
      return this.digits;
   }

   public int getForm() {
      return this.form;
   }

   public boolean getLostDigits() {
      return this.lostDigits;
   }

   public int getRoundingMode() {
      return this.roundingMode;
   }

   public String toString() {
      String var1 = null;
      boolean var2 = false;
      String var3 = null;
      if (this.form == 1) {
         var1 = "SCIENTIFIC";
      } else if (this.form == 2) {
         var1 = "ENGINEERING";
      } else {
         var1 = "PLAIN";
      }

      int var4 = ROUNDS.length;

      for(int var5 = 0; var4 > 0; ++var5) {
         if (this.roundingMode == ROUNDS[var5]) {
            var3 = ROUNDWORDS[var5];
            break;
         }

         --var4;
      }

      return "digits=" + this.digits + " " + "form=" + var1 + " " + "lostDigits=" + (this.lostDigits ? "1" : "0") + " " + "roundingMode=" + var3;
   }
}
