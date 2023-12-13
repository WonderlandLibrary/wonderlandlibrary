package org.apache.commons.io.input;

import java.io.Reader;
import java.io.Serializable;

public class CharSequenceReader extends Reader implements Serializable {
   private final CharSequence charSequence;
   private int idx;
   private int mark;

   public CharSequenceReader(CharSequence var1) {
      this.charSequence = (CharSequence)(var1 != null ? var1 : "");
   }

   public void close() {
      this.idx = 0;
      this.mark = 0;
   }

   public void mark(int var1) {
      this.mark = this.idx;
   }

   public boolean markSupported() {
      return true;
   }

   public int read() {
      return this.idx >= this.charSequence.length() ? -1 : this.charSequence.charAt(this.idx++);
   }

   public int read(char[] var1, int var2, int var3) {
      if (this.idx >= this.charSequence.length()) {
         return -1;
      } else if (var1 == null) {
         throw new NullPointerException("Character array is missing");
      } else if (var3 >= 0 && var2 >= 0 && var2 + var3 <= var1.length) {
         int var4 = 0;

         for(int var5 = 0; var5 < var3; ++var5) {
            int var6 = this.read();
            if (var6 == -1) {
               return var4;
            }

            var1[var2 + var5] = (char)var6;
            ++var4;
         }

         return var4;
      } else {
         throw new IndexOutOfBoundsException("Array Size=" + var1.length + ", offset=" + var2 + ", length=" + var3);
      }
   }

   public void reset() {
      this.idx = this.mark;
   }

   public long skip(long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Number of characters to skip is less than zero: " + var1);
      } else if (this.idx >= this.charSequence.length()) {
         return -1L;
      } else {
         int var3 = (int)Math.min((long)this.charSequence.length(), (long)this.idx + var1);
         int var4 = var3 - this.idx;
         this.idx = var3;
         return (long)var4;
      }
   }

   public String toString() {
      return this.charSequence.toString();
   }
}
