package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

@Beta
@GwtCompatible(
   emulated = true
)
public abstract class ForwardingSortedMultiset extends ForwardingMultiset implements SortedMultiset {
   protected ForwardingSortedMultiset() {
   }

   protected abstract SortedMultiset delegate();

   public NavigableSet elementSet() {
      return (NavigableSet)super.elementSet();
   }

   public Comparator comparator() {
      return this.delegate().comparator();
   }

   public SortedMultiset descendingMultiset() {
      return this.delegate().descendingMultiset();
   }

   public Multiset.Entry firstEntry() {
      return this.delegate().firstEntry();
   }

   protected Multiset.Entry standardFirstEntry() {
      Iterator var1 = this.entrySet().iterator();
      if (!var1.hasNext()) {
         return null;
      } else {
         Multiset.Entry var2 = (Multiset.Entry)var1.next();
         return Multisets.immutableEntry(var2.getElement(), var2.getCount());
      }
   }

   public Multiset.Entry lastEntry() {
      return this.delegate().lastEntry();
   }

   protected Multiset.Entry standardLastEntry() {
      Iterator var1 = this.descendingMultiset().entrySet().iterator();
      if (!var1.hasNext()) {
         return null;
      } else {
         Multiset.Entry var2 = (Multiset.Entry)var1.next();
         return Multisets.immutableEntry(var2.getElement(), var2.getCount());
      }
   }

   public Multiset.Entry pollFirstEntry() {
      return this.delegate().pollFirstEntry();
   }

   protected Multiset.Entry standardPollFirstEntry() {
      Iterator var1 = this.entrySet().iterator();
      if (!var1.hasNext()) {
         return null;
      } else {
         Multiset.Entry var2 = (Multiset.Entry)var1.next();
         var2 = Multisets.immutableEntry(var2.getElement(), var2.getCount());
         var1.remove();
         return var2;
      }
   }

   public Multiset.Entry pollLastEntry() {
      return this.delegate().pollLastEntry();
   }

   protected Multiset.Entry standardPollLastEntry() {
      Iterator var1 = this.descendingMultiset().entrySet().iterator();
      if (!var1.hasNext()) {
         return null;
      } else {
         Multiset.Entry var2 = (Multiset.Entry)var1.next();
         var2 = Multisets.immutableEntry(var2.getElement(), var2.getCount());
         var1.remove();
         return var2;
      }
   }

   public SortedMultiset headMultiset(Object var1, BoundType var2) {
      return this.delegate().headMultiset(var1, var2);
   }

   public SortedMultiset subMultiset(Object var1, BoundType var2, Object var3, BoundType var4) {
      return this.delegate().subMultiset(var1, var2, var3, var4);
   }

   protected SortedMultiset standardSubMultiset(Object var1, BoundType var2, Object var3, BoundType var4) {
      return this.tailMultiset(var1, var2).headMultiset(var3, var4);
   }

   public SortedMultiset tailMultiset(Object var1, BoundType var2) {
      return this.delegate().tailMultiset(var1, var2);
   }

   public Set elementSet() {
      return this.elementSet();
   }

   protected Multiset delegate() {
      return this.delegate();
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

   protected abstract class StandardDescendingMultiset extends DescendingMultiset {
      final ForwardingSortedMultiset this$0;

      public StandardDescendingMultiset(ForwardingSortedMultiset var1) {
         this.this$0 = var1;
      }

      SortedMultiset forwardMultiset() {
         return this.this$0;
      }
   }

   protected class StandardElementSet extends SortedMultisets.NavigableElementSet {
      final ForwardingSortedMultiset this$0;

      public StandardElementSet(ForwardingSortedMultiset var1) {
         super(var1);
         this.this$0 = var1;
      }
   }
}
