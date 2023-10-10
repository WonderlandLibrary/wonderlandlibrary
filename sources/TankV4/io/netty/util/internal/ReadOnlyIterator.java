package io.netty.util.internal;

import java.util.Iterator;

public final class ReadOnlyIterator implements Iterator {
   private final Iterator iterator;

   public ReadOnlyIterator(Iterator var1) {
      if (var1 == null) {
         throw new NullPointerException("iterator");
      } else {
         this.iterator = var1;
      }
   }

   public boolean hasNext() {
      return this.iterator.hasNext();
   }

   public Object next() {
      return this.iterator.next();
   }

   public void remove() {
      throw new UnsupportedOperationException("read-only");
   }
}
