package com.ibm.icu.util;

import java.nio.CharBuffer;

public final class CharsTrieBuilder extends StringTrieBuilder {
   private final char[] intUnits = new char[3];
   private char[] chars;
   private int charsLength;
   static final boolean $assertionsDisabled = !CharsTrieBuilder.class.desiredAssertionStatus();

   public CharsTrieBuilder add(CharSequence var1, int var2) {
      this.addImpl(var1, var2);
      return this;
   }

   public CharsTrie build(StringTrieBuilder.Option var1) {
      return new CharsTrie(this.buildCharSequence(var1), 0);
   }

   public CharSequence buildCharSequence(StringTrieBuilder.Option var1) {
      this.buildChars(var1);
      return CharBuffer.wrap(this.chars, this.chars.length - this.charsLength, this.charsLength);
   }

   private void buildChars(StringTrieBuilder.Option var1) {
      if (this.chars == null) {
         this.chars = new char[1024];
      }

      this.buildImpl(var1);
   }

   public CharsTrieBuilder clear() {
      this.clearImpl();
      this.chars = null;
      this.charsLength = 0;
      return this;
   }

   /** @deprecated */
   protected boolean matchNodesCanHaveValues() {
      return true;
   }

   /** @deprecated */
   protected int getMaxBranchLinearSubNodeLength() {
      return 5;
   }

   /** @deprecated */
   protected int getMinLinearMatch() {
      return 48;
   }

   /** @deprecated */
   protected int getMaxLinearMatchLength() {
      return 16;
   }

   private void ensureCapacity(int var1) {
      if (var1 > this.chars.length) {
         int var2 = this.chars.length;

         do {
            var2 *= 2;
         } while(var2 <= var1);

         char[] var3 = new char[var2];
         System.arraycopy(this.chars, this.chars.length - this.charsLength, var3, var3.length - this.charsLength, this.charsLength);
         this.chars = var3;
      }

   }

   /** @deprecated */
   protected int write(int var1) {
      int var2 = this.charsLength + 1;
      this.ensureCapacity(var2);
      this.charsLength = var2;
      this.chars[this.chars.length - this.charsLength] = (char)var1;
      return this.charsLength;
   }

   /** @deprecated */
   protected int write(int var1, int var2) {
      int var3 = this.charsLength + var2;
      this.ensureCapacity(var3);
      this.charsLength = var3;

      for(int var4 = this.chars.length - this.charsLength; var2 > 0; --var2) {
         this.chars[var4++] = this.strings.charAt(var1++);
      }

      return this.charsLength;
   }

   private int write(char[] var1, int var2) {
      int var3 = this.charsLength + var2;
      this.ensureCapacity(var3);
      this.charsLength = var3;
      System.arraycopy(var1, 0, this.chars, this.chars.length - this.charsLength, var2);
      return this.charsLength;
   }

   /** @deprecated */
   protected int writeValueAndFinal(int var1, boolean var2) {
      if (0 <= var1 && var1 <= 16383) {
         return this.write(var1 | (var2 ? '耀' : 0));
      } else {
         byte var3;
         if (var1 >= 0 && var1 <= 1073676287) {
            this.intUnits[0] = (char)(16384 + (var1 >> 16));
            this.intUnits[1] = (char)var1;
            var3 = 2;
         } else {
            this.intUnits[0] = 32767;
            this.intUnits[1] = (char)(var1 >> 16);
            this.intUnits[2] = (char)var1;
            var3 = 3;
         }

         this.intUnits[0] = (char)(this.intUnits[0] | (var2 ? '耀' : 0));
         return this.write(this.intUnits, var3);
      }
   }

   /** @deprecated */
   protected int writeValueAndType(boolean var1, int var2, int var3) {
      if (!var1) {
         return this.write(var3);
      } else {
         byte var4;
         if (var2 >= 0 && var2 <= 16646143) {
            if (var2 <= 255) {
               this.intUnits[0] = (char)(var2 + 1 << 6);
               var4 = 1;
            } else {
               this.intUnits[0] = (char)(16448 + (var2 >> 10 & 32704));
               this.intUnits[1] = (char)var2;
               var4 = 2;
            }
         } else {
            this.intUnits[0] = 32704;
            this.intUnits[1] = (char)(var2 >> 16);
            this.intUnits[2] = (char)var2;
            var4 = 3;
         }

         char[] var10000 = this.intUnits;
         var10000[0] |= (char)var3;
         return this.write(this.intUnits, var4);
      }
   }

   /** @deprecated */
   protected int writeDeltaTo(int var1) {
      int var2 = this.charsLength - var1;
      if (!$assertionsDisabled && var2 < 0) {
         throw new AssertionError();
      } else if (var2 <= 64511) {
         return this.write(var2);
      } else {
         byte var3;
         if (var2 <= 67043327) {
            this.intUnits[0] = (char)('ﰀ' + (var2 >> 16));
            var3 = 1;
         } else {
            this.intUnits[0] = '\uffff';
            this.intUnits[1] = (char)(var2 >> 16);
            var3 = 2;
         }

         byte var10001 = var3;
         int var4 = var3 + 1;
         this.intUnits[var10001] = (char)var2;
         return this.write(this.intUnits, var4);
      }
   }
}
