package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMultiset extends AbstractCollection implements Multiset {
   private transient Set elementSet;
   private transient Set entrySet;

   public int size() {
      return Multisets.sizeImpl(this);
   }

   public boolean isEmpty() {
      return this.entrySet().isEmpty();
   }

   public boolean contains(@Nullable Object var1) {
      return this.count(var1) > 0;
   }

   public Iterator iterator() {
      return Multisets.iteratorImpl(this);
   }

   public int count(@Nullable Object var1) {
      Iterator var2 = this.entrySet().iterator();

      Multiset.Entry var3;
      do {
         if (!var2.hasNext()) {
            return 0;
         }

         var3 = (Multiset.Entry)var2.next();
      } while(!Objects.equal(var3.getElement(), var1));

      return var3.getCount();
   }

   public boolean add(@Nullable Object var1) {
      this.add(var1, 1);
      return true;
   }

   public int add(@Nullable Object var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(@Nullable Object var1) {
      return this.remove(var1, 1) > 0;
   }

   public int remove(@Nullable Object var1, int var2) {
      throw new UnsupportedOperationException();
   }

   public int setCount(@Nullable Object var1, int var2) {
      return Multisets.setCountImpl(this, var1, var2);
   }

   public boolean setCount(@Nullable Object var1, int var2, int var3) {
      return Multisets.setCountImpl(this, var1, var2, var3);
   }

   public boolean addAll(Collection var1) {
      return Multisets.addAllImpl(this, var1);
   }

   public boolean removeAll(Collection var1) {
      return Multisets.removeAllImpl(this, var1);
   }

   public boolean retainAll(Collection var1) {
      return Multisets.retainAllImpl(this, var1);
   }

   public void clear() {
      Iterators.clear(this.entryIterator());
   }

   public Set elementSet() {
      Set var1 = this.elementSet;
      if (var1 == null) {
         this.elementSet = var1 = this.createElementSet();
      }

      return var1;
   }

   Set createElementSet() {
      return new AbstractMultiset.ElementSet(this);
   }

   abstract Iterator entryIterator();

   abstract int distinctElements();

   public Set entrySet() {
      Set var1 = this.entrySet;
      return var1 == null ? (this.entrySet = this.createEntrySet()) : var1;
   }

   Set createEntrySet() {
      return new AbstractMultiset.EntrySet(this);
   }

   public boolean equals(@Nullable Object var1) {
      return Multisets.equalsImpl(this, var1);
   }

   public int hashCode() {
      return this.entrySet().hashCode();
   }

   public String toString() {
      return this.entrySet().toString();
   }

   class EntrySet extends Multisets.EntrySet {
      final AbstractMultiset this$0;

      EntrySet(AbstractMultiset var1) {
         this.this$0 = var1;
      }

      Multiset multiset() {
         return this.this$0;
      }

      public Iterator iterator() {
         return this.this$0.entryIterator();
      }

      public int size() {
         return this.this$0.distinctElements();
      }
   }

   class ElementSet extends Multisets.ElementSet {
      final AbstractMultiset this$0;

      ElementSet(AbstractMultiset var1) {
         this.this$0 = var1;
      }

      Multiset multiset() {
         return this.this$0;
      }
   }
}
