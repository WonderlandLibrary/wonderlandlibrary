package com.sun.javafx.text;

import java.text.CharacterIterator;

class CharArrayIterator implements CharacterIterator {
   private char[] chars;
   private int pos;
   private int begin;

   public CharArrayIterator(char[] var1) {
      this.reset(var1, 0);
   }

   public CharArrayIterator(char[] var1, int var2) {
      this.reset(var1, var2);
   }

   public char first() {
      this.pos = 0;
      return this.current();
   }

   public char last() {
      if (this.chars.length > 0) {
         this.pos = this.chars.length - 1;
      } else {
         this.pos = 0;
      }

      return this.current();
   }

   public char current() {
      return this.pos >= 0 && this.pos < this.chars.length ? this.chars[this.pos] : '\uffff';
   }

   public char next() {
      if (this.pos < this.chars.length - 1) {
         ++this.pos;
         return this.chars[this.pos];
      } else {
         this.pos = this.chars.length;
         return '\uffff';
      }
   }

   public char previous() {
      if (this.pos > 0) {
         --this.pos;
         return this.chars[this.pos];
      } else {
         this.pos = 0;
         return '\uffff';
      }
   }

   public char setIndex(int var1) {
      var1 -= this.begin;
      if (var1 >= 0 && var1 <= this.chars.length) {
         this.pos = var1;
         return this.current();
      } else {
         throw new IllegalArgumentException("Invalid index");
      }
   }

   public int getBeginIndex() {
      return this.begin;
   }

   public int getEndIndex() {
      return this.begin + this.chars.length;
   }

   public int getIndex() {
      return this.begin + this.pos;
   }

   public Object clone() {
      CharArrayIterator var1 = new CharArrayIterator(this.chars, this.begin);
      var1.pos = this.pos;
      return var1;
   }

   void reset(char[] var1) {
      this.reset(var1, 0);
   }

   void reset(char[] var1, int var2) {
      this.chars = var1;
      this.begin = var2;
      this.pos = 0;
   }
}
