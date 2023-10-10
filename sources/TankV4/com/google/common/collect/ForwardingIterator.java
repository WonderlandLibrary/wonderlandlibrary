package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;

@GwtCompatible
public abstract class ForwardingIterator extends ForwardingObject implements Iterator {
   protected ForwardingIterator() {
   }

   protected abstract Iterator delegate();

   public boolean hasNext() {
      return this.delegate().hasNext();
   }

   public Object next() {
      return this.delegate().next();
   }

   public void remove() {
      this.delegate().remove();
   }

   protected Object delegate() {
      return this.delegate();
   }
}
