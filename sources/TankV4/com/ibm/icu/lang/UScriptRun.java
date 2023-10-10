package com.ibm.icu.lang;

import com.ibm.icu.text.UTF16;

/** @deprecated */
public final class UScriptRun {
   private char[] emptyCharArray = new char[0];
   private char[] text;
   private int textIndex;
   private int textStart;
   private int textLimit;
   private int scriptStart;
   private int scriptLimit;
   private int scriptCode;
   private static int PAREN_STACK_DEPTH = 32;
   private static UScriptRun.ParenStackEntry[] parenStack;
   private int parenSP = -1;
   private int pushCount = 0;
   private int fixupCount = 0;
   private static int[] pairedChars;
   private static int pairedCharPower;
   private static int pairedCharExtra;

   /** @deprecated */
   public UScriptRun() {
      Object var1 = null;
      this.reset((char[])var1, 0, 0);
   }

   /** @deprecated */
   public UScriptRun(String var1) {
      this.reset(var1);
   }

   /** @deprecated */
   public UScriptRun(String var1, int var2, int var3) {
      this.reset(var1, var2, var3);
   }

   /** @deprecated */
   public UScriptRun(char[] var1) {
      this.reset(var1);
   }

   /** @deprecated */
   public UScriptRun(char[] var1, int var2, int var3) {
      this.reset(var1, var2, var3);
   }

   /** @deprecated */
   public final void reset() {
      while(this == false) {
         this.pop();
      }

      this.scriptStart = this.textStart;
      this.scriptLimit = this.textStart;
      this.scriptCode = -1;
      this.parenSP = -1;
      this.pushCount = 0;
      this.fixupCount = 0;
      this.textIndex = this.textStart;
   }

   /** @deprecated */
   public final void reset(int var1, int var2) throws IllegalArgumentException {
      int var3 = 0;
      if (this.text != null) {
         var3 = this.text.length;
      }

      if (var1 >= 0 && var2 >= 0 && var1 <= var3 - var2) {
         this.textStart = var1;
         this.textLimit = var1 + var2;
         this.reset();
      } else {
         throw new IllegalArgumentException();
      }
   }

   /** @deprecated */
   public final void reset(char[] var1, int var2, int var3) {
      if (var1 == null) {
         var1 = this.emptyCharArray;
      }

      this.text = var1;
      this.reset(var2, var3);
   }

   /** @deprecated */
   public final void reset(char[] var1) {
      int var2 = 0;
      if (var1 != null) {
         var2 = var1.length;
      }

      this.reset((char[])var1, 0, var2);
   }

   /** @deprecated */
   public final void reset(String var1, int var2, int var3) {
      char[] var4 = null;
      if (var1 != null) {
         var4 = var1.toCharArray();
      }

      this.reset(var4, var2, var3);
   }

   /** @deprecated */
   public final void reset(String var1) {
      int var2 = 0;
      if (var1 != null) {
         var2 = var1.length();
      }

      this.reset((String)var1, 0, var2);
   }

   /** @deprecated */
   public final int getScriptStart() {
      return this.scriptStart;
   }

   /** @deprecated */
   public final int getScriptLimit() {
      return this.scriptLimit;
   }

   /** @deprecated */
   public final int getScriptCode() {
      return this.scriptCode;
   }

   /** @deprecated */
   public final boolean next() {
      if (this.scriptLimit >= this.textLimit) {
         return false;
      } else {
         this.scriptCode = 0;
         this.scriptStart = this.scriptLimit;
         this.syncFixup();

         while(this.textIndex < this.textLimit) {
            int var1 = UTF16.charAt(this.text, this.textStart, this.textLimit, this.textIndex - this.textStart);
            int var2 = UTF16.getCharCount(var1);
            int var3 = UScript.getScript(var1);
            int var4 = getPairIndex(var1);
            this.textIndex += var2;
            if (var4 >= 0) {
               if ((var4 & 1) == 0) {
                  this.push(var4, this.scriptCode);
               } else {
                  int var5 = var4 & -2;

                  while(this == false && this.top().pairIndex != var5) {
                     this.pop();
                  }

                  if (this == false) {
                     var3 = this.top().scriptCode;
                  }
               }
            }

            if (this.scriptCode <= var3) {
               this.textIndex -= var2;
               break;
            }

            if (this.scriptCode <= 1 && var3 > 1) {
               this.scriptCode = var3;
               this.fixup(this.scriptCode);
            }

            if (var4 >= 0 && (var4 & 1) != 0) {
               this.pop();
            }
         }

         this.scriptLimit = this.textIndex;
         return true;
      }
   }

   private static final int mod(int var0) {
      return var0 % PAREN_STACK_DEPTH;
   }

   private static final int inc(int var0, int var1) {
      return mod(var0 + var1);
   }

   private static final int inc(int var0) {
      return inc(var0, 1);
   }

   private static final int dec(int var0, int var1) {
      return mod(var0 + PAREN_STACK_DEPTH - var1);
   }

   private static final int dec(int var0) {
      return dec(var0, 1);
   }

   private static final int limitInc(int var0) {
      if (var0 < PAREN_STACK_DEPTH) {
         ++var0;
      }

      return var0;
   }

   private final void push(int var1, int var2) {
      this.pushCount = limitInc(this.pushCount);
      this.fixupCount = limitInc(this.fixupCount);
      this.parenSP = inc(this.parenSP);
      parenStack[this.parenSP] = new UScriptRun.ParenStackEntry(var1, var2);
   }

   private final void pop() {
      if (!(this <= 0)) {
         parenStack[this.parenSP] = null;
         if (this.fixupCount > 0) {
            --this.fixupCount;
         }

         --this.pushCount;
         this.parenSP = dec(this.parenSP);
         if (this <= 0) {
            this.parenSP = -1;
         }

      }
   }

   private final UScriptRun.ParenStackEntry top() {
      return parenStack[this.parenSP];
   }

   private final void syncFixup() {
      this.fixupCount = 0;
   }

   private final void fixup(int var1) {
      for(int var2 = dec(this.parenSP, this.fixupCount); this.fixupCount-- > 0; parenStack[var2].scriptCode = var1) {
         var2 = inc(var2);
      }

   }

   private static final byte highBit(int var0) {
      if (var0 <= 0) {
         return -32;
      } else {
         byte var1 = 0;
         if (var0 >= 65536) {
            var0 >>= 16;
            var1 = (byte)(var1 + 16);
         }

         if (var0 >= 256) {
            var0 >>= 8;
            var1 = (byte)(var1 + 8);
         }

         if (var0 >= 16) {
            var0 >>= 4;
            var1 = (byte)(var1 + 4);
         }

         if (var0 >= 4) {
            var0 >>= 2;
            var1 = (byte)(var1 + 2);
         }

         if (var0 >= 2) {
            var0 >>= 1;
            ++var1;
         }

         return var1;
      }
   }

   private static int getPairIndex(int var0) {
      int var1 = pairedCharPower;
      int var2 = 0;
      if (var0 >= pairedChars[pairedCharExtra]) {
         var2 = pairedCharExtra;
      }

      while(var1 > 1) {
         var1 >>= 1;
         if (var0 >= pairedChars[var2 + var1]) {
            var2 += var1;
         }
      }

      if (pairedChars[var2] != var0) {
         var2 = -1;
      }

      return var2;
   }

   static {
      parenStack = new UScriptRun.ParenStackEntry[PAREN_STACK_DEPTH];
      pairedChars = new int[]{40, 41, 60, 62, 91, 93, 123, 125, 171, 187, 8216, 8217, 8220, 8221, 8249, 8250, 12296, 12297, 12298, 12299, 12300, 12301, 12302, 12303, 12304, 12305, 12308, 12309, 12310, 12311, 12312, 12313, 12314, 12315};
      pairedCharPower = 1 << highBit(pairedChars.length);
      pairedCharExtra = pairedChars.length - pairedCharPower;
   }

   private static final class ParenStackEntry {
      int pairIndex;
      int scriptCode;

      public ParenStackEntry(int var1, int var2) {
         this.pairIndex = var1;
         this.scriptCode = var2;
      }
   }
}
