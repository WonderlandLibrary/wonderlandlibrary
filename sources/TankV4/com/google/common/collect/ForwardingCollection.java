package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import java.util.Collection;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingCollection extends ForwardingObject implements Collection {
   protected ForwardingCollection() {
   }

   protected abstract Collection delegate();

   public Iterator iterator() {
      return this.delegate().iterator();
   }

   public int size() {
      return this.delegate().size();
   }

   public boolean removeAll(Collection var1) {
      return this.delegate().removeAll(var1);
   }

   public boolean isEmpty() {
      return this.delegate().isEmpty();
   }

   public boolean contains(Object var1) {
      return this.delegate().contains(var1);
   }

   public boolean add(Object var1) {
      return this.delegate().add(var1);
   }

   public boolean remove(Object var1) {
      return this.delegate().remove(var1);
   }

   public boolean containsAll(Collection var1) {
      return this.delegate().containsAll(var1);
   }

   public boolean addAll(Collection var1) {
      return this.delegate().addAll(var1);
   }

   public boolean retainAll(Collection var1) {
      return this.delegate().retainAll(var1);
   }

   public void clear() {
      this.delegate().clear();
   }

   public Object[] toArray() {
      return this.delegate().toArray();
   }

   public Object[] toArray(Object[] var1) {
      return this.delegate().toArray(var1);
   }

   protected boolean standardContains(@Nullable Object var1) {
      return Iterators.contains(this.iterator(), var1);
   }

   protected boolean standardContainsAll(Collection var1) {
      return Collections2.containsAllImpl(this, var1);
   }

   protected boolean standardAddAll(Collection var1) {
      return Iterators.addAll(this, var1.iterator());
   }

   protected boolean standardRemove(@Nullable Object var1) {
      Iterator var2 = this.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }
      } while(!Objects.equal(var2.next(), var1));

      var2.remove();
      return true;
   }

   protected boolean standardRemoveAll(Collection var1) {
      return Iterators.removeAll(this.iterator(), var1);
   }

   protected boolean standardRetainAll(Collection var1) {
      return Iterators.retainAll(this.iterator(), var1);
   }

   protected void standardClear() {
      Iterators.clear(this.iterator());
   }

   protected boolean standardIsEmpty() {
      return !this.iterator().hasNext();
   }

   protected String standardToString() {
      return Collections2.toStringImpl(this);
   }

   protected Object[] standardToArray() {
      Object[] var1 = new Object[this.size()];
      return this.toArray(var1);
   }

   protected Object[] standardToArray(Object[] var1) {
      return ObjectArrays.toArrayImpl(this, var1);
   }

   protected Object delegate() {
      return this.delegate();
   }
}
