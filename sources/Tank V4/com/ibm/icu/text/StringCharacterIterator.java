package com.ibm.icu.text;

import java.text.CharacterIterator;

/** @deprecated */
public final class StringCharacterIterator implements CharacterIterator {
   private String text;
   private int begin;
   private int end;
   private int pos;

   /** @deprecated */
   public StringCharacterIterator(String var1) {
      this(var1, 0);
   }

   /** @deprecated */
   public StringCharacterIterator(String var1, int var2) {
      this(var1, 0, var1.length(), var2);
   }

   /** @deprecated */
   public StringCharacterIterator(String var1, int var2, int var3, int var4) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.text = var1;
         if (var2 >= 0 && var2 <= var3 && var3 <= var1.length()) {
            if (var4 >= var2 && var4 <= var3) {
               this.begin = var2;
               this.end = var3;
               this.pos = var4;
            } else {
               throw new IllegalArgumentException("Invalid position");
            }
         } else {
            throw new IllegalArgumentException("Invalid substring range");
         }
      }
   }

   /** @deprecated */
   public void setText(String var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.text = var1;
         this.begin = 0;
         this.end = var1.length();
         this.pos = 0;
      }
   }

   /** @deprecated */
   public char first() {
      this.pos = this.begin;
      return this.current();
   }

   /** @deprecated */
   public char last() {
      if (this.end != this.begin) {
         this.pos = this.end - 1;
      } else {
         this.pos = this.end;
      }

      return this.current();
   }

   /** @deprecated */
   public char setIndex(int var1) {
      if (var1 >= this.begin && var1 <= this.end) {
         this.pos = var1;
         return this.current();
      } else {
         throw new IllegalArgumentException("Invalid index");
      }
   }

   /** @deprecated */
   public char current() {
      return this.pos >= this.begin && this.pos < this.end ? this.text.charAt(this.pos) : '\uffff';
   }

   /** @deprecated */
   public char next() {
      if (this.pos < this.end - 1) {
         ++this.pos;
         return this.text.charAt(this.pos);
      } else {
         this.pos = this.end;
         return '\uffff';
      }
   }

   /** @deprecated */
   public char previous() {
      if (this.pos > this.begin) {
         --this.pos;
         return this.text.charAt(this.pos);
      } else {
         return '\uffff';
      }
   }

   /** @deprecated */
   public int getBeginIndex() {
      return this.begin;
   }

   /** @deprecated */
   public int getEndIndex() {
      return this.end;
   }

   /** @deprecated */
   public int getIndex() {
      return this.pos;
   }

   /** @deprecated */
   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof StringCharacterIterator)) {
         return false;
      } else {
         StringCharacterIterator var2 = (StringCharacterIterator)var1;
         if (this.hashCode() != var2.hashCode()) {
            return false;
         } else if (!this.text.equals(var2.text)) {
            return false;
         } else {
            return this.pos == var2.pos && this.begin == var2.begin && this.end == var2.end;
         }
      }
   }

   /** @deprecated */
   public int hashCode() {
      return this.text.hashCode() ^ this.pos ^ this.begin ^ this.end;
   }

   /** @deprecated */
   public Object clone() {
      try {
         StringCharacterIterator var1 = (StringCharacterIterator)super.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         throw new IllegalStateException();
      }
   }
}
