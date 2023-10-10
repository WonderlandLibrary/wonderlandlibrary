package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

@GwtCompatible(
   emulated = true
)
abstract class DescendingMultiset extends ForwardingMultiset implements SortedMultiset {
   private transient Comparator comparator;
   private transient NavigableSet elementSet;
   private transient Set entrySet;

   abstract SortedMultiset forwardMultiset();

   public Comparator comparator() {
      Comparator var1 = this.comparator;
      return var1 == null ? (this.comparator = Ordering.from(this.forwardMultiset().comparator()).reverse()) : var1;
   }

   public NavigableSet elementSet() {
      NavigableSet var1 = this.elementSet;
      return var1 == null ? (this.elementSet = new SortedMultisets.NavigableElementSet(this)) : var1;
   }

   public Multiset.Entry pollFirstEntry() {
      return this.forwardMultiset().pollLastEntry();
   }

   public Multiset.Entry pollLastEntry() {
      return this.forwardMultiset().pollFirstEntry();
   }

   public SortedMultiset headMultiset(Object var1, BoundType var2) {
      return this.forwardMultiset().tailMultiset(var1, var2).descendingMultiset();
   }

   public SortedMultiset subMultiset(Object var1, BoundType var2, Object var3, BoundType var4) {
      return this.forwardMultiset().subMultiset(var3, var4, var1, var2).descendingMultiset();
   }

   public SortedMultiset tailMultiset(Object var1, BoundType var2) {
      return this.forwardMultiset().headMultiset(var1, var2).descendingMultiset();
   }

   protected Multiset delegate() {
      return this.forwardMultiset();
   }

   public SortedMultiset descendingMultiset() {
      return this.forwardMultiset();
   }

   public Multiset.Entry firstEntry() {
      return this.forwardMultiset().lastEntry();
   }

   public Multiset.Entry lastEntry() {
      return this.forwardMultiset().firstEntry();
   }

   abstract Iterator entryIterator();

   public Set entrySet() {
      Set var1 = this.entrySet;
      return var1 == null ? (this.entrySet = this.createEntrySet()) : var1;
   }

   Set createEntrySet() {
      return new Multisets.EntrySet(this) {
         final DescendingMultiset this$0;

         {
            this.this$0 = var1;
         }

         Multiset multiset() {
            return this.this$0;
         }

         public Iterator iterator() {
            return this.this$0.entryIterator();
         }

         public int size() {
            return this.this$0.forwardMultiset().entrySet().size();
         }
      };
   }

   public Iterator iterator() {
      return Multisets.iteratorImpl(this);
   }

   public Object[] toArray() {
      return this.standardToArray();
   }

   public Object[] toArray(Object[] var1) {
      return this.standardToArray(var1);
   }

   public String toString() {
      return this.entrySet().toString();
   }

   public Set elementSet() {
      return this.elementSet();
   }

   protected Collection delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }

   public SortedSet elementSet() {
      return this.elementSet();
   }
}
