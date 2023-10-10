package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Queue;

@GwtCompatible
public abstract class ForwardingQueue extends ForwardingCollection implements Queue {
   protected ForwardingQueue() {
   }

   protected abstract Queue delegate();

   public boolean offer(Object var1) {
      return this.delegate().offer(var1);
   }

   public Object poll() {
      return this.delegate().poll();
   }

   public Object remove() {
      return this.delegate().remove();
   }

   public Object peek() {
      return this.delegate().peek();
   }

   public Object element() {
      return this.delegate().element();
   }

   protected boolean standardOffer(Object var1) {
      try {
         return this.add(var1);
      } catch (IllegalStateException var3) {
         return false;
      }
   }

   protected Object standardPeek() {
      try {
         return this.element();
      } catch (NoSuchElementException var2) {
         return null;
      }
   }

   protected Object standardPoll() {
      try {
         return this.remove();
      } catch (NoSuchElementException var2) {
         return null;
      }
   }

   protected Collection delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }
}
