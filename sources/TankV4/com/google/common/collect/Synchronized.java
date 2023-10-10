package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.RandomAccess;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(
   emulated = true
)
final class Synchronized {
   private Synchronized() {
   }

   private static Collection collection(Collection var0, @Nullable Object var1) {
      return new Synchronized.SynchronizedCollection(var0, var1);
   }

   @VisibleForTesting
   static Set set(Set var0, @Nullable Object var1) {
      return new Synchronized.SynchronizedSet(var0, var1);
   }

   private static SortedSet sortedSet(SortedSet var0, @Nullable Object var1) {
      return new Synchronized.SynchronizedSortedSet(var0, var1);
   }

   private static List list(List var0, @Nullable Object var1) {
      return (List)(var0 instanceof RandomAccess ? new Synchronized.SynchronizedRandomAccessList(var0, var1) : new Synchronized.SynchronizedList(var0, var1));
   }

   static Multiset multiset(Multiset var0, @Nullable Object var1) {
      return (Multiset)(!(var0 instanceof Synchronized.SynchronizedMultiset) && !(var0 instanceof ImmutableMultiset) ? new Synchronized.SynchronizedMultiset(var0, var1) : var0);
   }

   static Multimap multimap(Multimap var0, @Nullable Object var1) {
      return (Multimap)(!(var0 instanceof Synchronized.SynchronizedMultimap) && !(var0 instanceof ImmutableMultimap) ? new Synchronized.SynchronizedMultimap(var0, var1) : var0);
   }

   static ListMultimap listMultimap(ListMultimap var0, @Nullable Object var1) {
      return (ListMultimap)(!(var0 instanceof Synchronized.SynchronizedListMultimap) && !(var0 instanceof ImmutableListMultimap) ? new Synchronized.SynchronizedListMultimap(var0, var1) : var0);
   }

   static SetMultimap setMultimap(SetMultimap var0, @Nullable Object var1) {
      return (SetMultimap)(!(var0 instanceof Synchronized.SynchronizedSetMultimap) && !(var0 instanceof ImmutableSetMultimap) ? new Synchronized.SynchronizedSetMultimap(var0, var1) : var0);
   }

   static SortedSetMultimap sortedSetMultimap(SortedSetMultimap var0, @Nullable Object var1) {
      return (SortedSetMultimap)(var0 instanceof Synchronized.SynchronizedSortedSetMultimap ? var0 : new Synchronized.SynchronizedSortedSetMultimap(var0, var1));
   }

   private static Collection typePreservingCollection(Collection var0, @Nullable Object var1) {
      if (var0 instanceof SortedSet) {
         return sortedSet((SortedSet)var0, var1);
      } else if (var0 instanceof Set) {
         return set((Set)var0, var1);
      } else {
         return (Collection)(var0 instanceof List ? list((List)var0, var1) : collection(var0, var1));
      }
   }

   private static Set typePreservingSet(Set var0, @Nullable Object var1) {
      return (Set)(var0 instanceof SortedSet ? sortedSet((SortedSet)var0, var1) : set(var0, var1));
   }

   @VisibleForTesting
   static Map map(Map var0, @Nullable Object var1) {
      return new Synchronized.SynchronizedMap(var0, var1);
   }

   static SortedMap sortedMap(SortedMap var0, @Nullable Object var1) {
      return new Synchronized.SynchronizedSortedMap(var0, var1);
   }

   static BiMap biMap(BiMap var0, @Nullable Object var1) {
      return (BiMap)(!(var0 instanceof Synchronized.SynchronizedBiMap) && !(var0 instanceof ImmutableBiMap) ? new Synchronized.SynchronizedBiMap(var0, var1, (BiMap)null) : var0);
   }

   @GwtIncompatible("NavigableSet")
   static NavigableSet navigableSet(NavigableSet var0, @Nullable Object var1) {
      return new Synchronized.SynchronizedNavigableSet(var0, var1);
   }

   @GwtIncompatible("NavigableSet")
   static NavigableSet navigableSet(NavigableSet var0) {
      return navigableSet(var0, (Object)null);
   }

   @GwtIncompatible("NavigableMap")
   static NavigableMap navigableMap(NavigableMap var0) {
      return navigableMap(var0, (Object)null);
   }

   @GwtIncompatible("NavigableMap")
   static NavigableMap navigableMap(NavigableMap var0, @Nullable Object var1) {
      return new Synchronized.SynchronizedNavigableMap(var0, var1);
   }

   @GwtIncompatible("works but is needed only for NavigableMap")
   private static Entry nullableSynchronizedEntry(@Nullable Entry var0, @Nullable Object var1) {
      return var0 == null ? null : new Synchronized.SynchronizedEntry(var0, var1);
   }

   static Queue queue(Queue var0, @Nullable Object var1) {
      return (Queue)(var0 instanceof Synchronized.SynchronizedQueue ? var0 : new Synchronized.SynchronizedQueue(var0, var1));
   }

   @GwtIncompatible("Deque")
   static Deque deque(Deque var0, @Nullable Object var1) {
      return new Synchronized.SynchronizedDeque(var0, var1);
   }

   static SortedSet access$100(SortedSet var0, Object var1) {
      return sortedSet(var0, var1);
   }

   static List access$200(List var0, Object var1) {
      return list(var0, var1);
   }

   static Set access$300(Set var0, Object var1) {
      return typePreservingSet(var0, var1);
   }

   static Collection access$400(Collection var0, Object var1) {
      return typePreservingCollection(var0, var1);
   }

   static Collection access$500(Collection var0, Object var1) {
      return collection(var0, var1);
   }

   static Entry access$700(Entry var0, Object var1) {
      return nullableSynchronizedEntry(var0, var1);
   }

   @GwtIncompatible("Deque")
   private static final class SynchronizedDeque extends Synchronized.SynchronizedQueue implements Deque {
      private static final long serialVersionUID = 0L;

      SynchronizedDeque(Deque var1, @Nullable Object var2) {
         super(var1, var2);
      }

      Deque delegate() {
         return (Deque)super.delegate();
      }

      public void addFirst(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         this.delegate().addFirst(var1);
      }

      public void addLast(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         this.delegate().addLast(var1);
      }

      public boolean offerFirst(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().offerFirst(var1);
      }

      public boolean offerLast(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().offerLast(var1);
      }

      public Object removeFirst() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().removeFirst();
      }

      public Object removeLast() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().removeLast();
      }

      public Object pollFirst() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().pollFirst();
      }

      public Object pollLast() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().pollLast();
      }

      public Object getFirst() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().getFirst();
      }

      public Object getLast() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().getLast();
      }

      public Object peekFirst() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().peekFirst();
      }

      public Object peekLast() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().peekLast();
      }

      public boolean removeFirstOccurrence(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().removeFirstOccurrence(var1);
      }

      public boolean removeLastOccurrence(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().removeLastOccurrence(var1);
      }

      public void push(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         this.delegate().push(var1);
      }

      public Object pop() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().pop();
      }

      public Iterator descendingIterator() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().descendingIterator();
      }

      Queue delegate() {
         return this.delegate();
      }

      Collection delegate() {
         return this.delegate();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   private static class SynchronizedQueue extends Synchronized.SynchronizedCollection implements Queue {
      private static final long serialVersionUID = 0L;

      SynchronizedQueue(Queue var1, @Nullable Object var2) {
         super(var1, var2, null);
      }

      Queue delegate() {
         return (Queue)super.delegate();
      }

      public Object element() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().element();
      }

      public boolean offer(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().offer(var1);
      }

      public Object peek() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().peek();
      }

      public Object poll() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().poll();
      }

      public Object remove() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().remove();
      }

      Collection delegate() {
         return this.delegate();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   @GwtIncompatible("works but is needed only for NavigableMap")
   private static class SynchronizedEntry extends Synchronized.SynchronizedObject implements Entry {
      private static final long serialVersionUID = 0L;

      SynchronizedEntry(Entry var1, @Nullable Object var2) {
         super(var1, var2);
      }

      Entry delegate() {
         return (Entry)super.delegate();
      }

      public boolean equals(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().equals(var1);
      }

      public int hashCode() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().hashCode();
      }

      public Object getKey() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().getKey();
      }

      public Object getValue() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().getValue();
      }

      public Object setValue(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().setValue(var1);
      }

      Object delegate() {
         return this.delegate();
      }
   }

   @GwtIncompatible("NavigableMap")
   @VisibleForTesting
   static class SynchronizedNavigableMap extends Synchronized.SynchronizedSortedMap implements NavigableMap {
      transient NavigableSet descendingKeySet;
      transient NavigableMap descendingMap;
      transient NavigableSet navigableKeySet;
      private static final long serialVersionUID = 0L;

      SynchronizedNavigableMap(NavigableMap var1, @Nullable Object var2) {
         super(var1, var2);
      }

      NavigableMap delegate() {
         return (NavigableMap)super.delegate();
      }

      public Entry ceilingEntry(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Synchronized.access$700(this.delegate().ceilingEntry(var1), this.mutex);
      }

      public Object ceilingKey(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().ceilingKey(var1);
      }

      public NavigableSet descendingKeySet() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.descendingKeySet == null ? (this.descendingKeySet = Synchronized.navigableSet(this.delegate().descendingKeySet(), this.mutex)) : this.descendingKeySet;
      }

      public NavigableMap descendingMap() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.descendingMap == null ? (this.descendingMap = Synchronized.navigableMap(this.delegate().descendingMap(), this.mutex)) : this.descendingMap;
      }

      public Entry firstEntry() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return Synchronized.access$700(this.delegate().firstEntry(), this.mutex);
      }

      public Entry floorEntry(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Synchronized.access$700(this.delegate().floorEntry(var1), this.mutex);
      }

      public Object floorKey(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().floorKey(var1);
      }

      public NavigableMap headMap(Object var1, boolean var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return Synchronized.navigableMap(this.delegate().headMap(var1, var2), this.mutex);
      }

      public Entry higherEntry(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Synchronized.access$700(this.delegate().higherEntry(var1), this.mutex);
      }

      public Object higherKey(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().higherKey(var1);
      }

      public Entry lastEntry() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return Synchronized.access$700(this.delegate().lastEntry(), this.mutex);
      }

      public Entry lowerEntry(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Synchronized.access$700(this.delegate().lowerEntry(var1), this.mutex);
      }

      public Object lowerKey(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().lowerKey(var1);
      }

      public Set keySet() {
         return this.navigableKeySet();
      }

      public NavigableSet navigableKeySet() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.navigableKeySet == null ? (this.navigableKeySet = Synchronized.navigableSet(this.delegate().navigableKeySet(), this.mutex)) : this.navigableKeySet;
      }

      public Entry pollFirstEntry() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return Synchronized.access$700(this.delegate().pollFirstEntry(), this.mutex);
      }

      public Entry pollLastEntry() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return Synchronized.access$700(this.delegate().pollLastEntry(), this.mutex);
      }

      public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
         Object var5;
         synchronized(var5 = this.mutex){}
         return Synchronized.navigableMap(this.delegate().subMap(var1, var2, var3, var4), this.mutex);
      }

      public NavigableMap tailMap(Object var1, boolean var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return Synchronized.navigableMap(this.delegate().tailMap(var1, var2), this.mutex);
      }

      public SortedMap headMap(Object var1) {
         return this.headMap(var1, false);
      }

      public SortedMap subMap(Object var1, Object var2) {
         return this.subMap(var1, true, var2, false);
      }

      public SortedMap tailMap(Object var1) {
         return this.tailMap(var1, true);
      }

      SortedMap delegate() {
         return this.delegate();
      }

      Map delegate() {
         return this.delegate();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   @GwtIncompatible("NavigableSet")
   @VisibleForTesting
   static class SynchronizedNavigableSet extends Synchronized.SynchronizedSortedSet implements NavigableSet {
      transient NavigableSet descendingSet;
      private static final long serialVersionUID = 0L;

      SynchronizedNavigableSet(NavigableSet var1, @Nullable Object var2) {
         super(var1, var2);
      }

      NavigableSet delegate() {
         return (NavigableSet)super.delegate();
      }

      public Object ceiling(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().ceiling(var1);
      }

      public Iterator descendingIterator() {
         return this.delegate().descendingIterator();
      }

      public NavigableSet descendingSet() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.descendingSet == null) {
            NavigableSet var2 = Synchronized.navigableSet(this.delegate().descendingSet(), this.mutex);
            this.descendingSet = var2;
            return var2;
         } else {
            return this.descendingSet;
         }
      }

      public Object floor(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().floor(var1);
      }

      public NavigableSet headSet(Object var1, boolean var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return Synchronized.navigableSet(this.delegate().headSet(var1, var2), this.mutex);
      }

      public Object higher(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().higher(var1);
      }

      public Object lower(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().lower(var1);
      }

      public Object pollFirst() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().pollFirst();
      }

      public Object pollLast() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().pollLast();
      }

      public NavigableSet subSet(Object var1, boolean var2, Object var3, boolean var4) {
         Object var5;
         synchronized(var5 = this.mutex){}
         return Synchronized.navigableSet(this.delegate().subSet(var1, var2, var3, var4), this.mutex);
      }

      public NavigableSet tailSet(Object var1, boolean var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return Synchronized.navigableSet(this.delegate().tailSet(var1, var2), this.mutex);
      }

      public SortedSet headSet(Object var1) {
         return this.headSet(var1, false);
      }

      public SortedSet subSet(Object var1, Object var2) {
         return this.subSet(var1, true, var2, false);
      }

      public SortedSet tailSet(Object var1) {
         return this.tailSet(var1, true);
      }

      SortedSet delegate() {
         return this.delegate();
      }

      Set delegate() {
         return this.delegate();
      }

      Collection delegate() {
         return this.delegate();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   private static class SynchronizedAsMapValues extends Synchronized.SynchronizedCollection {
      private static final long serialVersionUID = 0L;

      SynchronizedAsMapValues(Collection var1, @Nullable Object var2) {
         super(var1, var2, null);
      }

      public Iterator iterator() {
         Iterator var1 = super.iterator();
         return new ForwardingIterator(this, var1) {
            final Iterator val$iterator;
            final Synchronized.SynchronizedAsMapValues this$0;

            {
               this.this$0 = var1;
               this.val$iterator = var2;
            }

            protected Iterator delegate() {
               return this.val$iterator;
            }

            public Collection next() {
               return Synchronized.access$400((Collection)super.next(), this.this$0.mutex);
            }

            public Object next() {
               return this.next();
            }

            protected Object delegate() {
               return this.delegate();
            }
         };
      }
   }

   private static class SynchronizedAsMap extends Synchronized.SynchronizedMap {
      transient Set asMapEntrySet;
      transient Collection asMapValues;
      private static final long serialVersionUID = 0L;

      SynchronizedAsMap(Map var1, @Nullable Object var2) {
         super(var1, var2);
      }

      public Collection get(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         Collection var3 = (Collection)super.get(var1);
         return var3 == null ? null : Synchronized.access$400(var3, this.mutex);
      }

      public Set entrySet() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.asMapEntrySet == null) {
            this.asMapEntrySet = new Synchronized.SynchronizedAsMapEntries(this.delegate().entrySet(), this.mutex);
         }

         return this.asMapEntrySet;
      }

      public Collection values() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.asMapValues == null) {
            this.asMapValues = new Synchronized.SynchronizedAsMapValues(this.delegate().values(), this.mutex);
         }

         return this.asMapValues;
      }

      public boolean containsValue(Object var1) {
         return this.values().contains(var1);
      }

      public Object get(Object var1) {
         return this.get(var1);
      }
   }

   @VisibleForTesting
   static class SynchronizedBiMap extends Synchronized.SynchronizedMap implements BiMap, Serializable {
      private transient Set valueSet;
      private transient BiMap inverse;
      private static final long serialVersionUID = 0L;

      private SynchronizedBiMap(BiMap var1, @Nullable Object var2, @Nullable BiMap var3) {
         super(var1, var2);
         this.inverse = var3;
      }

      BiMap delegate() {
         return (BiMap)super.delegate();
      }

      public Set values() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.valueSet == null) {
            this.valueSet = Synchronized.set(this.delegate().values(), this.mutex);
         }

         return this.valueSet;
      }

      public Object forcePut(Object var1, Object var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().forcePut(var1, var2);
      }

      public BiMap inverse() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.inverse == null) {
            this.inverse = new Synchronized.SynchronizedBiMap(this.delegate().inverse(), this.mutex, this);
         }

         return this.inverse;
      }

      public Collection values() {
         return this.values();
      }

      Map delegate() {
         return this.delegate();
      }

      Object delegate() {
         return this.delegate();
      }

      SynchronizedBiMap(BiMap var1, Object var2, BiMap var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   static class SynchronizedSortedMap extends Synchronized.SynchronizedMap implements SortedMap {
      private static final long serialVersionUID = 0L;

      SynchronizedSortedMap(SortedMap var1, @Nullable Object var2) {
         super(var1, var2);
      }

      SortedMap delegate() {
         return (SortedMap)super.delegate();
      }

      public Comparator comparator() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().comparator();
      }

      public Object firstKey() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().firstKey();
      }

      public SortedMap headMap(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Synchronized.sortedMap(this.delegate().headMap(var1), this.mutex);
      }

      public Object lastKey() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().lastKey();
      }

      public SortedMap subMap(Object var1, Object var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return Synchronized.sortedMap(this.delegate().subMap(var1, var2), this.mutex);
      }

      public SortedMap tailMap(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Synchronized.sortedMap(this.delegate().tailMap(var1), this.mutex);
      }

      Map delegate() {
         return this.delegate();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   private static class SynchronizedMap extends Synchronized.SynchronizedObject implements Map {
      transient Set keySet;
      transient Collection values;
      transient Set entrySet;
      private static final long serialVersionUID = 0L;

      SynchronizedMap(Map var1, @Nullable Object var2) {
         super(var1, var2);
      }

      Map delegate() {
         return (Map)super.delegate();
      }

      public void clear() {
         Object var1;
         synchronized(var1 = this.mutex){}
         this.delegate().clear();
      }

      public boolean containsKey(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().containsKey(var1);
      }

      public boolean containsValue(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().containsValue(var1);
      }

      public Set entrySet() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.entrySet == null) {
            this.entrySet = Synchronized.set(this.delegate().entrySet(), this.mutex);
         }

         return this.entrySet;
      }

      public Object get(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().get(var1);
      }

      public boolean isEmpty() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().isEmpty();
      }

      public Set keySet() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.keySet == null) {
            this.keySet = Synchronized.set(this.delegate().keySet(), this.mutex);
         }

         return this.keySet;
      }

      public Object put(Object var1, Object var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().put(var1, var2);
      }

      public void putAll(Map var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         this.delegate().putAll(var1);
      }

      public Object remove(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().remove(var1);
      }

      public int size() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().size();
      }

      public Collection values() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.values == null) {
            this.values = Synchronized.access$500(this.delegate().values(), this.mutex);
         }

         return this.values;
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            Object var2;
            synchronized(var2 = this.mutex){}
            return this.delegate().equals(var1);
         }
      }

      public int hashCode() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().hashCode();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   private static class SynchronizedAsMapEntries extends Synchronized.SynchronizedSet {
      private static final long serialVersionUID = 0L;

      SynchronizedAsMapEntries(Set var1, @Nullable Object var2) {
         super(var1, var2);
      }

      public Iterator iterator() {
         Iterator var1 = super.iterator();
         return new ForwardingIterator(this, var1) {
            final Iterator val$iterator;
            final Synchronized.SynchronizedAsMapEntries this$0;

            {
               this.this$0 = var1;
               this.val$iterator = var2;
            }

            protected Iterator delegate() {
               return this.val$iterator;
            }

            public Entry next() {
               Entry var1 = (Entry)super.next();
               return new ForwardingMapEntry(this, var1) {
                  final Entry val$entry;
                  final <undefinedtype> this$1;

                  {
                     this.this$1 = var1;
                     this.val$entry = var2;
                  }

                  protected Entry delegate() {
                     return this.val$entry;
                  }

                  public Collection getValue() {
                     return Synchronized.access$400((Collection)this.val$entry.getValue(), this.this$1.this$0.mutex);
                  }

                  public Object getValue() {
                     return this.getValue();
                  }

                  protected Object delegate() {
                     return this.delegate();
                  }
               };
            }

            public Object next() {
               return this.next();
            }

            protected Object delegate() {
               return this.delegate();
            }
         };
      }

      public Object[] toArray() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return ObjectArrays.toArrayImpl(this.delegate());
      }

      public Object[] toArray(Object[] var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return ObjectArrays.toArrayImpl(this.delegate(), var1);
      }

      public boolean contains(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Maps.containsEntryImpl(this.delegate(), var1);
      }

      public boolean containsAll(Collection var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Collections2.containsAllImpl(this.delegate(), var1);
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            Object var2;
            synchronized(var2 = this.mutex){}
            return Sets.equalsImpl(this.delegate(), var1);
         }
      }

      public boolean remove(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Maps.removeEntryImpl(this.delegate(), var1);
      }

      public boolean removeAll(Collection var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Iterators.removeAll(this.delegate().iterator(), var1);
      }

      public boolean retainAll(Collection var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Iterators.retainAll(this.delegate().iterator(), var1);
      }
   }

   private static class SynchronizedSortedSetMultimap extends Synchronized.SynchronizedSetMultimap implements SortedSetMultimap {
      private static final long serialVersionUID = 0L;

      SynchronizedSortedSetMultimap(SortedSetMultimap var1, @Nullable Object var2) {
         super(var1, var2);
      }

      SortedSetMultimap delegate() {
         return (SortedSetMultimap)super.delegate();
      }

      public SortedSet get(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Synchronized.access$100(this.delegate().get(var1), this.mutex);
      }

      public SortedSet removeAll(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().removeAll(var1);
      }

      public SortedSet replaceValues(Object var1, Iterable var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().replaceValues(var1, var2);
      }

      public Comparator valueComparator() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().valueComparator();
      }

      public Set replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      public Set removeAll(Object var1) {
         return this.removeAll(var1);
      }

      public Set get(Object var1) {
         return this.get(var1);
      }

      SetMultimap delegate() {
         return this.delegate();
      }

      public Collection get(Object var1) {
         return this.get(var1);
      }

      public Collection removeAll(Object var1) {
         return this.removeAll(var1);
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      Multimap delegate() {
         return this.delegate();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   private static class SynchronizedSetMultimap extends Synchronized.SynchronizedMultimap implements SetMultimap {
      transient Set entrySet;
      private static final long serialVersionUID = 0L;

      SynchronizedSetMultimap(SetMultimap var1, @Nullable Object var2) {
         super(var1, var2);
      }

      SetMultimap delegate() {
         return (SetMultimap)super.delegate();
      }

      public Set get(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Synchronized.set(this.delegate().get(var1), this.mutex);
      }

      public Set removeAll(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().removeAll(var1);
      }

      public Set replaceValues(Object var1, Iterable var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().replaceValues(var1, var2);
      }

      public Set entries() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.entrySet == null) {
            this.entrySet = Synchronized.set(this.delegate().entries(), this.mutex);
         }

         return this.entrySet;
      }

      public Collection entries() {
         return this.entries();
      }

      public Collection removeAll(Object var1) {
         return this.removeAll(var1);
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      public Collection get(Object var1) {
         return this.get(var1);
      }

      Multimap delegate() {
         return this.delegate();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   private static class SynchronizedListMultimap extends Synchronized.SynchronizedMultimap implements ListMultimap {
      private static final long serialVersionUID = 0L;

      SynchronizedListMultimap(ListMultimap var1, @Nullable Object var2) {
         super(var1, var2);
      }

      ListMultimap delegate() {
         return (ListMultimap)super.delegate();
      }

      public List get(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Synchronized.access$200(this.delegate().get(var1), this.mutex);
      }

      public List removeAll(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().removeAll(var1);
      }

      public List replaceValues(Object var1, Iterable var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().replaceValues(var1, var2);
      }

      public Collection removeAll(Object var1) {
         return this.removeAll(var1);
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         return this.replaceValues(var1, var2);
      }

      public Collection get(Object var1) {
         return this.get(var1);
      }

      Multimap delegate() {
         return this.delegate();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   private static class SynchronizedMultimap extends Synchronized.SynchronizedObject implements Multimap {
      transient Set keySet;
      transient Collection valuesCollection;
      transient Collection entries;
      transient Map asMap;
      transient Multiset keys;
      private static final long serialVersionUID = 0L;

      Multimap delegate() {
         return (Multimap)super.delegate();
      }

      SynchronizedMultimap(Multimap var1, @Nullable Object var2) {
         super(var1, var2);
      }

      public int size() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().size();
      }

      public boolean isEmpty() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().isEmpty();
      }

      public boolean containsKey(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().containsKey(var1);
      }

      public boolean containsValue(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().containsValue(var1);
      }

      public boolean containsEntry(Object var1, Object var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().containsEntry(var1, var2);
      }

      public Collection get(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Synchronized.access$400(this.delegate().get(var1), this.mutex);
      }

      public boolean put(Object var1, Object var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().put(var1, var2);
      }

      public boolean putAll(Object var1, Iterable var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().putAll(var1, var2);
      }

      public boolean putAll(Multimap var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().putAll(var1);
      }

      public Collection replaceValues(Object var1, Iterable var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().replaceValues(var1, var2);
      }

      public boolean remove(Object var1, Object var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().remove(var1, var2);
      }

      public Collection removeAll(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().removeAll(var1);
      }

      public void clear() {
         Object var1;
         synchronized(var1 = this.mutex){}
         this.delegate().clear();
      }

      public Set keySet() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.keySet == null) {
            this.keySet = Synchronized.access$300(this.delegate().keySet(), this.mutex);
         }

         return this.keySet;
      }

      public Collection values() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.valuesCollection == null) {
            this.valuesCollection = Synchronized.access$500(this.delegate().values(), this.mutex);
         }

         return this.valuesCollection;
      }

      public Collection entries() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.entries == null) {
            this.entries = Synchronized.access$400(this.delegate().entries(), this.mutex);
         }

         return this.entries;
      }

      public Map asMap() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.asMap == null) {
            this.asMap = new Synchronized.SynchronizedAsMap(this.delegate().asMap(), this.mutex);
         }

         return this.asMap;
      }

      public Multiset keys() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.keys == null) {
            this.keys = Synchronized.multiset(this.delegate().keys(), this.mutex);
         }

         return this.keys;
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            Object var2;
            synchronized(var2 = this.mutex){}
            return this.delegate().equals(var1);
         }
      }

      public int hashCode() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().hashCode();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   private static class SynchronizedMultiset extends Synchronized.SynchronizedCollection implements Multiset {
      transient Set elementSet;
      transient Set entrySet;
      private static final long serialVersionUID = 0L;

      SynchronizedMultiset(Multiset var1, @Nullable Object var2) {
         super(var1, var2, null);
      }

      Multiset delegate() {
         return (Multiset)super.delegate();
      }

      public int count(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().count(var1);
      }

      public int add(Object var1, int var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().add(var1, var2);
      }

      public int remove(Object var1, int var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().remove(var1, var2);
      }

      public int setCount(Object var1, int var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().setCount(var1, var2);
      }

      public boolean setCount(Object var1, int var2, int var3) {
         Object var4;
         synchronized(var4 = this.mutex){}
         return this.delegate().setCount(var1, var2, var3);
      }

      public Set elementSet() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.elementSet == null) {
            this.elementSet = Synchronized.access$300(this.delegate().elementSet(), this.mutex);
         }

         return this.elementSet;
      }

      public Set entrySet() {
         Object var1;
         synchronized(var1 = this.mutex){}
         if (this.entrySet == null) {
            this.entrySet = Synchronized.access$300(this.delegate().entrySet(), this.mutex);
         }

         return this.entrySet;
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            Object var2;
            synchronized(var2 = this.mutex){}
            return this.delegate().equals(var1);
         }
      }

      public int hashCode() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().hashCode();
      }

      Collection delegate() {
         return this.delegate();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   private static class SynchronizedRandomAccessList extends Synchronized.SynchronizedList implements RandomAccess {
      private static final long serialVersionUID = 0L;

      SynchronizedRandomAccessList(List var1, @Nullable Object var2) {
         super(var1, var2);
      }
   }

   private static class SynchronizedList extends Synchronized.SynchronizedCollection implements List {
      private static final long serialVersionUID = 0L;

      SynchronizedList(List var1, @Nullable Object var2) {
         super(var1, var2, null);
      }

      List delegate() {
         return (List)super.delegate();
      }

      public void add(int var1, Object var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         this.delegate().add(var1, var2);
      }

      public boolean addAll(int var1, Collection var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().addAll(var1, var2);
      }

      public Object get(int var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().get(var1);
      }

      public int indexOf(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().indexOf(var1);
      }

      public int lastIndexOf(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().lastIndexOf(var1);
      }

      public ListIterator listIterator() {
         return this.delegate().listIterator();
      }

      public ListIterator listIterator(int var1) {
         return this.delegate().listIterator(var1);
      }

      public Object remove(int var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().remove(var1);
      }

      public Object set(int var1, Object var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return this.delegate().set(var1, var2);
      }

      public List subList(int var1, int var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return Synchronized.access$200(this.delegate().subList(var1, var2), this.mutex);
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            Object var2;
            synchronized(var2 = this.mutex){}
            return this.delegate().equals(var1);
         }
      }

      public int hashCode() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().hashCode();
      }

      Collection delegate() {
         return this.delegate();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   static class SynchronizedSortedSet extends Synchronized.SynchronizedSet implements SortedSet {
      private static final long serialVersionUID = 0L;

      SynchronizedSortedSet(SortedSet var1, @Nullable Object var2) {
         super(var1, var2);
      }

      SortedSet delegate() {
         return (SortedSet)super.delegate();
      }

      public Comparator comparator() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().comparator();
      }

      public SortedSet subSet(Object var1, Object var2) {
         Object var3;
         synchronized(var3 = this.mutex){}
         return Synchronized.access$100(this.delegate().subSet(var1, var2), this.mutex);
      }

      public SortedSet headSet(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Synchronized.access$100(this.delegate().headSet(var1), this.mutex);
      }

      public SortedSet tailSet(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return Synchronized.access$100(this.delegate().tailSet(var1), this.mutex);
      }

      public Object first() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().first();
      }

      public Object last() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().last();
      }

      Set delegate() {
         return this.delegate();
      }

      Collection delegate() {
         return this.delegate();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   static class SynchronizedSet extends Synchronized.SynchronizedCollection implements Set {
      private static final long serialVersionUID = 0L;

      SynchronizedSet(Set var1, @Nullable Object var2) {
         super(var1, var2, null);
      }

      Set delegate() {
         return (Set)super.delegate();
      }

      public boolean equals(Object var1) {
         if (var1 == this) {
            return true;
         } else {
            Object var2;
            synchronized(var2 = this.mutex){}
            return this.delegate().equals(var1);
         }
      }

      public int hashCode() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().hashCode();
      }

      Collection delegate() {
         return this.delegate();
      }

      Object delegate() {
         return this.delegate();
      }
   }

   @VisibleForTesting
   static class SynchronizedCollection extends Synchronized.SynchronizedObject implements Collection {
      private static final long serialVersionUID = 0L;

      private SynchronizedCollection(Collection var1, @Nullable Object var2) {
         super(var1, var2);
      }

      Collection delegate() {
         return (Collection)super.delegate();
      }

      public boolean add(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().add(var1);
      }

      public boolean addAll(Collection var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().addAll(var1);
      }

      public void clear() {
         Object var1;
         synchronized(var1 = this.mutex){}
         this.delegate().clear();
      }

      public boolean contains(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().contains(var1);
      }

      public boolean containsAll(Collection var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().containsAll(var1);
      }

      public boolean isEmpty() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().isEmpty();
      }

      public Iterator iterator() {
         return this.delegate().iterator();
      }

      public boolean remove(Object var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().remove(var1);
      }

      public boolean removeAll(Collection var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().removeAll(var1);
      }

      public boolean retainAll(Collection var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().retainAll(var1);
      }

      public int size() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().size();
      }

      public Object[] toArray() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate().toArray();
      }

      public Object[] toArray(Object[] var1) {
         Object var2;
         synchronized(var2 = this.mutex){}
         return this.delegate().toArray(var1);
      }

      Object delegate() {
         return this.delegate();
      }

      SynchronizedCollection(Collection var1, Object var2, Object var3) {
         this(var1, var2);
      }
   }

   static class SynchronizedObject implements Serializable {
      final Object delegate;
      final Object mutex;
      @GwtIncompatible("not needed in emulated source")
      private static final long serialVersionUID = 0L;

      SynchronizedObject(Object var1, @Nullable Object var2) {
         this.delegate = Preconditions.checkNotNull(var1);
         this.mutex = var2 == null ? this : var2;
      }

      Object delegate() {
         return this.delegate;
      }

      public String toString() {
         Object var1;
         synchronized(var1 = this.mutex){}
         return this.delegate.toString();
      }

      @GwtIncompatible("java.io.ObjectOutputStream")
      private void writeObject(ObjectOutputStream var1) throws IOException {
         Object var2;
         synchronized(var2 = this.mutex){}
         var1.defaultWriteObject();
      }
   }
}
