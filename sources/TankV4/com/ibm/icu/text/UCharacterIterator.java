package com.ibm.icu.text;

import com.ibm.icu.impl.CharacterIteratorWrapper;
import com.ibm.icu.impl.ReplaceableUCharacterIterator;
import com.ibm.icu.impl.UCharArrayIterator;
import com.ibm.icu.impl.UCharacterIteratorWrapper;
import com.ibm.icu.impl.UCharacterProperty;
import java.text.CharacterIterator;

public abstract class UCharacterIterator implements Cloneable, UForwardCharacterIterator {
   protected UCharacterIterator() {
   }

   public static final UCharacterIterator getInstance(Replaceable var0) {
      return new ReplaceableUCharacterIterator(var0);
   }

   public static final UCharacterIterator getInstance(String var0) {
      return new ReplaceableUCharacterIterator(var0);
   }

   public static final UCharacterIterator getInstance(char[] var0) {
      return getInstance(var0, 0, var0.length);
   }

   public static final UCharacterIterator getInstance(char[] var0, int var1, int var2) {
      return new UCharArrayIterator(var0, var1, var2);
   }

   public static final UCharacterIterator getInstance(StringBuffer var0) {
      return new ReplaceableUCharacterIterator(var0);
   }

   public static final UCharacterIterator getInstance(CharacterIterator var0) {
      return new CharacterIteratorWrapper(var0);
   }

   public CharacterIterator getCharacterIterator() {
      return new UCharacterIteratorWrapper(this);
   }

   public abstract int current();

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

   public abstract int getLength();

   public abstract int getIndex();

   public abstract int next();

   public int nextCodePoint() {
      int var1 = this.next();
      if (UTF16.isLeadSurrogate((char)var1)) {
         int var2 = this.next();
         if (UTF16.isTrailSurrogate((char)var2)) {
            return UCharacterProperty.getRawSupplementary((char)var1, (char)var2);
         }

         if (var2 != -1) {
            this.previous();
         }
      }

      return var1;
   }

   public abstract int previous();

   public int previousCodePoint() {
      int var1 = this.previous();
      if (UTF16.isTrailSurrogate((char)var1)) {
         int var2 = this.previous();
         if (UTF16.isLeadSurrogate((char)var2)) {
            return UCharacterProperty.getRawSupplementary((char)var2, (char)var1);
         }

         if (var2 != -1) {
            this.next();
         }
      }

      return var1;
   }

   public abstract void setIndex(int var1);

   public void setToLimit() {
      this.setIndex(this.getLength());
   }

   public void setToStart() {
      this.setIndex(0);
   }

   public abstract int getText(char[] var1, int var2);

   public final int getText(char[] var1) {
      return this.getText(var1, 0);
   }

   public String getText() {
      char[] var1 = new char[this.getLength()];
      this.getText(var1);
      return new String(var1);
   }

   public int moveIndex(int var1) {
      int var2 = Math.max(0, Math.min(this.getIndex() + var1, this.getLength()));
      this.setIndex(var2);
      return var2;
   }

   public int moveCodePointIndex(int var1) {
      if (var1 > 0) {
         while(var1 > 0 && this.nextCodePoint() != -1) {
            --var1;
         }
      } else {
         while(var1 < 0 && this.previousCodePoint() != -1) {
            ++var1;
         }
      }

      if (var1 != 0) {
         throw new IndexOutOfBoundsException();
      } else {
         return this.getIndex();
      }
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }
}
