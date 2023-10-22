package com.sun.javafx;

import java.util.AbstractList;
import java.util.RandomAccess;

public class UnmodifiableArrayList extends AbstractList implements RandomAccess {
   private Object[] elements;
   private final int size;
   // $FF: synthetic field
   static final boolean $assertionsDisabled = !UnmodifiableArrayList.class.desiredAssertionStatus();

   public UnmodifiableArrayList(Object[] var1, int var2) {
      if (!$assertionsDisabled) {
         if (var1 == null) {
            if (var2 != 0) {
               throw new AssertionError();
            }
         } else if (var2 > var1.length) {
            throw new AssertionError();
         }
      }

      this.size = var2;
      this.elements = var1;
   }

   public Object get(int var1) {
      return this.elements[var1];
   }

   public int size() {
      return this.size;
   }
}
