package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class RegularImmutableSortedMap extends ImmutableSortedMap {
   private final transient RegularImmutableSortedSet keySet;
   private final transient ImmutableList valueList;

   RegularImmutableSortedMap(RegularImmutableSortedSet var1, ImmutableList var2) {
      this.keySet = var1;
      this.valueList = var2;
   }

   RegularImmutableSortedMap(RegularImmutableSortedSet var1, ImmutableList var2, ImmutableSortedMap var3) {
      super(var3);
      this.keySet = var1;
      this.valueList = var2;
   }

   ImmutableSet createEntrySet() {
      return new RegularImmutableSortedMap.EntrySet(this);
   }

   public ImmutableSortedSet keySet() {
      return this.keySet;
   }

   public ImmutableCollection values() {
      return this.valueList;
   }

   public Object get(@Nullable Object var1) {
      int var2 = this.keySet.indexOf(var1);
      return var2 == -1 ? null : this.valueList.get(var2);
   }

   private ImmutableSortedMap getSubMap(int var1, int var2) {
      if (var1 == 0 && var2 == this.size()) {
         return this;
      } else {
         return var1 == var2 ? emptyMap(this.comparator()) : from(this.keySet.getSubSet(var1, var2), this.valueList.subList(var1, var2));
      }
   }

   public ImmutableSortedMap headMap(Object var1, boolean var2) {
      return this.getSubMap(0, this.keySet.headIndex(Preconditions.checkNotNull(var1), var2));
   }

   public ImmutableSortedMap tailMap(Object var1, boolean var2) {
      return this.getSubMap(this.keySet.tailIndex(Preconditions.checkNotNull(var1), var2), this.size());
   }

   ImmutableSortedMap createDescendingMap() {
      return new RegularImmutableSortedMap((RegularImmutableSortedSet)this.keySet.descendingSet(), this.valueList.reverse(), this);
   }

   public NavigableMap tailMap(Object var1, boolean var2) {
      return this.tailMap(var1, var2);
   }

   public NavigableMap headMap(Object var1, boolean var2) {
      return this.headMap(var1, var2);
   }

   public Collection values() {
      return this.values();
   }

   public Set keySet() {
      return this.keySet();
   }

   public ImmutableSet keySet() {
      return this.keySet();
   }

   static ImmutableList access$100(RegularImmutableSortedMap var0) {
      return var0.valueList;
   }

   private class EntrySet extends ImmutableMapEntrySet {
      final RegularImmutableSortedMap this$0;

      private EntrySet(RegularImmutableSortedMap var1) {
         this.this$0 = var1;
      }

      public UnmodifiableIterator iterator() {
         return this.asList().iterator();
      }

      ImmutableList createAsList() {
         return new ImmutableAsList(this) {
            private final ImmutableList keyList;
            final RegularImmutableSortedMap.EntrySet this$1;

            {
               this.this$1 = var1;
               this.keyList = this.this$1.this$0.keySet().asList();
            }

            public Entry get(int var1) {
               return Maps.immutableEntry(this.keyList.get(var1), RegularImmutableSortedMap.access$100(this.this$1.this$0).get(var1));
            }

            ImmutableCollection delegateCollection() {
               return this.this$1;
            }

            public Object get(int var1) {
               return this.get(var1);
            }
         };
      }

      ImmutableMap map() {
         return this.this$0;
      }

      public Iterator iterator() {
         return this.iterator();
      }

      EntrySet(RegularImmutableSortedMap var1, Object var2) {
         this(var1);
      }
   }
}
