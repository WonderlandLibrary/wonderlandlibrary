package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Iterator;
import java.util.ListIterator;

@GwtCompatible
public abstract class ForwardingListIterator extends ForwardingIterator implements ListIterator {
   protected ForwardingListIterator() {
   }

   protected abstract ListIterator delegate();

   public void add(Object var1) {
      this.delegate().add(var1);
   }

   public boolean hasPrevious() {
      return this.delegate().hasPrevious();
   }

   public int nextIndex() {
      return this.delegate().nextIndex();
   }

   public Object previous() {
      return this.delegate().previous();
   }

   public int previousIndex() {
      return this.delegate().previousIndex();
   }

   public void set(Object var1) {
      this.delegate().set(var1);
   }

   protected Iterator delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }
}
