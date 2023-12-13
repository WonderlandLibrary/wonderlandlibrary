package com.ibm.icu.impl;

import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.ReplaceableString;
import com.ibm.icu.text.UCharacterIterator;
import com.ibm.icu.text.UTF16;

public class ReplaceableUCharacterIterator extends UCharacterIterator {
   private Replaceable replaceable;
   private int currentIndex;

   public ReplaceableUCharacterIterator(Replaceable var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.replaceable = var1;
         this.currentIndex = 0;
      }
   }

   public ReplaceableUCharacterIterator(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.replaceable = new ReplaceableString(var1);
         this.currentIndex = 0;
      }
   }

   public ReplaceableUCharacterIterator(StringBuffer var1) {
      if (var1 == null) {
         throw new IllegalArgumentException();
      } else {
         this.replaceable = new ReplaceableString(var1);
         this.currentIndex = 0;
      }
   }

   public Object clone() {
      try {
         return super.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   public int current() {
      return this.currentIndex < this.replaceable.length() ? this.replaceable.charAt(this.currentIndex) : -1;
   }

   public int currentCodePoint() {
      int var1 = this.current();
      if (UTF16.isLeadSurrogate((char)var1)) {
         this.next();
         int var2 = this.current();
         this.previous();
         if (UTF16.isTrailSurrogate((char)var2)) {
            return UCharacterProperty.getRawSupplementary((char)var1, (char)var2);
         }
      }

      return var1;
   }

   public int getLength() {
      return this.replaceable.length();
   }

   public int getIndex() {
      return this.currentIndex;
   }

   public int next() {
      return this.currentIndex < this.replaceable.length() ? this.replaceable.charAt(this.currentIndex++) : -1;
   }

   public int previous() {
      return this.currentIndex > 0 ? this.replaceable.charAt(--this.currentIndex) : -1;
   }

   public void setIndex(int var1) throws IndexOutOfBoundsException {
      if (var1 >= 0 && var1 <= this.replaceable.length()) {
         this.currentIndex = var1;
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public int getText(char[] var1, int var2) {
      int var3 = this.replaceable.length();
      if (var2 >= 0 && var2 + var3 <= var1.length) {
         this.replaceable.getChars(0, var3, var1, var2);
         return var3;
      } else {
         throw new IndexOutOfBoundsException(Integer.toString(var3));
      }
   }
}
