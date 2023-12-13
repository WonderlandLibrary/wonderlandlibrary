package com.sun.javafx.collections;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;

public final class UnmodifiableListSet extends AbstractSet {
   private List backingList;

   public UnmodifiableListSet(List var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.backingList = var1;
      }
   }

   public Iterator iterator() {
      final Iterator var1 = this.backingList.iterator();
      return new Iterator() {
         public boolean hasNext() {
            return var1.hasNext();
         }

         public Object next() {
            return var1.next();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public int size() {
      return this.backingList.size();
   }
}
