package com.ibm.icu.impl;

import com.ibm.icu.text.UCharacterIterator;
import java.text.CharacterIterator;

public class UCharacterIteratorWrapper implements CharacterIterator {
   private UCharacterIterator iterator;

   public UCharacterIteratorWrapper(UCharacterIterator var1) {
      this.iterator = var1;
   }

   public char first() {
      this.iterator.setToStart();
      return (char)this.iterator.current();
   }

   public char last() {
      this.iterator.setToLimit();
      return (char)this.iterator.previous();
   }

   public char current() {
      return (char)this.iterator.current();
   }

   public char next() {
      this.iterator.next();
      return (char)this.iterator.current();
   }

   public char previous() {
      return (char)this.iterator.previous();
   }

   public char setIndex(int var1) {
      this.iterator.setIndex(var1);
      return (char)this.iterator.current();
   }

   public int getBeginIndex() {
      return 0;
   }

   public int getEndIndex() {
      return this.iterator.getLength();
   }

   public int getIndex() {
      return this.iterator.getIndex();
   }

   public Object clone() {
      try {
         UCharacterIteratorWrapper var1 = (UCharacterIteratorWrapper)super.clone();
         var1.iterator = (UCharacterIterator)this.iterator.clone();
         return var1;
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }
}
