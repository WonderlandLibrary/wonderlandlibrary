package com.ibm.icu.impl;

import com.ibm.icu.text.UCharacterIterator;

public final class UCharArrayIterator extends UCharacterIterator {
   private final char[] text;
   private final int start;
   private final int limit;
   private int pos;

   public UCharArrayIterator(char[] var1, int var2, int var3) {
      if (var2 >= 0 && var3 <= var1.length && var2 <= var3) {
         this.text = var1;
         this.start = var2;
         this.limit = var3;
         this.pos = var2;
      } else {
         throw new IllegalArgumentException("start: " + var2 + " or limit: " + var3 + " out of range [0, " + var1.length + ")");
      }
   }

   public int current() {
      return this.pos < this.limit ? this.text[this.pos] : -1;
   }

   public int getLength() {
      return this.limit - this.start;
   }

   public int getIndex() {
      return this.pos - this.start;
   }

   public int next() {
      return this.pos < this.limit ? this.text[this.pos++] : -1;
   }

   public int previous() {
      return this.pos > this.start ? this.text[--this.pos] : -1;
   }

   public void setIndex(int var1) {
      if (var1 >= 0 && var1 <= this.limit - this.start) {
         this.pos = this.start + var1;
      } else {
         throw new IndexOutOfBoundsException("index: " + var1 + " out of range [0, " + (this.limit - this.start) + ")");
      }
   }

   public int getText(char[] var1, int var2) {
      int var3 = this.limit - this.start;
      System.arraycopy(this.text, this.start, var1, var2, var3);
      return var3;
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }
}
