package com.google.common.collect;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.Map.Entry;
import javax.annotation.Nullable;

abstract class AbstractNavigableMap extends AbstractMap implements NavigableMap {
   @Nullable
   public abstract Object get(@Nullable Object var1);

   @Nullable
   public Entry firstEntry() {
      return (Entry)Iterators.getNext(this.entryIterator(), (Object)null);
   }

   @Nullable
   public Entry lastEntry() {
      return (Entry)Iterators.getNext(this.descendingEntryIterator(), (Object)null);
   }

   @Nullable
   public Entry pollFirstEntry() {
      return (Entry)Iterators.pollNext(this.entryIterator());
   }

   @Nullable
   public Entry pollLastEntry() {
      return (Entry)Iterators.pollNext(this.descendingEntryIterator());
   }

   public Object firstKey() {
      Entry var1 = this.firstEntry();
      if (var1 == null) {
         throw new NoSuchElementException();
      } else {
         return var1.getKey();
      }
   }

   public Object lastKey() {
      Entry var1 = this.lastEntry();
      if (var1 == null) {
         throw new NoSuchElementException();
      } else {
         return var1.getKey();
      }
   }

   @Nullable
   public Entry lowerEntry(Object var1) {
      return this.headMap(var1, false).lastEntry();
   }

   @Nullable
   public Entry floorEntry(Object var1) {
      return this.headMap(var1, true).lastEntry();
   }

   @Nullable
   public Entry ceilingEntry(Object var1) {
      return this.tailMap(var1, true).firstEntry();
   }

   @Nullable
   public Entry higherEntry(Object var1) {
      return this.tailMap(var1, false).firstEntry();
   }

   public Object lowerKey(Object var1) {
      return Maps.keyOrNull(this.lowerEntry(var1));
   }

   public Object floorKey(Object var1) {
      return Maps.keyOrNull(this.floorEntry(var1));
   }

   public Object ceilingKey(Object var1) {
      return Maps.keyOrNull(this.ceilingEntry(var1));
   }

   public Object higherKey(Object var1) {
      return Maps.keyOrNull(this.higherEntry(var1));
   }

   abstract Iterator entryIterator();

   abstract Iterator descendingEntryIterator();

   public SortedMap subMap(Object var1, Object var2) {
      return this.subMap(var1, true, var2, false);
   }

   public SortedMap headMap(Object var1) {
      return this.headMap(var1, false);
   }

   public SortedMap tailMap(Object var1) {
      return this.tailMap(var1, true);
   }

   public NavigableSet navigableKeySet() {
      return new Maps.NavigableKeySet(this);
   }

   public Set keySet() {
      return this.navigableKeySet();
   }

   public abstract int size();

   public Set entrySet() {
      return new Maps.EntrySet(this) {
         final AbstractNavigableMap this$0;

         {
            this.this$0 = var1;
         }

         Map map() {
            return this.this$0;
         }

         public Iterator iterator() {
            return this.this$0.entryIterator();
         }
      };
   }

   public NavigableSet descendingKeySet() {
      return this.descendingMap().navigableKeySet();
   }

   public NavigableMap descendingMap() {
      return new AbstractNavigableMap.DescendingMap(this);
   }

   private final class DescendingMap extends Maps.DescendingMap {
      final AbstractNavigableMap this$0;

      private DescendingMap(AbstractNavigableMap var1) {
         this.this$0 = var1;
      }

      NavigableMap forward() {
         return this.this$0;
      }

      Iterator entryIterator() {
         return this.this$0.descendingEntryIterator();
      }

      DescendingMap(AbstractNavigableMap var1, Object var2) {
         this(var1);
      }
   }
}
