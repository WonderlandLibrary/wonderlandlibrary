package com.google.common.collect;

import com.google.common.annotations.Beta;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.Map.Entry;

public abstract class ForwardingNavigableMap extends ForwardingSortedMap implements NavigableMap {
   protected ForwardingNavigableMap() {
   }

   protected abstract NavigableMap delegate();

   public Entry lowerEntry(Object var1) {
      return this.delegate().lowerEntry(var1);
   }

   protected Entry standardLowerEntry(Object var1) {
      return this.headMap(var1, false).lastEntry();
   }

   public Object lowerKey(Object var1) {
      return this.delegate().lowerKey(var1);
   }

   protected Object standardLowerKey(Object var1) {
      return Maps.keyOrNull(this.lowerEntry(var1));
   }

   public Entry floorEntry(Object var1) {
      return this.delegate().floorEntry(var1);
   }

   protected Entry standardFloorEntry(Object var1) {
      return this.headMap(var1, true).lastEntry();
   }

   public Object floorKey(Object var1) {
      return this.delegate().floorKey(var1);
   }

   protected Object standardFloorKey(Object var1) {
      return Maps.keyOrNull(this.floorEntry(var1));
   }

   public Entry ceilingEntry(Object var1) {
      return this.delegate().ceilingEntry(var1);
   }

   protected Entry standardCeilingEntry(Object var1) {
      return this.tailMap(var1, true).firstEntry();
   }

   public Object ceilingKey(Object var1) {
      return this.delegate().ceilingKey(var1);
   }

   protected Object standardCeilingKey(Object var1) {
      return Maps.keyOrNull(this.ceilingEntry(var1));
   }

   public Entry higherEntry(Object var1) {
      return this.delegate().higherEntry(var1);
   }

   protected Entry standardHigherEntry(Object var1) {
      return this.tailMap(var1, false).firstEntry();
   }

   public Object higherKey(Object var1) {
      return this.delegate().higherKey(var1);
   }

   protected Object standardHigherKey(Object var1) {
      return Maps.keyOrNull(this.higherEntry(var1));
   }

   public Entry firstEntry() {
      return this.delegate().firstEntry();
   }

   protected Entry standardFirstEntry() {
      return (Entry)Iterables.getFirst(this.entrySet(), (Object)null);
   }

   protected Object standardFirstKey() {
      Entry var1 = this.firstEntry();
      if (var1 == null) {
         throw new NoSuchElementException();
      } else {
         return var1.getKey();
      }
   }

   public Entry lastEntry() {
      return this.delegate().lastEntry();
   }

   protected Entry standardLastEntry() {
      return (Entry)Iterables.getFirst(this.descendingMap().entrySet(), (Object)null);
   }

   protected Object standardLastKey() {
      Entry var1 = this.lastEntry();
      if (var1 == null) {
         throw new NoSuchElementException();
      } else {
         return var1.getKey();
      }
   }

   public Entry pollFirstEntry() {
      return this.delegate().pollFirstEntry();
   }

   protected Entry standardPollFirstEntry() {
      return (Entry)Iterators.pollNext(this.entrySet().iterator());
   }

   public Entry pollLastEntry() {
      return this.delegate().pollLastEntry();
   }

   protected Entry standardPollLastEntry() {
      return (Entry)Iterators.pollNext(this.descendingMap().entrySet().iterator());
   }

   public NavigableMap descendingMap() {
      return this.delegate().descendingMap();
   }

   public NavigableSet navigableKeySet() {
      return this.delegate().navigableKeySet();
   }

   public NavigableSet descendingKeySet() {
      return this.delegate().descendingKeySet();
   }

   @Beta
   protected NavigableSet standardDescendingKeySet() {
      return this.descendingMap().navigableKeySet();
   }

   protected SortedMap standardSubMap(Object var1, Object var2) {
      return this.subMap(var1, true, var2, false);
   }

   public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
      return this.delegate().subMap(var1, var2, var3, var4);
   }

   public NavigableMap headMap(Object var1, boolean var2) {
      return this.delegate().headMap(var1, var2);
   }

   public NavigableMap tailMap(Object var1, boolean var2) {
      return this.delegate().tailMap(var1, var2);
   }

   protected SortedMap standardHeadMap(Object var1) {
      return this.headMap(var1, false);
   }

   protected SortedMap standardTailMap(Object var1) {
      return this.tailMap(var1, true);
   }

   protected SortedMap delegate() {
      return this.delegate();
   }

   protected Map delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }

   @Beta
   protected class StandardNavigableKeySet extends Maps.NavigableKeySet {
      final ForwardingNavigableMap this$0;

      public StandardNavigableKeySet(ForwardingNavigableMap var1) {
         super(var1);
         this.this$0 = var1;
      }
   }

   @Beta
   protected class StandardDescendingMap extends Maps.DescendingMap {
      final ForwardingNavigableMap this$0;

      public StandardDescendingMap(ForwardingNavigableMap var1) {
         this.this$0 = var1;
      }

      NavigableMap forward() {
         return this.this$0;
      }

      protected Iterator entryIterator() {
         return new Iterator(this) {
            private Entry toRemove;
            private Entry nextOrNull;
            final ForwardingNavigableMap.StandardDescendingMap this$1;

            {
               this.this$1 = var1;
               this.toRemove = null;
               this.nextOrNull = this.this$1.forward().lastEntry();
            }

            public Entry next() {
               if (this != null) {
                  throw new NoSuchElementException();
               } else {
                  Entry var1 = this.nextOrNull;
                  this.toRemove = this.nextOrNull;
                  this.nextOrNull = this.this$1.forward().lowerEntry(this.nextOrNull.getKey());
                  return var1;
               }
            }

            public void remove() {
               CollectPreconditions.checkRemove(this.toRemove != null);
               this.this$1.forward().remove(this.toRemove.getKey());
               this.toRemove = null;
            }

            public Object next() {
               return this.next();
            }
         };
      }
   }
}
